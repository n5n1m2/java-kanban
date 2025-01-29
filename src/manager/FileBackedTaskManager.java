package manager;

import exceptions.ManagerSaveException;
import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;


    private FileBackedTaskManager(File file) {
        this.file = file;
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
                String[] durationParts = sp[4].split(":");
                Duration duration = Duration.ofDays(Long.parseLong(durationParts[0]))
                        .plusHours(Long.parseLong(durationParts[1]))
                        .plusMinutes(Long.parseLong(durationParts[2]));
                LocalDateTime startTime = LocalDateTime.parse(sp[5], Task.FORMATTER);
                if (sp.length >= 7) {
                    epicIdOrSubTaskCount = Integer.parseInt(sp[6]);
                }
                fbm.id = id;
                switch (type) {
                    case TASK -> fbm.addTask(new Task(id, name, status, duration, startTime));
                    case EPIC -> fbm.addEpic(new Epic(id, name, status, startTime));
                    case SUBTASK ->
                            fbm.addSubTask(new SubTask(id, name, status, duration, startTime, epicIdOrSubTaskCount));
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
        } catch (NumberFormatException | IOException e) {
            throw new ManagerSaveException("Ошибка загрузки данных из файла");
        }
        return fbm;
    }

    public static void main(String[] args) throws IOException {
        File file = File.createTempFile("Test", ".txt");
        String path = file.getAbsolutePath();
        FileBackedTaskManager fbm = FileBackedTaskManager.loadFromFile(file);

        fbm.addTask(new Task("Таск 0", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now()));
        fbm.addTask(new Task("Таск 1", TaskStatus.IN_PROGRESS, Duration.ofMinutes(45), LocalDateTime.of(2024, 11, 15, 16, 30)));
        fbm.addTask(new Task("Таск 2", TaskStatus.DONE, Duration.ofMinutes(0), LocalDateTime.now()));

        fbm.addEpic(new Epic("Эпик 3", TaskStatus.NEW, LocalDateTime.now()));
        fbm.addEpic(new Epic("Эпик 4", TaskStatus.NEW, LocalDateTime.now()));

        fbm.addSubTask(new SubTask("Сабтаск 5", TaskStatus.NEW, Duration.ofHours(2), LocalDateTime.now(), 3));
        fbm.addSubTask(new SubTask("Сабтаск 6", TaskStatus.NEW, Duration.ofHours(5), LocalDateTime.now(), 4));
        fbm.addSubTask(new SubTask("Сабтаск 7", TaskStatus.DONE, Duration.ofHours(6), LocalDateTime.now(), 4));
        fbm.addSubTask(new SubTask("Сабтаск 8", TaskStatus.DONE, Duration.ofHours(2), LocalDateTime.now(), 4));

        FileBackedTaskManager fbm1 = loadFromFile(new File(path));

        if (fbm.getAll().size() == fbm1.getAll().size()) {
            for (int i = 0; i < fbm1.getAll().size(); i++) {
                System.out.println("FBM " + fbm.getAll().get(i) + " " + fbm1.getAll().get(i).getEndTime().format(Task.FORMATTER));
                System.out.println("FBM1 " + fbm1.getAll().get(i) + " " + fbm1.getAll().get(i).getEndTime().format(Task.FORMATTER));
                System.out.println("\n\n");
                if (!fbm.getAll().get(i).equals(fbm1.getAll().get(i))) {
                    throw new ManagerSaveException("Данные после загрузки файла не совпадают");
                }
            }
        } else {
            throw new ManagerSaveException("Данные после загрузки файла не совпадают");
        }
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
            bw.write("id,type,name,status,duration,startTime,epic,subTaskCount");
            bw.newLine();
            for (Task task : super.getAll()) {
                bw.write(task.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения данных");
        }
    }
}
