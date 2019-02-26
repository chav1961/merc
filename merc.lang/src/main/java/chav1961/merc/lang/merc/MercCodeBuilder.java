package chav1961.merc.lang.merc;



import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
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
import chav1961.merc.api.interfaces.front.AvailableForTrack;
import chav1961.merc.api.interfaces.front.Entity;
import chav1961.merc.lang.merc.interfaces.CharDataOutput;
import chav1961.merc.lang.merc.interfaces.VarDescriptor;
import chav1961.purelib.basic.Utils;
import chav1961.purelib.basic.exceptions.PreparationException;
import chav1961.purelib.basic.exceptions.SyntaxException;
import chav1961.purelib.basic.interfaces.SyntaxTreeInterface;
import chav1961.purelib.cdb.SyntaxNodeUtils;
import chav1961.purelib.enumerations.ContinueMode;
import chav1961.purelib.enumerations.NodeEnterMode;
import chav1961.purelib.streams.char2byte.asm.CompilerUtils;

class MercCodeBuilder {
	private static final Method			PRODUCE_AREA_KEEPER;
	private static final Method			PRODUCE_BOOLEAN_KEEPER;
	private static final Method			PRODUCE_DOUBLE_KEEPER;
	private static final Method			PRODUCE_LONG_KEEPER;
	private static final Method			PRODUCE_POINT_KEEPER;
	private static final Method			PRODUCE_SIZE_KEEPER;
	private static final Method			PRODUCE_STRING_KEEPER;
	private static final Method			PRODUCE_TRACK_KEEPER;

	private static final Method			PRODUCE_LONG_INLIST;
	private static final Method			PRODUCE_DOUBLE_INLIST;
	private static final Method			PRODUCE_STRING_INLIST;
	private static final Method			PRODUCE_BOOLEAN_INLIST;
	private static final Method			PRODUCE_POINT_INLIST;
	private static final Method			PRODUCE_OBJECT_INLIST;

	private static final Method			PRODUCE_LONG_INCDEC;
	private static final Method			PRODUCE_DOUBLE_INCDEC;
	private static final Method			PRODUCE_CHAR_TRUNC;
	private static final Method			PRODUCE_CHAR_COMPARE;
	private static final Method			PRODUCE_CHAR_LIKE;
	
	private static final Method			AREA_KEEPER_SETVALUE;
	private static final Method			BOOLEAN_KEEPER_SETVALUE;
	private static final Method			DOUBLE_KEEPER_SETVALUE;
	private static final Method			LONG_KEEPER_SETVALUE;
	private static final Method			POINT_KEEPER_SETVALUE;
	private static final Method			SIZE_KEEPER_SETVALUE;
	private static final Method			STRING_KEEPER_SETVALUE;
	private static final Method			TRACK_KEEPER_SETVALUE;

	private static final Constructor<?>	AREA_CONSTRUCTOR_ENTITY;
	private static final Constructor<?>	AREA_CONSTRUCTOR_IIII;
	private static final Constructor<?>	AREA_CONSTRUCTOR_POINTPOINT;
	private static final Constructor<?>	AREA_CONSTRUCTOR_POINTSIZE;

	private static final Constructor<?>	POINT_CONSTRUCTOR_ENTITY;
	private static final Constructor<?>	POINT_CONSTRUCTOR_II;
	
	private static final Constructor<?>	SIZE_CONSTRUCTOR_ENTITY;
	private static final Constructor<?>	SIZE_CONSTRUCTOR_II;

	private static final Constructor<?>	TRACK_CONSTRUCTOR_II;
	private static final Constructor<?>	TRACK_CONSTRUCTOR_IIII;
	private static final Constructor<?>	TRACK_CONSTRUCTOR_POINTSIZE;
	private static final Constructor<?>	TRACK_CONSTRUCTOR_LIST;

	static {
		try{PRODUCE_AREA_KEEPER = BasicMercProgram.class.getDeclaredMethod("_newAreaKeeper_");
			PRODUCE_BOOLEAN_KEEPER = BasicMercProgram.class.getDeclaredMethod("_newBooleanKeeper_");
			PRODUCE_DOUBLE_KEEPER = BasicMercProgram.class.getDeclaredMethod("_newDoubleKeeper_");
			PRODUCE_LONG_KEEPER = BasicMercProgram.class.getDeclaredMethod("_newLongKeeper_");
			PRODUCE_POINT_KEEPER = BasicMercProgram.class.getDeclaredMethod("_newPointKeeper_");
			PRODUCE_SIZE_KEEPER = BasicMercProgram.class.getDeclaredMethod("_newSizeKeeper_");
			PRODUCE_STRING_KEEPER = BasicMercProgram.class.getDeclaredMethod("_newStringKeeper_");
			PRODUCE_TRACK_KEEPER = BasicMercProgram.class.getDeclaredMethod("_newTrackKeeper_");

			PRODUCE_LONG_INLIST = BasicMercProgram.class.getDeclaredMethod("_inList_",long.class,long[].class);
			PRODUCE_DOUBLE_INLIST = BasicMercProgram.class.getDeclaredMethod("_inList_",double.class,double[].class);
			PRODUCE_STRING_INLIST = BasicMercProgram.class.getDeclaredMethod("_inList_",char[].class,char[][].class);
			PRODUCE_BOOLEAN_INLIST = BasicMercProgram.class.getDeclaredMethod("_inList_",boolean.class,boolean[].class);
			PRODUCE_POINT_INLIST = BasicMercProgram.class.getDeclaredMethod("_inList_",Point.class,Track[].class);
			PRODUCE_OBJECT_INLIST = BasicMercProgram.class.getDeclaredMethod("_inList_",Object.class,Object[].class);

			PRODUCE_LONG_INCDEC = BasicMercProgram.class.getDeclaredMethod("_incDec_",LongKeeper.class,int.class);
			PRODUCE_DOUBLE_INCDEC = BasicMercProgram.class.getDeclaredMethod("_incDec_",DoubleKeeper.class,int.class);
			PRODUCE_CHAR_TRUNC = BasicMercProgram.class.getDeclaredMethod("_trunc_",char[].class);
			PRODUCE_CHAR_COMPARE = BasicMercProgram.class.getDeclaredMethod("_compare_",char[].class,char[].class);
			PRODUCE_CHAR_LIKE = BasicMercProgram.class.getDeclaredMethod("_like_",char[].class,char[].class);
			
			AREA_KEEPER_SETVALUE = AreaKeeper.class.getMethod("setValue",Area.class);
			BOOLEAN_KEEPER_SETVALUE = BooleanKeeper.class.getMethod("setValue",boolean.class);
			DOUBLE_KEEPER_SETVALUE = DoubleKeeper.class.getMethod("setValue",double.class);
			LONG_KEEPER_SETVALUE = LongKeeper.class.getMethod("setValue",long.class);
			POINT_KEEPER_SETVALUE = PointKeeper.class.getMethod("setValue",Point.class);
			SIZE_KEEPER_SETVALUE = SizeKeeper.class.getMethod("setValue",Size.class);
			STRING_KEEPER_SETVALUE = StringKeeper.class.getMethod("setValue",char[].class);
			TRACK_KEEPER_SETVALUE = TrackKeeper.class.getMethod("setValue",Track.class);
			
			AREA_CONSTRUCTOR_ENTITY = Area.class.getConstructor(Entity.class);
			AREA_CONSTRUCTOR_IIII = Area.class.getConstructor(int.class,int.class,int.class,int.class);
			AREA_CONSTRUCTOR_POINTPOINT = Area.class.getConstructor(Point.class,Point.class);
			AREA_CONSTRUCTOR_POINTSIZE = Area.class.getConstructor(Point.class,Size.class);
			
			POINT_CONSTRUCTOR_ENTITY = Point.class.getConstructor(Entity.class);
			POINT_CONSTRUCTOR_II = Point.class.getConstructor(int.class,int.class);

			SIZE_CONSTRUCTOR_ENTITY = Size.class.getConstructor(Entity.class);
			SIZE_CONSTRUCTOR_II = Size.class.getConstructor(int.class,int.class);

			TRACK_CONSTRUCTOR_II = Track.class.getConstructor(int.class,int.class);
			TRACK_CONSTRUCTOR_IIII = Track.class.getConstructor(int.class,int.class,int.class,int.class);
			TRACK_CONSTRUCTOR_POINTSIZE = Track.class.getConstructor(Point.class,Size.class);
			TRACK_CONSTRUCTOR_LIST = Track.class.getConstructor(AvailableForTrack[].class);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new PreparationException("Class ["+MercCodeBuilder.class+"] static initialization failed: "+e.getLocalizedMessage(),e);
		}
	}
	
	
	static void printHead(final MercSyntaxTreeNode node, final SyntaxTreeInterface<?> names, final MercClassRepo classes, final MercNameRepo vars, final CharDataOutput writer) throws IOException {
		// TODO Auto-generated method stub
		writer.writeln(" writePrologue");
	}

	static void printFields(final MercSyntaxTreeNode node, final SyntaxTreeInterface<?> names, final MercClassRepo classes, final MercNameRepo vars, final CharDataOutput writer) throws IOException {
		node.walk((mode,entity)->{
			if (mode == NodeEnterMode.ENTER && entity.getType() == MercSyntaxTreeNodeType.Variable) {
				try{writer.write(names.getName(entity.value)).write(" .field ").write(((VarDescriptor)entity.cargo).getNameType().getCanonicalName());
					if (((VarDescriptor)entity.cargo).isArray()) {
						for (int index = 0, maxIndex = ((VarDescriptor)entity.cargo).howManyDimensions(); index < maxIndex; index++) {
							writer.write("[]");
						}
					}
					writer.writeln();
				} catch (IOException e) {
					return ContinueMode.STOP;
				}
			}
			return ContinueMode.CONTINUE;
		});
	}

	static void printConstructor(final MercSyntaxTreeNode root, final SyntaxTreeInterface<?> names, final MercClassRepo classes, final MercNameRepo vars, final CharDataOutput writer) throws IOException {
		writer.writeln(" writeConstructor");
	}
	
	static void printFieldInitials(final MercSyntaxTreeNode node, final SyntaxTreeInterface<?> names, final MercClassRepo classes, final MercNameRepo vars, final CharDataOutput writer) throws IOException {
		node.walk((mode,entity)->{
			if (mode == NodeEnterMode.ENTER && entity.getType() == MercSyntaxTreeNodeType.Variable) {
				try{writer.writeln(" aload this");			
					switch (InternalUtils.defineSimplifiedType(((VarDescriptor)entity.cargo).getNameType())) {
						case AreaType	:
							writer.writeln(CompilerUtils.buildMethodCall(PRODUCE_AREA_KEEPER));
							if (entity.children.length != 0) {
								writer.writeln(" dup");
								printExpression(entity.children[0], names, classes, vars, 0, 0, writer);
								writer.writeln(CompilerUtils.buildMethodCall(AREA_KEEPER_SETVALUE));
							}
							break;
						case BooleanType:
							writer.writeln(CompilerUtils.buildMethodCall(PRODUCE_BOOLEAN_KEEPER));
							if (entity.children.length != 0) {
								writer.writeln(" dup");
								printExpression(entity.children[0].children[0], names, classes, vars, 0, 0, writer);
								writer.writeln(CompilerUtils.buildMethodCall(BOOLEAN_KEEPER_SETVALUE));
							}
							break;
						case DoubleType	:
							writer.writeln(CompilerUtils.buildMethodCall(PRODUCE_DOUBLE_KEEPER));
							if (entity.children.length != 0) {
								writer.writeln(" dup");
								printExpression(entity.children[0].children[0], names, classes, vars, 0, 0, writer);
								writer.writeln(CompilerUtils.buildMethodCall(DOUBLE_KEEPER_SETVALUE));
							}
							break;
						case LongType	:
							writer.writeln(CompilerUtils.buildMethodCall(PRODUCE_LONG_KEEPER));
							if (entity.children.length != 0) {
								writer.writeln(" dup");
								printExpression(entity.children[0].children[0], names, classes, vars, 0, 0, writer);
								writer.writeln(CompilerUtils.buildMethodCall(LONG_KEEPER_SETVALUE));
							}
							break;
						case OtherType	:
							if (entity.children.length != 0) {
								printExpression(entity.children[0], names, classes, vars, 0, 0, writer);
							}
							else {
								writer.writeln(" aconst_null");
							}
							break;
						case PointType	:
							writer.writeln(CompilerUtils.buildMethodCall(PRODUCE_POINT_KEEPER));
							if (entity.children.length != 0) {
								writer.writeln(" dup");
								printExpression(entity.children[0], names, classes, vars, 0, 0, writer);
								writer.writeln(CompilerUtils.buildMethodCall(POINT_KEEPER_SETVALUE));
							}
							break;
						case SizeType	:
							writer.writeln(CompilerUtils.buildMethodCall(PRODUCE_SIZE_KEEPER));
							if (entity.children.length != 0) {
								writer.writeln(" dup");
								printExpression(entity.children[0], names, classes, vars, 0, 0, writer);
								writer.writeln(CompilerUtils.buildMethodCall(SIZE_KEEPER_SETVALUE));
							}
							break;
						case StringType	:
							writer.writeln(CompilerUtils.buildMethodCall(PRODUCE_STRING_KEEPER));
							if (entity.children.length != 0) {
								writer.writeln(" dup");
								printExpression(entity.children[0].children[0], names, classes, vars, 0, 0, writer);
								writer.writeln(CompilerUtils.buildMethodCall(STRING_KEEPER_SETVALUE));
							}
							break;
						case TrackType	:
							writer.writeln(CompilerUtils.buildMethodCall(PRODUCE_TRACK_KEEPER));
							if (entity.children.length != 0) {
								writer.writeln(" dup");
								printExpression(entity.children[0], names, classes, vars, 0, 0, writer);
								writer.writeln(CompilerUtils.buildMethodCall(TRACK_KEEPER_SETVALUE));
							}
							break;
						default	: throw new UnsupportedOperationException("Simplified type ["+InternalUtils.defineSimplifiedType(((VarDescriptor)entity.cargo).getNameType())+"] is not supported yet"); 
					}
					writer.write(" putfield ").writeln(names.getName(entity.value));
				} catch (IOException | SyntaxException e) {
					return ContinueMode.STOP;
				}
				return ContinueMode.SKIP_CHILDREN;
			}
			else {
				return ContinueMode.CONTINUE;
			}
		});
	}
	
	static void printConstructorEnd(final MercSyntaxTreeNode root, final SyntaxTreeInterface<?> names, final MercClassRepo classes, final MercNameRepo vars, final CharDataOutput writer) throws IOException {
		writer.writeln(" endWriteConstructor");
	}
	
	static void printMain(final MercSyntaxTreeNode node, final SyntaxTreeInterface<?> names, final MercClassRepo classes, final MercNameRepo vars, final CharDataOutput writer) throws IOException, SyntaxException {
		writer.writeln(" writeExecutor");
		for (MercSyntaxTreeNode item : node.children) {
			printOperator(item,names,classes,vars,writer);
		}
		writer.writeln(" endWriteExecutor");
	}

	static void printFunc(final MercSyntaxTreeNode node, final SyntaxTreeInterface<?> names, final MercClassRepo classes, final MercNameRepo vars, final CharDataOutput writer) throws IOException, SyntaxException {
		printParametersAndVars(((MercSyntaxTreeNode)node.cargo),names,classes,vars,writer);
		for (MercSyntaxTreeNode item : node.children) {
			printOperator(item,names,classes,vars,writer);
		}
	}

	static void printTail(final MercSyntaxTreeNode node, final SyntaxTreeInterface<?> names, final MercClassRepo classes, final MercNameRepo vars, final CharDataOutput writer) throws IOException {
		writer.writeln(" writeEpilogue");
	}

	static void printParametersAndVars(final MercSyntaxTreeNode node, final SyntaxTreeInterface<?> names, final MercClassRepo classes, final MercNameRepo vars, final CharDataOutput writer) {
		// TODO Auto-generated method stub
		node.walk((mode,entity)->{
			if (mode == NodeEnterMode.ENTER && entity.getType() == MercSyntaxTreeNodeType.Variable) {
				try{writer.write(names.getName(entity.value)).write(" .field \n");
				} catch (IOException e) {
					return ContinueMode.STOP;
				}
			}
			return ContinueMode.CONTINUE;
		});
	}
	
	static void printOperator(final MercSyntaxTreeNode node, final SyntaxTreeInterface<?> names, final MercClassRepo classes, final MercNameRepo vars, final CharDataOutput writer) throws IOException, SyntaxException {
		// TODO Auto-generated method stub
		switch (node.getType()) {
			case Assign		:
				break;
			case Break		:
				break;
			case Call		:
				break;
			case Continue	:
				break;
			case LongIf		:
				break;
			case LongReturn	:
				break;
			case Print		:
				printPrintOperator(node, names, classes, vars, writer);
				break;
			case Sequence	:
				break;
			case ShortIf	:
				break;
			case ShortReturn:
				break;
			case TypedFor	:
				break;
			case Until		:
				break;
			case UntypedFor	:
				break;
			case While		:
				break;
			default :
				break;
		}
	}

	static void printPrintOperator(final MercSyntaxTreeNode node, final SyntaxTreeInterface<?> names, final MercClassRepo classes, final MercNameRepo vars, final CharDataOutput writer) throws IOException, SyntaxException {
		writer.writeln(" aload writer");
		for (MercSyntaxTreeNode item : node.children) {
			writer.writeln(" dup");
			printExpression(item,names,classes,vars,0,0,writer);
			final Class<?>	resultClass = inferenceExpressionType(item);
			
			switch (Utils.defineClassType(resultClass)) {
				case Utils.CLASSTYPE_REFERENCE	:
					if (char[].class.isAssignableFrom(resultClass)) {
						writer.writeln(" invokevirtual java.io.PrintWriter.print([C)V");
					}
					else {
						writer.writeln(" invokevirtual java.lang.Object.toString()Ljava/lang/String;");
						writer.writeln(" invokevirtual java.io.PrintWriter.print(Ljava/lang/String;)V");
					}
					break;
				case Utils.CLASSTYPE_BYTE	: case Utils.CLASSTYPE_SHORT	: case Utils.CLASSTYPE_INT	: case Utils.CLASSTYPE_CHAR	:
					writer.writeln(" i2l");
				case Utils.CLASSTYPE_LONG	:
					writer.writeln(" invokevirtual java.io.PrintWriter.print(J)V");
					break;
				case Utils.CLASSTYPE_FLOAT	:
					writer.writeln(" f2d");
				case Utils.CLASSTYPE_DOUBLE	:
					writer.writeln(" invokevirtual java.io.PrintWriter.print(D)V");
					break;
				case Utils.CLASSTYPE_BOOLEAN:
					writer.writeln(" invokevirtual java.io.PrintWriter.print(Z)V");
					break;
				default : throw new UnsupportedOperationException();
			}
			writer.writeln(" dup");
			writer.writeln(" ldc ' '");
			writer.writeln(" invokevirtual java.io.PrintWriter.print(C)V");
		}
		writer.writeln(" invokevirtual java.io.PrintWriter.println()V");
	}
	
	static void printExpression(final MercSyntaxTreeNode node, final SyntaxTreeInterface<?> names, final MercClassRepo classes, final MercNameRepo vars, final int trueLabel, final int falseLabel, final CharDataOutput writer) throws IOException, SyntaxException {
		switch (node.getType()) {
			case BoolConst	:
				writer.write(" ldc ").writeln(node.value);
				break;
			case Call	:
				final Class<?>		callType = inferenceExpressionType(node.children[0]);
				final String		methodName = names.getName(node.value);
				final Class<?>[]	callList;
				
				printExpression(node.children[0], names, classes, vars, trueLabel, falseLabel, writer);
				if (node.children.length == 2) {
					int		count = 0;
					
					callList = new Class[node.children[1].children.length];
					for (MercSyntaxTreeNode item : node.children[1].children) {
						printExpression(item, names, classes, vars, trueLabel, falseLabel, writer);
						callList[count++] = inferenceExpressionType(item);
					}
				}
				else {
					callList = new Class[0];
				}
				try{final Method	m = callType.getMethod(methodName,callList);

					writer.writeln(CompilerUtils.buildMethodCall(m));				
				} catch (NoSuchMethodException  e) {
					throw new SyntaxException(node.row,node.col,e.getMessage());
				}
				break;
			case Conversion	:
				printConversion(node, names, classes, vars, trueLabel, falseLabel, writer);
				break;
			case IndicedName	:
				printExpression(node.children[0], names, classes, vars, trueLabel, falseLabel, writer);
				for (MercSyntaxTreeNode item : node.children[1].children) {
					printExpression(item, names, classes, vars, trueLabel, falseLabel, writer);
					writer.write(" aaload");
				}
				break;
			case InstanceField	:
				printExpression(node.children[0], names, classes, vars, trueLabel, falseLabel, writer);
				writer.write(" getfield ").writeln(names.getName(node.children[1].value));
				break;
			case IntConst	:
				writer.write(" ldc2_w ").write(node.value).writeln("L");
				break;
			case LocalName	:
				writer.write(" aload ").writeln(names.getName(node.children[0].value));
				break;
			case OrdinalBinary	:
				printOrdinalBinaryExpression(node, names, classes, vars, trueLabel, falseLabel, writer);
				break;
			case OrdinalUnary	:
				printOrdinalUnaryExpression(node, names, classes, vars, trueLabel, falseLabel, writer);
				break;
			case Pipe	:
				break;
			case PredefinedName	:
				printPredefinedName(node, names, classes, vars, writer);
				break;
			case RealConst	:
				writer.write(" ldc2_w ").writeln(Double.longBitsToDouble(node.value));
				break;
			case RefConst	:
				break;
			case StandaloneName	:
				writer.writeln(" aload this");
				writer.write(" getfield ").writeln(names.getName(node.value));
				break;
			case StrConst	:
				writer.write(" ldc_w \"").write((char[])node.cargo).writeln("\"\n");
				writer.writeln(" invokevirtual java.lang.String.toCharArray()[C");
				break;
			default:
				break;
		}		
	}

	static void printConversion(final MercSyntaxTreeNode node, final SyntaxTreeInterface<?> names, final MercClassRepo classes, final MercNameRepo vars, final int trueLabel, final int falseLabel, final CharDataOutput writer) throws IOException, SyntaxException {
		switch (InternalUtils.defineSimplifiedType(((VarDescriptor)node.cargo).getNameType())) {
			case LongType	:
				switch (node.children.length) {
					case 1 :
						final Class<?>	resolved = MercOptimizer.processTypeConversions(node.children[0],null,null,classes,null);
						
						printExpression(node.children[0], names, classes, vars, trueLabel, falseLabel, writer);
						if (resolved == double.class) {
							writer.writeln(" d2l");
						}
						else if (resolved == char[].class) {
							writer.writeln(" l2d");
						}
						else {
							throw new SyntaxException(node.row, node.col, "Conversion erorr: source class ["+resolved+"] can't be converted to long");
						}
						break;
					default : throw new UnsupportedOperationException(""); 
				}
				break;
			case DoubleType	:
				switch (node.children.length) {
					case 1 :
						final Class<?>	resolved = MercOptimizer.processTypeConversions(node.children[0],null,null,classes,null);
						
						printExpression(node.children[0], names, classes, vars, trueLabel, falseLabel, writer);
						if (resolved == long.class) {
							writer.writeln(" l2d");
						}
						else if (resolved == char[].class) {
							writer.writeln(" l2d");
						}
						else {
							throw new SyntaxException(node.row, node.col, "Conversion erorr: source class ["+resolved+"] can't be converted to double");
						}
						break;
					default : throw new UnsupportedOperationException(""); 
				}
				break;
			case StringType	:
				switch (node.children.length) {
					case 1 :
						final Class<?>	resolved = MercOptimizer.processTypeConversions(node.children[0],null,null,classes,null);
						
						printExpression(node.children[0], names, classes, vars, trueLabel, falseLabel, writer);
						if (resolved == long.class) {
							writer.writeln(" invokestatic chav1961.merc.lang.merc.BasicMercProgram._toStr_(J)[C");
						}
						else if (resolved == double.class) {
							writer.writeln(" invokestatic chav1961.merc.lang.merc.BasicMercProgram._toStr_(D)[C");
						}
						else if (resolved == boolean.class) {
							writer.writeln(" invokestatic chav1961.merc.lang.merc.BasicMercProgram._toStr_(Z)[C");
						}
						else {
							throw new SyntaxException(node.row, node.col, "Conversion erorr: source class ["+resolved+"] can't be converted to char[]");
						}
						break;
					default : throw new UnsupportedOperationException(""); 
				}
				break;
			case BooleanType:				
				switch (node.children.length) {
					case 1 :
						final Class<?>	resolved = MercOptimizer.processTypeConversions(node.children[0],null,null,classes,null);
						
						printExpression(node.children[0], names, classes, vars, trueLabel, falseLabel, writer);
						if (resolved == char[].class) {
							writer.writeln(" l2d");
						}
						else {
							throw new SyntaxException(node.row, node.col, "Conversion erorr: source class ["+resolved+"] can't be converted to boolean");
						}
						break;
					default : throw new UnsupportedOperationException(""); 
				}
				break;
			case AreaType	:
				switch (node.children.length) {
					case 1 :
						newAndDup(writer,Area.class);
						printExpression(node.children[0], names, classes, vars, trueLabel, falseLabel, writer);
						writer.writeln(CompilerUtils.buildConstructorCall(AREA_CONSTRUCTOR_ENTITY));
						break;
					case 2 :
						newAndDup(writer,Area.class);
						printExpression(node.children[0], names, classes, vars, trueLabel, falseLabel, writer);
						printExpression(node.children[1], names, classes, vars, trueLabel, falseLabel, writer);
						writer.writeln(CompilerUtils.buildConstructorCall(AREA_CONSTRUCTOR_POINTSIZE));
						break;
					case 4 :
						newAndDup(writer,Area.class);
						for (MercSyntaxTreeNode item : node.children) {
							printExpression(item, names, classes, vars, trueLabel, falseLabel, writer);
							writer.writeln(" l2i");
						}
						writer.writeln(CompilerUtils.buildConstructorCall(AREA_CONSTRUCTOR_IIII));
						break;
					default : throw new UnsupportedOperationException(""); 
				}
				break;
			case OtherType	:
				writer.write(" checkcast ").writeln(((VarDescriptor)node.cargo).getNameType().getCanonicalName());
				break;
			case PointType	:
				switch (node.children.length) {
					case 1 :
						printExpression(node.children[0], names, classes, vars, trueLabel, falseLabel, writer);
						break;
					case 2 :
						newAndDup(writer,Point.class);
						for (MercSyntaxTreeNode item : node.children) {
							printExpression(item, names, classes, vars, trueLabel, falseLabel, writer);
							writer.writeln(" l2i");
						}
						writer.writeln(CompilerUtils.buildConstructorCall(POINT_CONSTRUCTOR_II));
						break;
					default : throw new UnsupportedOperationException(""); 
				}
				break;
			case SizeType	:
				switch (node.children.length) {
					case 1 :
						printExpression(node.children[0], names, classes, vars, trueLabel, falseLabel, writer);
						break;
					case 2 :
						newAndDup(writer,Size.class);
						for (MercSyntaxTreeNode item : node.children) {
							printExpression(item, names, classes, vars, trueLabel, falseLabel, writer);
							writer.writeln(" l2i");
						}
						writer.writeln(CompilerUtils.buildConstructorCall(SIZE_CONSTRUCTOR_II));
						break;
					default : throw new UnsupportedOperationException(""); 
				}
				break;
			case TrackType	:
				final Class<?>[]	innerNodeTypes = new Class[node.children.length];
				
				newAndDup(writer,Track.class);
				for (int index = 0; index < innerNodeTypes.length; index++) {
					innerNodeTypes[index] = InternalUtils.resolveNodeType(node.children[index]);
				}
				if (innerNodeTypes.length == 2) {
					if (innerNodeTypes[0] == long.class && innerNodeTypes[1] == long.class) {
						for (MercSyntaxTreeNode item : node.children) {
							printExpression(item, names, classes, vars, trueLabel, falseLabel, writer);
							writer.writeln(" l2i");
						}
						writer.writeln(CompilerUtils.buildConstructorCall(TRACK_CONSTRUCTOR_II));
						return;
					}
					else if (innerNodeTypes[0] == Point.class && innerNodeTypes[1] == Size.class) {
						for (MercSyntaxTreeNode item : node.children) {
							printExpression(item, names, classes, vars, trueLabel, falseLabel, writer);
						}
						writer.writeln(CompilerUtils.buildConstructorCall(TRACK_CONSTRUCTOR_POINTSIZE));
						return;
					}
				}
				if (innerNodeTypes.length == 4) {
					if (innerNodeTypes[0] == long.class && innerNodeTypes[1] == long.class && innerNodeTypes[2] == long.class && innerNodeTypes[3] == long.class) {
						for (MercSyntaxTreeNode item : node.children) {
							printExpression(item, names, classes, vars, trueLabel, falseLabel, writer);
							writer.writeln(" l2i");
						}
						writer.writeln(CompilerUtils.buildConstructorCall(TRACK_CONSTRUCTOR_IIII));
						return;
					}
				}
				writer.write(" ldc ").writeln(innerNodeTypes.length);
				writer.write(" anewarray ").writeln(CompilerUtils.buildClassPath(AvailableForTrack.class));
				for (int index = 0; index < node.children.length; index++) {
					writer.writeln(" dup");
					writer.write(" ldc ").writeln(index);
					printExpression(node.children[index], names, classes, vars, trueLabel, falseLabel, writer);
					writer.write(" checkcast ").writeln(CompilerUtils.buildClassPath(AvailableForTrack.class));
					writer.writeln(" aastore");
				}
				writer.writeln(CompilerUtils.buildConstructorCall(TRACK_CONSTRUCTOR_LIST));
				break;
			default : throw new UnsupportedOperationException("Simplified type ["+InternalUtils.defineSimplifiedType(((VarDescriptor)node.cargo).getNameType())+"] is not supported yet");
		}
	}

	static void printOrdinalUnaryExpression(final MercSyntaxTreeNode node, final SyntaxTreeInterface<?> names, final MercClassRepo classes, final MercNameRepo vars, final int trueLabel, final int falseLabel, final CharDataOutput writer) throws SyntaxException, IOException {
		Class<?>	infer;
		// TODO Auto-generated method stub
		switch (LexemaSubtype.values()[(int)node.value]) {
			case BitInv	:
				printExpression(node.children[0], names, classes, vars, trueLabel, falseLabel, writer);
				if (long.class.isAssignableFrom(inferenceExpressionType(node.children[0]))) {
					writer.writeln(" ldc2_w -1L");
					writer.writeln(" lxor");
				}
				else {
					throw new SyntaxException(node.row,node.col,"Bit inversion is not applicable for the given data type ["+inferenceExpressionType(node.children[0])+"]"); 
				}
				break;
			case Neg	:
				printExpression(node.children[0], names, classes, vars, trueLabel, falseLabel, writer);
				if (double.class.isAssignableFrom(inferenceExpressionType(node.children[0]))) {
					writer.writeln(" dneg");
				}
				else if (long.class.isAssignableFrom(inferenceExpressionType(node.children[0]))) {
					writer.writeln(" lneg");
				}
				else {
					throw new SyntaxException(node.row,node.col,"Negation is not applicable for the given data type ["+inferenceExpressionType(node.children[0])+"]"); 
				}
				break;
			case Not		:
				printExpression(node.children[0], names, classes, vars, trueLabel, falseLabel, writer);
				if (boolean.class.isAssignableFrom(inferenceExpressionType(node.children[0]))) {
					writer.writeln(" ldc 1");
					writer.writeln(" ixor");
				}
				else {
					throw new SyntaxException(node.row,node.col,"Logical NOT is not applicable for the given data type ["+inferenceExpressionType(node.children[0])+"]"); 
				}
				break;
			case PostDec	:
				infer = InternalUtils.resolveType4Value(inferenceExpressionType(node.children[0]));
				printExpression(node.children[0], names, classes, vars, trueLabel, falseLabel, writer);
				if (long.class.isAssignableFrom(infer)) {
					writer.writeln(" ldc 3");
					writer.writeln(CompilerUtils.buildMethodCall(PRODUCE_LONG_INCDEC));
				}
				else if (double.class.isAssignableFrom(infer)) {
					writer.writeln(" ldc 3");
					writer.writeln(CompilerUtils.buildMethodCall(PRODUCE_DOUBLE_INCDEC));
				}
				else {
					throw new SyntaxException(node.row,node.col,"Decrement is not applicable for the given data type ["+inferenceExpressionType(node.children[0])+"]"); 
				}
				break;
			case PostInc	:
				infer = InternalUtils.resolveType4Value(inferenceExpressionType(node.children[0]));
				printExpression(node.children[0], names, classes, vars, trueLabel, falseLabel, writer);
				if (long.class.isAssignableFrom(infer)) {
					writer.writeln(" ldc 1");
					writer.writeln(CompilerUtils.buildMethodCall(PRODUCE_LONG_INCDEC));
				}
				else if (double.class.isAssignableFrom(infer)) {
					writer.writeln(" ldc 1");
					writer.writeln(CompilerUtils.buildMethodCall(PRODUCE_DOUBLE_INCDEC));
				}
				else {
					throw new SyntaxException(node.row,node.col,"Increment is not applicable for the given data type ["+inferenceExpressionType(node.children[0])+"]"); 
				}
				break;
			case PreDec	:
				infer = InternalUtils.resolveType4Value(inferenceExpressionType(node.children[0]));
				printExpression(node.children[0], names, classes, vars, trueLabel, falseLabel, writer);
				if (long.class.isAssignableFrom(infer)) {
					writer.writeln(" ldc 2");
					writer.writeln(CompilerUtils.buildMethodCall(PRODUCE_LONG_INCDEC));
				}
				else if (double.class.isAssignableFrom(infer)) {
					writer.writeln(" ldc 2");
					writer.writeln(CompilerUtils.buildMethodCall(PRODUCE_DOUBLE_INCDEC));
				}
				else {
					throw new SyntaxException(node.row,node.col,"Decrement is not applicable for the given data type ["+inferenceExpressionType(node.children[0])+"]"); 
				}
				break;
			case PreInc	:
				infer = InternalUtils.resolveType4Value(inferenceExpressionType(node.children[0]));
				printExpression(node.children[0], names, classes, vars, trueLabel, falseLabel, writer);
				if (long.class.isAssignableFrom(infer)) {
					writer.writeln(" ldc 0");
					writer.writeln(CompilerUtils.buildMethodCall(PRODUCE_LONG_INCDEC));
				}
				else if (double.class.isAssignableFrom(infer)) {
					writer.writeln(" ldc 0");
					writer.writeln(CompilerUtils.buildMethodCall(PRODUCE_DOUBLE_INCDEC));
				}
				else {
					throw new SyntaxException(node.row,node.col,"Increment is not applicable for the given data type ["+inferenceExpressionType(node.children[0])+"]"); 
				}
				break;
		}
	}
	
	static void printOrdinalBinaryExpression(final MercSyntaxTreeNode node, final SyntaxTreeInterface<?> names, final MercClassRepo classes, final MercNameRepo vars, final int trueLabel, final int falseLabel, final CharDataOutput writer) throws IOException, SyntaxException {
		final LexemaSubtype[]	operators = (LexemaSubtype[])node.cargo;
		final Class<?>			firstClass = inferenceExpressionType(node.children[0]); 
		
		switch ((int)node.value) {
			case MercCompiler.PRTY_OR	:
				if (trueLabel == 0 && falseLabel == 0) {
					writer.writeln(" ldc 0");
					for (int index = 0; index < operators.length; index++) {
						printExpression(node.children[index], names, classes, vars, trueLabel, falseLabel, writer);
						writer.writeln(" ior");
					}
				}
				else {
					throw new UnsupportedOperationException();
				}
				break;
			case MercCompiler.PRTY_AND	:
				if (trueLabel == 0 && falseLabel == 0) {
					writer.writeln(" ldc 1");
					for (int index = 0; index < operators.length; index++) {
						printExpression(node.children[index], names, classes, vars, trueLabel, falseLabel, writer);
						writer.writeln(" iand");
					}
				}
				else {
					throw new UnsupportedOperationException();
				}
				break;
			case MercCompiler.PRTY_COMPARISON:
				printComparisonExpression(node,names,classes,vars,trueLabel,falseLabel,writer);
				break;
			case MercCompiler.PRTY_ADD		:
				if (char[].class.isAssignableFrom(firstClass)) {
					writer.writeln(" iload arr_len");
					writer.writeln(" ldc 0");
					writer.writeln(" istore arr_len");

					for (int index = 0; index < node.children.length; index++) {
						final MercSyntaxTreeNode item = node.children[index];
						
						printExpression(item, names, classes, vars, trueLabel, falseLabel, writer);
						if (operators[index] == LexemaSubtype.Sub) {
							writer.writeln(CompilerUtils.buildMethodCall(PRODUCE_CHAR_TRUNC));
						}
						writer.writeln(" dup");
						writer.writeln(" arraylength");
						writer.writeln(" iload arr_len");
						writer.writeln(" iadd");
						writer.writeln(" istore arr_len");
					}
					writer.writeln(" iload arr_len");
					writer.writeln(" newarray char");
					writer.writeln(" dup");
					writer.writeln(" dup");
					writer.writeln(" astore arr_ref");
					writer.writeln(" arraylength");
					writer.writeln(" invokestatic chav1961.merc.lang.merc.BasicMercProgram._concat_([C[CI)I");
					
					for (int index = 1; index < node.children.length; index++) {
						writer.writeln(" aload arr_ref");
						writer.writeln(" swap");
						writer.writeln(" invokestatic chav1961.merc.lang.merc.BasicMercProgram._concat_([C[CI)I");
					}
					writer.writeln(" pop");
					writer.writeln(" istore arr_len");
					writer.writeln(" aload arr_ref");
				}
				else {
					printExpression(node.children[0], names, classes, vars, trueLabel, falseLabel, writer);						
					for (int index = 1; index < operators.length; index++) {
						printExpression(node.children[index], names, classes, vars, trueLabel, falseLabel, writer);
						switch (operators[index]) {
							case Add	:
								if (long.class.isAssignableFrom(firstClass)) {
									writer.writeln(" ladd");
								}
								else if (double.class.isAssignableFrom(firstClass)) {
									writer.writeln(" dadd");
								}
								break;
							case Sub	:
								if (long.class.isAssignableFrom(firstClass)) {
									writer.writeln(" lsub");
								}
								else if (double.class.isAssignableFrom(firstClass)) {
									writer.writeln(" dsub");
								}
								else {
									throw new SyntaxException(node.row,node.col,"Substitution is not applicable for the given data type ["+inferenceExpressionType(node.children[index])+"]"); 
								}
								break;
							default :
						}
					}
				}
				break;
			case MercCompiler.PRTY_MUL		:
				printExpression(node.children[0], names, classes, vars, trueLabel, falseLabel, writer);
				for (int index = 1; index < operators.length; index++) {
					printExpression(node.children[index], names, classes, vars, trueLabel, falseLabel, writer);
					switch (operators[index]) {
						case Mul	:
							if (long.class.isAssignableFrom(firstClass)) {
								writer.writeln(" lmul");
							}
							else if (double.class.isAssignableFrom(firstClass)) {
								writer.writeln(" dmul");
							}
							else {
								throw new SyntaxException(node.row,node.col,"Multiplication is not applicable for the given data type ["+inferenceExpressionType(node.children[index])+"]"); 
							}
							break;
						case Div	:
							if (long.class.isAssignableFrom(firstClass)) {
								writer.writeln(" ldiv");
							}
							else if (double.class.isAssignableFrom(firstClass)) {
								writer.writeln(" ddiv");
							}
							else {
								throw new SyntaxException(node.row,node.col,"Dividion is not applicable for the given data type ["+inferenceExpressionType(node.children[index])+"]"); 
							}
							break;
						case Rem	:
							if (long.class.isAssignableFrom(firstClass)) {
								writer.writeln(" lrem");
							}
							else if (double.class.isAssignableFrom(firstClass)) {
								writer.writeln(" drem");
							}
							else {
								throw new SyntaxException(node.row,node.col,"Remainder is not applicable for the given data type ["+inferenceExpressionType(node.children[index])+"]"); 
							}
							break;
						default :
					}
				}
				break;
			case MercCompiler.PRTY_BITORXOR	:
				printExpression(node.children[0], names, classes, vars, trueLabel, falseLabel, writer);						
				for (int index = 1; index < operators.length; index++) {
					printExpression(node.children[index], names, classes, vars, trueLabel, falseLabel, writer);
					switch (operators[index]) {
						case BitOr	:
							writer.writeln(" lor");
							break;
						case BitXor	:
							writer.writeln(" lxor");
							break;
						default :
					}
				}
				break;
			case MercCompiler.PRTY_BITAND	:
				printExpression(node.children[0], names, classes, vars, trueLabel, falseLabel, writer);						
				for (int index = 1; index < operators.length; index++) {
					printExpression(node.children[index], names, classes, vars, trueLabel, falseLabel, writer);
					switch (operators[index]) {
						case BitAnd	:
							writer.writeln(" land");
							break;
						default :
					}
				}
				break;
			case MercCompiler.PRTY_SHIFT	:
				printExpression(node.children[0], names, classes, vars, trueLabel, falseLabel, writer);						
				for (int index = 1; index < operators.length; index++) {
					printExpression(node.children[index], names, classes, vars, trueLabel, falseLabel, writer);
					writer.writeln(" l2i");
					switch (operators[index]) {
						case Shl	:
							writer.writeln(" lshl");
							break;
						case Shr	:
							writer.writeln(" lshr");
							break;
						case Shra	:
							writer.writeln(" lushr");
							break;
						default :
					}
				}
				break;
		}
	}

	static void printComparisonExpression(final MercSyntaxTreeNode node, final SyntaxTreeInterface<?> names, final MercClassRepo classes, final MercNameRepo vars, final int trueLabel, final int falseLabel, final CharDataOutput writer) throws IOException, SyntaxException {
		final LexemaSubtype[]	operators = (LexemaSubtype[])node.cargo;
		final Class<?>			firstClass = inferenceExpressionType(node.children[0]); 

		printExpression(node.children[0], names, classes, vars, trueLabel, falseLabel, writer);
		if (long.class.isAssignableFrom(firstClass)) {
			switch (operators[1]) {
				case LT : case LE : case GT : case GE : case EQ : case NE : case IS :
					printExpression(node.children[1], names, classes, vars, trueLabel, falseLabel, writer);
					writer.writeln(" lcmp");
					break;
				case InList	:
					printRangedValuesList(node.children[1],long.class,names,classes,vars,0,0,writer);
					writer.writeln(CompilerUtils.buildMethodCall(PRODUCE_LONG_INLIST));
					break;
				default :
					throw new SyntaxException(node.row,node.col,"Operation ["+operators[1]+"] is not supported for the given types");
			}
		}
		else if (double.class.isAssignableFrom(firstClass)) {
			switch (operators[1]) {
				case LT : case LE : case GT : case GE : case EQ : case NE : case IS :
					printExpression(node.children[1], names, classes, vars, trueLabel, falseLabel, writer);
					writer.writeln(" dcmpg");
					break;
				case InList	:
					printRangedValuesList(node.children[1],double.class,names,classes,vars,0,0,writer);
					writer.writeln(CompilerUtils.buildMethodCall(PRODUCE_DOUBLE_INLIST));
					break;
				default :
					throw new SyntaxException(node.row,node.col,"Operation ["+operators[1]+"] is not supported for the given types");
			}
		}
		else if (char[].class.isAssignableFrom(firstClass)) {
			switch (operators[1]) {
				case LT : case LE : case GT : case GE : case EQ : case NE : case IS : 
					printExpression(node.children[1], names, classes, vars, trueLabel, falseLabel, writer);
					writer.writeln(CompilerUtils.buildMethodCall(PRODUCE_CHAR_COMPARE));
					break;
				case LIKE	:
					printExpression(node.children[1], names, classes, vars, trueLabel, falseLabel, writer);
					writer.writeln(CompilerUtils.buildMethodCall(PRODUCE_CHAR_LIKE));
				case InList	:
					printRangedValuesList(node.children[1],char[].class,names,classes,vars,0,0,writer);
					writer.writeln(CompilerUtils.buildMethodCall(PRODUCE_STRING_INLIST));
					break;
				default :
					throw new SyntaxException(node.row,node.col,"Operation ["+operators[1]+"] is not supported for the given types");
			}
		}
		else if (boolean.class.isAssignableFrom(firstClass)) {
			switch (operators[1]) {
				case LT : case LE : case GT : case GE : case EQ : case NE : case IS :
					printExpression(node.children[1], names, classes, vars, trueLabel, falseLabel, writer);
					writer.writeln(" isub");
					break;
				case InList	:
					printRangedValuesList(node.children[1],boolean.class,names,classes,vars,0,0,writer);
					writer.writeln(CompilerUtils.buildMethodCall(PRODUCE_BOOLEAN_INLIST));
					break;
				default :
					throw new SyntaxException(node.row,node.col,"Operation ["+operators[1]+"] is not supported for the given types");
			}
		}
		else if (Size.class.isAssignableFrom(firstClass)) {
			switch (operators[1]) {
				case LT : case LE : case GT : case GE : case EQ : case NE : case IS :
					printExpression(node.children[1], names, classes, vars, trueLabel, falseLabel, writer);
					writer.writeln(" invokestatic chav1961.merc.lang.merc.BasicMergProgram._compare_(Lchav1961/merc/api/Size;Lchav1961/merc/api/Size;)I");
					break;
				case InList	:
					printRangedValuesList(node.children[1],Size.class,names,classes,vars,0,0,writer);
					writer.writeln(CompilerUtils.buildMethodCall(PRODUCE_OBJECT_INLIST));
					break;
				default :
					throw new SyntaxException(node.row,node.col,"Operation ["+operators[1]+"] is not supported for the given types");
			}
		}
		else {
			switch (operators[1]) {
				case EQ : case NE :
					printExpression(node.children[1], names, classes, vars, trueLabel, falseLabel, writer);
					try{writer.write(" invokevirtual ").writeln(CompilerUtils.buildMethodCall(firstClass.getMethod("equals",Object.class)));
					} catch (NoSuchMethodException | SecurityException e) {
					}
					break;
				case InList	:
					printRangedValuesList(node.children[1],firstClass,names,classes,vars,0,0,writer);
					writer.writeln(CompilerUtils.buildMethodCall(PRODUCE_OBJECT_INLIST));
					break;
				default :
					throw new SyntaxException(node.row,node.col,"Operation ["+operators[1]+"] is not supported for the given types");
			}
		}
		if (trueLabel == 0 && falseLabel == 0) {
			final int	label = vars.getUniqueLabel(); 
					
			switch (operators[1]) {
				case LT		:
					writer.write(" ifge cmp_f").write(label).writeln();
					break;
				case LE		:
					writer.write(" ifgt cmp_f").write(label).writeln();
					break;
				case GT		:
					writer.write(" ifle cmp_f").write(label).writeln();
					break;
				case GE		:
					writer.write(" iflt cmp_f").write(label).writeln();
					break;
				case EQ		:
					writer.write(" ifne cmp_f").write(label).writeln();
					break;
				case NE		:
					writer.write(" ifeq cmp_f").write(label).writeln();
					break;
				case IS		:
					writer.write(" ifeq cmp_f").write(label).writeln();
					break;
				case LIKE	:
					writer.write(" ifne cmp_f").write(label).writeln();
					break;
				case InList	:
					writer.write(" ifne cmp_f").write(label).writeln();
					break;
			}
			writer.writeln(" ldc 1");
			writer.write(" goto cmp_t").writeln(label);
			writer.write("cmp_f").write(label).writeln(": ldc 0");
			writer.write("cmp_t").write(label).writeln(":");
		}
		else if (trueLabel != 0 && falseLabel == 0) {
			switch (operators[1]) {
				case LT		:
					writer.write(" iflt sx_").write(trueLabel).writeln();
					writer.write(" goto sx_").write(falseLabel).writeln();
					break;
				case LE		:
					writer.write(" ifle sx_").write(trueLabel).writeln();
					writer.write(" goto sx_").write(falseLabel).writeln();
					break;
				case GT		:
					writer.write(" ifgt sx_").write(trueLabel).writeln();
					writer.write(" goto sx_").write(falseLabel).writeln();
					break;
				case GE		:
					writer.write(" ifge sx_").write(trueLabel).writeln();
					writer.write(" goto sx_").write(falseLabel).writeln();
					break;
				case EQ		:
					writer.write(" ifeq sx_").write(trueLabel).writeln();
					writer.write(" goto sx_").write(falseLabel).writeln();
					break;
				case NE		:
					writer.write(" ifne sx_").write(trueLabel).writeln();
					writer.write(" goto sx_").write(falseLabel).writeln();
					break;
				case IS		:
					writer.write(" ifeq sx_").write(trueLabel).writeln();
					writer.write(" goto sx_").write(falseLabel).writeln();
					break;
				case LIKE	:
					writer.write(" ifeq sx_").write(trueLabel).writeln();
					writer.write(" goto sx_").write(falseLabel).writeln();
					break;
				case InList	:
					writer.write(" ifeq sx_").write(trueLabel).writeln();
					writer.write(" goto sx_").write(falseLabel).writeln();
					break;
			}
		}
		else if (trueLabel == 0 && falseLabel != 0) {
			switch (operators[1]) {
				case LT		:
					writer.write(" ifge sx_").write(falseLabel).writeln();
					break;
				case LE		:
					writer.write(" ifgt sx_").write(falseLabel).writeln();
					break;
				case GT		:
					writer.write(" ifle sx_").write(falseLabel).writeln();
					break;
				case GE		:
					writer.write(" iflt sx_").write(falseLabel).writeln();
					break;
				case EQ		:
					writer.write(" ifne sx_").write(falseLabel).writeln();
					break;
				case NE		:
					writer.write(" ifeq sx_").write(falseLabel).writeln();
					break;
				case IS		:
					writer.write(" ifne sx_").write(falseLabel).writeln();
					break;
				case LIKE	:
					writer.write(" ifne sx_").write(falseLabel).writeln();
					break;
				case InList	:
					writer.write(" ifne sx_").write(falseLabel).writeln();
					break;
			}
		}
		else {
			switch (operators[1]) {
				case LT		:
					writer.write(" iflt sx_").write(trueLabel).writeln();
					break;
				case LE		:
					writer.write(" ifle sx_").write(trueLabel).writeln();
					break;
				case GT		:
					writer.write(" ifgt sx_").write(trueLabel).writeln();
					break;
				case GE		:
					writer.write(" ifge sx_").write(trueLabel).writeln();
					break;
				case EQ		:
					writer.write(" ifeq sx_").write(trueLabel).writeln();
					break;
				case NE		:
					writer.write(" ifne sx_").write(trueLabel).writeln();
					break;
				case IS		:
					writer.write(" ifeq sx_").write(trueLabel).writeln();
					break;
				case LIKE	:
					writer.write(" ifeq sx_").write(trueLabel).writeln();
					break;
				case InList	:
					writer.write(" ifeq sx_").write(trueLabel).writeln();
					break;
			}
		}
	}	

	private static void printRangedValuesList(final MercSyntaxTreeNode node, final Class<?> awaited, final SyntaxTreeInterface<?> names, final MercClassRepo classes, final MercNameRepo vars, final int trueLabel, final int falseLabel, final CharDataOutput writer) throws IOException, SyntaxException {
		final int	arraySize = 2 * node.children.length;

		writer.write(" ldc ").writeln(arraySize);
		if (long.class.isAssignableFrom(awaited)) {
			writer.writeln(" newarray long");
		}
		else if (double.class.isAssignableFrom(awaited)) {
			writer.writeln(" newarray double");
		}
		else if (boolean.class.isAssignableFrom(awaited)) {
			writer.writeln(" newarray boolean");
		}
		else {
			writer.write(" anewarray ").writeln(CompilerUtils.buildClassPath(awaited));
		}
		for (int index = 0, maxIndex = node.children.length; index < maxIndex; index++) {
			writer.writeln(" dup");
			if (node.children[index].getType() == MercSyntaxTreeNodeType.Range) {
				writer.writeln(" dup");
				writer.write(" ldc ").writeln(2*index);
				printExpression(node.children[index].children[0],names,classes,vars,0,0,writer);
				printStoreArrayCommand(awaited,writer);
				writer.write(" ldc ").writeln(2*index+1);
				printExpression(node.children[index].children[1],names,classes,vars,0,0,writer);
				printStoreArrayCommand(awaited,writer);
			}
			else {
				writer.writeln(" dup");
				writer.write(" ldc ").writeln(2*index);
				writer.writeln(" swap");
				writer.write(" ldc ").writeln(2*index+1);
				printExpression(node.children[index],names,classes,vars,0,0,writer);
				if (long.class.isAssignableFrom(awaited) || double.class.isAssignableFrom(awaited)) {
					writer.writeln(" dup2_x2");
				}
				else {
					writer.writeln(" dup_x2");
				}
				printStoreArrayCommand(awaited,writer);
				printStoreArrayCommand(awaited,writer);
			}
		}
	}

	private static void printStoreArrayCommand(Class<?> awaited, CharDataOutput writer) throws IOException {
		if (long.class.isAssignableFrom(awaited)) {
			writer.writeln(" lastore");
		}
		else if (double.class.isAssignableFrom(awaited)) {
			writer.writeln(" dastore");
		}
		else if (boolean.class.isAssignableFrom(awaited)) {
			writer.writeln(" iastore");
		}
		else {
			writer.writeln(" aastore");
		}
	}

	static void printPredefinedName(final MercSyntaxTreeNode node, final SyntaxTreeInterface<?> names, final MercClassRepo classes, final MercNameRepo vars, final CharDataOutput writer) throws IOException {
		switch ((LexemaSubtype)node.cargo) {
			case Robo		:
				writer.writeln(" aload	world");
				writer.writeln(" getstatic chav1961.merc.api.Constants.ROBO_INSTANCE_UUID");
				writer.writeln(" invokeinterface chav1961.merc.api.interfaces.front.World.getEntity(Ljava/util/UUID;)Lchav1961/merc/api/interfaces/front/Entity;");
				writer.writeln(" checkcast chav1961.merc.core.robots.UniversalRobotInstance");
				break;
			case World		:
				writer.writeln(" aload	world");
				break;
			case Rt			:
				writer.writeln(" aload	world");
				writer.writeln(" invokeinterface chav1961.merc.api.interfaces.front.World.getRuntime()Lchav1961/merc/api/interfaces/front/RuntimeInterface;");
				break;
			case Market		:
				writer.writeln(" aload	world");
				writer.writeln(" invokeinterface chav1961.merc.api.interfaces.front.World.getRuntime()Lchav1961/merc/api/interfaces/front/RuntimeInterface;");
				writer.writeln(" invokeinterface chav1961.merc.api.interfaces.front.RuntimeInterface.market()Lchav1961/merc/api/interfaces/front/RuntimeInterface$MarketInterface;");
				break;
			case Teleport	:
				writer.writeln(" aload	world");
				writer.writeln(" invokeinterface chav1961.merc.api.interfaces.front.World.getRuntime()Lchav1961/merc/api/interfaces/front/RuntimeInterface;");
				writer.writeln(" invokeinterface chav1961.merc.api.interfaces.front.RuntimeInterface.teleport()Lchav1961/merc/api/interfaces/front/RuntimeInterface$TeleportInterface;");
				break;
			default : throw new IOException("Predefined name ["+node.cargo+"] is not known");
		}
	}
	
	
	static boolean isConstantExpression(final MercSyntaxTreeNode node) {
		return false;
	}
	
	static MercSyntaxTreeNode calculateConstanExpression(final MercSyntaxTreeNode node) {
		return null;
	}
	
	static Class<?> inferenceExpressionType(final MercSyntaxTreeNode node) {
		switch (node.getType()) {
			case AllocatedVariable:
				break;
			case Assign:
				break;
			case BoolConst	:
				return boolean.class;
			case Break:
				break;
			case Brick:
				break;
			case Call:
				return ((VarDescriptorImpl)node.cargo).getNameType();
			case Continue:
				break;
			case Conversion:
				return ((VarDescriptorImpl)node.cargo).getNameType();
			case Function:
				break;
			case Header:
				break;
			case HeaderWithReturned:
				break;
			case IndicedName:
				break;
			case InstanceField:
				return ((VarDescriptorImpl)node.cargo).getNameType();
			case IntConst	:
				return long.class;
			case List:
				break;
			case Lock:
				break;
			case LongIf:
				break;
			case LongReturn:
				break;
			case Null:
				break;
			case OrdinalBinary:
				switch ((int)node.value) {
					case MercCompiler.PRTY_BITAND	:
						return long.class;
					case MercCompiler.PRTY_BITORXOR	:
						return long.class;
					case MercCompiler.PRTY_SHIFT	:
						return long.class;
					case MercCompiler.PRTY_MUL		:
						return inferenceExpressionType(node.children[0]);
					case MercCompiler.PRTY_ADD		:
						return inferenceExpressionType(node.children[0]);
					case MercCompiler.PRTY_COMPARISON	:
						return boolean.class;
					case MercCompiler.PRTY_AND		:
						return boolean.class;
					case MercCompiler.PRTY_OR		:
						return boolean.class;
				}
				break;
			case OrdinalUnary:
				switch (LexemaSubtype.values()[(int)node.value]) {
					case BitInv: case PostDec: case PostInc: case PreDec: case PreInc: case Neg	:
						return inferenceExpressionType(node.children[0]);
					case Not:
						return boolean.class;
				}
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
			case RealConst	:
				return double.class;
			case RefConst:
				break;
			case Sequence:
				break;
			case ShortIf:
				break;
			case ShortReturn:
				break;
			case StandaloneName:
				return ((VarDescriptor)node.cargo).getNameType();
			case StrConst	:
				return char[].class;
			case TypedFor:
				break;
			case Unknown:
				break;
			case Until:
				break;
			case UntypedFor:
				break;
			case Variable:
				return ((VarDescriptor)node.cargo).getNameType();
			case Variables:
				break;
			case Vartype:
				break;
			case While:
				break;
			default:
				throw new UnsupportedOperationException("Node type ["+node.getType()+"] is not supported");
		}
		return null;
	}

	private static void newAndDup(final CharDataOutput writer, final Class<?> clazz) throws IOException {
		writer.write(" new ").writeln(CompilerUtils.buildClassPath(clazz)).writeln(" dup");
	}

}
