package alice;

/**
 * Enumerates the different types of tasks supported by the application.
 * Provides mapping between task instances and their corresponding type icons.
 */
public enum TaskType {
    TODO("[T]"),
    DEADLINE("[D]"),
    EVENT("[E]");

    private final String icon;

    TaskType(String icon) {
        this.icon = icon;
    }

    /**
     * Returns the icon string associated with this task type.
     *
     * @return The type icon (e.g., "[T]").
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Determines the TaskType of a given Task object.
     *
     * @param task The task instance.
     * @return The corresponding TaskType enum.
     * @throws IllegalArgumentException if the task type is unknown.
     */
    public static TaskType fromTask(Task task) {
        if (task instanceof ToDos) {
            return TODO;
        } else if (task instanceof Deadlines) {
            return DEADLINE;
        } else if (task instanceof Events) {
            return EVENT;
        } else {
            throw new IllegalArgumentException("Unknown task type");
        }
    }
}