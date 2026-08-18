import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Alice {
    // Instance variable
    private ArrayList<String> tasks;

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

            switch (input) {
                case "bye":
                    quitMessage();
                    break mainLoop;
                case "list":
                    listTasks();
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
        this.tasks.add(task);
        System.out.printf("added: %s%n", task);
        System.out.println("----------------------------------------------------------");
    }

    public void listTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks in your list yet!");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                System.out.printf("%d. %s\n", i+1, tasks.get(i));
            }
            System.out.println("----------------------------------------------------------");
        }
    }
}
