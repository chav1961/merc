package chav1961.merc.lang.merc.interfaces;

import java.io.PrintWriter;

import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.api.exceptions.MercEnvironmentException;
import chav1961.merc.api.interfaces.front.World;

public interface CallProgram {
	int execute(World world, PrintWriter writer) throws MercContentException, MercEnvironmentException;
}
