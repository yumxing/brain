package cn.com.wishtoday.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.*;

public class LogUtil {
    private static final Logger LOGGER = Logger.getLogger(LogUtil.class.getName());

    static {
        try {
            // 加载日志配置文件
            InputStream configInputStream = LogUtil.class.getResourceAsStream("/logging.xml");
            if (configInputStream != null) {
                LogManager.getLogManager().readConfiguration(configInputStream);
            } else {
                System.err.println("Could not find logging configuration file: logging.xml");
                // 如果配置文件不存在，设置默认的日志级别和处理器
                setupDefaultLogging();
            }
        } catch (IOException | SecurityException e) {
            System.err.println("Could not setup logger configuration: " + e.getMessage());
            // 如果加载配置文件失败，设置默认的日志级别和处理器
            setupDefaultLogging();
        }
    }

    private static void setupDefaultLogging() {
        // 设置默认的日志级别
        LOGGER.setLevel(Level.ALL);

        // 创建FileHandler并设置日志级别
        FileHandler fileHandler;
        try {
            Path logDir = Paths.get("logs");
            if (!Files.exists(logDir)) {
                Files.createDirectories(logDir);
            }
            fileHandler = new FileHandler("logs/app.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.ALL);
            LOGGER.addHandler(fileHandler);
        } catch (IOException e) {
            System.err.println("Could not setup default FileHandler: " + e.getMessage());
        }

        // 创建ConsoleHandler并设置日志级别
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        consoleHandler.setFormatter(new SimpleFormatter());
        LOGGER.addHandler(consoleHandler);

        // 不使用父处理器
        LOGGER.setUseParentHandlers(false);
    }

    public static void info(String message) {
        LOGGER.info(message);
    }

    public static void warning(String message) {
        LOGGER.warning(message);
    }

    public static void error(String message) {
        LOGGER.severe(message);
    }

    public static void error(String message, Throwable t) {
        LOGGER.log(Level.SEVERE, message, t);
    }

    // 可以根据需要添加更多方法，如debug, fine等
}