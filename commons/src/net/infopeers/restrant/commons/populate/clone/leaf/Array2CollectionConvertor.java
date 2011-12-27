/**
 * 
 */
package net.infopeers.restrant.commons.populate.clone.leaf;

import java.util.Collection;
import java.util.List;

import net.infopeers.restrant.commons.populate.clone.DefaultConvertor;
import net.infopeers.restrant.commons.populate.clone.Invoker;


public class Array2CollectionConvertor extends AbstractRecursiveConvertor {

	public Array2CollectionConvertor(DefaultConvertor parent) {
		super(parent);
	}

	public Object convert(Invoker invoker, int level) {
		if (invoker.getGetterClass().isArray()) {
			if (Collection.class.isAssignableFrom(invoker.getSetterClass())) {

				Object arg = invoker.invokeGetter();
				if (arg == null)
					return null;

				Object[] from = (Object[]) arg;

				Collection toCol = (Collection) invoker.getTargetObject();
				if (toCol == null) {
					toCol = createCollection(invoker.getSetterClass());
				}

				if (toCol instanceof List) {
					populate2List(from, level, toCol);
				} else {
					populate2Collection(from, level, toCol);
				}
				return toCol;
			}
		}
		return null;
	}

	protected void populate2Collection(Object[] from, int level, Collection toCol) {
		for (int i = 0, end = from.length; i < end; ++i) {
			toCol.add(recur(from[i], level));
		}
	}

	protected void populate2List(Object[] from, int level, Collection toCol) {
		List toList = (List) toCol;
		for (int i = 0, end = from.length; i < end; ++i) {
			Object t = null;
			if (i < toList.size())
				t = toList.get(i);

			if (t == null) {
				toCol.add(recur(from[i], level));
			} else {
				recur(from[i], level, t);
			}
		}
	}
}