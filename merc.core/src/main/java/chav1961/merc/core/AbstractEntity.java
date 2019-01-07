package chav1961.merc.core;

import java.io.IOException;
import java.util.UUID;

import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.api.exceptions.MercEnvironmentException;
import chav1961.merc.api.interfaces.front.ControlInterface;
import chav1961.merc.api.interfaces.front.Entity;
import chav1961.merc.api.interfaces.front.EntityClassDescription;
import chav1961.merc.api.interfaces.front.EntityStateDescriptor;
import chav1961.merc.api.interfaces.front.StateChangedListener;
import chav1961.purelib.basic.exceptions.PrintingException;
import chav1961.purelib.basic.exceptions.SyntaxException;
import chav1961.purelib.concurrent.LightWeightListenerList;
import chav1961.purelib.streams.JsonStaxParser;
import chav1961.purelib.streams.JsonStaxPrinter;

public abstract class AbstractEntity<State extends Enum<State>> implements Entity<State> {
	protected static final String				TIMESTAMP_FIELD = "timestamp";
	protected static final String				ID_FIELD = "id";
	protected static final String				CLASS_ID_FIELD = "classId";
	protected static final String				X_FIELD = "x";
	protected static final String				Y_FIELD = "y";
	protected static final String				WIDTH_FIELD = "width";
	protected static final String				HEIGHT_FIELD = "height";
	protected static final String				STATE_FIELD = "state";
	
	protected long								timestamp = 0;	
	protected int								x = 0, y = 0, width = 1, height = 1;

	private final LightWeightListenerList<StateChangedListener>	listeners = new LightWeightListenerList<StateChangedListener>(StateChangedListener.class);
	private UUID								entityId;
	private EntityClassDescription<State>		classDescription;
	private State								currentState;
	private EntityStateDescriptor<State>		previousStateDescriptor = null;

	public AbstractEntity(final EntityClassDescription<State> classDescription, final State initialState) throws NullPointerException {
		this(UUID.randomUUID(),classDescription,initialState);
	}
	
	public AbstractEntity(final UUID entityId, final EntityClassDescription<State> classDescription, final State initialState) throws NullPointerException {
		if (entityId == null) {
			throw new NullPointerException("Entity Id can't be null");
		}
		else if (classDescription == null) {
			throw new NullPointerException("Entity class description can't be null");
		}
		else if (initialState == null) {
			throw new NullPointerException("Initial state can't be null");
		}
		else {
			this.entityId = entityId;
			this.classDescription = classDescription;
			this.currentState = initialState; 
		}
	}

	public AbstractEntity(final AbstractEntity<State> template) throws NullPointerException {
		if (template == null) {
			throw new NullPointerException("Entity template can't be null");
		}
		else {
			this.entityId = template.entityId;
			this.classDescription = template.classDescription;
			this.currentState = template.currentState; 
			this.timestamp = 0;	
			this.x = template.x;
			this.y = template.y;
			this.width = template.width;
			this.height = template.height;
		}
	}
	
	@Override public abstract Entity<?> getOwner() throws MercEnvironmentException;
	@Override public abstract Iterable<Entity<?>> getComponents() throws MercEnvironmentException;
	@Override public abstract ControlInterface<State> getControlInterface() throws MercEnvironmentException;
	
	@Override
	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public int getWidth() {
		if (getClassDescription().hasFixedSize()) {
			return getClassDescription().getWidth();
		}
		else {
			return width;
		}
	}

	@Override
	public int getHeight() {
		if (getClassDescription().hasFixedSize()) {
			return getClassDescription().getHeight();
		}
		else {
			return height;
		}
	}

	@Override
	public State getState() {
		return currentState;
	}

	@Override
	public void serialize(final JsonStaxPrinter printer) throws PrintingException, IOException {
		if (printer == null) {
			throw new NullPointerException("Json printer can't be null");
		}
		else {
			printer.name(TIMESTAMP_FIELD).value(timestamp)
				   .name(ID_FIELD).value(getId().toString())
				   .name(CLASS_ID_FIELD).value(getClassDescription().getEntityClassId().toString())
				   .name(X_FIELD).value(getX())
				   .name(Y_FIELD).value(getY())
				   .name(WIDTH_FIELD).value(getWidth())
				   .name(HEIGHT_FIELD).value(getHeight())
				   .name(STATE_FIELD).value(getState().toString());
		}
	}

	@Override
	public void deserialize(final JsonStaxParser parser) throws SyntaxException, IOException {
		if (parser == null) {
			throw new NullPointerException("Json parser can't be null");
		}
		else {
			if (parser.name().equals(TIMESTAMP_FIELD)) {
				this.timestamp = parser.intValue();
			}
			else {
				throw new SyntaxException(parser.row(),parser.col(),"Field name ["+TIMESTAMP_FIELD+"] is missing");
			}
			if (parser.name().equals(ID_FIELD)) {
				this.entityId = UUID.fromString(parser.stringValue());
			}
			else {
				throw new SyntaxException(parser.row(),parser.col(),"Field name ["+ID_FIELD+"] is missing");
			}
			if (parser.name().equals(CLASS_ID_FIELD)) {
				final UUID	readed;
				
				if (!(readed = UUID.fromString(parser.stringValue())).equals(getClassDescription().getEntityClassId())) {
					throw new SyntaxException(parser.row(),parser.col(),"Field name ["+CLASS_ID_FIELD+"]: class id ["+readed+"] is differ from awaited ["+getClassDescription().getEntityClassId()+"]");
				}
			}
			else {
				throw new SyntaxException(parser.row(),parser.col(),"Field name ["+CLASS_ID_FIELD+"] is missing");
			}
			if (parser.name().equals(X_FIELD)) {
				this.x = (int) parser.intValue();
			}
			else {
				throw new SyntaxException(parser.row(),parser.col(),"Field name ["+X_FIELD+"] is missing");
			}
			if (parser.name().equals(Y_FIELD)) {
				this.y = (int) parser.intValue();
			}
			else {
				throw new SyntaxException(parser.row(),parser.col(),"Field name ["+Y_FIELD+"] is missing");
			}
			if (parser.name().equals(WIDTH_FIELD)) {
				this.width = (int) parser.intValue();
			}
			else {
				throw new SyntaxException(parser.row(),parser.col(),"Field name ["+WIDTH_FIELD+"] is missing");
			}
			if (parser.name().equals(HEIGHT_FIELD)) {
				this.height = (int) parser.intValue();
			}
			else {
				throw new SyntaxException(parser.row(),parser.col(),"Field name ["+HEIGHT_FIELD+"] is missing");
			}
			if (parser.name().equals(STATE_FIELD)) {
				this.currentState = (State) this.currentState.valueOf(this.currentState.getClass(),parser.stringValue());
			}
			else {
				throw new SyntaxException(parser.row(),parser.col(),"Field name ["+STATE_FIELD+"] is missing");
			}
		}
	}

	@Override
	public UUID getId() {
		return entityId;
	}

	@Override
	public EntityClassDescription<State> getClassDescription() {
		return classDescription;
	}

	@Override
	public Entity<State> addStateChangedListener(final StateChangedListener listener) {
		if (listener == null) {
			throw new NullPointerException("Listener to add can't be null");
		}
		else {
			listeners.addListener(listener);
			return this;
		}
	}

	@Override
	public Entity<State> removeStateChangedListener(final StateChangedListener listener) {
		if (listener == null) {
			throw new NullPointerException("Listener to remove can't be null");
		}
		else {
			listeners.removeListener(listener);
			return this;
		}
	}

	@Override
	public boolean wasModifiedAfter(final long timestamp) {
		return timestamp < this.timestamp;
	}

	@Override
	public EntityStateDescriptor<State> getPreviousState() {
		return previousStateDescriptor;
	}

	@Override
	public Entity<State> clearModification(final long timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	@Override
	public Entity<State> setX(final int x) throws MercEnvironmentException {
		if (getClassDescription().hasFixedLocation()) {
			throw new MercEnvironmentException("Attempt to change location for non-relocatable entity ["+getClassDescription().getEntityClass()+","+getClassDescription().getEntitySubclass()+"]");
		}
		else {
			this.x = x;
			return this;
		}
	}

	@Override
	public Entity<State> setY(final int y) throws MercEnvironmentException {
		if (getClassDescription().hasFixedLocation()) {
			throw new MercEnvironmentException("Attempt to change location for non-relocatable entity ["+getClassDescription().getEntityClass()+","+getClassDescription().getEntitySubclass()+"]");
		}
		else {
			this.y = y;
			return this;
		}
	}

	@Override
	public Entity<State> setWidth(final int width) throws MercEnvironmentException {
		if (getClassDescription().hasFixedSize()) {
			throw new MercEnvironmentException("Attempt to change size for non-resizable entity ["+getClassDescription().getEntityClass()+","+getClassDescription().getEntitySubclass()+"]");
		}
		else {
			this.width = width;
			return this;
		}
	}

	@Override
	public Entity<State> setHeight(final int height) throws MercEnvironmentException {
		if (getClassDescription().hasFixedSize()) {
			throw new MercEnvironmentException("Attempt to change size for non-resizable entity ["+getClassDescription().getEntityClass()+","+getClassDescription().getEntitySubclass()+"]");
		}
		else {
			this.height = height;
			return this;
		}
	}

	@Override
	public Entity<State> setState(final State state) throws MercEnvironmentException {
		if (state == null) {
			throw new NullPointerException("New state can't be null");
		}
		else {
			this.currentState = state;
			return this;
		}
	}

	@Override
	public Entity<State> fireStateChanged(final EntityStateDescriptor<State> previousState, final int changes) throws MercEnvironmentException {
		if (previousState == null) {
			throw new NullPointerException("Previous state can't be null");
		}
		else if (changes == 0) {
			throw new IllegalArgumentException("Changes can't be zero, ypu must mark change types by predefined StateChangedListener constants");
		}
		else {
			final MercContentException[]	exception = new MercContentException[] {null};
			
			listeners.fireEvent((callback)->{
				try{callback.stateChanged(this,previousState,this,changes);
				} catch (MercContentException e) {
					exception[0] = e;
				}
			});
			if (exception[0] != null) {
				throw new MercEnvironmentException(exception[0]);
			}
			else {
				this.previousStateDescriptor = previousState;
				return this;
			}
		}
	}
	
	@Override
	public String toString() {
		return "AbstractEntity [timestamp=" + timestamp + ", x=" + x + ", y=" + y + ", width=" + width + ", height="
				+ height + ", entityId=" + entityId + ", classDescription=" + classDescription.getEntityClassId() + ", currentState="
				+ currentState + "]";
	}

	public EntityStateDescriptor<State> getStateSnapshot() {
		return new EntityStateDescriptor<State>() {
			final long	timestamp = AbstractEntity.this.timestamp;
			final int	x = AbstractEntity.this.getX(), y = AbstractEntity.this.getY(), width = AbstractEntity.this.getWidth(), height = AbstractEntity.this.getHeight();
			final State	state = AbstractEntity.this.getState();
			
			@Override public long getTimestamp() {return timestamp;}
			@Override public int getX() {return x;}
			@Override public int getY() {return 0;}
			@Override public int getWidth() {return width;}
			@Override public int getHeight() {return height;}
			@Override public State getState() {return state;}
			@Override public String toString() {return "EntityStateDescriptor [timestamp="+timestamp+", x="+x+", y="+y+", width="+width+", height="+height+", state="+state+"]";}
		};
	}
}
