package task;

import manager.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Epic {
    private static final TaskType taskType = TaskType.SUBTASK;

    protected int epicId;

    public SubTask(String name, TaskStatus status, Duration duration, LocalDateTime startTime, int epicId) {
        super(name, status, duration, startTime);
        this.epicId = epicId;
        this.endTime = this.startTime.plus(duration);
    }

    public SubTask(int id, String name, TaskStatus status, Duration duration, LocalDateTime startTime, int epicId) {
        super(id, name, status, startTime);
        this.epicId = epicId;
        this.duration = duration;
        this.endTime = this.startTime.plus(duration);
    }

    public int getEpicId() {
        return epicId;
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
                epicId
        );
    }
}
