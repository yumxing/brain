package cn.com.wishtoday.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
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
     *
     * @return 返回值
     */
    private static String Base64ConvertString() {
        return Base64ConvertString("YWJjZA==");
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

}
