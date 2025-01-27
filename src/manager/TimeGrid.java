package manager;

import task.Task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.TreeMap;

public class TimeGrid<T extends  Task> {
    private LocalDateTime start = LocalDateTime.now().minusYears(1).truncatedTo(ChronoUnit.MINUTES);
    private final LocalDateTime end = start.plusYears(1);
    private final TreeMap<LocalDateTime, Boolean> timeGrid = new TreeMap<>();
    private final int step = 15;

public TimeGrid(){
    while(start.isBefore(end)){
        timeGrid.put(start, true);
        start = start.plusMinutes(step);
    }
}

public void add(T task){
    if (task.getStartTime() == null){
        return;
    }

    int minutes = task.getStartTime().getMinute()/step*step;
    LocalDate localDate = task.getStartTime().toLocalDate();
    LocalTime localTime = LocalTime.of(task.getStartTime().getHour(), minutes);
    timeGrid.put(LocalDateTime.of(localDate, localTime), false);
}

public Boolean timeIsAvailable(Task task){
    if (task.getStartTime() == null){
        return false;
    }
    LocalDateTime time = task.getStartTime();
    int minutes = time.getMinute()/step*step;
    LocalDateTime start = LocalDateTime.of(time.toLocalDate(), LocalTime.of(time.getHour(), minutes));
    while (start.isBefore(task.getEndTime())){
        if (!timeGrid.get(start)) {
            return false;
        }
        start = start.plusMinutes(step);
    }
    return true;
}

public void remove(Task task){
    if (task.getStartTime() == null){
        return;
    }
    LocalDateTime time = task.getStartTime();
    int minutes = time.getMinute()/step*step;
    LocalDateTime start = LocalDateTime.of(time.toLocalDate(), LocalTime.of(time.getHour(), minutes));
    timeGrid.replace(start, true);
}

    public TreeMap<LocalDateTime, Boolean> getTimeGrid() {
        return timeGrid;
    }
}
