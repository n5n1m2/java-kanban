package History;

import Task.Task;

public interface HistoryManager {
    void add(Task task);

    Task[] getHistory();
}
