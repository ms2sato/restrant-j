package net.infopeers.restrant.commons.reflect;

import java.beans.PropertyDescriptor;

public class ReflectionUtils {

	public static PropertyDescriptor findDescriptorByName(PropertyDescriptor[] pds,
			String name) {
	
		for (int i = 0, end = pds.length; i < end; ++i) {
			if (pds[i].getName().equals(name)) {
				return pds[i];
			}
		}
	
		return null;
	}

}
