package chav1961.merc.lang.merc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;

import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.api.exceptions.MercEnvironmentException;
import chav1961.merc.api.interfaces.front.World;
import chav1961.merc.lang.merc.SyntaxTreeNode.SyntaxTreeNodeType;
import chav1961.merc.lang.merc.interfaces.CallProgram;
import chav1961.merc.lang.merc.interfaces.CharDataOutput;
import chav1961.purelib.basic.ClassLoaderWrapper;
import chav1961.purelib.basic.OrdinalSyntaxTree;
import chav1961.purelib.basic.Utils;
import chav1961.purelib.basic.interfaces.SyntaxTreeInterface;
import chav1961.purelib.streams.char2byte.AsmWriter;

public class MercCodeBuilderTest {
	private static int	uniqueName = 0;
	
	@FunctionalInterface
	interface Insertion {
		void process(CharDataOutput out);
	}
	
	@Test
	public void basicTest() throws IOException, URISyntaxException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, MercContentException, MercEnvironmentException {
		final SyntaxTreeInterface<?>	names = new OrdinalSyntaxTree<>();
		final MercClassRepo				classes = new MercClassRepo(names,0);
		final MercNameRepo				vars = new MercNameRepo();
		final SyntaxTreeNode			root = new SyntaxTreeNode(SyntaxTreeNodeType.Print,-1,null
													,new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,20,null)
													,new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(12.5),null)
													,new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,-1,"test string".toCharArray())
													,new SyntaxTreeNode(SyntaxTreeNodeType.BoolConst,0,null)
													,new SyntaxTreeNode(SyntaxTreeNodeType.BoolConst,1,null)
													);
		
		Assert.assertEquals("20 12.5 test string false true ", execute(uniqueName++, null,(out)->{
			try{MercCodeBuilder.printPrintOperator(root,names,classes,vars,out);
			} catch (IOException e) {
				Assert.fail("Unwaited exception: "+e);
			}
		}));
	}

	@Test
	public void unaryExpressionWithoutIncDecTest() throws IOException, URISyntaxException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, MercContentException, MercEnvironmentException {
		final SyntaxTreeInterface<?>	names = new OrdinalSyntaxTree<>();
		final MercClassRepo				classes = new MercClassRepo(names,0);
		final MercNameRepo				vars = new MercNameRepo();
		final long						counterId = names.placeName("counter",null);
		
		Assert.assertEquals("-20 ", execute(uniqueName++, null,(out)->{
			try{MercCodeBuilder.printPrintOperator(
					new SyntaxTreeNode(SyntaxTreeNodeType.Print,-1,null
							,new SyntaxTreeNode(SyntaxTreeNodeType.Negation,0,null,new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,20,null))
					),
					names,classes,vars,out);
			} catch (IOException e) {
				Assert.fail("Unwaited exception: "+e);
			}
		}));
		Assert.assertEquals("-12.5 ", execute(uniqueName++, null,(out)->{
			try{MercCodeBuilder.printPrintOperator(
					new SyntaxTreeNode(SyntaxTreeNodeType.Print,-1,null
							,new SyntaxTreeNode(SyntaxTreeNodeType.Negation,0,null,new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(12.5),null))
					),
					names,classes,vars,out);
			} catch (IOException e) {
				Assert.fail("Unwaited exception: "+e);
			}
		}));
		Assert.assertEquals("-1 ", execute(uniqueName++, null,(out)->{
			try{MercCodeBuilder.printPrintOperator(
					new SyntaxTreeNode(SyntaxTreeNodeType.Print,-1,null
							,new SyntaxTreeNode(SyntaxTreeNodeType.BitInv,0,null,new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,0,null))
					),
					names,classes,vars,out);
			} catch (IOException e) {
				Assert.fail("Unwaited exception: "+e);
			}
		}));
		Assert.assertEquals("false ", execute(uniqueName++, null,(out)->{
			try{MercCodeBuilder.printPrintOperator(
					new SyntaxTreeNode(SyntaxTreeNodeType.Print,-1,null
							,new SyntaxTreeNode(SyntaxTreeNodeType.Not,0,null,new SyntaxTreeNode(SyntaxTreeNodeType.BoolConst,1,null))
					),
					names,classes,vars,out);
			} catch (IOException e) {
				Assert.fail("Unwaited exception: "+e);
			}
		}));
		Assert.assertEquals("true ", execute(uniqueName++, null,(out)->{
			try{MercCodeBuilder.printPrintOperator(
					new SyntaxTreeNode(SyntaxTreeNodeType.Print,-1,null
							,new SyntaxTreeNode(SyntaxTreeNodeType.Not,0,null,new SyntaxTreeNode(SyntaxTreeNodeType.BoolConst,0,null))
					),
					names,classes,vars,out);
			} catch (IOException e) {
				Assert.fail("Unwaited exception: "+e);
			}
		}));
	}

	@Test
	public void chainedBinaryExpressionTest() throws IOException, URISyntaxException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, MercContentException, MercEnvironmentException {
		final SyntaxTreeInterface<?>	names = new OrdinalSyntaxTree<>();
		final MercClassRepo				classes = new MercClassRepo(names,0);
		final MercNameRepo				vars = new MercNameRepo();
		final long						counterId = names.placeName("counter",null);
		
		/* integer */
		Assert.assertEquals("50 ", execute(uniqueName++, null,(out)->{
			try{MercCodeBuilder.printPrintOperator(
					new SyntaxTreeNode(SyntaxTreeNodeType.Print,-1,null
							,new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD,
									new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
									,new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,20,null)
									,new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,30,null))
					),
					names,classes,vars,out);
			} catch (IOException e) {
				Assert.fail("Unwaited exception: "+e);
			}
		}));
		Assert.assertEquals("-10 ", execute(uniqueName++, null,(out)->{
			try{MercCodeBuilder.printPrintOperator(
					new SyntaxTreeNode(SyntaxTreeNodeType.Print,-1,null
							,new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD,
									new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Sub}
									,new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,20,null)
									,new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,30,null))
					),
					names,classes,vars,out);
			} catch (IOException e) {
				Assert.fail("Unwaited exception: "+e);
			}
		}));

		Assert.assertEquals("600 ", execute(uniqueName++, null,(out)->{
			try{MercCodeBuilder.printPrintOperator(
					new SyntaxTreeNode(SyntaxTreeNodeType.Print,-1,null
							,new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_MUL,
									new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Mul}
									,new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,20,null)
									,new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,30,null))
					),
					names,classes,vars,out);
			} catch (IOException e) {
				Assert.fail("Unwaited exception: "+e);
			}
		}));
		Assert.assertEquals("0 ", execute(uniqueName++, null,(out)->{
			try{MercCodeBuilder.printPrintOperator(
					new SyntaxTreeNode(SyntaxTreeNodeType.Print,-1,null
							,new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_MUL,
									new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Div}
									,new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,20,null)
									,new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,30,null))
					),
					names,classes,vars,out);
			} catch (IOException e) {
				Assert.fail("Unwaited exception: "+e);
			}
		}));
		Assert.assertEquals("20 ", execute(uniqueName++, null,(out)->{
			try{MercCodeBuilder.printPrintOperator(
					new SyntaxTreeNode(SyntaxTreeNodeType.Print,-1,null
							,new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_MUL,
									new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Rem}
									,new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,20,null)
									,new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,30,null))
					),
					names,classes,vars,out);
			} catch (IOException e) {
				Assert.fail("Unwaited exception: "+e);
			}
		}));

		Assert.assertEquals("2 ", execute(uniqueName++, null,(out)->{
			try{MercCodeBuilder.printPrintOperator(
					new SyntaxTreeNode(SyntaxTreeNodeType.Print,-1,null
							,new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_BITAND,
									new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.BitAnd}
									,new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,2,null)
									,new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,3,null))
					),
					names,classes,vars,out);
			} catch (IOException e) {
				Assert.fail("Unwaited exception: "+e);
			}
		}));
		
		Assert.assertEquals("3 ", execute(uniqueName++, null,(out)->{
			try{MercCodeBuilder.printPrintOperator(
					new SyntaxTreeNode(SyntaxTreeNodeType.Print,-1,null
							,new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_BITORXOR,
									new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.BitOr}
									,new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,2,null)
									,new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,3,null))
					),
					names,classes,vars,out);
			} catch (IOException e) {
				Assert.fail("Unwaited exception: "+e);
			}
		}));
		Assert.assertEquals("1 ", execute(uniqueName++, null,(out)->{
			try{MercCodeBuilder.printPrintOperator(
					new SyntaxTreeNode(SyntaxTreeNodeType.Print,-1,null
							,new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_BITORXOR,
									new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.BitXor}
									,new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,2,null)
									,new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,3,null))
					),
					names,classes,vars,out);
			} catch (IOException e) {
				Assert.fail("Unwaited exception: "+e);
			}
		}));

		Assert.assertEquals("4 ", execute(uniqueName++, null,(out)->{
			try{MercCodeBuilder.printPrintOperator(
					new SyntaxTreeNode(SyntaxTreeNodeType.Print,-1,null
							,new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_SHIFT,
									new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Shl}
									,new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,2,null)
									,new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,1,null))
					),
					names,classes,vars,out);
			} catch (IOException e) {
				Assert.fail("Unwaited exception: "+e);
			}
		}));
		Assert.assertEquals("1 ", execute(uniqueName++, null,(out)->{
			try{MercCodeBuilder.printPrintOperator(
					new SyntaxTreeNode(SyntaxTreeNodeType.Print,-1,null
							,new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_SHIFT,
									new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Shr}
									,new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,2,null)
									,new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,1,null))
					),
					names,classes,vars,out);
			} catch (IOException e) {
				Assert.fail("Unwaited exception: "+e);
			}
		}));
		Assert.assertEquals("1 ", execute(uniqueName++, null,(out)->{
			try{MercCodeBuilder.printPrintOperator(
					new SyntaxTreeNode(SyntaxTreeNodeType.Print,-1,null
							,new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_SHIFT,
									new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Shra}
									,new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,2,null)
									,new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,1,null))
					),
					names,classes,vars,out);
			} catch (IOException e) {
				Assert.fail("Unwaited exception: "+e);
			}
		}));

		/* numeric */
		Assert.assertEquals("50.0 ", execute(uniqueName++, null,(out)->{
			try{MercCodeBuilder.printPrintOperator(
					new SyntaxTreeNode(SyntaxTreeNodeType.Print,-1,null
							,new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD,
									new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
									,new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(20),null)
									,new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(30),null))
					),
					names,classes,vars,out);
			} catch (IOException e) {
				Assert.fail("Unwaited exception: "+e);
			}
		}));
		Assert.assertEquals("-10.0 ", execute(uniqueName++, null,(out)->{
			try{MercCodeBuilder.printPrintOperator(
					new SyntaxTreeNode(SyntaxTreeNodeType.Print,-1,null
							,new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD,
									new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Sub}
									,new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(20),null)
									,new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(30),null))
					),
					names,classes,vars,out);
			} catch (IOException e) {
				Assert.fail("Unwaited exception: "+e);
			}
		}));

		Assert.assertEquals("600.0 ", execute(uniqueName++, null,(out)->{
			try{MercCodeBuilder.printPrintOperator(
					new SyntaxTreeNode(SyntaxTreeNodeType.Print,-1,null
							,new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_MUL,
									new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Mul}
									,new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(20),null)
									,new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(30),null))
					),
					names,classes,vars,out);
			} catch (IOException e) {
				Assert.fail("Unwaited exception: "+e);
			}
		}));
		Assert.assertEquals("0.6666666666666666 ", execute(uniqueName++, null,(out)->{
			try{MercCodeBuilder.printPrintOperator(
					new SyntaxTreeNode(SyntaxTreeNodeType.Print,-1,null
							,new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_MUL,
									new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Div}
									,new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(20),null)
									,new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(30),null))
					),
					names,classes,vars,out);
			} catch (IOException e) {
				Assert.fail("Unwaited exception: "+e);
			}
		}));
		Assert.assertEquals("20.0 ", execute(uniqueName++, null,(out)->{
			try{MercCodeBuilder.printPrintOperator(
					new SyntaxTreeNode(SyntaxTreeNodeType.Print,-1,null
							,new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_MUL,
									new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Rem}
									,new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(20),null)
									,new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(30),null))
					),
					names,classes,vars,out);
			} catch (IOException e) {
				Assert.fail("Unwaited exception: "+e);
			}
		}));
	}

	
	private String execute(final int classNameSuffix, final World world, final Insertion ins) {
		try(final StringWriter			wr = new StringWriter();
			final WriterCharDataOutput	out = new WriterCharDataOutput(wr)) {
		
			out.writeln(String.format(new String(Utils.loadCharsFromURI(MercCodeBuilderTest.class.getResource("beforeSimple.txt").toURI())),classNameSuffix));
			ins.process(out);
			out.writeln(String.format(new String(Utils.loadCharsFromURI(MercCodeBuilderTest.class.getResource("afterSimple.txt").toURI())),classNameSuffix));
			out.flush();
			
			try(final ByteArrayOutputStream	baos = new ByteArrayOutputStream();
				final AsmWriter				asm = new AsmWriter(baos)) {
			
				System.err.println(wr.toString());
				
				Utils.copyStream(new StringReader(wr.toString()),asm);
				asm.flush();
				
				final Class<CallProgram>	cl = (Class<CallProgram>)new ClassLoaderWrapper().createClass("chav1961.merc.lang.merc.Test"+classNameSuffix,baos.toByteArray());
				final CallProgram			instance = (CallProgram)cl.newInstance();
				
				try(final StringWriter		result = new StringWriter();
					final PrintWriter		pw = new PrintWriter(result)) {
					
					instance.execute(world,pw);
					result.flush();
					System.err.println("Result="+result);
					return result.toString().replace("\r","").replace("\n","");
				}
			}
		} catch (Exception exc) {
			exc.printStackTrace();
			Assert.fail("Unwaited exception: "+exc);
		}
		return null;
	}
}
