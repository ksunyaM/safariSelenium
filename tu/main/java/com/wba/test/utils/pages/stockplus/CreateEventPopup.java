/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.stockplus;

import java.util.Objects;
import static com.oneleo.test.automation.core.UIUtils.ui;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

public class CreateEventPopup extends LoadableComponent<CreateEventPopup>{

	@FindBy(id="dialog-header-title")
	private WebElement titleDialog;
	@FindBy(css="#cdk-overlay-0 > mat-dialog-container > rx-calendar-add-update-dialog > form > mat-dialog-content > div:nth-child(1) > mat-form-field.mat-input-container.mat-form-field.ng-tns-c6-4.mat-form-field-type-mat-input.mat-form-field-can-float.mat-form-field-should-float.mat-primary.ng-untouched.ng-pristine.ng-invalid")
	private WebElement fromDateField;
	@FindBy(css="#cdk-overlay-0 > mat-dialog-container > rx-calendar-add-update-dialog > form > mat-dialog-content > div:nth-child(1) > mat-form-field.mat-input-container.mat-form-field.ng-tns-c6-5.mat-form-field-type-mat-input.mat-form-field-can-float.mat-form-field-should-float.mat-primary.ng-untouched.ng-pristine.ng-invalid")
	private WebElement toDateField;
	@FindBy(id="event-title")
	private WebElement eventTitle;
	@FindBy(xpath="//div[contains(@id,'cdk-overlay')]/mat-dialog-container/rx-calendar-add-update-dialog/form/mat-dialog-actions/button[1]")
	private WebElement saveButton;
	@FindBy(xpath="//div[contains(@id,'cdk-overlay')]/mat-dialog-container/rx-calendar-add-update-dialog/form/mat-dialog-actions/button[2]")
	private WebElement cancelButton;
	@FindBy(css="#dialog-header-close-button")
	private WebElement closeButton;
	@FindBy(css="#from-input")
	private WebElement fromLabel;
	@FindBy(css="#to-input")
	private WebElement toLabel;
	@FindBy(css="#event-title > div > div.mat-input-flex.mat-form-field-flex > div > input")
	private WebElement eventNameLabel;
	@FindBy(xpath="//div[contains(@id,'cdk-overlay')]/mat-dialog-container/rx-calendar-add-update-dialog/form/mat-dialog-content/div[1]/mat-form-field[1]/div/div[3]")
	private WebElement errorMessageStartDate;
	
	private WebDriver driver;

	public CreateEventPopup(WebDriver driver) {
		this.driver = driver;
	}
	
	public OrderSchedulingMngSearchSuppliersPage closeButtonClick() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(closeButton), 10);
		this.closeButton.click();
		return new OrderSchedulingMngSearchSuppliersPage(driver);
	}
	
	public Boolean isTitleLoaded(){
		return titleDialog.isDisplayed();
	}

	public Boolean isFromLabelDisplayed(){
		return fromLabel.isDisplayed();
	}
	
	public Boolean isToLabelDisplayed(){
		return toLabel.isDisplayed();
	}
	
	public Boolean isEventNameLabelDisplayed(){
		return eventNameLabel.isDisplayed();
	}
	
	public Boolean isSaveButtonDisplayed(){
		return saveButton.isDisplayed();
	}
	
	public Boolean isErrorMessageStartDateDisplayed(){
		return errorMessageStartDate.isDisplayed();
	}
	
	public Boolean isCancelButtonDisplayed(){
		return cancelButton.isDisplayed();
	}
	
	public void insertDates(String fromDate, String toDate) throws Exception {
		 this.fromLabel.clear();
	        if (StringUtils.isNotBlank(fromDate)) {
	        	  synchronized (this) {
		                wait(1000);
		            }
	        	this.fromLabel.sendKeys(fromDate);
	            ui().waitForCondition(driver, ExpectedConditions.textToBePresentInElementValue(this.fromLabel, fromDate));
	        } else {
	            throw new java.lang.Exception("fromDate missing");
	        }
	        this.toLabel.clear();
	        if (StringUtils.isNotBlank(toDate)) {
	            this.toLabel.sendKeys(toDate);
	            synchronized (this) {
	                wait(2000);
	            }
	            ui().waitForCondition(driver, ExpectedConditions.textToBePresentInElementValue(this.toLabel, toDate));
	        } else {
	            throw new java.lang.Exception("toDate missing");
	        }	
	}
	
	public void insertEventName(String eventName) throws Exception {
		 this.eventNameLabel.clear();
	        if (StringUtils.isNotBlank(eventName)) {
	        	  synchronized (this) {
		                wait(1000);
		            }
	        	this.eventNameLabel.sendKeys(eventName);
	            ui().waitForCondition(driver, ExpectedConditions.textToBePresentInElementValue(this.eventNameLabel, eventName));
	        } else {
	            throw new java.lang.Exception("eventName missing");
	        }
	 }
	
	public void clickOnSaveButton() {
		saveButton.click();
	}
	
	public void clickOnCancelButton(){
		cancelButton.click();
	}
	
	public Boolean isFromDateDisplayed() {
		return fromDateField.isDisplayed();
	}
	
	public Boolean isToDateDisplayed() {
		return toDateField.isDisplayed();
	}
	
	public Boolean eventTitleDisplayed() {
		return eventTitle.isDisplayed();
	}
	
	public Boolean saveButtonDisplayed() {
		return saveButton.isDisplayed();
	}
	
	public Boolean cancelButtonDisplayed() {
		return cancelButton.isDisplayed();
	}
	
	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(titleDialog)) {
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
