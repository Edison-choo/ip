package alice;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Events extends Task {
    protected LocalDate from;
    protected LocalDate to;

    public Events(String description, LocalDate from, LocalDate to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getTo() {
        return to;
    }

    @Override
    public String getTaskTypeIcon() {
        return "E";
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy");
        return String.format("[E]%s (from: %s to: %s)", super.toString(),
                this.from.format(formatter), this.to.format(formatter));
    }

    public String getFromForFile() {
        return from.toString();
    }

    public String getToForFile() {
        return to.toString();
    }
}