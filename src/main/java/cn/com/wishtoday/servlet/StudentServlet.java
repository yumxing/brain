package cn.com.wishtoday.servlet;

import cn.com.wishtoday.model.Page;
import cn.com.wishtoday.model.StudentModel;
import cn.com.wishtoday.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "StudentServlet", urlPatterns = {"/student/*"})
public class StudentServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(StudentServlet.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || "/list.do".equals(pathInfo)) {
            handleListStudents(request, response);
        }else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid path");
        }
    }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if ("/update".equals(pathInfo)) {
            //handleUpdateStudent(request, response);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid update path");
        } else if ("/save.do".equals(pathInfo)) {
            handleSaveStudents(request, response);
        } else if ("/remove.do".equals(pathInfo)) {
            handleRemoveStudents(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid path");
        }
    }

    /**
     * 删除指定学生信息
     * @param request 请求
     * @param response 响应
     */
    private void handleRemoveStudents(HttpServletRequest request, HttpServletResponse response){
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
        jsonObject.put("msg", result ? "删除成功" : "删除失败，请重试");


        // 输出JSON响应
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        out.print(jsonObject.toString());
        out.flush();
    }

    /**
     * 添加或修改学生信息
     * @param request 请求
     * @param response 响应
     */
    private void handleSaveStudents(HttpServletRequest request, HttpServletResponse response) {
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
        } else {
            student.setId(Long.parseLong(id));
            Object[] params = new Object[]{code, name, sex, age, political, origin, professional, Long.parseLong(id)};
            rowsAffected = studentService.update(params);
        }
        boolean result = rowsAffected > 0;
        // 准备响应结果
        ObjectMapper mapper = new ObjectMapper();
        // 创建一个 JSON 对象
        ObjectNode jsonObject = mapper.createObjectNode();
        jsonObject.put("success", result);
        jsonObject.put("msg", result ? "提交成功" : "提交失败，请重试");

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
        out.flush();
    }

    /**
     * 获取所有学生信息
     * @param request 请求
     * @param response 响应
     * @throws IOException 异常
     */
    private void handleListStudents(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 设置响应的内容类型
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        int start = 0;
        int limit = 15;
        try{
            start = Integer.parseInt(request.getParameter("start"));
            limit = Integer.parseInt(request.getParameter("limit"));
        } catch (NumberFormatException e){
            log.error("无法将字符串转换为int类型的整数");
        }
        String sort = request.getParameter("sort") != null ? request.getParameter("sort") : "asc";;
        String dir = request.getParameter("dir") != null ? request.getParameter("dir") : "code";
        StudentService ss = new StudentService();
        Page page = ss.getList(start, limit, sort, dir);

        // 将学生列表转换成JSON字符串
        String jsonResponse = mapper.writeValueAsString(page);

        // 写入响应
        response.getWriter().write(jsonResponse);
    }

}
