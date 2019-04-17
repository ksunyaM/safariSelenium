/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.orders;

import java.util.Objects;

import org.junit.Assert;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;


public class OrderHomePage extends LoadableComponent<OrderHomePage> {
    @FindBy(xpath = "//mat-icon[contains(text(), 'menu')]")
    private WebElement menu;
    @FindBy(xpath = "//button[contains(@aria-label, 'Patient Search')]")
    private WebElement patientsearchoption;
    @FindBy(xpath = "//mat-icon[contains(text(), 'store')]")
    private WebElement rxorderstatusicon;
    @FindBy(xpath = "//mat-icon[contains(text(),'apps')]")
    private WebElement appswithermenu;
    @FindBy (xpath = "//span[contains(text(), 'Home')]")
    private WebElement homeappswither;
    @FindBy (xpath = "//span[contains(text(), 'Records')]")
    private WebElement recordsappswither;

    private WebDriver driver;

    public OrderHomePage(WebDriver driver) {
        this.driver = driver;
    }


    private void goToPatientSearchMenuOption() {
        menu.click();
        patientsearchoption.click();
    }
    private void goToOrderRxStatusMenuOption() {
        menu.click();
        rxorderstatusicon.click();
    }

    public SearchPage navigateToSearchPage() throws Throwable {
        goToPatientSearchMenuOption();
        return new SearchPage(driver);
    }


    @Override
    protected void load() {
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void isLoaded() throws Error {
        if (Objects.isNull(menu)) {
            throw new Error();
        }
        try {
        } catch (NoSuchElementException e) {
            Assert.fail("Page is not loaded!");
        }

    }
}
