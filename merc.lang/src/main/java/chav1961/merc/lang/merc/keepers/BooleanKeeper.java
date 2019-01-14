package chav1961.merc.lang.merc.keepers;

import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.lang.merc.interfaces.VarKeeper;

public class BooleanKeeper implements Cloneable, VarKeeper {
	boolean	assigned = false;
	boolean value;

	public boolean getValue() throws MercContentException {
		if (assigned) {
			return value;
		}
		else {
			throw new MercContentException("Attempt to use non-initialied variable");
		}
	}

	public void setValue(final boolean value) {
		this.value = value;
		assigned = true;
	}
	
	@Override
	public BooleanKeeper clone() throws CloneNotSupportedException {
		return (BooleanKeeper)super.clone();
	}

	@Override
	public String toString() {
		return "BooleanKeeper [assigned=" + assigned + ", value=" + value + "]";
	}
}
