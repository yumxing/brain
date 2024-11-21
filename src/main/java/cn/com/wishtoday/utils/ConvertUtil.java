package cn.com.wishtoday.utils;

import org.eclipse.jetty.util.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Base64;

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
     * 将下划线分隔的字段名转换为驼峰命名
     * 备注（这种方法性能能高）
     * @param underscoreName 下划线分隔的字段名
     * @return 驼峰命名的字段名
     */
    public static String convertFieldName(String underscoreName) {
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
     * 获取文件扩展名
     * @return java.lang.String
     */
    private static String getFileExtension(Path path) {
        String fileName = path.getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex > -1 ? fileName.substring(dotIndex + 1) : "";
    }


    /**
     * 使用Java的输入流和字符流来读取Resource并输出为字符串
     * @param resource 源路径
     * @return 返回值
     * @throws IOException 异常
     */
    public static String readResourceAsString(Resource resource) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        }
        return stringBuilder.toString();
    }

}
