package http.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.handlers.adapters.DurationAdapter;
import http.handlers.adapters.LocalDateTimeAdapter;
import http.handlers.endpoints.DeleteEndpoints;
import http.handlers.endpoints.Endpoint;
import http.handlers.endpoints.GetEndpoints;
import http.handlers.endpoints.PostEndpoints;
import http.server.Server;
import manager.TaskManager;
import task.Epic;
import task.Task;
import task.TaskStatus;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalDateTime;

public class EpicHandler implements HttpHandler {
    private TaskManager taskManager;
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public EpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint;

        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET" -> endpoint = GetEndpoints.endpointFromPatch(exchange.getRequestURI().getPath());
            case "POST" -> endpoint = PostEndpoints.endpointFromPatch(exchange.getRequestURI().getPath());
            case "DELETE" -> endpoint = DeleteEndpoints.endpointFromPatch(exchange.getRequestURI().getPath());
            default -> endpoint = GetEndpoints.UNKNOWN;
        }

        switch (endpoint) {
            case PostEndpoints.ADD_EPIC -> addEpic(exchange);
            case DeleteEndpoints.DELETE_EPIC_BY_ID -> deleteEpic(exchange);
            case GetEndpoints.GET_EPIC -> getEpic(exchange);
            case GetEndpoints.GET_EPIC_BY_ID -> getEpicById(exchange);
            case GetEndpoints.GET_EPIC_SUBTASKS -> getEpicSubTask(exchange);
            default -> ResponseWriter.writeResponse(exchange, null, 404);
        }
    }


    private void addEpic(HttpExchange exchange) throws IOException {
        JsonObject jsonObject;
        try (InputStreamReader inputStream = new InputStreamReader(exchange.getRequestBody(), Server.DEFAULT_CHARSET)) {
            jsonObject = JsonParser.parseReader(inputStream).getAsJsonObject();

            Integer id = jsonObject.has("id") ? jsonObject.get("id").getAsInt() : null;
            String name = jsonObject.get("name").getAsString();
            TaskStatus status = TaskStatus.valueOf(jsonObject.get("status").getAsString());
            LocalDateTime startTime = jsonObject.has("startTime") ? LocalDateTime.parse(jsonObject.get("startTime").getAsString(), Task.FORMATTER) : null;
            if (jsonObject.has("id") && id != -1) {
                Epic task;
                if (startTime != null) {
                    task = new Epic(id, name, status, startTime);
                } else {
                    task = new Epic(id, name, status);
                }
                boolean update = taskManager.epicUpdate(task);
                if (update) {
                    ResponseWriter.writeResponse(exchange, null, 201);
                } else {
                    ResponseWriter.writeResponse(exchange, null, 406);
                }
            } else {
                Epic task;
                if (startTime != null) {
                    task = new Epic(name, status, startTime);
                } else {
                    task = new Epic(name, status);
                }
                taskManager.addEpic(task);
                ResponseWriter.writeResponse(exchange, null, 201);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteEpic(HttpExchange exchange) {
        String[] splitURI = exchange.getRequestURI().getPath().split("/");
        taskManager.deleteEpicById(Integer.parseInt(splitURI[splitURI.length - 1]));
        ResponseWriter.writeResponse(exchange, null, 200);
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

    private void getEpic(HttpExchange exchange) {
        String json = gson.toJson(taskManager.getAllEpic());
        ResponseWriter.writeResponse(exchange, json, 200);
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
}
