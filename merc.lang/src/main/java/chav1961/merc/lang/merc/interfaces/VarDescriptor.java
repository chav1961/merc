package chav1961.merc.lang.merc.interfaces;

import chav1961.purelib.basic.interfaces.SyntaxTreeInterface;

public interface VarDescriptor {
	long getNameId();
	VarDescriptor[] getParameters();
	Class<?> getNameType();
	boolean isReference();
	boolean isArray();
	boolean isReadOnly();
	boolean isVar();
	boolean isMethod();
	int howManyDimensions();
	VarDescriptor[] contentFields();
	String toString(SyntaxTreeInterface<?> names);
}