package alice;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

class TaskTypeTest {

    @Test
    void fromTask_returnsCorrectType() {
        Task ToDos = new ToDos("read book");
        Task Deadlines = new Deadlines("return book", LocalDate.parse("2024-12-25"));
        Task Events = new Events("meeting", LocalDate.parse("2024-12-20"), LocalDate.parse("2024-12-22"));

        assertEquals(TaskType.TODO, TaskType.fromTask(ToDos));
        assertEquals(TaskType.DEADLINE, TaskType.fromTask(Deadlines));
        assertEquals(TaskType.EVENT, TaskType.fromTask(Events));
    }

    @Test
    void getIcon_returnsCorrectString() {
        assertEquals("[T]", TaskType.TODO.getIcon());
        assertEquals("[D]", TaskType.DEADLINE.getIcon());
        assertEquals("[E]", TaskType.EVENT.getIcon());
    }
}