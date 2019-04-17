/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.globalhome;

import org.junit.Assert;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.util.Objects;

import static com.oneleo.test.automation.core.UIUtils.ui;

public class SupervisingPharmacistFlagPage extends LoadableComponent<SupervisingPharmacistFlagPage> {

	@FindBy(xpath = "//*[@id=\"flag-component\"]/div[1]/h1")
	private WebElement flagText;
	@FindBy(xpath = "//*[@id=\"close-icon-flag\"]")
	private WebElement closeButton;

	private WebDriver driver;

	public SupervisingPharmacistFlagPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public boolean isTextOnFlagDisplayed() {
		return flagText.isDisplayed();
	}

	public boolean isCloseButtonDisplayed() {
		return closeButton.isDisplayed();
	}

	public String getTextInfoFromFlag() {
		return flagText.getText();
	}

	public void clickOnCloseButton() {
		closeButton.click();
	}
	
	public boolean waitForPageToBeLoaded() {
		synchronized (this) {
			try {
				wait(3000);
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}
		}		
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(flagText));
		try {
			this.isTextOnFlagDisplayed();
			return true;
		}
		catch (NoSuchElementException e){			
			throw new RuntimeException(String.format("The element: %s does not exists", flagText.getText()));
		}
	}	
		
	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(flagText)) {
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
