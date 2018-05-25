import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.URI;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


public class Server {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        HttpContext context = server.createContext("/", new MyHandler());
        context.getFilters();
        server.setExecutor(null);
        server.start();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) {
            RequestHandler requestHandler = new RequestHandler();
            URI uri = httpExchange.getRequestURI();
            String requestQuery = uri.toString();
            String requestMethod = httpExchange.getRequestMethod();
            String[] queryArray = requestQuery.split("/");
            try {
                for (Method method : requestHandler.getClass().getDeclaredMethods()) {
                    WebRoute annotation = method.getAnnotation(WebRoute.class);
                    if (annotation.path().equals(requestQuery) && annotation.method().equals(requestMethod)) {
                        method.invoke(requestHandler, httpExchange);
                    } else if (queryArray.length == 3 && (annotation.path().startsWith('/' + queryArray[1] + '/'))) {
                        method.invoke(requestHandler, httpExchange, queryArray[2]);
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

}

