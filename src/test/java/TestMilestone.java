import main.lib.DatabaseManager;
import main.lib.Milestone;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;

public class TestMilestone {
    @Test
    public void TestMilestoneConstructor() {
        Milestone newMilestone = new Milestone(
                15,
                "Etap 1",
                null,
                null,
                null,
                "Nothing",
                0,
                0,
                1,
                1
        );
        Assert.assertEquals(15, newMilestone.getId());
    }

    @Test
    public void TestMilestoneGetter() {
        DatabaseManager.connect(true);
        Milestone[] milestones = DatabaseManager.getMilestones(2, 1);
        Arrays.sort(milestones, Comparator.comparing(Milestone::getId));

        Assert.assertEquals(1, milestones[0].getId());
        Assert.assertEquals("Podstawy języka", milestones[0].getName());
        Assert.assertEquals("Najważniejsze elementy języka", milestones[0].getDescription());
        Assert.assertEquals("2024-02-02", milestones[0].getDateAdded().toString());
        Assert.assertEquals("2024-03-02", milestones[0].getDeadline().toString());
        Assert.assertEquals("2024-02-28", milestones[0].getDateCompleted().toString());
        Assert.assertEquals(3, milestones[0].getTasksAll());
        Assert.assertEquals(3, milestones[0].getTasksDone());
        Assert.assertEquals(1, milestones[0].getUserId());
        Assert.assertEquals(2, milestones[0].getCategoryId());

        DatabaseManager.disconnect();
    }

    @Test
    public void TestMilestoneSetter() {
        Milestone newMilestone = new Milestone(
                15,
                "Etap 1",
                null,
                null,
                null,
                "Nothing",
                0,
                0,
                1,
                1
        );
        newMilestone.setName("Physics");
        newMilestone.setDescription("Still nothing to study");
        newMilestone.setDeadline(Date.valueOf(LocalDate.now()));

        Assert.assertEquals("Physics", newMilestone.getName());
        Assert.assertEquals("Still nothing to study", newMilestone.getDescription());
        Assert.assertEquals(LocalDate.now(), newMilestone.getDeadline().toLocalDate());
    }

    @Test
    public void TestMilestoneSetter_LengthLimit() {
        Milestone newMilestone = new Milestone(
                15,
                "Etap 1",
                null,
                null,
                null,
                "Nothing",
                0,
                0,
                1,
                1
        );
        newMilestone.setName("P".repeat(Milestone.MAX_NAME_LENGTH + 5));
        newMilestone.setDescription("S".repeat(Milestone.MAX_DESCRIPTION_LENGTH + 5));

        Assert.assertEquals("P".repeat(Milestone.MAX_NAME_LENGTH), newMilestone.getName());
        Assert.assertEquals("S".repeat(Milestone.MAX_DESCRIPTION_LENGTH), newMilestone.getDescription());
    }
}
