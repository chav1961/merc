package chav1961.merc.lang.merc;

import org.junit.Assert;
import org.junit.Test;

import chav1961.merc.api.AreaKeeper;
import chav1961.merc.api.BooleanKeeper;
import chav1961.merc.api.DoubleKeeper;
import chav1961.merc.api.LongKeeper;
import chav1961.merc.api.Point;
import chav1961.merc.api.PointKeeper;
import chav1961.merc.api.Size;
import chav1961.merc.api.SizeKeeper;
import chav1961.merc.api.StringKeeper;
import chav1961.merc.api.Track;
import chav1961.merc.api.TrackKeeper;
import chav1961.merc.api.exceptions.MercContentException;

public class BasicMercProgramTest {
	@Test
	public void staticTest() throws MercContentException {
		final char[]	target = new char[10], source = "1234567890".toCharArray();
		
		BasicMercProgram._concat_(source,target,10);
		Assert.assertArrayEquals("1234567890".toCharArray(),target);
		
		Assert.assertEquals(0,BasicMercProgram._compare_("".toCharArray(),"".toCharArray()));
		Assert.assertTrue(BasicMercProgram._compare_("a".toCharArray(),"b".toCharArray()) < 0);
		Assert.assertTrue(BasicMercProgram._compare_("b".toCharArray(),"a".toCharArray()) > 0);
		Assert.assertTrue(BasicMercProgram._compare_("".toCharArray(),"a".toCharArray()) < 0);
		Assert.assertTrue(BasicMercProgram._compare_("a".toCharArray(),"".toCharArray()) > 0);
		Assert.assertEquals(0,BasicMercProgram._compare_("t".toCharArray(),"t".toCharArray()));
		Assert.assertTrue(BasicMercProgram._compare_("ta".toCharArray(),"tb".toCharArray()) < 0);
		Assert.assertTrue(BasicMercProgram._compare_("tb".toCharArray(),"ta".toCharArray()) > 0);
		Assert.assertTrue(BasicMercProgram._compare_("t".toCharArray(),"ta".toCharArray()) < 0);
		Assert.assertTrue(BasicMercProgram._compare_("ta".toCharArray(),"t".toCharArray()) > 0);

		Assert.assertTrue(BasicMercProgram._compare_(new Size(10,10),new Size(15,15)) < 0);
		Assert.assertTrue(BasicMercProgram._compare_(new Size(4,5),new Size(5,4)) == 0);
		Assert.assertTrue(BasicMercProgram._compare_(new Size(15,15),new Size(10,10)) > 0);
		
		Assert.assertEquals(0,BasicMercProgram._inList_(10L,new long[]{10L,10L}));
		Assert.assertEquals(0,BasicMercProgram._inList_(10L,new long[]{10L,20L}));
		Assert.assertEquals(-1,BasicMercProgram._inList_(30L,new long[]{10L,20L}));
		
		Assert.assertEquals(0,BasicMercProgram._inList_(10.0,new double[]{10.0,10.0}));
		Assert.assertEquals(0,BasicMercProgram._inList_(10.0,new double[]{10.0,20.0}));
		Assert.assertEquals(-1,BasicMercProgram._inList_(30.0,new double[]{10.0,20.0}));

		Assert.assertEquals(0,BasicMercProgram._inList_("10".toCharArray(),new char[][]{"10".toCharArray(),"10".toCharArray()}));
		Assert.assertEquals(0,BasicMercProgram._inList_("10".toCharArray(),new char[][]{"10".toCharArray(),"20".toCharArray()}));
		Assert.assertEquals(-1,BasicMercProgram._inList_("30".toCharArray(),new char[][]{"10".toCharArray(),"20".toCharArray()}));

		Assert.assertEquals(0,BasicMercProgram._inList_(true,new boolean[]{true}));
		Assert.assertEquals(-1,BasicMercProgram._inList_(true,new boolean[]{false}));

		Assert.assertEquals(0,BasicMercProgram._inList_(new Point(10,10),new Track[]{new Track(10,10)}));
		Assert.assertEquals(-1,BasicMercProgram._inList_(new Point(10,10),new Track[]{new Track(20,20)}));

		Assert.assertEquals(0,BasicMercProgram._inList_("test",new Object[]{"another","test"}));
		Assert.assertEquals(-1,BasicMercProgram._inList_("test",new Object[]{"another","nontest"})); 
		
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

		final LongKeeper	lk = new LongKeeper();
		
		lk.setValue(10);
		Assert.assertEquals(11,BasicMercProgram._incDec_(lk,0).getValue());
		Assert.assertEquals(10,BasicMercProgram._incDec_(lk,2).getValue());
		Assert.assertEquals(10,BasicMercProgram._incDec_(lk,1).getValue());
		Assert.assertEquals(11,BasicMercProgram._incDec_(lk,3).getValue());
		Assert.assertEquals(10,lk.getValue());

		final DoubleKeeper	dk = new DoubleKeeper();
		
		dk.setValue(10.0);
		Assert.assertEquals(11.0,BasicMercProgram._incDec_(dk,0).getValue(),0.001);
		Assert.assertEquals(10.0,BasicMercProgram._incDec_(dk,2).getValue(),0.001);
		Assert.assertEquals(10.0,BasicMercProgram._incDec_(dk,1).getValue(),0.001);
		Assert.assertEquals(11.0,BasicMercProgram._incDec_(dk,3).getValue(),0.001);
		Assert.assertEquals(10.0,lk.getValue(),0.001);
		
		Assert.assertArrayEquals("".toCharArray(),BasicMercProgram._trunc_("".toCharArray()));
		Assert.assertArrayEquals("".toCharArray(),BasicMercProgram._trunc_(" ".toCharArray()));
		Assert.assertArrayEquals("a".toCharArray(),BasicMercProgram._trunc_(" a".toCharArray()));
		Assert.assertArrayEquals("a".toCharArray(),BasicMercProgram._trunc_("a ".toCharArray()));
		Assert.assertArrayEquals("a".toCharArray(),BasicMercProgram._trunc_(" a ".toCharArray()));
	}
}
