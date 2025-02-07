package task;

import manager.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private static final TaskType taskType = TaskType.EPIC;
    private ArrayList<Integer> subTaskId = new ArrayList<>();

    public Epic(int id, String name, TaskStatus status, LocalDateTime startTime) {
        super(name, status, startTime);
        this.id = id;
    }

    protected Epic(String name, TaskStatus status, Duration duration, LocalDateTime startTime) {
        super(name, status, duration, startTime);
    }

    public Epic(String name, TaskStatus status, LocalDateTime startTime) {
        super(name, status, startTime);
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

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return String.format("%d,%S,%s,%s,%s,%s,%d",
                id,
                taskType,
                name,
                status,
                (duration.toDays() + ":" + duration.toHoursPart() + ":" + duration.toMinutesPart()),
                startTime.format(FORMATTER),
                getSubTaskCount()
        );
    }
}
