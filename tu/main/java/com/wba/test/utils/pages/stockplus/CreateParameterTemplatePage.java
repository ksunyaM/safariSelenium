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
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

public class CreateParameterTemplatePage extends LoadableComponent<CreateParameterTemplatePage> {

	@FindBy(css = "#page-header-title")
	private WebElement pageTitle;
	@FindBy(id = "page-header-back-button")
	private WebElement backButton;
	@FindBy(id = "mat-select-2")
	private WebElement templateMatSelect;
	@FindBy(id = "cdk-overlay-0")
	private WebElement templateOverlay;
	@FindBy(id = "select-template-button")
	private WebElement selectTemplateButton;
	@FindBy(id = "mat-input-2")
	private WebElement searchProductInput;
	@FindBy(id = "rx-product-search-button")
	private WebElement storesSearchButton;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters-product > div > div > div:nth-child(1) > rx-product-group-association > div > div:nth-child(4) > div > div:nth-child(2) > virtual-scroll")
	private WebElement foundProductVirtualScroll;
	@FindBy(css = "#dialog-container > div.mat-dialog-content.mat-body-1.margin-bottom-3x > div")
	private WebElement popupMessage;
	@FindBy(id="dialog-confirm-button")
	private WebElement popupOkButton;
	@FindBy(css = "#create-parameter-dialogId > mat-dialog-content > p")
	private WebElement popupErrorMessage;
	@FindBy(id="create-parameter-confirmButtonId")
	private WebElement okButton;
	@FindBy(id="mat-select-3")
	private WebElement storeSGSelection;
	
	@FindBy(xpath="//*[@id=\"itemSearchTable\"]/mat-row/mat-cell[4]/span")
	private WebElement currentTemplateLevel;
	
	/*
	 * Header
	 */
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters-product > div > div:nth-child(1) > rx-template-details > div > div:nth-child(1) > strong")
	private WebElement templateDetailsLabel;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters-product > div > div:nth-child(1) > rx-template-details > div > div:nth-child(2) > div:nth-child(1)")
	private WebElement globalTemplateDiv;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters-product > div > div:nth-child(1) > rx-template-details > div > div:nth-child(2) > div:nth-child(2)")
	private WebElement productDiv;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters-product > div > div:nth-child(1) > rx-template-details > div > div:nth-child(2) > div:nth-child(3)")
	private WebElement itemDiv;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters-product > div > div:nth-child(1) > rx-template-details > div > div:nth-child(2) > div:nth-child(4)")
	private WebElement createdDiv;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters-product > div > div:nth-child(1) > rx-template-details > div > div:nth-child(2) > div:nth-child(5)")
	private WebElement lastModifiedDiv;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters-product > div > div:nth-child(1) > rx-template-details > div > div:nth-child(2) > div:nth-child(6)")
	private WebElement levelDiv;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters-product > div > div:nth-child(1) > rx-template-details > div > div:nth-child(2) > div:nth-child(7)")
	private WebElement associationDiv;
	
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters > div > div.template-creation-header.padding-bottom-3x > div:nth-child(1) > strong")
	private WebElement templateDetailsTitle;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters > div > div.template-creation-header.padding-bottom-3x > div:nth-child(2) > div:nth-child(1)")
	private WebElement globalTemplateLabels;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters > div > div.template-creation-header.padding-bottom-3x > div:nth-child(2) > div:nth-child(2)")
	private WebElement levelLabels;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters > div > div.template-creation-header.padding-bottom-3x > div:nth-child(2) > div:nth-child(3)")
	private WebElement createdDateLabels;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters > div > div.template-creation-header.padding-bottom-3x > div:nth-child(2) > div:nth-child(4)")
	private WebElement lastModifiedLabels;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters > div > div.template-creation-header.padding-bottom-3x > div:nth-child(2) > div:nth-child(5)")
	private WebElement associationLabels;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters > div > div.template-creation-header.padding-bottom-3x > div:nth-child(2) > div:nth-child(1) > span.margin-top")
	private WebElement templateTypeLabel;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters > div > div:nth-child(2) > div > rx-store-group-association > div > div:nth-child(1) > strong")
	private WebElement storeTemplateLabel;
	
	/*
	 * Configure section
	 */
	@FindBy(id = "from-input")
	private WebElement inputStartDate;
	@FindBy(id = "mat-error-0")
	private WebElement startDateMissingDateError;
	@FindBy(id = "mat-error-1")
	private WebElement pastDateError;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters-product > div > div:nth-child(2) > div > rx-create-product-ranging > div > form > div > mat-form-field > div > div.mat-input-flex.mat-form-field-flex > div.mat-input-suffix.mat-form-field-suffix.ng-tns-c15-15 > mat-datepicker-toggle > button > span > mat-icon > svg")
	private WebElement datePicker;
	@FindBy(id = "cdk-overlay-1")
	private WebElement itemDescriptionOverlay;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters-product > div > div:nth-child(2) > div > rx-create-product-ranging > div > form > div > strong:nth-child(1)")
	private WebElement configureLabel;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters-product > div > div:nth-child(2) > div > rx-create-product-ranging > div > form > div > strong:nth-child(3)")
	private WebElement parametersLabel;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters-product > div > div:nth-child(2) > div > rx-create-product-ranging > div > form > div > div")
	private WebElement rangingParameterDiv;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters-product > div > div:nth-child(2) > div > rx-create-product-ranging > div > div > button.mat-raised-button.mat-tertiary")
	private WebElement cancelButton;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters-product > div > div:nth-child(2) > div > rx-create-product-ranging > div > div > button.mat-raised-button.mat-primary")
	private WebElement saveButton;
	@FindBy(id = "mat-slide-toggle-1")
	private WebElement toggleButton;
	@FindBy(id="mat-slide-toggle-2")
	private WebElement toggleButtonManualOrders;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters-product > div > div:nth-child(2) > div > rx-create-product-opol > div > form > div > strong:nth-child(1)")
	private WebElement configureTitle;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters > div > div:nth-child(2) > div > rx-ordering-template > form > div.actions > button.mat-raised-button.mat-tertiary")
	private WebElement cancelParametersButton;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters > div > div:nth-child(2) > div > rx-prevent-excess-orders-template > form > div.actions > button.mat-raised-button.mat-tertiary")
	private WebElement cancelParementersButtonForPreventExcess;
	
	/*
	 * Selected product section
	 */
	@FindBy(id = "store-input")
	private WebElement storeInput;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters-product > div > div > div:nth-child(1) > rx-product-group-association > div > div.margin-4x > button")
	private WebElement templateSelect;
	@FindBy(id = "rx-stores-search-button")
	private WebElement searchStoreFromSelectedProduct;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters-product > div > div > div:nth-child(2) > rx-product-level-apply > div > div:nth-child(5) > div")
	private WebElement foundStoresForProductTable;
	@FindBy(id = "mat-select-4")
	private WebElement levelMatSelect;
	@FindBy(css = "#mat-option-13 > span")
	private WebElement selectStoreGroup;
	@FindBy(id = "mat-autocomplete-3")
	private WebElement storeGroupMatSelect;
	@FindBy(id = "mat-option-556")
	private WebElement storeMatSelect;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters-product > div > div > div:nth-child(2) > rx-product-level-apply > div > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > span:nth-child(2)")
	private WebElement plnLabel;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters-product > div > div > div:nth-child(2) > rx-product-level-apply > div > div:nth-child(1) > div:nth-child(2) > div:nth-child(2) > span:nth-child(2)")
	private WebElement itemLabel;
	@FindBy(id="mat-autocomplete-2")
	private WebElement matSelectProduct;
	@FindBy(id="mat-autocomplete-3")
	private WebElement matSelectRefineProduct;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters-product > div > div > div:nth-child(2) > rx-product-level-apply > div > mat-toolbar > div > mat-toolbar-row > button")
	private WebElement configureCustomTemplateButton;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters-product > div > div > div:nth-child(1) > rx-product-group-association > div > div.padding-top-2x > b")
	private WebElement numberOfTemplateFound;
	@FindBy(id="mat-option-14")
	private WebElement ndcSuggested;
	@FindBy(id="rx-stores-clear-button")
	private WebElement storesClearButton;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters-product > div > div:nth-child(2) > div > rx-create-product-ranging > div > form > div > div > div > rx-info-icon-tooltip > span > mat-icon")
	private WebElement rangingInfoIcon;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters-product > div > div:nth-child(2) > div > rx-create-product-ordering > div > form > div > div > div > rx-info-icon-tooltip > span > mat-icon")
	private WebElement orderingInfoIcon;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters-product > div > div:nth-child(2) > div > rx-create-product-opol > div > form > div > div > div > div > rx-info-icon-tooltip > span > mat-icon")
	private WebElement opolInfoIcon;
	@FindBy(id="cdk-overlay-2")
	private WebElement storeOrStoreGroupInSelectedProductOverlay;
	
	/*
	 * Store group association
	 */
	@FindBy(id="itemSearchTable")
	private WebElement foundStoresForStoreTable;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters > div > div:nth-child(2) > div > rx-ordering-template > form > div:nth-child(1) > strong")
	private WebElement editTemplateStoreTitle;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters > div > div:nth-child(2) > div > rx-ordering-template > form > div.actions > button.mat-raised-button.mat-primary")
	private WebElement saveConfigurationButton;
	@FindBy(id="date-input")
	private WebElement configureStartDateInput;
	@FindBy(id="mat-select-3")
	private WebElement storeAndStoreGroupMatSelect;
	@FindBy(id="rx-stores-search-button")
	private WebElement storesAndStoreGroupSearchButton;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters > div > div:nth-child(2) > div > rx-store-group-association > div > mat-toolbar > div > mat-toolbar-row > button")
	private WebElement selectButton;
	@FindBy(css="#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-create-parameters > div > div:nth-child(2) > div > rx-prevent-excess-orders-template > form > div.actions > button.mat-raised-button.mat-secondary")
	private WebElement manageExclusionList;

	private WebDriver driver;

	public CreateParameterTemplatePage(WebDriver driver) {
		this.driver = driver;
	}

	/*
	 * Text handle
	 */
	public String getTemplateTypeLabel() {
		return templateTypeLabel.getText();
	}
	
	public String getCurrentTemplateLevel() {
		
		return currentTemplateLevel.getText();
	}
	
	
	public void clearConfigureStartDateInput() {
		configureStartDateInput.clear();
	}
	
	public void insertConfigureStartDateInput(String date) {
		configureStartDateInput.sendKeys(date);
	}
	
	public String getConfigureTitle() {
		return configureTitle.getText();
	}
	
	public void insertStoreInput(String store) {
		storeInput.sendKeys(store);
	}

	public String getSearchProduct() {
		return searchProductInput.getText();
	}

	public void insertSearchProduct(String product) {
		searchProductInput.sendKeys(product);
	}

	public void clearInputStartDate() {
		inputStartDate.clear();
	}

	public void insertInputStartDate(String date) {
		inputStartDate.sendKeys(date);
	}

	/*
	 * Displayed
	 */
	public Boolean popupOkButtonDisplayed() {
		return popupOkButton.isDisplayed();
	}
	
	public Boolean isSaveConfigurationButtonDisplayed() {
		return saveConfigurationButton.isDisplayed();
	}
	
	public Boolean isTemplateDetailsTitleDisplayed() {
		return templateDetailsTitle.isDisplayed();
	}
	
	public Boolean isGlobalTemplateLabelsDisplayed() {
		return globalTemplateLabels.isDisplayed();
	}
	
	public Boolean isStoreTemplateLabelsDisplayed() {
		return storeTemplateLabel.isDisplayed();
	}
	
	public Boolean isCreatedDateLabelsDisplayed() {
		return createdDateLabels.isDisplayed();
	}
	
	public Boolean isLastModifiedLabelsDisplayed(){
		return lastModifiedLabels.isDisplayed();
	}
	
	public Boolean isAssociationLabelsDisplayed() {
		return associationLabels.isDisplayed();
	}
	
	public Boolean isCancelParametersButtonDisplayed() {
		return cancelParametersButton.isDisplayed();
	}
	
	public Boolean isToggleButtonManualOrdersDisplayed() {
		return toggleButtonManualOrders.isDisplayed();
	}
	
	public Boolean isToggleCalculateSuggestedOrderDisplayed() {
		return toggleButton.isDisplayed();
	}
	
	public Boolean isEditTemplateStoreTitleDisplayed() {
		return editTemplateStoreTitle.isDisplayed();
	}
	
	public Boolean isOPOLInfoIconDisplayed() {
		return opolInfoIcon.isDisplayed();
	}
	
	public Boolean isRangingInfoIconDisplayed() {
		return rangingInfoIcon.isDisplayed();
	}
	
	public Boolean isOrderingInfoIconDisplayed() {
		return orderingInfoIcon.isDisplayed();
	}
	
	public Boolean isNumberOfTemplateFound() {
		return numberOfTemplateFound.isDisplayed();
	}
	
	public Boolean isMatSelectProductDisplayed() throws InterruptedException {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.matSelectProduct), 20);
		return matSelectProduct.isDisplayed();
	}
	
 	public Boolean isitemLabelDisplayed() {
		return itemLabel.isDisplayed();
	}
	
	public Boolean isPlnLabelDisplayed() {
		return plnLabel.isDisplayed();
	}
	
	public Boolean isStoreGroupMatSelectDisplayed() throws InterruptedException {
		synchronized (this) {
			wait(2000);
		}
		return storeGroupMatSelect.isDisplayed();
	}

	public Boolean isSaveButtonDisplayed() {
		return saveButton.isDisplayed();
	}

	public Boolean isCancelButtonDisplayed() {
		return cancelButton.isDisplayed();
	}

	public Boolean isRangingParameterDivDisplayed() {
		return rangingParameterDiv.isDisplayed();
	}

	public Boolean isParametersLabelDisplayed() {
		return parametersLabel.isDisplayed();
	}

	public Boolean isConfigureLabelDisplayed() {
		return configureLabel.isDisplayed();
	}

	public Boolean isAssociationDivDisplayed() {
		return associationDiv.isDisplayed();
	}

	public Boolean isLevelDivDisplyed() {
		return levelDiv.isDisplayed();
	}

	public Boolean isLastModifiedDivDisplayed() {
		return lastModifiedDiv.isDisplayed();
	}

	public Boolean isCreatedDivDisplayed() {
		return createdDiv.isDisplayed();
	}

	public Boolean isItemDivDisplayed() {
		return itemDiv.isDisplayed();
	}

	public Boolean isProductDivDisplayed() {
		return productDiv.isDisplayed();
	}

	public Boolean isGlobalTemplateDivDisplayed() {
		return globalTemplateDiv.isDisplayed();
	}

	public Boolean isTemplateDetailsLabelDislayed() {
		return templateDetailsLabel.isDisplayed();
	}

	public Boolean isTitleDisplayed() {
		return pageTitle.isDisplayed();
	}
	
	public Boolean isOkButtonDisplayed() {
		return okButton.isDisplayed();
	}

	public void selectStoreSG(String argument) {
		
		synchronized (this) {
            try {
				wait(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		
		List<WebElement> rows = driver.findElements(By.xpath("//*[contains(@ng-reflect-value,'STORE')]"));

		for (WebElement row : rows) {
			if (argument.equals(row.getText())) {
				row.click();
				break;
			}
		}
	
	}
	
	public List<Boolean> areFoundStoresForProductTableColumnsDisplayed() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.foundStoresForProductTable), 20);
		List<Boolean> allDisplayed = new ArrayList<>();
		List<WebElement> rows = foundStoresForProductTable.findElements(
				By.cssSelector("virtual-scroll > div.scrollable-content > div.mat-row.selected-row-style"));
		for (WebElement row : rows) {
			allDisplayed.add(row.findElement(By.cssSelector("span:nth-child(1)")).isDisplayed());
			allDisplayed.add(row.findElement(By.cssSelector("span:nth-child(2)")).isDisplayed());
			allDisplayed.add(row.findElement(By.cssSelector("span:nth-child(3)")).isDisplayed());
			allDisplayed.add(row.findElement(By.cssSelector("span:nth-child(4)")).isDisplayed());
		}
		return allDisplayed;
	}
	
	public List<Boolean> isEditButtonDisplayed() {
		List<Boolean> allDisplayed = new ArrayList<>();
		List<WebElement> stores = foundStoresForStoreTable.findElements(By.cssSelector("mat-row"));
		for (WebElement store : stores) {
			allDisplayed.add(store.findElement(By.cssSelector("mat-cell > button")).isDisplayed());
		}
		return allDisplayed;
	}

	/*
	 * Click
	 */
	public void clickPopupOkButton() {
		popupOkButton.click();
	}
	
	public void clickPopupErrorOkButton() {
		okButton.click();
	}
	
	public ManageExclusionListPage clickManageExclusionList() {
		manageExclusionList.click();
		return new ManageExclusionListPage(driver);
	}
	
	public void clickSelectButton() {
		selectButton.click();
	}
	
	public void clickStoreSG() {
		storeSGSelection.click();
	}
	
	public void clickSoreOrStoreGroupToSearch(String selected) {
		List<WebElement> values = itemDescriptionOverlay.findElements(By.cssSelector("div > div > mat-option"));
		for (WebElement value : values) {
			if (value.findElement(By.cssSelector("span")).getText().equals(selected)) {
				value.click();
				break;
			}
		}
	}
	
	public void clickSearchByMatSelect(String selectedValue) {
		List<WebElement> values = storeAndStoreGroupMatSelect.findElements(By.cssSelector("div > div > mat-option"));
		for (WebElement value : values) {
			if (value.findElement(By.cssSelector("span")).getText().equals(selectedValue)) {
				value.click();
				break;
			}
		}
	}
	
	public void clickStoreOrStoreGroupInSelectedProductOverlay(String selectedValue) {
		List<WebElement> values = storeOrStoreGroupInSelectedProductOverlay.findElements(By.cssSelector("div > div > mat-option"));
		for (WebElement value : values) {
			if (value.findElement(By.cssSelector("span")).getText().equals(selectedValue)) {
				value.click();
				break;
			}
		}
	}
	
	public void clickMatSelectRefineProduct(String searchedStore) {
		List<WebElement> values = storeGroupMatSelect.findElements(By.cssSelector("div > div > mat-option"));
		for (WebElement value : values) {
			if (value.findElement(By.cssSelector("span")).getText().equals(searchedStore)) {
				value.click();
				break;
			}
		}
	}
	
	public void clickStoreAndStoreGroupMatSelect() {
		storeAndStoreGroupMatSelect.click();
	}
	
	public void clickStoresAndStoreGroupSearchButton() {
		storesAndStoreGroupSearchButton.click();
	}
	
	public void clickCancelParementersButtonForPreventExcess() {
		cancelParementersButtonForPreventExcess.click();
	}
	
	public void clickSaveConfigurationButton() {
		saveConfigurationButton.click();
	}
	
	public void clickStoresClearButton() {
		storesClearButton.click();
	}
	
	public void clickOnNdcSuggested() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.ndcSuggested), 20);
		ndcSuggested.click();
	}
	
	public void clickConfigureCustomTemplateButton() {
		configureCustomTemplateButton.click();
	}
	
	public void clickstoreMatSelect() {
		storeMatSelect.click();
	}

	public void clickSelectStoreGroup() {
		selectStoreGroup.click();
	}

	public void clickLevelMatSelect() {
		levelMatSelect.click();
	}

	public void clickSearchStoreFromSelectedProduct() throws InterruptedException {
		synchronized (this) {
			wait(2000);
		}
		searchStoreFromSelectedProduct.click();
	}

	public void clickTemplateSelect() {
		templateSelect.click();
	}

	public void clickCancelButton() {
		cancelButton.click();
	}

	public void clickSaveButton() {
		saveButton.click();
	}

	public void clickBackButton() {
		backButton.click();
	}

	public void clickTemplateMatSelect() {
		templateMatSelect.click();
	}

	public void datePickerClick() {
		datePicker.click();
	}

	public void clickToggleButton() {
		toggleButton.click();
	}

	public void clickToSelectTemplate(String selectedTemplate) {
		List<WebElement> templates = templateOverlay.findElements(By.cssSelector("mat-option"));
		for (WebElement template : templates) {
			if (template.getAttribute("ng-reflect-value").equals(selectedTemplate)) {
				template.click();
				break;
			}
		}
	}
	
	public Boolean searchATemplate(String selectedTemplate) {
		Boolean found = false;
		List<WebElement> templates = templateOverlay.findElements(By.cssSelector("mat-option"));
		for (WebElement template : templates) {
			if (template.getAttribute("ng-reflect-value").equals(selectedTemplate)) {
				found = true;
			}
		}
		return found;
	}

	public void clickSelectTemplateButton() {
		selectTemplateButton.click();
	}

	public void clickStoresSearchButton() throws InterruptedException {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.ndcSuggested), 20);
		storesSearchButton.click();
	}

	public void selectAndClickProduct(String productType) {
		List<WebElement> products = foundProductVirtualScroll.findElements(By.cssSelector("mat-row"));
		for (WebElement item : products) {
			if (item.findElement(By.cssSelector("span:nth-child(4)")).getText().equals(productType)) {
				item.findElement(By.cssSelector("span:nth-child(5) > button")).click();
				break;
			}
		}
	}
	
	public void clickToEditAStoreTemplate(String storeType) {
		List<WebElement> stores = foundStoresForStoreTable.findElements(By.cssSelector("mat-row"));
		for (WebElement store : stores) {
			WebElement level = store.findElement(By.cssSelector("mat-cell.mat-cell.cdk-column-Current_template_level.mat-column-Current_template_level"));
			if (level.findElement(By.cssSelector("span")).getText().equals(storeType)) {
				store.findElement(By.cssSelector("mat-cell > button")).click();
				break;
			}
		}
	}

	/*
	 * Errors messages
	 */
	public String getStartDateMissingDateError() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.startDateMissingDateError), 20);
		return startDateMissingDateError.getText();
	}

	public String getPastDateError() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.pastDateError), 20);
		return pastDateError.getText();
	}

	public String getPopupMessage() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.popupMessage), 20);
		return popupMessage.getText();
	}

	public String getPopupErrorMessage() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.popupErrorMessage), 20);
		return popupErrorMessage.getText();
	}
	
	
	
	/*
	 * Hot keys
	 */
	public void tabFromInputStartDate() {
		inputStartDate.sendKeys(Keys.TAB);
	}

	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(pageTitle)) {
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
