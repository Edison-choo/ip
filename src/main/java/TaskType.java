public enum TaskType {
    TODO("[T]"),
    DEADLINE("[D]"),
    EVENT("[E]");

    private final String icon;

    TaskType(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

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