package chav1961.merc.lang.merc.keepers;

import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.lang.merc.interfaces.VarKeeper;

public class StringKeeper implements Cloneable, VarKeeper {
	boolean	assigned = false;
	char[]	value;
	
	public char[] getValue() throws MercContentException {
		if (assigned) {
			return value;
		}
		else {
			throw new MercContentException("Attempt to use non-initialied variable");
		}
	}

	public void setValue(final char[] value) {
		this.value = value;
		assigned = true;
	}
	
	@Override
	public StringKeeper clone() throws CloneNotSupportedException {
		return (StringKeeper)super.clone();
	}

	@Override
	public String toString() {
		return "StringKeeper [assigned=" + assigned + ", value=" + (value == null ? "null" : new String(value)) + "]";
	}
}
