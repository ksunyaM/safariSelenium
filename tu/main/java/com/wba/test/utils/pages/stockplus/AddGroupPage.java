/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.stockplus;

import static com.oneleo.test.automation.core.UIUtils.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

public class AddGroupPage extends LoadableComponent<AddGroupPage> {

	@FindBy(css = "#save-button")
	private WebElement saveButton;
	@FindBy(css = "#store-group-name")
	private WebElement storeGroupName;
	@FindBy(css = "#available-store-table > div.scrollable-container.bottom-store-card > div > div:nth-child(1) > div > mat-pseudo-checkbox")
	private WebElement checkStore;
	@FindBy(css = "#add-store-button > span > mat-icon")
	private WebElement addStoreButton;
	@FindBy(css = "#page-header-back-button")
	private WebElement backButton;
	@FindBy(css = "#mat-error-2 > div:nth-child(2)")
	private WebElement errorMessageForWrongChar;
	@FindBy(css = "#mat-error-2 > div:nth-child(1)")
	private WebElement errorMessageForWrongCharOnGroupName;
	@FindBy(css = "#store-group-start-date")
	private WebElement startDate;
	@FindBy(css = "#mat-error-4 > div:nth-child(1)")
	private WebElement errorMessageStartDate;
	@FindBy(css = "#mat-error-4 > div:nth-child(2)")
	private WebElement futureDateErrorMessage;
	@FindBy(css = "#dialog-container > div.mat-dialog-content.mat-body-1.margin-bottom-3x > div")
	private WebElement popupErrorMessage;
	@FindBy(id = "available-stores-refine-value")
	private WebElement inputStoreSearch;
	@FindBy(css = "#group-store-table > div.table-header > mat-checkbox > label > div")
	private WebElement checkBoxSelectAll;
	@FindBy(css = "#available-store-table > div.table-header > mat-checkbox > label > div")
	private WebElement checkBoxSelectAllAvailableList;
	@FindBy(css = "#remove-store-button > span > mat-icon")
	private WebElement buttonRemoveStore;
	@FindBy(id = "group-stores-count")
	private WebElement textCountStores;
	@FindBy(id = "cancel-button")
	private WebElement buttonCancel;
	@FindBy(id = "dialog-confirm-button")
	private WebElement buttonYes;
	@FindBy(css = "#dialog-cancel-button")
	private WebElement buttonNo;
	@FindBy(id = "available-stores-refine-button")
	private WebElement buttonRefineSearch;
	@FindBy(css = "#group-store-table > div.scrollable-container.bottom-store-card > div")
	private WebElement tableGroupStoreList;
	@FindBy(css = "#available-store-table > div.scrollable-container.bottom-store-card > div")
	private WebElement tableAvailableStoreList;
	@FindBy(css = "#available-store-table > div.scrollable-container.bottom-store-card > div > div > div > p:nth-child(2)")
	private WebElement refinedStoreNumber;
	@FindBy(css = "#mat-error-2 > div:nth-child(3)")
	private WebElement lessCharNameErrorMessage;
	@FindBy(css = "#mat-error-3 > div:nth-child(1)")
	private WebElement descriptionErrorMessage;
	@FindBy(css = "#dialog-container > div.mat-dialog-title.margin-bottom-2x")
	private WebElement popupTitle;
	@FindBy(css = "#group-store-table > div.scrollable-container.bottom-store-card > div > div > div > p:nth-child(2)")
	private WebElement addedToListStoreNumber;
	@FindBy(css = "#dialog-confirm-button")
	private WebElement dialogConfirmButton;
	@FindBy(css = "#mat-error-2 > div:nth-child(4)")
	private WebElement duplicateGroupNameErrorMessage;
	@FindBy(css = "#available-stores-refine-criteria > div > div.mat-select-arrow-wrapper > div")
	private WebElement typeSearchComboBoxOpen;
	@FindBy(css = "#mat-option-1")
	private WebElement storeTypeSelectionOnComboBox;
	@FindBy(css = "#store-group-description")
	private WebElement descriptionTextBox;
	@FindBy(css = "#available-stores-count > p")
	private WebElement storeFoundMessage;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-add-update-store-group > div > form > div > div:nth-child(1) > mat-card > mat-card-content > div:nth-child(1) > mat-form-field.margin-right-2x.mat-input-container.mat-form-field.ng-tns-c6-12.mat-form-field-type-mat-input.mat-form-field-can-float.mat-form-field-should-float.mat-primary.ng-untouched.ng-pristine.ng-valid > div > div.mat-input-flex.mat-form-field-flex > div")
	private WebElement refineField;
	@FindBy(css = "#cdk-overlay-1 > div > div")
	private WebElement typeComboBox;
	@FindBy(css = "#dialog-confirm-button")
	private WebElement confirmAddPopupWindow;
	@FindBy(css = "#dialog-cancel-button")
	private WebElement deleteAddPopupWindow;

	private WebDriver driver;
	private String numberOfStores;

	public AddGroupPage(WebDriver driver) {
		this.driver = driver;
	}

	public void confirmAddPopupWindowClick() {
		confirmAddPopupWindow.click();
	}

	public void deleteAddPopupWindowClick() {
		deleteAddPopupWindow.click();
	}

	public void selectStoreTypeOnComboBox() {
		this.typeSearchComboBoxOpen.click();
		this.storeTypeSelectionOnComboBox.click();
	}

	public void insertGroupName(String groupName) {
		storeGroupName.sendKeys(groupName);
	}

	public void insertDescription(String description) {
		descriptionTextBox.sendKeys(description);
	}

	public String getDescription() {

		return this.descriptionTextBox.getAttribute("value");
	}

	public String getGroupName() {
		return this.storeGroupName.getAttribute("value");
	}

	public void insertRefine(String storeNumber) {
		refinedStoreNumber.sendKeys(storeNumber);
	}

	public void leaveInsertGroupName() {
		storeGroupName.sendKeys(Keys.TAB);
	}

	public void leaveDescription() {
		descriptionTextBox.sendKeys(Keys.TAB);
	}

	public void checkStore() {
		checkStore.click();
	}

	public void addStoresfromlist(List<String> storeList) {
		for (String store : storeList) {
			inputStoreSearch.clear();
			inputStoreSearch.sendKeys(store);
			buttonRefineSearch.click();
			checkStore.click();
			addStoreButton.click();
		}
	}

	public void checkStoresfromlist(List<String> storeList) {
		for (String store : storeList) {
			selectStoreFromList(store);
		}
	}

	public void addStoreClick() {
		addStoreButton.click();
	}

	public void backButtonClick() {
		backButton.click();
	}

	public GroupManagementPage save() {
		saveButton.click();
		return new GroupManagementPage(driver);
	}

	public Boolean isSaveDisplayed() {
		return saveButton.isDisplayed();
	}

	public void insertStartDate(String date) {
		this.startDate.sendKeys("");
		this.startDate.clear();
		this.startDate.sendKeys(date);
	}

	public GroupManagementPage backToGroupManagementPage() {
		this.backButton.click();
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(dialogConfirmButton));
		this.dialogConfirmButton.click();
		return new GroupManagementPage(driver);
	}

	public void clickBackToGroupManagementPageAndCancel() {
		this.backButton.click();
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(buttonNo));
		this.buttonNo.click();
	}

	public String wrongCharErrorMessage() {
		ui().waitForCondition(driver,
				ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#mat-error-2 > div:nth-child(2)")), 10);
		return this.errorMessageForWrongChar.getText();
	}

	public String wrongDescriptionErrorMessage() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(descriptionErrorMessage), 10);
		return this.descriptionErrorMessage.getText();
	}

	public String popupTitle() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(popupTitle), 10);
		return this.popupTitle.getText();
	}

	public String getDuplicateGroupNameErrorMessage() {
		ui().waitForCondition(driver,
				ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#mat-error-2 > div:nth-child(4)")), 10);
		return this.duplicateGroupNameErrorMessage.getText();
	}

	public String wrongDateErrorMessage() {
		ui().waitForCondition(driver,
				ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#mat-error-4 > div:nth-child(1)")), 10);
		return this.errorMessageStartDate.getText();
	}

	public String futureDateErrorMessage() {
		ui().waitForCondition(driver,
				ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#mat-error-4 > div:nth-child(2)")), 10);
		return this.futureDateErrorMessage.getText();
	}

	public String lessCharNameErrorMessage() {
		ui().waitForCondition(driver,
				ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#mat-error-2 > div:nth-child(3)")), 10);
		return this.lessCharNameErrorMessage.getText();
	}

	public String wrongNameErrorMessage() {
		ui().waitForCondition(driver,
				ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#mat-error-2 > div:nth-child(1)")), 10);
		return this.errorMessageForWrongCharOnGroupName.getText();
	}

	public String storeFoundMessage() {
		return this.storeFoundMessage.getText();
	}

	public String checkPopupErrorMessage() {
		return this.popupErrorMessage.getText();
	}

	public void selectAllStores() {
		checkBoxSelectAll.click();
	}

	public void removeStore() {
		buttonRemoveStore.click();
	}

	public void removeAllStores() {
		selectAllStores();
		removeStore();
	}

	public void insertStore(String storeNumber) {
		inputStoreSearch.clear();
		inputStoreSearch.sendKeys(storeNumber);
		buttonRefineSearch.click();
		checkStore.click();
	}

	public void insertWrongStore(String storeNumber) {
		inputStoreSearch.clear();
		inputStoreSearch.sendKeys(storeNumber);
		buttonRefineSearch.click();
	}

	public GroupManagementPage cancelOperation() {
		buttonCancel.click();
		buttonYes.click();
		return new GroupManagementPage(driver);
	}

	public void cancelExit() {
		buttonCancel.click();
		buttonNo.click();
	}

	public void cancelButton() {
		this.buttonCancel.click();
	}

	public String exportCountStores() throws InterruptedException {
		numberOfStores = textCountStores.getText();
		return numberOfStores;
	}

	public String getRefinedStoreNumber() {
		return this.refinedStoreNumber.getText();
	}

	public String getAddedToListStoreNumber() {
		return this.addedToListStoreNumber.getText();
	}

	public void clickSelectAll() {
		checkBoxSelectAll.click();
	}

	public void clickSelectAllAvailableList() {
		checkBoxSelectAllAvailableList.click();
	}

	public void deselectStoreFromList(String storeNumber) {
		List<WebElement> storeList = tableGroupStoreList.findElements(By.tagName("div"));
		for (WebElement store : storeList) {
			if (store.findElement(By.cssSelector("div > p:nth-child(2)")).getText().equals(storeNumber)) {
				store.findElement(By.cssSelector("div > mat-pseudo-checkbox")).click();
			}
		}
	}

	public void deselectStoreFromAvailableList(String storeNumber) {
		List<WebElement> storeList = tableAvailableStoreList.findElements(By.tagName("div"));
		for (WebElement store : storeList) {
			if (store.findElement(By.cssSelector("div > p:nth-child(2)")).getText().equals(storeNumber)) {
				store.findElement(By.cssSelector("div > mat-pseudo-checkbox")).click();
			}
		}
	}

	public void selectStoreFromAvailableList(String storeNumber) {
		List<WebElement> storeList = tableAvailableStoreList.findElements(By.tagName("div"));
		for (WebElement store : storeList) {
			if (store.findElement(By.cssSelector("div > p:nth-child(2)")).getText().equals(storeNumber)) {
				if (store.findElement(By.cssSelector("div > mat-pseudo-checkbox")).getAttribute("ng-reflect-state")
						.equals("unchecked")) {
					store.findElement(By.cssSelector("div > mat-pseudo-checkbox")).click();
				}
			}
		}
	}

	public void selectStoreFromList(String storeNumber) {
		List<WebElement> storeList = tableGroupStoreList.findElements(By.tagName("div"));
		for (WebElement store : storeList) {
			if (store.findElement(By.cssSelector("div > p:nth-child(2)")).getText().equals(storeNumber)) {
				store.findElement(By.cssSelector("div > mat-pseudo-checkbox")).click();
				if (store.findElement(By.cssSelector("div > mat-pseudo-checkbox")).getAttribute("ng-reflect-state")
						.equals("unchecked")) {
					store.findElement(By.cssSelector("div > mat-pseudo-checkbox")).click();
				}
			}
		}
	}

	public List<WebElement> isEmptyStoreFromList() {
		return tableGroupStoreList.findElements(By.tagName("div"));
	}

	public WebElement selectedStoreFromList(String storeNumber) {
		WebElement storeSelected = null;
		List<WebElement> storeList = tableGroupStoreList.findElements(By.tagName("div"));
		for (WebElement store : storeList) {
			if (store.findElement(By.cssSelector("div > p:nth-child(2)")).getText().equals(storeNumber)) {
				storeSelected = store.findElement(By.cssSelector("div > mat-pseudo-checkbox"));
			}
		}
		return storeSelected;
	}

	public Boolean selectAllIsChecked() {
		WebElement parent = (WebElement) ((JavascriptExecutor) driver)
				.executeScript("return arguments[0].parentNode.parentNode;", checkBoxSelectAll);
		if (parent.getAttribute("class").contains("mat-checkbox-checked")) {
			return true;
		}
		return false;
	}

	public Boolean selectAllIsCheckedInAvailableList() {
		WebElement parent = (WebElement) ((JavascriptExecutor) driver)
				.executeScript("return arguments[0].parentNode.parentNode;", checkBoxSelectAllAvailableList);
		if (parent.getAttribute("class").contains("mat-checkbox-checked")) {
			return true;
		}
		return false;
	}

	public Boolean storeIsSelected(String storeNumber) {
		List<WebElement> storeList = tableGroupStoreList.findElements(By.tagName("div"));
		for (WebElement store : storeList) {
			if (store.findElement(By.cssSelector("div > p:nth-child(2)")).getText().equals(storeNumber)) {
				if (store.findElement(By.cssSelector("div > mat-pseudo-checkbox")).getAttribute("ng-reflect-state")
						.equals("checked")) {
					return true;
				}
				return false;
			}
		}
		return false;
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
