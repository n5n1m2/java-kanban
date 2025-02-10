import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import http.handlers.adapters.DurationAdapter;
import http.handlers.adapters.LocalDateTimeAdapter;
import http.server.Server;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Task;
import task.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class HttpTaskManagerTaskTest {
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    private TaskManager taskManager = Managers.getDefault();
    private Server httpServer = new Server(taskManager);
    private LocalDateTime now = LocalDateTime.now();
    private HttpClient client = HttpClient.newBuilder().build();

    public HttpTaskManagerTaskTest() throws IOException {
    }

    @BeforeEach
    public void startServer() {
        httpServer.serverStart();
    }

    @AfterEach
    public void serverStop() {
        httpServer.serverStop();
    }

    @Test
    public void addTask() throws URISyntaxException, IOException, InterruptedException {
        Task task = new Task("Таск 0", TaskStatus.NEW, Duration.ofMinutes(30), now);
        String json = gson.toJson(task);

        URI uri = new URI("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Получен неверный код ответа сервера");
        assertEquals(1, taskManager.getAllTask().size(), "Задача не была добавлена в таскменеджер");
        assertEquals(task.getName(), taskManager.getAllTask().getFirst().getName(), "Имя задач не совпадает");

        task = new Task("Таск 2", TaskStatus.NEW, Duration.ofMinutes(30), now.minusMinutes(5));
        json = gson.toJson(task);
        request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(json)).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode(), "Получен неверный код ответа сервера");
        assertEquals(1, taskManager.getAllTask().size());
    }

    @Test
    public void removeTask() throws URISyntaxException, IOException, InterruptedException {
        Task task = new Task("Таск 0", TaskStatus.NEW, Duration.ofMinutes(30), now);
        taskManager.addTask(task);
        int taskCount = taskManager.getAllTask().size();

        URI uri = new URI("http://localhost:8080/tasks/0");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertNotEquals(taskCount, taskManager.getAllTask().size());
    }

    @Test
    public void getTask() throws URISyntaxException, IOException, InterruptedException {
        Task task = new Task("Таск 0", TaskStatus.NEW, Duration.ofMinutes(30), now);
        taskManager.addTask(task);

        URI uri = new URI("http://localhost:8080/tasks/0");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();

        assertEquals(task.getName(), jsonObject.get("name").getAsString());
        assertEquals(task.getStartTime().format(Task.FORMATTER), jsonObject.get("startTime").getAsString());
    }
}
