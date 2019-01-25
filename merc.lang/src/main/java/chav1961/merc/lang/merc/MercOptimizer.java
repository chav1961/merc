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
import chav1961.merc.api.TrackWalkCallback;
import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.api.interfaces.front.World;
import chav1961.merc.lang.merc.SyntaxTreeNode.SyntaxTreeNodeType;
import chav1961.merc.lang.merc.interfaces.VarDescriptor;
import chav1961.purelib.basic.exceptions.SyntaxException;

class MercOptimizer {
	private static final Set<Class<?>>					POINT_CLASSES = new HashSet<>();
	private static final Map<LexemaSubtype,Class<?>>	PREDEFINED_CLASSES = new HashMap<>();
	private static final Map<Class<?>,Class<?>>			RESOLVED_4_VALUE = new HashMap<>();
	private static final Map<Class<?>,LexemaSubtype>				CONVERSION_TYPE = new HashMap<>();
	private static final Map<Class<?>,Map<Class<?>,Class<?>>>		RESOLVED_DOMINATOR = new HashMap<>();
	private static final Map<Class<?>,TypeClassification>			ORDINAL_TYPE_CLASSIFICATOR = new HashMap<>();
	private static final Set<Class<?>>								ORDINAL_TYPE_GETTER = new HashSet<>();

	enum TypeClassification {
		IntType, RealType, StrType, BoolType,
		IntTypeKeeper, RealTypeKeeper, StrTypeKeeper, BoolTypeKeeper,
		AreaType, PointType, SizeType, TrackType,
		AreaTypeKeeper, PointTypeKeeper, SizeTypeKeeper, TrackTypeKeeper,
		OtherwiseType
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
		
		CONVERSION_TYPE.put(long.class,LexemaSubtype.Int);
		CONVERSION_TYPE.put(double.class,LexemaSubtype.Real);
		CONVERSION_TYPE.put(char[].class,LexemaSubtype.Str);
		CONVERSION_TYPE.put(boolean.class,LexemaSubtype.Bool);
		CONVERSION_TYPE.put(Point.class,LexemaSubtype.Point);
		CONVERSION_TYPE.put(Area.class,LexemaSubtype.Area);
		CONVERSION_TYPE.put(Size.class,LexemaSubtype.Size);
		CONVERSION_TYPE.put(Track.class,LexemaSubtype.Track);
		
		Map<Class<?>,Class<?>>	target = new HashMap<>();
		
		target.put(long.class,long.class);
		target.put(LongKeeper.class,long.class);
		target.put(double.class,double.class);
		target.put(DoubleKeeper.class,double.class);
		target.put(char[].class,char[].class);
		target.put(StringKeeper.class,char[].class);
		RESOLVED_DOMINATOR.put(long.class,target);

		target = new HashMap<>();
		target.put(long.class,double.class);
		target.put(LongKeeper.class,double.class);
		target.put(double.class,double.class);
		target.put(DoubleKeeper.class,double.class);
		target.put(char[].class,char[].class);
		target.put(StringKeeper.class,char[].class);
		RESOLVED_DOMINATOR.put(double.class,target);

		target = new HashMap<>();
		target.put(long.class,char[].class);
		target.put(LongKeeper.class,char[].class);
		target.put(double.class,char[].class);
		target.put(DoubleKeeper.class,char[].class);
		target.put(char[].class,char[].class);
		target.put(StringKeeper.class,char[].class);
		target.put(boolean.class,char[].class);
		target.put(BooleanKeeper.class,char[].class);
		target.put(Point.class,char[].class);
		target.put(PointKeeper.class,char[].class);
		target.put(Area.class,char[].class);
		target.put(AreaKeeper.class,char[].class);
		target.put(Size.class,char[].class);
		target.put(SizeKeeper.class,char[].class);
		target.put(Track.class,char[].class);
		target.put(TrackKeeper.class,char[].class);
		RESOLVED_DOMINATOR.put(char[].class,target);

		target = new HashMap<>();
		target.put(char[].class,char[].class);
		target.put(StringKeeper.class,char[].class);
		target.put(boolean.class,boolean.class);
		target.put(BooleanKeeper.class,boolean.class);
		RESOLVED_DOMINATOR.put(boolean.class,target);
		
		target = new HashMap<>();
		target.put(char[].class,char[].class);
		target.put(StringKeeper.class,char[].class);
		target.put(Point.class,Track.class);
		target.put(PointKeeper.class,Track.class);
		target.put(Area.class,Track.class);
		target.put(AreaKeeper.class,Track.class);
		target.put(Track.class,Track.class);
		target.put(TrackKeeper.class,Track.class);
		RESOLVED_DOMINATOR.put(Point.class,target);

		target = new HashMap<>();
		target.put(char[].class,char[].class);
		target.put(StringKeeper.class,char[].class);
		target.put(Point.class,Track.class);
		target.put(PointKeeper.class,Track.class);
		target.put(Area.class,Track.class);
		target.put(AreaKeeper.class,Track.class);
		target.put(Track.class,Track.class);
		target.put(TrackKeeper.class,Track.class);
		RESOLVED_DOMINATOR.put(Area.class,target);

		target = new HashMap<>();
		target.put(char[].class,char[].class);
		target.put(StringKeeper.class,char[].class);
		RESOLVED_DOMINATOR.put(Size.class,target);
		
		target = new HashMap<>();
		target.put(char[].class,char[].class);
		target.put(StringKeeper.class,char[].class);
		target.put(Point.class,Track.class);
		target.put(PointKeeper.class,Track.class);
		target.put(Area.class,Track.class);
		target.put(AreaKeeper.class,Track.class);
		target.put(Track.class,Track.class);
		target.put(TrackKeeper.class,Track.class);
		RESOLVED_DOMINATOR.put(Track.class,target);
		
		ORDINAL_TYPE_CLASSIFICATOR.put(long.class, TypeClassification.IntType);
		ORDINAL_TYPE_CLASSIFICATOR.put(double.class, TypeClassification.RealType);
		ORDINAL_TYPE_CLASSIFICATOR.put(char[].class, TypeClassification.StrType);
		ORDINAL_TYPE_CLASSIFICATOR.put(boolean.class, TypeClassification.BoolType);
		ORDINAL_TYPE_CLASSIFICATOR.put(LongKeeper.class, TypeClassification.IntTypeKeeper);
		ORDINAL_TYPE_CLASSIFICATOR.put(DoubleKeeper.class, TypeClassification.RealTypeKeeper);
		ORDINAL_TYPE_CLASSIFICATOR.put(StringKeeper.class, TypeClassification.StrTypeKeeper);
		ORDINAL_TYPE_CLASSIFICATOR.put(BooleanKeeper.class, TypeClassification.BoolTypeKeeper);
		
	}
	
	static Class<?> processTypeConversions(final SyntaxTreeNode node, final Class<?> preferredClass, final Class<?> returnedClass, final MercClassRepo repo, List<SyntaxTreeNode> staticInitials) throws SyntaxException {
 		// TODO:
		switch (node.getType()) {
			case AllocatedVariable:
				break;
			case Assign:
				final Class<?>	assignLeft = processTypeConversions(node.children[0],null,returnedClass,repo,staticInitials);
				
				processTypeConversions(node.children[1],assignLeft,returnedClass,repo,staticInitials);
				if (preferredClass == null) {
					return assignLeft;
				}
				else if (preferredClass.isAssignableFrom(assignLeft)) {
					return preferredClass;
				}
				else {
					return convertValueTypeTo(node,preferredClass,repo,staticInitials);
				}
			case PostDec : case PostInc : case PreDec : case PreInc :
				if (preferredClass == null) {
					final Class<?>	resolved = processTypeConversions(node.children[0],null,returnedClass,repo,staticInitials);
					
					if (resolved != LongKeeper.class && resolved != DoubleKeeper.class) {
						throw new SyntaxException(node.row,node.col,"Non left-part operand in the operation"); 
					}
					else {
						return resolveType4Value(resolved);
					}
				}
				else {
					Class<?>	resolved = processTypeConversions(node.children[0],null,returnedClass,repo,staticInitials);
					
					if (resolved != LongKeeper.class && resolved != DoubleKeeper.class) {
						throw new SyntaxException(node.row,node.col,"Non left-part operand in the operation"); 
					}
					else {
						resolved = resolveType4Value(resolved);
						
						if (resolved != preferredClass) {
							insertConversion(node,resolved,preferredClass);
						}
						return preferredClass;
					}
				}
			case BitInv : 
				if (preferredClass == null) {
					final Class<?>	inner = processTypeConversions(node.children[0],long.class,returnedClass,repo,staticInitials);
					
					if (inner != long.class) {
						insertConversion(node,inner,long.class);
					}
					return long.class;
				}
				else {
					final Class<?>	inner = processTypeConversions(node.children[0],long.class,returnedClass,repo,staticInitials);
					
					if (inner != preferredClass) {
						insertConversion(node,inner,preferredClass);
					}
					return preferredClass;
				}
			case BoolConst:
				if (preferredClass == null) {
					return boolean.class;
				}
				else if (preferredClass.isAssignableFrom(boolean.class)) {
					return preferredClass;
				}
				else {
					return convertValueTypeTo(node,preferredClass,repo,staticInitials);
				}
			case Brick:
				for (SyntaxTreeNode item : node.children) {
					processTypeConversions(item,null,null,repo,staticInitials); 
				}
				return null;
			case Function:
				final Class<?>	returned = resolveType4Value((Class<?>)((SyntaxTreeNode)((SyntaxTreeNode)node.cargo).cargo).cargo);
				
				for (SyntaxTreeNode item : node.children) {
					processTypeConversions(item,null,returned,repo,staticInitials); 
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
					return convertValueTypeTo(node.children[0],preferredClass,repo,staticInitials);
				}
			case Empty:
				return null;
			case Header:
				return null;
			case HeaderWithReturned:
				return null;
			case IndicedName:
				for (SyntaxTreeNode item : node.children) {
					processTypeConversions(item,long.class,returnedClass,repo,staticInitials); 
				}
				if (preferredClass == null) {
					return ((VarDescriptor)node.cargo).getNameType();
				}
				else if (preferredClass.isAssignableFrom(((VarDescriptor)node.cargo).getNameType())) {
					return preferredClass;
				}
				else {
					return convertValueTypeTo(node,preferredClass,repo,staticInitials);
				}
			case Infinite:
				for (SyntaxTreeNode item : node.children) {
					processTypeConversions(item,null,returnedClass,repo,staticInitials); 
				}
				return null;
			case InstanceField:
				if (preferredClass == null) {
					return ((VarDescriptor)node.cargo).getNameType();
				}
				else if (preferredClass.isAssignableFrom(((VarDescriptor)node.cargo).getNameType())) {
					return preferredClass;
				}
				else {
					return convertValueTypeTo(node,preferredClass,repo,staticInitials);
				}
			case IntConst :
				if (preferredClass == null) {
					return long.class;
				}
				else if (preferredClass.isAssignableFrom(long.class)) {
					return preferredClass;
				}
				else {
					return convertValueTypeTo(node,preferredClass,repo,staticInitials);
				}
			case List:
				if (preferredClass == null) {
					final List<Class<?>>	memberClasses = new ArrayList<>(); 
							
					for (SyntaxTreeNode item : node.children) {
						memberClasses.add(processTypeConversions(item,null,returnedClass,repo,staticInitials)); 
					}
					final Class<?>			dominated = extractDominatingClass(memberClasses);
					
					for (SyntaxTreeNode item : node.children) {
						memberClasses.add(processTypeConversions(item,dominated,returnedClass,repo,staticInitials)); 
					}
					return dominated;
				}
				else {
					for (SyntaxTreeNode item : node.children) {
						processTypeConversions(item,preferredClass,returnedClass,repo,staticInitials); 
					}
					return preferredClass;
				}
			case Lock:
				final Class<?>			dominated = processTypeConversions(((SyntaxTreeNode)node.cargo),null,returnedClass,repo,staticInitials);
				
				if (POINT_CLASSES.contains(dominated)) {
					processTypeConversions(((SyntaxTreeNode)node.cargo),dominated,returnedClass,repo,staticInitials);
				}
				else {
					throw new UnsupportedOperationException("Unsupported conversion from [long] to ["+preferredClass.getCanonicalName()+"]");
				}
				
				for (SyntaxTreeNode item : node.children) {
					processTypeConversions(item,null,returnedClass,repo,staticInitials); 
				}
				return null;
			case LongReturn:
				return processTypeConversions(node.children[0],returnedClass,returnedClass,repo,staticInitials); 
			case Negation:
				if (preferredClass == null) {
					final Class<?>	inner = processTypeConversions(node.children[0],null,returnedClass,repo,staticInitials);
					
					if (RESOLVED_4_VALUE.containsKey(inner)) {
						return processTypeConversions(node.children[0],RESOLVED_4_VALUE.get(inner),returnedClass,repo,staticInitials);
					}
					else {
						return inner;
					}
				}
				else {
					final Class<?>	resolved = resolveType4Value(processTypeConversions(node.children[0],null,returnedClass,repo,staticInitials));
					final Class<?>	inner = processTypeConversions(node.children[0],resolved,returnedClass,repo,staticInitials);
					
					if (inner != preferredClass) {
						insertConversion(node,inner,preferredClass);
					}
					return preferredClass;
				}
			case Not:
				if (preferredClass == null) {
					final Class<?>	inner = processTypeConversions(node.children[0],boolean.class,returnedClass,repo,staticInitials);
					
					if (RESOLVED_4_VALUE.containsKey(inner)) {
						return processTypeConversions(node.children[0],RESOLVED_4_VALUE.get(inner),returnedClass,repo,staticInitials);
					}
					else {
						return inner;
					}
				}
				else {
					final Class<?>	inner = processTypeConversions(node.children[0],boolean.class,returnedClass,repo,staticInitials);
					
					if (inner != preferredClass) {
						insertConversion(node,inner,preferredClass);
					}
					return preferredClass;
				}
			case Null:
				return preferredClass;
			case OrdinalBinary:
				if (preferredClass == null) {
					switch ((int)node.value) {
						case MercCompiler.PRTY_BITAND : case MercCompiler.PRTY_BITORXOR : case MercCompiler.PRTY_SHIFT :
							for (SyntaxTreeNode item : node.children) {
								processTypeConversions(item,long.class,returnedClass,repo,staticInitials); 
							}
							return long.class;
						case MercCompiler.PRTY_MUL : case MercCompiler.PRTY_ADD	:
							final List<Class<?>>	mulClasses = new ArrayList<>();
							
							for (SyntaxTreeNode item : node.children) {
								mulClasses.add(resolveType4Value(processTypeConversions(item,null,returnedClass,repo,staticInitials)));
							}
							final Class<?>			mulRequired = extractDominatingClass(mulClasses);
							
							for (SyntaxTreeNode item : node.children) {
								processTypeConversions(item,mulRequired,returnedClass,repo,staticInitials); 
							}
							return mulRequired;
						case MercCompiler.PRTY_COMPARISON	:
							final Class<?>	leftClass = resolveType4Value(processTypeConversions(node.children[0],null,returnedClass,repo,staticInitials));

							if ((leftClass == Point.class || leftClass == Area.class) && ((LexemaSubtype[])node.cargo)[1] == LexemaSubtype.InList) {
								processTypeConversions(node.children[1],Track.class,returnedClass,repo,staticInitials);
							}
							else {
								processTypeConversions(node.children[1],leftClass,returnedClass,repo,staticInitials);
							}
							return boolean.class;
						case MercCompiler.PRTY_AND : case MercCompiler.PRTY_OR :
							for (SyntaxTreeNode item : node.children) {
								processTypeConversions(item,boolean.class,returnedClass,repo,staticInitials); 
							}
							return boolean.class;
						default : throw new UnsupportedOperationException("Prty=["+node.value+"] is not supported yet");
					}
				}
				else {
					switch ((int)node.value) {
						case MercCompiler.PRTY_BITAND : case MercCompiler.PRTY_BITORXOR : case MercCompiler.PRTY_SHIFT :
						case MercCompiler.PRTY_MUL : case MercCompiler.PRTY_ADD	:
							for (SyntaxTreeNode item : node.children) {
								processTypeConversions(item,long.class,returnedClass,repo,staticInitials); 
							}
							if (preferredClass != long.class) {
								insertConversion(node,long.class,preferredClass);
							}
							return preferredClass;
						case MercCompiler.PRTY_COMPARISON	:
							Class<?>	leftClass = processTypeConversions(node.children[0],null,returnedClass,repo,staticInitials);
							
							if (RESOLVED_4_VALUE.containsKey(leftClass)) {
								leftClass = insertValueGetter(node.children[0],leftClass,repo);
							}	
							if ((leftClass == Point.class || leftClass == Area.class) && ((LexemaSubtype[])node.cargo)[1] == LexemaSubtype.InList) {
								processTypeConversions(node.children[1],Track.class,returnedClass,repo,staticInitials);
							}
							else {
								processTypeConversions(node.children[1],leftClass,returnedClass,repo,staticInitials);
							}
							if (preferredClass != boolean.class) {
								insertConversion(node,boolean.class,preferredClass);
							}
							return preferredClass;
						case MercCompiler.PRTY_AND : case MercCompiler.PRTY_OR :
							for (SyntaxTreeNode item : node.children) {
								processTypeConversions(item,boolean.class,returnedClass,repo,staticInitials); 
							}
							if (preferredClass != boolean.class) {
								insertConversion(node,boolean.class,preferredClass);
							}
							return preferredClass;
						default : throw new UnsupportedOperationException("Prty=["+node.value+"] is not supported yet");
					}
				}
			case Pipe:
				for (SyntaxTreeNode item : ((SyntaxTreeNode[])node.cargo)) {
					processTypeConversions(item,null,returnedClass,repo,staticInitials); 
				}
				for (SyntaxTreeNode item : node.children) {
					processTypeConversions(item,null,returnedClass,repo,staticInitials); 
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
					processTypeConversions(item,null,returnedClass,repo,staticInitials); 
				}
				return null;
			case Program:
				processTypeConversions(((SyntaxTreeNode)node.cargo),null,returnedClass,repo,staticInitials); 
				for (SyntaxTreeNode item : node.children) {
					processTypeConversions(item,null,returnedClass,repo,staticInitials); 
				}
				return null;
			case Range:
				if (preferredClass == null) {
					Class<?>	rangeLeft = processTypeConversions(node.children[0],null,returnedClass,repo,staticInitials);
					
					processTypeConversions(node.children[1],rangeLeft,returnedClass,repo,staticInitials);
					return rangeLeft;
				}
				else {
					for (SyntaxTreeNode item : node.children) {
						processTypeConversions(item,preferredClass,returnedClass,repo,staticInitials); 
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
					return convertValueTypeTo(node,preferredClass,repo,staticInitials);
				}
			case RefConst:
				break;
			case Sequence:
				for (SyntaxTreeNode item : node.children) {
					processTypeConversions(item,null,returnedClass,repo,staticInitials); 
				}
				return null;
			case LongIf : case ShortIf :
				processTypeConversions((SyntaxTreeNode)node.cargo,boolean.class,returnedClass,repo,staticInitials);
				for (SyntaxTreeNode item : node.children) {
					processTypeConversions(item,null,returnedClass,repo,staticInitials); 
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
					return convertValueTypeTo(node,preferredClass,repo,staticInitials);
				}
			case StrConst	:
				if (preferredClass == null) {
					return char[].class;
				}
				else if (preferredClass.isAssignableFrom(char[].class)) {
					return preferredClass;
				}
				else {
					return convertValueTypeTo(node,preferredClass,repo,staticInitials);
				}
			case TypedFor:
				final Class<?>	forVariableTyped = processTypeConversions(node.children[1],null,returnedClass,repo,staticInitials);
				
				processTypeConversions(node.children[2],forVariableTyped,returnedClass,repo,staticInitials);
				processTypeConversions(node.children[3],null,returnedClass,repo,staticInitials);
				return null;
			case Unknown	:
				return null;
			case UntypedFor	:
				final Class<?>	forVariableUntyped = processTypeConversions(node.children[0],null,returnedClass,repo,staticInitials);
				
				processTypeConversions(node.children[1],forVariableUntyped,returnedClass,repo,staticInitials);
				processTypeConversions(node.children[2],null,returnedClass,repo,staticInitials);
				return null;
			case Variable 	:
				final VarDescriptor	varDesc = (VarDescriptor)node.cargo;
				
				for (SyntaxTreeNode item : node.children) {
					processTypeConversions(item,varDesc.getNameType(),returnedClass,repo,staticInitials); 
				}
				return varDesc.getNameType();
			case Variables	:
				simpleWalkTypeConversionsNode(node, returnedClass, repo, staticInitials);
				return null;
//			case Vartype	:
//				break;
			case Until : case While :
				simpleWalkTypeConversionsNode(node, returnedClass, repo, staticInitials);
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

	private static void simpleWalkTypeConversionsNode(final SyntaxTreeNode node, final Class<?> returnedClass, final MercClassRepo repo, final List<SyntaxTreeNode> staticInitials) throws SyntaxException {
		if (node.cargo instanceof SyntaxTreeNode) {
			processTypeConversions((SyntaxTreeNode)node.cargo,boolean.class,returnedClass,repo,staticInitials);
		}
		for (SyntaxTreeNode item : node.children) {
			processTypeConversions(item,null,returnedClass,repo,staticInitials); 
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

	static Class<?> convertValueTypeTo(final SyntaxTreeNode node, final Class<?> preferredClass, final MercClassRepo repo, final List<SyntaxTreeNode> staticInitials) throws SyntaxException {
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
					node.assignReal(node.row, node.col, (double)node.value);
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
				else if (preferredClass == boolean.class) {
					node.assignBoolean(node.row, node.col, Boolean.valueOf(new String((char[])node.cargo)));
					return preferredClass;
				}
				else {
					throw new SyntaxException(node.row,node.col,"Integer constant can be converted to ["+preferredClass.getCanonicalName()+"]");
				}
			case StandaloneName : case IndicedName : case InstanceField : case Call :
				if (preferredClass == null) {
					return ((VarDescriptor)node.cargo).getNameType();
				}
				else {
					Class<?>	standalone = (((VarDescriptor)node.cargo).getNameType());
					
					if (RESOLVED_4_VALUE.containsKey(standalone)) {
						standalone = insertValueGetter(node,standalone,repo);
					}
					if (standalone != preferredClass) {
						insertConversion(node,standalone,preferredClass);
					}
					return preferredClass;
				}
			default : throw new UnsupportedOperationException("Node type ["+node.getType()+"] is not supported yet");
		}
	}

	private static Class<?> insertConversion(final SyntaxTreeNode node, final Class<?> sourceClass, final Class<?> preferredClass) {
		if (CONVERSION_TYPE.containsKey(preferredClass)) {
			final SyntaxTreeNode	inner = new SyntaxTreeNode(node);
			final SyntaxTreeNode	innerList = new SyntaxTreeNode();
			
			innerList.assignList(node.row,node.col,inner);							
			node.assignConversion(node.row,node.col,CONVERSION_TYPE.get(preferredClass),innerList);
			return preferredClass;
		}
		else {
			throw new UnsupportedOperationException("Node type ["+node.getType()+"] is not supported yet");
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

	private static Class<?> insertValueGetter(final SyntaxTreeNode node, final Class<?> preferredClass, final MercClassRepo repo) throws SyntaxException {
		final Class<?>		varType = ((VarDescriptor)node.cargo).getNameType();
		final VarDescriptor	desc = repo.byClass(varType);
		final long			getValueId = repo.getNames().seekName("getValue");
		
		for (VarDescriptor item : desc.contentFields()) {
			if (item.getNameId() == getValueId) {
				final SyntaxTreeNode	getter = new SyntaxTreeNode();
				final SyntaxTreeNode	callMethod = new SyntaxTreeNode();
				
				callMethod.assignVarDefinition(node.row, node.col, getValueId, item);
				getter.assignField(node.row,node.col,(VarDescriptor)callMethod.cargo,node,callMethod);
				node.assign(getter);
				return ((VarDescriptor)callMethod.cargo).getNameType(); 
			}
		}
		throw new SyntaxException(node.row,node.col,"Variable doesn't contain getValue() method");
	}
}
