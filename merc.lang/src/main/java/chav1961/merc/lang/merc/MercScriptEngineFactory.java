package chav1961.merc.lang.merc;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.script.ScriptEngine;

import chav1961.purelib.basic.AbstractScriptEngineFactory;
import chav1961.purelib.basic.MimeType;
import chav1961.purelib.basic.exceptions.MimeParseException;


public class MercScriptEngineFactory extends AbstractScriptEngineFactory {
	public static final String	ENGINE_NAME = "MerLan";
	public static final String	ENGINE_VERSION = "0.0.1";
	public static final String	ENGINE_MIME_SUBTYPE = "merlan";
	public static final String	ENGINE_MIME_1_TYPE = "text";
	public static final String	ENGINE_MIME_2_TYPE = "application";
	public static final String	LANGUAGE = "MerLan";
	public static final String	LANGUAGE_VERSION = "0.1";
	public static final String	LANGUAGE_EXT = "MerLan";

	public MercScriptEngineFactory() throws MimeParseException {
		super(ENGINE_NAME, ENGINE_VERSION, Arrays.asList(new MimeType(ENGINE_MIME_1_TYPE, ENGINE_MIME_SUBTYPE),new MimeType(ENGINE_MIME_2_TYPE, ENGINE_MIME_SUBTYPE)), LANGUAGE, LANGUAGE_VERSION, Arrays.asList(LANGUAGE_EXT));
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
		try {
			return new MercScriptEngine(this);
		} catch (IllegalArgumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
