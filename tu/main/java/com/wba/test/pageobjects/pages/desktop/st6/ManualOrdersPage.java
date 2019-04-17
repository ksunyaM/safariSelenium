/* Copyright 2018 Walgreen Co.*/
package com.wba.test.pageobjects.pages.desktop.st6;

import java.util.Objects;

import org.junit.Assert;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

public class ManualOrdersPage extends LoadableComponent<ManualOrdersPage>{

	@FindBy(css="body > app-root > app-nav > app-rx-purchase-order-cart > div > mat-toolbar.page-header.mat-toolbar.mat-toolbar-single-row > span:nth-child(2)")
	private WebElement title;
	@FindBy(id="add-additional-items")
	private WebElement addItemsButton;
	
	private WebDriver driver;

	public ManualOrdersPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public StockSearchPage clickAddItemsButton() {
		addItemsButton.click();
		return new StockSearchPage(driver);
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
