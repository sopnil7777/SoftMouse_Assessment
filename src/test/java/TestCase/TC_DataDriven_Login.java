package TestCase;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import PageObject.LoginPage;
import Utilities.XLUtils;

public class TC_DataDriven_Login extends baseclass {

	@Test(dataProvider = "LoginData")
	public void loginTest(String email, String password) throws InterruptedException {

	    driver.get(baseurl);
	    logger.info("Navigated to: " + baseurl);
	    Thread.sleep(3000);

	    LoginPage lp = new LoginPage(driver);

	    lp.setEmail(email);
	    logger.info("Entered email: " + email);
	    Thread.sleep(3000);

	    lp.setPassword(password);
	    logger.info("Entered password");
	    Thread.sleep(3000);

	    lp.clickSubmit();
	    Thread.sleep(3000); // Consider using WebDriverWait for production-level tests

	    try {
	        String pageContent = driver.findElement(By.tagName("body")).getText();

	        if (pageContent.contains("Welcome") &&
	            pageContent.contains("to") &&
	            pageContent.contains("SoftMouse.NET!") &&
	            pageContent.contains("Pick an Option to Get Started")) {

	            logger.info("Login successful for user: " + email);
	            Assert.assertTrue(true);
	            Thread.sleep(3000);

	            lp.clickLogout(); // Log out only if login was successful
	            Thread.sleep(3000);
	        } else {
	            logger.warn("Login failed for user: " + email + ". Clearing fields.");
	            Assert.assertTrue(false);
	        }

	    } catch (Exception e) {
	        logger.error("Exception occurred while checking login success for user: " + email, e);
	        Assert.assertTrue(false);
	    }
	}

    

	@DataProvider(name = "LoginData")
	public String[][] getData() throws IOException {
	    String path = System.getProperty("user.dir") + "\\src\\test\\java\\TestData\\Softmouse.xlsx";
	    
	    System.out.println("Reading Excel file from: " + path); 

	    int rownum = XLUtils.getRowCount(path, "Sheet1");
	    int colcount = XLUtils.getCellCount(path, "Sheet1", 1);

	    System.out.println("Row count: " + rownum + ", Column count: " + colcount); 

	    String loginData[][] = new String[rownum][colcount];

	    for (int i = 1; i <= rownum; i++) {
	        for (int j = 0; j < colcount; j++) {
	            loginData[i - 1][j] = XLUtils.getCellData(path, "Sheet1", i, j);
	            System.out.println("Data [" + (i - 1) + "][" + j + "]: " + loginData[i - 1][j]); 
	        }
	    }

	    return loginData;
	}
}