package http.server;

import com.sun.net.httpserver.HttpServer;
import http.handlers.*;
import manager.Managers;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Server {
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final HttpServer server;

    static {
        try {
            server = HttpServer.create(new InetSocketAddress(8080), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Server() throws IOException {
    }

    public static TaskManager serverStart() {
        TaskManager taskManager = Managers.getDefault();
        server.createContext("/tasks", new TaskHandler(taskManager));
        server.createContext("/subtask", new SubTaskHandler(taskManager));
        server.createContext("/epics", new EpicHandler(taskManager));
        server.createContext("/history", new HistoryHandler(taskManager));
        server.createContext("/prioritized", new PrioritizedTaskHandler(taskManager));
        server.start();
        return taskManager;
    }

    public static void serverStop() {
        server.stop(1);
    }


}
