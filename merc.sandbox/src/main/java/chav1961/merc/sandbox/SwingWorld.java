package chav1961.merc.sandbox;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import chav1961.merc.api.Constants;
import chav1961.merc.api.Track;
import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.api.exceptions.MercEnvironmentException;
import chav1961.merc.api.interfaces.front.Entity;
import chav1961.merc.api.interfaces.front.EntityClass;
import chav1961.merc.api.interfaces.front.EntityClassDescription;
import chav1961.merc.api.interfaces.front.EntityStateDescriptor;
import chav1961.merc.api.interfaces.front.PresentationCallback;
import chav1961.merc.api.interfaces.front.PresentationType;
import chav1961.merc.api.interfaces.front.RuntimeInterface;
import chav1961.merc.api.interfaces.front.SerializableItem;
import chav1961.merc.api.interfaces.front.World;
import chav1961.merc.api.interfaces.front.WorldState;
import chav1961.merc.core.AbstractWorld;
import chav1961.merc.core.buildings.Teleport;
import chav1961.merc.core.buildings.TeleportInstance;
import chav1961.merc.core.landing.LandingPad;
import chav1961.merc.core.landing.LandingPadInstance;
import chav1961.merc.core.robots.UniversalRobot;
import chav1961.merc.core.robots.UniversalRobotInstance;

public class SwingWorld extends AbstractWorld {
	private static Swing2DPresentation	swing2d = new Swing2DPresentation();
	
	private Graphics2D 	g2d;

	public SwingWorld() throws MercContentException, MercEnvironmentException {
		super(new SwingRuntime());
		final Teleport					teleport = (Teleport) registered(Constants.TELEPORT_CLASS_UUID);
		final LandingPad				landingPad = (LandingPad) registered(Constants.LANDINGPAD_CLASS_UUID);
		final UniversalRobot			universalRobot = (UniversalRobot) registered(Constants.ROBO_CLASS_UUID);
		final TeleportInstance			teleportInstance = (TeleportInstance) teleport.newInstance(this);
		final LandingPadInstance		landingPadInstance = (LandingPadInstance) landingPad.newInstance(this);
		final UniversalRobotInstance	universalRobotInstance = (UniversalRobotInstance) universalRobot.newInstance(this);
		
		internalPlace(teleportInstance,new Track(teleportInstance.getX(),teleportInstance.getY(),teleportInstance.getWidth(),teleportInstance.getHeight()));
		internalPlace(landingPadInstance,new Track(landingPadInstance.getX(),landingPadInstance.getY(),landingPadInstance.getWidth(),landingPadInstance.getHeight()));
		internalPlace(universalRobotInstance,new Track(universalRobotInstance.getX(),universalRobotInstance.getY(),universalRobotInstance.getWidth(),universalRobotInstance.getHeight()));
	}

	void setCurrentG2D(final Graphics2D g2d) {
		this.g2d = g2d;
	}
	
	@Override
	public <T> T getPresentationEnvironment(PresentationType type) throws MercEnvironmentException {
		return (T) g2d;
	}

	@Override
	public SerializableItem total() throws MercEnvironmentException {
		return null;
	}

	@Override
	public SerializableItem updates(long timestamp) throws MercEnvironmentException {
		return null;
	}

	@Override
	public PresentationCallback<WorldState> getPresentation(final PresentationType type) throws MercEnvironmentException {
		return swing2d;
	}
	
	private static class Swing2DPresentation implements PresentationCallback<WorldState> {
		@Override
		public boolean draw(World world, Entity<WorldState> entity, EntityStateDescriptor<WorldState> previousState, EntityClassDescription<WorldState> desc) throws MercContentException, MercEnvironmentException {
			final Graphics2D	g2d = world.getPresentationEnvironment(null);
			final Color			oldColor = g2d.getColor();
			final Stroke		oldStroke = g2d.getStroke();
			
			g2d.setColor(Color.BLACK);
			g2d.fillRect(world.getWorldX(),world.getWorldY(),world.getWorldWidth(),world.getWorldHeight());
			g2d.setColor(Color.LIGHT_GRAY);
			g2d.fillRect(world.getStationX(),world.getStationY(),world.getStationWidth(),world.getStationHeight());
			
			g2d.setColor(Color.DARK_GRAY);
			g2d.setStroke(new BasicStroke(0.01f));
			for (int xIndex = world.getWorldX(), maxXIndex = world.getWorldX() + world.getWorldWidth(); xIndex < maxXIndex; xIndex++) {
				g2d.drawLine(xIndex,world.getWorldY(),xIndex,world.getWorldY()+world.getWorldHeight());
			}
			for (int yIndex = world.getWorldY(), maxYIndex = world.getWorldY() + world.getWorldHeight(); yIndex < maxYIndex; yIndex++) {
				g2d.drawLine(world.getWorldX(),yIndex,world.getWorldX()+world.getWorldWidth(),yIndex);
			}
			
			g2d.setColor(oldColor);
			g2d.setStroke(oldStroke);
			return true;
		}
	}
}
