package chav1961.merc.api;

import chav1961.merc.api.interfaces.front.Entity;

public final class Area extends BaseArea {
	public Area(Entity<? extends Enum<?>> entity) throws NullPointerException {
		super(entity);
	}

	public Area(int x, int y, int width, int height) throws IllegalArgumentException {
		super(x, y, width, height);
	}

	public Area(BasePoint p1, BasePoint p2) throws NullPointerException, IllegalArgumentException {
		super(p1, p2);
	}

	public Area(BasePoint point, BaseSize size) throws NullPointerException, IllegalArgumentException {
		super(point, size);
	}
}
