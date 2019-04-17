/*******************************************************************************
 * Copyright 2019 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.teammember;

import java.util.Objects;

import org.junit.Assert;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

public class RemoveAssociationDialog extends LoadableComponent<RemoveAssociationDialog> {

    @FindBy(xpath = "//div[contains(., 'Delete this access level?')]")
    private WebElement removePageHeader;
    @FindBy(xpath = "//div[contains(., 'CANCEL')]")
    private WebElement cancel;
    @FindBy(xpath = "//div[contains(., 'DELETE')]")
    private WebElement delete;    
    
    private WebDriver driver;
    
    public RemoveAssociationDialog(WebDriver driver) {
        this.driver = driver;
    }
    
    public boolean isRemovePageHeaderDisplayed() {
        return removePageHeader.isDisplayed(); 
    }
    
    public boolean isCancelDisplayed() {
        return cancel.isDisplayed(); 
    }
    
    public boolean isDeleteDisplayed() {
        return delete.isDisplayed(); 
    }
    
    public void clickCancel() {
        cancel.click();
    }
    
    public void clickDelete() {
        delete.click();
    }
    
    @Override
    protected void isLoaded() throws Error {
        if (Objects.isNull(removePageHeader)) {
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
