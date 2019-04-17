/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.uaam;

import static com.oneleo.test.automation.core.UIUtils.ui;

import java.util.Objects;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

public class AddANewRolePage extends LoadableComponent<AddANewRolePage> {

    @FindBy(xpath = "/html/body/rx-app/app-nav/app-role-details/mat-toolbar/div/mat-toolbar-row/span[1]")
    private WebElement addaroletext;
    @FindBy(xpath = "//*[@id=\"rx-admin-app-main-container\"]/app-role-details/mat-toolbar/div/mat-toolbar-row/button")
    private WebElement backbutton;
    @FindBy(xpath = "//*[@id=\"rx-admin-app-main-container\"]/app-role-details/div/app-general-info/mat-card/form/div/button[1]")
    private WebElement cancelbutton;
    @FindBy(xpath = "/html/body/rx-app/app-nav/app-role-details/div/app-general-info/mat-card/form/div[2]/button[2]")
    private WebElement savebutton;
    @FindBy(xpath = "//*[@id=\"mat-input-1\"]")
    private WebElement rolenameinputfield;
    @FindBy(xpath = "//*[@id=\"mat-input-2\"]")
    private WebElement decriptioninputfield;
    @FindBy(xpath = "//*[@id=\"mat-input-3\"]")
    private WebElement effectivedateinputfield;
    @FindBy(xpath = "//*[@id=\"rx-admin-app-main-container\"]/app-role-details/div/app-general-info/mat-card/form/mat-form-field[3]/div/div[1]/div[2]/mat-datepicker-toggle/button")
    private WebElement effectivedatepickerbutton;
    @FindBy(xpath = "//*[@id=\"rx-admin-app-main-container\"]/app-role-details/div/app-application-permissions/div")
    private WebElement addarolerightpanelmessage;
    @FindBy(xpath = "//*[@id=\"mat-tab-label-19-0\"]")
    private WebElement appfilltab;
    @FindBy(xpath = "//*[@id=\"rx-admin-app-main-container\"]/app-role-details/div/app-application-permissions/div[1]/div/a/span")
    private WebElement collapseallbutton;

    private WebDriver driver;

    public AddANewRolePage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean waitForPageToLoad() {
        synchronized (this) {
            try {
                wait(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ui().waitForCondition(driver, ExpectedConditions.visibilityOf(rolenameinputfield));
        try {
            rolenameinputfield.isDisplayed();
            return true;
        } catch (NoSuchElementException e) {
            throw new RuntimeException(String.format("The element: %s does not exists", rolenameinputfield.getText()));
        }
    }

    public boolean isAddARoleTextDisplayed() {
        return addaroletext.isDisplayed();
    }

    public boolean isBackButtonDisplayed() {
        return addaroletext.isDisplayed();
    }

    public void clickBackButton() {
        backbutton.click();
    }

    public boolean isRoleNameInputFieldDisplayed() {
        return rolenameinputfield.isDisplayed();
    }

    public void setRoleNameInputField(String rolename) {
        rolenameinputfield.sendKeys(rolename);
    }

    public boolean isDescriptionInputFieldDisplayed() {
        return decriptioninputfield.isDisplayed();
    }

    public void setDescriptionInputField(String description) {
        decriptioninputfield.sendKeys(description);
    }

    public void clickDescriptionInputField() {
        decriptioninputfield.click();
    }

    public boolean isEffectiveDateFieldDisplayed() {
        return effectivedateinputfield.isDisplayed();
    }

    public boolean isEffectiveDatePickerDisplayed() {
        return effectivedatepickerbutton.isDisplayed();
    }

    public boolean isCancelButtonDisplayed() {
        return cancelbutton.isDisplayed();
    }

    public boolean isCancelButtonEnabled() {
        return cancelbutton.isEnabled();
    }

    public boolean isSaveButtonDisplayed() {
        return savebutton.isDisplayed();
    }

    public boolean isSaveButtonEnabled() {
        return savebutton.isEnabled();
    }

    public boolean isRightPanelInfoMessageDisplayed() {
        return addarolerightpanelmessage.isDisplayed();
    }

    public boolean isAppsFieldDisplayed() {
        return false;
    }

    public boolean isFillAppTabDisplayed() {
        return appfilltab.isDisplayed();
    }

    public boolean isCollapseAllButtonDisplayed() {
        return collapseallbutton.isDisplayed();
    }

    public boolean isSelectAllCheckBoxDisplayed() {
        return false;
    }

    public boolean isErrorMessageWithGivenTextDisplayed(String text) {
        String errorMessage = driver.findElement(By.xpath("//*[@id=\"mat-error-3\"]")).getText();
        return errorMessage.equals(text);
    }

    public void fillRoleNameFieldWithGivenValue(String value) {
        rolenameinputfield.sendKeys(value);
        rolenameinputfield.sendKeys(Keys.TAB);
    }

    public void fillDescriptionFieldWithGivenValue(String value) {
        decriptioninputfield.sendKeys(value);
        decriptioninputfield.sendKeys(Keys.TAB);
    }

    public void fillEffectiveDateFieldWithGivenValue(String value) {
        effectivedateinputfield.sendKeys(value);
        effectivedateinputfield.sendKeys(Keys.TAB);
    }

    public Boolean isAdminFocused() throws InterruptedException {
        synchronized (this) {
            wait(3000);
        }
        WebElement element = driver.findElement(By.xpath("//*[@id=\"mat-tab-label-1-0\"]"));
        return element.getAttribute("class").contains("active") ? true : false;
    }

    public void checkAdminPermissionCheckbox() throws InterruptedException {
        synchronized (this) {
            wait(2000);
        }
        driver.findElement(By.xpath("//*[@id=\"mat-checkbox-2\"]")).click();
    }

    public AddANewRolePopUpPage clickSaveButton() throws InterruptedException {
        synchronized (this) {
            wait(2000);
        }
        savebutton.click();
        return new AddANewRolePopUpPage(driver);
    }

    @Override
    protected void isLoaded() throws Error {
        if (Objects.isNull(addaroletext)) {
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
