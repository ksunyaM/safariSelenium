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
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

public class CreateTemporaryScheduleBox extends LoadableComponent<CreateTemporaryScheduleBox> {

	WebDriver driver;

	@FindBy(css = "#cdk-overlay-1 > mat-dialog-container > edit-schedule > form > mat-dialog-content > div.name-container > strong")
	private WebElement scheduleName;
	@FindBy(id = "mat-input-8")
	private WebElement scheduleNameToModify;
	@FindBy(css = "#cdk-overlay-1 > mat-dialog-container > edit-schedule > form > mat-dialog-content > div.name-container > button")
	private WebElement pencilButton;
	@FindBy(css = "#from-input")
	private WebElement fromDateInput;
	@FindBy(css = "#to-input")
	private WebElement toDateInput;
	@FindBy(css = "#mat-error-1")
	private WebElement errorMessage;
	@FindBy(css = "#mat-error-2")
	private WebElement errorMessageEndDate;
	@FindBy(css = "#mat-radio-3 > label > div.mat-radio-container")
	private WebElement radioButtonWeekTwo;
	@FindBy(css = "#cdk-overlay-2 > mat-dialog-container > edit-schedule > form > mat-dialog-content > b:nth-child(10)")
	private WebElement weekTwoLabel;

	@FindBy(id = "orderGeneration-week2Data")
	private WebElement orderGenerationWeekTwo;
	@FindBy(id = "mat-select-1")
	private WebElement matSelectOrderOrigin;
	@FindBy(id = "mat-select-3")
	private WebElement matSelectReleaseWeekTwo;
	@FindBy(id = "orderCutoff-week2Data")
	private WebElement matSelectOrderCutoffWeekTwo;

	@FindBy(id = "orderGeneration-week1Data")
	private WebElement orderGenerationInputWeekOne;
	@FindBy(id = "mat-select-3")
	private WebElement orderOriginWeekOne;
	@FindBy(id = "mat-select-1")
	private WebElement matSelectReleaseWeekOne;
	@FindBy(id = "orderCutoff-week1Data")
	private WebElement matSelectOrderCutoffWeekOne;

	@FindBy(css = "#cdk-overlay-1 > mat-dialog-container > edit-schedule > form > mat-dialog-content > div.dates-container > mat-form-field.order-dates.mat-input-container.mat-form-field.ng-tns-c6-5.mat-form-field-type-mat-input.mat-form-field-can-float.mat-form-field-should-float.mat-primary.ng-untouched.ng-pristine.ng-valid > div > div.mat-input-flex.mat-form-field-flex > div.mat-input-suffix.mat-form-field-suffix.ng-tns-c6-5 > mat-datepicker-toggle > button > span > mat-icon > svg")
	private WebElement calendarButtonStartDate;
	@FindBy(css = "#cdk-overlay-1 > mat-dialog-container > edit-schedule > form > mat-dialog-content > div.dates-container > mat-form-field.order-dates.mat-input-container.mat-form-field.ng-tns-c6-6.mat-form-field-type-mat-input.mat-form-field-can-float.mat-form-field-should-float.mat-primary.ng-untouched.ng-pristine.ng-valid > div > div.mat-input-flex.mat-form-field-flex > div.mat-input-suffix.mat-form-field-suffix.ng-tns-c6-6 > mat-datepicker-toggle > button > span > mat-icon > svg")
	private WebElement calendarButtonToDate;
	@FindBy(css = "#mat-datepicker-0")
	private WebElement datePickerStartDate;
	@FindBy(css = "#mat-datepicker-1")
	private WebElement datePickerToDate;
	@FindBy(css = "#week1Data > div > div:nth-child(1)")
	private WebElement orderDaySunday;
	@FindBy(xpath = "//*[@id='cdk-overlay-2']/mat-dialog-container/edit-schedule/form/mat-dialog-actions/button[1]")
	private WebElement saveButton;
	@FindBy(xpath = "//*[@id='popover-event']/div[2]/div[2]/button[1]/span")
	WebElement buttonUpdate;
	@FindBy(xpath = "//*[@id='calendar-content-month']/div[3]/div[6]/div[2]/div[1]")
	WebElement temporaryShiftItem;
	public CreateTemporaryScheduleBox(WebDriver driver) {
		this.driver = driver;
	}

	/*
	 * text handle
	 */
	public String getScheduleName() {
		return scheduleName.getText();
	}

	public void insertScheduleName(String name) {
		scheduleNameToModify.clear();
		scheduleNameToModify.sendKeys(name);
	}

	public String getErrorMessage() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.errorMessage), 20);
		return errorMessage.getText();
	}

	public String getErrorMessageEndDate() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.errorMessage), 20);
		return errorMessageEndDate.getText();
	}

	public void insertStartDate(String date) {
		fromDateInput.clear();
		fromDateInput.sendKeys(date);
	}

	public void insertToDateInput(String date) {
		toDateInput.clear();
		toDateInput.sendKeys(date);
	}

	public void insertOrderGenerationWeekOne(String time) {
		orderGenerationInputWeekOne.sendKeys(time);
	}

	public void insertMatSelectOrderCutoffWeekOne(String time) {
		matSelectOrderCutoffWeekOne.sendKeys(time);
	}

	public String getStartDate() {
		return fromDateInput.getAttribute("ng-reflect-model");
	}

	public String getToDate() {
		return toDateInput.getAttribute("ng-reflect-model");
	}

	public String getWeekTwoLabel() {
		return weekTwoLabel.getText();
	}

	public String getOrderGenerationInputWeekOne() {
		return orderGenerationInputWeekOne.getAttribute("ng-reflect-model");
	}

	public String getOrderOriginWeekOne() {
		return orderOriginWeekOne.getText();
	}

	public String getMatSelectReleaseWeekOne() {
		return matSelectReleaseWeekOne.getText();
	}

	public String getMatSelectOrderCutoffWeekOne() {
		return matSelectOrderCutoffWeekOne.getAttribute("ng-reflect-model");
	}

	public String getOrderGenerationWeekTwo() {
		return orderGenerationWeekTwo.getAttribute("ng-reflect-model");
	}

	public String getMatSelectOrderGeneration() {
		return matSelectOrderOrigin.getText();
	}

	public String getMatSelectReleaseWeekTwo() {
		return matSelectReleaseWeekTwo.getText();
	}

	public String getMatSelectOrderCutoffWeekTwo() {
		return matSelectOrderCutoffWeekTwo.getAttribute("ng-reflect-model");
	}

	/*
	 * Is displayed
	 */
	public Boolean isScheduleNameDisplayed() {
		return scheduleName.isDisplayed();
	}

	public Boolean isFromDateInputDisplayed() {
		return fromDateInput.isDisplayed();
	}

	public Boolean isToDateInputDIsplayed() {
		return toDateInput.isDisplayed();
	}

	public Boolean isWeekTwoLabelDisplayed() {
		return weekTwoLabel.isDisplayed();
	}

	public Boolean isOrderGenerationWeekTwo() {
		return orderGenerationWeekTwo.isDisplayed();
	}

	public Boolean isMatSelectOrderGenerationDisplayed() {
		return matSelectOrderOrigin.isDisplayed();
	}

	public Boolean isMatSelectReleaseDisplayed() {
		return matSelectReleaseWeekTwo.isDisplayed();
	}

	public Boolean ismatSelectOrderCutoffDisplayed() {
		return matSelectOrderCutoffWeekTwo.isDisplayed();
	}

	public Boolean isDatePickerStartDateDisplayed() {
		return datePickerStartDate.isDisplayed();
	}

	public Boolean isDatePickerToDateDisplayed() {
		return datePickerToDate.isDisplayed();
	}

	/*
	 * Clicks
	 */
	public void pencilButtonClick() {
		pencilButton.click();
	}

	public void radioButtonWeekTwoClick() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(this.radioButtonWeekTwo), 20);
		radioButtonWeekTwo.click();
	}

	public void calendarStartDateClick() {
		calendarButtonStartDate.click();
	}

	public void calendarToDateClick() {
		calendarButtonToDate.click();
	}

	public void orderDaySundayClick() {
		orderDaySunday.click();
	}

	public void saveButtonClick() {
		saveButton.click();
	}

	public void clickItemPopOver() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(temporaryShiftItem), 20);
		temporaryShiftItem.click();
	}

	public void clickUpdatePopOver() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(buttonUpdate), 20);
		buttonUpdate.click();
	}

	public void clickBackButton() {
		WebElement backButton = driver.findElement(By.xpath("//*[@id='rx-calendar-view-left-button']/span/mat-icon"));
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(backButton), 20);
		backButton.click();
	}
	public void clickDeleteButton() {
		WebElement deleteButton = driver.findElement(By.xpath("//*[@id='cdk-overlay-10']/mat-dialog-container/edit-schedule/form/mat-dialog-actions/button[3]/div[1]"));
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(deleteButton), 20);
		deleteButton.click();
	}
	

	/*
	 * Hot keys
	 */
	public void leaveFromStartDate() {
		fromDateInput.sendKeys(Keys.TAB);
	}

	public void leaveFromToDate() {
		toDateInput.sendKeys(Keys.TAB);
	}

	public void leaveFromMatSelectOrderCutoffWeekOne() {
		matSelectOrderCutoffWeekOne.sendKeys(Keys.TAB);
	}

	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(scheduleName)) {
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
