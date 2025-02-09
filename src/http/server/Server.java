package http.server;

import com.sun.net.httpserver.HttpServer;
import http.handlers.*;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Server {
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private HttpServer server;

    public Server(TaskManager taskManager) throws IOException {
        server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/tasks", new TaskHandler(taskManager));
        server.createContext("/subtask", new SubTaskHandler(taskManager));
        server.createContext("/epics", new EpicHandler(taskManager));
        server.createContext("/history", new HistoryHandler(taskManager));
        server.createContext("/prioritized", new PrioritizedTaskHandler(taskManager));
    }

    public void serverStart() {
        server.start();
    }

    public void serverStop() {
        server.stop(0);
    }
}
