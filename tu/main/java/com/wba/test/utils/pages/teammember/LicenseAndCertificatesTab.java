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

public class LicenseAndCertificatesTab extends LoadableComponent<LicenseAndCertificatesTab> {

    @FindBy(xpath = "//div[contains(text(), 'LICENSES & CERTIFICATIONS']")
    private WebElement licenseAndCertsTabTitle;
    @FindBy(xpath = "//span[contains(., 'ADD')]")
    private WebElement addButton;

    private WebDriver driver;
    private LicenseAndCertificatesDialog licenseAndCertificatesDialog;

    public LicenseAndCertificatesTab(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isLicenseAndCertsTabTitle() {
        return licenseAndCertsTabTitle.isDisplayed();
    }

    public boolean isAddButtonDisplayed() {
        return addButton.isDisplayed();
    }

    public LicenseAndCertificatesDialog clickAddButton() {
        addButton.click();
        return new LicenseAndCertificatesDialog(driver);
    }

    @Override
    protected void isLoaded() throws Error {
        if (Objects.isNull(licenseAndCertsTabTitle)) {
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
