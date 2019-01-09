package chav1961.merc.api.interfaces.world;

import chav1961.merc.api.Point;
import chav1961.merc.api.Size;
import chav1961.merc.api.interfaces.front.EntityClass;

public interface SeenInterface<T> {
	/**
	 * <p>Get entity type</p>
	 * @return entity type
	 */
	EntityClass type();
	
	/**
	 * <p>Get entity subtype</p>
	 * @return
	 */
	String subtype();
	
	/**
	 * <p>Get paernt entity subtype (only BuildingComponents will use this metod)</p>
	 * @return
	 */
	String parentSubtype();
	
	/**
	 * <p>Get entity name in the game world</p>
	 * @return entity name
	 */
	String name();
	
	/**
	 * <p>Get location of this entity (anchor location for the buildings)</p>
	 * @return location
	 */
	Point location();
	
	/**
	 * <p>Get entity size</p>
	 * @return entity size
	 */
	Size size();
	
	/**
	 * <p>Get access to the entity in the game world</p>
	 * @return entity in the JavaScript
	 */
	T object();
}
