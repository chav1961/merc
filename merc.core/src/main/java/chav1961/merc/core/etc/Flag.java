package chav1961.merc.core.etc;


import java.awt.Graphics2D;
import java.io.IOException;
import java.util.Iterator;
import java.util.UUID;

import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.api.exceptions.MercEnvironmentException;
import chav1961.merc.api.interfaces.front.Entity;
import chav1961.merc.api.interfaces.front.EntityClass;
import chav1961.merc.api.interfaces.front.EntityClassDescription;
import chav1961.merc.api.interfaces.front.EntityStateDescriptor;
import chav1961.merc.api.interfaces.front.PresentationCallback;
import chav1961.merc.api.interfaces.front.PresentationType;
import chav1961.merc.api.interfaces.front.StateChangedListener;
import chav1961.merc.api.interfaces.front.World;
import chav1961.merc.core.CoreConstants;
import chav1961.purelib.basic.exceptions.LocalizationException;
import chav1961.purelib.fsys.interfaces.FileSystemInterface;
import chav1961.purelib.i18n.LocalizerFactory;
import chav1961.purelib.i18n.interfaces.Localizer;

public class Flag implements EntityClassDescription<FlagState> {
	private static final Iterator<EntityClassDescription<?>>	NULL_ITERATOR = new Iterator<EntityClassDescription<?>>() {
																	@Override public boolean hasNext() {return false;}
																	@Override public EntityClassDescription<?> next() {return null;}
																};
	private static final Iterable<EntityClassDescription<?>>	NULL_COMPONENTS = new Iterable<EntityClassDescription<?>>() {
																	@Override public Iterator<EntityClassDescription<?>> iterator() {return NULL_ITERATOR;}
																};
	private static Swing2DPresentation	swing2d = new Swing2DPresentation(); 

	private final UUID			classId = UUID.randomUUID();
	private final UUID			instanceId = UUID.randomUUID();
	private final FlagInstance	instance = new FlagInstance(instanceId,this,FlagState.Hidden);

	@Override
	public UUID getEntityClassId() {
		return classId;
	}

	@Override
	public EntityClass getEntityClass() {
		return EntityClass.Buildings;
	}

	@Override
	public String getEntitySubclass() {
		return this.getClass().getSimpleName();
	}

	@Override
	public PresentationCallback<FlagState> getPresentation(final PresentationType type) throws MercEnvironmentException {
		if (type == null) {
			throw new NullPointerException("Presentation type nca't be null");
		}
		else {
			switch (type) {
				case Swing2D : return swing2d;
				case Swing3D :
				case WEB2D :
				case WEB3D :
					throw new UnsupportedOperationException("Presentation type ["+type+"] is not implemented yet");
				default:
					throw new IllegalArgumentException("Presentation type ["+type+"] is unknown in the class");
			}
		}
	}

	@Override
	public FileSystemInterface getResourceRoot() throws MercEnvironmentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Localizer getLocalizerAssociated() throws LocalizationException, IOException {
		return LocalizerFactory.getLocalizer(CoreConstants.CORE_LOCALIZER_URI);
	}

	@Override
	public String getClassNameId() {
		return CoreConstants.FLAG_CLASS_NAME_ID;
	}

	@Override
	public String getClassDescriptionId() {
		return CoreConstants.FLAG_CLASS_DESCRIPTION_ID;
	}

	@Override
	public String getTooltipId() {
		return CoreConstants.FLAG_CLASS_TOOLTIP_ID;
	}

	@Override
	public String getHelpId() {
		return CoreConstants.FLAG_CLASS_HELP_ID;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public String getSingletonName() {
		return null;
	}
	
	@Override
	public boolean isPersistent() {
		return true;
	}

	@Override
	public boolean isAnchorable() {
		return true;
	}

	@Override
	public boolean canHaveName() {
		return false;
	}

	@Override
	public boolean hasFixedLocation() {
		return false;
	}

	@Override
	public boolean hasFixedSize() {
		return true;
	}

	@Override
	public boolean hasOwner() {
		return false;
	}

	@Override
	public int getMinimalLevel2Use() {
		return 0;
	}

	@Override
	public int getRelativeX() {
		return -1;
	}

	@Override
	public int getRelativeY() {
		return -1;
	}

	@Override
	public int getWidth() {
		return 1;
	}

	@Override
	public int getHeight() {
		return 1;
	}

	@Override
	public EntityClassDescription<?> getOwnerDescription() {
		return null;
	}

	@Override
	public Iterable<EntityClassDescription<?>> components() {
		return NULL_COMPONENTS;
	}

	@Override
	public Entity<FlagState> newInstance(final World world) throws MercEnvironmentException {
		return instance;
	}

	@Override
	public Entity<FlagState> newInstance(final World world, final UUID instanceId) throws MercEnvironmentException {
		throw new MercEnvironmentException("Flag can't be created with Instance ID assigned");
	}

	@Override
	public void removeInstance(final World world, final Entity<FlagState> instance) throws MercEnvironmentException {
		final EntityStateDescriptor<FlagState> 	prev = instance.getPreviousState();
		
		instance.setState(FlagState.Hidden).fireStateChanged(prev,StateChangedListener.STATE_CHANGED);
		try{world.removeEntity(instance.getId());
		} catch (MercContentException e) {
			throw new MercEnvironmentException(e);
		}
	}

	private static class Swing2DPresentation implements PresentationCallback<FlagState> {
		@Override
		public boolean draw(final World world, final Entity<FlagState> entity, final EntityStateDescriptor<FlagState> previousState, final EntityClassDescription<FlagState> desc) throws MercContentException, MercEnvironmentException {
			final Graphics2D	g2d = world.getPresentationEnvironment(PresentationType.Swing2D);
			
			return true;
		}
	}
}
