package chav1961.merc.api.exceptions;

import chav1961.purelib.basic.exceptions.EnvironmentException;

/**
 * <p>This class is used to throw runtime environment problems.</p>
 * @see Mercury landing project
 * @author Alexander Chernomyrdin aka chav1961
 * @since 0.0.1
 */
public class MercEnvironmentException extends EnvironmentException {
	private static final long serialVersionUID = 4855325936425934551L;

	public MercEnvironmentException() {
		super();
	}

	public MercEnvironmentException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public MercEnvironmentException(final String message) {
		super(message);
	}

	public MercEnvironmentException(final Throwable cause) {
		super(cause);
	}
}
