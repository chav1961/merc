package chav1961.merc.lang.merc;

import java.util.Arrays;

import chav1961.merc.lang.merc.interfaces.VarDescriptor;
import chav1961.purelib.basic.interfaces.SyntaxTreeInterface;

class VarDescriptorImpl implements VarDescriptor {
	private final long				nameId;
	private final Class<?>			nameType;
	private final boolean			isArray, isReadOnly, isVar, isMethod, isReference;
	private final int				howManyDimensions, allocated;
	private final VarDescriptor[] 	content;
	
	public VarDescriptorImpl(final int allocated, final long nameId, final Class<?> nameType, final boolean isReadOnly, final boolean isVar, final int howManyDimensions, final VarDescriptor... content) {
		this.allocated = allocated;
		this.nameId = nameId;
		this.nameType = nameType;
		this.isArray = howManyDimensions > 0;
		this.isReadOnly = isReadOnly;
		this.isVar = isVar;
		this.isMethod = false;
		this.isReference = false;
		this.howManyDimensions = howManyDimensions;
		this.content = content;
	}

	public VarDescriptorImpl(final int allocated, final long nameId, final VarDescriptor[] parameters, final Class<?> nameType, final int howManyDimensions) {
		this.allocated = allocated;
		this.nameId = nameId;
		this.nameType = nameType;
		this.isArray = howManyDimensions > 0;
		this.isReadOnly = false;
		this.isVar = false;
		this.isMethod = true;
		this.isReference = false;
		this.howManyDimensions = howManyDimensions;
		this.content = parameters;
	}

	public VarDescriptorImpl(final int allocated, final long nameId) {
		this.allocated = allocated;
		this.nameId = nameId;
		this.nameType = null;
		this.isArray = false;
		this.isReadOnly = false;
		this.isVar = false;
		this.isMethod = false;
		this.isReference = true;
		this.howManyDimensions = 0;
		this.content = null;
	}
	
	@Override
	public long getNameId() {
		return nameId;
	}

	@Override
	public VarDescriptor[] getParameters() {
		if (!isMethod()) {
			throw new IllegalStateException("Attempt to get parameters when entity is not a method!");
		}
		else {
			return this.content;
		}
	}

	@Override
	public Class<?> getNameType() {
		return nameType;
	}

	@Override
	public boolean isReference() {
		return isReference;
	}
	
	@Override
	public boolean isArray() {
		return isArray;
	}

	@Override
	public boolean isReadOnly() {
		return isReadOnly;
	}

	@Override
	public boolean isVar() {
		return isVar;
	}

	@Override
	public boolean isMethod() {
		return isMethod;
	}
	
	@Override
	public int howManyDimensions() {
		return howManyDimensions;
	}

	@Override
	public VarDescriptor[] contentFields() {
		return this.content;
	}

	@Override
	public String toString(final SyntaxTreeInterface<?> names) {
		final StringBuilder	sb = new StringBuilder();
		
		if (isVar()) {
			sb.append("var ");
		}
		sb.append(names.getName(nameId));
		if (isArray()) {
			sb.append('[');
			for (int index = 1; index < howManyDimensions; index++) {
				sb.append(',');
			}
			sb.append(']');
		}
		if (isReference) {
			sb.append('@');
		}
		else if (!isMethod()) {
			char	prefix = '{';
			
			for (VarDescriptor item : contentFields()) {
				sb.append(prefix).append(((VarDescriptorImpl)item).toString(names));
				prefix = ';';
			}
			if (prefix == ';') {
				sb.append('}');
			}
			sb.append(':').append(getNameType().getCanonicalName());
		}
		else {
			char	prefix = '(';
			
			for (VarDescriptor item : getParameters()) {
				sb.append(prefix).append(item.toString(names));
				prefix = ';';
			}
			if (prefix == ';') {
				sb.append(')');
			}
			else {
				sb.append("()");
			}
			sb.append(':').append(getNameType().getCanonicalName());
		}
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return "VarDescriptorImpl [nameId=" + nameId + ", nameType=" + nameType + ", isArray=" + isArray
				+ ", isReadOnly=" + isReadOnly + ", isVar=" + isVar + ", isMethod=" + isMethod + ", isReference="
				+ isReference + ", howManyDimensions=" + howManyDimensions + ", content=" + Arrays.toString(content)
				+ "]";
	}
}