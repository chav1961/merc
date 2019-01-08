package chav1961.merc.lang.merc;

import java.util.Arrays;
import java.util.List;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.script.ScriptEngine;

import chav1961.purelib.basic.AbstractScriptEngineFactory;


public class MercScriptEngineFactory extends AbstractScriptEngineFactory {
	public static final String	ENGINE_NAME = "MerLan (MERcurian LANguage)";
	public static final String	ENGINE_VERSION = "0.0.1";
	public static final String	ENGINE_MIME_1 = "text/merlan";
	public static final String	ENGINE_MIME_2 = "application/merlan";
	public static final String	LANGUAGE = "MerLan";
	public static final String	LANGUAGE_VERSION = "0.1";
	public static final String	LANGUAGE_EXT = "mer";

	public MercScriptEngineFactory(List<String> languageSynonyms) throws MimeTypeParseException {
		super(ENGINE_NAME, ENGINE_VERSION, Arrays.asList(new MimeType(ENGINE_MIME_1),new MimeType(ENGINE_MIME_2)), LANGUAGE, LANGUAGE_VERSION, Arrays.asList(LANGUAGE_EXT));
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
