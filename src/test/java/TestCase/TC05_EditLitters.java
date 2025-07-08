package TestCase;

import PageObject.Edit_Litters;
import PageObject.LoginPage;
import Utilities.TestDataUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.Random;

public class TC05_EditLitters extends baseclass {

    private static final Logger logger = LogManager.getLogger(TC05_EditLitters.class);

    @Test
    public void editLittersTest() {
        SoftAssert softAssert = new SoftAssert();

        try {
            logger.info("🐁 Starting Edit Litters test...");

            // Step 1: Login
            LoginPage loginPage = new LoginPage(driver);
            loginPage.setEmail(email);
            loginPage.setPassword(password);
            loginPage.clickSubmit();

            new WebDriverWait(driver, Duration.ofSeconds(15))
                    .until(ExpectedConditions.urlContains("HomePage.do"));
            logger.info("🔐 Login successful.");

            // Step 2: Navigate to Colony > Litters
            Edit_Litters littersPage = new Edit_Litters(driver);
            littersPage.openColonyTab();
            littersPage.clickLittersTab();
            logger.info("📂 Navigated to Litters tab.");

            // Step 3: Edit a Litter record
            littersPage.clickEditLitters();
            logger.info("📝 Opened edit litter form.");

            // Step 4: Read current comment before update (for verification)
            String oldComment = commentTextBeforeEdit();
            logger.info("Old Comment before edit: {}", oldComment);

            // Step 5: Generate unique comment
            String uniqueSuffix = generateRandomString(6);
            String newComment = "Updated litter entry via automation - " + uniqueSuffix;

            // Step 6: Set new values
            littersPage.setLitterTag(TestDataUtil.generateRandomTag());
            littersPage.clickGenerationIdValue();   // or littersPage.selectGenerationIdByVisibleText("value");
            littersPage.setComment(newComment);

            logger.info("✍️ Updated comment to: {}", newComment);

            // Step 7: Save changes
            littersPage.clickEditSaveAndBack();

            // Step 8: Wait for page update / navigation after save
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.or(
                            ExpectedConditions.urlContains("Litters"),
                            ExpectedConditions.presenceOfElementLocated(
                                    By.xpath("//*[contains(text(), '" + newComment + "')]")
                            )
                    ));

            // Step 9: Verify updated comment appears
            boolean commentUpdated = littersPage.isLitterRecordDisplayed(newComment);

            if (commentUpdated) {
                logger.info("✅ Comment updated and found: {}", newComment);
            } else {
                logger.warn("❌ Comment '{}' was NOT found after save.", newComment);
            }
            softAssert.assertTrue(commentUpdated, "Updated comment not found on page: " + newComment);

            softAssert.assertAll();
            logger.info("🏁 Edit Litters test completed successfully.");

        } catch (Exception e) {
            logger.error("🔥 Test failed due to exception: ", e);
            try {
                captureScreen(driver, "edit_litters_failure");
            } catch (Exception ioEx) {
                logger.error("📸 Failed to capture screenshot on failure: ", ioEx);
            }
            Assert.fail("Test failed due to: " + e.getMessage());
        }
    }

    // Helper method to fetch existing comment before edit for verification
    private String commentTextBeforeEdit() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement commentElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("comment")));
            return commentElement.getAttribute("value");
        } catch (Exception e) {
            logger.warn("Could not retrieve existing comment before edit: {}", e.getMessage());
            return "";
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
