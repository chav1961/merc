package chav1961.merc.lang.merc;

enum MercSyntaxTreeNodeType {
	StandaloneName, PredefinedName, LocalName, IndicedName,
	Call,
	InstanceField,
	Header, HeaderWithReturned,
	Program, Function, Brick,
	Conversion,
	OrdinalBinary,
	Assign,
	Pipe,
	Negation, 
	PreInc, PreDec, PostInc, PostDec, 
	BitInv, 
	Not,
	LongIf, ShortIf, While, Until, Break, Continue, ShortReturn, LongReturn, Print, Lock, TypedFor, UntypedFor, Sequence, Infinite,
	Variable, Variables, Vartype, AllocatedVariable,
	Null, IntConst, RealConst, StrConst, BoolConst, RefConst,  
	List, Range,
	Empty,
	Unknown,
	 
}