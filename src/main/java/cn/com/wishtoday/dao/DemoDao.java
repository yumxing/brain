package cn.com.wishtoday.dao;

import cn.com.wishtoday.model.CollegeModel;
import cn.com.wishtoday.repository.db.DBManager;
import cn.com.wishtoday.utils.ConvertUtil;
import cn.com.wishtoday.utils.DBUtil;
import cn.com.wishtoday.utils.EntityConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DemoDao {
    private static final Logger log = LoggerFactory.getLogger(DemoDao.class);

    public List<CollegeModel> gitCollegeList1(){
        List<CollegeModel> collegeModels = null;
        long startSql = System.currentTimeMillis();//sql开始执行时间
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("当前执行数据库操作的方法是："+methodName);
        String sql = "SELECT * FROM zhongzhi_college WHERE college_city=?";
        try {
            collegeModels = DBUtil.query(sql, new DBUtil.ResultSetHandler<List<CollegeModel>>(){
                @Override
                public List<CollegeModel> handler(ResultSet rs) throws SQLException {
                    List<CollegeModel> collegeModelList = new ArrayList<>();
                    while (rs.next()){
                        CollegeModel collegeModel = new CollegeModel();
                        collegeModel.setId(rs.getInt("id"));
                        collegeModel.setCollegeCode(rs.getString("college_code"));
                        collegeModel.setCollegeName(rs.getString("college_name"));
                        collegeModel.setCollegePublic(rs.getInt("college_public"));
                        collegeModel.setCollegeCity(rs.getString("college_city"));
                        collegeModel.setCollegeCategory(rs.getString("college_category"));
                        collegeModelList.add(collegeModel);
                    }
                    return collegeModelList;
                }
            }, "深圳");
            long endSql = System.currentTimeMillis();//sql执行完成时间
            log.info("数据返回结束，执行时间为："+(endSql-startSql)+"ms");
            return collegeModels;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Map<String, Object>> gitCollegeList2(){
        List<Map<String, Object>> listMap = null;
        long startSql = System.currentTimeMillis();//sql开始执行时间
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("当前执行数据库操作的方法是："+methodName);
        String sql = "SELECT * FROM zhongzhi_college WHERE college_city=?";
        try {
            Connection conn = DBManager.getInstance().createConnection();
            listMap = DBUtil.query(conn, sql, "深圳");
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
        List<Map<String, Object>> listMap = demoDao.gitCollegeList2();
//        for (Map<String, Object> map: listMap) {
//            System.out.println(map.get("id"));
//        }
        List<CollegeModel> listCollegeModel = demoDao.gitCollegeList3(listMap);
        for (CollegeModel college : listCollegeModel) {
            System.out.println(college.getCollegePublic());
        }
//        List<CollegeModel> collegeModels = demoDao.gitCollegeList1();
//        for (CollegeModel collegeModel: collegeModels) {
//            System.out.println(collegeModel.getId());
//        }
    }
}
