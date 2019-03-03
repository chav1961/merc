package chav1961.merc.api.interfaces.front;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p>This annotation is used to mark MerLan-programmatically accessed fields and methods in the entity.</p>
 * @see Mercury landing project
 * @author Alexander Chernomyrdin aka chav1961
 * @since 0.0.1
 * @param <State> entity state
 */

@Retention(RUNTIME)
@Target({ TYPE, FIELD, METHOD })
@Inherited
public @interface MerLan {
	/**
	 * <p>Return minimal world level this field/method will be accessible</p>
	 * @return minimal world level. Always non-negative
	 */
	int accessibleFrom() default 0;
	
	/**
	 * <p>Return money payment to call this function</p>
	 * @return money payment
	 */
	float cogsPayment() default 0.0f;
	
	/**
	 * <p>Return money payment to call this function</p>
	 * @return money payment
	 */
	float coinsPayment() default 0.0f;
}
