/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.notificationAdapter;

import java.util.Objects;

import org.junit.Assert;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import com.wba.test.utils.pages.notificationAdapter.AutocompletePage;
import com.wba.test.utils.pages.notificationAdapter.SsePage;

public class AutocompletePage extends LoadableComponent<AutocompletePage>{
    
    @FindBy(xpath = "/html/body/common-showcase/div/div/main/mat-toolbar/div/mat-toolbar-row/span[1]")
    private WebElement titleAutocomplete;
    @FindBy(xpath = "//a[contains(@href,'./server-sent-event')]")
    private WebElement sseButton;
    
    private WebDriver driver;

    public AutocompletePage(WebDriver driver) {
        this.driver = driver;
    }
    
    public SsePage navigateToSsePage() {
        sseButton.click();
        return new SsePage(driver);
    }

    @Override
    protected void load() {
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void isLoaded() throws Error {
        if (Objects.isNull(titleAutocomplete)) {
            throw new Error();
        }
        try {
        } catch (NoSuchElementException e) {
            Assert.fail("Page is not loaded!");
        }
    }

}
