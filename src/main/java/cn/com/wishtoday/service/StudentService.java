package cn.com.wishtoday.service;

import cn.com.wishtoday.dao.StudentDao;
import cn.com.wishtoday.model.Page;
import cn.com.wishtoday.model.StudentModel;

import java.util.List;

public class StudentService {

    public Page getList(int start, int limit, String sortParam){
        StudentDao sd = new StudentDao();
        //排序：ASC 或者 DESC
        return sd.pagedQuery(start, limit, sortParam);

    }

    public int insert(StudentModel student) {
        StudentDao sd = new StudentDao();
        return sd.insert(student);
    }

    public int update(Object[] params) {
        StudentDao sd = new StudentDao();
        return sd.update(params);
    }

    public int remove(long id){
        StudentDao sd = new StudentDao();
        return sd.remove(id);
    }
}
