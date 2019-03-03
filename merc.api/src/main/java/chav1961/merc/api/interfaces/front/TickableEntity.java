package chav1961.merc.api.interfaces.front;

import chav1961.merc.api.exceptions.MercContentException;

/**
 * <p>This interface is used to describe any entity that can have name and can process background changes in it.</p>
 * @see Mercury landing project
 * @author Alexander Chernomyrdin aka chav1961
 * @since 0.0.1
 */
public interface TickableEntity {
	/**
	 * <p>Get entoty name</p>
	 * @return entity name. Can be null or empty
	 */
	@MerLan(accessibleFrom=2)
	String getName();
	
	/**
	 * <p>Set entity name</p>
	 * @param name entity name. Can be null or empty
	 * @throws MercContentException
	 */
	@MerLan(accessibleFrom=2)
	void setName(final String name) throws MercContentException;
	
	/**
	 * <p>Process program tick in the entity</p>
	 * @throws MercContentException
	 */
	void tick() throws MercContentException;
}
