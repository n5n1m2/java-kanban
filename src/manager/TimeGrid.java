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
        LocalDateTime whileStart = LocalDateTime.now().minusYears(0).truncatedTo(ChronoUnit.HOURS);
        LocalDateTime end = whileStart.plusYears(2);
        while (whileStart.isBefore(end)) { // Из-за небольшого шага заполнение таблицы занимает время (приблизительно секунду).
            // Если такое время не подходит, то допускается ли ограничить пользователя во времени, которое он может указать?
            // Например, допускается ли вариант с добавлением округления времени в конструктор тасков, чтобы можно было задать время только кратное переменной step?
            timeGrid.put(whileStart, empty);
            whileStart = whileStart.plusMinutes(step);
        }
    }

    public void add(T task) {
        if (task.getStartTime() == null) {
            return;
        }
        if (timeIsAvailable(task)) {
            LocalDateTime start = getTime(task.getStartTime());
            while (start.isBefore(getTime(task.getEndTime()))) {
                timeGrid.put(start, task.getId());
                start = start.plusMinutes(step);
            }
        }
    }

    public boolean replace(T task, T oldTask) {
        if (task.getStartTime() == null || oldTask.getStartTime() == null) {
            return false;
        }
        boolean taskIsAdded = taskCanBeAdded(task);
        if (taskIsAdded) {
            remove(oldTask);
            LocalDateTime start = getTime(task.getStartTime());
            while (start.isBefore(oldTask.getEndTime())) {
                timeGrid.put(start, task.getId());
                start = start.plusMinutes(step);
            }
            add(task);
            return true;
        } else {
            return false;
        }
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
        if (step > 1) { // Округляет минуты в меньшую сторону, если установить шаг более 1.
            int minutes = time.getMinute() / step * step;
            LocalDate localDate = time.toLocalDate();
            LocalTime localTime = LocalTime.of(time.getHour(), minutes);
            return LocalDateTime.of(localDate, localTime);
        } else {
            return time.truncatedTo(ChronoUnit.MINUTES);
        }

    }

    private Boolean taskCanBeAdded(T task) { // возвращает true, если на промежутке время занято объектом с тем же id
        LocalDateTime start = getTime(task.getStartTime());
        while (start.isBefore(getTime(task.getEndTime()))) {
            if (timeGrid.get(start) == task.getId()) {
                start = start.plusMinutes(step);
            } else {
                return false;
            }
        }
        return true;
    }
}
