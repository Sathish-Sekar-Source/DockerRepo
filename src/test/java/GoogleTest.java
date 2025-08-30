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

        driver.get("https://www.google.com");
        Thread.sleep(5000);
        Assert.assertTrue(driver.getTitle().contains("Google"));
        System.out.println("Title verified: " + driver.getTitle());
    }
}
