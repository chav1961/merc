package chav1961.merc.api.interfaces.front;

import chav1961.merc.api.exceptions.MercEnvironmentException;
import chav1961.purelib.basic.exceptions.SyntaxException;
import chav1961.purelib.fsys.interfaces.FileSystemInterface;

/**
 * <p>This interface is used to describe any entity that can execute programs.</p>
 * @see Mercury landing project
 * @author Alexander Chernomyrdin aka chav1961
 * @since 0.0.1
 * @param <State> entity state
 */
public interface ProgrammableEntity extends TickableEntity {
	/**
	 * <p>Get current program name</p>
	 * @return "file" path in the file system with program loaded
	 */
	String getProgramName();
	
	/**
	 * <p>Load new program into entity</p>
	 * @param library file system with programs
	 * @param path path to program to load
	 * @throws SyntaxException on any errors in the program text
	 */
	void loadProgram(FileSystemInterface library, String path) throws SyntaxException;
	
	/**
	 * <p>Start program execution</p>
	 * @throws MercEnvironmentException program is not loaded or has errors
	 */
	void start() throws MercEnvironmentException;

	/**
	 * <p>Pause program execution</p>
	 * @throws MercEnvironmentException program is not loaded or has errors
	 */
	void pause() throws MercEnvironmentException;

	/**
	 * <p>Resume program execution</p>
	 * @throws MercEnvironmentException program is not loaded or has errors
	 */
	void resume() throws MercEnvironmentException;
	
	/**
	 * <p>Stop program execution</p>
	 * @throws MercEnvironmentException program is not loaded or has errors
	 */
	void stop() throws MercEnvironmentException;
}
