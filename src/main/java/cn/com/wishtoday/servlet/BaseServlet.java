package cn.com.wishtoday.servlet;

import cn.com.wishtoday.annotation.GetMapping;
import cn.com.wishtoday.annotation.PostMapping;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class BaseServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final Map<String, MethodHandle> getHandlerMethods = new HashMap<>();
    private final Map<String, MethodHandle> postHandlerMethods = new HashMap<>();
    private final MethodHandles.Lookup lookup = MethodHandles.lookup();

    @Override
    public void init() throws ServletException {
        super.init();
        // 初始化映射关系
        for (Method method : getClass().getDeclaredMethods()) {
            GetMapping getMapping = method.getAnnotation(GetMapping.class);
            if (getMapping != null) {
                getHandlerMethods.put(getMapping.value(), createMethodHandle(method));
            }

            PostMapping postMapping = method.getAnnotation(PostMapping.class);
            if (postMapping != null) {
                postHandlerMethods.put(postMapping.value(), createMethodHandle(method));
            }
        }
    }

    private MethodHandle createMethodHandle(Method method) {
        try {
            return lookup.unreflect(method).asFixedArity();
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to create method handle", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response, postHandlerMethods);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response, getHandlerMethods);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response, Map<String, MethodHandle> handlerMethods) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String pathInfo = request.getPathInfo();

        MethodHandle handlerMethod = handlerMethods.get(pathInfo);
        if (handlerMethod != null) {
            try {
                handlerMethod.invokeWithArguments(this, request, response);
            } catch (Throwable e) {
                throw new ServletException("Error invoking handler method", e);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid path");
        }
    }
}