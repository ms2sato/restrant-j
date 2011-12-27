/**
 * 
 */
package net.infopeers.restrant.commons.populate.clone.leaf;

import net.infopeers.restrant.commons.populate.clone.Convertor;
import net.infopeers.restrant.commons.populate.clone.Invoker;

public class AssignableConvertor implements Convertor {
	public Object convert(Invoker invoker, int level) {
		// �e�q�֌W�Ȃ����\
		if (invoker.getSetterClass().isAssignableFrom(
				invoker.getGetterClass())) {
			return invoker.invokeGetter();
		}
		return null;
	}
}