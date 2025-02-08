package http.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.handlers.adapters.DurationAdapter;
import http.handlers.adapters.LocalDateTimeAdapter;
import http.handlers.endpoints.DeleteEndpoints;
import http.handlers.endpoints.Endpoint;
import http.handlers.endpoints.GetEndpoints;
import http.handlers.endpoints.PostEndpoints;
import manager.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTaskHandler implements HttpHandler {
    private TaskManager taskManager;
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public SubTaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void handle(HttpExchange exchange) {
        Endpoint endpoint;

        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET" -> endpoint = GetEndpoints.endpointFromPatch(exchange.getRequestURI().getPath());
            case "POST" -> endpoint = PostEndpoints.endpointFromPatch(exchange.getRequestURI().getPath());
            case "DELETE" -> endpoint = DeleteEndpoints.endpointFromPatch(exchange.getRequestURI().getPath());
            default -> endpoint = GetEndpoints.UNKNOWN;
        }

        System.out.println(endpoint + " " + method);
        switch (endpoint) {
            case DeleteEndpoints.DELETE_SUBTASK_BY_ID -> deleteSubTask(exchange);
            case GetEndpoints.GET_SUBTASKS -> getSubTask(exchange);
            case GetEndpoints.GET_SUBTASKS_BY_ID -> getSubTaskById(exchange);
            default -> ResponseWriter.writeResponse(exchange, null, 404);
        }
    }

    private void deleteSubTask(HttpExchange exchange) {
        String[] splitURI = exchange.getRequestURI().getPath().split("/");
        taskManager.deleteSubtaskById(Integer.parseInt(splitURI[splitURI.length - 1]));
        ResponseWriter.writeResponse(exchange, null, 200);
    }

    private void getSubTask(HttpExchange exchange) {
        String json = gson.toJson(taskManager.getAllSubTask());
        ResponseWriter.writeResponse(exchange, json, 200);
    }

    private void getSubTaskById(HttpExchange exchange) {
        String[] splitURI = exchange.getRequestURI().getPath().split("/");
        String json = gson.toJson(taskManager.getSubtaskById(Integer.parseInt(splitURI[splitURI.length - 1])));
        if (!json.equals("null")) {
            ResponseWriter.writeResponse(exchange, json, 200);
        } else {
            ResponseWriter.writeResponse(exchange, json, 404);
        }
    }
}
