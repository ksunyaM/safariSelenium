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

public class EditAddingRolePage extends LoadableComponent<EditAddingRolePage>  {
	
	@FindBy(xpath = "//*[@id=\"rx-admin-app-main-container\"]/app-role-details/mat-toolbar/div/mat-toolbar-row/button")
	private WebElement backbutton;
	@FindBy(xpath = "//*[@id=\"rx-admin-app-main-container\"]/app-role-details/mat-toolbar/div/mat-toolbar-row/span[1]")
	private WebElement editroletext;
	
	private WebDriver driver;

	public EditAddingRolePage(WebDriver driver) {
		this.driver = driver;
	}
	
	public boolean isEditARoleTextDisplayed() {
		return editroletext.isDisplayed();
	}
	
	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(backbutton)) {
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
