import Manager.*;
import Task.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    TaskManager taskManager;
    @BeforeEach
    public void setTaskManager(){
        taskManager = Managers.getDefault();
    }

    @Test
    public void testTaskIdConflict(){


        Task newTestTask = new Task(15, "Имя", TaskStatus.NEW);
        Task genId = new Task(TaskStatus.NEW, "Имя 1" );

        taskManager.addTask(newTestTask);
        taskManager.addTask(genId);

        assertEquals(2, taskManager.getAllTask().size(), "Размер списка меньше, чем кол-во задач.");


        assertNotNull(taskManager.getTaskById(0), "Не найдена задача с айди 1");
        assertNotNull(taskManager.getTaskById(1), "Не найдена задача с айди 2");
    }
    @Test
    public void  equalityOfTasks(){ // Проверка неизменности задачи после добавления.
        Task task1 = new Task(TaskStatus.NEW, "Имя");
        taskManager.addTask(task1);
        Task task2 = taskManager.getTaskById(0);
        assertEquals(task1, task2, "Полученные объекты не равны");

    }

    @Test
    public void TasksAdd(){ // Проверка, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;
        Task task = new Task(TaskStatus.NEW, "Имя");
        Epic epic = new Epic(TaskStatus.NEW, "Эпик");
        SubTask subTask = new SubTask(TaskStatus.NEW, "СабТаск", 1);
        taskManager.addTask(task);
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask);

        assertEquals(task, taskManager.getTaskById(0), "Не найдена задача");
        assertEquals(epic, taskManager.getEpicById(1), "Не найден эпик");
        assertEquals(subTask, taskManager.getSubtaskById(2), "Не найдена подзадача");
        assertEquals(1, taskManager.getEpicById(1).getSubTaskCount(), "В эпике нет подзадач");
    }

    @Test
    public void addEpicAsSubTask(){
        Epic epic = new Epic(TaskStatus.NEW, "Эпик");
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask(0, "Имя", TaskStatus.NEW, 0);
        taskManager.addSubTask(subTask);
        assertNotEquals(taskManager.getEpicById(0), subTask);
        assertNotEquals(0, taskManager.getSubtaskById(1).getSubTaskId(), "Айди эпика и сабтаски равны");

    }

    @Test
    public void AddSubTaskAsEpic(){
        SubTask subTask = new SubTask(0, "Имя", TaskStatus.NEW, 0);
        taskManager.addSubTask(subTask);
        assertNull(taskManager.getSubtaskById(0));

    }

    @Test
    public void equality(){
        TaskManager taskManager1 = Managers.getDefault();

        Epic epic = new Epic(TaskStatus.NEW, "Эпик");
        Task task = new Task(TaskStatus.NEW, "Имя");
        Task task1 = new Task(TaskStatus.NEW, "Имя");
        SubTask subTask = new SubTask(TaskStatus.NEW, "Имя", 0);

        taskManager.addEpic(epic);
        taskManager1.addEpic(epic);

        taskManager.addTask(task);
        taskManager1.addTask(task);

        taskManager.addSubTask(subTask);
        taskManager1.addSubTask(subTask);

        assertEquals(taskManager1.getTaskById(1), taskManager.getTaskById(1), "Таски не равны");
        assertEquals(taskManager1.getEpicById(0), taskManager.getEpicById(0), "Эпики не равны");
        assertEquals(taskManager1.getSubtaskById(3), taskManager.getSubtaskById(3), "Сабтаски не равны");

        taskManager.addTask(task1);
        assertNotEquals(taskManager.getTaskById(1), taskManager.getTaskById(4), "Таски с разными айди равны");
    }

    //Внутри эпиков не должно оставаться неактуальных id подзадач.
    @Test
    public void EpicSubTaskIdSavingTest(){
        taskManager.addEpic(new Epic(TaskStatus.NEW, "Эпик 1"));
        taskManager.addSubTask(new SubTask(TaskStatus.NEW, "Имя 3", 0));
        taskManager.addSubTask(new SubTask(TaskStatus.NEW, "Имя 2", 0));
        taskManager.addSubTask(new SubTask(TaskStatus.NEW, "Имя 1", 0));
        taskManager.deleteSubtaskById(1);
        taskManager.deleteSubtaskById(2);
         System.out.println(taskManager.getEpicById(0).getSubTaskId());
         assertEquals(1, taskManager.getEpicById(0).getSubTaskId().size(), "эпик сохранил лишний Id");
    }
}