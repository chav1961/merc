package chav1961.merc.api.interfaces.front;

import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.api.exceptions.MercEnvironmentException;

/**
 * <p>This interface is used to describe compiled program to execute.</p>
 * @see Mercury landing project
 * @author Alexander Chernomyrdin aka chav1961
 * @since 0.0.1
 * @param <State> entity state
 */
public interface ProgramExecutor {
	/**
	 * <p>Start program execution. This call is synchronous and will return after terminate execution only</p>
	 * @param world world to execute program in
	 * @throws MercEnvironmentException program is not loaded or has errors
	 */
	void start(World world) throws MercEnvironmentException;

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
	
	/**
	 * <p>Process program tick in the entity</p>
	 * @throws MercContentException
	 */
	void tick() throws MercContentException;
}
