import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.List;

public class TaskManagerGUI {

    private JFrame frame;
    private JPanel loginPanel, taskPanel;
    private Manager manager;
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;

    public TaskManagerGUI() {
        manager = new Manager();
        frame = new JFrame("Task Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 450);

        initLoginPanel();
        initTaskPanel();

        frame.add(loginPanel);
        frame.setVisible(true);
    }

    private void initLoginPanel() {
        loginPanel = new JPanel(new GridLayout(7, 1));
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(userField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passField);
        loginPanel.add(loginBtn);
        loginPanel.add(registerBtn);

        loginBtn.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());
            if (manager.login(user, pass)) {
                JOptionPane.showMessageDialog(frame, "Login successful!");
                switchToTaskPanel();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials.");
            }
        });

        registerBtn.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());
            if (manager.register(user, pass)) {
                JOptionPane.showMessageDialog(frame, "User registered!");
            } else {
                JOptionPane.showMessageDialog(frame, "User already exists.");
            }
        });
    }

    private void initTaskPanel() {
        taskPanel = new JPanel(new BorderLayout());
        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);

        JButton addBtn = new JButton("Add");
        JButton removeBtn = new JButton("Remove");
        JButton completeBtn = new JButton("Complete");
        JButton searchBtn = new JButton("Search");
        JButton logoutBtn = new JButton("Logout");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addBtn);
        buttonPanel.add(removeBtn);
        buttonPanel.add(completeBtn);
        buttonPanel.add(searchBtn);
        buttonPanel.add(logoutBtn);

        taskPanel.add(new JScrollPane(taskList), BorderLayout.CENTER);
        taskPanel.add(buttonPanel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Task title:");
            if (name == null || name.trim().isEmpty()) return;

            String cat = JOptionPane.showInputDialog("Category:");
            if (cat == null || cat.trim().isEmpty()) return;

            String dateStr = JOptionPane.showInputDialog("Due date (YYYY-MM-DD):");
            try {
                LocalDate date = LocalDate.parse(dateStr);
                manager.addTask(name, cat, date);
                refreshTaskList();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid date format.");
            }
        });

        removeBtn.addActionListener(e -> {
            int index = taskList.getSelectedIndex();
            if (index != -1 && manager.removeTask(index)) {
                refreshTaskList();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid selection.");
            }
        });

        completeBtn.addActionListener(e -> {
            int index = taskList.getSelectedIndex();
            if (index != -1 && manager.completeTask(index)) {
                refreshTaskList();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid selection.");
            }
        });

        searchBtn.addActionListener(e -> {
            String kw = JOptionPane.showInputDialog("Search keyword:");
            if (kw == null || kw.trim().isEmpty()) return;
            List<Task> results = manager.searchTasks(kw);
            DefaultListModel<String> searchModel = new DefaultListModel<>();
            for (Task t : results) searchModel.addElement(t.toString());
            taskList.setModel(searchModel);
        });

        logoutBtn.addActionListener(e -> {
            manager.logout();
            taskList.setModel(taskListModel);  // reset to default
            frame.remove(taskPanel);
            frame.add(loginPanel);
            frame.revalidate();
            frame.repaint();
        });
    }

    private void switchToTaskPanel() {
        refreshTaskList();
        frame.remove(loginPanel);
        frame.add(taskPanel);
        frame.revalidate();
        frame.repaint();
    }

    private void refreshTaskList() {
        taskListModel.clear();
        for (Task t : manager.getList()) {
            taskListModel.addElement(t.toString());
        }
        taskList.setModel(taskListModel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TaskManagerGUI());
    }
}
