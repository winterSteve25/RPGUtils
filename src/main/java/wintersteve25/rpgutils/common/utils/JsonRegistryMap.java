package wintersteve25.rpgutils.common.utils;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import wintersteve25.rpgutils.RPGUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JsonRegistryMap<T> {

    private final BiMap<T, String> map;

    public JsonRegistryMap(Class<?> container, Class<T> objects) {
        map = HashBiMap.create();
        for (Field field : container.getFields()) {
            int modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers) && field.getType() == objects) {
                try {
                    map.put((T) field.get(null), toCamelCase(field.getName()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        map.entrySet().forEach(RPGUtils.LOGGER::info);
    }

    public String get(T key) {
        return map.get(key);
    }

    public T get(String key) {
        return map.inverse().get(key);
    }

    public Set<T> objectSet() {
        return map.keySet();
    }

    public Set<String> fieldNameSet() {
        return map.values();
    }

    public Set<Entry> entrySet() {
        return map.entrySet().stream().map(Entry::new).collect(Collectors.toSet());
    }

    public class Entry {

        private final String fieldName;

        private final T object;

        private Entry(String fieldName, T object) {
            this.fieldName = fieldName;
            this.object = object;
        }

        private Entry(Map.Entry<T, String> entry) {
            this(entry.getValue(), entry.getKey());
        }

        public String getFieldName() {
            return fieldName;
        }

        public T getObject() {
            return object;
        }
    }

    private static String toCamelCase(String s) {
        StringBuilder result = new StringBuilder();
        boolean nextUpper = false;
        for (char c : s.toCharArray()) {
            if (c == '_') {
                nextUpper = true;
            } else if (nextUpper) {
                result.append(String.valueOf(c).toUpperCase(Locale.ROOT));
                nextUpper = false;
            } else {
                result.append(String.valueOf(c).toLowerCase(Locale.ROOT));
            }
        }
        return result.toString();
    }
}
