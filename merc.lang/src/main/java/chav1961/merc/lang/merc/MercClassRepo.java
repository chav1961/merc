package chav1961.merc.lang.merc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

import chav1961.merc.api.AreaKeeper;
import chav1961.merc.api.BooleanKeeper;
import chav1961.merc.api.DoubleKeeper;
import chav1961.merc.api.EntityKeeper;
import chav1961.merc.api.LongKeeper;
import chav1961.merc.api.PointKeeper;
import chav1961.merc.api.SizeKeeper;
import chav1961.merc.api.StringKeeper;
import chav1961.merc.api.TrackKeeper;
import chav1961.merc.api.interfaces.front.EntityClassDescription;
import chav1961.merc.api.interfaces.front.MerLan;
import chav1961.merc.lang.merc.interfaces.VarDescriptor;
import chav1961.purelib.basic.interfaces.SyntaxTreeInterface;

public class MercClassRepo {
	static final Class<?>[]						BUILTIN = {AreaKeeper.class,BooleanKeeper.class,DoubleKeeper.class,EntityKeeper.class,LongKeeper.class,
														PointKeeper.class, SizeKeeper.class, StringKeeper.class, TrackKeeper.class
													};

	private final SyntaxTreeInterface<?>		names;
	private final Map<String,VarDescriptor>		declaredNames = new HashMap<>();
	private final Map<Class<?>,VarDescriptor>	declaredClasses = new HashMap<>();
	
	MercClassRepo(final SyntaxTreeInterface<?> names, final int minLevel) {
		if (names == null) {
			throw new NullPointerException("Names can't be null"); 
		}
		else if (minLevel < 0) {
			throw new NullPointerException("Min level ["+minLevel+"] can't be negative"); 
		}
		else {
			this.names = names;
			
			final Set<Long>	parsed = new HashSet<>();
			VarDescriptor	desc;
			
			for (Class<?> item : BUILTIN) {
				final String	name = item.getSimpleName();
				
				desc = prepareClass(minLevel,names.placeName(name,null),item,parsed);
				declaredNames.put(name,desc);
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
					desc = prepareClass(minLevel,nameId,item.getClass(),parsed);
					declaredNames.put(name,desc);
					declaredClasses.put(desc.getNameType(),desc);
				}
			}
		}
	}

	int size() {
		return declaredClasses.size();
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
	
	VarDescriptor prepareClass(final int minLevel, final long nameId, final Class<?> clazz, final Set<Long> parsed) {
		if (!parsed.contains(nameId)) {	// Protection against recursion in definitions
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
			parsed.add(nameId);
			
			final List<VarDescriptor>	children = new ArrayList<>();
			
			for (Field f : content.getFields()) {
				if (f.isAnnotationPresent(MerLan.class) && f.getAnnotation(MerLan.class).accessibleFrom() >= minLevel) {
					children.add(prepareClass(minLevel,names.placeOrChangeName(f.getName(),null),f.getType(),parsed));
				}
			}
			for (Method m : content.getMethods()) {
				if (m.isAnnotationPresent(MerLan.class) && m.getAnnotation(MerLan.class).accessibleFrom() >= minLevel) {
					final List<VarDescriptor>	parms = new ArrayList<>();
					
					for (Class<?> p : m.getParameterTypes()) {
						final long	classNameId = names.placeOrChangeName(p.getSimpleName(),null);
						
						if (!parsed.contains(classNameId)) {
							parsed.add(classNameId);
							parms.add(prepareClass(minLevel,classNameId,p,parsed));
						}
						else {
							return new VarDescriptorImpl(classNameId);
						}
					}
					children.add(new VarDescriptorImpl(names.placeOrChangeName(m.getName(),null),parms.toArray(new VarDescriptor[parms.size()]),m.getReturnType(),0));
				}
			}
			final VarDescriptor[]		forChildren = children.toArray(new VarDescriptor[children.size()]);
			
			return new VarDescriptorImpl(nameId,content,false,true,dimensionCount,forChildren);
		}
		else {
			return new VarDescriptorImpl(nameId);
		}
	}

	@Override
	public String toString() {
		return "MercClassRepo:\ndeclaredNames=" + declaredNames + "\ndeclaredClasses=" + declaredClasses + "\n";
	}
}
