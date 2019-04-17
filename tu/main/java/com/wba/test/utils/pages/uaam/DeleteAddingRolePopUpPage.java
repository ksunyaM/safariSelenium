/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.uaam;

import static com.oneleo.test.automation.core.UIUtils.ui;

import java.util.List;
import java.util.Objects;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

public class DeleteAddingRolePopUpPage extends LoadableComponent<DeleteAddingRolePopUpPage> {

    @FindBy(xpath = "//*[@id=\"cdk-overlay-0\"]/mat-dialog-container")
    private WebElement deletionpopup;
    @FindBy(xpath = "//*[@id=\"cdk-overlay-0\"]/mat-dialog-container/app-headline-dialog/h3")
    private WebElement topinfomessage;
    @FindBy(xpath = "//*[@id=\"cdk-overlay-0\"]/mat-dialog-container/app-headline-dialog/div/button[1]")
    private WebElement cancelbutton;
    @FindBy(xpath = "//*[@id=\"cdk-overlay-0\"]/mat-dialog-container/app-headline-dialog/div/button[2]")
    private WebElement deletebutton;

    private WebDriver driver;

    public DeleteAddingRolePopUpPage(WebDriver driver) {
        this.driver = driver;
    }

    public String contentOfTopPopUpMessage() {
        ui().waitForCondition(driver, ExpectedConditions.visibilityOf(topinfomessage));
        return topinfomessage.getText();
    }

    public boolean isCancelButtonDisplayed() {
        return cancelbutton.isDisplayed();
    }

    public RolesBeingAddedTabPage clickCancelButton() {
        cancelbutton.click();
        return new RolesBeingAddedTabPage(driver);
    }

    public boolean isDeleteButtonDisplayed() {
        return deletebutton.isDisplayed();
    }

    public RolesBeingAddedTabPage clickDeleteButton() throws InterruptedException {
        WebElement matDialog = driver.findElement(By.tagName("mat-dialog-container"));
        List<WebElement> buttons = matDialog.findElements(By.tagName("button"));
        buttons.get(1).click();
        synchronized (this) {
            wait(6000);
        }
        return new RolesBeingAddedTabPage(driver);
    }

    @Override
    protected void isLoaded() throws Error {
        if (Objects.isNull(deletebutton)) {
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
