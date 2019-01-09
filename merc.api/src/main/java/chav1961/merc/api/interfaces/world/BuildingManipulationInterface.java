package chav1961.merc.api.interfaces.world;

import chav1961.merc.api.Point;
import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.api.exceptions.MercEnvironmentException;

/**
 * <p>This interface is used to describe manipulations with the buildings.</p>
 * @see Mercury landing project
 * @author Alexander Chernomyrdin aka chav1961
 * @since 0.0.1
 * @param <State> returned type (to exclude type conversions in the chained calls).
 */
public interface BuildingManipulationInterface<Returned> {
	/**
	 * <p>Mount building component at the given point. Place to mount need to be free and has a basement. If all the components for the given building will be mount, the building will be created automatically.</p>
	 * @param point component coordinate
	 * @return self
	 */
	Returned mount(Point point);
	
	/**
	 * <p>Prepare basement for the building component</p>
	 * @param point basement
	 * @return self
	 * @throws MercContentException 
	 * @throws MercEnvironmentException 
	 */
	Returned prepareBasement(Point point) throws MercContentException, MercEnvironmentException;
	
	/**
	 * <p>Prepare basement 2x2 for the building component</p>
	 * @param point basement left upper corner coordinate
	 * @return self
	 */
	Returned prepareBasement2x2(Point point);
	
	/**
	 * <p>Unmount building component. Unmounting component at the left top corner of building will destroy the building. All basement under the component will be destroyed too</p>
	 * @param point component coordinate
	 * @return self
	 */
	Returned unmount(Point point);
}
