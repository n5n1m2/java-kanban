package http.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.handlers.adapters.DurationAdapter;
import http.handlers.adapters.LocalDateTimeAdapter;
import http.server.GetsEndpoints;
import manager.InMemoryTaskManager;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class GetsHandler implements HttpHandler {
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private InMemoryTaskManager taskManager;
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public GetsHandler(InMemoryTaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void handle(HttpExchange exchange) {
        GetsEndpoints endpoint = GetsEndpoints.endpointFromPatch(exchange.getRequestURI().getPath());
        System.out.println(endpoint);
        switch (endpoint) {
            case GET_EPIC -> getEpic(exchange);
            case GET_TASKS -> getTasks(exchange);
            case GET_HISTORY -> getHistory(exchange);
            case GET_SUBTASKS -> getSubTask(exchange);
            case GET_EPIC_BY_ID -> getEpicById(exchange);
            case GET_TASKS_BY_ID -> getTaskById(exchange);
            case GET_EPIC_SUBTASKS -> getEpicSubTask(exchange);
            case GET_SUBTASKS_BY_ID -> getSubTaskById(exchange);
            case GET_PRIORITIZED_TASKS -> getPrioritized(exchange);
            default -> ResponseWriter.writeResponse(exchange, null, 404);

        }
    }

    private void getTasks(HttpExchange exchange) {
        String json = gson.toJson(taskManager.getAllTask());
        ResponseWriter.writeResponse(exchange, json, 200);
    }

    private void getHistory(HttpExchange exchange) {
        String json = gson.toJson(taskManager.getHistory());
        ResponseWriter.writeResponse(exchange, json, 200);
    }

    private void getEpic(HttpExchange exchange) {
        String json = gson.toJson(taskManager.getAllEpic());
        ResponseWriter.writeResponse(exchange, json, 200);
    }

    private void getSubTask(HttpExchange exchange) {
        String json = gson.toJson(taskManager.getAllSubTask());
        ResponseWriter.writeResponse(exchange, json, 200);
    }

    private void getTaskById(HttpExchange exchange) {
        String[] splitURI = exchange.getRequestURI().getPath().split("/");
        String json = gson.toJson(taskManager.getTaskById(Integer.parseInt(splitURI[splitURI.length - 1])));
        if (!json.equals("null")) {
            ResponseWriter.writeResponse(exchange, json, 200);
        } else {
            ResponseWriter.writeResponse(exchange, json, 404);
        }
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

    private void getEpicById(HttpExchange exchange) {
        String[] splitURI = exchange.getRequestURI().getPath().split("/");
        String json = gson.toJson(taskManager.getEpicById(Integer.parseInt(splitURI[splitURI.length - 1])));
        if (!json.equals("null")) {
            ResponseWriter.writeResponse(exchange, json, 200);
        } else {
            ResponseWriter.writeResponse(exchange, json, 404);
        }
    }

    private void getPrioritized(HttpExchange exchange) {
        System.out.println(taskManager.getPrioritizedTasks());
        String json = gson.toJson(taskManager.getPrioritizedTasks());
        ResponseWriter.writeResponse(exchange, json, 200);
    }

    private void getEpicSubTask(HttpExchange exchange) {
        String[] splitURI = exchange.getRequestURI().getPath().split("/");
        String json = gson.toJson(taskManager.getEpicSubTask(Integer.parseInt(splitURI[splitURI.length - 2])));
        if (!json.equals("null")) {
            ResponseWriter.writeResponse(exchange, json, 200);
        } else {
            ResponseWriter.writeResponse(exchange, json, 404);
        }
    }

}
