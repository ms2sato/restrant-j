/**
 * 
 */
package net.infopeers.restrant.commons.populate.clone.leaf;

import net.infopeers.restrant.commons.populate.clone.Convertor;
import net.infopeers.restrant.commons.populate.clone.Invoker;

public class AssignableConvertor implements Convertor {
	public Object convert(Invoker invoker, int level) {
		// 親子関係なら代入可能
		if (invoker.getSetterClass().isAssignableFrom(
				invoker.getGetterClass())) {
			return invoker.invokeGetter();
		}
		return null;
	}
}