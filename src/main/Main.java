package main;

import manager.*;
import task.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        LocalDateTime now = LocalDateTime.now();
        Task task = new Task("Таск 0", TaskStatus.NEW, Duration.ofMinutes(15), now);
        taskManager.addTask(task);

        task = new Task("Таск 1", TaskStatus.NEW, Duration.ofMinutes(10), now.plusMinutes(15));
        taskManager.addTask(task);

       task = new Task(0,"Таск 2", TaskStatus.NEW, Duration.ofMinutes(3), now);
        taskManager.taskUpdate(task);


        taskManager.getAll().stream()
                .forEach(task1 -> System.out.println(task1 + " " + task1.getEndTime().format(Task.FORMATTER) + "\n"));


    }
}
