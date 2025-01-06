package main;

import manager.*;
import task.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("\n  \n \n");

        TaskManager taskManager = Managers.getDefault();
        Task task = new Task(TaskStatus.NEW, "Таск 0");
        taskManager.addTask(task);
        task = new Task(TaskStatus.NEW, "Таск 1");
        taskManager.addTask(task);

        Epic epic = new Epic(TaskStatus.NEW, "Эпик 2");
        taskManager.addEpic(epic);

        SubTask subTask = new SubTask(TaskStatus.NEW, "СабТаск 3", 2);
        taskManager.addSubTask(subTask);
        subTask = new SubTask(TaskStatus.NEW, "СабТаск 4", 2);
        taskManager.addSubTask(subTask);
        subTask = new SubTask(TaskStatus.NEW, "СабТаск 5", 2);
        taskManager.addSubTask(subTask);

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

        System.out.println(taskManager.getHistory() + "\n");

    }
}
