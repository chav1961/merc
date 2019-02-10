package chav1961.merc.sandbox;

import chav1961.purelib.basic.ArgParser;

public class ApplicationArgParser extends ArgParser {
	public ApplicationArgParser() {
		super(new URIArg("conn",false,false,"connect to host")
			 ,new IntegerArg("helpport",false,"help system port",0));
	}
}
