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
import chav1961.purelib.ui.HighlightItem;

public class MercHighlighter {
	public static HighlightItem<LexemaType>[] parseString(final String program) {
		if (program == null) {
			throw new NullPointerException("Program can't be null");
		}
		else {
			final SyntaxTreeInterface<Object>	names = new OrdinalSyntaxTree<>();
			final List<Lexema>					lexemas = new ArrayList<>();
			final char[]						content = program.replace("\r","").toCharArray();
			
			try(final LineByLineProcessor	lblp = new LineByLineProcessor((displacement,lineNo, data, from, length)->{MercCompiler.processLine(displacement,lineNo, data, from, length, true, names, lexemas);})) {
				lblp.write(content, 0, content.length);
			} catch (SyntaxException | IOException e) {
				e.printStackTrace();
			}
			lexemas.add(new Lexema(0,0,0,LexemaType.EOF));
			
			@SuppressWarnings("unchecked")
			final HighlightItem<LexemaType>[]	result = new HighlightItem[lexemas.size()-1];
			int			currentRow = 1, currentCol = 0, currentIndex = 0;
			
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
				result[index] = new HighlightItem<LexemaType>(currentIndex,lex.len,lex.type);
			}
			return result;
		}
	}
}
