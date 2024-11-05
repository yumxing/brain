package cn.com.wishtoday.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class GetClassUtil {
    private static final Logger log = LoggerFactory.getLogger(GetClassUtil.class);

    public static List<Class<?>> getClassByPage(String packageName){
        // 使用ClassLoader获取指定包下的所有类名
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        List<Class<?>> list = new ArrayList<>();
        try {
            Enumeration<URL> resources = classLoader.getResources(packageName.replace('.', '/'));
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                String path = resource.getPath();
                if ("file".equals(resource.getProtocol())) {
                    File directory = new File(path);
                    String[] files = directory.list();
                    if (files == null) {
                        continue; // 如果目录不存在或不是目录，跳过
                    }
                    for (String fileName : files) {
                        if (fileName.endsWith(".class")) {
                            String className = packageName + '.' + fileName.substring(0, fileName.length() - 6);

                            try {
                                Class<?> clazz = classLoader.loadClass(className);
                                list.add(clazz);
                            } catch (ClassNotFoundException e) {
                                log.error("找不到类，原因： {}", e.getMessage());
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            log.error("Could not list classes in package " + packageName + ": {}", e.getMessage());
        }
        return list;
    }

}
