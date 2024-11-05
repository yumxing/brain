package cn.com.wishtoday.dao;

import cn.com.wishtoday.model.Page;
import cn.com.wishtoday.model.StudentModel;
import cn.com.wishtoday.repository.db.DBManager;
import cn.com.wishtoday.utils.DBUtil;
import cn.com.wishtoday.utils.EntityConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class StudentDao {
    private static final Logger log = LoggerFactory.getLogger(StudentDao.class);
    /**
     * 删除
     * @param id 参数ID
     * @throws SQLException 异常
     */
    public int remove(long id){
        String sql ="delete from student where id=?";
        Connection conn = null;
        try {
            conn = DBManager.getInstance().createConnection();
            return DBUtil.executeSql(conn, sql, id);//受影响的行
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.closeQuietly(conn);
        }

    }

    /**
     * 查询
     * @param start 起始页
     * @param limit 结束页
     * @param sort 排序方式
     * @param dir 排序字段
     * @return 返回值
     */
    public Page pagedQuery(int start, int limit, String sort, String dir){
        Page page = null;
        long startSql = System.currentTimeMillis();//sql开始执行时间
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("当前执行数据库操作的方法是："+methodName);
        String sql = "SELECT * FROM student";
        if(sort != null && !sort.equals("") && dir != null && !dir.equals("")){
            sql += " order by "+dir+" "+sort;
        }
        if(start >= 0 && limit > 0){
            sql += " LIMIT "+start+", "+limit;
        }
        String sqlTotal = "select count(1) from student";
        Connection conn = null;
        try {
            conn = DBManager.getInstance().createConnection();
            conn.setAutoCommit(false); // 设置自动提交为false，以便于事务管理

            List<Map<String, Object>> listMap = DBUtil.query(conn, sql);
            List<StudentModel> listStudent = EntityConverter.convertListToEntity(listMap, StudentModel.class);
            int totalCount = DBUtil.toCount(conn, sqlTotal);
            page = new Page(totalCount, listStudent);
            // 提交事务
            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.closeQuietly(conn);
        }
        long endSql = System.currentTimeMillis();//sql执行完成时间
        log.info("数据返回结束，执行时间为："+(endSql-startSql)+"ms");
        return page;
    }

    public int insert(StudentModel student) {
        String code = student.getCode();
        String name = student.getName();
        int sex = student.getSex();
        int age = student.getAge();
        String political = student.getPolitical();
        String origin = student.getOrigin();
        String professional = student.getProfessional();
        Object[] params = new Object[]{code, name, sex, age, political, origin, professional};
        // SQL 插入语句，不包含自增的 id 字段
        String sql ="INSERT INTO `student` (`code`, `name`, `sex`, `age`, "+
                "`political`, `origin`, `professional`) VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DBManager.getInstance().createConnection();
            return DBUtil.executeSql(conn, sql, params);//受影响的行
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.closeQuietly(conn);
        }

    }

    public int update(Object[] params) {
        String sql = "UPDATE `student` SET `code` = ?, `name` = ?, `sex` = ?, `age` = ?,"+
                " `political` = ?, `origin` = ?, `professional` = ? WHERE `id` = ?";
        Connection conn = null;
        try {
            conn = DBManager.getInstance().createConnection();
            return DBUtil.executeSql(conn, sql, params);//受影响的行
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.closeQuietly(conn);
        }
    }
}
