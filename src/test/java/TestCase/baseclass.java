package TestCase;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.openqa.selenium.firefox.FirefoxDriver;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import Utilities.readconfig;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class baseclass {

    readconfig rc = new readconfig();

    public String baseurl = rc.getApplicationURL();
    public String email = rc.getEmail();
    public String password = rc.getpassword();

    public static WebDriver driver;
    public static Logger logger = LoggerFactory.getLogger(baseclass.class);

    @Parameters("browser")
    @BeforeClass
    public void setup(@Optional("chrome") String br) {

        logger.info("Initializing browser: {}", br);

        if (br.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*", "ignore-certificate-errors");
            driver = new ChromeDriver(options);
            logger.info("Chrome browser launched.");
        } else if (br.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
            logger.info("Firefox browser launched.");
        } else {
            logger.warn("Unknown browser '{}'. Defaulting to Chrome.", br);
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get(baseurl);

        logger.info("Navigated to URL: {}", baseurl);
    }

    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
            logger.info("Browser closed successfully.");
        }
    }

    public void captureScreen(WebDriver driver, String tname) throws IOException {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        File target = new File(System.getProperty("user.dir") + "/Screenshots/" + tname + ".png");
        FileUtils.copyFile(source, target);
        logger.info("Screenshot taken: {}", target.getAbsolutePath());
    }

    public String RandomString() {
        return RandomStringUtils.randomAlphabetic(6);
    }

    public String RandomNumber() {
        return RandomStringUtils.randomNumeric(5);
    }
}
