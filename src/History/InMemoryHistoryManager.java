package History;

import Task.*;

public class InMemoryHistoryManager implements HistoryManager {

    private  int counter = 0;
    private  int historySize = 10;

    private  Task[] history = new Task[historySize];

    @Override
    public  void add(Task task) {
        history[counter] = task;
        counter = (counter + 1) % historySize;

    }

    @Override
    public Task[] getHistory() {
        return history;
    }
}
