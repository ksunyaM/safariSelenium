/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.orders;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;


public class DetailsPage extends LoadableComponent<DetailsPage>  {

	@FindBy(xpath = "//mat-icon [contains(text(),'close')]")
	private WebElement closeButton;
	@FindBy(xpath = "//mat-dialog-container[contains(@id , 'mat-dialog')]//child::div[contains(@class,'patient-name')]")
	private WebElement nameField;
	@FindBy(xpath = "//mat-dialog-container[contains(@id , 'mat-dialog')]//child::p[contains(text(),'Gender')]/following-sibling::p")
	private WebElement genderField;
	@FindBy(xpath = "//mat-dialog-container[contains(@id , 'mat-dialog')]//child::p[contains(text(),'Date of birth')]/following-sibling::p")
	private WebElement dateOfBirthField;
	@FindBy(xpath = "//mat-dialog-container[contains(@id , 'mat-dialog')]//child::p[contains(text(),'Age')]/following-sibling::p")
	private WebElement ageField;
	@FindBy(xpath = "//mat-dialog-container[contains(@id , 'mat-dialog')]//child::p[contains(text(),'Phone ')]/following-sibling::p")
	private WebElement phoneField;
	@FindBy(id = "mat-tab-label-0-0")
	private WebElement patientDetailsTab;
	@FindBy(id = "mat-tab-label-0-1")
	private WebElement preferencesTab;
	@FindBy(id = "mat-tab-label-0-2")
	private WebElement clinicalInformationTab;
	@FindBy(id = "mat-tab-label-0-3")
	private WebElement prescriptionsTab;
	@FindBy(xpath = "//mat-card-title[contains(text(),'ADDRESS')]/following-sibling::div[contains(@class,'contact-container')]")
	private WebElement addressContainer;
	@FindBy(xpath = "//mat-card-title[contains(text(),'PHONE')]/following-sibling::div[contains(@class,'contact-container')]")
	private WebElement phoneContainer;
	@FindBy(xpath = "//div/p[contains(text(),'Home')]/following-sibling::p")
	private WebElement firstMobileField;
	@FindBy(xpath = "//div/p[contains(text(),'Mobile')]/following-sibling::p")
	private WebElement secondMobileField;
	@FindBy(xpath = "//mat-card-title[contains(text(),'ADDRESS')]/following-sibling::div[contains(@class,'contact-container')]//p[text()!='']")
	private WebElement firstHomeField;
	@FindBy(xpath = "//mat-card-title[contains(text(),'ADDRESS')]/following-sibling::div[contains(@class,'contact-container')]//following-sibling::p[text()!='']")
	private WebElement secondHomeField;

	private WebDriver driver;
	
	public DetailsPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public String getName() {
		return nameField.getText();
	}
	
	public String getGender() {
		return genderField.getText();
	}
	
	public String getDateOfBirth() {
		return dateOfBirthField.getText();
	}
	
	public String getAge() {
		return ageField.getText();
	}
	
	public String getPhoneFromField() {
		return phoneField.getText();
	}
	
	public String getAddressContainerText() {
		return addressContainer.getText();
	}
	
	public String getPhoneContainerText() {
		return phoneContainer.getText();
	}
	
	public String getFirstMobileText() {
		return firstMobileField.getText(); //it can return null
	}
	
	public String getSecondMobileText() {
		return secondMobileField.getText(); //it can return null
	}
	
	public String getFirstHomeText() {
		return firstHomeField.getText(); //it can return null
	}
	
	public String getSecondHomeText() {
		return secondHomeField.getText(); //it can return null
	}	
	
	public void goToPatientDetails() {
		patientDetailsTab.click();
	}
	
	public void goToPreferences() {
		preferencesTab.click();
	}
	
	public ClinicalInformationPage goToClinicalInformation() {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(clinicalInformationTab));
		clinicalInformationTab.click();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		return new ClinicalInformationPage(driver);
	}
	
	public boolean containsAddress(String address) {
		return addressContainer.getText().contains(address);
	}
	
	public boolean containsPhone(String phone) {
		return phoneContainer.getText().contains(phone);
	}
	public WebElement getPatientDetailsTab() {
		return patientDetailsTab;
	}
	
	@Override
	protected void load() {
		PageFactory.initElements(driver, this);
	}

	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(patientDetailsTab)) {
			throw new Error();
		}
		try {
		} catch (NoSuchElementException e) {
			Assert.fail("Page is not loaded!");
		}
		
	}

}
