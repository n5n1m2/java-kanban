import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Sprint8Tests {
    TaskManager taskManager;
    @BeforeEach
    public void setTaskManager() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void epicTest(){
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        taskManager.addEpic(new Epic("Эпик 0", TaskStatus.NEW, now));
        taskManager.addSubTask(new SubTask("Сабтаск 1",TaskStatus.NEW, Duration.ofMinutes(30), now, 0));
        taskManager.addSubTask(new SubTask("Сабтаск 2",TaskStatus.NEW, Duration.ofMinutes(55), now.plusMinutes(30), 0));
        taskManager.addSubTask(new SubTask("Сабтаск 3",TaskStatus.NEW, Duration.ofMinutes(30), now.plusDays(1), 0));

        assertEquals(1, taskManager.getAllEpic().size(), "Эпик не добавлен.");
        assertEquals(3, taskManager.getAllSubTask().size(), "Добавлены не все сабтаски.");
        assertEquals(
                taskManager.getAllSubTask()
                        .stream()
                        .map(Task::getDuration)
                        .reduce(Duration.ZERO, Duration::plus),
                taskManager.getAllEpic().getFirst().getDuration(),
                "Продолжительность эпика и сабтасков не совпадает."
        );

        taskManager.subTaskUpdate(new SubTask(1,"Сабтаск 1",TaskStatus.DONE, Duration.ofMinutes(30), now, 0));
        taskManager.subTaskUpdate(new SubTask(2,"Сабтаск 2",TaskStatus.DONE, Duration.ofMinutes(55), now.plusMinutes(30), 0));
        taskManager.subTaskUpdate(new SubTask(3,"Сабтаск 3",TaskStatus.DONE, Duration.ofMinutes(30), now.plusDays(1), 0));

        assertEquals(TaskStatus.DONE, taskManager.getAllEpic().getFirst().getStatus(), "Статус эпика не был изменен");
        assertEquals(
                taskManager.getAllSubTask()
                        .stream()
                        .map(Task::getDuration)
                        .reduce(Duration.ZERO, Duration::plus),
                taskManager.getAllEpic().getFirst().getDuration(),
                "Продолжительность эпика и сабтасков не совпадает при статусе эпика DONE."
        );

        taskManager.subTaskUpdate(new SubTask(2,"Сабтаск 2",TaskStatus.NEW, Duration.ofMinutes(55), now.plusMinutes(30), 0));

        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getAllEpic().getFirst().getStatus(), "Статус эпика не был изменен");
        assertEquals(
                taskManager.getAllSubTask()
                        .stream()
                        .map(Task::getDuration)
                        .reduce(Duration.ZERO, Duration::plus),
                taskManager.getAllEpic().getFirst().getDuration(),
                "Продолжительность эпика и сабтасков не совпадает при статусе эпика IN_PROGRESS."
        );

        taskManager.subTaskUpdate(new SubTask(1,"Сабтаск 1",TaskStatus.IN_PROGRESS, Duration.ofMinutes(30), now, 0));
        taskManager.subTaskUpdate(new SubTask(2,"Сабтаск 2",TaskStatus.IN_PROGRESS, Duration.ofMinutes(55), now.plusMinutes(30), 0));
        taskManager.subTaskUpdate(new SubTask(3,"Сабтаск 3",TaskStatus.IN_PROGRESS, Duration.ofMinutes(30), now.plusDays(1), 0));

        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getAllEpic().getFirst().getStatus(), "Статус эпика не был изменен");
        assertEquals(
                taskManager.getAllSubTask()
                        .stream()
                        .map(Task::getDuration)
                        .reduce(Duration.ZERO, Duration::plus),
                taskManager.getAllEpic().getFirst().getDuration(),
                "Продолжительность эпика и сабтасков не совпадает при статусе эпика IN_PROGRESS."
        );

    }

    @Test
}
