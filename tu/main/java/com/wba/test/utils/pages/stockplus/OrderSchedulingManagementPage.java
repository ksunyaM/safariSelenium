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

import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

public class OrderSchedulingManagementPage extends LoadableComponent<OrderSchedulingManagementPage> {


    @FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-order-schedule > div > nav > div > a.mat-tab-link.custom")
	WebElement globalTab;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-order-schedule > div > nav > div > a:nth-child(2)")
	WebElement supplierTab;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-order-schedule > div > nav > div > a:nth-child(3)")
	WebElement storesTab;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-order-schedule > div > div > rx-global > div > rx-calendar > div > div > rx-calendar-event-manager")
	private WebElement leftPanel;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-order-schedule > div > div > rx-global > div > rx-calendar > div > div > rx-calendar-view")
	private WebElement calendar;
	@FindBy(id="rx-calendar-view-month-year")
	private WebElement currentMonthLabel;
	@FindBy(id="mat-checkbox-1")
	private WebElement holidayCheckbox;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-order-schedule > div > div > rx-global > div > rx-calendar > div > div > rx-calendar-event-manager > div > mat-card > mat-card-actions > button")
	private WebElement addEventButton;
	@FindBy(id="rx-calendar-view-week-button")
	private WebElement weekButton;
	@FindBy(id="rx-calendar-view-month-button")
	private WebElement monthButton;
	@FindBy(id="popover-event")
	private WebElement popoverEvent;
	@FindBy(css="#popover-event > div.popover-dialog-content > div.margin-top-3x.margin-bottom")
	private WebElement popoverMessage;
	@FindBy(css="#calendar-content-month > div:nth-child(2) > div:nth-child(3) > div.container-events.calendar-cell-grid > div")
	private WebElement eventBar;
	@FindBy(id="rx-calendar-view-right-button")
	private WebElement nextMonthButton;
	@FindBy(id="rx-calendar-view-left-button")
	private WebElement previousMonthButton;
	@FindBy(id="rx-calendar-view-today-button")
	private WebElement todayButton;
	@FindBy(xpath = "//*[@id='popover-event']/div[2]/div[2]/button[1]")
	private WebElement updateEventButton;
	@FindBy(id = "rx-notification")
	private WebElement toastMessage;
	
	@FindBy(id="calendar-content-week")
	private WebElement calendarWeek;
	@FindBy(css="#calendar-content-week > div:nth-child(1) > div")
	private WebElement firstDayOfTheWeek;
	@FindBy(css="#calendar-content-week > div:nth-child(7) > div")
	private WebElement lastDayOfTheWeek;
	@FindBy(id = "rx-calendar-view-right-button")
	private WebElement nextWeekButton;
	@FindBy(id = "rx-calendar-view-left-button")
	private WebElement previousWeekButton;
	@FindBy(css = "#mat-checkbox-2 > label > div")
	private WebElement eventsCheckBox;
	@FindBy(id="page-header-title")
	private WebElement pageHeaderTitle;

	WebDriver driver;

	public OrderSchedulingManagementPage(WebDriver driver) {
		this.driver = driver;
	}
	
	/*
	 * Content in web elements
	 */
	public String getCurrentMonthLabel() {
		return currentMonthLabel.getText();
	}
	
	public int countDaysInCalendarMonth() {
		return calendar.findElements(By.className("calendar-cell")).size();
	}
	
	public List<String> getCurrentWeekDays(){
		List<String> weekDaysList = new ArrayList<>();
		List<WebElement> weekDays = calendar.findElements(By.className("dayNumber"));
		for (WebElement day : weekDays) {
			weekDaysList.add(day.getText());
		}
		return weekDaysList;
	}
	
	public List<String> getCurrentMonthDays(){
		List<String> monthDaysList = new ArrayList<>();
		List<WebElement> monthDays = calendar.findElements(By.className("dayNumber"));
		for (WebElement day : monthDays) {
			monthDaysList.add(day.getText());
		}
		return monthDaysList;
	}
	
	public String popoverMessageText() {
		return popoverMessage.getText();
	}
	
	public Map<Integer, String> getDaysInWeekCalendar(int day) {
		Map<Integer, String> weekDaysList = new HashMap<>();
		WebElement weekDay = calendar.findElement(By.cssSelector("#calendar-content-week > div:nth-child(" + day + ") > div"));
		weekDaysList.put(day, weekDay.findElement(By.className("dayName")).getText());
		return weekDaysList;
	}
	
	public Map<Integer, String> getDayNumberInWeekCalendar(int day) {
		Map<Integer, String> weekDaysMap = new HashMap<>();
		WebElement weekDay = calendar.findElement(By.cssSelector("#calendar-content-week > div:nth-child(" + day + ") > div"));
		weekDaysMap.put(day, weekDay.findElement(By.className("dayNumber")).getText());
		return weekDaysMap;
	}

	/*
	 * Displayed elements
	 */
	public Boolean isLeftPanelDisplayed() {
		return leftPanel.isDisplayed();
	}
	
	public Boolean isCalendarDisplayed() {
		return calendar.isDisplayed();
	}
	
	public Boolean isHolidayCheckboxDisplayed() {
		return holidayCheckbox.isDisplayed();
	}
	
	public String isHolidayCheckboxChecked() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(holidayCheckbox), 25);
		return holidayCheckbox.getAttribute("ng-reflect-checked");
	}
	
	public WebElement findBarInCalendar() {
		return calendar.findElement(By.cssSelector("span"));
	}
	
	public int countDaysInCalendar() {
		return calendar.findElements(By.className("calendar-cell")).size();
	}
	
	public Boolean isPopoverDisplayed() {
		return popoverEvent.isDisplayed();
	}
	
	public Boolean findEventInCalendar() {
		Boolean found = false;
		List<WebElement> weeks = calendar.findElements(By.cssSelector("ng-reflect-class-base"));
			if(weeks.size() > 0) {
				found = true;
		}
		return found;
	}
	
/*
 * Navigation
 */
	public OrderSchedulingMngSearchSuppliersPage navigateToSearchSuppliersPage() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(this.supplierTab), 20);
		this.supplierTab.click();
		return new OrderSchedulingMngSearchSuppliersPage(driver).get();

	}
	
	public OrderSchedulingMngSearchStoresPage navigateToSearchStoresPage() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(this.storesTab), 20);
		this.storesTab.click();
		return new OrderSchedulingMngSearchStoresPage(driver).get();

	}
	
	/*
	 * Press buttons
	 */
	public CreateEventPopup pressAddEventButton(){
		addEventButton.click();
		return new CreateEventPopup(driver);
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
	
	public void clickHolidayCheckbox() {
		holidayCheckbox.click();
	}

	public void clickOnFirstAliveGlobalEvent() {
		List<WebElement> events = this.driver.findElements(By.className("event"));
		WebElement event = events.get(events.size() - 1);
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(event), 20);
		event.click();
	}

	public UpdateEventPopup updateEvent() {
		clickOnFirstAliveGlobalEvent();
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(updateEventButton), 20);
		updateEventButton.click();
		return new UpdateEventPopup(driver).get();
	}

	public String getToastMessage() {
		String rv;
		try {
			ui().waitForCondition(driver, ExpectedConditions.visibilityOf(toastMessage), 30);
			rv = toastMessage.getText();
			rv = rv.replace("check_circle\n", "").replace("\nclose", "");

		} catch (Exception e) {
			rv = "Toast not appear";
		}
		return rv;
	}
	
	public void clickNextWeekButton() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(nextWeekButton));
		nextWeekButton.click();
	}
	
	public void clickPreviousWeekButton() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(previousWeekButton));
		previousWeekButton.click();
	}
	
	/*
	 * Disabled/Enabled elements
	 */
	public Boolean isPreviousMonthButtonEnabled() {
		return previousMonthButton.isEnabled();
	}
	
	public Boolean isNextMonthButtonEnabled() {
		return nextMonthButton.isEnabled();
	}
	
	public Boolean isOnEventsCheckBoxDisplayed(){
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(eventsCheckBox), 25);
		return eventsCheckBox.isDisplayed();
	}

	public String getPageHeaderTitle()
	{
		return pageHeaderTitle.getText();
	}

	public boolean isDisplayedAddEventButton()
	{
		boolean isDisplayed=false;
		try{
			ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(addEventButton), 25);
			isDisplayed = addEventButton.isDisplayed();
		}
		catch (NoSuchElementException ne){}
		catch (TimeoutException to){}
		return  isDisplayed;
	}

	public boolean isDisplayedGlobalTab()
	{
		boolean isDisplayed=false;
		try{
			ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(globalTab), 25);
			isDisplayed = globalTab.isDisplayed();
		}
		catch (NoSuchElementException ne){}
		catch (TimeoutException to){}
		return  isDisplayed;
	}



	@Override
	protected void load() {
		PageFactory.initElements(driver, this);

	}
	
	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(globalTab)) {
			throw new Error();
		}
		try {
		} catch (NoSuchElementException e) {
			Assert.fail("Page is not loaded!");
		}
	}

}
