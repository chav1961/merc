package chav1961.merc.api;

public final class ImmutableTrack extends BaseTrack {
	ImmutableTrack(final Track track) {
		this.points = track.points;
	}

	@Override
	public void walk(final TrackWalkCallback callback) throws NullPointerException {
		super.walk(callback);
	}
}
