import org.junit.Assert;
import org.junit.Test;
import main.lib.*;

public class TestDatabaseManager {
    @Test
    public void testBasic() {
        DatabaseManager.connect(true);
        Category[] categories = DatabaseManager.selectQuery("SELECT * FROM categories", Category::new, Category[]::new);
        Assert.assertEquals("Bazy Danych 2", categories[0].getName());
        Assert.assertEquals("AAAAAAAAAAAAAAAA", DatabaseManager.getSalt("Kacper"));
        Assert.assertEquals("EMPTY", DatabaseManager.getSalt("Random"));
        DatabaseManager.disconnect();
    }
}
