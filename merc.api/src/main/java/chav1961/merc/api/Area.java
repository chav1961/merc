package chav1961.merc.api;

import chav1961.merc.api.interfaces.front.Entity;
import chav1961.merc.api.interfaces.front.MerLan;

@MerLan
public final class Area extends BaseArea {
	public Area(Entity<? extends Enum<?>> entity) throws NullPointerException {
		super(entity);
	}

	public Area(int x, int y, int width, int height) throws IllegalArgumentException {
		super(x, y, width, height);
	}

	public Area(Point p1, Point p2) throws NullPointerException, IllegalArgumentException {
		super(p1, p2);
	}

	public Area(Point point, Size size) throws NullPointerException, IllegalArgumentException {
		super(point, size);
	}
}
