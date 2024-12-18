import manager.Managers;
import manager.TaskManager;
import task.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    @Test
    public void getManagerTest() {
        for (int i = 0; i < 10; i++) {
            TaskManager taskManager = Managers.getDefault();
            assertNotNull(taskManager, "Не получен объект №" + i);
            Task task = new Task(TaskStatus.NEW, "Имя");
            taskManager.addTask(task);
            assertNotNull(taskManager.getTaskById(0), "Ошибка добавления таска в объект №" + i);
        }

    }
}