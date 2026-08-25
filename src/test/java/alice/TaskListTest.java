package alice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;

class TaskListTest {

    private TaskList taskList;

    @BeforeEach
    void setUp() {
        // Reset the task list before each test
        taskList = new TaskList();
    }

    @Test
    void addAndSize_worksCorrectly() {
        assertEquals(0, taskList.size());
        assertTrue(taskList.isEmpty());

        taskList.add(new ToDos("read book"));
        assertEquals(1, taskList.size());
        assertFalse(taskList.isEmpty());

        taskList.add(new ToDos("buy milk"));
        assertEquals(2, taskList.size());

        taskList.add(new Deadlines("return book", LocalDate.parse("2024-12-25")));
        assertEquals(3, taskList.size());
    }

    @Test
    void get_returnsCorrectTask() {
        ToDos ToDos = new ToDos("read book");
        taskList.add(ToDos);
        taskList.add(new ToDos("buy milk"));

        Task retrieved = taskList.get(0);
        assertEquals(ToDos, retrieved);
        assertEquals("read book", retrieved.getDescription());

        // Test out-of-bounds
        assertThrows(IndexOutOfBoundsException.class, () -> taskList.get(99));
        assertThrows(IndexOutOfBoundsException.class, () -> taskList.get(-1));
    }

    @Test
    void remove_removesTaskAndReturnsIt() {
        ToDos ToDos1 = new ToDos("read book");
        ToDos ToDos2 = new ToDos("buy milk");
        taskList.add(ToDos1);
        taskList.add(ToDos2);

        assertEquals(2, taskList.size());

        Task removed = taskList.remove(0);
        assertEquals(ToDos1, removed);
        assertEquals(1, taskList.size());
        assertEquals(ToDos2, taskList.get(0));

        // Test out-of-bounds
        assertThrows(IndexOutOfBoundsException.class, () -> taskList.remove(99));
    }

    @Test
    void markAndUnmark_updatesStatusCorrectly() {
        taskList.add(new ToDos("read book"));
        taskList.add(new ToDos("buy milk"));

        // Initially both should be undone
        assertFalse(taskList.get(0).isDone());
        assertFalse(taskList.get(1).isDone());

        // Mark first task as done
        taskList.mark(0);
        assertTrue(taskList.get(0).isDone());
        assertFalse(taskList.get(1).isDone()); // Second unchanged

        // Unmark first task
        taskList.unmark(0);
        assertFalse(taskList.get(0).isDone());

        // Test out-of-bounds
        assertThrows(IndexOutOfBoundsException.class, () -> taskList.mark(99));
        assertThrows(IndexOutOfBoundsException.class, () -> taskList.unmark(99));
    }

    @Test
    void isValidIndex_worksCorrectly() {
        taskList.add(new ToDos("read book"));
        taskList.add(new ToDos("buy milk"));

        assertTrue(taskList.isValidIndex(0));
        assertTrue(taskList.isValidIndex(1));
        assertFalse(taskList.isValidIndex(2));
        assertFalse(taskList.isValidIndex(-1));
        assertFalse(taskList.isValidIndex(99));
    }

    @Test
    void getTasks_returnsInternalArrayList() {
        taskList.add(new ToDos("read book"));
        taskList.add(new ToDos("buy milk"));

        ArrayList<Task> tasks = taskList.getTasks();
        assertNotNull(tasks);
        assertEquals(2, tasks.size());
    }
}