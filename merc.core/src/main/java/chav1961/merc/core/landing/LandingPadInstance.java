package chav1961.merc.core.landing;

import java.util.Iterator;
import java.util.UUID;

import chav1961.merc.api.exceptions.MercEnvironmentException;
import chav1961.merc.api.interfaces.front.ControlInterface;
import chav1961.merc.api.interfaces.front.Entity;
import chav1961.merc.api.interfaces.front.EntityClassDescription;
import chav1961.merc.core.AbstractEntity;

public class LandingPadInstance extends AbstractEntity<LandingPadState> {
	private static final Iterator<Entity<?>>			NULL_ITERATOR = new Iterator<Entity<?>>() {
															@Override public boolean hasNext() {return false;}
															@Override public Entity<?> next() {return null;}
														};
	private static final Iterable<Entity<?>>			NULL_COMPONENTS = new Iterable<Entity<?>>() {
															@Override public Iterator<Entity<?>> iterator() {return NULL_ITERATOR;}
														};

	LandingPadInstance(final UUID entityId, final EntityClassDescription<LandingPadState> classDescription, final LandingPadState initialState) throws NullPointerException {
		super(entityId, classDescription, initialState);
		this.x = 0;		
		this.y = 0;
		this.width = classDescription.getWidth();
		this.height = classDescription.getHeight();
	}

	@Override
	public Entity<LandingPadState> setState(final LandingPadState state) throws MercEnvironmentException {
		throw new MercEnvironmentException("Landing pad can't change it's state");
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
	public ControlInterface<LandingPadState> getControlInterface() throws MercEnvironmentException {
		return null;
	}
}
