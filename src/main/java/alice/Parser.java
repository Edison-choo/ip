package alice;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Parser {

    public static Command parseCommand(String fullCommand) {
        if (fullCommand == null || fullCommand.trim().isEmpty()) {
            return Command.UNKNOWN;
        }
        String[] parts = fullCommand.trim().split(" ");
        return Command.parse(parts[0]);
    }

    public static String extractDescription(String fullCommand, String commandPrefix) {
        String trimmed = fullCommand.trim();
        if (!trimmed.startsWith(commandPrefix)) {
            return "";
        }
        String description = trimmed.substring(commandPrefix.length()).trim();
        return description.isEmpty() ? "" : description;
    }

    public static String[] parseDeadline(String fullCommand) {
        String trimmed = fullCommand.trim();
        if (!trimmed.startsWith("deadline ")) {
            return null;
        }
        String remaining = trimmed.substring("deadline ".length());
        if (!remaining.contains(" /by ")) {
            return null;
        }
        String[] parts = remaining.split(" /by ");
        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            return null;
        }
        return new String[]{parts[0].trim(), parts[1].trim()};
    }

    public static String[] parseEvent(String fullCommand) {
        String trimmed = fullCommand.trim();
        if (!trimmed.startsWith("event ")) {
            return null;
        }
        String remaining = trimmed.substring("event ".length());
        if (!remaining.contains(" /from ") || !remaining.contains(" /to ")) {
            return null;
        }
        String[] fromParts = remaining.split(" /from ");
        if (fromParts.length < 2 || fromParts[0].trim().isEmpty()) {
            return null;
        }
        String[] toParts = fromParts[1].split(" /to ");
        if (toParts.length < 2 || toParts[0].trim().isEmpty() || toParts[1].trim().isEmpty()) {
            return null;
        }
        return new String[]{fromParts[0].trim(), toParts[0].trim(), toParts[1].trim()};
    }

    public static int parseIndex(String[] parts) {
        if (parts.length < 2) {
            return -1;
        }
        try {
            int index = Integer.parseInt(parts[1]) - 1;
            if (index < 0) {
                return -1;
            }
            return index;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static LocalDate parseDate(String dateString) {
        try {
            return LocalDate.parse(dateString.trim());
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}