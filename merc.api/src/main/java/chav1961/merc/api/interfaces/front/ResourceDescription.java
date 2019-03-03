package chav1961.merc.api.interfaces.front;

/**
 * <p>This interface is used to describe any resources in the world.</p>
 * @see Mercury landing project
 * @author Alexander Chernomyrdin aka chav1961
 * @since 0.0.1
 * @param <State> entity state
 */
@MerLan
public interface ResourceDescription {
	/**
	 * <p>Get resource class</p>
	 * @return resource class. Can't be null
	 */
	@MerLan
	ResourceClass getResourceClass();
	
	/**
	 * <p>Get resource subclass</p>
	 * @return any non-null and non-empty resource subclass. Good idea is to return simple name of the class implemented the given resource
	 */
	@MerLan
	ResourceType getResourceType();
}
