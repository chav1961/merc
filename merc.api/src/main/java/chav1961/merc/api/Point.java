package chav1961.merc.api;

import chav1961.merc.api.interfaces.front.Entity;
import chav1961.merc.api.interfaces.front.MerLan;

@MerLan
public final class Point extends BasePoint {
	public Point(Entity<? extends Enum<?>> entity) throws NullPointerException {
		super(entity);
	}

	public Point(int x, int y) {
		super(x, y);
	}
}
