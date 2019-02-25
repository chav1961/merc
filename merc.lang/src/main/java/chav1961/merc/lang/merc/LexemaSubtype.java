package chav1961.merc.lang.merc;

enum LexemaSubtype {
	Int, Real, Str, Bool, Point, Area, Track, Size,
	Robo, World, Rt, Market, Teleport,		
	Neg, // prty=1 - negation
	Inc, Dec, PreInc, PreDec, PostInc, PostDec,	// prty=2
	BitInv,		// prty=3
	BitAnd, 	// prty=4
	BitOr, BitXor,	// prty=5
	Shl, Shr, Shra,	// prty=6
	Mul, Div, Rem,	// prty=7
	Add, Sub,	// prty=8
	LT, LE, GT, GE, EQ, NE, IS, LIKE, InList, // prty=9
	Not,   	// prty=10
	And, 	// prty=11
	Or,		// prty=12
	Assign,	// prty=13
	Undefined
}