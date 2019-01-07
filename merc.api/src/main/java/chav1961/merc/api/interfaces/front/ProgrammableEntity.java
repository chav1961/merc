package chav1961.merc.api.interfaces.front;

import chav1961.merc.api.exceptions.MercContentException;
import chav1961.purelib.basic.exceptions.SyntaxException;
import chav1961.purelib.fsys.interfaces.FileSystemInterface;

/**
 * <p>This interface is used to describe any entity that can execute programs.</p>
 * @see Mercury landing project
 * @author Alexander Chernomyrdin aka chav1961
 * @since 0.0.1
 * @param <State> entity state
 */
public interface ProgrammableEntity extends TickableEntity, ProgramExecutor {
	/**
	 * <p>Get current program name</p>
	 * @return "file" path in the file system with program loaded
	 */
	String getProgramName();
	
	/**
	 * <p>Load new program into entity</p>
	 * @param world world to execute program in
	 * @param library file system with programs
	 * @param path path to program to load
	 * @throws SyntaxException on any errors in the program text
	 * @throws MercContentException on any other problems
	 */
	void loadProgram(World world, FileSystemInterface library, String path) throws MercContentException, SyntaxException;
	
}
