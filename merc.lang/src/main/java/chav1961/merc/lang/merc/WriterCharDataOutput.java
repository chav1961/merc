package chav1961.merc.lang.merc;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;

import chav1961.merc.lang.merc.interfaces.CharDataOutput;

public class WriterCharDataOutput implements CharDataOutput, Flushable, Closeable {
	private final Writer	wr;
	
	public WriterCharDataOutput(final Writer wr) {
		this.wr = wr;
	}

	@Override
	public void flush() throws IOException {
		wr.flush();
	}

	@Override
	public void close() throws IOException {
	}

	@Override
	public CharDataOutput writeln() throws IOException {
		wr.append('\n');
		return this;
	}
	
	@Override
	public CharDataOutput write(byte b) throws IOException {
		return write((int)b);
	}

	@Override
	public CharDataOutput writeln(byte b) throws IOException {
		return writeln((int)b);
	}

	@Override
	public CharDataOutput write(char c) throws IOException {
		wr.append(c);
		return this; 
	}

	@Override
	public CharDataOutput writeln(char c) throws IOException {
		wr.append(c);
		wr.append('\n');
		return this; 
	}

	@Override
	public CharDataOutput write(char[] c) throws IOException {
		wr.write(c);
		return this;
	}

	@Override
	public CharDataOutput writeln(char[] c) throws IOException {
		wr.write(c);
		wr.append('\n');
		return this;
	}

	@Override
	public CharDataOutput write(char[] c, int from, int to) throws IOException {
		wr.write(c,from,to);
		return this;
	}

	@Override
	public CharDataOutput writeln(char[] c, int from, int to) throws IOException {
		wr.write(c,from,to);
		wr.append('\n');
		return this;
	}

	@Override
	public CharDataOutput write(String s) throws IOException {
		wr.write(s);
		return this;
	}

	@Override
	public CharDataOutput writeln(String s) throws IOException {
		wr.write(s);
		wr.append('\n');
		return this;
	}

	@Override
	public CharDataOutput write(String s, int from, int to) throws IOException {
		wr.write(s,from,to);
		return this;
	}

	@Override
	public CharDataOutput writeln(String s, int from, int to) throws IOException {
		wr.write(s,from,to);
		wr.append('\n');
		return this;
	}

	@Override
	public CharDataOutput write(short s) throws IOException {
		return write((int)s);
	}

	@Override
	public CharDataOutput writeln(short s) throws IOException {
		return writeln((int)s);
	}

	@Override
	public CharDataOutput write(int i) throws IOException {
		return write((long)i);
	}

	@Override
	public CharDataOutput writeln(int i) throws IOException {
		return writeln((long)i);
	}

	@Override
	public CharDataOutput write(long l) throws IOException {
		return write(String.valueOf(l));
	}

	@Override
	public CharDataOutput writeln(long l) throws IOException {
		return writeln(String.valueOf(l));
	}

	@Override
	public CharDataOutput write(float f) throws IOException {
		return write((double)f);
	}

	@Override
	public CharDataOutput writeln(float f) throws IOException {
		return writeln((double)f);
	}

	@Override
	public CharDataOutput write(double d) throws IOException {
		return write(String.valueOf(d));
	}

	@Override
	public CharDataOutput writeln(double d) throws IOException {
		return writeln(String.valueOf(d));
	}

	@Override
	public CharDataOutput write(boolean b) throws IOException {
		return writeln(String.valueOf(b));
	}

	@Override
	public CharDataOutput writeln(boolean b) throws IOException {
		return writeln(String.valueOf(b));
	}

	@Override
	public CharDataOutput write(Object obj) throws IOException {
		if (obj == null) {
			write("null");
			return this;
		}
		else {
			return write(obj.toString());
		}
	}

	@Override
	public CharDataOutput writeln(Object obj) throws IOException {
		if (obj == null) {
			writeln("null");
			return this;
		}
		else {
			return write(obj.toString());
		}
	}

}
