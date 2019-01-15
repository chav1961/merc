package chav1961.merc.api;

import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.api.interfaces.front.MerLan;
import chav1961.merc.api.interfaces.front.VarKeeper;

@MerLan
public class SizeKeeper implements Cloneable, VarKeeper {
	boolean	assigned = false;
	Size	value;

	@MerLan
	public Size getValue() throws MercContentException {
		if (assigned) {
			return value;
		}
		else {
			throw new MercContentException("Attempt to use non-initialied variable");
		}
	}

	@MerLan
	public void setValue(final Size value) {
		this.value = value;
		assigned = true;
	}
	
	@Override
	@MerLan
	public SizeKeeper clone() throws CloneNotSupportedException {
		return (SizeKeeper)super.clone();
	}

	@Override
	public String toString() {
		return "SizeKeeper [assigned=" + assigned + ", value=" + value + "]";
	}
}
