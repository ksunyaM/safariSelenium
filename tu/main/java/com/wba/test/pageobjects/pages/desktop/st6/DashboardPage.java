/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
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

public class DashboardPage extends LoadableComponent<DashboardPage> {

	@FindBy(css="#rx-app-drawer")
	private WebElement navigationPanel;
	@FindBy(css = "body > app-root > app-nav > rx-uikit-app-bar > div > button")
	private WebElement burgherMenuButton;
	@FindBy(css="#rx-app-drawer > ul > li:nth-child(1) > button > span > span")
	private WebElement dashboardButton;
	@FindBy(css="#rx-app-drawer > ul > li:nth-child(2) > button > span > span")
	private WebElement stockManagementButton;
	@FindBy(css="#rx-app-drawer > ul > li:nth-child(3) > button > span > span")
	private WebElement orderingButton;
	@FindBy(css="#rx-app-drawer > ul > li:nth-child(4) > button > span > span")
	private WebElement returnsButton;
	@FindBy(css="#rx-app-drawer > ul > li:nth-child(5) > button > span > span")
	private WebElement reportsButton;
	@FindBy(css="#searchStockAutocomplete > rx-uikit-base-autocomplete > mat-form-field > div > div.mat-form-field-flex > div > mat-icon")
	private WebElement cancelButton;
	
	//Orders menu
	@FindBy(css="#rx-app-drawer > ul > li:nth-child(3) > ul > li:nth-child(1) > button > span")
	private WebElement purchaseOrdersText;
	@FindBy(css="#rx-app-drawer > ul > li:nth-child(3) > ul > li:nth-child(1) > button")
	private WebElement purchaseOrdersButton;
	@FindBy(css="#rx-app-drawer > ul > li:nth-child(3) > ul > li:nth-child(2) > button")
	private WebElement ordersSchedulesButton;
	@FindBy(css="#rx-app-drawer > ul > li:nth-child(3) > ul > li:nth-child(3) > button")
	private WebElement manualOrdersButton;

	//Stock mngmnt menu
	@FindBy(css="#rx-app-drawer > ul > li:nth-child(2) > ul > li:nth-child(1) > button")
	private WebElement searchStockButton;
	@FindBy(css="#rx-app-drawer > ul > li:nth-child(2) > ul > li:nth-child(2) > button")
	private WebElement exceptionCountsButton;
	
	//reports menu
	@FindBy(css="#rx-app-drawer > ul > li:nth-child(5) > ul > li:nth-child(1) > button")
	private WebElement viewReportsButton;
	@FindBy(css="#rx-app-drawer > ul > li:nth-child(5) > ul > li:nth-child(2) > button")
	private WebElement itemMovementReport;
	
	//returns menu
	@FindBy(css="#rx-app-drawer > ul > li:nth-child(4) > ul > li > button")
	private WebElement returnsManagementButton;
	
	@FindBy(id="mat-input-0")
	private WebElement itemSearch;
	@FindBy(css="body > app-root > app-nav > app-dashboard > div.widget-container-sx.padding-top-3x.padding-bottom-8x.padding-left-3x > app-dashboard-widget.stock-search-widget > app-stock-search-widget > app-stock-search-form > form > button")
	private WebElement searchButton;
	@FindBy(css="#mat-autocomplete-0")
	private WebElement itemMatAutocomplete;
	
	//Widgets
	@FindBy(css="body > app-root > app-nav > app-dashboard > div.widget-container-sx.padding-top-3x.padding-bottom-8x.padding-left-3x > app-dashboard-widget.stock-search-widget > mat-toolbar")
	private WebElement stockSearchWidget;
	@FindBy(css="body > app-root > app-nav > app-dashboard > div.widget-container-sx.padding-top-3x.padding-bottom-8x.padding-left-3x > app-dashboard-widget.shipments-widget > mat-toolbar")
	private WebElement todaysIncomingShipmentsWidget;
	@FindBy(css="body > app-root > app-nav > app-dashboard > div.widget-container-sx.padding-top-3x.padding-bottom-8x.padding-left-3x > app-dashboard-widget.manual-orders-widget > mat-toolbar")
	private WebElement todaysOpenManualOrderWidget;
	@FindBy(css="body > app-root > app-nav > app-dashboard > div.widget-container-dx.padding-top-3x.paddding-right-3x.padding-bottom-8x > app-dashboard-widget.tasks-widget > mat-toolbar")
	private WebElement tasksWidget;
	@FindBy(css="body > app-root > app-nav > app-dashboard > div.widget-container-dx.padding-top-3x.paddding-right-3x.padding-bottom-8x > app-dashboard-widget.reports-widget > mat-toolbar")
	private WebElement reporsWidget;
	
	@FindBy(css="body > app-root > app-nav > app-dashboard > div.widget-container-sx.padding-top-3x.padding-bottom-8x.padding-left-3x > app-dashboard-widget.manual-orders-widget > mat-toolbar > button")
	private WebElement startAnOrderButton;
	
	
	private WebDriver driver;

	public DashboardPage(WebDriver driver) {
		this.driver = driver;
	}
	
	
	/*
	 * Autocomplete handle
	 */
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
	
	/*
	 * Click
	 */
	public StockSearchPage startAnOrderButtonClick() {
		startAnOrderButton.click();
		return new StockSearchPage(driver);
	}
	
	public void clickBurgherMenuButton() {
		burgherMenuButton.click();
	}
	
	public void clickStockManagementButton()
	{
		stockManagementButton.click();
	}
	
	public PurchaseOrderPage clickOrderingButton()
	{
		orderingButton.click();
		return new PurchaseOrderPage(driver);
	}
	
	public void clickReportsButton() {
		reportsButton.click();
	}
	
	public void clickReturnsButton() {
		returnsButton.click();
	}
	
	public ReturnsManagementPage clickReturnsManagementButton() {
		returnsManagementButton.click();
		return new ReturnsManagementPage(driver);
	}
	
	public PurchaseOrderPage clickPurchaseOrdersButton() {
		purchaseOrdersButton.click();
		return new PurchaseOrderPage(driver);
	}
	
	public StockSearchPage clickSearchStockButton() {
		searchStockButton.click();
		return new StockSearchPage(driver);
	}
	
	public StockSearchPage clicksearchButton() {
		searchButton.click();
		return new StockSearchPage(driver);
	}
	
	public ManualOrdersPage clickManualOrdersButton() {
		manualOrdersButton.click();
		return new ManualOrdersPage(driver);
	}
	
	public void cancelButtonClick() {
		cancelButton.click();
	}
	
	/*
	 * Displayed
	 */
	public Boolean navigationPanelDisplayed() {
		return navigationPanel.isDisplayed();
	}
	
	public Boolean stockSearchWidgetDisplayed() {
		return stockSearchWidget.isDisplayed();
	}
	
	public Boolean todaysIncomingShipmentsWidgetDisplayed() {
		return todaysIncomingShipmentsWidget.isDisplayed();
	}
	
	public Boolean todaysOpenManualOrderWidgetDisplayed() {
		return todaysOpenManualOrderWidget.isDisplayed();
	}
	
	public Boolean tasksWidgetDisplayed() {
		return tasksWidget.isDisplayed();
	}
	
	public Boolean reporsWidgetDisplayed() {
		return reporsWidget.isDisplayed();
	}
	
	public Boolean dashboardButtonDisplayed() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(dashboardButton));
		return dashboardButton.isDisplayed();
	}
	
	public Boolean stockManagementButtonDisplayed() {
		return stockManagementButton.isDisplayed();
	}
	
	public Boolean orderingButtonDisplayed() {
		return orderingButton.isDisplayed();
	}
	
	public Boolean returnsButtonDisplayed() {
		return returnsButton.isDisplayed();
	}
	
	public Boolean reportsButtonDisplayed() {
		return reportsButton.isDisplayed();
	}
	
	public Boolean purchaseOrdersButtonDisplayed() {
		return purchaseOrdersButton.isDisplayed();
	}
	
	public Boolean ordersSchedulesButtonDisplayed() {
		return ordersSchedulesButton.isDisplayed();
	}
	
	public Boolean manualOrdersButtonDisplayed() {
		return manualOrdersButton.isDisplayed();
	}
	
	public Boolean searchStockButtonDisplayed() {
		return searchStockButton.isDisplayed();
	}
	
	public Boolean exceptionCountsButtonDisplayed() {
		return exceptionCountsButton.isDisplayed();
	}
	
	public Boolean viewReportsButtonDisplayed() {
		return viewReportsButton.isDisplayed();
	}
	
	public Boolean itemMovementReportDisplayed() {
		return itemMovementReport.isDisplayed();
	}
	
	public Boolean returnsManagementButtonDisplayed() {
		return returnsManagementButton.isDisplayed();
	}
	
	public Boolean itemMatAutocompleteDisplayed() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(itemMatAutocomplete));
		return itemMatAutocomplete.isDisplayed();
	}
	
	public Boolean cancelButtonDisplayed() {
		return cancelButton.isDisplayed();
	}
	
	/*
	 * Text
	 */
	public String getPurchaseOrdersText() {
		return purchaseOrdersText.getText();
	}
	
	public void itemSearchInsert(String item) {
		itemSearch.sendKeys(item);
	}
	
	public String itemSearchPlaceHolder() {
		return itemSearch.getAttribute("placeholder");
	}
	
	public String getItemSearchText() {
		return itemSearch.getText();
	}
	
	public String itemMatAutocompleteScrollHeight() {
		return itemMatAutocomplete.getAttribute("scrollHeight");
	}
	
	/*
	 * Enabled
	 */
	public Boolean searchButtonEnabled() {
		return searchButton.isEnabled();
	}

	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(searchButton)) {
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
