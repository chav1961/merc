package chav1961.merc.api;

import java.util.Arrays;

/**
 * <p>This class is used to keep a set of points in the (x,y) pairs. No any ordering exists inside the set, and you must not take any assumptions about it.</p>
 * <p>This class is immutable and can be used in the multi-threaded environment</p>
 * @see Mercury landing project
 * @author Alexander Chernomyrdin aka chav1961
 * @since 0.0.1
 */

public class Track {
	private static final int[]	EMPTY_ARRAY = new int[0];

	/**
	 * <p>This interface describes walk callback for the class</p> 
	 * @see Mercury landing project
	 * @author Alexander Chernomyrdin aka chav1961
	 * @since 0.0.1
	 */
	@FunctionalInterface
	public interface TrackWalkCallback {
		boolean process(int x, int y);
	}
	
	private final int[]	points;

	
	/**
	 * <p>Constructor of the object (track with the only point)</p>
	 * @param x x-coordinate of the point 
	 * @param y y-coordinate of the point
	 */
	public Track(final int x, final int y) {
		points = new int[] {x,y};
	}

	/**
	 * <p>Constructor of the object (track with the all the points in area)</p>
	 * @param x x-coordinate of the bottom right corner of the area
	 * @param y y-coordinate of the bottom right corner of the area
	 * @param width area width
	 * @param height area height
	 * @throws IllegalArgumentException width or height is not positive
	 */
	public Track(final int x, final int y, final int width, final int height) throws IllegalArgumentException {
		if (width <= 0) {
			throw new IllegalArgumentException("Width ["+width+"] must be greater then 0");
		}
		else if (height <= 0) {
			throw new IllegalArgumentException("Height ["+height+"] must be greater then 0");
		}
		else {
			points = new int[2*width*height];

			for (int index = 0, yIndex = 0; yIndex < height; yIndex++) {
				for (int xIndex = 0; xIndex < width; xIndex++, index += 2) {
					points[index] = x + xIndex;
					points[index + 1] = y + yIndex;
				}
			}
		}
	}

	/**
	 * <p>Constructor of the object (pairs of (x,y) point coordinates)</p>
	 * @param coordinates list of the coordinates (firstly x, then y)
	 * @throws IllegalArgumentException coordinates are null or empty list or contain odd number of coordinates
	 */
	public Track(final int... coordinates) throws IllegalArgumentException {
		if (coordinates == null || coordinates.length == 0) {
			throw new IllegalArgumentException("Coordinates list can't be null or empty");
		}
		else if (coordinates.length % 2 != 0) {
			throw new IllegalArgumentException("Odd number of coordinates in the list! Must be pairs (x,y) only");
		}
		else {
			points = coordinates.clone();
		}
	}
	
	private Track(final int[] points, final boolean toDifferentSignature) {
		this.points = points;
	}
	
	/**
	 * <p>Is point inside track list</p>
	 * @param x x-coordinate of the point 
	 * @param y y-coordinate of the point
	 * @return true when inside
	 */
	public boolean isInside(final int x, final int y) {
		for (int index = 0, maxIndex = points.length; index < maxIndex; index += 2) {
			if (points[index] == x && points[index+1] == y) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * <p>Union two tracks</p>
	 * @param another track to union
	 * @return new united track instance
	 * @throws NullPointerException when another track is null
	 */
	public Track union(final Track another) throws NullPointerException {
		if (another == null) {
			throw new NullPointerException("Track to union can't be null"); 
		}
		else {
			final long[]	content = joinContent(points,another.points);
			int				count = 1;
			
			Arrays.sort(content);
			for (int index = 1, maxIndex = content.length; index < maxIndex; index++) {
				if (content[index] != content[index-1]) {
					count++;
				}
			}
			final int[] 	result = new int[2*count];

			result[0] = (int)((content[0] >> 32) & 0xFFFFFFFF);
			result[1] = (int)((content[0] >> 00) & 0xFFFFFFFF);
			for (int index = 1, destIndex = 2, maxIndex = content.length; index < maxIndex; index++) {
				if (content[index] != content[index-1]) {
					result[destIndex++] = (int)((content[index] >> 32) & 0xFFFFFFFF);
					result[destIndex++] = (int)((content[index] >> 00) & 0xFFFFFFFF);
				}
			}
			return new Track(result,false);
		}
	}

	/**
	 * <p>Intersect two tracks</p>
	 * @param another track to intersect
	 * @return new intersected track instance
	 * @throws NullPointerException when another track is null
	 */
	public Track intersect(final Track another) throws NullPointerException {
		if (another == null) {
			throw new NullPointerException("Track to union can't be null"); 
		}
		else {
			final long[]	content = joinContent(points,another.points);
			int				count = 1;
			
			Arrays.sort(content);
			for (int index = 1, maxIndex = content.length; index < maxIndex; index++) {
				if (content[index] == content[index-1]) {
					count++;
				}
			}
			final int[] 	result = new int[2*count];

			result[0] = (int)((content[0] >> 32) & 0xFFFFFFFF);
			result[1] = (int)((content[0] >> 00) & 0xFFFFFFFF);
			for (int index = 1, destIndex = 2, maxIndex = content.length; index < maxIndex; index++) {
				if (content[index] == content[index-1]) {
					result[destIndex++] = (int)((content[index] >> 32) & 0xFFFFFFFF);
					result[destIndex++] = (int)((content[index] >> 00) & 0xFFFFFFFF);
				}
			}
			return new Track(result,false);
		}
	}

	/**
	 * <p>Minus two tracks</p>
	 * @param another track to minus
	 * @return new minused track instance
	 * @throws NullPointerException when another track is null
	 */
	public Track minus(final Track another) throws NullPointerException {
		if (another == null) {
			throw new NullPointerException("Track to union can't be null"); 
		}
		else {
			final long[]	content = joinContent(points,EMPTY_ARRAY), toCompare = joinContent(another.points,EMPTY_ARRAY);
			int				count = 0;
			
			Arrays.sort(content);
			Arrays.sort(toCompare);
			for (int index = 0, compareIndex = 0, maxIndex = content.length, maxCompareIndex = toCompare.length; index < maxIndex && compareIndex < maxCompareIndex; index++) {
				while (compareIndex < maxCompareIndex && toCompare[compareIndex] < content[index]) {
					compareIndex++;
				}
				if (compareIndex < maxCompareIndex && toCompare[compareIndex] == content[index]) {
					count++;
					compareIndex++;
				}
			}
			final int[]		result = new int[2*(content.length-count)];
			
			for (int index = 0, compareIndex = 0, maxIndex = content.length, maxCompareIndex = toCompare.length; index < maxIndex && compareIndex < maxCompareIndex; index++) {
				while (compareIndex < maxCompareIndex && toCompare[compareIndex] < content[index]) {
					compareIndex++;
				}
				if (compareIndex < maxCompareIndex && toCompare[compareIndex] == content[index]) {
				}
				else {
					result[compareIndex++] = (int)((content[index] >> 32) & 0xFFFFFFFF);
					result[compareIndex++] = (int)((content[index] >> 00) & 0xFFFFFFFF);
				}
			}
			return new Track(result,false);
		}
	}
	
	/**
	 * <p>Get track points.</p>
	 * @return track points in the (x,y) pairs
	 */
	public int[] points() {
		return points.clone();
	}

	/**
	 * <p>Walk on all the points in the track</p>
	 * @param callback callback to process all the points
	 * @throws NullPointerException
	 */
	public void walk(final TrackWalkCallback callback) throws NullPointerException {
		if (callback == null) {
			throw new NullPointerException("Callback can't be null"); 
		}
		else {
			final int[]	p = points;
			
			for (int index = 0, maxIndex = p.length; index < maxIndex; index += 2) {
				if (!callback.process(p[index],p[index+1])) {
					break;
				}
			}
		}
	}
	
	/**
	 * <p>Get track points without cloning them.</p>
	 * @param track track instance to get points from
	 * @return track points in the (x,y) pairs
	 * @throws NullPointerException when track is null
	 */
	public static int[] nonClonedPoints(final Track track) throws NullPointerException {
		if (track == null) {
			throw new NullPointerException("Track to get points can't be null"); 
		}
		else {
			return track.points;
		}
	}
	
	static long[] joinContent(final int[] first, final int[] second) {
		final long[]	temp = new long[(first.length+second.length)/2];
		int				dest = 0;
		
		for (int index = 0, maxIndex = first.length; index < maxIndex; index += 2) {
			temp[dest++] = (((long)first[index]) << 32) | (((long)first[index+1]) << 0); 
		}
		for (int index = 0, maxIndex = second.length; index < maxIndex; index += 2) {
			temp[dest++] = (((long)second[index]) << 32) | (((long)second[index+1]) << 0); 
		}
		return temp;
	}
	
	@Override
	public String toString() {
		final StringBuilder	sb = new StringBuilder("Track ");
		char				prefix = '[';
		
		for (int index = 0, maxIndex = points.length; index < maxIndex; index += 2) {
			sb.append(prefix).append('(').append(points[index]).append(',').append(points[index+1]).append(')');
			prefix = ',';
		}
		return sb.append(']').toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(points);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Track other = (Track) obj;
		if (!Arrays.equals(points, other.points)) return false;
		return true;
	}
}
