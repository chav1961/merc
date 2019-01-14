package chav1961.merc.api;

import chav1961.merc.api.interfaces.front.Entity;
import chav1961.merc.api.interfaces.front.Immutable;
import chav1961.merc.api.interfaces.front.MerLan;
import chav1961.merc.api.interfaces.front.Printable;
import chav1961.purelib.basic.exceptions.PrintingException;

class BasePoint implements Printable, Cloneable, Immutable<BasePoint> {
	int x, y;

	BasePoint(){
	}
	
	BasePoint(int x, int y) {
		this.x = x;
		this.y = y;
	}

	BasePoint(final Entity<? extends Enum<?>> entity) throws NullPointerException {
		if (entity == null) {
			throw new NullPointerException("Entity can't be null");
		}
		else {
			this.x = entity.getX();
			this.y = entity.getY();
		}
	}
	
	@MerLan
	public int getX() {
		return x;
	}

	@MerLan
	public void setX(final int x) {
		this.x = x;
	}

	@MerLan
	public int getY() {
		return y;
	}

	@MerLan
	public void setY(final int y) {
		this.y = y;
	}

	@MerLan
	public float distance(final BasePoint another) throws NullPointerException {
		if (another == null) {
			throw new NullPointerException("Another point can't be null");
		}
		else {
			return (float) Math.sqrt(squareDistance(another)); 
		}
	}

	@MerLan
	public int squareDistance(final BasePoint another) throws NullPointerException {
		if (another == null) {
			throw new NullPointerException("Another point can't be null");
		}
		else {
			final int	dx = another.x - this.x;
			final int	dy = another.y - this.y;
			
			return dx*dx + dy*dy; 
		}
	}
	
	@MerLan
	@Override
	public BasePoint immutable() {
		return new ImmutablePoint(this);
	}

	@MerLan
	@Override
	public String print() throws PrintingException {
		return "point(" + x + "," + y + ")";
	}

	@MerLan
	@Override
	public BasePoint clone() throws CloneNotSupportedException {
		return (BasePoint) super.clone();
	}

	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof BasePoint)) return false;
		BasePoint other = (BasePoint) obj;
		if (x != other.x) return false;
		if (y != other.y) return false;
		return true;
	}

}
