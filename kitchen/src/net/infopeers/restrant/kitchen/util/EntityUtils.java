package net.infopeers.restrant.kitchen.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Id;

import org.apache.commons.lang.StringUtils;

public class EntityUtils {

	private static Long schemaVersion = 1L;

	public static void setSchemeVersion(Long version) {
		schemaVersion = version;
	}

	public static void initialize(Entity entity) {
		initialize(entity, false);
	}

	public static void initialize(Entity entity, Date now) {
		initialize(entity, now, false);
	}

	public static void initialize(Entity entity, boolean autoId) {
		initialize(entity, new Date(), autoId);
	}

	public static void initialize(Entity entity, Date now, boolean autoId) {
		entity.setCreatedAt(now);
		entity.setUpdatedAt(now);
		entity.setSchemeVersion(schemaVersion);

		if (autoId) {
			setAutoId(entity);
		}
	}

	public static void setAutoId(Object o) {
		Class<?> cls = o.getClass();
		for (Field f : cls.getDeclaredFields()) {
			Id id = f.getAnnotation(Id.class);
			if (id == null)
				continue;

			String capName = StringUtils.capitalize(f.getName());
			String getterName = "get" + capName;
			Method getter = getMethod(cls, getterName, new Class<?>[]{});
			
			try {
				if(getter.invoke(o, new Object[]{}) != null) return;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			String setterName = "set" + capName;
			Method setter = getMethod(cls, setterName, String.class);
			try {
				setter.invoke(o, UUID.randomUUID().toString());
				return;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		throw new IllegalArgumentException(cls
				+ "'s field has not @Id annotation.");
	}

	private static Method getMethod(Class<?> cls, String methodName, Class<?>...paramTypes) {
		try {
			return cls.getMethod(methodName, paramTypes);
		} catch (SecurityException e) {
			throw new RuntimeException(cls + "#" + methodName + " cannot get.",
					e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(cls + "#" + methodName + " not found.",
					e);
		}
	}

	public static <T extends Entity> void sortByUpdatesDesc(List<T> finished) {
		Collections.sort(finished, new Comparator<T>() {

			@Override
			public int compare(T o1, T o2) {
				return (int) (o2.getUpdatedAt().getTime() - o1.getUpdatedAt()
						.getTime());
			}
		});
	}
}
