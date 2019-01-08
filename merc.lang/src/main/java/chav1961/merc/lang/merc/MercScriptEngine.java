package chav1961.merc.lang.merc;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.script.ScriptEngineFactory;

import chav1961.purelib.basic.AbstractScriptEngine;
import chav1961.purelib.basic.AndOrTree;
import chav1961.purelib.basic.CharUtils;
import chav1961.purelib.basic.exceptions.SyntaxException;
import chav1961.purelib.basic.interfaces.SyntaxTreeInterface;

class MercScriptEngine extends AbstractScriptEngine {
	private static final SyntaxTreeInterface<Object>	KEYWORDS = new AndOrTree<>();
	
	enum LexemaType {
		If, Then, Else, For, In, Do, While, Var, Func, Brick, Break, Continue, Return, Print, Type, TypeDef, Lock,
		Name, PredefinedName, IntConst, RealConst, StrConst, BoolConst, NullConst, RefConst, 
		Open, Close, OpenB, CloseB, OpenF, CloseF, Dot, Colon, Semicolon, Period, Div, Pipe, 
		Operator, 
		EOF
	}
	
	enum LexemaSubtype {
		Int, Real, Str, Bool, Point, Area, Track,
		// prty=1 - negation
		Inc, Dec,	// prty=2
		BitInv,		// prty=3
		BitAnd, 	// prty=4
		BitOr, BitXor,	// prty=5
		Shl, Shr, Shra,	// prty=6
		Mul, Div, Rem,	// prty=7
		Add, Sub,	// prty=8
		LT, LE, GT, GE, EQ, NE, IS, LIKE, // prty=9
		Not,   	// prty=10
		And, 	// prty=11
		Or,		// prty=12
		Assign,	// prty=13
		Undefined
	}

	static final int	PRTY_NEGATION = 1;
	static final int	PRTY_INCDEC = 2;
	static final int	PRTY_BITINV = 3;
	static final int	PRTY_BITAND = 4;
	static final int	PRTY_BITORXOR = 5;
	static final int	PRTY_SHIFT = 6;
	static final int	PRTY_MUL = 7;
	static final int	PRTY_ADD = 8;
	static final int	PRTY_COMPARISON = 9;
	static final int	PRTY_NOT = 10;
	static final int	PRTY_AND = 11;
	static final int	PRTY_OR = 12;
	static final int	PRTY_ASSIGN = 13;

	static {
		KEYWORDS.placeName("if",new Lexema(0,0,LexemaType.If));
		KEYWORDS.placeName("then",new Lexema(0,0,LexemaType.Then));
		KEYWORDS.placeName("else",new Lexema(0,0,LexemaType.Else));
		KEYWORDS.placeName("for",new Lexema(0,0,LexemaType.For));
		KEYWORDS.placeName("in",new Lexema(0,0,LexemaType.In));
		KEYWORDS.placeName("do",new Lexema(0,0,LexemaType.Do));
		KEYWORDS.placeName("while",new Lexema(0,0,LexemaType.While));
		KEYWORDS.placeName("var",new Lexema(0,0,LexemaType.Var));
		KEYWORDS.placeName("type",new Lexema(0,0,LexemaType.TypeDef));
		KEYWORDS.placeName("func",new Lexema(0,0,LexemaType.Func));
		KEYWORDS.placeName("brick",new Lexema(0,0,LexemaType.Brick));
		KEYWORDS.placeName("break",new Lexema(0,0,LexemaType.Break));
		KEYWORDS.placeName("continue",new Lexema(0,0,LexemaType.Continue));
		KEYWORDS.placeName("return",new Lexema(0,0,LexemaType.Return));
		KEYWORDS.placeName("print",new Lexema(0,0,LexemaType.Print));
		KEYWORDS.placeName("lock",new Lexema(0,0,LexemaType.Lock));
		
		KEYWORDS.placeName("is",new Lexema(0,0,LexemaType.Operator,LexemaSubtype.IS,PRTY_COMPARISON));
		KEYWORDS.placeName("like",new Lexema(0,0,LexemaType.Operator,LexemaSubtype.LIKE,PRTY_COMPARISON));
		
		KEYWORDS.placeName("int",new Lexema(0,0,LexemaType.Type,LexemaSubtype.Int));
		KEYWORDS.placeName("real",new Lexema(0,0,LexemaType.Type,LexemaSubtype.Real));
		KEYWORDS.placeName("str",new Lexema(0,0,LexemaType.Type,LexemaSubtype.Str));
		KEYWORDS.placeName("bool",new Lexema(0,0,LexemaType.Type,LexemaSubtype.Bool));
		KEYWORDS.placeName("point",new Lexema(0,0,LexemaType.Type,LexemaSubtype.Point));
		KEYWORDS.placeName("area",new Lexema(0,0,LexemaType.Type,LexemaSubtype.Area));
		KEYWORDS.placeName("track",new Lexema(0,0,LexemaType.Type,LexemaSubtype.Track));

		KEYWORDS.placeName("true",new Lexema(0,0,true));
		KEYWORDS.placeName("false",new Lexema(0,0,false));
		KEYWORDS.placeName("null",new Lexema(0,0,LexemaType.NullConst));

		KEYWORDS.placeName("robo",new Lexema(0,0,LexemaType.PredefinedName));
		KEYWORDS.placeName("world",new Lexema(0,0,LexemaType.PredefinedName));
		KEYWORDS.placeName("rt",new Lexema(0,0,LexemaType.PredefinedName));
		KEYWORDS.placeName("market",new Lexema(0,0,LexemaType.PredefinedName));
		KEYWORDS.placeName("teleport",new Lexema(0,0,LexemaType.PredefinedName));
	}
	
	private final List<Lexema>		lexemas = new ArrayList<>();
	private final long[]			tempCell = new long[2];
	private SyntaxTreeInterface<?>	names;
	
	MercScriptEngine(final ScriptEngineFactory factory) {
		super(factory);
	}

	@Override
	protected Reader beforeCompile(final Reader reader, final OutputStream os) throws IOException {
		lexemas.clear();
		names = new AndOrTree<>();
		return super.beforeCompile(reader, os); 
	}

	@Override
	protected void processLineInternal(final int lineNo, final char[] data, final int from, final int length) throws IOException, SyntaxException {
		final int 	to = from + length;
		long		keywordId;
		int			pos = from, end;
		
		try{while (pos < to) {
				while (data[pos] <= ' ' && data[pos] != '\r'  && data[pos] != '\n' ) {
					pos++;
				}
				switch (data[pos]) {
					case '\n' : case '\r' :
						return;
					case ','	:
						lexemas.add(new Lexema(lineNo,pos-from,LexemaType.Div)); 
						pos++;
						break;
					case ':'	: 
						lexemas.add(new Lexema(lineNo,pos-from,LexemaType.Colon)); 
						pos++;
						break;
					case ';'	: 
						lexemas.add(new Lexema(lineNo,pos-from,LexemaType.Semicolon)); 
						pos++;
						break;
					case '('	: 
						lexemas.add(new Lexema(lineNo,pos-from,LexemaType.Open)); 
						pos++;
						break;
					case '['	: 
						lexemas.add(new Lexema(lineNo,pos-from,LexemaType.OpenB)); 
						pos++;
						break;
					case '{'	: 
						lexemas.add(new Lexema(lineNo,pos-from,LexemaType.OpenF)); 
						pos++;
						break;
					case ')'	: 
						lexemas.add(new Lexema(lineNo,pos-from,LexemaType.Close)); 
						pos++;
						break;
					case ']'	: 
						lexemas.add(new Lexema(lineNo,pos-from,LexemaType.CloseB)); 
						pos++;
						break;
					case '}'	: 
						lexemas.add(new Lexema(lineNo,pos-from,LexemaType.CloseF)); 
						pos++;
						break;
					case '~'	: 
						lexemas.add(new Lexema(lineNo,pos-from,LexemaType.Operator,LexemaSubtype.BitInv,PRTY_MUL));
						pos++;
						break;
					case '.'	:
						if (data[pos+1] == '.') {
							lexemas.add(new Lexema(lineNo,pos-from,LexemaType.Period));
							pos+=2;
						}
						else {
							lexemas.add(new Lexema(lineNo,pos-from,LexemaType.Dot)); 
							pos++;
						}
						break;
					case '+'	: 
						if (data[pos+1] == '+') {
							lexemas.add(new Lexema(lineNo,pos-from,LexemaType.Operator,LexemaSubtype.Inc,PRTY_INCDEC));
							pos+=2;
						}
						else {
							lexemas.add(new Lexema(lineNo,pos-from,LexemaType.Operator,LexemaSubtype.Add,PRTY_ADD)); 
							pos++;
						}
						break;
					case '-'	: 
						if (data[pos+1] == '-') {
							lexemas.add(new Lexema(lineNo,pos-from,LexemaType.Operator,LexemaSubtype.Dec,PRTY_INCDEC)); 
							pos+=2;
						}
						else if (data[pos+1] == '>') {
							lexemas.add(new Lexema(lineNo,pos-from,LexemaType.Pipe)); 
							pos+=2;
						}
						else {
							lexemas.add(new Lexema(lineNo,pos-from,LexemaType.Operator,LexemaSubtype.Sub,PRTY_ADD)); 
							pos++;
						}
						break;
					case '*'	: 
						lexemas.add(new Lexema(lineNo,pos-from,LexemaType.Operator,LexemaSubtype.Mul,PRTY_MUL));
						pos++;
						break;
					case '/'	:
						if (data[pos+1] == '/') {	// Comment
							return;
						}
						else {
							lexemas.add(new Lexema(lineNo,pos-from,LexemaType.Operator,LexemaSubtype.Div,PRTY_MUL));
							pos++;
						}
						break;
					case '%'	: 
						lexemas.add(new Lexema(lineNo,pos-from,LexemaType.Operator,LexemaSubtype.Rem,PRTY_MUL)); 
						pos++; 
						break;
					case '>'	: 
						if (data[pos+1] == '>') {
							if (data[pos+2] == '>') {
								lexemas.add(new Lexema(lineNo,pos-from,LexemaType.Operator,LexemaSubtype.Shra,PRTY_SHIFT)); 
								pos+=3;
							}
							else {
								lexemas.add(new Lexema(lineNo,pos-from,LexemaType.Operator,LexemaSubtype.Shr,PRTY_SHIFT)); 
								pos+=2;
							}
						}
						else if (data[pos+1] == '=') {
							lexemas.add(new Lexema(lineNo,pos-from,LexemaType.Operator,LexemaSubtype.GE,PRTY_COMPARISON)); 
							pos+=2;
						}
						else {
							lexemas.add(new Lexema(lineNo,pos-from,LexemaType.Operator,LexemaSubtype.GT,PRTY_COMPARISON));
							pos++;
						}
						break;
					case '<'	: 
						if (data[pos+1] == '<') {
							lexemas.add(new Lexema(lineNo,pos-from,LexemaType.Operator,LexemaSubtype.Shl,PRTY_SHIFT));
							pos+=2;
						}
						else if (data[pos+1] == '=') {
							lexemas.add(new Lexema(lineNo,pos-from,LexemaType.Operator,LexemaSubtype.LE,PRTY_COMPARISON));
							pos+=2;
						}
						else {
							lexemas.add(new Lexema(lineNo,pos-from,LexemaType.Operator,LexemaSubtype.LT,PRTY_COMPARISON)); 
							pos++;
						}
						break;
					case '='	: 
						if (data[pos+1] == '=') {
							lexemas.add(new Lexema(lineNo,pos-from,LexemaType.Operator,LexemaSubtype.EQ,PRTY_COMPARISON)); 
							pos+=2;
						}
						else {
							lexemas.add(new Lexema(lineNo,pos-from,LexemaType.Operator,LexemaSubtype.Assign,PRTY_ASSIGN)); 
							pos++;
						}
						break;
					case '!'	: 
						if (data[pos+1] == '=') {
							lexemas.add(new Lexema(lineNo,pos-from,LexemaType.Operator,LexemaSubtype.NE,PRTY_COMPARISON)); 
							pos+=2;
						}
						else {
							lexemas.add(new Lexema(lineNo,pos-from,LexemaType.Operator,LexemaSubtype.Not,PRTY_NOT)); 
							pos++;
						}
						break;
					case '&'	: 
						if (data[pos+1] == '&') {
							lexemas.add(new Lexema(lineNo,pos-from,LexemaType.Operator,LexemaSubtype.And,PRTY_AND)); 
							pos+=2;
						}
						else {
							lexemas.add(new Lexema(lineNo,pos-from,LexemaType.Operator,LexemaSubtype.BitAnd,PRTY_BITAND)); 
							pos++;
						}
						break;
					case '|'	: 
						if (data[pos+1] == '|') {
							lexemas.add(new Lexema(lineNo,pos-from,LexemaType.Operator,LexemaSubtype.Or,PRTY_OR)); 
							pos+=2;
						}
						else {
							lexemas.add(new Lexema(lineNo,pos-from,LexemaType.Operator,LexemaSubtype.BitOr,PRTY_BITORXOR)); 
							pos++;
						}
						break;
					case '0' : case '1' : case '2' : case '3' : case '4' : case '5' : case '6' : case '7' : case '8' : case '9' :
						pos = CharUtils.parseNumber(data,pos,tempCell,CharUtils.PREF_ANY,false);
						if ((tempCell[1] & (CharUtils.PREF_INT | CharUtils.PREF_LONG)) != 0) {
							lexemas.add(new Lexema(lineNo,pos-from,tempCell[0]));
						}
						else {
							lexemas.add(new Lexema(lineNo,pos-from,Double.longBitsToDouble(tempCell[0])));
						}
						break;
					default :
						end = pos;
						while (Character.isJavaIdentifierPart(data[end])) {
							end++;
						}
						if (end == pos) {
							throw new SyntaxException(lineNo, pos-from, "Unknown symbol ["+data[pos]+"]");
						}
						else if ((keywordId = KEYWORDS.seekName(data,pos,end)) >= 0) {
							lexemas.add(new Lexema(lineNo,pos-from,KEYWORDS.getCargo(keywordId)));
							pos = end;
						}
						else {
							lexemas.add(new Lexema(lineNo,pos-from,LexemaSubtype.Undefined,names.placeOrChangeName(data,pos,end,null)));
							pos = end;
						}
				}
			}
		} catch (SyntaxException | RuntimeException e) {
			lexemas.clear();
			names.clear();
			names = null;
			throw e;
		}
	}

	@Override
	protected void afterCompile(final Reader reader, final OutputStream os) throws IOException {
		try{lexemas.add(new Lexema(0,0,LexemaType.EOF));
			MercCompiler.compile(lexemas.toArray(new Lexema[lexemas.size()]),names,os);
		} catch (SyntaxException e) {
			throw new IOException(e);
		} finally {
			lexemas.clear();
			names.clear();
			names = null;
		}
	}

	static class Lexema {
		final int			row, col;
		final LexemaType	type;
		final LexemaSubtype	subtype;
		final int			prty;
		final boolean		boolval;
		final long			intval;
		final double		realval;
		final char[]		strval;
		final Object		refval;
		
		private Lexema(int row, int col, boolean boolval) {
			this.row = row;
			this.col = col;
			this.type = LexemaType.BoolConst;
			this.subtype = LexemaSubtype.Undefined;
			this.prty = 0;
			this.boolval = boolval;
			this.intval = 0;
			this.realval = 0;
			this.strval = null;
			this.refval = null;
		}

		private Lexema(int row, int col, long intval) {
			this.row = row;
			this.col = col;
			this.type = LexemaType.IntConst;
			this.subtype = LexemaSubtype.Undefined;
			this.prty = 0;
			this.boolval = false;
			this.intval = intval;
			this.realval = 0;
			this.strval = null;
			this.refval = null;
		}

		private Lexema(int row, int col, double realval) {
			this.row = row;
			this.col = col;
			this.type = LexemaType.RealConst;
			this.subtype = LexemaSubtype.Undefined;
			this.prty = 0;
			this.boolval = false;
			this.intval = 0;
			this.realval = realval;
			this.strval = null;
			this.refval = null;
		}

		private Lexema(int row, int col, char[] strval, int from, int to) {
			this.row = row;
			this.col = col;
			this.type = LexemaType.StrConst;
			this.subtype = LexemaSubtype.Undefined;
			this.prty = 0;
			this.boolval = false;
			this.intval = 0;
			this.realval = 0;
			this.strval = Arrays.copyOfRange(strval, from, to);
			this.refval = null;
		}

		private Lexema(int row, int col, Object refval) {
			this.row = row;
			this.col = col;
			this.type = LexemaType.RefConst;
			this.subtype = LexemaSubtype.Undefined;
			this.prty = 0;
			this.boolval = false;
			this.intval = 0;
			this.realval = 0;
			this.strval = null;
			this.refval = refval;
		}

		private Lexema(int row, int col, LexemaType type) {
			this.row = row;
			this.col = col;
			this.type = type;
			this.subtype = LexemaSubtype.Undefined;
			this.prty = 0;
			this.boolval = false;
			this.intval = 0;
			this.realval = 0;
			this.strval = null;
			this.refval = null;
		}

		private Lexema(int row, int col, LexemaType type, LexemaSubtype subtype) {
			this.row = row;
			this.col = col;
			this.type = type;
			this.subtype = subtype;
			this.prty = 0;
			this.boolval = false;
			this.intval = 0;
			this.realval = 0;
			this.strval = null;
			this.refval = null;
		}

		private Lexema(int row, int col, LexemaType type, LexemaSubtype subtype, int prty) {
			this.row = row;
			this.col = col;
			this.type = type;
			this.subtype = subtype;
			this.prty = prty;
			this.boolval = false;
			this.intval = 0;
			this.realval = 0;
			this.strval = null;
			this.refval = null;
		}

		public Lexema(int row, int col, LexemaSubtype subtype, long intval) {
			this.row = row;
			this.col = col;
			this.type = LexemaType.Name;
			this.subtype = subtype;
			this.prty = 0;
			this.boolval = false;
			this.intval = intval;
			this.realval = 0;
			this.strval = null;
			this.refval = null;
		}

		public Lexema(int row, int col, Lexema another) {
			this.row = row;
			this.col = col;
			this.type = another.type;
			this.subtype = another.subtype;
			this.prty = another.prty;
			this.boolval = another.boolval;
			this.intval = another.intval;
			this.realval = another.realval;
			this.strval = another.strval;
			this.refval = another.refval;
		}

		@Override
		public String toString() {
			return "Lexema [row=" + row + ", col=" + col + ", type=" + type + ", subtype=" + subtype + ", prty=" + prty
					+ ", boolval=" + boolval + ", intval=" + intval + ", realval=" + realval + ", strval="
					+ Arrays.toString(strval) + ", refval=" + refval + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (boolval ? 1231 : 1237);
			result = prime * result + col;
			result = prime * result + (int) (intval ^ (intval >>> 32));
			result = prime * result + prty;
			long temp;
			temp = Double.doubleToLongBits(realval);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			result = prime * result + ((refval == null) ? 0 : refval.hashCode());
			result = prime * result + row;
			result = prime * result + Arrays.hashCode(strval);
			result = prime * result + ((subtype == null) ? 0 : subtype.hashCode());
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			Lexema other = (Lexema) obj;
			if (boolval != other.boolval) return false;
			if (intval != other.intval) return false;
			if (prty != other.prty) return false;
			if (Double.doubleToLongBits(realval) != Double.doubleToLongBits(other.realval)) return false;
			if (refval == null) { 
				if (other.refval != null) return false;
			} else if (!refval.equals(other.refval)) return false;
			if (!Arrays.equals(strval, other.strval)) return false;
			if (subtype != other.subtype) return false;
			if (type != other.type) return false;
			return true;
		}
	}
}
