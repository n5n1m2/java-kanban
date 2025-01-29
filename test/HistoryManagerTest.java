import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Task;
import task.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class HistoryManagerTest extends TaskManagerTest<TaskManager> {

    @BeforeEach
    public void setTaskManagerAndSetTime() {
        super.setTaskManager(Managers.getDefault(), LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
    }

    @Test
    public void historyManagerSizeTest() {
        int count = 300;
        time = super.addNewTask(count / 2, TaskStatus.NEW, time);
        super.addNewEpic(count / 2);

        IntStream.range(0, count / 2).forEach(num -> taskManager.getTaskById(num));
        IntStream.range(count / 2, count).forEach(num -> taskManager.getEpicById(num));
        List<Task> tasks = taskManager.getHistory();

        assertEquals(count, tasks.size(), "Не совпадают размеры. Вызвано " + count + " элементов, получено " + tasks.size() + " элементов.");
    }

    @Test
    public void historyManagerAddAndRemoveTest() {
        LocalDateTime oldTime = time;
        time = super.addNewTask(1, TaskStatus.NEW, time);
        taskManager.getTaskById(0);
        taskManager.getTaskById(0);
        taskManager.getTaskById(0);

        assertEquals(1,
                taskManager.getHistory().size(),
                "Не совпадает размер ожидаемого списка. Получено " + taskManager.getHistory().size() + " элементов, ожидается 1.");

        taskManager.taskUpdate(new Task(0, "Имя", TaskStatus.NEW, Duration.ofMinutes(30), oldTime));
        taskManager.getTaskById(0);

        assertEquals("Имя",
                taskManager.getHistory().getFirst().getName(),
                "Не совпадают имена объектов");

        time = super.addNewTask(2, TaskStatus.NEW, time);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.deleteTaskById(1);

        assertNotEquals(1,
                taskManager.getHistory().get(1).getId(),
                "Удаленная таска осталась в истории. Получен id " + taskManager.getHistory().get(1).getId());

        super.addNewTask(2, TaskStatus.NEW, time);

        int deleteTaskId = taskManager.getAllTask().getLast().getId();
        taskManager.getTaskById(deleteTaskId);
        taskManager.deleteTaskById(deleteTaskId);

        assertEquals(true,
                taskManager.getHistory().stream().allMatch(obj -> obj.getId() != deleteTaskId),
                "Удаленная таска осталась в истории.");
    }
}
