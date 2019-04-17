/* Copyright 2018 Walgreen Co.*/
package com.wba.test.pageobjects.pages.desktop.st6;

import static com.oneleo.test.automation.core.UIUtils.ui;

import java.util.List;
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

public class PurchaseOrderPage extends LoadableComponent<PurchaseOrderPage>{

	@FindBy(id="po-table")
	private WebElement poTable;
	@FindBy(css="body > app-root > app-nav > app-rx-purchase-order-view > div > mat-toolbar > span:nth-child(2)")
	private WebElement title;
	@FindBy(id="mat-input-3")
	private WebElement orderInput;
	@FindBy(id="mat-select-0")
	private WebElement statusSelect;
	@FindBy(id="mat-input-1")
	private WebElement dateInputFrom;
	@FindBy(id="mat-input-2")
	private WebElement dateInputTo;
	@FindBy(css="body > app-root > app-nav > app-rx-purchase-order-view > div > div:nth-child(2) > mat-card > form > div:nth-child(4) > button.mat-raised-button.mat-primary")
	private WebElement searchButton;
	@FindBy(css="body > app-root > app-nav > app-rx-purchase-order-view > div > div:nth-child(2) > mat-card > form > div:nth-child(4) > button.mat-raised-button.mat-tertiary")
	private WebElement clearAllButton;
	
	private WebDriver driver;

	public PurchaseOrderPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public String getTitle() {
		return title.getText();
	}
	
	
	/*
	 * Click
	 */
	public OrderDetailsPage orderNumberButtonClick() {
		List<WebElement> buttons = poTable.findElements(By.cssSelector("tbody > tr > td > button"));
		if(buttons.size() != 0) {
			for (WebElement button : buttons) {
				button.click();
				break;
			}
		}
		return new OrderDetailsPage(driver);
	}
	
	public void searchButtonClick() {
		searchButton.click();
	}
	
	public void searchAllButtonClick() {
		clearAllButton.click();
	}
	
	/*
	 * Displayed
	 */
	public Boolean orderInputDisplayed() {
		return orderInput.isDisplayed();
	}
	
	public Boolean statusSelectDisplayed() {
		return statusSelect.isDisplayed();
	}
	
	public Boolean dateInputFromDisplayed() {
		return dateInputFrom.isDisplayed();
	}
	
	public Boolean dateInputToDisplayed() {
		return dateInputTo.isDisplayed();
	}
	
	public Boolean searchButtonDisplayed() {
		return searchButton.isDisplayed();
	}
	
	public Boolean clearAllButtonDisplayed() {
		return clearAllButton.isDisplayed();
	}
	
	public Boolean poTableDisplayed() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(poTable));
		return poTable.isDisplayed();
	}
	
	public Boolean orderNumberListDisplayed() {
		boolean found = false;
		List<WebElement> rows = poTable.findElements(By.cssSelector("tbody > tr > td"));
		if(rows.size() != 0) {
			for (WebElement row : rows) {
				found= row.isDisplayed();
			}
		}
		return found;
	}
	
	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(title)) {
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
