package chav1961.merc.api.interfaces.front;

import chav1961.merc.api.exceptions.MercEnvironmentException;

/**
 * <p>This interface is used to describe any drawable entity in the world</p>
 * @see Mercury landing project
 * @author Alexander Chernomyrdin aka chav1961
 * @since 0.0.1
 * @param <State> entity state
 */
public interface DrawableEntity<State extends Enum<State>> {
	/**
	 * <p>Get presentation to support drawing of the class in the different environment</p>
	 * @param type presentation type (see {@linkplain PresentationType})
	 * @return presentation to draw
	 * @throws MercEnvironmentException if the class doesn't support presentation of the given type
	 */
	PresentationCallback<State> getPresentation(PresentationType type) throws MercEnvironmentException;
}
