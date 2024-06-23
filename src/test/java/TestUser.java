import main.lib.User;
import org.junit.Assert;
import org.junit.Test;

public class TestUser {
    @Test
    public void testGenerateSalt() {
        Assert.assertEquals(16, User.generateSalt().length());
    }

    @Test
    public void computeHash() {
        char[] password = "password".toCharArray();
        String salt = User.generateSalt();
        String hash = User.computeHash(password, salt);
        Assert.assertEquals(64, hash.length());
    }
}
