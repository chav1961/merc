package chav1961.merc.sandbox;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JComponent;

import chav1961.merc.api.exceptions.MercContentException;
import chav1961.merc.api.exceptions.MercEnvironmentException;
import chav1961.merc.api.interfaces.front.Entity;
import chav1961.merc.api.interfaces.front.EntityClass;
import chav1961.merc.api.interfaces.front.PresentationType;

public class Screen extends JComponent {
	private static final long 			serialVersionUID = 1659662023574191068L;
	private static final EntityClass[]	DRAWING_ORDER = {
											EntityClass.Land,
											EntityClass.Resources,
											EntityClass.Pipes,
											EntityClass.Tracks,
											EntityClass.Buildings,
											EntityClass.Robots,
											EntityClass.Life,
										};

	private final SwingWorld	world;		
	private final Timer			timer = new Timer(true); 
			
	public Screen() throws MercContentException, MercEnvironmentException {
		world = new SwingWorld();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				world.redraw();
				repaint();
			}
		}, 1000, 100);
		
	}
	
	@Override
	public void paintComponent(final Graphics g) {
		final Graphics2D		g2d = (Graphics2D)g;
		final AffineTransform	oldAt = g2d.getTransform();
		final AffineTransform	at = pickCoordinates(g2d);

		world.setCurrentG2D(g2d);
		g2d.setTransform(at);
		try{world.getPresentation(PresentationType.Swing2D).draw(world,null,null,null);
		
			for (EntityClass type : DRAWING_ORDER) {
				for (Entity item : world.content(type)) {
					item.getClassDescription().getPresentation(PresentationType.Swing2D).draw(world,item,null,item.getClassDescription());
				}
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
