package history;

import history.subclass.HistoryManagerLinkedList;
import task.*;

import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private HistoryManagerLinkedList<Task> history = new HistoryManagerLinkedList<>();


    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (history.contain(task.getId())) {
            history.removeById(task.getId());
            history.addLast(task, task.getId());
        } else {
            history.addLast(task, task.getId());
        }

    }

    @Override
    public List<Task> getHistory() {
        return history.getTasks();
    }

    @Override
    public void remove(int id) {
        history.removeById(id);
    }
}
