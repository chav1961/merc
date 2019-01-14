package chav1961.merc.api;

import chav1961.merc.api.interfaces.front.Entity;
import chav1961.merc.api.interfaces.front.MerLan;

@MerLan
public final class Size extends BaseSize {
	public Size(Entity<? extends Enum<?>> entity) throws NullPointerException {
		super(entity);
	}

	public Size(int width, int height) {
		super(width, height);
	}
}
