/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.stockplus;

import static com.oneleo.test.automation.core.UIUtils.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

public class ReplenishmentParametersPage extends LoadableComponent<ReplenishmentParametersPage> {

	@FindBy(css = "#page-header-title")
	private WebElement pageTitle;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-replenishment-parameters > div > nav > div > a:nth-child(2)")
	private WebElement storeTab;
	@FindBy(id = "param-tab-2")
	private WebElement gridOverridesTab;
	@FindBy(id = "param-tab-0")
	private WebElement productTab;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-replenishment-parameters > div > div > rx-product-parameters > div > div:nth-child(3) > button")
	private WebElement crateTemplateButton;
	@FindBy(css = "#row-table-central-text > span:nth-child(5) > mat-chip-list > div > button")
	private WebElement tableFlatButton;
	@FindBy(css = "#cdk-overlay-0 > mat-dialog-container > app-grid-overrides-table-dialog > div")
	private WebElement productGroupsDialogWindow;
	@FindBy(id = "mat-select-1")
	private WebElement matSelectTemplateType;
	@FindBy(id = "mat-select-4")
	private WebElement matSelectProductTemplateType;
	@FindBy(css = "#th-alignment > tr")
	private WebElement templateTableHeader;
	@FindBy(id = "order-detail-table-body")
	private WebElement templatesTable;
	@FindBy(css = "#order-detail-table-body > tr > td > virtual-scroll > div.scrollable-content > mat-accordion > mat-expansion-panel.mat-expansion-panel.ng-tns-c21-27 > mat-expansion-panel-header > span > mat-panel-description > div > span:nth-child(6) > button")
	private WebElement firstRowExpansionButton;
	@FindBy(id = "rx-ordering-edit-button")
	private WebElement editButton;
	@FindBy(id = "rx-opol-delete-button")
	private WebElement deleteButton;
	@FindBy(id = "rx-opol-cancel-button")
	private WebElement opolCancelButton;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-replenishment-parameters > div > div > rx-product-parameters > div > div:nth-child(3) > div > button.mat-raised-button.mat-secondary")
	private WebElement searchButton;
	@FindBy(id = "from-input")
	private WebElement fromDateInput;
	@FindBy(css = "#mat-input-1")
	private WebElement fromDateForProductOrdering;
	@FindBy(id = "mat-input-1")
	private WebElement templateSearch;
	@FindBy(id = "rx-ordering-cancel-button")
	private WebElement cancelButton;
	@FindBy(id = "mat-option-4")
	private WebElement allCheckBox;
	@FindBy(id = "mat-option-5")
	private WebElement opolCheckBox;
	@FindBy(id = "mat-option-6")
	private WebElement orderingCheckBox;
	@FindBy(id = "mat-option-7")
	private WebElement preventExcessOrderCheckBox;
	@FindBy(id = "mat-option-8")
	private WebElement suggestedOrderCheckBox;
	@FindBy(css = "#order-detail-table-body > tr > td > virtual-scroll > div.scrollable-content > mat-accordion > mat-expansion-panel.mat-expansion-panel.ng-tns-c21-45 > mat-expansion-panel-header > span > mat-panel-description > div > span:nth-child(6) > button > span > mat-icon")
	private WebElement chevronButton;
	@FindBy(id = "mat-datepicker-0")
	private WebElement calendar;
	@FindBy(css = "#cdk-accordion-child-2 > div > div > div > rx-product-ordering-detail > form > div:nth-child(2) > mat-form-field > div > div.mat-input-flex.mat-form-field-flex > div.mat-input-suffix.mat-form-field-suffix.ng-tns-c15-20 > mat-datepicker-toggle > button > span > mat-icon > svg")
	private WebElement datePicker;
	@FindBy(css = "#rx-opol-detail-parameters-form > div > div:nth-child(2) > mat-form-field.mat-input-container.mat-form-field.ng-tns-c15-19.margin-right-5x.spacing-for-errors.custom-mat-form-field.mat-form-field-type-mat-input.mat-form-field-can-float.mat-form-field-should-float.mat-primary.ng-pristine.ng-valid.ng-touched > div > div.mat-input-flex.mat-form-field-flex > div.mat-input-suffix.mat-form-field-suffix.ng-tns-c15-19 > mat-datepicker-toggle > button > span > mat-icon > svg > path:nth-child(1)")
	private WebElement opolDatePicker;
	@FindBy(id = "from-input")
	private WebElement inputCalendar;
	@FindBy(id = "filter-by-name")
	private WebElement filterByName;
	@FindBy(id = "cdk-overlay-0")
	private WebElement suggestedTemplates;
	@FindBy(id = "mat-autocomplete-0")
	private WebElement suggestedTemplateForProduct;
	@FindBy(css = "#dialog-container > div.mat-dialog-title.margin-bottom-2x")
	private WebElement deletePopupTitle;
	@FindBy(css = "#dialog-container > div.mat-dialog-content.mat-body-1.margin-bottom-3x > div")
	private WebElement deletePopupMessage;
	@FindBy(id = "dialog-cancel-button")
	private WebElement popupCancelButton;
	@FindBy(id = "dialog-confirm-button")
	private WebElement popupConfirmButton;

	/*
	 * OPOL
	 */
	@FindBy(id = "historicalDemandPeriod")
	private WebElement historicalDemandPeriodInput;
	@FindBy(id = "replenishmentOrderCandidate")
	private WebElement replenishmentOrderCandidateInput;
	@FindBy(id = "returnsPeriod")
	private WebElement returnsPeriodInput;
	@FindBy(id = "mat-slide-toggle-1-input")
	private WebElement calculateOrderPointToggle;
	@FindBy(id = "opolGeneration")
	private WebElement opolTimeInput;
	@FindBy(id = "rx-opol-edit-button")
	private WebElement opolEditButton;
	@FindBy(css = "#mat-hint-0")
	private WebElement hintForHistoricalDemand;
	@FindBy(css = "#mat-hint-1")
	private WebElement hintForOrderCandidate;
	@FindBy(css = "#mat-hint-2")
	private WebElement hintForCandidateForReturnPeriod;
	@FindBy(id = "mat-error-5")
	private WebElement missingDateError;
	@FindBy(id = "mat-error-3")
	private WebElement wrongTimeError;
	@FindBy(id = "mat-error-2")
	private WebElement wrongCandidateMessage;
	@FindBy(id = "mat-error-1")
	private WebElement wrongReplenishmentOrderError;
	@FindBy(id = "mat-error-0")
	private WebElement wrongHistoricalDemandPeriodError;
	@FindBy(id = "mat-error-4")
	private WebElement wrongPFLOrderError;
	@FindBy(id = "from-input-exclude")
	private WebElement fromInputExclude;
	@FindBy(id = "end-input-exclude")
	private WebElement endInputExclude;

	/*
	 * Prevent Excess Orders
	 */
	@FindBy(id = "rx-prevent-excess-orders-cancel-button")
	private WebElement preventExcessOrdersCancelButton;
	@FindBy(id = "rx-prevent-excess-orders-edit-button")
	private WebElement preventExcessOrdersEditOrSaveButton;
	@FindBy(id = "rx-prevent-excess-orders-manage-excluded-items-button")
	private WebElement manageExcludedItemsButton;
	@FindBy(id = "y-order-qty-low-limit-units")
	private WebElement orderQuantityY;
	@FindBy(id = "z-order-qty-high-limit-units")
	private WebElement orderQuantityZ;
	@FindBy(id = "x-order-qty-limit-units")
	private WebElement orderQuantityX;
	@FindBy(id = "a-order-qty-used-order-greater-than-z-units")
	private WebElement orderQuantityA;
	@FindBy(id = "q-pfl-order-limit-qty-units")
	private WebElement orderLimitQ;
	@FindBy(id = "rx-prevent-excess-orders-delete-button")
	private WebElement preventExcessOrdersDeleteButton;

	/*
	 * Ordering
	 */
	@FindBy(id = "manual-order-cap-units")
	private WebElement manualOrderCapUnitsInput;
	@FindBy(id = "rx-ordering-edit-button")
	private WebElement orderingSaveButton;
	@FindBy(css = "#order-detail-table-body > tr > td > virtual-scroll > div.scrollable-content > mat-accordion > mat-expansion-panel > mat-expansion-panel-header > span > mat-panel-description > div > span:nth-child(6) > button")
	private WebElement orderingChevronButton;
	@FindBy(id = "on-hand-forward-view")
	private WebElement onHandForwardView;
	@FindBy(id = "on-hand-backward-view")
	private WebElement onHandBackwardView;
	@FindBy(id = "rx-ordering-delete-button")
	private WebElement orderingDeleteButton;

	/*
	 * Suggested order
	 */
	@FindBy(id = "rx-suggested-order-edit-button")
	private WebElement suggestedOrderEditButton;
	@FindBy(id = "rx-suggested-order-cancel-button")
	private WebElement suggestedOrderCancelButton;
	@FindBy(id = "rx-suggested-order-delete-button")
	private WebElement suggestedOrderDeleteButton;
	@FindBy(css = "#order-detail-table-body > tr > td > virtual-scroll > div.scrollable-content > mat-accordion > mat-expansion-panel > mat-expansion-panel-header > span > mat-panel-description > div > span:nth-child(6) > button")
	private WebElement suggestedOrderChevronButton;
	/*
	@FindBy(css = "#order-detail-table-body > tr > td > virtual-scroll > div.scrollable-content > mat-accordion > mat-expansion-panel.mat-expansion-panel.ng-tns-c21-29.expanded.mat-expanded.mat-expansion-panel-spacing > mat-expansion-panel-header > span > mat-panel-description > div > span:nth-child(6) > button")
	private WebElement suggestedOrderChevronButton;
	 */
	/*
	 * Grid Overrides
	 */
	@FindBy(id = "rx-grid-overrides-edit-button")
	private WebElement gridOverridesEditButton;
	@FindBy(id = "mat-input-2")
	private WebElement startDate;
	@FindBy(id = "mat-input-3")
	private WebElement endDate;
	@FindBy(id = "eslm-k")
	private WebElement eslmK;
	@FindBy(id = "eslm-k-max")
	private WebElement eslmKMax;
	@FindBy(id = "eslm-risk-factor")
	private WebElement eslmRiskFactor;
	@FindBy(id = "rx-grid-cell-parameters-tsl-service-level")
	private WebElement serviceLevel;
	@FindBy(id = "rx-peak-number-of-weeks")
	private WebElement numberOfWeeks;
	@FindBy(id = "rslmServiceLevel")
	private WebElement rslmServiceLevel;
	@FindBy(id = "rslmEarlyTime")
	private WebElement rslmEarlyTime;
	@FindBy(id = "rx-grid-overrides-cancel-button")
	private WebElement gridOverridesCancelButton;
	@FindBy(id = "rx-grid-overrides-save-button")
	private WebElement gridOverridesSaveButton;
	@FindBy(id = "eslmEarlyTime")
	private WebElement eslmEarlyTime;
	@FindBy(id = "rslm-risk-factor")
	private WebElement rslmRiskFactor;
	@FindBy(id = "mat-error-4")
	private WebElement wrongDateFormatError;
	@FindBy(id = "mat-error-6")
	private WebElement wrongRslmEarlyTimeError;
	@FindBy(id = "mat-error-7")
	private WebElement wrongPeakNumberWeeks;
	@FindBy(id = "mat-error-9")
	private WebElement wrongStartDateNotPriorToEndDate;
	@FindBy(id = "mat-error-10")
	private WebElement wrongEndDatePriorToStartDate;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-replenishment-parameters > div > div > rx-grid-overrides-parameters > div > div:nth-child(1) > div.margin-left-3x.margin-bottom-3x.margin-right-3x > div:nth-child(2) > button")
	private WebElement gridOverridescreateNewTemplateButton;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-replenishment-parameters > div > div > rx-create-grid-override > div > div.header-heigth > h3")
	private WebElement titleGridOverrideTemplate;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-replenishment-parameters > div > div > rx-create-grid-override > div > div.header-heigth > form > mat-form-field.order-dates.mat-input-container.mat-form-field.ng-tns-c15-25.mat-form-field-type-mat-input.mat-form-field-can-float.mat-form-field-should-float.mat-primary.ng-untouched.ng-pristine.ng-valid > div > div.mat-input-flex.mat-form-field-flex > div.mat-input-suffix.mat-form-field-suffix.ng-tns-c15-25 > mat-datepicker-toggle > button > span > mat-icon > svg > path:nth-child(1)")
	private WebElement datePickerStartDate;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-replenishment-parameters > div > div > rx-create-grid-override > div > div.bodyHeight > mat-card > div > mat-card-actions > button")
	private WebElement addToListButton;
	@FindBy(id = "end-input")
	private WebElement endDateInput;
	@FindBy(id = "mat-input-5")
	private WebElement searchProductInput;
	@FindBy(id = "rx-grid-override-search-button")
	private WebElement searchProductButton;
	@FindBy(id = "mat-checkbox-1")
	private WebElement foundProductCheckBox;
	@FindBy(id = "mat-checkbox-2")
	private WebElement productToRemoveCheckBox;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-replenishment-parameters > div > div > rx-create-grid-override > div > div.bodyHeight > mat-card:nth-child(2) > div > mat-card-actions > button")
	private WebElement removeSelectedButton;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-replenishment-parameters > div > div > rx-create-grid-override > div > div.bodyHeight > mat-card:nth-child(2) > div > div:nth-child(3) > div.mat-header-row.removed-header-row")
	private WebElement removedDiv;
	@FindBy(id = "rx-grid-override-clear-button")
	private WebElement clearProductButton;
	@FindBy(id = "mat-error-11")
	private WebElement errorOnProductSearch;
	@FindBy(id = "rx-grid-override-next-button")
	private WebElement gridOverrideNextButton;
	@FindBy(id = "eslmk")
	private WebElement eslmkToggle;
	@FindBy(id = "mat-slide-toggle-2")
	private WebElement eslmKMaxToggle;
	@FindBy(id = "mat-slide-toggle-3")
	private WebElement eslmkRiskFactorToggle;
	@FindBy(id = "mat-slide-toggle-4")
	private WebElement eslmkEarlyTimeToggle;
	@FindBy(id = "mat-slide-toggle-5")
	private WebElement rslmkServiceLevelToggle;
	@FindBy(id = "mat-slide-toggle-6")
	private WebElement rslmkRiskFactorToggle;
	@FindBy(id = "mat-slide-toggle-7")
	private WebElement rslmEarlyTimeToggle;
	@FindBy(id = "mat-slide-toggle-8")
	private WebElement tslServiceLevelToggle;
	@FindBy(id = "mat-slide-toggle-9")
	private WebElement tslNumberOfWeeksToggle;
	@FindBy(id = "mat-input-6")
	private WebElement eslmKOverrideValue;
	@FindBy(id = "mat-input-7")
	private WebElement eslmKMaxOverrideValue;
	@FindBy(id = "mat-input-8")
	private WebElement eslmkRiskFactorOverrideValue;
	@FindBy(id = "mat-input-9")
	private WebElement eslmkEarlyTimeOverrideValue;
	@FindBy(id = "mat-input-10")
	private WebElement rslmkServiceLevelOverrideValue;
	@FindBy(id = "mat-input-11")
	private WebElement rslmkRiskFactorOverrideValue;
	@FindBy(id = "mat-input-12")
	private WebElement rslmEarlyTimeOverrideValue;
	@FindBy(id = "mat-select-2")
	private WebElement tslServiceLevelMatSelect;
	@FindBy(id = "mat-input-13")
	private WebElement tslNumberOfWeeksOverrideValue;
	@FindBy(id = "cdk-overlay-1")
	private WebElement tslServiceLevelOverlay;
	@FindBy(id = "cdk-overlay-2")
	private WebElement tslServiceLevelOverlayWithoutProduct;
	@FindBy(id = "rx-grid-override-save-button")
	private WebElement saveGridOverride;
	@FindBy(id = "dialog-confirm-button")
	private WebElement dialogConfirmButton;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-replenishment-parameters > div > div > rx-parameter-settings > div:nth-child(1) > h3")
	private WebElement gridOverrideTemplateTitle;
	@FindBy(css = "#dialog-container > div.mat-dialog-content.mat-body-1.margin-bottom-3x > div")
	private WebElement popupMessage;
	@FindBy(id = "ProductGroupTable")
	public WebElement productGroupTable;
	@FindBy(id = "PAGTable")
	public WebElement PAGTable;

	/*
	 * Product
	 */
	@FindBy(id = "mat-slide-toggle-1-input")
	private WebElement manualOrderToggle;
	@FindBy(id = "rx-grid-overrides-delete-button")
	private WebElement deleteButtonForProduct;
	@FindBy(id = "rx-grid-overrides-edit-button")
	private WebElement editButtonForProduct;
	@FindBy(id = "mat-option-526")
	private WebElement allProductsTemplateType;
	@FindBy(id = "cdk-overlay-0")
	private WebElement productTemplatesTypeCheckBoxes;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-replenishment-parameters > div > div > rx-product-parameters > div > rx-product-parameters-table > div")
	private WebElement numberOfResultsFoundLabel;
	@FindBy(id = "mat-select-1")
	private WebElement matSelectTemplateTypeForProduct;
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-replenishment-parameters > div > div > rx-product-parameters > div > div:nth-child(3) > div > button.mat-raised-button.mat-tertiary")
	private WebElement clearButtonForProduct;
	@FindBy(css = "#template-type > div > button")
	private WebElement templateTypeColumnHeader;
	@FindBy(css = "#th-alignment > tr > th:nth-child(2)")
	private WebElement productGroupColumnHeader;
	@FindBy(css = "#created")
	private WebElement createdDateColumnHeader;
	@FindBy(css = "#last-modified > div > button")
	private WebElement lastModifiedColumnHeader;
	@FindBy(css = "#level")
	private WebElement levelColumnHeader;
	@FindBy(css = "#th-alignment > tr > th:nth-child(6)")
	private WebElement storeGroupAssociation;

	/*
	 * Store
	 */
	@FindBy(css = "#rx-corporate-user-app-main-container > dashboard-view > div > div > div.content-container > rx-replenishment-parameters > div > div > rx-store-parameters > div > div > div:nth-child(3) > mat-card-actions > button")
	private WebElement createNewTemplateButton;
	@FindBy(id="select-template-button")
	private WebElement selectTemplateButton;
	@FindBy(id="mat-select-3")
	private WebElement searchTypeMatSelect;
	@FindBy(id="store-input")
	private WebElement storeInput;
	
	private WebDriver driver;

	public ReplenishmentParametersPage(WebDriver driver) {
		this.driver = driver;
	}

	/*
	 * Checked
	 */
	public String isAllCheckBoxChecked() {
		WebElement pseudoCheckBox = allCheckBox.findElement(By.cssSelector("span > mat-pseudo-checkbox"));
		return pseudoCheckBox.getAttribute("ng-reflect-state");
	}

	public String isOPOLCheckBoxChecked() {
		WebElement pseudoCheckBox = opolCheckBox.findElement(By.cssSelector("span > mat-pseudo-checkbox"));
		return pseudoCheckBox.getAttribute("ng-reflect-state");
	}

	public String isOrderingCheckBoxChecked() {
		WebElement pseudoCheckBox = orderingCheckBox.findElement(By.cssSelector("span > mat-pseudo-checkbox"));
		return pseudoCheckBox.getAttribute("ng-reflect-state");
	}

	public String isPreventAccessOrderCheckBoxChecked() {
		WebElement pseudoCheckBox = preventExcessOrderCheckBox
				.findElement(By.cssSelector("span > mat-pseudo-checkbox"));
		return pseudoCheckBox.getAttribute("ng-reflect-state");
	}

	public String isSuggestedOrderCheckBoxCheckBoxChecked() {
		WebElement pseudoCheckBox = suggestedOrderCheckBox.findElement(By.cssSelector("span > mat-pseudo-checkbox"));
		return pseudoCheckBox.getAttribute("ng-reflect-state");
	}

	/*
	 * Text handle
	 */
	 	public void insertStoreInput(String store) {
		storeInput.sendKeys(store);
	}
	
	public String getPopupMessage() {
		return popupMessage.getText();
	}

	public void insertEslmKOverrideValue(String k) {
		eslmKOverrideValue.sendKeys(k);
	}

	public void insertEslmKMaxOverrideValue(String max) {
		eslmKMaxOverrideValue.sendKeys(max);
	}

	public void insertEslmkRiskFactorOverrideValue(String risk) {
		eslmkRiskFactorOverrideValue.sendKeys(risk);
	}

	public void insertEslmkEarlyTimeOverrideValue(String time) {
		eslmkEarlyTimeOverrideValue.sendKeys(time);
	}

	public void insertRslmkServiceLevelOverrideValue(String level) {
		rslmkServiceLevelOverrideValue.sendKeys(level);
	}

	public void insertRslmkRiskFactorOverrideValue(String risk) {
		rslmkRiskFactorOverrideValue.sendKeys(risk);
	}

	public void insertRslmEarlyTimeOverrideValue(String time) {
		rslmEarlyTimeOverrideValue.sendKeys(time);
	}

	public void insertTslNumberOfWeeksOverrideValue(String weeks) {
		tslNumberOfWeeksOverrideValue.sendKeys(weeks);
	}

	public String getSearchProductInput() {
		return searchProductInput.getAttribute("ng-reflect-model");
	}

	public void insertSearchProductInput(String text) {
		searchProductInput.sendKeys(text);
	}

	public String getTitleGridOverrideTemplate() {
		return titleGridOverrideTemplate.getText();
	}

	public String getDeletePopupTitle() {
		return deletePopupTitle.getText();
	}

	public String getdeletePopupMessage() {
		return deletePopupMessage.getText();
	}

	public void insertTemplateSearch(String template) throws InterruptedException {
		templateSearch.sendKeys(template);
		synchronized (this) {
			wait(5000);
		}
	}

	public void insertFromDateInput(String date) {
		fromDateInput.sendKeys(date);
	}

	public void clearFromDateInput() {
		fromDateInput.clear();
	}

	public void clearFromInputExclude() {
		fromInputExclude.clear();
	}

	public String getFromInputExclude() {
		return fromInputExclude.getText();
	}

	public void insertFromInputExclude(String date) {
		fromInputExclude.sendKeys(date);
	}

	public void clearEndInputExclude() {
		endInputExclude.clear();
	}

	public String getEndInputExclude() {
		return endInputExclude.getText();
	}

	public void insertEndInputExclude(String date) {
		endInputExclude.sendKeys(date);
	}

	public void insertOnHandBackwardView(String days) {
		onHandBackwardView.sendKeys(days);
	}

	public void clearOnHandBackwardView() {
		onHandBackwardView.clear();
	}

	public String getOnHandBackwardView() {
		return onHandBackwardView.getText();
	}

	public void insertOnHandForwardView(String days) {
		onHandForwardView.sendKeys(days);
	}

	public void clearOnHandForwardView() {
		onHandForwardView.clear();
	}

	public String getOnHandForwardView() {
		return onHandForwardView.getText();
	}

	public String getFromDateInput() {
		return inputCalendar.getText();
	}

	public String getChevronButtonStatus() {
		return chevronButton.getText();
	}

	public String getOrderingChevronButton() {
		return orderingChevronButton.getText();
	}

	public String getSuggestedOrderChevronButton() {
		return suggestedOrderChevronButton.getText();
	}

	public void deleteDate() {
		inputCalendar.clear();
	}

	public void insertFromDate(String date) {
		inputCalendar.sendKeys(date);
	}

	public void insertEndDateInput(String date) {
		endDateInput.sendKeys(date);
	}

	public void clearEndDateInput() {
		endDateInput.clear();
	}

	public void insertOPOLTime(String time) {
		opolTimeInput.sendKeys(time);
	}

	public void deleteOPOLTime() {
		opolTimeInput.clear();
	}

	public void insertReturnsPeriodInput(String period) {
		returnsPeriodInput.sendKeys(period);
	}

	public void deleteReturnsPeriodInput() {
		returnsPeriodInput.clear();
	}

	public void insertReplenishmentOrderCandidateInput(String period) {
		replenishmentOrderCandidateInput.sendKeys(period);
	}

	public void deleteReplenishmentOrderCandidateInput() {
		replenishmentOrderCandidateInput.clear();
	}

	public void insertHistoricalDemandPeriodInput(String period) {
		historicalDemandPeriodInput.sendKeys(period);
	}

	public void deleteHistoricalDemandPeriodInput() {
		historicalDemandPeriodInput.clear();
	}

	public void insertOrderQuantityYInput(String y) {
		orderQuantityY.sendKeys(y);
	}

	public void deleteOrderQuantityYInput() {
		orderQuantityY.clear();
	}

	public String getOrderQuantityY() {
		return orderQuantityY.getText();
	}

	public void deleteOrderQuantityZ() {
		orderQuantityZ.clear();
	}

	public void insertOrderQuantityZ(String z) {
		orderQuantityZ.sendKeys(z);
	}

	public void deleteOrderQuantityX() {
		orderQuantityX.clear();
	}

	public void insertOrderQuantityX(String x) {
		orderQuantityX.sendKeys(x);
	}

	public void deleteOrderQuantityA() {
		orderQuantityA.clear();
	}

	public void insertOrderQuantityA(String a) {
		orderQuantityA.sendKeys(a);
	}

	public void clearOrderLimitQ() {
		orderLimitQ.clear();
	}

	public void insertOrderLimitQ(String q) {
		orderLimitQ.sendKeys(q);
	}

	public void clearEslmK() {
		eslmK.clear();
	}

	public void insertEslmK(String K) {
		eslmK.sendKeys(K);
	}

	public String getEslmK() {
		return eslmK.getAttribute("ng-reflect-model");
	}

	public void insertManualOrderCapUnitsInput(String units) {
		manualOrderCapUnitsInput.sendKeys(units);
	}

	public void clearManualOrderCapUnitsInput() {
		manualOrderCapUnitsInput.clear();
	}

	public String getManualOrderCapUnitsInput() {
		return manualOrderCapUnitsInput.getAttribute("ng-reflect-model");
	}

	// ---------------------------------------------GRID OVERRIDES
	public void clearEslmKMax() {
		eslmKMax.clear();
	}

	public void insertEslmKMax(String K) {
		eslmKMax.sendKeys(K);
	}

	public String getEslmKMax() {
		return eslmKMax.getAttribute("ng-reflect-model");
	}

	public void clearEslmRiskFactor() {
		eslmRiskFactor.clear();
	}

	public void insertEslmRiskFactor(String K) {
		eslmRiskFactor.sendKeys(K);
	}

	public String getEslmRiskFactor() {
		return eslmRiskFactor.getAttribute("ng-reflect-model");
	}

	public void clearNumberOfWeeks() {
		numberOfWeeks.clear();
	}

	public void insertNumberOfWeeks(String K) {
		numberOfWeeks.sendKeys(K);
	}

	public String getNumberOfWeeks() {
		return numberOfWeeks.getAttribute("ng-reflect-model");
	}

	public void clearStartDate() {
		startDate.clear();
	}

	public void insertStartDate(String date) {
		startDate.sendKeys(date);
	}

	public String getStartDate() {
		return startDate.getAttribute("ng-reflect-model");
	}

	public void clearEndDate() {
		endDate.clear();
	}

	public void insertEndDate(String date) {
		endDate.sendKeys(date);
	}

	public String getEndDate() {
		return endDate.getAttribute("ng-reflect-model");
	}

	public void clearRslmServiceLevel() {
		rslmServiceLevel.clear();
	}

	public void insertrsRslmServiceLevel(String serviceLevel) {
		rslmServiceLevel.sendKeys(serviceLevel);
	}

	public String getRslmServiceLevel() {
		return rslmServiceLevel.getAttribute("ng-reflect-model");
	}

	public void clearRslmEarlyTime() {
		rslmEarlyTime.clear();
	}

	public void insertRslmEarlyTime(String time) {
		rslmEarlyTime.sendKeys(time);
	}

	public String getRslmEarlyTime() {
		return rslmEarlyTime.getAttribute("ng-reflect-model");
	}

	public void clearEslmEarlyTime() {
		eslmEarlyTime.clear();
	}

	public void insertEslmEarlyTime(String time) {
		eslmEarlyTime.sendKeys(time);
	}

	public String getEslmEarlyTime() {
		return eslmEarlyTime.getAttribute("ng-reflect-model");
	}

	public void clearRslmRiskFactor() {
		rslmRiskFactor.clear();
	}

	public void insertRslmRiskFactor(String risk) {
		rslmRiskFactor.sendKeys(risk);
	}

	public String getRslmRiskFactor() {
		return rslmRiskFactor.getAttribute("ng-reflect-model");
	}

	public void insertNameFilter(String name) {
		filterByName.sendKeys(name);
	}

	public String clickAndGetTemplateFiltered() {
		String templateName = "";
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.suggestedTemplates), 20);
		List<WebElement> suggestedTemplatesList = suggestedTemplates.findElements(By.cssSelector("mat-option"));
		for (WebElement template : suggestedTemplatesList) {
			templateName = template.getText();
			template.click();
			break;
		}
		return templateName;
	}

	public void clickSuggestedTemplates(String selectedTemplate) {
		List<WebElement> suggestedTemplatesList = suggestedTemplates.findElements(By.cssSelector("mat-option"));
		for (WebElement template : suggestedTemplatesList) {
			if (template.findElement(By.cssSelector("span")).getText().equals(selectedTemplate)) {
				template.click();
				break;
			}
		}
	}

	/*
	 * Click
	 */
	 	
	public void clickSearchTypeMatSelect() {
		searchTypeMatSelect.click();
	}
	
	public void clickSelectTemplateButton() {
		selectTemplateButton.click();
	}
	
	public void clickDialogConfirmButton() {
		dialogConfirmButton.click();
	}

	public void clickSaveGridOverride() {
		saveGridOverride.click();
	}

	public void clickTslServiceLevelMatSelect() {
		tslServiceLevelMatSelect.click();
	}

	public void clickEslmkToggle() {
		eslmkToggle.click();
	}

	public void clickEslmKMaxToggle() {
		eslmKMaxToggle.click();
	}

	public void clickEslmkRiskFactorToggle() {
		eslmkRiskFactorToggle.click();
	}

	public void clickEslmkEarlyTimeToggle() {
		eslmkEarlyTimeToggle.click();
	}

	public void clickRslmkServiceLevelToggle() {
		rslmkServiceLevelToggle.click();
	}

	public void clickRslmkRiskFactorToggle() {
		rslmkRiskFactorToggle.click();
	}

	public void clickRslmEarlyTimeToggle() {
		rslmEarlyTimeToggle.click();
	}

	public void clickTslServiceLevelToggle() {
		tslServiceLevelToggle.click();
	}

	public void clickTslNumberOfWeeksToggle() {
		tslNumberOfWeeksToggle.click();
	}

	public void clickTslServiceLevel(String selectedValue) {
		List<WebElement> values = tslServiceLevelOverlay.findElements(By.cssSelector("div > div > mat-option"));
		for (WebElement value : values) {
			if (value.findElement(By.cssSelector("span")).getText().equals(selectedValue)) {
				value.click();
				break;
			}
		}
	}

	public void clickTslServiceLevelWithoutProduct(String selectedValue) {
		List<WebElement> values = tslServiceLevelOverlayWithoutProduct
				.findElements(By.cssSelector("div > div > mat-option"));
		for (WebElement value : values) {
			if (value.findElement(By.cssSelector("span")).getText().equals(selectedValue)) {
				value.click();
				break;
			}
		}
	}

	public void clickGridOverrideNextButton() {
		gridOverrideNextButton.click();
	}

	public void clickClearProductButton() {
		clearProductButton.click();
	}

	public void clickRemoveSelectedButton() {
		removeSelectedButton.click();
	}

	public void clickSearchProductButton() throws InterruptedException {
		synchronized (this) {
			wait(5000);
		}
		searchProductButton.click();
	}

	public void clickGridOverridescreateNewTemplateButton() {
		gridOverridescreateNewTemplateButton.click();
	}

	public void clickDatePickerStartDate() {
		Actions action = new Actions(driver);
		action.click(datePickerStartDate);
	}

	/*
	 * Displayed
	 */
	public Boolean isTemplateTypeColumnHeaderDisplayed() {
		return templateTypeColumnHeader.isDisplayed();
	}

	public Boolean isProductGroupColumnHeaderDisplayed() {
		return productGroupColumnHeader.isDisplayed();
	}

	public Boolean isCreatedDateColumnHeaderDisplayed() {
		return createdDateColumnHeader.isDisplayed();
	}

	public Boolean isLastModifiedColumnHeaderDisplayed() {
		return lastModifiedColumnHeader.isDisplayed();
	}

	public Boolean isLevelColumnHeaderDisplayed() {
		return levelColumnHeader.isDisplayed();
	}

	public Boolean isStoreGroupAssociationDisplayed() {
		return storeGroupAssociation.isDisplayed();
	}

	public Boolean isClearButtonForProductDisplayed() {
		return clearButtonForProduct.isDisplayed();
	}

	public Boolean isSearchButtonForProductDisplayed() {
		return searchButton.isDisplayed();
	}

	public Boolean isSearchProductDisplayed() {
		return fromDateForProductOrdering.isDisplayed();
	}

	public Boolean isPopupCancelButtonDisplayed() {
		return popupCancelButton.isDisplayed();
	}

	public Boolean isPopupConfirmButtonDisplayed() {
		return popupConfirmButton.isDisplayed();
	}

	public Boolean isGridOverrideTemplateTitle() {
		return gridOverrideTemplateTitle.isDisplayed();
	}

	public Boolean isRemovedDivDisplayed() {
		return removedDiv.isDisplayed();
	}

	public Boolean isTitleGridOverrideTemplateDisplayed() {
		return titleGridOverrideTemplate.isDisplayed();
	}

	public List<Boolean> areProductGroupTableElementsDisplayed() {
		List<Boolean> allDisplayed = new ArrayList<>();
		List<WebElement> rows = productGroupTable.findElements(By.cssSelector("mat-row"));
		for (WebElement row : rows) {
			allDisplayed.add(row.findElement(By.cssSelector("mat-cell")).isDisplayed());
			allDisplayed.add(row.findElement(By.cssSelector("mat-cell")).isDisplayed());
			allDisplayed.add(row.findElement(By.cssSelector("mat-cell")).isDisplayed());
		}
		return allDisplayed;
	}

	public List<Boolean> arePAGTableElementsDisplayed() {
		List<Boolean> allDisplayed = new ArrayList<>();
		List<WebElement> rows = PAGTable.findElements(By.cssSelector("mat-row"));
		for (WebElement row : rows) {
			allDisplayed.add(row.findElement(By.cssSelector("mat-cell")).isDisplayed());
			allDisplayed.add(row.findElement(By.cssSelector("mat-cell")).isDisplayed());
			allDisplayed.add(row.findElement(By.cssSelector("mat-cell")).isDisplayed());
		}
		return allDisplayed;
	}

	/*
	 * Error messages
	 */
	public String getErrorOnProductSearch() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.errorOnProductSearch), 15);
		return errorOnProductSearch.getText();
	}

	public String getMissingDateError() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.missingDateError), 15);
		return missingDateError.getText();
	}

	public String getWrongTimeError() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(this.wrongTimeError), 15);
		return wrongTimeError.getText();
	}

	public String getWrongCandidateMessage() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(this.wrongCandidateMessage), 20);
		return wrongCandidateMessage.getText();
	}

	public String getWrongReplenishmentOrderError() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(this.wrongReplenishmentOrderError), 15);
		return wrongReplenishmentOrderError.getText();
	}

	public String getWrongHistoricalDemandPeriodError() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(this.wrongHistoricalDemandPeriodError),
				15);
		return wrongHistoricalDemandPeriodError.getText();
	}

	public String getWrongPFLOrderError() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(this.wrongPFLOrderError), 15);
		return wrongPFLOrderError.getText();
	}

	public String getWrongDateFormatError() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(this.wrongDateFormatError), 15);
		return wrongDateFormatError.getText();
	}

	public String getWrongRslmEarlyTimeError() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(this.wrongRslmEarlyTimeError), 15);
		return wrongRslmEarlyTimeError.getText();
	}

	public String getWrongPeakNumberWeeks() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(this.wrongPeakNumberWeeks), 15);
		return wrongPeakNumberWeeks.getText();
	}

	public String getWrongEndDatePriorToStartDate() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(this.wrongEndDatePriorToStartDate), 15);
		return wrongEndDatePriorToStartDate.getText();
	}

	public String getwrongStartDateNotPriorToEndDate() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(this.wrongStartDateNotPriorToEndDate),
				15);
		return wrongStartDateNotPriorToEndDate.getText();
	}

	/*
	 * Click elements
	 */
	public void clickProductToRemoveCheckBox() {
		productToRemoveCheckBox.click();
	}

	public void clickAddToListButton() {
		addToListButton.click();
	}

	public void clickFoundProductCheckBox() {

		foundProductCheckBox.click();
	}

	public CreateParameterTemplatePage clickCreateNewTemplateButton() {
		createNewTemplateButton.click();
		return new CreateParameterTemplatePage(driver);
	}

	public void clickPopupConfirmButton() {
		popupConfirmButton.click();
	}

	public void clickPopupCancelButton() {
		popupCancelButton.click();
	}

	public void clickMatSelectTemplateTypeForProduct() {
		matSelectTemplateTypeForProduct.click();
	}

	public void clickAllProductsTemplateType() {
		allProductsTemplateType.click();
	}

	public void clickMatSelectProductTemplateType() {
		matSelectProductTemplateType.click();
	}

	public void clickSearchButton() {
		searchButton.click();
	}

	public void clicksuggestedOrderDeleteButton() {
		suggestedOrderDeleteButton.click();
	}

	public void clickSuggestedOrderEditButton() {
		suggestedOrderEditButton.click();
	}

	public void clickTableFlatButton() {
		tableFlatButton.click();
	}

	public void clickStoreTab() {
		storeTab.click();
	}

	public void clickProductTab() {
		productTab.click();
	}
	
	public void clickProductTabInternal() throws InterruptedException {
		WebElement productTabInternal=driver.findElement(By.id("param-tab-0") );
		productTabInternal.click();
	}
	
	public void clickStoreTabInternal() throws InterruptedException {
		WebElement storeTabInternal=driver.findElement(By.id("param-tab-1") );
		storeTabInternal.click();
	}

	public void clickGridOverridesTab() {
		gridOverridesTab.click();
	}

	public CreateParameterTemplatePage clickCreateTemplate() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(this.crateTemplateButton), 15);
		crateTemplateButton.click();
		return new CreateParameterTemplatePage(driver);
	}

	public void clickFirstRowExpansionButton() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.firstRowExpansionButton), 20);
		firstRowExpansionButton.click();
	}

	public void clickEditButton() {
		editButton.click();
	}

	public void clickMatSelectTemplateType() {
		matSelectTemplateType.click();
	}

	public void clickAllCheckBox() {
		allCheckBox.click();
	}

	public void clickSuggestedOrderCheckBox() {
		suggestedOrderCheckBox.click();
	}

	public void clickOPOLCheckBox() {
		opolCheckBox.click();
	}

	public void clickOrderingCheckBox() {
		orderingCheckBox.click();
	}

	public void clickPreventExcessOrderCheckBox() {
		preventExcessOrderCheckBox.click();
	}

	public void clickOnDatePicker() {
		datePicker.click();
	}

	public void clickOpolDatePicker() {
		Actions action = new Actions(driver);
		action.click(opolDatePicker);
	}

	public void clickOpolEditButton() {
		opolEditButton.click();
	}

	public void clickPreventExcessOrdersEditOrSaveButton() {
		preventExcessOrdersEditOrSaveButton.click();
	}

	public void clickPreventExcessOrdersCancelButton() {
		preventExcessOrdersCancelButton.click();
	}

	public void clickDeleteButton() {
		deleteButton.click();
	}

	public void clickGridOverridesEditButton() {
		gridOverridesEditButton.click();
	}

	public void clickGridOverridesCancelButton() {
		gridOverridesCancelButton.click();
	}

	public void clickGridOverridesSaveButton() {
		gridOverridesSaveButton.click();
	}

	public void clickOrderingSaveButton() {
		orderingSaveButton.click();
	}

	public void clickSuggestedOrderChevronButton() {
		suggestedOrderChevronButton.click();
	}

	public void clickProductTemplatesTypeCheckBoxes(String templateType) {
		List<WebElement> templates = productTemplatesTypeCheckBoxes.findElements(By.cssSelector("mat-option"));
		for (WebElement template : templates) {
			if (template.getAttribute("ng-reflect-value").equals(templateType)) {
				template.click();
				break;
			}
		}
	}

	/*
	 * Displayed
	 */
	public Boolean isNumberOfResultsFoundLabelDisplayed() {
		return numberOfResultsFoundLabel.isDisplayed();
	}

	public Boolean isPageTitleDisplayed() {
		return pageTitle.isDisplayed();
	}

	public Boolean isOpolCancelButtonDisplayed() {
		return opolCancelButton.isDisplayed();
	}

	public Boolean isEditButtonForProductDisplayed() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(editButtonForProduct), 30);
		return editButtonForProduct.isDisplayed();
	}

	public Boolean isDeleteButtonForProductDisplayed() {
		return deleteButtonForProduct.isDisplayed();
	}

	public Boolean isSuggestedOrderDeleteButtonDisplayed() {
		return suggestedOrderDeleteButton.isDisplayed();
	}

	public Boolean isSuggestedOrderCancelButtonDisplayed() {
		return suggestedOrderCancelButton.isDisplayed();
	}

	public Boolean isSuggestedOrderEditButtonDisplayed() {
		return suggestedOrderEditButton.isDisplayed();
	}

	public Boolean isProductGroupsDialogWindowDisplayed() {
		return productGroupsDialogWindow.isDisplayed();
	}

	public Boolean isCreateTemplateDisplayed() {
		return crateTemplateButton.isDisplayed();
	}

	public Boolean isMatSelectTemplateTypeDisplayed() {
		return matSelectTemplateType.isDisplayed();
	}

	public Boolean isTemplateTableHeaderDisplayed() {
		return templateTableHeader.isDisplayed();
	}

	public Boolean isTemplateTableDisplayed() {
		return templatesTable.isDisplayed();
	}

	public Boolean isCancelButtonDisplayed() {
		return cancelButton.isDisplayed();
	}

	public Boolean isFromDateDisplayed() {
		return fromDateInput.isDisplayed();
	}

	public Boolean isDeleteButtonDisplayed() {
		return deleteButton.isDisplayed();
	}

	public Boolean isEditButtonDisplayed() {
		return editButton.isDisplayed();
	}

	public Boolean isHistoricalDemandPeriodInputDisplayed() {
		return historicalDemandPeriodInput.isDisplayed();
	}

	public Boolean isReplenishmentOrderCandidateInputDisplayed() {
		return replenishmentOrderCandidateInput.isDisplayed();
	}

	public Boolean isReturnsPeriodInputDisplayed() {
		return returnsPeriodInput.isDisplayed();
	}

	public Boolean isCalculateOrderPointToggleDisplayed() {
		return calculateOrderPointToggle.isDisplayed();
	}

	public Boolean isOpolTimeInputDisplayed() {
		return opolTimeInput.isDisplayed();
	}

	public Boolean isOpolEditButtonDisplayed() {
		return opolEditButton.isDisplayed();
	}

	public Boolean isHintForHistoricalDemandDisplayed() {
		return hintForHistoricalDemand.isDisplayed();
	}

	public Boolean isHintForOrderCandidateDisplayed() {
		return hintForOrderCandidate.isDisplayed();
	}

	public Boolean isHintForCandidateForReturnPeriodDisplayed() {
		return hintForCandidateForReturnPeriod.isDisplayed();
	}

	public Boolean isPreventExcessOrdersCancelButtonDisplayed() {
		return preventExcessOrdersCancelButton.isDisplayed();
	}

	public Boolean isPreventExcessOrdersEditOrSaveButtonDisplayed() {
		return preventExcessOrdersEditOrSaveButton.isDisplayed();
	}

	public Boolean isManageExcludedItemsButtonDisplayed() {
		return manageExcludedItemsButton.isDisplayed();
	}

	public Boolean isOrderQuantityYDisplayed() {
		return orderQuantityY.isDisplayed();
	}

	public Boolean isOrderQuantityZDisplayed() {
		return orderQuantityZ.isDisplayed();
	}

	public Boolean isOrderQuantityXDisplayed() {
		return orderQuantityX.isDisplayed();
	}

	public Boolean isOrderQuantityADisplayed() {
		return orderQuantityA.isDisplayed();
	}

	public Boolean isorderLimitQDisplayed() {
		return orderLimitQ.isDisplayed();
	}

	public Boolean isPreventExcessOrdersDeleteButton() {
		return preventExcessOrdersDeleteButton.isDisplayed();
	}

	public Boolean isGridOverridesEditButtonDisplayed() {
		return gridOverridesEditButton.isDisplayed();
	}

	public Boolean isStartDateDisplayed() {
		return startDate.isDisplayed();
	}

	public Boolean isEndDateDisplayed() {
		return endDate.isDisplayed();
	}

	public Boolean isEslmKDisplayed() {
		return eslmK.isDisplayed();
	}

	public Boolean isEslmKMaxDisplayed() {
		return eslmKMax.isDisplayed();
	}

	public Boolean isEslmRiskFactorDisplayed() {
		return eslmRiskFactor.isDisplayed();
	}

	public Boolean isServiceLevelDisplayed() {
		return serviceLevel.isDisplayed();
	}

	public Boolean isNumberOfWeeksDisplayed() {
		return numberOfWeeks.isDisplayed();
	}

	public Boolean isGridOverridesCancelDisplayed() {
		return gridOverridesCancelButton.isDisplayed();
	}

	public Boolean isGridOverridesSaveDisplayed() {
		return gridOverridesSaveButton.isDisplayed();
	}

	public Boolean isClickOrderingSaveButton() {
		return orderingSaveButton.isDisplayed();
	}

	public Boolean isDeleteOrderingButton() {
		return orderingDeleteButton.isDisplayed();
	}

	public Boolean areProductTemplatesTypeInCheckBoxesDisplayed(String templateType) {
		Boolean displayed = false;
		List<WebElement> templates = productTemplatesTypeCheckBoxes.findElements(By.cssSelector("mat-option"));
		for (WebElement template : templates) {
			if (template.getAttribute("ng-reflect-value").equals(templateType)) {
				displayed = template.isDisplayed();
			}
		}
		return displayed;
	}

	/*
	 * Table handle
	 */
	public List<WebElement> getRowsFromTable() {
		return templatesTable.findElements(By.cssSelector("mat-expansion-panel"));
	}

	public List<Map<String, String>> getElementsInRowsFromTable() {
		List<Map<String, String>> templatesMaps = new ArrayList<>();
		List<WebElement> rows = templatesTable.findElements(By.cssSelector(
				"tr > td > virtual-scroll > div.scrollable-content > mat-accordion > mat-expansion-panel > mat-expansion-panel-header > span > mat-panel-description > div"));

		for (WebElement row : rows) {
			Map<String, String> template = new HashMap<>();
			template.put("TemplateType", row.findElement(By.cssSelector("span:nth-child(1)")).getText());
			template.put("Created", row.findElement(By.cssSelector("span:nth-child(2)")).getText());
			template.put("LastModified", row.findElement(By.cssSelector("span:nth-child(3)")).getText());
			template.put("Level", row.findElement(By.cssSelector("span:nth-child(4)")).getText());
			template.put("StoreGroup", row.findElement(By.cssSelector("span:nth-child(5)")).getText());
			templatesMaps.add(template);
		}
		return templatesMaps;
	}

	public void selectAndExpandElementInTable(String templateType) throws InterruptedException {
		synchronized (this) {
			wait(5000);
		}
		List<WebElement> rows = templatesTable.findElements(By.cssSelector(
				"tr > td > virtual-scroll > div.scrollable-content > mat-accordion > mat-expansion-panel > mat-expansion-panel-header > span > mat-panel-description > div"));
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(By.cssSelector("span:nth-child(1)")), 30);
		for (WebElement row : rows) {
			if (row.findElement(By.cssSelector("span:nth-child(1)")).getText().equals(templateType)) {
				row.click();
				ui().waitForCondition(driver,
						ExpectedConditions.visibilityOf(row.findElement(By.cssSelector("span:nth-child(6) > button"))),
						30);
				WebElement expansionButton = row.findElement(By.cssSelector("span:nth-child(6) > button"));
				expansionButton.click();
				break;
			}
		}
	}

	public void selectAndExpandNotGlobalElementInTableForProduct(String templateType) throws InterruptedException {
		synchronized (this) {
			wait(5000);
		}
		List<WebElement> rows = templatesTable.findElements(By.cssSelector(
				"tr > td > virtual-scroll > div.scrollable-content > mat-accordion > mat-expansion-panel > mat-expansion-panel-header > span > mat-panel-description > div"));
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(By.cssSelector("span:nth-child(1)")), 30);
		for (WebElement row : rows) {

			if (row.findElement(By.cssSelector("span:nth-child(1)")).getText().equals(templateType)
					&& !row.findElement(By.cssSelector("span:nth-child(5)")).getText().equals("GLOBAL")) {
				row.click();
				ui().waitForCondition(driver,
						ExpectedConditions.visibilityOf(row.findElement(By.cssSelector("span:nth-child(7) > button"))),
						30);
				WebElement expansionButton = row.findElement(By.cssSelector("span:nth-child(7) > button"));
				expansionButton.click();
				break;
			}
		}
	}

	public String checkChevronStatusForProduct(String templateType) {
		String status = "";
		List<WebElement> rows = templatesTable.findElements(By.cssSelector(
				"tr > td > virtual-scroll > div.scrollable-content > mat-accordion > mat-expansion-panel > mat-expansion-panel-header > span > mat-panel-description > div"));
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(By.cssSelector("span:nth-child(1)")), 30);
		for (WebElement row : rows) {

			if (row.findElement(By.cssSelector("span:nth-child(1)")).getText().equals(templateType)) {
				ui().waitForCondition(driver,
						ExpectedConditions.visibilityOf(row.findElement(By.cssSelector("span:nth-child(7) > button"))),
						30);
				status = row.findElement(By.cssSelector("span:nth-child(7) > button > span > mat-icon")).getText();
				break;
			}
		}
		return status;
	}

	public List<Boolean> areElementsInTableForProductDisplayed() throws InterruptedException {
		synchronized (this) {
			wait(5000);
		}
		List<Boolean> allDisplayed = new ArrayList<>();
		List<WebElement> rows = templatesTable.findElements(By.cssSelector(
				"tr > td > virtual-scroll > div.scrollable-content > mat-accordion > mat-expansion-panel > mat-expansion-panel-header > span > mat-panel-description > div"));
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(By.cssSelector("span:nth-child(1)")), 30);
		for (WebElement row : rows) {
			allDisplayed.add(row.findElement(By.cssSelector("span:nth-child(6)")).isDisplayed());
			allDisplayed.add(row.findElement(By.cssSelector("span:nth-child(5)")).isDisplayed());
			allDisplayed.add(row.findElement(By.cssSelector("span:nth-child(4)")).isDisplayed());
			allDisplayed.add(row.findElement(By.cssSelector("span:nth-child(3)")).isDisplayed());
			allDisplayed.add(row.findElement(By.cssSelector("span:nth-child(2)")).isDisplayed());
			allDisplayed.add(row.findElement(By.cssSelector("span:nth-child(1)")).isDisplayed());
		}
		return allDisplayed;
	}

	public String getAndExpandArrowInTable(String templateType) {
		String arrowStatus = "";
		List<WebElement> rows = templatesTable.findElements(By.cssSelector(
				"tr > td > virtual-scroll > div.scrollable-content > mat-accordion > mat-expansion-panel > mat-expansion-panel-header > span > mat-panel-description > div"));
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(By.cssSelector("span:nth-child(1)")), 30);
		for (WebElement row : rows) {
			if (row.findElement(By.cssSelector("span:nth-child(1)")).getText().equals(templateType)) {
				ui().waitForCondition(driver,
						ExpectedConditions.visibilityOf(row.findElement(By.cssSelector("span:nth-child(6) > button"))),
						30);
				WebElement expansionButton = row.findElement(By.cssSelector("span:nth-child(6) > button"));
				arrowStatus = expansionButton.getText();
			}
		}

		return arrowStatus;
	}

	public Boolean findTemplateInTable(String templateType) {
		Boolean found = false;
		List<WebElement> rows = templatesTable.findElements(By.cssSelector(
				"tr > td > virtual-scroll > div.scrollable-content > mat-accordion > mat-expansion-panel > mat-expansion-panel-header > span > mat-panel-description > div"));
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(By.cssSelector("span:nth-child(1)")), 15);
		for (WebElement row : rows) {
			if (row.findElement(By.cssSelector("span:nth-child(1)")).getText().equals(templateType)) {
				found = true;
				break;
			}
		}
		return found;
	}

	public List<Boolean> checkIfChoosenTemplateIsInTableForProduct(String templateType) throws InterruptedException {
		synchronized (this) {
			wait(5000);
		}
		Boolean found = false;
		List<Boolean> allDisplayed = new ArrayList<>();
		List<WebElement> rows = templatesTable.findElements(By.cssSelector(
				"tr > td > virtual-scroll > div.scrollable-content > mat-accordion > mat-expansion-panel > mat-expansion-panel-header > span > mat-panel-description > div"));
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(By.cssSelector("span:nth-child(1)")), 15);
		for (WebElement row : rows) {
			if (row.findElement(By.cssSelector("span:nth-child(1)")).getText().equals(templateType)) {
				found = true;
			}
			allDisplayed.add(found);
		}
		return allDisplayed;
	}

	public List<Boolean> checkIfTwoChoosenTemplateAreInTableForProduct(String templateType0, String templateType1)
			throws InterruptedException {
		synchronized (this) {
			wait(5000);
		}
		Boolean found = false;
		List<Boolean> allDisplayed = new ArrayList<>();
		List<WebElement> rows = templatesTable.findElements(By.cssSelector(
				"tr > td > virtual-scroll > div.scrollable-content > mat-accordion > mat-expansion-panel > mat-expansion-panel-header > span > mat-panel-description > div"));
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(By.cssSelector("span:nth-child(1)")), 15);
		for (WebElement row : rows) {
			if (row.findElement(By.cssSelector("span:nth-child(1)")).getText().equals(templateType0)
					|| row.findElement(By.cssSelector("span:nth-child(1)")).getText().equals(templateType1)) {
				found = true;
			}
			allDisplayed.add(found);
		}
		return allDisplayed;
	}

	public void calendarDayClick(String day) {
		List<WebElement> monthDays = calendar.findElements(
				By.cssSelector("div.mat-calendar-content > mat-month-view > table > tbody > tr:nth-child(2)"));
		for (WebElement monthDay : monthDays) {
			if (day.equals(monthDay.getText())) {
				monthDay.click();
			}
		}
	}

	public Boolean checkCalendarDayIsToday(String today) {
		Boolean found = false;
		List<WebElement> calendarRows = calendar.findElements(
				By.cssSelector("#mat-datepicker-0 > div.mat-calendar-content > mat-month-view > table > tbody > tr"));
		for (WebElement row : calendarRows) {
			List<WebElement> weeks = row.findElements(By.className("mat-calendar-body-cell"));
			for (WebElement week : weeks) {
				if (week.getAttribute("aria-label").equals(today)) {
					found = true;
					break;
				}
			}
		}
		return found;
	}

	/*
	 * Read only
	 */
	public String isFromDateReadOnly() {
		return fromDateInput.getAttribute("ng-reflect-readonly");
	}

	public String isHistoricalDemandPeriodReadOnly() {
		return historicalDemandPeriodInput.getAttribute("ng-reflect-readonly");
	}

	public String isReplenishmentOrderCandidateReadOnly() {
		return replenishmentOrderCandidateInput.getAttribute("ng-reflect-readonly");
	}

	public String isReturnsPeriodInputReadOnly() {
		return returnsPeriodInput.getAttribute("ng-reflect-readonly");
	}

	public String isOpolTimeInputReadOnly() {
		return opolTimeInput.getAttribute("ng-reflect-readonly");
	}

	public String isPreventExcessOrdersEditOrSaveButtonReadOnly() {
		return preventExcessOrdersEditOrSaveButton.getAttribute("ng-reflect-readonly");
	}

	public String isOrderQuantityYReadOnly() {
		return orderQuantityY.getAttribute("ng-reflect-readonly");
	}

	public String isOrderQuantityZReadOnly() {
		return orderQuantityZ.getAttribute("ng-reflect-readonly");
	}

	public String isOrderQuantityXReadOnly() {
		return orderQuantityX.getAttribute("ng-reflect-readonly");
	}

	public String isOrderQuantityAReadOnly() {
		return orderQuantityA.getAttribute("ng-reflect-readonly");
	}

	public String isOrderLimitQReadOnly() {
		return orderLimitQ.getAttribute("ng-reflect-readonly");
	}

	public String isEslmKReadOnly() {
		return eslmK.getAttribute("ng-reflect-readonly");
	}

	public String isEslmKMaxReadOnly() {
		return eslmKMax.getAttribute("ng-reflect-readonly");
	}

	public String isEslmRiskFactorReadOnly() {
		return eslmRiskFactor.getAttribute("ng-reflect-readonly");
	}

	public String isNumberOfWeeksReadOnly() {
		return numberOfWeeks.getAttribute("ng-reflect-readonly");
	}

	public String isManualOrderCapReadOnly() {
		return manualOrderCapUnitsInput.getAttribute("ng-reflect-readonly");
	}

	public String isOnHandForwardView() {
		return onHandForwardView.getAttribute("ng-reflect-readonly");
	}

	public String isOnHandBackwardViewReadOnly() {
		return onHandBackwardView.getAttribute("ng-reflect-readonly");
	}

	/*
	 * Hot keys
	 */

	public void tabFromEslmKOverrideValue() {
		eslmKOverrideValue.sendKeys(Keys.TAB);
	}

	public void tabFromEslmkToggle() {
		eslmkToggle.sendKeys(Keys.TAB);
	}

	public void tabFromSearchProductInput() {
		searchProductInput.sendKeys(Keys.TAB);
	}

	public void escapeFromCheckBox() {
		opolCheckBox.sendKeys(Keys.ESCAPE);
	}

	public void tabFromCalendar() {
		inputCalendar.sendKeys(Keys.TAB);
	}

	public void tabFromOpolTimeInput() {
		opolTimeInput.sendKeys(Keys.TAB);
	}

	public void deleteInCalendar() {
		calendar.sendKeys(Keys.CANCEL);
	}

	public void tabFromOrderQuantityY() {
		orderQuantityY.sendKeys(Keys.TAB);
	}

	public void tabFromStartDate() {
		startDate.sendKeys(Keys.TAB);
	}

	public void tabFormManualOrderCapUnits() {
		manualOrderCapUnitsInput.sendKeys(Keys.TAB);
	}

	public void tabFromFromDateInput() {
		fromDateInput.sendKeys(Keys.TAB);
	}

	/*
	 * Actions
	 */
	public void setFocusOnCalendar() {
		Actions action = new Actions(driver);
		action.moveToElement(calendar);
	}

	/*
	 * Enabled
	 */
	public Boolean isFromInputExcludeEnabled() {
		return fromInputExclude.isEnabled();
	}

	public Boolean isEndInputExcludeEnabled() {
		return endInputExclude.isEnabled();
	}

	public Boolean isManualOrderToggleEnabled() {
		return manualOrderToggle.isEnabled();
	}

	public Boolean isFromDateInputEnabled() {
		return fromDateInput.isEnabled();
	}

	public Boolean isInputCalendarEnabled() {
		return inputCalendar.isEnabled();
	}

	public Boolean isAddToListButtonEnabled() {
		return addToListButton.isEnabled();
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
