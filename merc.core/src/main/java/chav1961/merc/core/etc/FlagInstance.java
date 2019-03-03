package chav1961.merc.core.etc;

import java.util.Iterator;
import java.util.UUID;

import chav1961.merc.api.exceptions.MercEnvironmentException;
import chav1961.merc.api.interfaces.front.ControlInterface;
import chav1961.merc.api.interfaces.front.Entity;
import chav1961.merc.api.interfaces.front.EntityClassDescription;
import chav1961.merc.api.interfaces.front.World;
import chav1961.merc.core.AbstractEntity;

public class FlagInstance extends AbstractEntity<FlagState> {
	private static final Iterator<Entity<?>>			NULL_ITERATOR = new Iterator<Entity<?>>() {
															@Override public boolean hasNext() {return false;}
															@Override public Entity<?> next() {return null;}
														};
	private static final Iterable<Entity<?>>			NULL_COMPONENTS = new Iterable<Entity<?>>() {
															@Override public Iterator<Entity<?>> iterator() {return NULL_ITERATOR;}
														};

	FlagInstance(UUID entityId, EntityClassDescription<FlagState> classDescription, FlagState initialState) throws NullPointerException {
		super(entityId, classDescription, initialState);
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
	public ControlInterface<FlagState> getControlInterface() throws MercEnvironmentException {
		return null;
	}
}
