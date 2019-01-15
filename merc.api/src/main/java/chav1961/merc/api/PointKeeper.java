package chav1961.merc.api;

import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.api.interfaces.front.MerLan;
import chav1961.merc.api.interfaces.front.VarKeeper;

@MerLan
public class PointKeeper implements Cloneable, VarKeeper {
	boolean	assigned = false;
	Point	value;

	@MerLan
	public Point getValue() throws MercContentException {
		if (assigned) {
			return value;
		}
		else {
			throw new MercContentException("Attempt to use non-initialied variable");
		}
	}

	@MerLan
	public void setValue(final Point value) {
		this.value = value;
		assigned = true;
	}
	
	@Override
	@MerLan
	public PointKeeper clone() throws CloneNotSupportedException {
		return (PointKeeper)super.clone();
	}

	@Override
	public String toString() {
		return "PointKeeper [assigned=" + assigned + ", value=" + value + "]";
	}
}
