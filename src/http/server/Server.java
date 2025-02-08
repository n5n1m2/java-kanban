package http.server;

import com.sun.net.httpserver.HttpServer;
import http.handlers.GetsHandler;
import manager.InMemoryTaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    public final HttpServer server;

    public Server(InMemoryTaskManager taskManager) throws IOException {
        server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new GetsHandler(taskManager));



    }

    public void serverStart() {
        server.start();
    }

    public void serverStop() {
        server.stop(1);
    }


}
