package chav1961.merc.api.interfaces.world;

import chav1961.merc.api.Point;
import chav1961.merc.api.Track;
import chav1961.merc.api.interfaces.front.EntityClass;

public interface DiscoveryInterface<Returned> extends SharedInterface<Returned> {
	/**
	 * <p>Move robot at the given point. Trace to the point builds by the robot self. Trace is predictable only in parallel/meridian and diagonal directions, otherwise robot moves by own rules. When robot is between beginTrace and endTrace command, in can moves in parallels/meridians only!</p> 
	 * @param x destination X-coordinate
	 * @param y destination Y-coordinate
	 * @return self
	 * @see beginTrace, endTrace
	 */
	Returned moveTo(Point... to);

	/**
	 * <p>Move robot at the given point. Trace to the point builds by the robot self. Trace is predictable only in parallel/meridian and diagonal directions, otherwise robot moves by own rules. When robot is between beginTrace and endTrace command, in can moves in parallels/meridians only!</p> 
	 * @param x destination X-coordinate
	 * @param y destination Y-coordinate
	 * @return self
	 * @see beginTrace, endTrace
	 */
	Returned moveTo(Point to, Track road);
	
	/**
	 * <p>See all located at the given point</p>
	 * @param point point to see
	 * @return all was seen. Can be empty but not null. 
	 */
	<T> SeenInterface<T>[] see(Point point);
	
	/**
	 * <p>See all located at the given point with the given type</p>
	 * @param point point to see
	 * @param type entity type awaited
	 * @return all was seen. Can be empty but not null. 
	 */
	<T> SeenInterface<T>[] see(Point point, EntityClass type);		
	
	/**
	 * <p>Get actual robot location</p>
	 * @return actual location
	 */
	Point where(); 
}
