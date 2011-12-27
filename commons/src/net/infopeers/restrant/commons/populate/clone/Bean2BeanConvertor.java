/**
 * 
 */
package net.infopeers.restrant.commons.populate.clone;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import net.infopeers.restrant.commons.populate.Populator;
import net.infopeers.restrant.commons.reflect.ReflectionUtils;


public class Bean2BeanConvertor implements Convertor {

	private final DefaultConvertor convertor;

	private final Class fromClass;

	private final Class toClass;

	private final TargetInstanceCreator tiCreator;

	private static final TargetInstanceCreator DEFAULT_CRETOR = new TargetInstanceCreator() {

		public Object getTargetInstance(Object from, Invoker invoker) {
			try {
				return invoker.getSetterClass().newInstance();
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

	};

	public Bean2BeanConvertor(DefaultConvertor traits, Class fromClass,
			Class toClass) {
		this.convertor = traits;
		this.fromClass = fromClass;
		this.toClass = toClass;
		this.tiCreator = DEFAULT_CRETOR;
	}

	public Bean2BeanConvertor(DefaultConvertor traits, Class fromClass,
			Class toClass, TargetInstanceCreator tiCreator) {
		this.convertor = traits;
		this.fromClass = fromClass;
		this.toClass = toClass;
		this.tiCreator = tiCreator;
	}

	public Object convert(Invoker invoker, int level) {
		if (!fromClass.isAssignableFrom(invoker.getGetterClass()))
			return null;

		Object from = invoker.invokeGetter();
		if (from == null)
			return null;

		Object target = invoker.getTargetObject();
		if (target == null) {
			return populate(from, new BeansInvoker(from, toClass), level);
		} else {
			return populate(from, new BeansInvoker(from, target), level);
		}
	}

	private Object populate(Object from, BeansInvoker invoker, int level) {
		BeanInfo finfo;
		try {
			finfo = Introspector.getBeanInfo(invoker.getGetterClass());
			PropertyDescriptor[] fdescs = finfo.getPropertyDescriptors();

			BeanInfo tinfo = Introspector.getBeanInfo(invoker.getSetterClass());
			PropertyDescriptor[] tdescs = tinfo.getPropertyDescriptors();

			Object to = invoker.getTargetObject();
			if (to == null)
				to = tiCreator.getTargetInstance(from, invoker);
			if (to == null)
				to = DEFAULT_CRETOR.getTargetInstance(from, invoker);

			for (int i = 0, end = fdescs.length; i < end; ++i) {
				PropertyDescriptor fdesc = fdescs[i];
				String name = fdesc.getName();

				PropertyDescriptor tdesc = ReflectionUtils
						.findDescriptorByName(tdescs, name);
				if (tdesc == null)
					continue;

				populate(from, fdesc, to, tdesc, level);
			}
			return to;

		} catch (IntrospectionException e) {
			throw new RuntimeException(e);
		}
	}

	private void populate(Object from, PropertyDescriptor fdesc, Object to,
			PropertyDescriptor tdesc, int level) {
		try {
			Method getter = fdesc.getReadMethod();
			if (getter == null)
				return;

			Method setter = tdesc.getWriteMethod();
			if (setter == null)
				return;

			MethodInvoker invoker = new MethodInvoker(from, fdesc, to, tdesc);

			Object arg = convertor.convert(invoker, level);
			if (arg == Populator.INVALID_OBJECT)
				return;
			invoker.invokeSetter(arg);
			return;

		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}