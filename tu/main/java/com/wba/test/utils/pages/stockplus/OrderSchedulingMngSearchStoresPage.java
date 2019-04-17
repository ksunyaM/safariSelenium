/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.stockplus;

import static com.oneleo.test.automation.core.UIUtils.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.assertj.core.api.AssertDelegateTarget;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

import com.oneleo.test.automation.core.CucumberCollector;

import cucumber.api.CucumberOptions;
import kafka.coordinator.group.Empty;

public class OrderSchedulingMngSearchStoresPage extends LoadableComponent<OrderSchedulingMngSearchStoresPage> {
	WebDriver driver;

	public OrderSchedulingMngSearchStoresPage(WebDriver driver) {
		this.driver = driver;
	}

	@FindBy(id = "mat-select-0")
	WebElement optionSelected;

	@FindBy(id = "mat-option-0")
	WebElement storeNumberOpt;
	@FindBy(id = "mat-option-1")
	WebElement districtOpt;
	@FindBy(id = "mat-option-2")
	WebElement cityStateOpt;
	@FindBy(id = "mat-option-3")
	WebElement zipCodeOpt;
	@FindBy(id = "mat-option-4")
	private WebElement selectSuggestedStore;
	@FindBy(id="mat-checkbox-5")
	private WebElement storeShiftCheckBox;

	//@FindBy(id = "mat-input-2")
	@FindBy(id = "mat-input-0")
	WebElement searchBox;
	@FindBy(css="#mat-checkbox-2 > label > div")
	private WebElement eventsCheckBox;
	@FindBy(id = "rx-stores-search-button")
	WebElement searchButton;
	@FindBy(id = "rx-stores-clear-button")
	WebElement clearButton;

	@FindBy(id = "mat-autocomplete-0")
	WebElement suggestedDropDown;

	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-order-schedule > div > div > rx-stores > div > div.margin-left-3x.padding-top-2x > b")
	WebElement countLabel;

	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-order-schedule > div > div > rx-global > div > rx-calendar > div > div > rx-calendar-event-manager > div > mat-card:nth-child(3) > mat-card-content")
	private WebElement suppliersMatCard;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-order-schedule > div > div > rx-global > div > rx-calendar > div > div > rx-calendar-event-manager > div > mat-card:nth-child(3) > mat-card-content > div:nth-child(1) > div > button")
	private WebElement editButton;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-order-schedule > div > div > rx-global > div > rx-calendar > div > div > rx-calendar-event-manager > div > mat-card:nth-child(3) > mat-card-actions > button")
	private WebElement addScheduling;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-order-schedule > div > div > rx-global > div > rx-calendar > div > div > rx-calendar-event-manager > div > mat-card:nth-child(4) > mat-card-actions > button")
	private WebElement addTemporary;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-order-schedule > div > div > rx-global > div > rx-calendar > div > div > rx-calendar-view")
	private WebElement calendar;
	@FindBy(id = "rx-calendar-view-month-year")
	private WebElement currentMonthLabel;
	@FindBy(id = "rx-calendar-view-right-button")
	private WebElement nextWeekButton;
	@FindBy(id = "rx-calendar-view-left-button")
	private WebElement previousWeekButton;
	@FindBy(id = "rx-calendar-view-today-button")
	private WebElement todayButton;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-order-schedule > div > div > rx-global > div > rx-calendar > div > div > rx-calendar-event-manager > div > mat-card:nth-child(1) > mat-card-content > div")
	private WebElement storeInfoDiv;
	@FindBy(id="item-view-button")
	private WebElement viewButton;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-order-schedule > div > div > rx-global > div > rx-calendar > div > div > rx-calendar-event-manager")
	private WebElement leftPanel;
	@FindBy(id="rx-calendar-view-week-button")
	private WebElement weekButton;
	@FindBy(id="rx-calendar-view-month-button")
	private WebElement monthButton;
	@FindBy(id="rx-calendar-view-right-button")
	private WebElement nextMonthButton;
	@FindBy(id="rx-calendar-view-left-button")
	private WebElement previousMonthButton;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-order-schedule > div > div > rx-global > div > rx-calendar > div > div > rx-calendar-event-manager > div > mat-card:nth-child(1) > mat-card-content")
	private WebElement storeInfoMatCard;

	@FindBy(id = "mat-checkbox-4")
	private WebElement warehouseCheckbox;

	@FindBy(id = "calendar-content-week")
	private WebElement calendarWeek;

	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-order-schedule > div > div > rx-global > div > rx-calendar > div > div > rx-calendar-event-manager > div > mat-card:nth-child(4)")
	private WebElement temporaryShiftMatCard;
	@FindBy(css="#mat-checkbox-3 > label > span")
	private WebElement selectedSupplierLabel;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-order-schedule > div > div > rx-global > div > rx-calendar > div > div > rx-calendar-event-manager > div > mat-card:nth-child(4) > mat-card-actions > button")
	private WebElement addTempScheduleButton;

	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-order-schedule > div > div > rx-global > div > rx-calendar > div > div > rx-calendar-event-manager > div > mat-card:nth-child(2) > mat-card-actions > button")
	private WebElement addEventButton;

	public void isPresentElement(String componentName) {

		if (componentName.equals("searchForStoreDropDown"))
			Assert.assertThat("check " + componentName, !Objects.isNull(optionSelected),
					Matchers.equalTo(Boolean.TRUE));
		if (componentName.equals("searchForStoreBox"))
			Assert.assertThat("check " + componentName, !Objects.isNull(searchBox), Matchers.equalTo(Boolean.TRUE));
		if (componentName.equals("searchButton"))
			Assert.assertThat("check " + componentName, !Objects.isNull(searchButton), Matchers.equalTo(Boolean.TRUE));
		if (componentName.equals("clearButton"))
			Assert.assertThat("check " + componentName, !Objects.isNull(clearButton), Matchers.equalTo(Boolean.TRUE));

	}

	public String getCurrentMonthLabel() {
		return currentMonthLabel.getText();
	}

	public List<String> getSuggestedResults() {
		List<String> rl = new ArrayList<>();
		try {
			List<WebElement> suggestedResults = suggestedDropDown.findElements(By.className("mat-option-text"));
			if (!suggestedResults.isEmpty())
				suggestedResults.stream().forEach(sr -> rl.add(sr.getText()));
		} catch (Exception e) {

		}

		return rl;
	}

	public void clickOnSuggestedResult() {
		List<WebElement> suggestedResults = suggestedDropDown.findElements(By.className("mat-option-text"));
		Assert.assertThat("Suggested Result not Empty", suggestedResults.isEmpty(), Matchers.equalTo(Boolean.FALSE));

		WebElement suggestedResult = suggestedResults.get(0);
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(suggestedResult));
		suggestedResult.click();

	}
	
	public CreateTemporaryScheduleBox addTempSchedule() {
		addTempScheduleButton.click();
		return new CreateTemporaryScheduleBox(driver);
	}
	
	public List<String> getCurrentMonthDays(){
		List<String> monthDaysList = new ArrayList<>();
		List<WebElement> monthDays = calendar.findElements(By.className("dayNumber"));
		for (WebElement day : monthDays) {
			monthDaysList.add(day.getText());
		}
		return monthDaysList;
	}
	
	public String getSelectedSupplierLabel() {
		return selectedSupplierLabel.getText();
	}

	public void clickNextWeekButton() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(nextWeekButton));
		nextWeekButton.click();
	}

	public void clickPreviousWeekButton() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(previousWeekButton));
		previousWeekButton.click();
	}

	public void clickTodayButton() {
		todayButton.click();
	}

	public void clickOnSearch() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(searchButton), 25);
		searchButton.click();
	}
	
	public void clickTypeSearch() {
		optionSelected.click();
	}

	public void clickOnSuggestedStore() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(selectSuggestedStore), 25);
		selectSuggestedStore.click();
	}
	
	public void viewStoreScheduling() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(viewButton));
		viewButton.click();
	}

	public UpdateEventPopup clickOnEdit() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(editButton), 25);
		editButton.click();
		return new UpdateEventPopup(driver);
	}

	public void clickWarehouseCheckbox() {
		warehouseCheckbox.click();
	}

	public void clickStoreShiftCheckBox() {
		WebElement chkBox = temporaryShiftMatCard.findElement(By.cssSelector("mat-checkbox"));
		chkBox.click();
	}

	public CreateEventPopup clickAddScheduleButton() {
		addScheduling.click();
		return new CreateEventPopup(driver);
	}

	public CreateEventPopup clickAddTemporaryButton() {
		addTemporary.click();
		return new CreateEventPopup(driver);
	}

	public void performSearch() {
		Assert.assertThat("Perform store search",
				Objects.nonNull(ui().waitForCondition(driver, ExpectedConditions.visibilityOf(countLabel))),
				Matchers.equalTo(Boolean.TRUE));
	}
	
	/*
	 * Displayed
	 */
	public Boolean isLeftPanelDisplayed() {
		return leftPanel.isDisplayed();
	}
	
	public Boolean isCalendarDisplayed() {
		return calendar.isDisplayed();
	}
	
	public Boolean isWeekButtonDisplayed() {
		return weekButton.isDisplayed();
	}
	
	public Boolean isMonthButtonDisplayed() {
		return monthButton.isDisplayed();
	}
	
	public Boolean isNextMonthButtonDisplayed() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(nextMonthButton));
		return nextMonthButton.isDisplayed();
	}
	
	public Boolean isPreviousMonthButtonDisplayed() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(previousMonthButton));
		return previousMonthButton.isDisplayed();
	}
	
	public Boolean isStoreInfoMatCardDisplayed() {
		return storeInfoMatCard.isDisplayed();
	}
	
	public Boolean isOnEventsCheckBoxDisplayed(){
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(eventsCheckBox), 25);
		return eventsCheckBox.isDisplayed();
	}
	
	public Boolean isStoreShiftCheckBoxDisplayed() {
		return storeShiftCheckBox.isDisplayed();
	}
	
	public String isStoreShiftCheckBoxChecked() {
		return storeShiftCheckBox.getAttribute("ng-reflect-checked").toString();
	}
	
	
	/**
	 * Search for store with a given criteria
	 * 
	 * @param option
	 * @param criteriaValue
	 * @throws Exception
	 */
	public OrderSchedulingMngStoresResultsPage searchforStore(String option, String criteriaValue) throws Exception {
		selectDropDownOption(option);
		insertSearchCriteria(criteriaValue);
		clickOnSuggestedResult();
		clickOnSearch();
		return new OrderSchedulingMngStoresResultsPage(driver).get();
	}

	public void clearSearch() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(clearButton), 25);
		clearButton.click();
		ui().waitForCondition(driver, ExpectedConditions.textToBePresentInElementValue(searchBox, ""));
	}

	public String getSearchGhostMessage() {
		return this.driver.findElement(By.className("mat-input-placeholder")).getText();
	}

	public String getSelectedSearchOption() {
		return this.optionSelected.getText();
	}

	public String getSearchCriteria() {
		return this.searchBox.getAttribute("ng-reflect-model");
	}

	public List<String> getDropDownOptions() {
		List<String> rv = new ArrayList();
		List<WebElement> l = this.driver.findElements(By.className("mat-option-text"));
		l.stream().forEach(w -> rv.add(w.getText()));
		return rv;
	}

	public void insertSearchCriteria(String criteria) throws Exception {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(this.searchBox), 25);
		searchBox.click();
		ui().waitForCondition(driver, ExpectedConditions
				.attributeContains(this.driver.findElement(By.id("mat-input-0")), "ng-reflect-model", ""));
		if (StringUtils.isNotBlank(criteria)) {
			this.searchBox.clear();
			this.searchBox.sendKeys(criteria);
		} else {
			throw new java.lang.Exception("search criteria missing");
		}
	}

	public void clickOnSearchDropDown() {
		WebElement arrow = this.driver.findElement(By.className("mat-select-arrow"));
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(arrow));
		arrow.click();
	}

	public List<String> findPositionsInCalendar() {
		List<String> classes = new ArrayList<>();
		List<WebElement> events = calendar.findElements(By.cssSelector("event event-start event-end ro"));
		for (WebElement event : events) {
			classes.add(event.getClass().toString());
		}
		return classes;
	}

	public List<String> findBarInDays() {
		List<String> bars = new ArrayList<>();
		List<WebElement> days = calendar.findElements(By.className("calendar-cell"));
		for (WebElement day : days) {
			if (day.findElement(By.className("supplier-name")).isDisplayed()) {
				bars.add(day.findElement(By.className("supplier-name")).getText());
			}
		}
		return bars;
	}

	public Boolean isStoreInfoDisplayed() {
		return storeInfoDiv.isDisplayed();
	}

	public List<WebElement> findSuppliers() {
		return suppliersMatCard.findElements(By.cssSelector("div > mat-checkbox"));
	}

	public Boolean findEventInCalendar(String event) {
		Boolean found = false;
		List<WebElement> weeks = calendar.findElements(By.cssSelector("ng-reflect-class-base"));
		for (WebElement week : weeks) {
			if (week.getText() == event) {
				found = true;
			}
		}
		return found;
	}

	public List<String> findSupplierInWeekCalendar(String event) {
		List<String> found = new ArrayList<>();
		List<WebElement> suppliers = calendarWeek.findElements(By.className("supplier-name"));
		for (WebElement supplier : suppliers) {
			if (supplier.getText().contains(event)) {
				found.add(supplier.getText());
			}

		}
		return found;
	}

	public void selectDropDownOption(String option) {
		clickOnSearchDropDown();
		switch (option) {
		case "District":
			districtOpt.click();
			break;
		case "City, State":
			cityStateOpt.click();
			break;
		case "ZIP Code":
			zipCodeOpt.click();
			break;
		default:
			storeNumberOpt.click();
			break;
		}
	}

	public boolean isSearchBoxEmpty() {
		return searchBox.getText().isEmpty();
	}

	public boolean isDisplayedAddEventButton()
	{
		boolean isDisplayed=false;
		try{
			isDisplayed = addEventButton.isDisplayed();
		}
		catch (NoSuchElementException ne){}
		catch (TimeoutException to){}
		return  isDisplayed;
	}

	public boolean isDisplayedAddSchedulingButton()
	{
		boolean isDisplayed=false;
		try{
			isDisplayed = addScheduling.isDisplayed();
		}
		catch (NoSuchElementException ne){}
		catch (TimeoutException to){}
		return  isDisplayed;
	}

	public boolean isDisplayedAddTemporaryButton()
	{
		boolean isDisplayed=false;
		try{
			isDisplayed = addTemporary.isDisplayed();
		}
		catch (NoSuchElementException ne){}
		catch (TimeoutException to){}
		return  isDisplayed;
	}


	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(searchButton)) {
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
