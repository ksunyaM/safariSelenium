/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.stockplus;

import static com.oneleo.test.automation.core.UIUtils.ui;

import java.util.Objects;

import org.junit.Assert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

public class ProductGroupManagementPage extends LoadableComponent<ProductGroupManagementPage>{
	
	@FindBy(id="pg-gm-delete-group-button")
	private WebElement buttonDelete;
	@FindBy(id="dialog-container")
	private WebElement popupConfirm;
	
	@FindBy(id="pg-gm-add-group-button")
	private WebElement addButtonElement;
	
	private WebDriver driver;
	
	@FindBy(id="group-information-title")
	private WebElement groupInformationTitle;
	
	@FindBy(id="product-group-name")
	private WebElement inputProductGroupName;
	

	
    public ProductGroupManagementPage(WebDriver driver) {
        this.driver = driver;
    }
    
	public void clickDeleteButton() {
		buttonDelete.click();
	}
	
	public void clickAddButton() {
		addButtonElement.click();
	}
	
	public void clickCanelButton() {
		buttonDelete.click();
	}
	
	public Boolean isPopupConfirmDisplayed() {
		return popupConfirm.isDisplayed();
	}
	
	public Boolean isGroupInformationTitle() {
		return groupInformationTitle.isDisplayed();
	}
	
	public Boolean isAddButtonDisplayed() {
		return addButtonElement.isDisplayed();
	}
	
	public Boolean isInputProductGroupNameDisplayed() {
		return inputProductGroupName.isDisplayed();
	}
	
	public void cancelByHotKeys() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(buttonDelete));
		Actions keyAction = new Actions(driver);
        keyAction.keyDown(Keys.ALT).sendKeys("d").perform();
	}
	
	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(buttonDelete)) {
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


