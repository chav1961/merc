package chav1961.merc.api.interfaces.front;

import java.util.UUID;

import chav1961.merc.api.exceptions.MercContentException;

/**
 * <p>This interface is used to describe runtime environment in the world.</p>
 * @see Mercury landing project
 * @author Alexander Chernomyrdin aka chav1961
 * @since 0.0.1
 * @param <State> entity state
 */
@MerLan
public interface RuntimeInterface {
	/**
	 * <p>This interface describes cost of the associated item</p>
	 * @author Alexander Chernomyrdin aka chav1961
	 * @since 0.0.1
	 * @param <Assoc> association with the cost interface
	 */
	@MerLan
	public interface CostInterface<Assoc> {
		/**
		 * <p>Get item associated with the given cost interface</p>
		 * @return item associated. Can't be null
		 */
		@MerLan
		Assoc getItemAssociated();
		
		/**
		 * <p>Is the item non-splitable</p>
		 * @return true if item can be splitted to pieces
		 */
		@MerLan
		boolean hasFloatUnits();
		
		/**
		 * <p>Get buy cost for one unit of the associated item</p>
		 * @return buy cost. Always positive
		 */
		@MerLan
		float getBuyCost();
		
		/**
		 * <p>Get sell cost for one unit of the associated item</p>
		 * @return sell cost. Always positive
		 */
		@MerLan
		float getSellCost();
		
		/**
		 * <p>Get transaction delay</p>
		 * @return transaction delay. Can't be negative. Must return delay for the same slow execution mode
		 */
		@MerLan
		long getTranactionDelay();
	}

	/**
	 * <p>This interface describes teleport interface in the world</p>
	 * @author Alexander Chernomyrdin aka chav1961
	 * @since 0.0.1
	 */
	@MerLan
	public interface TeleportInterface {
		/**
		 * <p>Get resource cost</p>
		 * @return all registered resource cost. Can be empty but not null
		 */
		@MerLan
		Iterable<CostInterface<ResourceDescription>> getResourceCost();
		
		/**
		 * <p>Get resource cost</p>
		 * @param resourceClass resource class to get cost for. Can't be null
		 * @return all registered resource cost with the given class. Can be empty but not null
		 */
		@MerLan
		Iterable<CostInterface<ResourceDescription>> getResourceCost(ResourceClass resourceClass);
		
		/**
		 * <p>Get resource cost</p>
		 * @param resourceClass resource class to get cost for. Can't be null
		 * @param resourceSubclass resource subclass to get cost for. Can't be nether null nor empty
		 * @return resource cost. If missing, return null
		 */
		@MerLan
		CostInterface<ResourceDescription> getResourceCost(ResourceClass resourceClass, String resourceSubclass);
		
		/**
		 * <p>Buy resource</p>
		 * @param resource resource descriptor</p>
		 * @param quantity resource quantity. Must be positive.
		 * @return outcome sum
		 * @throws MercContentException if used account doesn't have enough money to buy
		 */
		@MerLan
		float buyResource(ResourceDescription resource, float quantity) throws MercContentException;
		
		/**
		 * <p>Sell resource</p>
		 * @param resource resource descriptor</p>
		 * @param quantity resource quantity. Must be positive.
		 * @return income sum.
		 * @throws MercContentException
		 */
		@MerLan
		float sellResource(ResourceDescription resource, float quantity) throws MercContentException;
	}
	
	/**
	 * <p>This interface describes market interface in the world</p>
	 * @author Alexander Chernomyrdin aka chav1961
	 * @since 0.0.1
	 */
	@MerLan
	public interface MarketInterface extends TeleportInterface {
		/**
		 * <p>Get entities cost</p>
		 * @return all registered entities cost. Can be empty but not null
		 */
		@MerLan
		Iterable<CostInterface<EntityClassDescription<?>>> getEntityCost();
		
		/**
		 * <p>Get entities cost</p>
		 * @param clazz entities class to get cost for. Can't be null
		 * @return all registered entities cost with the given class. Can be empty but not null
		 */
		@MerLan
		Iterable<CostInterface<EntityClassDescription<?>>> getEntityCost(EntityClass clazz);

		/**
		 * <p>Get entity cost</p>
		 * @param clazz entity class to get cost for. Can't be null
		 * @param subclazz entity subclass to get cost for. Can't be null or empty
		 * @return entity cost. If missing, return null 
		 */
		@MerLan
		CostInterface<EntityClassDescription<?>> getEntityCost(EntityClass clazz, String subclazz);
		
		/**
		 * <p>Buy entity</p>
		 * @param entityClass entity class. Can't be null
		 * @param entitySubclass entity subclass. Can't be neither null nor empty
		 * @param quantity quantity to by. Must be positive
		 * @return outcome sum
		 * @throws MercContentException if used account doesn't have enough money to byu
		 */
		@MerLan
		float buyEntity(EntityClass entityClass, String entitySubclass, int quantity) throws MercContentException;
		
		/**
		 * <p>Sell entity</p>
		 * @param world world to sell entity in
		 * @param entityIds entity ids to sell
		 * @return income sum
		 * @throws MercContentException
		 */
		@MerLan
		float sellEntity(World world, UUID... entityIds) throws MercContentException;
	}
	
	/**
	 * <p>This interface describes bank interface in the world</p>
	 * @author Alexander Chernomyrdin aka chav1961
	 * @since 0.0.1
	 */
	public interface BankInterface {
		/**
		 * <p>Get current cache amount</p>
		 * @return current cache amount
		 */
		float getCacheAmount();
		
		/**
		 * <p>Increase cache amount</p>
		 * @param delta sum to increase. Must be non-negative</p>
		 * @return current cache amount 
		 * @throws MercContentException
		 */
		float increaseCacheAmount(float delta) throws MercContentException;

		/**
		 * <p>Decrease cache amount</p>
		 * @param delta sum to decrease. Must be non-negative</p>
		 * @return current cache amount 
		 * @throws MercContentException
		 */
		float decreaseCacheAmount(float delta) throws MercContentException;
	}
	
	/**
	 * <p>Get access to teleport interface</p>
	 * @return teleport interface. Can't be null
	 */
	TeleportInterface teleport();
	
	/**
	 * <p>Get access to market interface</p>
	 * @return market interface. Can't be null
	 */
	MarketInterface market();
	
	/**
	 * <p>Get access to bank interface</p>
	 * @return bank interface. Can't be null
	 */
	BankInterface bank();
	
	/**
	 * <p>Get resources registered</p>
	 * @return resources registered. Can be empty but not null.
	 */
	@MerLan
	Iterable<ResourceDescription> resources();

	/**
	 * <p>Get resources registered</p>
	 * @param clazz resource class to get list for. Can't be null
	 * @return resources registered. Can be empty but not null.
	 */
	@MerLan
	Iterable<ResourceDescription> resources(ResourceClass clazz);
	
	/**
	 * <p>Get entities registered</p>
	 * @return entities registered. Can be empty but not null
	 */
	@MerLan
	Iterable<EntityClassDescription<?>> entities();

	/**
	 * <p>Get entities registered</p>
	 * @param clazz entity class to get list for. Can't be null
	 * @return entities registered. Can be empty but not null
	 */
	@MerLan
	Iterable<EntityClassDescription<?>> entities(EntityClass clazz);
	
	/**
	 * <p>Clear total money counter</p>
	 */
	void clearTotal();
	
	/**
	 * <p>Get total bought sum for the current time</p>
	 * @return total bought sum. Always non-negative
	 */
	float getTotalBought();
	
	/**
	 * <p>Get total sold sum for the current time</p>
	 * @return total sold sum. Always non-negative
	 */
	float getTotalSold();
	
	/**
	 * <p>Get current cache</p>
	 * @return current cache (see {@linkplain BankInterface#getCacheAmount()}
	 */
	@MerLan
	float getCurrentCache();
	
	/**
	 * <p>Get the same minimal sum in the cache for the current time</p>
	 * @return low water sum. Can be negative
	 */
	float getLowWater();
	
	/**
	 * <p>Get the same maximal sum in the cache for the current time</p>
	 * @return high water sum. Can be negative
	 */
	float getHighWater();
	
	/**
	 * <p>Complete process execution</p>
	 */
	void complete();
	
	/**
	 * <p>Abort process execution</p>
	 * @param t exception to describe. Can be null
	 * @param format String to describe problem. Can't be null
	 * @param parameters
	 */
	void abort(final Throwable t, final String format, final Object... parameters);
}
