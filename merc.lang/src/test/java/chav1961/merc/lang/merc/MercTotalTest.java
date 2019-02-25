package chav1961.merc.lang.merc;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.junit.Assert;
import org.junit.Test;

import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.api.exceptions.MercEnvironmentException;
import chav1961.merc.lang.merc.interfaces.CallProgram;

public class MercTotalTest {
	@Test
	public void intTest() throws ScriptException, MercContentException, MercEnvironmentException, InstantiationException, IllegalAccessException, IOException {
		Assert.assertEquals("20 \n",execute("var x : int(20); print x"));
		Assert.assertEquals("-20 \n",execute("var x : int(20); print -x"));
		Assert.assertEquals("0 \n",execute("var x : int(20); print x++"));
		Assert.assertEquals("0 \n",execute("var x : int(20); print x--"));
		Assert.assertEquals("0 \n",execute("var x : int(20); print ++x"));
		Assert.assertEquals("0 \n",execute("var x : int(20); print --x"));
		Assert.assertEquals("0 \n",execute("var x : int(20); print x & 10"));
		Assert.assertEquals("30 \n",execute("var x : int(20); print x | 10"));
		Assert.assertEquals("-21 \n",execute("var x : int(20); print ~x"));
		Assert.assertEquals("80 \n",execute("var x : int(20); print x << 2"));
		Assert.assertEquals("5 \n",execute("var x : int(20); print x >> 2"));
		Assert.assertEquals("40 \n",execute("var x : int(20); print 2 * x;"));
		Assert.assertEquals("4 \n",execute("var x : int(20); print x / 5;"));
		Assert.assertEquals("8 \n",execute("var x : int(20); print x % 12;"));
		Assert.assertEquals("22 \n",execute("var x : int(20); print x + 2;"));
		Assert.assertEquals("13 \n",execute("var x : int(20); print x - 7;"));
		Assert.assertEquals("true \n",execute("var x : int(20); print x > 10;"));
		Assert.assertEquals("true \n",execute("var x : int(20); print x >= 10;"));
		Assert.assertEquals("true \n",execute("var x : int(20); print x < 30;"));
		Assert.assertEquals("true \n",execute("var x : int(20); print x <= 30;"));
		Assert.assertEquals("true \n",execute("var x : int(20); print x == 20;"));
		Assert.assertEquals("true \n",execute("var x : int(20); print x != 21;"));
		Assert.assertEquals("true \n",execute("var x : int(20); print x in 10,20..22;"));
	}
	
	private String execute(final String source) {
		final ScriptEngineManager	mgr = new ScriptEngineManager();
		final MercScriptEngine		engine = (MercScriptEngine)mgr.getEngineByName("MerLan");
		
		try(final Writer				wr = new StringWriter();
			final PrintWriter			pwr = new PrintWriter(wr)) {
			final Class<CallProgram>	built = (Class<CallProgram>)engine.eval(source);
			
			built.newInstance().execute(null,pwr);
			pwr.flush();
			return wr.toString().replace("\r","");
		} catch (IOException | MercContentException | MercEnvironmentException | InstantiationException | IllegalAccessException | ScriptException e) {
			Assert.fail(e.getMessage());
			return null;
		}
	}
}
