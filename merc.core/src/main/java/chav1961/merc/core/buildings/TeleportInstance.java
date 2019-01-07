package chav1961.merc.core.buildings;


import java.util.Iterator;

import chav1961.merc.api.Constants;
import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.api.exceptions.MercEnvironmentException;
import chav1961.merc.api.interfaces.front.ControlInterface;
import chav1961.merc.api.interfaces.front.Entity;
import chav1961.merc.api.interfaces.front.TickableEntity;
import chav1961.merc.core.AbstractEntity;

public class TeleportInstance extends AbstractEntity<TeleportState> implements TickableEntity {
	private static final Iterator<Entity<?>>			NULL_ITERATOR = new Iterator<Entity<?>>() {
															@Override public boolean hasNext() {return false;}
															@Override public Entity<?> next() {return null;}
														};
	private static final Iterable<Entity<?>>			NULL_COMPONENTS = new Iterable<Entity<?>>() {
															@Override public Iterator<Entity<?>> iterator() {return NULL_ITERATOR;}
														};
	
	private final TeleportControl	control = new TeleportControl(this);
	private final String			name = "teleport";
	
	TeleportInstance(final Teleport description) {
		super(Constants.TELEPORT_INSTANCE_UUID,description,TeleportState.Exists);
	}

	@Override
	public long getTimestamp() {
		return 0;
	}

	@Override
	public int getX() {
		return 0;
	}

	@Override
	public int getY() {
		return 5;
	}

	@Override
	public Entity<TeleportState> setState(final TeleportState state) throws MercEnvironmentException {
		throw new MercEnvironmentException("Teleport can't change it's state");
	}

	@Override
	public Entity<?> getOwner() throws MercEnvironmentException {
		return null;
	}

	@Override
	public Iterable<Entity<?>> getComponents() throws MercEnvironmentException {
		return NULL_COMPONENTS;
	}

	@Override
	public ControlInterface<TeleportState> getControlInterface() throws MercEnvironmentException {
		return control;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) throws MercContentException {
		throw new MercContentException("Teleport can't be renamed");
	}

	@Override
	public void tick() throws MercContentException {
		control.tick();
	}
}