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
import chav1961.merc.lang.merc.interfaces.VarDescriptor;
import chav1961.purelib.basic.AndOrTree;
import chav1961.purelib.basic.OrdinalSyntaxTree;
import chav1961.purelib.basic.exceptions.SyntaxException;
import chav1961.purelib.basic.interfaces.SyntaxTreeInterface;

public class MercOptimizerTest {
	@Test
	public void calculateConstantUnaryExprTest() {
		MercSyntaxTreeNode	root;
		
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.Negation,20,null,
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,20,null))
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.IntConst,root.type);
		Assert.assertEquals(-20,root.value);
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.Negation,20,null,
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(20),null))
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.RealConst,root.type);
		Assert.assertEquals(-20.0,Double.longBitsToDouble(root.value),0.0001);
		
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.BitInv,0,null,
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,1,null))
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.IntConst,root.type);
		Assert.assertEquals(-2,root.value);
		
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.Not,0,null,
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.BoolConst,1,null))
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.Not,0,null,
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.BoolConst,0,null))
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);
	}
	
	@Test
	public void calculateConstantChainedBinaryExprTest() {
		MercSyntaxTreeNode	root;

		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_OR,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Or,LexemaSubtype.Or},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.BoolConst,0,null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.BoolConst,1,null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.BoolConst,0,null)
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_OR,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Or,LexemaSubtype.Or},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.BoolConst,0,null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.BoolConst,0,null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.BoolConst,0,null)
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);

		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_AND,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.And,LexemaSubtype.And},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.BoolConst,1,null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.BoolConst,0,null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.BoolConst,1,null)
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_AND,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.And,LexemaSubtype.And},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.BoolConst,0,null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.BoolConst,0,null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.BoolConst,0,null)
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);
		
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
							MercCompiler.PRTY_ADD,
							new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add},
							new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,20,null),
							new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,30,null)
							)
						);
		Assert.assertEquals(MercSyntaxTreeNodeType.IntConst,root.type);
		Assert.assertEquals(50,root.value);
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
							MercCompiler.PRTY_ADD,
							new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add,LexemaSubtype.Sub},
							new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,20,null),
							new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,30,null),
							new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,40,null)
							)
						);
		Assert.assertEquals(MercSyntaxTreeNodeType.IntConst,root.type);
		Assert.assertEquals(10,root.value);

		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_ADD,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(20.0),null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(30.0),null)
						)
					);
		Assert.assertEquals(50.0,Double.longBitsToDouble(root.value),0.0001);
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_ADD,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add,LexemaSubtype.Sub},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(20.0),null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(30.0),null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(40.0),null)
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.RealConst,root.type);
		Assert.assertEquals(10.0,Double.longBitsToDouble(root.value),0.0001);

		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_ADD,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0," first ".toCharArray()),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0," second ".toCharArray())
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.StrConst,root.type);
		Assert.assertEquals(" first  second ",new String((char[])root.cargo));
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_ADD,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add,LexemaSubtype.Sub},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0," first ".toCharArray()),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0," second ".toCharArray()),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0," third ".toCharArray())
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.StrConst,root.type);
		Assert.assertEquals(" first  second third",new String((char[])root.cargo));

		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_MUL,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Mul},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,20,null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,30,null)
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.IntConst,root.type);
		Assert.assertEquals(600,root.value);
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_MUL,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Mul,LexemaSubtype.Div,LexemaSubtype.Rem},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,20,null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,30,null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,150,null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,3,null)
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.IntConst,root.type);
		Assert.assertEquals(1,root.value);
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_MUL,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Mul},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(20.0),null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(30.0),null)
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.RealConst,root.type);
		Assert.assertEquals(600.0,Double.longBitsToDouble(root.value),0.0001);
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_MUL,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Mul,LexemaSubtype.Div,LexemaSubtype.Rem},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(20.0),null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(30.0),null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(150.0),null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null)
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.RealConst,root.type);
		Assert.assertEquals(1,Double.longBitsToDouble(root.value),0.0001);

		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_SHIFT,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Shr},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,-2,null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,1,null)
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.IntConst,root.type);
		Assert.assertEquals(-1,root.value);
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_SHIFT,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Shr,LexemaSubtype.Shl,LexemaSubtype.Shra},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,-2,null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,1,null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,62,null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,62,null)
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.IntConst,root.type);
		Assert.assertEquals(3,root.value);

		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_BITORXOR,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.BitOr},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,1,null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,2,null)
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.IntConst,root.type);
		Assert.assertEquals(3,root.value);
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_BITORXOR,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.BitOr,LexemaSubtype.BitXor},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,1,null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,2,null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,2,null)
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.IntConst,root.type);
		Assert.assertEquals(1,root.value);
		
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_BITAND,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.BitAnd},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,3,null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,2,null)
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.IntConst,root.type);
		Assert.assertEquals(2,root.value);
	}

	@Test
	public void calculateConstantNonChainedBinaryExprLongTest() {
		MercSyntaxTreeNode	root;
		
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.EQ},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,3,null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,3,null)
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.EQ},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,3,null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,2,null)
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);

		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.NE},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,3,null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,3,null)
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.NE},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,3,null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,2,null)
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);

		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.LT},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,3,null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,3,null)
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.LT},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,3,null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,4,null)
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);

		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
				MercCompiler.PRTY_COMPARISON,
				new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.LE},
				new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,3,null),
				new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,2,null)
				)
			);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.LE},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,3,null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,3,null)
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);

		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.GT},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,3,null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,3,null)
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.GT},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,4,null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,3,null)
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);

		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
				MercCompiler.PRTY_COMPARISON,
				new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.GE},
				new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,3,null),
				new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,4,null)
				)
			);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.GE},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,3,null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,3,null)
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);

		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
				MercCompiler.PRTY_COMPARISON,
				new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.InList},
				new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,3,null),
				new MercSyntaxTreeNode(MercSyntaxTreeNodeType.List,0,null,
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,3,null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,4,null)
						)
				)
			);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
				MercCompiler.PRTY_COMPARISON,
				new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.InList},
				new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,2,null),
				new MercSyntaxTreeNode(MercSyntaxTreeNodeType.List,0,null,
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,3,null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,4,null)
						)
				)
			);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);

		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
				MercCompiler.PRTY_COMPARISON,
				new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.InList},
				new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,4,null),
				new MercSyntaxTreeNode(MercSyntaxTreeNodeType.List,0,null,
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.Range,0,null,
							new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,3,null),
							new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,5,null)
							)
						)
				)
			);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
				MercCompiler.PRTY_COMPARISON,
				new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.InList},
				new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,2,null),
				new MercSyntaxTreeNode(MercSyntaxTreeNodeType.List,0,null,
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.Range,0,null,
							new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,3,null),
							new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,5,null)
							)
						)
				)
			);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);
	}

	@Test
	public void calculateConstantNonChainedBinaryExprDoubleTest() {
		MercSyntaxTreeNode	root;
		
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.EQ},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null)
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.EQ},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(2.0),null)
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);

		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.NE},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null)
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.NE},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(2.0),null)
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);

		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.LT},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null)
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.LT},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(4.0),null)
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);

		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
				MercCompiler.PRTY_COMPARISON,
				new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.LE},
				new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null),
				new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(2.0),null)
				)
			);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.LE},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null)
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);

		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.GT},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null)
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.GT},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(4.0),null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null)
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);

		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
				MercCompiler.PRTY_COMPARISON,
				new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.GE},
				new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null),
				new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(4.0),null)
				)
			);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.GE},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null)
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);

		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
				MercCompiler.PRTY_COMPARISON,
				new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.InList},
				new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null),
				new MercSyntaxTreeNode(MercSyntaxTreeNodeType.List,0,null,
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(4.0),null)
						)
				)
			);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
				MercCompiler.PRTY_COMPARISON,
				new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.InList},
				new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(2.0),null),
				new MercSyntaxTreeNode(MercSyntaxTreeNodeType.List,0,null,
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(4.0),null)
						)
				)
			);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);

		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
				MercCompiler.PRTY_COMPARISON,
				new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.InList},
				new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(4.0),null),
				new MercSyntaxTreeNode(MercSyntaxTreeNodeType.List,0,null,
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.Range,0,null,
							new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null),
							new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(5.0),null)
							)
						)
				)
			);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
				MercCompiler.PRTY_COMPARISON,
				new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.InList},
				new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(2.0),null),
				new MercSyntaxTreeNode(MercSyntaxTreeNodeType.List,0,null,
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.Range,0,null,
							new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(3.0),null),
							new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(5.0),null)
							)
						)
				)
			);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);
	}

	@Test
	public void calculateConstantNonChainedBinaryExprStrTest() {
		MercSyntaxTreeNode	root;
		
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.EQ},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"3".toCharArray()),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"3".toCharArray())
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.EQ},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"3".toCharArray()),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"4".toCharArray())
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);
		
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.NE},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"3".toCharArray()),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"4".toCharArray())
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.NE},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"3".toCharArray()),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"3".toCharArray())
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);

		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.LT},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"3".toCharArray()),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"4".toCharArray())
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.LT},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"3".toCharArray()),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"3".toCharArray())
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);

		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.LE},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"3".toCharArray()),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"3".toCharArray())
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.LE},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"3".toCharArray()),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"2".toCharArray())
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);

		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.GT},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"3".toCharArray()),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"2".toCharArray())
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.GT},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"3".toCharArray()),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"3".toCharArray())
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);

		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.GE},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"3".toCharArray()),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"3".toCharArray())
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
						MercCompiler.PRTY_COMPARISON,
						new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.GE},
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"3".toCharArray()),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"4".toCharArray())
						)
					);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);

		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
				MercCompiler.PRTY_COMPARISON,
				new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.InList},
				new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"4".toCharArray()),
				new MercSyntaxTreeNode(MercSyntaxTreeNodeType.List,0,null,
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"3".toCharArray()),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"4".toCharArray())
						)
				)
			);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
				MercCompiler.PRTY_COMPARISON,
				new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.InList},
				new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"2".toCharArray()),
				new MercSyntaxTreeNode(MercSyntaxTreeNodeType.List,0,null,
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"3".toCharArray()),
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"4".toCharArray())
						)
				)
			);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);
		
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
				MercCompiler.PRTY_COMPARISON,
				new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.InList},
				new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"4".toCharArray()),
				new MercSyntaxTreeNode(MercSyntaxTreeNodeType.List,0,null,
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.Range,0,null,
							new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"3".toCharArray()),
							new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"5".toCharArray())
							)
						)
				)
			);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);
		MercOptimizer.processConstantExpressions(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,
				MercCompiler.PRTY_COMPARISON,
				new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.InList},
				new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"2".toCharArray()),
				new MercSyntaxTreeNode(MercSyntaxTreeNodeType.List,0,null,
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.Range,0,null,
							new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"3".toCharArray()),
							new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"5".toCharArray())
							)
						)
				)
			);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);
	}
	
	@Test
	public void constantConversionTest() throws SyntaxException {
		MercSyntaxTreeNode	root;
		
		MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,10,null)
					,double.class,null,null,null
				);
		Assert.assertEquals(MercSyntaxTreeNodeType.RealConst,root.type);
		Assert.assertEquals(10.0,Double.longBitsToDouble(root.value),0.001);
		MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,10,null)
					,char[].class,null,null,null
				);
		Assert.assertEquals(MercSyntaxTreeNodeType.StrConst,root.type);
		Assert.assertEquals("10",new String((char[])root.cargo));

		MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(10.0),null)
					,long.class,null,null,null
				);
		Assert.assertEquals(MercSyntaxTreeNodeType.IntConst,root.type);
		Assert.assertEquals(10,root.value);
		MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.RealConst,Double.doubleToLongBits(10.0),null)
					,char[].class,null,null,null
				);
		Assert.assertEquals(MercSyntaxTreeNodeType.StrConst,root.type);
		Assert.assertEquals("10.0",new String((char[])root.cargo));

		MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"10".toCharArray())
					,long.class,null,null,null
				);
		Assert.assertEquals(MercSyntaxTreeNodeType.IntConst,root.type);
		Assert.assertEquals(10,root.value);
		MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"10".toCharArray())
					,double.class,null,null,null
				);
		Assert.assertEquals(MercSyntaxTreeNodeType.RealConst,root.type);
		Assert.assertEquals(10.0,Double.longBitsToDouble(root.value),0.0001);
		MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"true".toCharArray())
					,boolean.class,null,null,null
				);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(1,root.value);
		MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StrConst,0,"false".toCharArray())
					,boolean.class,null,null,null
				);
		Assert.assertEquals(MercSyntaxTreeNodeType.BoolConst,root.type);
		Assert.assertEquals(0,root.value);

		MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.BoolConst,1,null)
					,char[].class,null,null,null
				);
		Assert.assertEquals(MercSyntaxTreeNodeType.StrConst,root.type);
		Assert.assertEquals("true",new String((char[])root.cargo));
		MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.BoolConst,0,null)
					,char[].class,null,null,null
				);
		Assert.assertEquals(MercSyntaxTreeNodeType.StrConst,root.type);
		Assert.assertEquals("false",new String((char[])root.cargo));
	}

	@Test
	public void variableConversionTest() throws SyntaxException {
		final SyntaxTreeInterface<?>	names = new AndOrTree<>();
		final MercClassRepo				repo = new MercClassRepo(names,0);
		MercSyntaxTreeNode	root;
		
		MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
					,long.class,null,repo,null
				);
		Assert.assertEquals(MercSyntaxTreeNodeType.Call,root.type);
		Assert.assertEquals(long.class,((VarDescriptor)root.cargo).getNameType());

		MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],DoubleKeeper.class,0))
					,double.class,null,repo,null
				);
		Assert.assertEquals(MercSyntaxTreeNodeType.Call,root.type);
		Assert.assertEquals(double.class,((VarDescriptor)root.cargo).getNameType());

		MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],StringKeeper.class,0))
					,char[].class,null,repo,null
				);
		Assert.assertEquals(MercSyntaxTreeNodeType.Call,root.type);
		Assert.assertEquals(char[].class,((VarDescriptor)root.cargo).getNameType());

		MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],BooleanKeeper.class,0))
					,boolean.class,null,repo,null
				);
		Assert.assertEquals(MercSyntaxTreeNodeType.Call,root.type);
		Assert.assertEquals(boolean.class,((VarDescriptor)root.cargo).getNameType());

		Assert.assertEquals(DoubleKeeper.class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IndicedName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],DoubleKeeper.class,0)
								, new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,1,null))
					,null,null,repo,null
				)
		);
		Assert.assertEquals(boolean.class,
			MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IndicedName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],BooleanKeeper.class,0)
								, new MercSyntaxTreeNode(MercSyntaxTreeNodeType.IntConst,1,null))
					,boolean.class,null,repo,null
				)
		);
		Assert.assertEquals(MercSyntaxTreeNodeType.Call,root.type);
		Assert.assertEquals(boolean.class,((VarDescriptor)root.cargo).getNameType());
	}
 
	@Test
	public void ordinalBinaryAddMulConversionTest() throws SyntaxException {
		final SyntaxTreeInterface<?>	names = new AndOrTree<>();
		final MercClassRepo				repo = new MercClassRepo(names,0);
		MercSyntaxTreeNode	root;
		
		Assert.assertEquals(long.class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(double.class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
					)
					,double.class,null,repo,null
				)
		);
		Assert.assertEquals(MercSyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(MercSyntaxTreeNodeType.OrdinalBinary,root.children[0].type);

		Assert.assertEquals(double.class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],DoubleKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],DoubleKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(long.class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],DoubleKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],DoubleKeeper.class,0))
					)
					,long.class,null,repo,null
				)
		);
		Assert.assertEquals(MercSyntaxTreeNodeType.OrdinalBinary,root.type);
		Assert.assertEquals(MercSyntaxTreeNodeType.Conversion,root.children[0].type);
		Assert.assertEquals(MercSyntaxTreeNodeType.Conversion,root.children[1].type);

		Assert.assertEquals(char[].class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],StringKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],StringKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(char[].class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],StringKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(char[].class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],DoubleKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],StringKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(char[].class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],BooleanKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],StringKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(char[].class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],PointKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],StringKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(char[].class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],SizeKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],StringKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(char[].class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],AreaKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],StringKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(char[].class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],TrackKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],StringKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(double.class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						, new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],StringKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],StringKeeper.class,0))
					)
					,double.class,null,repo,null
				)
		);
		Assert.assertEquals(MercSyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(MercSyntaxTreeNodeType.OrdinalBinary,root.children[0].type);

		Assert.assertEquals(Track.class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],PointKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],PointKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(Track.class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],PointKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],PointKeeper.class,0))
					)
					,Track.class,null,repo,null
				)
		);
		Assert.assertEquals(MercSyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(MercSyntaxTreeNodeType.OrdinalBinary,root.children[0].type);

		Assert.assertEquals(Track.class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],PointKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],AreaKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(Track.class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],PointKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],AreaKeeper.class,0))
					)
					,Track.class,null,repo,null
				)
		);
		Assert.assertEquals(MercSyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(MercSyntaxTreeNodeType.OrdinalBinary,root.children[0].type);

		Assert.assertEquals(Track.class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],AreaKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],AreaKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(Track.class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],AreaKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],AreaKeeper.class,0))
					)
					,Track.class,null,repo,null
				)
		);
		Assert.assertEquals(MercSyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(MercSyntaxTreeNodeType.OrdinalBinary,root.children[0].type);

		Assert.assertEquals(Track.class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],PointKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],TrackKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(Track.class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],PointKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],TrackKeeper.class,0))
					)
					,Track.class,null,repo,null
				)
		);
		Assert.assertEquals(MercSyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(MercSyntaxTreeNodeType.OrdinalBinary,root.children[0].type);
		
		Assert.assertEquals(Track.class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],AreaKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],TrackKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(Track.class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],AreaKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],TrackKeeper.class,0))
					)
					,Track.class,null,repo,null
				)
		);
		Assert.assertEquals(MercSyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(MercSyntaxTreeNodeType.OrdinalBinary,root.children[0].type);

		Assert.assertEquals(Track.class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],TrackKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],TrackKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(Track.class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_ADD
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.Add}
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],TrackKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],TrackKeeper.class,0))
					)
					,Track.class,null,repo,null
				)
		);
		Assert.assertEquals(MercSyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(MercSyntaxTreeNodeType.OrdinalBinary,root.children[0].type);
	}

	@Test
	public void ordinalBinaryBitConversionTest() throws SyntaxException {
		final SyntaxTreeInterface<?>	names = new AndOrTree<>();
		final MercClassRepo				repo = new MercClassRepo(names,0);
		MercSyntaxTreeNode	root;
		
		Assert.assertEquals(long.class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_BITAND
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.BitAnd}
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(double.class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_BITAND,null,
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
					)
					,double.class,null,repo,null
				)
		);
		Assert.assertEquals(MercSyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(MercSyntaxTreeNodeType.OrdinalBinary,root.children[0].type);
		Assert.assertEquals(MercSyntaxTreeNodeType.Call,root.children[0].children[0].type);
		Assert.assertEquals(MercSyntaxTreeNodeType.Call,root.children[0].children[1].type);

		Assert.assertEquals(long.class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_BITAND
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.BitAnd}
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],DoubleKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(double.class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_BITAND,null,
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],DoubleKeeper.class,0))
					)
					,double.class,null,repo,null
				)
		);
		Assert.assertEquals(MercSyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(MercSyntaxTreeNodeType.OrdinalBinary,root.children[0].type);
		Assert.assertEquals(MercSyntaxTreeNodeType.Call,root.children[0].children[0].type);
		Assert.assertEquals(MercSyntaxTreeNodeType.Conversion,root.children[0].children[1].type);

		Assert.assertEquals(long.class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_BITAND
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.BitAnd}
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],DoubleKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(double.class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_BITAND,null,
						new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],DoubleKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
					)
					,double.class,null,repo,null
				)
		);
		Assert.assertEquals(MercSyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(MercSyntaxTreeNodeType.OrdinalBinary,root.children[0].type);
		Assert.assertEquals(MercSyntaxTreeNodeType.Conversion,root.children[0].children[0].type);
		Assert.assertEquals(MercSyntaxTreeNodeType.Call,root.children[0].children[1].type);
	}

	@Test
	public void ordinalComparisonConversionTest() throws SyntaxException {
		final SyntaxTreeInterface<?>	names = new AndOrTree<>();
		final MercClassRepo				repo = new MercClassRepo(names,0);
		MercSyntaxTreeNode	root;
		
		Assert.assertEquals(boolean.class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_COMPARISON
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.EQ}
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(char[].class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_BITAND
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.EQ}
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
					)
					,char[].class,null,repo,null
				)
		);
		Assert.assertEquals(MercSyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(MercSyntaxTreeNodeType.OrdinalBinary,root.children[0].type);
		Assert.assertEquals(MercSyntaxTreeNodeType.Call,root.children[0].children[0].type);
		Assert.assertEquals(MercSyntaxTreeNodeType.Call,root.children[0].children[1].type);

		Assert.assertEquals(boolean.class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_COMPARISON
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.InList}
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],PointKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],TrackKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(char[].class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_COMPARISON
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.InList}
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],PointKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],TrackKeeper.class,0))
					)
					,char[].class,null,repo,null
				)
		);
		Assert.assertEquals(MercSyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(MercSyntaxTreeNodeType.OrdinalBinary,root.children[0].type);
		Assert.assertEquals(MercSyntaxTreeNodeType.Call,root.children[0].children[0].type);
		Assert.assertEquals(MercSyntaxTreeNodeType.Call,root.children[0].children[1].type);

		Assert.assertEquals(boolean.class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_COMPARISON
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.InList}
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],PointKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],PointKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(char[].class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_COMPARISON
						,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.InList}
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],PointKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],PointKeeper.class,0))
					)
					,char[].class,null,repo,null
				)
		);
		Assert.assertEquals(MercSyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(MercSyntaxTreeNodeType.OrdinalBinary,root.children[0].type);
		Assert.assertEquals(MercSyntaxTreeNodeType.Call,root.children[0].children[0].type);
		Assert.assertEquals(MercSyntaxTreeNodeType.Conversion,root.children[0].children[1].type);
	}

	@Test
	public void ordinalAndOrConversionTest() throws SyntaxException {
		final SyntaxTreeInterface<?>	names = new AndOrTree<>();
		final MercClassRepo				repo = new MercClassRepo(names,0);
		MercSyntaxTreeNode	root;
		
		Assert.assertEquals(boolean.class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_AND
						,null
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],BooleanKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],BooleanKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(char[].class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_AND
						,null
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],BooleanKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],BooleanKeeper.class,0))
					)
					,char[].class,null,repo,null
				)
		);
		Assert.assertEquals(MercSyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(MercSyntaxTreeNodeType.OrdinalBinary,root.children[0].type);
		Assert.assertEquals(MercSyntaxTreeNodeType.Call,root.children[0].children[0].type);
		Assert.assertEquals(MercSyntaxTreeNodeType.Call,root.children[0].children[1].type);

		Assert.assertEquals(boolean.class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_AND
						,null
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],BooleanKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],StringKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(char[].class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.OrdinalBinary,MercCompiler.PRTY_AND
						,null
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],BooleanKeeper.class,0))
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],StringKeeper.class,0))
					)
					,char[].class,null,repo,null
				)
		);
		Assert.assertEquals(MercSyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(MercSyntaxTreeNodeType.OrdinalBinary,root.children[0].type);
		Assert.assertEquals(MercSyntaxTreeNodeType.Call,root.children[0].children[0].type);
		Assert.assertEquals(MercSyntaxTreeNodeType.Conversion,root.children[0].children[1].type);
	}


	@Test
	public void unaryConversionTest() throws SyntaxException {
		final SyntaxTreeInterface<?>	names = new AndOrTree<>();
		final MercClassRepo				repo = new MercClassRepo(names,0);
		MercSyntaxTreeNode	root;
		
		Assert.assertEquals(long.class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.Negation
						, -1
						,null
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(double.class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.Negation
						,-1
						,null
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],DoubleKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(char[].class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.Negation
						, -1
						,null
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
					)
					,char[].class,null,repo,null
				)
		);
		Assert.assertEquals(MercSyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(MercSyntaxTreeNodeType.Negation,root.children[0].type);

		Assert.assertEquals(long.class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.BitInv
						, -1
						,null
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(long.class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.BitInv
						, -1
						,null
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],DoubleKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(char[].class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.BitInv
						, -1
						,null
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
					)
					,char[].class,null,repo,null
				)
		);
		Assert.assertEquals(MercSyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(MercSyntaxTreeNodeType.BitInv,root.children[0].type);

		Assert.assertEquals(long.class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.PreInc
						, -1
						,null
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(double.class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.PreInc
						, -1
						,null
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],DoubleKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(char[].class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.PreInc
						, -1
						,null
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
	 				)
					,char[].class,null,repo,null
				)
		);
		Assert.assertEquals(MercSyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(MercSyntaxTreeNodeType.PreInc,root.children[0].type);
		
		
		Assert.assertEquals(boolean.class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.Not
						, -1
						,null
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],BooleanKeeper.class,0))
					)
					,null,null,repo,null
				)
		);
		Assert.assertEquals(char[].class,
				MercOptimizer.processTypeConversions(
					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.Not
						, -1
						,null
						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],BooleanKeeper.class,0))
					)
					,char[].class,null,repo,null
				)
		);
		Assert.assertEquals(MercSyntaxTreeNodeType.Conversion,root.type);
		Assert.assertEquals(MercSyntaxTreeNodeType.Not,root.children[0].type);
		Assert.assertEquals(MercSyntaxTreeNodeType.Call,root.children[0].children[0].type);
	}

	@Test
	public void rValueConversionTest() throws SyntaxException {
		final SyntaxTreeInterface<?>	names = new AndOrTree<>();
		final MercClassRepo				repo = new MercClassRepo(names,0);
		MercSyntaxTreeNode	root;

		names.placeName("getValue",null);
		Assert.assertEquals(long.class,
				MercOptimizer.insertValueGetter(root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
				,long.class,repo)
		);

		Assert.assertEquals(MercSyntaxTreeNodeType.Call,root.getType());
		
//		Assert.assertEquals(long.class,
//				MercOptimizer.processTypeConversions(
//					root = new MercSyntaxTreeNode(MercSyntaxTreeNodeType.Negation
//						, -1
//						,null
//						,new MercSyntaxTreeNode(MercSyntaxTreeNodeType.StandaloneName,-1,new VarDescriptorImpl(0,-1,new VarDescriptor[0],LongKeeper.class,0))
//					)
//					,null,null,repo,null
//				)
//		);
	}
}
