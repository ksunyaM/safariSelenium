/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.stockplus;

import static com.oneleo.test.automation.core.UIUtils.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

public class UpdateEventPopup extends LoadableComponent<UpdateEventPopup> {

	@FindBy(id = "dialog-header-title")
	private WebElement titleDialog;

	@FindBy(xpath = "//*[@id='cdk-overlay-1']/mat-dialog-container/rx-calendar-add-update-dialog/form/mat-dialog-actions/button[1]")
	private WebElement saveButton;
	@FindBy(xpath = "//*[@id='cdk-overlay-1']/mat-dialog-container/rx-calendar-add-update-dialog/form/mat-dialog-actions/button[2]")
	private WebElement removeButton;
	@FindBy(xpath = "//*[@id='cdk-overlay-1']/mat-dialog-container/rx-calendar-add-update-dialog/form/mat-dialog-actions/button[3]")
	private WebElement cancelButton;
	@FindBy(id = "from-input")
	private WebElement fromDate;
	@FindBy(id = "to-input")
	private WebElement toDate;
	@FindBy(id = "mat-input-4")
	private WebElement eventTitle;
	@FindBy(id = "dialog-header-close-button")
	private WebElement xButton;
	@FindBy(id = "cdk-overlay-4")
	private WebElement dialog;

	@FindBy(xpath = "//*[@id='cdk-overlay-1']/mat-dialog-container/rx-calendar-add-update-dialog/form/mat-dialog-content/div[1]/mat-form-field[2]/div/div[1]/div[2]/mat-datepicker-toggle/button")
	private WebElement fromDatePicker;
	@FindBy(xpath = "//*[@id='cdk-overlay-1']/mat-dialog-container/rx-calendar-add-update-dialog/form/mat-dialog-content/div[1]/mat-form-field[2]/div/div[1]/div[2]/mat-datepicker-toggle/button")
	private WebElement toDatePicker;
	
	@FindBy(id="order-search-table")
	private WebElement orderSearchTable;
	@FindBy(css="#order-search-table > mat-row:nth-child(2) > mat-cell.mat-cell.cdk-column-name.mat-column-name > div:nth-child(1) > span.badge.primary")
	private WebElement defaultLabel;

	public void isPresentElement(String componentName) {
		if (componentName.equals("'From' date field "))
			Assert.assertThat("check " + componentName, !Objects.isNull(fromDate), Matchers.equalTo(Boolean.TRUE));
		else if (componentName.equals("'To' date field "))
			Assert.assertThat("check " + componentName, !Objects.isNull(toDate), Matchers.equalTo(Boolean.TRUE));
		else if (componentName.equals("'Event title' text box"))
			Assert.assertThat("check " + componentName, !Objects.isNull(eventTitle), Matchers.equalTo(Boolean.TRUE));
		else if (componentName.equals("Save button"))
			Assert.assertThat("check " + componentName, !Objects.isNull(saveButton), Matchers.equalTo(Boolean.TRUE));
		else if (componentName.equals("Remove button"))
			Assert.assertThat("check " + componentName, !Objects.isNull(removeButton), Matchers.equalTo(Boolean.TRUE));
		else if (componentName.equals("Cancel button"))
			Assert.assertThat("check " + componentName, !Objects.isNull(cancelButton), Matchers.equalTo(Boolean.TRUE));
		else if (componentName.equals("X"))
			Assert.assertThat("check " + componentName, !Objects.isNull(xButton), Matchers.equalTo(Boolean.TRUE));
		else if (componentName.equals("Date Pickers"))
			Assert.assertThat("check " + componentName,
					!Objects.isNull(fromDatePicker) && !Objects.isNull(toDatePicker), Matchers.equalTo(Boolean.TRUE));
	}

	WebDriver driver;

	public UpdateEventPopup(WebDriver driver) {
		this.driver = driver;
	}

	public Date getFromDate() throws ParseException {
		String dateStr = fromDate.getAttribute("ng-reflect-model");
		dateStr = dateStr.substring(4, dateStr.length() - 15);
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd yyyy", Locale.ENGLISH);
		Date date = sdf.parse(dateStr);
		return date;
	}

	public Date getToDate() throws ParseException {
		String dateStr = toDate.getAttribute("ng-reflect-model");
		dateStr = dateStr.substring(4, dateStr.length() - 15);
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd yyyy", Locale.ENGLISH);
		Date date = sdf.parse(dateStr);
		return date;
	}
	
	public List<WebElement> getSupplierList() {
		return orderSearchTable.findElements(By.className("mat-row"));
	}
	
	public List<WebElement> getSupplierListName() {
		return orderSearchTable.findElements(By.cssSelector("mat-row > mat-cell > div > span"));
	}
	
	public List<WebElement> getTemporarySchedule() {
		List<WebElement> suppliers = orderSearchTable.findElements(By.cssSelector("mat-cell.mat-cell.cdk-column-name.mat-column-name"));
		List<WebElement> temporarySupplier = new ArrayList<>();
		for(WebElement supplier : suppliers) {
			List<WebElement> tempSuppliers = supplier.findElements(By.className("row-spacing"));
			temporarySupplier.addAll(tempSuppliers);
		}
		
		return temporarySupplier;
	}

	public boolean isFromDatePickerEnabled() {
		return Objects.nonNull(fromDatePicker.getAttribute("disabled"));
	}

	/**
	 * sends the dates keys
	 * 
	 * @param date
	 *            the date to send
	 * @param type
	 *            from or To date
	 */
	public void modifyDate(String date, String type) {
		if (isFromDatePickerEnabled() && type.equals("from")) {
			fromDate.clear();
			fromDate.sendKeys(date);
		} else {
			toDate.clear();
			toDate.sendKeys(date);
		}
		eventTitle.click();
	}

	public void modifyTitle(String title) {
		eventTitle.clear();
		eventTitle.sendKeys(title);
	}

	public List<String> getValidationErrors() {
		List<String> rv = new ArrayList();
		List<WebElement> l = this.driver.findElements(By.tagName("mat-error"));
		l.forEach(e -> rv.add(e.getText()));

		return rv;
	}
	
	public String getSupplierOnTitle() {
		return titleDialog.getText();
	}

	public void clickOnCancel() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(cancelButton));
		cancelButton.click();
	}

	public void clickOnSave() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(saveButton));
		saveButton.click();
	}

	public Boolean isTitleLoaded() {
		boolean isTitleLoaded = false;
		try {
			isTitleLoaded = titleDialog.isDisplayed();
		} catch (Exception e) {
		}
		return isTitleLoaded;
	}

	public Boolean isDialogDisplayed() {
		boolean isDisplayed = false;
		try {
			ui().waitForCondition(driver, ExpectedConditions.invisibilityOfElementLocated(By.id("cdk-overlay-4")));
			isDisplayed = dialog.isDisplayed();
		} catch (Exception e) {
		}
		return isDisplayed;
	}

	public void removeEvent() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(removeButton));
		removeButton.click();
	}
	
	public Boolean areCheckBoxesDisplayed() {
		WebElement checkBox;
		Boolean displayed = true;
		List<WebElement> tempSuppliers = orderSearchTable.findElements(By.className("mat-row"));
		for(WebElement tempSupplier : tempSuppliers) {
			checkBox = tempSupplier.findElement(By.cssSelector("mat-cell > mat-radio-button"));
			if(!checkBox.isDisplayed()) {
				displayed = false;
				break;
			}
		}
		
		return displayed;
	}
	
	public Boolean isDefaultLabelDisplayed() {
		return defaultLabel.isDisplayed();
	}
	
	public Boolean isTempLabelDisplayed() {
		WebElement tempLabel;
		Boolean displayed = true;
		List<WebElement> tempSuppliers = orderSearchTable.findElements(By.className("mat-row"));
		for(WebElement tempSupplier : tempSuppliers) {
			tempLabel = tempSupplier.findElement(By.cssSelector("mat-cell.mat-cell.cdk-column-name.mat-column-name > div > span"));
			if(!tempLabel.isDisplayed()) {
				displayed = false;
				break;
			}
		}
		
		return displayed;
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
