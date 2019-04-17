/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.stockplus;

import static com.oneleo.test.automation.core.UIUtils.ui;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;

import javax.xml.xpath.XPath;

import org.apache.commons.lang.StringUtils;
import org.codehaus.groovy.transform.EqualsAndHashCodeASTTransformation;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By.ByClassName;
import org.openqa.selenium.By.ByTagName;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

import javolution.io.Struct.Bool;

public class OrderItemsPage extends LoadableComponent<OrderItemsPage> {
	private WebDriver driver;

	@FindBy(css="#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-purchase-order-manual > div > mat-card")
	private WebElement header;

	@FindBy(id = "page-header-title")
	private WebElement labelOrderItems;

	@FindBy(id = "shopping-cart-icon")
	private WebElement cartIcon;

	@FindBy(css = "#page-header-back-button > span > mat-icon")
	private WebElement backIcon;

	@FindBy(xpath = "//*[@id='form-wrapper']/form/mat-form-field/div/div[1]/div")
	private WebElement itemSearch;
	
	@FindBy(id="search-button")
	private WebElement searchButton;
	
	@FindBy(css="#form-wrapper > form > button.mat-raised-button.mat-neutral")
	private WebElement clearButton;
	
	@FindBy(css="#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-purchase-order-manual > div > div > div.purchase-order-manual-scroll.purchase-order-manual-background")
	private WebElement leftPanel;

	@FindBy(css="#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-purchase-order-manual > div > div > div:nth-child(2)")
	private WebElement rightPanel;

	public OrderItemsPage(WebDriver driver) {
		this.driver = driver;
	}

	public boolean existsItemsPage() {

		try {
			ui().waitForCondition(driver, ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//*[@id='form-wrapper']/form/mat-form-field/div/div[1]/div")), 10);
			driver.findElement(By.xpath("//*[@id='form-wrapper']/form/mat-form-field/div/div[1]/div"));
		} catch (Throwable e) {
			return false;
		}
		return true;
	}
	
	/*
	 * displayed
	 */
	public Boolean searchButtonDisplayed() {
		return searchButton.isDisplayed();
	}
	
	public Boolean clearButtonDisplayed() {
		return clearButton.isDisplayed();
	}
	
	public Boolean rightPanelDisplayed() {
		return rightPanel.isDisplayed();
	}
	
	public Boolean leftPanelDisplayed() {
		return leftPanel.isDisplayed();
	}
	
	public Boolean itemSearchDisplayed() {
		return itemSearch.isDisplayed();
	}
	
	public Boolean cartIconDisplayed() {
		return cartIcon.isDisplayed();
	}
	
	public Boolean headerDisplayed() {
		return header.isDisplayed();
	}

	public CartPage navigateToCartPage() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(cartIcon), 20);
		cartIcon.click();
		return new CartPage(driver);
	}

	@Override
	protected void isLoaded() throws Error {

		if (Objects.isNull(labelOrderItems)) {
			throw new Error();
		}
		try {
			// assertThat(signInButton.isDisplayed(), equalTo(Boolean.TRUE));
			ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(cartIcon));
		} catch (NoSuchElementException e) {
			Assert.fail("Page is not loaded!");
		}

	}

	@Override
	protected void load() {
		PageFactory.initElements(driver, this);
	}

}
