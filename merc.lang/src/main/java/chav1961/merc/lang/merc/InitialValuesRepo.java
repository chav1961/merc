package chav1961.merc.lang.merc;

import java.util.ArrayList;
import java.util.List;

import chav1961.merc.lang.merc.SyntaxTreeNode.SyntaxTreeNodeType;

class InitialValuesRepo {
	private final List<ValuesContainer>	repo = new ArrayList<>();
	private final int	repoPrefix;
	private int			uniqueCounter = 0;
	
	InitialValuesRepo(final int repoPrefix) {
		this.repoPrefix = repoPrefix;
	}
	
	@FunctionalInterface
	interface WalkCallback {
		void process(String identifier, SyntaxTreeNode node, Class<?> clazz, Object value);
	}
	
	boolean isValuePresent(final SyntaxTreeNode node) {
		for (ValuesContainer item : repo) {
			if (item.isEquals(node)) {
				return true;
			}
		}
		return false;
	}
	
	void addValue(final SyntaxTreeNode node, final Class<?> clazz) {
		for (ValuesContainer item : repo) {
			if (item.isEquals(node)) {
				item.count++;
				return;
			}
		}
		repo.add(new ValuesContainer(clazz.getSimpleName()+"_"+repoPrefix+"_"+uniqueCounter++, node, clazz));
	}

	void removeValue(final SyntaxTreeNode node) {
		for (ValuesContainer item : repo) {
			if (item.isEquals(node)) {
				item.count--;
				return;
			}
		}
	}

	<T> Class<T> getClass(final SyntaxTreeNode node) {
		for (ValuesContainer item : repo) {
			if (item.isEquals(node)) {
				return (Class<T>) item.clazz;
			}
		}
		return null;
	}
	
	<T> T getValue(final SyntaxTreeNode node) {
		for (ValuesContainer item : repo) {
			if (item.isEquals(node)) {
				return null;
			}
		}
		return null;
	}

	String getIdentifier(final SyntaxTreeNode node) {
		for (ValuesContainer item : repo) {
			if (item.isEquals(node)) {
				return item.identifier;
			}
		}
		return null;
	}
	
	void walk(final WalkCallback callback) {
		for (ValuesContainer item : repo) {
			if (item.count > 0) {
				callback.process(item.identifier,item.node,item.clazz,item.value);
			}
		}
	}

	private static <T> T buildByClassAndNode(final SyntaxTreeNode node, final Class<T> clazz) {
		if (node.getType() == SyntaxTreeNodeType.Conversion) {
			return buildByClassAndNode((LexemaSubtype)node.cargo,node.children);
		}
		else if (node.getType() == SyntaxTreeNodeType.Vartype) {
			return buildByClassAndNode(null,node.children);
		}
		else {
			throw new UnsupportedOperationException("Node type ["+node.getType()+"] is not implemented yet");
		}
	}

	private static <T> T buildByClassAndNode(final LexemaSubtype subtype, final SyntaxTreeNode[] initials) {
		switch (subtype) {
			case Area	:
				break;
			case Point	:
				break;
			case Str	:
				break;
			case Size	:
				break;
			case Track	:
				break;
			default : throw new UnsupportedOperationException("Subtype ["+subtype+"] is not implemented yet");
		}
		return null;
	}

	
	private static class ValuesContainer {
		final String			identifier;
		final SyntaxTreeNode	node;
		final Class<?>			clazz;
		int						count = 0;
		Object					value = null;
		
		ValuesContainer(final String identifier, final SyntaxTreeNode node, final Class<?> clazz) {
			this.identifier = identifier;
			this.node = node;
			this.clazz = clazz;
		}
		
		boolean isEquals(final SyntaxTreeNode another) {
			return true;
		}

		@Override
		public String toString() {
			return "ValuesContainer [identifier=" + identifier + ", node=" + node + ", clazz=" + clazz + ", count=" + count + ", value=" + value + "]";
		}
	}
	
}
