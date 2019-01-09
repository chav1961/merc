package chav1961.merc.api.interfaces.world;

import chav1961.merc.api.Point;

public interface PipeManipulationInterface <Returned> {
	/**
	 * <p>Begin excavation for the existent pipe/feeder. Robot need to have nothing or the same cable/feeder piece in it's hands</p>
	 * @param pipeType pipe type to start excavation
	 * @return self
	 */
	Returned beginExcavation(String pipeType);

	/**
	 * <p>Begin trace for new pipe/feeder. Robot need to have pipe piece in it's hands</p>
	 * @return self
	 */
	Returned beginTrace();
	
	/**
	 * <p>Connect pipe/feeder to the building or two pieces of pipes. Pipes will be joined. The second connection point is the point where robot is located now</p>
	 * @param point component location
	 * @return self
	 */
	Returned connect(Point point);
	
	/**
	 * <p>Connect pipe/feeder to the building or two pieces of pipes. Pipes will be joined.</p>
	 * @param pointFrom from component location
	 * @param pointTo to component location coordinate
	 * @return self
	 */
	Returned connect(Point pointFrom, Point pointTo);
	
	/**
	 * <p>Cut pipe on two pieces and make connectors at ends of the pieces. Pipe must be disconnected from pipe content producers! The second cutting point is the point where robot is located now</p>
	 * @param point component location
	 * @return self
	 */
	Returned cut(Point point);
	
	/**
	 * <p>Disconnect building from the pipe. The second disconnection point is the point where robot is located now</p> 
	 * @param point from component location
	 * @return self
	 */
	Returned disconnect(Point point);
	
	/**
	 * <p>Disconnect building from the pipe.</p> 
	 * @param fromPoint from component location
	 * @param toPoint second component location
	 * @return self
	 */
	Returned disconnect(Point fromPoint, Point toPoint);
	
	/**
	 * <p>End excavation pipe/feeder.</p> 
	 * @return self
	 * @see beginTrace
	 */
	Returned endExcavation();
	
	/**
	 * <p>End tracing pipe/feeder and build the pipe/feeder. You must have enough pipe/cable piece in robot hands to make it</p> 
	 * @return self
	 * @see beginTrace
	 */
	Returned endTrace();
}
