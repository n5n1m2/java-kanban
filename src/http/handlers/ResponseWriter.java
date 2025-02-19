package http.handlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class ResponseWriter {
    public static void writeResponse(HttpExchange exchange, String jsonObject, int code) {
        try (OutputStream outputStream = exchange.getResponseBody()) {
            if (jsonObject != null && !jsonObject.equals("null")) {
                exchange.sendResponseHeaders(code, jsonObject.getBytes().length);
                outputStream.write(jsonObject.getBytes());
            } else {
                exchange.sendResponseHeaders(code, 0);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        exchange.close();
    }
}
