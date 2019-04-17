/*******************************************************************************
 * Copyright 2019 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.globalhome;

import java.util.HashMap;
import java.util.Objects;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

import static com.oneleo.test.automation.core.UIUtils.ui;

public class AppSwitcherPage extends LoadableComponent<AppSwitcherPage> {

	private static final HashMap<String, Integer> appsMap;
	static
	{
		appsMap = new HashMap<String, Integer>();
		appsMap.put("Home", 1);
		appsMap.put("Clinical", 2);
		appsMap.put("Fill", 3);
		appsMap.put("Inventory", 4);
		appsMap.put("Orders", 5);
		appsMap.put("Records", 6);
	}

	private static String appXpath = "//*[@id=\"cdk-overlay-1\"]/div/div/section[2]/div[%d]/span";

	@FindBy(xpath = "//*[@id=\"cdk-overlay-1\"]/div/div/section[1]/p")
	private WebElement appSwitcherHeader;
	@FindBy(xpath = "//*[@id=\"cdk-overlay-1\"]/div/div/section[1]/mat-icon")
	private WebElement appSwitcherButton;
	private WebDriver driver;

	public AppSwitcherPage(WebDriver driver) {
		this.driver = driver;
	}

	public boolean isAppDisplayed(String app) {
		synchronized (this) {
			try {
				wait(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};
		if (AppSwitcherPage.appsMap.get(app) != null) {
			Integer appId = AppSwitcherPage.appsMap.get(app);
			return driver.findElement(By.xpath(String.format(appXpath, appId))).isDisplayed();
		} else {
			throw new NoSuchElementException(String.format("The application called: %s has not been recognised.", app));
		}
	}

	public boolean isAppSwitcherButtonDisplayed() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(appSwitcherButton));
		return appSwitcherButton.isDisplayed();
	}

	public boolean isAppSwitcherHeaderDisplayed() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(appSwitcherHeader));
		return appSwitcherHeader.isDisplayed();
	}

	public void clickOnSelectedApp(String app) {
		synchronized (this) {
			try {
				wait(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};
		if (AppSwitcherPage.appsMap.get(app) != null) {
			Integer appId = AppSwitcherPage.appsMap.get(app);
			driver.findElement(By.xpath(String.format(appXpath, appId))).click();
		} else {
			throw new NoSuchElementException(String.format("The application called: %s has not been recognised.", app));
		}
	}

	public GlobalHomePage clickOnAppSwitcher() {
		appSwitcherButton.click();
		return new GlobalHomePage(driver);
	}

	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(appSwitcherHeader)) {
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
