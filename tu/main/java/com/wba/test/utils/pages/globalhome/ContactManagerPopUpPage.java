/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.globalhome;

import static com.oneleo.test.automation.core.UIUtils.ui;

import java.util.Objects;

import org.junit.Assert;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

public class ContactManagerPopUpPage extends LoadableComponent<ContactManagerPopUpPage> {	

	@FindBy(xpath = "//*[@id=\"mat-dialog-0\"]/rx-lib-no-active-roles-dialog/div/button")
	private WebElement okButton;
	@FindBy(xpath = "//*[@id=\"mat-dialog-0\"]/rx-lib-no-active-roles-dialog/p")
	private WebElement message;
	
	private WebDriver driver;

	public ContactManagerPopUpPage(WebDriver driver) {
		this.driver = driver;
	}

	public boolean isOkButtonDisplayed() {
		return okButton.isDisplayed();
	}

	public LoginPage clickOnOkButton() {
		okButton.click();
		return new LoginPage(driver);
	}

	public String getPopupMessage() {
		return message.getText();
	}
	
	public boolean waitForPageToBeLoaded() {
		synchronized (this) {
			try {
				wait(1000);
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}
		}		
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(okButton));
		try {
			this.isOkButtonDisplayed();
			return true;
		}
		catch (NoSuchElementException e){			
			throw new RuntimeException(String.format("The element: %s does not exists", okButton.getText()));			
		}
	}	
		
	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(okButton)) {
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
