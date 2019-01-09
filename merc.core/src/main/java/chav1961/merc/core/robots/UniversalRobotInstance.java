package chav1961.merc.core.robots;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.UUID;

import javax.naming.Binding;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;

import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.api.exceptions.MercEnvironmentException;
import chav1961.merc.api.interfaces.front.ControlInterface;
import chav1961.merc.api.interfaces.front.Entity;
import chav1961.merc.api.interfaces.front.EntityClassDescription;
import chav1961.merc.api.interfaces.front.ProgramExecutor;
import chav1961.merc.api.interfaces.front.ProgrammableEntity;
import chav1961.merc.api.interfaces.front.World;
import chav1961.merc.core.AbstractEntity;
import chav1961.purelib.basic.exceptions.SyntaxException;
import chav1961.purelib.fsys.interfaces.FileSystemInterface;

public class UniversalRobotInstance extends AbstractEntity<UniversalRobotState> implements ProgrammableEntity {
	private static final Iterator<Entity<?>>			NULL_ITERATOR = new Iterator<Entity<?>>() {
															@Override public boolean hasNext() {return false;}
															@Override public Entity<?> next() {return null;}
														};
	private static final Iterable<Entity<?>>			NULL_COMPONENTS = new Iterable<Entity<?>>() {
															@Override public Iterator<Entity<?>> iterator() {return NULL_ITERATOR;}
														};

	private final UniversalRobotControl		control = new UniversalRobotControl(this);
	private final String 					name = "robo";
	private final World						world;
	private String 							programName = null;
	private ProgramExecutor					executor = null;
	
	UniversalRobotInstance(final World world, final UUID entityId, final EntityClassDescription<UniversalRobotState> classDescription, final UniversalRobotState initialState) throws NullPointerException {
		super(entityId, classDescription, initialState);
		this.world = world;
	}
							
	World getWorld() {
		return world;
	}
	
	@Override
	public Entity<?> getOwner() throws MercEnvironmentException {
		return null;
	}
	
	@Override
	public Iterable<Entity<?>> getComponents() throws MercEnvironmentException {
		return NULL_COMPONENTS;
	}
	
	@Override
	public ControlInterface<UniversalRobotState> getControlInterface() throws MercEnvironmentException {
		return control;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(final String name) throws MercContentException {
		throw new MercContentException("Universal robot can't be renamed");
	}

	@Override
	public void tick() throws MercContentException {
		if (executor != null) {
			executor.tick();
		}
		control.tick();
	}

	@Override
	public String getProgramName() {
		return programName;
	}

	@Override
	public void loadProgram(final World world, final FileSystemInterface library, final String path) throws SyntaxException, MercContentException {
		if (world == null) {
			throw new NullPointerException("World can't be null");
		}
		else if (library == null) {
			throw new NullPointerException("Library can't be null");
		}
		else if (path == null || path.isEmpty()) {
			throw new IllegalArgumentException("Path to program file can't be null or empty");
		}
		else {
			try(final FileSystemInterface	source = library.clone().open(path)) {
				if (!source.exists() || !source.isFile()) {
					throw new MercContentException("Program path ["+path+"] is not exists or is not a file");
				}
				else {
					String	name = source.getName();
					
					if (name.lastIndexOf('.') == -1) {
						throw new MercContentException("Program path ["+path+"]: program name has no extension suffix");
					}
					else {
						name = name.substring(name.lastIndexOf('.')+1);
						executor = null;
						
						for (ScriptEngineFactory item : ServiceLoader.load(ScriptEngineFactory.class)) {
							if (item.getExtensions().contains(name)) {
								final ScriptEngine	engine = item.getScriptEngine();
								final Bindings		bindings = engine.createBindings();
								
								bindings.put("world",world);
								
								try(final Reader	rdr = source.charRead()) {
									executor = (ProgramExecutor) engine.eval(rdr,bindings);
									
								} catch (ScriptException | ClassCastException exc) {
									throw new SyntaxException(0,0,exc.getLocalizedMessage(),exc);
								}
							}
						}
						if (executor == null) {
							programName = null;
							throw new MercContentException("Program path ["+path+"]: unknown language routine for "+name+" extension");
						}
						else {
							programName = path;
						}
					}
				}
			} catch (IOException e) {
				throw new MercContentException("Program path ["+path+"]: I/O error: "+e.getLocalizedMessage(),e);
			}
		}
	}

	@Override
	public void start(final World world) throws MercEnvironmentException {
		if (world == null) {
			throw new NullPointerException("World can't be null"); 
		}
		else if (executor == null) {
			throw new MercEnvironmentException("No program to execute or prefilus loading failed");
		}
		else {
			executor.start(world);
		}
	}

	@Override
	public void pause() throws MercEnvironmentException {
		if (executor == null) {
			throw new MercEnvironmentException("No program to execute or prefilus loading failed");
		}
		else {
			executor.pause();
		}
	}

	@Override
	public void resume() throws MercEnvironmentException {
		if (executor == null) {
			throw new MercEnvironmentException("No program to execute or prefilus loading failed");
		}
		else {
			executor.resume();
		}
	}

	@Override
	public void stop() throws MercEnvironmentException {
		if (executor == null) {
			throw new MercEnvironmentException("No program to execute or prefilus loading failed");
		}
		else {
			executor.stop();
		}
	}
}
