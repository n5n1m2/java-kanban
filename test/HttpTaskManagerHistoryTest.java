import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
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

public class HttpTaskManagerHistoryTest {
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    private TaskManager taskManager = Managers.getDefault();
    private Server httpServer = new Server(taskManager);
    private LocalDateTime now = LocalDateTime.now();
    private HttpClient client = HttpClient.newBuilder().build();

    public HttpTaskManagerHistoryTest() throws IOException {
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
    public void getHistory() throws URISyntaxException, IOException, InterruptedException {
        Epic epic = new Epic("Эпик 0", TaskStatus.NEW, now);
        taskManager.addEpic(epic);

        SubTask task = new SubTask("Таск 1", TaskStatus.NEW, Duration.ofMinutes(30), now, 0);
        taskManager.addSubTask(task);

        taskManager.getSubtaskById(1);
        taskManager.getEpicById(0);

        URI uri = new URI("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(taskManager.getHistory().size(), JsonParser.parseString(response.body()).getAsJsonArray().size());
    }
}
