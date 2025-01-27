package main;

import manager.*;
import task.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        // Добавление задач с новыми конструкторами
        LocalDateTime now = LocalDateTime.now();
        Task task = new Task("Таск 0", TaskStatus.NEW, Duration.ofMinutes(30), now);
        taskManager.addTask(task);

        task = new Task("Таск 1", TaskStatus.NEW, Duration.ofMinutes(45), now.plusDays(1));
        taskManager.addTask(task);

        // Создание и добавление эпика
        Epic epic = new Epic("Эпик 2", TaskStatus.NEW, now.plusDays(2));
        taskManager.addEpic(epic);

        // Добавление сабтасков
        SubTask subTask = new SubTask("СабТаск 3", TaskStatus.NEW, Duration.ofHours(1), now.plusDays(3), 2);
        taskManager.addSubTask(subTask);

        subTask = new SubTask("СабТаск 4", TaskStatus.NEW, Duration.ofHours(2), now.plusDays(3), 2);
        taskManager.addSubTask(subTask);

        subTask = new SubTask("СабТаск 5", TaskStatus.NEW, Duration.ofHours(3), now.plusDays(3), 2);
        taskManager.addSubTask(subTask);

        System.out.println(taskManager.getPrioritizedTasks() + "\nSorted");

        taskManager.getTaskById(0);
        taskManager.getTaskById(1);
        taskManager.getTaskById(0);
        taskManager.getEpicById(2);
        taskManager.getSubtaskById(3);
        taskManager.getSubtaskById(4);
        taskManager.getSubtaskById(5);
        taskManager.getSubtaskById(3);
        taskManager.getSubtaskById(5);
        taskManager.getEpicById(2);
        System.out.println(taskManager.getHistory() + "\n");

        taskManager.getSubtaskById(5);
        taskManager.getSubtaskById(5);
        taskManager.getSubtaskById(5);
        taskManager.getSubtaskById(3);
        taskManager.getEpicById(2);
        taskManager.getTaskById(1);
        taskManager.getTaskById(1);
        taskManager.getTaskById(1);
        taskManager.getEpicById(2);


        System.out.println(taskManager.getHistory() + "\n");

        taskManager.deleteTaskById(0);
        taskManager.deleteSubtaskById(5);

        System.out.println(taskManager.getHistory() + "\n");

        taskManager.addSubTask(subTask);
        taskManager.getSubtaskById(5);
        taskManager.deleteEpicById(2);

        System.out.println(taskManager.getHistory() + "\n\n\n");



    }
}
