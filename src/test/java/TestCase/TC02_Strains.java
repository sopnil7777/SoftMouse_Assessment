package TestCase;

import PageObject.Strains;
import Utilities.TestDataUtil;
import PageObject.LoginPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class TC02_Strains extends baseclass {
    private static final Logger logger = LogManager.getLogger(TC02_Strains.class);

    @Test
    public void editNewStrainTest() {
        SoftAssert softAssert = new SoftAssert();
        try {
            logger.info("Starting strain creation test...");

            LoginPage loginPage = new LoginPage(driver);
            loginPage.setEmail(email);
            loginPage.setPassword(password);
            loginPage.clickSubmit();

            // Use explicit wait for login completion
            Thread.sleep(3000); // Replace with proper wait for production

            Strains strainsPage = new Strains(driver);
            strainsPage.openColonyTab();

            strainsPage.clickAnimalTab();
            captureScreen(driver, "after_clicking_animal_tab");

            strainsPage.clickJustStrain();

            // Navigate to create new strain
            strainsPage.clickNewStrain();

            // Create random strain name
            String strainName = TestDataUtil.generateStrainName();
            strainsPage.setName(strainName);
            strainsPage.setTailAge("2");
            strainsPage.setWeanAge("1");
            strainsPage.setFemaleMatureAge("3");
            strainsPage.setMaleMatureAge("2");
            strainsPage.setComment("Automated test");

            strainsPage.save();

            // Wait for redirection & page load after saving
            Thread.sleep(4000);  // Consider a smarter wait here

            logger.info("Checking if strain '{}' is displayed on page...", strainName);
            boolean strainFound = strainsPage.isStrainDisplayed(strainName);

            if (!strainFound) {
                logger.warn("Strain '{}' not found immediately, refreshing and retrying...", strainName);
                driver.navigate().refresh();
                Thread.sleep(3000);
                strainFound = strainsPage.isStrainDisplayed(strainName);
            }

            softAssert.assertTrue(strainFound, "Strain not found on page after save: " + strainName);
            captureScreen(driver, "strain_creation_result");

            softAssert.assertAll();
            logger.info("Strain creation test completed successfully for: {}", strainName);

        } catch (Exception e) {
            logger.error("Test failed due to exception: ", e);
            try {
                captureScreen(driver, "test_failure");
            } catch (Exception ioEx) {
                logger.error("Failed to capture screenshot on failure: ", ioEx);
            }
            Assert.fail("Test failed due to: " + e.getMessage());
        }
    }
}
