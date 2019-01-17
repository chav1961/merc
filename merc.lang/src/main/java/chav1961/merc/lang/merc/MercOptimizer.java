package chav1961.merc.lang.merc;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import chav1961.merc.lang.merc.SyntaxTreeNode.SyntaxTreeNodeType;
import chav1961.merc.lang.merc.interfaces.VarDescriptor;

class MercOptimizer {
	static Class<?> processTypeConversions(final SyntaxTreeNode node, final Class<?> preferredClass, List<SyntaxTreeNode> staticInitials) {
		// TODO:
		switch (node.getType()) {
			case AllocatedVariable:
				break;
			case Assign:
				final Class<?>	left = processTypeConversions(node.children[0],null,staticInitials);
				
				processTypeConversions(node.children[1],left,staticInitials);
				return left;
			case BitInv : case PostDec : case PostInc : case PreDec : case PreInc :
				return processTypeConversions(node.children[0],long.class,staticInitials); 
			case BoolConst:
				if (preferredClass == null) {
					return boolean.class;
				}
				else if (preferredClass.isAssignableFrom(boolean.class)) {
					return preferredClass;
				}
				else {
					throw new UnsupportedOperationException("Unsupported conversion from [boolean] to ["+preferredClass.getCanonicalName()+"]");
				}
			case Call:
				break;
			case Continue:
				break;
			case Conversion:
				break;
			case Empty:
				break;
			case Function:
				break;
			case Header:
				break;
			case HeaderWithReturned:
				break;
			case InList:
				break;
			case IndicedName:
				for (SyntaxTreeNode item : node.children) {
					processTypeConversions(item,null,staticInitials); 
				}
				if (preferredClass == null) {
					return ((VarDescriptor)node.cargo).getNameType();
				}
				else if (preferredClass.isAssignableFrom(((VarDescriptor)node.cargo).getNameType())) {
					return preferredClass;
				}
				else {
					throw new UnsupportedOperationException("Unsupported conversion from ["+((VarDescriptor)node.cargo).getNameType()+"] to ["+preferredClass.getCanonicalName()+"]");
				}
			case Infinite:
				for (SyntaxTreeNode item : node.children) {
					processTypeConversions(item,null,staticInitials); 
				}
				return null;
			case InstanceField:
				break;
			case IntConst :
				if (preferredClass == null) {
					return long.class;
				}
				else if (preferredClass.isAssignableFrom(long.class)) {
					return preferredClass;
				}
				else {
					throw new UnsupportedOperationException("Unsupported conversion from [long] to ["+preferredClass.getCanonicalName()+"]");
				}
			case List:
				break;
			case Lock:
				break;
			case LongReturn:
				break;
			case Negation:
				break;
			case Not:
				return processTypeConversions(node.children[0],boolean.class,staticInitials); 
			case Null:
				break;
			case OrdinalBinary:
				break;
			case Pipe:
				break;
			case PredefinedName:
				break;
			case Print:
				break;
			case Program:
				break;
			case Range:
				break;
			case RealConst:
				if (preferredClass == null) {
					return double.class;
				}
				else if (preferredClass.isAssignableFrom(double.class)) {
					return preferredClass;
				}
				else {
					throw new UnsupportedOperationException("Unsupported conversion from [boolean] to ["+preferredClass.getCanonicalName()+"]");
				}
			case RefConst:
				break;
			case Sequence:
				for (SyntaxTreeNode item : node.children) {
					processTypeConversions(item,null,staticInitials); 
				}
				return null;
			case LongIf : case ShortIf :
				processTypeConversions((SyntaxTreeNode)node.cargo,boolean.class,staticInitials);
				for (SyntaxTreeNode item : node.children) {
					processTypeConversions(item,null,staticInitials); 
				}
				return null;
			case ShortReturn:
				break;
			case StandaloneName:
				if (preferredClass == null) {
					return ((VarDescriptor)node.cargo).getNameType();
				}
				else if (preferredClass.isAssignableFrom(((VarDescriptor)node.cargo).getNameType())) {
					return preferredClass;
				}
				else {
					throw new UnsupportedOperationException("Unsupported conversion from [boolean] to ["+preferredClass.getCanonicalName()+"]");
				}
			case StrConst	:
				if (preferredClass == null) {
					return char[].class;
				}
				else if (preferredClass.isAssignableFrom(char[].class)) {
					return preferredClass;
				}
				else {
					throw new UnsupportedOperationException("Unsupported conversion from [boolean] to ["+preferredClass.getCanonicalName()+"]");
				}
			case TypedFor:
				break;
			case Unknown	:
				break;
			case UntypedFor	:
				break;
			case Variable 	:
				break;
			case Variables	:
				break;
			case Vartype	:
				break;
			case Until : case While :
				simpleWalkTypeConversionsNode(node, staticInitials);
				return null;
			default : throw new UnsupportedOperationException("Node type ["+node.getType()+"] is not supported yet");		
		}
		return preferredClass;
	}

	static void processSpeculativeAssignments(final SyntaxTreeNode node) {
		// TODO:
	}
	
	static void processConstantExpressions(final SyntaxTreeNode node) {
		if (isConstantValue(node)) {
			node.assign(buildConstantValue(node));
		}
		else {
			switch(node.getType()) {
				case LongIf		:
					if (isConstantValue((SyntaxTreeNode)node.cargo)) {
						final SyntaxTreeNode	value = buildConstantValue(node);
						
						if (value.value == 1) {	// Always true
							processConstantExpressions(node.children[0]);
							node.assign(node.children[0]);
						}
						else {
							processConstantExpressions(node.children[1]);
							node.assign(node.children[1]);
						}
					}
					else {
						simpleWalkConstantExpressionNode(node);
					}
					break;
				case ShortIf	:
					if (isConstantValue((SyntaxTreeNode)node.cargo)) {
						final SyntaxTreeNode	value = buildConstantValue(node);
						
						if (value.value == 1) {	// Always true
							processConstantExpressions(node.children[0]);
							node.assign(node.children[0]);
						}
						else {
							node.assign(new SyntaxTreeNode(SyntaxTreeNodeType.Empty,0,null,new SyntaxTreeNode[0]));
						}
					}
					else {
						simpleWalkConstantExpressionNode(node);
					}
					break;
				case While : case Until	:
					if (isConstantValue((SyntaxTreeNode)node.cargo)) {
						final SyntaxTreeNode	value = buildConstantValue(node);
						
						if (value.value == 1) {	// Always true
							processConstantExpressions(node.children[0]);
							node.assign(new SyntaxTreeNode(SyntaxTreeNodeType.Infinite,0,null,new SyntaxTreeNode[]{node.children[0]}));
						}
						else {
							node.assign(new SyntaxTreeNode(SyntaxTreeNodeType.Empty,0,null,new SyntaxTreeNode[0]));
						}
					}
					else {
						simpleWalkConstantExpressionNode(node);
					}
					break;
				default :
					simpleWalkConstantExpressionNode(node);
			}
		}
	}
	
	private static void simpleWalkConstantExpressionNode(SyntaxTreeNode node) {
		if (node.cargo instanceof SyntaxTreeNode) {
			processConstantExpressions((SyntaxTreeNode)node.cargo);
		}
		for (SyntaxTreeNode item : node.children) {
			processConstantExpressions(item);
		}
	}

	private static void simpleWalkTypeConversionsNode(final SyntaxTreeNode node, final List<SyntaxTreeNode> staticInitials) {
		if (node.cargo instanceof SyntaxTreeNode) {
			processTypeConversions((SyntaxTreeNode)node.cargo,boolean.class,staticInitials);
		}
		for (SyntaxTreeNode item : node.children) {
			processTypeConversions(item,null,staticInitials); 
		}
	}
	
	private static boolean isConstantValue(final SyntaxTreeNode node) {
		switch (node.getType()) {
			case BoolConst : case IntConst : case RealConst : case StrConst : case Null : case RefConst:
				return true;
			case AllocatedVariable : case IndicedName : case InstanceField : case Assign :
			case PostDec : case PostInc : case PreDec : case PreInc : case PredefinedName :
			case Call : case StandaloneName :
				return false;
			case Negation : case BitInv : case Not :
				return isConstantValue(node.children[0]);
			case Conversion : case List : case Range : case OrdinalBinary:
				for (SyntaxTreeNode item : node.children) {
					if (!isConstantValue(item)) {
						return false;
					}
				}
				return true;
			default : throw new UnsupportedOperationException("Node type ["+node.getType()+"] is not supported yet");
		}
	}

	private static SyntaxTreeNode buildConstantValue(final SyntaxTreeNode node) {
		SyntaxTreeNode	child;
		LexemaSubtype[]	operators;
		long			longValue;
		double			doubleValue;
		
		switch (node.getType()) {
			case BoolConst : case IntConst : case RealConst : case StrConst : case Null : case RefConst:
				return node;
			case Negation : 
				child = buildConstantValue(node.children[0]);
				if (child.getType() == SyntaxTreeNodeType.RealConst) {
					child.value = Double.doubleToLongBits(-Double.longBitsToDouble(child.value));
				}
				else {
					child.value = -child.value;
				}
				return child;
			case BitInv : 
				child = buildConstantValue(node.children[0]);
				child.value = ~child.value;
				return child;
			case Not :
				child = buildConstantValue(node.children[0]);
				child.value = 1 - child.value;
				return child;
			case OrdinalBinary :
				switch ((int)node.value) {
					case MercCompiler.PRTY_BITAND	:
						longValue = buildConstantValue(node.children[0]).value;
						for (int index = 1; index < node.children.length; index++) {
							longValue &= buildConstantValue(node.children[index]).value;
						}
						node.assignInt(longValue);
						return node;
					case MercCompiler.PRTY_BITORXOR	:
						longValue = buildConstantValue(node.children[0]).value;
						operators = (LexemaSubtype[])node.cargo;
						for (int index = 1; index < node.children.length; index++) {
							if (operators[index] == LexemaSubtype.BitOr) {
								longValue |= buildConstantValue(node.children[index]).value;
							}
							else {
								longValue ^= buildConstantValue(node.children[index]).value;
							}
						}
						node.assignInt(longValue);
						return node;
					case MercCompiler.PRTY_SHIFT	:
						longValue = buildConstantValue(node.children[0]).value;
						operators = (LexemaSubtype[])node.cargo;
						for (int index = 1; index < node.children.length; index++) {
							if (operators[index] == LexemaSubtype.Shl) {
								longValue <<= buildConstantValue(node.children[index]).value;
							}
							else if (operators[index] == LexemaSubtype.Shr) {
								longValue >>= buildConstantValue(node.children[index]).value;
							}
							else {
								longValue >>>= buildConstantValue(node.children[index]).value;
							}
						}
						node.assignInt(longValue);
						return node;
					case MercCompiler.PRTY_MUL		:
						child = buildConstantValue(node.children[0]);
						operators = (LexemaSubtype[])node.cargo;
						if (child.getType() == SyntaxTreeNodeType.RealConst) {
							doubleValue = Double.longBitsToDouble(child.value);
							for (int index = 1; index < node.children.length; index++) {
								if (operators[index] == LexemaSubtype.Mul) {
									doubleValue *= Double.longBitsToDouble(buildConstantValue(node.children[index]).value);
								}
								else if (operators[index] == LexemaSubtype.Div) {
									doubleValue /= Double.longBitsToDouble(buildConstantValue(node.children[index]).value);
								}
								else {
									doubleValue %= Double.longBitsToDouble(buildConstantValue(node.children[index]).value);
								}
							}
							node.assignReal(doubleValue);
						}
						else {
							longValue = child.value;
							for (int index = 1; index < node.children.length; index++) {
								if (operators[index] == LexemaSubtype.Mul) {
									longValue *= buildConstantValue(node.children[index]).value;
								}
								else if (operators[index] == LexemaSubtype.Div) {
									longValue /= buildConstantValue(node.children[index]).value;
								}
								else {
									longValue %= buildConstantValue(node.children[index]).value;
								}
							}
							node.assignInt(longValue);
						}
						return node;
					case MercCompiler.PRTY_ADD		:
						child = buildConstantValue(node.children[0]);
						operators = (LexemaSubtype[])node.cargo;
						if (child.getType() == SyntaxTreeNodeType.RealConst) {
							doubleValue = Double.longBitsToDouble(child.value);
							for (int index = 1; index < node.children.length; index++) {
								if (operators[index] == LexemaSubtype.Add) {
									doubleValue += Double.longBitsToDouble(buildConstantValue(node.children[index]).value);
								}
								else if (operators[index] == LexemaSubtype.Sub) {
									doubleValue -= Double.longBitsToDouble(buildConstantValue(node.children[index]).value);
								}
							}
							node.assignReal(doubleValue);
						}
						else if (child.getType() == SyntaxTreeNodeType.IntConst) {
							longValue = child.value;
							for (int index = 1; index < node.children.length; index++) {
								if (operators[index] == LexemaSubtype.Add) {
									longValue += buildConstantValue(node.children[index]).value;
								}
								else if (operators[index] == LexemaSubtype.Sub) {
									longValue -= buildConstantValue(node.children[index]).value;
								}
							}
							node.assignInt(longValue);
						}
						else {
							final StringBuilder	sb = new StringBuilder();
							
							sb.append((char[])child.cargo);
							for (int index = 1; index < node.children.length; index++) {
								if (operators[index] == LexemaSubtype.Add) {
									sb.append((char[])buildConstantValue(node.children[index]).cargo);
								}
								else if (operators[index] == LexemaSubtype.Sub) {
									sb.append(new String((char[])buildConstantValue(node.children[index]).cargo).trim());
								}
							}
							node.assignStr(sb.toString().toCharArray());
						}
						return node;
					case MercCompiler.PRTY_COMPARISON:
						child = buildConstantValue(node.children[0]);
						operators = (LexemaSubtype[])node.cargo;
						switch (((LexemaSubtype[])node.cargo)[1]) {
							case EQ 	:
								node.assignBoolean(compareConstants(child,buildConstantValue(node.children[1])) == 0);
								break;
							case NE 	:
								node.assignBoolean(compareConstants(child,buildConstantValue(node.children[1])) != 0);
								break;
							case LT 	:
								node.assignBoolean(compareConstants(child,buildConstantValue(node.children[1])) < 0);
								break;
							case LE 	:
								node.assignBoolean(compareConstants(child,buildConstantValue(node.children[1])) <= 0);
								break;
							case GT 	:
								node.assignBoolean(compareConstants(child,buildConstantValue(node.children[1])) > 0);
								break;
							case GE 	:
								node.assignBoolean(compareConstants(child,buildConstantValue(node.children[1])) >= 0);
								break;
							case IS 	:
								return node;
							case LIKE	:
								node.assignBoolean(Pattern.matches(new String((char[])buildConstantValue(node.children[1]).cargo), new String((char[])child.cargo)));
								return node;
							case InList	:
								for (SyntaxTreeNode item : node.children[1].children) {
									if (item.getType() == SyntaxTreeNodeType.Range) {
										if (compareConstants(child,buildConstantValue(item.children[0])) >= 0 && compareConstants(child,buildConstantValue(item.children[1])) <= 0) {
											node.assignBoolean(true);
											return node;
										}
									}
									else if (compareConstants(child,buildConstantValue(item)) == 0) {
										node.assignBoolean(true);
										return node;
									}
								}
								node.assignBoolean(false);
								return node;
							default : throw new UnsupportedOperationException("Operation type ["+((LexemaSubtype[])node.cargo)[1]+"] is not supported yet");
						}
						return node;
					case MercCompiler.PRTY_AND		:
						longValue = buildConstantValue(node.children[0]).value;
						for (int index = 1; index < node.children.length && longValue != 0; index++) {	// Short scheme!
							longValue &= buildConstantValue(node.children[index]).value;
						}
						node.assignBoolean(longValue == 1);
						return node;
					case MercCompiler.PRTY_OR		:
						longValue = buildConstantValue(node.children[0]).value;
						for (int index = 1; index < node.children.length && longValue == 0; index++) {	// Short scheme!
							longValue |= buildConstantValue(node.children[index]).value;
						}
						node.assignBoolean(longValue == 1);
						return node;
					default : throw new UnsupportedOperationException("Node priority ["+node.value+"] is not supported yet");
				}
			case Conversion :
				return node;
			default : throw new UnsupportedOperationException("Node type ["+node.getType()+"] is not supported yet");
		}
	}

	private static int compareConstants(final SyntaxTreeNode left, final SyntaxTreeNode right) {
		switch (left.getType()) {
			case BoolConst : case IntConst :
				return (int)(left.value - right.value);
			case RealConst 	:
				return (int)Math.signum(Double.longBitsToDouble(left.value) - Double.longBitsToDouble(right.value));
			case StrConst 	:
				return new String((char[])left.cargo).compareTo(new String((char[])right.cargo));
			default : throw new UnsupportedOperationException("Node type ["+left.getType()+"] is not supported yet");
		}
	}
}
