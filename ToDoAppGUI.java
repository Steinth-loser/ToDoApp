import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

// --- Task Sınıfı ---
class Task {
    String title, description, dueDate;
    boolean isCompleted = false;

    Task() {}

    Task(String title, String description, String dueDate, boolean isCompleted) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.isCompleted = isCompleted;
    }

    public void markComplete() {
        isCompleted = true;
    }

    @Override
    public String toString() {
        return title + " (Due: " + dueDate + ") - " + (isCompleted ? "Completed" : "Not Completed");
    }
}

// --- WorkTask Sınıfı ---
class WorkTask extends Task {
    WorkTask() {}

    WorkTask(String title, String description, String dueDate, boolean isCompleted) {
        super(title, description, dueDate, isCompleted);
    }

    @Override
    public String toString() {
        return "[Work] " + super.toString();
    }
}

// --- PersonalTask Sınıfı ---
class PersonalTask extends Task {
    PersonalTask() {}

    PersonalTask(String title, String description, String dueDate, boolean isCompleted) {
        super(title, description, dueDate, isCompleted);
    }

    @Override
    public String toString() {
        return "[Personal] " + super.toString();
    }
}

// --- User Sınıfı ---
class User {
    String name;
    ArrayList<Task> tasks = new ArrayList<>();

    User() {}

    User(String name) {
        this.name = name;
    }

    void addTask(Task t) {
        tasks.add(t);
    }

    void markTaskComplete(int taskId) {
        if (taskId >= 0 && taskId < tasks.size()) {
            tasks.get(taskId).markComplete();
        }
    }

    ArrayList<Task> getAllTasks() {
        return tasks;
    }

    ArrayList<Task> getIncompleteTasks() {
        ArrayList<Task> incomplete = new ArrayList<>();
        for (Task t : tasks) {
            if (!t.isCompleted) {
                incomplete.add(t);
            }
        }
        return incomplete;
    }
}

// --- GUI Sınıfı ---
public class ToDoAppGUI {
    private JFrame frame;
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;
    private User user;

    public ToDoAppGUI() {
        user = new User("User");

        frame = new JFrame("To-Do List Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        // Panel
        JPanel panel = new JPanel(new BorderLayout());

        // Task list model and JList
        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        panel.add(new JScrollPane(taskList), BorderLayout.CENTER);

        // Buttons
        JButton addButton = new JButton("Add Task");
        JButton markCompleteButton = new JButton("Mark as Complete");
        JButton showIncompleteButton = new JButton("Show Incomplete");
        JButton showAllButton = new JButton("Show All Tasks");

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.add(addButton);
        buttonPanel.add(markCompleteButton);
        buttonPanel.add(showIncompleteButton);
        buttonPanel.add(showAllButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        addButton.addActionListener(e -> addTask());
        markCompleteButton.addActionListener(e -> markSelectedTaskComplete());
        showIncompleteButton.addActionListener(e -> showIncompleteTasks());
        showAllButton.addActionListener(e -> showTasks());

        frame.add(panel);
        frame.setVisible(true);
    }

    private void addTask() {
        String[] types = {"Work", "Personal"};
        String type = (String) JOptionPane.showInputDialog(frame, "Select task type:", "Type",
                JOptionPane.QUESTION_MESSAGE, null, types, types[0]);

        if (type == null) return;

        String title = JOptionPane.showInputDialog("Title:");
        String desc = JOptionPane.showInputDialog("Description:");
        String dueDate = JOptionPane.showInputDialog("Due date (yyyy-mm-dd):");

        if (title == null || dueDate == null) return;

        Task newTask;
        if (type.equalsIgnoreCase("Work")) {
            newTask = new WorkTask(title, desc, dueDate, false);
        } else {
            newTask = new PersonalTask(title, desc, dueDate, false);
        }

        user.addTask(newTask);
        refreshTaskList();
    }

    private void markSelectedTaskComplete() {
        int index = taskList.getSelectedIndex();
        if (index != -1) {
            user.markTaskComplete(index);
            refreshTaskList();
        } else {
            JOptionPane.showMessageDialog(frame, "Select a task first.");
        }
    }

    private void showTasks() {
        taskListModel.clear();
        for(Task task : user.getAllTasks()) {
            taskListModel.addElement(task.toString());
        }
    }

    private void showIncompleteTasks() {
        taskListModel.clear();
        for (Task task : user.getIncompleteTasks()) {
            taskListModel.addElement(task.toString());
        }
    }

    private void refreshTaskList() {
        taskListModel.clear();
        for (Task task : user.getAllTasks()) {
            taskListModel.addElement(task.toString());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ToDoAppGUI::new);
    }
}
