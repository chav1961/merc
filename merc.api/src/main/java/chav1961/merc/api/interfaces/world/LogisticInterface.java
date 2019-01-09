package chav1961.merc.api.interfaces.world;

import chav1961.merc.api.Point;
import chav1961.merc.api.interfaces.front.Entity;

public interface LogisticInterface <Returned> {
	/**
	 * <p>Take building/pipe component into robot hands</p> 
	 * @param point component location
	 * @return self
	 */
	Returned capture(Point point);
	
	/**
	 * <p>Place something was captured earlier</p>
	 * @param point place coordinate
	 * @return self
	 */
	Returned place(Point point);
	
	/**
	 * <p>Has any captured in the hands</p>
	 * @return true if yes
	 */
	boolean hasAnyCaptured();
	
	/**
	 * <p>What entity was captured?</p>
	 * @return captured entity or null if missing
	 */
	Entity<?> whatCaptured();
}
