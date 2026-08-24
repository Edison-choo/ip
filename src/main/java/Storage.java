import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public void save(ArrayList<Task> tasks) throws IOException {
        Path directory = Paths.get(filePath).getParent();
        if (directory != null && !Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        try (FileWriter fw = new FileWriter(filePath)) {
            for (Task task : tasks) {
                fw.write(taskToFileFormat(task) + System.lineSeparator());
            }
        }
    }

    public ArrayList<Task> load() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return tasks;
        }

        List<String> lines = Files.readAllLines(Paths.get(filePath));
        for (String line : lines) {
            Task task = fileFormatToTask(line);
            if (task != null) {
                tasks.add(task);
            }
        }
        return tasks;
    }

    private String taskToFileFormat(Task task) {
        String type = "";
        String isDone = Objects.equals(task.getStatusIcon(), "x") ? "1" : "0";
        String description = task.getDescription();

        if (task instanceof ToDos) {
            type = "T";
            return type + " | " + isDone + " | " + description;
        } else if (task instanceof Deadlines) {
            type = "D";
            Deadlines d = (Deadlines) task;
            return type + " | " + isDone + " | " + description + " | " + d.getBy();
        } else if (task instanceof Events) {
            type = "E";
            Events e = (Events) task;
            return type + " | " + isDone + " | " + description + " | " + e.getFrom() + " | " + e.getTo();
        }
        return "";
    }

    private Task fileFormatToTask(String line) {
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            return null;
        }

        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        Task task = null;

        switch (type) {
            case "T":
                task = new ToDos(description);
                break;
            case "D":
                if (parts.length < 4) return null;
                task = new Deadlines(description, parts[3]);
                break;
            case "E":
                if (parts.length < 5) return null;
                task = new Events(description, parts[3], parts[4]);
                break;
            default:
                return null;
        }

        if (task != null && isDone) {
            task.toggleStatus();
        }
        return task;
    }
}