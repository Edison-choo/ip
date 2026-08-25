package alice;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

class TaskTest {

    @Test
    void ToDos_constructorAndToString_works() {
        ToDos ToDos = new ToDos("read book");
        assertEquals("read book", ToDos.getDescription());
        assertFalse(ToDos.isDone());
        assertEquals("[T][ ] read book", ToDos.toString());
    }

    @Test
    void ToDos_markAndUnmark_works() {
        ToDos ToDos = new ToDos("read book");
        ToDos.toggleStatus();
        assertTrue(ToDos.isDone());
        assertEquals("[T][X] read book", ToDos.toString());
        ToDos.toggleStatus();
        assertFalse(ToDos.isDone());
        assertEquals("[T][ ] read book", ToDos.toString());
    }

    @Test
    void Deadlines_constructorAndToString_works() {
        LocalDate date = LocalDate.parse("2024-12-25");
        Deadlines Deadlines = new Deadlines("return book", date);
        assertEquals("return book", Deadlines.getDescription());
        assertEquals(date, Deadlines.getBy());
        assertFalse(Deadlines.isDone());
        assertEquals("[D][ ] return book (by: Dec 25 2024)", Deadlines.toString());
    }

    @Test
    void Deadlines_markAndUnmark_works() {
        Deadlines Deadlines = new Deadlines("return book", LocalDate.parse("2024-12-25"));
        Deadlines.toggleStatus();
        assertTrue(Deadlines.isDone());
        assertEquals("[D][X] return book (by: Dec 25 2024)", Deadlines.toString());
        Deadlines.toggleStatus();
        assertFalse(Deadlines.isDone());
        assertEquals("[D][ ] return book (by: Dec 25 2024)", Deadlines.toString());
    }

    @Test
    void Events_constructorAndToString_works() {
        LocalDate from = LocalDate.parse("2024-12-20");
        LocalDate to = LocalDate.parse("2024-12-22");
        Events Events = new Events("conference", from, to);
        assertEquals("conference", Events.getDescription());
        assertEquals(from, Events.getFrom());
        assertEquals(to, Events.getTo());
        assertFalse(Events.isDone());
        assertEquals("[E][ ] conference (from: Dec 20 2024 to: Dec 22 2024)", Events.toString());
    }

    @Test
    void Events_markAndUnmark_works() {
        LocalDate from = LocalDate.parse("2024-12-20");
        LocalDate to = LocalDate.parse("2024-12-22");
        Events Events = new Events("conference", from, to);
        Events.toggleStatus();
        assertTrue(Events.isDone());
        assertEquals("[E][X] conference (from: Dec 20 2024 to: Dec 22 2024)", Events.toString());
        Events.toggleStatus();
        assertFalse(Events.isDone());
        assertEquals("[E][ ] conference (from: Dec 20 2024 to: Dec 22 2024)", Events.toString());
    }

    @Test
    void task_getStatusIcon_works() {
        ToDos ToDos = new ToDos("test");
        assertEquals(" ", ToDos.getStatusIcon());
        ToDos.toggleStatus();
        assertEquals("X", ToDos.getStatusIcon());
    }
}