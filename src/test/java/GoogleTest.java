import org.testng.Assert;
import org.testng.annotations.Test;

public class GoogleTest extends BaseTest {

    @Test
    public void testGoogleTitle() throws InterruptedException {
        //docker compose up -d
        //http://localhost:4444/ui/#
        //mvn clean test
        //docker compose down
        //docker compose down -v
        test.get().info("Opening Google...");
        driver.get("https://www.google.com");
        Thread.sleep(4000);
        Assert.assertTrue(driver.getTitle().contains("Google"));
        System.out.println("Title verified: " + driver.getTitle());
        test.get().pass("Google Title Test Passed");
    }

    @Test
    public void testWrongGoogleTitle() throws InterruptedException {
        test.get().info("Opening Google...");

        driver.get("https://www.google.com");
        Thread.sleep(3000);

        String title = driver.getTitle();
        if (!title.contains("Bing")) {  // Wrong on purpose to fail
            test.get().fail("Expected title to contain 'Bing' but found: " + title);
            Assert.fail("Title verification failed!");
        }

        test.get().pass("Google Title Test Passed");
    }
}
