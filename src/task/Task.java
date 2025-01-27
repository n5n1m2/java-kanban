package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import manager.TaskType;

public class Task {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private static final TaskType taskType = TaskType.TASK;

    protected int id = -1;
    protected String name;
    protected TaskStatus status;
    protected Duration duration = Duration.ZERO;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;


    public Task(String name, TaskStatus status, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = startTime.plus(duration);
    }


    protected Task(String name, TaskStatus status, LocalDateTime startTime) {
        this.name = name;
        this.status = status;
        this.startTime = startTime;
        this.endTime = this.startTime.plus(duration);
    }

    public Task(int id, String name, TaskStatus status, Duration duration, LocalDateTime startTime) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = this.startTime.plus(duration);
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
        return String.format("%d,%S,%s,%s,%s,%s",
                id,
                taskType,
                name,
                status,
                (duration.toDays() + ":" + duration.toHoursPart() + ":" + duration.toMinutesPart()),
                startTime.format(FORMATTER)
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
