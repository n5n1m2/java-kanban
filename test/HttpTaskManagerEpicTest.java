import com.google.gson.*;
import http.handlers.adapters.DurationAdapter;
import http.handlers.adapters.LocalDateTimeAdapter;
import http.server.Server;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;
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

public class HttpTaskManagerEpicTest {
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    private TaskManager taskManager = Managers.getDefault();
    private Server httpServer = new Server(taskManager);
    private LocalDateTime now = LocalDateTime.now();
    private HttpClient client = HttpClient.newBuilder().build();

    public HttpTaskManagerEpicTest() throws IOException {
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
    public void addEpicTest() throws URISyntaxException, IOException, InterruptedException {
        Epic epic = new Epic("Эпик 0", TaskStatus.NEW, now);
        String json = gson.toJson(epic);

        URI uri = new URI("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Получен неверный код ответа сервера");
        assertEquals(1, taskManager.getAllEpic().size(), "Задача не была добавлена в таскменеджер");
        assertEquals(epic.getName(), taskManager.getAllEpic().getFirst().getName(), "Имя задач не совпадает");
    }

    @Test
    public void removeEpic() throws URISyntaxException, IOException, InterruptedException {
        Epic epic = new Epic("Эпик 0", TaskStatus.NEW, now);
        taskManager.addEpic(epic);
        int taskCount = taskManager.getAllEpic().size();

        URI uri = new URI("http://localhost:8080/epics/0");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertNotEquals(taskCount, taskManager.getAllEpic().size());
    }

    @Test
    public void getEpic() throws URISyntaxException, IOException, InterruptedException {
        Epic epic = new Epic("Эпик 0", TaskStatus.NEW, now);
        taskManager.addEpic(epic);

        URI uri = new URI("http://localhost:8080/epics/0");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();

        assertEquals(epic.getName(), jsonObject.get("name").getAsString());
        assertEquals(epic.getStartTime().format(Task.FORMATTER), jsonObject.get("startTime").getAsString());

        uri = new URI("http://localhost:8080/epics/2");
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    public void getEpicsSubtask() throws IOException, InterruptedException, URISyntaxException {
        Epic epic = new Epic("Эпик 0", TaskStatus.NEW, now);
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask("Сабтаск 1", TaskStatus.NEW, Duration.ofDays(1), now, 0);
        taskManager.addSubTask(subTask);

        URI uri = new URI("http://localhost:8080/epics/0/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        JsonArray json = JsonParser.parseString(response.body()).getAsJsonArray();
        JsonObject jsonObject = json.get(0).getAsJsonObject();

        assertEquals(subTask.getName(), jsonObject.get("name").getAsString());
        assertEquals(subTask.getStartTime().format(Task.FORMATTER), jsonObject.get("startTime").getAsString());

        uri = new URI("http://localhost:8080/epics/2/subtasks");
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }
}
