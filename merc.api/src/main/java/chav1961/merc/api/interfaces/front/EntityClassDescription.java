package chav1961.merc.api.interfaces.front;

import java.io.IOException;
import java.util.UUID;

import chav1961.merc.api.exceptions.MercEnvironmentException;
import chav1961.purelib.basic.exceptions.LocalizationException;
import chav1961.purelib.fsys.interfaces.FileSystemInterface;
import chav1961.purelib.i18n.interfaces.Localizer;

/**
 * <p>This interface is used to get access to entity class description. Any entity in the world must implements this interface
 * and provide access to it thru standard Java SPI mechanism.</p>
 * @see Mercury landing project
 * @author Alexander Chernomyrdin aka chav1961
 * @since 0.0.1
 * @param <State> entity state
 */
public interface EntityClassDescription<State extends Enum<State>> extends DrawableEntity<State> {
	/**
	 * <p>Get entity class id.
	 * @return Any valid UUID. Must be the same UUID for all the life of the given entity, because it will be used in the serialization/deserialization mechanism
	 */
	@MerLan
	UUID getEntityClassId();
	
	/**
	 * <p>Get entity class</p>
	 * @return any valid {@linkplain EntityClass} constant. Can't be null
	 */
	@MerLan
	EntityClass getEntityClass();
	
	/**
	 * <p>Get entity subclass.</p>
	 * @return any valid non-null and non-empty string. A good idea is to return simple class name of the {@linkplain EntityClassDescription} implementation
	 */
	@MerLan
	String getEntitySubclass();
	
	
	/**
	 * <p>Get file system root associated with the given class.</p>
	 * @return any valid file system. Can't be null
	 * @throws MercEnvironmentException
	 */
	FileSystemInterface getResourceRoot() throws MercEnvironmentException;
	
	/**
	 * <p>Get localizer associated with the given entity class.</p> 
	 * @return localizer associated. If the class is a component of any owner, it's owner localizer must be returned instead. 
	 * Owner must have common localizer for all it's components and itself
	 * @throws LocalizationException
	 * @throws IOException
	 */
	Localizer getLocalizerAssociated() throws LocalizationException, IOException;
	
	/**
	 * <p>Get identifier of the class name to use with the localizer</p>
	 * @return any non-null and non-empty string. Key with the returned value must exists in the localizer associated 
	 */
	String getClassNameId();
	
	/**
	 * <p>Get identifier of the class description to use with the localizer</p>
	 * @return any non-null and non-empty string. Key with the returned value must exists in the localizer associated 
	 */
	String getClassDescriptionId();

	/**
	 * <p>Get identifier of the class tooltip to use with the localizer</p>
	 * @return any non-null and non-empty string. Key with the returned value must exists in the localizer associated 
	 */
	String getTooltipId();

	/**
	 * <p>Get identifier of the class help to use with the localizer</p>
	 * @return any non-null and non-empty string. Key with the returned value must exists in the localizer associated 
	 */
	String getHelpId();
	
	/**
	 * <p>Is the class singleton.</p>
	 * @return true if yes. Singleton classes can have the only instance in the world
	 */
	boolean isSingleton();
	
	/**
	 * <p>Get name to access to singleton instance</p>
	 * @return name to access. If entity is not a singleton, returns null;
	 */
	String getSingletonName();
	
	/**
	 * <p>Is the entity persistent.</p>
	 * @return true if yes. Landing elements and basements are usually non-persistent
	 */
	boolean isPersistent();
	
	/**
	 * <p>Is the entity anchorable</p>
	 * @return true if yes. All buildings and pipes are usually anchorable
	 */
	boolean isAnchorable();
	
	/**
	 * <p>Is the class supports naming</p>
	 * @return true if yes. Usually all the classes are implementing {@linkplain NotifiableEntity} or {@linkplain TickableEntity} must support naming 
	 */
	@MerLan
	boolean canHaveName();
	
	/**
	 * <p>Has the instance fixed location in the world.</p>
	 * @return true if yes. Usually the classes that have owner, or some singleton objects can return true 
	 */
	boolean hasFixedLocation();
	
	/**
	 * <p>Has the instance fixed size in the world.</p>
	 * @return true if yes. Usually the buildings, landing elements and pipes can return true
	 */
	boolean hasFixedSize();
	
	/**
	 * <p>Has the class owner (container)</p>
	 * @return true if yes
	 */
	boolean hasOwner();
	
	/**
	 * <p>Get minimal gamer level, when he can use this entity</p> 
	 * @return minimal level. Any positive number. 0 means free to use on any level
	 */
	@MerLan
	int getMinimalLevel2Use();
	
	/**
	 * <p>Get relative x-location of the class to it's owner right bottom corner</p>
	 * @return relative x-location. -1 means float location 
	 */
	@MerLan
	int getRelativeX();
	
	/**
	 * <p>Get relative y-location of the class to it's owner right bottom corner</p>
	 * @return relative y-location. -1 means float location 
	 */
	@MerLan
	int getRelativeY();
	
	/**
	 * <p>Get width of the class instance</p>
	 * @return width of the class instance. -1 means float width
	 */
	int getWidth();
	
	/**
	 * <p>Get height of the class instance</p>
	 * @return width of the class instance. -1 means float width
	 */
	int getHeight();
	
	/**
	 * <p>Get description of the class owner (container).</p>
	 * @return owner description. Can be null, if {@linkplain #hasOwner()} method returns false
	 */
	@MerLan
	EntityClassDescription<?> getOwnerDescription();
	
	/**
	 * <p>Get component list of the class</p>
	 * @return component list. Can be empty but not null
	 */
	@MerLan
	Iterable<EntityClassDescription<?>> components();
	
	/**
	 * <p>Create new instance of the class</p>
	 * @param world world to create instance for
	 * @return instance created with the scratch id
	 * @throws MercEnvironmentException
	 */
	Entity<State> newInstance(World world) throws MercEnvironmentException;
	
	/**
	 * <p>Create new instance of the class and assign id to it</p>
	 * @param world world to create instance for
	 * @param instanceId instance id to assign to the instance created
	 * @return instance created with the given id
	 * @throws MercEnvironmentException
	 */
	Entity<State> newInstance(World world, UUID instanceId) throws MercEnvironmentException;
	
	/**
	 * <p>Remove class instance</p> 
	 * @param world world to remove instance from
	 * @param instance instance to remove
	 * @throws MercEnvironmentException
	 */
	void removeInstance(World world, Entity<State> instance) throws MercEnvironmentException;
}
