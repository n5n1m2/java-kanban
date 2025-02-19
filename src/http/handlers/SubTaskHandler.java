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
import task.SubTask;
import task.Task;
import task.TaskStatus;

import java.io.InputStreamReader;
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

        switch (endpoint) {
            case PostEndpoints.ADD_SUBTASK -> addSubTask(exchange);
            case DeleteEndpoints.DELETE_SUBTASK_BY_ID -> deleteSubTask(exchange);
            case GetEndpoints.GET_SUBTASKS -> getSubTask(exchange);
            case GetEndpoints.GET_SUBTASKS_BY_ID -> getSubTaskById(exchange);
            default -> ResponseWriter.writeResponse(exchange, null, 404);
        }
    }

    private void addSubTask(HttpExchange exchange) {
        JsonObject jsonObject;
        try (InputStreamReader inputStream = new InputStreamReader(exchange.getRequestBody(), Server.DEFAULT_CHARSET)) {
            jsonObject = JsonParser.parseReader(inputStream).getAsJsonObject();

            Integer id = jsonObject.has("id") ? jsonObject.get("id").getAsInt() : null;
            String name = jsonObject.get("name").getAsString();
            TaskStatus status = TaskStatus.valueOf(jsonObject.get("status").getAsString());
            String[] durationParts = jsonObject.get("duration").getAsString().split(":");
            Duration duration = Duration.ofDays(Long.parseLong(durationParts[0]))
                    .plusHours(Long.parseLong(durationParts[1]))
                    .plusMinutes(Long.parseLong(durationParts[2]));
            LocalDateTime startTime = jsonObject.has("startTime") ? LocalDateTime.parse(jsonObject.get("startTime").getAsString(), Task.FORMATTER) : null;
            int epicId = jsonObject.get("epicId").getAsInt();
            if (jsonObject.has("id") && id != -1) {
                SubTask task;
                if (startTime != null) {
                    task = new SubTask(id, name, status, duration, startTime, epicId);
                } else {
                    task = new SubTask(id, name, status, epicId);
                }
                boolean update = taskManager.subTaskUpdate(task);
                if (update) {
                    ResponseWriter.writeResponse(exchange, null, 201);
                } else {
                    ResponseWriter.writeResponse(exchange, null, 406);
                }
            } else {
                SubTask task;
                if (startTime != null) {
                    task = new SubTask(name, status, duration, startTime, epicId);
                } else {
                    task = new SubTask(name, status, epicId);
                }
                boolean added = taskManager.addSubTask(task);
                if (added) {
                    ResponseWriter.writeResponse(exchange, null, 201);
                } else {
                    ResponseWriter.writeResponse(exchange, null, 406);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
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
