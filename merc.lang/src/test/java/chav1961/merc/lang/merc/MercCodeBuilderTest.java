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
import chav1961.merc.lang.merc.interfaces.CallProgram;
import chav1961.merc.lang.merc.interfaces.CharDataOutput;
import chav1961.purelib.basic.ClassLoaderWrapper;
import chav1961.purelib.basic.OrdinalSyntaxTree;
import chav1961.purelib.basic.Utils;
import chav1961.purelib.basic.interfaces.SyntaxTreeInterface;
import chav1961.purelib.streams.char2byte.AsmWriter;

public class MercCodeBuilderTest {
	@FunctionalInterface
	interface Insertion {
		void process(CharDataOutput out);
	}
	
	@Test
	public void basicTest() throws IOException, URISyntaxException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, MercContentException, MercEnvironmentException {
		final SyntaxTreeInterface<?>	names = new OrdinalSyntaxTree<>();
		final MercClassRepo				classes = new MercClassRepo(names,0);
		final MercNameRepo				vars = new MercNameRepo();
		final SyntaxTreeNode			root = new SyntaxTreeNode();
		
		execute(null,(out)->{
			try{MercCodeBuilder.printPrintOperator(root,names,classes,vars,out);
			} catch (IOException e) {
				Assert.fail("Unwaited exception: "+e);
			}
		});
	}
	
	private String execute(final World world, final Insertion ins) {
		try(final StringWriter			wr = new StringWriter();
			final WriterCharDataOutput	out = new WriterCharDataOutput(wr)) {
		
			out.writeln(Utils.loadCharsFromURI(MercCodeBuilderTest.class.getResource("").toURI()));
			ins.process(out);
			out.writeln(Utils.loadCharsFromURI(MercCodeBuilderTest.class.getResource("").toURI()));
			out.flush();
			
			try(final ByteArrayOutputStream	baos = new ByteArrayOutputStream();
				final AsmWriter				asm = new AsmWriter(baos)) {
			
				Utils.copyStream(new StringReader(wr.toString()),asm);
				asm.flush();
				
				final Class<CallProgram>	cl = (Class<CallProgram>)new ClassLoaderWrapper().createClass("",baos.toByteArray());
				final CallProgram			instance = (CallProgram)cl.newInstance();
				
				try(final StringWriter		result = new StringWriter();
					final PrintWriter		pw = new PrintWriter(result)) {
					
					instance.execute(world,pw);
					result.flush();
					return result.toString();
				}
			}
		} catch (Exception exc) {
			Assert.fail("Unwaited exception: "+exc);
		}
		return null;
	}
}
