import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Alice {
    // Instance variable
    private ArrayList<Task> tasks;
    public static final String DOTTEDLINE = "----------------------------------------------------------";
    private final Storage storage;

    public Alice() {
        this.tasks = new ArrayList<>();
        this.storage = new Storage("./data/alice.txt");
        loadTasks();
    }

    public static void main(String[] args) {
        Alice alice = new Alice();
        alice.run();
    }

    private void loadTasks() {
        try {
            tasks = storage.load();
            System.out.println("Loaded " + tasks.size() + " tasks from file.");
        } catch (IOException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }
    }

    private void saveTasks() {
        try {
            storage.save(tasks);
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    public void run() {
        // Setup
        Scanner scanner = new Scanner(System.in);

        printGreetings();

        mainLoop:
        while (true) {
            // Read user input
            String input = scanner.nextLine();

            // Split to get the first word
            String [] parts = input.split(" ");
            String command = parts[0];

            switch (command) {
                case "bye":
                    quitMessage();
                    break mainLoop;
                case "list":
                    listTasks();
                    break;
                case "mark":
                    toggleTaskStatus("mark", parts);
                    break;
                case "unmark":
                    toggleTaskStatus("unmark", parts);
                    break;
                case "todo":
                    // Check if there's anything after "todo"
                    if (parts.length < 2 || input.substring("todo ".length()).trim().isEmpty()) {
                        System.out.println("AIYO! The description of a todo cannot be empty.");
                        System.out.println(DOTTEDLINE);
                        break;
                    }
                    
                    addTask("todo", input);
                    break;
                case "deadline":
                    // Check if there's anything after "deadline"
                    if (parts.length < 2) {
                        System.out.println("AIYO!!! The description of a deadline cannot be empty.");
                        System.out.println(DOTTEDLINE);
                        break;
                    }

                    // Check if the input contains " /by "
                    if (!input.contains(" /by ")) {
                        System.out.println("AIYO! Please specify a deadline using /by (e.g. deadline return book /by Sunday)");
                        System.out.println(DOTTEDLINE);
                        break;
                    }

                    addTask("deadline", input);
                    break;
                case "event":
                    // Check if there's anything after "event"
                    if (parts.length < 2) {
                        System.out.println("AIYO! The description of an event cannot be empty.");
                        System.out.println(DOTTEDLINE);
                        break;
                    }

                    // Check if input contains both " /from " and " /to "
                    if (!input.contains(" /from ") || !input.contains(" /to ")) {
                        System.out.println("AYIO! Please specify event using /from and /to " +
                                "(e.g. event meeting /from Mon 2pm /to 4pm)");
                        System.out.println(DOTTEDLINE);
                        break;
                    }

                    addTask("event", input);
                    break;
                case "delete":
                    deleteTask(parts);
                case "view":
                    if (parts.length < 2) {
                        System.out.println("AIYO!!! Please specify a date to view (e.g., view 2024-12-25)");
                        break;
                    }
                    try {
                        LocalDate dateToView = LocalDate.parse(parts[1].trim());
                        viewTasksOnDate(dateToView);
                    } catch (DateTimeParseException e) {
                        System.out.println("AIYO!!! Please enter the date in yyyy-MM-dd format (e.g., 2024-12-25)");
                    }
                    break;
                default:
                    unknownMessage();
                    break;
            }

            // Echo user input
            // echoInput(input);
        }
    }

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

    public void echoInput(String input) {
        System.out.println(input);
        System.out.println(DOTTEDLINE);
    }

    public void quitMessage() {
        System.out.println(DOTTEDLINE);
        System.out.println("Bye Bye. Can't wait to talk to you again!");
        System.out.println(DOTTEDLINE);
    }

    public void addTask(String type, String input) {
        Task taskItem;
        if (Objects.equals(type, "todo")) {
            String description = input.substring("todo ".length());
            taskItem = new ToDos(description);
            this.tasks.add(taskItem);
        } else if (Objects.equals(type, "deadline")) {
            String[] parts = input.substring("deadline ".length()).split(" /by ");

            // Check description is not empty
            if (parts.length < 2 || parts[0].trim().isEmpty()) {
                System.out.println("AIYO! The description of a deadline cannot be empty.");
                System.out.println(DOTTEDLINE);
                return;
            }

            // Check /by value is not empty
            if (parts[1].trim().isEmpty()) {
                System.out.println("AIYO! The deadline date/time cannot be empty.");
                System.out.println(DOTTEDLINE);
                return;
            }
            try {
                LocalDate byDate = LocalDate.parse(parts[1].trim());
                taskItem = new Deadlines(parts[0], byDate);
                this.tasks.add(taskItem);
            } catch (DateTimeParseException e) {
                System.out.println("AIYO! Please enter the date in yyyy-MM-dd format. (e.g., 2024-12-20)");
                System.out.println(DOTTEDLINE);
                return;
            }

        } else if (Objects.equals(type, "event")) {
            String[] parts = input.substring("event ".length()).split(" /from ");

            // Check description is not empty
            if (parts.length < 2 || parts[0].trim().isEmpty()) {
                System.out.println("AIYO! The description of an event cannot be empty.");
                System.out.println(DOTTEDLINE);
                return;
            }

            String[] fromToParts = parts[1].split(" /to ");

            // Check /from value is not empty
            if (fromToParts.length < 2 || fromToParts[0].trim().isEmpty()) {
                System.out.println("AIYO! The event start time (/from) cannot be empty.");
                System.out.println(DOTTEDLINE);
                return;
            }

            // Check /to value is not empty
            if (fromToParts[1].trim().isEmpty()) {
                System.out.println("AIYO! The event end time (/to) cannot be empty.");
                System.out.println(DOTTEDLINE);
                return;
            }

            try {
                LocalDate fromDate = LocalDate.parse(fromToParts[0].trim());
                LocalDate toDate = LocalDate.parse(fromToParts[1].trim());
                taskItem = new Events(parts[0], fromDate, toDate);
                this.tasks.add(taskItem);
            } catch (DateTimeParseException e) {
                System.out.println("AIYO!!! Please enter dates in yyyy-MM-dd format (e.g., 2024-12-20)");
                System.out.println(DOTTEDLINE);
                return;
            }


        }

        saveTasks();
        System.out.printf("""
                Got it. I've added this task:
                  %s
                Now you have %d tasks in the list.
                """,
                tasks.getLast(), tasks.size());
        System.out.println(DOTTEDLINE);
    }

    public void listTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks in your list yet!");
        } else {
            System.out.println("Here are the tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.printf("%d.%s\n", i+1, tasks.get(i));
            }
            System.out.println(DOTTEDLINE);
        }
    }

    public void toggleTaskStatus(String type, String[] parts) {
        if (parts.length < 2) {
            System.out.println("AIYO! Please specify which task to mark (e.g. mark 2)");
            System.out.println(DOTTEDLINE);
            return;
        }

        try {
            int index = Integer.parseInt(parts[1]) - 1;
            Task selectedTask = tasks.get(index);

            if (selectedTask.isDone && Objects.equals(type, "unmark")) {
                System.out.println("Ok, I've marked this task as not done yet;");
                selectedTask.toggleStatus();
                System.out.printf("  [%s] %s\n", selectedTask.getStatusIcon(), selectedTask);
                saveTasks();
            } else if (!selectedTask.isDone && Objects.equals(type, "mark")) {
                System.out.println("Nice! I've marked this task as done;");
                selectedTask.toggleStatus();
                System.out.printf("  %s\n", selectedTask);
                saveTasks();
            } else {
                System.out.printf("This task is already %s\n", type);
            }
        } catch (NumberFormatException e1) {
            System.out.println("AIYO! Please enter a valid number! (e.g. mark 2)");
        } catch (IndexOutOfBoundsException e2) {
            System.out.println("AIYO! Please enter a valid number from 1 to " + tasks.size() + "!");
        }
        System.out.println(DOTTEDLINE);
    }

    public void unknownMessage() {
        System.out.println(DOTTEDLINE);
        System.out.println("Sorry! I am not sure what are you talking about :(");
        System.out.println(DOTTEDLINE);
    }

    public void deleteTask(String[] parts) {
        if (parts.length < 2) {
            System.out.println("AIYO! Please specify which task to delete (e.g. delete 2)");
            System.out.println(DOTTEDLINE);
            return;
        }

        try {
            int index = Integer.parseInt(parts[1]) - 1;
            Task removedTask = tasks.remove(index);
            System.out.printf("""
                    Noted, I've removed this task:
                      %s
                    Now you have %d tasks in the list.
                    """, removedTask, tasks.size());
            saveTasks();
        } catch (NumberFormatException e1) {
            System.out.println("AIYO! Please enter a valid number! (e.g. mark 2)");
        } catch (IndexOutOfBoundsException e2) {
            System.out.println("AIYO! Please enter a valid number from 1 to " + tasks.size() + "!");
        }
        System.out.println(DOTTEDLINE);
    }

    public void viewTasksOnDate(LocalDate date) {
        boolean found = false;
        int count = 0;

        // Format the date nicely for display
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy");
        System.out.println("Tasks on " + date.format(formatter) + ":");

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
                // Check if the date falls within the event range (inclusive)
                if (!e.getFrom().isAfter(date) && !e.getTo().isBefore(date)) {
                    isOnDate = true;
                }
            }

            if (isOnDate) {
                count++;
                System.out.println((count) + "." + task);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No tasks found on this date.");
        }
        System.out.println(DOTTEDLINE);
    }
}
