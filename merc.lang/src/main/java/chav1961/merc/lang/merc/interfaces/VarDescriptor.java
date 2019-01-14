package chav1961.merc.lang.merc.interfaces;

public interface VarDescriptor {
	long getNameId();
	VarDescriptor[] getParameters();
	Class<?> getNameType();
	boolean isArray();
	boolean isReadOnly();
	boolean isVar();
	boolean isMethod();
	int howManyDimensions();
	VarDescriptor[] contentFields();
}