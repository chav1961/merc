package chav1961.merc.core.landing;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
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

public class Basement implements EntityClassDescription<BasementState> {
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
		return Constants.BASEMENT_CLASS_UUID;
	}

	@Override
	public EntityClass getEntityClass() {
		return EntityClass.Land;
	}

	@Override
	public String getEntitySubclass() {
		return this.getClass().getSimpleName();
	}

	@Override
	public PresentationCallback<BasementState> getPresentation(final PresentationType type) throws MercEnvironmentException {
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
		return CoreConstants.BASEMENT_CLASS_NAME_ID;
	}

	@Override
	public String getClassDescriptionId() {
		return CoreConstants.BASEMENT_CLASS_DESCRIPTION_ID;
	}

	@Override
	public String getTooltipId() {
		return CoreConstants.BASEMENT_CLASS_TOOLTIP_ID;
	}

	@Override
	public String getHelpId() {
		return CoreConstants.BASEMENT_CLASS_HELP_ID;
	}

	@Override
	public boolean isSingleton() {
		return false;
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
	public Entity<BasementState> newInstance(World world) throws MercEnvironmentException {
		return new BasementInstance(UUID.randomUUID(),this,BasementState.Hidden);
	}

	@Override
	public Entity<BasementState> newInstance(World world, UUID instanceId) throws MercEnvironmentException {
		throw new MercEnvironmentException("Basement can't be created with Instance ID assigned");
	}

	@Override
	public void removeInstance(World world, Entity<BasementState> instance) throws MercEnvironmentException {
		// TODO Auto-generated method stub
		
	}
	
	private static class Swing2DPresentation implements PresentationCallback<BasementState> {
		@Override
		public boolean draw(World world, Entity<BasementState> entity, EntityStateDescriptor<BasementState> previousState, EntityClassDescription<BasementState> desc) throws MercContentException, MercEnvironmentException {
			final Graphics2D	g2d = world.getPresentationEnvironment(PresentationType.Swing2D);
			final Color			oldColor = g2d.getColor();
			final Stroke		oldStroke = g2d.getStroke();
			final Paint			oldPaint = g2d.getPaint();
			
			switch (entity.getState()) {
				case Building	:
					final float 	wave = 0.01f * (Math.abs(entity.getRedrawCount() % 50 - 25)) + 0.2f;
					
					g2d.setPaint(new RadialGradientPaint((float)(entity.getX()+0.5*entity.getWidth()), (float)(entity.getY()+0.5*entity.getHeight()),0.5f*wave, new float[]{0.0f, 1.0f}, new Color[]{Color.LIGHT_GRAY, Color.DARK_GRAY}, CycleMethod.REFLECT));
					g2d.fill(new Rectangle2D.Double(entity.getX(),entity.getY(),entity.getWidth(),entity.getHeight()));
					g2d.setPaint(oldPaint);
					break;
				case Hidden		:
					break;
				case Ready		:
					g2d.setColor(Color.GRAY);
					g2d.fillRect(entity.getX(),entity.getY(),entity.getWidth(),entity.getHeight());

					g2d.setStroke(new BasicStroke(0.05f));
					g2d.setColor(Color.WHITE);
					g2d.draw(new Line2D.Double(entity.getX(),entity.getY(),entity.getX(),entity.getY()+entity.getHeight()));
					g2d.draw(new Line2D.Double(entity.getX(),entity.getY()+entity.getHeight(),entity.getX()+entity.getWidth(),entity.getY()+entity.getHeight()));
					g2d.setColor(Color.DARK_GRAY);
					g2d.draw(new Line2D.Double(entity.getX()+entity.getWidth(),entity.getY(),entity.getX()+entity.getWidth(),entity.getY()+entity.getHeight()));
					g2d.draw(new Line2D.Double(entity.getX(),entity.getY(),entity.getX()+entity.getWidth(),entity.getY()));
					break;
				default	:
					break;
			}
			
			g2d.setColor(oldColor);
			g2d.setStroke(oldStroke);
			return true;
		}
	}
}
