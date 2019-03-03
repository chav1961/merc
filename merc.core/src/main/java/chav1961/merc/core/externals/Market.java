package chav1961.merc.core.externals;

import chav1961.merc.api.interfaces.front.RuntimeInterface;
import chav1961.merc.api.interfaces.front.RuntimeInterface.BankInterface;
import chav1961.merc.api.interfaces.front.World;

public class Market {
	private final World				world;
	private final RuntimeInterface	rti;
	private final BankInterface		bank;
	
	public Market(final World world) {
		if (world == null) {
			throw new NullPointerException("World can't be null");
		}
		else {
			this.world = world;
			this.rti = world.getRuntime();
			this.bank = world.getRuntime().bank();
		}
	}
	
	
}
