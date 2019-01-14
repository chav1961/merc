package chav1961.merc.api.interfaces.front;

/**
 * <p>This interface is used to describe general parameters of any entity.</p>
 * @see Mercury landing project
 * @author Alexander Chernomyrdin aka chav1961
 * @since 0.0.1
 * @param <State> entity state
 */
@MerLan

public interface EntityStateDescriptor<State extends Enum<State>> {
	/**
	 * <p>Get timestamp of the current state</p>
	 * @return timestamp related to process start. Can't be negative
	 */
	long getTimestamp();
	
	/**
	 * <p>Get x-coordinate of the bottom-left corner of the entity</p>
	 * @return x-coordinate of the bottom-left corner of the entity
	 */
	@MerLan
	int getX();
	
	/**
	 * <p>Get y-coordinate of the bottom-left corner of the entity</p>
	 * @return y-coordinate of the bottom-left corner of the entity
	 */
	@MerLan
	int getY();
	
	/**
	 * <p>Get current entity width</p>
	 * @return current entity width
	 */
	@MerLan
	int getWidth();
	
	/**
	 * <p>Get current entity height</p>
	 * @return current entity height
	 */
	@MerLan
	int getHeight();
	
	/**
	 * <p>Get current entity state</p>
	 * @return current entity state. Can't be null
	 */
	State getState();
}
