package chav1961.merc.lang.merc;

import org.junit.Assert;
import org.junit.Test;

import chav1961.merc.api.AreaKeeper;
import chav1961.merc.api.BooleanKeeper;
import chav1961.merc.api.DoubleKeeper;
import chav1961.merc.api.LongKeeper;
import chav1961.merc.api.Point;
import chav1961.merc.api.PointKeeper;
import chav1961.merc.api.SizeKeeper;
import chav1961.merc.api.StringKeeper;
import chav1961.merc.api.Track;
import chav1961.merc.api.TrackKeeper;
import chav1961.merc.lang.merc.SyntaxTreeNode.SyntaxTreeNodeType;
import chav1961.merc.lang.merc.interfaces.VarDescriptor;
import chav1961.purelib.basic.AndOrTree;
import chav1961.purelib.basic.OrdinalSyntaxTree;
import chav1961.purelib.basic.exceptions.SyntaxException;
import chav1961.purelib.basic.interfaces.SyntaxTreeInterface;

public class MercOptimizerTest {
	@Test
	public void calculateConstantUnaryExprTest() {
		SyntaxTreeNode	root;
		
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.Negation,20,null,
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,20,null))
					);
		Assert.assertEquals(SyntaxTreeNodeType.IntConst,root.type);
		Assert.assertEquals(-20,root.value);
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.Negation,20,null,
						new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(20),null))
					);
		Assert.assertEquals(SyntaxTreeNodeType.RealConst,root.type);
		Assert.assertEquals(-20.0,Double.longBitsToDouble(root.value),0.0001);
		
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.BitInv,0,null,
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,1,null))
					);
		Assert.assertEquals(SyntaxTreeNodeType.IntConst,root.type);
		Assert.assertEquals(-2,root.value);
		
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.Not,0,null,
						new SyntaxTreeNode(SyntaxTreeNodeType.BoolConst,1,null))
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.Not,0,null,
						new SyntaxTreeNode(SyntaxTreeNodeType.BoolConst,0,null))
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);
	}
	
	@Test
	public void calculateConstantChainedBinaryExprTest() {
		SyntaxTreeNode	root;

		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_OR,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Or,LexemaSubtype.Or},
						new SyntaxTreeNode(SyntaxTreeNodeType.BoolConst,0,null),
						new SyntaxTreeNode(SyntaxTreeNodeType.BoolConst,1,null),
						new SyntaxTreeNode(SyntaxTreeNodeType.BoolConst,0,null)
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_OR,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Or,LexemaSubtype.Or},
						new SyntaxTreeNode(SyntaxTreeNodeType.BoolConst,0,null),
						new SyntaxTreeNode(SyntaxTreeNodeType.BoolConst,0,null),
						new SyntaxTreeNode(SyntaxTreeNodeType.BoolConst,0,null)
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);

		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_AND,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.And,LexemaSubtype.And},
						new SyntaxTreeNode(SyntaxTreeNodeType.BoolConst,1,null),
						new SyntaxTreeNode(SyntaxTreeNodeType.BoolConst,0,null),
						new SyntaxTreeNode(SyntaxTreeNodeType.BoolConst,1,null)
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_AND,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.And,LexemaSubtype.And},
						new SyntaxTreeNode(SyntaxTreeNodeType.BoolConst,0,null),
						new SyntaxTreeNode(SyntaxTreeNodeType.BoolConst,0,null),
						new SyntaxTreeNode(SyntaxTreeNodeType.BoolConst,0,null)
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);
		
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
							MercCompiler.PRTY_ADD,
							new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add},
							new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,20,null),
							new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,30,null)
							)
						);
		Assert.assertEquals(SyntaxTreeNodeType.IntConst,root.type);
		Assert.assertEquals(50,root.value);
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
							MercCompiler.PRTY_ADD,
							new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add,LexemaSubtype.Sub},
							new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,20,null),
							new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,30,null),
							new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,40,null)
							)
						);
		Assert.assertEquals(SyntaxTreeNodeType.IntConst,root.type);
		Assert.assertEquals(10,root.value);

		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_ADD,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add},
						new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(20.0),null),
						new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(30.0),null)
						)
					);
		Assert.assertEquals(50.0,Double.longBitsToDouble(root.value),0.0001);
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_ADD,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add,LexemaSubtype.Sub},
						new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(20.0),null),
						new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(30.0),null),
						new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(40.0),null)
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.RealConst,root.type);
		Assert.assertEquals(10.0,Double.longBitsToDouble(root.value),0.0001);

		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_ADD,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add},
						new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0," first ".toCharArray()),
						new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0," second ".toCharArray())
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.StrConst,root.type);
		Assert.assertEquals(" first  second ",new String((char[])root.cargo));
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_ADD,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add,LexemaSubtype.Sub},
						new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0," first ".toCharArray()),
						new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0," second ".toCharArray()),
						new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0," third ".toCharArray())
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.StrConst,root.type);
		Assert.assertEquals(" first  second third",new String((char[])root.cargo));

		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_MUL,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Mul},
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,20,null),
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,30,null)
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.IntConst,root.type);
		Assert.assertEquals(600,root.value);
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_MUL,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Mul,LexemaSubtype.Div,LexemaSubtype.Rem},
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,20,null),
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,30,null),
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,150,null),
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,3,null)
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.IntConst,root.type);
		Assert.assertEquals(1,root.value);
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_MUL,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Mul},
						new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(20.0),null),
						new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(30.0),null)
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.RealConst,root.type);
		Assert.assertEquals(600.0,Double.longBitsToDouble(root.value),0.0001);
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_MUL,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Mul,LexemaSubtype.Div,LexemaSubtype.Rem},
						new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(20.0),null),
						new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(30.0),null),
						new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(150.0),null),
						new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null)
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.RealConst,root.type);
		Assert.assertEquals(1,Double.longBitsToDouble(root.value),0.0001);

		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_SHIFT,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Shr},
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,-2,null),
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,1,null)
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.IntConst,root.type);
		Assert.assertEquals(-1,root.value);
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_SHIFT,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Shr,LexemaSubtype.Shl,LexemaSubtype.Shra},
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,-2,null),
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,1,null),
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,62,null),
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,62,null)
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.IntConst,root.type);
		Assert.assertEquals(3,root.value);

		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_BITORXOR,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.BitOr},
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,1,null),
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,2,null)
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.IntConst,root.type);
		Assert.assertEquals(3,root.value);
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_BITORXOR,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.BitOr,LexemaSubtype.BitXor},
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,1,null),
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,2,null),
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,2,null)
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.IntConst,root.type);
		Assert.assertEquals(1,root.value);
		
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_BITAND,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.BitAnd},
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,3,null),
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,2,null)
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.IntConst,root.type);
		Assert.assertEquals(2,root.value);
	}

	@Test
	public void calculateConstantNonChainedBinaryExprLongTest() {
		SyntaxTreeNode	root;
		
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.EQ},
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,3,null),
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,3,null)
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.EQ},
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,3,null),
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,2,null)
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);

		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.NE},
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,3,null),
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,3,null)
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.NE},
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,3,null),
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,2,null)
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);

		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.LT},
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,3,null),
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,3,null)
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.LT},
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,3,null),
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,4,null)
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);

		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
				MercCompiler.PRTY_COMPARISON,
				new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.LE},
				new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,3,null),
				new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,2,null)
				)
			);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.LE},
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,3,null),
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,3,null)
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);

		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.GT},
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,3,null),
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,3,null)
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.GT},
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,4,null),
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,3,null)
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);

		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
				MercCompiler.PRTY_COMPARISON,
				new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.GE},
				new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,3,null),
				new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,4,null)
				)
			);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.GE},
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,3,null),
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,3,null)
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);

		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
				MercCompiler.PRTY_COMPARISON,
				new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.InList},
				new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,3,null),
				new SyntaxTreeNode(SyntaxTreeNodeType.List,0,null,
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,3,null),
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,4,null)
						)
				)
			);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
				MercCompiler.PRTY_COMPARISON,
				new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.InList},
				new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,2,null),
				new SyntaxTreeNode(SyntaxTreeNodeType.List,0,null,
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,3,null),
						new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,4,null)
						)
				)
			);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);

		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
				MercCompiler.PRTY_COMPARISON,
				new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.InList},
				new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,4,null),
				new SyntaxTreeNode(SyntaxTreeNodeType.List,0,null,
						new SyntaxTreeNode(SyntaxTreeNodeType.Range,0,null,
							new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,3,null),
							new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,5,null)
							)
						)
				)
			);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
				MercCompiler.PRTY_COMPARISON,
				new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.InList},
				new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,2,null),
				new SyntaxTreeNode(SyntaxTreeNodeType.List,0,null,
						new SyntaxTreeNode(SyntaxTreeNodeType.Range,0,null,
							new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,3,null),
							new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,5,null)
							)
						)
				)
			);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);
	}

	@Test
	public void calculateConstantNonChainedBinaryExprDoubleTest() {
		SyntaxTreeNode	root;
		
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.EQ},
						new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null),
						new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null)
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.EQ},
						new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null),
						new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(2.0),null)
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);

		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.NE},
						new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null),
						new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null)
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.NE},
						new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null),
						new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(2.0),null)
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);

		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.LT},
						new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null),
						new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null)
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.LT},
						new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null),
						new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(4.0),null)
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);

		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
				MercCompiler.PRTY_COMPARISON,
				new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.LE},
				new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null),
				new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(2.0),null)
				)
			);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.LE},
						new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null),
						new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null)
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);

		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.GT},
						new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null),
						new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null)
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.GT},
						new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(4.0),null),
						new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null)
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);

		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
				MercCompiler.PRTY_COMPARISON,
				new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.GE},
				new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null),
				new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(4.0),null)
				)
			);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.GE},
						new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null),
						new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null)
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);

		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
				MercCompiler.PRTY_COMPARISON,
				new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.InList},
				new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null),
				new SyntaxTreeNode(SyntaxTreeNodeType.List,0,null,
						new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null),
						new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(4.0),null)
						)
				)
			);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
				MercCompiler.PRTY_COMPARISON,
				new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.InList},
				new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(2.0),null),
				new SyntaxTreeNode(SyntaxTreeNodeType.List,0,null,
						new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null),
						new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(4.0),null)
						)
				)
			);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);

		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
				MercCompiler.PRTY_COMPARISON,
				new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.InList},
				new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(4.0),null),
				new SyntaxTreeNode(SyntaxTreeNodeType.List,0,null,
						new SyntaxTreeNode(SyntaxTreeNodeType.Range,0,null,
							new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null),
							new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(5.0),null)
							)
						)
				)
			);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
				MercCompiler.PRTY_COMPARISON,
				new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.InList},
				new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(2.0),null),
				new SyntaxTreeNode(SyntaxTreeNodeType.List,0,null,
						new SyntaxTreeNode(SyntaxTreeNodeType.Range,0,null,
							new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null),
							new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(5.0),null)
							)
						)
				)
			);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);
	}

	@Test
	public void calculateConstantNonChainedBinaryExprStrTest() {
		SyntaxTreeNode	root;
		
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.EQ},
						new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"3".toCharArray()),
						new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"3".toCharArray())
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.EQ},
						new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"3".toCharArray()),
						new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"4".toCharArray())
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);
		
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.NE},
						new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"3".toCharArray()),
						new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"4".toCharArray())
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.NE},
						new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"3".toCharArray()),
						new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"3".toCharArray())
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);

		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.LT},
						new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"3".toCharArray()),
						new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"4".toCharArray())
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.LT},
						new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"3".toCharArray()),
						new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"3".toCharArray())
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);

		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.LE},
						new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"3".toCharArray()),
						new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"3".toCharArray())
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.LE},
						new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"3".toCharArray()),
						new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"2".toCharArray())
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);

		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.GT},
						new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"3".toCharArray()),
						new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"2".toCharArray())
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.GT},
						new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"3".toCharArray()),
						new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"3".toCharArray())
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);

		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.GE},
						new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"3".toCharArray()),
						new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"3".toCharArray())
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.GE},
						new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"3".toCharArray()),
						new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"4".toCharArray())
						)
					);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);

		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
				MercCompiler.PRTY_COMPARISON,
				new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.InList},
				new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"4".toCharArray()),
				new SyntaxTreeNode(SyntaxTreeNodeType.List,0,null,
						new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"3".toCharArray()),
						new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"4".toCharArray())
						)
				)
			);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
				MercCompiler.PRTY_COMPARISON,
				new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.InList},
				new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"2".toCharArray()),
				new SyntaxTreeNode(SyntaxTreeNodeType.List,0,null,
						new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"3".toCharArray()),
						new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"4".toCharArray())
						)
				)
			);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);
		
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
				MercCompiler.PRTY_COMPARISON,
				new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.InList},
				new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"4".toCharArray()),
				new SyntaxTreeNode(SyntaxTreeNodeType.List,0,null,
						new SyntaxTreeNode(SyntaxTreeNodeType.Range,0,null,
							new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"3".toCharArray()),
							new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"5".toCharArray())
							)
						)
				)
			);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);
		MercOptimizer.processConstantExpressions(root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,
				MercCompiler.PRTY_COMPARISON,
				new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.InList},
				new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"2".toCharArray()),
				new SyntaxTreeNode(SyntaxTreeNodeType.List,0,null,
						new SyntaxTreeNode(SyntaxTreeNodeType.Range,0,null,
							new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"3".toCharArray()),
							new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"5".toCharArray())
							)
						)
				)
			);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);
	}
	
	@Test
	public void constantConversionTest() throws SyntaxException {
		SyntaxTreeNode	root;
		
		MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,10,null)
					,double.class,null,null,null
				);
		Assert.assertEquals(SyntaxTreeNodeType.RealConst,root.type);
		Assert.assertEquals(10.0,Double.longBitsToDouble(root.value),0.001);
		MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,10,null)
					,char[].class,null,null,null
				);
		Assert.assertEquals(SyntaxTreeNodeType.StrConst,root.type);
		Assert.assertEquals("10",new String((char[])root.cargo));

		MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(10.0),null)
					,long.class,null,null,null
				);
		Assert.assertEquals(SyntaxTreeNodeType.IntConst,root.type);
		Assert.assertEquals(10,root.value);
		MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.RealConst,Double.doubleToLongBits(10.0),null)
					,char[].class,null,null,null
				);
		Assert.assertEquals(SyntaxTreeNodeType.StrConst,root.type);
		Assert.assertEquals("10.0",new String((char[])root.cargo));

		MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"10".toCharArray())
					,long.class,null,null,null
				);
		Assert.assertEquals(SyntaxTreeNodeType.IntConst,root.type);
		Assert.assertEquals(10,root.value);
		MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"10".toCharArray())
					,double.class,null,null,null
				);
		Assert.assertEquals(SyntaxTreeNodeType.RealConst,root.type);
		Assert.assertEquals(10.0,Double.longBitsToDouble(root.value),0.0001);
		MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"true".toCharArray())
					,boolean.class,null,null,null
				);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);
		MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.StrConst,0,"false".toCharArray())
					,boolean.class,null,null,null
				);
		Assert.assertEquals(SyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);

		MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.BoolConst,1,null)
					,char[].class,null,null,null
				);
		Assert.assertEquals(SyntaxTreeNodeType.StrConst,root.type);
		Assert.assertEquals("true",new String((char[])root.cargo));
		MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.BoolConst,0,null)
					,char[].class,null,null,null
				);
		Assert.assertEquals(SyntaxTreeNodeType.StrConst,root.type);
		Assert.assertEquals("false",new String((char[])root.cargo));
	}

	@Test
	public void variableConversionTest() throws SyntaxException {
		final SyntaxTreeInterface<?>	names = new AndOrTree<>();
		final MercClassRepo				repo = new MercClassRepo(names,0);
		SyntaxTreeNode	root;
		
		MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
					,long.class,null,repo,null
				);
		Assert.assertEquals(SyntaxTreeNodeType.InstanceField,root.type);
		Assert.assertEquals(long.class,((VarDescriptor)root.children[1].cargo).getNameType());

		MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],DoubleKeeper.class,0))
					,double.class,null,repo,null
				);
		Assert.assertEquals(SyntaxTreeNodeType.InstanceField,root.type);
		Assert.assertEquals(double.class,((VarDescriptor)root.children[1].cargo).getNameType());

		MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],StringKeeper.class,0))
					,char[].class,null,repo,null
				);
		Assert.assertEquals(SyntaxTreeNodeType.InstanceField,root.type);
		Assert.assertEquals(char[].class,((VarDescriptor)root.children[1].cargo).getNameType());

		MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],BooleanKeeper.class,0))
					,boolean.class,null,repo,null
				);
		Assert.assertEquals(SyntaxTreeNodeType.InstanceField,root.type);
		Assert.assertEquals(boolean.class,((VarDescriptor)root.children[1].cargo).getNameType());

		Assert.assertEquals(DoubleKeeper.class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.IndicedName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],DoubleKeeper.class,0)
								, new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,1,null))
					,null,null,repo,null
				)
		);
		Assert.assertEquals(boolean.class,
			MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.IndicedName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],BooleanKeeper.class,0)
								, new SyntaxTreeNode(SyntaxTreeNodeType.IntConst,1,null))
					,boolean.class,null,repo,null
				)
		);
		Assert.assertEquals(SyntaxTreeNodeType.InstanceField,root.type);
		Assert.assertEquals(boolean.class,((VarDescriptor)root.children[1].cargo).getNameType());
	}
 
	@Test
	public void ordinalBinaryAddMulConversionTest() throws SyntaxException {
		final SyntaxTreeInterface<?>	names = new AndOrTree<>();
		final MercClassRepo				repo = new MercClassRepo(names,0);
		SyntaxTreeNode	root;
		
		Assert.assertEquals(long.class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(double.class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
					)
					,double.class,null,repo,null
				)
		);
		Assert.assertEquals(SyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(SyntaxTreeNodeType.OrdinalBinary,root.children[0].type);

		Assert.assertEquals(double.class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],DoubleKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],DoubleKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(long.class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],DoubleKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],DoubleKeeper.class,0))
					)
					,long.class,null,repo,null
				)
		);
		Assert.assertEquals(SyntaxTreeNodeType.OrdinalBinary,root.type);
		Assert.assertEquals(SyntaxTreeNodeType.Conversion,root.children[0].type);
		Assert.assertEquals(SyntaxTreeNodeType.Conversion,root.children[1].type);

		Assert.assertEquals(char[].class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],StringKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],StringKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(char[].class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],StringKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(char[].class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],DoubleKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],StringKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(char[].class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],BooleanKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],StringKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(char[].class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],PointKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],StringKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(char[].class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],SizeKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],StringKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(char[].class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],AreaKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],StringKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(char[].class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],TrackKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],StringKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(double.class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						, new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],StringKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],StringKeeper.class,0))
					)
					,double.class,null,repo,null
				)
		);
		Assert.assertEquals(SyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(SyntaxTreeNodeType.OrdinalBinary,root.children[0].type);

		Assert.assertEquals(Track.class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],PointKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],PointKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(Track.class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],PointKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],PointKeeper.class,0))
					)
					,Track.class,null,repo,null
				)
		);
		Assert.assertEquals(SyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(SyntaxTreeNodeType.OrdinalBinary,root.children[0].type);

		Assert.assertEquals(Track.class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],PointKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],AreaKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(Track.class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],PointKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],AreaKeeper.class,0))
					)
					,Track.class,null,repo,null
				)
		);
		Assert.assertEquals(SyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(SyntaxTreeNodeType.OrdinalBinary,root.children[0].type);

		Assert.assertEquals(Track.class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],AreaKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],AreaKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(Track.class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],AreaKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],AreaKeeper.class,0))
					)
					,Track.class,null,repo,null
				)
		);
		Assert.assertEquals(SyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(SyntaxTreeNodeType.OrdinalBinary,root.children[0].type);

		Assert.assertEquals(Track.class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],PointKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],TrackKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(Track.class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],PointKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],TrackKeeper.class,0))
					)
					,Track.class,null,repo,null
				)
		);
		Assert.assertEquals(SyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(SyntaxTreeNodeType.OrdinalBinary,root.children[0].type);
		
		Assert.assertEquals(Track.class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],AreaKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],TrackKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(Track.class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],AreaKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],TrackKeeper.class,0))
					)
					,Track.class,null,repo,null
				)
		);
		Assert.assertEquals(SyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(SyntaxTreeNodeType.OrdinalBinary,root.children[0].type);

		Assert.assertEquals(Track.class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],TrackKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],TrackKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(Track.class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],TrackKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],TrackKeeper.class,0))
					)
					,Track.class,null,repo,null
				)
		);
		Assert.assertEquals(SyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(SyntaxTreeNodeType.OrdinalBinary,root.children[0].type);
	}

	@Test
	public void ordinalBinaryBitConversionTest() throws SyntaxException {
		final SyntaxTreeInterface<?>	names = new AndOrTree<>();
		final MercClassRepo				repo = new MercClassRepo(names,0);
		SyntaxTreeNode	root;
		
		Assert.assertEquals(long.class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_BITAND
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.BitAnd}
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(double.class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_BITAND,null,
						new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
					)
					,double.class,null,repo,null
				)
		);
		Assert.assertEquals(SyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(SyntaxTreeNodeType.OrdinalBinary,root.children[0].type);
		Assert.assertEquals(SyntaxTreeNodeType.InstanceField,root.children[0].children[0].type);
		Assert.assertEquals(SyntaxTreeNodeType.InstanceField,root.children[0].children[1].type);

		Assert.assertEquals(long.class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_BITAND
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.BitAnd}
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],DoubleKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(double.class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_BITAND,null,
						new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],DoubleKeeper.class,0))
					)
					,double.class,null,repo,null
				)
		);
		Assert.assertEquals(SyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(SyntaxTreeNodeType.OrdinalBinary,root.children[0].type);
		Assert.assertEquals(SyntaxTreeNodeType.InstanceField,root.children[0].children[0].type);
		Assert.assertEquals(SyntaxTreeNodeType.Conversion,root.children[0].children[1].type);

		Assert.assertEquals(long.class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_BITAND
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.BitAnd}
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],DoubleKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(double.class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_BITAND,null,
						new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],DoubleKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
					)
					,double.class,null,repo,null
				)
		);
		Assert.assertEquals(SyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(SyntaxTreeNodeType.OrdinalBinary,root.children[0].type);
		Assert.assertEquals(SyntaxTreeNodeType.Conversion,root.children[0].children[0].type);
		Assert.assertEquals(SyntaxTreeNodeType.InstanceField,root.children[0].children[1].type);
	}

	@Test
	public void ordinalComparisonConversionTest() throws SyntaxException {
		final SyntaxTreeInterface<?>	names = new AndOrTree<>();
		final MercClassRepo				repo = new MercClassRepo(names,0);
		SyntaxTreeNode	root;
		
		Assert.assertEquals(boolean.class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_COMPARISON
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.EQ}
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(char[].class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_BITAND
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.EQ}
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
					)
					,char[].class,null,repo,null
				)
		);
		Assert.assertEquals(SyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(SyntaxTreeNodeType.OrdinalBinary,root.children[0].type);
		Assert.assertEquals(SyntaxTreeNodeType.InstanceField,root.children[0].children[0].type);
		Assert.assertEquals(SyntaxTreeNodeType.InstanceField,root.children[0].children[1].type);

		Assert.assertEquals(boolean.class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_COMPARISON
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.InList}
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],PointKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],TrackKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(char[].class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_COMPARISON
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.InList}
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],PointKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],TrackKeeper.class,0))
					)
					,char[].class,null,repo,null
				)
		);
		Assert.assertEquals(SyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(SyntaxTreeNodeType.OrdinalBinary,root.children[0].type);
		Assert.assertEquals(SyntaxTreeNodeType.InstanceField,root.children[0].children[0].type);
		Assert.assertEquals(SyntaxTreeNodeType.InstanceField,root.children[0].children[1].type);

		Assert.assertEquals(boolean.class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_COMPARISON
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.InList}
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],PointKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],PointKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(char[].class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_COMPARISON
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.InList}
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],PointKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],PointKeeper.class,0))
					)
					,char[].class,null,repo,null
				)
		);
		Assert.assertEquals(SyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(SyntaxTreeNodeType.OrdinalBinary,root.children[0].type);
		Assert.assertEquals(SyntaxTreeNodeType.InstanceField,root.children[0].children[0].type);
		Assert.assertEquals(SyntaxTreeNodeType.Conversion,root.children[0].children[1].type);
	}

	@Test
	public void ordinalAndOrConversionTest() throws SyntaxException {
		final SyntaxTreeInterface<?>	names = new AndOrTree<>();
		final MercClassRepo				repo = new MercClassRepo(names,0);
		SyntaxTreeNode	root;
		
		Assert.assertEquals(boolean.class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_AND
						,null
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],BooleanKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],BooleanKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(char[].class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_AND
						,null
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],BooleanKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],BooleanKeeper.class,0))
					)
					,char[].class,null,repo,null
				)
		);
		Assert.assertEquals(SyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(SyntaxTreeNodeType.OrdinalBinary,root.children[0].type);
		Assert.assertEquals(SyntaxTreeNodeType.InstanceField,root.children[0].children[0].type);
		Assert.assertEquals(SyntaxTreeNodeType.InstanceField,root.children[0].children[1].type);

		Assert.assertEquals(boolean.class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_AND
						,null
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],BooleanKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],StringKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(char[].class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_AND
						,null
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],BooleanKeeper.class,0))
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],StringKeeper.class,0))
					)
					,char[].class,null,repo,null
				)
		);
		Assert.assertEquals(SyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(SyntaxTreeNodeType.OrdinalBinary,root.children[0].type);
		Assert.assertEquals(SyntaxTreeNodeType.InstanceField,root.children[0].children[0].type);
		Assert.assertEquals(SyntaxTreeNodeType.Conversion,root.children[0].children[1].type);
	}


	@Test
	public void unaryConversionTest() throws SyntaxException {
		final SyntaxTreeInterface<?>	names = new AndOrTree<>();
		final MercClassRepo				repo = new MercClassRepo(names,0);
		SyntaxTreeNode	root;
		
		Assert.assertEquals(long.class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.Negation
						, -1
						,null
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(double.class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.Negation
						,-1
						,null
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],DoubleKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(char[].class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.Negation
						, -1
						,null
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
					)
					,char[].class,null,repo,null
				)
		);
		Assert.assertEquals(SyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(SyntaxTreeNodeType.Negation,root.children[0].type);

		Assert.assertEquals(long.class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.BitInv
						, -1
						,null
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(long.class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.BitInv
						, -1
						,null
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],DoubleKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(char[].class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.BitInv
						, -1
						,null
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
					)
					,char[].class,null,repo,null
				)
		);
		Assert.assertEquals(SyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(SyntaxTreeNodeType.BitInv,root.children[0].type);

		Assert.assertEquals(long.class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.PreInc
						, -1
						,null
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(double.class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.PreInc
						, -1
						,null
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],DoubleKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(char[].class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.PreInc
						, -1
						,null
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
	 				)
					,char[].class,null,repo,null
				)
		);
		Assert.assertEquals(SyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(SyntaxTreeNodeType.PreInc,root.children[0].type);
		
		
		Assert.assertEquals(boolean.class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.Not
						, -1
						,null
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],BooleanKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(char[].class,
				MercOptimizer.processTypeConversions(
					root = new SyntaxTreeNode(SyntaxTreeNodeType.Not
						, -1
						,null
						,new SyntaxTreeNode(SyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],BooleanKeeper.class,0))
					)
					,char[].class,null,repo,null
				)
		);
		Assert.assertEquals(SyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(SyntaxTreeNodeType.Not,root.children[0].type);
		Assert.assertEquals(SyntaxTreeNodeType.InstanceField,root.children[0].children[0].type);
	}

}
