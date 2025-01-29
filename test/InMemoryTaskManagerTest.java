import manager.*;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
class InMemoryTaskManagerTest extends TaskManagerTest<TaskManager> {

    @BeforeEach
    public void setTaskManagerAndSetTime() {
        super.setTaskManager(Managers.getDefault(), LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
    }
}
