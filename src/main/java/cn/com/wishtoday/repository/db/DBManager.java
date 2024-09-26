package cn.com.wishtoday.repository.db;


import cn.com.wishtoday.repository.db.pojo.Database;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

public class DBManager {
    private static final Logger log = LoggerFactory.getLogger(DBManager.class);
    @JsonProperty("database")
    private Database database;
    private static volatile DataSource dataSource;// HikariCP DataSource
    private static volatile DBManager instance;

    private DBManager() {}

    public static DBManager getInstance(){
        if(instance == null){
            synchronized (DBManager.class){
                if (null == instance){
                    instance = new DBManager();
                    instance.loadDBManager();
                    instance.initializeDataSource();
                }
            }
        }
        return instance;
    }

    /**
     * 加载配置文件并设置数据库属性
     */
    private void loadDBManager() {
        ObjectMapper objectMapper = new ObjectMapper();
        ClassLoader classLoader = DBManager.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream("dbconfig.json")) {
            if (inputStream == null) {
                throw new RuntimeException("DBManager.json not found in classpath");
            }
            DBManager DBManager = objectMapper.readValue(inputStream, DBManager.class);
            this.database = DBManager.database;
        } catch (IOException e) {
            log.error("Database configuration loaded error. {}", e.getMessage(), e);
            this.database = null;
        } catch (Exception e) {
            log.error("Failed to read config file: {}", e.getMessage(), e);
            throw new RuntimeException("Error reading config file: " + e.getMessage(), e);
        }
    }
    /**
     * 初始化HikariCP数据源
     */
    private void initializeDataSource() {
        if (database == null) {
            throw new IllegalStateException("Database configuration is not initialized. Please check the configuration file.");
        }
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(database.getUrl());
        config.setUsername(database.getUsername());
        config.setPassword(database.getPassword());

        // 可以根据需要设置其他配置项
        config.setMaximumPoolSize(10);
        config.setConnectionTimeout(30000);  // 30秒

        dataSource = new HikariDataSource(config);
        log.info("HikariCP data source initialized successfully.");
    }
    // 创建数据库连接
    public Connection createConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
