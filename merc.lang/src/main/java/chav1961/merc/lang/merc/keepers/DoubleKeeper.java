package chav1961.merc.lang.merc.keepers;

import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.lang.merc.interfaces.VarKeeper;

public class DoubleKeeper implements Cloneable, VarKeeper {
	boolean	assigned = false;
	double	value;

	public double getValue() throws MercContentException {
		if (assigned) {
			return value;
		}
		else {
			throw new MercContentException("Attempt to use non-initialied variable");
		}
	}

	public void setValue(final double value) {
		this.value = value;
		assigned = true;
	}
	
	@Override
	public DoubleKeeper clone() throws CloneNotSupportedException {
		return (DoubleKeeper)super.clone();
	}

	@Override
	public String toString() {
		return "DoubleKeeper [assigned=" + assigned + ", value=" + value + "]";
	}
}
