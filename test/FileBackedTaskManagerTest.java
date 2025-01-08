import manager.FileBackedTaskManager;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;

import java.io.File;
import java.io.IOException;


import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest {

    @Test
    public void savingAndLoadingFromFile() throws IOException {
        File file = File.createTempFile("Test", ".txt");
        String path = file.getAbsolutePath();
        FileBackedTaskManager fbm = FileBackedTaskManager.loadFromFile(file);

        Task task = new Task(TaskStatus.NEW, "Таск 0");
        fbm.addTask(task);
        task = new Task(TaskStatus.NEW, "Таск 1");
        fbm.addTask(task);

        Epic epic = new Epic(TaskStatus.NEW, "Эпик 2");
        fbm.addEpic(epic);

        SubTask subTask = new SubTask(TaskStatus.NEW, "СабТаск 3", 2);
        fbm.addSubTask(subTask);
        subTask = new SubTask(TaskStatus.NEW, "СабТаск 4", 2);
        fbm.addSubTask(subTask);
        subTask = new SubTask(TaskStatus.NEW, "СабТаск 5", 2);
        fbm.addSubTask(subTask);

        assertEquals(6, fbm.getAll().size(), "Добавлены не все задачи");

        FileBackedTaskManager fbm1 = FileBackedTaskManager.loadFromFile(new File(path));
        assertEquals(fbm.getAll().size(), fbm1.getAll().size(), "Размеры списков задач не совпадают после загрузки из файла");

        for (int i = 0; i < fbm1.getAll().size(); i++) {
            assertEquals(fbm.getAll().get(i), fbm1.getAll().get(i), "Объекты в менеджере не совпадают после загрузки файла");
        }

        fbm.removeAllTask();


        fbm.addTask(new Task(TaskStatus.NEW, "123"));

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

        fbm.addTask(new Task(TaskStatus.NEW, "Имя"));
        assertEquals(1, fbm.getAll().size(), "В загруженный из файла менеджер не добавляются задачи");

        fbm.removeAllTask();
        assertEquals(0, fbm.getAll().size(), "Из менеджера не удаляются задачи");

        fbm.addTask(new Task(TaskStatus.NEW, "Имя"));
        fbm.deleteTaskById(1);
        assertEquals(0, fbm.getAll().size(), "Из менеджера не удаляются задачи по айди");
    }


}