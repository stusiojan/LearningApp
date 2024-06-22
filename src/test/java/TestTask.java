import main.lib.DatabaseManager;
import main.lib.Task;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;

public class TestTask {
    @Test
    public void TestTaskConstructor() {
        Task newTask = new Task(
                15,
                "Math",
                null,
                "Nothing to study",
                4
        );
        Assert.assertEquals(15, newTask.getId());
    }

    @Test
    public void TestTaskGetter() {
        DatabaseManager.connect(true);
        Task[] tasks = DatabaseManager.getTasks(1);
        Arrays.sort(tasks, Comparator.comparing(Task::getId));

        Assert.assertEquals(1, tasks[0].getId());
        Assert.assertEquals("Typy danych", tasks[0].getName());
        Assert.assertEquals("Ciągi znaków, liczby całkowite i zmiennoprzecinkowe", tasks[0].getDescription());
        Assert.assertEquals(1, tasks[0].getMilestoneId());
        Assert.assertEquals("2024-02-04", tasks[0].getDateCompleted().toString());
        Assert.assertEquals("04-02-2024", tasks[0].dateCompletedToString());

        DatabaseManager.disconnect();
    }

    @Test
    public void TestTaskSetter() {
        Task newTask = new Task(
                15,
                "Math",
                null,
                "Nothing to study",
                4
        );
        newTask.setName("Physics");
        newTask.setDescription("Still nothing to study");
        newTask.setDateCompleted(Date.valueOf(LocalDate.now()));

        Assert.assertEquals("Physics", newTask.getName());
        Assert.assertEquals("Still nothing to study", newTask.getDescription());
        Assert.assertEquals(LocalDate.now(), newTask.getDateCompleted().toLocalDate());
    }

    @Test
    public void TestTaskSetter_LengthLimit() {
        Task newTask = new Task(
                15,
                "Math",
                null,
                "Nothing to study",
                4
        );
        newTask.setName("P".repeat(Task.MAX_NAME_LENGTH + 5));
        newTask.setDescription("S".repeat(Task.MAX_DESCRIPTION_LENGTH + 5));

        Assert.assertEquals("P".repeat(Task.MAX_NAME_LENGTH), newTask.getName());
        Assert.assertEquals("S".repeat(Task.MAX_DESCRIPTION_LENGTH), newTask.getDescription());
    }
}
