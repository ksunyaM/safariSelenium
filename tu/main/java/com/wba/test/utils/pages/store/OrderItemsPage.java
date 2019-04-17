/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.store;

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
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

public class OrderItemsPage extends LoadableComponent<OrderItemsPage> {
	private WebDriver driver;

	@FindBy(id = "page-header-title")
	private WebElement labelOrderItems;

	@FindBy(id = "shopping-cart-icon")
	private WebElement cartIcon;

	@FindBy(css = "#page-header-back-button > span > mat-icon")
	private WebElement backIcon;

	@FindBy(id = "mat-input-0")
	private WebElement itemSearch;

	@FindBy(id = "mat-autocomplete-0")
	private WebElement autocomplete;

	@FindBy(css = "#form-wrapper > form > button.mat-raised-button.mat-neutral")
	private WebElement clearButton;

	@FindBy(id = "search-button")
	private WebElement searchButton;

	@FindBy(css = "#mat-error-0")
	private WebElement searchErrorMessage;

	@FindBy(css = "#mat-card0")
	private WebElement foundMatCard;

	@FindBy(css = "#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-purchase-order-manual > div > mat-card")
	private WebElement header;

	@FindBy(css = "#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-purchase-order-manual > div > div > div.purchase-order-manual-scroll.purchase-order-manual-background > rx-purchase-order-manual-line")
	private WebElement leftPanel;

	@FindBy(css = "#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-purchase-order-manual > div > div > div:nth-child(2)")
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
	 * Get data
	 */
	public List<WebElement> getAllNDC() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(autocomplete));
		WebElement ndcGroup = autocomplete.findElement(By.id("ndc-group"));
		return ndcGroup.findElements(By.cssSelector("mat-option"));
	}

	public List<WebElement> getAllUPC() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(autocomplete));
		WebElement ndcGroup = autocomplete.findElement(By.id("upc-group"));
		return ndcGroup.findElements(By.cssSelector("mat-option"));
	}

	public List<WebElement> getAllNames() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(autocomplete), 20);
		WebElement names = autocomplete.findElement(By.id("produc-name-group"));
		return names.findElements(By.cssSelector("mat-option"));
	}

	public void clickSpecificName(String name) throws InterruptedException {
		synchronized (this) {
			wait(2000);
		}
		WebElement names = autocomplete.findElement(By.id("produc-name-group"));
		List<WebElement> anySingleName = names.findElements(By.cssSelector("mat-option"));
		for (WebElement singleName : anySingleName) {
			if (singleName.getAttribute("ng-reflect-value").equals(name)) {
				singleName.click();
				break;
			}
		}
	}

	public void clickFirstName() throws InterruptedException {
		synchronized (this) {
			wait(2000);
		}
		WebElement names = autocomplete.findElement(By.id("produc-name-group"));
		List<WebElement> anySingleName = names.findElements(By.cssSelector("mat-option"));
		for (WebElement singleName : anySingleName) {
			singleName.click();
			break;
		}
	}

	public void clickSpecificNameWithoutSpaces(String name) {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(autocomplete), 20);
		WebElement names = autocomplete.findElement(By.id("produc-name-group"));
		List<WebElement> anySingleName = names.findElements(By.cssSelector("mat-option"));
		for (WebElement singleName : anySingleName) {
			String nameWithoutSpaces = singleName.getAttribute("ng-reflect-value").replace(" ", "");
			name = name.replace(" ", "");
			if (nameWithoutSpaces.equals(name)) {
				singleName.click();
				break;
			}
		}
	}

	public String getItemSearch() {
		return itemSearch.getAttribute("ng-reflect-model");
	}

	public String getErrorMessageMissingItem() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(searchErrorMessage), 15);
		return searchErrorMessage.getText();
	}

	/*
	 * Insert text
	 */
	public void insertItemSearch(String item) {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(itemSearch));
		itemSearch.sendKeys(item);

	}

	/*
	 * Hot Keys
	 */
	public void spaceInInsertItem() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(itemSearch));
		itemSearch.sendKeys(Keys.SPACE);
	}

	/*
	 * Buttons
	 */
	public void clickClearButton() {
		clearButton.click();
	}

	public void clickSearchButton() {
		searchButton.click();
	}

	/*
	 * Displayed
	 */
	public Boolean foundItemDisplayed() {
		return foundMatCard.isDisplayed();
	}

	public Boolean cartIconDisplayed() {
		return cartIcon.isDisplayed();
	}

	public Boolean backIconDisplayed() {
		return backIcon.isDisplayed();
	}

	public Boolean itemSearchDisplayed() {
		return itemSearch.isDisplayed();
	}

	public Boolean clearButtonDisplayed() {
		return clearButton.isDisplayed();
	}

	public Boolean searchButtonDisplayed() {
		return searchButton.isDisplayed();
	}

	public Boolean headerDisplayed() {
		return header.isDisplayed();
	}

	public Boolean leftPanelDisplayed() {
		return leftPanel.isDisplayed();
	}

	public Boolean rightPanelDisplayed() {
		return rightPanel.isDisplayed();
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
