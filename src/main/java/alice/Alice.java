package alice;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Alice {
    // Instance variable
    private final Ui ui;
    private final Storage storage;
    private final Parser parser;
    private TaskList tasks;

    public Alice(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        this.parser = new Parser();
        this.tasks = new TaskList();
        loadTasks();
    }

    public static void main(String[] args) {
        Alice alice = new Alice("./data/alice.txt");
        alice.run();
    }

    private void loadTasks() {
        try {
            tasks = new TaskList(storage.load());
            System.out.println("Loaded " + tasks.size() + " tasks from file.");
        } catch (IOException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }
    }

    private void saveTasks() {
        try {
            storage.save(tasks.getTasks());
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    public void run() {
        // Setup
        Scanner scanner = new Scanner(System.in);

        ui.printGreetings();

        mainLoop:
        while (true) {
            // Read user input
            String input = scanner.nextLine();
            Command command = Parser.parseCommand(input);

            switch (command) {
                case BYE:
                    ui.quitMessage();
                    break mainLoop;
                case LIST:
                    ui.showTaskList(tasks);
                    break;
                case MARK:
                    toggleTaskStatus("mark", input);
                    break;
                case UNMARK:
                    toggleTaskStatus("unmark", input);
                    break;
                case TODO:
                    addTask("todo", input);
                    break;
                case DEADLINE:
                    addTask("deadline", input);
                    break;
                case EVENT:
                    addTask("event", input);
                    break;
                case DELETE:
                    deleteTask(input);
                    break;
                case VIEW:
                    viewDate(input);
                    break;
                default:
                    ui.showUnknownCommand();
                    break;
            }
        }
    }

    public void addTask(String type, String input) {
        Task taskItem;
        if (Objects.equals(type, "todo")) {
            String description = Parser.extractDescription(input, "todo");
            if (description.isEmpty()) {
                ui.showError("AIYO!!! The description of a todo cannot be empty.");
                return;
            }
            taskItem = new ToDos(description);
            this.tasks.add(taskItem);
            ui.showAddTask(tasks);
        } else if (Objects.equals(type, "deadline")) {
            String[] parts = Parser.parseDeadline(input);
            if (parts == null) {
                ui.showError("AIYO!!! Please use: deadline <description> /by yyyy-MM-dd");
                return;
            }
            LocalDate date = Parser.parseDate(parts[1]);
            if (date == null) {
                ui.showError("AIYO!!! Please enter the date in yyyy-MM-dd format (e.g., 2024-12-25)");
                return;
            }
            taskItem = new Deadlines(parts[0], date);
            this.tasks.add(taskItem);
            ui.showAddTask(tasks);
        } else if (Objects.equals(type, "event")) {
            String[] parts = Parser.parseEvent(input);
            if (parts == null) {
                ui.showError("AIYO!!! Please use: event <description> /from yyyy-MM-dd /to yyyy-MM-dd");
                return;
            }
            LocalDate from = Parser.parseDate(parts[1]);
            LocalDate to = Parser.parseDate(parts[2]);
            if (from == null || to == null) {
                ui.showError("AIYO!!! Please enter dates in yyyy-MM-dd format (e.g., 2024-12-20)");
                return;
            }
            taskItem = new Events(parts[0], from, to);
            this.tasks.add(taskItem);
            ui.showAddTask(tasks);
        }
        saveTasks();
    }

    public void toggleTaskStatus(String type, String input) {
        String[] parts = input.split(" ");
        int index = Parser.parseIndex(parts);

        if (index == -1 || !tasks.isValidIndex(index)) {
            ui.showError("AIYO! Please specify a valid task number (e.g. mark 2)");
            return;
        }

        try {
            Task selectedTask = tasks.get(index);

            if (selectedTask.isDone && Objects.equals(type, "unmark")) {
                selectedTask.toggleStatus();
                ui.showUnmarkTask(selectedTask);
                saveTasks();
            } else if (!selectedTask.isDone && Objects.equals(type, "mark")) {
                selectedTask.toggleStatus();
                ui.showMarkTask(selectedTask);
                saveTasks();
            } else {
                ui.showError("This task is already " + type);
            }
        } catch (IndexOutOfBoundsException e2) {
            ui.showError("AIYO! Please enter a valid number from 1 to " + tasks.size() + "!");
        }
    }

    public void deleteTask(String input) {
        String[] parts = input.split(" ");
        int index = Parser.parseIndex(parts);
        if (index == -1 || !tasks.isValidIndex(index)) {
            ui.showError("AIYO!!! Please specify a valid task number (e.g., delete 2)");
            return;
        }

        try {
            Task removedTask = tasks.remove(index);
            ui.showDeleteTask(removedTask, tasks.size());
            saveTasks();
        } catch (IndexOutOfBoundsException e2) {
            System.out.println("AIYO! Please enter a valid number from 1 to " + tasks.size() + "!");
        }
    }

    public void viewDate(String input) {
        String [] parts = input.split(" ");
        if (parts.length < 2) {
            ui.showError("AIYO!!! Please specify a date to view (e.g., view 2024-12-25)");
            return;
        }
        LocalDate date = Parser.parseDate(parts[1]);
        if (date == null) {
            ui.showError("AIYO!!! Please enter the date in yyyy-MM-dd format (e.g., 2024-12-25)");
            return;
        }
        ui.showTasksOnDate(tasks, date);
    }
}
