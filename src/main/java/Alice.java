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
                default:
                    addTask(input);
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

    public void addTask(String task) {
        Task taskItem = new Task(task);
        this.tasks.add(taskItem);
        System.out.printf("added: %s%n", taskItem);
        System.out.println("----------------------------------------------------------");
    }

    public void listTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks in your list yet!");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                System.out.printf("%d.[%s] %s\n", i+1, tasks.get(i).getStatusIcon(), tasks.get(i));
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
                System.out.printf("  [%s] %s\n", selectedTask.getStatusIcon(), selectedTask);
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
