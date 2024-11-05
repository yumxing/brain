package cn.com.wishtoday.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class ApplicationConfig {
    private static final Logger log = LoggerFactory.getLogger(ApplicationConfig.class);

    private static volatile ApplicationConfig INSTANCE;
    private final Properties properties;

    private ApplicationConfig() {
        properties = new Properties();
        loadProperties();
    }

    public static ApplicationConfig getInstance() {
        ApplicationConfig localInstance = INSTANCE;
        if (localInstance == null) {
            synchronized (ApplicationConfig.class) {
                localInstance = INSTANCE;
                if (localInstance == null) {
                    INSTANCE = localInstance = new ApplicationConfig();
                }
            }
        }
        return localInstance;
    }

    private void loadProperties() {
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream("application.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                // 更详细的错误处理，例如记录日志
                log.error("Unable to find application.properties");
            }
        } catch (Exception e) {
            log.error("Error loading properties file: {}", e.getMessage());
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public int getProperty(String key, int defaultValue) {
        String value = properties.getProperty(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.error("Invalid integer value for key '" + key + "': " + value);
            return defaultValue;
        }
    }

    public long getProperty(String key, long defaultValue) {
        String value = properties.getProperty(key);
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            log.error("Invalid long value for key '" + key + "': " + value);
            return defaultValue;
        }
    }

}
