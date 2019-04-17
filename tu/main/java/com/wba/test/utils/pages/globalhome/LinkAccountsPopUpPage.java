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

public class LinkAccountsPopUpPage extends LoadableComponent<LinkAccountsPopUpPage> {	

	@FindBy(xpath = "//*[@id=\"mat-dialog-0\"]/rx-lib-icplus-credentials-synched-exception-dialog/div/button")
	private WebElement goToWebsdlButton;
    @FindBy(xpath = "//*[@id=\"mat-dialog-0\"]/rx-lib-icplus-credentials-synched-exception-dialog/h3")
	private WebElement linkAccountsMessage;
	
	private WebDriver driver;

	public LinkAccountsPopUpPage(WebDriver driver) {
		this.driver = driver;
	}

	public boolean isLinkAccountsMessageDisplayed() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(linkAccountsMessage));
		return linkAccountsMessage.isDisplayed();
	}

	public String getLinkAccountsMessageText() {
	    return linkAccountsMessage.getText();
	}

	public boolean isGoToWebsdlButtonDisplayed() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(goToWebsdlButton));
		return goToWebsdlButton.isDisplayed();
	}

	public WebSdlAuthenticatorPage clickGoToWebsdlButton() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(goToWebsdlButton));
		goToWebsdlButton.click();
		for(String winHandle : driver.getWindowHandles()){
			driver.switchTo().window(winHandle);
		}
		return new WebSdlAuthenticatorPage(driver);
	}
	
	public boolean waitForPageToBeLoaded() {
		synchronized (this) {
			try {
				wait(1000);
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}
		}		
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(goToWebsdlButton));
		try {
			this.isGoToWebsdlButtonDisplayed();
			return true;
		}
		catch (NoSuchElementException e){			
			throw new RuntimeException(String.format("The element: %s does not exists", goToWebsdlButton.getText()));			
		}
	}	
		
	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(goToWebsdlButton)) {
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
