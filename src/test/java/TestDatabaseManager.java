import org.junit.Assert;
import org.junit.Test;
import main.lib.*;

public class TestDatabaseManager {
    @Test
    public void testBasic() {
        DatabaseManager.connect(true);
        Category[] categories = DatabaseManager.selectQuery("SELECT * FROM categories", Category::new, Category[]::new);
        Assert.assertEquals("Niemiecki", categories[0].getName());
        Assert.assertEquals("a1b2c3d4e5f6g7h8", DatabaseManager.getSalt("user3"));
        Assert.assertEquals("EMPTY", DatabaseManager.getSalt("Random"));
        DatabaseManager.disconnect();
    }
}
