package net.infopeers.restrant.kitchen.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class EntityUtils {
	
	private static Long schemaVersion = 1L;
	
	public static void setSchemeVersion(Long version){
		schemaVersion = version;
	} 

	public static void initialize(Entity entity) {
		initialize(entity, new Date());
	}

	public static void initialize(Entity entity, Date now) {
		entity.setCreatedAt(now);
		entity.setUpdatedAt(now);
		entity.setSchemeVersion(schemaVersion);
	}

	public static <T extends Entity> void sortByUpdatesDesc(List<T> finished) {
		Collections.sort(finished, new Comparator<T>() {
	
			@Override
			public int compare(T o1, T o2) {
				return (int)(o2.getUpdatedAt().getTime() - o1.getUpdatedAt().getTime());
			}
		});
	}
}
