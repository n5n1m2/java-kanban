package manager;

import history.HistoryManager;
import history.InMemoryHistoryManager;
import task.Task;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager<Task> getDefaultHistory() {
        return new InMemoryHistoryManager<>();
    }
}
