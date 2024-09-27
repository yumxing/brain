package cn.com.wishtoday.dao;

import cn.com.wishtoday.model.CollegeModel;
import cn.com.wishtoday.utils.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DemoDao {
    private static final Logger log = LoggerFactory.getLogger(DemoDao.class);
    public static void main(String[] args) {
        long startSql = System.currentTimeMillis();//sql开始执行时间
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("当前执行数据库操作的方法是："+methodName);
        String sql = "SELECT * FROM zhongzhi_college WHERE college_city=?";
        try {
            List<CollegeModel> collegeModels = DBUtil.query(sql, new DBUtil.ResultSetHandler<List<CollegeModel>>(){
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
            for (CollegeModel collegeModel :collegeModels) {
                System.out.println(collegeModel.getCollegeName());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //List<CollegeModel> listCollegeModel = ConvertUtil.convertListToEntity(listMap, CollegeModel.class);
        long endSql = System.currentTimeMillis();//sql执行完成时间
        log.info("数据返回结束，执行时间为："+(endSql-startSql)+"ms");
    }
}
