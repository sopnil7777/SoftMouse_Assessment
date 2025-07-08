
package TestCase;

import org.testng.Assert;
import org.testng.annotations.Test;
import PageObject.LoginPage;

public class TC01_Login extends baseclass {

    @Test
    public void testLogin() throws InterruptedException {
        // Navigate to the login page
        driver.get(baseurl);

        // Create Page Object
        LoginPage lp = new LoginPage(driver);

        // Enter credentials
        lp.setEmail(email);
        Thread.sleep(3000);

        lp.setPassword(password);
        Thread.sleep(3000);


        // Click login
        lp.clickSubmit();

        // Wait for page to load
        Thread.sleep(3000);

        // Validate login by checking title
        String expectedTitle ="Iseehear: SoftMouseDB Transgenic Mouse Breeding & Colony Management Database Software"; 
        String actualTitle = driver.getTitle();

        Assert.assertEquals(actualTitle, expectedTitle, "Login failed: Title does not match.");
    }
}
