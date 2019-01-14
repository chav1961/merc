package chav1961.merc.lang.merc.keepers;

import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.api.interfaces.front.Entity;
import chav1961.merc.lang.merc.interfaces.VarKeeper;

public class EntityKeeper<State extends  Enum<State>> implements Cloneable, VarKeeper {
	boolean	assigned = false;
	Entity<State>	value;

	public Entity<State> getValue() throws MercContentException {
		if (assigned) {
			return value;
		}
		else {
			throw new MercContentException("Attempt to use non-initialied variable");
		}
	}

	public void setValue(final Entity<State> value) {
		this.value = value;
		assigned = true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public EntityKeeper<State> clone() throws CloneNotSupportedException {
		return (EntityKeeper<State>)super.clone();
	}

	@Override
	public String toString() {
		return "EntityKeeper [assigned=" + assigned + ", value=" + value + "]";
	}
}
