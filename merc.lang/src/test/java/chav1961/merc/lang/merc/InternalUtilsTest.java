package chav1961.merc.lang.merc;

import org.junit.Assert;
import org.junit.Test;

import chav1961.merc.api.Area;
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
import chav1961.merc.lang.merc.interfaces.VarDescriptor;
import chav1961.purelib.basic.AndOrTree;
import chav1961.purelib.basic.exceptions.SyntaxException;
import chav1961.purelib.basic.interfaces.SyntaxTreeInterface;

public class InternalUtilsTest {
	@Test
	public void rValueConversionTest() throws SyntaxException {
		final SyntaxTreeInterface<?>	names = new AndOrTree<>();
		final MercClassRepo				repo = new MercClassRepo(names,0);
		MercSyntaxTreeNode	root;

		names.placeName("getValue",null);
		
		Assert.assertEquals(long.class,
				InternalUtils.insertValueGetter(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
				,repo)
		);
		Assert.assertEquals(MercSyntaxTreeNodeType.Call,root.getType());

		Assert.assertEquals(double.class,
				InternalUtils.insertValueGetter(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],DoubleKeeper.class,0))
				,repo)
		);
		Assert.assertEquals(MercSyntaxTreeNodeType.Call,root.getType());

		Assert.assertEquals(char[].class,
				InternalUtils.insertValueGetter(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],StringKeeper.class,0))
				,repo)
		);
		Assert.assertEquals(MercSyntaxTreeNodeType.Call,root.getType());

		Assert.assertEquals(boolean.class,
				InternalUtils.insertValueGetter(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],BooleanKeeper.class,0))
				,repo)
		);
		Assert.assertEquals(MercSyntaxTreeNodeType.Call,root.getType());

		Assert.assertEquals(Area.class,
				InternalUtils.insertValueGetter(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],AreaKeeper.class,0))
				,repo)
		);
		Assert.assertEquals(MercSyntaxTreeNodeType.Call,root.getType());

		Assert.assertEquals(Point.class,
				InternalUtils.insertValueGetter(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],PointKeeper.class,0))
				,repo)
		);
		Assert.assertEquals(MercSyntaxTreeNodeType.Call,root.getType());

		Assert.assertEquals(Size.class,
				InternalUtils.insertValueGetter(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],SizeKeeper.class,0))
				,repo)
		);
		Assert.assertEquals(MercSyntaxTreeNodeType.Call,root.getType());

		Assert.assertEquals(Track.class,
				InternalUtils.insertValueGetter(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],TrackKeeper.class,0))
				,repo)
		);
		Assert.assertEquals(MercSyntaxTreeNodeType.Call,root.getType());
	}
}
