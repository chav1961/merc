package chav1961.merc.api;

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