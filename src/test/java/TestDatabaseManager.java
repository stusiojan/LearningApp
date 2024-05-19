import org.junit.Assert;
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
        Assert.assertTrue(DatabaseManager.hasUser(login));
        Assert.assertEquals(salt, DatabaseManager.getSalt(login));

        DatabaseManager.deleteUser(login);
        Assert.assertFalse(DatabaseManager.hasUser(login));

        DatabaseManager.disconnect();
    }
}
