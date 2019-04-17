/*Copyright 2018 Walgreen Co.*/
package com.wba.test.utils.pages.stockplus;

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

public class OrderHistoryPage extends LoadableComponent<OrderHistoryPage>{

	private WebDriver driver;
	
	@FindBy(id="page-header-back-button")
	private WebElement backButton;
	
	@FindBy(id="page-header-title")
	private WebElement title;
	
	@FindBy(css="#order-history > div:nth-child(1)")
	private WebElement storeNumberDiv;
	
	@FindBy(css="#order-history > div:nth-child(2)")
	private WebElement itemDescriptionDiv;
	
	@FindBy(css="#order-history > div:nth-child(3)")
	private WebElement plnDiv;
	
	@FindBy(css="#mat-tab-content-1-0 > div > div > mat-dialog-content > rx-order-history-table > mat-table")
	private WebElement orderTable;
	
	@FindBy(id="order-history-dialog-view-button")
	private WebElement viewButton;
	
	public OrderHistoryPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public List<WebElement> getOrdersByTable() {
		return orderTable.findElements(By.cssSelector("mat-row"));
	}
	
	public Boolean backButtonDisplayed() {
		return backButton.isDisplayed();
	}
	
	public Boolean titleDisplayed() {
		return title.isDisplayed();
	}
	
	public Boolean storeNumberDivDisplayed() {
		return storeNumberDiv.isDisplayed();
	}
	
	public Boolean itemDescriptionDivDisplayed() {
		return itemDescriptionDiv.isDisplayed();
	}
	
	public Boolean plnDivDisplayed() {
		return plnDiv.isDisplayed();
	}
	
	public Boolean viewButtonDisplayed() {
		return viewButton.isDisplayed();
	}
	
	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(backButton)) {
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
