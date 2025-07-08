package TestCase;

import PageObject.Edit_Animal;
import PageObject.LoginPage;
import Utilities.TestDataUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;

public class TC03_EditAnimal extends baseclass {

    private static final Logger logger = LogManager.getLogger(TC03_EditAnimal.class);

    @Test
    public void editAnimalTest() {
        SoftAssert softAssert = new SoftAssert();

        try {
            logger.info("🐭 Starting Edit Animal test...");

            // Step 1: Login
            LoginPage loginPage = new LoginPage(driver);
            loginPage.setEmail(email);
            loginPage.setPassword(password);
            loginPage.clickSubmit();

            new WebDriverWait(driver, Duration.ofSeconds(15))
                    .until(ExpectedConditions.urlContains("HomePage.do")); 
            logger.info("🔐 Login successful, redirected to dashboard.");

            // Step 2: Navigate to Colony > Animals
            Edit_Animal animalPage = new Edit_Animal(driver);
            animalPage.openColonyTab();
            animalPage.clickAnimalTab();

            // Step 3: Open Edit Page
            animalPage.clickEditAnimal();
            logger.info("🛠️ Opened animal edit form.");

            // Step 4: Input Data
            String randomTag = TestDataUtil.generateRandomTag();
            animalPage.setPhysicalTag(randomTag);
            animalPage.openDatepicker();
            animalPage.selectExactDate();
            animalPage.setComment("Updated via automated test");
            animalPage.setPhenotype("Wild Type");
            animalPage.setPlateId("PLT-101");
            animalPage.selectMouseNotice("Sick");

            logger.info("✍️ Filled form with Tag: {}", randomTag);

            // Step 5: Save
            animalPage.clickSave();

            // Step 6: Wait for redirect/confirmation
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.urlContains("edit.do")); // Update URL keyword if needed

            // Step 7: Validate updated tag is shown
            boolean tagFound = animalPage.isAnimalRecordDisplayed(randomTag);

            if (tagFound) {
                logger.info("✅ Successfully found the updated animal tag on page: {}", randomTag);
            } else {
                logger.warn("❌ Could not find the updated tag '{}' after save!", randomTag);
            }

            softAssert.assertTrue(tagFound, "Updated animal tag not found on page: " + randomTag);
            captureScreen(driver, "animal_edit_verification");

            softAssert.assertAll();
            logger.info("🏁 Edit Animal test completed successfully.");

        } catch (Exception e) {
            logger.error("🔥 Test failed due to exception: ", e);
            try {
                captureScreen(driver, "edit_animal_failure");
            } catch (Exception ioEx) {
                logger.error("📸 Failed to capture screenshot on failure: ", ioEx);
            }
            Assert.fail("Test failed due to: " + e.getMessage());
        }
    }
}
