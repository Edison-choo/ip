import java.util.Objects;
import java.util.Scanner;

public class Alice {
    public static void main(String[] args) {
        printGreetings();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Read user input
            String input = scanner.nextLine();

            // Check if user wants to quit
            if (Objects.equals(input, "bye")) {
                quitMessage();
                break;
            }

            // Echo user input
            echoInput(input);
        }
    }

    public static void printGreetings() {
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

    public static void echoInput(String input) {
        System.out.println(input);
        System.out.println("----------------------------------------------------------");
    }

    public static void quitMessage() {
        System.out.println("----------------------------------------------------------");
        System.out.println("Bye Bye. Can't wait to talk to you again!");
        System.out.println("----------------------------------------------------------");
    }
}
