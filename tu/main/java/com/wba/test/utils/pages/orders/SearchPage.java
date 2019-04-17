/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.orders;

import static com.oneleo.test.automation.core.UIUtils.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;


public class SearchPage extends LoadableComponent<SearchPage> {
	@FindBy(id = "rx-orders-clear-button")
	private WebElement clearallbutton;
	@FindBy(xpath = "//mat-icon[contains(text(),'arrow_back')]")
	private WebElement backbutton;
	@FindBy(xpath = "//span[contains(@class,'search-legend text-body')]")
	private WebElement searchresultstring;
	@FindBy(className = "mat-row ng-star-inserted")
	public List<WebElement> patientResultsList;
	@FindBy(id="rx-orders-last-name")
	private WebElement lastnamefield;
	@FindBy(id="rx-orders-first-name")
	private WebElement firstnamefield;
	@FindBy(id="rx-orders-birth-date")
	private WebElement dateofbirthfield;
	@FindBy(id="rx-orders-phone")
	private WebElement phonefield;
	@FindBy(id="rx-orders-search-button")
	private WebElement searchbutton;
	@FindBy(xpath = "//td[contains(@class,'itemsReady')]")
	private WebElement readyItemsString;
	@FindBy(id = "rx-orders-more-options")
	private WebElement showMoreButton;
	//Sub-xpath
	private String itemsReadyXpath = ".//td[contains(@class,'itemsReady')]";

	private WebDriver driver;

	public SearchPage(WebDriver driver) {
		this.driver = driver;
	}




	public void setLastName(String lastname) {
		lastnamefield.clear();
		lastnamefield.sendKeys(lastname);
	}
	public void setFirstName(String firstname) {
		firstnamefield.clear();
		firstnamefield.sendKeys(firstname);
	}
	public void setDateofBirthday(String dateofbirthday) {
		dateofbirthfield.clear();
		dateofbirthfield.sendKeys(dateofbirthday);
	}
	public void setPhoneNumber(String phonenumber) {
		Actions actions = new Actions(driver);
		actions.moveToElement(driver.findElement(By.id("rx-orders-birth-date")));
		actions.click();
		actions.sendKeys(Keys.chord(Keys.CONTROL, "a"));
		actions.sendKeys(Keys.DELETE);
		actions.sendKeys(phonenumber);
		actions.build().perform();
	}

	public String getLastName() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(lastnamefield));
		return lastnamefield.getAttribute("value");
	}
	public String getFirstName() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(firstnamefield));
		return firstnamefield.getAttribute("value");
	}
	public String getPhoneNumber() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(phonefield));
		return phonefield.getAttribute("value");
	}
	public String getDateofBirthday() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(dateofbirthfield));
		return dateofbirthfield.getAttribute("value");
	}

	public void clickOnSearch() {
		searchbutton.click();
	}
	public void clickOnClearAll() {
		clearallbutton.click();
	}
	public void clickOnShowMore() {
		showMoreButton.click();
	}

	public String getSearchResultLegend() {
		return searchresultstring.getText();
	}


	public PatientPage navigateToPatientPage(int patientposition) {
		List <WebElement> listOfPatients = driver.findElements(By.xpath("//mat-icon[contains(text(),'chevron_right')]"));
		listOfPatients.get(patientposition).click();
		return new PatientPage(driver);
	}

	public String readyItemsText() {
		return readyItemsString.getText();
	}

	public boolean isItemsReadyDisplayed(int index) {
		return !patientResultsList.get(index).findElement(By.xpath(itemsReadyXpath)).getText().equals("");
	}

	public void enterSearchInputsUsingKeys(HashMap<String, String> patientDataMap) {
		//Start with the last name field
		lastnamefield.sendKeys();
		if(driver.switchTo().activeElement().equals(lastnamefield)) {
			System.out.println("Last name element is in a focus");
			lastnamefield.sendKeys(patientDataMap.get("last_name"));
			lastnamefield.sendKeys(Keys.TAB);
		}
		else
			System.out.println("ASSERTION - Last name element not in the focus");


		//Verify that we in the first name field
		if(driver.switchTo().activeElement().equals(firstnamefield)) {
			System.out.println("First Name element is in a focus");
			firstnamefield.sendKeys(patientDataMap.get("first_name"));
			firstnamefield.sendKeys(Keys.TAB);
		}
		else
			System.out.println("ASSERTION - First Name element not in the focus");

		//Verify that we in the birth field
		if(driver.switchTo().activeElement().equals(dateofbirthfield)) {
			System.out.println("Birth element is in a focus");
			dateofbirthfield.sendKeys(patientDataMap.get("date_of_birth"));
			dateofbirthfield.sendKeys(Keys.TAB);
		}
		else
			System.out.println("ASSERTION - Birth element not in the focus");

		//Verify that we in the phone field
		if(driver.switchTo().activeElement().equals(phonefield)) {
			System.out.println("Phone element is in a focus");
			phonefield.sendKeys(patientDataMap.get("phone"));
			phonefield.sendKeys(Keys.TAB);
		}
		else
			System.out.println("ASSERTION - Phone element not in the focus");

		//Verify that we in the phone field
		if(driver.switchTo().activeElement().equals(searchbutton)) {
			System.out.println("Start search element is in a focus");
			searchbutton.sendKeys(Keys.ENTER);
		}
		else
			System.out.println("ASSERTION - Start search element not in the focus");
	}

	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(searchbutton)) {
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
