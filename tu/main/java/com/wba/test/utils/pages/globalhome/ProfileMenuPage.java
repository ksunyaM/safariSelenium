/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.globalhome;

import org.junit.Assert;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Objects;

import static com.oneleo.test.automation.core.UIUtils.ui;

public class ProfileMenuPage extends LoadableComponent<ProfileMenuPage> {

	@FindBy(css = "#cdk-overlay-3 > div > div > div.user-menu-header > div > div > div.text-bold-body")
	private WebElement userFullName;
	@FindBy(css = "#cdk-overlay-3 > div > div > div.user-menu-header > div > div > div.text-body.email.ng-star-inserted")
	private WebElement userEmail;
	@FindBy(css = "#cdk-overlay-3 > div > div > div.user-menu-header > div > span")
	private WebElement initials;
	@FindBy(css = "#cdk-overlay-3 > div > div > div.item-menu.log-out.ng-star-inserted > span")
	private WebElement signOff;

	private WebDriver driver;

	public ProfileMenuPage(WebDriver driver) {
		this.driver = driver;
	}

	public boolean isFullNameDisplayed() {
		return userFullName.isDisplayed();
	}

	public boolean isEmailDisplayed() {
		return userEmail.isDisplayed();
	}

	public boolean isInitialsDisplayed() {
		return initials.isDisplayed();
	}

	public boolean isSignOffOptionDisplayed() {
		return signOff.isDisplayed();
	}

	public String getFullNameText() {
		return userFullName.getText();
	}

	public String getUserEmailText() {
		return userEmail.getText();
	}

	public String getInitialsText() {
		return initials.getText();
	}

	public void waitForPageToBeLoaded() {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(signOff));
	}

	public void clickSignOff() {
		synchronized (this) {
			try {
				wait(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};
		signOff.click();
	}

	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(signOff)) {
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
