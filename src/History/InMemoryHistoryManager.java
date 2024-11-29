package History;

import Task.*;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {


    private int historySize = 10;

    private List<Task> history = new ArrayList<>();


    @Override
    public void add(Task task) {
        if (history.size() > historySize) {
            history.removeFirst();
            history.add(task);

        } else {
            history.add(task);
        }

    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
