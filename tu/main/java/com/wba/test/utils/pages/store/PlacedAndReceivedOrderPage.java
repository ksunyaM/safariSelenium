/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.store;

import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.util.List;
import java.util.Objects;

import static com.oneleo.test.automation.core.UIUtils.ui;

public class PlacedAndReceivedOrderPage extends LoadableComponent<PlacedAndReceivedOrderPage> {

	@FindBy(xpath = "//*[@id=\"form-wrapper\"]/form/button[2]")
	private WebElement clearButton;
	private WebDriver driver;



	public PlacedAndReceivedOrderPage(WebDriver driver) {
		this.driver = driver;
	}


	@Override
	protected void isLoaded() throws Error {

		if (Objects.isNull(clearButton)) {
			throw new Error();
		}
		try {
			ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(clearButton));
		} catch (NoSuchElementException e) {
			Assert.fail("Page is not loaded!");
		}

	}

	@Override
	protected void load() {
		PageFactory.initElements(driver, this);
	}

}
