package cn.com.wishtoday;

import cn.com.wishtoday.config.ApplicationConfig;
import cn.com.wishtoday.filter.GzipFilter;
import cn.com.wishtoday.utils.YmxUtil;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;

import javax.servlet.DispatcherType;
import javax.servlet.Servlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.util.EnumSet;
import java.util.List;
public class JettyStarter {
    private static final ApplicationConfig appConfig = ApplicationConfig.getInstance();
    static int port = appConfig.getProperty("server.port", 8080);
    static long idleTimeout = appConfig.getProperty("server.idleTimeout", 60*1000);
    static String contextPath = appConfig.getProperty("server.servlet.context-path", "/");
    static String pathSpec = appConfig.getProperty("server.servlet.pathSpec", "cn.com.wishtoday.controller");

    /**
     * 启动服务
     */
    public static void main(String[] args) throws Exception {
        Server server = new Server();
        // 配置 HTTP 连接器
        HttpConfiguration httpConfig = new HttpConfiguration();
        httpConfig.setIdleTimeout(60000); // 设置空闲超时时间为60秒
        server.addConnector(getConnector(server));
        server.setHandler(getContexts());
        // 启动服务器
        try {
            server.start();
            server.join();
        } finally {
            server.destroy();
        }
    }

    /**
     * 服务器连接器
     * @return 返回Server服务
     */
    private static ServerConnector getConnector(Server server) {
        // 创建一个ServerConnector , new HttpConnectionFactory(httpConfig)
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        // 设置空闲连接的超时时间（毫秒），这里设置为60秒
        connector.setIdleTimeout(idleTimeout);
        return connector;
    }

    /**
     * 上下文处理程序
     * @return 返回值
     */
    private static ContextHandlerCollection getContexts() {
        try {
            // 获取Resource对象
            Resource resource = Resource.newClassPathResource("/static");
            // 创建一个ServletContextHandler
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath(contextPath);

            context.setBaseResource(resource); // 设置静态资源路径
            context.addServlet(DefaultServlet.class, "/");
            context.setWelcomeFiles(new String[]{"index.html"});

            // 注册Servlet
            registerServlets(context, pathSpec);

            // 设置自动列表和索引页显示，方便查看资源列表
            context.setInitParameter("dirAllowed", "true");
            //热部署
            String rb = "D:/home/project/brain/src/main/resources/static";
            context.setResourceBase(rb);
            context.setInitParameter("resourceBase", rb);

            // 注册过滤器
            FilterHolder gzipJsFilterHolder = new FilterHolder(new GzipFilter());
            context.addFilter(gzipJsFilterHolder, "/*", EnumSet.of(DispatcherType.REQUEST));

            ContextHandlerCollection contexts = new ContextHandlerCollection();
            contexts.addHandler(context);

            return contexts;
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize context handlers", e);
        }
    }

    private static void registerServlets(ServletContextHandler context, String packageName) {
        List<Class<?>> list = YmxUtil.getClassByPage(packageName);
        for (Class<?> clazz : list) {
            if (HttpServlet.class.isAssignableFrom(clazz)) {
                WebServlet webServletAnnotation = clazz.getAnnotation(WebServlet.class);
                if (webServletAnnotation != null) {
                    @SuppressWarnings("unchecked")
                    ServletHolder holder = new ServletHolder((Class<? extends Servlet>) clazz);
                    context.addServlet(holder, webServletAnnotation.urlPatterns()[0]);
                }
            }
        }
    }
}