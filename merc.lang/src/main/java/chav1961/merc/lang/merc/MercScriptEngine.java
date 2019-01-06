package chav1961.merc.lang.merc;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;

import javax.script.ScriptEngineFactory;

import chav1961.purelib.basic.AbstractScriptEngine;
import chav1961.purelib.basic.exceptions.SyntaxException;

public class MercScriptEngine extends AbstractScriptEngine {

	protected MercScriptEngine(ScriptEngineFactory factory) {
		super(factory);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void processLineInternal(int lineNo, char[] data, int from, int length) throws IOException, SyntaxException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void afterCompile(Reader reader, OutputStream os) throws IOException {
		// TODO Auto-generated method stub
		
	}
}
