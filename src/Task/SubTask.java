package Task;

public class SubTask extends Epic{
    protected int epicId;

    public SubTask(TaskStatus status, String name, int epicId) { // SubTask - часть Epic, поэтому для создания запрашивается epicId
        super(status, name);
        this.epicId = epicId;
    }

    public SubTask(int id, String name, TaskStatus status, int epicId) {
        super(id, name, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "status=" + status +
                ", name='" + name + '\'' +
                ", id=" + id +
                ", epicId=" + epicId +
                '}' + "\n";
    }
}
