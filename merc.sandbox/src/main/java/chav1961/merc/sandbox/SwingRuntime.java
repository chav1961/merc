package chav1961.merc.sandbox;

import chav1961.merc.api.interfaces.front.EntityClass;
import chav1961.merc.api.interfaces.front.EntityClassDescription;
import chav1961.merc.api.interfaces.front.ResourceClass;
import chav1961.merc.api.interfaces.front.ResourceDescription;
import chav1961.merc.api.interfaces.front.RuntimeInterface;

public class SwingRuntime implements RuntimeInterface{

	@Override
	public TeleportInterface teleport() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MarketInterface market() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BankInterface bank() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<ResourceDescription> resources() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<ResourceDescription> resources(ResourceClass clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<EntityClassDescription<?>> entities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<EntityClassDescription<?>> entities(EntityClass clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearTotal() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getTotalBought() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getTotalSold() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getCurrentCache() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getLowWater() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getHighWater() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void complete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void abort(Throwable t, String format, Object... parameters) {
		// TODO Auto-generated method stub
		
	}
}
