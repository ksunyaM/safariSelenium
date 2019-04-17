/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.stockplus;

import static com.oneleo.test.automation.core.UIUtils.ui;

import java.util.List;
import java.util.Objects;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

public class CustomGridSettingsPage extends LoadableComponent<CustomGridSettingsPage> {

	@FindBy(id = "rx-grid-settings-save-button")
	private WebElement saveButton;
	@FindBy(id = "rx-grid-management-create-button")
	private WebElement createButton;
	@FindBy(id = "page-header-back-button")
	private WebElement backButton;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-grid-settings > div > rx-page-header")
	private WebElement header;
	@FindBy(id = "rx-grid-settings-grid-name")
	private WebElement inputGridName;
	@FindBy(xpath="//*[@id='rx-corporate-user-app-main-container']/dashboard-view/div/div/div[2]/rx-grid-settings/div/div/div/div[1]/div/div[1]/mat-card/mat-card-content/form/mat-form-field[1]/div/div[1]/div/label")
	private WebElement placeholderGridName;
	@FindBy(id = "rx-grid-settings-grid-description")
	private WebElement textAreaDescription;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-grid-settings > div > div > div > div.blocks > div > div.grid-details > mat-card > mat-card-content > form > mat-form-field.grid-description.margin-top-4x.mat-input-container.mat-form-field.ng-tns-c6-7.mat-form-field-type-mat-input.mat-form-field-can-float.mat-form-field-should-float.mat-primary.ng-untouched.ng-pristine.ng-valid > div > div.mat-input-flex.mat-form-field-flex > div > label")
	private WebElement placeholderDescription;
	@FindBy(id = "rx-grid-settings-grid-start-date")
	private WebElement inputStartDate;
	@FindBy(id = "rx-autocomplete-chip-input")
	private WebElement inputAssociations;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-grid-settings > div > div > div > div.grid-settings-view > mat-card > mat-card-content > div > div.item.grid-list-block > div > div:nth-child(1) > mat-grid-list")
	private WebElement matGridList;
	@FindBy(css = "#mat-hint-0")
	private WebElement counter;
	@FindBy(id = "rx-grid-settings-frequency")
	private WebElement frequencyInput;
	@FindBy(id = "rx-grid-settings-cost")
	private WebElement costInput;
	@FindBy(id="mat-autocomplete-0")
	private WebElement templateAssociationAutoComplete;
	@FindBy(css="#rx-grid-settings-autocomplete-chips > div > mat-chip-list > div > mat-chip")
	private WebElement groupSelected0;
	@FindBy(css="#rx-grid-settings-autocomplete-chips > div > mat-chip-list > div > mat-chip:nth-child(2)")
	private WebElement groupSelected1;
	@FindBy(css="#mat-option-30")
	private WebElement groupDisabled;
	@FindBy(id="frequency_4")
	private WebElement frequencyWeekly;
	@FindBy(id="cost_2")
	private WebElement costWeekly;
	@FindBy(css="#rx-grid-association-exists-dialog > mat-dialog-content > p")
	private WebElement errorSavingMessageDuplicateGird;
	@FindBy(css="#rx-grid-settings-apply-button")
	private WebElement applyButton;
	@FindBy(css="#dialog-confirm-button")
	private WebElement confirmApplyButton;
	@FindBy(css="#dialog-container > div.mat-dialog-content.mat-body-1.margin-bottom-3x > div")
	private WebElement alertMessageGroupAssociation;
	@FindBy(id="rx-grid-settings-update-parameters-button")
	private WebElement updateParametersButton;
	
	@FindBy(css="#rx-grid-association-exists-dialog > mat-dialog-content > p")
	private WebElement popupNoSaveErrorMessage;
	
	private WebDriver driver;

	public CustomGridSettingsPage(WebDriver driver) {
		this.driver = driver;
	}
	
	/*
	 * Template disabled
	 */
	public String checkGroupDisabled(String group){
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(templateAssociationAutoComplete), 30);
		String check = null;
		List<WebElement> groupList = templateAssociationAutoComplete.findElements(By.className("mat-option"));
		for (WebElement item : groupList) {
			if (item.findElement(By.cssSelector("span")).getText().equals(group)) {
				check = item.getAttribute("aria-disabled");
			}
		}
		return check;
	}
	
	/*
	 * Template associations
	 */
	public String getOneGroup(String group) {
		String storeSelected = null;
		List<WebElement> groupList = templateAssociationAutoComplete.findElements(By.className("mat-option"));
		for (WebElement item : groupList) {
			if (item.findElement(By.cssSelector("span")).getText().equals(group)) {
				storeSelected = item.findElement(By.cssSelector("span")).getText();
			}
		}
		return storeSelected;
	}
	
	public void clickOneGroup(String group) {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(templateAssociationAutoComplete), 30);
		List<WebElement> groupList = templateAssociationAutoComplete.findElements(By.className("mat-option"));
		for (WebElement item : groupList) {
			if (item.findElement(By.cssSelector("span")).getText().equals(group)) {
				item.findElement(By.cssSelector("span")).click();
			}
		}
	}

	/*
	 * Check placeholders
	 * 
	 */
	public String getPlaceHolderGridNameContent() {
		return placeholderGridName.getText();
	}
	
	public String getAlertMessageContent() {
		return alertMessageGroupAssociation.getText();
	}
	
	public String getErrorSavingMessageDuplicateGird() {
		return errorSavingMessageDuplicateGird.getText();
	}
	
	public String getPlaceHolderDescription() {
		return placeholderDescription.getText();
	}
	
	public String getPlaceholderInputAssociations() {
		return inputAssociations.getAttribute("placeholder").toString();
	}

	/*
	 * Content in the fields checks
	 * 
	 */
	public String getInputGridNameContent() {
		return inputGridName.getText();
	}
	
	public String getValueGridNameContent() {
		return inputGridName.getAttribute("value").toString();
	}

	public String getInputAssociations() {
		return inputAssociations.getText();
	}

	public String getInputTextAreaDescription() {
		return textAreaDescription.getText();
	}
	
	public String getValueTextAreaDescription() {
		return textAreaDescription.getAttribute("value").toString();
	}

	public void insertGridName(String gridName) {
		inputGridName.sendKeys(gridName);
	}

	public void insertTextAreaDescription(String gridName) {
		textAreaDescription.sendKeys(gridName);
	}

	public String getInputStartDate() {
		return inputStartDate.getText();
	}
	
	public void insertAssociations(String groupName) {
		inputAssociations.sendKeys(groupName);
	}
	
	public String getGridAssociation() {
		return groupSelected0.getText();
	}
	
	public String getGroupAssociation() {
		return groupSelected1.getText();
	}
	
	public void insertFrequencyInput(String frequency) {
		frequencyInput.clear();
		frequencyInput.sendKeys(frequency);
	}

	public void insertCostInput(String cost) {
		costInput.clear();
		costInput.sendKeys(cost);
	}
	
	public void insertFrequencyWeeklyInput(String frequency) {
		frequencyWeekly.clear();
		frequencyWeekly.sendKeys(frequency);
	}

	public void insertCostWeeklyInput(String cost) {
		costWeekly.clear();
		costWeekly.sendKeys(cost);
	}
	
	
	public String getValueCostWeeklyInput() {
		return costWeekly.getAttribute("value").toString();
	}
	
	public String getFrequencyWeeklyInput() {
		return frequencyWeekly.getAttribute("value").toString();
	}
	
	public void insertCostRange (Integer i, String CostRange) throws InterruptedException {
	    WebElement insertCostRange = driver.findElement	(By.xpath("//*[@id='cost_"+i+"']"));
        insertCostRange.sendKeys(CostRange);
	}
	
	/*
	 * Displayed fields checks
	 * 
	 */
	public Boolean isBackButtonDisplayed() {
		return backButton.isDisplayed();
	}

	public Boolean isHeaderDisplayed() {
		return header.isDisplayed();
	}

	public Boolean isInputGridNameDisplayed() {
		return inputGridName.isDisplayed();
	}

	public Boolean isTextAreaDescriptionDisplayed() {
		return textAreaDescription.isDisplayed();
	}

	public Boolean isInputStartDateDisplayed() {
		return inputStartDate.isDisplayed();
	}

	public Boolean isInputAssociationsDisplayed() {
		return inputAssociations.isDisplayed();
	}

	public Boolean isMatGridListDisplayed() {
		return matGridList.isDisplayed();
	}

	public Boolean isCounterDisplayed() {
		return counter.isDisplayed();
	}

	public Boolean isFrequencyInputDisplayed() {
		return frequencyInput.isDisplayed();
	}

	public Boolean isCostInputDisplayed() {
		return costInput.isDisplayed();
	}
	
	/*
	 * Hot keys
	 * 
	 */
	public void leaveInputGridName() {
		inputGridName.sendKeys(Keys.TAB);
	}
	
	public void leaveFrequencyInput() {
		frequencyInput.sendKeys(Keys.TAB);
	}
	
	public void leaveCostInput() {
		costInput.sendKeys(Keys.TAB);
	}
	
	/*
	 * Click Buttons
	 * 
	 */
	
	public void clickSaveButton() {
		saveButton.click();
	}
	
	public void clickApplyButton() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(applyButton), 10);
		applyButton.click();
	}
	
	public void clickConfirmApplyButton() {
		confirmApplyButton.click();
	}
	
	public Boolean selectActiveGrid() {
		boolean found = false;
		List<WebElement> grids = matGridList.findElements(By.cssSelector("mat-grid-tile"));
		for(WebElement grid : grids) {
			if(grid.findElement(By.cssSelector("mat-grid-tile > figure")).getText().equals("Y")) {
				grid.click();
				found = true;
				break;
			}
		}
		return found;
	}
	
	public CellParametersPopup clickUpdateParametersButton() {
		updateParametersButton.click();
		return new CellParametersPopup(driver);
	}
	
	/*
	 * Errors message
	 */
	public String getPopupNoSaveErrorMessage() {
		return popupNoSaveErrorMessage.getText();
	}
	
	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(saveButton)) {
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
