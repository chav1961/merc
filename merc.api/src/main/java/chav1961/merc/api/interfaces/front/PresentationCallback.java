package chav1961.merc.api.interfaces.front;

import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.api.exceptions.MercEnvironmentException;

/**
 * <p>This interface is used to describe presentation callback.</p>
 * @see Mercury landing project
 * @author Alexander Chernomyrdin aka chav1961
 * @since 0.0.1
 * @param <State> entity state
 */
@FunctionalInterface
public interface PresentationCallback<State extends Enum<State>> {
	/**
	 * <p>Draw current state of the entity</p>
	 * @param world world when the entity locates
	 * @param entity entity to draw 
	 * @param previousState previous entity state. Can be null
	 * @param desc descriptor of the entity class
	 * @return true of success
	 * @throws MercContentException
	 * @throws MercEnvironmentException
	 */
	boolean draw(World world, Entity<State> entity, EntityStateDescriptor<State> previousState, EntityClassDescription<State> desc) throws MercContentException, MercEnvironmentException;
}
