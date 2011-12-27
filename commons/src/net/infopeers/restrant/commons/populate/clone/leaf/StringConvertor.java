/**
 * 
 */
package net.infopeers.restrant.commons.populate.clone.leaf;

import net.infopeers.restrant.commons.populate.clone.Convertor;
import net.infopeers.restrant.commons.populate.clone.Invoker;

public class StringConvertor implements Convertor {
	public Object convert(Invoker invoker, int level) {
		if (invoker.getSetterClass().equals(String.class)) {
			// オブジェクトは文字列に変換できる。
			Object arg = invoker.invokeGetter();
			if (arg == null)
				return null;
			return arg.toString();
		}
		return null;
	}
}