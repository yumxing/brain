package cn.com.wishtoday;

import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class App {

    public static void main(String[] args) throws Exception {
        // 创建服务器实例
        Server server = new Server();
        // 配置 HTTP 连接器
        HttpConfiguration httpConfig = new HttpConfiguration();
        httpConfig.setIdleTimeout(60000); // 设置空闲超时时间为60秒

        ServerConnector connector = new ServerConnector(server, new HttpConnectionFactory(httpConfig));
        connector.setPort(8080);
        server.addConnector(connector);

        // 创建Web应用程序上下文
        WebAppContext webapp = new WebAppContext();

        // 设置上下文路径
        webapp.setContextPath("/");

        // 设置资源基础目录
        // 如果在开发环境中运行，可以使用以下路径
        String resourceBase = "src/main/resources";

        // 如果在生产环境中运行JAR，应该使用以下路径
        //String resourceBase = System.getProperty("user.dir") + "/target/classes";
        //webapp.setResourceAliases();
        webapp.setBaseResource(Resource.newResource(new File(resourceBase).toURI().toURL()));

        // 设置资源别名（如果需要）
        Map<String, String> resourceAliases = new HashMap<>();
        resourceAliases.put("/images", "src/main/resources/images");
        resourceAliases.put("/styles", "src/main/resources/css");
        webapp.setInitParameter("org.eclipse.jetty.servlet.Default.resourceAliases",
                resourceAliases.entrySet().stream()
                        .map(entry -> entry.getKey() + "=" + entry.getValue())
                        .reduce((a, b) -> a + "," + b)
                        .orElse("")
        );
        // 设置WAR位置（如果需要的话）
        // webapp.setWar("path/to/war/file.war");

        // 添加到服务器
        server.setHandler(webapp);

        // 启动服务器
        try {
            server.start();
            server.join();
        } finally {
            server.destroy();
        }
    }
}