public class Deadlines extends Task {
    protected String by;

    public Deadlines(String description, String by) {
        super(description);
        this.by = by;
    }

    public String getBy() {
        return by;
    }

    @Override
    public String getTaskTypeIcon() {
        return "D";
    }

    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(), this.by);
    }
}