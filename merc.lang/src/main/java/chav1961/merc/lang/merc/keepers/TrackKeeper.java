package chav1961.merc.lang.merc.keepers;

import chav1961.merc.api.Track;
import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.lang.merc.interfaces.VarKeeper;

public class TrackKeeper implements Cloneable, VarKeeper {
	boolean	assigned = false;
	Track	value;
	
	public Track getValue() throws MercContentException {
		if (assigned) {
			return value;
		}
		else {
			throw new MercContentException("Attempt to use non-initialied variable");
		}
	}

	public void setValue(final Track value) {
		this.value = value;
		assigned = true;
	}
	
	@Override
	public TrackKeeper clone() throws CloneNotSupportedException {
		return (TrackKeeper)super.clone();
	}

	@Override
	public String toString() {
		return "TrackKeeper [assigned=" + assigned + ", value=" + value + "]";
	}
}
