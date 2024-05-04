import org.junit.Assert;
import org.junit.Test;
import main.lib.*;

import java.sql.SQLException;

public class TestDatabaseManager {
    @Test
    public void testBasic() throws SQLException {
        DatabaseManager.connect(true);
        Category[] categories = DatabaseManager.selectQuery("SELECT * FROM categories", Category::new, Category[]::new);
        Assert.assertEquals(categories[0].getName(), "Bazy Danych 2");
        DatabaseManager.disconnect();
    }
}
