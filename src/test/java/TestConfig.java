import main.lib.Config;
import org.junit.Assert;
import org.junit.Test;

public class TestConfig {
    @Test
    public void testConfigDBName() {
        Assert.assertEquals("learning_app_db", Config.getDatabaseName());
    }
}
