/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/

package com.wba.test.utils.pages.orders;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

public class ClinicalInformationPage extends LoadableComponent<ClinicalInformationPage>{

	@FindBy(id = "mat-tab-label-0-2")
	private WebElement clinicalInformationTab;
	@FindBy(xpath = "//*[@id ='mat-list']//p")
	private List<WebElement> containersList = new ArrayList<WebElement>();
	private WebDriver driver;
	private WebElement allergiesContainer;
	private WebElement healthConditionsContainer;

	
	public ClinicalInformationPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public String allergiesText() {
		allergiesContainer = containersList.get(0);
		return allergiesContainer.getText();
	}
	
	public String healthConditionsText() {
		healthConditionsContainer = containersList.get(1);
		return healthConditionsContainer.getText();
	}

	@Override
	protected void load() {
		PageFactory.initElements(driver, this);
	}

	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(clinicalInformationTab)) {
			throw new Error();
		}
		try {
		} catch (NoSuchElementException e) {
			Assert.fail("Page is not loaded!");
		}
		
	}
}
