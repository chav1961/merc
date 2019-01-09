package chav1961.merc.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.TreeMap;
import java.util.UUID;

import chav1961.merc.api.Track;
import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.api.exceptions.MercEnvironmentException;
import chav1961.merc.api.interfaces.front.Entity;
import chav1961.merc.api.interfaces.front.EntityClass;
import chav1961.merc.api.interfaces.front.EntityClassDescription;
import chav1961.merc.api.interfaces.front.EntityStateDescriptor;
import chav1961.merc.api.interfaces.front.PresentationType;
import chav1961.merc.api.interfaces.front.RuntimeInterface;
import chav1961.merc.api.interfaces.front.SerializableItem;
import chav1961.merc.api.interfaces.front.StateChangedListener;
import chav1961.merc.api.interfaces.front.TickableEntity;
import chav1961.merc.api.interfaces.front.World;
import chav1961.purelib.basic.SequenceIterator;
import chav1961.purelib.basic.Utils;
import chav1961.purelib.basic.exceptions.LocalizationException;
import chav1961.purelib.i18n.interfaces.Localizer;

public abstract class AbstractWorld implements World {
	private static final Iterator<Entity<?>>		NULL_ITERATOR = new Iterator<Entity<?>>() {
														@Override public boolean hasNext() {return false;}
														@Override public Entity<?> next() {return null;}
													};
   private static final Iterable<Entity<?>>			NULL_COMPONENTS = new Iterable<Entity<?>>() {
														@Override public Iterator<Entity<?>> iterator() {return NULL_ITERATOR;}
													};

	private final Map<EntityClass,Map<String,EntityClassDescription<?>>> 	registeredByClass = new HashMap<>();
	private final Map<UUID,EntityClassDescription<?>> 						registeredByUUID = new HashMap<>();
	private final Map<UUID,Entity<?>> 										entitiesByUUID = new HashMap<>();
	private final Map<String,Entity<?>> 									entitiesByName = new HashMap<>();
	private final Map<EntityClass,Map<String,List<Entity<?>>>> 				entitiesByClass = new HashMap<>();
	private final TreeMap<Long,List<Entity<?>>>								entitiesByLocation = new TreeMap<>();
	private final Map<Long,UUID>											worldLocks = new HashMap<>();
	private final List<Entity<?>>											updated = new ArrayList<>();
	private final RuntimeInterface 		runtime;
	private final StateChangedListener	listener = new StateChangedListener() {
											@Override
											public <State extends Enum<State>> void stateChanged(final Entity<State> entity, final EntityStateDescriptor<State> oldState, final EntityStateDescriptor<State> newState, final int flags) throws MercContentException {
												if ((flags & (X_CHANGED|Y_CHANGED|WIDTH_CHANGED|HEIGHT_CHANGED)) != 0) {
													internalRemove(entity,new Track(oldState.getX(),oldState.getY(),oldState.getWidth(),oldState.getHeight()));
													internalPlace(entity,new Track(newState.getX(),newState.getY(),newState.getWidth(),newState.getHeight()));
												}
												synchronized(updated) {
													updated.add(entity);
												}
												refresh();
											}
										};
	
	private long	worldTimestamp = 0;
	private int		worldLevel = 0;
	private int		worldX = -5, worldY = -5, worldWidth = 30, worldHeight = 30;
	private int		stationX = 0, stationY = 0, stationWidth = 20, stationHeight = 20;
	private int		refreshCounter = 0;

	protected AbstractWorld(final RuntimeInterface runtime) {
		this.runtime = runtime;
		for (EntityClassDescription<?> item : ServiceLoader.load(EntityClassDescription.class)) {
			if (!registeredByClass.containsKey(item.getEntityClass())) {
				registeredByClass.put(item.getEntityClass(),new HashMap<>());
			}
			registeredByClass.get(item.getEntityClass()).put(item.getEntitySubclass(),item);
			registeredByUUID.put(item.getEntityClassId(),item);
		}
	}

	@Override public abstract Localizer getLocalizerAssociated() throws IOException, LocalizationException;
	@Override public abstract <T> T getPresentationEnvironment(PresentationType type) throws MercEnvironmentException;
	@Override public abstract SerializableItem total() throws MercEnvironmentException;
	@Override public abstract SerializableItem updates(long timestamp) throws MercEnvironmentException;

	@Override
	public long getWorldTimestamp() {
		return worldTimestamp;
	}
	
	@Override
	public World setWorldTimestamp(final long timestamp) {
		if (timestamp < 0) {
			throw new IllegalArgumentException("World timestamp ["+timestamp+"] less than zero"); 
		}
		else {
			this.worldTimestamp = timestamp;
			return this;
		}
	}
	
	@Override
	public int getWorldLevel() {
		return worldLevel;
	}

	@Override
	public int getWorldX() {
		return worldX;
	}

	@Override
	public int getWorldY() {
		return worldY;
	}

	@Override
	public int getWorldWidth() {
		return worldWidth;
	}

	@Override
	public int getWorldHeight() {
		return worldHeight;
	}

	@Override
	public World setWorldX(final int x) throws MercEnvironmentException {
		if (x > this.worldX) {
			throw new MercEnvironmentException("Can't decrease world x");
		}
		else {
			this.worldX = x;
			return this;
		}
	}

	@Override
	public World setWorldY(final int y) throws MercEnvironmentException {
		if (y > this.worldY) {
			throw new MercEnvironmentException("Can't decrease world y");
		}
		else {
			this.worldY = y;
			return this;
		}
	}

	@Override
	public World setWorldWidth(final int width) throws MercEnvironmentException {
		if (width < this.worldWidth) {
			throw new MercEnvironmentException("Can't decrease world width");
		}
		else {
			this.worldWidth = width;
			return this;
		}
	}

	@Override
	public World setWorldHeight(final int height) throws MercEnvironmentException {
		if (height < this.worldHeight) {
			throw new MercEnvironmentException("Can't decrease world height");
		}
		else {
			this.worldHeight = height;
			return this;
		}
	}

	@Override
	public World setWorldLevel(final int level) throws MercEnvironmentException {
		if (level < this.worldLevel) {
			throw new MercEnvironmentException("Can't decrease world level");
		}
		else {
			this.worldLevel = level;
			return this;
		}
	}

	@Override
	public int getStationX() {
		return stationX;
	}

	@Override
	public int getStationY() {
		return stationY;
	}

	@Override
	public int getStationWidth() {
		return stationWidth;
	}

	@Override
	public int getStationHeight() {
		return stationHeight;
	}

	@Override
	public World setStationWidth(final int width) throws MercContentException {
		if (width < stationWidth) {
			throw new MercContentException("Can't decrease station width");
		}
		else {
			this.stationWidth = width;
			return this;
		}
	}

	@Override
	public World setStationHeight(final int height) throws MercContentException {
		if (height < stationHeight) {
			throw new MercContentException("Can't decrease station height");
		}
		else {
			this.stationHeight = height;
			return this;
		}
	}

	@Override
	public Iterable<EntityClassDescription<?>> registered() throws MercContentException {
		final List<EntityClassDescription<?>>	result = new ArrayList<>();
		
		for (Entry<EntityClass, Map<String, EntityClassDescription<?>>> item : registeredByClass.entrySet()) {
			for (Entry<String, EntityClassDescription<?>> subitem : item.getValue().entrySet()) {
				result.add(subitem.getValue());
			}
		}
		return result;
	}

	@Override
	public Iterable<EntityClassDescription<?>> registered(final EntityClass entityClass) throws MercContentException {
		if (entityClass == null) {
			throw new NullPointerException("Entity class can't be null");
		}
		else  if (registeredByClass.containsKey(entityClass)) {
			return registeredByClass.get(entityClass).values();
		}
		else {
			return null;
		}
	}

	@Override
	public EntityClassDescription<?> registered(final EntityClass entityClass, final String entitySubclass) throws MercContentException {
		if (entityClass == null) {
			throw new NullPointerException("Entity class can't be null");
		}
		else if (entitySubclass == null || entitySubclass.isEmpty()) {
			throw new IllegalArgumentException("Entity subclass can't be null or empty");
		}
		else  if (registeredByClass.containsKey(entityClass)) {
			return registeredByClass.get(entityClass).get(entitySubclass);
		}
		else {
			return null;
		}
	}
	
	@Override
	public EntityClassDescription<?> registered(final UUID entityClassId) throws MercContentException {
		if (entityClassId == null) {
			throw new NullPointerException("Entity class can't be null");
		}
		else  if (registeredByUUID.containsKey(entityClassId)) {
			return registeredByUUID.get(entityClassId);
		}
		else {
			return null;
		}
	}
	
	
	@Override
	public Iterable<Entity<?>> content() throws MercContentException {
		return new Iterable<Entity<?>>() {
			@Override
			public Iterator<Entity<?>> iterator() {
				return new Iterator<Entity<?>>() {
					final Iterator<Entry<UUID,Entity<?>>>	temp = entitiesByUUID.entrySet().iterator(); 
					
					@Override public boolean hasNext() {return temp.hasNext();}
					@Override public Entity<?> next() {return temp.next().getValue();}
				};
			}
		};
	}

	@Override
	public Iterable<Entity<?>> content(final int x, final int y) throws MercContentException {
		if (x < getWorldX() || x > getWorldX() + getWorldWidth()) {
			throw new MercContentException("X ["+x+"] is out of range ["+getWorldX()+".."+(getWorldX()+getWorldWidth())+"]");
		}
		else if (y < getWorldY() || y > getWorldY() + getWorldHeight()) {
			throw new MercContentException("Y ["+y+"] is out of range ["+getWorldY()+".."+(getWorldY()+getWorldHeight())+"]");
		}
		else {
			return new Iterable<Entity<?>>() {
				@Override
				public Iterator<Entity<?>> iterator() {
					final Long	key = (((long)x) << 32) | ((long)y);
					
					if (!entitiesByLocation.containsKey(key)) {
						return NULL_ITERATOR;
					}
					else {
						return entitiesByLocation.get(key).iterator();
					}
				}
			};
		}
	}

	@Override
	public Iterable<Entity<?>> content(final int x, final int y, final int width, final int height) throws MercContentException {
		if (x < getWorldX() || x > getWorldX() + getWorldWidth()) {
			throw new MercContentException("X ["+x+"] is out of range ["+getWorldX()+".."+(getWorldX()+getWorldWidth())+"]");
		}
		else if (y < getWorldY() || y > getWorldY() + getWorldHeight()) {
			throw new MercContentException("Y ["+y+"] is out of range ["+getWorldY()+".."+(getWorldY()+getWorldHeight())+"]");
		}
		else if (x + width < getWorldX() || x + width > getWorldX() + getWorldWidth()) {
			throw new MercContentException("X+width ["+(x+width)+"] is out of range ["+getWorldX()+".."+(getWorldX()+getWorldWidth())+"]");
		}
		else if (y + height < getWorldY() || y + height > getWorldY() + getWorldHeight()) {
			throw new MercContentException("Y+height ["+(y+height)+"] is out of range ["+getWorldY()+".."+(getWorldY()+getWorldHeight())+"]");
		}
		else {
			final Track						track = new Track(x,y,width,height);
			final List<Iterator<Entity<?>>>	collection = new ArrayList<>();
			
			track.walk((xP,yP)->{
				final Long	key = (((long)x) << 32) | ((long)y);
				
				if (entitiesByLocation.containsKey(key)) {
					collection.add(entitiesByLocation.get(key).iterator());
				}
				return true;
			});
			return new Iterable<Entity<?>>() {
				@Override
				public Iterator<Entity<?>> iterator() {
					return new SequenceIterator<Entity<?>>(collection.toArray(new Iterator[collection.size()]));
				}
			};
		}
	}

	@Override
	public Iterable<Entity<?>> content(final EntityClass entityClass) throws MercContentException {
		return content(entityClass,"*");
	}

	@Override
	public Iterable<Entity<?>> content(final EntityClass entityClass, final String subclassPattern) throws MercContentException {
		if (entityClass == null) {
			throw new NullPointerException("Entity class can't be null");
		}
		else if (subclassPattern == null || subclassPattern.isEmpty()) {
			throw new IllegalArgumentException("Entity subclass can't be null or empty");
		}
		else if (!entitiesByClass.containsKey(entityClass)) {
			return NULL_COMPONENTS;
		}
		else if (subclassPattern.equals("*")) {
			final List<Iterator<Entity<?>>>	collection = new ArrayList<>();
			
			for (Entry<String, List<Entity<?>>> item : entitiesByClass.get(entityClass).entrySet()) {
				collection.add(item.getValue().iterator());
			}
			return new Iterable<Entity<?>>() {
				@Override
				public Iterator<Entity<?>> iterator() {
					return new SequenceIterator<>(collection.toArray(new Iterator[collection.size()]));
				}
			};
		}
		else if (subclassPattern.indexOf('*') == -1) {
			if (!entitiesByClass.get(entityClass).containsKey(subclassPattern)) {
				return NULL_COMPONENTS;
			}
			else {
				return entitiesByClass.get(entityClass).get(subclassPattern);
			}
		}
		else {
			final List<Iterator<Entity<?>>>	collection = new ArrayList<>();
			final Pattern					pattern = Pattern.compile(Utils.fileMask2Regex(subclassPattern));
			
			for (Entry<String, List<Entity<?>>> item : entitiesByClass.get(entityClass).entrySet()) {
				if (pattern.matcher(item.getKey()).matches()) {
					collection.add(item.getValue().iterator());
				}
			}
			return new Iterable<Entity<?>>() {
				@Override
				public Iterator<Entity<?>> iterator() {
					return new SequenceIterator<Entity<?>>(collection.toArray(new Iterator[collection.size()]));
				}
			};
		}
	}

	@Override
	public <State extends Enum<State>> World placeEntity(final Entity<State> entity) throws MercContentException {
		if (entity == null) {
			throw new NullPointerException("Entity to place can't be null");
		}
		else if (isEntityExists(entity.getId())) {
			throw new MercContentException("Attempt to place duplicated entity");
		}
		else {
			internalPlace(entity,new Track(entity.getX(),entity.getY(),entity.getWidth(),entity.getHeight()));
			entity.addStateChangedListener(listener);
			return this;
		}
	}

	@Override
	public boolean isEntityExists(final UUID entity) {
		if (entity == null) {
			throw new NullPointerException("Entity id can't be null"); 
		}
		else {
			return entitiesByUUID.containsKey(entity); 
		}
	}

	@Override
	public Entity<?> getEntity(final UUID entity) throws MercContentException {
		if (entity == null) {
			throw new NullPointerException("Entity id can't be null"); 
		}
		else {
			return (Entity<?>) entitiesByUUID.get(entity); 
		}
	}

	@Override
	public boolean isEntityExists(final String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Entity name can't be null or empty"); 
		}
		else {
			return entitiesByName.containsKey(name); 
		}
	}

	@Override
	public <State extends Enum<State>> Entity<State> getEntity(final String name) throws MercContentException {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Entity name can't be null or empty"); 
		}
		else {
			return (Entity<State>) entitiesByName.get(name); 
		}
	}

	@Override
	public <State extends Enum<State>> Entity<State> removeEntity(final UUID entityId) throws MercContentException {
		if (entityId == null) {
			throw new NullPointerException("Entity id can't be null");
		}
		else if (!isEntityExists(entityId)) {
			throw new MercContentException("Entity id ["+entityId+"] id not known in the world");
		}
		else {
			final Entity<?>	result = getEntity(entityId);
			
			result.removeStateChangedListener(listener);
			internalRemove(result,new Track(result.getX(),result.getY(),result.getWidth(),result.getHeight()));
			return (Entity<State>) result;
		}
	}

	@Override
	public WorldLocker lock(final UUID entity, final int x, final int y) throws MercContentException {
		return tryLock(entity, x, y);
	}

	@Override
	public WorldLocker lock(final UUID entity, final int x, final int y, final int width, final int height) throws MercContentException {
		return tryLock(entity, x, y, width, height);
	}

	@Override
	public WorldLocker lock(final UUID entity, final Track track) throws MercContentException {
		return tryLock(entity, track);
	}

	@Override
	public WorldLocker tryLock(final UUID entity, final int x, final int y) throws MercContentException {
		if (entity == null) {
			throw new NullPointerException("Owner entity can't be null");
		}
		else if (x < getWorldX() || x > getWorldX() + getWorldWidth()) {
			throw new MercContentException("X ["+x+"] is out of range ["+getWorldX()+".."+(getWorldX()+getWorldWidth())+"]");
		}
		else if (y < getWorldY() || y > getWorldY() + getWorldHeight()) {
			throw new MercContentException("Y ["+y+"] is out of range ["+getWorldY()+".."+(getWorldY()+getWorldHeight())+"]");
		}
		else {
			final Long	key = (((long)x) << 32) | ((long)y);
			
			synchronized(worldLocks) {
				if (!worldLocks.containsKey(key)) {
					worldLocks.put(key,entity);
					return new WorldLocker() {
						final Track	track = new Track(x,y);
						
						@Override public UUID getOwner() {return entity;}
						@Override public Track getLockedCells() {return track;}
						@Override  
						public void close() throws MercContentException {
							synchronized(worldLocks) {
								worldLocks.remove(key);
							}
						}
					};
				}
				else {
					return null;
				}
			}
		}
	}

	@Override
	public WorldLocker tryLock(UUID entity, int x, int y, int width, int height) throws MercContentException {
		if (entity == null) {
			throw new NullPointerException("Owner entity can't be null");
		}
		else if (x < getWorldX() || x > getWorldX() + getWorldWidth()) {
			throw new MercContentException("X ["+x+"] is out of range ["+getWorldX()+".."+(getWorldX()+getWorldWidth())+"]");
		}
		else if (x + width < getWorldX() || x + width > getWorldX() + getWorldWidth()) {
			throw new MercContentException("X+width ["+(x+width)+"] is out of range ["+getWorldX()+".."+(getWorldX()+getWorldWidth())+"]");
		}
		else if (y + height < getWorldY() || y + height > getWorldY() + getWorldHeight()) {
			throw new MercContentException("Y+height ["+(y+height)+"] is out of range ["+getWorldY()+".."+(getWorldY()+getWorldHeight())+"]");
		}
		else {
			synchronized(worldLocks) {
				final Track		track = new Track(x,y,width,height);
				final boolean[]	result = new boolean[] {true};
				
				track.walk((xP,yP)->{return result[0] = !worldLocks.containsKey(((long)(xP) << 32) | (long)(yP));});
				if (!result[0]) {
					return null;
				}
				else {
					track.walk((xP,yP)->{worldLocks.put(((long)(xP) << 32) | (long)(yP),entity); return true;});
					return new WorldLocker() {
						@Override public UUID getOwner() {return entity;}
						@Override public Track getLockedCells() {return track;}
						@Override  
						public void close() throws MercContentException {
							synchronized(worldLocks) {
								track.walk((xP,yP)->{worldLocks.remove(((long)(xP) << 32) | (long)(yP)); return true;});
							}
						}
					};
				}
			}
		}
	}

	@Override
	public WorldLocker tryLock(UUID entity, Track track) throws MercContentException {
		if (entity == null) {
			throw new NullPointerException("Owner entity can't be null");
		}
		else if (track == null) {
			throw new NullPointerException("Track can't be null");
		}
		else {
			synchronized(worldLocks) {
				final boolean[]	result = new boolean[] {true};
				
				track.walk((xP,yP)->{return result[0] = !worldLocks.containsKey(((long)(xP) << 32) | (long)(yP));});
				if (!result[0]) {
					return null;
				}
				else {
					track.walk((xP,yP)->{worldLocks.put(((long)(xP) << 32) | (long)(yP),entity); return true;});
					return new WorldLocker() {
						@Override public UUID getOwner() {return entity;}
						@Override public Track getLockedCells() {return track;}
						@Override  
						public void close() throws MercContentException {
							synchronized(worldLocks) {
								track.walk((xP,yP)->{worldLocks.remove(((long)(xP) << 32) | (long)(yP)); return true;});
							}
						}
					};
				}
			}
		}
	}

	@Override
	public boolean isAvailable(final int x, final int y) throws MercContentException {
		for (Entity<?> item : content(x,y)) {
			switch (item.getClassDescription().getEntityClass()) {
				case Buildings:
				case Pipes:
				case Tracks:
					return false;
				case Resources:
				case Fake:
				case Land:
				case Life:
				case None:
				case Robots:
				case System:
					break;
				default:
					throw new UnsupportedOperationException("Item class ["+item.getClassDescription().getEntityClass()+"] is not supported yet");
			}
		}
		return true;
	}

	@Override
	public boolean isFree(int x, int y) throws MercContentException {
		for (Entity<?> item : content(x,y)) {
			return false;
		}
		return true;
	}

	@Override
	public World refresh() throws MercContentException {
		refreshCounter++;
		return this;
	}

	@Override
	public World tick() throws MercContentException {
		synchronized(updated) {
			updated.clear();
		}
		for (Entry<String, Entity<?>> item : entitiesByName.entrySet()) {
			if (item.getValue() instanceof TickableEntity) {
				((TickableEntity)item.getValue()).tick();
			}
		}
		return this;
	}

	@Override
	public RuntimeInterface getRuntime() {
		return runtime;
	}

	protected void internalPlace(final Entity<?> entity, final Track location) throws MercContentException {
		entitiesByUUID.put(entity.getId(),entity);
		if (entity instanceof TickableEntity) {
			entitiesByName.put(((TickableEntity)entity).getName(),entity);
		}
		if (!entitiesByClass.containsKey(entity.getClassDescription().getEntityClass())) {
			entitiesByClass.put(entity.getClassDescription().getEntityClass(),new HashMap<>());
		}
		if (!entitiesByClass.get(entity.getClassDescription().getEntityClass()).containsKey(entity.getClassDescription().getEntitySubclass())) {
			entitiesByClass.get(entity.getClassDescription().getEntityClass()).put(entity.getClassDescription().getEntitySubclass(),new ArrayList<>());
		}
		entitiesByClass.get(entity.getClassDescription().getEntityClass()).get(entity.getClassDescription().getEntitySubclass()).add(entity);
		location.walk((xP,yP)->{
			final Long	key = (((long)(xP) << 32) | (long)(yP));
			
			if (!entitiesByLocation.containsKey(key)) {
				entitiesByLocation.put(key,new ArrayList<>());
			}
			entitiesByLocation.get(key).add(entity);
			return true;
		});
		refresh();
	}

	protected void internalRemove(final Entity<?> entity, final Track location) throws MercContentException {
		entitiesByUUID.remove(entity.getId());
		if (entity instanceof TickableEntity) {
			entitiesByName.remove(((TickableEntity)entity).getName());
		}
		entitiesByClass.get(entity.getClassDescription().getEntityClass()).get(entity.getClassDescription().getEntitySubclass()).remove(entity);
		location.walk((xP,yP)->{
			final Long				key = (((long)(xP) << 32) | (long)(yP));
			final List<Entity<?>>	list = entitiesByLocation.get(key); 
			
			list.remove(entity);
			if (list.size() == 0) {
				entitiesByLocation.remove(key);
			}
			return true;
		});
		refresh();
	}
	
	protected boolean testAndResetRefresh() {
		if (refreshCounter > 0) {
			refreshCounter = 0;
			return true;
		}
		else {
			refreshCounter = 0;
			return true;
		}
	}
}
