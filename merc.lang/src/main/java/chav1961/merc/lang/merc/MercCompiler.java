package chav1961.merc.lang.merc;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import chav1961.merc.lang.merc.MercCompiler.SyntaxTreeNode.SyntaxTreeNodeType;
import chav1961.merc.lang.merc.MercScriptEngine.Lexema;
import chav1961.merc.lang.merc.MercScriptEngine.LexemaSubtype;
import chav1961.merc.lang.merc.MercScriptEngine.LexemaType;
import chav1961.purelib.basic.exceptions.SyntaxException;
import chav1961.purelib.basic.interfaces.SyntaxTreeInterface;

class MercCompiler {
	static void compile(final Lexema[] lexemas, final SyntaxTreeInterface<?> names, final OutputStream target) throws SyntaxException {
		
	}

	static int buildSyntaxTree(final Lexema[] lexemas, final int current, final SyntaxTreeInterface<?> names, final SyntaxTreeNode node) throws SyntaxException {
		final SyntaxTreeNode		main = new SyntaxTreeNode();
		final List<SyntaxTreeNode>	funcs = new ArrayList<>();
		final List<SyntaxTreeNode>	bricks = new ArrayList<>();
		
		int	pos = buildMainBlockSyntaxTree(0,lexemas,current,names,main);
		while (lexemas[pos].type == LexemaType.Func || lexemas[pos].type == LexemaType.Brick) {
			final SyntaxTreeNode	temp = new SyntaxTreeNode();
			
			switch (lexemas[pos].type) {
				case Func 	:
					pos = buildFuncSyntaxTree(0,lexemas,pos,names,temp);
					funcs.add(temp);
					break;
				case Brick	:
					pos = buildBrickSyntaxTree(0,lexemas,pos,names,temp);
					bricks.add(temp);
					break;
				default	:
					throw new UnsupportedOperationException("Lex type ["+lexemas[pos].type+"] is not supported yet");
			}
		}
		if (lexemas[pos].type != LexemaType.EOF) {
			throw new SyntaxException(lexemas[pos].row,lexemas[pos].row,"Unparsed tail in the program!");
		}
		else {
			return pos;
		}
	}
	
	static int buildMainBlockSyntaxTree(final int depth, final Lexema[] lexemas, final int current, final SyntaxTreeInterface<?> names, final SyntaxTreeNode node) throws SyntaxException {
		final List<SyntaxTreeNode>	list = new ArrayList<>();
		int		pos = current-1;	// To skip missing ';' at the same first statement
		
		do {final SyntaxTreeNode	temp = new SyntaxTreeNode();
			
			pos = buildBodySyntaxTree(depth,lexemas,pos+1,names,temp);
			list.add(temp);
		} while(lexemas[pos].type == LexemaType.Semicolon);
		clearInnerDefinitions(depth+1, names);
		
		node.assignSequence(list.toArray(new SyntaxTreeNode[list.size()]));
		list.clear();
		return pos;
	}

	static int buildFuncSyntaxTree(final int depth, final Lexema[] lexemas, final int current, final SyntaxTreeInterface<?> names, final SyntaxTreeNode node) throws SyntaxException {
		return buildBodySyntaxTree(depth, lexemas, current, names, node);
	}

	static int buildBrickSyntaxTree(final int depth, final Lexema[] lexemas, final int current, final SyntaxTreeInterface<?> names, final SyntaxTreeNode node) throws SyntaxException {
		return buildBodySyntaxTree(depth, lexemas, current, names, node);
	}

	static int buildBodySyntaxTree(final int depth, final Lexema[] lexemas, final int current, final SyntaxTreeInterface<?> names, final SyntaxTreeNode node) throws SyntaxException {
		int						pos = current;
		
		switch (lexemas[pos].type) {
			case Var		:	// 'var' (<name> ':' <type>[<dimensions>] ['('<initial>')'])','...
				do {
					
				} while (lexemas[pos].type == LexemaType.Div);
				break;
			case TypeDef	:	// 'type' (<name> ':' <java.class.name>)','...
				do {
					
				} while (lexemas[pos].type == LexemaType.Div);
				break;
			case Name		:	// {<left>'='<right> | <name>.<method> }
				pos = buildExpressionSyntaxTree(lexemas,pos,names,node);
				break;
			case PredefinedName	:	// <name>.<method>
				pos = buildCallSyntaxTree(lexemas,pos,names,node);
				break;
			case If			:	// 'if' <condition> 'then' <operator> ['else' <operator>]
				final SyntaxTreeNode	ifCond = new SyntaxTreeNode();
				
				pos = buildExpressionSyntaxTree(lexemas,pos+1,names,ifCond);
				if (lexemas[pos].type == LexemaType.Then) {
					final SyntaxTreeNode	thenBody = new SyntaxTreeNode();
					
					pos = buildBodySyntaxTree(depth+1,lexemas,pos+1,names,thenBody);
					clearInnerDefinitions(depth+1, names);
					if (lexemas[pos].type == LexemaType.Else) {
						final SyntaxTreeNode	elseBody = new SyntaxTreeNode();
						
						pos = buildBodySyntaxTree(depth+1,lexemas,pos+1,names,elseBody);
						clearInnerDefinitions(depth+1, names);
						node.assignIf(ifCond,thenBody,elseBody);
					}
					else {
						node.assignIf(ifCond,thenBody);
					}
				}
				break;
			case Do			:	// 'do' <operator> 'while' <condition>
				final SyntaxTreeNode	untilBody = new SyntaxTreeNode();
				
				pos = buildBodySyntaxTree(depth+1,lexemas,pos+1,names,untilBody);
				clearInnerDefinitions(depth+1, names);
				if (lexemas[pos].type == LexemaType.While) {
					final SyntaxTreeNode	untilCond = new SyntaxTreeNode();
					
					pos = buildExpressionSyntaxTree(lexemas,pos+1,names,untilCond);
					node.assignUntil(untilCond,untilBody);
				}
				else {
					throw new SyntaxException(lexemas[pos].row,lexemas[pos].row,"Missing 'while' clause!");
				}
				break;
			case While		:	// 'while' <condition> 'do' <operator>
				final SyntaxTreeNode	whileCond = new SyntaxTreeNode();
				
				pos = buildExpressionSyntaxTree(lexemas,pos+1,names,whileCond);
				if (lexemas[pos].type == LexemaType.Do) {
					final SyntaxTreeNode	whileBody = new SyntaxTreeNode();
					
					pos = buildBodySyntaxTree(depth+1,lexemas,pos+1,names,whileBody);
					clearInnerDefinitions(depth+1, names);
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
				
				pos = buildLeftPartSyntaxTree(lexemas,pos+1,names,forName);
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
					
					pos = buildListSyntaxTree(lexemas, pos+1, names, true, forList);
					if (lexemas[pos].type == LexemaType.Do) {
						final SyntaxTreeNode	forBody = new SyntaxTreeNode();
						
						pos = buildBodySyntaxTree(depth+1,lexemas, pos+1, names, forBody);
						clearInnerDefinitions(depth+1, names);
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
				
				pos = buildExpressionSyntaxTree(lexemas,pos+1,names,returnExpression);
				if (pos > oldPos) {
					node.assignReturn(returnExpression);
				}
				else {
					node.assignReturn();
				}
				break;
			case Print		:	// 'print' (<expression>)','...
				final SyntaxTreeNode	printExpression = new SyntaxTreeNode();
				
				pos = buildListSyntaxTree(lexemas,pos+1,names,false,printExpression);
				node.assignPrint(printExpression);
				break;
			case Lock		:	// 'lock' <list> ':' <operator>
				final SyntaxTreeNode	lockExpression = new SyntaxTreeNode();
				
				pos = buildListSyntaxTree(lexemas,pos+1,names,true,lockExpression);
				if (lexemas[pos].type == LexemaType.Colon) {
					final SyntaxTreeNode	lockBody = new SyntaxTreeNode();
					
					pos = buildBodySyntaxTree(depth+1,lexemas,pos+1,names,lockBody);
					clearInnerDefinitions(depth+1, names);
					node.assignLock(lockExpression,lockBody);
				}
				else {
					throw new SyntaxException(lexemas[pos].row,lexemas[pos].row,"Missing ':'!");
				}
				break;
			case OpenF		:	// '{' <operator>[';'...] '}'
				final List<SyntaxTreeNode>	list = new ArrayList<>();				
				
				do {final SyntaxTreeNode	temp = new SyntaxTreeNode();
					
					pos = buildBodySyntaxTree(depth+1,lexemas,pos+1,names,temp);
					list.add(temp);
				} while(lexemas[pos].type == LexemaType.Semicolon);
				clearInnerDefinitions(depth+1, names);
				
				if (lexemas[pos].type == LexemaType.CloseF) {
					node.assignSequence(list.toArray(new SyntaxTreeNode[list.size()]));
					list.clear();
					pos++;
				}
				else {
					throw new SyntaxException(lexemas[pos].row,lexemas[pos].row,"Missing '}'!");
				}
				break;
			case Semicolon : case Func : case Brick : case EOF :
				break;
			default :
				throw new SyntaxException(lexemas[pos].row,lexemas[pos].row,"Unwaited lexema!");
		}
		return pos;
	}

	static int buildLeftPartSyntaxTree(final Lexema[] lexemas, final int current, final SyntaxTreeInterface<?> names, final SyntaxTreeNode node) throws SyntaxException {
		return 0;
	}
	
	static int buildExpressionSyntaxTree(final Lexema[] lexemas, final int current, final SyntaxTreeInterface<?> names, final SyntaxTreeNode node) throws SyntaxException {
		return 0;
	}

	static int buildListSyntaxTree(final Lexema[] lexemas, final int current, final SyntaxTreeInterface<?> names, final boolean supportRanges, final SyntaxTreeNode node) throws SyntaxException {
		return 0;
	}
	
	static int buildCallSyntaxTree(final Lexema[] lexemas, final int current, final SyntaxTreeInterface<?> names, final SyntaxTreeNode node) throws SyntaxException {
		return 0;
	}
	
	static void buildNameDefinition(final int depth, final Lexema[] lexemas, final int current, final SyntaxTreeInterface<?> names, final boolean allowDimensions, final boolean allowInitials, final SyntaxTreeNode node) throws SyntaxException {
	}

	static void clearInnerDefinitions(final int depth, final SyntaxTreeInterface<?> names) throws SyntaxException {
		names.walk((name,len,id,cargo)->{
			if (cargo != null) {
				((NestedDefinition)cargo).clear(depth);
			}
			return true;
		});
	}
	
	static class NestedDefinition {
		void define(int depth) {
			
		}
		
		boolean isDefined(int depth) {
			return false;
		}
		
		void clear(int depth) {
			
		}
	}
	
	static class SyntaxTreeNode {
		enum SyntaxTreeNodeType {
			StandaloneName, 
			LeftPart,
			LongIf, ShortIf, While, Until, Break, Continue, ShortReturn, LongReturn, Print, Lock, TypedFor, UntypedFor,
			Sequence,
			Unknown
		}
		
		private SyntaxTreeNodeType	type = SyntaxTreeNodeType.Unknown; 
		
		SyntaxTreeNodeType getType() {
			return type;
		}
		
		void assignIf(final SyntaxTreeNode cond, final SyntaxTreeNode thenBody) {
			// TODO Auto-generated method stub
			
		}

		void assignIf(final SyntaxTreeNode cond, final SyntaxTreeNode thenBody, final SyntaxTreeNode elseBody) {
			// TODO Auto-generated method stub
			
		}
		
		void assignWhile(final SyntaxTreeNode cond, final SyntaxTreeNode body) {
			// TODO Auto-generated method stub
			
		}
		
		void assignUntil(final SyntaxTreeNode cond, final SyntaxTreeNode body) {
			// TODO Auto-generated method stub
			
		}

		void assignBreak(final int depth) {
			// TODO Auto-generated method stub
			
		}

		void assignContinue(final int depth) {
			// TODO Auto-generated method stub
			
		}

		void assignReturn() {
			// TODO Auto-generated method stub
			
		}

		void assignReturn(SyntaxTreeNode returnExpression) {
			// TODO Auto-generated method stub
			
		}

		void assignPrint(SyntaxTreeNode expressions) {
			// TODO Auto-generated method stub
			
		}

		void assignLock(SyntaxTreeNode lockExpression, SyntaxTreeNode lockBody) {
			// TODO Auto-generated method stub
			
		}

		void assignType(LexemaSubtype type) {
			// TODO Auto-generated method stub
			
		}

		void assignFor(SyntaxTreeNode forName, SyntaxTreeNode forType, SyntaxTreeNode forList, SyntaxTreeNode forBody) {
			// TODO Auto-generated method stub
			
		}

		void assignFor(SyntaxTreeNode forName, SyntaxTreeNode forList, SyntaxTreeNode forBody) {
			// TODO Auto-generated method stub
			
		}
		
		public void assignSequence(SyntaxTreeNode... array) {
			// TODO Auto-generated method stub
			
		}

	}
}
