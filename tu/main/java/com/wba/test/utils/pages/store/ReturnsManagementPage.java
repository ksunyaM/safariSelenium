/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.store;

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

public class ReturnsManagementPage extends LoadableComponent<ReturnsManagementPage>{

	@FindBy(id="claim-search-input")
	private WebElement claimSearchInput;
	@FindBy(id="claim-date-from")
	private WebElement createdOnOrAfter;
	@FindBy(id="claim-date-to")
	private WebElement createdOnOrBefore;
	@FindBy(id="search-button")
	private WebElement searchButton;
	@FindBy(id="clear-button")
	private WebElement clearButton;
	@FindBy(css="#claim-list-claim-number-header")
	private WebElement claimNumberHeader;
	@FindBy(css="#claim-list-claim-type-header")
	private WebElement claimTypeHeader;
	@FindBy(id="claim-list-status-header")
	private WebElement statusHeader;
	@FindBy(id="claim-list-status-data-header")
	private WebElement createdHeader;
	@FindBy(css="#claim-list-supplier-number-header")
	private WebElement supplierNumberHeader;
	@FindBy(css="#claim-list-supplier-name-header")
	private WebElement supplierHeader;
	@FindBy(css="#claim-list-return-reason-header")
	private WebElement returnReasonHeader;
	@FindBy(id="claim-detail-view-button")
	private WebElement viewButton;
	@FindBy(css="#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-returns-management > div > rx-return-claim > div > rx-returns-claim-search > div > mat-card")
	private WebElement header;
	
	@FindBy(id="rm-table")
	private WebElement claimsTable;
	
	private WebDriver driver;

	public ReturnsManagementPage(WebDriver driver) {
		this.driver = driver;
	}
	
	/*
	 * Buttons
	 */
	public ClaimDetailsPage clickViewButton() throws InterruptedException {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.returnReasonHeader), 30);
		viewButton.click();
		return new ClaimDetailsPage(driver);
	}
	
	/*
	 * Displayed
	 */
	public Boolean claimSearchInputDisplayed() {
		return claimSearchInput.isDisplayed();
	}
	
	public Boolean createdOnOrAfterDisplayed() {
		return createdOnOrAfter.isDisplayed();
	}
	
	public Boolean createdOnOrBeforeDisplayed() {
		return createdOnOrBefore.isDisplayed();
	}
	
	public Boolean searchButtonDisplayed() {
		return searchButton.isDisplayed();
	}
	
	public Boolean clearButtonDisplayed() {
		return clearButton.isDisplayed();
	}
	
	public Boolean claimNumberHeaderDisplayed() {
		return claimNumberHeader.isDisplayed();
	}
	
	public Boolean claimTypeHeaderDisplayed() {
		return claimTypeHeader.isDisplayed();
	}
	
	public Boolean statusHeaderDisplayed() {
		return statusHeader.isDisplayed();
	}
	
	public Boolean createdHeaderDisplayed() {
		return createdHeader.isDisplayed();
	}
	
	public Boolean supplierNumberHeaderDisplayed() {
		return supplierNumberHeader.isDisplayed();
	}
	
	public Boolean supplierHeaderDisplayed() {
		return supplierHeader.isDisplayed();
	}
	
	public Boolean returnReasonHeaderDisplayed() {
		return returnReasonHeader.isDisplayed();
	}
	
	public Boolean viewButtonDisplayed() {
		return viewButton.isDisplayed();
	}
	
	public Boolean headerDisplayed() {
		return header.isDisplayed();
	}
	
	/*
	 * Click
	 */
	public void selectClaimByStatus(String status) {
		boolean found = false;
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.claimsTable), 30);
		List<WebElement> claims = claimsTable.findElements(By.cssSelector("mat-row"));
		for(WebElement claim : claims) {
			List<WebElement> rowElement = claim.findElements(By.cssSelector("mat-cell"));
			for(WebElement element : rowElement) {
				if(element.getText().equals(status)) {
					element.click();
					found = true;
					break;
				}
			}
			if(found) {
				break;
			}
		}
	}
	
	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(claimSearchInput)) {
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
