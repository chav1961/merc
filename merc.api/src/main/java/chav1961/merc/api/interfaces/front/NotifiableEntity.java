package chav1961.merc.api.interfaces.front;

import chav1961.merc.api.exceptions.MercContentException;

/**
 * <p>This interface is used to describe any entity that can process messages.</p>
 * @see Mercury landing project
 * @author Alexander Chernomyrdin aka chav1961
 * @since 0.0.1
 * @param <State> entity state
 */
@FunctionalInterface
public interface NotifiableEntity {
	/**
	 * <p>Process message and send answer</p>
	 * @param sender message sender (see {@linkplain Entity#getName()})
	 * @param message message received. Any string with any format
	 * @return answer. Null means doesn't reply.
	 * @throws MercContentException
	 */
	String processMessage(String sender, String message) throws MercContentException;
}
