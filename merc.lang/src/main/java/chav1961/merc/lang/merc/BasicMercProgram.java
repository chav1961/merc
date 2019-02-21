package chav1961.merc.lang.merc;

import java.util.Arrays;

import chav1961.merc.api.AreaKeeper;
import chav1961.merc.api.BooleanKeeper;
import chav1961.merc.api.DoubleKeeper;
import chav1961.merc.api.LongKeeper;
import chav1961.merc.api.PointKeeper;
import chav1961.merc.api.SizeKeeper;
import chav1961.merc.api.StringKeeper;
import chav1961.merc.api.TrackKeeper;
import chav1961.purelib.basic.CharUtils;

public class BasicMercProgram {
	private static final char[]		TRUE = "true".toCharArray();
	private static final char[]		FALSE = "false".toCharArray();
	private static final char[]		temporary = new char[100];
	
	protected static int _concat_(final char[] source, final char[] target, final int before) {
		System.arraycopy(source,0,target,before-source.length,source.length);
		return before - source.length;
	}
	
	protected static AreaKeeper _newAreaKeeper_() {
		return new AreaKeeper();
	}

	protected static BooleanKeeper _newBooleanKeeper_() {
		return new BooleanKeeper();
	}

	protected static DoubleKeeper _newDoubleKeeper_() {
		return new DoubleKeeper();
	}

	protected static LongKeeper _newLongKeeper_() {
		return new LongKeeper();
	}

	protected static PointKeeper _newPointKeeper_() {
		return new PointKeeper();
	}

	protected static SizeKeeper _newSizeKeeper_() {
		return new SizeKeeper();
	}

	protected static StringKeeper _newStringKeeper_() {
		return new StringKeeper();
	}

	protected static TrackKeeper _newTrackKeeper_() {
		return new TrackKeeper();
	}
	
	protected static final char[] _toStr_(final long value) {
		final int 	len = CharUtils.printLong(temporary,0,value,true);
		
		return Arrays.copyOfRange(temporary,0,len);
	}

	protected static final char[] _toStr_(final double value) {
		final int 	len = CharUtils.printDouble(temporary,0,value,true);
		
		return Arrays.copyOfRange(temporary,0,len);
	}

	protected static final char[] _toStr_(final boolean value) {
		return value ? TRUE.clone() : FALSE.clone();
	}
}
