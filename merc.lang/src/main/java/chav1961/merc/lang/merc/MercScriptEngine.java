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
		
		Lexema(int row, int col, boolean boolval) {
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

		Lexema(int row, int col, long intval) {
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

		Lexema(int row, int col, double realval) {
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

		Lexema(int row, int col, char[] strval, int from, int to) {
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

		Lexema(int row, int col, Object refval) {
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

		Lexema(int row, int col, LexemaType type) {
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

		Lexema(int row, int col, LexemaType type, LexemaSubtype subtype) {
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

		Lexema(int row, int col, LexemaType type, LexemaSubtype subtype, int prty) {
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

		Lexema(int row, int col, LexemaSubtype subtype, long intval) {
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

		Lexema(int row, int col, Lexema another) {
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
