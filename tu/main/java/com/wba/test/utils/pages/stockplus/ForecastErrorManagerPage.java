/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.stockplus;

import static com.oneleo.test.automation.core.UIUtils.ui;

import java.util.Objects;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

public class ForecastErrorManagerPage extends LoadableComponent<ForecastErrorManagerPage> {
	private WebDriver driver;

	//@FindBy(id = "search-button")
	//private WebElement searchButton;
	
	
	@FindBy(xpath = "//*[@id=\"mat-tab-content-0-0\"]/div/rx-replenishment-diagnostic-item/div/form/button[1]")
	private WebElement searchButton;

	@FindBy(xpath = "//*[@id=\"mat-option-0\"]")
	private WebElement OnFirstResultSuggest;
	
	@FindBy(xpath = "//*[@id=\"rx-corporate-user-app-main-container\"]/dashboard-view/div/div/div[2]/rx-forecast-parameters/div[1]/rx-forecast-parameters-template-manager/mat-card/form/mat-form-field/div/div[1]/div")
	private WebElement searchTemplate;
	
	@FindBy(xpath = "//*[@id=\"mat-card0\"]")
	private WebElement OnFirstResult;
	
	@FindBy(xpath = "//*[@id='mat-card0']/div/span[1]/b")
	private WebElement itemFinded;
	@FindBy(xpath = "//*[@id='mat-card1']/div/span[1]/b")
	private WebElement itemFinded2;
	@FindBy(xpath = "//*[@id=\"mat-input-1\"]")
	private WebElement formEnterUpc;
	@FindBy(xpath = "//*[@id=\"mat-input-0\"]")
	private WebElement formEnterStore;
	@FindBy(xpath = "//*[@id=\"form-wrapper\"]/form/mat-form-field[1]/div/div[1]/div")
	private WebElement itemEmpty;
	
	@FindBy(xpath = "//*[@id=\"rx-corporate-user-app-main-container\"]/dashboard-view/div/div/div[2]")
	private WebElement mainPage;
	
	@FindBy(xpath = "//*[@id=\"rx-corporate-user-app-main-container\"]/dashboard-view/div/div/div[2]/rx-forecast-calculation/div/div/div/div[2]/rx-forecast-calculation-detail/div")
	private WebElement itemNotEmpty;
	
	@FindBy(xpath = "//*[@id=\"rx-corporate-user-app-main-container\"]/dashboard-view/div/div/div[2]/rx-forecast-calculation/div/div/div/div[2]/rx-forecast-calculation-detail/div/mat-card[1]")
	private WebElement panelFull;
	
	@FindBy(xpath = "//*[@id=\"form-wrapper\"]/form/button[2]")
	private WebElement clearButton;
	@FindBy(xpath = "//*[@id=\"page-header-back-button\"]/span/mat-icon")
	private WebElement backButton;
	@FindBy(xpath = "//*[@id=\"mat-option-3\"]")
	private WebElement dropdownSmart;
	
	@FindBy(xpath = "//*//*[@id=\"mat-card0\"]")
	private WebElement suggestDropdownSmart;
	
	@FindBy(xpath = "//*[@id=\"mat-option-0\"]")
	private WebElement suggestFormEnter;
	
	@FindBy(xpath = "//*[@id=\"mat-error-0\"]")
	private WebElement formMsgError;
	
	
	
	public ForecastErrorManagerPage(WebDriver driver) {
		this.driver = driver;
	}

	public void insertformEnterStore(String s) {

		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(formEnterStore), 30);
		formEnterStore.sendKeys(s);
	}

	public void insertformEnterUpc(String s) throws InterruptedException {

		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(formEnterUpc), 50);
		formEnterUpc.sendKeys(s);
		WebElement suggestEnterUpc = driver.findElement(By.id("mat-input-1"));
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(suggestEnterUpc), 30);
		suggestEnterUpc.click();
		}
	
	
	public void insertformEnterUpcSomeCharacters(String s) throws InterruptedException {

		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(formEnterUpc), 50);
		formEnterUpc.sendKeys(s);
		WebElement suggestEnterUpc = driver.findElement(By.id("mat-input-1"));
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(suggestEnterUpc), 30);
		searchButton.click();
		}
	
	public void insertformEnterUpcEmpty(String s) throws InterruptedException {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(formEnterUpc), 30);
		formEnterUpc.sendKeys(s);
		
		WebElement suggestEnterUpc = driver.findElement(By.xpath("//*[@id='mat-autocomplete-0']/mat-optgroup/mat-option"));
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(suggestEnterUpc), 30);
		suggestEnterUpc.click();
		}

	public void clickButtonSearch() {
		searchButton.click();
	}
	
	public void clickOnFirstResultSuggest() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(OnFirstResultSuggest), 30);
		OnFirstResultSuggest.click();
	}
	
	public void clickOnFirstResult() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(OnFirstResult), 30);
		OnFirstResult.click();
	}
	
	public void clickButtonClear() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(itemFinded), 30);
		clearButton.click();
	}
	
	public boolean isEnabledDropdownSmart() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(suggestFormEnter), 30);
		return suggestFormEnter.isEnabled();
	}

	public boolean isEnabledsearchTemplate() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(searchTemplate), 30);
		return searchTemplate.isEnabled();
	}
	
	
	public boolean isDisplayedDropdownSmart() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(dropdownSmart), 10);
		return dropdownSmart.isDisplayed();
	}

	public boolean checkItemFinded(String item) {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(itemFinded), 30);
		return itemFinded.getText().equals(item);
	}
	
	public boolean checkCountRow(String s) {
		
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(itemEmpty), 30);
		return itemEmpty.getText().equals(s);
	}
	
	
	public boolean checkAllItemFinded(String item,String item2) {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(itemFinded), 30);
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(itemFinded2), 30);
		return (itemFinded.getText().equals(item) && itemFinded2.getText().equals(item2) );
	}
	
	public boolean checkformMsgError(String value) {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(formMsgError), 50);
		return (formMsgError.getText().equals(value) );
	}
	
	
	public boolean notEnable_NDC_UPC_ItemDescription() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(formEnterUpc), 30);
		return ( formEnterUpc.isEnabled() );
	}
	
	
	public boolean isVisibleSearchButton() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(formEnterUpc), 30);
		return ( searchButton.isDisplayed() );
	}
	
	public boolean isEnabledSearchButton() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(formEnterUpc), 30);
		return ( searchButton.isEnabled() );
	}
	
	public boolean isVisibleClearButton() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(clearButton), 30);
		return ( clearButton.isDisplayed() );
	}
	
	public boolean isEnableClearButton() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(clearButton), 30);
		return ( clearButton.isDisplayed() );
	}
	
	public boolean isDisplayedBackButton() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(backButton), 30);
		return ( backButton.isDisplayed() );
	}
	
	public boolean isEmptyStoreNumber() {
		return ( formEnterStore.getText().equals(""));
	}

	
	
	public boolean isPanelEmpty () {
		try {
			panelFull.isDisplayed();
			return false;
		} catch (Exception e) {
			return true;
		}
	}
	
    public void refineByHotkey() {
        WebElement refineButton = driver.findElement(By.xpath("//*[@id='search-button']"));
        ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(refineButton), 30);
        Actions keyAction = new Actions(driver);
        keyAction.keyDown(Keys.ALT).sendKeys("s").perform();
    }
    
    public void refineByHotkeyClear() {
        WebElement refineButton = driver.findElement(By.xpath("//*[@id=\"form-wrapper\"]/form/button[2]"));
        ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(refineButton), 30);
        Actions keyAction = new Actions(driver);
        keyAction.keyDown(Keys.ALT).sendKeys("l").perform();
    }
    
    
	public boolean checkItemNotEmpty(String label) {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(itemNotEmpty), 30);
		return (itemNotEmpty.getText().equals(label)  );
	}
	
	public boolean isEnabledMainPage() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(mainPage), 30);
		return mainPage.isEnabled();
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
