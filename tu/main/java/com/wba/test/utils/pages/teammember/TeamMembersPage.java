/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.teammember;

import com.wba.test.utils.StatefulSingleton;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.util.List;
import java.util.Objects;

public class TeamMembersPage extends LoadableComponent<TeamMembersPage> {

    @FindBy(xpath = "//input[contains(@ng-reflect-name, 'lastName')]")
    private WebElement firstName;
    @FindBy(xpath = "//input[contains(@ng-reflect-name, 'firstName')]")
    private WebElement lastName;
    @FindBy(xpath = "//input[contains(@ng-reflect-name, 'oneID')]")
    private WebElement oneId;
    @FindBy(xpath = "//input[contains(@ng-reflect-name, 'employeeID')]")
    private WebElement employeeId;
    @FindBy(xpath = "//input[contains(@ng-reflect-name, 'storeNumber')]")
    private WebElement storeNumber;
    @FindBy(xpath = "//span[text()=' SEARCH ']")
    private WebElement searchButton;
    @FindBy(xpath = "//span[text()=' CLEAR ALL '")
    private WebElement clearAllButton;
    @FindBy(xpath = "//span[text()=' VIEW & EDIT ']")
    private WebElement viewAndEditButton;
    @FindBy(xpath = "//table[@class='mat-table']")
    private WebElement teamMembersTable;

    private static final String ROW_XPATH = "//table/tbody/tr[@role='row']";
    private static final String ROW_PARAMETRIZED_XPATH = "//table/tbody/tr[position()=%d]/td[position()=1]";
    private static String COLUMN_NAME_XPATH = "//table/thead/tr/th[contains(., ' %s ')]";
    private static String TABLE_XPATH = "//table[@class='mat-table']";

    private WebDriver driver;

    public TeamMembersPage(WebDriver driver) {
        this.driver = driver;
        StatefulSingleton.instance().setDriver(driver);
    }

    public boolean isLastNameFieldDisplayed() {
        return lastName.isDisplayed();
    }

    public boolean isFirstNameFieldDisplayed() {
        return firstName.isDisplayed();
    }

    public boolean isOneIdFieldDisplayed() {
        return oneId.isDisplayed();
    }

    public boolean isEmployeeIdFieldDisplayed() {
        return employeeId.isDisplayed();
    }

    public boolean isStoreNumberFieldDisplayed() {
        return storeNumber.isDisplayed();
    }

    public boolean isSearchButtonDisplayed() {
        return searchButton.isDisplayed();
    }

    public boolean isClearAllButtonDisplayed() {
        return clearAllButton.isDisplayed();
    }

    public boolean isViewAndEditButtonDisplayed() {
        return searchButton.isDisplayed();
    }

    public boolean isTeamMembersTableDisplayed() {
        return teamMembersTable.isDisplayed();
    }

    public boolean isGivenColumnDisplayed(String columnName) {
        return driver.findElement(By.xpath(String.format(COLUMN_NAME_XPATH, columnName))).isDisplayed();
    }

    public boolean isStoreNumberFieldEmpty() {
        return storeNumber.getAttribute("value").isEmpty();
    }

    public boolean isViewAndEditButtonActive() {
        return viewAndEditButton.isEnabled();
    }

    public String getStoreNumberValue() {
        return storeNumber.getAttribute("value");
    }

    public void clearStoreNumberFieldContent() {
        storeNumber.clear();
    }

    public TeamMemberDetailsPage clickViewAndEditButton() {
        viewAndEditButton.click();
        return new TeamMemberDetailsPage(driver);
    }

    public void clickStoreNumber() {
        storeNumber.click();
    }

    public void clickSearchButton() {
        searchButton.click();
    }

    public void clickOnGivenColumnByColumnName(String columnName) {
        driver.findElement(By.xpath(String.format(COLUMN_NAME_XPATH, columnName))).click();
    }

    public boolean isTeamMemberDisplayedInTeamMembersTable(String teamMemberName) {
        List<WebElement> rows = teamMembersTable.findElements(By.xpath(ROW_XPATH));
        int index = 1;
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.xpath(String.format(ROW_PARAMETRIZED_XPATH, index)));
            if ((cells.get(0).getText()).equals(teamMemberName)) {
                return true;
            } else {
                ++index;
                continue;
            }
        }
        return false;
    }

    public void clickSelectedTeamMemberInTeamMembersTable(String teamMemberName) {
        List<WebElement> rows = teamMembersTable.findElements(By.xpath(ROW_XPATH));
        int index = 1;
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.xpath(String.format(ROW_PARAMETRIZED_XPATH, index)));
            if ((cells.get(0).getText()).contains(teamMemberName)) {
                cells.get(0).click();
                break;
            } else {
                ++index;
                continue;
            }
        }
    }

    public boolean isSelectedRowGreyedOut(String teamMember) {
        List<WebElement> rows = teamMembersTable.findElements(By.xpath(ROW_XPATH));
        int index = 1;
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.xpath(String.format(ROW_PARAMETRIZED_XPATH, index)));
            if ((cells.get(0).getText()).contains(teamMember)) {
                return row.getAttribute("class").contains("selected");
            } else {
                ++index;
                continue;
            }
        }
        throw new NotFoundException(String.format("Team Member with name = %s has not been found", teamMember));
    }

    public boolean isAnyRowSelectedInTeamMembersTable() {
        List<WebElement> rows = teamMembersTable.findElements(By.xpath(ROW_XPATH));
        int index = 1;
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.xpath(String.format(ROW_PARAMETRIZED_XPATH, index)));
            if ((cells.get(0).isSelected())) {
                return true;
            } else {
                ++index;
                continue;
            }
        }
        return false;
    }

    public boolean isTeamMembersTableEmpty() {
        CommonUtils.fluentWaitForObject(By.xpath(TABLE_XPATH));
        List<WebElement> rows = teamMembersTable.findElements(By.xpath(ROW_XPATH));
        if (rows.isEmpty())
            return true;
        else
            return false;
    }

    public void insertTextIntoStoreNumberField(String text) {
        storeNumber.sendKeys(text);
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
