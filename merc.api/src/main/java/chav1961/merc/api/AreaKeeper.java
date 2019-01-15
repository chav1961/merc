package chav1961.merc.api;

import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.api.interfaces.front.MerLan;
import chav1961.merc.api.interfaces.front.VarKeeper;

@MerLan
public class AreaKeeper implements Cloneable, VarKeeper {
	boolean	assigned = false;
	Area	value;

	@MerLan
	public Area getValue() throws MercContentException {
		if (assigned) {
			return value;
		}
		else {
			throw new MercContentException("Attempt to use non-initialied variable");
		}
	}

	@MerLan
	public void setValue(final Area value) {
		this.value = new Area(value.getX(),value.getY(),value.getWidth(),value.getHeight());
		assigned = true;
	}

	@Override
	@MerLan
	public AreaKeeper clone() throws CloneNotSupportedException {
		return (AreaKeeper)super.clone();
	}

	@Override
	public String toString() {
		return "AreaKeeper [assigned=" + assigned + ", value=" + value + "]";
	}
}
