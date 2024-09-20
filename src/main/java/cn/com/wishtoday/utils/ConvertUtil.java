package cn.com.wishtoday.utils;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
public class ConvertUtil {

    public static String StringConvertBase64(String str){
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(str.getBytes());
    }

    public static String Base64ConvertString(String base64){
        Base64.Decoder decoder = Base64.getDecoder();
        String str = null;
        try {
            str = new String(decoder.decode(base64.getBytes()), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("base64转换异常类型：{}",e.getClass());
            log.error("base64转换异常消息：{}",e.getMessage());
        }
        return str;
    }


    public static void main(String[] args) {
        String s1 = StringConvertBase64("北京监管合规平台系统科技金融");

        System.out.println(s1);
        System.out.println("Base64字符长度："+s1.length());
        System.out.println(Base64ConvertString("5YyX5Lqs55uR566h5ZCI6KeE5bmz5Y+w57O757uf56eR5oqA6YeR6J6N"));
    }
}
