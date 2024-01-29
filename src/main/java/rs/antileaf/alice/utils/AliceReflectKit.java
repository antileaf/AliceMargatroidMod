package rs.antileaf.alice.utils;

import java.lang.reflect.Field;
import java.util.HashMap;

public abstract class AliceReflectKit {
	static HashMap<Class<?>, HashMap<String, Field>> fieldCache = new HashMap<>();
	
	private static void updateCache(Class<?> clazz) {
		for (Class<?> clz = clazz; clz != null && !fieldCache.containsKey(clz); clz = clz.getSuperclass()) {
			Field[] fields = clz.getDeclaredFields();
			HashMap<String, Field> map = new HashMap<>();
			
			for (Field field : fields) {
				field.setAccessible(true);
				map.put(field.getName(), field);
			}
			
			fieldCache.put(clz, map);
		}
	}
	
	// To get the field of a specific instance:
	// AliceReflectKit.getField(Clazz, fieldName).get(obj)
	public static Field getField(Class<?> clazz, String fieldName) {
		AliceReflectKit.updateCache(clazz);
		
		for (Class<?> clz = clazz; clz != null; clz = clz.getSuperclass()) {
			if (fieldCache.get(clz).containsKey(fieldName))
				return fieldCache.get(clz).get(fieldName);
		}
		
		return null;
	}
	
	public static boolean hasField(Class<?> clazz, String fieldName) {
		return AliceReflectKit.getField(clazz, fieldName) != null;
	}
}
