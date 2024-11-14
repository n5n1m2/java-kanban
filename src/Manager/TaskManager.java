package Manager;

import Task.*;

import java.util.ArrayList;
import java.util.HashMap;


public class TaskManager {
    private static int id = 0;
    private static HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private static HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private static HashMap<Integer, SubTask> subTaskHashMap = new HashMap<>();

    public void addTask(Task task) {
        task.setId(id++);
        taskHashMap.put(task.getId(), task);

    }

    public void addEpic(Epic epic) {
        epic.setId(id++);
        epicHashMap.put(epic.getId(), epic);

    }

    public void addSubTask(SubTask subTask) {
        subTask.setId(id++);
        subTaskHashMap.put(subTask.getId(), subTask);
        Epic epic = epicHashMap.get(subTask.getEpicId());
        epic.addSubTaskId(subTask.getId());
        updateEpicStatus(epic);
    }

    public ArrayList<Task> getAllTask() {
        return new ArrayList<>(taskHashMap.values());
    }

    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epicHashMap.values());
    }

    public ArrayList<SubTask> getAllSubTask() {
        return new ArrayList<>(subTaskHashMap.values());
    }

    public void removeAllTask() {
        taskHashMap.clear();
    }

    public void removeAllEpic() {
        epicHashMap.clear();
        subTaskHashMap.clear(); //Т.к SubTask - часть Epic, то при удалении Epic также удаляем SubTask
    }

    public void removeAllSubTask() {
        for (Epic value : epicHashMap.values()) {
            value.removeAllSubTask();
            updateEpicStatus(value);
        }
        subTaskHashMap.clear();
    }


    public void deleteTaskById(int id) {
        taskHashMap.remove(id);
    }

    public void deleteEpicById(int id) {
        Epic epic = epicHashMap.get(id);
        for (Integer i : epic.getSubTaskId()) {
            subTaskHashMap.remove(i);
        }
        epic.removeAllSubTask();
        epicHashMap.remove(epic.getId());
    }

    public void deleteSubtaskById(int id) {
        SubTask subTask = subTaskHashMap.get(id);
        Epic epic = epicHashMap.get(subTask.getEpicId());
        epic.removeSubTask(subTask.getId());
        subTaskHashMap.remove(subTask.getId());
        updateEpicStatus(epic);
    }

    public Task getTaskById(int taskId) {
        return taskHashMap.get(taskId);
    }

    public Epic getEpicById(int epicId) {
        return epicHashMap.get(epicId);
    }

    public SubTask getSubtaskById(int subTaskId) {
        return subTaskHashMap.get(subTaskId);
    }


    public void taskUpdate(Task task) {
        taskHashMap.replace(task.getId(), task);
    }

    public void epicUpdate(Epic epic) {
        epicHashMap.replace(epic.getId(), epic);
        updateEpicStatus(epic);
    }

    public void subTaskUpdate(SubTask subTask) {
        if (subTask.getEpicId() == subTaskHashMap.get(subTask.getId()).getEpicId()) { // Проверяем, изменился ли Epic id.
            subTaskHashMap.replace(subTask.getId(), subTask); // Если не изменился - просто заменяем объкт.
        } else {
            Epic epic = epicHashMap.get(subTaskHashMap.get(subTask.getId()).getEpicId()); // Иначе получаем из HashMap старый Epic по epicId старого SubTask
            epic.removeSubTask(subTask.getId()); // удаляем из списка старого эпика SubTask
            subTaskHashMap.replace(subTask.getId(), subTask);
            epic = epicHashMap.get(subTask.getEpicId()); // Получаем новый Epic
            epic.addSubTaskId(subTask.getId()); // Добавляем в список нового эпика id subtask
        }
        updateEpicStatus(epicHashMap.get(subTask.getEpicId())); // Повторная проверка статуса эпика.
    }

    public ArrayList<SubTask> getEpicSubTask(int epicId) {
        ArrayList<SubTask> subTasks = new ArrayList<>();
        for (Integer i : epicHashMap.get(epicId).getSubTaskId()) {
            subTasks.add(subTaskHashMap.get(i));

        }
        return subTasks;
    }

    private void updateEpicStatus(Epic epic) {
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


}


