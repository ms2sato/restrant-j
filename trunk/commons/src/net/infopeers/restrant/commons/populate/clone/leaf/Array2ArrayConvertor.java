/**
 * 
 */
package net.infopeers.restrant.commons.populate.clone.leaf;

import java.lang.reflect.Array;

import net.infopeers.restrant.commons.populate.clone.DefaultConvertor;
import net.infopeers.restrant.commons.populate.clone.Invoker;


public class Array2ArrayConvertor extends AbstractRecursiveConvertor {
	public Array2ArrayConvertor(DefaultConvertor parent) {
		super(parent);
	}

	public Object convert(Invoker invoker, int level) {
		if (invoker.getGetterClass().isArray()) {
			if (invoker.getSetterClass().isArray()) {
				Object arg = invoker.invokeGetter();
				if (arg == null)
					return null;

				Object[] from = (Object[]) arg;
				Object[] to = (Object[]) invoker.getTargetObject();
				if (to == null) {

					Class componentType = invoker.getSetterClass()
							.getComponentType();
					to = (Object[]) Array.newInstance(componentType,
							from.length);

				}

				for (int i = 0, end = from.length; i < end; ++i) {
					Object t = null;
					if (i < to.length)
						t = to[i];

					if(t == null) to[i] = recur(from[i], level);
					else recur(from[i], level, t);
				}

				return to;
			}
		}
		return null;
	}

}