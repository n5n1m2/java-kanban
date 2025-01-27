import manager.Managers;
import manager.TaskManager;
import task.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    TaskManager taskManager;
    String name;

    @BeforeEach
    public void h() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void historyManagerSizeTest() {
        int i;
        for (i = 0; i < 300; i++) {
            if (i % 5 == 0 && i > 0) {
                name = "Эпик " + i;
                Epic epic = new Epic(name, TaskStatus.NEW, LocalDateTime.now().plusDays(i));
                taskManager.addEpic(epic);
                taskManager.getEpicById(i);
            } else {
                name = "Таск " + i;
                Task task = new Task(name, TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(i));
                taskManager.addTask(task);
                taskManager.getTaskById(i);
            }
        }
        List<Task> tasks = taskManager.getHistory();

        assertEquals(i, tasks.size(), "Не совпадают размеры. Вызвано " + i + " элементов, получено " + tasks.size() + " элементов.");
    }

    @Test
    public void historyManagerAddAndRemoveTest() {
        Task task = new Task("1", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        taskManager.addTask(task);
        taskManager.getTaskById(0);
        taskManager.getTaskById(0);
        taskManager.getTaskById(0);
        assertEquals(1, taskManager.getHistory().size(), "Не совпадает размер ожидаемого списка. Получено " + taskManager.getHistory().size() + " элементов, ожидается 1.");
        taskManager.taskUpdate(new Task(0, "Имя", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now()));
        taskManager.getTaskById(0);
        assertEquals("Имя", taskManager.getHistory().getFirst().getName(), "Не совпадают имена объектов");
    }
}
