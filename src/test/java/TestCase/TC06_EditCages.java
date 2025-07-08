package TestCase;
import PageObject.Edit_Cages;
import PageObject.LoginPage;
import Utilities.TestDataUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.Random;

public class TC06_EditCages extends baseclass {

    private static final Logger logger = LogManager.getLogger(TC06_EditCages.class);

    @Test
    public void editCagesTest() {
        SoftAssert softAssert = new SoftAssert();

        try {
            logger.info("🐁 Starting Edit Cages test...");

            // Step 1: Login
            LoginPage loginPage = new LoginPage(driver);
            loginPage.setEmail(email);
            loginPage.setPassword(password);
            loginPage.clickSubmit();

            new WebDriverWait(driver, Duration.ofSeconds(15))
                    .until(ExpectedConditions.urlContains("HomePage.do"));
            logger.info("🔐 Login successful.");

            // Step 2: Navigate to Colony > Cage
            Edit_Cages cagesPage = new Edit_Cages(driver);
            cagesPage.openColonyTab();
            cagesPage.clickCageTab();
            logger.info("📂 Navigated to Cage tab.");

            // Step 3: Open cage edit form
            cagesPage.clickEditCage();
            logger.info("📝 Opened edit cage form.");

            // Step 4: Generate unique Cage Tag
            String uniqueTag = "CageTag_" + generateRandomString(6);

            // Step 5: Update cage tag
            cagesPage.setCageTag(uniqueTag);

            // Optionally update other fields (comment, strain, barcode)
            cagesPage.setComment("Updated cage comment via automation.");
            cagesPage.selectCageStrain();
            cagesPage.setCageBarcode("BARCODE-" + generateRandomString(4));

            // Step 6: Save changes
            cagesPage.saveAndBack();

            // Step 7: Wait for update
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.or(
                            ExpectedConditions.urlContains("Cage"),
                            ExpectedConditions.presenceOfElementLocated(
                                    By.xpath("//*[contains(text(), '" + uniqueTag + "')]")
                            )
                    ));

            // Step 8: Verify the cage tag is uniquely displayed on the page
            boolean isUnique = cagesPage.isCageTagUniqueOnPage(uniqueTag);
            softAssert.assertTrue(isUnique, "Cage tag should be uniquely displayed on page: " + uniqueTag);

            softAssert.assertAll();
            logger.info("🏁 Edit Cages test completed successfully.");

        } catch (Exception e) {
            logger.error("🔥 Test failed due to exception: ", e);
            try {
                captureScreen(driver, "edit_cages_failure");
            } catch (Exception ioEx) {
                logger.error("📸 Failed to capture screenshot on failure: ", ioEx);
            }
            Assert.fail("Test failed due to: " + e.getMessage());
        }
    }

    // Simple random string generator (alphanumeric)
    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random rng = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(rng.nextInt(characters.length())));
        }
        return sb.toString();
    }
}
