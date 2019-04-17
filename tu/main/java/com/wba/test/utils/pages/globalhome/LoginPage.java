/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.globalhome;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.*;
import static com.oneleo.test.automation.core.UIUtils.ui;
import java.net.MalformedURLException;

import java.util.Objects;
import java.util.Set;
import org.junit.Assert;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;


public class LoginPage extends LoadableComponent<LoginPage>{

	@FindBy(id = "username")
	private WebElement username;
	@FindBy(id = "password")
	private WebElement password;
	@FindBy(xpath = "//*[@id='rx-app-nav']/rx-uikit-app-bar/div/button")
	private WebElement signInButton;

	private WebDriver driver;

	public LoginPage(WebDriver driver) {
		this.driver = driver;
	}

	public boolean isSignInButtonDisplayed() {
		return signInButton.isDisplayed();
	}

	public void goToLoginPage() throws MalformedURLException {
        synchronized (this) {
            try {
                wait(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

	public void insertLoginCredentials(String username, String password) throws Throwable {
		synchronized (this) {
			try {
				wait(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.username.clear();
		if (StringUtils.isNotBlank(username)) {
			this.username.sendKeys(username);
			ui().waitForCondition(driver, ExpectedConditions.textToBePresentInElementValue(this.username, username));
		} else {
			throw new java.lang.Exception("username missing");
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

	public GlobalHomePage clickSignInButton() {
		synchronized (this) {
			try {
				wait(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};
		signInButton.click();
		return new GlobalHomePage(driver);
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
