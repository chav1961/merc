package chav1961.merc.api;

import java.io.IOException;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import chav1961.merc.api.exceptions.MercEnvironmentException;
import chav1961.merc.api.interfaces.front.ControlInterface;
import chav1961.merc.api.interfaces.front.Entity;
import chav1961.merc.api.interfaces.front.EntityClassDescription;
import chav1961.merc.api.interfaces.front.EntityStateDescriptor;
import chav1961.merc.api.interfaces.front.StateChangedListener;
import chav1961.merc.api.interfaces.front.World;
import chav1961.purelib.basic.exceptions.PrintingException;
import chav1961.purelib.basic.exceptions.SyntaxException;
import chav1961.purelib.streams.JsonStaxParser;
import chav1961.purelib.streams.JsonStaxPrinter;


public class GeometryTest {
	public static final Entity<? extends Enum<?>>	DUMMY = new Entity() {
														@Override public long getTimestamp() {return 0;}
														@Override public int getX() {return 1;}
														@Override public int getY() {return 2;}
														@Override public int getWidth() {return 3;}
														@Override public int getHeight() {return 4;}
														@Override public Enum getState() {return null;}
														@Override public void serialize(JsonStaxPrinter printer) throws PrintingException, IOException {}
														@Override public void deserialize(JsonStaxParser parser) throws SyntaxException, IOException {}
														@Override public UUID getId() {return null;}
														@Override public EntityClassDescription getClassDescription() {return null;}
														@Override public Entity addStateChangedListener(StateChangedListener listener) {return null;}
														@Override public Entity removeStateChangedListener(StateChangedListener listener) {return null;}
														@Override public boolean wasModifiedAfter(long timestamp) {return false;}
														@Override public EntityStateDescriptor getPreviousState() {return null;}
														@Override public Entity clearModification(long timestamp) {return null;}
														@Override public Entity setX(int x) throws MercEnvironmentException {return null;}
														@Override public Entity setY(int y) throws MercEnvironmentException {return null;}
														@Override public Entity setWidth(int width) throws MercEnvironmentException {return null;}
														@Override public Entity setHeight(int height) throws MercEnvironmentException {return null;}
														@Override public Entity setState(Enum state) throws MercEnvironmentException {return null;}
														@Override public Entity getOwner() throws MercEnvironmentException {return null;}
														@Override public Iterable getComponents() throws MercEnvironmentException {return null;}
														@Override public ControlInterface getControlInterface() throws MercEnvironmentException {return null;}
														@Override public Entity fireStateChanged(EntityStateDescriptor previousState, int changes) throws MercEnvironmentException {return null;}
														@Override public void redraw() {}
														@Override public long getRedrawCount() {return 0;}
														@Override public World getWorld() throws MercEnvironmentException {return null;}
														@Override public void setWorld(World world) throws MercEnvironmentException {}
													};
	
	@Test
	public void pointTest() throws PrintingException, CloneNotSupportedException {
		final Point	point = new Point(2,3);
		
		Assert.assertEquals(2,point.getX());
		Assert.assertEquals(3,point.getY());
		Assert.assertEquals(new Point(2,3),point);
		Assert.assertEquals(new Point(2,3).toString(),point.toString());
		Assert.assertEquals(new Point(2,3).hashCode(),point.hashCode());
		Assert.assertEquals("point(2,3)",point.print());
		Assert.assertEquals(point,point.clone());
		Assert.assertEquals(point,point.immutable());
		Assert.assertEquals(new Point(1,2),new Point(DUMMY));

		Assert.assertEquals(1,point.distance(new Point(3,3)),0.001f);
		Assert.assertEquals(1,point.squareDistance(new Point(3,3)),0.001f);

		point.setX(3);
		Assert.assertEquals(new Point(3,3),point);
		point.setY(4);
		Assert.assertEquals(new Point(3,4),point);
		
		try{new Point(null);
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (NullPointerException exc) {
		}
		
		try{point.distance(null);
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (NullPointerException exc) {
		}
		try{point.squareDistance(null);
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (NullPointerException exc) {
		}
	}

	@Test
	public void sizeTest() throws PrintingException, CloneNotSupportedException {
		final Size	size = new Size(2,3);
		
		Assert.assertEquals(2,size.getWidth());
		Assert.assertEquals(3,size.getHeight());
		Assert.assertEquals(new Size(2,3),size);
		Assert.assertEquals(new Size(2,3).toString(),size.toString());
		Assert.assertEquals(new Size(2,3).hashCode(),size.hashCode());
		Assert.assertEquals("size(2,3)",size.print());
		Assert.assertEquals(size,size.clone());
		Assert.assertEquals(size,size.immutable());
		Assert.assertEquals(new Size(3,4),new Size(DUMMY));
		Assert.assertTrue(new Size(2,2).compareTo(size) < 0);

		size.setWidth(3);
		Assert.assertEquals(new Size(3,3),size);
		size.setHeight(4);
		Assert.assertEquals(new Size(3,4),size);
		
		try{new Size(null);
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (NullPointerException exc) {
		}
		try{new Size(-1,2);
			Assert.fail("Mandatory exception was not detected (negative 1-st argument)");
		} catch (IllegalArgumentException exc) {
		}
		try{new Size(2,-1);
			Assert.fail("Mandatory exception was not detected (negative 2-nd argument)");
		} catch (IllegalArgumentException exc) {
		}
		
		try{size.setWidth(-1);
			Assert.fail("Mandatory exception was not detected (negative 1-st argument)");
		} catch (IllegalArgumentException exc) {
		}
		try{size.setHeight(-1);
			Assert.fail("Mandatory exception was not detected (negative 1-st argument)");
		} catch (IllegalArgumentException exc) {
		}

		try{size.compareTo(null);
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (NullPointerException exc) {
		}
	}
	
	@Test
	public void areaTest() throws PrintingException, CloneNotSupportedException {
		final Area	area = new Area(2,3,4,5);
		
		Assert.assertEquals(2,area.getX());
		Assert.assertEquals(3,area.getY());
		Assert.assertEquals(4,area.getWidth());
		Assert.assertEquals(5,area.getHeight());
		Assert.assertEquals(new Area(2,3,4,5),area);
		Assert.assertEquals(new Area(2,3,4,5).toString(),area.toString());
		Assert.assertEquals(new Area(2,3,4,5).hashCode(),area.hashCode());
		Assert.assertEquals(new Point(2,3),area.getPoint());
		Assert.assertEquals(new Size(4,5),area.getSize());
		Assert.assertEquals("area(point(2,3),size(4,5))",area.print());
		Assert.assertEquals(area,area.clone());
		Assert.assertEquals(area,area.immutable());
		Assert.assertEquals(new Area(1,2,3,4),new Area(DUMMY));
		Assert.assertTrue(new Area(2,3,2,3).compareTo(area) < 0);
		
		Assert.assertTrue(area.isInside(3,4));
		Assert.assertTrue(area.isInside(new Point(3,4)));
		Assert.assertFalse(area.isInside(1,4));
		
		Assert.assertTrue(area.isInside(new Area(3,4,1,1)));
		Assert.assertFalse(area.isInside(new Area(3,4,10,10)));
		
		Assert.assertTrue(area.isIntersects(new Area(3,4,1,1)));
		Assert.assertTrue(area.isIntersects(new Area(1,2,3,4)));
		Assert.assertFalse(area.isIntersects(new Area(10,10,1,1)));
		
		Assert.assertEquals(new Area(2,3,2,0),area.intersect(new Area(1,1,3,3)));

		
		Assert.assertEquals(new Area(new Point(0,0),new Point(1,1)),new Area(new Point(0,1),new Point(1,0)));
		Assert.assertEquals(new Area(new Point(0,0),new Point(1,1)),new Area(new Point(1,0),new Point(0,1)));
		Assert.assertEquals(new Area(new Point(0,0),new Point(1,1)),new Area(new Point(1,1),new Point(0,0)));
		Assert.assertEquals(new Area(new Point(2,3),new Size(4,5)),area);

		area.setX(3);
		Assert.assertEquals(new Point(3,3),area.getPoint());
		area.setY(4);
		Assert.assertEquals(new Point(3,4),area.getPoint());
		area.setWidth(5);
		Assert.assertEquals(new Size(5,5),area.getSize());
		area.setHeight(6);
		Assert.assertEquals(new Size(5,6),area.getSize());
		
		try{new Area(1,1,-1,0);
			Assert.fail("Mandatory exception was not detected (negative 3-rd argument)");
		} catch (IllegalArgumentException exc) {
		}
		try{new Area(1,1,0,-1);
			Assert.fail("Mandatory exception was not detected (negative 4-th argument)");
		} catch (IllegalArgumentException exc) {
		}
		try{new Area(null,new Point(0,0));
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (NullPointerException exc) {
		}
		try{new Area(new Point(0,0),(Point)null);
			Assert.fail("Mandatory exception was not detected (null 2-nd argument)");
		} catch (NullPointerException exc) {
		}
		try{new Area(null,new Size(1,1));
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (NullPointerException exc) {
		}
		try{new Area(new Point(0,0),(Size)null);
			Assert.fail("Mandatory exception was not detected (null 2-nd argument)");
		} catch (NullPointerException exc) {
		}
		try{new Area(null);
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (NullPointerException exc) {
		}
		
		try{area.setWidth(-1);
			Assert.fail("Mandatory exception was not detected (negative 1-st argument)");
		} catch (IllegalArgumentException exc) {
		}
		try{area.setHeight(-1);
			Assert.fail("Mandatory exception was not detected (negative 1-st argument)");
		} catch (IllegalArgumentException exc) {
		}

		try{area.compareTo(null);
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (NullPointerException exc) {
		}
		
		try{area.isInside((Point)null);
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (NullPointerException exc) {
		}
		try{area.isInside((Area)null);
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (NullPointerException exc) {
		}

		try{area.isIntersects((Area)null);
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (NullPointerException exc) {
		}

		try{area.intersect((Area)null);
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (NullPointerException exc) {
		}
		try{area.intersect(new Area(-1,-1,1,1));
			Assert.fail("Mandatory exception was not detected (1-st argument does not intersect)");
		} catch (IllegalArgumentException exc) {
		}
	}

	@Test
	public void trackTest() throws PrintingException, CloneNotSupportedException {
		trackTest(new Track(2,3));
		trackTest(new Track(2,3,1,1));
		trackTest(new Track(new Point(2,3)));
		trackTest(new Track(new Point(2,3),new Size(1,1)));
		trackTest(new Track(new Area(2,3,1,1)));
		
		final Track	track = new Track(new Area(2,3,3,3));

		Assert.assertEquals(9,track.size());
		Assert.assertEquals(18,BaseTrack.nonClonedPoints(track).length);
		Assert.assertEquals(new Track(new Area(2,3,3,3)),track);
		Assert.assertEquals(new Track(new Area(2,3,3,3)).toString(),track.toString());
		Assert.assertEquals(new Track(new Area(2,3,3,3)).hashCode(),track.hashCode());
		Assert.assertEquals(track,track.clone());
		Assert.assertEquals(track,track.immutable());
		
		Assert.assertTrue(track.isInside(3,3));
		Assert.assertFalse(track.isInside(2,2));
		
		Assert.assertTrue(track.isInside(new Point(3,3)));
		Assert.assertFalse(track.isInside(new Point(2,2)));
		try{track.isInside((Point)null);
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (NullPointerException exc) {
		}

		Assert.assertTrue(track.isInside(new Area(3,3,1,1)));
		Assert.assertFalse(track.isInside(new Area(2,2,1,1)));
		Assert.assertFalse(track.isInside(new Area(0,0,1,1)));
		try{track.isInside((Area)null);
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (NullPointerException exc) {
		}

		Assert.assertTrue(track.isInside(new Track(3,3,1,1)));
		Assert.assertFalse(track.isInside(new Track(2,2,1,1)));
		Assert.assertFalse(track.isInside(new Track(0,0,1,1)));
		try{track.isInside((Track)null);
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (NullPointerException exc) {
		}
		
		Assert.assertTrue(track.isIntersects(new Area(3,3,1,1)));
		Assert.assertTrue(track.isIntersects(new Area(2,2,1,2)));
		Assert.assertFalse(track.isIntersects(new Area(0,0,1,1)));
		try{track.isIntersects((Area)null);
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (NullPointerException exc) {
		}

		Assert.assertTrue(track.isIntersects(new Track(3,3,1,1)));
		Assert.assertTrue(track.isIntersects(new Track(2,2,1,2)));
		Assert.assertFalse(track.isIntersects(new Track(0,0,1,1)));
		try{track.isIntersects((Track)null);
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (NullPointerException exc) {
		}
		
		try{new Track(1,1,-1,0);
			Assert.fail("Mandatory exception was not detected (negative 3-nd argument)");
		} catch (IllegalArgumentException exc) {
		}
		try{new Track(1,1,0,-1);
			Assert.fail("Mandatory exception was not detected (negative 4-th argument)");
		} catch (IllegalArgumentException exc) {
		}
		try{new Track((Point)null);
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (NullPointerException exc) {
		}
		try{new Track(null,new Size(1,1));
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (NullPointerException exc) {
		}
		try{new Track(new Point(0,0),null);
			Assert.fail("Mandatory exception was not detected (null 2-nd argument)");
		} catch (NullPointerException exc) {
		}
		try{new Track((Area)null);
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (NullPointerException exc) {
		}

		try{track.walk(null);
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (NullPointerException exc) {
		}
	}

	@Test
	public void extendedTrackTest() throws PrintingException, CloneNotSupportedException {
		Assert.assertEquals(4,new Track(0,0,2,2).union(new Track(0,0,2,2)).size());
		Assert.assertEquals(7,new Track(0,0,2,2).union(new Track(1,1,2,2)).size());
		Assert.assertEquals(8,new Track(0,0,2,2).union(new Track(2,2,2,2)).size());
		
		try{new Track(0,0,2,2).union((Track)null);
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (NullPointerException exc) {
		}
		try{new Track(0,0,2,2).union((Area)null);
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (NullPointerException exc) {
		}
		try{new Track(0,0,2,2).union((Point)null);
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (NullPointerException exc) {
		}
		
		Assert.assertEquals(4,new Track(0,0,2,2).intersect(new Track(0,0,2,2)).size());
		Assert.assertEquals(1,new Track(0,0,2,2).intersect(new Track(1,1,2,2)).size());
		Assert.assertEquals(0,new Track(0,0,2,2).intersect(new Track(2,2,2,2)).size());
		
		try{new Track(0,0,2,2).intersect((Track)null);
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (NullPointerException exc) {
		}
		try{new Track(0,0,2,2).intersect((Area)null);
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (NullPointerException exc) {
		}
		try{new Track(0,0,2,2).intersect((Point)null);
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (NullPointerException exc) {
		}
		
		Assert.assertEquals(0,new Track(0,0,2,2).minus(new Track(0,0,2,2)).size());
		Assert.assertEquals(3,new Track(0,0,2,2).minus(new Track(1,1,2,2)).size());
		Assert.assertEquals(4,new Track(0,0,2,2).minus(new Track(2,2,2,2)).size());
		
		try{new Track(0,0,2,2).minus((Track)null);
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (NullPointerException exc) {
		}
		try{new Track(0,0,2,2).minus((Area)null);
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (NullPointerException exc) {
		}
		try{new Track(0,0,2,2).minus((Point)null);
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (NullPointerException exc) {
		}
	}

	private void trackTest(final Track track) throws PrintingException {
		Assert.assertEquals(1,track.size());
		track.walk((xP,yP)->{
			Assert.assertEquals(2,xP); 
			Assert.assertEquals(3,yP); 
			return true;
		});
		Assert.assertEquals("track(point(2,3))",track.print());
	}
}
