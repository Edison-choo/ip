package alice;

public enum Command {
    BYE,
    LIST,
    MARK,
    UNMARK,
    DELETE,
    TODO,
    DEADLINE,
    EVENT,
    UNKNOWN,
    VIEW;

    public static Command parse(String input) {
        if (input == null || input.trim().isEmpty()) {
            return UNKNOWN;
        }
        String lowerInput = input.trim().toLowerCase().split(" ")[0];
        switch (lowerInput) {
            case "bye":
                return BYE;
            case "list":
                return LIST;
            case "mark":
                return MARK;
            case "unmark":
                return UNMARK;
            case "delete":
                return DELETE;
            case "todo":
                return TODO;
            case "deadline":
                return DEADLINE;
            case "event":
                return EVENT;
            case "view":
                return VIEW;
            default:
                return UNKNOWN;
        }
    }

    public boolean isAddCommand() {
        return this == TODO || this == DEADLINE || this == EVENT;
    }

    public boolean requiresIndex() {
        return this == MARK || this == UNMARK || this == DELETE;
    }
}