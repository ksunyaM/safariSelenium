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
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

public class CustomGridUpdatePage extends LoadableComponent<CustomGridUpdatePage>{

	@FindBy(id = "rx-grid-settings-grid-name")
	private WebElement inputGridName;
	@FindBy(id = "rx-grid-settings-grid-description")
	private WebElement textAreaDescription;
	@FindBy(xpath="//*[@id='mat-error-1']/div[1]")
	private WebElement missingGridNameErrorMessage;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-grid-settings > div > div > div > div.blocks > div > div.grid-details > mat-card > mat-card-content > form > mat-form-field.margin-top-2x.mat-input-container.mat-form-field.ng-tns-c6-8.mat-form-field-type-mat-input.mat-form-field-can-float.mat-form-field-should-float.mat-primary.ng-invalid.ng-dirty.mat-input-invalid.mat-form-field-invalid.ng-touched > div > div.mat-input-subscript-wrapper.mat-form-field-subscript-wrapper")
	private WebElement wrongDateErrorMessage;
	@FindBy(id="rx-grid-settings-grid-start-date")
	private WebElement startDate;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-grid-settings > div > div > div > div.blocks > div > div.grid-details > mat-card > mat-card-content > form > mat-form-field.margin-top-2x.mat-input-container.mat-form-field.ng-tns-c6-8.mat-form-field-type-mat-input.mat-form-field-can-float.mat-form-field-should-float.mat-primary.ng-invalid.ng-dirty.mat-input-invalid.mat-form-field-invalid.ng-touched > div > div.mat-input-flex.mat-form-field-flex > div.mat-input-infix.mat-form-field-infix > label")
	private WebElement startDatePlaceHolder;
	@FindBy(id = "rx-autocomplete-chip-input")
	private WebElement inputAssociations;
	@FindBy(css="#mat-option-0")
	private WebElement productGroupSection;
	@FindBy(css="#mat-option-12")
	private WebElement storeGroupSection;
	@FindBy(css="#rx-grid-settings-autocomplete-chips > div > mat-chip-list > div > mat-chip > mat-icon")
	private WebElement buttonToCancelAssociation;
	@FindBy(css="#rx-grid-settings-autocomplete-chips > div > mat-chip-list > div > mat-chip")
	private WebElement selectedAssociation;
	@FindBy(id="mat-autocomplete-0")
	private WebElement templateAssociationAutoComplete;
	@FindBy(css="#mat-error-3 > div")
	private WebElement noAssociationsErrorMessage;
	@FindBy(id = "rx-grid-settings-save-button")
	private WebElement saveButton;
	@FindBy(id="mat-error-4")
	private WebElement errorIcon;
	@FindBy(css="#frequency_2")
	private WebElement frequencyInput;
	@FindBy(id="cost_2")
	private WebElement costInput;
	@FindBy(id="rx-grid-settings-frequency")
	private WebElement gridSettingsFrequency;
	@FindBy(id="rx-grid-settings-cost")
	private WebElement gridSettingsCost;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-grid-settings > div > div > div > div.grid-settings-view > mat-card > mat-card-content > div > div.item.grid-list-block > div > div:nth-child(1) > mat-grid-list")
	private WebElement grid;
	@FindBy(id="rx-grid-settings-apply-button")
	private WebElement applyButton;
	@FindBy(id="dialog-confirm-button")
	private WebElement confirmButtonYes;
	@FindBy(id="dialog-cancel-button")
	private WebElement cancelButtonNo;
	@FindBy(id="rx-grid-settings-increase-frequency-button")
	private WebElement increaseFrequencyButton;
	@FindBy(id="rx-grid-settings-increase-cost-button")
	private WebElement increaseCostButton;
	@FindBy(id="frequency_4")
	private WebElement frequencyWeekly;
	@FindBy(id="cost_2")
	private WebElement costWeekly;
	@FindBy(css="#dialog-container > div.mat-dialog-content.mat-body-1.margin-bottom-3x > div")
	private WebElement popupApplyMessage;
	@FindBy(css="#dialog-confirm-button")
	private WebElement confirmYes;
	@FindBy(css="#rx-grid-settings-cancel-button")
	private WebElement cancelButton;
	@FindBy(id="rx-grid-settings-update-parameters-button")
	private WebElement updateParametersButton;
	@FindBy(css="#rx-grid-association-exists-dialog > mat-dialog-content > p")
	private WebElement popupNoSaveErrorMessage;
	
	@FindBy(css="#rx-grid-settings-autocomplete-chips > div > mat-chip-list > div > mat-chip > mat-icon")
	private WebElement deleteTemplateButton;
	
	private WebDriver driver;

    public CustomGridUpdatePage(WebDriver driver) {
        this.driver = driver;
    }
    
    /*
	 * Content in the fields checks
	 * 
	 */
    public List<WebElement> getGridElementsWhenAdded() {
    	List <WebElement> listOfGridElements = grid.findElements(By.className("mat-grid-tile"));
    	ui().waitForCondition(driver, ExpectedConditions.numberOfElementsToBeMoreThan(By.className("mat-grid-tile"), listOfGridElements.size()), 10);
    	return grid.findElements(By.className("mat-grid-tile"));
    }
    
    public List<WebElement> getGridElements() {
    	List <WebElement> listOfGridElements = grid.findElements(By.className("mat-grid-tile"));
    	ui().waitForCondition(driver, ExpectedConditions.numberOfElementsToBe(By.className("mat-grid-tile"), listOfGridElements.size()), 10);
    	return grid.findElements(By.className("mat-grid-tile"));
    }
    
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
	
	public String getInputGridNameContent() {
		return inputGridName.getText();
	}
	
	public String getValueGridNameContent() {
		return inputGridName.getAttribute("value").toString();
	}
	
	public void insertGridName(String gridName) {
		inputGridName.clear();
		inputGridName.sendKeys(gridName);
	}
	
	public void cancelGridName() {
		inputGridName.clear();
	}
	
	public void cancelStartDate() {
		startDate.clear();
	}
	
	public String getInputTextAreaDescription() {
		return textAreaDescription.getText();
	}
	
	public String getValueTextAreaDescription() {
		return textAreaDescription.getAttribute("value").toString();
	}
	
	public void insertTextAreaDescription(String gridName) {
		textAreaDescription.clear();
		textAreaDescription.sendKeys(gridName);
	}
	
	public void insertStartDate(String date) {
		startDate.clear();
		startDate.sendKeys(date);
	}
	
	public void insertAssociations(String groupName) {
		deleteTemplateButton.click();
		inputAssociations.sendKeys(groupName);
	}
	
	public void clearAssociations() {
		buttonToCancelAssociation.click();
	}
	 
    public GridManagementPage confirmSaveGrid() {
    	confirmYes.click();
    	return new GridManagementPage(driver);
    }
	
	public String getSelectedAssociation() {
		return selectedAssociation.getText();
	}
	
	public void insertFrequencyInput(String frequency) {
		frequencyInput.sendKeys(frequency);
	}
	
	public void deleteFrequencyInput() {
		frequencyInput.clear();
	}
	
	public String getFrequencyInput() {
		return frequencyInput.getAttribute("value").toString();
	}
	
	public void insertCostInput(String cost) {
		costInput.sendKeys(cost);
	}
	
	public void deleteCostInput() {
		costInput.clear();
	}
	
	public String getCostInput() {
		return costInput.getAttribute("value").toString();
	}
	
	public void insertGridSettingsFrequency(String frequency) {
		gridSettingsFrequency.sendKeys(frequency);
	}
	
	public void deleteGridSettingsFrequency() {
		gridSettingsFrequency.clear();
	}
	
	public void insertGridSettingsCost(String cost) {
		gridSettingsCost.sendKeys(cost);
	}
	
	public void deleteGridSettingsCost() {
		gridSettingsCost.clear();
	}
	
	public String getGridSettingsFrequency() {
		return gridSettingsFrequency.getAttribute("ng-reflect-model");
	}
	
	public String getGridSettingsCost() {
		return gridSettingsCost.getAttribute("ng-reflect-model");
	}
	
	public void insertCostWeeklyInput(String cost) {
		costWeekly.clear();
		costWeekly.sendKeys(cost);
	}
	
	public void insertFrequencyWeeklyInput(String frequency) {
		frequencyWeekly.clear();
		frequencyWeekly.sendKeys(frequency);
	}
	
	public String getValueCostWeeklyInput() {
		return costWeekly.getAttribute("value").toString();
	}
	
	public String getFrequencyWeeklyInput() {
		return frequencyWeekly.getAttribute("value").toString();
	}
	
	public String getPopupApplyMessage() {
		return popupApplyMessage.getText();
	}
	
	/*
	 * Click elements
	 */
	
	public CellParametersPopup clickUpdateParametersButton() {
		updateParametersButton.click();
		return new CellParametersPopup(driver);
	}
	
	public void clickOneGroup(String group) {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(templateAssociationAutoComplete), 30);
		List<WebElement> groupList = templateAssociationAutoComplete.findElements(By.className("mat-option"));
		for (WebElement item : groupList) {
			if (item.findElement(By.cssSelector("span")).getText().equals(group)) {
				item.findElement(By.cssSelector("span")).click();
				break;
			}
		}
	}
	
	public Boolean selectActiveGrid() {
		boolean found = false;
		List<WebElement> grids = grid.findElements(By.cssSelector("mat-grid-tile"));
		for(WebElement grid : grids) {
			if(grid.findElement(By.cssSelector("mat-grid-tile > figure")).getText().equals("Y")) {
				grid.click();
				found = true;
				break;
			}
		}
		return found;
	}
	
	public void saveButtonClick() {
		saveButton.click();
	}
	
	public void cancelButtonClick() {
		cancelButton.click();
	}
	
	public void applyButtonClick() {
		applyButton.click();
	}
	
	public void confirmYesButtonClick() {
		confirmButtonYes.click();
	}
	
	public void cancelNoButtonClick() {
		cancelButtonNo.click();
	}
	
	public void increaseFrequencyClick() {
		increaseFrequencyButton.click();
	}
	
	public void increaseCostClick() {
		increaseCostButton.click();
	}
	
	/*
	 * error messages
	 */
	public String getPopupNoSaveErrorMessage() {
		return popupNoSaveErrorMessage.getText();
	}
	
	public String getMissingGridNameErrorMessage(){
		return missingGridNameErrorMessage.getText();
	}
	
	public String getWrongDateErrorMessage(){
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(wrongDateErrorMessage.findElement(By.cssSelector("#mat-error-2 > div:nth-child(1)"))), 20);
		 return wrongDateErrorMessage.findElement(By.cssSelector("#mat-error-2 > div:nth-child(1)")).getText();
	}
	
	public String getPastDateErrorMessage(){
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(wrongDateErrorMessage.findElement(By.cssSelector("#mat-error-2 > div:nth-child(1)"))), 20);
		return wrongDateErrorMessage.findElement(By.cssSelector("#mat-error-2 > div:nth-child(1)")).getText();
	}
	
	public String noAssociationsErrorMessage(){
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(noAssociationsErrorMessage), 20);
		return noAssociationsErrorMessage.getText();
	}
	
	/*
	 * Hot keys
	 * 
	 */
	public void leaveInputGridName() {
		inputGridName.sendKeys(Keys.TAB);
	}
	
	public void leaveAssociation() {
		inputAssociations.sendKeys(Keys.TAB);
	}
	
	public void leaveStartDate() {
		startDate.sendKeys(Keys.TAB);
	}
	
	public void leaveInputGridNameByAction() throws InterruptedException {
		 Actions actions = new Actions(driver);
		 actions.moveToElement(textAreaDescription);
	}
	
	public void leaveFrequencyInput() {
		frequencyInput.sendKeys(Keys.TAB);
	}
	
	public void leaveCostInput() {
		costInput.sendKeys(Keys.TAB);
	}
	
	public void leaveGridSettingsFrequency() {
		gridSettingsFrequency.sendKeys(Keys.TAB);
	}
	
	public void leaveGridSettingsCost() {
		gridSettingsCost.sendKeys(Keys.TAB);
	}
	
	/*
	 * PlaceHolders
	 */
	public String getStartDatePlaceHolder() {
		return startDatePlaceHolder.getText();
	}
	
	/*
	 *	enabled 
	 */
	public String isProductGroupSectionDisabled() {
		return productGroupSection.getAttribute("aria-disabled");
	}
	
	public String isStoreGroupSectionDisabled() {
		return storeGroupSection.getAttribute("aria-disabled");
	}
	
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
	 * Elements displayed
	 */
	public Boolean isErrorIconEnabled() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(errorIcon), 10);
		return errorIcon.isDisplayed();
	}
	
	
    
	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(inputGridName)) {
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
