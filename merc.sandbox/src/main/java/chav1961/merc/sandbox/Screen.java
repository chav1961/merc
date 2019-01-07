package chav1961.merc.sandbox;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.api.exceptions.MercEnvironmentException;
import chav1961.merc.api.interfaces.front.Entity;
import chav1961.merc.api.interfaces.front.PresentationType;

public class Screen extends JComponent {
	private static final long serialVersionUID = 1659662023574191068L;
	private static final String		HELLO_WORLD = "Hello, world!";
	private static final double		ASTERISK_CYRCLE_RADIUS = 0.4;
	private static final double		ASTERISK_SIZE = 0.05;
	private static final double		LETTER_SIZE = 0.075;
	private static final double		WINDOW_SIZE = 1;
	private static final float		LINE_WIDTH = 0.01f;
	private static final double[][]	NODES = new double[][]{
											new double[]{1.0, 0.0},
											new double[]{0.25, 0.25},
											new double[]{0.0, 1.0},
											new double[]{-0.25, 0.25},
											new double[]{-1.0, 0.0},
											new double[]{-0.25, -0.25},
											new double[]{0.0, -1.0},
											new double[]{0.25, -0.25},
											new double[]{1.0, 0.0}
										};

	private final SwingWorld	world;		
			
	public Screen() throws MercContentException, MercEnvironmentException {
		world = new SwingWorld();
	}
	
	@Override
	public void paintComponent(final Graphics g) {
		final Graphics2D		g2d = (Graphics2D)g;
		final AffineTransform	oldAt = g2d.getTransform();
		final AffineTransform	at = pickCoordinates(g2d);

		world.setCurrentG2D(g2d);
		g2d.setTransform(at);
		try{world.getPresentation(PresentationType.Swing2D).draw(world,null,null,null);
		
			for (Entity item : world.content()) {
				item.getClassDescription().getPresentation(PresentationType.Swing2D).draw(world,item,null,item.getClassDescription());
			}
		} catch (MercContentException | MercEnvironmentException e) {
			e.printStackTrace();
		}
		g2d.setTransform(oldAt);
	}

	private AffineTransform pickCoordinates(final Graphics2D g2d) {
		final Dimension			screenSize = this.getSize();
		final AffineTransform	result = new AffineTransform();
		
		result.scale(screenSize.getWidth()/world.getWorldWidth(), -screenSize.getHeight()/world.getWorldHeight());
		result.translate(-world.getWorldX(), - (world.getWorldHeight() + world.getWorldY()));
		return result;
	}
}
