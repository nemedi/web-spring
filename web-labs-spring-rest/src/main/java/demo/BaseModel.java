package demo;

import static java.util.Map.entry;
import static java.util.stream.Collectors.toMap;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;

public interface BaseModel {

	default Map<String, Object> sparseFields(String[] fields) {
		return Arrays.stream(getClass().getDeclaredFields())
			.filter(field -> Arrays.asList(fields).contains(field.getName()))
			.map(field -> entry(field.getName(), getFieldValue(field, this)))
			.filter(entry -> entry.getValue() != null)
			.collect(toMap(Entry::getKey, Entry::getValue));
	}
	
	@SuppressWarnings("unchecked")
	static BiFunction<Object, Object, Integer> sort(String field) {
		return (first, second) -> {
			try {
				if (field == null || field.isEmpty()) {
					return 0;
				}
				Object firstValue = first instanceof Map
						? ((Map<Object, Object>) first).get(field)
						: getFieldValue(first.getClass().getDeclaredField(field), first);
				Object secondValue = second instanceof Map
						? ((Map<Object, Object>) second).get(field)
						: getFieldValue(second.getClass().getDeclaredField(field), second);
				return firstValue instanceof Comparable
						? ((Comparable<Object>) firstValue).compareTo(secondValue)
						: firstValue.toString().compareTo(secondValue.toString());
			} catch (NoSuchFieldException | SecurityException e) {
				return 0;
			}
		};
	}
	
	private static Object getFieldValue(Field field, Object object) {
		try {
			field.setAccessible(true);
			return field.get(object);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return null;
		}
	}
	
}
	