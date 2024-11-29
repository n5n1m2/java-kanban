package History;

import Task.*;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private  int counter = 0;
    private  int historySize = 10;

    private  List<Task> history = new ArrayList<>();

   public InMemoryHistoryManager(){
        for (int i = 0; i < historySize; i++) {
            history.add(null);
        }
    }

    @Override
    public  void add(Task task) {
            history.set(counter, task);
            counter = (counter + 1) % historySize;
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
