/**
 * 
 */
package net.infopeers.restrant.commons.populate.clone.leaf;

import java.util.Collection;
import java.util.Iterator;

import net.infopeers.restrant.commons.populate.clone.DefaultConvertor;
import net.infopeers.restrant.commons.populate.clone.Invoker;


public class Collection2CollectionConvertor extends AbstractRecursiveConvertor {
	/**
	 * @param convertor
	 */
	public Collection2CollectionConvertor(DefaultConvertor convertor) {
		super(convertor);
	}

	public Object convert(Invoker invoker, int level) {
		if (Collection.class.isAssignableFrom(invoker.getGetterClass())) {
			if (Collection.class.isAssignableFrom(invoker.getSetterClass())) {
				Object arg = invoker.invokeGetter();
				if (arg == null)
					return null;

				Collection from = (Collection) arg;
				Collection to = createCollection(invoker.getSetterClass());

				for (Iterator itr = from.iterator(); itr.hasNext();) {
					to.add(recur(itr.next(), level));
				}
				return to;
			}
		}
		return null;
	}
}