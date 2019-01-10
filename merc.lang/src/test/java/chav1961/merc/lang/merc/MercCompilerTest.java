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
	@Test
	public void basicTest() throws SyntaxException, IOException {
		final SyntaxTreeInterface<Object>	names = new OrdinalSyntaxTree<>();
		
		Assert.assertEquals(LexemaType.If,callParser("if",names)[0].type);
		Assert.assertEquals(LexemaType.Then,callParser("then",names)[0].type);
		Assert.assertEquals(LexemaType.Else,callParser("else",names)[0].type);
		Assert.assertEquals(LexemaType.For,callParser("for",names)[0].type);
		Assert.assertEquals(LexemaType.In,callParser("in",names)[0].type);
		Assert.assertEquals(LexemaType.Do,callParser("do",names)[0].type);
		Assert.assertEquals(LexemaType.While,callParser("while",names)[0].type);
		Assert.assertEquals(LexemaType.Var,callParser("var",names)[0].type);
		Assert.assertEquals(LexemaType.Func,callParser("func",names)[0].type);
		Assert.assertEquals(LexemaType.Brick,callParser("brick",names)[0].type);
		Assert.assertEquals(LexemaType.Break,callParser("break",names)[0].type);
		Assert.assertEquals(LexemaType.Continue,callParser("continue",names)[0].type);
		Assert.assertEquals(LexemaType.Return,callParser("return",names)[0].type);
		Assert.assertEquals(LexemaType.Print,callParser("print",names)[0].type);
		Assert.assertEquals(LexemaType.TypeDef,callParser("type",names)[0].type);
		Assert.assertEquals(LexemaType.Lock,callParser("lock",names)[0].type);
		Assert.assertEquals(LexemaType.Name,callParser("vassya",names)[0].type);
		Assert.assertEquals(LexemaType.PredefinedName,callParser("market",names)[0].type);
		Assert.assertEquals(LexemaType.IntConst,callParser("100",names)[0].type);
		Assert.assertEquals(LexemaType.RealConst,callParser("1.2",names)[0].type);
		Assert.assertEquals(LexemaType.StrConst,callParser("\"test string\"",names)[0].type);
		Assert.assertEquals(LexemaType.BoolConst,callParser("true",names)[0].type);
		Assert.assertEquals(LexemaType.BoolConst,callParser("false",names)[0].type);
		Assert.assertEquals(LexemaType.NullConst,callParser("null",names)[0].type);
//		Assert.assertEquals(LexemaType.RefConst,callParser("",names)[0].type);
		Assert.assertEquals(LexemaType.Open,callParser("(",names)[0].type);
		Assert.assertEquals(LexemaType.Close,callParser(")",names)[0].type);
		Assert.assertEquals(LexemaType.OpenB,callParser("[",names)[0].type);
		Assert.assertEquals(LexemaType.CloseB,callParser("]",names)[0].type);
		Assert.assertEquals(LexemaType.OpenF,callParser("{",names)[0].type);
		Assert.assertEquals(LexemaType.CloseF,callParser("}",names)[0].type);
		Assert.assertEquals(LexemaType.Dot,callParser(".",names)[0].type);
		Assert.assertEquals(LexemaType.Colon,callParser(":",names)[0].type);
		Assert.assertEquals(LexemaType.Semicolon,callParser(";",names)[0].type);
		Assert.assertEquals(LexemaType.Period,callParser("..",names)[0].type);
		Assert.assertEquals(LexemaType.Div,callParser(",",names)[0].type);
		Assert.assertEquals(LexemaType.Pipe,callParser("->",names)[0].type);
//		Assert.assertEquals(LexemaType.Operator,callParser("+",names)[0].type); 
//		Assert.assertEquals(LexemaType.Unknown,
//		Assert.assertEquals(LexemaType.Comment,		

		Assert.assertEquals(LexemaSubtype.Int,callParser("int",names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Real,callParser("real",names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Str,callParser("str",names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Bool,callParser("bool",names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Point,callParser("point",names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Area,callParser("area",names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Track,callParser("track",names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Size,callParser("size",names)[0].subtype);
	
		Assert.assertEquals(LexemaSubtype.Inc,callParser("++",names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Dec,callParser("--",names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.BitInv,callParser("~e",names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.BitAnd,callParser("&",names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.BitOr,callParser("|",names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.BitXor,callParser("^",names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Shl,callParser("<<",names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Shr,callParser(">>",names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Shra,callParser(">>>",names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Mul,callParser("*",names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Div,callParser("/",names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Rem,callParser("%",names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Add,callParser("+",names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Sub,callParser("-",names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.LT,callParser("<",names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.LE,callParser("<=",names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.GT,callParser(">",names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.GE,callParser(">=",names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.EQ,callParser("==",names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.NE,callParser("!=",names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.IS,callParser("is",names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.LIKE,callParser("like",names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Not,callParser("!",names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.And,callParser("&&",names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Or,callParser("||",names)[0].subtype);
		Assert.assertEquals(LexemaSubtype.Assign,callParser("=",names)[0].subtype);
	}

	private Lexema[] callParser(final String text, final SyntaxTreeInterface<Object> names) throws SyntaxException, IOException {
		final List<Lexema>	lexemas = new ArrayList<>();
		final String		data = text + "\n";
		
		MercCompiler.processLine(0, data.toCharArray(), 0, data.length(), false, names, lexemas);
		return lexemas.toArray(new Lexema[lexemas.size()]);
	}
}
