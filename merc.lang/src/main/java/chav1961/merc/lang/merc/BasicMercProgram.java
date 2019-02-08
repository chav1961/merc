package chav1961.merc.lang.merc;

import chav1961.merc.api.AreaKeeper;
import chav1961.merc.api.BooleanKeeper;
import chav1961.merc.api.DoubleKeeper;
import chav1961.merc.api.LongKeeper;
import chav1961.merc.api.PointKeeper;
import chav1961.merc.api.SizeKeeper;
import chav1961.merc.api.StringKeeper;
import chav1961.merc.api.TrackKeeper;

public class BasicMercProgram {
	public static int _concat_(final char[] source, final char[] target, final int before) {
		System.arraycopy(source,0,target,before-source.length,source.length);
		return before - source.length;
	}
	
	public static AreaKeeper _newAreaKeeper_() {
		return new AreaKeeper();
	}

	public static BooleanKeeper _newBooleanKeeper_() {
		return new BooleanKeeper();
	}

	public static DoubleKeeper _newDoubleKeeper_() {
		return new DoubleKeeper();
	}

	public static LongKeeper _newLongKeeper_() {
		return new LongKeeper();
	}

	public static PointKeeper _newPointKeeper_() {
		return new PointKeeper();
	}

	public static SizeKeeper _newSizeKeeper_() {
		return new SizeKeeper();
	}

	public static StringKeeper _newStringKeeper_() {
		return new StringKeeper();
	}

	public static TrackKeeper _newTrackKeeper_() {
		return new TrackKeeper();
	}
}
