import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;
    protected int id;
    protected LocalDateTime time;


    protected Random random = new Random();

    void setTaskManager(T taskManager, LocalDateTime time) {
        this.taskManager = taskManager;
        this.time = time;
    }

    @Test
    public void equalityOfTasks() {
        Task task1 = new Task("Имя", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        taskManager.addTask(task1);
        Task task2 = taskManager.getTaskById(task1.getId());
        assertEquals(task1, task2, "Полученные объекты не равны");
    }

    @Test
    public void testTaskIdConflict() {
        Task newTestTask = new Task(15, "Имя", TaskStatus.NEW, Duration.ofMinutes(30), time);
        Task task = new Task(15, "Имя 1", TaskStatus.NEW, Duration.ofMinutes(30), time.plusHours(1));

        taskManager.addTask(newTestTask);
        taskManager.addTask(task);

        assertEquals(2, taskManager.getAllTask().size(), "Не все задачи были добавлены");

    }

    @Test
    public void tasksAdd() {
        time = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        time = addNewTask(1, TaskStatus.NEW, time);
        addNewEpic(1);
        addNewSubTask(1, TaskStatus.NEW, time, taskManager.getAllEpic().getFirst().getId());

        assertNotEquals(null, taskManager.getTaskById(0), "Не найдена задача");
        assertNotEquals(null, taskManager.getEpicById(1), "Не найден эпик");
        assertNotEquals(null, taskManager.getSubtaskById(2), "Не найдена подзадача");
        assertEquals(1, taskManager.getAllEpic().getFirst().getSubTaskCount(), "В эпике нет подзадач");
        assertEquals(taskManager.getAllEpic().getFirst().getId(), taskManager.getAllSubTask().getFirst().getEpicId(), "Подзадача не сохранила информацию о эпике");
    }

    @Test
    public void equality() {
        TaskManager taskManager1 = Managers.getDefault();

        Epic epic = new Epic("Эпик", TaskStatus.NEW, LocalDateTime.now());
        Task task = new Task("Таск 1", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        Task task1 = new Task("Таск 2", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        SubTask subTask = new SubTask("Сабтаск 3", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now(), 0);

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
        addNewEpic(1);
        addNewSubTask(3, TaskStatus.NEW, time, taskManager.getAllEpic().getFirst().getId());
        taskManager.deleteSubtaskById(1);
        taskManager.deleteSubtaskById(2);
        assertEquals(1, taskManager.getEpicById(0).getSubTaskId().size(), "эпик сохранил лишний Id");
    }

    LocalDateTime addNewTask(int count, TaskStatus status, LocalDateTime startTime) {
        int counter = 0;
        String name = "Таск" + id;
        Duration duration = Duration.ofMinutes(30);
        LocalDateTime start = startTime;

        while (counter < count) {
            Task task = new Task(
                    "Таск",
                    status,
                    duration,
                    start);
            taskManager.addTask(task);
            counter++;
            id++;
            start = start.plusMinutes(30);
        }
        return start;
    }

    void addNewEpic(int count) {
        int counter = 0;
        String name = "Эпик" + id;

        while (counter < count) {
            Epic epic = new Epic(
                    name,
                    TaskStatus.NEW,
                    LocalDateTime.now());
            taskManager.addEpic(epic);
            counter++;
            id++;
        }
    }

    LocalDateTime addNewSubTask(int count, TaskStatus status, LocalDateTime startTime, int epicId) {
        int counter = 0;
        String name = "Сабтаск" + id;
        Duration duration = Duration.ofMinutes(30);
        LocalDateTime start = startTime;

        while (counter < count) {
            SubTask subTask = new SubTask(
                    name,
                    status,
                    duration,
                    start,
                    epicId);
            taskManager.addSubTask(subTask);
            counter++;
            id++;
            start = start.plusMinutes(30);
        }
        return start;
    }
}
