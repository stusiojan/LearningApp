package main;

import com.mindfusion.scheduling.Calendar;
import com.mindfusion.scheduling.CalendarView;
import main.lib.DatabaseManager;
import main.lib.Task;
import main.lib.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

public class DashboardPanel extends JPanel implements ActionListener {
    private final User user;
    private final Calendar calendar;
    private final DefaultListModel<String> taskListModel;
    private final JComboBox<String> selectBox;
    private final JList<String> taskList;

    public DashboardPanel(User user) throws SQLException {
        this.user = user;
        this.calendar = new Calendar();
        this.taskListModel = new DefaultListModel<>();
        String[] selectBoxLabels = {"All Tasks", "Overdue Tasks", "Tasks for the Week", "Tasks for the Month"};
        this.selectBox = new JComboBox<>(selectBoxLabels);
        this.taskList = new JList<>(taskListModel);

        initializeUI();
        updateCalendarAndTasks();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        selectBox.setSelectedIndex(0);
        selectBox.addActionListener(this);

        taskList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int index = taskList.locationToIndex(evt.getPoint());
                    if (index >= 0) {
                        String selectedTaskName = taskListModel.getElementAt(index);
                        showTaskDetails(selectedTaskName);
                    }
                }
            }
        });

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createCalendarPanel(), createTaskListPanel());
        splitPane.setDividerLocation(400);
        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel createCalendarPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        calendar.setCurrentView(CalendarView.SingleMonth);
        panel.add(calendar, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createTaskListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(taskList), BorderLayout.CENTER);
        panel.add(createFilterPanel(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(selectBox);
        return panel;
    }

    private void updateCalendarAndTasks() throws SQLException {
        List<String> tasks = switch (selectBox.getSelectedIndex()) {
            case 1 -> DatabaseManager.fetchOverdueTasks(user.getId());
            case 2 -> DatabaseManager.fetchTasksForWeek(user.getId());
            case 3 -> DatabaseManager.fetchTasksForMonth(user.getId());
            default -> DatabaseManager.fetchAllTasks(user.getId());
        };

        populateTaskList(tasks);
        // populateCalendar(tasks); // Uncomment if calendar functionality is needed
    }

    private void populateTaskList(List<String> tasks) {
        taskListModel.clear();
        for (String taskLabel : tasks) {
            taskListModel.addElement(taskLabel);
        }
    }

    private void showTaskDetails(String taskLabel) {
        String taskId = taskLabel.split("#")[1].split(" - ")[0];
        Task task = DatabaseManager.getTask(Integer.parseInt(taskId));
        if (task != null) {
            TaskDetailsDialog dialog = new TaskDetailsDialog(task, this::refreshData);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        }
    }

    private void refreshData() {
        try {
            updateCalendarAndTasks();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        refreshData();
    }
}
