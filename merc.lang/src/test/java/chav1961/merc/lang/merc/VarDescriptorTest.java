package chav1961.merc.lang.merc;

import org.junit.Assert;
import org.junit.Test;

import chav1961.merc.lang.merc.interfaces.VarDescriptor;
import chav1961.purelib.basic.AndOrTree;
import chav1961.purelib.basic.interfaces.SyntaxTreeInterface;

public class VarDescriptorTest {
	@Test
	public void basicTest() {
		final SyntaxTreeInterface<?>	names = new AndOrTree<>();
		final long						nameId1 = names.placeName("test1",null);
		final long						nameId2 = names.placeName("test2",null);
		final long						nameId3 = names.placeName("test3",null);
		final VarDescriptor				desc1 = new VarDescriptorImpl(nameId1,String.class,false,false,0);
		
		Assert.assertEquals(nameId1,desc1.getNameId());
		Assert.assertEquals(String.class,desc1.getNameType());
		Assert.assertFalse(desc1.isArray());
		Assert.assertFalse(desc1.isReadOnly());
		Assert.assertFalse(desc1.isVar());
		Assert.assertFalse(desc1.isMethod());
		Assert.assertFalse(desc1.isReference());
		Assert.assertEquals(0,desc1.howManyDimensions());
		Assert.assertEquals(0,desc1.contentFields().length);
		Assert.assertEquals("test1:java.lang.String",desc1.toString(names));
		
		final VarDescriptor				desc2 = new VarDescriptorImpl(nameId2,String.class,false,true,0,desc1);

		Assert.assertEquals(1,desc2.contentFields().length);
		Assert.assertEquals("var test2{test1:java.lang.String}:java.lang.String",desc2.toString(names));

		final VarDescriptor				desc3 = new VarDescriptorImpl(nameId3,String.class,false,true,0,desc1,desc1);

		Assert.assertEquals(2,desc3.contentFields().length);
		Assert.assertEquals("var test3{test1:java.lang.String;test1:java.lang.String}:java.lang.String",desc3.toString(names));

		final VarDescriptor				desc4 = new VarDescriptorImpl(nameId1,String.class,false,true,1,desc1);

		Assert.assertEquals(1,desc4.howManyDimensions());
		Assert.assertEquals(1,desc4.contentFields().length);
		Assert.assertEquals("var test1[]{test1:java.lang.String}:java.lang.String",desc4.toString(names));

		final VarDescriptor				desc5 = new VarDescriptorImpl(nameId1,String.class,false,true,2,desc1);

		Assert.assertEquals(2,desc5.howManyDimensions());
		Assert.assertEquals(1,desc5.contentFields().length);
		Assert.assertEquals("var test1[,]{test1:java.lang.String}:java.lang.String",desc5.toString(names));

		final VarDescriptor				desc6 = new VarDescriptorImpl(nameId1,new VarDescriptor[0],String.class,0);

		Assert.assertFalse(desc6.isArray());
		Assert.assertTrue(desc6.isMethod());
		Assert.assertEquals(0,desc6.getParameters().length);
		Assert.assertEquals("test1():java.lang.String",desc6.toString(names));

		final VarDescriptor				desc7 = new VarDescriptorImpl(nameId1,new VarDescriptor[]{desc1},String.class,0);

		Assert.assertEquals(1,desc7.getParameters().length);
		Assert.assertEquals("test1(test1:java.lang.String):java.lang.String",desc7.toString(names));

		final VarDescriptor				desc8 = new VarDescriptorImpl(nameId1,new VarDescriptor[]{desc1,desc2},String.class,0);

		Assert.assertEquals(2,desc8.getParameters().length);
		Assert.assertEquals("test1(test1:java.lang.String;var test2{test1:java.lang.String}:java.lang.String):java.lang.String",desc8.toString(names));

		final VarDescriptor				desc9 = new VarDescriptorImpl(nameId1);

		Assert.assertTrue(desc9.isReference());
		Assert.assertEquals("test1@",desc9.toString(names));
	}
}
