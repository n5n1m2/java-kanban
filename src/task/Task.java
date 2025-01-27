package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import manager.TaskType;

public class Task {
    private static final TaskType taskType = TaskType.TASK;

    protected int id = -1;
    protected String name;
    protected TaskStatus status;
    protected Duration duration = Duration.ZERO;
    protected LocalDateTime startTime = LocalDateTime.of(1, 1 , 1, 0, 0);
    protected LocalDateTime endTime = startTime.plus(duration);
    protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

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

    public String getName() {
        return name;
    }


    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return String.format("%d,%S,%s,%s,%s,%s,%s",
                id,
                taskType,
                name,
                status,
                (duration.toDays() + ":" + duration.toHoursPart() + ":" + duration.toMinutesPart()),
                startTime.format(formatter),
                endTime.format(formatter)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status);
    }
}
