/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.uaam;

import static com.oneleo.test.automation.core.UIUtils.ui;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

public class LoginPage extends LoadableComponent<LoginPage> {

    @FindBy(id = "username")
    private WebElement username;
    @FindBy(id = "password")
    private WebElement password;
    @FindBy(xpath = "//a[contains(@title,'Log in')]")
    private WebElement signInButton;

    private Set<String> listWindows;
    private WebDriver driver;
    private static final String VK_ = "VK_";

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    protected void insertCredential(String userName, String password) throws Exception {
        this.username.clear();
        if (StringUtils.isNotBlank(userName)) {
            this.username.sendKeys(userName);
            ui().waitForCondition(driver, ExpectedConditions.textToBePresentInElementValue(this.username, userName));
        } else {
            throw new java.lang.Exception("userName missing");
        }
        this.password.clear();
        if (StringUtils.isNotBlank(password)) {
            this.password.sendKeys(password);
            ui().waitForCondition(driver, ExpectedConditions.textToBePresentInElementValue(this.password, password));
        } else {
            throw new java.lang.Exception("password missing");
        }
    }

    protected String windowsLogin(Set<String> listWindows, int index) {
        Object arr[] = listWindows.toArray();
        return (String) arr[index].toString();
    }

    public RolesAndPermissionsPage navigateToRolesAndPermissionsPage(String userName, String password) throws Exception {
        synchronized (this) {
            wait(25000);
        }
        Set<String> listWindows = driver.getWindowHandles();
        driver.switchTo().window(windowsLogin(listWindows, 1));
        insertCredential(userName, password);
        driver.close();
        listWindows = driver.getWindowHandles();
        driver.switchTo().window(windowsLogin(listWindows, 0));
        return new RolesAndPermissionsPage(driver);
    }

    public RolesAndPermissionsPage navigateToRolesAndPermissionsPageFromKeycloackLogin(String userName, String password) throws Exception {
        Robot robot = new Robot();
        traslateToKeyEvent(userName, robot);
        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);
        traslateToKeyEvent(password, robot);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        synchronized (this) {
            wait(5000);
        }
        return new RolesAndPermissionsPage(driver);
    }

    public void insertCredentialKeycloackLogin(String userName, String password) throws Exception {
        synchronized (this) {
            wait(25000);
        }
        listWindows = driver.getWindowHandles();
        synchronized (this) {
            wait(6000);
        }
        driver.switchTo().window(windowsLogin(listWindows, 1));
        insertCredential(userName, password);
        signInButton.click();
    }

    public Boolean isLoginClosed() {
        Boolean loginClosed = Boolean.FALSE;
        try {
            driver.switchTo().window(windowsLogin(listWindows, 1));
        } catch (NoSuchWindowException | ArrayIndexOutOfBoundsException e) {
            loginClosed = Boolean.TRUE;
        }
        return loginClosed;
    }

    public Boolean isFieldLoaded(String field) {
        return !Objects.isNull(field);
    }

    public String getErrorMessage() throws Exception {
        synchronized (this) {
            wait(10000);
        }
        WebElement errorMessage = driver.findElement(By.className("ping-error"));
        return errorMessage.getText();
    }

    private static void traslateToKeyEvent(String input, Robot robot) {
        char[] charInput = input.toCharArray();
        Character[] charInputArray = ArrayUtils.toObject(charInput);
        Arrays.stream(charInputArray).forEach(s -> {

            if (Character.isUpperCase(s)) {
                robot.keyPress(KeyEvent.VK_SHIFT);
            }
            robot.keyPress(Character.toUpperCase(s));
            robot.keyRelease(Character.toUpperCase(s));
            robot.keyRelease(KeyEvent.VK_SHIFT);
        });
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
