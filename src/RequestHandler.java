import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;


public class RequestHandler {
    @WebRoute(path = "/test")
    public void test(HttpExchange requestData) throws IOException {
        String response =
                "<h1>Enter your name</h1>" +
                "<form method='post' action='/users'>" +
                "<input type='text' name='name' id='name'>" +
                "<button type='submit'>Submit</button>" +
                "</form>";
        requestData.sendResponseHeaders(200, response.length());
        OutputStream os = requestData.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    @WebRoute(method = "POST", path = "/users")
    public void users(HttpExchange requestData) throws IOException {
        String name = readNameFromRequestBody(requestData);
        String response = String.format("<h1>Logged in as: %s</h1>", name);
        requestData.sendResponseHeaders(200, response.length());
        OutputStream os = requestData.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private String readNameFromRequestBody(HttpExchange requestData) throws IOException {
        InputStreamReader isr =  new InputStreamReader(requestData.getRequestBody(),"utf-8");
        BufferedReader br = new BufferedReader(isr);
        int b;
        StringBuilder buf = new StringBuilder(512);
        while ((b = br.read()) != -1) {
            buf.append((char) b);
        }
        br.close();
        isr.close();
        return buf.toString().split("=")[1];
    }

    @WebRoute(path = "/users")
    public void getUsers(HttpExchange requestData) throws IOException {
        String response = "<h1>Users</h1>" +
                "<a href='/user/boy'>boy</a></br>" +
                "<a href='/user/girl'>girl</a></br>" +
                "<a href='/user/dog'>dog</a>";
        requestData.sendResponseHeaders(200, response.length());
        OutputStream os = requestData.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    @WebRoute(path = "/user/<userName>")
    public void user(HttpExchange requestData, String userName) throws IOException {
        String response = String.format("<h1>User: %s</h1>", userName);
        requestData.sendResponseHeaders(200, response.length());
        OutputStream os = requestData.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}
