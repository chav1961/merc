package chav1961.merc.lang.merc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import chav1961.merc.lang.merc.interfaces.VarDescriptor;
import chav1961.purelib.basic.exceptions.SyntaxException;

public class InternalUtils {
	private static final Map<Class<?>,Map<Class<?>,Class<?>>>		RESOLVED_DOMINATOR = new HashMap<>();
	static final Map<Class<?>,Class<?>>			RESOLVED_4_VALUE = new HashMap<>();
	
	static {
		RESOLVED_4_VALUE.put(BooleanKeeper.class,boolean.class);
		RESOLVED_4_VALUE.put(DoubleKeeper.class,double.class);
		RESOLVED_4_VALUE.put(LongKeeper.class,long.class);
		RESOLVED_4_VALUE.put(StringKeeper.class,char[].class);
		RESOLVED_4_VALUE.put(AreaKeeper.class,Area.class);
		RESOLVED_4_VALUE.put(PointKeeper.class,Point.class);
		RESOLVED_4_VALUE.put(SizeKeeper.class,Size.class);
		RESOLVED_4_VALUE.put(TrackKeeper.class,Track.class);
		
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
	}
	
	public enum SimplifiedType {
		LongType, DoubleType, StringType, BooleanType,
		PointType, AreaType, SizeType, TrackType,
		OtherType
	}
	
	public static SimplifiedType defineSimplifiedType(final Class<?> clazz) {
		if (clazz == null) {
			throw new NullPointerException("Class to define simplified type can't be null");
		}
		else if (long.class.isAssignableFrom(clazz) || Long.class.isAssignableFrom(clazz) || LongKeeper.class.isAssignableFrom(clazz)) {
			return SimplifiedType.LongType;
		}
		else if (double.class.isAssignableFrom(clazz) || Double.class.isAssignableFrom(clazz) || DoubleKeeper.class.isAssignableFrom(clazz)) {
			return SimplifiedType.DoubleType;
		}
		else if (char[].class.isAssignableFrom(clazz) || StringKeeper.class.isAssignableFrom(clazz)) {
			return SimplifiedType.StringType;
		}
		else if (boolean.class.isAssignableFrom(clazz) || Boolean.class.isAssignableFrom(clazz) || BooleanKeeper.class.isAssignableFrom(clazz)) {
			return SimplifiedType.BooleanType;
		}
		else if (Point.class.isAssignableFrom(clazz) || PointKeeper.class.isAssignableFrom(clazz)) {
			return SimplifiedType.PointType;
		}
		else if (Area.class.isAssignableFrom(clazz) || AreaKeeper.class.isAssignableFrom(clazz)) {
			return SimplifiedType.AreaType;
		}
		else if (Size.class.isAssignableFrom(clazz) || SizeKeeper.class.isAssignableFrom(clazz)) {
			return SimplifiedType.SizeType;
		}
		else if (Track.class.isAssignableFrom(clazz) || TrackKeeper.class.isAssignableFrom(clazz)) {
			return SimplifiedType.TrackType;
		}
		else {
			return SimplifiedType.OtherType;
		}
	}
	
	public static Class<?> resolveNodeType(final MercSyntaxTreeNode node) throws SyntaxException {
		if (node == null) {
			throw new NullPointerException("Node can't be null");
		}
		else {
			switch (node.getType()) {
				case AllocatedVariable	:
					return ((VarDescriptor)node.cargo).getNameTrueType();
				case Assign	:
					return resolveNodeType(node.children[0]);
				case BoolConst	:
					return boolean.class;
				case Call	:
					return ((VarDescriptor)node.cargo).getNameTrueType();
				case Conversion	:
					return ((VarDescriptor)node.cargo).getNameTrueType();
				case Function	:
					return resolveNodeType(((MercSyntaxTreeNode)node.cargo));
				case Header	:
					return void.class;
				case HeaderWithReturned	:
					return ((VarDescriptor)node.cargo).getNameTrueType();
				case IndicedName	:
					return ((VarDescriptor)node.cargo).getNameTrueType();
				case InstanceField	:
					return ((VarDescriptor)node.cargo).getNameTrueType();
				case IntConst	:
					return long.class;
				case List : case Range :
					final List<Class<?>>	content = new ArrayList<>();
					
					for (MercSyntaxTreeNode item : node.children) {
						content.add(resolveNodeType(item));
					}
					return extractDominatingType(content.toArray(new Class[content.size()]));
				case LocalName	:
					return ((VarDescriptor)node.cargo).getNameTrueType();
				case LongReturn	:
					return ((VarDescriptor)node.cargo).getNameTrueType();
				case Null	:
					return Object.class;
				case OrdinalBinary	:
					switch ((int)node.value) {
					}
					return void.class;
				case Pipe	:
					return void.class;
				case OrdinalUnary	:
					return ((VarDescriptor)node.cargo).getNameTrueType();
				case PredefinedName	:
					return ((VarDescriptor)node.cargo).getNameTrueType();
				case RealConst	:
					return double.class;
				case RefConst	:
					return ((VarDescriptor)node.cargo).getNameTrueType();
				case StandaloneName	:
					return ((VarDescriptor)node.cargo).getNameTrueType();
				case StrConst	:
					return char[].class;
				case Variable	:
					return ((VarDescriptor)node.cargo).getNameTrueType();
				case Break : case Brick : case Continue : case Empty : case Infinite :
				case Lock : case LongIf : case Print : case Program : case Sequence :
				case ShortIf : case ShortReturn : case TypedFor : case Unknown : 
				case Until : case UntypedFor : case While : case Variables :
					return void.class;
				case Vartype	:
					return void.class;
				default:
					throw new UnsupportedOperationException("Node type ["+node.getType()+"] is not supported yet");
			}
		}
	}

	public static Class<?> extractDominatingType(final Class<?>... memberClasses) throws SyntaxException {
		Class<?>	result = memberClasses[0];
		
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
	
	public static void toRValue(final MercSyntaxTreeNode source, final MercClassRepo repo) throws SyntaxException {
		final Class<?>	clazz = resolveNodeType(source);
		
		if (RESOLVED_4_VALUE.containsKey(clazz)) {
			insertValueGetter(source,repo);
		}
	}
	
	public static Class<?> insertValueGetter(final MercSyntaxTreeNode node, final MercClassRepo repo) throws SyntaxException {
		final Class<?>		varType = ((VarDescriptor)node.cargo).getNameType();
		final VarDescriptor	desc = repo.byClass(varType);
		final long			getValueId = repo.getNames().seekName("getValue");
		
		for (VarDescriptor item : desc.contentFields()) {
			if (item.getNameId() == getValueId) {
				final MercSyntaxTreeNode	variable = new MercSyntaxTreeNode(node);

				node.assignCall(node.row,node.col,getValueId,item,variable);
				return ((VarDescriptor)node.cargo).getNameType(); 
			}
		}
		throw new SyntaxException(node.row,node.col,"Variable doesn't contain getValue() method");
	}
	
	public static Class<?> subtype2Class(final LexemaSubtype nameType) {
		switch (nameType) {
			case Int	: return LongKeeper.class;
			case Real	: return DoubleKeeper.class;
			case Str	: return StringKeeper.class;
			case Bool	: return BooleanKeeper.class;
			case Point	: return PointKeeper.class;
			case Area	: return AreaKeeper.class;
			case Track	: return TrackKeeper.class;
			case Size	: return SizeKeeper.class;
			default : throw new UnsupportedOperationException("Subtype ["+nameType+"] is not supported yet"); 
		}
	}

	public static Class<?> resolveType4Value(final Class<?> sourceClass) {
		final Class<?>	converted = InternalUtils.RESOLVED_4_VALUE.get(sourceClass);
		
		return converted != null ? converted : sourceClass;
	}
}
