package chav1961.merc.lang.merc;

import chav1961.merc.lang.merc.interfaces.VarDescriptor;
import chav1961.purelib.basic.LongIdMap;
import chav1961.purelib.basic.interfaces.SyntaxTreeInterface;

class MercNameRepo {
	private final SyntaxTreeInterface<?>	names;
	private LevelCollection					top = new LevelCollection(); 
	
	MercNameRepo(final SyntaxTreeInterface<?> names) {
		if (names == null) {
			throw new NullPointerException("Names can't be null"); 
		}
		else {
			this.names = names;
		}
	}
	
	void pushLevel(final long unitName) {
		top = new LevelCollection(top,unitName);
	}

	void pushLevel() {
		top = new LevelCollection(top);
	}
	
	void popLevel() {
		if (top.previous == null) {
			throw new IllegalStateException("Attempt to pop empty stack");
		}
		else {
			top = top.previous;
		}
	}
	
	int currentDepth() {
		LevelCollection	temp = top;
		int				depth = 0;
		
		while (temp != null) {
			temp = temp.previous;
			depth++;
		}
		return depth;
	}
	
	void addParameter(final VarDescriptor desc) {
		if (desc == null) {
			throw new NullPointerException("Var descriptor can't be null");
		}
		else if (hasNameAtTop(desc.getNameId())) {
			throw new IllegalArgumentException("Duplicate name in the current level");
		}
		else {
			top.vars.put(desc.getNameId(),desc);
		}
	}

	void addLocalVar(final VarDescriptor desc) {
		if (desc == null) {
			throw new NullPointerException("Var descriptor can't be null");
		}
		else if (hasNameAtTop(desc.getNameId())) {
			throw new IllegalArgumentException("Duplicate name in the current level");
		}
		else {
			top.vars.put(desc.getNameId(),desc);
		}
	}

	boolean hasName(final long nameId) {
		return hasName(top,nameId);
	}

	boolean hasNameAtTop(final long nameId) {
		return top.vars.contains(nameId);
	}
	
	VarDescriptor getName(final long nameId) {
		return getName(top,nameId);
	}

	private boolean hasName(final LevelCollection node, final long nameId) {
		if (node == null) {
			return false;
		}
		else if (node.vars.contains(nameId)) {
			return true;
		}
		else {
			return hasName(node.previous,nameId);
		}
	}
	
	private VarDescriptor getName(final LevelCollection node, final long nameId) {
		if (node == null) {
			return null;
		}
		else if (node.vars.contains(nameId)) {
			return node.vars.get(nameId);
		}
		else {
			return getName(node.previous,nameId);
		}
	}

	private static class LevelCollection {
		private final LevelCollection			previous;
		private final LongIdMap<VarDescriptor>	vars = new LongIdMap<VarDescriptor>(VarDescriptor.class);
		private final long						unitName;
		
		LevelCollection() {
			this.previous = null;
			this.unitName = -1;
		}

		LevelCollection(final LevelCollection previous) {
			this.previous = previous;
			this.unitName = -1;
		}

		LevelCollection(final LevelCollection previous, final long unitName) {
			this.previous = previous;
			this.unitName = unitName;
		}

		@Override
		public String toString() {
			if (unitName == -1) {
				return "LevelCollection [vars=" + vars + "]";
			}
			else {
				return "LevelCollection [vars=" + vars + ", unitName=" + unitName + "]";
			}
		}
	}
}
