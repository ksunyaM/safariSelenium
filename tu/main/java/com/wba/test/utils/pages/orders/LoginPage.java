/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.orders;

import java.util.Objects;
import com.wba.test.utils.AwtRobot;
import com.wba.test.utils.StatefulSingleton;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;


public class LoginPage extends LoadableComponent<LoginPage>{
	
	@FindBy(id = "username")
	private WebElement username;
	@FindBy(id = "password")
	private WebElement password;
	//@FindBy(css = "body > div > div.bg-box > div > div > form > div.ping-button-container > a")
	@FindBy(xpath = "//a[contains(@title,'Log in')]")
	private WebElement signInButton;
	//@FindBy(css = "#syncAccounts > mat-sidenav-content > rx-login-form > form > div:nth-child(4) > button.mat-raised-button.mat-tertiary")
	//private WebElement cancelButton;
	
	
	
	private WebDriver driver;
	private AwtRobot awtRobot;

	public LoginPage(WebDriver driver) {
		this.driver = driver;
	}
	
	/*protected void insertCredential(String userName, String password) throws Exception {
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
	}*/

	public OrderHomePage navigateToOrderHomePage(String userName, String password) throws Throwable {
		synchronized (this) {
			wait(25000);
		}
		/*Set<String> listWindows = driver.getWindowHandles();
		driver.switchTo().window(windowsLogin(listWindows, 1));
		insertCredential(userName, password);
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(signInButton));
		signInButton.click();
		listWindows = driver.getWindowHandles();
		driver.switchTo().window(windowsLogin(listWindows, 0));*/
		StatefulSingleton.instance().setDriver(driver);
		Thread.sleep(8000);
		try{
			driver.findElement(By.xpath("//*[@id='rx-app-nav']/rx-uikit-app-bar/div/button")).click();
		}catch(Exception E){
			awtRobot= new AwtRobot(200);
			awtRobot.typeString(userName);
			awtRobot.sendSeleniumKeys(Keys.TAB,1);
			awtRobot.typeString(password);
			awtRobot.sendSeleniumKeys(Keys.ENTER,1);
			//driver.findElement(By.xpath("//*[@id='rx-app-nav']/rx-uikit-app-bar/div/button")).click();

		}
		return new OrderHomePage(driver);
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
