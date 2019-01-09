package chav1961.merc.api.interfaces.world;

import chav1961.merc.api.exceptions.MercContentException;

public interface CommunicationInterface<Returned> {
	/**
	 * <p>Receive message from anybody. This command available only when you have a RadioTower building.</p> 
	 * @param wait wait any message, if missing. Command will not be ended until some message appears 
	 * @return message received or null if no messages now and wait = false.
	 * @throws MercContentException 
	 */
	MessageInterface receiveMessage(final boolean wait) throws MercContentException;	
	
	/**
	 * <p>Send message to any game entity. This command available only when you have a RadioTower building. Receiver of the messages need to receive them, otherwise the message will be lost</p>
	 * @param entityId receiver id (for example, "robo")
	 * @param message message text.
	 * @return self
	 */
	Returned sendMessage(final String entityId, final Object message);
}
