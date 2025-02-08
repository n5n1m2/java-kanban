package manager;

import history.HistoryManager;
import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;


public class InMemoryTaskManager implements TaskManager {

    protected int id = 0;
    private HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private HashMap<Integer, SubTask> subTaskHashMap = new HashMap<>();
    private HistoryManager<Task> historyManager = Managers.getDefaultHistory();
    private TreeSet<Task> sortedTasks = new TreeSet<>(Task.compareByStartTime());
    private TimeGrid<Task> timeGrid = new TimeGrid<Task>();

    @Override
    public boolean addTask(Task task) {
        task.setId(id++);
        if (task.getStartTime() != null) {
            if (timeGrid.timeIsAvailable(task)) {
                sortedTasks.add(task);
                taskHashMap.put(task.getId(), task);
                timeGrid.add(task);
                return true;
            } else {
                return false;
            }
        } else {
            taskHashMap.put(task.getId(), task);
            return true;
        }
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(id++);
        if (epic.getStartTime() != null) {
            epicHashMap.put(epic.getId(), epic);
            sortedTasks.add(epic);
        } else {
            epicHashMap.put(epic.getId(), epic);
        }
    }

    @Override
    public boolean addSubTask(SubTask subTask) {
        subTask.setId(id++);
        if (subTask.getStartTime() != null && epicHashMap.containsKey(subTask.getEpicId())) {
            if (!timeGrid.timeIsAvailable(subTask)) {
                return false;
            }
            subTaskHashMap.put(subTask.getId(), subTask);
            Epic epic = epicHashMap.get(subTask.getEpicId());
            epic.addSubTaskId(subTask.getId());
            sortedTasks.add(subTask);
            timeGrid.add(subTask);
            updateEpicStatus(epic);
            return true;
        } else if (epicHashMap.containsKey(subTask.getEpicId())) {
            subTaskHashMap.put(subTask.getId(), subTask);
            Epic epic = epicHashMap.get(subTask.getEpicId());
            epic.addSubTaskId(subTask.getId());
            updateEpicStatus(epic);
            return true;
        }
        return false;
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
        if (taskHashMap.get(id).getStartTime() != null) {
            timeGrid.remove(taskHashMap.get(id));
            sortedTasks.remove(taskHashMap.get(id));
        }
        historyManager.remove(id);
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
        epicHashMap.remove(epic.getId());
    }

    @Override
    public void deleteSubtaskById(int id) {
        SubTask subTask = subTaskHashMap.get(id);
        Epic epic = epicHashMap.get(subTask.getEpicId());
        epic.removeSubTask(subTask.getId());
        if (subTask.getStartTime() != null) {
            sortedTasks.remove(subTask);
            timeGrid.remove(subTaskHashMap.get(id));
        }
        subTaskHashMap.remove(subTask.getId());
        updateEpicStatus(epic);
        historyManager.remove(id);
    }

    @Override
    public Task getTaskById(int taskId) {
        historyManager.add(taskHashMap.getOrDefault(taskId, null));
        return taskHashMap.get(taskId);
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
    public boolean taskUpdate(Task task) {
        if (task.getStartTime() != null && timeGrid.replace(task, taskHashMap.get(task.getId()))) {
            sortedTasks.remove(taskHashMap.get(task.getId()));
            sortedTasks.add(task);
            taskHashMap.replace(task.getId(), task);
            return true;
        } else if (task.getStartTime() == null) {
            sortedTasks.remove(taskHashMap.get(task.getId()));
            taskHashMap.replace(task.getId(), task);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean epicUpdate(Epic epic) {
        if (epic.getStartTime() != null) {
            sortedTasks.remove(epicHashMap.get(epic.getId()));
            sortedTasks.add(epic);
            epicHashMap.replace(epic.getId(), epic);
            updateEpicStatus(epic);
            return true;
        } else {
            sortedTasks.remove(epicHashMap.get(epic.getId()));
            epicHashMap.replace(epic.getId(), epic);
            updateEpicStatus(epic);
            return true;
        }
    }

    @Override
    public boolean subTaskUpdate(SubTask subTask) {
        if (subTask.getStartTime() != null && timeGrid.timeIsAvailable(subTask)) {
            if (subTask.getEpicId() == subTaskHashMap.get(subTask.getId()).getEpicId()) {
                sortedTasks.remove(subTaskHashMap.get(subTask.getId()));
                sortedTasks.add(subTask);
                timeGrid.replace(subTask, subTaskHashMap.get(subTask.getId()));
                subTaskHashMap.replace(subTask.getId(), subTask);
                updateEpicStatus(epicHashMap.get(subTask.getEpicId()));
                return true;
            } else {
                Epic epic = epicHashMap.get(subTaskHashMap.get(subTask.getId()).getEpicId());
                epic.removeSubTask(subTask.getId());
                timeGrid.replace(subTask, subTaskHashMap.get(subTask.getId()));
                subTaskHashMap.replace(subTask.getId(), subTask);
                sortedTasks.remove(subTaskHashMap.get(subTask.getId()));
                sortedTasks.add(subTask);
                epic = epicHashMap.get(subTask.getEpicId());
                epic.addSubTaskId(subTask.getId());
                updateEpicStatus(epicHashMap.get(subTask.getEpicId()));
                return true;
            }

        } else if (subTask.getStartTime() != null && !timeGrid.timeIsAvailable(subTask)) {
            return false;
        } else if (subTask.getEpicId() == subTaskHashMap.get(subTask.getId()).getEpicId()) {
            subTaskHashMap.replace(subTask.getId(), subTask);
            updateEpicStatus(epicHashMap.get(subTask.getEpicId()));
            return true;
        } else {
            Epic epic = epicHashMap.get(subTaskHashMap.get(subTask.getId()).getEpicId());
            epic.removeSubTask(subTask.getId());
            subTaskHashMap.replace(subTask.getId(), subTask);
            epic = epicHashMap.get(subTask.getEpicId());
            epic.addSubTaskId(subTask.getId());
            updateEpicStatus(epicHashMap.get(subTask.getEpicId()));
            return true;
        }
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
                .filter(val -> val.getValue().getStartTime() != null)
                .map(map -> map.getValue().getEndTime())
                .max(LocalDateTime::compareTo)
                .orElse(null));
    }
}


