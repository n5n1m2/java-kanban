import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import task.Task;
import task.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManagersTest {
    @Test
    public void getManagerTest() {
        for (int i = 0; i < 10; i++) {
            TaskManager taskManager = Managers.getDefault();
            assertNotNull(taskManager, "Не получен объект №" + i);
            Task task = new Task("Имя", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now());
            taskManager.addTask(task);
            assertNotNull(taskManager.getTaskById(0), "Ошибка добавления таска в объект №" + i);
        }
    }
}
