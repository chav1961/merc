package chav1961.merc.api.interfaces.world;

import chav1961.merc.api.Point;

public interface UniversalRobotInterface extends MillManipulationInterface<UniversalRobotInterface>
							, CommunicationInterface<UniversalRobotInterface>
							, PipeManipulationInterface<UniversalRobotInterface>
							, CargoManipulationInterface<UniversalRobotInterface>
							, DiscoveryInterface<UniversalRobotInterface>
							, LogisticInterface<UniversalRobotInterface>
							, BuildingManipulationInterface<UniversalRobotInterface> {
	/**
	* <p>Ask connected entity. Use logon to connect to the item</p> 
	* @return JavaScript interface associated with the connected entity
	* @see logon, logoff
	*/
	Object ask();
	
	/**
	* <p>Excavate resource by robot</p>
	* @param x resource location X-coordinate
	* @param y resource location Y-coordinate
	* @return self
	* @see upload
	*/
	UniversalRobotInterface excavate(final int x, final int y);
	
	/**
	* <p>Log off from the entity was logged on earlier</p> 
	* @return self
	* @see logon
	*/
	UniversalRobotInterface logoff();
	
	/**
	* <p>Log on to the game entity. Game entity need to support logging</p>
	* @param x entity X-coordinate
	* @param y entity Y-coordinate
	* @return self
	* @see logoff, ask
	*/
	UniversalRobotInterface logon(Point point);
	
	/**
	* <p>Set the name for the given entity. Entity must supports naming. Named entity will be accessible by rt.name(...) call in the game.</p>
	* @param x any entity-located X-coordinate
	* @param y any entity-located Y-coordinate
	* @param name any unique & valid JavaScript name
	* @return self
	*/
	UniversalRobotInterface assignName(Point point, String name);
}
