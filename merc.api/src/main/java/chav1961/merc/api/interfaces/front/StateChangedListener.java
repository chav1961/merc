package chav1961.merc.api.interfaces.front;

import chav1961.merc.api.exceptions.MercContentException;

/**
 * <p>This interface is used to describe change listener for the entity.</p>
 * @see Mercury landing project
 * @author Alexander Chernomyrdin aka chav1961
 * @since 0.0.1
 * @param <State> entity state
 */
@FunctionalInterface
public interface StateChangedListener {
	int 	X_CHANGED = 0b00000001;
	int 	Y_CHANGED = 0b00000010;
	int 	WIDTH_CHANGED = 0b00000100;
	int 	HEIGHT_CHANGED = 0b00001000;
	int 	STATE_CHANGED = 0b00010000;
	int 	TOTAL_CHANGED = 0b00100000;
	
	/**
	 * <p>Process changing state</p>
	 * @param entity entity changed
	 * @param oldState old entity state
	 * @param newState new entity state
	 * @param flags what changed in the entity
	 * @throws MercContentException
	 */
	<State extends Enum<State>> void stateChanged(Entity<State> entity, EntityStateDescriptor<State> oldState, EntityStateDescriptor<State> newState, int flags) throws MercContentException;
}
