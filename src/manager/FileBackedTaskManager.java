package manager;

import exceptions.ManagerSaveException;
import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    private FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        save();
    }

    @Override
    public void removeAllTask() {
        super.removeAllTask();
        save();
    }

    @Override
    public void removeAllEpic() {
        super.removeAllEpic();
        save();
    }

    @Override
    public void removeAllSubTask() {
        super.removeAllSubTask();
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void taskUpdate(Task task) {
        super.taskUpdate(task);
        save();
    }

    @Override
    public void epicUpdate(Epic epic) {
        super.epicUpdate(epic);
        save();
    }

    @Override
    public void subTaskUpdate(SubTask subTask) {
        super.subTaskUpdate(subTask);
        save();
    }

    private void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write("id,type,name,status,epic,subTaskCount");
            bw.newLine();
            for (Task task : super.getAll()) {
                bw.write(task.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения данных");
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fbm = new FileBackedTaskManager(file);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            while (br.ready()) {
                int epicIdOrSubTaskCount = 0;
                String[] sp = br.readLine().split(",");
                int id = Integer.parseInt(sp[0]);
                TaskType type = TaskType.valueOf(sp[1]);
                String name = sp[2];
                TaskStatus status = TaskStatus.valueOf(sp[3]);
                if (sp.length >= 5) {
                    epicIdOrSubTaskCount = Integer.parseInt(sp[4]);
                }
                fbm.id = id;
                switch (type) {
                    case TASK -> fbm.addTask(new Task(id, name, status));
                    case EPIC ->
                            fbm.addEpic(new Epic(id, name, status)); // epicIdOrSubTaskCount не используется, т.к кол-во сабтасков задает менеджер при привязке сабтаска к эпику.
                    case SUBTASK -> fbm.addSubTask(new SubTask(id, name, status, epicIdOrSubTaskCount));
                }
            }
            int counter = 0;
            for (Task task : fbm.getAll()) {
                if (task.getId() > counter) {
                    counter = task.getId();
                }
            }
            if (counter > 0) {
                fbm.id = counter + 1;
            }
        } catch (IOException e) {

        }
        return fbm;
    }

    public static void main(String[] args) throws IOException {
        File file = File.createTempFile("Test", ".txt");
        String path = file.getAbsolutePath();
        FileBackedTaskManager fbm = FileBackedTaskManager.loadFromFile(file);

        fbm.addTask(new Task(TaskStatus.NEW, "Таск 0"));
        fbm.addTask(new Task(TaskStatus.IN_PROGRESS, "Таск 1"));
        fbm.addTask(new Task(TaskStatus.DONE, "Таск 2"));

        fbm.addEpic(new Epic(TaskStatus.NEW, "Эпик 3"));
        fbm.addEpic(new Epic(TaskStatus.NEW, "Эпик 4"));

        fbm.addSubTask(new SubTask(TaskStatus.NEW, "сабтаск 5", 3));
        fbm.addSubTask(new SubTask(TaskStatus.NEW, "сабтаск 6", 3));
        fbm.addSubTask(new SubTask(TaskStatus.DONE, "сабтаск 7", 4));
        fbm.addSubTask(new SubTask(TaskStatus.DONE, "сабтаск 8", 4));

        FileBackedTaskManager fbm1 = loadFromFile(new File(path));

        if (fbm.getAll().size() == fbm1.getAll().size()) {
            for (int i = 0; i < fbm1.getAll().size(); i++) {
                if (fbm.getAll().get(i).equals(fbm1.getAll().get(i))) {
                } else {
                    throw new ManagerSaveException("Данные посл загрузки файла не совпадают");
                }
            }
        } else {
            throw new ManagerSaveException("Данные посл загрузки файла не совпадают");
        }
    }
}
