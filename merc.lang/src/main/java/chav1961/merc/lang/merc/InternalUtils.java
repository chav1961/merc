package chav1961.merc.lang.merc;

import chav1961.merc.api.Area;
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

public class InternalUtils {
	public enum SimplifiedType {
		LongType, DoubleType, StringType, BooleanType,
		PointType, AreaType, SizeType, TrackType,
		OtherType
	}
	
	public static SimplifiedType defineSimplifiedType(final Class<?> clazz) {
		if (long.class.isAssignableFrom(clazz) || Long.class.isAssignableFrom(clazz) || LongKeeper.class.isAssignableFrom(clazz)) {
			return SimplifiedType.LongType;
		}
		else if (double.class.isAssignableFrom(clazz) || Double.class.isAssignableFrom(clazz) || DoubleKeeper.class.isAssignableFrom(clazz)) {
			return SimplifiedType.DoubleType;
		}
		else if (char[].class.isAssignableFrom(clazz) || StringKeeper.class.isAssignableFrom(clazz)) {
			return SimplifiedType.StringType;
		}
		else if (boolean.class.isAssignableFrom(clazz) || Boolean.class.isAssignableFrom(clazz) || BooleanKeeper.class.isAssignableFrom(clazz)) {
			return SimplifiedType.BooleanType;
		}
		else if (Point.class.isAssignableFrom(clazz) || PointKeeper.class.isAssignableFrom(clazz)) {
			return SimplifiedType.PointType;
		}
		else if (Area.class.isAssignableFrom(clazz) || AreaKeeper.class.isAssignableFrom(clazz)) {
			return SimplifiedType.AreaType;
		}
		else if (Size.class.isAssignableFrom(clazz) || SizeKeeper.class.isAssignableFrom(clazz)) {
			return SimplifiedType.SizeType;
		}
		else if (Track.class.isAssignableFrom(clazz) || TrackKeeper.class.isAssignableFrom(clazz)) {
			return SimplifiedType.TrackType;
		}
		else {
			return SimplifiedType.OtherType;
		}
	}
}
