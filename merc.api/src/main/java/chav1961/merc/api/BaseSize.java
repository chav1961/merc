package chav1961.merc.api;

import chav1961.merc.api.interfaces.front.Entity;
import chav1961.merc.api.interfaces.front.Immutable;
import chav1961.merc.api.interfaces.front.MerLan;
import chav1961.merc.api.interfaces.front.Printable;
import chav1961.purelib.basic.exceptions.PrintingException;

class BaseSize implements Printable, Cloneable, Immutable<BaseSize>, Comparable<BaseSize> {
	int 	width, height;

	BaseSize() {
	}
	
	public BaseSize(final int width, final int height) {
		if (width < 0) {
			throw new IllegalArgumentException("Width ["+width+"] can't be negative"); 
		}
		else if (height < 0) {
			throw new IllegalArgumentException("Height ["+height+"] can't be negative"); 
		}
		else {
			this.width = width;
			this.height = height;
		}
	}

	public BaseSize(final Entity<? extends Enum<?>> entity) throws NullPointerException {
		if (entity == null) {
			throw new NullPointerException("Entity can't be null");
		}
		else {
			this.width = entity.getWidth();
			this.height = entity.getHeight();
		}
	}
	
	@MerLan
	public int getWidth() {
		return width;
	}

	@MerLan
	public void setWidth(final int width) throws IllegalArgumentException {
		if (width < 0) {
			throw new IllegalArgumentException("Width ["+width+"] can't be negative"); 
		}
		else {
			this.width = width;
		}
	}

	@MerLan
	public int getHeight() {
		return height;
	}

	@MerLan
	public void setHeight(final int height) throws IllegalArgumentException {
		if (height < 0) {
			throw new IllegalArgumentException("Height ["+height+"] can't be negative"); 
		}
		else {
			this.height = height;
		}
	}

	@Override
	@MerLan
	public int compareTo(final BaseSize another) throws NullPointerException {
		if (another == null) {
			throw new NullPointerException("Another size can't be null"); 
		}
		else {
			return this.width*this.height - another.width*another.height;
		}
	}
	
	@Override
	@MerLan
	public BaseSize immutable() {
		return new ImmutableSize(this);
	}
	
	@Override
	@MerLan
	public String print() throws PrintingException {
		return "size(" + width + "," + height + ")";
	}

	@Override
	@MerLan
	public BaseSize clone() throws CloneNotSupportedException {
		return (BaseSize) super.clone();
	}

	@Override
	public String toString() {
		return "Size [width=" + width + ", height=" + height + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + width;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof BaseSize)) return false;
		BaseSize other = (BaseSize) obj;
		if (height != other.height) return false;
		if (width != other.width) return false;
		return true;
	}
}
