package TestCase;

import PageObject.Edit_Mating;
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

public class TC04_EditMating extends baseclass {

    private static final Logger logger = LogManager.getLogger(TC04_EditMating.class);

    @Test
    public void editMatingTest() {
        SoftAssert softAssert = new SoftAssert();

        try {
            logger.info("🐁 Starting Edit Mating test...");

            // Step 1: Login
            LoginPage loginPage = new LoginPage(driver);
            loginPage.setEmail(email);
            loginPage.setPassword(password);
            loginPage.clickSubmit();

            new WebDriverWait(driver, Duration.ofSeconds(15))
                    .until(ExpectedConditions.urlContains("HomePage.do"));
            logger.info("🔐 Login successful.");

            // Step 2: Navigate to Colony > Matings
            Edit_Mating matingPage = new Edit_Mating(driver);
            matingPage.openColonyTab();
            matingPage.clickMatingsTab();
            logger.info("📂 Navigated to Matings tab.");

            // Step 3: Edit a Mating Record
            matingPage.clickEditMating();
            logger.info("📝 Opened edit mating form.");

            // Read current comment before update (to verify change later)
            String oldComment = commentTextBeforeEdit();
            logger.info("Old Comment before edit: {}", oldComment);

            // Prepare new unique comment text
            String uniqueSuffix = generateRandomString(6);
            String newComment = "Updated mating test entry via automation - " + uniqueSuffix;

            matingPage.setMatingTag(TestDataUtil.generateRandomTag());  // Optional: still update tag
            matingPage.openDatepicker();
            matingPage.selectExactDate();
            matingPage.setExpectFirstLitter("10");
            matingPage.selectLitterStrain("Test2");
            matingPage.setComment(newComment);

            logger.info("✍️ Updated comment to: {}", newComment);

            // Step 4: Save
            matingPage.clickSave();

            // Wait for page update / navigation after save
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.or(
                            ExpectedConditions.urlContains("Matings"),
                            ExpectedConditions.presenceOfElementLocated(
                                    By.xpath("//*[contains(text(), '" + newComment + "')]")
                            )
                    ));

            // Step 5: Verify comment updated
            boolean commentUpdated = matingPage.isCommentDisplayed(newComment);
            captureScreen(driver, "edit_mating_verification");

            if (commentUpdated) {
                logger.info("✅ Comment updated and found: {}", newComment);
            } else {
                logger.warn("❌ Comment '{}' was NOT found after save.", newComment);
            }

            softAssert.assertTrue(commentUpdated, "Updated comment not found on page: " + newComment);

            softAssert.assertAll();
            logger.info("🏁 Edit Mating test completed successfully.");

        } catch (Exception e) {
            logger.error("🔥 Test failed due to exception: ", e);
            try {
                captureScreen(driver, "edit_mating_failure");
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
