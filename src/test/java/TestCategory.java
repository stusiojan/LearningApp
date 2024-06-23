import java.sql.ResultSet;

import org.junit.Assert;
import org.junit.Test;
import main.lib.*;


public class TestCategory {
    @Test
    public void testCategoryGetter() {
        DatabaseManager.connect(true);

        Category[] categories = DatabaseManager.getCategories();
        Assert.assertEquals(3, categories[2].getId());
        Assert.assertEquals("Bazy Danych 2", categories[2].getName());
        Assert.assertEquals(
                "Projektowanie aplikacji z bazami danych przy użyciu zaawansowanych mechanizmów",
                categories[2].getDescription()
        );
        Assert.assertEquals(9, categories[2].getTasksAll());
        Assert.assertEquals(6, categories[2].getTasksDone());

        DatabaseManager.disconnect();
    }

    @Test
    public void testCategorySetter() {
        DatabaseManager.connect(true);

        Category[] categories = DatabaseManager.getCategories();
        Category category = categories[2];
        category.setName("Bazy Danych 2 - Zmiana");
        category.setDescription("Zmiana opisu");
        Assert.assertEquals("Bazy Danych 2 - Zmiana", category.getName());
        Assert.assertEquals("Zmiana opisu", category.getDescription());

        DatabaseManager.disconnect();
    }

    @Test
    public void testCategorySetter_LengthLimit() {
        DatabaseManager.connect(true);

        Category[] categories = DatabaseManager.getCategories();
        Category category = categories[2];
        category.setName("A".repeat(Category.MAX_NAME_LENGTH + 5));
        category.setDescription("B".repeat(Category.MAX_DESCRIPTION_LENGTH + 5));
        Assert.assertEquals("A".repeat(Category.MAX_NAME_LENGTH), category.getName());
        Assert.assertEquals(Category.MAX_NAME_LENGTH, category.getName().length());
        Assert.assertEquals("B".repeat(Category.MAX_DESCRIPTION_LENGTH), category.getDescription());
        Assert.assertEquals(Category.MAX_DESCRIPTION_LENGTH, category.getDescription().length());

        DatabaseManager.disconnect();
    }
}
