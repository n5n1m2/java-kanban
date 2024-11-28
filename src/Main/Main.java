package Main;
import Manager.*;
import Task.*;

public class Main {

    public static void main(String[] args) {

        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();



    inMemoryTaskManager.addTask(new Task(TaskStatus.NEW, "Задача 1"));
    inMemoryTaskManager.addTask(new Task(TaskStatus.NEW, "Задача 2"));

    inMemoryTaskManager.addEpic(new Epic(TaskStatus.NEW, "Эпик 1"));
    inMemoryTaskManager.addEpic(new Epic(TaskStatus.NEW, "Эпик 2"));

    inMemoryTaskManager.addSubTask(new SubTask(TaskStatus.NEW, "Подзадача 1", inMemoryTaskManager.getAllEpic().get(0).getId()));
        inMemoryTaskManager.addSubTask(new SubTask(TaskStatus.NEW, "Подзадача 2", inMemoryTaskManager.getAllEpic().get(1).getId()));
        inMemoryTaskManager.addSubTask(new SubTask(TaskStatus.NEW, "Подзадача 3", inMemoryTaskManager.getAllEpic().get(1).getId()));


        System.out.println(inMemoryTaskManager.getAllTask());
        System.out.println(inMemoryTaskManager.getAllEpic());
        System.out.println(inMemoryTaskManager.getAllSubTask());
        System.out.println(inMemoryTaskManager.getTaskById(1));
        System.out.println(inMemoryTaskManager.getEpicById(3));
        System.out.println(inMemoryTaskManager.getSubtaskById(6));

        System.out.println("****************************************" + "\n");

        inMemoryTaskManager.subTaskUpdate(new SubTask(4, "Подзадача 1",TaskStatus.DONE, 2 ));
        inMemoryTaskManager.subTaskUpdate(new SubTask(5, "Подзадача 2", TaskStatus.DONE, inMemoryTaskManager.getAllEpic().get(1).getId()));

        System.out.println(inMemoryTaskManager.getAllTask());
        System.out.println(inMemoryTaskManager.getAllEpic());
        System.out.println(inMemoryTaskManager.getAllSubTask());

        System.out.println("****************************************" + "\n");

        inMemoryTaskManager.subTaskUpdate(new SubTask(6, "Подзадача 3", TaskStatus.DONE, inMemoryTaskManager.getAllEpic().get(1).getId()));

        System.out.println(inMemoryTaskManager.getAllTask());
        System.out.println(inMemoryTaskManager.getAllEpic());
        System.out.println(inMemoryTaskManager.getAllSubTask());

        System.out.println("****************************************" + "\n");

        inMemoryTaskManager.subTaskUpdate(new SubTask(6, "Подзадача 3", TaskStatus.IN_PROGRESS, inMemoryTaskManager.getAllEpic().get(1).getId()));

        System.out.println(inMemoryTaskManager.getAllTask());
        System.out.println(inMemoryTaskManager.getAllEpic());
        System.out.println(inMemoryTaskManager.getAllSubTask());

        System.out.println("****************************************" + "\n");

        inMemoryTaskManager.deleteEpicById(2);
        inMemoryTaskManager.removeAllSubTask();
        inMemoryTaskManager.removeAllTask();

        System.out.println(inMemoryTaskManager.getAllTask());
        System.out.println(inMemoryTaskManager.getAllEpic());
        System.out.println(inMemoryTaskManager.getAllSubTask());

        System.out.println("****************************************" + "\n");

        inMemoryTaskManager.removeAllEpic();

        System.out.println(inMemoryTaskManager.getAllTask());
        System.out.println(inMemoryTaskManager.getAllEpic());
        System.out.println(inMemoryTaskManager.getAllSubTask());






    }
}
