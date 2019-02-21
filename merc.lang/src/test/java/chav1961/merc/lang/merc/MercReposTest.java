package chav1961.merc.lang.merc;

import org.junit.Assert;
import org.junit.Test;

import chav1961.merc.api.Point;
import chav1961.merc.api.PointKeeper;
import chav1961.merc.lang.merc.interfaces.VarDescriptor;
import chav1961.purelib.basic.OrdinalSyntaxTree;
import chav1961.purelib.basic.interfaces.SyntaxTreeInterface;

public class MercReposTest {

	@Test
	public void classRepoTest() {
		final SyntaxTreeInterface<Object>	names = new OrdinalSyntaxTree<>();
		final MercClassRepo					repo = new MercClassRepo(names,0);
		
		Assert.assertEquals(MercClassRepo.BUILTIN.length+4,repo.size());
		
		Assert.assertNotNull(repo.byName("PointKeeper"));
		Assert.assertNotNull(repo.byClass(PointKeeper.class));
		Assert.assertNull(repo.byName("Unknown"));
		Assert.assertNull(repo.byClass(this.getClass()));
		
		Assert.assertEquals(names.seekName("PointKeeper"), repo.byClass(PointKeeper.class).getNameId());
		Assert.assertEquals(PointKeeper.class, repo.byClass(PointKeeper.class).getNameType());
		Assert.assertEquals(0, repo.byClass(PointKeeper.class).howManyDimensions());
		Assert.assertFalse(repo.byClass(PointKeeper.class).isReadOnly());
		Assert.assertFalse(repo.byClass(PointKeeper.class).isArray());
		Assert.assertTrue(repo.byClass(PointKeeper.class).isVar());
		Assert.assertFalse(repo.byClass(PointKeeper.class).isReference());
		Assert.assertFalse(repo.byClass(PointKeeper.class).isMethod());
		Assert.assertEquals(3,repo.byClass(PointKeeper.class).contentFields().length);
		
		for (VarDescriptor item : repo.byClass(PointKeeper.class).contentFields()) {
			System.err.println(item.toString(names));
		}
	}

	@Test
	public void nameRepoTest() {
		final SyntaxTreeInterface<Object>	names = new OrdinalSyntaxTree<>();
		final MercNameRepo					repo = new MercNameRepo();
		
		Assert.assertEquals(1,repo.currentDepth());
		repo.addParameter(new VarDescriptorImpl(0,1,String.class,false,false,0));
		repo.addLocalVar(new VarDescriptorImpl(0,2,String.class,false,false,0));
		Assert.assertTrue(repo.hasName(1));
		Assert.assertTrue(repo.hasNameAtTop(1));
		Assert.assertTrue(repo.hasName(2));
		Assert.assertTrue(repo.hasNameAtTop(2));
		Assert.assertFalse(repo.hasName(3));
		Assert.assertFalse(repo.hasNameAtTop(3));
		Assert.assertEquals(String.class,repo.getName(1).getNameType());
		
		try{repo.addParameter(null);
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (NullPointerException exc) {
		}
		try{repo.addLocalVar(null);
			Assert.fail("Mandatory exception was not detected (null 1-st argument)");
		} catch (NullPointerException exc) {
		}
		try{repo.addParameter(new VarDescriptorImpl(0,1,String.class,false,false,0));
			Assert.fail("Mandatory exception was not detected (duplicate name)");
		} catch (IllegalArgumentException exc) {
		}
		try{repo.addLocalVar(new VarDescriptorImpl(0,1,String.class,false,false,0));
			Assert.fail("Mandatory exception was not detected (duplicate name)");
		} catch (IllegalArgumentException exc) {
		}
		
		repo.pushLevel();
		Assert.assertEquals(2,repo.currentDepth());
		repo.addParameter(new VarDescriptorImpl(0,1,int.class,false,false,0));
		Assert.assertTrue(repo.hasName(1));
		Assert.assertTrue(repo.hasNameAtTop(1));
		Assert.assertTrue(repo.hasName(2));
		Assert.assertFalse(repo.hasNameAtTop(2));
		Assert.assertFalse(repo.hasName(3));
		Assert.assertFalse(repo.hasNameAtTop(3));
		Assert.assertEquals(int.class,repo.getName(1).getNameType());
		Assert.assertEquals(String.class,repo.getName(2).getNameType());
		
		repo.popLevel();
		Assert.assertEquals(1,repo.currentDepth());
		Assert.assertTrue(repo.hasName(1));
		Assert.assertTrue(repo.hasNameAtTop(1));
		Assert.assertTrue(repo.hasName(2));
		Assert.assertTrue(repo.hasNameAtTop(2));
		Assert.assertFalse(repo.hasName(3));
		Assert.assertFalse(repo.hasNameAtTop(3));
		Assert.assertEquals(String.class,repo.getName(1).getNameType());
		
		try{repo.popLevel();
			Assert.fail("Mandatory exception was not detected (stack exhausted)");
		} catch (IllegalStateException exc) {
		}
	}
}
