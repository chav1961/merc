package chav1961.merc.api;

import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.api.interfaces.front.Entity;
import chav1961.merc.api.interfaces.front.MerLan;
import chav1961.merc.api.interfaces.front.VarKeeper;

@MerLan
public class EntityKeeper<State extends  Enum<State>> implements Cloneable, VarKeeper {
	boolean	assigned = false;
	Entity<State>	value;

	@MerLan
	public Entity<State> getValue() throws MercContentException {
		if (assigned) {
			return value;
		}
		else {
			throw new MercContentException("Attempt to use non-initialied variable");
		}
	}

	@MerLan
	public void setValue(final Entity<State> value) {
		this.value = value;
		assigned = true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@MerLan
	public EntityKeeper<State> clone() throws CloneNotSupportedException {
		return (EntityKeeper<State>)super.clone();
	}

	@Override
	public String toString() {
		return "EntityKeeper [assigned=" + assigned + ", value=" + value + "]";
	}
}
