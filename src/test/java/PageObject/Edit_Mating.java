package PageObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;

public class Edit_Mating {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private static final Logger logger = LogManager.getLogger(Edit_Mating.class);

    public Edit_Mating(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    // Web Elements
    @FindBy(xpath = "//*[@id='root']/div[5]/div/div[2]/div[1]/div/div/div")
    private WebElement colonyTab;

    @FindBy(id = "matings")
    private WebElement matings;

    @FindBy(xpath = "//*[@id=\"1718225\"]/td[3]/a")
    private WebElement editMating;

    @FindBy(xpath = "//*[@id=\"matingForm\"]/div/div/div/div[1]/div[2]/div/div/input")
    private WebElement matingTag;

    @FindBy(xpath = "//*[@id=\"matingForm\"]/div/div/div/div[2]/div[2]/div/div[2]/input")
    private WebElement expectFirstLitter;

    @FindBy(id = "comment")
    private WebElement comment;

    @FindBy(xpath = "//*[@id=\"setupDate\"]")
    private WebElement datepicker;

    @FindBy(xpath = "//*[@id=\"ui-datepicker-div\"]/table/tbody/tr[1]/td[5]")
    private WebElement exactDate;

    @FindBy(id = "saveback")
    private WebElement saveBack;

    // Methods
    public void openColonyTab() {
        String parentWindow = driver.getWindowHandle();
        wait.until(ExpectedConditions.elementToBeClickable(colonyTab)).click();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.numberOfWindowsToBe(2));

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

    public void clickMatingsTab() {
        wait.until(ExpectedConditions.elementToBeClickable(matings)).click();
        logger.info("Clicked on 'Matings' tab.");
    }

    public void clickEditMating() {
        wait.until(ExpectedConditions.elementToBeClickable(editMating)).click();
        logger.info("Clicked 'Edit' button for mating.");
    }

    public void setMatingTag(String tag) {
        wait.until(ExpectedConditions.elementToBeClickable(matingTag)).clear();
        matingTag.sendKeys(tag);
        logger.info("Set Mating Tag: {}", tag);
    }

    public void openDatepicker() {
        wait.until(ExpectedConditions.elementToBeClickable(datepicker)).click();
        logger.info("Opened Mating Datepicker");
    }

    public void selectExactDate() {
        wait.until(ExpectedConditions.elementToBeClickable(exactDate)).click();
        logger.info("Selected exact mating date.");
    }

    public void setExpectFirstLitter(String date) {
        wait.until(ExpectedConditions.elementToBeClickable(expectFirstLitter)).clear();
        expectFirstLitter.sendKeys(date);
        logger.info("Set Expected First Litter date: {}", date);
    }

    public void setComment(String text) {
        wait.until(ExpectedConditions.elementToBeClickable(comment)).clear();
        comment.sendKeys(text);
        logger.info("Set Comment: {}", text);
    }

    public void selectLitterStrain(String visibleText) {
        try {
            WebElement selectElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("selectmouselineid")));
            Select dropdown = new Select(selectElement);
            dropdown.selectByVisibleText(visibleText);
            logger.info("Selected Litter Strain option: {}", visibleText);
        } catch (NoSuchElementException e) {
            logger.error("Option '{}' not found in Litter Strain dropdown.", visibleText);
            throw e;
        }
    }

    public void closeDatepickerIfOpen() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            WebElement datepickerPopup = shortWait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("ui-datepicker-div"))
            );
            if (datepickerPopup.isDisplayed()) {
                driver.findElement(By.tagName("body")).click();
                logger.info("Closed datepicker popup.");
            }
        } catch (TimeoutException e) {
            logger.info("Datepicker popup was not open.");
        }
    }

    public void clickSave() {
        closeDatepickerIfOpen();
        wait.until(ExpectedConditions.elementToBeClickable(saveBack)).click();
        logger.info("Clicked Save button.");
    }

    public boolean isCommentDisplayed(String commentText) {
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(20))
                .pollingEvery(Duration.ofSeconds(2))
                .ignoring(NoSuchElementException.class);

        try {
            return fluentWait.until(d -> {
                List<WebElement> elements = d.findElements(By.xpath("//*[not(self::script) and not(self::style)]"));
                for (WebElement el : elements) {
                    String text = el.getText().trim();
                    if (!text.isEmpty() && text.equalsIgnoreCase(commentText)) {
                        ((JavascriptExecutor) d).executeScript("arguments[0].scrollIntoView(true);", el);
                        logger.info("✅ Found comment '{}' on the page.", commentText);
                        return true;
                    }
                }
                return false;
            });
        } catch (TimeoutException e) {
            logger.warn("❌ Timeout: comment '{}' not found on page.", commentText);
            return false;
        }
    }
}
