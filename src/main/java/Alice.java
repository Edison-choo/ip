import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Alice {
    // Instance variable
    private final ArrayList<Task> tasks;

    public Alice() {
        this.tasks = new ArrayList<>();
    }

    public static void main(String[] args) {
        Alice alice = new Alice();
        alice.run();
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
                    addTask("todo", input);
                    break;
                case "deadline":
                    addTask("deadline", input);
                    break;
                case "event":
                    addTask("event", input);
                    break;
                default:
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
        System.out.println("----------------------------------------------------------");
    }

    public void quitMessage() {
        System.out.println("----------------------------------------------------------");
        System.out.println("Bye Bye. Can't wait to talk to you again!");
        System.out.println("----------------------------------------------------------");
    }

    public void addTask(String type, String input) {
        Task taskItem;
        if (Objects.equals(type, "todo")) {
            String description = input.substring("todo ".length());
            taskItem = new ToDos(description);
            this.tasks.add(taskItem);
        } else if (Objects.equals(type, "deadline")) {
            String[] parts = input.substring("deadline ".length()).split(" /by ");
            taskItem = new Deadlines(parts[0], parts[1]);
            this.tasks.add(taskItem);
        } else if (Objects.equals(type, "event")) {
            String[] parts = input.substring("event ".length()).split(" /from ");
            String[] fromToParts = parts[1].split(" /to ");
            taskItem = new Events(parts[0], fromToParts[0], fromToParts[1]);
            this.tasks.add(taskItem);
        }

        System.out.printf("""
                Got it. I've added this task:
                  %s
                Now you have %d tasks in the list.
                """,
                tasks.getLast(), tasks.size());
        System.out.println("----------------------------------------------------------");
    }

    public void listTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks in your list yet!");
        } else {
            System.out.println("Here are the tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.printf("%d.%s\n", i+1, tasks.get(i));
            }
            System.out.println("----------------------------------------------------------");
        }
    }

    public void toggleTaskStatus(String type, String[] parts) {
        if (parts.length < 2) {
            System.out.println("Please specify which task to mark (e.g. mark 2)");
            System.out.println("----------------------------------------------------------");
            return;
        }

        try {
            int index = Integer.parseInt(parts[1]) - 1;
            Task selectedTask = tasks.get(index);

            if (selectedTask.isDone && Objects.equals(type, "unmark")) {
                System.out.println("Ok, I've marked this task as not done yet;");
                selectedTask.toggleStatus();
                System.out.printf("  [%s] %s\n", selectedTask.getStatusIcon(), selectedTask);
            } else if (!selectedTask.isDone && Objects.equals(type, "mark")) {
                System.out.println("Nice! I've marked this task as done;");
                selectedTask.toggleStatus();
                System.out.printf("  %s\n", selectedTask);
            } else {
                System.out.printf("This task is already %s\n", type);
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number! (e.g. mark 2)");
            System.out.println("----------------------------------------------------------");
        }
        System.out.println("----------------------------------------------------------");
        return;

    }
}
