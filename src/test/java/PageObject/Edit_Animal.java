package PageObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class Edit_Animal {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private static final Logger logger = LogManager.getLogger(Edit_Animal.class);

    @FindBy(xpath = "//*[@id='root']/div[5]/div/div[2]/div[1]/div/div/div")
    private WebElement colonyTab;

    @FindBy(xpath = "//a[contains(text(), 'Animals')]")
    private WebElement animalTab;

    @FindBy(xpath = "//*[@id=\"26944042\"]/td[6]/a")
    private WebElement editAnimal;

    @FindBy(id = "physicalTag")
    private WebElement physicalTag;

    @FindBy(xpath = "//*[@id=\"editMouseForm\"]/div[1]/div[1]/div[4]/div[1]/div/div/img")
    private WebElement datepicker;

    @FindBy(xpath = "//*[@id=\"ui-datepicker-div\"]/table/tbody/tr[2]/td[2]/a")
    private WebElement exactDate;

    @FindBy(id = "comment")
    private WebElement comment;

    @FindBy(id = "phenotype")
    private WebElement phenotype;

    @FindBy(id = "editSaveAndBack")
    private WebElement editSaveAndBack;

    @FindBy(id = "plateId")
    private WebElement plateId;

    @FindBy(id = "mouselineId")
    private WebElement strainDropdown;

    @FindBy(id = "mouseNoticeId")
    private WebElement mouseNoticeId;

    public Edit_Animal(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    public void openColonyTab() {
        String parentWindow = driver.getWindowHandle();
        wait.until(ExpectedConditions.elementToBeClickable(colonyTab)).click();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.numberOfWindowsToBe(2));

        Set<String> windows = driver.getWindowHandles();
        for (String window : windows) {
            if (!window.equals(parentWindow)) {
                driver.switchTo().window(window);
                break;
            }
        }

        wait.until(driver -> ((JavascriptExecutor) driver)
                .executeScript("return document.readyState").equals("complete"));
        logger.info("Switched to colony tab window with title: {}", driver.getTitle());
    }

    public void clickAnimalTab() {
        wait.until(ExpectedConditions.elementToBeClickable(animalTab)).click();
        logger.info("Clicked Animal tab");
    }

    public void clickEditAnimal() {
        wait.until(ExpectedConditions.elementToBeClickable(editAnimal)).click();
        logger.info("Clicked Edit Animal");
    }

    public void setPhysicalTag(String value) {
        wait.until(ExpectedConditions.elementToBeClickable(physicalTag)).clear();
        physicalTag.sendKeys(value);
        logger.info("Set physicalTag: {}", value);
    }

    public void openDatepicker() {
        wait.until(ExpectedConditions.elementToBeClickable(datepicker)).click();
        logger.info("Opened datepicker");
    }

    public void selectExactDate() {
        wait.until(ExpectedConditions.elementToBeClickable(exactDate)).click();
        logger.info("Selected exact date");
    }

    public void setComment(String text) {
        wait.until(ExpectedConditions.elementToBeClickable(comment)).clear();
        comment.sendKeys(text);
        logger.info("Set comment: {}", text);
    }

    public void setPhenotype(String text) {
        wait.until(ExpectedConditions.elementToBeClickable(phenotype)).clear();
        phenotype.sendKeys(text);
        logger.info("Set phenotype: {}", text);
    }

    public void clickSave() {
        wait.until(ExpectedConditions.elementToBeClickable(editSaveAndBack)).click();
        logger.info("Clicked Save and Back");
    }

    public void setPlateId(String value) {
        wait.until(ExpectedConditions.elementToBeClickable(plateId)).clear();
        plateId.sendKeys(value);
        logger.info("Set plateId: {}", value);
    }

    public void selectMouseNotice(String visibleText) {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(mouseNoticeId));
        Select select = new Select(dropdown);
        select.selectByVisibleText(visibleText);
        logger.info("Selected mouse notice value: {}", visibleText);
    }


    public boolean isAnimalRecordDisplayed(String recordText) {
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(20))
                .pollingEvery(Duration.ofSeconds(2))
                .ignoring(NoSuchElementException.class);

        try {
            return fluentWait.until(driver -> {
                List<WebElement> elements = driver.findElements(By.xpath("//*[not(self::script) and not(self::style)]"));
                for (WebElement el : elements) {
                    String text = el.getText().trim();
                    if (!text.isEmpty() && text.equalsIgnoreCase(recordText)) {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);
                        logger.info("✅ Found Animal record '{}' on the page.", recordText);
                        return true;
                    }
                }
                return false;
            });
        } catch (TimeoutException e) {
            logger.warn("❌ Timeout: Animal record '{}' not found on page.", recordText);
            return false;
        }
    }
}
