package cn.com.wishtoday.utils;

import cn.com.wishtoday.repository.db.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DBUtil {

    /**
     * 从ResultSet中提取数据并转换为List<Map<String, Object>>.
     * @return 包含查询结果的List<Map<String, Object>>
     */
    public static List<Map<String, Object>> query(Connection conn, String sql, Object... params) throws SQLException {
        List<Map<String, Object>> listMap = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i+1, params[i]);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                int columnCount = rs.getMetaData().getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(rs.getMetaData().getColumnName(i), rs.getObject(i));
                }
                listMap.add(row);
            }
            return listMap;
        } finally {
            closeQuietly(rs);
            closeQuietly(pstmt);
            closeQuietly(conn);
        }
    }


    /**
     * 封装一个通用的查询方法
     * @param sql String
     * @param handler ResultSetHandler<T>
     * @param params Object...
     * @return 返回
     * @param <T> 返回值类型
     * @throws SQLException 异常
     */
    public static <T> T query(String sql, ResultSetHandler<T> handler, Object... params) throws SQLException{
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBManager.getInstance().createConnection();
            pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i+1, params[i]);
            }
            rs = pstmt.executeQuery();
            return handler.handler(rs);
        } finally {
            closeQuietly(rs);
            closeQuietly(pstmt);
            closeQuietly(conn);
        }
    }

    /**
     * 辅助方法用于安静地关闭资源
     * @param closeable 参数
     */
    private static void closeQuietly(AutoCloseable closeable){
        if(closeable != null){
            try {
                closeable.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 定义一个接口来处理ResultSet
     * @param <T>
     */
    public interface ResultSetHandler<T> {
        T handler(ResultSet rs) throws SQLException;
    }
}
