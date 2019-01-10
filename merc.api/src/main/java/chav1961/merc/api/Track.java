package chav1961.merc.api;

public final class Track extends BaseTrack {
	public Track(int x, int y, int width, int height) throws IllegalArgumentException {
		super(x, y, width, height);
	}

	public Track(int x, int y) {
		super(x, y);
	}

	public Track() {
		super();
	}

	public Track(Area area) throws NullPointerException {
		super(area);
	}

	public Track(Point point, Size size) throws NullPointerException {
		super(point, size);
	}

	public Track(Point point) throws NullPointerException {
		super(point);
	}

	@Override
	public void walk(final TrackWalkCallback callback) throws NullPointerException {
		super.walk(callback);
	}
}
