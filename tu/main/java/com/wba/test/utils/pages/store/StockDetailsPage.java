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

public class StockDetailsPage extends LoadableComponent<StockDetailsPage>{

	@FindBy(css="#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-search-stock > div > rx-view-item > div > rx-item-information > div > mat-card > div.margin-top > div > div.margin-bottom-2x > div:nth-child(1)")
	private WebElement itemDescriptionDiv;
	@FindBy(css="#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-search-stock > div > rx-view-item > div > rx-item-information > div > mat-card > div.margin-top > div > div.margin-bottom-2x > div:nth-child(2)")
	private WebElement ndcDiv;
	@FindBy(css="#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-search-stock > div > rx-view-item > div > rx-item-information > div > mat-card > div.margin-top > div > div.margin-bottom-2x > div:nth-child(3)")
	private WebElement wicDiv;
	@FindBy(css="#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-search-stock > div > rx-view-item > div > rx-item-information > div > mat-card > div.margin-top > div > div.margin-bottom-2x > div:nth-child(4)")
	private WebElement brandGenericDiv;
	@FindBy(css="#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-search-stock > div > rx-view-item > div > rx-item-information > div > mat-card > div.margin-top > div > div.margin-bottom-2x > div:nth-child(5)")
	private WebElement packageQtyDiv;
	@FindBy(css="#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-search-stock > div > rx-view-item > div > rx-item-information > div > mat-card > div.margin-top > div > div:nth-child(2) > div:nth-child(1)")
	private WebElement manufacturerDiv;
	@FindBy(css="#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-search-stock > div > rx-view-item > div > rx-item-information > div > mat-card > div.margin-top > div > div:nth-child(2) > div:nth-child(2)")
	private WebElement upcDiv;
	@FindBy(css="#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-search-stock > div > rx-view-item > div > rx-item-information > div > mat-card > div.margin-top > div > div:nth-child(2) > div:nth-child(3)")
	private WebElement preferredDiv;
	@FindBy(css="#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-search-stock > div > rx-view-item > div > rx-item-information > div > mat-card > div.margin-top > div > div:nth-child(2) > div:nth-child(4)")
	private WebElement classDiv;
	@FindBy(css="#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-search-stock > div > rx-view-item > div > rx-item-information > div > mat-card > div.margin-top > div > div:nth-child(2) > div:nth-child(5)")
	private WebElement unitOfMeasureDiv;
	@FindBy(css="#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-search-stock > div > rx-view-item > div > rx-item-information > div > mat-card > div.margin-top > div > div:nth-child(2) > div:nth-child(6)")
	private WebElement location;
	
	@FindBy(css="#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-search-stock > div > rx-view-item > div > rx-item-movement-inventory > div > mat-card > div > div.min-height-100 > div.item-inventory-data > div:nth-child(1)")
	private WebElement onShelfQtyDiv;
	@FindBy(css="#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-search-stock > div > rx-view-item > div > rx-item-movement-inventory > div > mat-card > div > div.min-height-100 > div.item-inventory-data > div:nth-child(2)")
	private WebElement inTransitDiv;
	@FindBy(css="#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-search-stock > div > rx-view-item > div > rx-item-movement-inventory > div > mat-card > div > div.min-height-100 > div.item-inventory-data > div:nth-child(3)")
	private WebElement orderedDiv;
	@FindBy(css="#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-search-stock > div > rx-view-item > div > rx-item-movement-inventory > div > mat-card > div > div.min-height-100 > div.item-inventory-data > div:nth-child(4)")
	private WebElement inReadyBinDiv;
	@FindBy(css="#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-search-stock > div > rx-view-item > div > rx-item-movement-inventory > div > mat-card > div > div.min-height-100 > div.item-inventory-data > div:nth-child(5)")
	private WebElement blockedDiv;
	@FindBy(id="view-addl-btn")
	private WebElement viewAdditionalButton;

	@FindBy(css="#totl_lnk_qty_lbl")
	private WebElement totalLinkedQuantityLabel;
	
	private WebDriver driver;

	public StockDetailsPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public Boolean brandGenericDivDisplayed() {
		return brandGenericDiv.isDisplayed();
	}

	public Boolean classDivDisplayed() {
		return classDiv.isDisplayed();
	}

	public Boolean blockedDivDisplayed() {
		return blockedDiv.isDisplayed();
	}

	public Boolean itemDescriptionDivDisplayed() {
		return itemDescriptionDiv.isDisplayed();
	}

	public Boolean ndcDivDisplayed() {
		return ndcDiv.isDisplayed();
	}

	public Boolean wicDivDisplayed() {
		return wicDiv.isDisplayed();
	}

	public Boolean packageQtyDivDisplayed() {
		return packageQtyDiv.isDisplayed();
	}

	public Boolean manufacturerDivDisplayed() {
		return manufacturerDiv.isDisplayed();
	}

	public Boolean upcDivDisplayed() {
		return upcDiv.isDisplayed();
	}

	public Boolean preferredDivDisplayed() {
		return preferredDiv.isDisplayed();
	}

	public Boolean unitOfMeasureDivDisplayed() {
		return unitOfMeasureDiv.isDisplayed();
	}

	public Boolean location() {
		return location.isDisplayed();
	}

	public Boolean onShelfQtyDiv() {
		return onShelfQtyDiv.isDisplayed();
	}

	public Boolean inTransitDivDisplayed() {
		return inTransitDiv.isDisplayed();
	}

	public Boolean orderedDivDisplayed() {
		return orderedDiv.isDisplayed();
	}

	public Boolean inReadyBinDivDisplayed() {
		return inReadyBinDiv.isDisplayed();
	}

	public Boolean viewAdditionalButtonDisplayed() {
		return viewAdditionalButton.isDisplayed();
	}

	public Boolean totalLinkedQuantityLabelDisplayed() {
		return totalLinkedQuantityLabel.isDisplayed();
	}
	
	
	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(itemDescriptionDiv)) {
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
