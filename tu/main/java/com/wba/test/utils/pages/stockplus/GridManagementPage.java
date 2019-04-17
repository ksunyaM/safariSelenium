/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.stockplus;

import static com.oneleo.test.automation.core.UIUtils.ui;

import java.util.List;
import java.util.Objects;

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

public class GridManagementPage extends LoadableComponent<GridManagementPage> {

	@FindBy(id = "rx-grid-management-create-button")
	private WebElement createButton;
	@FindBy(id = "rx-grid-management-search")
	private WebElement gridManagementSearch;
	@FindBy(id = "rx-grid-management-refine-button")
	private WebElement gridManagementRefineButton;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-grid-management > div > div > rx-grid-management-search-view > div > form > div:nth-child(1) > mat-card > mat-card-content > div.margin-top-3x")
	private WebElement gridsNumberFoundLabel;
	@FindBy(id = "rx-grid-management-update-button")
	private WebElement updateButton;
	@FindBy(id = "rx-grid-management-results-table")
	private WebElement gridsTable;
	@FindBy(id="rx-grid-management-delete-button")
	private WebElement deleteButton;
	
	@FindBy(xpath = "//*[@id='rx-corporate-user-app-main-container']/dashboard-view/div/div/div[2]/rx-replenishment-parameters/div/nav/div/a[3]")
	private WebElement tabGridOverriddes;
	private WebDriver driver;

	public GridManagementPage(WebDriver driver) {
		this.driver = driver;
	}

	public CustomGridSettingsPage clickCreateButton() {
		this.createButton.click();
		return new CustomGridSettingsPage(driver);
	}

	public CustomGridUpdatePage clickUpdateButton() {
		this.updateButton.click();
		return new CustomGridUpdatePage(driver);
	}

	public GlobalGridUpdatePage clickUpdateButtonGlobal() {
		this.updateButton.click();
		return new GlobalGridUpdatePage(driver);
	}
	
	public void clickDeleteButton() {
		this.deleteButton.click();
	}

	public GlobalGridSettingsPage clickUpdateGlobalButton() {
		this.updateButton.click();
		return new GlobalGridSettingsPage(driver);
	}

	public Boolean isUpdateButtonDisplayed() {
		return updateButton.isDisplayed();
	}

	public void clickOneGroup(String grid) {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(gridsTable), 10);
		List<WebElement> groupList = gridsTable.findElements(By.className("mat-row"));
		for (WebElement item : groupList) {
			if (item.findElement(By.cssSelector("mat-cell")).getText().equals(grid)) {
				item.findElement(By.cssSelector("mat-cell")).click();
			}
		}
	}
	
	public Boolean clickAndFindOneGroup(String group) {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(gridsTable), 30);
		boolean found = false;
		List<WebElement> groupList = gridsTable.findElements(By.className("mat-row"));
		for (WebElement item : groupList) {
			if (item.findElement(By.cssSelector("mat-cell")).getText().equals(group)) {
				item.findElement(By.cssSelector("mat-cell")).click();
				found = true;
			}
		}
		return found;
	}
	
	public GridOverridesPage clickTabGridOverriddes(){
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(tabGridOverriddes), 10);
		tabGridOverriddes.click();
		return new GridOverridesPage(driver);
	}

	/*
	 * hot key
	 */
	public GlobalGridSettingsPage updateHotKey() {
		Actions keyAction = new Actions(driver);
		keyAction.keyDown(Keys.ALT).sendKeys("u").perform();
		return new GlobalGridSettingsPage(driver);
	}

	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(createButton)) {
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
