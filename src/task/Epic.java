package task;

import manager.TaskType;

import java.util.ArrayList;

public class Epic extends Task {
    public static final TaskType tasktype = TaskType.EPIC;

    private ArrayList<Integer> subTaskId = new ArrayList<>();

    public Epic(TaskStatus status, String name) {
        super(status, name);
    }

    public Epic(int id, String name, TaskStatus status) {
        super(id, name, status);
    }

    public void addSubTaskId(int subTaskId) {
        this.subTaskId.add(subTaskId);
    }

    public void removeAllSubTask() {
        subTaskId.clear();
    }

    public ArrayList<Integer> getSubTaskId() {
        return subTaskId;
    }

    public void removeSubTask(int id) {
        subTaskId.remove((Integer) id);
    }

    public int getSubTaskCount() {
        return subTaskId.size();
    }

    public void setEpicStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTaskId=" + subTaskId +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", SubTaskCount=" + subTaskId.size() +
                '}'
                + "\n";
    }
}
