package chav1961.merc.api.interfaces.world;

import chav1961.merc.api.interfaces.front.ResourceClass;
import chav1961.merc.api.interfaces.front.ResourceType;

public interface ResourceDescriptorInterface {
	/**
	 * <p>Get resource class was detected</p>
	 * @return resource type. If missing, ResourceClass.None will be returned
	 */
	ResourceClass getResourceClass();
	
	/**
	 * <p>Get resource type was detected</p>
	 * @return resourceType. If missing, ResourceType.None will be returned
	 */
	ResourceType getResourceType();
	
	/**
	 * <p>Get mill subtype to use this resources</p>
	 * @return mill type
	 */
	String getMillType();
	
	/**
	 * <p>Get approximated resource quantity (available +/- 20%)
	 * @return approximated resource quantity
	 */
	float getApproxAmount();
}
