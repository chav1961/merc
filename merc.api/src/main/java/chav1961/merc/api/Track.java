package chav1961.merc.api;

import chav1961.merc.api.interfaces.front.AvailableForTrack;
import chav1961.merc.api.interfaces.front.MerLan;

@MerLan
public final class Track extends BaseTrack {
	public Track(int x, int y, int width, int height) throws IllegalArgumentException {
		this(new Point(x, y), new Size(width, height));
	}

	public Track(int x, int y) {
		this(new Point(x, y));
	}

	public Track(Point point, Size size) throws NullPointerException {
		this(new Area(point, size));
	}

	public Track(final AvailableForTrack... elements) throws NullPointerException {
		super(elements);
	}	
	
	@Override
	public void walk(final TrackWalkCallback callback) throws NullPointerException {
		super.walk(callback);
	}
}
