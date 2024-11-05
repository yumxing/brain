package cn.com.wishtoday.utils;

import java.sql.*;
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
        }
    }


    /**
     * 查询操作
     * @param conn 数据库连接
     * @param sql sql语句
     * @param listArray ResultSet结果集
     * @param params sql参数
     * @return 返回值
     * @throws SQLException 异常
     */
    public static List<Map<String, Object>> query(Connection conn, String sql, List<String[]> listArray, Object... params) throws SQLException {
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
                if(!listArray.isEmpty()){
                    for (String[] strArray: listArray) {
                        if(strArray.length==2){
                            row.put(strArray[0], rs.getObject(strArray[1]));
                        }
                    }
                    listMap.add(row);
                }
            }
            return listMap;
        } finally {
            closeQuietly(rs);
            closeQuietly(pstmt);

        }
    }

    /**
     * 删除操作
     * @param conn 数据库连接
     * @param sql sql语句
     * @param params sql参数
     * @throws SQLException 异常
     */
    public static int executeSql(Connection conn, String sql, Object... params) throws SQLException {
        //int rowsAffected = 0;
        PreparedStatement pstmt = null;
        try{
            pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i+1, params[i]);
            }
            //执行数据库操作,受影响的行int rowsAffected
            return pstmt.executeUpdate();
        } finally {
            closeQuietly(pstmt);
            closeQuietly(conn);
        }

    }

    /**
     * 获取数据库总记录数
     * @param conn 数据库连接
     * @param sql sql语句
     * @throws SQLException 异常
     */
    public static int toCount(Connection conn, String sql) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            int totalCount = 0;
            if(rs.next()){
                totalCount = rs.getInt(1);
            }
            return totalCount;
        } finally {
            closeQuietly(rs);
            closeQuietly(pstmt);
        }
    }

    /**
     * 辅助方法用于安静地关闭资源
     * @param closeable 参数
     */
    public static void closeQuietly(AutoCloseable closeable){
        if(closeable != null){
            try {
                closeable.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
