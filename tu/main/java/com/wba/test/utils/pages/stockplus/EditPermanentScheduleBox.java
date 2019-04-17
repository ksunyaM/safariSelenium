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

public class EditPermanentScheduleBox extends LoadableComponent<EditPermanentScheduleBox>{

	WebDriver driver;
	
	@FindBy(css="#cdk-overlay-1 > mat-dialog-container > edit-schedule > form > mat-dialog-content > div.name-container > strong")
	private WebElement scheduleName;
	@FindBy(id="mat-input-8")
	private WebElement scheduleNameToModify;
	@FindBy(css="#cdk-overlay-1 > mat-dialog-container > edit-schedule > form > mat-dialog-content > div.name-container > button")
	private WebElement pencilButton;
	@FindBy(css="#from-input")
	private WebElement fromDateInput;
	@FindBy(css="#to-input")
	private WebElement toDateInput;
	@FindBy(css="#mat-error-1")
	private WebElement errorMessage;
	@FindBy(css="#mat-radio-3 > label > div.mat-radio-container")
	private WebElement radioButtonWeekTwo;
	@FindBy(css="#cdk-overlay-2 > mat-dialog-container > edit-schedule > form > mat-dialog-content > b:nth-child(10)")
	private WebElement weekTwoLabel;
	
	@FindBy(id="orderGeneration-week2Data")
	private WebElement orderGenerationWeekTwo;
	@FindBy(id="mat-select-2")
	private WebElement matSelectOrderOrigin;
	@FindBy(id="mat-select-3")
	private WebElement matSelectReleaseWeekTwo;
	@FindBy(id="orderCutoff-week2Data")
	private WebElement matSelectOrderCutoffWeekTwo;

	
	@FindBy(id="orderGeneration-week1Data")
	private WebElement orderGenerationInputWeekOne;
	@FindBy(id="mat-select-0")
	private WebElement orderOriginWeekOne;
	@FindBy(id="mat-select-1")
	private WebElement matSelectReleaseWeekOne;
	@FindBy(id="orderCutoff-week1Data")
	private WebElement matSelectOrderCutoffWeekOne;
	

	public EditPermanentScheduleBox(WebDriver driver) {
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
		return errorMessage.getText();
	}
	
	public void insertStartDate(String date) {
		fromDateInput.clear();
		fromDateInput.sendKeys(date);
	}
	
	public void insertOrderGenerationWeekOne(String time) {
		orderGenerationInputWeekOne.sendKeys(time);
	}
	
	public void insertMatSelectOrderCutoffWeekOne(String time) {
		matSelectOrderCutoffWeekOne.sendKeys(time);
	}
	
	public String getStartDate() {
		return fromDateInput.getText();
	}
	
	public String getToDate() {
		return toDateInput.getText();
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
		return orderGenerationWeekTwo.getText();
	}
	
	public String getMatSelectOrderGeneration() {
		return matSelectOrderOrigin.getText();
	}
	
	public String getMatSelectReleaseWeekTwo() {
		return matSelectReleaseWeekTwo.getText();
	}
	
	public String getMatSelectOrderCutoffWeekTwo() {
		return matSelectOrderCutoffWeekTwo.getText();
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
	

	
	/*
	 * Clicks
	 */
	public void pencilButtonClick() {
		pencilButton.click();
	}
	
	public void radioButtonWeekTwoClick() {
		radioButtonWeekTwo.click();
	}

	
	/*
	 * Hot keys
	 */
	public void leaveFromStartDate() {
		fromDateInput.sendKeys(Keys.TAB);
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
