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

public class Strains {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private static final Logger logger = LogManager.getLogger(Strains.class);

    @FindBy(xpath = "//*[@id='root']/div[5]/div/div[2]/div[1]/div/div/div")
    private WebElement colonyTab;

    @FindBy(xpath = "//a[contains(text(), 'Animals')]")
    private WebElement animalTab;

    @FindBy(id = "mouseline")
    private WebElement justStrain;

    @FindBy(xpath = "//*[@id=\"262274\"]/td[3]/a")
    private WebElement clickNewStrain;

    @FindBy(id = "name")
    private WebElement nameField;

    @FindBy(id = "tailAge")
    private WebElement tailAgeField;

    @FindBy(id = "weanAge")
    private WebElement weanAgeField;

    @FindBy(id = "femaleMatureAge")
    private WebElement femaleMatureAgeField;

    @FindBy(id = "maleMatureAge")
    private WebElement maleMatureAgeField;

    @FindBy(id = "comment")
    private WebElement commentField;

    @FindBy(xpath = "//*[@id=\"editSaveAndBack\"]")
    private WebElement saveButton;

    public Strains(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    public void openColonyTab() {
        String parentWindow = driver.getWindowHandle();
        wait.until(ExpectedConditions.elementToBeClickable(colonyTab)).click();

        // Wait for new window & switch
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.numberOfWindowsToBe(2));

        Set<String> windows = driver.getWindowHandles();
        for (String window : windows) {
            if (!window.equals(parentWindow)) {
                driver.switchTo().window(window);
                break;
            }
        }
        wait.until(driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
        logger.info("Switched to colony tab window with title: {}", driver.getTitle());
    }

    public void clickAnimalTab() {
        wait.until(ExpectedConditions.elementToBeClickable(animalTab)).click();
        logger.info("Clicked Animal tab");
    }

    public void clickJustStrain() {
        wait.until(ExpectedConditions.elementToBeClickable(justStrain)).click();
        logger.info("Clicked Just Strain");
    }

    public void clickNewStrain() {
        wait.until(ExpectedConditions.elementToBeClickable(clickNewStrain)).click();
        logger.info("Clicked New Strain");
    }

    public void setName(String name) {
        wait.until(ExpectedConditions.visibilityOf(nameField));
        nameField.clear();
        nameField.sendKeys(name);
        logger.info("Set strain name: {}", name);
    }

    public void setTailAge(String age) {
        tailAgeField.clear();
        tailAgeField.sendKeys(age);
    }

    public void setWeanAge(String age) {
        weanAgeField.clear();
        weanAgeField.sendKeys(age);
    }

    public void setFemaleMatureAge(String age) {
        femaleMatureAgeField.clear();
        femaleMatureAgeField.sendKeys(age);
    }

    public void setMaleMatureAge(String age) {
        maleMatureAgeField.clear();
        maleMatureAgeField.sendKeys(age);
    }

    public void setComment(String comment) {
        commentField.clear();
        commentField.sendKeys(comment);
    }

    public void save() {
        wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
        logger.info("Clicked Save button");
    }

    public String getName() {
        wait.until(ExpectedConditions.visibilityOf(nameField));
        return nameField.getAttribute("value");
    }

    /**
     * Checks entire page for strain name text.
     * FluentWait for stability.
     */
    public boolean isStrainDisplayed(String strainName) {
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(20))
                .pollingEvery(Duration.ofSeconds(2))
                .ignoring(NoSuchElementException.class);

        try {
            return fluentWait.until(new Function<WebDriver, Boolean>() {
                public Boolean apply(WebDriver driver) {
                    List<WebElement> elements = driver.findElements(By.xpath("//*[not(self::script) and not(self::style)]"));
                    for (WebElement el : elements) {
                        String text = el.getText().trim();
                        if (!text.isEmpty() && text.equalsIgnoreCase(strainName)) {
                            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);
                            logger.info("Found strain '{}' in page.", strainName);
                            return true;
                        }
                    }
                    return false;
                }
            });
        } catch (TimeoutException e) {
            logger.warn("Timeout while searching for strain: {}", strainName);
            return false;
        }
    }
}
