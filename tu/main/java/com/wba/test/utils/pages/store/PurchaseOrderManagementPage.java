/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.store;

import java.util.Objects;

import org.junit.Assert;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

public class PurchaseOrderManagementPage extends LoadableComponent<PurchaseOrderManagementPage>{

	@FindBy(id="mat-input-0")
	private WebElement orderFilterInput;
	@FindBy(id="mat-input-1")
	private WebElement deliveryDateFromInput;
	@FindBy(id="mat-input-2")
	private WebElement toDateInput;
	@FindBy(id="search-button")
	private WebElement searchButton;
	@FindBy(css="#form-wrapper > form > button.mat-raised-button.mat-neutral")
	private WebElement clearButton;
	@FindBy(id="pov-mat-card")
	private WebElement header;
	@FindBy(css="#po-table > mat-header-row > mat-header-cell.mat-header-cell.cdk-column-orderNumber.mat-column-orderNumber")
	private WebElement orderNumberHeader;
	@FindBy(css="#po-table > mat-header-row > mat-header-cell.mat-header-cell.ng-tns-c18-5.cdk-column-deliveryDate.mat-column-deliveryDate > div")
	private WebElement dateHeader;
	@FindBy(css="#po-table > mat-header-row > mat-header-cell.mat-header-cell.cdk-column-supplier.mat-column-supplier")
	private WebElement supplierHeader;
	@FindBy(css="#po-table > mat-header-row > mat-header-cell.mat-header-cell.cdk-column-orderOrigin.mat-column-orderOrigin")
	private WebElement orderOriginHeader;
	@FindBy(css="#po-table > mat-header-row > mat-header-cell.mat-header-cell.ng-tns-c18-6.cdk-column-orderStatus.mat-column-orderStatus")
	private WebElement orderStatusHeader;
	@FindBy(css="#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-purchase-order-view > div > mat-toolbar.bottom-tray.mat-toolbar > div > mat-toolbar-row > button")
	private WebElement viewButton;
	
	
	private WebDriver driver;

	public PurchaseOrderManagementPage(WebDriver driver) {
		this.driver = driver;
	}
	
	/*
	 * Displayed
	 */
	public Boolean orderFilterInputDisplayed() {
		return orderFilterInput.isDisplayed();
	}
	
	public Boolean deliveryDateFromInputDisplayed() {
		return deliveryDateFromInput.isDisplayed();
	}
	
	public Boolean toDateInputDisplayed() {
		return toDateInput.isDisplayed();
	}
	
	public Boolean searchButtonDisplayed() {
		return searchButton.isDisplayed();
	}
	
	public Boolean clearButtonDisplayed() {
		return clearButton.isDisplayed();
	}
	
	public Boolean headerDisplayed() {
		return header.isDisplayed();
	}
	
	public Boolean orderNumberHeaderDisplayed() {
		return orderNumberHeader.isDisplayed();
	}
	
	public Boolean dateHeaderDisplayed() {
		return dateHeader.isDisplayed();
	}
	
	public Boolean supplierHeaderDisplayed() {
		return supplierHeader.isDisplayed();
	}
	
	public Boolean orderOriginHeaderDisplayed() {
		return orderOriginHeader.isDisplayed();
	}
	
	public Boolean orderStatusHeaderDisplayed() {
		return orderStatusHeader.isDisplayed();
	}
	
	public Boolean viewButtonDisplayed() {
		return viewButton.isDisplayed();
	}
	
	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(orderFilterInput)) {
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
