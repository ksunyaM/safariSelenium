/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.stockplus;

import static com.oneleo.test.automation.core.UIUtils.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

public class OrderSchedulingMngSearchSuppliersPage extends LoadableComponent<OrderSchedulingMngSearchSuppliersPage> {
	WebDriver driver;

	public OrderSchedulingMngSearchSuppliersPage(WebDriver driver) {
		this.driver = driver;
	}

	@FindBy(id = "mat-input-2")
	WebElement searchBox;
	@FindBy(id = "rx-suppliers-search-button")
	WebElement searchButton;
	@FindBy(id = "rx-suppliers-clear-button")
	WebElement clearButton;
	@FindBy(id = "mat-autocomplete-0")
	WebElement suggestedDropDown;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-order-schedule > div > div > rx-suppliers > div > div:nth-child(4)")
	WebElement countLabel;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-order-schedule > div > div > rx-global > div > rx-calendar > div > div > rx-calendar-view > div > rx-calendar-month-view")
	private WebElement calendar;
	@FindBy(id="calendar-content-week")
	private WebElement calendarWeek;
	@FindBy(id = "rx-suppliers-view-button")
	private WebElement viewButton;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-order-schedule > div > div > rx-global > div > rx-calendar > div > div > rx-calendar--manager > div > mat-card:nth-child(1)")
	private WebElement supplierNameMatCard;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-order-schedule > div > div > rx-global > div > rx-calendar > div > div > rx-calendar--manager > div > mat-card:nth-child(2)")
	private WebElement eventsMatCard;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-order-schedule > div > div > rx-global > div > rx-calendar > div > div > rx-calendar-event-manager > div > mat-card:nth-child(3)")
	private WebElement permanentScheduleMatCard;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-order-schedule > div > div > rx-global > div > rx-calendar > div > div > rx-calendar-event-manager > div > mat-card:nth-child(4)")
	private WebElement temporaryScheduleMatCard;
	@FindBy(css = "#mat-checkbox-2 > label > div")
	private WebElement eventsCheckBox;
	@FindBy(id="mat-checkbox-4")
	private WebElement temporaryCheckBox;
	@FindBy(css = "#mat-checkbox-2 > label > span")
	private WebElement eventLabel;
	@FindBy(id = "mat-checkbox-3")
	private WebElement permanentScheduleCheckBox;
	@FindBy(css = "#mat-checkbox-3 > label > span")
	private WebElement permanentScheduleLabel;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-order-schedule > div > div > rx-global > div > rx-calendar > div > div > rx-calendar-event-manager > div > mat-card:nth-child(2) > mat-card-actions > button")
	private WebElement addEventButton;
	@FindBy(css = "#cdk-overlay-1 > mat-dialog-container")
	private WebElement supplierPopover;
	@FindBy(id = "rx-calendar-view-month-year")
	private WebElement currentMonthLabel;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-order-schedule > div > div > rx-global > div > rx-calendar > div > div > rx-calendar-event-manager")
	private WebElement leftPanel;
	@FindBy(id="rx-calendar-view-week-button")
	private WebElement weekButton;
	@FindBy(id="rx-calendar-view-month-button")
	private WebElement monthButton;
	@FindBy(id="rx-calendar-view-right-button")
	private WebElement nextMonthButton;
	@FindBy(id = "rx-calendar-view-right-button")
	private WebElement nextWeekButton;
	@FindBy(id = "rx-calendar-view-left-button")
	private WebElement previousWeekButton;
	@FindBy(id="rx-calendar-view-left-button")
	private WebElement previousMonthButton;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-order-schedule > div > div > rx-global > div > rx-calendar > div > div > rx-calendar-event-manager > div > mat-card:nth-child(1) > mat-card-content")
	private WebElement storeInfoMatCard;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-order-schedule > div > div > rx-global > div > rx-calendar > div > div > rx-calendar-event-manager > div > mat-card:nth-child(3) > mat-card-content")
	private WebElement suppliersMatCard;
	@FindBy(css="#calendar-content-month > div:nth-child(2) > div:nth-child(3) > div.container-events.calendar-cell-grid > div")
	private WebElement eventBar;
	@FindBy(id="rx-calendar-view-today-button")
	private WebElement todayButton;
	@FindBy(id="mat-checkbox-2")
	private WebElement holidayCheckbox;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-order-schedule > div > div > rx-global > div > rx-calendar > div > div > rx-calendar-event-manager > div > mat-card:nth-child(3) > mat-card-content > div > div > button")
	private WebElement editButton;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-order-schedule > div > div > rx-global > div > rx-calendar > div > div > rx-calendar-event-manager > div > mat-card:nth-child(4) > mat-card-actions > button")
	private WebElement addTempScheduleButton;
	@FindBy(id="rx-suppliers-delete-button")
	private WebElement supplierSearchDeleteButton;
	@FindBy(id="mat-input-0")
	private WebElement supplierSearchInputField;
	@FindBy(id="order-search-table")
	private WebElement supplierSearchTable;
	@FindBy(id="mat-autocomplete-0")
	private WebElement belowListSearchInputField;


	public void isPresentElement(String componentName) {

		if (componentName.equals("searchForStoreBox"))
			Assert.assertThat("check " + componentName, !Objects.isNull(searchBox), Matchers.equalTo(Boolean.TRUE));
		if (componentName.equals("searchButton"))
			Assert.assertThat("check " + componentName, !Objects.isNull(searchButton), Matchers.equalTo(Boolean.TRUE));
		if (componentName.equals("clearButton"))
			Assert.assertThat("check " + componentName, !Objects.isNull(clearButton), Matchers.equalTo(Boolean.TRUE));
		if (componentName.equals("supplierSearchTable"))
			Assert.assertThat("check " + componentName, !Objects.isNull(supplierSearchTable), Matchers.equalTo(Boolean.TRUE));
	}

	public CreateEventPopup addNewEvent() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(addEventButton));
		this.addEventButton.click();
		return new CreateEventPopup(driver);
	}
	
	public EditPermanentScheduleBox editSchedule() {
		editButton.click();
		return new EditPermanentScheduleBox(driver);
	}
	
	public CreateTemporaryScheduleBox addTempSchedule() {
		addTempScheduleButton.click();
		return new CreateTemporaryScheduleBox(driver);
	}

	public List<String> getSuggestedResults() {
		List<String> rl = new ArrayList<>();
		List<WebElement> suggestedResults = suggestedDropDown.findElements(By.className("mat-option-text"));

		if (!suggestedResults.isEmpty())
			suggestedResults.stream().forEach(sr -> rl.add(sr.getText()));

		return rl;
	}
	
	public Boolean findEventInCalendar() {
		Boolean found = false;
		List<WebElement> weeks = calendar.findElements(By.cssSelector("ng-reflect-class-base"));
			if(weeks.size() > 0) {
				found = true;
		}
		return found;
	}
	
	public Boolean findEventInCalendarWeek() {
		Boolean found = false;
		List<WebElement> weeks = calendarWeek.findElements(By.cssSelector("ng-reflect-class-base"));
			if(weeks.size() > 0) {
				found = true;
		}
		return found;
	}
	
	public int countDaysInCalendarWeek() {
		return calendarWeek.findElements(By.className("calendar-cell")).size();
	}
	
	public int countDaysInCalendarMonth() {
		return calendar.findElements(By.className("calendar-cell")).size();
	}

	public List<String> getCurrentMonthDays() {
		List<String> monthDaysList = new ArrayList<>();
		List<WebElement> monthDays = calendar.findElements(By.className("dayNumber"));
		for (WebElement day : monthDays) {
			monthDaysList.add(day.getText());
		}
		return monthDaysList;
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
	
	public List<WebElement> findSuppliers() {
		return suppliersMatCard.findElements(By.cssSelector("div > mat-checkbox"));
	}

	public String getEventLabel() {
		return eventLabel.getText();
	}

	public String getCurrentMonthLabel() {
		return currentMonthLabel.getText();
	}

	public String getPermanentScheduleLabel() {
		return permanentScheduleLabel.getText();
	}

	public void clickOnSuggestedResult() {
		List<WebElement> suggestedResults = suggestedDropDown.findElements(By.className("mat-option-text"));
		Assert.assertThat("Suggested Result not Empty", suggestedResults.isEmpty(), Matchers.equalTo(Boolean.FALSE));

		WebElement suggestedResult = suggestedResults.get(0);
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(suggestedResult));
		suggestedResult.click();

	}

	public void clickOnSuggestedResultBySupplier(String supplier) {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(suggestedDropDown));
		List<WebElement> suggestedResults = suggestedDropDown.findElements(By.className("mat-option-text"));
		for (WebElement singelSupplier : suggestedResults) {
			String sup = singelSupplier.getText();
			if(sup.equals(supplier)) {
				singelSupplier.click();
				break;
			}
		}
		Assert.assertThat("Suggested Result not Empty", suggestedResults.isEmpty(), Matchers.equalTo(Boolean.FALSE));
	}

	public void clickOnSearch() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(searchButton), 25);
		searchButton.click();
	}

	public void clickOnEventsCheckBox() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(eventsCheckBox), 25);
		eventsCheckBox.click();
	}
	
	public void clickWeekButton() {
		weekButton.click();
	}
	
	public void clickMonthButton() {
		monthButton.click();
	}
	
	public void clickEventBar() {
		eventBar.click();
	}
	
	public void clickNextWeekButton() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(nextWeekButton));
		nextWeekButton.click();
	}
	
	public void clickPreviousWeekButton() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(previousWeekButton));
		previousWeekButton.click();
	}
	
	public void clickNextMonthButton() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(nextMonthButton));
		nextMonthButton.click();
	}
	
	public void clickPreviousMonthButton() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(previousMonthButton));
		previousMonthButton.click();
	}
	
	public void clickTodayButton() {
		todayButton.click();
	}

	public void clickOnPermanentScheduleCheckBox() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(permanentScheduleCheckBox), 25);
		permanentScheduleCheckBox.click();
	}
	
	public void clickHolidayCheckbox() {
		holidayCheckbox.click();
	}
	
	public Boolean isPermanentScheduleCheckBoxDisplayed() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(permanentScheduleCheckBox), 25);
		return permanentScheduleCheckBox.isDisplayed();
	}
	
	public String isPermanentScheduleCheckBoxChecked() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(permanentScheduleCheckBox), 25);
		return permanentScheduleCheckBox.getAttribute("ng-reflect-checked");
	}
	
	public String isTemporaryCheckBoxChecked() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(temporaryCheckBox), 25);
		return temporaryCheckBox.getAttribute("ng-reflect-checked");
	}
	

	public void viewSupplierScheduling() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(viewButton));
		viewButton.click();
	}

	public void performSearch() {
		Assert.assertThat("Perform supplier search",
				Objects.nonNull(ui().waitForCondition(driver, ExpectedConditions.visibilityOf(countLabel))),
				Matchers.equalTo(Boolean.TRUE));
	}

	public void clearSearch() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(clearButton), 25);
		clearButton.click();
		ui().waitForCondition(driver, ExpectedConditions.textToBePresentInElementValue(searchBox, ""));

	}

	public String getSearchGhostMessage() {
		return this.driver.findElement(By.className("mat-input-placeholder")).getText();
	}

	public void insertSearchCriteria(String criteria) throws Exception {

		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(this.searchBox), 25);
		searchBox.click();
		ui().waitForCondition(driver, ExpectedConditions.textToBePresentInElementValue(this.searchBox, ""), 25);

		if (StringUtils.isNotBlank(criteria)) {
			this.searchBox.sendKeys(criteria);
			ui().waitForCondition(driver, ExpectedConditions.textToBePresentInElementValue(this.searchBox, criteria));
		} else {
			throw new java.lang.Exception("search criteria missing");
		}
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
	
	public Map<Integer, String> getDaysInWeekCalendar(int day) {
		Map<Integer, String> weekDaysList = new HashMap<>();
		WebElement weekDay = calendarWeek.findElement(By.cssSelector("#calendar-content-week > div:nth-child(" + day + ") > div"));
		weekDaysList.put(day, weekDay.findElement(By.className("dayName")).getText());
		return weekDaysList;
	}
	
	public Map<Integer, String> getDayNumberInWeekCalendar(int day) {
		Map<Integer, String> weekDaysMap = new HashMap<>();
		WebElement weekDay = calendarWeek.findElement(By.cssSelector("#calendar-content-week > div:nth-child(" + day + ") > div"));
		weekDaysMap.put(day, weekDay.findElement(By.className("dayNumber")).getText());
		return weekDaysMap;
	}

	public boolean isSearchBoxEmpty() {
		return searchBox.getText().isEmpty();
	}

	/*
	 * Displayed
	 */
	public Boolean isSupplierNameDisplayed() {
		return supplierNameMatCard.isDisplayed();
	}

	public Boolean isEventsDisplayed() {
		return eventsMatCard.isDisplayed();
	}

	public Boolean isPermanentScheduleDisplayed() {
		return permanentScheduleMatCard.isDisplayed();
	}

	public Boolean isTemporaryScheduleDisplayed() {
		return temporaryScheduleMatCard.isDisplayed();
	}

	public Boolean isAddEventDisplayed() {
		return addEventButton.isDisplayed();
	}

	public Boolean isPopoverDisplayed() {
		return supplierPopover.isDisplayed();
	}
	
	public Boolean isPreviousMonthButtonEnabled() {
		return previousMonthButton.isEnabled();
	}
	
	public Boolean isNextMonthButtonEnabled() {
		return nextMonthButton.isEnabled();
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
		return permanentScheduleCheckBox.isDisplayed();
	}
	
	public String isStoreShiftCheckBoxChecked() {
		return permanentScheduleCheckBox.getAttribute("ng-reflect-checked").toString();
	}

	public void clickSupplierSearchButtonSearchButton()
	{
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(searchButton));
		searchButton.click();
	}

	public boolean isDisplayedSupplierSearchDeleteButton()
	{
		boolean isDisplayed=false;
		try{
			ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(supplierSearchDeleteButton), 25);
			isDisplayed = supplierSearchDeleteButton.isDisplayed();
		}
		catch (NoSuchElementException ne){}
		catch (TimeoutException to){}
		return  isDisplayed;
	}

	public void insertTextIntoSupplierSearchInputField(String stringSearch) throws Throwable
	{

		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(this.supplierSearchInputField), 25);
		supplierSearchInputField.click();
		ui().waitForCondition(driver, ExpectedConditions.textToBePresentInElementValue(this.supplierSearchInputField, ""), 25);

		if (StringUtils.isNotBlank(stringSearch)) {
			this.supplierSearchInputField.sendKeys(stringSearch);
			ui().waitForCondition(driver, ExpectedConditions.textToBePresentInElementValue(this.supplierSearchInputField, stringSearch));
		} else {
			throw new java.lang.Exception("search criteria missing");
		}
	}


	public boolean isDisplayedSupplierSearchTable()
	{
		boolean isDisplayed=false;
		try{

			isDisplayed = supplierSearchTable.isDisplayed();
		}
		catch (NoSuchElementException ne){}
		catch (TimeoutException to){}
		return  isDisplayed;
	}

	public void clickBelowListSearchInputField()
	{
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(this.belowListSearchInputField));
		belowListSearchInputField.click();
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
