package xianhhh.WebUi;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import xianhhh.Utils.PlayerUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class WebUiBase {

    private static HttpServer server;

    public static void start() {
        try {
            int port = 8080;

            server = HttpServer.create(new java.net.InetSocketAddress(port), 0);
            server.createContext("/", new HtmlHandler());
            server.start();

            print("WebClickGui started on http://localhost:" + port);
        } catch (IOException e) {
            print("Error starting WebClickGui: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void stop() {
        if (server != null) {
            print("WebClickGui stop");
            server.stop(0);
        }
    }

    static class HtmlHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("index.html");

            if (inputStream == null) {
                exchange.sendResponseHeaders(404, 0);
                OutputStream os = exchange.getResponseBody();
                os.write("Resource not found".getBytes(StandardCharsets.UTF_8));
                os.close();
                return;
            }

            byte[] response = inputStream.readAllBytes();
            exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, response.length);
            OutputStream os = exchange.getResponseBody();
            os.write(response);
            os.close();
        }
    }

    public static void print(String str) {
        PlayerUtils.sendMessage(str);
    }
}
