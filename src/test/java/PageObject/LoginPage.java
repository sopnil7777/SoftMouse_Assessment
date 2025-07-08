package PageObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage { 

 WebDriver ldriver;
   public LoginPage(WebDriver rdriver){
	   ldriver = rdriver;
	   PageFactory.initElements(rdriver, this);
   }
   @FindBy(id="inputUsernameEmail")
   WebElement txtemail;
   
   @FindBy(id="inputPassword")
   WebElement txtPassword;
     
   @FindBy(id="secureLogin")
   WebElement secureLogin;
   
   @FindBy(xpath="/html/body/div[2]/div/div[3]/div/div[3]")
   WebElement setting;
   
   @FindBy(id="signout")
   WebElement signout;
   
   @FindBy(xpath="//*[@id=\"dialog-form\"]/table/tbody/tr[3]/td[3]/label")
   WebElement Yes;
  

   public void clickLogout(){
	   setting.click();
	   signout.click();
	   Yes.click();
   }
   
   
   public void setEmail(String Email) {
	   txtemail.clear();
	   txtemail.sendKeys(Email); 
   }
   public void setPassword(String password) {
	   txtPassword.clear();
	   txtPassword.sendKeys(password);   
   }
   
   public void  clickSubmit() {
	   secureLogin.click();
   }
   
   
}