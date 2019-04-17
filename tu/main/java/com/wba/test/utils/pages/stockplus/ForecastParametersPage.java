/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.stockplus;

import static com.oneleo.test.automation.core.UIUtils.ui;

import java.util.Objects;

import org.junit.Assert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

public class ForecastParametersPage extends LoadableComponent<ForecastParametersPage>{

	private WebDriver driver;
	
	@FindBy(id="rx-forecast-error-period")
	private WebElement forecastErrorAdjustement;
	@FindBy(id="rx-forecast-parameters-template-manager-create-button")
	private WebElement createNewTemplateButton;
	@FindBy(id="rx-forecast-parameter-new-template-input")
	private WebElement inputTemplateName;
	@FindBy(id="rx-forecast-new-template-dialog-save-button")
	private WebElement saveNewTemplateButton;
	@FindBy(css="#mat-error-5")
	private WebElement errorMessageForecastAdjustment;
	
	@FindBy(id = "rx-forecast-parameters-template-manager-create-button")
	private WebElement createTemplateButton;
	@FindBy(id = "rx-forecast-parameter-new-template-input")
	private WebElement textTemplateName;
	@FindBy(id = "rx-forecast-new-template-dialog-save-button")
	private WebElement saveButton;
	@FindBy(id = "rx-forecast-parameter-new-template-input")
	private WebElement newTemplateManagerInput;
	@FindBy(id = "rx-forecast-parameters-template-manager-select")
	private WebElement templateManagerSelect;
	@FindBy(id = "rx-autocomplete-chip-input")
	private WebElement searchFieldinput;
	@FindBy(id = "rx-forecast-parameters-template-manager-start-date")
	private WebElement dataPicker;
	@FindBy(id = "rx-standard-parameter")
	private WebElement standardParameter;	
	
	
	public ForecastParametersPage(WebDriver driver) {
		this.driver = driver;
	}
	
	/*
	 * Buttons
	 */
	public void clickCreateNewTemplateButton() throws InterruptedException {
		synchronized (this) {
			wait(50000);
		}
		createNewTemplateButton.click();
	}
	
	public void clickSaveNewTemplateButton() {
		saveNewTemplateButton.click();
	}
	
	public void clickCreateTemplateButton() {
		createTemplateButton.click();
	}
	
	public void clickSaveButton() {
		saveButton.click();
	}
	
	
	
	/*
	 * Text handle
	 */
	public String getForecastErrorAdjustement() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.forecastErrorAdjustement), 20);
		return forecastErrorAdjustement.getText();
	}
	
	public void insertForecastErrorAdjustement(String value) throws InterruptedException {
		synchronized (this) {
			wait(50000);
		}
		forecastErrorAdjustement.clear();
		forecastErrorAdjustement.sendKeys(value);
	}
	
	public void insertTemplateName(String name) {
		inputTemplateName.sendKeys(name);
	}
	
	public String getTemplateManagerSelectHeader()
	{
		return templateManagerSelect.getAttribute("aria-label");
	}

	
	public Boolean isDisplayedSearchFieldinput()
	{
		return searchFieldinput.isDisplayed();
	}

	public Boolean isDisplayedDataPicker()
	{
		return dataPicker.isDisplayed();
	}
	
	
	public Boolean isDisplayedbStandardParameter()
	{
		return standardParameter.isDisplayed();
	}
	
	/*
	 * Hot Keys
	 */
	public void tabFromForecastErrorAdjustement() {
		forecastErrorAdjustement.sendKeys(Keys.TAB);
	}
	
	/*
	 * Errors
	 */
	public String getErrorMessageForecastAdjustment() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.errorMessageForecastAdjustment), 15);
		return errorMessageForecastAdjustment.getText();
	}
	
	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(forecastErrorAdjustement)) {
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
