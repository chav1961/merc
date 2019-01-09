package chav1961.merc.api.interfaces.world;

import chav1961.merc.api.Area;
import chav1961.merc.api.Point;
import chav1961.merc.api.Track;

public interface SharedInterface<Returned> {
	public interface LocksListManipulation {
		/**
		 * <p>Add lock</p>
		 * @param point lock point
		 */
		void addLock(Point point);
		
		/**
		 * <p>Remove lock></p>
		 * @param point point to remove
		 */
		void removeLock(Point point);
		
		/**
		 * <p>Is the point locked
		 * @param point point to test
		 * @return true if yes
		 */
		boolean isLocked(Point point);
		
		/**
		 * <p>Get list of the locked points</p>
		 * @return list of the locked points. Can be empty, but not null
		 */
		Iterable<Point> getLocks();
	}
	
	/**
	 * <p>Lock the given set of points against occupation by any entity. The only way to avoid crushes when the other robots exist. Operation will wait until all the point becomes available</p>
	 * @param coords points to locate
	 * @return self
	 * @see unlock 
	 */
	Returned lock(Point... coords);

	/**
	 * <p>Lock the given set of points against occupation by any entity. The only way to avoid crushes when the other robots exist. Operation will wait until all the point becomes available</p>
	 * @param coords points to locate
	 * @return self
	 * @see unlock 
	 */
	Returned lock(Area... coords);
	
	/**
	 * <p>Lock the given set of points against occupation by any entity. The only way to avoid crushes when the other robots exist. Operation will wait until all the point becomes available</p>
	 * @param coords points to locate
	 * @return self
	 * @see unlock 
	 */
	Returned lock(Track coords);
	
	/**
	 * <p>Unlock the given set of points. The points must be locked earlier</p>
	 * @param coords points to unlock
	 * @return self
	 * @see lock
	 */
	Returned unlock(Point... coords);

	/**
	 * <p>Unlock the given set of points. The points must be locked earlier</p>
	 * @param coords points to unlock
	 * @return self
	 * @see lock
	 */
	Returned unlock(Track coords);
	
	/**
	 * <p>Lock the given area. The area must be locked earlier</p>
	 * @param area area location
	 * @return self
	 * @see lockArea
	 */
	Returned unlock(Area area);
}
