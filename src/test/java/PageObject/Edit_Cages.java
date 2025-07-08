package PageObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;

public class Edit_Cages {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private static final Logger logger = LogManager.getLogger(Edit_Cages.class);

    public Edit_Cages(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    // Web Elements
    @FindBy(xpath = "//*[@id='root']/div[5]/div/div[2]/div[1]/div/div/div")
    private WebElement colonyTab;

    @FindBy(id = "cage")
    private WebElement cageTab;

    @FindBy(xpath = "//*[@id=\"6869963\"]/td[5]/a")
    private WebElement editCageButton;

    @FindBy(id = "cageTag")
    private WebElement cageTagInput;

    @FindBy(id = "cageMouselineId")
    private WebElement cageStrainDropdown;

    @FindBy(xpath = "//*[@id=\"cageMouselineId\"]/option[4]")
    private WebElement cageStrainOption;

    @FindBy(id = "cageBarcode")
    private WebElement cageBarcodeInput;

    @FindBy(id = "comment")
    private WebElement commentInput;

    @FindBy(id = "editSaveAndBack")
    private WebElement saveAndBackButton;

    // Methods

    public void openColonyTab() {
        String parentWindow = driver.getWindowHandle();
        wait.until(ExpectedConditions.elementToBeClickable(colonyTab)).click();

        wait.until(ExpectedConditions.numberOfWindowsToBe(2));

        for (String window : driver.getWindowHandles()) {
            if (!window.equals(parentWindow)) {
                driver.switchTo().window(window);
                break;
            }
        }

        wait.until(d -> ((JavascriptExecutor) d)
                .executeScript("return document.readyState").equals("complete"));
        logger.info("Switched to colony tab window: {}", driver.getTitle());
    }

    public void clickCageTab() {
        wait.until(ExpectedConditions.elementToBeClickable(cageTab)).click();
        logger.info("Clicked on 'Cage' tab.");
    }

    public void clickEditCage() {
        wait.until(ExpectedConditions.elementToBeClickable(editCageButton)).click();
        logger.info("Clicked 'Edit' button for cage.");
    }

    public void setCageTag(String tag) {
        wait.until(ExpectedConditions.elementToBeClickable(cageTagInput)).clear();
        cageTagInput.sendKeys(tag);
        logger.info("Set Cage Tag: {}", tag);
    }

    public void selectCageStrainByVisibleText(String visibleText) {
        WebElement selectElement = wait.until(ExpectedConditions.visibilityOf(cageStrainDropdown));
        Select select = new Select(selectElement);
        select.selectByVisibleText(visibleText);
        logger.info("Selected Cage Strain: {}", visibleText);
    }

    public void setCageBarcode(String barcode) {
        wait.until(ExpectedConditions.elementToBeClickable(cageBarcodeInput)).clear();
        cageBarcodeInput.sendKeys(barcode);
        logger.info("Set Cage Barcode: {}", barcode);
    }

    public void setComment(String text) {
        wait.until(ExpectedConditions.elementToBeClickable(commentInput)).clear();
        commentInput.sendKeys(text);
        logger.info("Set Comment: {}", text);
    }
    public void selectCageStrain() {
        wait.until(ExpectedConditions.elementToBeClickable(cageStrainDropdown)).click();
        logger.info("Clicked cageStrainDropdown to open options.");
        
        wait.until(ExpectedConditions.elementToBeClickable(cageStrainOption)).click();
        logger.info("Clicked cageStrainOption to select the strain.");
    }


    public void closeDatepickerIfOpen() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            WebElement datepickerPopup = shortWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ui-datepicker-div")));
            if (datepickerPopup.isDisplayed()) {
                driver.findElement(By.tagName("body")).click();
                logger.info("Closed datepicker popup.");
            }
        } catch (TimeoutException e) {
            logger.info("Datepicker popup was not open.");
        }
    }

    public void saveAndBack() {
        closeDatepickerIfOpen();
        wait.until(ExpectedConditions.elementToBeClickable(saveAndBackButton)).click();
        logger.info("Clicked Save and Back.");
    }

    /**
     * Verify that the cage tag is uniquely displayed on the page.
     * Returns true only if exactly one element on the page matches the tag text.
     */
    public boolean isCageTagUniqueOnPage(String cageTag) {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body"))); // ensure page loaded
        List<WebElement> matchingElements = driver.findElements(By.xpath("//*[text()[normalize-space()='" + cageTag + "']]"));

        if (matchingElements.size() == 1) {
            WebElement el = matchingElements.get(0);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);
            logger.info("✅ Cage Tag '{}' found uniquely on the page.", cageTag);
            return true;
        } else if (matchingElements.size() > 1) {
            logger.warn("❌ Cage Tag '{}' found multiple times ({} times) on the page.", cageTag, matchingElements.size());
            return false;
        } else {
            logger.warn("❌ Cage Tag '{}' not found on the page.", cageTag);
            return false;
        }
    }
}
