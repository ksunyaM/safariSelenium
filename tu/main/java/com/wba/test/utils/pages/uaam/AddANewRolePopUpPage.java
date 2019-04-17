/*******************************************************************************
 * Copyright 2019 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.uaam;

import java.util.Objects;

import org.junit.Assert;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

public class AddANewRolePopUpPage extends LoadableComponent<AddANewRolePopUpPage> {

    @FindBy(xpath = "//*[@id=\"cdk-overlay-0\"]/mat-dialog-container/app-headline-dialog/h3")
    private WebElement addRoleTitlePopup;
    @FindBy(xpath = "//*[@id=\"cdk-overlay-0\"]/mat-dialog-container/app-headline-dialog/div[2]/button[2]")
    private WebElement saveButton;

    private WebDriver driver;

    public AddANewRolePopUpPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getPopUpTitle() {
        return addRoleTitlePopup.getText();
    }

    public RolesAndPermissionsPage clickSaveButton() throws InterruptedException {
        saveButton.click();
        synchronized (this) {
            wait(10000);
        }
        return new RolesAndPermissionsPage(driver);
    }

    @Override
    protected void load() {
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void isLoaded() throws Error {
        if (Objects.isNull(addRoleTitlePopup)) {
            throw new Error();
        }
        try {
        } catch (NoSuchElementException e) {
            Assert.fail("Page is not loaded!");
        }
    }

}
