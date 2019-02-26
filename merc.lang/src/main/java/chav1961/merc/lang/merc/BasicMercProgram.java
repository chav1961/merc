package chav1961.merc.lang.merc;

import java.util.Arrays;

import chav1961.merc.api.AreaKeeper;
import chav1961.merc.api.BooleanKeeper;
import chav1961.merc.api.DoubleKeeper;
import chav1961.merc.api.LongKeeper;
import chav1961.merc.api.Point;
import chav1961.merc.api.PointKeeper;
import chav1961.merc.api.Size;
import chav1961.merc.api.SizeKeeper;
import chav1961.merc.api.StringKeeper;
import chav1961.merc.api.Track;
import chav1961.merc.api.TrackKeeper;
import chav1961.merc.api.exceptions.MercContentException;
import chav1961.purelib.basic.CharUtils;

public class BasicMercProgram {
	private static final char[]		TRUE = "true".toCharArray();
	private static final char[]		FALSE = "false".toCharArray();
	private static final char[]		temporary = new char[100];
	
	protected static int _concat_(final char[] source, final char[] target, final int before) {
		System.arraycopy(source,0,target,before-source.length,source.length);
		return before - source.length;
	}
	
	protected static int _compare_(final char[] left, final char[] right) {
		int		result;
		
		for (int index = 0, maxIndex = Math.min(left.length,right.length); index < maxIndex; index++) {
			if ((result = left[index] - right[index]) != 0) {
				return result;
			}
		}
		return left.length-right.length;
	}

	protected static int _compare_(final Size left, final Size right) {
		return left.compareTo(right);
	}
	
	protected static int _inList_(final long value, final long[] content) {
		for (int index = 0, maxIndex = content.length; index < maxIndex; index+=2) {
			if (value >= content[index] && value <= content[index+1]) {
				return 0;
			}
		}
		return -1;
	}
	
	protected static int _inList_(final double value, final double[] content) {
		for (int index = 0, maxIndex = content.length; index < maxIndex; index+=2) {
			if (value >= content[index] && value <= content[index+1]) {
				return 0;
			}
		}
		return -1;
	}

	protected static int _inList_(final char[] value, final char[][] content) {
		for (int index = 0, maxIndex = content.length; index < maxIndex; index+=2) {
			if (_compare_(value,content[index]) >= 0 && _compare_(value,content[index+1]) <= 0) {
				return 0;
			}
		}
		return -1;
	}

	protected static int _inList_(final boolean value, final boolean[] content) {
		for (int index = 0, maxIndex = content.length; index < maxIndex; index++) {
			if (value == content[index]) {
				return 0;
			}
		}
		return -1;
	}

	protected static int _inList_(final Point value, final Track[] content) {
		for (int index = 0, maxIndex = content.length; index < maxIndex; index++) {
			if (content[index].isInside(value)) {
				return 0;
			}
		}
		return -1;
	}
	
	protected static int _inList_(final Object value, final Object[] content) {
		if (value instanceof Point && content instanceof Track[]) { 
			return _inList_((Point)value,(Track[])content);
		}
		else {
			for (int index = 0, maxIndex = content.length; index < maxIndex; index++) {
				if (value.equals(content[index])) {
					return 0;
				}
			}
			return -1;
		}
	}

	protected static int _like_(final char[] left, final char[] right) {
		int		result;
		
		for (int index = 0, maxIndex = Math.min(left.length,right.length); index < maxIndex; index++) {
			if ((result = left[index] - right[index]) != 0) {
				return result;
			}
		}
		return left.length-right.length;
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
	
	protected static LongKeeper _incDec_(final LongKeeper keeper, final int mode) throws MercContentException {
		final LongKeeper	temp;
		
		switch (mode) {
			case 0 : 
				keeper.setValue(keeper.getValue()+1); 
				return keeper;
			case 1 : 
				temp = new LongKeeper();
				temp.setValue(keeper.getValue());
				keeper.setValue(keeper.getValue()+1);
				return temp;
			case 2 : 
				keeper.setValue(keeper.getValue()-1); 
				return keeper;
			case 3 : 
				temp = new LongKeeper();
				temp.setValue(keeper.getValue());
				keeper.setValue(keeper.getValue()-1); 
				return temp;
			default : throw new MercContentException("Illegal mode ["+mode+"] to increment/decrement");
		}
	}

	protected static DoubleKeeper _incDec_(final DoubleKeeper keeper, final int mode) throws MercContentException {
		final DoubleKeeper	temp;
		
		switch (mode) {
			case 0 : 
				keeper.setValue(keeper.getValue()+1); 
				return keeper;
			case 1 : 
				temp = new DoubleKeeper();
				temp.setValue(keeper.getValue());
				keeper.setValue(keeper.getValue()+1);
				return temp;
			case 2 : 
				keeper.setValue(keeper.getValue()-1); 
				return keeper;
			case 3 : 
				temp = new DoubleKeeper();
				temp.setValue(keeper.getValue());
				keeper.setValue(keeper.getValue()-1); 
				return temp;
			default : throw new MercContentException("Illegal mode ["+mode+"] to increment/decrement");
		}
	}

	protected static char[] _trunc_(final char[] source) {
		int	count = 0, start = 0, end = source.length - 1;
		
		for (int index = 0; index <= end; index++, count++, start++) {
			if (source[index] > ' ') {
				break;
			}
		}

		for (int index = end; index > start; index--, count++, end--) {
			if (source[index] > ' ') {
				break;
			}
		}
		if (count == 0) {
			return source;
		}
		else {
			return Arrays.copyOfRange(source,start,end+1);
		}
	}
}
