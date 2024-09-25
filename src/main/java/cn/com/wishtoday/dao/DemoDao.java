package cn.com.wishtoday.dao;

import cn.com.wishtoday.config.DbConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DemoDao {
    private static final Logger log = LoggerFactory.getLogger(DemoDao.class);
    public static void main(String[] args) {
        long startSql = System.currentTimeMillis();//sql开始执行时间
        DbConfig dbConfig = DbConfig.getInstance();
        try (Connection conn = dbConfig.createConnection();
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT * FROM zhongzhi_college";
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    System.out.println(rs.getString("college_name")); // 根据实际列名替换
                }
            }
        } catch (Exception e) {
            log.error("Database operation failed: {}", e.getMessage(), e);
        }
        long endSql = System.currentTimeMillis();//sql执行完成时间
        log.info("数据返回结束，执行时间为："+(endSql-startSql)+"ms");
    }
}
