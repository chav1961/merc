package chav1961.merc.api;

public final class ImmutableTrack extends BaseTrack {
	ImmutableTrack(final BaseTrack baseTrack) {
		this.points = baseTrack.points;
	}

	@Override
	public void walk(final TrackWalkCallback callback) throws NullPointerException {
		super.walk(callback);
	}
}
