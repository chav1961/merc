package chav1961.merc.lang.merc;

public class BasicMercProgram {
	public static int _concat_(final char[] source, final char[] target, final int before) {
		System.arraycopy(source,0,target,before-source.length,source.length);
		return before - source.length;
	}
}
