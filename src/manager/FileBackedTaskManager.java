package manager;

import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;

import java.io.*;

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
            System.out.println(e);
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
        File tf = File.createTempFile("Test", ".txt");
        FileBackedTaskManager fb = new FileBackedTaskManager(tf);
        Task task = new Task(TaskStatus.NEW, "Таск 0");
        fb.addTask(task);
        task = new Task(TaskStatus.NEW, "Таск 1");
        fb.addTask(task);

        Epic epic = new Epic(TaskStatus.NEW, "Эпик 2");
        fb.addEpic(epic);

        SubTask subTask = new SubTask(TaskStatus.NEW, "СабТаск 3", 2);
        fb.addSubTask(subTask);
        subTask = new SubTask(TaskStatus.NEW, "СабТаск 4", 2);
        fb.addSubTask(subTask);
        subTask = new SubTask(TaskStatus.NEW, "СабТаск 5", 2);
        fb.addSubTask(subTask);
        System.out.println("\n\n\n" + fb.getAll() + "\n fb \n\n\n");

        String filePath = tf.getAbsolutePath();

        FileBackedTaskManager fb1 = loadFromFile(new File(filePath));

        System.out.println(fb1.getAll());
    }
}
