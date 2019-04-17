/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.globalhome;

import static com.oneleo.test.automation.core.UIUtils.ui;

import java.util.Objects;

import org.junit.Assert;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

public class GlobalHomePage extends LoadableComponent<GlobalHomePage> {

	@FindBy(xpath = "//*[@id=\"rx-app-nav\"]/rx-uikit-app-bar/div")
	private WebElement topAppBar;
	@FindBy(xpath = "//*[@id=\"rx-app-nav\"]/rx-uikit-app-bar/div/div/rx-uikit-app-switcher/button/span/mat-icon")
	private WebElement appSwitcherButton;
	@FindBy(xpath = "//*[@id=\"empty-component-menu-message\"]")
	private WebElement centralTextMessage;
	@FindBy(xpath = "//*[@id=\"rx-app-nav\"]/rx-uikit-app-bar/div/div/rx-uikit-app-user-menu/button")
	private WebElement profileButton;

	private WebDriver driver;

	public GlobalHomePage(WebDriver driver) {
		this.driver = driver;
	}

	public boolean isTopAppBarDisplayed() {
		return topAppBar.isDisplayed();
	}

	public boolean isAppSwitcherDisplayed() {
		return appSwitcherButton.isDisplayed();
	}

	public boolean isCentralTextDisplayed() {
		return centralTextMessage.isDisplayed();
	}

	public String getCentralTextValue() {
		return centralTextMessage.getText();
	}

	public AppSwitcherPage clickOnAppSwitcherButton() {
		synchronized (this) {
			try {
				wait(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		appSwitcherButton.click();
		return new AppSwitcherPage(driver);
	}

	public ContactManagerPopUpPage goToContactManagerPopup() {
		return new ContactManagerPopUpPage(driver);
	}

	public LinkAccountsPopUpPage goToLinkAccountsPopup() {
		return new LinkAccountsPopUpPage(driver);
	}

	public boolean waitForPageToBeLoaded() {
		synchronized (this) {
			try {
				wait(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			this.isTopAppBarDisplayed();
			return true;
		}
		catch (NoSuchElementException e){
			throw new RuntimeException(String.format("The element: %s does not exists", topAppBar.getText()));
		}
	}

	public ProfileMenuPage clickProfileButton() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(profileButton));
		profileButton.click();
		return new ProfileMenuPage(driver);
	}

	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(topAppBar)) {
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
