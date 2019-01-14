package chav1961.merc.lang.merc.interfaces;

public interface VarDescriptor {
	long getNameId();
	Class<?> getNameType();
	boolean isArray();
	boolean isReadOnly();
	boolean isVar();
	int howManyDimensions();
	VarDescriptor[] content();
}