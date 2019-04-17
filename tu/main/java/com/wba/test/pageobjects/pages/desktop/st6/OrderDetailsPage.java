/* Copyright 2018 Walgreen Co.*/
package com.wba.test.pageobjects.pages.desktop.st6;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

public class OrderDetailsPage extends LoadableComponent<OrderDetailsPage>{

	@FindBy(css="#mat-dialog-0 > app-rx-purchase-order-details > mat-toolbar > span:nth-child(1)")
	private WebElement title;
	@FindBy(css="#rx-order-details-header > div.margin-top-6x > mat-list")
	private WebElement cardMatList;
	@FindBy(id="mat-input-4")
	private WebElement lineItemInput;
	@FindBy(id="mat-select-1")
	private WebElement filterDropdown;
	@FindBy(id="search-button")
	private WebElement searchButton;
	@FindBy(id="clear-button")
	private WebElement clearButton;
	@FindBy(css="#mat-dialog-1 > app-rx-purchase-order-details > app-rx-order-details-table > table")
	private WebElement itemsTable;
	@FindBy(css="#cdk-overlay-2 > div")
	private WebElement statusOverlay;
	
	private WebDriver driver;

	public OrderDetailsPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public String statusOverlayElements() {
		String selected = "";
		List<WebElement> statuses = statusOverlay.findElements(By.cssSelector("div > mat-option"));
		for(WebElement status : statuses) {
			if (status.getAttribute("ng-reflect-value").equals("*"))
			selected = status.getAttribute("aria-selected");
		}
		return selected;
	}
	

	
	/*
	 * Text
	 */	
	public String getPageTitle() {
		return title.getText();
	}
	
	public String lineItemInputPlaceholder() {
		return lineItemInput.getAttribute("ng-reflect-placeholder");
	}
	
	public String getLineItemInputText() {
		return lineItemInput.getAttribute("value");
	}
	
	public void insertLineItemInputText(String text) {
		lineItemInput.sendKeys(text);
	}
	
	/*
	 * Displayed
	 */
	public Boolean itemsTableDisplayed() {
		return itemsTable.isDisplayed();
	}
	
	public Boolean lineItemInputDisplayed() {
		return lineItemInput.isDisplayed();
	}
	
	public List<WebElement> getCardListItem() {
		return cardMatList.findElements(By.cssSelector("mat-list-item"));
	}

	public Boolean filterDropdownDisplayed() {
		return filterDropdown.isDisplayed();
	}
	
	public Boolean searchButtonDisplayed() {
		return searchButton.isDisplayed();
	}
	
	public Boolean clearButtonDisplayed() {
		return clearButton.isDisplayed();
	}
	
	/*
	 * Click
	 */
	public void clearButtonClick() {
		clearButton.click();
	}
	
	public void filterDropdownClick() {
		filterDropdown.click();
	}
	
	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(cardMatList)) {
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
