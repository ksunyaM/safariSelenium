/*******************************************************************************
 * Copyright 2018 Walgreen Co.
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


public class EditDeletingRolePopUpPage extends LoadableComponent<EditDeletingRolePopUpPage>  {
	
	@FindBy(xpath = "//*[@id=\"cdk-overlay-0\"]/mat-dialog-container/app-date-picker-dialog/h3")
	private WebElement popUpTitle;
	@FindBy(xpath = "//*[@id=\"cdk-overlay-2\"]/mat-dialog-container/app-date-picker-dialog/form/mat-form-field/div/div[1]/div[1]")
	private WebElement scheduleDeletionDate;
	
	
	private WebDriver driver;

	public EditDeletingRolePopUpPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public String getPageNameText() {
		return popUpTitle.getText();
	}
		
	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(popUpTitle)) {
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
