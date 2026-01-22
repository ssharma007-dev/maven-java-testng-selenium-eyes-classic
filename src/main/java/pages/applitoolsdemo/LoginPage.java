package pages.applitoolsdemo;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    final WebDriver driver;

    @FindBy(id="username")
    WebElement username;


    @FindBy(id="password")
    WebElement password;

    @FindBy(id="log-in")
    WebElement login_btn;

    public LoginPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void enterUserName(String username_text){
        username.sendKeys(username_text);
    }

    public void enterPassword(String password_text){
        password.sendKeys(password_text);
    }

    public void clickLogin(){
        login_btn.click();
    }

}
