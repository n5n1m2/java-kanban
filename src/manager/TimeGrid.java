package manager;

import task.Task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.TreeMap;

public class TimeGrid<T extends Task> {
    private final TreeMap<LocalDateTime, Integer> timeGrid = new TreeMap<>();
    private final int step = 1; // Если шаг более 1, то возможны пересечения из-за округления времени.
    private final int empty = -1;

    public TimeGrid() {
        LocalDateTime whileStart = LocalDateTime.now().minusHours(4).truncatedTo(ChronoUnit.HOURS);
        LocalDateTime end = whileStart.plusYears(2);
        while (whileStart.isBefore(end)) {
            timeGrid.put(whileStart, empty);
            whileStart = whileStart.plusMinutes(step);
        }
    }

    public boolean add(T task) {
        if (task.getStartTime() == null) {
            return false;
        }
        if (timeIsAvailable(task)) {
            LocalDateTime start = getTime(task.getStartTime());
            while (start.isBefore(getTime(task.getEndTime()))) {
                timeGrid.put(start, task.getId());
                start = start.plusMinutes(step);
            }
            return true;
        }
        System.out.println("Таск адед");
        return false;
    }

    public boolean replace(T task, T oldTask) {
        if (task.getStartTime() == null || oldTask.getStartTime() == null) {
            return false;
        }
        return taskCanBeAdded(task) && remove(oldTask) && add(task);
    }

    public Boolean timeIsAvailable(T task) {
        if (task.getStartTime() == null) {
            return false;
        }
        LocalDateTime start = getTime(task.getStartTime());
        LocalDateTime end = getTime(task.getEndTime());

        return timeGrid.subMap(start, end).values()
                .stream()
                .allMatch(val -> (val == task.getId() && val != null) || (val == empty && val != null));
    }

    public TreeMap<LocalDateTime, Integer> getTimeGrid() {
        return timeGrid;
    }

    public boolean remove(T task) {
        if (task.getStartTime() == null) {
            return false;
        }
        boolean taskIsAdded = taskCanBeAdded(task);
        if (taskIsAdded) {
            LocalDateTime start = getTime(task.getStartTime());
            LocalDateTime end = getTime(task.getEndTime());
            while (start.isBefore(end)) {
                timeGrid.put(start, empty);
                start = start.plusMinutes(step);
            }
            return true;
        }
        return false;
    }

    private LocalDateTime getTime(LocalDateTime time) {
        if (step > 1) {
            int minutes = time.getMinute() / step * step;
            LocalDate localDate = time.toLocalDate();
            LocalTime localTime = LocalTime.of(time.getHour(), minutes);
            return LocalDateTime.of(localDate, localTime);
        } else {
            return time.truncatedTo(ChronoUnit.MINUTES);
        }

    }

    private Boolean taskCanBeAdded(T task) {
        if (task.getStartTime() == null) {
            return false;
        }
        LocalDateTime start = getTime(task.getStartTime());
        while (start.isBefore(getTime(task.getEndTime()))) {
            if (timeGrid.get(start) == task.getId() || timeGrid.get(start) == empty) {
                start = start.plusMinutes(step);
            } else {
                return false;
            }
        }
        return true;
    }
}
