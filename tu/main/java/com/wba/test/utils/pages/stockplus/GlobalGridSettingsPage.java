/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.stockplus;

import static com.oneleo.test.automation.core.UIUtils.ui;

import java.util.List;
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

public class GlobalGridSettingsPage extends LoadableComponent<GlobalGridSettingsPage>{

	@FindBy(id = "rx-grid-settings-save-button")
	private WebElement saveButton;
	@FindBy(css="#rx-grid-settings-frequency")
	private WebElement settingFrequency;
	@FindBy(css="#rx-grid-settings-cost")
	private WebElement settingCost;
	@FindBy(css="#rx-grid-settings-apply-button")
	private WebElement applyButton;
	@FindBy(css="#dialog-confirm-button")
	private WebElement applyConfirmButton;	
	@FindBy(css="#dialog-container > div.mat-dialog-content.mat-body-1.margin-bottom-3x > div")
	private WebElement applyConfirmDialog;
	@FindBy(css="#dialog-cancel-button")
	private WebElement cancelConfirmDialog;	
	@FindBy(css="#frequency_0")
	private WebElement fristFrequencyCellRange;
	@FindBy(id="page-header-back-button")
	private WebElement backButton;
	@FindBy(id="page-header-title")
	private WebElement headerTitle;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-grid-settings > div > div > div > div.blocks > div > div.grid-details > mat-card > mat-card-content > div > p.margin-bottom-3x")
	private WebElement inputGridName;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-grid-settings > div > div > div > div.blocks > div > div.grid-details > mat-card > mat-card-content > div > p:nth-child(4)")
	private WebElement textAreaDescription;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-grid-settings > div > div > div > div.grid-settings-view > mat-card > mat-card-content > div > div.item.grid-list-block > div > div:nth-child(1) > mat-grid-list")
	private WebElement grid;
	@FindBy(id="rx-grid-settings-frequency")
	private WebElement gridSettingsFrequency;
	@FindBy(id="rx-grid-settings-cost")
	private WebElement gridSettingsCost;
	@FindBy(id="dialog-confirm-button")
	private WebElement confirmButtonYes;
	@FindBy(id="frequency_4")
	private WebElement frequencyWeekly;
	@FindBy(id="cost_2")
	private WebElement costWeekly;
	@FindBy(css="#frequency_2")
	private WebElement frequencyInput;
	@FindBy(id="cost_2")
	private WebElement costInput;

	
	
	
	
	private WebDriver driver;

    public GlobalGridSettingsPage(WebDriver driver) {
        this.driver = driver;
    }
    
    /*
     * content in the field checks
     */
    public void deleteGridSettingsCost() {
		gridSettingsCost.clear();
	}
    
	public void insertGridSettingsCost(String cost) {
		gridSettingsCost.sendKeys(cost);
	}
	
    public List<WebElement> getGridElementsWhenAdded() {
    	List <WebElement> listOfGridElements = grid.findElements(By.className("mat-grid-tile"));
    	ui().waitForCondition(driver, ExpectedConditions.numberOfElementsToBeMoreThan(By.className("mat-grid-tile"), listOfGridElements.size()), 10);
    	return grid.findElements(By.className("mat-grid-tile"));
    }
    
	public void insertCostWeeklyInput(String cost) {
		costWeekly.clear();
		costWeekly.sendKeys(cost);
	}
	
	public void insertFrequencyWeeklyInput(String frequency) {
		frequencyWeekly.clear();
		frequencyWeekly.sendKeys(frequency);
	}
	
	public String getValueCostWeeklyInput() {
		return costWeekly.getAttribute("value").toString();
	}
	
	public String getFrequencyWeeklyInput() {
		return frequencyWeekly.getAttribute("value").toString();
	}
	
    /*
	 * Click elements
	 */
    public void insertFrequencyAndCost(String frequency, String Cost) {
    	settingFrequency.clear();
    	settingCost.clear();
    	settingFrequency.sendKeys(frequency);
    	settingCost.sendKeys(Cost);
	}
    
    
    public void insertFrequencyFirstRangeCell(String frequency) {
    	fristFrequencyCellRange.clear();
    	fristFrequencyCellRange.sendKeys(frequency);
	}
    
    public String getInputFrequency() {
    	return settingFrequency.getAttribute("ng-reflect-model").toString();
	}
    
    public String getInputCost() {
		return settingCost.getAttribute("ng-reflect-model").toString();
	}
    
    public void cancelConfirmDialogButtonClick() {
    	cancelConfirmDialog.click();
	}
    
    public void saveButtonClick() {
		saveButton.click();
	}
	
    public void applyButtonClick() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(applyButton), 30);
    	applyButton.click();
	}
    
    public void applyConfirmButtonClick() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(applyConfirmButton), 30);
    	applyConfirmButton.click();
	}
    
    public Boolean isApplyConfirmDialogDisplayed() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(applyConfirmDialog), 10);
		return applyConfirmDialog.isDisplayed();
	}
    
	public String getValueCells (Integer i, Integer j) throws InterruptedException {
	    WebElement gridCells = driver.findElement(By.xpath("//*[@id='rx-grid-settings-cell_"+i+"_"+j+"']/figure"));
	    return gridCells.getText();
	}
	
	public String getGridSettingsFrequency() {
		return gridSettingsFrequency.getAttribute("ng-reflect-model");
	}
	
	public String getGridSettingsCost() {
		return gridSettingsCost.getAttribute("ng-reflect-model");
	}
	
	public void deleteGridSettingsFrequency() {
		gridSettingsFrequency.clear();
	}
	
	public void insertGridSettingsFrequency(String frequency) {
		gridSettingsFrequency.sendKeys(frequency);
	}
	
	public void confirmYesButtonClick() {
		confirmButtonYes.click();
	}
	
	/*
	 * displayed
	 */
	
	public Boolean isBackButtonDisplayed() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(backButton));
		return backButton.isDisplayed();
	}
	
	public Boolean isHeaderTitleDisplayed() {
		return headerTitle.isDisplayed();
	}
	
	public Boolean isInputGridNameDisplayed() {
		return inputGridName.isDisplayed();
	}
	
	public Boolean isTextAreaDescriptionDisplayed() {
		return textAreaDescription.isDisplayed();
	}
	
	public List<WebElement> getGridElements() {
    	List <WebElement> listOfGridElements = grid.findElements(By.className("mat-grid-tile"));
    	ui().waitForCondition(driver, ExpectedConditions.numberOfElementsToBe(By.className("mat-grid-tile"), listOfGridElements.size()), 10);
    	return grid.findElements(By.className("mat-grid-tile"));
    }
	
	/*
	 * Hot keys
	 */
	public void leaveCostInput() {
		costInput.sendKeys(Keys.TAB);
	}
	
	public void leaveFrequencyInput() {
		frequencyInput.sendKeys(Keys.TAB);
	}
	
	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(headerTitle)) {
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
