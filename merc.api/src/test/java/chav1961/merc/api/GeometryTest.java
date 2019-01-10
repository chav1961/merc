package chav1961.merc.api;

import org.junit.Assert;
import org.junit.Test;

import chav1961.purelib.basic.exceptions.PrintingException;


public class GeometryTest {
	@Test
	public void pointTest() throws PrintingException, CloneNotSupportedException {
		final Point	point = new Point(2,3);
		
		Assert.assertEquals(2,point.getX());
		Assert.assertEquals(3,point.getY());
		Assert.assertEquals(new Point(2,3),point);
		Assert.assertEquals("point(2,3)",point.print());
		Assert.assertEquals(point,point.clone());
		Assert.assertEquals(point,point.immutable());
		Assert.assertEquals(1,point.distance(new Point(3,3)),0.001f);

		try{new Point(null);
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (NullPointerException exc) {
		}
		
		try{point.distance(null);
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
		Assert.assertEquals("size(2,3)",size.print());
		Assert.assertEquals(size,size.clone());
		Assert.assertEquals(size,size.immutable());
		Assert.assertTrue(new Size(2,2).compareTo(size) < 0);

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
	}
	
	@Test
	public void areaTest() throws PrintingException, CloneNotSupportedException {
		final Area	area = new Area(2,3,4,5);
		
		Assert.assertEquals(2,area.getX());
		Assert.assertEquals(3,area.getY());
		Assert.assertEquals(4,area.getWidth());
		Assert.assertEquals(5,area.getHeight());
		Assert.assertEquals(new Area(2,3,4,5),area);
		Assert.assertEquals("area(point(2,3),size(4,5))",area.print());
		Assert.assertEquals(area,area.clone());
		Assert.assertEquals(area,area.immutable());
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
		try{new Area(new Point(0,0),(BasePoint)null);
			Assert.fail("Mandatory exception was not detected (null 2-nd argument)");
		} catch (NullPointerException exc) {
		}
		try{new Area(null,new Size(1,1));
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (NullPointerException exc) {
		}
		try{new Area(new Point(0,0),(BaseSize)null);
			Assert.fail("Mandatory exception was not detected (null 2-nd argument)");
		} catch (NullPointerException exc) {
		}
		try{new Area(null);
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (NullPointerException exc) {
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
