/**
 * 
 */
package net.infopeers.restrant.commons.populate.clone.leaf;

import net.infopeers.restrant.commons.populate.clone.Convertor;
import net.infopeers.restrant.commons.populate.clone.Invoker;

public class AssignableConvertor implements Convertor {
	public Object convert(Invoker invoker, int level) {
		// eqŠÖŒW‚È‚ç‘ã“ü‰Â”\
		if (invoker.getSetterClass().isAssignableFrom(
				invoker.getGetterClass())) {
			return invoker.invokeGetter();
		}
		return null;
	}
}