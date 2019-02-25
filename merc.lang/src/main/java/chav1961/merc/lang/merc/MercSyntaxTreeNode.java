package chav1961.merc.lang.merc;

import java.util.Arrays;

import chav1961.merc.lang.merc.interfaces.VarDescriptor;
import chav1961.purelib.basic.interfaces.SyntaxTreeInterface;
import chav1961.purelib.cdb.SyntaxNode;
import chav1961.purelib.enumerations.ContinueMode;
import chav1961.purelib.enumerations.NodeEnterMode;

class MercSyntaxTreeNode extends SyntaxNode<MercSyntaxTreeNodeType,MercSyntaxTreeNode> {
	static final String		STAIRWAY_STEP = "   ";
	
	@FunctionalInterface
	interface WalkCallback {
		ContinueMode process(NodeEnterMode mode, MercSyntaxTreeNode node);
	}
	
	MercSyntaxTreeNode() {
		super(0,0,MercSyntaxTreeNodeType.Unknown,-1,null);
	}

	MercSyntaxTreeNode(final MercSyntaxTreeNode from) {
		super(from.row, from.col, from.type, from.value, from.cargo, from.children != null ? from.children.clone() : null);
	}

	MercSyntaxTreeNode(final MercSyntaxTreeNodeType type, final long value, final Object cargo, final MercSyntaxTreeNode... children) {
		this(0, 0, type, value, cargo, children);
	}
	
	MercSyntaxTreeNode(final int row, final int col, final MercSyntaxTreeNodeType type, final long value, final Object cargo, final MercSyntaxTreeNode... children) {
		super(row, col, type, value, cargo, children);
	}
	
	void rewrite(MercSyntaxTreeNode from) {
		
	}

	ContinueMode walk(final WalkCallback callback) {
		ContinueMode	cont;
		
		switch (cont = callback.process(NodeEnterMode.ENTER,this)) {
			case CONTINUE		:
				if (cargo instanceof MercSyntaxTreeNode) {
					switch (cont = ((MercSyntaxTreeNode)cargo).walk(callback)) {
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
loop:				for (MercSyntaxTreeNode item : children) {
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
	
	public void assign(final MercSyntaxTreeNode another) {
		this.row = another.row;
		this.col = another.col;
		this.type = another.type;
		this.value = another.value;
		this.cargo = another.cargo;
		this.children = another.children == null ? null : another.children.clone();
	}
	
	public void assignVarDefs(final int row, final int col, final MercSyntaxTreeNode[] array) {
		this.row = row;
		this.col = col;
		this.type = MercSyntaxTreeNodeType.Variables;
		this.value = -1;
		this.cargo = null;
		this.children = array.clone();
	}

	public void assignHeader(final int row, final int col, final long name, final MercSyntaxTreeNode... parms) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.Header;
		value = name;
		cargo = null;
		children = parms.clone();
	}

	public void assignHeaderWithReturned(final int row, final int col, final long name, final Class<?> returned, final MercSyntaxTreeNode... parms) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.HeaderWithReturned;
		value = name;
		cargo = returned;
		children = parms.clone();
	}
	
	public void assignBrick(final int row, final int col, final MercSyntaxTreeNode head, final MercSyntaxTreeNode body) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.Brick;
		value = -1;
		cargo = head;
		children = body.getType() == MercSyntaxTreeNodeType.Sequence ? body.children.clone() : new MercSyntaxTreeNode[]{body};
	}

	public void assignFunc(final int row, final int col, final MercSyntaxTreeNode head, final MercSyntaxTreeNode body) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.Function;
		value = -1;
		cargo = head;
		children = body.getType() == MercSyntaxTreeNodeType.Sequence ? body.children.clone() : new MercSyntaxTreeNode[]{body};
	}

	public void assignProgram(final int row, final int col, final MercSyntaxTreeNode main, final MercSyntaxTreeNode[] funcs) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.Program;
		value = -1;
		cargo = main;
		children = funcs;
	}
	
	public void assignField(final int row, final int col, final VarDescriptor desc, final MercSyntaxTreeNode owner, final MercSyntaxTreeNode field) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.InstanceField;
		value = -1;
		cargo = desc;
		children = new MercSyntaxTreeNode[]{owner,field};
	}

	public void assignName(final int row, final int col, final long intval) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.StandaloneName;
		value = intval;
		cargo = null;
		children = null;
	}

	public void assignPredefinedName(final int row, final int col, final LexemaSubtype subtype) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.PredefinedName;
		value = -1;
		cargo = subtype;
		children = null;
	}

	public void assignNameIndiced(final int row, final int col, final long name, final VarDescriptor desc, final MercSyntaxTreeNode indices) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.IndicedName;
		value = name;
		cargo = desc;
		if (indices.getType() != MercSyntaxTreeNodeType.List) {
			throw new IllegalArgumentException("Indices must be list!");
		}
		else {
			children = indices.children.clone();
		}
	}

	public void assignVarDefinition(final int row, final int col, final long nameId, final VarDescriptor desc) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.Variable;
		value = nameId;
		cargo = desc;
		children = new MercSyntaxTreeNode[0];
	}
	
	public void assignVarDefinition(final int row, final int col, final long nameId, final VarDescriptor desc, final MercSyntaxTreeNode initial) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.Variable;
		value = nameId;
		cargo = desc;
		children = new MercSyntaxTreeNode[]{initial};
	}
	
	public void assignCall(final int row, final int col, final long name, final VarDescriptor desc, final MercSyntaxTreeNode item, final MercSyntaxTreeNode parm) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.Call;
		value = name;
		cargo = desc;
		if (parm.getType() != MercSyntaxTreeNodeType.List) {
			throw new IllegalArgumentException("Parameters must be list!");
		}
		else {
			children = new MercSyntaxTreeNode[]{item,parm};
		}
	}

	public void assignCall(final int row, final int col, final long name, final VarDescriptor desc, final MercSyntaxTreeNode item) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.Call;
		value = name;
		cargo = desc;
		children = new MercSyntaxTreeNode[]{item};
	}
	
	public void assignConversion(final int row, final int col, final VarDescriptor conv, final MercSyntaxTreeNode inner) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.Conversion;
		value = -1;
		cargo = conv;
		if (inner.getType() != MercSyntaxTreeNodeType.List) {
			throw new IllegalArgumentException("Parameters must be list!");
		}
		else {
			children = inner.children.clone();
		}
	}

	public void assignStr(final int row, final int col, final char[] strval) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.StrConst;
		value = -1;
		cargo = strval;
		children = null;
	}

	public void assignRefConst(final int row, final int col, final Object refval) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.RealConst;
		value = -1;
		cargo = refval;
		children = null;
	}

	public void assignReal(final int row, final int col, final double realval) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.RealConst;
		value = Double.doubleToLongBits(realval);
		cargo = null;
		children = null;
	}

	public void assignNull(final int row, final int col) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.Null;
		value = -1;
		cargo = null;
		children = null;
	}

	public void assignInt(final int row, final int col, final long intval) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.IntConst;
		value = intval;
		cargo = null;
		children = null;
	}

	public void assignBoolean(final int row, final int col, final boolean boolval) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.BoolConst;
		value = boolval ? 1 : 0;
		cargo = null;
		children = null;
	}

	void assignIf(final int row, final int col, final MercSyntaxTreeNode cond, final MercSyntaxTreeNode thenBody) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.ShortIf;
		value = -1;
		cargo = cond;
		children = new MercSyntaxTreeNode[]{thenBody};
	}

	void assignIf(final int row, final int col, final MercSyntaxTreeNode cond, final MercSyntaxTreeNode thenBody, final MercSyntaxTreeNode elseBody) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.LongIf;
		value = -1;
		cargo = cond;
		children = new MercSyntaxTreeNode[]{thenBody,elseBody};
	}
	
	void assignWhile(final int row, final int col, final MercSyntaxTreeNode cond, final MercSyntaxTreeNode body) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.While;
		value = -1;
		cargo = cond;
		children = new MercSyntaxTreeNode[]{body};
	}
	
	void assignUntil(final int row, final int col, final MercSyntaxTreeNode cond, final MercSyntaxTreeNode body) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.Until;
		value = -1;
		cargo = cond;
		children = new MercSyntaxTreeNode[]{body};
	}

	void assignBreak(final int row, final int col, final int depth) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.Break;
		value = depth;
		cargo = null;
		children = null;
	}

	void assignContinue(final int row, final int col, final int depth) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.Continue;
		value = depth;
		cargo = null;
		children = null;
	}

	void assignReturn(final int row, final int col) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.ShortReturn;
		value = -1;
		cargo = null;
		children = null;
	}

	void assignReturn(final int row, final int col, final MercSyntaxTreeNode returnExpression) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.LongReturn;
		value = -1;
		cargo = null;
		children = new MercSyntaxTreeNode[]{returnExpression};
	}

	void assignPrint(final int row, final int col, final MercSyntaxTreeNode expressions) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.Print;
		value = -1;
		cargo = null;
		if (expressions.getType() == MercSyntaxTreeNodeType.List) {
			children = expressions.children.clone();
		}
		else {
			throw new IllegalArgumentException("Expressions is not a list!"); 
		}
	}

	void assignLock(final int row, final int col, final MercSyntaxTreeNode lockExpression, final MercSyntaxTreeNode lockBody) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.Lock;
		value = -1;
		cargo = lockExpression;
		children = new MercSyntaxTreeNode[]{lockBody};
	}

	void assignType(final int row, final int col, final LexemaSubtype varType) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.Vartype;
		value = -1;
		cargo = varType;
		children = new MercSyntaxTreeNode[0];
	}

	void assignFor(final int row, final int col, final MercSyntaxTreeNode forName, final MercSyntaxTreeNode forType, final MercSyntaxTreeNode forList, final MercSyntaxTreeNode forBody) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.TypedFor;
		value = -1;
		cargo = null;
		children = new MercSyntaxTreeNode[]{forName,forType,forList,forBody};
	}

	void assignFor(final int row, final int col, final MercSyntaxTreeNode forName, final MercSyntaxTreeNode forList, final MercSyntaxTreeNode forBody) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.UntypedFor;
		value = -1;
		cargo = null;
		children = new MercSyntaxTreeNode[]{forName,forList,forBody};
	}
	
	public void assignSequence(final int row, final int col, final MercSyntaxTreeNode... array) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.Sequence;
		value = -1;
		cargo = null;
		children = array.clone();
	}

	void assignRange(final int row, final int col, final MercSyntaxTreeNode itemFrom, final MercSyntaxTreeNode itemTo) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.Range;
		value = -1;
		cargo = null;
		children = new MercSyntaxTreeNode[]{itemFrom,itemTo};
	}

	void assignList(final int row, final int col, MercSyntaxTreeNode... array) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.List;
		value = -1;
		cargo = null;
		children = array.clone();
	}

	void assignUnary(final int row, final int col, final LexemaSubtype oper, final VarDescriptor desc, final MercSyntaxTreeNode node) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.OrdinalUnary;
		value = oper.ordinal();
		cargo = desc;
		children = new MercSyntaxTreeNode[]{node};
	}

	void assignBinary(final int row, final int col, final long prty, final LexemaSubtype[] operations, MercSyntaxTreeNode[] operands) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.OrdinalBinary;
		value = prty;
		if (operations.length != operands.length) {
			throw new IllegalArgumentException("Operations list length differ from operands one"); 
		}
		else {
			cargo = operations;
			children = operands.clone();
		}
	}

	void assignAssignment(final int row, final int col, final MercSyntaxTreeNode left, final MercSyntaxTreeNode right) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.Assign;
		value = -1;
		cargo = null;
		children = new MercSyntaxTreeNode[]{left,right};
	}

	void assignPipe(final int row, final int col, final MercSyntaxTreeNode startPipe, final MercSyntaxTreeNode[] intermediate, final MercSyntaxTreeNode endPipe) {
		this.row = row;
		this.col = col;
		type = MercSyntaxTreeNodeType.Pipe;
		value = -1;
		cargo = new MercSyntaxTreeNode[]{startPipe,endPipe};
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
			case BoolConst	:
				sb.append(prefix).append(value == 1 ? "true" : "false").append('\n');
				break;
			case Break		:
				sb.append(prefix).append("break").append(value == 0 ? "" : " "+value).append('\n');
				break;
			case Brick		:
				sb.append(prefix).append("brick:\n");
				sb.append(((MercSyntaxTreeNode)cargo).toString(prefix+STAIRWAY_STEP,names));
				if (children.length > 0) {
					before = "body\n";
					sb.append(prefix).append(before);
					for (MercSyntaxTreeNode item : children) {
						sb.append(item.toString(prefix+STAIRWAY_STEP,names));
					}
				}
				sb.append(prefix).append("end brick\n");
				break;
			case Call		:
				final VarDescriptor	varType = (VarDescriptor)cargo;
				
				sb.append(prefix).append("Call ").append(names.getName(value)).append(": ").append(varType.isVar() ? "variable " : "").append("name ").append(names.getName(value)).append(" : ").append(varType.getNameType()).append('\n');
				if (children.length > 0) {
					sb.append(prefix).append("with :\n");
					before = "";
					for (MercSyntaxTreeNode item : children) {
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
				sb.append(prefix).append("convert to ").append(((VarDescriptor)cargo).getNameType()).append("\n");
				before = "";
				for (MercSyntaxTreeNode item : children) {
					sb.append(before).append(item.toString(prefix+STAIRWAY_STEP,names));
					before = prefix+STAIRWAY_STEP+",\n";
				}
				sb.append(prefix).append(")\n");
				break;
			case Function	:
				sb.append(prefix).append("func:\n");
				sb.append(((MercSyntaxTreeNode)cargo).toString(prefix+STAIRWAY_STEP,names));
				if (children.length > 0) {
					before = "body\n";
					sb.append(prefix).append(before);
					for (MercSyntaxTreeNode item : children) {
						sb.append(item.toString(prefix+STAIRWAY_STEP,names));
					}
				}
				sb.append(prefix).append("end func\n");
				break;
			case Header		:
				sb.append(prefix).append("header ").append(names.getName(value)).append(":\n");
				before = "";
				for (MercSyntaxTreeNode item : children) {
					sb.append(before).append(item.toString(prefix+STAIRWAY_STEP,names));
					before = prefix+",\n";
				}
				sb.append(prefix).append("end header\n");
				break;
			case HeaderWithReturned	:
				sb.append(prefix).append("header ").append(names.getName(value)).append(":\n");
				before = "";
				for (MercSyntaxTreeNode item : children) {
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
				for (MercSyntaxTreeNode item : children) {
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
				for (MercSyntaxTreeNode item : children) {
					sb.append(before);
					sb.append(item.toString(prefix+STAIRWAY_STEP,names));
					before = prefix+",\n"; 
				}
				sb.append(prefix).append(")\n");
				break;
			case Lock		:
				sb.append(prefix).append("lock\n");
				sb.append(((MercSyntaxTreeNode)cargo).toString(prefix+STAIRWAY_STEP,names));
				sb.append(prefix).append("--->\n");
				sb.append(children[0].toString(prefix+STAIRWAY_STEP,names));
				sb.append(prefix).append("end lock\n");
				break;
			case LongIf		: 
				sb.append(prefix).append("if\n");
				sb.append(((MercSyntaxTreeNode)cargo).toString(prefix+STAIRWAY_STEP,names));
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
			case Null	:
				sb.append(prefix).append("null\n");
				break;
			case OrdinalBinary:
				sb.append(prefix).append("binary list (\n");
				for (int index = 0; index < children.length; index++) {
					if (index > 0) {
						sb.append(prefix).append(((LexemaSubtype[])cargo)[index]).append('\n');
					}
					sb.append(children[index].toString(prefix+STAIRWAY_STEP,names));
				}
				sb.append(prefix).append(")\n");
				break;
			case OrdinalUnary:
				switch (LexemaSubtype.values()[(int)value]) {
					case BitInv		:
						sb.append(prefix).append("~ (\n");
						sb.append(children[0].toString(prefix+STAIRWAY_STEP,names));
						sb.append(prefix).append(")\n");
						break;
					case Neg		:
						sb.append(prefix).append("- (\n");
						sb.append(children[0].toString(prefix+STAIRWAY_STEP,names));
						sb.append(prefix).append(")\n");
						break;
					case Not		:
						sb.append(prefix).append("not (\n");
						sb.append(children[0].toString(prefix+STAIRWAY_STEP,names));
						sb.append(prefix).append(")\n");
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
				}
				break;
			case Pipe	:
				sb.append(prefix).append("pipe:\n");
				sb.append(prefix+STAIRWAY_STEP).append("from\n");
				sb.append(((MercSyntaxTreeNode[])cargo)[0].toString(prefix+STAIRWAY_STEP+STAIRWAY_STEP,names));
				if (children.length > 0) {
					before = "thru\n";
					sb.append(prefix+STAIRWAY_STEP).append(before);
					for (MercSyntaxTreeNode item : children) {
						sb.append(item.toString(prefix+STAIRWAY_STEP+STAIRWAY_STEP,names));
					}
					before = "----\n";
				}
				sb.append(prefix+STAIRWAY_STEP).append("to\n");
				sb.append(((MercSyntaxTreeNode[])cargo)[1].toString(prefix+STAIRWAY_STEP+STAIRWAY_STEP,names));
				sb.append(prefix).append("end pipe\n");
				break;
			case PredefinedName:
				sb.append(prefix).append(cargo).append('\n');
				break;
			case Print	:
				sb.append(prefix).append("print\n");
				for (MercSyntaxTreeNode item : children) {
					sb.append(item.toString(prefix+STAIRWAY_STEP,names));
				}
				sb.append(prefix).append("end print\n");
				break;
			case Program	:
				sb.append(prefix).append("program\n");
				sb.append(((MercSyntaxTreeNode)cargo).toString(prefix+STAIRWAY_STEP,names));
				for (MercSyntaxTreeNode item : children) {
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
				for (MercSyntaxTreeNode item : children) {
					sb.append(item.toString(prefix+STAIRWAY_STEP,names));
				}
				sb.append(prefix).append("}\n");
				break;
			case ShortIf	:
				sb.append(prefix).append("if\n");
				sb.append(((MercSyntaxTreeNode)cargo).toString(prefix+STAIRWAY_STEP,names));
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
				sb.append(((MercSyntaxTreeNode)cargo).toString(prefix+STAIRWAY_STEP,names));
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
				for (MercSyntaxTreeNode item : children) {
					sb.append(item.toString(prefix,names));
				}
				break;
			case Variable	:
				final VarDescriptor	callType = (VarDescriptor)cargo;
				
				sb.append(prefix).append(callType.isVar() ? "variable " : "").append("name ").append(names.getName(value)).append(" : ").append(callType.getNameType());
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
				sb.append(((MercSyntaxTreeNode)cargo).toString(prefix+STAIRWAY_STEP,names));
				sb.append(prefix).append("do\n");
				sb.append(children[0].toString(prefix+STAIRWAY_STEP,names));
				sb.append(prefix).append("end while\n");
				break;
			default:
				throw new UnsupportedOperationException("Type ["+getType()+"] is not supported yet");
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return "SyntaxTreeNode [row=" + row + ", col=" + col + ", type=" + type + ", value=" + value + ", cargo=" + cargo + ", children=" + Arrays.toString(children) + "]";
	}
}