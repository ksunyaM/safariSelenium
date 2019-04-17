/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.stockplus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import scala.Array;

public class ManageExclusionListPage extends LoadableComponent<ManageExclusionListPage>{

	@FindBy(id="mat-input-8")
	private WebElement refineByProductInput;
	@FindBy(id="rx-stores-search-button")
	private WebElement searchProductButton;
	@FindBy(id="itemSearchTable")
	private WebElement itemSearchTable;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-manage-exclusion-list > div > div:nth-child(3) > mat-card:nth-child(1) > form > div > mat-toolbar > div > mat-toolbar-row > button")
	private WebElement addExclusionListButton;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-manage-exclusion-list > div > div:nth-child(3) > mat-card:nth-child(2) > div:nth-child(5) > div.scrollTable")
	private WebElement exclusionList;
	@FindBy(id="rx-grid-override-save-button")
	private WebElement saveButton;
	@FindBy(id="rx-grid-override-cancel-button")
	private WebElement cancelButton;
	@FindBy(id="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-manage-exclusion-list > div > div:nth-child(3) > mat-card:nth-child(2) > div:nth-child(5) > div:nth-child(3) > mat-card-actions > button")
	private WebElement removeFromExclusionList;
	@FindBy(id="mat-checkbox-71")
	private WebElement selectAllFromExclusionList;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-manage-exclusion-list > div > div:nth-child(3) > mat-card:nth-child(1) > form > div > div:nth-child(3) > div > b")
	private WebElement numberOfItemFound;
	@FindBy(id="rx-grid-override-search-button")
	private WebElement filterButton;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-manage-exclusion-list > div > mat-card > div:nth-child(2) > div.margin-bottom-9x")
	private WebElement globalTemplateDiv;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-manage-exclusion-list > div > mat-card > div:nth-child(2) > div:nth-child(2)")
	private WebElement levelDiv;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-manage-exclusion-list > div > div:nth-child(3) > mat-card:nth-child(1)")
	private WebElement productSearchMatCard;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-manage-exclusion-list > div > div:nth-child(3) > mat-card:nth-child(2)")
	private WebElement exclusionListMatCard;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-manage-exclusion-list > div > div:nth-child(3) > mat-card:nth-child(2) > div:nth-child(5) > div:nth-child(3) > mat-card-actions > button")
	private WebElement removeFromExclusionListButton;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-manage-exclusion-list > div > div:nth-child(3) > mat-card:nth-child(2) > div:nth-child(5) > div.scrollTable > div.mat-header-row.removed-header-row")
	private WebElement removedLabel;
	
	private WebDriver driver;

	public ManageExclusionListPage(WebDriver driver) {
		this.driver = driver;
	}
	
	/*
	 * Text
	 */
	public void insertRefineByProductInput(String product) {
		refineByProductInput.sendKeys(product);
	}
	
	/*
	 * Displayed
	 */
	public Boolean isRemovedLabelDisplayed() {
		return removedLabel.isDisplayed();
	}
	
	public Boolean isGlobalTemplateDivDisplayed() {
		return globalTemplateDiv.isDisplayed();
	}
	
	public Boolean isLevelDivDisplayed() {
		return levelDiv.isDisplayed();
	}
	
	public Boolean isProductSearchMatCardDisplayed() {
		return productSearchMatCard.isDisplayed();
	}
	
	public Boolean isExclusionListMatCardDisplayed() {
		return exclusionListMatCard.isDisplayed();
	}
	
	public List<Boolean> areColumnOnProductSearchTableDisplayed() {
		List<Boolean> allDisplayed = new ArrayList<>();
		List<WebElement> rows = itemSearchTable.findElements(By.cssSelector("mat-row"));
		for(WebElement row : rows) {
			List<WebElement> columns = row.findElements(By.cssSelector("mat-cell > span"));
			for(WebElement column : columns) {
				allDisplayed.add(column.isDisplayed());
			}
		}
		return allDisplayed;
	}
	
	public Boolean isNumberOfItemFoundDisplayed() {
		return numberOfItemFound.isDisplayed();
	}
	
	public List<Boolean> areItemsFromExclusionListDisplayed() {
		List<Boolean> allDisplayed = new ArrayList<>();
		List<WebElement> rows = exclusionList.findElements(By.cssSelector("mat-row"));
		for(WebElement row : rows) { 
			allDisplayed.add(row.findElement(By.cssSelector("mat-cell")).isDisplayed());
		}
		
		return allDisplayed;
	}
	
	/*
	 * Click
	 */
	public void clickRemoveFromExclusionListButton() {
		removeFromExclusionListButton.click();
	}
	
	public void clickFilterButton() {
		filterButton.click();
	}
	
	public void clickSelectAllFromExclusionList() {
		selectAllFromExclusionList.click();
	}
	
	public void clickSearchProductButton() throws InterruptedException {
		synchronized (this) {
			wait(5000);
		}
		searchProductButton.click();
	}
	
	public void clickAddExclusionListButton() {
		addExclusionListButton.click();
	}
	
	public void clickRemoveFromExclusionList() {
		removeFromExclusionList.click();
	}
	
	public void selectItemesOnProductSearchTable() {
		List<WebElement> checkboxes = itemSearchTable.findElements(By.cssSelector("mat-checkbox"));
		for(int i = 1; i <= 2; i++) {
			checkboxes.get(i).click();
		}
	}
	
	public void selectItemsFromExclusionList() {
		List<WebElement> items = exclusionList.findElements(By.cssSelector("mat-row"));
		for(int i = 1; i <= 2; i++) {
			items.get(i).findElement(By.cssSelector("mat-checkbox")).click();
		}
	}
	
	public void clickSaveButton() {
		saveButton.click();
	}
	
	public void clickCancelButton() {
		cancelButton.click();
	}
	
	
	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(refineByProductInput)) {
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
