package chav1961.merc.lang.merc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import chav1961.merc.lang.merc.MercScriptEngine.Lexema;
import chav1961.merc.lang.merc.MercScriptEngine.LexemaSubtype;
import chav1961.purelib.basic.OrdinalSyntaxTree;
import chav1961.purelib.basic.exceptions.SyntaxException;
import chav1961.purelib.basic.interfaces.SyntaxTreeInterface;

public class MercCompilerTest {
	private static final Lexema[]	TEMPLATE = {
			new Lexema(0,0,0,LexemaType.If,LexemaSubtype.Undefined),
			new Lexema(0,0,0,LexemaType.Name,LexemaSubtype.Undefined),
			new Lexema(0,0,0,LexemaType.Operator,LexemaSubtype.GT),
			new Lexema(0,0,0,LexemaType.IntConst,LexemaSubtype.Undefined),
			new Lexema(0,0,0,LexemaType.Then,LexemaSubtype.Undefined),
			new Lexema(0,0,0,LexemaType.Print,LexemaSubtype.Undefined),
			new Lexema(0,0,0,LexemaType.PredefinedName,LexemaSubtype.Robo),
			new Lexema(0,0,0,LexemaType.Pipe,LexemaSubtype.Undefined),
			new Lexema(0,0,0,LexemaType.Name,LexemaSubtype.Undefined),
			new Lexema(0,0,0,LexemaType.Open,LexemaSubtype.Undefined),
			new Lexema(0,0,0,LexemaType.IntConst,LexemaSubtype.Undefined),
			new Lexema(0,0,0,LexemaType.Div,LexemaSubtype.Undefined),
			new Lexema(0,0,0,LexemaType.IntConst,LexemaSubtype.Undefined),
			new Lexema(0,0,0,LexemaType.Close,LexemaSubtype.Undefined),
			new Lexema(0,0,0,LexemaType.Else,LexemaSubtype.Undefined),
			new Lexema(0,0,0,LexemaType.Return,LexemaSubtype.Undefined),
			new Lexema(0,0,0,LexemaType.Semicolon,LexemaSubtype.Undefined),
	};
	
	@Test
	public void parserTest() throws SyntaxException, IOException {
		final SyntaxTreeInterface<Object>	names = new OrdinalSyntaxTree<>();
		
		Assert.assertEquals(LexemaType.If,callParser("if",false,names)[0].type);
		Assert.assertEquals(LexemaType.Then,callParser("then",false,names)[0].type);
		Assert.assertEquals(LexemaType.Else,callParser("else",false,names)[0].type);
		Assert.assertEquals(LexemaType.For,callParser("for",false,names)[0].type);
		Assert.assertEquals(LexemaType.In,callParser("in",false,names)[0].type);
		Assert.assertEquals(LexemaType.Do,callParser("do",false,names)[0].type);
		Assert.assertEquals(LexemaType.While,callParser("while",false,names)[0].type);
		Assert.assertEquals(LexemaType.Var,callParser("var",false,names)[0].type);
		Assert.assertEquals(LexemaType.Func,callParser("func",false,names)[0].type);
		Assert.assertEquals(LexemaType.Brick,callParser("brick",false,names)[0].type);
		Assert.assertEquals(LexemaType.Break,callParser("break",false,names)[0].type);
		Assert.assertEquals(LexemaType.Continue,callParser("continue",false,names)[0].type);
		Assert.assertEquals(LexemaType.Return,callParser("return",false,names)[0].type);
		Assert.assertEquals(LexemaType.Print,callParser("print",false,names)[0].type);
		Assert.assertEquals(LexemaType.TypeDef,callParser("type",false,names)[0].type);
		Assert.assertEquals(LexemaType.Lock,callParser("lock",false,names)[0].type);
		Assert.assertEquals(LexemaType.Name,callParser("vassya",false,names)[0].type);
		Assert.assertEquals(LexemaType.PredefinedName,callParser("market",false,names)[0].type);
		Assert.assertEquals(LexemaType.IntConst,callParser("100",false,names)[0].type);
		Assert.assertEquals(LexemaType.RealConst,callParser("1.2",false,names)[0].type);
		Assert.assertEquals(LexemaType.StrConst,callParser("\"test string\"",false,names)[0].type);
		Assert.assertEquals(LexemaType.BoolConst,callParser("true",false,names)[0].type);
		Assert.assertEquals(LexemaType.BoolConst,callParser("false",false,names)[0].type);
		Assert.assertEquals(LexemaType.NullConst,callParser("null",false,names)[0].type);
		Assert.assertEquals(LexemaType.Open,callParser("(",false,names)[0].type);
		Assert.assertEquals(LexemaType.Close,callParser(")",false,names)[0].type);
		Assert.assertEquals(LexemaType.OpenB,callParser("[",false,names)[0].type);
		Assert.assertEquals(LexemaType.CloseB,callParser("]",false,names)[0].type);
		Assert.assertEquals(LexemaType.OpenF,callParser("{",false,names)[0].type);
		Assert.assertEquals(LexemaType.CloseF,callParser("}",false,names)[0].type);
		Assert.assertEquals(LexemaType.Dot,callParser(".",false,names)[0].type);
		Assert.assertEquals(LexemaType.Colon,callParser(":",false,names)[0].type);
		Assert.assertEquals(LexemaType.Semicolon,callParser(";",false,names)[0].type);
		Assert.assertEquals(LexemaType.Period,callParser("..",false,names)[0].type);
		Assert.assertEquals(LexemaType.Div,callParser(",",false,names)[0].type);
		Assert.assertEquals(LexemaType.Pipe,callParser("->",false,names)[0].type);

		Assert.assertEquals(LexemaSubtype.Int,callParser("int",false,names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Real,callParser("real",false,names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Str,callParser("str",false,names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Bool,callParser("bool",false,names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Point,callParser("point",false,names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Area,callParser("area",false,names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Track,callParser("track",false,names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Size,callParser("size",false,names)[0].subtype);
	
		Assert.assertEquals(LexemaSubtype.Inc,callParser("++",false,names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Dec,callParser("--",false,names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.BitInv,callParser("~",false,names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.BitAnd,callParser("&",false,names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.BitOr,callParser("|",false,names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.BitXor,callParser("^",false,names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Shl,callParser("<<",false,names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Shr,callParser(">>",false,names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Shra,callParser(">>>",false,names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Mul,callParser("*",false,names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Div,callParser("/",false,names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Rem,callParser("%",false,names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Add,callParser("+",false,names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Sub,callParser("-",false,names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.LT,callParser("<",false,names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.LE,callParser("<=",false,names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.GT,callParser(">",false,names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.GE,callParser(">=",false,names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.EQ,callParser("==",false,names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.NE,callParser("!=",false,names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.IS,callParser("is",false,names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.LIKE,callParser("like",false,names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Not,callParser("!",false,names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.And,callParser("&&",false,names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Or,callParser("||",false,names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Assign,callParser("=",false,names)[0].subtype);
		
		Assert.assertEquals(LexemaType.Unknown,callParser("?",true,names)[0].type);
		Assert.assertEquals(LexemaType.Comment,callParser("// comment",true,names)[0].type);

		final Lexema[]	result = callParser("if x > 0 then print robo->test(1,2) else return; // comment",false,names);
		
		Assert.assertEquals(TEMPLATE.length,result.length);
		for (int index = 0; index < result.length; index++) {
			Assert.assertEquals(TEMPLATE[index].type,result[index].type);
			Assert.assertEquals(TEMPLATE[index].subtype,result[index].subtype);
		}

		try{callParser("?",false,names);
			Assert.fail("Mandatory exception was not detected (unknown lexema)"); 
		} catch (SyntaxException exc) {
		}
		try{callParser("12.",false,names);
			Assert.fail("Mandatory exception was not detected (invalid number)");
		} catch (SyntaxException exc) {
		}
		try{callParser("\"test",false,names);
			Assert.fail("Mandatory exception was not detected (unclosed string)");
		} catch (SyntaxException exc) {
		}
	}

	@Test
	public void buildNameSyntaxTreeTest() throws SyntaxException, IOException {
		final SyntaxTreeInterface<Object>	names = new OrdinalSyntaxTree<>();
		
		SyntaxTreeNode	node = new SyntaxTreeNode();
		MercCompiler.buildNameSyntaxTree(callParser("x",false,true,names),0,names,node);
		Assert.assertEquals("x\n",node.toString(names));
		MercCompiler.buildNameSyntaxTree(callParser("x[1]",false,true,names),0,names,node);
		Assert.assertEquals("x[\n"+SyntaxTreeNode.STAIRWAY_STEP+"1\n]\n",node.toString(names));
		MercCompiler.buildNameSyntaxTree(callParser("x[1,2]",false,true,names),0,names,node);
		Assert.assertEquals("x[\n"+SyntaxTreeNode.STAIRWAY_STEP+"1\n"+SyntaxTreeNode.STAIRWAY_STEP+",\n"+SyntaxTreeNode.STAIRWAY_STEP+"2\n]\n",node.toString(names));
		MercCompiler.buildNameSyntaxTree(callParser("x()",false,true,names),0,names,node);
		Assert.assertEquals("Call\n"+SyntaxTreeNode.STAIRWAY_STEP+"x\nend call\n",node.toString(names));
		MercCompiler.buildNameSyntaxTree(callParser("x(1)",false,true,names),0,names,node);
		Assert.assertEquals("Call\n"+SyntaxTreeNode.STAIRWAY_STEP+"x\nwith :\n"+SyntaxTreeNode.STAIRWAY_STEP+"1\nend call\n",node.toString(names));
		MercCompiler.buildNameSyntaxTree(callParser("x.y",false,true,names),0,names,node);
		Assert.assertEquals("field\n"+SyntaxTreeNode.STAIRWAY_STEP+"x\n--->\n"+SyntaxTreeNode.STAIRWAY_STEP+"y\nend field\n",node.toString(names));
		MercCompiler.buildNameSyntaxTree(callParser("x[1,2]().y",false,true,names),0,names,node);
		Assert.assertEquals("field\n"+SyntaxTreeNode.STAIRWAY_STEP+"Call\n"+SyntaxTreeNode.STAIRWAY_STEP+SyntaxTreeNode.STAIRWAY_STEP+"x[\n"+SyntaxTreeNode.STAIRWAY_STEP+SyntaxTreeNode.STAIRWAY_STEP+SyntaxTreeNode.STAIRWAY_STEP+"1\n"+SyntaxTreeNode.STAIRWAY_STEP+SyntaxTreeNode.STAIRWAY_STEP+SyntaxTreeNode.STAIRWAY_STEP+",\n"+SyntaxTreeNode.STAIRWAY_STEP+SyntaxTreeNode.STAIRWAY_STEP+SyntaxTreeNode.STAIRWAY_STEP+"2\n"+SyntaxTreeNode.STAIRWAY_STEP+SyntaxTreeNode.STAIRWAY_STEP+"]\n"+SyntaxTreeNode.STAIRWAY_STEP+"end call\n--->\n"+SyntaxTreeNode.STAIRWAY_STEP+"y\nend field\n",node.toString(names));

		try{MercCompiler.buildNameSyntaxTree(callParser("x[1,2",false,true,names),0,names,node);
			Assert.fail("Mandatory exception was not detected (closed ']' missing)");
		} catch (SyntaxException exc) {
		}
		try{MercCompiler.buildNameSyntaxTree(callParser("x(1,2",false,true,names),0,names,node);
			Assert.fail("Mandatory exception was not detected (closed ')' missing)");
		} catch (SyntaxException exc) {
		}
		try{MercCompiler.buildNameSyntaxTree(callParser("robo[",false,true,names),0,names,node);
			Assert.fail("Mandatory exception was not detected (not supports '[')");
		} catch (SyntaxException exc) {
		}
		try{MercCompiler.buildNameSyntaxTree(callParser("robo(",false,true,names),0,names,node);
			Assert.fail("Mandatory exception was not detected (not supports '(')");
		} catch (SyntaxException exc) {
		}
	}

	@Test
	public void buildTermSyntaxTreeTest() throws SyntaxException, IOException {
		final SyntaxTreeInterface<Object>	names = new OrdinalSyntaxTree<>();
		
		SyntaxTreeNode	node = new SyntaxTreeNode();
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_TERM,callParser("true",false,true,names),0,names,node);
		Assert.assertEquals("true\n",node.toString(names));
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_TERM,callParser("false",false,true,names),0,names,node);
		Assert.assertEquals("false\n",node.toString(names));
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_TERM,callParser("123",false,true,names),0,names,node);
		Assert.assertEquals("123\n",node.toString(names));
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_TERM,callParser("null",false,true,names),0,names,node);
		Assert.assertEquals("null\n",node.toString(names));
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_TERM,callParser("x",false,true,names),0,names,node);
		Assert.assertEquals("x\n",node.toString(names));
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_TERM,callParser("robo",false,true,names),0,names,node);
		Assert.assertEquals("Robo\n",node.toString(names));
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_TERM,callParser("123.456",false,true,names),0,names,node);
		Assert.assertEquals("123.456\n",node.toString(names));
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_TERM,callParser("\"test\"",false,true,names),0,names,node);
		Assert.assertEquals("\"test\"\n",node.toString(names));
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_TERM,callParser("int(10)",false,true,names),0,names,node);
		Assert.assertEquals("convert to Int\n"+SyntaxTreeNode.STAIRWAY_STEP+"10\n)\n",node.toString(names));
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_TERM,callParser("(123)",false,true,names),0,names,node);
		Assert.assertEquals("123\n",node.toString(names));
		
		try{MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_TERM,callParser("+",false,true,names),0,names,node);
			Assert.fail("Mandatory exception was not detected (term missing)");
		} catch (SyntaxException exc) {
		}
		try{MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_TERM,callParser("int",false,true,names),0,names,node);
			Assert.fail("Mandatory exception was not detected (opened '(' missing)");
		} catch (SyntaxException exc) {
		}
		try{MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_TERM,callParser("int(10",false,true,names),0,names,node);
			Assert.fail("Mandatory exception was not detected (closed ')' missing)");
		} catch (SyntaxException exc) {
		}
		try{MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_TERM,callParser("(12",false,true,names),0,names,node);
			Assert.fail("Mandatory exception was not detected (closed ')' missing)");
		} catch (SyntaxException exc) {
		}
	}
	
	@Test
	public void buildListSyntaxTreeTest() throws SyntaxException, IOException {
		final SyntaxTreeInterface<Object>	names = new OrdinalSyntaxTree<>();
		
		SyntaxTreeNode	node = new SyntaxTreeNode();
		MercCompiler.buildListSyntaxTree(callParser("x",false,true,names),0,names,false,node);
		Assert.assertEquals("(\n"+SyntaxTreeNode.STAIRWAY_STEP+"x\n)\n",node.toString(names));
		MercCompiler.buildListSyntaxTree(callParser("x,y",false,true,names),0,names,false,node);
		Assert.assertEquals("(\n"+SyntaxTreeNode.STAIRWAY_STEP+"x\n,\n"+SyntaxTreeNode.STAIRWAY_STEP+"y\n)\n",node.toString(names));
		MercCompiler.buildListSyntaxTree(callParser("x..y",false,true,names),0,names,true,node);
		Assert.assertEquals("(\n"+SyntaxTreeNode.STAIRWAY_STEP+"(\n"+SyntaxTreeNode.STAIRWAY_STEP+SyntaxTreeNode.STAIRWAY_STEP+"x\n"+SyntaxTreeNode.STAIRWAY_STEP+"..\n"+SyntaxTreeNode.STAIRWAY_STEP+SyntaxTreeNode.STAIRWAY_STEP+"y\n"+SyntaxTreeNode.STAIRWAY_STEP+")\n)\n",node.toString(names));

		try{MercCompiler.buildListSyntaxTree(callParser("x..y",false,true,names),0,names,false,node);
			Assert.fail("Mandatory exception was not detected (ranges not allowed)");
		} catch (SyntaxException exc) {
		}
	}	

	@Test
	public void buildUnarySyntaxTreeTest() throws SyntaxException, IOException {
		final SyntaxTreeInterface<Object>	names = new OrdinalSyntaxTree<>();
		
		SyntaxTreeNode	node = new SyntaxTreeNode();
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_NEGATION,callParser("-x",false,true,names),0,names,node);
		Assert.assertEquals("- (\n"+SyntaxTreeNode.STAIRWAY_STEP+"x\n)\n",node.toString(names));
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_INCDEC,callParser("++x",false,true,names),0,names,node);
		Assert.assertEquals("++ (\n"+SyntaxTreeNode.STAIRWAY_STEP+"x\n)\n",node.toString(names));
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_INCDEC,callParser("--x",false,true,names),0,names,node);
		Assert.assertEquals("-- (\n"+SyntaxTreeNode.STAIRWAY_STEP+"x\n)\n",node.toString(names));
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_INCDEC,callParser("x++",false,true,names),0,names,node);
		Assert.assertEquals("(\n"+SyntaxTreeNode.STAIRWAY_STEP+"x\n) ++\n",node.toString(names));
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_INCDEC,callParser("x--",false,true,names),0,names,node);
		Assert.assertEquals("(\n"+SyntaxTreeNode.STAIRWAY_STEP+"x\n) --\n",node.toString(names));
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_BITINV,callParser("~x",false,true,names),0,names,node);
		Assert.assertEquals("~ (\n"+SyntaxTreeNode.STAIRWAY_STEP+"x\n)\n",node.toString(names));
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_NOT,callParser("!x",false,true,names),0,names,node);
		Assert.assertEquals("not (\n"+SyntaxTreeNode.STAIRWAY_STEP+"x\n)\n",node.toString(names));

		try{MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_INCDEC,callParser("++1",false,true,names),0,names,node);
			Assert.fail("Mandatory exception was not detected (not left-part)");
		} catch (SyntaxException exc) {
		}
		try{MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_INCDEC,callParser("1++",false,true,names),0,names,node);
			Assert.fail("Mandatory exception was not detected (not left-part)");
		} catch (SyntaxException exc) {
		}
		try{MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_INCDEC,callParser("--1",false,true,names),0,names,node);
			Assert.fail("Mandatory exception was not detected (not left-part)");
		} catch (SyntaxException exc) {
		}
		try{MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_INCDEC,callParser("1--",false,true,names),0,names,node);
			Assert.fail("Mandatory exception was not detected (not left-part)");
		} catch (SyntaxException exc) {
		}
	} 

	@Test
	public void buildBinarySyntaxTreeTest() throws SyntaxException, IOException {
		final SyntaxTreeInterface<Object>	names = new OrdinalSyntaxTree<>();
		
		SyntaxTreeNode	node = new SyntaxTreeNode();
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_OR,callParser("x || y",false,true,names),0,names,node);
		Assert.assertEquals("binary list (\n"+SyntaxTreeNode.STAIRWAY_STEP+"x\nOr\n"+SyntaxTreeNode.STAIRWAY_STEP+"y\n)\n",node.toString(names));
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_AND,callParser("x && y",false,true,names),0,names,node);
		Assert.assertEquals("binary list (\n"+SyntaxTreeNode.STAIRWAY_STEP+"x\nAnd\n"+SyntaxTreeNode.STAIRWAY_STEP+"y\n)\n",node.toString(names));
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_OR,callParser("x + y",false,true,names),0,names,node);
		Assert.assertEquals("binary list (\n"+SyntaxTreeNode.STAIRWAY_STEP+"x\nAdd\n"+SyntaxTreeNode.STAIRWAY_STEP+"y\n)\n",node.toString(names));
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_OR,callParser("x - y",false,true,names),0,names,node);
		Assert.assertEquals("binary list (\n"+SyntaxTreeNode.STAIRWAY_STEP+"x\nSub\n"+SyntaxTreeNode.STAIRWAY_STEP+"y\n)\n",node.toString(names));
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_OR,callParser("x * y",false,true,names),0,names,node);
		Assert.assertEquals("binary list (\n"+SyntaxTreeNode.STAIRWAY_STEP+"x\nMul\n"+SyntaxTreeNode.STAIRWAY_STEP+"y\n)\n",node.toString(names));
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_OR,callParser("x / y",false,true,names),0,names,node);
		Assert.assertEquals("binary list (\n"+SyntaxTreeNode.STAIRWAY_STEP+"x\nDiv\n"+SyntaxTreeNode.STAIRWAY_STEP+"y\n)\n",node.toString(names));
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_OR,callParser("x % y",false,true,names),0,names,node);
		Assert.assertEquals("binary list (\n"+SyntaxTreeNode.STAIRWAY_STEP+"x\nRem\n"+SyntaxTreeNode.STAIRWAY_STEP+"y\n)\n",node.toString(names));
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_OR,callParser("x << y",false,true,names),0,names,node);
		Assert.assertEquals("binary list (\n"+SyntaxTreeNode.STAIRWAY_STEP+"x\nShl\n"+SyntaxTreeNode.STAIRWAY_STEP+"y\n)\n",node.toString(names));
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_OR,callParser("x >> y",false,true,names),0,names,node);
		Assert.assertEquals("binary list (\n"+SyntaxTreeNode.STAIRWAY_STEP+"x\nShr\n"+SyntaxTreeNode.STAIRWAY_STEP+"y\n)\n",node.toString(names));
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_OR,callParser("x >>> y",false,true,names),0,names,node);
		Assert.assertEquals("binary list (\n"+SyntaxTreeNode.STAIRWAY_STEP+"x\nShra\n"+SyntaxTreeNode.STAIRWAY_STEP+"y\n)\n",node.toString(names));
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_OR,callParser("x | y",false,true,names),0,names,node);
		Assert.assertEquals("binary list (\n"+SyntaxTreeNode.STAIRWAY_STEP+"x\nBitOr\n"+SyntaxTreeNode.STAIRWAY_STEP+"y\n)\n",node.toString(names));
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_OR,callParser("x & y",false,true,names),0,names,node);
		Assert.assertEquals("binary list (\n"+SyntaxTreeNode.STAIRWAY_STEP+"x\nBitAnd\n"+SyntaxTreeNode.STAIRWAY_STEP+"y\n)\n",node.toString(names));
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_OR,callParser("x < y",false,true,names),0,names,node);
		Assert.assertEquals("binary list (\n"+SyntaxTreeNode.STAIRWAY_STEP+"x\nLT\n"+SyntaxTreeNode.STAIRWAY_STEP+"y\n)\n",node.toString(names));
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_OR,callParser("x <= y",false,true,names),0,names,node);
		Assert.assertEquals("binary list (\n"+SyntaxTreeNode.STAIRWAY_STEP+"x\nLE\n"+SyntaxTreeNode.STAIRWAY_STEP+"y\n)\n",node.toString(names));
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_OR,callParser("x > y",false,true,names),0,names,node);
		Assert.assertEquals("binary list (\n"+SyntaxTreeNode.STAIRWAY_STEP+"x\nGT\n"+SyntaxTreeNode.STAIRWAY_STEP+"y\n)\n",node.toString(names));
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_OR,callParser("x >= y",false,true,names),0,names,node);
		Assert.assertEquals("binary list (\n"+SyntaxTreeNode.STAIRWAY_STEP+"x\nGE\n"+SyntaxTreeNode.STAIRWAY_STEP+"y\n)\n",node.toString(names));
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_OR,callParser("x == y",false,true,names),0,names,node);
		Assert.assertEquals("binary list (\n"+SyntaxTreeNode.STAIRWAY_STEP+"x\nEQ\n"+SyntaxTreeNode.STAIRWAY_STEP+"y\n)\n",node.toString(names));
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_OR,callParser("x != y",false,true,names),0,names,node);
		Assert.assertEquals("binary list (\n"+SyntaxTreeNode.STAIRWAY_STEP+"x\nNE\n"+SyntaxTreeNode.STAIRWAY_STEP+"y\n)\n",node.toString(names));
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_OR,callParser("x like y",false,true,names),0,names,node);
		Assert.assertEquals("binary list (\n"+SyntaxTreeNode.STAIRWAY_STEP+"x\nLike\n"+SyntaxTreeNode.STAIRWAY_STEP+"y\n)\n",node.toString(names));
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_OR,callParser("x in y",false,true,names),0,names,node);
		Assert.assertEquals("binary list (\n"+SyntaxTreeNode.STAIRWAY_STEP+"x\nInList\n"+SyntaxTreeNode.STAIRWAY_STEP+"(\n"+SyntaxTreeNode.STAIRWAY_STEP+SyntaxTreeNode.STAIRWAY_STEP+"y\n"+SyntaxTreeNode.STAIRWAY_STEP+")\n)\n",node.toString(names));
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_ASSIGN,callParser("x = y",false,true,names),0,names,node);
		Assert.assertEquals("assign (\n"+SyntaxTreeNode.STAIRWAY_STEP+"x\n<---\n"+SyntaxTreeNode.STAIRWAY_STEP+"y\n)\n",node.toString(names));
		MercCompiler.buildExpressionSyntaxTree(MercCompiler.PRTY_PIPE,callParser("x -> y -> z",false,true,names),0,names,node);
		Assert.assertEquals("pipe:\n"+SyntaxTreeNode.STAIRWAY_STEP+"from\n"+SyntaxTreeNode.STAIRWAY_STEP+SyntaxTreeNode.STAIRWAY_STEP+"x\n"+SyntaxTreeNode.STAIRWAY_STEP+"thru\n"+SyntaxTreeNode.STAIRWAY_STEP+SyntaxTreeNode.STAIRWAY_STEP+"y\n"+SyntaxTreeNode.STAIRWAY_STEP+"to\n"+SyntaxTreeNode.STAIRWAY_STEP+SyntaxTreeNode.STAIRWAY_STEP+"z\nend pipe\n",node.toString(names));
	}

	@Test
	public void buildNameDefinitionTreeTest() throws SyntaxException, IOException {
		final SyntaxTreeInterface<Object>	names = new OrdinalSyntaxTree<>();
		
		SyntaxTreeNode	node = new SyntaxTreeNode();
		MercCompiler.buildNameDef(callParser("x : int",false,true,names),0,names,false,false,node);
		Assert.assertEquals("name x : Int\n",node.toString(names));
		MercCompiler.buildNameDef(callParser("var x : int",false,true,names),0,names,true,false,node);
		Assert.assertEquals("variable name x : Int\n",node.toString(names));
		MercCompiler.buildNameDef(callParser("x : int(10)",false,true,names),0,names,false,true,node);
		Assert.assertEquals("name x : Int(\n"+SyntaxTreeNode.STAIRWAY_STEP+"(\n"+SyntaxTreeNode.STAIRWAY_STEP+SyntaxTreeNode.STAIRWAY_STEP+"10\n"+SyntaxTreeNode.STAIRWAY_STEP+")\n)\n",node.toString(names));
		
		try{MercCompiler.buildNameDef(callParser("x : int(10)",false,true,names),0,names,false,false,node);
			Assert.fail("Mandatory exception was not detected (initials not available)");
		} catch (SyntaxException exc) {
		}
		try{MercCompiler.buildNameDef(callParser("var x : int",false,true,names),0,names,false,false,node);
			Assert.fail("Mandatory exception was not detected (var keyword not available)");
		} catch (SyntaxException exc) {
		}
	}
	
	
	private Lexema[] callParser(final String text, final boolean suppressErrors, final SyntaxTreeInterface<Object> names) throws SyntaxException, IOException {
		return callParser(text,suppressErrors,false,names);
	}
	
	private Lexema[] callParser(final String text, final boolean suppressErrors, final boolean appendEOF, final SyntaxTreeInterface<Object> names) throws SyntaxException, IOException {
		final List<Lexema>	lexemas = new ArrayList<>();
		final String		data = text + "\n";
		
		MercCompiler.processLine(0, data.toCharArray(), 0, data.length(), suppressErrors, names, lexemas);
		if (appendEOF) {
			lexemas.add(new Lexema(0,0,0,LexemaType.EOF));
		}
		return lexemas.toArray(new Lexema[lexemas.size()]);
	}
}
