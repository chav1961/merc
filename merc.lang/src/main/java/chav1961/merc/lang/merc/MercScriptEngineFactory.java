package chav1961.merc.lang.merc;

import java.util.List;

import javax.activation.MimeType;
import javax.script.ScriptEngine;

import chav1961.purelib.basic.AbstractScriptEngineFactory;

public class MercScriptEngineFactory extends AbstractScriptEngineFactory {

	protected MercScriptEngineFactory(String engineName, String engineVersion, List<MimeType> engineMIMEs, String language, String languageVersion, List<String> languageSynonyms) {
		super(engineName, engineVersion, engineMIMEs, language, languageVersion, languageSynonyms);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getMethodCallSyntax(String obj, String m, String... args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOutputStatement(String toDisplay) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProgram(String... statements) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScriptEngine getScriptEngine() {
		// TODO Auto-generated method stub
		return null;
	}
}
