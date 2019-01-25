package chav1961.merc.lang.merc;

import chav1961.merc.lang.merc.interfaces.VarDescriptor;
import chav1961.purelib.basic.interfaces.SyntaxTreeInterface;
import chav1961.purelib.enumerations.ContinueMode;
import chav1961.purelib.enumerations.NodeEnterMode;

class SyntaxTreeNode {
	static final String		STAIRWAY_STEP = "   ";
	
	enum SyntaxTreeNodeType {
		StandaloneName, PredefinedName, IndicedName,
		Call,
		InstanceField,
		Header, HeaderWithReturned,
		Program, Function, Brick,
		Conversion,
		OrdinalBinary,
		Assign,
		Pipe,
		Negation, 
		PreInc, PreDec, PostInc, PostDec, 
		BitInv, 
		Not,
		LongIf, ShortIf, While, Until, Break, Continue, ShortReturn, LongReturn, Print, Lock, TypedFor, UntypedFor, Sequence, Infinite,
		Variable, Variables, Vartype, AllocatedVariable,
		Null, IntConst, RealConst, StrConst, BoolConst, RefConst,  
		List, Range,
		Empty,
		Unknown,
		 
	}

	@FunctionalInterface
	interface WalkCallback {
		ContinueMode process(NodeEnterMode mode, SyntaxTreeNode node);
	}
	
	int					row, col;
	SyntaxTreeNodeType	type = SyntaxTreeNodeType.Unknown;
	long				value = -1;
	Object				cargo = null;
	SyntaxTreeNode[]	children = null;

	SyntaxTreeNode() {
		
	}

	SyntaxTreeNode(final SyntaxTreeNode from) {
		this.row = from.row;
		this.col = from.col;
		this.type = from.type;
		this.value = from.value;
		this.cargo = from.cargo;
		this.children = from.children != null ? from.children.clone() : null;
	}

	SyntaxTreeNode(final SyntaxTreeNodeType type, final long value, final Object cargo, final SyntaxTreeNode... children) {
		this(0, 0, type, value, cargo, children);
	}
	
	SyntaxTreeNode(final int row, final int col, final SyntaxTreeNodeType type, final long value, final Object cargo, final SyntaxTreeNode... children) {
		this.row = row;
		this.col = col;
		this.type = type;
		this.value = value;
		this.cargo = cargo;
		this.children = children;
	}
	
	SyntaxTreeNode.SyntaxTreeNodeType getType() {
		return type;
	}

	void rewrite(SyntaxTreeNode from) {
		
	}

	ContinueMode walk(final WalkCallback callback) {
		ContinueMode	cont;
		
		switch (cont = callback.process(NodeEnterMode.ENTER,this)) {
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
	
	public void assign(final SyntaxTreeNode another) {
		this.row = another.row;
		this.col = another.col;
		this.type = another.type;
		this.value = another.value;
		this.cargo = another.cargo;
		this.children = another.children == null ? null : another.children.clone();
	}
	
	public void assignVarDefs(final int row, final int col, final SyntaxTreeNode[] array) {
		this.row = row;
		this.col = col;
		this.type = SyntaxTreeNodeType.Variables;
		this.value = -1;
		this.cargo = null;
		this.children = array.clone();
	}

	public void assignHeader(final int row, final int col, final long name, final SyntaxTreeNode... parms) {
		this.row = row;
		this.col = col;
		type = SyntaxTreeNodeType.Header;
		value = name;
		cargo = null;
		children = parms.clone();
	}

	public void assignHeaderWithReturned(final int row, final int col, final long name, final Class<?> returned, final SyntaxTreeNode... parms) {
		this.row = row;
		this.col = col;
		type = SyntaxTreeNodeType.HeaderWithReturned;
		value = name;
		cargo = returned;
		children = parms.clone();
	}
	
	public void assignBrick(final int row, final int col, final SyntaxTreeNode head, final SyntaxTreeNode body) {
		this.row = row;
		this.col = col;
		type = SyntaxTreeNodeType.Brick;
		value = -1;
		cargo = head;
		children = body.getType() == SyntaxTreeNodeType.Sequence ? body.children.clone() : new SyntaxTreeNode[]{body};
	}

	public void assignFunc(final int row, final int col, final SyntaxTreeNode head, final SyntaxTreeNode body) {
		this.row = row;
		this.col = col;
		type = SyntaxTreeNodeType.Function;
		value = -1;
		cargo = head;
		children = body.getType() == SyntaxTreeNodeType.Sequence ? body.children.clone() : new SyntaxTreeNode[]{body};
	}

	public void assignProgram(final int row, final int col, final SyntaxTreeNode main, final SyntaxTreeNode[] funcs) {
		this.row = row;
		this.col = col;
		type = SyntaxTreeNodeType.Program;
		value = -1;
		cargo = main;
		children = funcs;
	}
	
	public void assignField(final int row, final int col, final VarDescriptor desc, final SyntaxTreeNode owner, final SyntaxTreeNode field) {
		this.row = row;
		this.col = col;
		type = SyntaxTreeNodeType.InstanceField;
		value = -1;
		cargo = desc;
		children = new SyntaxTreeNode[]{owner,field};
	}

	public void assignName(final int row, final int col, final long intval) {
		this.row = row;
		this.col = col;
		type = SyntaxTreeNodeType.StandaloneName;
		value = intval;
		cargo = null;
		children = null;
	}

	public void assignPredefinedName(final int row, final int col, final LexemaSubtype subtype) {
		this.row = row;
		this.col = col;
		type = SyntaxTreeNodeType.PredefinedName;
		value = -1;
		cargo = subtype;
		children = null;
	}

	public void assignNameIndiced(final int row, final int col, final long name, final SyntaxTreeNode indices) {
		this.row = row;
		this.col = col;
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

	public void assignVarDefinition(final int row, final int col, final long nameId, final VarDescriptor desc) {
		this.row = row;
		this.col = col;
		type = SyntaxTreeNodeType.Variable;
		value = nameId;
		cargo = desc;
		children = new SyntaxTreeNode[0];
	}
	
	public void assignVarDefinition(final int row, final int col, final long nameId, final VarDescriptor desc, final SyntaxTreeNode initial) {
		this.row = row;
		this.col = col;
		type = SyntaxTreeNodeType.Variable;
		value = nameId;
		cargo = desc;
		children = new SyntaxTreeNode[]{initial};
	}
	
	public void assignCall(final int row, final int col, final SyntaxTreeNode item, final SyntaxTreeNode parm) {
		this.row = row;
		this.col = col;
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

	public void assignCall(final int row, final int col, final SyntaxTreeNode item) {
		this.row = row;
		this.col = col;
		type = SyntaxTreeNodeType.Call;
		value = -1;
		cargo = item;
		children = new SyntaxTreeNode[0];
	}
	
	public void assignConversion(final int row, final int col, final LexemaSubtype conv, final SyntaxTreeNode inner) {
		this.row = row;
		this.col = col;
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

	public void assignStr(final int row, final int col, final char[] strval) {
		this.row = row;
		this.col = col;
		type = SyntaxTreeNodeType.StrConst;
		value = -1;
		cargo = strval;
		children = null;
	}

	public void assignRefConst(final int row, final int col, final Object refval) {
		this.row = row;
		this.col = col;
		type = SyntaxTreeNodeType.RealConst;
		value = -1;
		cargo = refval;
		children = null;
	}

	public void assignReal(final int row, final int col, final double realval) {
		this.row = row;
		this.col = col;
		type = SyntaxTreeNodeType.RealConst;
		value = Double.doubleToLongBits(realval);
		cargo = null;
		children = null;
	}

	public void assignNull(final int row, final int col) {
		this.row = row;
		this.col = col;
		type = SyntaxTreeNodeType.Null;
		value = -1;
		cargo = null;
		children = null;
	}

	public void assignInt(final int row, final int col, final long intval) {
		this.row = row;
		this.col = col;
		type = SyntaxTreeNodeType.IntConst;
		value = intval;
		cargo = null;
		children = null;
	}

	public void assignBoolean(final int row, final int col, final boolean boolval) {
		this.row = row;
		this.col = col;
		type = SyntaxTreeNodeType.BoolConst;
		value = boolval ? 1 : 0;
		cargo = null;
		children = null;
	}

	void assignIf(final int row, final int col, final SyntaxTreeNode cond, final SyntaxTreeNode thenBody) {
		this.row = row;
		this.col = col;
		type = SyntaxTreeNodeType.ShortIf;
		value = -1;
		cargo = cond;
		children = new SyntaxTreeNode[]{thenBody};
	}

	void assignIf(final int row, final int col, final SyntaxTreeNode cond, final SyntaxTreeNode thenBody, final SyntaxTreeNode elseBody) {
		this.row = row;
		this.col = col;
		type = SyntaxTreeNodeType.LongIf;
		value = -1;
		cargo = cond;
		children = new SyntaxTreeNode[]{thenBody,elseBody};
	}
	
	void assignWhile(final int row, final int col, final SyntaxTreeNode cond, final SyntaxTreeNode body) {
		this.row = row;
		this.col = col;
		type = SyntaxTreeNodeType.While;
		value = -1;
		cargo = cond;
		children = new SyntaxTreeNode[]{body};
	}
	
	void assignUntil(final int row, final int col, final SyntaxTreeNode cond, final SyntaxTreeNode body) {
		this.row = row;
		this.col = col;
		type = SyntaxTreeNodeType.Until;
		value = -1;
		cargo = cond;
		children = new SyntaxTreeNode[]{body};
	}

	void assignBreak(final int row, final int col, final int depth) {
		this.row = row;
		this.col = col;
		type = SyntaxTreeNodeType.Break;
		value = depth;
		cargo = null;
		children = null;
	}

	void assignContinue(final int row, final int col, final int depth) {
		this.row = row;
		this.col = col;
		type = SyntaxTreeNodeType.Continue;
		value = depth;
		cargo = null;
		children = null;
	}

	void assignReturn(final int row, final int col) {
		this.row = row;
		this.col = col;
		type = SyntaxTreeNodeType.ShortReturn;
		value = -1;
		cargo = null;
		children = null;
	}

	void assignReturn(final int row, final int col, final SyntaxTreeNode returnExpression) {
		this.row = row;
		this.col = col;
		type = SyntaxTreeNodeType.LongReturn;
		value = -1;
		cargo = null;
		children = new SyntaxTreeNode[]{returnExpression};
	}

	void assignPrint(final int row, final int col, final SyntaxTreeNode expressions) {
		this.row = row;
		this.col = col;
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

	void assignLock(final int row, final int col, final SyntaxTreeNode lockExpression, final SyntaxTreeNode lockBody) {
		this.row = row;
		this.col = col;
		type = SyntaxTreeNodeType.Lock;
		value = -1;
		cargo = lockExpression;
		children = new SyntaxTreeNode[]{lockBody};
	}

	void assignType(final int row, final int col, final LexemaSubtype varType) {
		this.row = row;
		this.col = col;
		type = SyntaxTreeNodeType.Vartype;
		value = -1;
		cargo = varType;
		children = new SyntaxTreeNode[0];
	}

	void assignFor(final int row, final int col, final SyntaxTreeNode forName, final SyntaxTreeNode forType, final SyntaxTreeNode forList, final SyntaxTreeNode forBody) {
		this.row = row;
		this.col = col;
		type = SyntaxTreeNodeType.TypedFor;
		value = -1;
		cargo = null;
		children = new SyntaxTreeNode[]{forName,forType,forList,forBody};
	}

	void assignFor(final int row, final int col, final SyntaxTreeNode forName, final SyntaxTreeNode forList, final SyntaxTreeNode forBody) {
		this.row = row;
		this.col = col;
		type = SyntaxTreeNodeType.UntypedFor;
		value = -1;
		cargo = null;
		children = new SyntaxTreeNode[]{forName,forList,forBody};
	}
	
	public void assignSequence(final int row, final int col, final SyntaxTreeNode... array) {
		this.row = row;
		this.col = col;
		type = SyntaxTreeNodeType.Sequence;
		value = -1;
		cargo = null;
		children = array.clone();
	}

	void assignRange(final int row, final int col, final SyntaxTreeNode itemFrom, final SyntaxTreeNode itemTo) {
		this.row = row;
		this.col = col;
		type = SyntaxTreeNodeType.Range;
		value = -1;
		cargo = null;
		children = new SyntaxTreeNode[]{itemFrom,itemTo};
	}

	void assignList(final int row, final int col, SyntaxTreeNode... array) {
		this.row = row;
		this.col = col;
		type = SyntaxTreeNodeType.List;
		value = -1;
		cargo = null;
		children = array.clone();
	}

	void assignUnary(final int row, final int col, final SyntaxTreeNodeType subtype, final SyntaxTreeNode node) {
		this.row = row;
		this.col = col;
		type = subtype;
		value = -1;
		cargo = null;
		children = new SyntaxTreeNode[]{node};
	}

	void assignBinary(final int row, final int col, final long prty, final LexemaSubtype[] operations, SyntaxTreeNode[] operands) {
		this.row = row;
		this.col = col;
		type = SyntaxTreeNodeType.OrdinalBinary;
		value = prty;
		if (operations.length != operands.length) {
			throw new IllegalArgumentException("Operations list length differ from operands one"); 
		}
		else {
			cargo = operations;
			children = operands.clone();
		}
	}

	void assignAssignment(final int row, final int col, final SyntaxTreeNode left, final SyntaxTreeNode right) {
		this.row = row;
		this.col = col;
		type = SyntaxTreeNodeType.Assign;
		value = -1;
		cargo = null;
		children = new SyntaxTreeNode[]{left,right};
	}

	void assignPipe(final int row, final int col, final SyntaxTreeNode startPipe, final SyntaxTreeNode[] intermediate, final SyntaxTreeNode endPipe) {
		this.row = row;
		this.col = col;
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
				sb.append(prefix+STAIRWAY_STEP).append(cargo).append('\n');
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
			case Program	:
				sb.append(prefix).append("program\n");
				sb.append(((SyntaxTreeNode)cargo).toString(prefix+STAIRWAY_STEP,names));
				for (SyntaxTreeNode item : children) {
					sb.append(prefix).append("function/brick\n");
					sb.append(item.toString(prefix+STAIRWAY_STEP,names));
				}
				sb.append(prefix).append("end program\n");
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
				final VarDescriptor	varType = (VarDescriptor)cargo;
				
				sb.append(prefix).append(varType.isVar() ? "variable " : "").append("name ").append(names.getName(value)).append(" : ").append(varType.getNameType());
				if (children.length > 0) {
					sb.append("(\n").append(children[0].toString(prefix+STAIRWAY_STEP,names)).append(prefix).append(")\n");
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