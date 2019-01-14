package chav1961.merc.lang.merc.keepers;

import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.api.interfaces.front.MerLan;
import chav1961.merc.lang.merc.interfaces.VarKeeper;

@MerLan
public class LongKeeper implements Cloneable, VarKeeper {
	boolean	assigned = false;
	long	value;

	@MerLan
	public long getValue() throws MercContentException {
		if (assigned) {
			return value;
		}
		else {
			throw new MercContentException("Attempt to use non-initialied variable");
		}
	}

	@MerLan
	public void setValue(final long value) {
		this.value = value;
		assigned = true;
	}
		
	@Override
	@MerLan
	public LongKeeper clone() throws CloneNotSupportedException {
		return (LongKeeper)super.clone();
	}

	@Override
	public String toString() {
		return "LongKeeper [assigned=" + assigned + ", value=" + value + "]";
	}
}
