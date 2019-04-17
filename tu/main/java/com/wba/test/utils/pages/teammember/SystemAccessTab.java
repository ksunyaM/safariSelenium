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

public class SystemAccessTab extends LoadableComponent<SystemAccessTab> {

    @FindBy(xpath = "//div[contains(text(), 'SYSTEM ACCESS']")
    private WebElement systemAccessTabTitle;
    @FindBy(xpath = "//*[@id=\"mat-tab-content-1-1\"]/div/div/app-user-roles-list/div/section/app-user-roles-list-item[1]/mat-card/div[1]/button/mat-icon")
    private WebElement cancelButton;
    @FindBy(xpath = "//span[contains(., 'ADD ACCESS')]")
    private WebElement addButton;
    @FindBy(xpath = "//*[@id=\"mat-tab-content-1-1\"]/div/div/app-user-roles-list/div")
    private WebElement accessRoleList;
    @FindBy(xpath = "//*[@id=\"mat-tab-content-1-1\"]/div/div/app-user-roles-list/div/section/app-user-roles-list-item[1]/mat-card/div[3]")
    private WebElement deletingMessage;
    @FindBy(xpath = "//div[contains(., 'Granted on')]")
    private WebElement grantedOn;
    @FindBy(xpath = "//div[contains(., 'Granted by')]")
    private WebElement grantedBy;
    
    @FindBy(xpath = "//*[@id=\"mat-tab-content-1-1\"]/div/div/app-user-roles-list/div/section/app-user-roles-list-item[1]/mat-card/div[1]/button/mat-icon")
    private WebElement RemoveAssociationDialog;
    
    private WebDriver driver;
    
    public RemoveAssociationDialog clickRemoveAssociationDialog() {
        RemoveAssociationDialog.click();
        return new RemoveAssociationDialog(driver);
    }

    public SystemAccessTab(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isSystemAccessTabTitle() {
        return systemAccessTabTitle.isDisplayed();
    }

    public boolean isCancelButtonDisplayed() {
        return cancelButton.isDisplayed();
    }
    
    public boolean isAddButtonDisplayed() {
        return addButton.isDisplayed();
    }
    
    public boolean isAccessRoleListDisplayed() {
        return accessRoleList.isDisplayed();
    }
    
    public boolean isDeletingMessageDisplayd() {
       return deletingMessage.isDisplayed(); 
    }

    public boolean isGrantedOnDisplayd() {
        return grantedOn.isDisplayed(); 
    }
    
    public boolean isGrantedByDisplayd() {
        return grantedBy.isDisplayed(); 
    }

    @Override
    protected void isLoaded() throws Error {
        if (Objects.isNull(systemAccessTabTitle)) {
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
