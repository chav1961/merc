package chav1961.merc.api;

import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.api.interfaces.front.MerLan;
import chav1961.merc.api.interfaces.front.VarKeeper;

@MerLan
public class DoubleKeeper implements Cloneable, VarKeeper {
	boolean	assigned = false;
	double	value;

	@MerLan
	public double getValue() throws MercContentException {
		if (assigned) {
			return value;
		}
		else {
			throw new MercContentException("Attempt to use non-initialied variable");
		}
	}

	@MerLan
	public void setValue(final double value) {
		this.value = value;
		assigned = true;
	}
	
	@Override
	@MerLan
	public DoubleKeeper clone() throws CloneNotSupportedException {
		return (DoubleKeeper)super.clone();
	}

	@Override
	public String toString() {
		return "DoubleKeeper [assigned=" + assigned + ", value=" + value + "]";
	}
}
