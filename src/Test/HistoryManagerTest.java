import Manager.Managers;
import Manager.TaskManager;
import Task.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    @Test
    public void HistoryManagerTest(){
        TaskManager taskManager = Managers.getDefault();
        Task task = new Task(TaskStatus.NEW, "Имя");
        taskManager.addTask(task);
        taskManager.getTaskById(0);
        Task task1 = new Task(TaskStatus.NEW, "Имя 1");
        taskManager.taskUpdate(task1);
        taskManager.getTaskById(0);

        List<Task> tasks = taskManager.getHistory();
        assertEquals(task, tasks.get(1));
    }
}