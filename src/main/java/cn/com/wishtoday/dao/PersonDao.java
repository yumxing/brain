package cn.com.wishtoday.dao;

import cn.com.wishtoday.model.PersonModel;
import cn.com.wishtoday.utils.ConvertUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonDao {
    public static void main(String[] args) {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("APP_UserName", "张三");
        map1.put("AGE", 30);
        map1.put("ADDress", "北京");
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("app_userName", "李四");
        map2.put("age", 25);
        map2.put("ADDress", "上海");
        List<Map<String, Object>> listMaps = new ArrayList<>();
        listMaps.add(map1);
        listMaps.add(map2);

        List<PersonModel> personList = ConvertUtil.convertListToEntity(listMaps, PersonModel.class);


        for (PersonModel person :personList) {
            System.out.println(person.getAppUsername());
        }
        // 输出转换后的personList
        //personList.forEach(System.out::println);
    }
}
