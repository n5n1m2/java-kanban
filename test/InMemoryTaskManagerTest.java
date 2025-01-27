import manager.*;
import task.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    TaskManager taskManager;

    @BeforeEach
    public void setTaskManager() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void testTaskIdConflict() {
        Task newTestTask = new Task(15, "Имя", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        Task genId = new Task("Имя 1", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now());

        taskManager.addTask(newTestTask);
        taskManager.addTask(genId);

        assertEquals(2, taskManager.getAllTask().size(), "Размер списка меньше, чем кол-во задач.");
        assertNotNull(taskManager.getTaskById(0), "Не найдена задача с айди 1");
        assertNotNull(taskManager.getTaskById(1), "Не найдена задача с айди 2");
    }

    @Test
    public void equalityOfTasks() {
        Task task1 = new Task("Имя", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        taskManager.addTask(task1);
        Task task2 = taskManager.getTaskById(0);
        assertEquals(task1, task2, "Полученные объекты не равны");
    }

    @Test
    public void tasksAdd() {
        Task task = new Task("Имя", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        Epic epic = new Epic("Эпик", TaskStatus.NEW, LocalDateTime.now());
        SubTask subTask = new SubTask("СабТаск", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now(), 1);
        taskManager.addTask(task);
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask);

        assertEquals(task, taskManager.getTaskById(0), "Не найдена задача");
        assertEquals(epic, taskManager.getEpicById(1), "Не найден эпик");
        assertEquals(subTask, taskManager.getSubtaskById(2), "Не найдена подзадача");
        assertEquals(1, taskManager.getEpicById(1).getSubTaskCount(), "В эпике нет подзадач");
    }

    @Test
    public void addEpicAsSubTask() {
        Epic epic = new Epic("Эпик", TaskStatus.NEW, LocalDateTime.now());
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask(0, "Имя", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now(), 0);
        taskManager.addSubTask(subTask);
        assertNotEquals(taskManager.getEpicById(0), subTask);
        assertNotEquals(0, taskManager.getSubtaskById(1).getSubTaskId(), "Айди эпика и сабтаски равны");
    }

    @Test
    public void addSubTaskAsEpic() {
        SubTask subTask = new SubTask(0, "Имя", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now(), 0);
        taskManager.addSubTask(subTask);
        assertNull(taskManager.getSubtaskById(0));
    }

    @Test
    public void equality() {
        TaskManager taskManager1 = Managers.getDefault();

        Epic epic = new Epic("Эпик", TaskStatus.NEW, LocalDateTime.now());
        Task task = new Task("Имя", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        Task task1 = new Task("Имя", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        SubTask subTask = new SubTask("Имя", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now(), 0);

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

    @Test
    public void epicSubTaskIdSavingTest() {
        taskManager.addEpic(new Epic("Эпик 1", TaskStatus.NEW, LocalDateTime.now()));
        taskManager.addSubTask(new SubTask("Имя 3", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now(), 0));
        taskManager.addSubTask(new SubTask("Имя 2", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now(), 0));
        taskManager.addSubTask(new SubTask("Имя 1", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now(), 0));
        taskManager.deleteSubtaskById(1);
        taskManager.deleteSubtaskById(2);
        assertEquals(1, taskManager.getEpicById(0).getSubTaskId().size(), "эпик сохранил лишний Id");
    }
}
