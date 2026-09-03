package alice;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Scanner;

/**
 * The main class for the Alice chatbot application.
 * This class orchestrates the flow of the program, handling user input,
 * parsing commands, managing tasks, and coordinating between UI, Storage,
 * and Parser components.
 */
public class Alice {
    // Instance variable
    private final Ui ui;
    private final Storage storage;
    private final Parser parser;
    private TaskList tasks;

    /**
     * Constructs an Alice instance with the specified file path for task storage.
     * Initializes all core components and loads tasks from the given file.
     *
     * @param filePath The relative or absolute path to the data file.
     */
    public Alice(String filePath) {
        this(filePath, new Ui());
    }

    /**
     * Constructs an Alice instance with a custom output interface.
     *
     * @param filePath The relative or absolute path to the data file.
     * @param ui       The interface used to display application messages.
     */
    public Alice(String filePath, Ui ui) {
        this.ui = Objects.requireNonNull(ui);
        this.storage = new Storage(filePath);
        this.parser = new Parser();
        this.tasks = new TaskList();
        loadTasks();
    }

    /**
     * The entry point of the application.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        Alice alice = new Alice("./data/alice.txt");
        alice.run();
    }

    /**
     * Loads tasks from the storage file.
     * If an error occurs during loading, an empty task list is initialized.
     */
    private void loadTasks() {
        try {
            tasks = new TaskList(storage.load());
            ui.showMessage("Loaded " + tasks.size() + " tasks from file.");
        } catch (IOException e) {
            ui.showMessage("Error loading tasks: " + e.getMessage());
        }
    }

    /**
     * Saves the current task list to the storage file.
     * If an error occurs during saving, an error message is printed to the UI.
     */
    private void saveTasks() {
        try {
            storage.save(tasks.getTasks());
        } catch (IOException e) {
            ui.showMessage("Error saving tasks: " + e.getMessage());
        }
    }

    /**
     * Starts the main interaction loop of the chatbot.
     * Reads user commands, parses them, and executes the corresponding actions
     * until the user types the "bye" command.
     */
    public void run() {
        // Setup
        Scanner scanner = new Scanner(System.in);

        ui.printGreetings();

        mainLoop:
        while (true) {
            // Read user input
            String input = scanner.nextLine();
            if (!processCommand(input)) {
                break mainLoop;
            }
        }
    }

    /**
     * Processes one command and sends its response to the configured user interface.
     *
     * @param input The raw command entered by the user.
     * @return false when the command exits the application, true otherwise.
     */
    public boolean processCommand(String input) {
        Command command = Parser.parseCommand(input);
        switch (command) {
            case BYE:
                ui.quitMessage();
                return false;
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
            case FIND:
                findTasks(input);
                break;
            default:
                ui.showUnknownCommand();
                break;
        }
        return true;
    }

    /**
     * Adds a new task to the list based on the specified type.
     * Validates the input format and description before creating the task.
     *
     * @param type  The type of task to add: "todo", "deadline", or "event".
     * @param input The raw user input containing the task description and optional dates.
     */
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

    /**
     * Toggles the status of a task based on the specified action.
     * If the action is "mark", the task is marked as done only if it is not already done.
     * If the action is "unmark", the task is marked as not done only if it is already done.
     * If the task is already in the desired state, an error message is shown.
     *
     * @param type  The action to perform, either "mark" or "unmark".
     * @param input The raw user input containing the task index.
     */
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

    /**
     * Deletes a task from the list based on the index provided in the input.
     *
     * @param input The raw user input containing the task index to delete.
     */
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
            ui.showMessage("AIYO! Please enter a valid number from 1 to " + tasks.size() + "!");
        }
    }

    /**
     * Displays all tasks scheduled on a specific date.
     * For deadlines, it matches the exact 'by' date.
     * For events, it matches if the date falls within the event's start and end dates.
     *
     * @param input The raw user input containing the date to view in yyyy-MM-dd format.
     */
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

    /**
     * Handles the "find" command to search for tasks by keyword.
     *
     * @param input The raw user input containing the keyword.
     */
    private void findTasks(String input) {
        String[] parts = input.split(" ");
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            ui.showError("AIYO!!! Please specify a keyword to search for (e.g., find book)");
            return;
        }
        String keyword = parts[1].trim();
        TaskList matches = tasks.find(keyword);
        ui.showMatchingTasks(matches, keyword);
    }
}
