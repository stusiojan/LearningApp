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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DashboardPanel extends JPanel implements ActionListener {
    private final User user;
    private final DatabaseManager dbManager;
    private Calendar calendar;
    private DefaultListModel<String> taskListModel;
    private JCheckBox overdueCheckBox;
    private JCheckBox weekCheckBox;
    private JCheckBox monthCheckBox;

    public DashboardPanel(User user) throws SQLException {
        this.user = user;
        this.dbManager = new DatabaseManager();
        taskListModel = new DefaultListModel<>();
        initializeUI();
        updateCalendarAndTasks();
    }
    private void initializeUI() {
        setLayout(new BorderLayout());

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(400);

        // Left panel: Calendar
        Calendar calendar = new Calendar();
        calendar.setCurrentView(CalendarView.SingleMonth);
        splitPane.setLeftComponent(calendar);

        // Right panel: Task list view
        JPanel taskListPanel = new JPanel();
        taskListPanel.setLayout(new BorderLayout());

        // Top part: List of tasks
        JList<String> taskList = new JList<>(taskListModel);
        JScrollPane taskListScrollPane = new JScrollPane(taskList);
        taskListPanel.add(taskListScrollPane, BorderLayout.CENTER);

        // Bottom part: Filter options
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout());

        JCheckBox overdueCheckBox = new JCheckBox("Overdue Tasks");
        JCheckBox weekCheckBox = new JCheckBox("Tasks for the Week");
        JCheckBox monthCheckBox = new JCheckBox("Tasks for the Month");

        // set overdueCheckBox to selected
        overdueCheckBox.setSelected(true);

        filterPanel.add(overdueCheckBox);
        filterPanel.add(weekCheckBox);
        filterPanel.add(monthCheckBox);

        taskListPanel.add(filterPanel, BorderLayout.SOUTH);

        splitPane.setRightComponent(taskListPanel);

        add(splitPane, BorderLayout.CENTER);
    }
    private void updateCalendarAndTasks() throws SQLException {
        // Get current date

        // Populate calendar
//        List<Task> monthlyTasks = dbManager.fetchTasksByMonth(
//                currentDate.getMonth() + 1,
//                currentDate.getYear() + 1900
//        );
//        populateCalendar(monthlyTasks);

        // Populate task list based on filter options
//        if (overdueCheckBox.isSelected()) {
            List<Task> overdueTasks = dbManager.fetchOverdueTasks(this.user.getId());
            populateTaskList(overdueTasks);
//        }
//        else if (weekCheckBox.isSelected()) {
//            Date startOfWeek = getStartOfWeek(currentDate);
//            Date endOfWeek = getEndOfWeek(currentDate);
//            List<Task> weeklyTasks = dbManager.fetchTasksForWeek(startOfWeek, endOfWeek);
//            populateTaskList(weeklyTasks);
//        } else if (monthCheckBox.isSelected()) {
//            Date startOfMonth = getStartOfMonth(currentDate);
//            Date endOfMonth = getEndOfMonth(currentDate);
//            List<Task> monthlyTasksForList = dbManager.fetchTasksForMonth(startOfMonth, endOfMonth);
//            populateTaskList(monthlyTasksForList);
//        } else {
//            List<Task> allTasks = new ArrayList<>();
//            populateTaskList(allTasks);
//        }
    }
//
//    private void populateCalendar(List<Task> tasks) {
//        calendar.getSchedule().getItems().clear();
//        for (Task task : tasks) {
//            com.mindfusion.scheduling.model.Appointment appointment = new com.mindfusion.scheduling.model.Appointment();
//            appointment.setStartTime(new com.mindfusion.common.DateTime(task.getDateCompleted()));
//            appointment.setEndTime(new com.mindfusion.common.DateTime(task.getDateCompleted()).addMinutes(30));
//            appointment.setHeaderText(task.getName());
//            calendar.getSchedule().getItems().add(appointment);
//        }
//    }
//
    private void populateTaskList(List<Task> tasks) {
        taskListModel.clear();
        for (Task task : tasks) {
            taskListModel.addElement(task.getName());
        }
    }
//
//    private Date getStartOfWeek(Date date) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
//        return calendar.getTime();
//    }
//
//    private Date getEndOfWeek(Date date) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
//        calendar.add(Calendar.DAY_OF_WEEK, 6);
//        return calendar.getTime();
//    }
//
//    private Date getStartOfMonth(Date date) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        calendar.set(Calendar.DAY_OF_MONTH, 1);
//        return calendar.getTime();
//    }
//
//    private Date getEndOfMonth(Date date) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
//        return calendar.getTime();
//    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            updateCalendarAndTasks();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
