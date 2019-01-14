package chav1961.merc.lang.merc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import chav1961.merc.api.interfaces.front.EntityClassDescription;
import chav1961.merc.api.interfaces.front.MerLan;
import chav1961.merc.lang.merc.interfaces.VarDescriptor;
import chav1961.merc.lang.merc.keepers.AreaKeeper;
import chav1961.merc.lang.merc.keepers.BooleanKeeper;
import chav1961.merc.lang.merc.keepers.DoubleKeeper;
import chav1961.merc.lang.merc.keepers.EntityKeeper;
import chav1961.merc.lang.merc.keepers.LongKeeper;
import chav1961.merc.lang.merc.keepers.PointKeeper;
import chav1961.merc.lang.merc.keepers.SizeKeeper;
import chav1961.merc.lang.merc.keepers.StringKeeper;
import chav1961.merc.lang.merc.keepers.TrackKeeper;
import chav1961.purelib.basic.interfaces.SyntaxTreeInterface;

public class MercClassRepo {
	private static final Class<?>[]				BUILTIN = {AreaKeeper.class,BooleanKeeper.class,DoubleKeeper.class,EntityKeeper.class,LongKeeper.class,
														PointKeeper.class, SizeKeeper.class, StringKeeper.class, TrackKeeper.class
													};

	private final SyntaxTreeInterface<?>		names = null;
	private final Map<String,VarDescriptor>		declaredNames = new HashMap<>();
	private final Map<Class<?>,VarDescriptor>	declaredClasses = new HashMap<>();
	
	MercClassRepo(final int minLevel) {
		VarDescriptor	desc;
		
		for (Class<?> item : BUILTIN) {
			desc = prepareClass(minLevel,names.placeName(item.getSimpleName(),null),item);
			declaredNames.put(item.getSimpleName(),desc);
			declaredClasses.put(desc.getNameType(),desc);
		}
		for (EntityClassDescription<? extends Enum<?>> item :  ServiceLoader.load(EntityClassDescription.class)) {
			if (item.getMinimalLevel2Use() >= minLevel) {
				final String	name;
				final long		nameId;
				
				if (item.isSingleton()) {
					nameId = names.placeOrChangeName(name = item.getSingletonName(),null);
				}
				else {
					nameId = names.placeOrChangeName(name = item.getEntitySubclass(),null);
				}
				desc = prepareClass(minLevel,nameId,item.getClass());
				declaredNames.put(name,desc);
				declaredClasses.put(desc.getNameType(),desc);
			}
		}
	}
	
	VarDescriptor byClass(final Class<?> clazz) throws NullPointerException {
		if (clazz == null) {
			throw new NullPointerException("Clazz can't be null"); 
		}
		else {
			return declaredClasses.get(clazz);
		}
	}

	VarDescriptor byName(final String name) throws IllegalArgumentException {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Name can't be null or empty"); 
		}
		else {
			return declaredNames.get(name);
		}
	}
	
	VarDescriptor prepareClass(final int minLevel, final long nameId, final Class<?> clazz) {
		final Class<?>	content;
		int				dimensionCount = 0;
		
		if (clazz.isArray()) {
			Class<?> temp = clazz;
			
			while (temp.isArray()) {
				temp.getComponentType();
				dimensionCount++;				
			}
			content = temp;
		}
		else {
			content = clazz;
		}
		
		final List<VarDescriptor>	children = new ArrayList<>();
		
		for (Field f : content.getFields()) {
			if (f.isAnnotationPresent(MerLan.class) && f.getAnnotation(MerLan.class).accessibleFrom() >= minLevel) {
				children.add(prepareClass(minLevel,names.placeOrChangeName(f.getName(),null),f.getType()));
			}
		}
		for (Method m : content.getMethods()) {
			if (m.isAnnotationPresent(MerLan.class) && m.getAnnotation(MerLan.class).accessibleFrom() >= minLevel) {
				final List<VarDescriptor>	parms = new ArrayList<>();
				
				parms.add(prepareClass(minLevel,names.placeOrChangeName(m.getName(),null),m.getReturnType()));
				for (Parameter p : m.getParameters()) {
					parms.add(prepareClass(minLevel,names.placeOrChangeName(p.getName(),null),p.getType()));
				}
			}
		}
		final VarDescriptor[]		forChildren = children.toArray(new VarDescriptor[children.size()]);
		
		return new VarDescriptorImpl(nameId,content,false,true,dimensionCount,forChildren);
	}
	
	private static class VarDescriptorImpl implements VarDescriptor {
		private final long				nameId;
		private final Class<?>			nameType;
		private final boolean			isArray, isReadOnly, isVar, isMethod;
		private final int				howManyDimensions;
		private final VarDescriptor[] 	content;
		
		public VarDescriptorImpl(final long nameId, final Class<?> nameType, final boolean isReadOnly, final boolean isVar, final int howManyDimensions, final VarDescriptor[] content) {
			this.nameId = nameId;
			this.nameType = nameType;
			this.isArray = howManyDimensions > 0;
			this.isReadOnly = isReadOnly;
			this.isVar = isVar;
			this.isMethod = false;
			this.howManyDimensions = howManyDimensions;
			this.content = content;
		}

		public VarDescriptorImpl(final long nameId, final VarDescriptor[] parameters, final Class<?> nameType, final int howManyDimensions) {
			this.nameId = nameId;
			this.nameType = nameType;
			this.isArray = howManyDimensions > 0;
			this.isReadOnly = false;
			this.isVar = false;
			this.isMethod = false;
			this.howManyDimensions = howManyDimensions;
			this.content = parameters;
		}
		
		@Override
		public long getNameId() {
			return nameId;
		}

		@Override
		public VarDescriptor[] getParameters() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Class<?> getNameType() {
			return nameType;
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
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public int howManyDimensions() {
			return howManyDimensions;
		}

		@Override
		public VarDescriptor[] contentFields() {
			return content;
		}

		@Override
		public String toString() {
			return "VarDescriptorImpl [nameId=" + nameId + ", nameType=" + nameType + ", isArray=" + isArray
					+ ", isReadOnly=" + isReadOnly + ", isVar=" + isVar + ", howManyDimensions=" + howManyDimensions
					+ ", content=" + Arrays.toString(content) + "]";
		}

	}
}
