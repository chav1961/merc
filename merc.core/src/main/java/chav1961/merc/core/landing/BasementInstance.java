package chav1961.merc.core.landing;

import java.util.Iterator;
import java.util.UUID;

import chav1961.merc.api.exceptions.MercEnvironmentException;
import chav1961.merc.api.interfaces.front.ControlInterface;
import chav1961.merc.api.interfaces.front.Entity;
import chav1961.merc.api.interfaces.front.EntityClassDescription;
import chav1961.merc.core.AbstractEntity;

public class BasementInstance extends AbstractEntity<BasementState> {
	private static final Iterator<Entity<?>>			NULL_ITERATOR = new Iterator<Entity<?>>() {
															@Override public boolean hasNext() {return false;}
															@Override public Entity<?> next() {return null;}
														};
    private static final Iterable<Entity<?>>			NULL_COMPONENTS = new Iterable<Entity<?>>() {
															@Override public Iterator<Entity<?>> iterator() {return NULL_ITERATOR;}
														};

	BasementInstance(final UUID entityId, final EntityClassDescription<BasementState> classDescription, final BasementState initialState) throws NullPointerException {
    	super(entityId, classDescription, initialState);
		this.width = classDescription.getWidth();
		this.height = classDescription.getHeight();
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
	public ControlInterface<BasementState> getControlInterface() throws MercEnvironmentException {
		return null;
	}
}
