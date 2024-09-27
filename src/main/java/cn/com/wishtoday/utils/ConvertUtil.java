package cn.com.wishtoday.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConvertUtil {
    private static final Logger log = LoggerFactory.getLogger(ConvertUtil.class);

    /**
     * String类型转换成Base64
     * @param str 参数1
     * @return 返回值
     */
    public static String StringConvertBase64(String str){
        if(!str.isEmpty()){
            Base64.Encoder encoder = Base64.getEncoder();
            return encoder.encodeToString(str.getBytes());
        }
        return str;
    }

    /**
     * Base64转换成String类型字符串
     * @param base64 参数1
     * @return 返回值
     */
    public static String Base64ConvertString(String base64){
        Base64.Decoder decoder = Base64.getDecoder();
        String str = null;
        try {
            str = new String(decoder.decode(base64.getBytes()), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("base64转换异常类型：{}", e.getClass().getName());
            log.error("base64转换异常消息：{}", e.getMessage());
        }
        return str;
    }

    /**
     * 将List<Map<String, Object>>转换为目标类型的列表
     *
     * @param sourceList 源List<Map<String, Object>>
     * @param clazz 目标类的Class对象
     * @param <T> 目标类型
     * @return 转换后的目标类型列表
     */
    public static <T> List<T> convertListToEntity(List<Map<String, Object>> sourceList, Class<T> clazz) {
        return sourceList.stream().map(source -> {
            try {
                T target = clazz.getDeclaredConstructor().newInstance();
                for (Map.Entry<String, Object> entry : source.entrySet()) {
                    String fieldName = convertFieldName(entry.getKey());
                    Object fieldValue = entry.getValue();

                    // 使用反射找到并调用setter方法
                    Method method = findSetterMethod(clazz, fieldName, fieldValue.getClass());
                    if (method != null) {
                        method.invoke(target, fieldValue);
                    }
                }
                return target;
            } catch (Exception e) {
                throw new RuntimeException("Error converting map to object", e);
            }
        }).collect(Collectors.toList());
    }

    /**
     * 将下划线分隔的字段名转换为驼峰命名
     * 备注（这种方法性能能高）
     * @param underscoreName 下划线分隔的字段名
     * @return 驼峰命名的字段名
     */
    private static String convertFieldName(String underscoreName) {
        int len = underscoreName.length();
        StringBuilder sBuilder = new StringBuilder(len);// 预分配容量
        boolean isFirstField = true;// 标记是否是第一个字段
        for (int i = 0; i < len; i++) {
            char c = underscoreName.charAt(i);
            if (c == '_') {
                isFirstField = false;
                continue;
            }
            if (!isFirstField) {
                c = Character.toUpperCase(c);
                isFirstField = true;
            } else {
                c = Character.toLowerCase(c);
            }
            sBuilder.append(c);
        }
        return sBuilder.toString();
    }

    /**
     * 根据字段名和值的类型查找合适的setter方法
     *
     * @param clazz 类的Class对象
     * @param fieldName 字段名
     * @param type 值的类型
     * @return setter方法
     */
    private static Method findSetterMethod(Class<?> clazz, String fieldName, Class<?> type) {
        String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        try {
            return clazz.getMethod(methodName, type);
        } catch (NoSuchMethodException e) {
            return null; // 如果没有找到对应的方法，返回null
        }
    }

}
