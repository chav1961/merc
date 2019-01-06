package chav1961.merc.api.interfaces.front;

import chav1961.purelib.basic.exceptions.PrintingException;
import chav1961.purelib.basic.exceptions.SyntaxException;
import chav1961.purelib.streams.JsonStaxParser;
import chav1961.purelib.streams.JsonStaxPrinter;

/**
 * <p>This interface is used to describe any entities can be serialized/deserialized via JSON.</p>
 * @see Mercury landing project
 * @author Alexander Chernomyrdin aka chav1961
 * @since 0.0.1
 * @param <State> entity state
 */
public interface SerializableItem {
	/**
	 * <p>Serialize entity to the given JSON stream</p>
	 * @param printer printer to serialize to
	 * @throws PrintingException
	 */
	void serialize(JsonStaxPrinter printer) throws PrintingException;
	
	/**
	 * <p>Deserialize entity from the given JSON stream</p>
	 * @param parser parser to serialize from
	 * @throws SyntaxException
	 */
	void deserialize(JsonStaxParser parser) throws SyntaxException;
}
