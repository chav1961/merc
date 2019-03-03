package chav1961.merc.core.buildings;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.api.interfaces.front.ControlInterface;
import chav1961.merc.api.interfaces.front.Entity;
import chav1961.merc.api.interfaces.front.MerLan;
import chav1961.merc.api.interfaces.front.ResourceClass;
import chav1961.merc.api.interfaces.front.ResourceDescription;
import chav1961.merc.api.interfaces.front.ResourceType;
import chav1961.merc.api.interfaces.front.RuntimeInterface.CostInterface;
import chav1961.merc.api.interfaces.front.RuntimeInterface.TeleportInterface;

public class TeleportControl implements ControlInterface<TeleportState>, TeleportInterface {
	private final TeleportInstance 	instance;
	
	private final Map<ResourceType,ResourceDefinition>	resources = new HashMap<>(); 

	public TeleportControl(final TeleportInstance instance) {
		this.instance = instance;
	}

	@Override
	public ResourceDescription[] connectedResources() {
		synchronized(resources) {
			final ResourceDescription[]	result = new ResourceDescription[resources.size()];
			int	index = 0;
			
			for (Entry<ResourceType, ResourceDefinition> item : resources.entrySet()) {
				result[index++] = item.getValue();
			}
			return result;
		}
	}

	@Override
	public CostInterface<ResourceDescription>[] getResourceCost() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CostInterface<ResourceDescription>[] getResourceCost(final ResourceClass resourceClass) throws MercContentException {
		if (resourceClass == null) {
			throw new MercContentException("Resource class to get cost for can't be null");
		}
		else {
			// TODO Auto-generated method stub
			return null;
		}
	}

	@Override
	public CostInterface<ResourceDescription> getResourceCost(final ResourceType resourceType) throws MercContentException {
		if (resourceType == null) {
			throw new MercContentException("Resource class to get cost for can't be null");
		}
		else {
			// TODO Auto-generated method stub
			return null;
		}
	}

	@Override
	public float getAmountBought(final ResourceType resourceType) throws MercContentException {
		if (resourceType == null) {
			throw new MercContentException("Resource class to get amount for can't be null");
		}
		else {
			synchronized (resources) {
				if (resources.containsKey(resourceType)) {
					return resources.get(resourceType).amountReceived;
				}
				else {
					return 0;
				}
			}
		}
	}

	@Override
	public float getSumBought(final ResourceType resourceType) throws MercContentException {
		if (resourceType == null) {
			throw new MercContentException("Resource class to get sum for can't be null");
		}
		else {
			synchronized (resources) {
				if (resources.containsKey(resourceType)) {
					return resources.get(resourceType).sumReceived;
				}
				else {
					return 0;
				}
			}
		}
	}

	@Override
	public float getTotalSumBought() {
		float	total = 0;
		
		synchronized (resources) {
			for (Entry<ResourceType, ResourceDefinition> item : resources.entrySet()) {
				total += item.getValue().sumReceived;
			}
		}
		return total;
	}

	@Override
	public float getAmountSold(final ResourceType resourceType) throws MercContentException {
		if (resourceType == null) {
			throw new MercContentException("Resource class to get amount for can't be null");
		}
		else {
			synchronized (resources) {
				if (resources.containsKey(resourceType)) {
					return resources.get(resourceType).amountTransmitted;
				}
				else {
					return 0;
				}
			}
		}
	}

	@Override
	public float getSumSold(ResourceType resourceType) throws MercContentException {
		if (resourceType == null) {
			throw new MercContentException("Resource class to get sum for can't be null");
		}
		else {
			synchronized (resources) {
				if (resources.containsKey(resourceType)) {
					return resources.get(resourceType).sumTransmitted;
				}
				else {
					return 0;
				}
			}
		}
	}

	@Override
	public float getTotalSumSold() {
		float	total = 0;
		
		synchronized (resources) {
			for (Entry<ResourceType, ResourceDefinition> item : resources.entrySet()) {
				total += item.getValue().sumTransmitted;
			}
		}
		return total;
	}

	@Override
	public void lockResourceTransmission(final ResourceType resourceType) throws MercContentException {
		if (resourceType == null) {
			throw new MercContentException("Resource class to lock transmission for can't be null");
		}
		else {
			synchronized (resources) {
				if (resources.containsKey(resourceType)) {
					resources.get(resourceType).transmittingEnabled = false;
				}
				else {
					throw new MercContentException("Resource pipe ["+resourceType+"] is not connected to teleport yet");
				}
			}
		}
	}

	@Override
	public boolean isResourceTransmissionLocked(final ResourceType resourceType) throws MercContentException {
		if (resourceType == null) {
			throw new MercContentException("Resource type to test locking transmission for can't be null");
		}
		else {
			synchronized (resources) {
				if (resources.containsKey(resourceType)) {
					return !resources.get(resourceType).transmittingEnabled;
				}
				else {
					throw new MercContentException("Resource pipe ["+resourceType+"] is not connected to teleport yet");
				}
			}
		}
	}

	@Override
	public void unlockResourceTransmission(final ResourceType resourceType) throws MercContentException {
		if (resourceType == null) {
			throw new MercContentException("Resource class to lock transmission for can't be null");
		}
		else {
			synchronized (resources) {
				if (resources.containsKey(resourceType)) {
					resources.get(resourceType).transmittingEnabled = true;
				}
				else {
					throw new MercContentException("Resource pipe ["+resourceType+"] is not connected to teleport yet");
				}
			}
		}
	}

	@Override
	public void lockResourceReceiving(final ResourceType resourceType) throws MercContentException {
		if (resourceType == null) {
			throw new MercContentException("Resource class to lock receiving for can't be null");
		}
		else {
			synchronized (resources) {
				if (resources.containsKey(resourceType)) {
					resources.get(resourceType).receivingEnabled = false;
				}
				else {
					throw new MercContentException("Resource pipe ["+resourceType+"] is not connected to teleport yet");
				}
			}
		}
	}

	@Override
	public boolean isResourceReceivingLocked(final ResourceType resourceType) throws MercContentException {
		if (resourceType == null) {
			throw new MercContentException("Resource type to test locking receiving for can't be null");
		}
		else {
			synchronized (resources) {
				if (resources.containsKey(resourceType)) {
					return !resources.get(resourceType).receivingEnabled;
				}
				else {
					throw new MercContentException("Resource pipe ["+resourceType+"] is not connected to teleport yet");
				}
			}
		}
	}

	@Override
	public void unlockResourceReceiving(final ResourceType resourceType) throws MercContentException {
		if (resourceType == null) {
			throw new MercContentException("Resource class to lock receiving for can't be null");
		}
		else {
			synchronized (resources) {
				if (resources.containsKey(resourceType)) {
					resources.get(resourceType).receivingEnabled = true;
				}
				else {
					throw new MercContentException("Resource pipe ["+resourceType+"] is not connected to teleport yet");
				}
			}
		}
	}

	@Override
	public void registerConnection(final ResourceType resourceType) throws MercContentException {
		if (resourceType == null) {
			throw new MercContentException("Resource class to lock receiving for can't be null");
		}
		else {
			synchronized (resources) {
				if (resources.containsKey(resourceType)) {
					resources.get(resourceType).numberOfConnectors++;
				}
				else {
					resources.put(resourceType,new ResourceDefinition(resourceType,null));
				}
			}
		}
	}

	@Override
	public void unregisterConnection(final ResourceType resourceType) throws MercContentException {
		if (resourceType == null) {
			throw new MercContentException("Resource class to lock receiving for can't be null");
		}
		else {
			synchronized (resources) {
				if (resources.containsKey(resourceType)) {
					if (--resources.get(resourceType).numberOfConnectors == 0) {
						resources.remove(resourceType);
					}
				}
			}
		}
	}

	@Override
	public float buyResource(ResourceType resource, float quantity) throws MercContentException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float sellResource(ResourceType resource, float quantity) throws MercContentException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Entity<TeleportState> getEntity() {
		return instance;
	}

	void tick() {
		// TODO Auto-generated method stub
	}
	
	private static class ResourceDefinition implements ResourceDescription {
		final ResourceType	type;
		final ResourceClass	clazz;
		int			numberOfConnectors = 1;
		boolean		transmittingEnabled = true;
		boolean		receivingEnabled = true;
		float		amountTransmitted = 0;
		float		amountReceived = 0;
		float		sumTransmitted = 0;
		float		sumReceived = 0;
		
		public ResourceDefinition(final ResourceType type, final ResourceClass clazz) {
			this.type = type;
			this.clazz = clazz;
		}

		@Override
		public ResourceClass getResourceClass() {
			return clazz;
		}

		@Override
		public ResourceType getResourceType() {
			return type;
		}

		@Override
		public String toString() {
			return "ResourceDefinition [type=" + type + ", clazz=" + clazz + ", transittingEnabled="
					+ transmittingEnabled + ", receivingEnabled=" + receivingEnabled + ", amountTransmitted="
					+ amountTransmitted + ", amountReceived=" + amountReceived + ", sumTransmitted=" + sumTransmitted
					+ ", sumReceived=" + sumReceived + "]";
		}
	}
}