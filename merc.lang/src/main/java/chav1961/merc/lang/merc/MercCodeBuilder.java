package chav1961.merc.lang.merc;


import java.io.IOException;
import java.nio.channels.UnsupportedAddressTypeException;

import chav1961.merc.lang.merc.SyntaxTreeNode.SyntaxTreeNodeType;
import chav1961.merc.lang.merc.interfaces.CharDataOutput;
import chav1961.merc.lang.merc.interfaces.VarDescriptor;
import chav1961.purelib.basic.Utils;
import chav1961.purelib.basic.interfaces.SyntaxTreeInterface;
import chav1961.purelib.enumerations.ContinueMode;
import chav1961.purelib.enumerations.NodeEnterMode;

class MercCodeBuilder {
	static void printHead(final SyntaxTreeNode node, final SyntaxTreeInterface<?> names, final MercClassRepo classes, final MercNameRepo vars, final CharDataOutput writer) {
		// TODO Auto-generated method stub
		
	}

	static void printFields(final SyntaxTreeNode node, final SyntaxTreeInterface<?> names, final MercClassRepo classes, final MercNameRepo vars, final CharDataOutput writer) {
		// TODO Auto-generated method stub
		node.walk((mode,entity)->{
			if (mode == NodeEnterMode.ENTER && entity.getType() == SyntaxTreeNodeType.Variable) {
				try{writer.write(names.getName(entity.value)).write(" .field \n");
				} catch (IOException e) {
					return ContinueMode.STOP;
				}
			}
			return ContinueMode.CONTINUE;
		});
	}

	static void printMain(final SyntaxTreeNode node, final SyntaxTreeInterface<?> names, final MercClassRepo classes, final MercNameRepo vars, final CharDataOutput writer) throws IOException {
		// TODO Auto-generated method stub
		for (SyntaxTreeNode item : node.children) {
			printOperator(item,names,classes,vars,writer);
		}
	}

	static void printFunc(final SyntaxTreeNode node, final SyntaxTreeInterface<?> names, final MercClassRepo classes, final MercNameRepo vars, final CharDataOutput writer) throws IOException {
		printParametersAndVars(((SyntaxTreeNode)node.cargo),names,classes,vars,writer);
		for (SyntaxTreeNode item : node.children) {
			printOperator(item,names,classes,vars,writer);
		}
	}

	static void printTail(final SyntaxTreeNode node, final SyntaxTreeInterface<?> names, final MercClassRepo classes, final MercNameRepo vars, final CharDataOutput writer) {
		// TODO Auto-generated method stub
		
	}

	static void printParametersAndVars(final SyntaxTreeNode node, final SyntaxTreeInterface<?> names, final MercClassRepo classes, final MercNameRepo vars, final CharDataOutput writer) {
		// TODO Auto-generated method stub
		node.walk((mode,entity)->{
			if (mode == NodeEnterMode.ENTER && entity.getType() == SyntaxTreeNodeType.Variable) {
				try{writer.write(names.getName(entity.value)).write(" .field \n");
				} catch (IOException e) {
					return ContinueMode.STOP;
				}
			}
			return ContinueMode.CONTINUE;
		});
	}
	
	static void printOperator(final SyntaxTreeNode node, final SyntaxTreeInterface<?> names, final MercClassRepo classes, final MercNameRepo vars, final CharDataOutput writer) throws IOException {
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

	static void printPrintOperator(final SyntaxTreeNode node, final SyntaxTreeInterface<?> names, final MercClassRepo classes, final MercNameRepo vars, final CharDataOutput writer) throws IOException {
		writer.writeln(" aload writer");
		for (SyntaxTreeNode item : node.children) {
			writer.writeln(" dup");
			printExpression(item,names,classes,vars,writer);
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
	
	static void printExpression(final SyntaxTreeNode node, final SyntaxTreeInterface<?> names, final MercClassRepo classes, final MercNameRepo vars, final CharDataOutput writer) throws IOException {
		switch (node.getType()) {
			case BitInv	:
				printExpression(node.children[0], names, classes, vars, writer);
				if (long.class.isAssignableFrom(inferenceExpressionType(node.children[0]))) {
					writer.writeln(" ldc2_w -1L");
					writer.writeln(" lxor");
				}
				else {
					throw new IOException(); 
				}
				break;
			case BoolConst	:
				writer.write(" ldc ").writeln(node.value);
				break;
			case Call	:
				break;
			case Conversion	:
				break;
			case IndicedName	:
				break;
			case InstanceField	:
				break;
			case IntConst	:
				writer.write(" ldc2_w ").write(node.value).writeln("L");
				break;
			case Negation	:
				printExpression(node.children[0], names, classes, vars, writer);
				if (double.class.isAssignableFrom(inferenceExpressionType(node.children[0]))) {
					writer.writeln(" dneg");
				}
				else if (long.class.isAssignableFrom(inferenceExpressionType(node.children[0]))) {
					writer.writeln(" lneg");
				}
				else {
					throw new IOException(); 
				}
				break;
			case Not		:
				printExpression(node.children[0], names, classes, vars, writer);
				if (boolean.class.isAssignableFrom(inferenceExpressionType(node.children[0]))) {
					writer.writeln(" ldc 1");
					writer.writeln(" ixor");
				}
				else {
					throw new IOException(); 
				}
				break;
			case OrdinalBinary	:
				printOrdinalBinaryExpression(node, names, classes, vars, writer);
				break;
			case Pipe	:
				break;
			case PostDec	:
				printExpression(node.children[0], names, classes, vars, writer);
//				if (double.class.isAssignableFrom(inferenceExpressionType(node.children[0]))) {
//					writer.writeln(" dneg");
//				}
//				else if (long.class.isAssignableFrom(inferenceExpressionType(node.children[0]))) {
//					writer.writeln(" lneg");
//				}
//				else {
//					throw new IOException(); 
//				}
				break;
			case PostInc	:
				printExpression(node.children[0], names, classes, vars, writer);
//				if (double.class.isAssignableFrom(inferenceExpressionType(node.children[0]))) {
//					writer.writeln(" dneg");
//				}
//				else if (long.class.isAssignableFrom(inferenceExpressionType(node.children[0]))) {
//					writer.writeln(" lneg");
//				}
//				else {
//					throw new IOException(); 
//				}
				break;
			case PreDec	:
				printExpression(node.children[0], names, classes, vars, writer);
//				if (double.class.isAssignableFrom(inferenceExpressionType(node.children[0]))) {
//					writer.writeln(" dneg");
//				}
//				else if (long.class.isAssignableFrom(inferenceExpressionType(node.children[0]))) {
//					writer.writeln(" lneg");
//				}
//				else {
//					throw new IOException(); 
//				}
				break;
			case PreInc	:
				printExpression(node.children[0], names, classes, vars, writer);
//				if (double.class.isAssignableFrom(inferenceExpressionType(node.children[0]))) {
//					writer.writeln(" dneg");
//				}
//				else if (long.class.isAssignableFrom(inferenceExpressionType(node.children[0]))) {
//					writer.writeln(" lneg");
//				}
//				else {
//					throw new IOException(); 
//				}
				break;
			case PredefinedName	:
				break;
			case RealConst	:
				writer.write(" ldc2_w ").writeln(Double.longBitsToDouble(node.value));
				break;
			case RefConst	:
				break;
			case StandaloneName	:
				break;
			case StrConst	:
				writer.write(" ldc_w \"").write((char[])node.cargo).writeln("\"\n");
				writer.writeln(" invokevirtual java.lang.String.toCharArray()[C");
				break;
			default:
				break;
		}		
	}

	static void printOrdinalBinaryExpression(final SyntaxTreeNode node, final SyntaxTreeInterface<?> names, final MercClassRepo classes, final MercNameRepo vars, final CharDataOutput writer) throws IOException {
		final LexemaSubtype[]	operators = (LexemaSubtype[])node.cargo;
		final Class<?>			firstClass = inferenceExpressionType(node.children[0]); 
		
		switch ((int)node.value) {
			case MercCompiler.PRTY_ADD		:
				if (char[].class.isAssignableFrom(firstClass)) {
					writer.writeln(" load arr_len");
					writer.writeln(" ldc 0");
					writer.writeln(" store arr_len");

					for (SyntaxTreeNode item : node.children) {
						printExpression(item, names, classes, vars, writer);
						writer.writeln(" dup");
						writer.writeln(" arraylength");
						writer.writeln(" load arr_len");
						writer.writeln(" iadd");
						writer.writeln(" store arr_len");
					}
					writer.writeln(" load arr_len");
					writer.writeln(" arraynew char");
					writer.writeln(" dup");
					writer.writeln(" store arr_ref");
					writer.writeln(" arraylength");
					writer.writeln(" invokevirtual _concat_([C[CI)I");
					
					for (int index = 1; index < node.children.length; index++) {
						writer.writeln(" load arr_ref");
						writer.writeln(" swap");
						writer.writeln(" invokevirtual _concat_([C[CI)I");
					}
					writer.writeln(" pop");
					writer.writeln(" store arr_len");
					writer.writeln(" load arr_ref");
				}
				else {
					printExpression(node.children[0], names, classes, vars, writer);						
					for (int index = 1; index < operators.length; index++) {
						printExpression(node.children[index], names, classes, vars, writer);
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
								break;
							default :
						}
					}
				}
				break;
			case MercCompiler.PRTY_MUL		:
				printExpression(node.children[0], names, classes, vars, writer);						
				for (int index = 1; index < operators.length; index++) {
					printExpression(node.children[index], names, classes, vars, writer);
					switch (operators[index]) {
						case Mul	:
							if (long.class.isAssignableFrom(firstClass)) {
								writer.writeln(" lmul");
							}
							else if (double.class.isAssignableFrom(firstClass)) {
								writer.writeln(" dmul");
							}
							break;
						case Div	:
							if (long.class.isAssignableFrom(firstClass)) {
								writer.writeln(" ldiv");
							}
							else if (double.class.isAssignableFrom(firstClass)) {
								writer.writeln(" ddiv");
							}
							break;
						case Rem	:
							if (long.class.isAssignableFrom(firstClass)) {
								writer.writeln(" lrem");
							}
							else if (double.class.isAssignableFrom(firstClass)) {
								writer.writeln(" drem");
							}
							break;
						default :
					}
				}
				break;
			case MercCompiler.PRTY_BITORXOR	:
				printExpression(node.children[0], names, classes, vars, writer);						
				for (int index = 1; index < operators.length; index++) {
					printExpression(node.children[index], names, classes, vars, writer);
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
				printExpression(node.children[0], names, classes, vars, writer);						
				for (int index = 1; index < operators.length; index++) {
					printExpression(node.children[index], names, classes, vars, writer);
					switch (operators[index]) {
						case BitAnd	:
							writer.writeln(" land");
							break;
						default :
					}
				}
				break;
			case MercCompiler.PRTY_SHIFT	:
				printExpression(node.children[0], names, classes, vars, writer);						
				for (int index = 1; index < operators.length; index++) {
					printExpression(node.children[index], names, classes, vars, writer);
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
	
	
	static boolean isConstantExpression(final SyntaxTreeNode node) {
		return false;
	}
	
	static SyntaxTreeNode calculateConstanExpression(final SyntaxTreeNode node) {
		return null;
	}
	
	static Class<?> inferenceExpressionType(final SyntaxTreeNode node) {
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
			case Continue:
				break;
			case Conversion:
				break;
			case Function:
				break;
			case Header:
				break;
			case HeaderWithReturned:
				break;
			case IndicedName:
				break;
			case InstanceField:
				break;
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
			case BitInv: case PostDec: case PostInc: case PreDec: case PreInc: case Negation	:
				return inferenceExpressionType(node.children[0]);
			case Not:
				return boolean.class;
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
				break;
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

	static String buildSignature(final Class<?> clazz) {
		return null;
	}
	
	static Class<?> convert2ExpressionType(final SyntaxTreeNode node, final Class<?> awaited) {
		return null;
	}
}
