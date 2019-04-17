/* Copyright 2018 Walgreen Co.*/
package com.wba.test.utils.pages.stockplus;

import static com.oneleo.test.automation.core.UIUtils.ui;

import java.util.Objects;

import org.openqa.selenium.Keys;
import org.junit.Assert;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

public class LimitHistoryPage extends LoadableComponent<LimitHistoryPage> {

	@FindBy(id="page-header-title")
	private WebElement pageHeaderTitle;
	@FindBy(css="#container-white > div.padding-top-4x.padding-left-4x.padding-right-4x.padding-bottom-4x.limit-container-white > div:nth-child(1) > div.text-bold-body")
	private WebElement storeNumber;
	@FindBy(css="#container-white > div.padding-top-4x.padding-left-4x.padding-right-4x.padding-bottom-4x.limit-container-white > div:nth-child(2) > div.text-bold-body")
	private WebElement item;
	@FindBy(css="#container-white > div.padding-top-4x.padding-left-4x.padding-right-4x.padding-bottom-4x.limit-container-white > div:nth-child(3) > div.text-bold-body")
	private WebElement pln;
	@FindBy(id="mat-input-2")
	private WebElement releaseDateFromInput;
	@FindBy(id="mat-input-3")
	private WebElement toInput;
	@FindBy(css="#container-white > div.padding-top-2x.padding-left-4x.padding-right-4x.padding-bottom-2x.limit-container-white > form > button.margin-right-2x.mat-raised-button.mat-secondary")
	private WebElement searchButton;
	@FindBy(id="replenishment-clear-btn")
	private WebElement clearAllButton;
	@FindBy(css="#replenishment-diagnostic-limit-table > mat-header-row > mat-header-cell.mat-header-cell.ng-tns-c30-6.cdk-column-releaseDate.mat-column-releaseDate.mat-sort-header-sorted")
	private WebElement releaseDateHeader;
	@FindBy(css="#replenishment-diagnostic-limit-table > mat-header-row > mat-header-cell.text-align-right.mat-header-cell.cdk-column-orderNumber.mat-column-orderNumber")
	private WebElement orderNumberHeader;
	@FindBy(css="#replenishment-diagnostic-limit-table > mat-header-row > mat-header-cell.mat-header-cell.cdk-column-orderType.mat-column-orderType")
	private WebElement orderTypeHeader;
	@FindBy(css="#replenishment-diagnostic-limit-table > mat-header-row > mat-header-cell.text-align-right.mat-header-cell.cdk-column-suggestedQty.mat-column-suggestedQty")
	private WebElement suggestedQtyHeader;
	@FindBy(css="#replenishment-diagnostic-limit-table > mat-header-row > mat-header-cell.text-align-right.mat-header-cell.cdk-column-orderQty.mat-column-orderQty")
	private WebElement orderQtyHeader;
	@FindBy(css="#replenishment-diagnostic-limit-table > mat-header-row > mat-header-cell.text-align-right.mat-header-cell.cdk-column-ndc.mat-column-ndc")
	private WebElement ndcHeader;
	@FindBy(css="#replenishment-diagnostic-limit-table > mat-header-row > mat-header-cell.text-align-right.mat-header-cell.cdk-column-upc.mat-column-upc")
	private WebElement upcHeader;
	@FindBy(css="#replenishment-diagnostic-limit-table > mat-header-row > mat-header-cell.text-align-right.mat-header-cell.cdk-column-wic.mat-column-wic")
	private WebElement wicHeader;
	@FindBy(id="replenishment-diagnostic-limit-table")
	private WebElement limitTable;
	
	@FindBy(id="mat-error-2")
	private WebElement errorFromDate;
	@FindBy(id="mat-error-3")
	private WebElement errorToDate;
	
	private WebDriver driver;

	public LimitHistoryPage(WebDriver driver) {
		this.driver = driver;
	}
	
	/*
	 * Error messages
	 */
	public String errorFromDateMessage() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(errorFromDate));
		return errorFromDate.getText();
	}
	
	public String errorToDateMessage() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(errorToDate));
		return errorToDate.getText();
	}
	
	/*
	 * Click
	 */
	public void clearAllButtonClick() {
		clearAllButton.click();
	}
	
	/*
	 * Text handle
	 */
	public void releaseDateFromInputInsert(String dateFrom) {
		releaseDateFromInput.sendKeys(dateFrom);
	}
	
	public void toInputInsert(String dateTo) {
		toInput.sendKeys(dateTo);
	}
	
	public String getReleaseDateFromInputInsert() {
		return releaseDateFromInput.getText();
	}
	
	/*
	 * Key
	 */
	public void releaseDateFromInputTab() {
		releaseDateFromInput.sendKeys(Keys.TAB);
	}
	
	public void releaseDateToInputTab() {
		toInput.sendKeys(Keys.TAB);
	}
	
	/*
	 * Enabled
	 */
	public Boolean searchButtonEnabled() {
		return searchButton.isEnabled();
	}
	
	public Boolean clearAllButtonEnabled() {
		return clearAllButton.isEnabled();
	}
	
	/*
	 * Displayed
	 */
	public Boolean limitTableDisplayed() {
		return limitTable.isDisplayed();
	}
	
	public Boolean pageHeaderTitleDisplayed() {
		return pageHeaderTitle.isDisplayed();
	}
	
	public Boolean storeNumberDisplayed() {
		return storeNumber.isDisplayed();
	}
	
	public Boolean itemDisplayed() {
		return item.isDisplayed();
	}
	
	public Boolean plnDisplayed() {
		return pln.isDisplayed();
	}
	
	public Boolean releaseDateFromInputDisplayed() {
		return releaseDateFromInput.isDisplayed();
	}
	
	public Boolean toInputDisplayed() {
		return toInput.isDisplayed();
	}
	
	public Boolean searchButtonDisplayed() {
		return searchButton.isDisplayed();
	}
	
	public Boolean clearAllButtonDisplayed() {
		return clearAllButton.isDisplayed();
	}
	
	public Boolean releaseDateHeaderDisplayed() {
		return releaseDateHeader.isDisplayed();
	}
	
	public Boolean orderNumberHeaderDisplayed() {
		return orderNumberHeader.isDisplayed();
	}
	
	public Boolean orderTypeHeaderDisplayed() {
		return orderTypeHeader.isDisplayed();
	}
	
	public Boolean suggestedQtyHeaderDisplayed() {
		return suggestedQtyHeader.isDisplayed();
	}
	
	public Boolean orderQtyHeaderDisplayed() {
		return orderQtyHeader.isDisplayed();
	}
	
	public Boolean ndcHeaderDisplayed() {
		return ndcHeader.isDisplayed();
	}
	
	public Boolean upcHeaderDisplayed() {
		return upcHeader.isDisplayed();
	}
	
	public Boolean wicHeaderDisplayed() {
		return wicHeader.isDisplayed();
	}
	
	
	
	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(pageHeaderTitle)) {
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
