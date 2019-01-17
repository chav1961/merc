package chav1961.merc.lang.merc;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chav1961.merc.api.AreaKeeper;
import chav1961.merc.api.BooleanKeeper;
import chav1961.merc.api.DoubleKeeper;
import chav1961.merc.api.LongKeeper;
import chav1961.merc.api.PointKeeper;
import chav1961.merc.api.SizeKeeper;
import chav1961.merc.api.StringKeeper;
import chav1961.merc.api.TrackKeeper;
import chav1961.merc.lang.merc.MercScriptEngine.Lexema;
import chav1961.merc.lang.merc.SyntaxTreeNode.SyntaxTreeNodeType;
import chav1961.merc.lang.merc.interfaces.CharDataOutput;
import chav1961.merc.lang.merc.interfaces.LexemaType;
import chav1961.merc.lang.merc.interfaces.VarDescriptor;
import chav1961.purelib.basic.AndOrTree;
import chav1961.purelib.basic.CharUtils;
import chav1961.purelib.basic.Utils;
import chav1961.purelib.basic.exceptions.SyntaxException;
import chav1961.purelib.basic.interfaces.SyntaxTreeInterface;
import chav1961.purelib.enumerations.ContinueMode;
import chav1961.purelib.streams.JsonStaxParser.LexType;
import chav1961.purelib.streams.char2byte.AsmWriter;

class MercCompiler {
	static final int	PRTY_TERM = 0;
	static final int	PRTY_INCDEC = 1;
	static final int	PRTY_NEGATION = 2;
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
	static final int	PRTY_PIPE = 14;

	private static final SyntaxTreeInterface<Lexema>			KEYWORDS = new AndOrTree<>();
	private static final Map<LexemaSubtype,SyntaxTreeNodeType>	CONVERSIONS = new HashMap<>();
	
	static {
		KEYWORDS.placeName("if",new Lexema(0,0,0,LexemaType.If));
		KEYWORDS.placeName("then",new Lexema(0,0,0,LexemaType.Then));
		KEYWORDS.placeName("else",new Lexema(0,0,0,LexemaType.Else));
		KEYWORDS.placeName("for",new Lexema(0,0,0,LexemaType.For));
		KEYWORDS.placeName("in",new Lexema(0,0,0,LexemaType.In));
		KEYWORDS.placeName("do",new Lexema(0,0,0,LexemaType.Do));
		KEYWORDS.placeName("while",new Lexema(0,0,0,LexemaType.While));
		KEYWORDS.placeName("var",new Lexema(0,0,0,LexemaType.Var));
		KEYWORDS.placeName("type",new Lexema(0,0,0,LexemaType.TypeDef));
		KEYWORDS.placeName("func",new Lexema(0,0,0,LexemaType.Func));
		KEYWORDS.placeName("brick",new Lexema(0,0,0,LexemaType.Brick));
		KEYWORDS.placeName("break",new Lexema(0,0,0,LexemaType.Break));
		KEYWORDS.placeName("continue",new Lexema(0,0,0,LexemaType.Continue));
		KEYWORDS.placeName("return",new Lexema(0,0,0,LexemaType.Return));
		KEYWORDS.placeName("print",new Lexema(0,0,0,LexemaType.Print));
		KEYWORDS.placeName("lock",new Lexema(0,0,0,LexemaType.Lock));
		
		KEYWORDS.placeName("is",new Lexema(0,0,0,LexemaType.Operator,LexemaSubtype.IS,PRTY_COMPARISON));
		KEYWORDS.placeName("like",new Lexema(0,0,0,LexemaType.Operator,LexemaSubtype.LIKE,PRTY_COMPARISON));
		
		KEYWORDS.placeName("int",new Lexema(0,0,0,LexemaType.Type,LexemaSubtype.Int));
		KEYWORDS.placeName("real",new Lexema(0,0,0,LexemaType.Type,LexemaSubtype.Real));
		KEYWORDS.placeName("str",new Lexema(0,0,0,LexemaType.Type,LexemaSubtype.Str));
		KEYWORDS.placeName("bool",new Lexema(0,0,0,LexemaType.Type,LexemaSubtype.Bool));
		KEYWORDS.placeName("point",new Lexema(0,0,0,LexemaType.Type,LexemaSubtype.Point));
		KEYWORDS.placeName("area",new Lexema(0,0,0,LexemaType.Type,LexemaSubtype.Area));
		KEYWORDS.placeName("track",new Lexema(0,0,0,LexemaType.Type,LexemaSubtype.Track));
		KEYWORDS.placeName("size",new Lexema(0,0,0,LexemaType.Type,LexemaSubtype.Size));

		KEYWORDS.placeName("true",new Lexema(0,0,0,true));
		KEYWORDS.placeName("false",new Lexema(0,0,0,false));
		KEYWORDS.placeName("null",new Lexema(0,0,0,LexemaType.NullConst));

		KEYWORDS.placeName("robo",new Lexema(0,0,0,LexemaType.PredefinedName,LexemaSubtype.Robo));
		KEYWORDS.placeName("world",new Lexema(0,0,0,LexemaType.PredefinedName,LexemaSubtype.World));
		KEYWORDS.placeName("rt",new Lexema(0,0,0,LexemaType.PredefinedName,LexemaSubtype.Rt));
		KEYWORDS.placeName("market",new Lexema(0,0,0,LexemaType.PredefinedName,LexemaSubtype.Market));
		KEYWORDS.placeName("teleport",new Lexema(0,0,0,LexemaType.PredefinedName,LexemaSubtype.Teleport));

		CONVERSIONS.put(LexemaSubtype.Neg,SyntaxTreeNodeType.Negation);
		CONVERSIONS.put(LexemaSubtype.Inc,SyntaxTreeNodeType.PreInc);
		CONVERSIONS.put(LexemaSubtype.Dec,SyntaxTreeNodeType.PreDec);
		CONVERSIONS.put(LexemaSubtype.BitInv,SyntaxTreeNodeType.BitInv);
		CONVERSIONS.put(LexemaSubtype.Not,SyntaxTreeNodeType.Not);
	}
	
	static void processLine(final int lineNo, final char[] data, final int from, final int length, final boolean parseForHighlight, final SyntaxTreeInterface<?> names, final List<Lexema> lexemas) throws IOException, SyntaxException {
		final int 	to = from + length, tempResult[] = new int[2];
		long		keywordId, tempCell[] = new long[2];
		int			pos = from, end;
		
		try{while (pos < to) {
				while (data[pos] <= ' ' && data[pos] != '\r'  && data[pos] != '\n' ) {
					pos++;
				}
				switch (data[pos]) {
					case '\n' : case '\r' :
						return;
					case ','	:
						lexemas.add(new Lexema(lineNo,pos-from,1,LexemaType.Div)); 
						pos++;
						break;
					case ':'	: 
						lexemas.add(new Lexema(lineNo,pos-from,1,LexemaType.Colon)); 
						pos++;
						break;
					case ';'	: 
						lexemas.add(new Lexema(lineNo,pos-from,1,LexemaType.Semicolon)); 
						pos++;
						break;
					case '('	: 
						lexemas.add(new Lexema(lineNo,pos-from,1,LexemaType.Open)); 
						pos++;
						break;
					case '['	: 
						lexemas.add(new Lexema(lineNo,pos-from,1,LexemaType.OpenB)); 
						pos++;
						break;
					case '{'	: 
						lexemas.add(new Lexema(lineNo,pos-from,1,LexemaType.OpenF)); 
						pos++;
						break;
					case ')'	: 
						lexemas.add(new Lexema(lineNo,pos-from,1,LexemaType.Close)); 
						pos++;
						break;
					case ']'	: 
						lexemas.add(new Lexema(lineNo,pos-from,1,LexemaType.CloseB)); 
						pos++;
						break;
					case '}'	: 
						lexemas.add(new Lexema(lineNo,pos-from,1,LexemaType.CloseF)); 
						pos++;
						break;
					case '~'	: 
						lexemas.add(new Lexema(lineNo,pos-from,1,LexemaType.Operator,LexemaSubtype.BitInv,PRTY_MUL));
						pos++;
						break;
					case '^'	: 
						lexemas.add(new Lexema(lineNo,pos-from,1,LexemaType.Operator,LexemaSubtype.BitXor,PRTY_SHIFT));
						pos++;
						break;
					case '.'	:
						if (data[pos+1] == '.') {
							lexemas.add(new Lexema(lineNo,pos-from,2,LexemaType.Period));
							pos+=2;
						}
						else {
							lexemas.add(new Lexema(lineNo,pos-from,1,LexemaType.Dot)); 
							pos++;
						}
						break;
					case '+'	: 
						if (data[pos+1] == '+') {
							lexemas.add(new Lexema(lineNo,pos-from,2,LexemaType.Operator,LexemaSubtype.Inc,PRTY_INCDEC));
							pos+=2;
						}
						else {
							lexemas.add(new Lexema(lineNo,pos-from,1,LexemaType.Operator,LexemaSubtype.Add,PRTY_ADD)); 
							pos++;
						}
						break;
					case '-'	: 
						if (data[pos+1] == '-') {
							lexemas.add(new Lexema(lineNo,pos-from,2,LexemaType.Operator,LexemaSubtype.Dec,PRTY_INCDEC)); 
							pos+=2;
						}
						else if (data[pos+1] == '>') {
							lexemas.add(new Lexema(lineNo,pos-from,2,LexemaType.Pipe)); 
							pos+=2;
						}
						else {
							lexemas.add(new Lexema(lineNo,pos-from,1,LexemaType.Operator,LexemaSubtype.Sub,PRTY_ADD)); 
							pos++;
						}
						break;
					case '*'	: 
						lexemas.add(new Lexema(lineNo,pos-from,1,LexemaType.Operator,LexemaSubtype.Mul,PRTY_MUL));
						pos++;
						break;
					case '/'	:
						if (data[pos+1] == '/') {	// Comment
							if (parseForHighlight) {
								lexemas.add(new Lexema(lineNo,pos-from,to-pos));
							}
							return;
						}
						else {
							lexemas.add(new Lexema(lineNo,pos-from,1,LexemaType.Operator,LexemaSubtype.Div,PRTY_MUL));
							pos++;
						}
						break;
					case '%'	: 
						lexemas.add(new Lexema(lineNo,pos-from,1,LexemaType.Operator,LexemaSubtype.Rem,PRTY_MUL)); 
						pos++; 
						break;
					case '>'	: 
						if (data[pos+1] == '>') {
							if (data[pos+2] == '>') {
								lexemas.add(new Lexema(lineNo,pos-from,3,LexemaType.Operator,LexemaSubtype.Shra,PRTY_SHIFT)); 
								pos+=3;
							}
							else {
								lexemas.add(new Lexema(lineNo,pos-from,2,LexemaType.Operator,LexemaSubtype.Shr,PRTY_SHIFT)); 
								pos+=2;
							}
						}
						else if (data[pos+1] == '=') {
							lexemas.add(new Lexema(lineNo,pos-from,2,LexemaType.Operator,LexemaSubtype.GE,PRTY_COMPARISON)); 
							pos+=2;
						}
						else {
							lexemas.add(new Lexema(lineNo,pos-from,1,LexemaType.Operator,LexemaSubtype.GT,PRTY_COMPARISON));
							pos++;
						}
						break;
					case '<'	: 
						if (data[pos+1] == '<') {
							lexemas.add(new Lexema(lineNo,pos-from,2,LexemaType.Operator,LexemaSubtype.Shl,PRTY_SHIFT));
							pos+=2;
						}
						else if (data[pos+1] == '=') {
							lexemas.add(new Lexema(lineNo,pos-from,2,LexemaType.Operator,LexemaSubtype.LE,PRTY_COMPARISON));
							pos+=2;
						}
						else {
							lexemas.add(new Lexema(lineNo,pos-from,1,LexemaType.Operator,LexemaSubtype.LT,PRTY_COMPARISON)); 
							pos++;
						}
						break;
					case '='	: 
						if (data[pos+1] == '=') {
							lexemas.add(new Lexema(lineNo,pos-from,2,LexemaType.Operator,LexemaSubtype.EQ,PRTY_COMPARISON)); 
							pos+=2;
						}
						else {
							lexemas.add(new Lexema(lineNo,pos-from,1,LexemaType.Operator,LexemaSubtype.Assign,PRTY_ASSIGN)); 
							pos++;
						}
						break;
					case '!'	: 
						if (data[pos+1] == '=') {
							lexemas.add(new Lexema(lineNo,pos-from,2,LexemaType.Operator,LexemaSubtype.NE,PRTY_COMPARISON)); 
							pos+=2;
						}
						else {
							lexemas.add(new Lexema(lineNo,pos-from,1,LexemaType.Operator,LexemaSubtype.Not,PRTY_NOT)); 
							pos++;
						}
						break;
					case '&'	: 
						if (data[pos+1] == '&') {
							lexemas.add(new Lexema(lineNo,pos-from,2,LexemaType.Operator,LexemaSubtype.And,PRTY_AND)); 
							pos+=2;
						}
						else {
							lexemas.add(new Lexema(lineNo,pos-from,1,LexemaType.Operator,LexemaSubtype.BitAnd,PRTY_BITAND)); 
							pos++;
						}
						break;
					case '|'	: 
						if (data[pos+1] == '|') {
							lexemas.add(new Lexema(lineNo,pos-from,2,LexemaType.Operator,LexemaSubtype.Or,PRTY_OR)); 
							pos+=2;
						}
						else {
							lexemas.add(new Lexema(lineNo,pos-from,1,LexemaType.Operator,LexemaSubtype.BitOr,PRTY_BITORXOR)); 
							pos++;
						}
						break;
					case '0' : case '1' : case '2' : case '3' : case '4' : case '5' : case '6' : case '7' : case '8' : case '9' :
						final int	numberStart = pos;
						
						try {pos = CharUtils.parseNumber(data,pos,tempCell,CharUtils.PREF_ANY,false);
						} catch (NumberFormatException exc) {
							if (parseForHighlight) {
								pos = numberStart+1;
								tempCell[1] = CharUtils.PREF_INT;
							}
							else {
								throw new SyntaxException(lineNo, pos-from, exc.getLocalizedMessage());
							}
						}
						if ((tempCell[1] & (CharUtils.PREF_INT | CharUtils.PREF_LONG)) != 0) {
							lexemas.add(new Lexema(lineNo,numberStart-from,pos-numberStart,tempCell[0]));
						}
						else {
							lexemas.add(new Lexema(lineNo,numberStart-from,pos-numberStart,Double.longBitsToDouble(tempCell[0])));
						}
						break;
					case '\"'	:
						final int	charConstStart = pos;
						
						try{pos = CharUtils.parseUnescapedString(data,charConstStart+1,'\"',true,tempResult);
						} catch (IllegalArgumentException exc) {
							if (parseForHighlight) {
								pos = to;
							}
							else {
								throw new SyntaxException(lineNo, pos-from, exc.getLocalizedMessage());
							}
						}
						lexemas.add(new Lexema(lineNo,charConstStart-from,pos-charConstStart,data,charConstStart+1,pos-1));
						break;
					default :
						end = pos;
						while (Character.isJavaIdentifierPart(data[end])) {
							end++;
						}
						if (end == pos) {
							if (parseForHighlight) {
								lexemas.add(new Lexema(lineNo,pos-from,1,data[pos++]));
							}
							else {
								throw new SyntaxException(lineNo, pos-from, "Unknown symbol ["+data[pos]+"]");
							}
						}
						else if ((keywordId = KEYWORDS.seekName(data,pos,end)) >= 0) {
							lexemas.add(new Lexema(lineNo,pos-from,end-pos,KEYWORDS.getCargo(keywordId)));
							pos = end;
						}
						else {
							lexemas.add(new Lexema(lineNo,pos-from,end-pos,LexemaSubtype.Undefined,names.placeOrChangeName(data,pos,end,null)));
							pos = end;
						}
				}
			}
		} catch (SyntaxException e) {
			lexemas.clear();
			names.clear();
			throw e;
		}
	}
	
	static void compile(final Lexema[] lexemas, final int current, final SyntaxTreeInterface<?> names, final OutputStream target) throws SyntaxException, IOException {
		final MercClassRepo		classes = new MercClassRepo(names,current);
		final MercNameRepo		vars = new MercNameRepo();
		final SyntaxTreeNode	root = new SyntaxTreeNode();
		final int				lastParsed = buildSyntaxTree(lexemas,0,names,classes,vars,root); 
	
		if (lexemas[lastParsed].type != LexemaType.EOF) {
			throw new SyntaxException(lexemas[lastParsed].row,lexemas[lastParsed].col,"Unparsed tail in the program!");
		}
		else {
			try(final AsmWriter			writer = new AsmWriter(target)) {
				try(final InputStream	is = MercCompiler.class.getResourceAsStream("macros.txt");
					final Reader		rdr = new InputStreamReader(is)) {
					
					Utils.copyStream(rdr,writer);
				}
				final CharDataOutput	out = null;
				
				MercCodeBuilder.printHead(root,names,classes,vars,out);
				MercCodeBuilder.printFields(((SyntaxTreeNode)root.cargo),names,classes,vars,out);
				MercCodeBuilder.printMain(((SyntaxTreeNode)root.cargo),names,classes,vars,out);
				for (SyntaxTreeNode item : root.children) {
					MercCodeBuilder.printFunc(((SyntaxTreeNode)item.cargo),names,classes,vars,out);
				}
				MercCodeBuilder.printTail(root,names,classes,vars,out);
			}
		}
	}

	static int buildSyntaxTree(final Lexema[] lexemas, final int current, final SyntaxTreeInterface<?> names, final MercClassRepo classes, final MercNameRepo vars, final SyntaxTreeNode node) throws SyntaxException {
		final SyntaxTreeNode		main = new SyntaxTreeNode();
		final List<SyntaxTreeNode>	funcs = new ArrayList<>();
		final int[]					allocation = new int[]{0};
		
		buildGlobalReferences(lexemas,0,names,classes,vars,allocation);	// Collect functions and bricks 
		
		int	pos = buildMainBlockSyntaxTree(lexemas,current,names,classes,vars,allocation,main);
		
		while(lexemas[pos].type == LexemaType.Semicolon) {
			pos++;
		}
		while (lexemas[pos].type == LexemaType.Func || lexemas[pos].type == LexemaType.Brick) {
			final SyntaxTreeNode	temp = new SyntaxTreeNode();
			
			switch (lexemas[pos].type) {
				case Func 	:
					pos = buildFuncSyntaxTree(lexemas,pos,names,classes,vars,new int[]{0},temp);
					funcs.add(temp);
					break;
				case Brick	:
					pos = buildBrickSyntaxTree(lexemas,pos,names,classes,vars,new int[]{0},temp);
					funcs.add(temp);
					break;
				default	:
					throw new UnsupportedOperationException("Lex type ["+lexemas[pos].type+"] is not supported yet");
			}
			while(lexemas[pos].type == LexemaType.Semicolon) {
				pos++;
			}
		}
		if (lexemas[pos].type != LexemaType.EOF) {
			throw new SyntaxException(lexemas[pos].row,lexemas[pos].row,"Unparsed tail in the program!");
		}
		else {
			node.assignProgram(main,funcs.toArray(new SyntaxTreeNode[funcs.size()]));
			return pos;
		}
	}
	
	private static void buildGlobalReferences(final Lexema[] lexemas, final int current, final SyntaxTreeInterface<?> names, final MercClassRepo classes, final MercNameRepo vars, final int[] allocation) throws SyntaxException {
		for (int index = current, maxIndex = lexemas.length; index < maxIndex; index++) {
			switch (lexemas[index].type) {
				case Func 	:
					final SyntaxTreeNode	funcNode = new SyntaxTreeNode();
					
					index = buildHeadSyntaxTree(lexemas,index+1,names,classes,vars,true,new int[]{0},funcNode);
					vars.addLocalVar(new VarDescriptorImpl(allocation[0]++,funcNode.value,extractHeadParameters(funcNode.children),(Class<?>)funcNode.cargo,0));
					break;
				case Brick	:
					final SyntaxTreeNode	brickNode = new SyntaxTreeNode();
					
					index = buildHeadSyntaxTree(lexemas,index+1,names,classes,vars,false,new int[]{0},brickNode); 
					vars.addLocalVar(new VarDescriptorImpl(allocation[0]++,brickNode.value,extractHeadParameters(brickNode.children),void.class,0));
					break;
				default :
			}
		}
	}

	static int buildMainBlockSyntaxTree(final Lexema[] lexemas, final int current, final SyntaxTreeInterface<?> names,final MercClassRepo classes, final MercNameRepo vars, final int[] allocation, final SyntaxTreeNode node) throws SyntaxException {
		final List<SyntaxTreeNode>	list = new ArrayList<>();
		int		pos = current-1;	// To skip missing ';' at the same first statement
		
		do {final SyntaxTreeNode	temp = new SyntaxTreeNode();
			
			pos = buildBodySyntaxTree(lexemas,pos+1,names,classes,vars,allocation,temp);
			list.add(temp);
		} while(lexemas[pos].type == LexemaType.Semicolon);
		
		node.assignSequence(list.toArray(new SyntaxTreeNode[list.size()]));
		list.clear();
		return pos;
	}

	static int buildFuncSyntaxTree(final Lexema[] lexemas, final int current, final SyntaxTreeInterface<?> names,final MercClassRepo classes, final MercNameRepo vars, final int[] allocation, final SyntaxTreeNode node) throws SyntaxException {
		final SyntaxTreeNode	head = new SyntaxTreeNode(), body = new SyntaxTreeNode();
		int		pos = current;
		
		pos = buildHeadSyntaxTree(lexemas, pos+1, names, classes, vars, true, allocation, head);
		if (lexemas[pos].type == LexemaType.Semicolon) {
			pos = buildBodySyntaxTree(lexemas, pos+1, names, classes, vars, allocation, body);
			node.assignFunc(head,body);
		}
		else {
			throw new SyntaxException(lexemas[pos].row,lexemas[pos].row,"Missing (;)");
		}
		vars.popLevel();// Push was made in head processing
		return pos;
	}

	static int buildBrickSyntaxTree(final Lexema[] lexemas, final int current, final SyntaxTreeInterface<?> names,final MercClassRepo classes, final MercNameRepo vars, final int[] allocation, final SyntaxTreeNode node) throws SyntaxException {
		final SyntaxTreeNode	head = new SyntaxTreeNode(), body = new SyntaxTreeNode();
		int		pos = current;
		
		pos = buildHeadSyntaxTree(lexemas, pos+1, names, classes, vars, false, allocation, head);
		if (lexemas[pos].type == LexemaType.Semicolon) {
			pos = buildBodySyntaxTree(lexemas, pos+1, names, classes, vars, allocation, body);
			node.assignBrick(head,body);
		}
		else {
			throw new SyntaxException(lexemas[pos].row,lexemas[pos].row,"Missing (;)");
		}
		vars.popLevel();// Push was made in head processing
		return pos;
	}

	static int buildHeadSyntaxTree(final Lexema[] lexemas, final int current, final SyntaxTreeInterface<?> names, final MercClassRepo classes, final MercNameRepo vars, final boolean hasReturnedValue, final int[] allocation, final SyntaxTreeNode node) throws SyntaxException {
		int		pos = current;
		
		if (lexemas[pos].type == LexemaType.Name) {
			final List<SyntaxTreeNode>	parms = new ArrayList<>();
			final long	name = lexemas[pos++].intval;
			
			vars.pushLevel(name);
			if (lexemas[pos].type == LexemaType.Open) {
				if (lexemas[pos+1].type == LexemaType.Close) {
					pos += 2;
				}
				else {
					do {pos++;
						if (lexemas[pos].type == LexemaType.Var || lexemas[pos].type == LexemaType.Name) {
							final SyntaxTreeNode	parm = new SyntaxTreeNode();
							
							pos = buildNameDef(lexemas, pos, names, classes, vars, true, false, allocation, parm);
							parms.add(parm);
						}
						else {
							throw new SyntaxException(lexemas[pos].row,lexemas[pos].row,"Missing name");
						}
					} while(lexemas[pos].type == LexemaType.Div);
					
					if (lexemas[pos].type == LexemaType.Close) {
						pos++;
					}
					else {
						throw new SyntaxException(lexemas[pos].row,lexemas[pos].row,"Missing ')'!");
					}
				}
				if (hasReturnedValue) {
					if (lexemas[pos].type == LexemaType.Colon) {
						if (lexemas[pos+1].type == LexemaType.Type) {
							node.assignHeaderWithReturned(name,subtype2Class(lexemas[pos+1].subtype),parms.toArray(new SyntaxTreeNode[parms.size()]));
							pos += 2;
						}
						else {
							throw new SyntaxException(lexemas[pos].row,lexemas[pos].row,"Missing returned value descriptor!");
						}
					}
					else {
						throw new SyntaxException(lexemas[pos].row,lexemas[pos].row,"Missing (:)!");
					}							
				}
				else {
					node.assignHeader(name,parms.toArray(new SyntaxTreeNode[parms.size()]));
				}
				parms.clear();
			}
			return pos;
		}
		else {
			throw new SyntaxException(lexemas[pos].row,lexemas[pos].row,"Missing name");
		}
	}
	
	static int buildNameDef(final Lexema[] lexemas, final int current, final SyntaxTreeInterface<?> names, final MercClassRepo classes, final MercNameRepo vars, final boolean useVar, final boolean useInitial, final int[] allocation, final SyntaxTreeNode parm) throws SyntaxException {
		boolean			isVar = false;
		long			nameId = -1;
		LexemaSubtype	nameType;
		int				pos = current;
		
		if (useVar && lexemas[pos].type == LexemaType.Var) {
			isVar = true;
			pos++;
		}
		if (lexemas[pos].type == LexemaType.Name) { 
			nameId = lexemas[pos].intval;
			if (lexemas[++pos].type == LexemaType.Colon) {
				 if (lexemas[++pos].type == LexemaType.Type) {
					 nameType = lexemas[pos].subtype;
					 
					 if (lexemas[++pos].type == LexemaType.Open) {
						 if (useInitial) {
							final SyntaxTreeNode	initial = new SyntaxTreeNode();
							
							pos = buildListSyntaxTree(lexemas, pos+1, names, classes, vars, false, initial);
							if (lexemas[pos].type == LexemaType.Close) {
								final VarDescriptor	desc = new VarDescriptorImpl(allocation[0]++,nameId,subtype2Class(nameType),false,isVar,0); 
								
								if (useVar) {
									vars.addParameter(desc);
								}
								else {
									vars.addLocalVar(desc);
								}
								parm.assignVarDefinition(nameId,desc,initial);
								pos++;
							}
							else {
								throw new SyntaxException(lexemas[pos].row,lexemas[pos].row,"Missing ')'");
							}
						 }
						 else {
							throw new SyntaxException(lexemas[pos].row,lexemas[pos].row,"Initial values are not supported here");
						 }
					 }
					 else {
						final VarDescriptor	desc = new VarDescriptorImpl(allocation[0]++,nameId,subtype2Class(nameType),false,isVar,0); 
						
						if (useVar) {
							vars.addParameter(desc);
						}
						else {
							vars.addLocalVar(desc);
						}
						parm.assignVarDefinition(nameId,desc);
					 }
				 }
				 else {
					throw new SyntaxException(lexemas[pos].row,lexemas[pos].row,"Missing type definition");
				 }
			 }
			 else {
				throw new SyntaxException(lexemas[pos].row,lexemas[pos].row,"Missing (:)");
			 }
		}
		else {
			throw new SyntaxException(lexemas[pos].row,lexemas[pos].row,"Missing name");
		}
		
		return pos;
	}

	static int buildBodySyntaxTree(final Lexema[] lexemas, final int current, final SyntaxTreeInterface<?> names,final MercClassRepo classes, final MercNameRepo vars, final int[] allocation, final SyntaxTreeNode node) throws SyntaxException {
		int						pos = current;
		
		switch (lexemas[pos].type) {
			case Var		:	// 'var' (<name> ':' <type>[<dimensions>] ['('<initial>')'])','...
				final List<SyntaxTreeNode>	varDefs = new ArrayList<>();
				
				do {final SyntaxTreeNode	varNode = new SyntaxTreeNode();
				
					pos = buildNameDef(lexemas,pos+1, names, classes, vars, false, true, allocation, varNode);
					varDefs.add(varNode);
				} while (lexemas[pos].type == LexemaType.Div);
				
				node.assignVarDefs(varDefs.toArray(new SyntaxTreeNode[varDefs.size()]));
				varDefs.clear();
				break;
			case TypeDef	:	// 'type' (<name> ':' <java.class.name>)','...
//				do {
//					
//				} while (lexemas[pos].type == LexemaType.Div);
//				break;
				throw new UnsupportedOperationException("Type def ot implemented yet"); 
			case Name		:	// {<left>'='<right> | <name>.<method> }
				pos = buildExpressionSyntaxTree(PRTY_ASSIGN, lexemas, pos, names, classes, vars, node);
				break;
			case PredefinedName	:	// <name>.<method>
				pos = buildNameSyntaxTree(lexemas, pos, names, classes, vars, node);
				break;
			case If			:	// 'if' <condition> 'then' <operator> ['else' <operator>]
				final SyntaxTreeNode	ifCond = new SyntaxTreeNode();
				
				pos = buildExpressionSyntaxTree(PRTY_ASSIGN, lexemas, pos+1, names, classes, vars, ifCond);
				if (lexemas[pos].type == LexemaType.Then) {
					final SyntaxTreeNode	thenBody = new SyntaxTreeNode();
					
					pos = buildBodySyntaxTree(lexemas, pos+1, names, classes, vars, allocation, thenBody);
					if (lexemas[pos].type == LexemaType.Else) {
						final SyntaxTreeNode	elseBody = new SyntaxTreeNode();
						
						pos = buildBodySyntaxTree(lexemas, pos+1, names, classes, vars, allocation, elseBody);
						node.assignIf(ifCond,thenBody,elseBody);
					}
					else {
						node.assignIf(ifCond,thenBody);
					}
				}
				break;
			case Do			:	// 'do' <operator> 'while' <condition>
				final SyntaxTreeNode	untilBody = new SyntaxTreeNode();
				
				pos = buildBodySyntaxTree(lexemas, pos+1, names, classes, vars, allocation, untilBody);
				if (lexemas[pos].type == LexemaType.While) {
					final SyntaxTreeNode	untilCond = new SyntaxTreeNode();
					
					pos = buildExpressionSyntaxTree(PRTY_ASSIGN, lexemas, pos+1, names, classes, vars, untilCond);
					node.assignUntil(untilCond,untilBody);
				}
				else {
					throw new SyntaxException(lexemas[pos].row,lexemas[pos].row,"Missing 'while' clause!");
				}
				break;
			case While		:	// 'while' <condition> 'do' <operator>
				final SyntaxTreeNode	whileCond = new SyntaxTreeNode();
				
				pos = buildExpressionSyntaxTree(PRTY_ASSIGN, lexemas, pos+1, names, classes, vars, whileCond);
				if (lexemas[pos].type == LexemaType.Do) {
					final SyntaxTreeNode	whileBody = new SyntaxTreeNode();
					
					pos = buildBodySyntaxTree(lexemas, pos+1, names, classes, vars, allocation, whileBody);
					node.assignWhile(whileCond,whileBody);
				}
				else {
					throw new SyntaxException(lexemas[pos].row,lexemas[pos].row,"Missing 'do' clause!");
				}
				break;
			case For		: 	// 'for' <name> [':' <type>] 'in' <list> 'do' <operator>
				final SyntaxTreeNode	forName = new SyntaxTreeNode();
				final SyntaxTreeNode	forType = new SyntaxTreeNode();
				boolean					typePresents = false;

				vars.pushLevel();
				pos = buildNameSyntaxTree(lexemas, pos+1, names, classes, vars, forName);
				if (lexemas[pos].type == LexemaType.Colon) {
					if (forName.getType() == SyntaxTreeNodeType.StandaloneName) {
						if (lexemas[pos+1].type == LexemaType.Type) {
							forType.assignType(lexemas[pos+1].subtype);
							typePresents = true;
							pos += 2;
						}
						else {
							throw new SyntaxException(lexemas[pos].row,lexemas[pos].row,"Unknown type for the variable");
						}
					}
					else {
						throw new SyntaxException(lexemas[pos].row,lexemas[pos].row,"Atempt to define type for complex expression! Only standalone name can be used in this case");
					}
				}
				if (lexemas[pos].type == LexemaType.In) {
					final SyntaxTreeNode	forList = new SyntaxTreeNode();
					
					pos = buildListSyntaxTree(lexemas, pos+1, names, classes, vars, true, forList);
					if (lexemas[pos].type == LexemaType.Do) {
						final SyntaxTreeNode	forBody = new SyntaxTreeNode();
						
						pos = buildBodySyntaxTree(lexemas, pos+1, names, classes, vars, allocation, forBody);
						if (typePresents) {
							node.assignFor(forName,forType,forList,forBody);
						}
						else {
							node.assignFor(forName,forList,forBody);
						}
					}
					else {
						throw new SyntaxException(lexemas[pos].row,lexemas[pos].row,"Missing 'do' clause!");
					}
				}
				else {
					throw new SyntaxException(lexemas[pos].row,lexemas[pos].row,"Missing 'in' clause!");
				}
				vars.popLevel();
				break;
			case Break		:	// 'break' [<integer>]
				if (lexemas[pos+1].type == LexemaType.IntConst) {
					node.assignBreak((int)lexemas[pos+1].intval);
					pos+=2;
				}
				else {
					node.assignBreak(0);
					pos++;
				}
				break;
			case Continue	:	// 'continue' [<integer>] 
				if (lexemas[pos+1].type == LexemaType.IntConst) {
					node.assignContinue((int)lexemas[pos+1].intval);
					pos+=2;
				}
				else {
					node.assignContinue(0);
					pos++;
				}
				break;
			case Return		:	// 'return' [<expression>]
				final SyntaxTreeNode	returnExpression = new SyntaxTreeNode();
				int						oldPos = pos;
				
				try{pos = buildExpressionSyntaxTree(PRTY_ASSIGN, lexemas, pos+1, names, classes, vars, returnExpression);
					node.assignReturn(returnExpression);
				} catch (SyntaxException exc) {
					// TODO:
					node.assignReturn();
					pos++;
				}
				break;
			case Print		:	// 'print' (<expression>)','...
				final SyntaxTreeNode	printExpression = new SyntaxTreeNode();
				
				pos = buildListSyntaxTree(lexemas, pos+1, names, classes, vars, false, printExpression);
				node.assignPrint(printExpression);
				break;
			case Lock		:	// 'lock' <list> ':' <operator>
				final SyntaxTreeNode	lockExpression = new SyntaxTreeNode();
				
				pos = buildListSyntaxTree(lexemas, pos+1, names, classes, vars, true,lockExpression);
				if (lexemas[pos].type == LexemaType.Colon) {
					final SyntaxTreeNode	lockBody = new SyntaxTreeNode();
					
					pos = buildBodySyntaxTree(lexemas, pos+1, names, classes, vars, allocation, lockBody);
					node.assignLock(lockExpression,lockBody);
				}
				else {
					throw new SyntaxException(lexemas[pos].row,lexemas[pos].row,"Missing ':'!");
				}
				break;
			case OpenF		:	// '{' <operator>[';'...] '}'
				final List<SyntaxTreeNode>	list = new ArrayList<>();				
				
				vars.pushLevel();
				do {final SyntaxTreeNode	temp = new SyntaxTreeNode();
					
					pos = buildBodySyntaxTree(lexemas, pos+1, names, classes, vars, allocation, temp);
					list.add(temp);
				} while(lexemas[pos].type == LexemaType.Semicolon);
				vars.popLevel();
				
				if (lexemas[pos].type == LexemaType.CloseF) {
					node.assignSequence(list.toArray(new SyntaxTreeNode[list.size()]));
					list.clear();
					pos++;
				}
				else {
					throw new SyntaxException(lexemas[pos].row,lexemas[pos].row,"Missing '}'!");
				}
				break;
			case Semicolon :
				pos++;
				break;
			case Func : case Brick : case EOF :
				break;
			default :
				throw new SyntaxException(lexemas[pos].row,lexemas[pos].row,"Unwaited lexema!");
		}
		return pos;
	}

	static int buildNameSyntaxTree(final Lexema[] lexemas, final int current, final SyntaxTreeInterface<?> names,final MercClassRepo classes, final MercNameRepo vars, final SyntaxTreeNode node) throws SyntaxException {
		int 	pos = current;
		long	nameId = lexemas[pos].intval;

		if (lexemas[pos].type == LexemaType.PredefinedName) {
			node.assignPredefinedName(lexemas[pos].subtype);
			if (lexemas[pos+1].type == LexemaType.Open || lexemas[pos+1].type == LexemaType.OpenB) {
				throw new SyntaxException(lexemas[pos].row,lexemas[pos].row,"Predefined name can't neither indiced not callable!");
			}
			else {
				pos++;
			}
		}
		else {
			node.assignName(nameId);
			if (lexemas[pos+1].type == LexemaType.OpenB) {
				final SyntaxTreeNode	indices = new SyntaxTreeNode();
				
				pos = buildListSyntaxTree(lexemas, pos+2, names, classes, vars, false, indices);
				if (lexemas[pos].type == LexemaType.CloseB) {
					node.assignNameIndiced(nameId,indices);
					pos++;
				}
				else {
					throw new SyntaxException(lexemas[pos].row,lexemas[pos].row,"Missing ']'!");
				}
			}
			else {
				pos++;
			}
			if (lexemas[pos].type == LexemaType.Open) {
				final SyntaxTreeNode	call = new SyntaxTreeNode(node);	
				final SyntaxTreeNode	parms = new SyntaxTreeNode();
				
				if (lexemas[pos+1].type == LexemaType.Close) {
					node.assignCall(call);
					pos += 2;
				}
				else {
					pos = buildListSyntaxTree(lexemas, pos+1, names, classes, vars, false, parms);
					if (lexemas[pos].type == LexemaType.Close) {
						node.assignCall(call,parms);
						pos++;
					}
					else {
						throw new SyntaxException(lexemas[pos].row,lexemas[pos].row,"Missing ')'!");
					}
				}
			}
		}
		if (lexemas[pos].type == LexemaType.Dot) {
			final SyntaxTreeNode	owner = new SyntaxTreeNode(node);
			final SyntaxTreeNode	field = new SyntaxTreeNode();
			
			pos = buildNameSyntaxTree(lexemas, pos+1, names, classes, vars, field);
			node.assignField(owner,field);
		}
		return pos;
	}
	
	static int buildExpressionSyntaxTree(final int level, final Lexema[] lexemas, final int current, final SyntaxTreeInterface<?> names,final MercClassRepo classes, final MercNameRepo vars, final SyntaxTreeNode node) throws SyntaxException {
		int	pos = current;
		
		switch (level) {
			case PRTY_TERM		:
				switch (lexemas[pos].type) {
					case BoolConst	:
						node.assignBoolean(lexemas[pos].boolval);
						pos++;
						break;
					case IntConst	:
						node.assignInt(lexemas[pos].intval);
						pos++;
						break;
					case NullConst	:
						node.assignNull();
						pos++;
						break;
					case Open		:
						pos = buildExpressionSyntaxTree(PRTY_ASSIGN, lexemas, pos+1, names, classes, vars, node);
						if (lexemas[pos].type == LexemaType.Close) {
							pos++;
						}
						else {
							throw new SyntaxException(lexemas[pos].row,lexemas[pos].row,"Missing ')'!");
						}
						break;
					case Name			:
					case PredefinedName	:
						pos = buildNameSyntaxTree(lexemas, pos, names, classes, vars, node);
						break;
					case RealConst	:
						node.assignReal(lexemas[pos].realval);
						pos++;
						break;
					case RefConst	:
						node.assignRefConst(lexemas[pos].refval);
						pos++;
						break;
					case StrConst	:
						node.assignStr(lexemas[pos].strval);
						pos++;
						break;
					case Type		:
						final LexemaSubtype	conv = lexemas[pos].subtype;
						
						if (lexemas[pos+1].type == LexemaType.Open) {
							final SyntaxTreeNode	inner = new SyntaxTreeNode();
							
							pos = buildListSyntaxTree(lexemas, pos+2, names, classes, vars, false, inner);
							if (lexemas[pos].type == LexemaType.Close) {
								node.assignConversion(conv,inner);
								pos++;
							}
							else {
								throw new SyntaxException(lexemas[pos].row,lexemas[pos].row,"Missing ')'!");
							}
						}
						else {
							throw new SyntaxException(lexemas[pos].row,lexemas[pos].row,"Missing '('!");
						}
						break;
					default	:
						throw new SyntaxException(lexemas[pos].row,lexemas[pos].row,"Missing operand");
				}
				break;
			case PRTY_INCDEC	:
				if (lexemas[pos].type == LexemaType.Operator && inList(lexemas[pos].subtype,LexemaSubtype.Inc,LexemaSubtype.Dec)) {
					final SyntaxTreeNode	child = new SyntaxTreeNode();
					final LexemaSubtype		oper = lexemas[pos].subtype;
					
					pos = buildExpressionSyntaxTree(level-1, lexemas, pos+1, names, classes, vars, child);
					if (testLeftPart(child)) {
						node.assignUnary(convert2TreeNodeType(oper),child);
					}
					else {
						throw new SyntaxException(lexemas[pos-1].row,lexemas[pos-1].row,"Operand is not assignable!");
					}
				}
				else {
					pos = buildExpressionSyntaxTree(level-1, lexemas, pos, names, classes, vars, node);
					if (lexemas[pos].type == LexemaType.Operator && inList(lexemas[pos].subtype,LexemaSubtype.Inc,LexemaSubtype.Dec)) {
						final SyntaxTreeNode	child = new SyntaxTreeNode(node);
						
						if (testLeftPart(child)) {
							node.assignUnary(lexemas[pos].subtype == LexemaSubtype.Inc ? SyntaxTreeNodeType.PostInc : SyntaxTreeNodeType.PostDec, child);
							pos++;
						}
						else {
							throw new SyntaxException(lexemas[pos-1].row,lexemas[pos-1].row,"Operand is not assignable!");
						}
					}
				}
				break;
			case PRTY_NEGATION	:
				pos = buildUnary(level, lexemas, pos, names, classes, vars, node, LexemaSubtype.Sub);
				break;
			case PRTY_BITINV	:
				pos = buildUnary(level, lexemas, pos, names, classes, vars, node, LexemaSubtype.BitInv);
				break;
			case PRTY_BITAND	:
				pos = buildBinary(level, lexemas, pos, names, classes, vars, node, true, LexemaSubtype.BitAnd);
				break;
			case PRTY_BITORXOR	:
				pos = buildBinary(level, lexemas, pos, names, classes, vars, node, true, LexemaSubtype.BitOr,LexemaSubtype.BitXor);
				break;
			case PRTY_SHIFT	:
				pos = buildBinary(level, lexemas, pos, names, classes, vars, node, false, LexemaSubtype.Shl, LexemaSubtype.Shr, LexemaSubtype.Shra);
				break;
			case PRTY_MUL		:
				pos = buildBinary(level, lexemas, pos, names, classes, vars, node, true, LexemaSubtype.Mul, LexemaSubtype.Div, LexemaSubtype.Rem);
				break;
			case PRTY_ADD		:
				pos = buildBinary(level, lexemas, pos, names, classes, vars, node, true, LexemaSubtype.Add, LexemaSubtype.Sub);
				break;
			case PRTY_COMPARISON	:
				pos = buildExpressionSyntaxTree(level-1, lexemas, pos, names, classes, vars, node);
				if (lexemas[pos].type == LexemaType.Operator && inList(lexemas[pos].subtype,LexemaSubtype.EQ,LexemaSubtype.NE,LexemaSubtype.LT,LexemaSubtype.LE,LexemaSubtype.GT,LexemaSubtype.GE,LexemaSubtype.LIKE)){
					final SyntaxTreeNode	left = new SyntaxTreeNode(node);
					final SyntaxTreeNode	right = new SyntaxTreeNode();
					final LexemaSubtype		oper = lexemas[pos].subtype;
					
					pos = buildExpressionSyntaxTree(level-1, lexemas, pos+1, names, classes, vars, right);
					node.assignBinary(PRTY_COMPARISON,new LexemaSubtype[]{LexemaSubtype.Undefined,oper},new SyntaxTreeNode[]{left,right});
				}
				else if (lexemas[pos].type == LexemaType.In) {
					final SyntaxTreeNode	left = new SyntaxTreeNode(node);
					final SyntaxTreeNode	right = new SyntaxTreeNode();
					
					pos = buildListSyntaxTree(lexemas, pos+1, names, classes, vars, true, right);
					node.assignBinary(PRTY_COMPARISON,new LexemaSubtype[]{LexemaSubtype.Undefined,LexemaSubtype.InList},new SyntaxTreeNode[]{left,right});
				}
				break;
			case PRTY_NOT		:
				pos = buildUnary(level, lexemas, pos, names, classes, vars, node, LexemaSubtype.Not);
				break;
			case PRTY_AND		:
				pos = buildBinary(level, lexemas, pos, names, classes, vars, node, true, LexemaSubtype.And);
				break;
			case PRTY_OR		:
				pos = buildBinary(level, lexemas, pos, names, classes, vars, node, true, LexemaSubtype.Or);
				break;
			case PRTY_ASSIGN	:
				pos = buildExpressionSyntaxTree(level-1, lexemas, pos, names, classes, vars, node);
				if (lexemas[pos].type == LexemaType.Operator && lexemas[pos].subtype == LexemaSubtype.Assign) {
					if (testLeftPart(node)) {
						final SyntaxTreeNode	left = new SyntaxTreeNode(node), right = new SyntaxTreeNode();
						
						pos = buildExpressionSyntaxTree(level, lexemas, pos+1, names, classes, vars, right);
						node.assignAssignment(left,right);
					}
					else {
						throw new SyntaxException(lexemas[pos].row,lexemas[pos].row,"Illegal expression type left-part");
					}
				}
				break;
			case PRTY_PIPE		:
				pos = buildExpressionSyntaxTree(level-1, lexemas, pos, names, classes, vars, node);
				if (lexemas[pos].type == LexemaType.Pipe) {
					final SyntaxTreeNode		startPipe = new SyntaxTreeNode(node);
					final List<SyntaxTreeNode>	collection = new ArrayList<>();
					
					do {final SyntaxTreeNode	item = new SyntaxTreeNode();
					
						pos = buildExpressionSyntaxTree(level-1, lexemas, pos+1, names, classes, vars, item);
						collection.add(item);
					} while (lexemas[pos].type == LexemaType.Pipe);
					
					final SyntaxTreeNode		endPipe = collection.remove(collection.size()-1);
					
					node.assignPipe(startPipe,collection.toArray(new SyntaxTreeNode[collection.size()]),endPipe);
					collection.clear();
				}
				break;
			default :
				throw new UnsupportedOperationException("Level ["+level+"] is not supported yet");
		}
		return pos;
	}

	private static int buildUnary(final int level, final Lexema[] lexemas, final int current, final SyntaxTreeInterface<?> names, final MercClassRepo classes, final MercNameRepo vars, final SyntaxTreeNode node, final LexemaSubtype... operations) throws SyntaxException {
		int		pos = current;
		
		if (lexemas[pos].type == LexemaType.Operator && inList(lexemas[pos].subtype,operations)) {
			final LexemaSubtype		oper = lexemas[pos].subtype == LexemaSubtype.Sub ? LexemaSubtype.Neg : lexemas[pos].subtype;
			final SyntaxTreeNode	subNode = new SyntaxTreeNode();
			
			pos = buildExpressionSyntaxTree(level-1,lexemas, pos+1, names, classes, vars, subNode);
			node.assignUnary(convert2TreeNodeType(oper),subNode);
		}
		else {
			pos = buildExpressionSyntaxTree(level-1,lexemas, pos, names, classes, vars, node); 
		}
		return pos;
	}
	
	private static int buildBinary(final int level, final Lexema[] lexemas, final int current, final SyntaxTreeInterface<?> names, final MercClassRepo classes, final MercNameRepo vars, final SyntaxTreeNode node, final boolean repeatable, final LexemaSubtype... operations) throws SyntaxException {
		int		pos = current;
		
		pos = buildExpressionSyntaxTree(level-1, lexemas, pos, names, classes, vars, node);
		if (lexemas[pos].type == LexemaType.Operator && inList(lexemas[pos].subtype,operations)) {
			final List<SyntaxTreeNode>	nodeCollection = new ArrayList<>();
			final List<LexemaSubtype>	operCollection = new ArrayList<>();
			
			nodeCollection.add(new SyntaxTreeNode(node));
			operCollection.add(LexemaSubtype.Undefined);
			while (lexemas[pos].type == LexemaType.Operator && inList(lexemas[pos].subtype,operations)) {
				final LexemaSubtype		oper  = lexemas[pos].subtype;
				final SyntaxTreeNode	item = new SyntaxTreeNode(node);
				
				pos = buildExpressionSyntaxTree(level-1, lexemas, pos+1, names, classes, vars, item);
				nodeCollection.add(item);
				operCollection.add(oper);
				if (!repeatable) {
					break;
				}
			}
			node.assignBinary(level,operCollection.toArray(new LexemaSubtype[operCollection.size()]),nodeCollection.toArray(new SyntaxTreeNode[nodeCollection.size()]));
			nodeCollection.clear();
			operCollection.clear();
		}
		return pos;
	}

	private static boolean inList(final LexemaSubtype subtype, final LexemaSubtype... operations) {
		for (LexemaSubtype item : operations) {
			if (item == subtype) {
				return true;
			}
		}
		return false;
	}

	static int buildListSyntaxTree(final Lexema[] lexemas, final int current, final SyntaxTreeInterface<?> names, final MercClassRepo classes, final MercNameRepo vars, final boolean supportRanges, final SyntaxTreeNode node) throws SyntaxException {
		final List<SyntaxTreeNode>	collection = new ArrayList<>();
		int	pos = current-1;	// To skip first ',' missing
		
		do {final SyntaxTreeNode	itemNode = new SyntaxTreeNode();
			
			pos = buildExpressionSyntaxTree(PRTY_ASSIGN, lexemas, pos+1, names, classes, vars, itemNode);
			if (lexemas[pos].type == LexemaType.Period) {
				if (supportRanges) {
					final SyntaxTreeNode	nextNode = new SyntaxTreeNode(), rangeNode = new SyntaxTreeNode();
					
					pos = buildExpressionSyntaxTree(PRTY_ASSIGN, lexemas, pos+1, names, classes, vars, nextNode);
					rangeNode.assignRange(itemNode,nextNode);
					collection.add(rangeNode);
				}
				else {
					throw new SyntaxException(lexemas[pos].row, lexemas[pos].col, "Ranges is not supported in this place!"); 
				}
			}
			else {
				collection.add(itemNode);
			}
		} while (lexemas[pos].type == LexemaType.Div);

		node.assignList(collection.toArray(new SyntaxTreeNode[collection.size()]));
		collection.clear();
		return pos;
	}
	
	private static boolean testLeftPart(final SyntaxTreeNode node) {
		final SyntaxTreeNodeType	val = node.getType(); 
		
		return val == SyntaxTreeNodeType.StandaloneName || val == SyntaxTreeNodeType.IndicedName || val == SyntaxTreeNodeType.InstanceField;
	}

	private static SyntaxTreeNodeType convert2TreeNodeType(final LexemaSubtype subtype) {
		final SyntaxTreeNodeType	result = CONVERSIONS.get(subtype);
		
		if (result != null) {
			return result;
		}
		else {
			throw new UnsupportedOperationException("Subtype ["+subtype+"] is not supported yet");
		}
	}

	private static Class<?> subtype2Class(final LexemaSubtype nameType) {
		switch (nameType) {
			case Int	: return LongKeeper.class;
			case Real	: return DoubleKeeper.class;
			case Str	: return StringKeeper.class;
			case Bool	: return BooleanKeeper.class;
			case Point	: return PointKeeper.class;
			case Area	: return AreaKeeper.class;
			case Track	: return TrackKeeper.class;
			case Size	: return SizeKeeper.class;
			default : throw new UnsupportedOperationException("Subtype ["+nameType+"] is not supported yet"); 
		}
	}

	private static VarDescriptor[] extractHeadParameters(final SyntaxTreeNode[] node) {
		final VarDescriptor[]	result = new VarDescriptor[node.length];
		
		for (int index = 0; index < result.length; index++) {
			result[index] = (VarDescriptor)node[index].cargo;
		}
		return result;
	}
}
