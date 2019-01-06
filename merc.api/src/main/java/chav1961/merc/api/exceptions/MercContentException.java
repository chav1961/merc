package chav1961.merc.api.exceptions;

import chav1961.purelib.basic.exceptions.ContentException;

/**
 * <p>This class is used to throw runtime content problems.</p>
 * @see Mercury landing project
 * @author Alexander Chernomyrdin aka chav1961
 * @since 0.0.1
 */
public class MercContentException extends ContentException {
	private static final long serialVersionUID = 2646106565972806766L;

	public MercContentException() {
		super();
	}

	public MercContentException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public MercContentException(final String message) {
		super(message);
	}

	public MercContentException(final Throwable cause) {
		super(cause);
	}
}
