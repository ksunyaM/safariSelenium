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

public class ViewAddingRolePage extends LoadableComponent<ViewAddingRolePage>  {

	private static final String categoryparametrisedarrow = "//*[@id=\"mat-tab-content-8-0\"]/div/app-permissions-tab/div[%d]/mat-expansion-panel/mat-expansion-panel-header/span[1]";
	
	@FindBy(xpath = "//*[@id=\"rx-admin-app-main-container\"]/app-role-details/mat-toolbar/div/mat-toolbar-row/span[1]")
	private WebElement viewaroleheader;
	@FindBy(xpath = "//*[@id=\"rx-admin-app-main-container\"]/app-role-details/div/app-general-info/mat-card/form/div/button[1]")
	private WebElement editicon;
	@FindBy(xpath = "//*[@id=\"rx-admin-app-main-container\"]/app-role-details/div/app-general-info/mat-card/form/div/button[2]")
	private WebElement cloneicon;
	@FindBy(xpath = "//*[@id=\"rx-admin-app-main-container\"]/app-role-details/div/app-general-info/mat-card/form/div/button[3]")
	private WebElement deleteicon;
	@FindBy(xpath = "//*[@id=\"mat-input-47\"]")
	private WebElement statusfield;
	@FindBy(xpath = "//*[@id=\"mat-input-48\"]")
	private WebElement scheduledeffectivedatefield;
	@FindBy(xpath = "//*[@id=\"mat-input-49\"]")
	private WebElement lasteditedfield;
	@FindBy(xpath = "//*[@id=\"mat-input-50\"]")
	private WebElement rolenamefield;
	@FindBy(xpath = "//*[@id=\"mat-input-51\"]")
	private WebElement dexcriptionfield;
	@FindBy(xpath = "//*[@id=\"mat-input-52\"]")
	private WebElement appsfield;
	@FindBy(xpath = "//*[@id=\"mat-input-53\"]")
	private WebElement createdfield;
	@FindBy(xpath = "//*[@id=\"rx-admin-app-main-container\"]/app-role-details/mat-toolbar/div/mat-toolbar-row/span[1]")
	private WebElement apptabparametrisedxpath;
	@FindBy(xpath = "//*[@id=\"rx-admin-app-main-container\"]/app-role-details/div/app-application-permissions/div/div/a")
	private WebElement expandcollapseallbutton;
	@FindBy(xpath = "//*[@id=\"rx-admin-app-main-container\"]/app-role-details/mat-toolbar/div/mat-toolbar-row/button")
	private WebElement backbutton;
	@FindBy(xpath = "//*[@id=\"mat-tab-content-8-0\"]/div/app-permissions-tab/div[1]/mat-expansion-panel/mat-expansion-panel-header/span[1]/mat-panel-title")
	private WebElement categoryname;
	
	private WebDriver driver;

	public ViewAddingRolePage(WebDriver driver) {
		this.driver = driver;
	}
	
	public boolean isViewARoleTextDisplayed() {
		return viewaroleheader.isDisplayed();
	}
	
	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(viewaroleheader)) {
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
