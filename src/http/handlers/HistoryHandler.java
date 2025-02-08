package http.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.handlers.adapters.DurationAdapter;
import http.handlers.adapters.LocalDateTimeAdapter;
import http.handlers.endpoints.GetEndpoints;
import manager.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

public class HistoryHandler implements HttpHandler {
    private TaskManager taskManager;
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void handle(HttpExchange exchange) {
        GetEndpoints endpoint = GetEndpoints.endpointFromPatch(exchange.getRequestURI().getPath());
        System.out.println(endpoint);

        switch (endpoint) {
            case GET_HISTORY -> getHistory(exchange);
            default -> ResponseWriter.writeResponse(exchange, null, 404);
        }
    }


    private void getHistory(HttpExchange exchange) {
        String json = gson.toJson(taskManager.getHistory());
        ResponseWriter.writeResponse(exchange, json, 200);
    }
}
