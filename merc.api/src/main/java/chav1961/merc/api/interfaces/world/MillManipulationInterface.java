package chav1961.merc.api.interfaces.world;

import chav1961.merc.api.Point;
import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.api.exceptions.MercEnvironmentException;

public interface MillManipulationInterface<Returned> {
	/**
	 * <p>Destroy a mill</p>
	 * @param point mill location
	 * @return self
	 * @throws MercContentException 
	 * @throws MercEnvironmentException 
	 */
	Returned destroy(Point point) throws MercContentException, MercEnvironmentException;

	/**
	 * <p>Has any probe in the hands</p>
	 * @return true if yes
	 */
	boolean hasAnyProbe();

	/**
	 * <p>Build a mill. Mill need to be located on the known resource</p>
	 * @param point mill coordinate
	 * @param millType resource mill type (simple class name for the mill). Need to appropriate to the resource type.
	 * @param millName mill name to register it in the game repo.
	 * @return self
	 * @throws MercContentException 
	 * @throws MercEnvironmentException 
	 */
	Returned mill(Point point, String millType, String millName) throws MercContentException, MercEnvironmentException;
	
	/**
	 * <p>Get probe from any cell to send it to the laboratory</p>
	 * @param point point to probe
	 * @return self
	 * @throws MercEnvironmentException 
	 * @throws MercContentException 
	 */
	Returned probe(Point point) throws MercEnvironmentException, MercContentException;
	
	/**
	 * <p>Send probe to laboratory. If the probe contains any resource, opens this resource on the game world map.</p> 
	 * @param point any teleport-located point
	 * @return analyzing results. Always is non-null, but can contain resource type = None (nothing interesting was found)
	 * @throws MercContentException 
	 * @throws MercEnvironmentException 
	 */
	ResourceDescriptorInterface send2Laboratory(Point point) throws MercEnvironmentException, MercContentException;
}
