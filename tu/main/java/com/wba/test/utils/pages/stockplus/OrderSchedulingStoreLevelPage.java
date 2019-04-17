/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.stockplus;

import static com.oneleo.test.automation.core.UIUtils.ui;

import java.util.List;
import java.util.Objects;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

public class OrderSchedulingStoreLevelPage extends LoadableComponent<OrderSchedulingStoreLevelPage> {

	@FindBy(xpath = "//*[@id='rx-corporate-user-app-main-container']/dashboard-view/div/div/div[2]/rx-order-schedule/div/div/rx-global/div/rx-calendar/div/div/rx-calendar-event-manager/div/mat-card[1]/mat-card-content/div/span")
	WebElement storeInfo;
	@FindBy(xpath = "//*[@id='calendar-content-month']/div[3]/div[6]/div[2]/div[1]")
	WebElement temporaryShiftItem;
	@FindBy(xpath = "//*[@id='popover-event']/div[2]/div[2]/button[2]/span")
	WebElement buttonCancel;
	@FindBy(xpath = "//*[@id='popover-event']/div[2]/div[2]/button[1]/span")
	WebElement buttonUpdate;
	@FindBy(xpath = "//*[@id='popover-event']/div[2]/div[1]")
	WebElement message;
	@FindBy(xpath = "//*[@id='popover-event']/div[1]/div")
	WebElement title;
	@FindBy(xpath = "//*[@id='mat-radio-3']/label/div[1]/div[1]")
	WebElement twoWeek;
	@FindBy(xpath = "//*[@id='dialog-header-close-button']/span/mat-icon")
	WebElement closeButton;
	@FindBy(xpath = "//*[@id='from-input']")
	WebElement inputStartDate;
	@FindBy(xpath = "//*[@id='to-input']")
	WebElement inputEndDate;

	@FindBy(xpath = "//*[@id='orderGenerationTime-week1Data']")
	WebElement order;
	@FindBy(xpath = "//*[@id='orderGenerationTime-week2Data']")
	WebElement order2;
	@FindBy(xpath = "//*[@id='orderGenerationTime-week1Data']")
	WebElement weeekData;
	@FindBy(xpath = "//*[@id='orderGenerationTime-week2Data']")
	WebElement weeekData2;
	@FindBy(xpath = "//*[@id='mat-select-1']/div/div[1]/span/span")
	WebElement firstDropDownList;
	@FindBy(xpath = "//*[@id='mat-select-3']/div/div[1]/span/span")
	WebElement firstDropDownList2;
	@FindBy(xpath = "//*[@id='mat-select-1']/div/div[1]/span/span")
	WebElement secondDropDownList;
	@FindBy(xpath = "//*[@id='mat-select-3']/div/div[1]/span/span")
	WebElement secondDropDownList2;
	@FindBy(xpath = "//*[@id='cutOffTime-week1Data']")
	WebElement cutOffTime;
	@FindBy(xpath = "//*[@id='cutOffTime-week2Data']")
	WebElement cutOffTime2;
	@FindBy(xpath = "//*[@id='cdk-overlay-3']/mat-dialog-container/edit-schedule/form/mat-dialog-actions/button[1]")
	WebElement saveButton;
	@FindBy(xpath = "//*[@id='calendar-content-month']/div[5]/div[1]/div[2]/div")
	WebElement shiftItem;
	@FindBy(xpath = "//*[@id='week1Data']/div/div[1]")
	WebElement orderDayMonday;
	@FindBy(id = "mat-error-1")
	WebElement errorMessage;
	@FindBy(id = "mat-error-2")
	WebElement errorMessage2;

	WebDriver driver;

	public OrderSchedulingStoreLevelPage(WebDriver driver) {
		this.driver = driver;
	}

	/*
	 * Content in web elements
	 */
	public void clickItemPopOver() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(temporaryShiftItem), 20);
		temporaryShiftItem.click();
	}

	public boolean checkPopOverMessage(String s) {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(buttonCancel), 20);
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(buttonUpdate), 20);
		return (buttonCancel.isDisplayed() && buttonUpdate.isDisplayed() && title.isDisplayed()
				&& message.getText().trim().equals(s));
	}

	public void clickUpdatePopOver() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(buttonUpdate), 20);
		buttonUpdate.click();
	}

	public void changeStartDate(String s) {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(inputStartDate), 20);
		inputStartDate.clear();
		if (!s.isEmpty())
			inputStartDate.sendKeys(s);
	}

	public void changeEndDate(String s) {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(inputEndDate), 20);
		inputEndDate.clear();
		if (!s.isEmpty())
			inputEndDate.sendKeys(s);
	}

	public void clickCloseButton() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(closeButton), 20);
		closeButton.click();
	}

	public boolean checkMessageError(String errorType, String errorMessageText) throws Throwable {

		if (errorType.equals("StartDate")) {
			ui().waitForCondition(driver, ExpectedConditions.visibilityOf(errorMessage), 50);
			return errorMessage.getText().equals(errorMessageText);
		} else if (errorType.equals("EndDate")) {
			ui().waitForCondition(driver, ExpectedConditions.visibilityOf(errorMessage2), 50);
			return errorMessage2.getText().equals(errorMessageText);
		}
		return false;
	}

	public boolean checkMessageNotError(String errorType, String errorMessageText) throws Throwable {

		if (errorType.equals("StartDate")) {
			try {
				ui().waitForCondition(driver, ExpectedConditions.visibilityOf(errorMessage));
			} catch (Exception e) {
				return true;
			}

		} else if (errorType.equals("EndDate")) {
			try {
				ui().waitForCondition(driver, ExpectedConditions.visibilityOf(errorMessage2));
			} catch (Exception e) {
				return true;
			}
		}
		return false;
	}

	public void clickTwoWeek() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(twoWeek), 20);
		twoWeek.click();
	}

	public boolean checkTwoWeek() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(order), 20);
		return order.getAttribute("ng-reflect-model").equals(order2.getAttribute("ng-reflect-model"))
				&& weeekData.getText().equals(weeekData2.getText())
				&& firstDropDownList.getText().equals(firstDropDownList2.getText())
				&& secondDropDownList.getText().equals(secondDropDownList2.getText())
				&& cutOffTime.getAttribute("ng-reflect-model").equals(cutOffTime2.getAttribute("ng-reflect-model"));

	}

	public void insertOrderGeneration(String orderGeneration) {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(order), 20);
		order.sendKeys(orderGeneration);
	}

	public void insertCutOffTime(String cutOffTimeItem) {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(cutOffTime), 20);
		cutOffTime.sendKeys(cutOffTimeItem);
	}

	public boolean checkCutOffTime(String cutOffTimeItem) {

		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(cutOffTime), 20);

		cutOffTime.getAttribute("ng-reflect-model");
		return cutOffTime.getAttribute("ng-reflect-model").equals(cutOffTimeItem);
	}

	public void clickSaveButton() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(saveButton), 20);
		saveButton.click();
	}

	public void clickshiftItem() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(shiftItem), 20);
		shiftItem.click();
	}

	public boolean checkItemShift(String s) {
		WebElement titleItem = driver.findElement(By.xpath("//*[@id='popover-event']/div[1]/div"));
		titleItem.getText();
		return titleItem.getText().equals(s);
	}

	public boolean checkPopOver() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(saveButton), 20);
		return (order.isDisplayed() && weeekData.isDisplayed() && closeButton.isDisplayed() && saveButton.isDisplayed()
				&& inputStartDate.isDisplayed() && inputEndDate.isDisplayed() && buttonUpdate.isDisplayed()
				&& cutOffTime.isDisplayed() && firstDropDownList.isDisplayed() && secondDropDownList.isDisplayed());
	}

	@Override
	protected void load() {
		PageFactory.initElements(driver, this);
	}

	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(storeInfo)) {
			throw new Error();
		}
		try {
		} catch (NoSuchElementException e) {
			Assert.fail("Page is not loaded!");
		}
	}

}
