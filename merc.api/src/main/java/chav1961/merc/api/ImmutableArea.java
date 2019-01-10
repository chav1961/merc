package chav1961.merc.api;

public final class ImmutableArea extends BaseArea {
	ImmutableArea(final BaseArea area) {
		this.x = area.x;
		this.y = area.y;
		this.width = area.width;
		this.height = area.height;
	}

	@Override
	public void setX(final int x) throws IllegalStateException {
		throw new IllegalStateException("This area instance is immutable! You can't change it's x-coordinate");
	}

	@Override
	public void setY(final int y) {
		throw new IllegalStateException("This area instance is immutable! You can't change it's y-coordinate");
	}

	@Override
	public void setWidth(final int width) throws IllegalArgumentException {
		throw new IllegalStateException("This area instance is immutable! You can't change it's width");
	}

	@Override
	public void setHeight(final int height) {
		throw new IllegalStateException("This area instance is immutable! You can't change it's height");
	}
}
