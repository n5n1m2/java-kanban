import manager.FileBackedTaskManager;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest {

    @Test
    public void savingAndLoadingFromFile() throws IOException {
        File file = File.createTempFile("Test", ".txt");
        String path = file.getAbsolutePath();
        FileBackedTaskManager fbm = FileBackedTaskManager.loadFromFile(file);

        Task task = new Task("Таск 0", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        fbm.addTask(task);
        task = new Task("Таск 1", TaskStatus.NEW, Duration.ofMinutes(45), LocalDateTime.now().plusDays(1));
        fbm.addTask(task);

        Epic epic = new Epic("Эпик 2", TaskStatus.NEW, LocalDateTime.now().plusDays(2));
        fbm.addEpic(epic);

        SubTask subTask = new SubTask("СабТаск 3", TaskStatus.NEW, Duration.ofHours(1), LocalDateTime.now().plusDays(3), 2);
        fbm.addSubTask(subTask);
        subTask = new SubTask("СабТаск 4", TaskStatus.NEW, Duration.ofHours(2), LocalDateTime.now().plusDays(3), 2);
        fbm.addSubTask(subTask);
        subTask = new SubTask("СабТаск 5", TaskStatus.NEW, Duration.ofHours(3), LocalDateTime.now().plusDays(3), 2);
        fbm.addSubTask(subTask);

        assertEquals(6, fbm.getAll().size(), "Добавлены не все задачи");

        FileBackedTaskManager fbm1 = FileBackedTaskManager.loadFromFile(new File(path));
        assertEquals(fbm.getAll().size(), fbm1.getAll().size(), "Размеры списков задач не совпадают после загрузки из файла");

        for (int i = 0; i < fbm1.getAll().size(); i++) {
            assertEquals(fbm.getAll().get(i), fbm1.getAll().get(i), "Объекты в менеджере не совпадают после загрузки файла");
        }

        fbm.removeAllTask();

        fbm.addTask(new Task("123", TaskStatus.NEW, Duration.ofMinutes(20), LocalDateTime.now()));

        fbm1 = FileBackedTaskManager.loadFromFile(new File(path));

        assertEquals(fbm.getAll().size(), fbm1.getAll().size(), "Размеры списков задач не совпадают после загрузки из файла");
        for (int i = 0; i < fbm1.getAll().size(); i++) {
            assertEquals(fbm.getAll().get(i), fbm1.getAll().get(i), "Объекты в менеджере не совпадают после удаления задачи и загрузки файла");
        }
    }

    @Test
    public void saveEmptyFile() throws IOException {
        File file = File.createTempFile("Test", ".txt");
        FileBackedTaskManager fbm = FileBackedTaskManager.loadFromFile(file);
        assertEquals(0, fbm.getAll().size(), "Созданы лишние объекты после загрузки файла.");

        fbm.addTask(new Task("Имя", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now()));
        assertEquals(1, fbm.getAll().size(), "В загруженный из файла менеджер не добавляются задачи");

        fbm.removeAllTask();
        assertEquals(0, fbm.getAll().size(), "Из менеджера не удаляются задачи");

        fbm.addTask(new Task("Имя", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now()));
        fbm.deleteTaskById(1);
        assertEquals(0, fbm.getAll().size(), "Из менеджера не удаляются задачи по айди");
    }
}
