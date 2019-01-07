package chav1961.merc.core.buildings;

import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.api.interfaces.front.ControlInterface;
import chav1961.merc.api.interfaces.front.Entity;

public class TeleportControl implements ControlInterface<TeleportState> {
	private final TeleportInstance 	instance;

	public TeleportControl(final TeleportInstance instance) {
		this.instance = instance;
	}

	@Override
	public Entity<TeleportState> getEntity() {
		return instance;
	}
	
	void tick() throws MercContentException {
		// TODO Auto-generated method stub
	}
}