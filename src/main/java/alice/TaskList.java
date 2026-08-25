package alice;

import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks != null ? tasks : new ArrayList<>();
    }

    public int size() {
        return tasks.size();
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    public Task get(int index) {
        if (index < 0 || index >= tasks.size()) {
            throw new IndexOutOfBoundsException("Task index out of range");
        }
        return tasks.get(index);
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public Task remove(int index) {
        if (index < 0 || index >= tasks.size()) {
            throw new IndexOutOfBoundsException("Task index out of range");
        }
        return tasks.remove(index);
    }

    public void mark(int index) {
        get(index).toggleStatus();
    }

    public void unmark(int index) {
        get(index).toggleStatus();
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Checks if a given index is valid.
     *
     * @param index The 0-based index to check.
     * @return true if the index is within bounds.
     */
    public boolean isValidIndex(int index) {
        return index >= 0 && index < tasks.size();
    }
}