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
import org.openqa.selenium.support.ui.WebDriverWait;

public class ExceptionCountsPage extends LoadableComponent<ExceptionCountsPage> {

	@FindBy(id = "page-header-title")
	private WebElement pageTitle;
	@FindBy(id = "mat-input-0")
	private WebElement startDateFilter;
	@FindBy(id = "mat-input-1")
	private WebElement endDateFilter;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-view-exception-count > div > div > mat-card > form > button.btn-position.mat-raised-button.mat-secondary")
	private WebElement buttonFilter;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-view-exception-count > div > div > mat-card > form > button.btn-position.mat-raised-button.mat-neutral")
	private WebElement buttonClearFilter;
	@FindBy(css = "#mat-select-0 > div > div.mat-select-arrow-wrapper")
	private WebElement StatusArrow;
	@FindBy(css = "#ec-table > mat-header-row > mat-header-cell.mat-header-cell.ng-tns-c17-4.cdk-column-description.mat-column-description > div > button")
	private WebElement buttonItemDescription;
	@FindBy(css = "#ec-table > mat-header-row > mat-header-cell.table-align-right.header-right-alignment.mat-header-cell.ng-tns-c17-6.cdk-column-creation_date.mat-column-creation_date > div > button")
	private WebElement buttonCreationDate;
	@FindBy(css = "#ec-table > mat-header-row > mat-header-cell.mat-header-cell.ng-tns-c17-7.cdk-column-status.mat-column-status > div > button")
	private WebElement buttonStatus;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-view-exception-count > div > div > mat-card > div.margin-top.margin-bottom > div > span.text-heading")
	private WebElement counter;
	@FindBy(id = "rx-corp-list-button-2")
	private WebElement stockManagementButton;
	@FindBy(css = "#refine-msg > span")
	private WebElement refineMessage;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-view-exception-count > div > div > mat-card > div.margin-top.margin-bottom > div > span.text-heading")
	private WebElement countsFound;
	
	private WebDriver driver;

	public ExceptionCountsPage(WebDriver driver) {
		this.driver = driver;
	}

	public int getMapSize() {
		List<WebElement> rows = driver.findElements(By.cssSelector("[class='mat-row']"));
		return rows.size();
	}	
	
	public String getCreationDate(int i) {
		
		return driver.findElement(By.cssSelector("#ec-table > mat-row:nth-child(" + i + ") > mat-cell.table-align-right.mat-cell.cdk-column-CREATION_DATE.mat-column-CREATION_DATE")).getText();	
		
	}
	
	public String getFirstCreationDate() {
		
		return driver.findElement(By.cssSelector("#ec-table > mat-row.mat-row.selected > mat-cell.table-align-right.mat-cell.cdk-column-CREATION_DATE.mat-column-CREATION_DATE")).getText();	
		
	}
	
	public String getFirstItemDescription() {
		
		return driver.findElement(By.cssSelector("#ec-table > mat-row.mat-row.selected > mat-cell.break-text.padding-right.mat-cell.cdk-column-description.mat-column-description")).getText();	
		
	}
	
	public String getFirstStatus() {
		
		return driver.findElement(By.cssSelector("#ec-table > mat-row.mat-row.selected > mat-cell.mat-cell.cdk-column-status.mat-column-status")).getText();	
		
	}
	
	
	public String getCounter() {
		
		return counter.getText();	
	}
	
	public String getRefineMessage() {
		
		return refineMessage.getText();	
	}
	
	public String getCountsFound() {
		
		return countsFound.getText() + " counts found";	
	}
	
	public String getItemDescription(int i) {
		
		return driver.findElement(By.cssSelector("#ec-table > mat-row:nth-child(" + i + ") > mat-cell.break-text.padding-right.mat-cell.cdk-column-description.mat-column-description")).getText();	
	}
	
	public String getStatus(int i) {
		
		return driver.findElement(By.cssSelector("#ec-table > mat-row:nth-child(" + i + ") > mat-cell.mat-cell.cdk-column-status.mat-column-status")).getText();	
	}
	
	public void setStartDate(String value) {
		
		startDateFilter.clear();
		startDateFilter.click();
		startDateFilter.sendKeys(value); 
	}
	
	public void setEndDate(String value) {
		endDateFilter.clear();
		endDateFilter.click();
		endDateFilter.sendKeys(value); 
	}
	
	public void setStatus(String value) {
		
		StatusArrow.click();
		
		for (int j= 0; j<=3; j++ ) {
			WebElement riga = driver.findElement(By.cssSelector("#mat-option-" + j));
			ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(riga), 20);
			String element = riga.getText();
						
			if (element.equals(value)) {
				riga.click();
				break;
			}	
		}
				
	}
	
	
	public void clickFilterButton()
	{
		buttonFilter.click();
	}
	
	public void clickClearFilterButton()
	{
		buttonClearFilter.click();
	}
	
	public void clickItemDescriptionHeader()
	{
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(buttonItemDescription), 40);
		buttonItemDescription.click();
	}
	
	public void clickCreationDateHeader()
	{
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(buttonCreationDate), 40);
		buttonCreationDate.click();
	}
	
	public void clickStatusHeader()
	{
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(buttonStatus), 40);
		buttonStatus.click();
	}
	
	public void useTab()
	{
		stockManagementButton.sendKeys(Keys.TAB);
	}
	
	
	
	public String getDateErrorMessage()
	{
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[class='mat-error']")));
		List<WebElement> error = driver.findElements(By.cssSelector("[class='mat-error']"));
		return error.get(0).getText();
		
	}
	
	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(pageTitle)) {
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
