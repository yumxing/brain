package cn.com.wishtoday.dao;

import cn.com.wishtoday.model.CollegeModel;
import cn.com.wishtoday.repository.db.DBManager;
import cn.com.wishtoday.utils.DBUtil;
import cn.com.wishtoday.utils.EntityConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DemoDao {
    private static final Logger log = LoggerFactory.getLogger(DemoDao.class);

    public List<Map<String, Object>> gitCollegeList1(){
        List<Map<String, Object>> listMap = null;
        long startSql = System.currentTimeMillis();//sql开始执行时间
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("当前执行数据库操作的方法是："+methodName);
        String sql = "SELECT * FROM zhongzhi_college WHERE college_city=?";
        Object[] params = {"深圳"};
        String[] strArray1 = {"id","id"};
        String[] strArray2 = {"collegeName","college_name"};
        String[] strArray3 = {"college_public","college_public"};
        List<String[]> listArray = new ArrayList<>();
        listArray.add(strArray1);
        listArray.add(strArray2);
        listArray.add(strArray3);
        Connection conn = null;
        try {
            conn = DBManager.getInstance().createConnection();
            listMap = DBUtil.query(conn, sql, listArray, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.closeQuietly(conn);
        }
        long endSql = System.currentTimeMillis();//sql执行完成时间
        log.info("数据返回结束，执行时间为："+(endSql-startSql)+"ms");
        return listMap;
    }

    public List<Map<String, Object>> gitCollegeList2(){
        List<Map<String, Object>> listMap = null;
        long startSql = System.currentTimeMillis();//sql开始执行时间
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("当前执行数据库操作的方法是："+methodName);
        String sql = "SELECT * FROM zhongzhi_college WHERE college_city=?";
        Connection conn = null;
        try {
            conn = DBManager.getInstance().createConnection();
            listMap = DBUtil.query(conn, sql, "深圳");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.closeQuietly(conn);
        }
        long endSql = System.currentTimeMillis();//sql执行完成时间
        log.info("数据返回结束，执行时间为："+(endSql-startSql)+"ms");
        return listMap;
    }

    public List<CollegeModel> gitCollegeList3(List<Map<String, Object>> listMap){
        return EntityConverter.convertListToEntity(listMap, CollegeModel.class);
    }

    public static void main(String[] args) {
        DemoDao demoDao = new DemoDao();
        List<Map<String, Object>> listMap = demoDao.gitCollegeList1();
        //List<Map<String, Object>> listMap = demoDao.gitCollegeList2();
//        for (Map<String, Object> map: listMap) {
//            System.out.println(map.get("id"));
//            System.out.println(map.get("collegeName"));
//            System.out.println(map.get("college_public"));
//        }
        List<CollegeModel> listCollegeModel = demoDao.gitCollegeList3(listMap);
        for (CollegeModel college : listCollegeModel) {
            System.out.println(college.getId());
            System.out.println(college.getCollegePublic());
        }
    }
}
