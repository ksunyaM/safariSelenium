/** Copyright 2018 Walgreen Co.*/
package com.wba.test.pageobjects.pages.desktop.st6;

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

public class ReturnsManagementPage extends LoadableComponent<ReturnsManagementPage>{

	@FindBy(css="body > app-root > app-nav > app-rx-returns-management > div > app-rx-return-claim > div > mat-toolbar.page-header.mat-toolbar.mat-toolbar-single-row > span:nth-child(2)")
	private WebElement title;
	@FindBy(id="rm-table")
	private WebElement rmTable;
	@FindBy(id="claim-detail-view-button")
	private WebElement claimDetailButton;
	
	private WebDriver driver;
	
	public ReturnsManagementPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public String getTitle()  {
		return title.getText();
	}
	
	public Boolean rmTableDisplayed() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(rmTable));
		return rmTable.isDisplayed();
	}
	
	public Boolean claimDetailButtonDisplayed() {
		return claimDetailButton.isDisplayed();
	}
	
	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(title)) {
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
