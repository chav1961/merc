package chav1961.merc.api.interfaces.front;

/**
 * <p>This interface is used to get access to specific entity software. It's implementation 
 * can contains any fields and methods associated with the given entity. These ones can be used
 * directly in the control programs of the entities.</p>
 * @see Mercury landing project
 * @author Alexander Chernomyrdin aka chav1961
 * @since 0.0.1
 * @param <State> entity state
 */
public interface ControlInterface<State extends Enum<State>> {
	/**
	 * <p>Get owner of the given interface</p> 
	 * @return owner of the given interface. Can't be null
	 */
	Entity<State> getEntity();
}
