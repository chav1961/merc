package chav1961.merc.lang.merc;

import chav1961.merc.lang.merc.MercCompiler.VarType;
import chav1961.merc.lang.merc.MercScriptEngine.LexemaSubtype;
import chav1961.purelib.basic.interfaces.SyntaxTreeInterface;
import chav1961.purelib.enumerations.ContinueMode;

class SyntaxTreeNode {
	static final String		STAIRWAY_STEP = "   ";
	
	enum SyntaxTreeNodeType {
		StandaloneName, PredefinedName, IndicedName,
		Call,
		InstanceField,
		Header, HeaderWithReturned,
		Function,
		Brick,
		Conversion,
		OrdinalBinary,
		Assign,
		Pipe,
		Negation, 
		PreInc, PreDec, PostInc, PostDec, 
		BitInv, BitAnd, BitOr, BitXOr, 
		Shl, Shr, Shra,
		Mul, Div, Rem, Add, Sub, Concat,
		LT, LE, GT, GE, EQ, NE, Is, Like,
		Not, And, Or,
		LeftPart,
		InList,
		LongIf, ShortIf, While, Until, Break, Continue, ShortReturn, LongReturn, Print, Lock, TypedFor, UntypedFor, Sequence,
		Variable, Variables, Vartype, AllocatedVariable,
		Null, IntConst, RealConst, StrConst, BoolConst, RefConst,  
		List, Range, 
		Unknown,
		 
	}

	@FunctionalInterface
	interface WalkCallback {
		ContinueMode process(SyntaxTreeNode node);
	}
	
	SyntaxTreeNode.SyntaxTreeNodeType	type = SyntaxTreeNodeType.Unknown;
	long								value = -1;
	Object								cargo = null;
	SyntaxTreeNode[]					children = null;

	SyntaxTreeNode() {
		
	}

	SyntaxTreeNode(final SyntaxTreeNode from) {
		type = from.type;
		value = from.value;
		cargo = from.cargo;
		children = from.children != null ? from.children.clone() : null;
	}
	
	SyntaxTreeNode.SyntaxTreeNodeType getType() {
		return type;
	}

	void rewrite(SyntaxTreeNode from) {
		
	}
	
	ContinueMode walk(final WalkCallback callback) {
		ContinueMode	cont;
		
		switch (cont = callback.process(this)) {
			case CONTINUE		:
				if (cargo instanceof SyntaxTreeNode) {
					switch (cont = ((SyntaxTreeNode)cargo).walk(callback)) {
						case CONTINUE		:
							break;
						case PARENT_ONLY	:
							return ContinueMode.CONTINUE;
						case SIBLINGS_ONLY	:
							break;
						case SKIP_CHILDREN	:
							return ContinueMode.CONTINUE;
						case SKIP_PARENT	:
							return ContinueMode.CONTINUE;
						case SKIP_SIBLINGS	:
							return ContinueMode.CONTINUE;
						case STOP			:
							return ContinueMode.STOP;
						default : throw new UnsupportedOperationException("Continue mode [cont] is not supported yet");
					}
				}
				if (children != null) {
loop:				for (SyntaxTreeNode item : children) {
						switch (item.walk(callback)) {
							case CONTINUE		:
								break;
							case PARENT_ONLY	:
								return ContinueMode.CONTINUE;
							case SIBLINGS_ONLY	:
								break;
							case SKIP_CHILDREN	:
								break loop;
							case SKIP_PARENT	:
								return ContinueMode.CONTINUE;
							case SKIP_SIBLINGS	:
								break loop;
							case STOP:
								return ContinueMode.STOP;
							default : throw new UnsupportedOperationException("Continue mode [cont] is not supported yet");
						}
					}
				}
				return ContinueMode.CONTINUE;
			case PARENT_ONLY	:
				return ContinueMode.CONTINUE;
			case SIBLINGS_ONLY	:
				return ContinueMode.CONTINUE;
			case SKIP_CHILDREN	:
				return ContinueMode.CONTINUE;
			case SKIP_PARENT	:
				return ContinueMode.CONTINUE;
			case SKIP_SIBLINGS	:
				return ContinueMode.CONTINUE;
			case STOP:
				return ContinueMode.STOP;
			default : throw new UnsupportedOperationException("Continue mode [cont] is not supported yet");
		}
	}
	
	
	public void assignVarDefs(final SyntaxTreeNode[] array) {
		type = SyntaxTreeNodeType.Variables;
		value = -1;
		cargo = null;
		children = array.clone();
	}

	public void assignHeader(final long name, final SyntaxTreeNode... parms) {
		type = SyntaxTreeNodeType.Header;
		value = name;
		cargo = null;
		children = parms.clone();
	}

	public void assignHeaderWithReturned(final long name, final SyntaxTreeNode returned, final SyntaxTreeNode... parms) {
		type = SyntaxTreeNodeType.HeaderWithReturned;
		value = name;
		cargo = returned;
		children = parms.clone();
	}
	
	public void assignBrick(final SyntaxTreeNode head, final SyntaxTreeNode body) {
		type = SyntaxTreeNodeType.Brick;
		value = -1;
		cargo = head;
		children = body.getType() == SyntaxTreeNodeType.Sequence ? body.children.clone() : new SyntaxTreeNode[]{body};
	}

	public void assignFunc(SyntaxTreeNode head, SyntaxTreeNode body) {
		type = SyntaxTreeNodeType.Function;
		value = -1;
		cargo = head;
		children = body.getType() == SyntaxTreeNodeType.Sequence ? body.children.clone() : new SyntaxTreeNode[]{body};
	}

	public void assignField(final SyntaxTreeNode owner, final SyntaxTreeNode field) {
		type = SyntaxTreeNodeType.InstanceField;
		value = -1;
		cargo = null;
		children = new SyntaxTreeNode[]{owner,field};
	}

	public void assignName(final long intval) {
		type = SyntaxTreeNodeType.StandaloneName;
		value = intval;
		cargo = null;
		children = null;
	}

	public void assignPredefinedName(final LexemaSubtype subtype) {
		type = SyntaxTreeNodeType.PredefinedName;
		value = -1;
		cargo = subtype;
		children = null;
	}

	public void assignNameIndiced(final long name, final SyntaxTreeNode indices) {
		type = SyntaxTreeNodeType.IndicedName;
		value = name;
		cargo = null;
		if (indices.getType() != SyntaxTreeNodeType.List) {
			throw new IllegalArgumentException("Indices must be list!");
		}
		else {
			children = indices.children.clone();
		}
	}

	public void assignVarDefinition(final long nameId, final VarType varType) {
		type = SyntaxTreeNodeType.Variable;
		value = nameId;
		cargo = varType;
		children = null;
	}
	
	public void assignCall(final SyntaxTreeNode item, final SyntaxTreeNode parm) {
		type = SyntaxTreeNodeType.Call;
		value = -1;
		cargo = item;
		if (parm.getType() != SyntaxTreeNodeType.List) {
			throw new IllegalArgumentException("Parameters must be list!");
		}
		else {
			children = parm.children.clone();
		}
	}

	public void assignCall(final SyntaxTreeNode item) {
		type = SyntaxTreeNodeType.Call;
		value = -1;
		cargo = item;
		children = new SyntaxTreeNode[0];
	}
	
	public void assignConversion(final LexemaSubtype conv, final SyntaxTreeNode inner) {
		type = SyntaxTreeNodeType.Conversion;
		value = -1;
		cargo = conv;
		if (inner.getType() != SyntaxTreeNodeType.List) {
			throw new IllegalArgumentException("Parameters must be list!");
		}
		else {
			children = inner.children.clone();
		}
	}

	public void assignStr(final char[] strval) {
		type = SyntaxTreeNodeType.StrConst;
		value = -1;
		cargo = strval;
		children = null;
	}

	public void assignRefConst(final Object refval) {
		type = SyntaxTreeNodeType.RealConst;
		value = -1;
		cargo = refval;
		children = null;
	}

	public void assignReal(final double realval) {
		type = SyntaxTreeNodeType.RealConst;
		value = Double.doubleToLongBits(realval);
		cargo = null;
		children = null;
	}

	public void assignNull() {
		type = SyntaxTreeNodeType.Null;
		value = -1;
		cargo = null;
		children = null;
	}

	public void assignInt(final long intval) {
		type = SyntaxTreeNodeType.IntConst;
		value = intval;
		cargo = null;
		children = null;
	}

	public void assignBoolean(final boolean boolval) {
		type = SyntaxTreeNodeType.BoolConst;
		value = boolval ? 1 : 0;
		cargo = null;
		children = null;
	}

	void assignIf(final SyntaxTreeNode cond, final SyntaxTreeNode thenBody) {
		type = SyntaxTreeNodeType.ShortIf;
		value = -1;
		cargo = cond;
		children = new SyntaxTreeNode[]{thenBody};
	}

	void assignIf(final SyntaxTreeNode cond, final SyntaxTreeNode thenBody, final SyntaxTreeNode elseBody) {
		type = SyntaxTreeNodeType.LongIf;
		value = -1;
		cargo = cond;
		children = new SyntaxTreeNode[]{thenBody,elseBody};
	}
	
	void assignWhile(final SyntaxTreeNode cond, final SyntaxTreeNode body) {
		type = SyntaxTreeNodeType.While;
		value = -1;
		cargo = cond;
		children = new SyntaxTreeNode[]{body};
	}
	
	void assignUntil(final SyntaxTreeNode cond, final SyntaxTreeNode body) {
		type = SyntaxTreeNodeType.Until;
		value = -1;
		cargo = cond;
		children = new SyntaxTreeNode[]{body};
	}

	void assignBreak(final int depth) {
		type = SyntaxTreeNodeType.Break;
		value = depth;
		cargo = null;
		children = null;
	}

	void assignContinue(final int depth) {
		type = SyntaxTreeNodeType.Continue;
		value = depth;
		cargo = null;
		children = null;
	}

	void assignReturn() {
		type = SyntaxTreeNodeType.ShortReturn;
		value = -1;
		cargo = null;
		children = null;
	}

	void assignReturn(final SyntaxTreeNode returnExpression) {
		type = SyntaxTreeNodeType.LongReturn;
		value = -1;
		cargo = null;
		children = new SyntaxTreeNode[]{returnExpression};
	}

	void assignPrint(final SyntaxTreeNode expressions) {
		type = SyntaxTreeNodeType.Print;
		value = -1;
		cargo = null;
		if (expressions.getType() == SyntaxTreeNodeType.List) {
			children = expressions.children.clone();
		}
		else {
			throw new IllegalArgumentException("Expressions is not a list!"); 
		}
	}

	void assignLock(final SyntaxTreeNode lockExpression, final SyntaxTreeNode lockBody) {
		type = SyntaxTreeNodeType.Lock;
		value = -1;
		cargo = lockExpression;
		children = new SyntaxTreeNode[]{lockBody};
	}

	void assignType(final LexemaSubtype varType) {
		type = SyntaxTreeNodeType.Vartype;
		value = -1;
		cargo = varType;
		children = new SyntaxTreeNode[0];
	}

	void assignFor(final SyntaxTreeNode forName, final SyntaxTreeNode forType, final SyntaxTreeNode forList, final SyntaxTreeNode forBody) {
		type = SyntaxTreeNodeType.TypedFor;
		value = -1;
		cargo = null;
		children = new SyntaxTreeNode[]{forName,forType,forList,forBody};
	}

	void assignFor(final SyntaxTreeNode forName, final SyntaxTreeNode forList, final SyntaxTreeNode forBody) {
		type = SyntaxTreeNodeType.UntypedFor;
		value = -1;
		cargo = null;
		children = new SyntaxTreeNode[]{forName,forList,forBody};
	}
	
	public void assignSequence(SyntaxTreeNode... array) {
		type = SyntaxTreeNodeType.Sequence;
		value = -1;
		cargo = null;
		children = array.clone();
	}

	void assignRange(final SyntaxTreeNode itemFrom, final SyntaxTreeNode itemTo) {
		type = SyntaxTreeNodeType.Range;
		value = -1;
		cargo = null;
		children = new SyntaxTreeNode[]{itemFrom,itemTo};
	}

	void assignList(SyntaxTreeNode... array) {
		type = SyntaxTreeNodeType.List;
		value = -1;
		cargo = null;
		children = array.clone();
	}

	void assignUnary(final SyntaxTreeNodeType subtype, final SyntaxTreeNode node) {
		type = subtype;
		value = -1;
		cargo = null;
		children = new SyntaxTreeNode[]{node};
	}

	void assignBinary(final SyntaxTreeNodeType[] operations, SyntaxTreeNode[] operands) {
		type = SyntaxTreeNodeType.OrdinalBinary;
		value = -1;
		if (operations.length != operands.length) {
			throw new IllegalArgumentException("Operations list length differ from operands one"); 
		}
		else {
			cargo = operations;
			children = operands.clone();
		}
	}

	void assignAssignment(final SyntaxTreeNode left, final SyntaxTreeNode right) {
		type = SyntaxTreeNodeType.Assign;
		value = -1;
		cargo = null;
		children = new SyntaxTreeNode[]{left,right};
	}

	void assignPipe(SyntaxTreeNode startPipe, SyntaxTreeNode[] intermediate, SyntaxTreeNode endPipe) {
		type = SyntaxTreeNodeType.Pipe;
		value = -1;
		cargo = new SyntaxTreeNode[]{startPipe,endPipe};
		children = intermediate.clone();
	}

	public String toString(final SyntaxTreeInterface<?> names) {
		if (names == null) {
			throw new NullPointerException("Names can't be null");
		}
		else {
			return toString("",names);
		}
	}
	
	private String toString(final String prefix, final SyntaxTreeInterface<?> names) {
		final StringBuilder	sb = new StringBuilder();
		String	before;
		
		switch(getType()) {
			case Assign		:
				sb.append(prefix).append("assign (\n");
				sb.append(prefix).append(children[0].toString(prefix+STAIRWAY_STEP,names))
				  .append(prefix).append("<---\n")
				  .append(children[1].toString(prefix+STAIRWAY_STEP,names));
				sb.append(prefix).append(")\n");
				break;
			case BitInv		:
				sb.append(prefix).append("~ (\n");
				sb.append(children[0].toString(prefix+STAIRWAY_STEP,names));
				sb.append(prefix).append(")\n");
				break;
			case BoolConst	:
				sb.append(prefix).append(value == 1 ? "true" : "false").append('\n');
				break;
			case Break		:
				sb.append(prefix).append("break").append(value == 0 ? "" : " "+value).append('\n');
				break;
			case Brick		:
				sb.append(prefix).append("brick:\n");
				sb.append(((SyntaxTreeNode)cargo).toString(prefix+STAIRWAY_STEP,names));
				if (children.length > 0) {
					before = "body\n";
					sb.append(prefix).append(before);
					for (SyntaxTreeNode item : children) {
						sb.append(item.toString(prefix+STAIRWAY_STEP,names));
					}
				}
				sb.append(prefix).append("end brick\n");
				break;
			case Call		:
				sb.append(prefix).append("Call\n");
				sb.append(((SyntaxTreeNode)cargo).toString(prefix+STAIRWAY_STEP,names));
				if (children.length > 0) {
					sb.append(prefix).append("with :\n");
					before = "";
					for (SyntaxTreeNode item : children) {
						sb.append(before).append(item.toString(prefix+STAIRWAY_STEP,names));
						before = prefix+STAIRWAY_STEP+",\n";
					}
				}
				sb.append(prefix).append("end call\n");
				break;
			case Continue	:
				sb.append(prefix).append("continue").append(value == 0 ? "" : " "+value).append('\n');
				break;
			case Conversion	:
				sb.append(prefix).append("convert to ").append(cargo).append("\n");
				before = "";
				for (SyntaxTreeNode item : children) {
					sb.append(before).append(item.toString(prefix+STAIRWAY_STEP,names));
					before = prefix+STAIRWAY_STEP+",\n";
				}
				sb.append(prefix).append(")\n");
				break;
			case Function	:
				sb.append(prefix).append("func:\n");
				sb.append(((SyntaxTreeNode)cargo).toString(prefix+STAIRWAY_STEP,names));
				if (children.length > 0) {
					before = "body\n";
					sb.append(prefix).append(before);
					for (SyntaxTreeNode item : children) {
						sb.append(item.toString(prefix+STAIRWAY_STEP,names));
					}
				}
				sb.append(prefix).append("end func\n");
				break;
			case Header		:
				sb.append(prefix).append("header ").append(names.getName(value)).append(":\n");
				before = "";
				for (SyntaxTreeNode item : children) {
					sb.append(before).append(item.toString(prefix+STAIRWAY_STEP,names));
					before = prefix+",\n";
				}
				sb.append(prefix).append("end header\n");
				break;
			case HeaderWithReturned	:
				sb.append(prefix).append("header ").append(names.getName(value)).append(":\n");
				before = "";
				for (SyntaxTreeNode item : children) {
					sb.append(before).append(item.toString(prefix+STAIRWAY_STEP,names));
					before = prefix+",\n";
				}
				sb.append(prefix).append("returned\n");
				sb.append(((SyntaxTreeNode)cargo).toString(prefix+STAIRWAY_STEP,names));
				sb.append(prefix).append("end header\n");
				break;
			case IndicedName:
				sb.append(prefix).append(names.getName(value)).append("[\n");
				before = "";
				for (SyntaxTreeNode item : children) {
					sb.append(before).append(item.toString(prefix+STAIRWAY_STEP,names));
					before = prefix+STAIRWAY_STEP+",\n";
				}
				sb.append(prefix).append("]\n");
				break;
			case InstanceField:
				sb.append(prefix).append("field\n");
				sb.append(prefix).append(children[0].toString(prefix+STAIRWAY_STEP,names));
				sb.append(prefix).append("--->\n");
				sb.append(prefix).append(children[1].toString(prefix+STAIRWAY_STEP,names));
				sb.append(prefix).append("end field\n");
				break;
			case IntConst	:
				sb.append(prefix).append(value).append('\n');
				break;
			case List		:
				sb.append(prefix).append("(\n");
				before = "";
				for (SyntaxTreeNode item : children) {
					sb.append(before);
					sb.append(item.toString(prefix+STAIRWAY_STEP,names));
					before = prefix+",\n"; 
				}
				sb.append(prefix).append(")\n");
				break;
			case Lock		:
				sb.append(prefix).append("lock\n");
				sb.append(((SyntaxTreeNode)cargo).toString(prefix+STAIRWAY_STEP,names));
				sb.append(prefix).append("--->\n");
				sb.append(children[0].toString(prefix+STAIRWAY_STEP,names));
				sb.append(prefix).append("end lock\n");
				break;
			case LongIf		: 
				sb.append(prefix).append("if\n");
				sb.append(((SyntaxTreeNode)cargo).toString(prefix+STAIRWAY_STEP,names));
				sb.append(prefix).append("then\n");
				sb.append(children[0].toString(prefix+STAIRWAY_STEP,names));
				sb.append(prefix).append("else\n");
				sb.append(children[1].toString(prefix+STAIRWAY_STEP,names));
				sb.append(prefix).append("end if\n");
				break;
			case LongReturn	:
				sb.append(prefix).append("return\n");
				sb.append(children[0].toString(prefix+STAIRWAY_STEP,names));
				sb.append(prefix).append("end return\n");
				break;
			case Negation	:
				sb.append(prefix).append("- (\n");
				sb.append(children[0].toString(prefix+STAIRWAY_STEP,names));
				sb.append(prefix).append(")\n");
				break;
			case Not		:
				sb.append(prefix).append("not (\n");
				sb.append(children[0].toString(prefix+STAIRWAY_STEP,names));
				sb.append(prefix).append(")\n");
				break;
			case Null	:
				sb.append(prefix).append("null\n");
				break;
			case OrdinalBinary:
				sb.append(prefix).append("binary list (\n");
				for (int index = 0; index < children.length; index++) {
					if (index > 0) {
						sb.append(prefix).append(((SyntaxTreeNodeType[])cargo)[index]).append('\n');
					}
					sb.append(children[index].toString(prefix+STAIRWAY_STEP,names));
				}
				sb.append(prefix).append(")\n");
				break;
			case Pipe	:
				sb.append(prefix).append("pipe:\n");
				sb.append(prefix+STAIRWAY_STEP).append("from\n");
				sb.append(((SyntaxTreeNode[])cargo)[0].toString(prefix+STAIRWAY_STEP+STAIRWAY_STEP,names));
				if (children.length > 0) {
					before = "thru\n";
					sb.append(prefix+STAIRWAY_STEP).append(before);
					for (SyntaxTreeNode item : children) {
						sb.append(item.toString(prefix+STAIRWAY_STEP+STAIRWAY_STEP,names));
					}
					before = "----\n";
				}
				sb.append(prefix+STAIRWAY_STEP).append("to\n");
				sb.append(((SyntaxTreeNode[])cargo)[1].toString(prefix+STAIRWAY_STEP+STAIRWAY_STEP,names));
				sb.append(prefix).append("end pipe\n");
				break;
			case PostDec	:
				sb.append(prefix).append("(\n");
				sb.append(children[0].toString(prefix+STAIRWAY_STEP,names));
				sb.append(prefix).append(") --\n");
				break;
			case PostInc	:
				sb.append(prefix).append("(\n");
				sb.append(children[0].toString(prefix+STAIRWAY_STEP,names));
				sb.append(prefix).append(") ++\n");
				break;
			case PredefinedName:
				sb.append(prefix).append(cargo).append('\n');
				break;
			case PreDec	:
				sb.append(prefix).append("-- (\n");
				sb.append(children[0].toString(prefix+STAIRWAY_STEP,names));
				sb.append(prefix).append(")\n");
				break;
			case PreInc	:
				sb.append(prefix).append("++ (\n");
				sb.append(children[0].toString(prefix+STAIRWAY_STEP,names));
				sb.append(prefix).append(")\n");
				break;
			case Print	:
				sb.append(prefix).append("print\n");
				for (SyntaxTreeNode item : children) {
					sb.append(item.toString(prefix+STAIRWAY_STEP,names));
				}
				sb.append(prefix).append("end print\n");
				break;
			case Range	:
				sb.append(prefix).append("(\n");
				sb.append(children[0].toString(prefix+STAIRWAY_STEP,names));
				sb.append(prefix).append("..\n");
				sb.append(children[1].toString(prefix+STAIRWAY_STEP,names));
				sb.append(prefix).append(")\n");
				break;
			case RealConst	:
				sb.append(prefix).append(Double.longBitsToDouble(value)).append('\n');
				break;
			case RefConst	:
				break;
			case Sequence	:
				sb.append(prefix).append("{\n");
				for (SyntaxTreeNode item : children) {
					sb.append(item.toString(prefix+STAIRWAY_STEP,names));
				}
				sb.append(prefix).append("}\n");
				break;
			case ShortIf	:
				sb.append(prefix).append("if\n");
				sb.append(((SyntaxTreeNode)cargo).toString(prefix+STAIRWAY_STEP,names));
				sb.append(prefix).append("then\n");
				sb.append(children[0].toString(prefix+STAIRWAY_STEP,names));
				sb.append(prefix).append("end if\n");
				break;
			case ShortReturn:
				sb.append(prefix).append("return\n");
				break;
			case StandaloneName:
				sb.append(prefix).append(names.getName(value)).append('\n');
				break;
			case StrConst	:
				sb.append(prefix).append('\"').append(new String((char[])cargo)).append("\"\n");
				break;
			case TypedFor	:
				sb.append(prefix).append("for\n");
				sb.append(children[0].toString(prefix+STAIRWAY_STEP,names));
				sb.append(prefix).append("typed\n");
				sb.append(children[1].toString(prefix+STAIRWAY_STEP,names));
				sb.append(prefix).append("in\n");
				sb.append(children[2].toString(prefix+STAIRWAY_STEP,names));
				sb.append(prefix).append("do\n");
				sb.append(children[3].toString(prefix+STAIRWAY_STEP,names));
				sb.append(prefix).append("end for\n");
				break;
			case Until		:
				sb.append(prefix).append("do\n");
				sb.append(children[0].toString(prefix+STAIRWAY_STEP,names));
				sb.append(prefix).append("until\n");
				sb.append(((SyntaxTreeNode)cargo).toString(prefix+STAIRWAY_STEP,names));
				sb.append(prefix).append("end until\n");
				break;
			case UntypedFor	:
				sb.append(prefix).append("for\n");
				sb.append(children[0].toString(prefix+STAIRWAY_STEP,names));
				sb.append(prefix).append("in\n");
				sb.append(children[1].toString(prefix+STAIRWAY_STEP,names));
				sb.append(prefix).append("do\n");
				sb.append(children[2].toString(prefix+STAIRWAY_STEP,names));
				sb.append(prefix).append("end for\n");
				break;
			case Variables	:
				for (SyntaxTreeNode item : children) {
					sb.append(item.toString(prefix,names));
				}
				break;
			case Variable	:
				final VarType	varType = (VarType)cargo;
				
				sb.append(prefix).append(varType.isVar ? "variable " : "").append("name ").append(names.getName(value)).append(" : ").append(varType.dataType);
				if (varType.initial != null) {
					sb.append("(\n").append(varType.initial.toString(prefix+STAIRWAY_STEP,names)).append(prefix).append(")\n");
				}
				else {
					sb.append("\n");
				}
				break;
			case Vartype	:
				sb.append(prefix).append(cargo).append('\n');
				break;
			case While:
				sb.append(prefix).append("while\n");
				sb.append(((SyntaxTreeNode)cargo).toString(prefix+STAIRWAY_STEP,names));
				sb.append(prefix).append("do\n");
				sb.append(children[0].toString(prefix+STAIRWAY_STEP,names));
				sb.append(prefix).append("end while\n");
				break;
			default:
				throw new UnsupportedOperationException("Type ["+getType()+"] is not supported yet");
		}
		return sb.toString();
	}
}