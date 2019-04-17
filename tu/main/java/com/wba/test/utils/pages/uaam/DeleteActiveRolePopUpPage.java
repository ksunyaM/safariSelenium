/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.uaam;

import static com.oneleo.test.automation.core.UIUtils.ui;

import java.util.Objects;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

public class DeleteActiveRolePopUpPage extends LoadableComponent<DeleteActiveRolePopUpPage>  {
	
	@FindBy(xpath = "//*[@id=\"cdk-overlay-9\"]/mat-dialog-container")
	private WebElement deletionpopup;
	@FindBy(xpath = "//*[@id=\"mat-input-1\"]")
	private WebElement scheduleddeletiondate;
	@FindBy(xpath = "//*[@id=\"cdk-overlay-0\"]/mat-dialog-container/app-date-picker-dialog/h3")
	private WebElement topinfomessage;
	@FindBy (xpath = "//*[@id=\"cdk-overlay-0\"]/mat-dialog-container/app-delete-dialog/form/mat-form-field/div/div[1]/div[2]/mat-datepicker-toggle/button")
	private WebElement deletiondatepicker;
	@FindBy (xpath = "//*[@id=\"cdk-overlay-0\"]/mat-dialog-container/app-date-picker-dialog/div/button[1]")
	private WebElement cancelbutton;
	@FindBy (xpath = "//*[@id=\"cdk-overlay-0\"]/mat-dialog-container/app-date-picker-dialog/div/button[2]")
	private WebElement savebutton;
	@FindBy (xpath = "//*[@id=\"cdk-overlay-0\"]/mat-dialog-container/app-date-picker-dialog/form/mat-form-field/div/div[1]/div[1]/span/label")
	private WebElement scheduleddeletiondateplaceholder;
	
	private WebDriver driver;

	public DeleteActiveRolePopUpPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void waitForPopUpToLoad() {	
		synchronized (this) {
			try {
				wait(5000);
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}
		}		
	}	
	
	public String contentOfTopPopUpMessage() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(topinfomessage));
		return topinfomessage.getText();
	}
	
	public boolean isScheduledDeletionDatePlaceholderDisplayed() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(scheduleddeletiondateplaceholder));
		return scheduleddeletiondateplaceholder.isDisplayed();
	}
	
	public String contentOfScheduledDeletionDatePlaceholderDisplayed() {
		return scheduleddeletiondateplaceholder.getText();
	}
	
	public void insertScheduledDeletionDate(String scheduledDeletionDate) {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(scheduleddeletiondate));
		scheduleddeletiondate.clear();
		scheduleddeletiondate.sendKeys(scheduledDeletionDate);
		scheduleddeletiondate.sendKeys(Keys.TAB);
	}
	
	public boolean isCancelButtonDisplayed() {
		return cancelbutton.isDisplayed();
	}
	
	public RolesAndPermissionsPage clickCancelButton() {
		cancelbutton.click();
		return new RolesAndPermissionsPage(driver);
	}
	
	public boolean isSaveButtonDisplayed() {
		return savebutton.isDisplayed();
	}
	
	public RolesBeingDeletedTabPage clickSaveButton() {
		savebutton.click();
		return new RolesBeingDeletedTabPage(driver);
	}
	
	public void clickOnDeletionDatePicker() {
		deletiondatepicker.click();
	}
	
	public boolean isInlineErrorMessageForDateDisplayed() {		
		try {
			WebElement errormessage = driver.findElement(By.xpath("//*[@id=\"mat-error-1\"]"));		
			errormessage.isDisplayed();
			return true;
		}
		catch (NoSuchElementException e) {
			return false;
		}
		catch (StaleElementReferenceException f) {
			return false;
		}
	}
	
	public String getErrorMessageForDateContent() {
		WebElement errormessage = driver.findElement(By.xpath("//*[@id=\"mat-error-0\"]"));	     
	    return errormessage.getText();
	}
	
	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(scheduleddeletiondate)) {
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
