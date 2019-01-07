package chav1961.merc.core.robots;

import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.api.interfaces.front.ControlInterface;
import chav1961.merc.api.interfaces.front.Entity;

public class UniversalRobotControl implements ControlInterface<UniversalRobotState> {
	private final UniversalRobotInstance 	instance;

	public UniversalRobotControl(final UniversalRobotInstance instance) {
		this.instance = instance;
	}

	@Override
	public Entity<UniversalRobotState> getEntity() {
		return instance;
	}
	
	void tick() throws MercContentException {
	}
}
