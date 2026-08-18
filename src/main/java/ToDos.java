public class ToDos extends Task {
    public ToDos(String description) {
        super(description);
    }

    @Override
    public String getTaskTypeIcon() {
        return "T";
    }

    @Override
    public String toString() {
        return String.format("[T]%s", super.toString());
    }
}
