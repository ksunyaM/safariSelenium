/*******************************************************************************
 * Copyright 2019 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.teammember;

import org.junit.Assert;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.util.Objects;

public class LicenseAndCertificatesDialog extends LoadableComponent<LicenseAndCertificatesDialog> {

    @FindBy(xpath = "//div[contains(., 'Add a license or certification')]")
    private WebElement addLicenseOrCertTitle;

    private WebDriver driver;

    public LicenseAndCertificatesDialog(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isAddLicenseOrCertTitleDisplayed() {
        return addLicenseOrCertTitle.isDisplayed();
    }

    @Override
    protected void isLoaded() throws Error {
        if (Objects.isNull(addLicenseOrCertTitle)) {
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
