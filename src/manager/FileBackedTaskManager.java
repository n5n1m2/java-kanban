package manager;

import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
        try(BufferedWriter br = new BufferedWriter(new FileWriter(file))) {
            br.write("id,type,name,status,epic,subTaskCount");
            br.newLine();
            for (Task task : super.getAll()) {
                br.write(task.toString());
                br.newLine();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) throws IOException {
        FileBackedTaskManager fb = new FileBackedTaskManager(File.createTempFile("Test", ".txt"));
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
    }
}
