package chav1961.merc.api.interfaces.front;

import java.util.UUID;

import chav1961.merc.api.exceptions.MercEnvironmentException;

/**
 * <p>This interface is used to describe any entity instance in the world.</p>
 * @see Mercury landing project
 * @author Alexander Chernomyrdin aka chav1961
 * @since 0.0.1
 * @param <State> entity state
 */

public interface Entity<State extends Enum<State>> extends EntityStateDescriptor<State>, SerializableItem {
	/**
	 * <p>Get entity Id</p>
	 * @return entity Id. Can't be null
	 */
	UUID getId();

	/**
	 * <p>Get entity class description</p>
	 * @return entity class description. Can't be null
	 */
	EntityClassDescription<State> getClassDescription();

	/**
	 * <p>Add listener to entity state changes</p>
	 * @param listener listener to add. Can't be null
	 * @return self
	 */
	Entity<State> addStateChangedListener(StateChangedListener listener);
	
	/**
	 * <p>Remove listener from list</p>
	 * @param listener listener to remove. Can't be null
	 * @return self
	 */
	Entity<State> removeStateChangedListener(StateChangedListener listener);
	
	/**
	 * <p>Was the entity modified since the given timestamp</p>
	 * @param timestamp timestamp to check. IS relative to starting process
	 * @return tiue if was modified
	 */
	boolean wasModifiedAfter(long timestamp);
	
	/**
	 * <p>Get rpesious entity state</p>
	 * @return previous entity state. Can be null
	 */
	EntityStateDescriptor<State> getPreviousState();
	
	/**
	 * <p>Clear all modifications before the given timestamp</p>
	 * @param timestamp timestamp to clear modification before
	 * @return self
	 */
	Entity<State> clearModification(long timestamp);
	
	/**
	 * <p>Move x-coordinate of the bottom-left corner of the entity to the given location</p>
	 * @param x x-coordinate of the bottom-left corner
	 * @return self
	 * @throws MercEnvironmentException
	 */
	Entity<State> setX(int x) throws MercEnvironmentException;

	/**
	 * <p>Move y-coordinate of the bottom-left corner of the entity to the given location</p>
	 * @param y y-coordinate of the bottom-left corner
	 * @return self
	 * @throws MercEnvironmentException
	 */
	Entity<State> setY(int y) throws MercEnvironmentException;

	/**
	 * <p>Change item width</p>
	 * @param width item width
	 * @return self
	 * @throws MercEnvironmentException
	 */
	Entity<State> setWidth(int width) throws MercEnvironmentException;

	/**
	 * <p>Change item height</p>
	 * @param height item height
	 * @return self
	 * @throws MercEnvironmentException
	 */
	Entity<State> setHeight(int height) throws MercEnvironmentException;
	
	/**
	 * <p>Change item state</p>
	 * @param state item state. Can't be null
	 * @return self
	 * @throws MercEnvironmentException
	 */
	Entity<State> setState(State state) throws MercEnvironmentException;
	
	/**
	 * <p>Get item owner</p>
	 * @return current item owner or null
	 * @throws MercEnvironmentException
	 */
	Entity<?> getOwner() throws MercEnvironmentException;
	
	/**
	 * <p>Get item components</p>
	 * @return current item components. Can be empty but not null. Item list can be shorter then {@linkplain EntityClassDescription#components()} returns
	 * @throws MercEnvironmentException
	 */
	Iterable<Entity<?>> getComponents() throws MercEnvironmentException;
	
	/**
	 * <p>Get control interface of the entity</p>
	 * @return control interface. Can be null if the item is not controller programmatically
	 * @throws MercEnvironmentException
	 */
	ControlInterface<State> getControlInterface() throws MercEnvironmentException;

	/**
	 * <p>Fires listeners that entity state changed</p>  
	 * @param previousState previous entity state. Can be null
	 * @param changes type of changes (see {@linkplain StateChangedListener} constants)
	 * @return self
	 * @throws MercEnvironmentException
	 */
	Entity<State> fireStateChanged(EntityStateDescriptor<State> previousState, int changes) throws MercEnvironmentException;
}
