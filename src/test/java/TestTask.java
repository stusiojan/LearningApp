import main.lib.DatabaseManager;
import main.lib.Task;
import org.junit.Assert;
import org.junit.Test;

public class TestTask {
    @Test
    public void TestTaskGetter() {
        DatabaseManager.connect(true);
        Task[] tasks = DatabaseManager.getTasks(1);
//        Task task_nr_1 =
        Assert.assertEquals(1, tasks[0].getId());
        Assert.assertEquals("Typy danych", tasks[0].getName());
        Assert.assertEquals("Ciągi znaków, liczby całkowite i zmiennoprzecinkowe", tasks[0].getDescription());
        Assert.assertEquals(1, tasks[0].getMilestoneId());
        Assert.assertEquals("04-02-2024", String.valueOf(tasks[0].getDateCompleted()));

        DatabaseManager.disconnect();
    }
}
