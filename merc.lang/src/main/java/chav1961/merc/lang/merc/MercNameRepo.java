package chav1961.merc.lang.merc;

import chav1961.merc.lang.merc.MercScriptEngine.LexemaSubtype;
import chav1961.merc.lang.merc.interfaces.VarDescriptor;
import chav1961.purelib.basic.interfaces.SyntaxTreeInterface;

class MercNameRepo {
	MercNameRepo(final SyntaxTreeInterface<?> names) {
		
	}
	
	void pushLevel(final long unitName) {
		
	}

	void pushLevel() {
		
	}
	
	void popLevel() {
		
	}
	
	void addParameter(final boolean isVar, final long nameId, final LexemaSubtype typeId) {
		
	}

	void addParameter(final boolean isVar, final long nameId, final LexemaSubtype typeId, final String additional) {
		
	}
	
	void addLocalVar(final boolean readOnly, final long nameId, final LexemaSubtype typeId) {
		
	}

	void addLocalVar(final boolean readOnly, final long nameId, final LexemaSubtype typeId, final String additional) {
		
	}

	boolean hasName(final long nameId) {
		return false;
	}
	
	VarDescriptor getName(final long nameId) {
		return null;
	}
}
