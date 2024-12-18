package history;

import task.Task;

import java.util.List;

public interface HistoryManager<T extends Task> {
    void add(T task);

    List<T> getHistory();

    void remove(int id);
}
