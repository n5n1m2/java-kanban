package Main;
import Manager.*;
import Task.*;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();



    taskManager.addTask(new Task(TaskStatus.NEW, "Задача 1"));
    taskManager.addTask(new Task(TaskStatus.NEW, "Задача 2"));

    taskManager.addEpic(new Epic(TaskStatus.NEW, "Эпик 1"));
    taskManager.addEpic(new Epic(TaskStatus.NEW, "Эпик 2"));

    taskManager.addSubTask(new SubTask(TaskStatus.NEW, "Подзадача 1", taskManager.getAllEpic().get(0).getId()));
        taskManager.addSubTask(new SubTask(TaskStatus.NEW, "Подзадача 2", taskManager.getAllEpic().get(1).getId()));
        taskManager.addSubTask(new SubTask(TaskStatus.NEW, "Подзадача 3", taskManager.getAllEpic().get(1).getId()));


        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubTask());
        System.out.println(taskManager.getTaskById(1));
        System.out.println(taskManager.getEpicById(3));
        System.out.println(taskManager.getSubtaskById(6));

        System.out.println("****************************************" + "\n");

        taskManager.subTaskUpdate(new SubTask(4, "Подзадача 1",TaskStatus.DONE, 2 ));
        taskManager.subTaskUpdate(new SubTask(5, "Подзадача 2", TaskStatus.DONE, taskManager.getAllEpic().get(1).getId()));

        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubTask());

        System.out.println("****************************************" + "\n");

        taskManager.subTaskUpdate(new SubTask(6, "Подзадача 3", TaskStatus.DONE, taskManager.getAllEpic().get(1).getId()));

        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubTask());

        System.out.println("****************************************" + "\n");

        taskManager.subTaskUpdate(new SubTask(6, "Подзадача 3", TaskStatus.IN_PROGRESS, taskManager.getAllEpic().get(1).getId()));

        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubTask());

        System.out.println("****************************************" + "\n");

        taskManager.deleteEpicById(2);
        taskManager.removeAllSubTask();
        taskManager.removeAllTask();

        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubTask());

        System.out.println("****************************************" + "\n");

        taskManager.removeAllEpic();

        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubTask());






    }
}
