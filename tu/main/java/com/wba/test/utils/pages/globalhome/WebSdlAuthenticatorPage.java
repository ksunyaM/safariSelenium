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

public class WebSdlAuthenticatorPage extends LoadableComponent<WebSdlAuthenticatorPage> {

	@FindBy(xpath = "/html/body/form/table/tbody/tr[2]/td/h3")
	private WebElement webSdlText;

	private WebDriver driver;

	public WebSdlAuthenticatorPage(WebDriver driver) {
		this.driver = driver;
	}

	public boolean isWebSdlTextDisplayed() {
		return webSdlText.isDisplayed();
	}

	public boolean waitForPageToBeLoaded() {
		synchronized (this) {
			try {
				wait(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(webSdlText));
		try {
			this.isWebSdlTextDisplayed();
			return true;
		}
		catch (NoSuchElementException e){
			throw new RuntimeException(String.format("The element: %s does not exists", webSdlText.getText()));
		}
	}

	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(webSdlText)) {
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
