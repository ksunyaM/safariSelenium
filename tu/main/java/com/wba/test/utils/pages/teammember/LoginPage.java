/*******************************************************************************
 * Copyright 2019 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.teammember;

import com.wba.test.utils.StatefulSingleton;
import org.junit.Assert;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.net.MalformedURLException;
import java.util.Objects;
import java.util.Set;

import static com.oneleo.test.automation.core.UIUtils.ui;


public class LoginPage extends LoadableComponent<LoginPage> {

    @FindBy(id = "username")
    private WebElement username;
    @FindBy(id = "password")
    private WebElement password;
    @FindBy(xpath = "//a[contains(@title,'Sign On')]")
    private WebElement signInButton;

    private final String SIGN_IN_XPATH = "//a[contains(@title,'Sign On')]";

    private WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        StatefulSingleton.instance().setDriver(driver);
    }

    public boolean isSignInButtonDisplayed() {
        return signInButton.isDisplayed();
    }

    public void goToLoginPage() throws MalformedURLException {
        CommonUtils.fluentWaitForObject(By.xpath(SIGN_IN_XPATH));
    }

    public void insertLoginCredentials(String username, String password) throws Throwable {
        CommonUtils.fluentWaitForObject(By.xpath(SIGN_IN_XPATH));
        this.username.click();
        this.username.clear();
        if (StringUtils.isNotBlank(username)) {
            this.username.sendKeys(username);
            ui().waitForCondition(driver, ExpectedConditions.textToBePresentInElementValue(this.username, username));
        } else {
            throw new Exception("username missing");
        }
        this.password.click();
        this.password.clear();
        if (StringUtils.isNotBlank(password)) {
            this.password.sendKeys(password);
            ui().waitForCondition(driver, ExpectedConditions.textToBePresentInElementValue(this.password, password));
        } else {
            throw new Exception("password missing");
        }
    }

    protected String windowsLogin(Set<String> listWindows, int index) {
        Object arr[] = listWindows.toArray();
        return (String) arr[index].toString();
    }


    public AdminMainPage clickSignInButton() {
        CommonUtils.fluentWaitForObject(By.xpath(SIGN_IN_XPATH));
        signInButton.click();
        return new AdminMainPage(driver);
    }

    @Override
    protected void isLoaded() throws Error {
        if (Objects.isNull(signInButton)) {
            throw new Error();
        }
        try {
        } catch (NoSuchElementException e) {
            Assert.fail("Page is not loaded!");
        }
    }

    @Override
    protected void load() {
        PageFactory.initElements(driver, this);
    }
}
