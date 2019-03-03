package chav1961.merc.core.externals;

import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.api.interfaces.front.RuntimeInterface.BankInterface;
import chav1961.merc.api.interfaces.front.TickableEntity;

public class Bank implements TickableEntity, BankInterface {
	private final boolean	throwOnExhausting;
	private volatile float	currentCogs, peakMinimum, peakMaximum;
	
	public Bank(final float initialCogs, final boolean throwOnExhausting) {
		this.throwOnExhausting = throwOnExhausting;
		this.currentCogs = this.peakMinimum = this.peakMaximum = initialCogs;
	}
	
	@Override
	public float getCacheAmount() {
		return currentCogs;
	}

	@Override
	public float increaseCacheAmount(final float delta) throws MercContentException {
		if (delta < 0) {
			throw new MercContentException("Sum to increase cache can't be negative. Use decrease(...) instead");
		}
		else {
			currentCogs += delta; 
			return getCacheAmount();
		}
	}

	@Override
	public float decreaseCacheAmount(final float delta) throws MercContentException {
		if (delta < 0) {
			throw new MercContentException("Sum to increase cache can't be negative. Use increase(...) instead");
		}
		else {
			currentCogs -= delta; 
			return getCacheAmount();
		}
	}
	
	public float getPeakMinimum() {
		return peakMinimum;
	}

	public float getPeakMaximum() {
		return peakMaximum;
	}
	
	@Override
	public String getName() {
		return "bank";
	}

	@Override
	public void setName(final String name) throws MercContentException {
		throw new MercContentException("Bank name is predefined and can't be changed programmatically");
	}

	@Override
	public void tick() throws MercContentException {
		if (throwOnExhausting && currentCogs < 0) {
			throw new MercContentException("Your account balance is exhausted (no money!). Program terminated");
		}
		else {
			peakMinimum = Math.min(peakMinimum,currentCogs);
			peakMaximum = Math.max(peakMinimum,currentCogs);
		}
	}
}
