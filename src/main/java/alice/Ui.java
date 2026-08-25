package alice;

/**
 * Handles all interactions with the user, including displaying messages,
 * task lists, error messages, and formatting outputs for the console.
 */
public class Ui {
    private static final String DOTTEDLINE = "----------------------------------------------------------";

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
        System.out.println(greetings);
    }

    /**
     * Prints the goodbye message when the user exits the application.
     */
    public void quitMessage() {
        System.out.println(DOTTEDLINE);
        System.out.println("Bye Bye. Can't wait to talk to you again!");
        System.out.println(DOTTEDLINE);
    }

    /**
     * Prints a visual separator line (dotted line) to structure the output.
     */
    public void showSeparator() {
        System.out.println(DOTTEDLINE);
    }

    /**
     * Prints an error message followed by a separator line.
     *
     * @param message The error message to display.
     */
    public void showError(String message) {
        System.out.println(message);
        showSeparator();
    }

    /**
     * Prints a confirmation message indicating that a task was successfully added.
     *
     * @param tasks The updated task list (used to retrieve the most recently added task and total count).
     */
    public void showAddTask(TaskList tasks) {
        System.out.printf("""
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
        System.out.printf("""
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
        System.out.println("Nice! I've marked this task as done;");
        System.out.printf("  %s\n", task);
        showSeparator();
    }

    /**
     * Prints a confirmation message indicating that a task was successfully unmarked.
     *
     * @param task The task that was marked as not done.
     */
    public void showUnmarkTask(Task task) {
        System.out.println("Ok, I've marked this task as not done yet;");
        System.out.printf("  %s\n", task);
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
            System.out.println("No tasks in your list yet!");
        } else {
            System.out.println("Here are the tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.printf("%d.%s\n", i+1, tasks.get(i));
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
        System.out.println("Tasks on " + date.format(formatter) + ":");

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
                System.out.println((i + 1) + "." + task);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No tasks found on this date.");
        }
        showSeparator();
    }

    /**
     * Prints a generic error message when an unknown or unrecognized command is entered.
     */
    public void showUnknownCommand() {
        System.out.println("Sorry! I am not sure what are you talking about :(");
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
            System.out.println("No tasks found containing \"" + keyword + "\"");
        } else {
            System.out.println("Here are the matching tasks in your list:");
            for (int i = 0; i < matchingTasks.size(); i++) {
                System.out.printf("%d.%s\n", i + 1, matchingTasks.get(i));
            }
        }
        showSeparator();
    }
}
