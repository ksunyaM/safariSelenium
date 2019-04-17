/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.uaam;

import static com.oneleo.test.automation.core.UIUtils.ui;

import java.util.Objects;

import org.junit.Assert;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

public class ViewActiveRolePage extends LoadableComponent<ViewActiveRolePage> {

    public static String categoryxpath;
    public static String tabxpath = "/*[@id=\"%s\"]/div/app-permissions-tab/div[%d]/mat-expansion-panel/mat-expansion-panel-header/span[2]";

    @FindBy(xpath = "//*[@id=\"rx-admin-app-main-container\"]/app-role-details/mat-toolbar/div/mat-toolbar-row/span[1]")
    private WebElement viewaroleheader;
    @FindBy(xpath = "//*[@id=\"mat-input-21\"]")
    private WebElement rolename;
    @FindBy(xpath = "//*[@id=\"mat-input-22\"]")
    private WebElement description;
    @FindBy(xpath = "//*[@id=\"mat-input-23\"]")
    private WebElement apps;
    @FindBy(xpath = "//*[@id=\"mat-input-28\"]")
    private WebElement effectivedate;
    @FindBy(xpath = "//*[@id=\"mat-input-29\"]")
    private WebElement lastedited;
    @FindBy(xpath = "//*[@id=\"mat-input-24\"]")
    private WebElement lasteditedby;
    @FindBy(xpath = "//*[@id=\"rx-admin-app-main-container\"]/app-role-details/div/app-general-info/mat-card/div/button[1]")
    private WebElement editbutton;
    @FindBy(xpath = "//*[@id=\"rx-admin-app-main-container\"]/app-role-details/div/app-general-info/mat-card/div/button[2]")
    private WebElement deletebutton;
    @FindBy(xpath = "//*[@id=\"rx-admin-app-main-container\"]/app-role-details/div/app-general-info/mat-card/div/button[3]")
    private WebElement clonebutton;
    @FindBy(xpath = "//*[@id=\"mat-tab-label-5-0\"]")
    private WebElement adminapptab;
    @FindBy(xpath = "//*[@id=\"mat-tab-label-5-1\"]")
    private WebElement fillapptab;
    @FindBy(xpath = "//*[@id=\"mat-tab-label-5-0\"]/div/span")
    private WebElement adminappcounter;
    @FindBy(xpath = "//*[@id=\"mat-tab-label-5-1\"]/div/span")
    private WebElement fillappcounter;
    @FindBy(xpath = "//*[@id=\"rx-admin-app-main-container\"]/app-role-details/div/app-application-permissions/div/div/a")
    private WebElement collapseexpandall;
    @FindBy(xpath = "//*[@id=\"mat-tab-label-1-7\"]/div/div")
    private WebElement globalhomeapptab;
    @FindBy(xpath = "/html/body/app-root/app-nav/app-role-details/div/app-application-permissions/div/mat-tab-group/mat-tab-header/div[3]")
    private WebElement tabHeaderPagination;

    private WebDriver driver;

    public ViewActiveRolePage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isViewARoleTextDisplayed() {
        return viewaroleheader.isDisplayed();
    }

    public boolean isRoleNameInputFieldDisplayed() {
        return rolename.isDisplayed();
    }

    public boolean isDescriptionInputFieldDisplayed() {
        return description.isDisplayed();
    }

    public boolean isEffectiveDateFieldDisplayed() {
        return effectivedate.isDisplayed();
    }

    public boolean isAppsFieldDisplayed() {
        return apps.isDisplayed();
    }

    public void createAppXpath(String tabname) {
        switch (tabname) {
        case "ADMIN":
            this.categoryxpath = String.format(tabxpath, "mat-tab-content-5-0");
            break;
        case "FILL":
            this.categoryxpath = String.format(tabxpath, "mat-tab-content-5-1");
            break;
        }
        throw new IllegalArgumentException(String.format("The system does not handle the given tab name %s case", tabname));
    }

    public boolean isAppTabDisplayed(String tabname) {
        switch (tabname) {
        case "ADMIN":
            return adminapptab.isDisplayed();
        case "FILL":
            return fillapptab.isDisplayed();
        case "Global Home":
            return globalhomeapptab.isDisplayed();
        }
        throw new IllegalArgumentException(String.format("The system does not handle the given tab name %s case", tabname));
    }

    public String getEitherCollapseOrExpandAllButtonIsDiplayed(String buttonname) {
        if (collapseexpandall.isDisplayed())
            return collapseexpandall.getText();
        else
            throw new NoSuchElementException(String.format("Button with name: %s has not been found", buttonname));
    }

    public void clickCollapseExpandAllButton() {
        collapseexpandall.click();
    }

    @Override
    protected void isLoaded() throws Error {
        if (Objects.isNull(viewaroleheader)) {
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

    public void clickOnTabHeaderPagination() {
        ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(tabHeaderPagination));
        tabHeaderPagination.click();
    }
}
