package chav1961.merc.api;

public final class ImmutableSize extends BaseSize {
	ImmutableSize(final BaseSize size) {
		this.width = size.width;
		this.height = size.height;
	}

	@Override
	public void setWidth(final int width) throws IllegalStateException {
		throw new IllegalStateException("This area instance is immutable! You can't change it's width");
	}

	@Override
	public void setHeight(final int height) throws IllegalStateException {
		throw new IllegalStateException("This area instance is immutable! You can't change it's height");
	}
}
