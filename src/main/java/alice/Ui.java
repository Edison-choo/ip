package alice;

public class Ui {
    private static final String DOTTEDLINE = "----------------------------------------------------------";

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

    public void quitMessage() {
        System.out.println(DOTTEDLINE);
        System.out.println("Bye Bye. Can't wait to talk to you again!");
        System.out.println(DOTTEDLINE);
    }

    public void showSeparator() {
        System.out.println(DOTTEDLINE);
    }

    public void showError(String message) {
        System.out.println(message);
        showSeparator();
    }

    public void showAddTask(TaskList tasks) {
        System.out.printf("""
                Got it. I've added this task:
                  %s
                Now you have %d tasks in the list.
                """,
                tasks.get(tasks.size() - 1), tasks.size());
        showSeparator();
    }

    public void showDeleteTask(Task task, int totalCount) {
        System.out.printf("""
                    Noted, I've removed this task:
                      %s
                    Now you have %d tasks in the list.
                    """, task, totalCount);
        showSeparator();
    }

    public void showMarkTask(Task task) {
        System.out.println("Nice! I've marked this task as done;");
        System.out.printf("  %s\n", task);
        showSeparator();
    }

    public void showUnmarkTask(Task task) {
        System.out.println("Ok, I've marked this task as not done yet;");
        System.out.printf("  %s\n", task);
        showSeparator();
    }

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

    public void showUnknownCommand() {
        System.out.println("Sorry! I am not sure what are you talking about :(");
        showSeparator();
    }
}
