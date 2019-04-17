/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.stockplus;

import static com.oneleo.test.automation.core.UIUtils.ui;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

public class ReplenishmentPage extends LoadableComponent<ReplenishmentPage> {

	@FindBy(id = "rx-corp-submenu-Replenishment Parameters-list-name-4")
	
	private WebDriver driver;
	
	@FindBy(id = "rx-corp-submenu-list-6")
	private WebElement gridManagementButton;
   
    @FindBy(id="rx-corp-submenu-Forecasting Parameters-list-name-2")
    private WebElement forecastParameter;
    
    @FindBy(id="rx-corp-submenu-Forecast Diagnostic-list-name-3")
    private WebElement forecastDiagnostic;
    
    @FindBy(id="rx-corp-submenu-Grid Management-list-name-6")
    private WebElement gridManagementLink;
        
    @FindBy(id="rx-corp-submenu-list-3")
    private WebElement forecastDiagnosticLink;

    @FindBy(id="rx-corp-submenu-Replenishment Diagnostic-list-name-4")
    private WebElement replenishmenttDiagnosticLink;
 
    
      
	@FindBy(id = "param-tab-2")
	private WebElement gridOverridesTab;


	public ReplenishmentPage(WebDriver driver) {
		this.driver = driver;
	}

	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(gridManagementButton)) {
			throw new Error();
		}
		try {
		} catch (NoSuchElementException e) {
			Assert.fail("Page is not loaded!");
		}

	}
	public void setFilters(String store, String item) {

		if (StringUtils.isNotBlank(store)) {
			WebElement storeElement = getStoreField();
			storeElement.click();
			storeElement.sendKeys(store);
		}

		if (StringUtils.isNotBlank(item)) {
			WebElement itemElement = getItemField();
			itemElement.click();
			itemElement.sendKeys(item);
		}
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
	
	public WebElement getSearchButton(){
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@id='mat-tab-content-0-0']/div/rx-replenishment-diagnostic-item/div/form/button[1]"))), 20);
		return driver.findElement(By.xpath("//*[@id='mat-tab-content-0-0']/div/rx-replenishment-diagnostic-item/div/form/button[1]"));
	}
	
	public WebElement getClearButton(){
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@id='replenishment-clear-btn']"))), 20);
		return driver.findElement(By.xpath("//*[@id='replenishment-clear-btn']"));
	}
	
	public WebElement getSearchBoxItemDescriptions(){
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(By.id("mat-option-0")));
		return driver.findElement(By.xpath("//*[@id='mat-autocomplete-0']"));
	}
	
	public WebElement getErrorMessageBox(){
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(By.id("mat-error-7")));
		return driver.findElement(By.xpath("//*[@id='mat-error-7']"));
	}
	
	public Boolean isSearchBoxItemDescriptionsVisible(){
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@id='mat-autocomplete-0']"))), 20);
		return driver.findElement(By.xpath("//*[@id='mat-autocomplete-0']")).isDisplayed();
	}
	
	public Boolean isSearchBoxVisible(){
		return driver.findElement(By.xpath("//*[@id='mat-autocomplete-0']")).isDisplayed();
	}
	
	
	public Boolean isNDCBoxSectionVisible(){
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(By.id("ndc-group")));
		return driver.findElement(By.xpath("//*[@id='ndc-group']")).isDisplayed();
	}
	
	public List<WebElement> getListItems(){
		return driver.findElements(By.className("mat-option"));
	}
	
	



	public GridManagementPage clickGridOverridesTab() {
		this.gridOverridesTab.click();
		return new GridManagementPage(driver);
	}
	
	public GridManagementPage clickGridManagementButton() {
		this.gridManagementButton.click();
		return new GridManagementPage(driver);
	}
	
    public ForecastErrorManagerPage clickForecastDiagnosticButton() {
        this.forecastDiagnostic.click();
        return new ForecastErrorManagerPage(driver);
     }
    
    public ForecastErrorManagerPage clickForecastParameterButton() {
        this.forecastParameter.click();
        return new ForecastErrorManagerPage(driver);
     }    
    
    public ForecastErrorManagerPage clickForecastDiagnosticLink() {
        this.forecastDiagnosticLink.click();
        return new ForecastErrorManagerPage(driver);
     }

    public ForecastErrorManagerPage clickReplenishmenttDiagnosticLink() {
        this.replenishmenttDiagnosticLink.click();
        return new ForecastErrorManagerPage(driver);
     }
    
    public ReplenishmentDiagnosingStorePage clickReplenishmenttDiagnostic() {
        this.replenishmenttDiagnosticLink.click();
        return new ReplenishmentDiagnosingStorePage(driver);
     }
    
    public Boolean isDisplayedGridManagementButton() {
        Boolean isPresentAndDisplayed = Boolean.FALSE;

        try {
            WebElement element = driver.findElement(By.id("rx-corp-submenu-Grid Management-list-name-6"));
            if (element.isDisplayed()) {
                isPresentAndDisplayed = Boolean.TRUE;
            }
        } catch (NoSuchElementException nsee) {
            isPresentAndDisplayed = Boolean.FALSE;
        }

        return isPresentAndDisplayed;
    }
    

	@Override
	protected void load() {
		PageFactory.initElements(driver, this);
	}

}
