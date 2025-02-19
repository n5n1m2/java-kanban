package http.handlers.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationAdapter extends TypeAdapter<Duration> {
    @Override
    public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
        jsonWriter.value(duration.toDays() + ":" + duration.toHoursPart() + ":" + duration.toMinutesPart());
    }

    @Override
    public Duration read(JsonReader jsonReader) throws IOException {
        String[] durationParts = jsonReader.nextString().split(":");

        Duration duration = Duration.ofDays(Long.parseLong(durationParts[0]))
                .plusHours(Long.parseLong(durationParts[1]))
                .plusMinutes(Long.parseLong(durationParts[2]));
        return duration;
    }
}
