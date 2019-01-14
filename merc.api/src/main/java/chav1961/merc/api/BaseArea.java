package chav1961.merc.api;

import chav1961.merc.api.interfaces.front.Entity;
import chav1961.merc.api.interfaces.front.Immutable;
import chav1961.merc.api.interfaces.front.MerLan;
import chav1961.merc.api.interfaces.front.Printable;
import chav1961.purelib.basic.exceptions.PrintingException;

class BaseArea implements Printable, Cloneable, Immutable<BaseArea>, Comparable<BaseArea> {
	int	x, y, width, height;

	BaseArea() {}
	
	BaseArea(final int x, final int y, final int width, final int height) throws IllegalArgumentException {
		if (width < 0) {
			throw new IllegalArgumentException("Area width ["+width+"] can't be negative"); 
		}
		else if (height < 0) {
			throw new IllegalArgumentException("Area height ["+height+"] can't be negative"); 
		}
		else {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
	}

	BaseArea(final BasePoint p1, final BasePoint p2) throws NullPointerException, IllegalArgumentException {
		if (p1 == null) {
			throw new NullPointerException("Point p1 can't be null");
		}
		else if (p2 == null) {
			throw new NullPointerException("Point p2 can't be null");
		}
		else {
			this.x = Math.min(p1.x,p2.x);
			this.y = Math.min(p1.y,p2.y);
			this.width = Math.abs(p2.x-p1.x);
			this.height = Math.abs(p2.y-p1.y);
		}
	}

	BaseArea(final BasePoint point, final BaseSize size) throws NullPointerException, IllegalArgumentException {
		if (point == null) {
			throw new NullPointerException("Point can't be null");
		}
		else if (size == null) {
			throw new NullPointerException("Size can't be null");
		}
		else {
			this.x = point.x;
			this.y = point.y;
			this.width = size.width;
			this.height = size.height;
		}
	}

	BaseArea(final Entity<? extends Enum<?>> entity) throws NullPointerException {
		if (entity == null) {
			throw new NullPointerException("Entity can't be null");
		}
		else {
			this.x = entity.getX();
			this.y = entity.getY();
			this.width = entity.getWidth();
			this.height = entity.getHeight();
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
	public void setHeight(final int height) {
		if (height < 0) {
			throw new IllegalArgumentException("Height ["+height+"] can't be negative"); 
		}
		else {
			this.height = height;
		}
	}

	@MerLan
	public Point getPoint() {
		return new Point(x,y);
	}

	@MerLan
	public Size getSize() {
		return new Size(width,height);
	}
	
	@MerLan
	public boolean isInside(final BasePoint point) throws NullPointerException {
		if (point == null) {
			throw new NullPointerException("Point can't be null");
		}
		else {
			return isInside(point.x,point.y); 
		}
	}

	@MerLan
	public boolean isInside(final BaseArea area) throws NullPointerException {
		if (area == null) {
			throw new NullPointerException("Area can't be null");
		}
		else {
			return area.x >= this.x && area.y >= this.y && area.x + area.width < this.x + this.width && area.y + area.height < this.y + this.height;
		}
	}
	
	@MerLan
	public boolean isIntersects(final BaseArea area) throws NullPointerException {
		if (area == null) {
			throw new NullPointerException("Area can't be null");
		}
		else {
			return isInside(area.x,area.y) || isInside(area.x+area.width,area.y) || isInside(area.x,area.y+area.height) || isInside(area.x+area.width,area.y+area.height)
				|| area.isInside(this.x,this.y) || area.isInside(this.x+this.width,this.y) || area.isInside(this.x,this.y+this.height) || area.isInside(this.x+this.width,this.y+this.height);
		}		
	}

	@MerLan
	public BaseArea intersect(final BaseArea area) throws NullPointerException, IllegalArgumentException {
		if (area == null) {
			throw new NullPointerException("Area can't be null");
		}
		else if (!isIntersects(area)) {
			throw new IllegalArgumentException("Areas don't intersects");
		}
		else {
		    final int 	x1 = Math.max(this.x, area.x); 
		    final int	y1 = Math.max(this.y, area.y);
		    final int	x2 = Math.min(this.x + this.width, area.x + area.width);
		    final int	y2 = Math.min(this.y + this.height, area.y + area.height);
	
		    return new BaseArea(x1,y1,x2-x1,y2-y2); 
		}
	}
	
	@MerLan
	@Override
	public BaseArea immutable() {
		return new ImmutableArea(this);
	}
	
	@Override
	@MerLan
	public String print() throws PrintingException {
		return "area(point(" + x + "," + y + "),size(" + width + "," + height + "))";
	}
	
	@Override
	@MerLan
	public BaseArea clone() throws CloneNotSupportedException {
		return (BaseArea) super.clone();
	}

	@Override
	@MerLan
	public int compareTo(final BaseArea another) throws NullPointerException {
		if (another == null) {
			throw new NullPointerException("Another area can't be null"); 
		}
		else {
			return this.width*this.height - another.width*another.height;
		}
	}
	
	@Override
	public String toString() {
		return "Area [x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + width;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof BaseArea)) return false;
		BaseArea other = (BaseArea) obj;
		if (height != other.height) return false;
		if (width != other.width) return false;
		if (x != other.x) return false;
		if (y != other.y) return false;
		return true;
	}
	
	boolean isInside(final int x, final int y) {
		return x >= this.x && y >= this.y && x < this.x + this.width && y < this.y + this.height; 
	}
}
