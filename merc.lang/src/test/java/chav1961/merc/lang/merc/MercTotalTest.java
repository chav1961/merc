package chav1961.merc.lang.merc;

import java.io.PrintWriter;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.junit.Test;

import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.api.exceptions.MercEnvironmentException;
import chav1961.merc.lang.merc.interfaces.CallProgram;

public class MercTotalTest {
	@Test
	public void lifeCycleTest() throws ScriptException, MercContentException, MercEnvironmentException, InstantiationException, IllegalAccessException {
		final ScriptEngineManager	mgr = new ScriptEngineManager();
		final MercScriptEngine		engine = (MercScriptEngine)mgr.getEngineByName("MerLan");
		final Class<CallProgram>	built = (Class<CallProgram>)engine.eval("var x : int(20); print \"X=\",x;");
		
		try(final PrintWriter	wr = new PrintWriter(System.out)) {
			built.newInstance().execute(null,wr);
		}
	}
}
