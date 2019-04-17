/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/

package com.wba.test.utils.pages.stockplus;

import java.util.Objects;

import org.junit.Assert;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

public class StockSearchPage extends LoadableComponent<StockSearchPage>{

	private WebDriver driver;
	
	@FindBy(id="str_name_lbl")
	private WebElement storeInput;
	
	@FindBy(id="srch_lbl")
	private WebElement searchInput;
	
	@FindBy(id="srch_btn")
	private WebElement searchButton;
	
	@FindBy(id="item-view-button")
	private WebElement viewItemButton;

	@FindBy(css="#itm_desc_hdr > div > button")
	private WebElement itemDescriptionHeader;
	
	@FindBy(css="#mfr_hdr")
	private WebElement manufacturerHeader;
	
	@FindBy(css="#pref_hdr > div > button")
	private WebElement preferredHeader;
	
	@FindBy(css="#itemSearchTable > mat-header-row > mat-header-cell.table-align-right.mat-header-cell.cdk-column-NDC_UPC.mat-column-NDC_UPC > div")
	private WebElement ndcUpcHeader;
	
	@FindBy(css="#wic_hdr")
	private WebElement wicHeader;
	
	@FindBy(css="#qty_on_shelf_hdr")
	private WebElement qtyOnShelf;
	
	@FindBy(css="#inventory-full-container > div > form")
	private WebElement header;
	

	public StockSearchPage(WebDriver driver) {
		this.driver = driver;
	}
	
	/*
	 * Input
	 */
	public void searchInput(String item) {
		searchInput.sendKeys(item);
	}
	
	public void setStore(String item) {
		storeInput.sendKeys(item);
	}
	
	public String getStore() {
		return storeInput.getText();  
	}
	
	/*
	 * Click
	 */
	public void searchButtonClick() {
		searchButton.click();
	}
	
	public StockDetailsPage viewButtonClick() throws InterruptedException {
		synchronized (this) {
			wait(25000);
		}
		viewItemButton.click();
		return new StockDetailsPage(driver);
	}
	
	/*
	 * Displayed
	 */
	public Boolean storeInputDisplayed() {
		return storeInput.isDisplayed();
	}
	
	public Boolean searchInputDisplayed() {
		return searchInput.isDisplayed();
	}
	
	public Boolean searchButtonDisplayed() {
		return searchButton.isDisplayed();
	}
	
	public Boolean headerDisplayed() {
		return header.isDisplayed();
	}
	
	public Boolean viewItemButtonDisplayed() throws InterruptedException {
		synchronized (this) {
			wait(25000);
		}
		return viewItemButton.isDisplayed();
	}

	public Boolean itemDescriptionHeaderDisplayed() {
		return itemDescriptionHeader.isDisplayed();
	}

	public Boolean manufacturerHeader() {
		return manufacturerHeader.isDisplayed();
	}

	public Boolean preferredHeader() {
		return preferredHeader.isDisplayed();
	}

	public Boolean ndcUpcHeaderDisplayed() {
		return ndcUpcHeader.isDisplayed();
	}

	public Boolean wicHeaderDisplayed() {
		return wicHeader.isDisplayed();
	}

	public Boolean qtyOnShelfDisplayed() {
		return qtyOnShelf.isDisplayed();
	}
	
	public Boolean searchInputEnabled() {
		return searchInput.isEnabled();
	}
	public Boolean searchButtonEnabled() {
		return searchButton.isEnabled();
	}
	
	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(searchInput)) {
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
