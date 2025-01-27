package manager;

import task.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {
    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubTask(SubTask subTask);

    ArrayList<Task> getAllTask();

    ArrayList<Epic> getAllEpic();

    ArrayList<SubTask> getAllSubTask();

    void removeAllTask();

    void removeAllEpic();

    void removeAllSubTask();

    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubtaskById(int id);

    Task getTaskById(int taskId);

    Epic getEpicById(int epicId);

    SubTask getSubtaskById(int subTaskId);

    void taskUpdate(Task task);

    void epicUpdate(Epic epic);

    void subTaskUpdate(SubTask subTask);

    List<SubTask> getEpicSubTask(int epicId);

    ArrayList<Task> getAll();

    TreeSet<Task> getPrioritizedTasks();

    void updateEpicStatus(Epic epic);

    List<Task> getHistory();

}
