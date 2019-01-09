package chav1961.merc.api.interfaces.world;

import chav1961.merc.api.interfaces.front.ResourceClass;

public interface ResourceDescriptorInterface {
	/**
	 * <p>Get resource type was detected</p>
	 * @return resource type. If missing, ResourceType.None will be returned
	 */
	ResourceClass getResourceType();
	
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
