package main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.handlers.adapters.DurationAdapter;
import http.handlers.adapters.LocalDateTimeAdapter;
import http.server.Server;
import manager.InMemoryTaskManager;
import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();

        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Server server = new Server(taskManager);


        taskManager.addTask(new Task("имя", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now()));
        taskManager.addTask(new Task("имя2", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(30)));
        taskManager.addEpic(new Epic("Эпик", TaskStatus.NEW, LocalDateTime.now()));
        taskManager.addSubTask(new SubTask("СабТаск", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now(), 2));
        taskManager.addSubTask(new SubTask("СабТаск", TaskStatus.NEW, 2));
        System.out.println(gson.toJson(taskManager.getAllTask()));
        taskManager.getTaskById(0);
        String json = gson.toJson(taskManager.getPrioritizedTasks());
        System.out.println(taskManager.getAllSubTask());


        server.serverStart();




    }
}
