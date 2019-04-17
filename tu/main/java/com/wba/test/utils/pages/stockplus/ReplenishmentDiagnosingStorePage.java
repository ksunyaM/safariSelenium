/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.stockplus;

import static com.oneleo.test.automation.core.UIUtils.ui;

import java.util.List;
import java.util.Objects;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

public class ReplenishmentDiagnosingStorePage extends LoadableComponent<ReplenishmentDiagnosingStorePage>{

	private WebDriver driver;



	@FindBy(id = "store-input")
	private WebElement storeInput;
	
	@FindBy(id = "mat-input-1")
	private WebElement itemInput;
	
	@FindBy(css = "mat-option")
	private WebElement itemInputPopUp;
	 
	@FindBy(id = "mat-tab-content-0-1")
	private WebElement buttonSearch;
	@FindBy(css="#container-white > form > button.margin-button.mat-raised-button.mat-secondary")
	private WebElement storeSearchButton;
	
	@FindBy(css="#mat-tab-content-0-0 > div > rx-replenishment-diagnostic-item > div > form > button.button-margin-top.mat-raised-button.mat-secondary")
	private WebElement itemButtonSearch;

	@FindBy(id = "replenishment-clear-btn")
	private WebElement clearButton;

	@FindBy(css = "#mat-error-1 > div")
	private WebElement errorStoreMessage;

	@FindBy(id = "mat-tab-label-0-0")
	private WebElement itemTab;
	@FindBy(id = "mat-tab-label-0-1")
	private WebElement storeTab;
	
	@FindBy(id="produc-name-group")
	private WebElement itemNameGroup;
	
	@FindBy(id="mat-autocomplete-0")
	private WebElement itemAutocomplete;


	@FindBy(id = "replenishment-diagnostic-store-input")
	private WebElement inputStore;
	
	@FindBy(id="store-number-input")
	private WebElement storeNumberInput;
	
	@FindBy(id="no-results")
	private WebElement noResultsMessage;
	
	@FindBy(id="replenishment-diagnostic-store-table")
	private WebElement storeTable;
	
	@FindBy(id="view-button")
	private WebElement viewButton;
	
	@FindBy(css="#replenishment-error-data-container > mat-card > div.margin-top-6x.padding-top-6x > button.mat-raised-button.mat-primary")
	private WebElement viewOrderHistoryButton;
	@FindBy(css="#container-item-white > form > button.button-margin-top.mat-raised-button.mat-secondary")
	private WebElement itemSearchButton;
	@FindBy(id="mat-input-3")
	private WebElement itemSearchInput;
	@FindBy(css="#replenishment-error-data-container > mat-card > div.margin-top-6x.padding-top-6x > button.margin-right-2x.mat-raised-button.mat-neutral")
	private WebElement limitHistoryButton;
	
	private String element;
	
	public ReplenishmentDiagnosingStorePage(WebDriver driver) {
		this.driver = driver;
	}

	public void clickItemInAutocomplete(String itemSearched) {
		List<WebElement> items = itemAutocomplete.findElements(By.cssSelector("mat-optgroup > mat-option"));
		for(WebElement item : items) {
			if(item.getAttribute("ng-reflect-value").equals(itemSearched)) {
				item.click();
				break;
			}
		}
	}
	
	public void clickItemTab(){
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(itemTab), 20);
		itemTab.click();
	}
	

	public void clickStoreTab(){
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(storeTab), 20);
		storeTab.click();
	}
	
	
	public void insertInputStore(String store)
	{
		inputStore.sendKeys(store);
	}
	
	public void storeNumberInput(String store) {
		storeNumberInput.sendKeys(store);
	}
	
	public void itemSearchInputInsert(String item) {
		itemSearchInput.sendKeys(item);
	}
	
	public void insertItem(String item) {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(itemInput), 20);
		itemInput.sendKeys(item);
	}
	
	
public void setFilters(String store, String item) {

	ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(storeInput), 20);
	storeInput.click();
	storeInput.sendKeys(store);
	
	ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(itemInput), 20);
	itemInput.click();
	itemInput.sendKeys(item);
	ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(itemInputPopUp), 20);
	itemInputPopUp.click();
		
	}
	public Boolean isItemTabDisplayed() {
		return itemTab.isDisplayed();
	}
	public Boolean isStoreTabDisplayed() {
		return storeTab.isDisplayed();
	}
	public Boolean isClearButtonDisplayed() {
		return clearButton.isDisplayed();
	}
	public Boolean isButtonSearchDisplayed() {
		return buttonSearch.isDisplayed();
	}
	public Boolean limitHistoryButtonDisplayed() throws InterruptedException {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(limitHistoryButton), 60);
		return limitHistoryButton.isDisplayed();
	}
	
	public OrderHistoryPage clickViewOrderHistoryButton() {
		// Create instance of Javascript executor
		 
		JavascriptExecutor je = (JavascriptExecutor) driver;
				  		 
		//Identify the WebElement which will appear after scrolling down
						
		WebElement Scrollelement = driver.findElement(By.cssSelector("#replenishment-error-data-container > mat-card > div.margin-top-6x.padding-top-6x > button"));
		// execute query which actually will scroll until that element is not appeared on page.
		je.executeScript("arguments[0].scrollIntoView(true);",Scrollelement);
		
		Scrollelement.click();
		return new OrderHistoryPage(driver);
	}
	
	public Boolean isViewOrderHistoryButtonDisplayed() {
		// Create instance of Javascript executor
		 
		JavascriptExecutor je = (JavascriptExecutor) driver;
				  		 
		//Identify the WebElement which will appear after scrolling down
						
		WebElement Scrollelement = driver.findElement(By.cssSelector("#replenishment-error-data-container > mat-card > div.margin-top-6x.padding-top-6x > button"));
		// execute query which actually will scroll until that element is not appeared on page.
		je.executeScript("arguments[0].scrollIntoView(true);",Scrollelement);
		
		return Scrollelement.isDisplayed();
	}
	public String getDrugHeaderElement(int j,int k) {
		return element = driver.findElement(By.cssSelector("#replenishment-error-data-container > mat-card > rx-replenishment-drug-details > div > div:nth-child(3) > div:nth-child(j) > div.section-label > span:nth-child(k)")).getText();
	}
	
	public void scrollDownToReplenishment() {
		
		// Create instance of Javascript executor
		 
		JavascriptExecutor je = (JavascriptExecutor) driver;
		  		 
		//Identify the WebElement which will appear after scrolling down
				
		WebElement Scrollelement = driver.findElement(By.cssSelector("#replenishment-error-data-container > mat-card > rx-replenishment-drug-details > div > div:nth-child(3) > div:nth-child(3) > div.section-label > span:nth-child(1)"));
		// execute query which actually will scroll until that element is not appeared on page.
		je.executeScript("arguments[0].scrollIntoView(true);",Scrollelement);
			
	}
	
	public void scrollDownToCurrentShelves() {
		
		// Create instance of Javascript executor
		 
		JavascriptExecutor je = (JavascriptExecutor) driver;
		  		 
		//Identify the WebElement which will appear after scrolling down
				
		WebElement Scrollelement = driver.findElement(By.cssSelector("#replenishment-error-data-container > mat-card > div.card-section-alignment > rx-replenishment-output > div > div:nth-child(3) > div.section-label > span:nth-child(1)"));
		// execute query which actually will scroll until that element is not appeared on page.
		je.executeScript("arguments[0].scrollIntoView(true);",Scrollelement);
			
	}
	
	public void scrollDownToStockException() {
		
		// Create instance of Javascript executor
		 
		JavascriptExecutor je = (JavascriptExecutor) driver;
		  		 
		//Identify the WebElement which will appear after scrolling down
				
		WebElement Scrollelement = driver.findElement(By.cssSelector("#replenishment-error-data-container > mat-card > div.card-section-alignment > rx-replenishment-most-recent-stock-exception > div > div:nth-child(2) > div.section-label > span:nth-child(1)"));
		// execute query which actually will scroll until that element is not appeared on page.
		je.executeScript("arguments[0].scrollIntoView(true);",Scrollelement);
			
	}
	
	
	public String getReplenishmentHeaderElement(int j,int k) {
		
		return element = driver.findElement(By.cssSelector("#replenishment-error-data-container > mat-card > rx-replenishment-drug-details > div > div:nth-child(3) > div:nth-child(j) > div.section-label > span:nth-child(k)")).getText();
		 
	}
	
	public String getCurrentShelvesHeaderElement(int k) {
		
		return element = driver.findElement(By.cssSelector("#replenishment-error-data-container > mat-card > div.card-section-alignment > rx-replenishment-output > div > div:nth-child(3) > div.section-label > span:nth-child(k)")).getText();
	}
	
	public String getStockExceptionHeaderElement(int j,int k) {
		
		return element = driver.findElement(By.cssSelector("#replenishment-error-data-container > mat-card > div.card-section-alignment > rx-replenishment-most-recent-stock-exception > div > div:nth-child(j) > div.section-label > span:nth-child(k)")).getText();
	}
	
		public WebElement getStoreField(){
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@id='store-input']"))), 20);
		return driver.findElement(By.xpath("//*[@id='store-input']"));
	}

	public WebElement getRxSmartSearchProduct(){
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(driver.findElement(By.tagName("rx-smart-search-product"))), 20);
		return driver.findElement(By.tagName("rx-smart-search-product"));
	}

	public WebElement getItemField(){
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@id='mat-input-1']"))), 20);
		return driver.findElement(By.xpath("//*[@id='mat-input-1']"));
	}
	
	public String searchRightPanelItem(){
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(driver.findElement(By.cssSelector("#product-card-list > div > mat-card > mat-card-header > div > mat-card-title"))), 20);
		return driver.findElement(By.cssSelector("#product-card-list > div > mat-card > mat-card-header > div > mat-card-title")).getText();
	}
	
	public String searchLeftPanelItemStore(){
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(driver.findElement(By.cssSelector("#product-card-list > div > mat-card > mat-card-header > div > mat-card-title"))), 20);
		return driver.findElement(By.cssSelector("#replenishment-error-data-container > mat-card > rx-replenishment-drug-details > div > div:nth-child(2)")).getText();
	}
	
	public String getNoResultsMessage() {
		return noResultsMessage.getText();
	}


	public void setInputStore(String value)
	{
		storeInput.sendKeys(value);
	}
	
	public void itemSearchButtonClick() {
		itemSearchButton.click();
	}
	
	public LimitHistoryPage limitHistoryButtonClick() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(limitHistoryButton), 65);
		limitHistoryButton.click();
		return new LimitHistoryPage(driver);
	}
	
	public void viewOrderHistoryButtonClick() {
		viewOrderHistoryButton.click();
	}

	public void clickSearchButton()
	{
		buttonSearch.click();
	}
	
	public void clickStoreSearchButton() {
		storeSearchButton.click();
	}
	
	public void clickItemButtonSearch() {
		itemButtonSearch.click();
	}
	
	public Boolean viewButtonDisplayed() {
		return viewButton.isDisplayed();
	}


	public boolean isVisiblestoreInput()
	{
		boolean isDisplayed=false;
		try{

			ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.storeInput), 5);
			isDisplayed = storeInput.isDisplayed();
		}
		catch (NoSuchElementException ne){}
		return  isDisplayed;
	}

	public boolean isVisibleButtonSearch()
	{
		boolean isDisplayed=false;
		try{
			ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.buttonSearch), 5);
			isDisplayed = buttonSearch.isDisplayed();
		}
		catch (NoSuchElementException ne){}
		return  isDisplayed;
	}

	public boolean isVisibleClearButton()
	{
		boolean isDisplayed=false;
		try{
			ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.clearButton), 5);
			isDisplayed = clearButton.isDisplayed();
		}
		catch (NoSuchElementException ne){}
		return  isDisplayed;
	}


	public String getStoreErrorMessage()
	{
		String errorMessage="";
		try{
			ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.errorStoreMessage), 10);
			errorMessage = errorStoreMessage.getText();
		}
		catch (NoSuchElementException ne){}
		return  errorMessage;
	}
	
	public void clickItemFromProductGroup(String itemName) throws InterruptedException {
		synchronized (this) {
			wait(25000);
		}
		List<WebElement> items = itemNameGroup.findElements(By.cssSelector("mat-option"));
		for(WebElement item : items) {
			if(item.getText().contains(itemName)) {
				item.click();
				break;
			}
		}
	}
	
	public void clickClearButton() {
		clearButton.click();
	}
	
	public int getMapSize() {
		List<WebElement> rows = storeTable.findElements(By.cssSelector("[class='mat-row']"));
		return rows.size();
	}

	public String getFirstCreationDate() {
		
		return storeTable.findElement(By.cssSelector("#replenishment-diagnostic-store-table > mat-row.mat-row.selected > mat-cell.text-align-right.mat-cell.cdk-column-processingDateTime.mat-column-processingDateTime")).getText();	
		
	}
	
	public String getCreationDate(int i) {
		
		return driver.findElement(By.cssSelector("#mat-row.mat-row:nth-child(" + i + ") > mat-cell.text-align-right.mat-cell.cdk-column-processingDateTime.mat-column-processingDateTime")).getText();	
		
	}
	
	public String getStoreInput() {
		return storeNumberInput.getAttribute("value");
	}

	@Override
	protected void load() {
		PageFactory.initElements(driver, this);
	}

	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(storeInput)) {
			throw new Error();
		}
		try {
		} catch (NoSuchElementException e) {
			Assert.fail("Page is not loaded!");
		}
	}

}
