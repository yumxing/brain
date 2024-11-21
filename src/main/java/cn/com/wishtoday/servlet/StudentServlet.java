package cn.com.wishtoday.servlet;

import cn.com.wishtoday.annotation.GetMapping;
import cn.com.wishtoday.annotation.PostMapping;
import cn.com.wishtoday.model.Page;
import cn.com.wishtoday.model.StudentModel;
import cn.com.wishtoday.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "StudentServlet", urlPatterns = {"/student/*"})
public class StudentServlet extends BaseServlet {
    private static final Logger log = LoggerFactory.getLogger(StudentServlet.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 删除指定学生信息
     * @param request 请求
     * @param response 响应
     */
    @PostMapping("/remove.do")
    public void handleRemoveStudents(HttpServletRequest request, HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        StudentService studentService= new StudentService();
        int rowsAffected = studentService.remove(Long.parseLong(id));
        boolean result = rowsAffected > 0;
        // 准备响应结果
        ObjectMapper mapper = new ObjectMapper();
        // 创建一个 JSON 对象
        ObjectNode jsonObject = mapper.createObjectNode();
        jsonObject.put("success", result);
        jsonObject.put("msg", rowsAffected > 0 ? "删除成功" : "删除失败，请重试");

        PrintWriter out = getPrintWriter(response, jsonObject);
        out.flush();
    }

    /**
     * 添加或修改学生信息
     * @param request 请求
     * @param response 响应
     */
    @PostMapping("/save.do")
    public void handleSaveStudents(HttpServletRequest request, HttpServletResponse response) {
        // 设置响应的内容类型
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        String code = request.getParameter("code");
        String name = request.getParameter("name");
        int sex = Integer.parseInt(request.getParameter("sex"));
        int age = Integer.parseInt(request.getParameter("age"));
        String political = request.getParameter("political");
        String origin = request.getParameter("origin");
        String professional = request.getParameter("professional");

        int rowsAffected = 0;
        String msg = "";
        StudentModel student = new StudentModel();
        student.setCode(code);
        student.setName(name);
        student.setSex(sex);
        student.setAge(age);
        student.setPolitical(political);
        student.setOrigin(origin);
        student.setProfessional(professional);

        StudentService studentService= new StudentService();
        if(id == null || id.isEmpty()){
            rowsAffected = studentService.insert(student);
            msg =  rowsAffected > 0 ? "添加成功" : "添加失败，请重试";
        } else {
            student.setId(Long.parseLong(id));
            Object[] params = new Object[]{code, name, sex, age, political, origin, professional, Long.parseLong(id)};
            rowsAffected = studentService.update(params);
            msg =  rowsAffected > 0 ? "修改成功" : "修改失败，请重试";
        }

        // 准备响应结果
        ObjectMapper mapper = new ObjectMapper();
        // 创建一个 JSON 对象
        ObjectNode jsonObject = mapper.createObjectNode();
        jsonObject.put("success", rowsAffected > 0);
        jsonObject.put("msg", msg);

        PrintWriter out = getPrintWriter(response, jsonObject);
        out.flush();
    }

    /**
     * 获取所有学生信息
     * @param request 请求
     * @param response 响应
     * @throws IOException 异常
     */
    @GetMapping("/list.do")
    public void handleListStudents(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 设置响应的内容类型
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 获取请求参数
        //String dc = request.getParameter("_dc");
        //int pageParam = 1;
        int start = 0;
        int limit = 15;
        try{
            //pageParam = Integer.parseInt(request.getParameter("page"));
            start = Integer.parseInt(request.getParameter("start"));
            limit = Integer.parseInt(request.getParameter("limit"));
        } catch (NumberFormatException e){
            log.error("无法将字符串转换为int类型的整数");
        }
        // 解析排序参数
        String sortParam = request.getParameter("sort");

        StudentService ss = new StudentService();
        Page page = ss.getList(start, limit, sortParam);

        // 将学生列表转换成JSON字符串
        String jsonResponse = objectMapper.writeValueAsString(page);

        // 写入响应
        response.getWriter().write(jsonResponse);
    }

    private static PrintWriter getPrintWriter(HttpServletResponse response, ObjectNode jsonObject) {
        // 创建一个 JSON 数组
//        ArrayNode hobbies = mapper.createArrayNode();
//        hobbies.add("reading");
//        hobbies.add("swimming");
        // 输出JSON响应
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        out.print(jsonObject.toString());
        return out;
    }
}
