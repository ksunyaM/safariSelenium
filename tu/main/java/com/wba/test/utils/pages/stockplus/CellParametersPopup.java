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
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

public class CellParametersPopup extends LoadableComponent<CellParametersPopup>{

	@FindBy(id="rx-grid-cell-parameters-calculation-select")
	private WebElement calculationMatSelect;
	@FindBy(id="cdk-overlay-1")
	private WebElement calculationOverlay;
	@FindBy(id="rx-grid-cell-parameters-days-in-buy")
	private WebElement extraDaysInBuyInput;
	@FindBy(id="rx-grid-cell-parameters-service-level")
	private WebElement kInput;
	@FindBy(id="rx-grid-cell-parameters-k-max")
	private WebElement kMaxInput;
	@FindBy(id="rx-grid-cell-parameters-service-level")
	private WebElement serviceLevelInput;
	@FindBy(id="rx-grid-cell-parameters-early-time")
	private WebElement earlyTime;
	@FindBy(id="rx-grid-cell-parameters-risk-factor")
	private WebElement riskFactor;
	@FindBy(id="rx-grid-cell-parameters-radio-peak")
	private WebElement peakRadioButton;
	@FindBy(id="rx-grid-cell-parameters-hold-order")
	private WebElement daysToHoldOrder;
	@FindBy(id="rx-grid-cell-parameters-toggle")
	private WebElement cellParametersToggle;
	@FindBy(id="rx-grid-cell-parameters-radio-static")
	private WebElement statisticalRadioButton;
	@FindBy(id="mat-tab-label-0-0")
	private WebElement brandTab;
	@FindBy(id="mat-tab-label-0-1")
	private WebElement genericTab;
	@FindBy(id="rx-grid-cell-parameters-qty-select")
	private WebElement binQtyDropdown;
	@FindBy(id="rx-grid-cell-parameters-dialog-save-button")
	private WebElement saveButton;
	@FindBy(id="rx-grid-cell-parameters-dialog-cancel-button")
	private WebElement cancelButton;
	@FindBy(id="rx-grid-cell-parameters-weeks-no")
	private WebElement numberOfWeeks;
	
	/*
	 * Displayed elements
	 */
	@FindBy(css="#rx-grid-cell-parameters-toggle")
	private WebElement autoOrderToggle;
	@FindBy(css="#rx-grid-cell-parameters-popup > mat-dialog-content > div:nth-child(1) > div:nth-child(2) > mat-radio-group")
	private WebElement modelRadioButtons;
	@FindBy(css="#rx-grid-cell-parameters-popup > mat-dialog-content > div:nth-child(1) > div:nth-child(3) > div:nth-child(2)")
	private WebElement balancingDiv;
	@FindBy(css="#rx-grid-cell-parameters-popup > mat-dialog-content > div:nth-child(2) > div.selected-area-background")
	private WebElement selectedLeftDiv;
	@FindBy(id="rx-grid-cell-parameters-same-as-checkbox")
	private WebElement sameAsGenericCheckBox;
	
	
	/*
	 * Errors
	 */
	@FindBy(id="mat-error-13")
	private WebElement serviceLevelError;
	@FindBy(id="mat-error-2")
	private WebElement kErrorMessage;
	@FindBy(id="mat-error-4")
	private WebElement extraDaysInBuyError;
	@FindBy(id="mat-error-1")
	private WebElement kMaxError;
	@FindBy(id="mat-error-5")
	private WebElement earlyTimeError;
	@FindBy(id="mat-error-3")
	private WebElement riskFactorError;
	@FindBy(id="mat-error-14")
	private WebElement daysToHoldOrderError;
	@FindBy(id="mat-error-6")
	private WebElement daysToHoldOrderErrorForStatistical;
	
	private WebDriver driver;

    public CellParametersPopup(WebDriver driver) {
        this.driver = driver;
    }
    
    /*
     * Text handle
     */
    public void insertServiceLevelInput(String level) {
    	serviceLevelInput.sendKeys(level);
    }
    
    public void clearKInput() {
    	kInput.clear();
    }
    
    public void insertkInput(String k) {
    	kInput.sendKeys(k);
    }
    
    public void clearKMaxInput() {
    	kMaxInput.clear();
    }
    
    public void insertKMaxInput(String kMax) {
    	kMaxInput.sendKeys(kMax);
    }
    
    public void clearExtraDaysInBuyInput() {
    	extraDaysInBuyInput.clear();
    }
    
    public void insertExtraDaysInBuyInput(String extra) {
    	extraDaysInBuyInput.sendKeys(extra);
    }
    
    public void clearEarlyTime() {
    	earlyTime.clear();
    }
    
    public void insertEarlyTime(String time) {
    	earlyTime.sendKeys(time);
    }
    
    public void clearRiskFactor() {
    	riskFactor.clear();
    }
    
    public void insertRiskFactor(String risk) {
    	riskFactor.sendKeys(risk);
    }
    
    public void clearDaysToHoldOrder() {
    	daysToHoldOrder.clear();
    }
    
    public void insertDaysToHoldOrder(String days) {
    	daysToHoldOrder.clear();
    	daysToHoldOrder.sendKeys(days);
    }
    
    public void insertNumberOfWeeks(String days) {
    	numberOfWeeks.clear();
    	numberOfWeeks.sendKeys(days);
    }
    
    /*
     * Click
     */
    public void clickCellParametersToggle() {
    	cellParametersToggle.click();
    }
    
    public void clickCalculationMatSelect() {
    	calculationMatSelect.click();
    }
    
    public void clickCalculationOverlay() {
    	calculationOverlay.click();
    }
    
    public void selectSpecificCalculation(String selectedCalculation) {
    	List<WebElement> calculations = calculationOverlay.findElements(By.cssSelector("div > div > mat-option"));
    	for(WebElement calculation : calculations) {
    		if(calculation.getText().equals(selectedCalculation)) {
    			calculation.click();
    			break;
    		}
    	}
    }
    
    public void clickPeakRadioButton() {
    	peakRadioButton.click();
    }
    
    public void clickSaveButton() {
    	saveButton.click();
    }
    
    /*
     * Placeholders
     */
    public String getKPlaceholders() {
    	return kInput.getAttribute("placeholder");
    }
    
    public String getExtraDaysInBuyPlaceholder() {
    	return extraDaysInBuyInput.getAttribute("placeholder");
    }
    
    /*
     * Displayed
     */
    public Boolean isSameAsGenericCheckBoxDIsplayed() {
    	return sameAsGenericCheckBox.isDisplayed();
    }
    
    public Boolean isSelectedLeftDivDisplayed() {
    	return selectedLeftDiv.isDisplayed();
    }
    
    public Boolean isBalancingDivDisplayed() {
    	return balancingDiv.isDisplayed();
    }
    
    public Boolean isModelRadioButtonsDisplayed() {
    	return modelRadioButtons.isDisplayed();
    }
    
    public Boolean isAutoOrderToggleDisplayed() {
    	return autoOrderToggle.isDisplayed();
    }
    
    public Boolean isPeakRadioButtonDisplayed() {
    	return peakRadioButton.isDisplayed();
    }
    
    public Boolean isStatisticalRadioButtonDisplayed() {
    	return statisticalRadioButton.isDisplayed();
    }
    
    public Boolean isBrandTabDisplayed() {
    	return brandTab.isDisplayed();
    }
    
    public Boolean isGenericTabDisplayed() {
    	return genericTab.isDisplayed();
    }
    
    public Boolean isCalculationMatSelectDisplayed() {
    	return calculationMatSelect.isDisplayed();
    }
    
    public Boolean isExtraDaysInBuyInputDisplayed() {
    	return extraDaysInBuyInput.isDisplayed();
    }
    
    public Boolean isKInputDisplayed() {
    	return kInput.isDisplayed();
    }
    
    public Boolean isKMaxInputDisplayed() {
    	return kMaxInput.isDisplayed();
    }
    
    public Boolean isEarlyTimeDisplayed() {
    	return earlyTime.isDisplayed();
    }
    
    public Boolean isRiskFactorDisplayed() {
    	return riskFactor.isDisplayed();
    }
    
    public Boolean isDaysToHoldOrderDisplayed() {
    	return daysToHoldOrder.isDisplayed();
    }
    
    /*
     * Hot Keys
     */
    public void tabFromExtraDaysInBuyInput() {
    	extraDaysInBuyInput.sendKeys(Keys.TAB);
    }
    
    public void tabFromServiceLevelInput() {
    	serviceLevelInput.sendKeys(Keys.TAB);
    }
    
    public void tabDaysToHoldOrder() {
    	daysToHoldOrder.sendKeys(Keys.TAB);
    }
    

    /*
     * Errors
     */
    public String getextraDaysInBuyError() {
    	ui().waitForCondition(driver, ExpectedConditions.visibilityOf(extraDaysInBuyError), 10);
    	return extraDaysInBuyError.getText();
    }
    
    public String getServiceLevelError() {
    	ui().waitForCondition(driver, ExpectedConditions.visibilityOf(serviceLevelError), 10);
    	return serviceLevelError.getText();
    }
    
    public String getKError() {
    	ui().waitForCondition(driver, ExpectedConditions.visibilityOf(kErrorMessage), 10);
    	return kErrorMessage.getText();
    }
    
    public String getkMaxError() {
    	ui().waitForCondition(driver, ExpectedConditions.visibilityOf(kMaxError), 10);
    	return kMaxError.getText();
    }
    
    public String getEarlyTimeError() {
    	ui().waitForCondition(driver, ExpectedConditions.visibilityOf(earlyTimeError), 10);
    	return earlyTimeError.getText();
    }
    
    public String getRiskFactorError() {
    	ui().waitForCondition(driver, ExpectedConditions.visibilityOf(riskFactorError), 10);
    	return riskFactorError.getText();
    }
    
    public String getdaysToHoldOrderError() {
    	ui().waitForCondition(driver, ExpectedConditions.visibilityOf(daysToHoldOrderError), 10);
    	return daysToHoldOrderError.getText();
    }
    
    public String getDaysToHoldOrderErrorForStatistical() {
    	ui().waitForCondition(driver, ExpectedConditions.visibilityOf(daysToHoldOrderErrorForStatistical), 10);
    	return daysToHoldOrderErrorForStatistical.getText();
    }
    
    
	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(calculationMatSelect)) {
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
