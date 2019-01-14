package chav1961.merc.core.landing;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.IOException;
import java.util.Iterator;
import java.util.UUID;

import chav1961.merc.api.Constants;
import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.api.exceptions.MercEnvironmentException;
import chav1961.merc.api.interfaces.front.Entity;
import chav1961.merc.api.interfaces.front.EntityClass;
import chav1961.merc.api.interfaces.front.EntityClassDescription;
import chav1961.merc.api.interfaces.front.EntityStateDescriptor;
import chav1961.merc.api.interfaces.front.PresentationCallback;
import chav1961.merc.api.interfaces.front.PresentationType;
import chav1961.merc.api.interfaces.front.World;
import chav1961.merc.core.CoreConstants;
import chav1961.purelib.basic.exceptions.LocalizationException;
import chav1961.purelib.fsys.interfaces.FileSystemInterface;
import chav1961.purelib.i18n.LocalizerFactory;
import chav1961.purelib.i18n.interfaces.Localizer;

public class LandingPad implements EntityClassDescription<LandingPadState> {
	private static final Iterator<EntityClassDescription<?>>	NULL_ITERATOR = new Iterator<EntityClassDescription<?>>() {
																	@Override public boolean hasNext() {return false;}
																	@Override public EntityClassDescription<?> next() {return null;}
																};
	private static final Iterable<EntityClassDescription<?>>	NULL_COMPONENTS = new Iterable<EntityClassDescription<?>>() {
																	@Override public Iterator<EntityClassDescription<?>> iterator() {return NULL_ITERATOR;}
																};

	private static Swing2DPresentation	swing2d = new Swing2DPresentation(); 
	
	@Override
	public UUID getEntityClassId() {
		return Constants.LANDINGPAD_CLASS_UUID;
	}
	
	@Override
	public EntityClass getEntityClass() {
		return EntityClass.Land;
	}
	
	@Override
	public String getEntitySubclass() {
		return getClass().getSimpleName();
	}
	
	@Override
	public PresentationCallback<LandingPadState> getPresentation(PresentationType type) throws MercEnvironmentException {
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
		return CoreConstants.LANDINGPAD_CLASS_NAME_ID;
	}
	
	@Override
	public String getClassDescriptionId() {
		return CoreConstants.LANDINGPAD_CLASS_DESCRIPTION_ID;
	}
	
	@Override
	public String getTooltipId() {
		return CoreConstants.LANDINGPAD_CLASS_TOOLTIP_ID;
	}
	
	@Override
	public String getHelpId() {
		return CoreConstants.LANDINGPAD_CLASS_HELP_ID;
	}
	
	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public String getSingletonName() {
		return getEntitySubclass();
	}
	
	@Override
	public boolean isPersistent() {
		return true;
	}
	
	@Override
	public boolean isAnchorable() {
		return false;
	}
	
	@Override
	public boolean canHaveName() {
		return false;
	}
	
	@Override
	public boolean hasFixedLocation() {
		return true;
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
		return 5;
	}
	
	@Override
	public int getHeight() {
		return 5;
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
	public Entity<LandingPadState> newInstance(final World world) throws MercEnvironmentException {
		return new LandingPadInstance(Constants.LANDINGPAD_UUID,this,LandingPadState.Exists);
	}
	
	@Override
	public Entity<LandingPadState> newInstance(final World world, final UUID instanceId) throws MercEnvironmentException {
		throw new MercEnvironmentException("Landing pag can't be created with Instance ID assigned");
	}
	
	@Override
	public void removeInstance(final World world, final Entity<LandingPadState> instance) throws MercEnvironmentException {
		throw new MercEnvironmentException("Landing pag can't be removed form the world");
	}

	private static class Swing2DPresentation implements PresentationCallback<LandingPadState> {
		@Override
		public boolean draw(World world, Entity<LandingPadState> entity, EntityStateDescriptor<LandingPadState> previousState, EntityClassDescription<LandingPadState> desc) throws MercContentException, MercEnvironmentException {
			final Graphics2D	g2d = world.getPresentationEnvironment(PresentationType.Swing2D);
			final Color			oldColor = g2d.getColor();
			final Stroke		oldStroke = g2d.getStroke();
			
			g2d.setColor(Color.GRAY);
			g2d.fillRect(entity.getX(),entity.getY(),entity.getWidth(),entity.getHeight());

			g2d.setColor(Color.ORANGE);
			g2d.setStroke(new BasicStroke(0.075f));
			for (int index = 0; index < 10; index++) {
				g2d.draw(new Line2D.Double(entity.getX()+0.5,entity.getY()+1+0.3*index,entity.getX()+1.5,entity.getY()+1+0.3*index));
			}
			g2d.draw(new Line2D.Double(entity.getX()+2,entity.getY()+4,entity.getX()+3.5,entity.getY()+4));
			g2d.draw(new Line2D.Double(entity.getX()+2.5,entity.getY()+3,entity.getX()+2.5,entity.getY()+4.5));
			g2d.draw(new Ellipse2D.Double(entity.getX()+3,entity.getY()+2,1.5,1.5));

			g2d.setColor(Color.WHITE);
			for (int index = 0; index < 10; index++) {
				g2d.draw(new Line2D.Double(entity.getX()+2.5+0.2*index,entity.getY()+0.8,entity.getX()+2.5+0.2*index,entity.getY()+1.5));
			}
			
			g2d.setColor(oldColor);
			g2d.setStroke(oldStroke);
			return true;
		}
	}
}
