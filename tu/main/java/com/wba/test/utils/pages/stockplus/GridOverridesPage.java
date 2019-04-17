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

public class GridOverridesPage extends LoadableComponent<GridOverridesPage> {
	private WebDriver driver;
	@FindBy(xpath = "//*[@id='rx-corporate-user-app-main-container']/dashboard-view/div/div/div[2]/rx-replenishment-parameters/div/div/rx-grid-overrides-parameters/div/div[1]/div[1]/h2")
	private WebElement labelGridOverrides;
	@FindBy(xpath = "//*[@id='rx-corporate-user-app-main-container']/dashboard-view/div/div/div[2]/rx-replenishment-parameters/div/div/rx-create-grid-override/div/div[2]/mat-card/div/div[1]/div[1]/mat-form-field/div/div[1]")
	private WebElement productValue;
	

	public GridOverridesPage(WebDriver driver) {
		this.driver = driver;
	}
	// Click elements

	public void clickCreateTemplate() {
		WebElement buttonCreateTemplate = driver.findElement(By.xpath(
				"//*[@id='rx-corporate-user-app-main-container']/dashboard-view/div/div/div[2]/rx-replenishment-parameters/div/div/rx-grid-overrides-parameters/div/div[1]/div[2]/div[2]/button"));
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(buttonCreateTemplate), 50);
		buttonCreateTemplate.click();

	}

	public void clickSearch(){
		WebElement buttonSearch = driver.findElement(By.xpath("//*[@id='rx-grid-override-search-button']"));
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(buttonSearch), 50);
		buttonSearch.click();
	}
	public void insertcAndClearProductValue() {
		WebElement productValue = driver.findElement(By.xpath("//*[@id='mat-input-4']"));
		productValue.clear();
		productValue.sendKeys("p");
		productValue.sendKeys("\u0008");
	}

	// insert element
	public void insertStartData(String s) {
		WebElement textBoxStartData = driver.findElement(By.id("from-input"));
		textBoxStartData.clear();
		textBoxStartData.sendKeys(s);
	}

	public void insertTemplateName(String s) {
		WebElement textTemplateName = driver.findElement(By.id("mat-input-1"));
		textTemplateName.clear();
		textTemplateName.sendKeys(s);
	}

	public void insertEndDate(String s) {
		WebElement textBoxEndData = driver.findElement(By.id("end-input"));
		textBoxEndData.clear();
		textBoxEndData.sendKeys(s);
	}

	public void emptyStartData() {
		WebElement textBoxStartData = driver.findElement(By.id("from-input"));
		textBoxStartData.clear();
	}

	// verify elements
	public boolean isDefaultTemplateCreation() throws InterruptedException {
		WebElement textBoxStartData = driver.findElement(By.id("from-input"));
		WebElement testBoxEndData = driver.findElement(By.id("end-input"));

		synchronized (this) {
			wait(200);
		}
		return (textBoxStartData.getText().isEmpty() && testBoxEndData.getText().isEmpty());
	}

	public boolean messageErrorStartDate(String s) {
		WebElement templateNameError = driver.findElement(By.id("mat-error-2"));
		return templateNameError.getText().trim().equals(s);
	}

	public boolean messageErrorSearchEmpty(String s) {
		WebElement templateNameError = driver.findElement(By.id("mat-error-3"));
		return templateNameError.getText().trim().equals(s);
	}
	
	public boolean messageErrorProductValue(String s) {
		WebElement templateNameError = driver.findElement(By.id("mat-error-1"));
		return templateNameError.getText().trim().equals(s);
	}

	public boolean messageErrorEndDate(String s) {
		WebElement templateNameError = driver.findElement(By.id("mat-error-4"));
		return templateNameError.getText().trim().equals(s);
	}

	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(labelGridOverrides)) {
			throw new Error();
		}
		try {
			ui().waitForCondition(driver, ExpectedConditions.visibilityOf(labelGridOverrides), 20);
		} catch (NoSuchElementException e) {
			Assert.fail("Page is not loaded!");
		}
	}

	@Override
	protected void load() {
		PageFactory.initElements(driver, this);
	}

}
