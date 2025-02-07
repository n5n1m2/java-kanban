package main;

import manager.InMemoryTaskManager;
import manager.Managers;
import task.Task;
import task.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        taskManager.addTask(new Task("имя", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now()));
        taskManager.addTask(new Task("имя2", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(30)));
        taskManager.taskUpdate( new Task(0, "имя1", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now()));
        System.out.println(taskManager.getAll());


    }
}
