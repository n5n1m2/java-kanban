package manager;


import history.*;
import task.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class InMemoryTaskManager implements TaskManager {

    protected int id = 0;
    private HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private HashMap<Integer, SubTask> subTaskHashMap = new HashMap<>();
    private HistoryManager<Task> historyManager = Managers.getDefaultHistory();
    private TreeSet<Task> sortedTasks = new TreeSet<>(Task.compareByStartTime());
    private TimeGrid<Task> timeGrid = new TimeGrid<Task>();

    @Override
    public void addTask(Task task) {
        if (task.getStartTime() != null) {
            task.setId(id++);
            if (timeGrid.timeIsAvailable(task)) {
                sortedTasks.add(task);
                taskHashMap.put(task.getId(), task);
                timeGrid.add(task);
            }
        }
    }

    @Override
    public void addEpic(Epic epic) { // Epic в timeGrid не сохраняется т.к его время выполнения зависит от сабтасков. Сабтаски в timegrid добавляются отдельно.
        if (epic.getStartTime() != null) {
                epic.setId(id++);
                epicHashMap.put(epic.getId(), epic);
                sortedTasks.add(epic);
            }
    }

    @Override
    public void addSubTask(SubTask subTask) {
        if (subTask.getStartTime() != null) {
            subTask.setId(id++);
            if (timeGrid.timeIsAvailable(subTask)) {
                if (epicHashMap.containsKey(subTask.getEpicId())) {
                    subTaskHashMap.put(subTask.getId(), subTask);
                    Epic epic = epicHashMap.get(subTask.getEpicId());
                    epic.addSubTaskId(subTask.getId());
                    sortedTasks.add(subTask);
                    timeGrid.add(subTask);
                    updateEpicStatus(epic);
                }
            }
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
        taskHashMap.values().forEach(obj -> timeGrid.remove(obj));
        taskHashMap.clear();
    }

    @Override
    public void removeAllEpic() {
        epicHashMap.clear();
        subTaskHashMap.values().forEach(obj -> timeGrid.remove(obj));
        subTaskHashMap.clear();
    }

    @Override
    public void removeAllSubTask() {
        epicHashMap.values()
                .forEach(epic -> {
                    epic.removeAllSubTask();
                    updateEpicStatus(epic);
                });
        subTaskHashMap.values().forEach(obj -> timeGrid.remove(obj));
        subTaskHashMap.clear();
    }

    @Override
    public void deleteTaskById(int id) {
        historyManager.remove(id);
        timeGrid.remove(taskHashMap.get(id));
        sortedTasks.remove(taskHashMap.get(id));
        taskHashMap.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epicHashMap.get(id);
        sortedTasks.remove(epic);
        epic.getSubTaskId().forEach(
                subId -> {
                    timeGrid.remove(subTaskHashMap.get(subId));
                    subTaskHashMap.remove(subId);
                    historyManager.remove(subId);
                }
        );
        epic.removeAllSubTask();
        historyManager.remove(id);
        timeGrid.remove(epicHashMap.get(id));
        epicHashMap.remove(epic.getId());
    }

    @Override
    public void deleteSubtaskById(int id) {
        SubTask subTask = subTaskHashMap.get(id);

        Epic epic = epicHashMap.get(subTask.getEpicId());
        epic.removeSubTask(subTask.getId());
        sortedTasks.remove(subTask);
        timeGrid.remove(subTaskHashMap.get(id));
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
        if (task.getStartTime() != null) {
            if (timeGrid.replace(task, taskHashMap.get(task.getId()))){
                sortedTasks.remove(taskHashMap.get(task.getId()));
                sortedTasks.add(task);
                taskHashMap.replace(task.getId(), task);
            }
        }
    }

    @Override
    public void epicUpdate(Epic epic) { // Epic в timeGrid не сохраняется т.к его время выполнения зависит от сабтасков. Сабтаски в timegrid добавляются отдельно.
        if (epic.getStartTime() != null) {
                sortedTasks.remove(epicHashMap.get(epic.getId()));
                sortedTasks.add(epic);
                epicHashMap.replace(epic.getId(), epic);
        }
        updateEpicStatus(epic);
    }

    @Override
    public void subTaskUpdate(SubTask subTask) {
        if (subTask.getStartTime() != null) {
            if (timeGrid.timeIsAvailable(subTask)) {
                if (subTask.getEpicId() == subTaskHashMap.get(subTask.getId()).getEpicId()) {
                    sortedTasks.remove(subTaskHashMap.get(subTask.getId()));
                    sortedTasks.add(subTask);
                    timeGrid.replace(subTask,subTaskHashMap.get(subTask.getId()));
                    subTaskHashMap.replace(subTask.getId(), subTask);
                } else {
                    Epic epic = epicHashMap.get(subTaskHashMap.get(subTask.getId()).getEpicId());
                    epic.removeSubTask(subTask.getId());
                    timeGrid.replace(subTask,subTaskHashMap.get(subTask.getId()));
                    subTaskHashMap.replace(subTask.getId(), subTask);
                    sortedTasks.remove(subTaskHashMap.get(subTask.getId()));
                    sortedTasks.add(subTask);
                    epic = epicHashMap.get(subTask.getEpicId());
                    epic.addSubTaskId(subTask.getId());
                }
            }
        }
        updateEpicStatus(epicHashMap.get(subTask.getEpicId()));
    }

    @Override
    public List<SubTask> getEpicSubTask(int epicId) {
        return epicHashMap.get(epicId)
                .getSubTaskId()
                .stream()
                .map(subTaskHashMap::get)
                .collect(Collectors.toList());
    }


    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public ArrayList<Task> getAll() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.addAll(getAllTask());
        tasks.addAll(getAllEpic());
        tasks.addAll(getAllSubTask());
        return tasks;
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return sortedTasks;
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        updEpicTaskStatus(epic);
        epicUpdateDuration(epic);
        epicUpdateEndTime(epic);
    }

    private void updEpicTaskStatus(Epic epic) {
        int counter = 0;
        for (Integer i : epic.getSubTaskId()) {
            if (subTaskHashMap.get(i).getStatus() == TaskStatus.DONE) {
                counter = counter + 2;
            } else if (subTaskHashMap.get(i).getStatus() == TaskStatus.IN_PROGRESS) {
                counter++;
            }
        }
        if (counter == 0) {
            epic.setEpicStatus(TaskStatus.NEW);
        } else if (counter == epic.getSubTaskCount() * 2) {
            epic.setEpicStatus(TaskStatus.DONE);
        } else if (counter < epic.getSubTaskCount() * 2) {
            epic.setEpicStatus(TaskStatus.IN_PROGRESS);
        }
    }

    private void epicUpdateDuration(Epic epic) {
        epic.setDuration(subTaskHashMap
                .entrySet()
                .stream()
                .filter(entry -> epic.getSubTaskId().contains(entry.getKey()))
                .map(map -> map.getValue().getDuration())
                .reduce(Duration.ZERO, Duration::plus));
    }

    private void epicUpdateEndTime(Epic epic) {
        epic.setEndTime(subTaskHashMap
                .entrySet()
                .stream()
                .filter(entry -> epic.getSubTaskId().contains(entry.getKey()))
                .map(map -> map.getValue().getEndTime())
                .max(LocalDateTime::compareTo)
                .orElse(null));
    }
}


