package chav1961.merc.lang.merc.keepers;

import chav1961.merc.api.Size;
import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.lang.merc.interfaces.VarKeeper;

public class SizeKeeper implements Cloneable, VarKeeper {
	boolean	assigned = false;
	Size	value;

	public Size getValue() throws MercContentException {
		if (assigned) {
			return value;
		}
		else {
			throw new MercContentException("Attempt to use non-initialied variable");
		}
	}

	public void setValue(final Size value) {
		this.value = value;
		assigned = true;
	}
	
	@Override
	public SizeKeeper clone() throws CloneNotSupportedException {
		return (SizeKeeper)super.clone();
	}

	@Override
	public String toString() {
		return "SizeKeeper [assigned=" + assigned + ", value=" + value + "]";
	}
}
