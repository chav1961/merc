package chav1961.merc.lang.merc;

import org.junit.Assert;
import org.junit.Test;

import chav1961.merc.api.AreaKeeper;
import chav1961.merc.api.BooleanKeeper;
import chav1961.merc.api.DoubleKeeper;
import chav1961.merc.api.LongKeeper;
import chav1961.merc.api.PointKeeper;
import chav1961.merc.api.SizeKeeper;
import chav1961.merc.api.StringKeeper;
import chav1961.merc.api.TrackKeeper;

public class BasicMercProgramTest {
	@Test
	public void staticTest() {
		final char[]	target = new char[10], source = "1234567890".toCharArray();
		
		BasicMercProgram._concat_(source,target,10);
		Assert.assertArrayEquals("1234567890".toCharArray(),target);
		
		Assert.assertEquals(AreaKeeper.class,BasicMercProgram._newAreaKeeper_().getClass());
		Assert.assertEquals(BooleanKeeper.class,BasicMercProgram._newBooleanKeeper_().getClass());
		Assert.assertEquals(DoubleKeeper.class,BasicMercProgram._newDoubleKeeper_().getClass());
		Assert.assertEquals(LongKeeper.class,BasicMercProgram._newLongKeeper_().getClass());
		Assert.assertEquals(PointKeeper.class,BasicMercProgram._newPointKeeper_().getClass());
		Assert.assertEquals(SizeKeeper.class,BasicMercProgram._newSizeKeeper_().getClass());
		Assert.assertEquals(StringKeeper.class,BasicMercProgram._newStringKeeper_().getClass());
		Assert.assertEquals(TrackKeeper.class,BasicMercProgram._newTrackKeeper_().getClass());

		Assert.assertArrayEquals("10".toCharArray(),BasicMercProgram._toStr_(10L));
		Assert.assertArrayEquals("20.5".toCharArray(),BasicMercProgram._toStr_(20.5));
		Assert.assertArrayEquals("true".toCharArray(),BasicMercProgram._toStr_(true));
		Assert.assertArrayEquals("false".toCharArray(),BasicMercProgram._toStr_(false));
	}
}
