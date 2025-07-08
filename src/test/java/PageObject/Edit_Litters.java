package PageObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;
import java.util.Set;

public class Edit_Litters {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private static final Logger logger = LogManager.getLogger(Edit_Litters.class);

    @FindBy(xpath = "//*[@id='root']/div[5]/div/div[2]/div[1]/div/div/div")
    private WebElement colonyTab;

    @FindBy(id = "litters")
    private WebElement litters;

    @FindBy(xpath = "//*[@id=\"3854020\"]/td[2]/a")
    private WebElement editLitters;

    @FindBy(xpath = "//*[@id=\"editLitterForm\"]/div[3]/div/div[1]/div[2]/div/div/input")
    private WebElement litterTag;

    @FindBy(id = "generationId")
    private WebElement generationId;

    @FindBy(xpath = "//*[@id=\"generationId\"]/option[5]")
    private WebElement generationIdValue;

    @FindBy(id = "comment")
    private WebElement comment;

    @FindBy(id = "jqg_pupTable_26955899")
    private WebElement pupInfo1;

    @FindBy(id = "jqg_pupTable_26955901")
    private WebElement pupInfo2;

    @FindBy(id = "editSaveAndBack")
    private WebElement editSaveAndBack;

    public Edit_Litters(WebDriver driver) {
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

        wait.until(d -> ((JavascriptExecutor) d)
                .executeScript("return document.readyState").equals("complete"));
        logger.info("Switched to colony tab window with title: {}", driver.getTitle());
    }

    public void clickLittersTab() {
        wait.until(ExpectedConditions.elementToBeClickable(litters)).click();
        logger.info("Clicked litters tab");
    }

    public void clickEditLitters() {
        wait.until(ExpectedConditions.elementToBeClickable(editLitters)).click();
        logger.info("Clicked Edit Litters");
    }

    public void setLitterTag(String value) {
        wait.until(ExpectedConditions.elementToBeClickable(litterTag)).clear();
        litterTag.sendKeys(value);
        logger.info("Set LitterTag: {}", value);
    }

    public void clickPupInfo1() {
        wait.until(ExpectedConditions.elementToBeClickable(pupInfo1)).click();
        logger.info("Clicked pupInfo1");
    }

    public void clickPupInfo2() {
        wait.until(ExpectedConditions.elementToBeClickable(pupInfo2)).click();
        logger.info("Clicked pupInfo2");
    }

    public void clickEditSaveAndBack() {
        wait.until(ExpectedConditions.elementToBeClickable(editSaveAndBack)).click();
        logger.info("Clicked editSaveAndBack");
    }

    public void clickGenerationIdValue() {
        wait.until(ExpectedConditions.elementToBeClickable(generationIdValue)).click();
        logger.info("Selected generationId_value");
    }

    public void setComment(String text) {
        wait.until(ExpectedConditions.elementToBeClickable(comment)).clear();
        comment.sendKeys(text);
        logger.info("Set comment: {}", text);
    }

    public void clickSave() {
        wait.until(ExpectedConditions.elementToBeClickable(editSaveAndBack)).click();
        logger.info("Clicked Save and Back");
    }

    public void selectGenerationIdByVisibleText(String visibleText) {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(generationId));
        Select select = new Select(dropdown);
        select.selectByVisibleText(visibleText);
        logger.info("Selected generationId value: {}", visibleText);
    }

    public boolean isLitterRecordDisplayed(String recordText) {
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
                        logger.info("✅ Found Litters record '{}' on the page.", recordText);
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
