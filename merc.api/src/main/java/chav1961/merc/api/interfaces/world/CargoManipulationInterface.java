package chav1961.merc.api.interfaces.world;

import chav1961.merc.api.Point;
import chav1961.merc.api.interfaces.front.ResourceClass;

public interface CargoManipulationInterface<Returned> {
	/**
	 * <p>Download cargo from the resource producer</p>
	 * @param point resource producer location
	 * @param resource resource type to download
	 * @return self
	 */
	Returned download(Point point, String resource);
	
	/**
	 * <p>Download cargo from the resource producer</p>
	 * @param point resource producer location
	 * @param resource resource type to download
	 * @return self
	 */
	Returned download(Point point, ResourceClass resource);
	
	/**
	 * <p>Download cargo from the resource producer</p>
	 * @param point resource producer location
	 * @param resource resource type to download
	 * @param amount resource amount to download
	 * @return self
	 */
	Returned download(Point point, String resource, float amount);
	
	/**
	 * <p>Download cargo from the resource producer</p>
	 * @param point resource producer location Y-coordinate
	 * @param resource resource type to download
	 * @param amount resource amount to download
	 * @return self
	 */
	Returned download(Point point, ResourceClass resource, float amount);
	
	/**
	 * <p>Ask amount of cargo in robot hands</p> 
	 * @return actual amount of cargo or zero if missing.
	 * @see download
	 */
	float howManyCargo();

	/**
	 * <p>Upload the resource to the given consumer. Robot needs to have the resource in its hands. The resource consumer must be needed in the resource</p>
	 * @param point any consumer-located coordinate
	 * @return self
	 */
	Returned upload(Point point);
	
	/**
	 * <p>Get cargo type in robot hands.</p>
	 * @return cargo type or None if missing
	 */
	ResourceClass cargoType(); 
}
