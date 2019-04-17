/* Copyright 2018 Walgreen Co.*/
package com.wba.test.pageobjects.pages.desktop.st6;

import static com.oneleo.test.automation.core.UIUtils.ui;

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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

public class StockSearchPage extends LoadableComponent<StockSearchPage> {

	private WebDriver driver;
	
	@FindBy(id="mat-input-1")
	private WebElement itemInput;
	@FindBy(css="body > app-root > app-nav > app-stock-search > mat-card > app-stock-search-form > form > button")
	private WebElement searchButton;
	@FindBy(css="#searchStockAutocomplete > rx-uikit-base-autocomplete > mat-form-field > div > div.mat-form-field-flex > div > mat-icon")
	private WebElement cancelButton;
	@FindBy(id="mat-autocomplete-1")
	private WebElement itemMatAutocomplete;
	@FindBy(css="body > app-root > app-nav > app-stock-search > mat-card > div > b > span")
	private WebElement numberItemsFoundLabel;
	@FindBy(css="body > app-root > app-nav > app-stock-search > mat-card > div > b")
	private WebElement itemsFoundLabel;
	@FindBy(css="body > app-root > app-nav > app-stock-search > app-stock-search-results > div > div > label")
	private WebElement noItemsMessage;
	@FindBy(css="body > app-root > app-nav > app-stock-search > app-stock-search-results > div > table")
	private WebElement itemTable;
	
	/*
	 * Headers
	 */
	@FindBy(css="body > app-root > app-nav > app-stock-search > app-stock-search-results > div > table > thead > tr > th.mat-header-cell.ng-tns-c28-10.cdk-column-itemDescription.mat-column-itemDescription.ng-tns-c26-9.mat-table-sticky.ng-star-inserted")
	private WebElement itemDescriptionHeader;
	@FindBy(css="body > app-root > app-nav > app-stock-search > app-stock-search-results > div > table > thead > tr > th.mat-header-cell.cdk-column-packageQty.mat-column-packageQty.ng-tns-c26-9.mat-table-sticky.ng-star-inserted")
	private WebElement packageQtyHeader;
	@FindBy(css="body > app-root > app-nav > app-stock-search > app-stock-search-results > div > table > thead > tr > th.mat-header-cell.cdk-column-manufacturer.mat-column-manufacturer.ng-tns-c26-9.mat-table-sticky.ng-star-inserted")
	private WebElement manufacturerHeader;
	@FindBy(css="body > app-root > app-nav > app-stock-search > app-stock-search-results > div > table > thead > tr > th.mat-header-cell.ng-tns-c28-11.cdk-column-preferred.mat-column-preferred.ng-tns-c26-9.mat-table-sticky.ng-star-inserted")
	private WebElement preferredHeader;
	@FindBy(css="body > app-root > app-nav > app-stock-search > app-stock-search-results > div > table > thead > tr > th.mat-header-cell.cdk-column-NDC.mat-column-NDC.ng-tns-c26-9.mat-table-sticky.ng-star-inserted")
	private WebElement ndcHeader;
	@FindBy(css="body > app-root > app-nav > app-stock-search > app-stock-search-results > div > table > thead > tr > th.mat-header-cell.cdk-column-UPC.mat-column-UPC.ng-tns-c26-9.mat-table-sticky.ng-star-inserted")
	private WebElement upcHeader;
	@FindBy(css="body > app-root > app-nav > app-stock-search > app-stock-search-results > div > table > thead > tr > th.mat-header-cell.cdk-column-WIC.mat-column-WIC.ng-tns-c26-9.mat-table-sticky.ng-star-inserted")
	private WebElement wicHeader;
	@FindBy(css="body > app-root > app-nav > app-stock-search > app-stock-search-results > div > table > thead > tr > th.mat-header-cell.cdk-column-location.mat-column-location.ng-tns-c26-9.mat-table-sticky.ng-star-inserted")
	private WebElement locationHeader;
	@FindBy(css="body > app-root > app-nav > app-stock-search > app-stock-search-results > div > table > thead > tr > th.mat-header-cell.ng-tns-c28-12.cdk-column-qtyOnShelf.mat-column-qtyOnShelf.ng-tns-c26-9.mat-table-sticky.ng-star-inserted")
	private WebElement qtyOnShelfHeader;
	
	/*
	 * Cards
	 */
	@FindBy(css="body > app-root > app-nav > app-stock-search > app-stock-search-results > div > table > tbody > tr.detail-row.mat-row.ng-tns-c26-9.ng-star-inserted > td > app-stock-search-results-details > app-results-details-item-information > mat-card > p")
	private WebElement itemInfoTitle;
	@FindBy(css="body > app-root > app-nav > app-stock-search > app-stock-search-results > div > table > tbody > tr.detail-row.mat-row.ng-tns-c26-9.ng-star-inserted > td > app-stock-search-results-details > app-results-details-inventory-availability > mat-card > p")
	private WebElement inventoryAvailabilityTitle;
	@FindBy(css="body > app-root > app-nav > app-stock-search > app-stock-search-results > div > table > tbody > tr.detail-row.mat-row.ng-tns-c26-9.ng-star-inserted > td > app-stock-search-results-details > app-results-details-inventory-availability > mat-card > div.box-container > div.box.selected")
	private WebElement qtyOnShelfDiv;
	@FindBy(css="body > app-root > app-nav > app-stock-search > app-stock-search-results > div > table > tbody > tr.detail-row.mat-row.ng-tns-c26-9.ng-star-inserted > td > app-stock-search-results-details > app-results-details-inventory-availability > mat-card > div.box-container > div:nth-child(2)")
	private WebElement rxReadyBinDiv;
	@FindBy(css="body > app-root > app-nav > app-stock-search > app-stock-search-results > div > table > tbody > tr.detail-row.mat-row.ng-tns-c26-9.ng-star-inserted > td > app-stock-search-results-details > app-results-details-inventory-availability > mat-card > div.box-container > div:nth-child(3)")
	private WebElement blockedDiv;
	@FindBy(css="body > app-root > app-nav > app-stock-search > app-stock-search-results > div > table > tbody > tr.detail-row.mat-row.ng-tns-c26-9.ng-star-inserted > td > app-stock-search-results-details > app-results-details-inventory-availability > mat-card > div.box-container > div:nth-child(4)")
	private WebElement inTransitDiv;
	@FindBy(css="body > app-root > app-nav > app-stock-search > app-stock-search-results > div > table > tbody > tr.detail-row.mat-row.ng-tns-c26-9.ng-star-inserted > td > app-stock-search-results-details > app-results-details-inventory-availability > mat-card > div.box-container > div:nth-child(5)")
	private WebElement orderedDiv;
	@FindBy(css="body > app-root > app-nav > app-stock-search > app-stock-search-results > div > table > tbody > tr.detail-row.mat-row.ng-tns-c26-9.ng-star-inserted > td > app-stock-search-results-details > app-results-details-item-information > mat-card > div:nth-child(2) > div:nth-child(1)")
	private WebElement expectedDeliveryDiv;
	@FindBy(css="body > app-root > app-nav > app-stock-search > app-stock-search-results > div > table > tbody > tr.detail-row.mat-row.ng-tns-c26-9.ng-star-inserted > td > app-stock-search-results-details > app-results-details-item-information > mat-card > div:nth-child(2) > div:nth-child(2)")
	private WebElement genericDiv;
	@FindBy(css="body > app-root > app-nav > app-stock-search > app-stock-search-results > div > table > tbody > tr.detail-row.mat-row.ng-tns-c26-9.ng-star-inserted > td > app-stock-search-results-details > app-results-details-item-information > mat-card > div:nth-child(2) > div:nth-child(3)")
	private WebElement dispenseUnitDiv;
	@FindBy(css="body > app-root > app-nav > app-stock-search > app-stock-search-results > div > table > tbody > tr.detail-row.mat-row.ng-tns-c26-9.ng-star-inserted > td > app-stock-search-results-details > app-results-details-item-information > mat-card > div:nth-child(3) > div:nth-child(1)")
	private WebElement supplierDiv;
	@FindBy(css="body > app-root > app-nav > app-stock-search > app-stock-search-results > div > table > tbody > tr.detail-row.mat-row.ng-tns-c26-9.ng-star-inserted > td > app-stock-search-results-details > app-results-details-item-information > mat-card > div:nth-child(3) > div:nth-child(2)")
	private WebElement classDiv;
	@FindBy(css="body > app-root > app-nav > app-stock-search > app-stock-search-results > div > table > tbody > tr.detail-row.mat-row.ng-tns-c26-9.ng-star-inserted > td > app-stock-search-results-details > app-results-details-item-information > mat-card > div:nth-child(2) > div:nth-child(2)")
	private WebElement brandOrGenericDiv;
	@FindBy(css="body > app-root > app-nav > app-stock-search > app-stock-search-results > div > table > tbody > tr.detail-row.mat-row.ng-tns-c26-9.ng-star-inserted > td > app-stock-search-results-details > app-results-details-inventory-availability > mat-card > div:nth-child(3) > button:nth-child(1)")
	private WebElement checkOtherStoresButton;
	@FindBy(css="body > app-root > app-nav > app-stock-search > app-stock-search-results > div > table > tbody > tr.detail-row.mat-row.ng-tns-c26-9.ng-star-inserted > td > app-stock-search-results-details > app-results-details-inventory-availability > mat-card > div:nth-child(3) > button:nth-child(2)")
	private WebElement addToOrderButton;
	
	public StockSearchPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public Boolean checkItemMatAutocomplete(String itemExpected) {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(itemMatAutocomplete));
		boolean found = false;
		List<WebElement> options = itemMatAutocomplete.findElements(By.cssSelector("mat-option"));
		for(WebElement option : options) {
			WebElement span = option.findElement(By.cssSelector("span"));
			if(span.findElement(By.className("text-body")).getText().equals(itemExpected)) {
				found = true;
			}
		}
		return found;
	}
	
	public String checkNDCUPCMatAutocomplete(String itemExpected) {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(itemMatAutocomplete));
		boolean found = false;
		String upcNdcToCheck = "";
		List<WebElement> options = itemMatAutocomplete.findElements(By.cssSelector("mat-option"));
		for(WebElement option : options) {
			WebElement span = option.findElement(By.cssSelector("span"));
			if(span.findElement(By.className("text-body")).getText().equals(itemExpected)) {
				upcNdcToCheck = span.findElement(By.className("text-caption")).getText();
			}
		}
		return upcNdcToCheck;
	}
	
	public List<WebElement> checkItemList() {
		return itemTable.findElements(By.cssSelector("tbody > tr"));
	}
	
	public List<String> checkRowsCollapsed() {
		List<String> expandedStatus = new ArrayList<>();
		List<WebElement> itemList = checkItemList();
		for(WebElement item : itemList) {
			List<WebElement> cellElements = item.findElements(By.cssSelector("td"));
			for(WebElement cell : cellElements){
				if(cell.getAttribute("id").contains("expanded-detail")) {
					expandedStatus.add(cell.findElement(By.cssSelector("mat-icon")).getText());
					break;
				}
			}
		}
		return expandedStatus;
	}
	
	public void expandRows() {
		List<WebElement> itemList = checkItemList();
		for(WebElement item : itemList) {
			List<WebElement> cellElements = item.findElements(By.cssSelector("td"));
			for(WebElement cell : cellElements){
				if(cell.getAttribute("id").contains("expanded-detail")) {
					cell.findElement(By.cssSelector("mat-icon")).click();
					break;
				}
			}
			break;
		}
	}
	
	/*
	 * Displayed
	 */
	public Boolean brandOrGenericDivDisplayed() {
		return brandOrGenericDiv.isDisplayed();
	}
	
	public Boolean itemInfoTitleDisplayed() {
		return itemInfoTitle.isDisplayed();
	}
	
	public Boolean checkOtherStoresButtonDisplayed() {
		return checkOtherStoresButton.isDisplayed();
	}
	
	public Boolean addToOrderButtonDisplayed() {
		return addToOrderButton.isDisplayed();
	}
	
	public Boolean classDivDisplayed() {
		return classDiv.isDisplayed();
	}
	
	public Boolean supplierDivDisplayed() {
		return supplierDiv.isDisplayed();
	}
	
	public Boolean dispenseUnitDivDisplayed() {
		return dispenseUnitDiv.isDisplayed();
	}
	
	public Boolean genericDivDisplayed() {
		return genericDiv.isDisplayed();
	}
	
	public Boolean expectedDeliveryDivDisplayed() {
		return expectedDeliveryDiv.isDisplayed();
	}
	
	public Boolean orderedDivDisplayed() {
		return orderedDiv.isDisplayed();
	}
	
	public Boolean inTransitDivDisplayed() {
		return inTransitDiv.isDisplayed();
	}
	
	public Boolean blockedDivDisplayed() {
		return blockedDiv.isDisplayed();
	}
	
	public Boolean rxReadyBinDivDisplayed() {
		return rxReadyBinDiv.isDisplayed();
	}
	
	public Boolean qtyOnShelfDivDisplayed() {
		return qtyOnShelfDiv.isDisplayed();
	}
	
	public Boolean inventoryAvailabilityTitleDisplayed() {
		return inventoryAvailabilityTitle.isDisplayed();
	}
	
	public Boolean qtyOnShelfHeaderDisplayed() {
		return qtyOnShelfHeader.isDisplayed();
	}
	
	public Boolean locationHeaderDisplayed() {
		return locationHeader.isDisplayed();
	}
	
	public Boolean wicHeaderDisplayed() {
		return wicHeader.isDisplayed();
	}
	
	public Boolean upcHeaderDisplayed() {
		return upcHeader.isDisplayed();
	}
	
	public Boolean ndcHeaderDisplayed() {
		return ndcHeader.isDisplayed();
	}
	
	public Boolean preferredHeaderDisplayed() {
		return preferredHeader.isDisplayed();
	}
	
	public Boolean manufacturerHeaderDisplayed() {
		return manufacturerHeader.isDisplayed();
	}
	
	public Boolean packageQtyHeaderDisplayed() {
		return packageQtyHeader.isDisplayed();
	}
	
	public Boolean itemDescriptionHeaderDisplayed() {
		return itemDescriptionHeader.isDisplayed();
	}
	
	public Boolean numberItemsFoundLabelDisplayed() {
		return numberItemsFoundLabel.isDisplayed();
	}
	
	public Boolean cancelButtonDisplayed() {
		return cancelButton.isDisplayed();
	}
	
	public Boolean itemMatAutocompleteDisplayed() {
		return itemMatAutocomplete.isDisplayed();
	}
	
	public Boolean searchButtonDisplayed() {
		return searchButton.isDisplayed();
	}
	
	/*
	 * Text handle
	 */
	public String getNoItemsMessage() {
		return noItemsMessage.getText();
	}
	
	public String getNumberItemFound() {
		return numberItemsFoundLabel.getText();
	}
	
	public String getItemFound() {
		return itemsFoundLabel.getText();
	}
	
	public String getItemInputPlaceholder() {
		return itemInput.getAttribute("placeholder");
	}
	
	public String getItemInputText() {
		return itemInput.getText();
	}
	
	public void insertItemInput(String input) {
		itemInput.sendKeys(input);
	}
	
	public Boolean itemInputDisplayed() {
		return itemInput.isDisplayed();
	}
	
	public void cancelButtonClick() {
		cancelButton.click();
	}
	
	/*
	 * Enabled
	 */
	public Boolean searchButtonEnabled() {
		return searchButton.isEnabled();
	}
	
	/*
	 * Click
	 */
	public void searchButtonClick() {
		searchButton.click();
	}
	
	public void checkOtherStoresButtonClick() {
		checkOtherStoresButton.click();
	}
	
	public void addToOrderButtonClick() {
		addToOrderButton.click();
	}
	
	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(itemInput)) {
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
