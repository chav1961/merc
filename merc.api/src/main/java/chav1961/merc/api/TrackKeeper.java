package chav1961.merc.api;

import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.api.interfaces.front.MerLan;
import chav1961.merc.api.interfaces.front.VarKeeper;

@MerLan
public class TrackKeeper implements Cloneable, VarKeeper {
	boolean	assigned = false;
	Track	value;
	
	@MerLan
	public Track getValue() throws MercContentException {
		if (assigned) {
			return value;
		}
		else {
			throw new MercContentException("Attempt to use non-initialied variable");
		}
	}

	@MerLan
	public void setValue(final Track value) {
		this.value = value;
		assigned = true;
	}
	
	@Override
	@MerLan
	public TrackKeeper clone() throws CloneNotSupportedException {
		return (TrackKeeper)super.clone();
	}

	@Override
	public String toString() {
		return "TrackKeeper [assigned=" + assigned + ", value=" + value + "]";
	}
}
