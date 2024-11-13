package Task;

public class Task {
    protected int id = -1;
    protected String name;
    protected TaskStatus status;

    public Task(TaskStatus status, String name) {
        this.status = status;
        this.name = name;
    }

    public Task(int id, String name, TaskStatus status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}' + "\n";
    }
}
