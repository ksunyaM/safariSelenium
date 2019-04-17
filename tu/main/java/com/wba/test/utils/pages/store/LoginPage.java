/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.store;

import static com.oneleo.test.automation.core.UIUtils.ui;

import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

import com.wba.test.utils.pages.store.DashboardPage;
import com.wba.test.utils.AwtRobot;
import com.wba.test.utils.StatefulSingleton;
import com.wba.test.utils.pages.stockplus.OrderItemsPage;
import com.wba.test.utils.pages.stockplus.PurchaseOrderPage;

public class LoginPage extends LoadableComponent<LoginPage> {

    @FindBy(id = "username")
    private WebElement username;
    @FindBy(id = "password")
    private WebElement password;

    @FindBy(xpath = "/html/body/div/div[2]/div/form/div[4]/a")
    private WebElement signInButton;
    @FindBy(css = "#syncAccounts > mat-sidenav-content > rx-login-form > form > div:nth-child(4) > button.mat-raised-button.mat-tertiary")
    private WebElement cancelButton;
    
    @FindBy(css = "#syncAccounts > mat-sidenav-content > rx-login-form > form > div:nth-child(4) > button.mat-raised-button.mat-tertiary")
    private WebElement oodsReceiptDetailsButton;
    
    private Actions action;
    private AwtRobot awtRobot;

    private WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }
    
    public void login() throws Throwable {

    	StatefulSingleton.instance().setDriver(driver);
	    Thread.sleep(8000);
	    try{
	        driver.findElement(By.xpath("//*[@id='rx-app-nav']/rx-uikit-app-bar/div/button")).click();
	    }catch(Exception E){
	        awtRobot= new AwtRobot(200);
	        awtRobot.typeString("devrph00");
	        awtRobot.sendSeleniumKeys(Keys.TAB,1);
	        awtRobot.typeString("Welcome1");
	        awtRobot.sendSeleniumKeys(Keys.ENTER,1);
	    }

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
		 Object arr[]=listWindows.toArray();
		 return (String)arr[index].toString();
	}
    
    public DashboardPage navigateToDashboardPage(String userName, String password) throws Exception {
		synchronized (this) {
			wait(25000);
		}
		Set<String> listWindows = driver.getWindowHandles();
		driver.switchTo().window(windowsLogin(listWindows, 1));
		insertCredential(userName, password);
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(signInButton));
		signInButton.click();
		listWindows = driver.getWindowHandles();
		driver.switchTo().window(windowsLogin(listWindows, 0));
		return new DashboardPage(driver);
	}

    public OrderItemsPage navigateToOrderItemsPage(String userName, String password) throws Exception {
        insertCredential(userName, password);
        ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(signInButton));
        signInButton.click();
        return new OrderItemsPage(driver);
    }

    public PurchaseOrderPage navigateToPurchaseOrderPage(String userName, String password) throws Exception {
    	ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(username));
    	
        insertCredential(userName, password);
        ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(signInButton));
        signInButton.click();
        return new PurchaseOrderPage(driver);
    }

    public PurchaseOrderPage navigateToPurchaseOrderPageMng(String userName, String password) throws Exception {
        insertCredential(userName, password);
        ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(signInButton));
        signInButton.click();
        return new PurchaseOrderPage(driver);
    }

    
    public PurchaseOrderPage navigateToPurchaseOrderPageHotKeyAltS(String userName, String password) throws Exception {

        insertCredential(userName, password);
        ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(signInButton));
        Actions keyAction = new Actions(driver);
        keyAction.keyDown(Keys.ALT).sendKeys("s").perform();
        return new PurchaseOrderPage(driver);
    }

    public PurchaseOrderPage navigateToPurchaseOrderPageHotKeEnter(String userName, String password) throws Exception {

        insertCredential(userName, password);
        ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(signInButton));
        Actions keyAction = new Actions(driver);
        this.password.sendKeys(Keys.ENTER);
        return new PurchaseOrderPage(driver);
    }

    

    @Override
    protected void load() {
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void isLoaded() throws Error {
        if (Objects.isNull(signInButton)) {
            throw new Error();
        }
        try {
            // assertThat(signInButton.isDisplayed(), equalTo(Boolean.TRUE));
        } catch (NoSuchElementException e) {
            Assert.fail("Page is not loaded!");
        }
    }
}
