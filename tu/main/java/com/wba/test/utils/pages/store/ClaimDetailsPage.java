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

public class ClaimDetailsPage extends LoadableComponent<ClaimDetailsPage>{

	private WebDriver driver;

	@FindBy(css="#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-returns-management > div > rx-return-claim-detail > div > rx-claim-details-info > div > mat-card > div:nth-child(3) > div:nth-child(1)")
	private WebElement claimTypeDiv;
	@FindBy(css="#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-returns-management > div > rx-return-claim-detail > div > rx-claim-details-info > div > mat-card > div:nth-child(3) > div:nth-child(2)")
	private WebElement scheduleDiv;
	@FindBy(css="#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-returns-management > div > rx-return-claim-detail > div > rx-claim-details-info > div > mat-card > div:nth-child(3) > div:nth-child(3)")
	private WebElement createdDiv;
	@FindBy(css="#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-returns-management > div > rx-return-claim-detail > div > rx-claim-details-info > div > mat-card > div:nth-child(3) > div:nth-child(4)")
	private WebElement supplierNumberDiv;
	@FindBy(css="#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-returns-management > div > rx-return-claim-detail > div > rx-claim-details-info > div > mat-card > div:nth-child(3) > div:nth-child(5)")
	private WebElement lastEditedByDiv;
	@FindBy(css="#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-returns-management > div > rx-return-claim-detail > div > rx-claim-details-info > div > mat-card > div:nth-child(3) > div:nth-child(7)")
	private WebElement totalCostDiv;
	@FindBy(css="#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-returns-management > div > rx-return-claim-detail > div > rx-claim-details-info > div > mat-card > div:nth-child(3) > div:nth-child(8)")
	private WebElement returnReasonDiv;
	
	@FindBy(id="claim-search-input")
	private WebElement itemSearchInput;
	@FindBy(id="search-button")
	private WebElement searchButton;
	@FindBy(id="clear-button")
	private WebElement clearButton;
	
	@FindBy(css="#item-description-header")
	private WebElement itemDescriptionHeader;
	@FindBy(css="#manufacturer-header")
	private WebElement manufacturerHeader;
	@FindBy(css="#ormd-header")
	private WebElement ormdHeader;
	@FindBy(css="#ndc-header")
	private WebElement ndcRowHeader;
	@FindBy(css="#upc-header")
	private WebElement upcHeader;
	@FindBy(css="#wic-header")
	private WebElement wicHeader;
	@FindBy(css="#cost-header")
	private WebElement costHeader;
	@FindBy(css="#claim-qty-header")
	private WebElement claimQtyHeader;
	@FindBy(css="#approved-qty-header")
	private WebElement approvedQtyHeader;
	@FindBy(css="#shipped-qty-header")
	private WebElement shippedQtyHeader;
	@FindBy(id="clam-detail-print-button")
	private WebElement printMaterialsButton;
	
	@FindBy(css="#mat-checkbox-1 > label > div")
	private WebElement selectAllCheckbox;
	@FindBy(id="print-check-box-0")
	private WebElement manifestChecbox;
	@FindBy(id="print-check-box-1")
	private WebElement boxCheckbox;
	@FindBy(id="rx-return-management-create-claim-cancel-button")
	private WebElement cancelPrintButton;
	@FindBy(id="rx-return-management-delete-claim-delete-button")
	private WebElement printButton;
	
	@FindBy(css="#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-returns-management > div > rx-return-claim-detail > div > rx-claim-details-info > div > mat-card > div:nth-child(3) > div:nth-child(8)")
	private WebElement fedexLabel;
	@FindBy(css="#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-returns-management > div > rx-return-claim-detail > div > rx-claim-details-info > div > mat-card > div:nth-child(3) > div:nth-child(9)")
	private WebElement trackingNoDiv;
	
	public ClaimDetailsPage(WebDriver driver) {
		this.driver = driver;
	}
	
	/*
	 * Checked
	 */
	public String boxCheckboxChecked() {
		return boxCheckbox.getAttribute("ng-reflect-checked");
	}
	
	public String manifestChecboxChecked() {
		return manifestChecbox.getAttribute("ng-reflect-checked");
	}
	
	
	
	/*
	 * Displayed
	 */
	public Boolean boxCheckboxDisplayed() {
		return boxCheckbox.isDisplayed();
	}
	
	public Boolean manifestChecboxDisplayed() {
		return manifestChecbox.isDisplayed();
	}
	
	public Boolean selectAllCheckboxDisplayed() {
		return selectAllCheckbox.isDisplayed();
	}
	
	public Boolean cancelPrintButtonDisplayed() {
		return cancelPrintButton.isDisplayed();
	}
	
	public Boolean printButtonDisplayed() {
		return printButton.isDisplayed();
	}
	
	public Boolean fedexLabelDisplayed() {
		return fedexLabel.isDisplayed();
	}
	
	public Boolean trackingNoDivDisplayed() {
		return trackingNoDiv.isDisplayed();
	}
	
	public Boolean printMaterialsButtonDisplayed() {
		return printMaterialsButton.isDisplayed();
	}
	
	public Boolean claimTypeDivDisplayed() {
		return claimTypeDiv.isDisplayed();
	}
	
	public Boolean scheduleDivDisplayed() {
		return scheduleDiv.isDisplayed();
	}
	
	public Boolean createdDivDisplayed() {
		return createdDiv.isDisplayed();
	}
	
	public Boolean supplierNumberDivDisplayed() {
		return supplierNumberDiv.isDisplayed();
	}
	
	public Boolean lastEditedByDivDisplayed() {
		return lastEditedByDiv.isDisplayed();
	}
	
	public Boolean totalCostDivDisplayed() {
		return totalCostDiv.isDisplayed();
	}
	
	public Boolean returnReasonDivDisplayed() {
		return returnReasonDiv.isDisplayed();
	}
	
	public Boolean itemSearchInputDisplayed() {
		return itemSearchInput.isDisplayed();
	}
	
	public Boolean searchButtonDisplayed() {
		return searchButton.isDisplayed();
	}
	
	public Boolean clearButtonDisplayed() {
		return clearButton.isDisplayed();
	}
	
	public Boolean itemDescriptionHeaderDisplayed() {
		return itemDescriptionHeader.isDisplayed();
	}
	
	public Boolean manufacturerHeaderDisplayed() {
		return manufacturerHeader.isDisplayed();
	}
	
	public Boolean ormdHeaderDisplayed() {
		return ormdHeader.isDisplayed();
	}
	
	public Boolean ndcRowHeaderDisplayed() {
		return ndcRowHeader.isDisplayed();
	}
	
	public Boolean upcHeaderDisplayed() {
		return upcHeader.isDisplayed();
	}
	
	public Boolean wicHeaderDisplayed() {
		return wicHeader.isDisplayed();
	}
	
	public Boolean costHeaderDisplayed() {
		return costHeader.isDisplayed();
	}
	
	public Boolean claimQtyHeaderDisplayed() {
		return claimQtyHeader.isDisplayed();
	}
	
	public Boolean approvedQtyHeaderDisplayed() {
		return approvedQtyHeader.isDisplayed();
	}
	
	public Boolean shippedQtyHeaderDisplayed() {
		return shippedQtyHeader.isDisplayed();
	}
	
	/*
	 * Click
	 */
	public void selectAllCheckboxClick() {
		selectAllCheckbox.click();
	}
	
	public void printMaterialsButtonClick() {
		printMaterialsButton.click();
	}
	
	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(claimTypeDiv)) {
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
