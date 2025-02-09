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
import task.Task;
import task.TaskStatus;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalDateTime;

public class TaskHandler implements HttpHandler {
    private TaskManager taskManager;
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public TaskHandler(TaskManager taskManager) {
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
            case PostEndpoints.ADD_TASK -> addTask(exchange);
            case DeleteEndpoints.DELETE_TASK_BY_ID -> deleteTask(exchange);
            case GetEndpoints.GET_TASKS -> getTasks(exchange);
            case GetEndpoints.GET_TASKS_BY_ID -> getTaskById(exchange);
            default -> ResponseWriter.writeResponse(exchange, null, 404);
        }
    }

    private void addTask(HttpExchange exchange) {
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
            if (jsonObject.has("id") && id != -1) {
                Task task;
                if (startTime != null) {
                    task = new Task(id, name, status, duration, startTime);
                } else {
                    task = new Task(id, name, status);
                }
                boolean update = taskManager.taskUpdate(task);
                if (update) {
                    ResponseWriter.writeResponse(exchange, null, 201);
                } else {
                    ResponseWriter.writeResponse(exchange, null, 406);
                }
            } else {
                Task task;
                if (startTime != null) {
                    task = new Task(name, status, duration, startTime);
                } else {
                    task = new Task(name, status);
                }
                taskManager.addTask(task);
                ResponseWriter.writeResponse(exchange, null, 201);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteTask(HttpExchange exchange) {
        String[] splitURI = exchange.getRequestURI().getPath().split("/");
        taskManager.deleteTaskById(Integer.parseInt(splitURI[splitURI.length - 1]));
        ResponseWriter.writeResponse(exchange, null, 200);
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

    private void getTasks(HttpExchange exchange) {
        String json = gson.toJson(taskManager.getAllTask());
        ResponseWriter.writeResponse(exchange, json, 200);
    }
}
