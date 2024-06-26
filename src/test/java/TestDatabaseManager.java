import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import javax.xml.crypto.Data;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import main.lib.*;

public class TestDatabaseManager {
    @Test
    public void testBasic() {
        DatabaseManager.connect(true);

        Category[] categories = DatabaseManager.getCategories();
        Assert.assertEquals("Niemiecki", categories[0].getName());
        Assert.assertEquals("Bezpieczeństwo systemów i sieci", categories[6].getName());

        Assert.assertEquals("a1b2c3d4e5f6g7h8", DatabaseManager.getSalt("user3"));
        Assert.assertEquals("EMPTY", DatabaseManager.getSalt("Random"));

        DatabaseManager.disconnect();
    }

    @Test
    public void testHasUser() {
        DatabaseManager.connect(true);

        Assert.assertTrue(DatabaseManager.hasUser("user1"));
        Assert.assertTrue(DatabaseManager.hasUser("user2"));
        Assert.assertTrue(DatabaseManager.hasUser("user3"));
        Assert.assertFalse(DatabaseManager.hasUser("other_user"));

        DatabaseManager.disconnect();
    }

    @Test
    public void testAddAndDeleteUser() {
        DatabaseManager.connect(true);

        String login = "TestUser321";
        String hash = "83c8eb7ba4a496c82f87e4702f2858a41b1051faf6191b21268033f63ec99924";
        String salt = "EZ~Il!i)IwodI-kh";

        Assert.assertFalse(DatabaseManager.hasUser(login));

        DatabaseManager.addUser(login, hash, salt);
        User user = DatabaseManager.getUser(login).get();
        Assert.assertTrue(DatabaseManager.hasUser(login));
        Assert.assertEquals(salt, DatabaseManager.getSalt(login));
        Assert.assertEquals(login, user.getLogin());
        Assert.assertEquals(hash, user.getHash());
        Assert.assertEquals(salt, user.getSalt());

        DatabaseManager.deleteUser(login);
        Assert.assertFalse(DatabaseManager.hasUser(login));

        DatabaseManager.disconnect();
    }

    @Test
    public void testGetMilestones() {
        DatabaseManager.connect(true);

        Category category = DatabaseManager.getCategories()[1];
        User user = DatabaseManager.getUser("user1").get();
        Milestone[] milestones = DatabaseManager.getMilestones(category.getId(), user.getId());

        Assert.assertEquals("Podstawy języka", milestones[0].getName());
        Assert.assertEquals("Najważniejsze elementy języka", milestones[0].getDescription());
        Assert.assertEquals(Date.valueOf("2024-2-2"), milestones[0].getDateAdded());
        Assert.assertEquals("Programowanie obiektowe", milestones[1].getName());

        DatabaseManager.disconnect();
    }

    @Test
    public void testGetTasks() {
        DatabaseManager.connect(true);

        Category category = DatabaseManager.getCategories()[1];
        User user = DatabaseManager.getUser("user1").get();
        Milestone[] milestones = DatabaseManager.getMilestones(category.getId(), user.getId());
        ArrayList<Task> tasks = new ArrayList<Task>();
        Collections.addAll(tasks, DatabaseManager.getTasks(milestones[0].getId()));

        tasks.removeIf(task -> !task.getName().equals("Typy danych"));
        Assert.assertEquals(1, tasks.size());
        Assert.assertEquals(Date.valueOf("2024-02-04"), tasks.get(0).getDateCompleted());
        Assert.assertEquals("Ciągi znaków, liczby całkowite i zmiennoprzecinkowe", tasks.get(0).getDescription());

        DatabaseManager.disconnect();
    }

    @Test
    public void testAddUpdateDeleteCategory() {
        DatabaseManager.connect(true);

        // Check categories
        String catName = "My Test Category 123456";
        var categories = DatabaseManager.getCategories();
        for (var cat : categories) {
            Assert.assertNotEquals(cat.getName(), catName);
        }

        // Add category
        DatabaseManager.addCategory(catName);

        categories = DatabaseManager.getCategories();
        int numCatsFound = 0;
        Category testCat = null;
        for (var cat : categories) {
            if (cat.getName().equals(catName)) {
                numCatsFound += 1;
                testCat = cat;
            }
        }
        Assert.assertEquals(numCatsFound, 1);

        // Update category
        catName = "Another Test Category 123456";
        testCat.setName(catName);
        DatabaseManager.updateCategory(testCat);

        categories = DatabaseManager.getCategories();
        numCatsFound = 0;
        for (var cat : categories) {
            if (cat.getName().equals(catName)) {
                numCatsFound += 1;
            }
        }
        Assert.assertEquals(numCatsFound, 1);

        // Delete category
        DatabaseManager.deleteCategory(testCat.getId());
        categories = DatabaseManager.getCategories();
        for (var cat : categories) {
            Assert.assertNotEquals(cat.getName(), catName);
        }

        DatabaseManager.disconnect();
    }

    @Test
    public void testGetTasksList_All() throws SQLException {
        DatabaseManager.connect(true);
        try {
            DatabaseManager.startTransaction();

            // All Tasks
            List<String> allTasks = DatabaseManager.fetchAllTasks(3); // mil_id = 3->9
            Assert.assertEquals(14, allTasks.size());
        } catch (SQLException e) {
            throw new SQLException("Failed to fetch tasks list.");
        } finally {
            DatabaseManager.rollbackTransaction();
            DatabaseManager.disconnect();
        }
    }

    @Test
    public void testGetTaskList_Overdue() throws SQLException {
        DatabaseManager.connect(true);
        try {
            DatabaseManager.startTransaction();

            // Overdue Tasks
            List<String> overDueTasks = DatabaseManager.fetchOverdueTasks(3);
            Assert.assertEquals(9, overDueTasks.size());
            Milestone milestone = DatabaseManager
                    .getMilestones(3)
                    .stream()
                    .filter(m -> m.getId() == 4)
                    .findFirst()
                    .get(); // Milestone 4 has 3 tasks -> to overdue
            milestone.setDeadline(Date.valueOf(LocalDate.now().minusDays(1)));
            DatabaseManager.updateMilestone(milestone);
            overDueTasks = DatabaseManager.fetchOverdueTasks(3);
            Assert.assertEquals(9, overDueTasks.size());
        } catch (SQLException e) {
            throw new SQLException("Failed to fetch overdue tasks list.");
        } finally {
            DatabaseManager.rollbackTransaction();
            DatabaseManager.disconnect();
        }
    }

    @Test
    public void testGetTaskList_ForWeek() throws SQLException {
        DatabaseManager.connect(true);
        try {
            DatabaseManager.startTransaction();

            // Tasks for the week
            List<String> tasksForWeek = DatabaseManager.fetchTasksForWeek(3);
            Assert.assertEquals(5, tasksForWeek.size());
            Milestone milestone = DatabaseManager
                    .getMilestones(3)
                    .stream()
                    .filter(m -> m.getId() == 5)
                    .findFirst()
                    .get();
            milestone.setDeadline(Date.valueOf(LocalDate.now().plusDays(6)));
            DatabaseManager.updateMilestone(milestone);
            tasksForWeek = DatabaseManager.fetchTasksForWeek(3);
            Assert.assertEquals(7, tasksForWeek.size());
        } catch (SQLException e) {
            throw new SQLException("Failed to fetch tasks for the week list.");
        } finally {
            DatabaseManager.rollbackTransaction();
            DatabaseManager.disconnect();
        }
    }

    @Test
    public void testGetTaskList_ForMonth() throws SQLException {
        DatabaseManager.connect(true);
        try {
            DatabaseManager.startTransaction();

            // Tasks for the week
            List<String> tasksForWeek = DatabaseManager.fetchTasksForWeek(3);
            Assert.assertEquals(5, tasksForWeek.size());
            Milestone milestone = DatabaseManager
                    .getMilestones(3)
                    .stream()
                    .filter(m -> m.getId() == 5)
                    .findFirst()
                    .get();
            milestone.setDeadline(Date.valueOf(LocalDate.now().plusDays(28)));
            DatabaseManager.updateMilestone(milestone);
            tasksForWeek = DatabaseManager.fetchTasksForMonth(3);
            Assert.assertEquals(7, tasksForWeek.size());
        } catch (SQLException e) {
            throw new SQLException("Failed to fetch tasks for the week list.");
        } finally {
            DatabaseManager.rollbackTransaction();
            DatabaseManager.disconnect();
        }
    }

    @Test
    public void testAddDeleteMilestone() {
        DatabaseManager.connect(true);

        DatabaseManager.addMilestone("test", 1, 1);

        Milestone mil = DatabaseManager.getMilestones(1).getLast();
        Assert.assertEquals("test", mil.getName());
        Assert.assertEquals(Date.valueOf(LocalDate.now()).toString(), mil.getDateAdded().toString());
        Assert.assertEquals(Date.valueOf(LocalDate.now()).toString(), mil.getDeadline().toString());
        Assert.assertEquals(Date.valueOf(LocalDate.now()).toString(), mil.getDateCompleted().toString());
        Assert.assertNull(mil.getDescription());
        Assert.assertEquals(0, mil.getTasksAll());
        Assert.assertEquals(0, mil.getTasksDone());
        Assert.assertEquals(1, mil.getUserId());
        Assert.assertEquals(1, mil.getCategoryId());
        DatabaseManager.deleteMilestone(mil.getId());

        DatabaseManager.disconnect();
    }

    @Test
    public void testTaskCount() {
        DatabaseManager.connect(true);

        DatabaseManager.addCategory("test");
        Category cat = DatabaseManager.getCategories()[DatabaseManager.getCategories().length - 1];

        DatabaseManager.addMilestone("test", 1, cat.getId());
        Milestone mil = DatabaseManager.getMilestones(1).getLast();

        mil.setDeadline(Date.valueOf(LocalDate.now().minusDays(5)));
        DatabaseManager.updateMilestone(mil);

        int overdue = DatabaseManager.countTasksOverdue();
        int week = DatabaseManager.countTasksForWeek();
        int month = DatabaseManager.countTasksForMonth();

        DatabaseManager.addTask("test", mil.getId());
        Task task = DatabaseManager.getTasks(mil.getId())[0];

        Assert.assertEquals(overdue + 1, DatabaseManager.countTasksOverdue());
        Assert.assertEquals(week + 1, DatabaseManager.countTasksForWeek());
        Assert.assertEquals(month + 1, DatabaseManager.countTasksForMonth());

        DatabaseManager.switchTaskDone(task.getId());

        Assert.assertEquals(overdue, DatabaseManager.countTasksOverdue());
        Assert.assertEquals(week, DatabaseManager.countTasksForWeek());
        Assert.assertEquals(month, DatabaseManager.countTasksForMonth());

        DatabaseManager.switchTaskDone(task.getId());
        mil.setDeadline(Date.valueOf(LocalDate.now().plusDays(5)));
        DatabaseManager.updateMilestone(mil);

        Assert.assertEquals(overdue, DatabaseManager.countTasksOverdue());
        Assert.assertEquals(week + 1, DatabaseManager.countTasksForWeek());
        Assert.assertEquals(month + 1, DatabaseManager.countTasksForMonth());

        mil.setDeadline(Date.valueOf(LocalDate.now().plusDays(10)));
        DatabaseManager.updateMilestone(mil);

        Assert.assertEquals(overdue, DatabaseManager.countTasksOverdue());
        Assert.assertEquals(week, DatabaseManager.countTasksForWeek());
        Assert.assertEquals(month + 1, DatabaseManager.countTasksForMonth());

        mil.setDeadline(Date.valueOf(LocalDate.now().plusDays(50)));
        DatabaseManager.updateMilestone(mil);

        Assert.assertEquals(overdue, DatabaseManager.countTasksOverdue());
        Assert.assertEquals(week, DatabaseManager.countTasksForWeek());
        Assert.assertEquals(month, DatabaseManager.countTasksForMonth());

        DatabaseManager.deleteTask(task.getId());
        DatabaseManager.deleteMilestone(mil.getId());
        DatabaseManager.deleteCategory(cat.getId());

        DatabaseManager.disconnect();
    }

    @Test
    public void testTaskTriggersSwitch() {
        DatabaseManager.connect(true);

        DatabaseManager.addCategory("test");
        Category cat = DatabaseManager.getCategories()[DatabaseManager.getCategories().length - 1];

        DatabaseManager.addMilestone("test", 1, cat.getId());
        Milestone mil = DatabaseManager.getMilestones(1).getLast();

        DatabaseManager.addTask("test", mil.getId());
        Task task = DatabaseManager.getTasks(mil.getId())[0];
        cat = DatabaseManager.getCategory(cat.getId());
        mil = DatabaseManager.getMilestone(mil.getId());

        Assert.assertEquals(1, cat.getTasksAll());
        Assert.assertEquals(0, cat.getTasksDone());
        Assert.assertEquals(1, mil.getTasksAll());
        Assert.assertEquals(0, mil.getTasksDone());
        Assert.assertNull(mil.getDateCompleted());

        DatabaseManager.switchTaskDone(task.getId());
        cat = DatabaseManager.getCategory(cat.getId());
        mil = DatabaseManager.getMilestone(mil.getId());

        Assert.assertEquals(1, cat.getTasksAll());
        Assert.assertEquals(1, cat.getTasksDone());
        Assert.assertEquals(1, mil.getTasksAll());
        Assert.assertEquals(1, mil.getTasksDone());
        Assert.assertNotNull(mil.getDateCompleted());

        DatabaseManager.switchTaskDone(task.getId());
        DatabaseManager.deleteTask(task.getId());
        cat = DatabaseManager.getCategory(cat.getId());
        mil = DatabaseManager.getMilestone(mil.getId());

        Assert.assertEquals(0, cat.getTasksAll());
        Assert.assertEquals(0, cat.getTasksDone());
        Assert.assertEquals(0, mil.getTasksAll());
        Assert.assertEquals(0, mil.getTasksDone());
        Assert.assertNotNull(mil.getDateCompleted());

        DatabaseManager.deleteMilestone(mil.getId());
        DatabaseManager.deleteCategory(cat.getId());

        DatabaseManager.disconnect();
    }
}


