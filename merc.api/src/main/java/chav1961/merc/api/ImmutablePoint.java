package chav1961.merc.api;

public final class ImmutablePoint extends BasePoint {
	ImmutablePoint(final BasePoint point) {
		this.x = point.x;
		this.y = point.y;
	}

	@Override
	public void setX(int x) throws IllegalStateException {
		throw new IllegalStateException("This point instance is immutable! You can't change it's x-coordinate");
	}

	@Override
	public void setY(int y) throws IllegalStateException {
		throw new IllegalStateException("This point instance is immutable! You can't change it's y-coordinate");
	}
}
