package Main;
import Manager.*;
import Task.*;

public class Main {

    public static void main(String[] args) {

        Manager manager = new Manager();



    manager.addTask(new Task(TaskStatus.NEW, "Задача 1"));
    manager.addTask(new Task(TaskStatus.NEW, "Задача 2"));

    manager.addEpic(new Epic(TaskStatus.NEW, "Эпик 1"));
    manager.addEpic(new Epic(TaskStatus.NEW, "Эпик 2"));

    manager.addSubTask(new SubTask(TaskStatus.NEW, "Подзадача 1",manager.getAllEpic().get(0).getId()));
        manager.addSubTask(new SubTask(TaskStatus.NEW, "Подзадача 2",manager.getAllEpic().get(1).getId()));
        manager.addSubTask(new SubTask(TaskStatus.NEW, "Подзадача 3",manager.getAllEpic().get(1).getId()));


        System.out.println(manager.getAllTask());
        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllSubTask());
        System.out.println(manager.getObject(3));

        System.out.println("****************************************" + "\n");

        manager.subTaskUpdate(new SubTask(4, "Подзадача 1",TaskStatus.DONE, 2 ));
        manager.subTaskUpdate(new SubTask(5, "Подзадача 2", TaskStatus.DONE, manager.getAllEpic().get(1).getId()));

        System.out.println(manager.getAllTask());
        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllSubTask());

        System.out.println("****************************************" + "\n");

        manager.subTaskUpdate(new SubTask(6, "Подзадача 3", TaskStatus.DONE, manager.getAllEpic().get(1).getId()));

        System.out.println(manager.getAllTask());
        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllSubTask());

        System.out.println("****************************************" + "\n");

        manager.subTaskUpdate(new SubTask(6, "Подзадача 3", TaskStatus.IN_PROGRESS, manager.getAllEpic().get(1).getId()));

        System.out.println(manager.getAllTask());
        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllSubTask());

        System.out.println("****************************************" + "\n");

        manager.idRemove(2);
        manager.removeAllSubTask();
        manager.removeAllTask();

        System.out.println(manager.getAllTask());
        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllSubTask());

        System.out.println("****************************************" + "\n");

        manager.removeAllEpic();

        System.out.println(manager.getAllTask());
        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllSubTask());






    }
}
