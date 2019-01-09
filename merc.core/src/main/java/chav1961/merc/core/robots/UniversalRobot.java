package chav1961.merc.core.robots;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
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

public class UniversalRobot  implements EntityClassDescription<UniversalRobotState> {
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
		return Constants.ROBO_CLASS_UUID;
	}


	@Override
	public EntityClass getEntityClass() {
		return EntityClass.Robots;
	}

	@Override
	public String getEntitySubclass() {
		return getClass().getSimpleName();
	}

	@Override
	public PresentationCallback<UniversalRobotState> getPresentation(PresentationType type) throws MercEnvironmentException {
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
		return CoreConstants.UNIVERSAL_ROBOT_CLASS_NAME_ID;
	}

	@Override
	public String getClassDescriptionId() {
		return CoreConstants.UNIVERSAL_ROBOT_CLASS_DESCRIPTION_ID;
	}

	@Override
	public String getTooltipId() {
		return CoreConstants.UNIVERSAL_ROBOT_CLASS_TOOLTIP_ID;
	}

	@Override
	public String getHelpId() {
		return CoreConstants.UNIVERSAL_ROBOT_CLASS_HELP_ID;
	}

	@Override
	public boolean isSingleton() {
		return true;
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
		return true;
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
	public Entity<UniversalRobotState> newInstance(final World world) throws MercEnvironmentException {
		if (world == null) {
			throw new NullPointerException("World can't be null");
		}
		else {
			return new UniversalRobotInstance(world,Constants.ROBO_INSTANCE_UUID,this,UniversalRobotState.Sleep);
		}
	}

	@Override
	public Entity<UniversalRobotState> newInstance(final World world, final UUID instanceId) throws MercEnvironmentException {
		throw new MercEnvironmentException("Universal robot can't be created with Instance ID assigned");
	}

	@Override
	public void removeInstance(final World world, final Entity<UniversalRobotState> instance) throws MercEnvironmentException {
		throw new MercEnvironmentException("Universal robot can't be removed form the world");
	}

	private static class Swing2DPresentation implements PresentationCallback<UniversalRobotState> {
		@Override
		public boolean draw(World world, Entity<UniversalRobotState> entity, EntityStateDescriptor<UniversalRobotState> previousState, EntityClassDescription<UniversalRobotState> desc) throws MercContentException, MercEnvironmentException {
			final Graphics2D			g2d = world.getPresentationEnvironment(PresentationType.Swing2D);
			final Color					oldColor = g2d.getColor();
			final Stroke				oldStroke = g2d.getStroke();
			final Rectangle2D.Double	leftTrack = new Rectangle2D.Double(entity.getX()+0.1,entity.getY()+0.1,0.15,0.8); 
			final Rectangle2D.Double	rightTrack = new Rectangle2D.Double(entity.getX()+0.75,entity.getY()+0.1,0.15,0.8);
			final Ellipse2D.Double		body = new Ellipse2D.Double(entity.getX()+0.1,entity.getY()+0.1,0.8,0.8);
			final Ellipse2D.Double		tower = new Ellipse2D.Double(entity.getX()+0.25,entity.getY()+0.35,0.3,0.3);
			final Line2D.Double			bound = new Line2D.Double(entity.getX()+0.65,entity.getY()+0.17,entity.getX()+0.65,entity.getY()+0.83);
			final Rectangle2D.Double	block = new Rectangle2D.Double(entity.getX()+0.65,entity.getY()+0.4,0.2,0.2);
			final Rectangle2D.Double	hand = new Rectangle2D.Double(entity.getX()+0.7,entity.getY()+0.6,0.1,0.35);

			g2d.setColor(Color.BLACK);
			g2d.fill(leftTrack);
			g2d.fill(rightTrack);
			g2d.setStroke(new BasicStroke(0.05f));
			g2d.setColor(Color.BLACK);
			g2d.draw(leftTrack);
			g2d.draw(rightTrack);
			g2d.setColor(Color.GREEN);
			g2d.fill(body);
			g2d.setColor(Color.CYAN);
			g2d.fill(tower);
			g2d.setColor(Color.BLACK);
			g2d.draw(body);
			g2d.draw(tower);
			g2d.draw(bound);
			g2d.setColor(Color.ORANGE);
			g2d.fill(block);
			g2d.fill(hand);
			g2d.setColor(Color.BLACK);
			g2d.draw(block);
			g2d.draw(hand);
			
			g2d.setColor(oldColor);
			g2d.setStroke(oldStroke);
			return true;
		}
	}

}
