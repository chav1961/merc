package chav1961.merc.lang.merc.keepers;

import chav1961.merc.api.Point;
import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.lang.merc.interfaces.VarKeeper;

public class PointKeeper implements Cloneable, VarKeeper {
	boolean	assigned = false;
	Point	value;

	public Point getValue() throws MercContentException {
		if (assigned) {
			return value;
		}
		else {
			throw new MercContentException("Attempt to use non-initialied variable");
		}
	}

	public void setValue(final Point value) {
		this.value = value;
		assigned = true;
	}
	
	@Override
	public PointKeeper clone() throws CloneNotSupportedException {
		return (PointKeeper)super.clone();
	}

	@Override
	public String toString() {
		return "PointKeeper [assigned=" + assigned + ", value=" + value + "]";
	}
}
