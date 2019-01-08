package chav1961.merc.lang.merc;

import java.util.Arrays;
import java.util.List;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.script.ScriptEngine;

import chav1961.purelib.basic.AbstractScriptEngineFactory;


public class MercScriptEngineFactory extends AbstractScriptEngineFactory {
	public static final String	ENGINE_NAME = "Mercury script";
	public static final String	ENGINE_VERSION = "0.0.1";
	public static final String	ENGINE_MIME = "text/merc-script";
	public static final String	LANGUAGE = "merc";
	public static final String	LANGUAGE_VERSION = "0.1";

	public MercScriptEngineFactory(List<String> languageSynonyms) throws MimeTypeParseException {
		super(ENGINE_NAME, ENGINE_VERSION, Arrays.asList(new MimeType(ENGINE_MIME)), LANGUAGE, LANGUAGE_VERSION, Arrays.asList(LANGUAGE));
	}

	@Override
	public String getMethodCallSyntax(final String obj, final String m, final String... args) {
		final StringBuilder	sb = new StringBuilder();
		char				prefix = '(';
		
		sb.append(obj).append('.').append(m);
		if (args.length > 0) {
			for (String parm : args) {
				sb.append(prefix).append(parm);
				prefix = ',';
			}
		}
		else {
			sb.append(prefix);
		}
		return sb.append(')').toString();
	}

	@Override
	public String getOutputStatement(final String toDisplay) {
		return "print \"" + toDisplay +"\"";
	}

	@Override
	public String getProgram(final String... statements) {
		final StringBuilder	sb = new StringBuilder(); 
		
		for (String item : statements) {
			sb.append(item).append(";\n");
		}
		return sb.toString();
	}

	@Override
	public ScriptEngine getScriptEngine() {
		return new MercScriptEngine(this);
	}
}
