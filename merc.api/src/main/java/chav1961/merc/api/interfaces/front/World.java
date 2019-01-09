package chav1961.merc.api.interfaces.front;

import java.io.IOException;
import java.util.UUID;

import chav1961.merc.api.Track;
import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.api.exceptions.MercEnvironmentException;
import chav1961.purelib.basic.exceptions.LocalizationException;
import chav1961.purelib.i18n.interfaces.Localizer;

/**
 * <p>This interface is used to describe world.</p>
 * @see Mercury landing project
 * @author Alexander Chernomyrdin aka chav1961
 * @since 0.0.1
 */
public interface World extends DrawableEntity<WorldState> {
	/**
	 * <p>This interface describes locks in the world</p>
	 * @see Mercury landing project
	 * @author Alexander Chernomyrdin aka chav1961
	 * @since 0.0.1
	 */
	public interface WorldLocker extends AutoCloseable{
		/**
		 * <p>Get lock owner</p>
		 * @return lock owner. Can't be null
		 */
		UUID getOwner();
		
		/**
		 * <p>Get locked cells for the given lock</p>
		 * @return locked cells. Can't be null
		 */
		Track getLockedCells();
		void close() throws MercContentException;
	}

	/**
	 * <p>Get current timestamp of the world</p>
	 * @return current timestamp
	 */
	long getWorldTimestamp();
	
	/**
	 * <p>Set timestamp of the world</p>
	 * @param timestamp timestamp of the world. Can't be negative
	 * @return self
	 */
	World setWorldTimestamp(long timestamp); 
	
	/**
	 * <p>Get level of the world</p>
	 * @return any positive integer
	 */
	int getWorldLevel();
	
	/**
	 * <p>Get world bottom-left corner x-coordinate related to station bottom-left corner 
	 * @return x-coordinate. Must be not greater than station x-coordinate
	 */
	int getWorldX();

	/**
	 * <p>Get world bottom-left corner y-coordinate related to station bottom-left corner 
	 * @return y-coordinate. Must be not greater than station y-coordinate
	 */
	int getWorldY();
	
	/**
	 * <p>Get world width</p>
	 * @return world width. Must be greater than station width
	 */
	int getWorldWidth();

	/**
	 * <p>Get world height</p>
	 * @return world height. Must be greater than station height
	 */
	int getWorldHeight();
	
	/**
	 * <p>Set world bottom-left corner x-coordinate related to station bottom-left corner 
	 * @param x corner x-coordinate related to station bottom-left corner
	 * @return self
	 * @throws MercEnvironmentException
	 */
	World setWorldX(int x) throws MercEnvironmentException;
	
	/**
	 * <p>Set world bottom-left corner y-coordinate related to station bottom-left corner 
	 * @param y corner y-coordinate related to station bottom-left corner
	 * @return self
	 * @throws MercEnvironmentException
	 */
	World setWorldY(int y) throws MercEnvironmentException;
	
	/**
	 * <p>Set world width</p>
	 * @param width world width
	 * @return self
	 * @throws MercEnvironmentException
	 */
	World setWorldWidth(int width) throws MercEnvironmentException;

	/**
	 * <p>Set world height</p>
	 * @param height world height
	 * @return self
	 * @throws MercEnvironmentException
	 */
	World setWorldHeight(int height) throws MercEnvironmentException;
	
	/**
	 * <p>Set world level.</p>
	 * @param level world level. Must be greater than current</p>
	 * @return self
	 * @throws MercEnvironmentException
	 */
	World setWorldLevel(int level) throws MercEnvironmentException;

	/**
	 * <p>Get station x-coordinate</p>
	 * @return always return 0
	 */
	int getStationX();
	
	/**
	 * <p>Get station y-coordinate</p>
	 * @return always return 0
	 */
	int getStationY();
	
	/**
	 * <p>Get station width</p>
	 * @return station width. Always greater than 0
	 */
	int getStationWidth();
	
	/**
	 * <p>Get station height</p>
	 * @return station height. Always greater than 0
	 */
	int getStationHeight();

	/**
	 * <p>Set station width</p>
	 * @param width station width</p>
	 * @return self
	 * @throws MercContentException
	 */
	World setStationWidth(int width) throws MercContentException;
	
	/**
	 * <p>Set station height</p>
	 * @param height station height</p>
	 * @return self
	 * @throws MercContentException
	 */
	World setStationHeight(int height) throws MercContentException;
	
	/**
	 * <p>Get localizer associated with the world</p>
	 * @return localizer associated. Can't be null
	 * @throws IOException
	 * @throws LocalizationException
	 */
	Localizer getLocalizerAssociated() throws IOException, LocalizationException;
	
	/**
	 * <p>Get all entities in the world</p>
	 * @return all entities in the world. Can be empty but not null
	 * @throws MercContentException
	 */
	Iterable<Entity<?>> content() throws MercContentException;

	/**
	 * <p>Get all entities in the world at the given point</p>
	 * @param x x-coordinate of the point examined
	 * @param y y-coordinate of the point examined
	 * @return all entities intersects with the given point. Can be empty but not null
	 * @throws MercContentException
	 */
	Iterable<Entity<?>> content(int x, int y) throws MercContentException;

	/**
	 * <p>Get registered entity classes in the world</p>
	 * @return registered classes. Can be empty but not null
	 * @throws MercContentException
	 */
	Iterable<EntityClassDescription<?>> registered() throws MercContentException;

	/**
	 * <p>Get registered entity classes in the world</p>
	 * @param entityClass entity types
	 * @return registered classes. if missing, return null
	 * @throws MercContentException
	 */
	Iterable<EntityClassDescription<?>> registered(EntityClass entityClass) throws MercContentException;

	/**
	 * <p>Get registered entity classes in the world</p>
	 * @param entityClass entity types
	 * @param entitySubclass subclass name.
	 * @return registered class. If missing, return null
	 * @throws MercContentException
	 */
	EntityClassDescription<?> registered(EntityClass entityClass, String entitySubclass) throws MercContentException;

	/**
	 * <p>Get registered entity classes in the world</p>
	 * @param entityClassId entity class id
	 * @return registered class. If missing, return null
	 * @throws MercContentException
	 */
	EntityClassDescription<?> registered(UUID entityClassId) throws MercContentException;
	
	/**
	 * <p>Get all entities in the world at the given area</p>
	 * @param x x-coordinate of the bottom-left corner of the area examined
	 * @param y y-coordinate of the bottom-left corner of the area examined
	 * @param width area width
	 * @param height area height
	 * @return all entities intersects with the given area. Can be empty but not null
	 * @throws MercContentException
	 */
	Iterable<Entity<?>> content(int x, int y, int width, int height) throws MercContentException;
	
	/**
	 * <p>Get all entities in the world with the given class</p>
	 * @param entityClass entity class to get instances for. Can't be null
	 * @return all entities with the given class. Can be empty but not null
	 * @throws MercContentException
	 */
	Iterable<Entity<?>> content(EntityClass entityClass) throws MercContentException;
	
	/**
	 * <p>Get all entities in the world with the given class and subclass</p>
	 * @param entityClass entity class to get instances for. Can't be null
	 * @param subclassPattern any non-null and non-empty string. Can contains wildcards '*' and '?'
	 * @return all entities with the given class and subclass pattern. Can be empty but not null
	 * @throws MercContentException
	 */
	Iterable<Entity<?>> content(EntityClass entityClass, String subclassPattern) throws MercContentException;
	
	/**
	 * <p>Place entity into the world</p>
	 * @param entity entity to place. Can't be null
	 * @return self
	 * @throws MercContentException entite can't be placed
	 */
	<State extends Enum<State>> World placeEntity(Entity<State> entity) throws MercContentException;
	
	/**
	 * <p>Is entity exists</p>
	 * @param entity entity id to test
	 * @return true if exists
	 */
	boolean isEntityExists(UUID entity);
	
	/**
	 * <p>Get entity by it's id</p>
	 * @param entity entity id to get
	 * @return entity found. If missing, null returns
	 * @throws MercContentException
	 */
	Entity<?> getEntity(UUID entity) throws MercContentException;
	
	/**
	 * <p>Is named entity exists</p> 
	 * @param name entity name. Can't be null or empty
	 * @return true if exists
	 */
	boolean isEntityExists(String name);
	
	/**
	 * <p>Get entity by it's name</p>
	 * @param name entity name. Can't be null or empty
	 * @return entity found. If missing, null returns
	 * @throws MercContentException
	 */
	<State extends Enum<State>> Entity<State> getEntity(String name) throws MercContentException;
	
	/**
	 * <p>Remove entity by it's id</p>
	 * @param entityId entity id to remove
	 * @return entity removed
	 * @throws MercContentException if entity doesn't exist
	 */
	<State extends Enum<State>> Entity<State> removeEntity(UUID entityId) throws MercContentException;
	
	/**
	 * <p>Lock the given point in the world</p>
	 * @param entity lock owner
	 * @param x x-coordinate of the point to lock
	 * @param y y-coordinate of the point to lock
	 * @return lock. Will wait until lock will be available
	 * @throws MercContentException
	 */
	WorldLocker lock(UUID entity, int x, int y) throws MercContentException;

	/**
	 * <p>Lock the given area in the world</p>
	 * @param entity lock owner
	 * @param x x-coordinate of the bottom-left corner of the area to lock
	 * @param y y-coordinate of the bottom-left corner of the area to lock
	 * @param width area width
	 * @param height area width
	 * @return lock. Will wait until lock will be available
	 * @throws MercContentException
	 */
	WorldLocker lock(UUID entity, int x, int y, int width, int height) throws MercContentException;
	
	/**
	 * <p>Lock the given track in the world</p>
	 * @param entity lock owner
	 * @param track track to lock
	 * @return lock. Will wait until lock will be available
	 * @throws MercContentException
	 */
	WorldLocker lock(UUID entity, Track track) throws MercContentException;

	/**
	 * <p>Try to lock the given point in the world</p>
	 * @param entity lock owner
	 * @param x x-coordinate of the point to lock
	 * @param y y-coordinate of the point to lock
	 * @return lock. if lock is not taken, return immediately with null 
	 * @throws MercContentException
	 */
	WorldLocker tryLock(UUID entity, int x, int y) throws MercContentException;

	/**
	 * <p>Try to lock the given area in the world</p>
	 * @param entity lock owner
	 * @param x x-coordinate of the bottom-left corner of the area to lock
	 * @param y y-coordinate of the bottom-left corner of the area to lock
	 * @param width area width
	 * @param height area width
	 * @return lock. if lock is not taken, return immediately with null
	 * @throws MercContentException
	 */
	WorldLocker tryLock(UUID entity, int x, int y, int width, int height) throws MercContentException;
	
	/**
	 * <p>Try to lock the given track in the world</p>
	 * @param entity lock owner
	 * @param track track to lock
	 * @return lock. if lock is not taken, return immediately with null
	 * @throws MercContentException
	 */
	WorldLocker tryLock(UUID entity, Track track) throws MercContentException;
	
	/**
	 * <p>Is the point available to locate persistent entity</p>
	 * @param x x-coordinate of the point to test
	 * @param y y-coordinate of the point to test
	 * @return true if yes
	 * @throws MercContentException
	 */
	boolean isAvailable(int x, int y) throws MercContentException;

	/**
	 * <p>Is the point free to locate any entity</p>
	 * @param x x-coordinate of the point to test
	 * @param y y-coordinate of the point to test
	 * @return true if yes
	 * @throws MercContentException
	 */
	boolean isFree(int x, int y) throws MercContentException;
	
	/**
	 * <p>Refresh and redraw world</p>
	 * @return self
	 * @throws MercContentException
	 */
	World refresh() throws MercContentException;
	
	/**
	 * <p>Process program tick in the world</p>
	 * @return self
	 * @throws MercContentException
	 */
	World tick() throws MercContentException;
	
	/**
	 * <p>Get runtime interface for the given world</p>
	 * @return runtime interface. Can't be null
	 */
	RuntimeInterface getRuntime();
	
	/**
	 * <p>Get presentation environment for the given presentation type</p>
	 * @param type presentation type to get environment for. Can't be null
	 * @return presentation environment. Can't be null
	 * @throws MercEnvironmentException if the world doesn't support the given environment
	 */
	<T> T getPresentationEnvironment(PresentationType type) throws MercEnvironmentException;
	
	/**
	 * <p>Get total serializer/deserializer for the world</p>
	 * @return total serializer/deserializer. Can't be null
	 * @throws MercEnvironmentException
	 */
	SerializableItem total() throws MercEnvironmentException;
	
	/**
	 * <p>Get updates serializer/deserializer for the world</p>
	 * @param timestamp timestamp for changes
	 * @return updates serializer/deserializer. Can't be null
	 * @throws MercEnvironmentException
	 */
	SerializableItem updates(long timestamp) throws MercEnvironmentException;

	/**
	 * <p>Process game time delay for any program</p>
	 * @param delay 
	 * @throws MercEnvironmentException
	 */
	void delayGameTime(long delay) throws MercEnvironmentException;
	
	/**
	 * <p>Get payment panel to support online payments</p>
	 * @return payment panel. Can't be null
	 * @throws MercEnvironmentException
	 */
	PaymentPanel getPaymentPanel() throws MercEnvironmentException;
}
