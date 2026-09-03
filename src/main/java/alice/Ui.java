package alice;

import java.io.PrintStream;

/**
 * Handles all interactions with the user, including displaying messages,
 * task lists, error messages, and formatting outputs for the console.
 */
public class Ui {
    private static final String DOTTED_LINE = "----------------------------------------------------------";
    private final PrintStream output;

    /**
     * Constructs a UI that writes to the standard output stream.
     */
    public Ui() {
        this(System.out);
    }

    /**
     * Constructs a UI that writes to the supplied output stream.
     *
     * @param output The destination for UI messages.
     */
    public Ui(PrintStream output) {
        this.output = output;
    }

    /**
     * Prints the welcome banner and greeting message to the console.
     */
    public void printGreetings() {
        String greetings = """
                ----------------------------------------------------------
                ██████  ██     ████  █████  █████
                ██  ██  ██      ██   ██     ██
                ██████  ██      ██   ██     ████
                ██  ██  ██      ██   ██     ██
                ██  ██  █████  ████  █████  █████
                Hello! Alice is here to chat!
                What do you want to discuss with me?
                ----------------------------------------------------------""";
        output.println(greetings);
    }

    /**
     * Prints a plain-ASCII greeting banner for the graphical chat interface.
     */
    public void printChatGreetings() {
        String greetings = """
                ----------------------------------------------------------
                AAAAA   L        I   CCCC   EEEEE
               A     A  L        I  C       E
               AAAAAAA  L        I  C       EEEE
               A     A  L        I  C       E
               A     A  LLLLL    I   CCCC   EEEEE

                Hello! Alice is here to chat!
                What do you want to discuss with me?
                ----------------------------------------------------------""";
        output.println(greetings);
    }

    /**
     * Prints the goodbye message when the user exits the application.
     */
    public void quitMessage() {
        output.println(DOTTED_LINE);
        output.println("Bye Bye. Can't wait to talk to you again!");
        output.println(DOTTED_LINE);
    }

    /**
     * Prints a visual separator line (dotted line) to structure the output.
     */
    public void showSeparator() {
        output.println(DOTTED_LINE);
    }

    /**
     * Prints a plain informational message.
     *
     * @param message The message to display.
     */
    public void showMessage(String message) {
        output.println(message);
    }

    /**
     * Prints an error message followed by a separator line.
     *
     * @param message The error message to display.
     */
    public void showError(String message) {
        output.println(message);
        showSeparator();
    }

    /**
     * Prints a confirmation message indicating that a task was successfully added.
     *
     * @param tasks The updated task list (used to retrieve the most recently added task and total count).
     */
    public void showAddTask(TaskList tasks) {
        output.printf("""
                Got it. I've added this task:
                  %s
                Now you have %d tasks in the list.
                """,
                tasks.get(tasks.size() - 1), tasks.size());
        showSeparator();
    }

    /**
     * Prints a confirmation message indicating that a task was successfully deleted.
     *
     * @param task        The task that was removed.
     * @param totalCount  The remaining number of tasks after deletion.
     */
    public void showDeleteTask(Task task, int totalCount) {
        output.printf("""
                    Noted, I've removed this task:
                      %s
                    Now you have %d tasks in the list.
                    """, task, totalCount);
        showSeparator();
    }

    /**
     * Prints a confirmation message indicating that a task was successfully marked as done.
     *
     * @param task The task that was marked as done.
     */
    public void showMarkTask(Task task) {
        output.println("Nice! I've marked this task as done;");
        output.printf("  %s\n", task);
        showSeparator();
    }

    /**
     * Prints a confirmation message indicating that a task was successfully unmarked.
     *
     * @param task The task that was marked as not done.
     */
    public void showUnmarkTask(Task task) {
        output.println("Ok, I've marked this task as not done yet;");
        output.printf("  %s\n", task);
        showSeparator();
    }

    /**
     * Prints the current list of tasks to the console.
     * If the list is empty, a message is displayed.
     *
     * @param tasks The task list to display.
     */
    public void showTaskList(TaskList tasks) {
        if (tasks.isEmpty()) {
            output.println("No tasks in your list yet!");
        } else {
            output.println("Here are the tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                output.printf("%d.%s\n", i + 1, tasks.get(i));
            }
        }
        showSeparator();
    }

    /**
     * Prints tasks that occur on a specific date.
     * For deadlines, it checks the exact 'by' date.
     * For events, it checks if the date falls within the event's range.
     *
     * @param tasks The task list to search in.
     * @param date  The date to filter tasks by.
     */
    public void showTasksOnDate(TaskList tasks, java.time.LocalDate date) {
        java.time.format.DateTimeFormatter formatter =
                java.time.format.DateTimeFormatter.ofPattern("MMM d yyyy");
        output.println("Tasks on " + date.format(formatter) + ":");

        boolean found = false;
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            boolean isOnDate = false;

            if (task instanceof Deadlines) {
                Deadlines d = (Deadlines) task;
                if (d.getBy().equals(date)) {
                    isOnDate = true;
                }
            } else if (task instanceof Events) {
                Events e = (Events) task;
                if (!e.getFrom().isAfter(date) && !e.getTo().isBefore(date)) {
                    isOnDate = true;
                }
            }

            if (isOnDate) {
                output.println((i + 1) + "." + task);
                found = true;
            }
        }

        if (!found) {
            output.println("No tasks found on this date.");
        }
        showSeparator();
    }

    /**
     * Prints a generic error message when an unknown or unrecognized command is entered.
     */
    public void showUnknownCommand() {
        output.println("Sorry! I am not sure what are you talking about :(");
        showSeparator();
    }

    /**
     * Displays the matching tasks for a given keyword search.
     *
     * @param matchingTasks The list of tasks that matched the keyword.
     * @param keyword       The keyword that was searched for.
     */
    public void showMatchingTasks(TaskList matchingTasks, String keyword) {
        if (matchingTasks.isEmpty()) {
            output.println("No tasks found containing \"" + keyword + "\"");
        } else {
            output.println("Here are the matching tasks in your list:");
            for (int i = 0; i < matchingTasks.size(); i++) {
                output.printf("%d.%s\n", i + 1, matchingTasks.get(i));
            }
        }
        showSeparator();
    }
}
