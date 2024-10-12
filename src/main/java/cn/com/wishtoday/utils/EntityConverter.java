package cn.com.wishtoday.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class EntityConverter {
    private static final Map<Class<?>, Constructor<?>> constructorCache = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Map<String, Method>> setterMethodCache = new ConcurrentHashMap<>();

    public static <T> List<T> convertListToEntity(List<Map<String, Object>> sourceList, Class<T> clazz) {
        // 初始化缓存
        initCaches(clazz);
        return sourceList.stream().map(source -> {
            try {
                T target = createInstance(clazz);
                for (Map.Entry<String, Object> entry : source.entrySet()) {
                    String fieldName = ConvertUtil.convertFieldName(entry.getKey());
                    Object fieldValue = entry.getValue();
                    setFieldValue(target, fieldName, fieldValue, clazz);
                }
                return target;
            } catch (Exception e) {
                throw new RuntimeException("Error converting map to object", e);
            }
        }).collect(Collectors.toList());
    }

    private static <T> void initCaches(Class<T> clazz) {
        if (!constructorCache.containsKey(clazz)) {
            try {
                Constructor<T> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                constructorCache.put(clazz, constructor);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("No default constructor found for " + clazz.getName(), e);
            }
        }

        if (!setterMethodCache.containsKey(clazz)) {
            Map<String, Method> methodMap = new ConcurrentHashMap<>();
            for (Method method : clazz.getMethods()) {
                if (method.getName().startsWith("set") && method.getParameterCount() == 1) {
                    String fieldName = method.getName().substring(3);
                    fieldName = Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);
                    methodMap.put(fieldName, method);
                }
            }
            setterMethodCache.put(clazz, methodMap);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T createInstance(Class<T> clazz) throws Exception {
        return (T) constructorCache.get(clazz).newInstance();
    }

    /**
     *
     * @param target 缓存实体类
     * @param fieldName 去掉下划线后的key
     * @param value 当前字段对应的value值
     * @param clazz 要转换的实体类
     * @param <T> 实体类通配符
     * @throws Exception 异常
     */
    private static <T> void setFieldValue(T target, String fieldName, Object value, Class<T> clazz) throws Exception {
        Method method = setterMethodCache.get(clazz).get(fieldName);
        if (method != null) {
            Class<?> parameterType = method.getParameterTypes()[0];
            if (parameterType.isPrimitive() && value instanceof Number) {
                // Handle primitive type conversions, e.g., Integer to int
                if (parameterType == int.class) {
                    method.invoke(target, ((Number) value).intValue());
                } else if (parameterType == long.class) {
                    method.invoke(target, ((Number) value).longValue());
                } else if (parameterType == double.class) {
                    method.invoke(target, ((Number) value).doubleValue());
                } else if (parameterType == float.class) {
                    method.invoke(target, ((Number) value).floatValue());
                } else if (parameterType == boolean.class) {
                    method.invoke(target, Boolean.parseBoolean(value.toString()));
                }
            } else {
                method.invoke(target, value);
            }
        }
    }
}
