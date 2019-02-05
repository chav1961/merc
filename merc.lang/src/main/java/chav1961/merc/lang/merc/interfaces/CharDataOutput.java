package chav1961.merc.lang.merc.interfaces;

import java.io.IOException;

public interface CharDataOutput {
	CharDataOutput writeln() throws IOException;
	CharDataOutput write(byte b) throws IOException;
	CharDataOutput writeln(byte b) throws IOException;
	CharDataOutput write(char c) throws IOException;
	CharDataOutput writeln(char c) throws IOException;
	CharDataOutput write(char[] c) throws IOException;
	CharDataOutput writeln(char[] c) throws IOException;
	CharDataOutput write(char[] c, int from, int to) throws IOException;
	CharDataOutput writeln(char[] c, int from, int to) throws IOException;
	CharDataOutput write(String s) throws IOException;
	CharDataOutput writeln(String s) throws IOException;
	CharDataOutput write(String s, int from, int to) throws IOException;
	CharDataOutput writeln(String s, int from, int to) throws IOException;
	CharDataOutput write(short s) throws IOException;
	CharDataOutput writeln(short s) throws IOException;
	CharDataOutput write(int i) throws IOException;
	CharDataOutput writeln(int i) throws IOException;
	CharDataOutput write(long l) throws IOException;
	CharDataOutput writeln(long l) throws IOException;
	CharDataOutput write(float f) throws IOException;
	CharDataOutput writeln(float f) throws IOException;
	CharDataOutput write(double d) throws IOException;
	CharDataOutput writeln(double d) throws IOException;
	CharDataOutput write(boolean d) throws IOException;
	CharDataOutput writeln(boolean d) throws IOException;
	CharDataOutput write(Object obj) throws IOException;
	CharDataOutput writeln(Object obj) throws IOException;
}
