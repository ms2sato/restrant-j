/**
 * 
 */
package net.infopeers.restrant.commons.populate.clone.leaf;

import net.infopeers.restrant.commons.populate.clone.Convertor;
import net.infopeers.restrant.commons.populate.clone.Invoker;

public class StringConvertor implements Convertor {
	public Object convert(Invoker invoker, int level) {
		if (invoker.getSetterClass().equals(String.class)) {
			// �I�u�W�F�N�g�͕�����ɕϊ��ł���B
			Object arg = invoker.invokeGetter();
			if (arg == null)
				return null;
			return arg.toString();
		}
		return null;
	}
}