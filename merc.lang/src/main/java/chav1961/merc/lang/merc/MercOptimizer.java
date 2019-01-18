package chav1961.merc.lang.merc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import chav1961.merc.api.Area;
import chav1961.merc.api.AreaKeeper;
import chav1961.merc.api.BooleanKeeper;
import chav1961.merc.api.DoubleKeeper;
import chav1961.merc.api.LongKeeper;
import chav1961.merc.api.Point;
import chav1961.merc.api.PointKeeper;
import chav1961.merc.api.Size;
import chav1961.merc.api.SizeKeeper;
import chav1961.merc.api.StringKeeper;
import chav1961.merc.api.Track;
import chav1961.merc.api.TrackKeeper;
import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.api.interfaces.front.World;
import chav1961.merc.lang.merc.SyntaxTreeNode.SyntaxTreeNodeType;
import chav1961.merc.lang.merc.interfaces.VarDescriptor;
import chav1961.purelib.basic.exceptions.SyntaxException;

class MercOptimizer {
	private static final Set<Class<?>>					POINT_CLASSES = new HashSet<>();
	private static final Map<LexemaSubtype,Class<?>>	PREDEFINED_CLASSES = new HashMap<>();
	private static final Map<Class<?>,Class<?>>			RESOLVED_4_VALUE = new HashMap<>();
	private static final Map<Class<?>,Map<Class<?>,ConvertType>>	RESOLVED_GETTER = new HashMap<>();
	private static final Map<Class<?>,Map<Class<?>,Class<?>>>		RESOLVED_DOMINATOR = new HashMap<>();

	enum ConvertType {
		None, CallGetValue,
	}
	
	static {
		POINT_CLASSES.add(Point.class);
		POINT_CLASSES.add(Area.class);
		POINT_CLASSES.add(Track.class);
		
		PREDEFINED_CLASSES.put(LexemaSubtype.World, World.class);
		
		RESOLVED_4_VALUE.put(BooleanKeeper.class,boolean.class);
		RESOLVED_4_VALUE.put(DoubleKeeper.class,double.class);
		RESOLVED_4_VALUE.put(LongKeeper.class,long.class);
		RESOLVED_4_VALUE.put(StringKeeper.class,char[].class);
		RESOLVED_4_VALUE.put(AreaKeeper.class,Area.class);
		RESOLVED_4_VALUE.put(PointKeeper.class,Point.class);
		RESOLVED_4_VALUE.put(SizeKeeper.class,Size.class);
		RESOLVED_4_VALUE.put(TrackKeeper.class,Track.class);
		
		Map<Class<?>,ConvertType>	content = new HashMap<>();
		
		content.put(long.class, ConvertType.CallGetValue);
		RESOLVED_GETTER.put(LongKeeper.class, content);
	}
	
	
	static Class<?> processTypeConversions(final SyntaxTreeNode node, final Class<?> preferredClass, final Class<?> returnedClass, List<SyntaxTreeNode> staticInitials) throws SyntaxException {
		// TODO:
		switch (node.getType()) {
			case AllocatedVariable:
				break;
			case Assign:
				final Class<?>	assignLeft = processTypeConversions(node.children[0],null,returnedClass,staticInitials);
				
				processTypeConversions(node.children[1],assignLeft,returnedClass,staticInitials);
				if (preferredClass == null) {
					return assignLeft;
				}
				else if (preferredClass.isAssignableFrom(assignLeft)) {
					return preferredClass;
				}
				else {
					return convertValueTypeTo(node,preferredClass,staticInitials);
				}
			case BitInv : case PostDec : case PostInc : case PreDec : case PreInc :
				if (preferredClass == null) {
					return processTypeConversions(node.children[0],long.class,returnedClass,staticInitials); 
				}
				else if (long.class.isAssignableFrom(preferredClass)) {
					return processTypeConversions(node.children[0],preferredClass,returnedClass,staticInitials);
				}
				else {
					return convertValueTypeTo(node.children[0],preferredClass,staticInitials);
				}
			case BoolConst:
				if (preferredClass == null) {
					return boolean.class;
				}
				else if (preferredClass.isAssignableFrom(boolean.class)) {
					return preferredClass;
				}
				else {
					return convertValueTypeTo(node,preferredClass,staticInitials);
				}
			case Brick:
				for (SyntaxTreeNode item : node.children) {
					processTypeConversions(item,null,null,staticInitials); 
				}
				return null;
			case Function:
				final Class<?>	returned = resolveType4Value((Class<?>)((SyntaxTreeNode)((SyntaxTreeNode)node.cargo).cargo).cargo);
				
				for (SyntaxTreeNode item : node.children) {
					processTypeConversions(item,null,returned,staticInitials); 
				}
				return null;
			case Break: case Continue:
				return null;
			case Call:
				break;
			case Conversion:
				if (preferredClass == null) {
					return resolveType4Value((Class<?>)node.cargo);
				}
				else {
					return convertValueTypeTo(node.children[0],preferredClass,staticInitials);
				}
			case Empty:
				return null;
			case Header:
				return null;
			case HeaderWithReturned:
				return null;
			case IndicedName:
				for (SyntaxTreeNode item : node.children) {
					processTypeConversions(item,long.class,returnedClass,staticInitials); 
				}
				if (preferredClass == null) {
					return ((VarDescriptor)node.cargo).getNameType();
				}
				else if (preferredClass.isAssignableFrom(((VarDescriptor)node.cargo).getNameType())) {
					return preferredClass;
				}
				else {
					// TODO:
					return convertValueTypeTo(node.children[0],preferredClass,staticInitials);
				}
			case Infinite:
				for (SyntaxTreeNode item : node.children) {
					processTypeConversions(item,null,returnedClass,staticInitials); 
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
					return convertValueTypeTo(node,preferredClass,staticInitials);
				}
			case List:
				if (preferredClass == null) {
					final List<Class<?>>	memberClasses = new ArrayList<>(); 
							
					for (SyntaxTreeNode item : node.children) {
						memberClasses.add(processTypeConversions(item,null,returnedClass,staticInitials)); 
					}
					final Class<?>			dominated = extractDominatingClass(memberClasses);
					
					for (SyntaxTreeNode item : node.children) {
						memberClasses.add(processTypeConversions(item,dominated,returnedClass,staticInitials)); 
					}
					return dominated;
				}
				else {
					for (SyntaxTreeNode item : node.children) {
						processTypeConversions(item,preferredClass,returnedClass,staticInitials); 
					}
					return preferredClass;
				}
			case Lock:
				final Class<?>			dominated = processTypeConversions(((SyntaxTreeNode)node.cargo),null,returnedClass,staticInitials);
				
				if (POINT_CLASSES.contains(dominated)) {
					processTypeConversions(((SyntaxTreeNode)node.cargo),dominated,returnedClass,staticInitials);
				}
				else {
					throw new UnsupportedOperationException("Unsupported conversion from [long] to ["+preferredClass.getCanonicalName()+"]");
				}
				
				for (SyntaxTreeNode item : node.children) {
					processTypeConversions(item,null,returnedClass,staticInitials); 
				}
				return null;
			case LongReturn:
				return processTypeConversions(node.children[0],returnedClass,returnedClass,staticInitials); 
			case Negation:
				if (preferredClass == null) {
					return processTypeConversions(node.children[0],null,returnedClass,staticInitials);
				}
				else if (preferredClass.isAssignableFrom(long.class) || preferredClass.isAssignableFrom(double.class)) {
					return processTypeConversions(node.children[0],preferredClass,returnedClass,staticInitials);
				}
				else {
					throw new UnsupportedOperationException("Unsupported conversion from [long] to ["+preferredClass.getCanonicalName()+"]");
				}
			case Not:
				return processTypeConversions(node.children[0],boolean.class,returnedClass,staticInitials); 
			case Null:
				return preferredClass;
			case OrdinalBinary:
				if (preferredClass == null) {
					switch ((int)node.value) {
					
					}
				}
				break;
			case Pipe:
				for (SyntaxTreeNode item : ((SyntaxTreeNode[])node.cargo)) {
					processTypeConversions(item,null,returnedClass,staticInitials); 
				}
				for (SyntaxTreeNode item : node.children) {
					processTypeConversions(item,null,returnedClass,staticInitials); 
				}
				return null;
			case PredefinedName:
				if (preferredClass == null) {
					return PREDEFINED_CLASSES.get((LexemaSubtype)node.cargo);
				}
				else if (preferredClass.isAssignableFrom(PREDEFINED_CLASSES.get((LexemaSubtype)node.cargo))) {
					return preferredClass;
				}
				else {
					throw new UnsupportedOperationException("Unsupported conversion from ["+PREDEFINED_CLASSES.get((LexemaSubtype)node.cargo)+"] to ["+preferredClass.getCanonicalName()+"]");
				}
			case Print:
				for (SyntaxTreeNode item : node.children) {
					processTypeConversions(item,null,returnedClass,staticInitials); 
				}
				return null;
			case Program:
				processTypeConversions(((SyntaxTreeNode)node.cargo),null,returnedClass,staticInitials); 
				for (SyntaxTreeNode item : node.children) {
					processTypeConversions(item,null,returnedClass,staticInitials); 
				}
				return null;
			case Range:
				if (preferredClass == null) {
					Class<?>	rangeLeft = processTypeConversions(node.children[0],null,returnedClass,staticInitials);
					
					processTypeConversions(node.children[1],rangeLeft,returnedClass,staticInitials);
					return rangeLeft;
				}
				else {
					for (SyntaxTreeNode item : node.children) {
						processTypeConversions(item,preferredClass,returnedClass,staticInitials); 
					}
					return preferredClass;
				}
			case RealConst:
				if (preferredClass == null) {
					return double.class;
				}
				else if (preferredClass.isAssignableFrom(double.class)) {
					return preferredClass;
				}
				else {
					// TODO:
					return convertValueTypeTo(node.children[0],preferredClass,staticInitials);
				}
			case RefConst:
				break;
			case Sequence:
				for (SyntaxTreeNode item : node.children) {
					processTypeConversions(item,null,returnedClass,staticInitials); 
				}
				return null;
			case LongIf : case ShortIf :
				processTypeConversions((SyntaxTreeNode)node.cargo,boolean.class,returnedClass,staticInitials);
				for (SyntaxTreeNode item : node.children) {
					processTypeConversions(item,null,returnedClass,staticInitials); 
				}
				return null;
			case ShortReturn:
				return null;
			case StandaloneName:
				if (preferredClass == null) {
					return ((VarDescriptor)node.cargo).getNameType();
				}
				else if (preferredClass.isAssignableFrom(((VarDescriptor)node.cargo).getNameType())) {
					return preferredClass;
				}
				else {
					return convertValueTypeTo(node,preferredClass,staticInitials);
				}
			case StrConst	:
				if (preferredClass == null) {
					return char[].class;
				}
				else if (preferredClass.isAssignableFrom(char[].class)) {
					return preferredClass;
				}
				else {
					// TODO:
					return convertValueTypeTo(node.children[0],preferredClass,staticInitials);
				}
			case TypedFor:
				final Class<?>	forVariableTyped = processTypeConversions(node.children[1],null,returnedClass,staticInitials);
				
				processTypeConversions(node.children[2],forVariableTyped,returnedClass,staticInitials);
				processTypeConversions(node.children[3],null,returnedClass,staticInitials);
				return null;
			case Unknown	:
				return null;
			case UntypedFor	:
				final Class<?>	forVariableUntyped = processTypeConversions(node.children[0],null,returnedClass,staticInitials);
				
				processTypeConversions(node.children[1],forVariableUntyped,returnedClass,staticInitials);
				processTypeConversions(node.children[2],null,returnedClass,staticInitials);
				return null;
			case Variable 	:
				final VarDescriptor	varDesc = (VarDescriptor)node.cargo;
				
				for (SyntaxTreeNode item : node.children) {
					processTypeConversions(item,varDesc.getNameType(),returnedClass,staticInitials); 
				}
				return varDesc.getNameType();
			case Variables	:
				for (SyntaxTreeNode item : node.children) {
					processTypeConversions(item,null,returnedClass,staticInitials); 
				}
				return null;
//			case Vartype	:
//				break;
			case Until : case While :
				simpleWalkTypeConversionsNode(node, returnedClass, staticInitials);
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
							node.assign(new SyntaxTreeNode(node.row,node.col,SyntaxTreeNodeType.Empty,0,null,new SyntaxTreeNode[0]));
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
							node.assign(new SyntaxTreeNode(node.row,node.col,SyntaxTreeNodeType.Infinite,0,null,new SyntaxTreeNode[]{node.children[0]}));
						}
						else {
							node.assign(new SyntaxTreeNode(node.row,node.col,SyntaxTreeNodeType.Empty,0,null,new SyntaxTreeNode[0]));
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

	private static void simpleWalkTypeConversionsNode(final SyntaxTreeNode node, final Class<?> returnedClass, final List<SyntaxTreeNode> staticInitials) throws SyntaxException {
		if (node.cargo instanceof SyntaxTreeNode) {
			processTypeConversions((SyntaxTreeNode)node.cargo,boolean.class,returnedClass,staticInitials);
		}
		for (SyntaxTreeNode item : node.children) {
			processTypeConversions(item,null,returnedClass,staticInitials); 
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
						node.assignInt(node.row,node.col,longValue);
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
						node.assignInt(node.row,node.col,longValue);
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
						node.assignInt(node.row,node.col,longValue);
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
							node.assignReal(node.row,node.col,doubleValue);
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
							node.assignInt(node.row,node.col,longValue);
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
							node.assignReal(node.row,node.col,doubleValue);
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
							node.assignInt(node.row,node.col,longValue);
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
							node.assignStr(node.row,node.col,sb.toString().toCharArray());
						}
						return node;
					case MercCompiler.PRTY_COMPARISON:
						child = buildConstantValue(node.children[0]);
						operators = (LexemaSubtype[])node.cargo;
						switch (((LexemaSubtype[])node.cargo)[1]) {
							case EQ 	:
								node.assignBoolean(node.row,node.col,compareConstants(child,buildConstantValue(node.children[1])) == 0);
								break;
							case NE 	:
								node.assignBoolean(node.row,node.col,compareConstants(child,buildConstantValue(node.children[1])) != 0);
								break;
							case LT 	:
								node.assignBoolean(node.row,node.col,compareConstants(child,buildConstantValue(node.children[1])) < 0);
								break;
							case LE 	:
								node.assignBoolean(node.row,node.col,compareConstants(child,buildConstantValue(node.children[1])) <= 0);
								break;
							case GT 	:
								node.assignBoolean(node.row,node.col,compareConstants(child,buildConstantValue(node.children[1])) > 0);
								break;
							case GE 	:
								node.assignBoolean(node.row,node.col,compareConstants(child,buildConstantValue(node.children[1])) >= 0);
								break;
							case IS 	:
								return node;
							case LIKE	:
								node.assignBoolean(node.row,node.col,Pattern.matches(new String((char[])buildConstantValue(node.children[1]).cargo), new String((char[])child.cargo)));
								return node;
							case InList	:
								for (SyntaxTreeNode item : node.children[1].children) {
									if (item.getType() == SyntaxTreeNodeType.Range) {
										if (compareConstants(child,buildConstantValue(item.children[0])) >= 0 && compareConstants(child,buildConstantValue(item.children[1])) <= 0) {
											node.assignBoolean(node.row,node.col,true);
											return node;
										}
									}
									else if (compareConstants(child,buildConstantValue(item)) == 0) {
										node.assignBoolean(node.row,node.col,true);
										return node;
									}
								}
								node.assignBoolean(node.row,node.col,false);
								return node;
							default : throw new UnsupportedOperationException("Operation type ["+((LexemaSubtype[])node.cargo)[1]+"] is not supported yet");
						}
						return node;
					case MercCompiler.PRTY_AND		:
						longValue = buildConstantValue(node.children[0]).value;
						for (int index = 1; index < node.children.length && longValue != 0; index++) {	// Short scheme!
							longValue &= buildConstantValue(node.children[index]).value;
						}
						node.assignBoolean(node.row,node.col,longValue == 1);
						return node;
					case MercCompiler.PRTY_OR		:
						longValue = buildConstantValue(node.children[0]).value;
						for (int index = 1; index < node.children.length && longValue == 0; index++) {	// Short scheme!
							longValue |= buildConstantValue(node.children[index]).value;
						}
						node.assignBoolean(node.row,node.col,longValue == 1);
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

	static Class<?> convertValueTypeTo(final SyntaxTreeNode node, final Class<?> preferredClass, final List<SyntaxTreeNode> staticInitials) throws SyntaxException {
		switch (node.getType()) {
			case BoolConst 		:
				if (preferredClass == char[].class) {
					node.assignStr(node.row, node.col, node.value == 1 ? "true".toCharArray() : "false".toCharArray());
					return preferredClass;
				}
				else {
					throw new SyntaxException(node.row,node.col,"Boolean constant can be converted to ["+preferredClass.getCanonicalName()+"]");
				}
			case IntConst 		:
				if (preferredClass == double.class) {
					node.assignReal(node.row, node.col, Double.doubleToLongBits((double)node.value));
					return preferredClass;
				}
				else if (preferredClass == char[].class) {
					node.assignStr(node.row, node.col, String.valueOf(node.value).toCharArray());
					return preferredClass;
				}
				else {
					throw new SyntaxException(node.row,node.col,"Integer constant can be converted to ["+preferredClass.getCanonicalName()+"]");
				}
			case RealConst 		:
				if (preferredClass == long.class) {
					node.assignInt(node.row, node.col, (long)Double.longBitsToDouble(node.value));
					return preferredClass;
				}
				else if (preferredClass == char[].class) {
					node.assignStr(node.row, node.col, String.valueOf(Double.longBitsToDouble(node.value)).toCharArray());
					return preferredClass;
				}
				else {
					throw new SyntaxException(node.row,node.col,"Integer constant can be converted to ["+preferredClass.getCanonicalName()+"]");
				}
			case StrConst 		:
				if (preferredClass == long.class) {
					node.assignInt(node.row, node.col, Long.valueOf(new String((char[])node.cargo)));
					return preferredClass;
				}
				else if (preferredClass == double.class) {
					node.assignReal(node.row, node.col, Double.valueOf(new String((char[])node.cargo)));
					return preferredClass;
				}
				else {
					throw new SyntaxException(node.row,node.col,"Integer constant can be converted to ["+preferredClass.getCanonicalName()+"]");
				}
			case StandaloneName : case IndicedName : case InstanceField : case Call :
				final Class<?>	standaloneResolved = resolveType4Value((((VarDescriptor)node.cargo).getNameType()));
					
				insertValueGetter(node,standaloneResolved);
				if (standaloneResolved != preferredClass) {
					return processTypeConversions(node,preferredClass,null,staticInitials);
				}
				else {
					return preferredClass;
				}
			default : throw new UnsupportedOperationException("Node type ["+node.getType()+"] is not supported yet");
		}
	}

	private static Class<?> extractDominatingClass(final List<Class<?>> memberClasses) throws SyntaxException {
		Class<?>	result = memberClasses.get(0);
		
		for (Class<?> candidate : memberClasses) {
			if (RESOLVED_DOMINATOR.containsKey(result)) {
				if (RESOLVED_DOMINATOR.get(result).containsKey(candidate)) {
					result = RESOLVED_DOMINATOR.get(result).get(candidate);
				}
			}
			else {
				throw new SyntaxException(0,0,"Uncompatible operands in the expression");
			}
		}
		return result;
	}

	private static Class<?> resolveType4Value(final Class<?> sourceClass) {
		final Class<?>	converted = RESOLVED_4_VALUE.get(sourceClass);
		
		return converted != null ? converted : sourceClass;
	}

	private static void insertValueGetter(final SyntaxTreeNode node, final Class<?> preferredClass) {
		// TODO Auto-generated method stub
		if (RESOLVED_GETTER.containsKey(((VarDescriptor)node.cargo).getNameType())) {
			final SyntaxTreeNode	getter = new SyntaxTreeNode(node);
		}
	}
}
