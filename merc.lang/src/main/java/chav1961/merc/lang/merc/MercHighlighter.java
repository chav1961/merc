package chav1961.merc.lang.merc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import chav1961.merc.lang.merc.MercScriptEngine.Lexema;
import chav1961.merc.lang.merc.interfaces.LexemaType;
import chav1961.purelib.basic.LineByLineProcessor;
import chav1961.purelib.basic.OrdinalSyntaxTree;
import chav1961.purelib.basic.exceptions.SyntaxException;
import chav1961.purelib.basic.interfaces.SyntaxTreeInterface;

public class MercHighlighter {
	public static HighlightItem[] parseString(final String program) {
		if (program == null) {
			throw new NullPointerException("Program can't be null");
		}
		else {
			final SyntaxTreeInterface<Object>	names = new OrdinalSyntaxTree<>();
			final List<Lexema>					lexemas = new ArrayList<>();
			final char[]						content = program.replace("\r","").toCharArray();
			
			try(final LineByLineProcessor	lblp = new LineByLineProcessor((lineNo, data, from, length)->{MercCompiler.processLine(lineNo, data, from, length, true, names, lexemas);})) {
				lblp.write(content, 0, content.length);
			} catch (SyntaxException | IOException e) {
				e.printStackTrace();
			}
			lexemas.add(new Lexema(0,0,0,LexemaType.EOF));
			
			final HighlightItem[]	result = new HighlightItem[lexemas.size()-1];
			int						currentRow = 1, currentCol = 0, currentIndex = 0;
			
			for (int index = 0, maxIndex = result.length; index < maxIndex; index++) {
				final Lexema	lex = lexemas.get(index);

				while (currentIndex < content.length && (currentRow < lex.row || currentCol < lex.col)) {
					if (content[currentIndex] == '\n') {
						currentRow++;
						currentCol = 0;
					}
					else {
						currentCol++;
					}
					currentIndex++;
				}
				if (currentIndex >= content.length) {
					break;
				}
				result[index] = new HighlightItem(currentIndex,lex.len,lex.type);
			}
			return result;
		}
	}
	

	public static class HighlightItem {
		public final int 		from;
		public final int		length;
		public final LexemaType	type;
		
		public HighlightItem(int from, int length, LexemaType type) {
			this.from = from;
			this.length = length;
			this.type = type;
		}

		@Override
		public String toString() {
			return "HighlightItem [from=" + from + ", length=" + length + ", type=" + type + "]";
		}
	}
}
