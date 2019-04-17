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

public class GlobalGridUpdatePage extends LoadableComponent<GlobalGridUpdatePage>{

	@FindBy(css="#page-header-title")
	private WebElement pageTitleHeader;
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
	@FindBy(id="rx-grid-settings-update-parameters-button")
	private WebElement updateParemetersButton;
	/*
	 * Grid elements
	 */
	@FindBy(id="rx-grid-settings-cell_1_5")
	private WebElement genericGridElement;
	
	private WebDriver driver;

    public GlobalGridUpdatePage(WebDriver driver) {
        this.driver = driver;
    }
	
    /*
	 * Click elements
	 */

	public CellParametersPopup clickUpdateParemetersButton() {
		updateParemetersButton.click();
		return new CellParametersPopup(driver);
	}
	
	public void clickGenericGridElement() {
		genericGridElement.click();
	}
	
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
	
	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(pageTitleHeader)) {
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
