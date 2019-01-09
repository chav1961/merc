package chav1961.merc.api.interfaces.world;

public interface MessageInterface {
	/**
	 * <p>Get message sender</p>
	 * @return message sender
	 */
	String getSender();
	
	/**
	 * <p>Get message content</p>
	 * @return message content
	 */
	Object getMessage();
}
