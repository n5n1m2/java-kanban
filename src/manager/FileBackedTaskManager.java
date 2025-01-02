package manager;

import task.Epic;
import task.SubTask;
import task.Task;

public class FileBackedTaskManager extends InMemoryTaskManager {

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

    }

}
