package manager;


import history.*;
import task.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class InMemoryTaskManager implements TaskManager {

    protected int id = 0;
    private HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private HashMap<Integer, SubTask> subTaskHashMap = new HashMap<>();
    private HistoryManager<Task> historyManager = Managers.getDefaultHistory();


    @Override
    public void addTask(Task task) {
        task.setId(id++);
        taskHashMap.put(task.getId(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(id++);
        epicHashMap.put(epic.getId(), epic);
    }

    @Override
    public void addSubTask(SubTask subTask) {
        if (epicHashMap.containsKey(subTask.getEpicId())) {
            subTask.setId(id++);
            subTaskHashMap.put(subTask.getId(), subTask);
            Epic epic = epicHashMap.get(subTask.getEpicId());
            epic.addSubTaskId(subTask.getId());
            updateEpicStatus(epic);
        }
    }

    @Override
    public ArrayList<Task> getAllTask() {
        return new ArrayList<>(taskHashMap.values());
    }

    @Override
    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epicHashMap.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubTask() {
        return new ArrayList<>(subTaskHashMap.values());
    }

    @Override
    public void removeAllTask() {
        taskHashMap.clear();
    }

    @Override
    public void removeAllEpic() {
        epicHashMap.clear();
        subTaskHashMap.clear();
    }

    @Override
    public void removeAllSubTask() {
        for (Epic value : epicHashMap.values()) {
            value.removeAllSubTask();
            updateEpicStatus(value);
        }
        subTaskHashMap.clear();
    }

    @Override
    public void deleteTaskById(int id) {
        historyManager.remove(id);
        taskHashMap.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epicHashMap.get(id);
        for (Integer i : epic.getSubTaskId()) {
            subTaskHashMap.remove(i);
            historyManager.remove(i);
        }
        epic.removeAllSubTask();
        historyManager.remove(id);
        epicHashMap.remove(epic.getId());
    }

    @Override
    public void deleteSubtaskById(int id) {
        SubTask subTask = subTaskHashMap.get(id);
        Epic epic = epicHashMap.get(subTask.getEpicId());
        epic.removeSubTask(subTask.getId());
        subTaskHashMap.remove(subTask.getId());
        updateEpicStatus(epic);
        historyManager.remove(id);
    }

    @Override
    public Task getTaskById(int taskId) {
        historyManager.add(taskHashMap.getOrDefault(taskId, null));
        return taskHashMap.getOrDefault(taskId, null);
    }

    @Override
    public Epic getEpicById(int epicId) {
        historyManager.add(epicHashMap.getOrDefault(epicId, null));
        return epicHashMap.getOrDefault(epicId, null);
    }

    @Override
    public SubTask getSubtaskById(int subTaskId) {
        historyManager.add(subTaskHashMap.getOrDefault(subTaskId, null));
        return subTaskHashMap.getOrDefault(subTaskId, null);
    }

    @Override
    public void taskUpdate(Task task) {
        taskHashMap.replace(task.getId(), task);
    }

    @Override
    public void epicUpdate(Epic epic) {
        epicHashMap.replace(epic.getId(), epic);
        updateEpicStatus(epic);
    }

    @Override
    public void subTaskUpdate(SubTask subTask) {
        if (subTask.getEpicId() == subTaskHashMap.get(subTask.getId()).getEpicId()) {
            subTaskHashMap.replace(subTask.getId(), subTask);
        } else {
            Epic epic = epicHashMap.get(subTaskHashMap.get(subTask.getId()).getEpicId());
            epic.removeSubTask(subTask.getId());
            subTaskHashMap.replace(subTask.getId(), subTask);
            epic = epicHashMap.get(subTask.getEpicId());
            epic.addSubTaskId(subTask.getId());
        }
        updateEpicStatus(epicHashMap.get(subTask.getEpicId()));
    }

    @Override
    public ArrayList<SubTask> getEpicSubTask(int epicId) {
        ArrayList<SubTask> subTasks = new ArrayList<>();
        for (Integer i : epicHashMap.get(epicId).getSubTaskId()) {
            subTasks.add(subTaskHashMap.get(i));

        }
        return subTasks;
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        int counter = 0;
        for (Integer i : epic.getSubTaskId()) {
            if (subTaskHashMap.get(i).getStatus() == TaskStatus.DONE) {
                counter++;
            }
        }
        if (counter == 0) {
            epic.setEpicStatus(TaskStatus.NEW);
        } else if (counter == epic.getSubTaskCount()) {
            epic.setEpicStatus(TaskStatus.DONE);
        } else if (counter < epic.getSubTaskCount()) {
            epic.setEpicStatus(TaskStatus.IN_PROGRESS);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public ArrayList<Task> getAll() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.addAll(getAllTask());
        tasks.addAll(getAllEpic());
        tasks.addAll(getAllSubTask());
        return tasks;
    }

//    protected void addEpicFromFile(Epic epic, int id) {
//        if (id > this.id) {
//            this.id = (id + 1);
//        }
//        epicHashMap.put(id, epic);
//    }
//
//    protected void addTaskFormFile(Task task, int id){
//        if (id > this.id) {
//            this.id = (id + 1);
//        }
//        taskHashMap.put(id, task);
//    }
//
//    protected void addSubTaskFromFile(SubTask subTask, int id) {
//        if (id > this.id) {
//            this.id = (id + 1);
//        }
//        if (epicHashMap.containsKey(subTask.getEpicId())) {
//            subTask.setId(id);
//            subTaskHashMap.put(subTask.getId(), subTask);
//            Epic epic = epicHashMap.get(subTask.getEpicId());
//            epic.addSubTaskId(subTask.getId());
//            updateEpicStatus(epic);
//        }
//    }
}


