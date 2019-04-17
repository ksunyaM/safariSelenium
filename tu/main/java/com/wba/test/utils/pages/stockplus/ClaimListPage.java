/* Copyright 2018 Walgreen Co. */
package com.wba.test.utils.pages.stockplus;

import static com.oneleo.test.automation.core.UIUtils.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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

public class ClaimListPage extends LoadableComponent<ClaimListPage> {

	private WebDriver driver;
	@FindBy(id = "page-header-back-button")
	private WebElement pageHeaderBackButton;
	@FindBy(css = "#page-header-title")
	private WebElement pageHeaderTitle;
	@FindBy(id = "claim-search-input")
	private WebElement itemSearchInput;
	@FindBy(id = "claim-filter")
	private WebElement claimFilter;
	@FindBy(id = "claim-date-from")
	private WebElement createdBeforeInput;
	@FindBy(id = "claim-date-to")
	public WebElement createdAfterInput;
	@FindBy(id = "cdk-overlay-0")
	public WebElement filterOverlay;

	@FindBy(id = "search-button")
	private WebElement searchButton;
	@FindBy(id = "clear-button")
	private WebElement clearButton;
	@FindBy(css = "#claim-list-container > rx-returns-claim-search > mat-card > div.padding-top-2x.returnsClaimSearchForm > form > button.mat-button.mat-tertiary")
	private WebElement createAClaimButton;

	@FindBy(css = "#claim-list-container > rx-returns-claim-search > mat-card > div.margin-top-3x")
	private WebElement claimsFoundDiv;

	@FindBy(id = "store-information")
	private WebElement storeInformation;

	@FindBy(id = "rc-table")
	private WebElement claimListTable;

	@FindBy(css = "#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-returns-management > div > rx-return-claim-detail > div > rx-claim-details-info > div > mat-card > div:nth-child(3) > div:nth-child(1)")
	private WebElement claimTypeDiv;
	@FindBy(css = "#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-returns-management > div > rx-return-claim-detail > div > rx-claim-details-info > div > mat-card > div:nth-child(3) > div:nth-child(2)")
	private WebElement scheduleDiv;
	@FindBy(css = "#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-returns-management > div > rx-return-claim-detail > div > rx-claim-details-info > div > mat-card > div:nth-child(3) > div:nth-child(3)")
	private WebElement createdDiv;
	@FindBy(css = "#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-returns-management > div > rx-return-claim-detail > div > rx-claim-details-info > div > mat-card > div:nth-child(3) > div:nth-child(4)")
	private WebElement supplierNumberDiv;
	@FindBy(css = "#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-returns-management > div > rx-return-claim-detail > div > rx-claim-details-info > div > mat-card > div:nth-child(3) > div:nth-child(5)")
	private WebElement lastEditedByDiv;
	@FindBy(css = "#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-returns-management > div > rx-return-claim-detail > div > rx-claim-details-info > div > mat-card > div:nth-child(3) > div:nth-child(7)")
	private WebElement totalCostDiv;
	@FindBy(css = "#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-returns-management > div > rx-return-claim-detail > div > rx-claim-details-info > div > mat-card > div:nth-child(3) > div:nth-child(8)")
	private WebElement returnReasonDiv;

	@FindBy(css = "#item-description-header")
	private WebElement itemDescriptionHeader;
	@FindBy(css = "#manufacturer-header")
	private WebElement manufacturerHeader;
	@FindBy(css = "#ormd-header")
	private WebElement ormdHeader;
	@FindBy(css = "#ndc-header")
	private WebElement ndcRowHeader;
	@FindBy(css = "#upc-header")
	private WebElement upcHeader;
	@FindBy(css = "#wic-header")
	private WebElement wicHeader;
	@FindBy(css = "#cost-header")
	private WebElement costHeader;
	@FindBy(css = "#claim-qty-header")
	private WebElement claimQtyHeader;
	@FindBy(css = "#approved-qty-header")
	private WebElement approvedQtyHeader;
	@FindBy(css = "#shipped-qty-header")
	private WebElement shippedQtyHeader;
	@FindBy(id = "clam-detail-print-button")
	private WebElement printMaterialsButton;

	@FindBy(id = "mat-option-0")
	private WebElement selectAllCheckbox;

	@FindBy(id = "mat-option-1")
	private WebElement typeVendorCheckbox;
	@FindBy(id = "mat-option-2")
	private WebElement typeControlSalvageCheckbox;
	@FindBy(id = "mat-option-3")
	private WebElement typeNONControlSalvageCheckbox;

	@FindBy(id = "mat-option-4")
	private WebElement statusOpenCheckbox;
	@FindBy(id = "mat-option-5")
	private WebElement statusPendingCheckbox;
	@FindBy(id = "mat-option-6")
	private WebElement statusApprovedCheckbox;
	@FindBy(id = "mat-option-7")
	private WebElement statusConfirmedCheckbox;
	@FindBy(id = "mat-option-8")
	private WebElement statusShippedCheckbox;
	@FindBy(id = "mat-option-9")
	private WebElement statusDeniedCheckbox;

	@FindBy(id = "mat-option-10")
	private WebElement statusClosedCheckbox;

	@FindBy(id = "print-check-box-0")
	private WebElement manifestChecbox;
	@FindBy(xpath = "//*[@id=\"mat-option-0\"]/span[1]/mat-pseudo-checkbox")
	private WebElement boxCheckbox;
	@FindBy(id = "rx-return-management-create-claim-cancel-button")
	private WebElement cancelPrintButton;
	@FindBy(id = "rx-return-management-delete-claim-delete-button")
	private WebElement printButton;

	@FindBy(id = "claim-list-container")
	private WebElement claimListContainer;

	@FindBy(id = "claim-list-view-button")
	private WebElement claimListViewButton;

	@FindBy(id = "mat-error-1")
	private WebElement claimNumberMsg;

	@FindBy(css = "#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-returns-management > div > rx-return-claim-detail > div > rx-claim-details-info > div > mat-card > div:nth-child(3) > div:nth-child(8)")
	private WebElement fedexLabel;
	@FindBy(css = "#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-returns-management > div > rx-return-claim-detail > div > rx-claim-details-info > div > mat-card > div:nth-child(3) > div:nth-child(9)")
	private WebElement trackingNoDiv;

	@FindBy(css = "#claim-list-container > rx-returns-claim-search > mat-card > div.margin-top-3x > span.text-heading")
	private WebElement numberOfClaimsFound;
	@FindBy(css = "#claim-list-container > rx-returns-claim-search > mat-card > div.margin-top-3x > span.text-body")
	private WebElement claimsFoundLabel;
	@FindBy(css = "#store-information > div:nth-child(1) > span.text-body.black-87-opacity")
	private WebElement storeNumberInCard;

	@FindBy(css = "#claim-list-container > rx-returns-claim-search > mat-card > div.padding-top-2x.returnsClaimSearchForm > form > mat-form-field.mat-input-container.mat-form-field.ng-tns-c15-4.mat-form-field-type-mat-input.mat-form-field-can-float.mat-form-field-should-float.mat-primary.ng-dirty.ng-invalid.mat-input-invalid.mat-form-field-invalid.ng-touched > div > div.mat-input-subscript-wrapper.mat-form-field-subscript-wrapper > div")
	private WebElement beforeDateErrorMessage;
	@FindBy(css = "#claim-list-container > rx-returns-claim-search > mat-card > div.padding-top-2x.returnsClaimSearchForm > form > mat-form-field.mat-input-container.mat-form-field.ng-tns-c15-5.mat-form-field-type-mat-input.mat-form-field-can-float.mat-form-field-should-float.mat-primary.ng-dirty.ng-invalid.mat-input-invalid.mat-form-field-invalid.ng-touched > div > div.mat-input-subscript-wrapper.mat-form-field-subscript-wrapper > div")
	private WebElement afterDateError;

	@FindBy(css = "#claim-list-container > rx-returns-claim-search > mat-card > div.padding-top-2x.returnsClaimSearchForm > form > mat-form-field.mat-input-container.mat-form-field.ng-tns-c15-2.mat-form-field-type-mat-select.mat-form-field-can-float.mat-form-field-should-float.mat-primary.ng-valid.ng-dirty.ng-touched > div > div.mat-input-flex.mat-form-field-flex > div")
	private WebElement noneLabelInFilter;

	@FindBy(css = "#claim-list-container > div.text-align-center.margin-top-10x")
	private WebElement noClaimsFoundMessage;

	/*
	 * Create a claim popup
	 */
	@FindBy(id = "mat-select-1")
	private WebElement selectClaimTypeMatSelect;
	@FindBy(id = "mat-select-2")
	private WebElement selectAReasonMatSelect;
	@FindBy(css = "#cdk-overlay-0 > mat-dialog-container > rx-create-claim-dialog > div > button.mat-raised-button.mat-tertiary")
	private WebElement cancelButton;
	@FindBy(css = "#cdk-overlay-0 > mat-dialog-container > rx-create-claim-dialog > div > button.mat-raised-button.mat-primary")
	private WebElement createButton;
	@FindBy(css = "#mat-select-1")
	private WebElement selectAClaimTypeMatSelect;
	@FindBy(id = "mat-select-2")
	private WebElement selectReasonCodeMatSelect;
	@FindBy(id = "cdk-overlay-1")
	private WebElement claimTypesOverlay;
	@FindBy(id = "cdk-overlay-2")
	private WebElement reasonCodeOverlay;
	@FindBy(css = "#cdk-overlay-0 > mat-dialog-container > rx-create-claim-dialog > mat-form-field.dialog-select.mat-input-container.mat-form-field.ng-tns-c15-13.mat-form-field-type-mat-select.mat-form-field-can-float.mat-form-field-should-float.mat-primary > div > div.mat-input-flex.mat-form-field-flex > div > div > span")
	private WebElement reasonPlaceHolder;

	@FindBy(id = "mat-select-3")
	private WebElement selectAClaimTypeMatSelectToCreate;
	@FindBy(id = "cdk-overlay-3")
	private WebElement claimTypesOverlayToCreate;
	@FindBy(id = "mat-select-4")
	private WebElement selectReasonCodeMatSelectToCreate;
	@FindBy(id = "cdk-overlay-7")
	private WebElement reasonCodeOverlayToCreate;

	public ClaimListPage(WebDriver driver) {
		this.driver = driver;
	}

	/*
	 * Text
	 */
	public String getReasonPlaceHolder() {
		return reasonPlaceHolder.getText();
	}

	public String getNoneLabelInFilter() {
		return noneLabelInFilter.findElement(By.cssSelector("[class='none-label']")).getText();
	}

	public void insertItemSearchInput(String claim) {
		itemSearchInput.sendKeys(claim);
	}

	public String getClaimsFoundLabelText() {
		return claimsFoundLabel.getText();
	}

	public String getStoreNumberInCard() {
		return storeNumberInCard.getText();
	}

	public void insertCreatedBeforeInput(String date) {
		createdBeforeInput.sendKeys(date);
	}

	public void insertCreatedAfterInput(String date) {
		createdAfterInput.sendKeys(date);
	}

	public String getBeforeDate() {
		createdBeforeInput.sendKeys(Keys.TAB);
		return createdBeforeInput.getAttribute("ng-reflect-model");
	}

	/*
	 * Click and select
	 */

	
	public void clickTypeVendorCheckbox() {
		typeVendorCheckbox.click();
	}
	
	public void clickstatusShippedCheckbox() {
		statusShippedCheckbox.click();
	}

	public void clickstatusConfirmedCheckbox() {
		statusConfirmedCheckbox.click();
	}
	

	public void clickAndSelectReasonCodeOverlay() {
		clickSelectReasonCodeMatSelect();
		List<WebElement> types = reasonCodeOverlay.findElements(By.cssSelector("div > div > mat-option"));
		for (WebElement type : types) {
			type.click();
			break;
		}
	}

	public List<String> clickAndGetAClaimNameReasonCodeOverlay() {
		clickSelectReasonCodeMatSelect();
		List<String> salvageClaims = new ArrayList<>();
		List<WebElement> types = reasonCodeOverlay.findElements(By.cssSelector("div > div > mat-option > span"));
		for (WebElement type : types) {
			salvageClaims.add(type.getText());
		}
		return salvageClaims;
	}

	public void clickAndSelectAClaimTypeMatSelectByName(String nameType) {
		clickAClaimTypeMatSelect();
		List<WebElement> types = claimTypesOverlay.findElements(By.cssSelector("div > div > mat-option > span"));
		for (WebElement type : types) {
			if (type.getText().equals(nameType)) {
				type.click();
				break;
			}
		}
	}

	public void clickAndSelectAClaimTypeMatSelect() {
		clickAClaimTypeMatSelect();
		List<WebElement> types = claimTypesOverlay.findElements(By.cssSelector("div > div > mat-option"));
		for (WebElement type : types) {
			type.click();
			break;
		}
	}

	public List<String> clickAndGetAClaimName() {
		List<String> claimNames = new ArrayList<>();
		List<WebElement> types = claimTypesOverlay.findElements(By.cssSelector("div > div > mat-option > span"));
		for (WebElement type : types) {
			claimNames.add(type.getText());
		}
		return claimNames;
	}

	// Create after delete
	public void clickSelectAClaimTypeMatSelectToCreate() {
		selectAClaimTypeMatSelectToCreate.click();
	}

	public void clickSelectReasonCodeMatSelectToCreate() {
		selectReasonCodeMatSelectToCreate.click();
	}

	public void clickAndSelectAClaimTypeMatSelectByNameToCreate(String nameType) {
		clickSelectAClaimTypeMatSelectToCreate();
		List<WebElement> types = claimTypesOverlayToCreate
				.findElements(By.cssSelector("div > div > mat-option > span"));
		for (WebElement type : types) {
			if (type.getText().equals(nameType)) {
				type.click();
				break;
			}
		}
	}

	public void clickAndSelectReasonCodeOverlayToCreate() {
		clickSelectReasonCodeMatSelectToCreate();
		List<WebElement> types = reasonCodeOverlayToCreate.findElements(By.cssSelector("div > div > mat-option"));
		for (WebElement type : types) {
			type.click();
			break;
		}
	}
	/////////////////////////////////////////////

	public void clickAClaimTypeMatSelect() {
		selectAClaimTypeMatSelect.click();
	}

	public void clickSelectReasonCodeMatSelect() {
		selectReasonCodeMatSelect.click();
	}

	public void selectFilter(String filter) {
		itemSearchInput.click();
		List<WebElement> filters = filterOverlay.findElements(By.cssSelector("mat-option"));
	}

	public List<WebElement> tableElementsDisplayed() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(claimListTable), 30);
		return claimListTable.findElements(By.cssSelector("mat-row"));
	}

	public Boolean selectTableElementsByStatus(String status) {
		Boolean found = false;
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(claimListTable), 30);
		List<WebElement> rows = tableElementsDisplayed();
		for (WebElement row : rows) {
			List<WebElement> cells = row.findElements(By.cssSelector("mat-cell"));
			for (WebElement cell : cells) {
				if (cell.getText().equals(status)) {
					found = true;
					row.click();
					break;
				}
			}
			if (found) {
				break;
			}
		}
		return found;
	}

	public Boolean selectTableElementsByStatusAndClaimType(String claimType, String status) {
		Boolean found = false;
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(claimListTable), 60);
		List<WebElement> rows = tableElementsDisplayed();
		for (WebElement row : rows) {
			List<WebElement> cells = row.findElements(By.cssSelector("mat-cell"));
			for (WebElement cellClaim : cells) {
				if (cellClaim.getText().contains(claimType)) {
					for (WebElement cellType : cells) {
						if (cellType.getText().equals(status)) {
							found = true;
							row.click();
							break;
						}
					}
				}
			}
			if (found) {
				break;
			}
		}
		return found;
	}
	
	

	public void selectFirstTableElement() {
		List<WebElement> rows = tableElementsDisplayed();
		for (WebElement row : rows) {
			row.click();
			break;
		}
	}

	public int checkNumberOfTableElementSelected() throws InterruptedException {
		int counter = 0;
		List<WebElement> rows = tableElementsDisplayed();
		rows.get(0).click();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		rows.get(1).click();
		for (WebElement row : rows) {
			if (row.getAttribute("class").equals("mat-row selected")) {
				counter++;
			}
		}
		return counter;
	}

	/*
	 * Displayed
	 */
	public Boolean selectClaimTypeMatSelectDisplayed() {
		return selectClaimTypeMatSelect.isDisplayed();
	}

	public Boolean selectAReasonMatSelectDisplayed() {
		return selectAReasonMatSelect.isDisplayed();
	}

	public Boolean cancelButtonDisplayed() {
		return cancelButton.isDisplayed();
	}

	public Boolean createButtonDisplayed() {
		return createButton.isDisplayed();
	}

	public Boolean numberOfClaimsFoundDisplayed() {
		return numberOfClaimsFound.isDisplayed();
	}

	public Boolean pageHeaderBackButtonDisplayed() {
		return pageHeaderBackButton.isDisplayed();
	}

	public Boolean pageHeaderTitleDisplayed() {
		return pageHeaderTitle.isDisplayed();
	}

	public Boolean itemSearchInputDisplayed() {
		return itemSearchInput.isDisplayed();
	}

	public Boolean claimFilterDisplayed() {
		return claimFilter.isDisplayed();
	}

	public Boolean createdBeforeInputDisplayed() {
		return createdBeforeInput.isDisplayed();
	}

	public Boolean createdAfterInputDisplayed() {
		return createdAfterInput.isDisplayed();
	}

	public Boolean searchButtonDisplayed() {
		return searchButton.isDisplayed();
	}

	public Boolean clearButtonDisplayed() {
		return clearButton.isDisplayed();
	}

	public Boolean claimsFoundDivDisplayed() {
		return claimsFoundDiv.isDisplayed();
	}

	public Boolean storeInformationDisplayed() {
		return storeInformation.isDisplayed();
	}

	/*
	 * Checked
	 */

	public String checkValueStatus(int j) {

		WebElement status = driver.findElement(By.xpath("//*[@id=\"return-claims-row-" + j + "\"]/mat-cell[3]"));

		return status.getText();
	}

	public String boxCheckboxChecked() {
		return boxCheckbox.getAttribute("ng-reflect-checked");
	}

	public String manifestChecboxChecked() {
		return manifestChecbox.getAttribute("ng-reflect-checked");
	}

	public String checkPageHeaderTitle() {
		return pageHeaderTitle.getText();
	}

	public String checkClaimListContainer() {
		return claimListContainer.getText();
	}

	public String checkClaimInputMsg() {
		return claimNumberMsg.getText();
	}

	public String getTextInItemSearchInput() {
		return itemSearchInput.getAttribute("value");
	}

	/*
	 * Displayed
	 */
	public Boolean createAClaimButtonDisplayed() {
		return createAClaimButton.isDisplayed();
	}

	public List<WebElement> storeInformationElementsDisplayed() {
		return storeInformation.findElements(By.cssSelector("div"));
	}

	public Boolean claimListViewButtonDisplayed() {
		return claimListViewButton.isDisplayed();
	}

	public Boolean claimListContainerDisplayed() {
		return claimListContainer.isDisplayed();
	}

	public Boolean storeInformationisDisplayed() {
		return storeInformation.isDisplayed();
	}

	public Boolean claimFilterisDisplayed() {
		return claimFilter.isDisplayed();
	}

	public Boolean boxCheckboxDisplayed() {
		return boxCheckbox.isDisplayed();
	}

	public Boolean manifestChecboxDisplayed() {
		return manifestChecbox.isDisplayed();
	}

	public Boolean selectAllCheckboxDisplayed() {
		return selectAllCheckbox.isDisplayed();
	}

	public Boolean allCheckboxSelected() {
		return selectAllCheckbox.isSelected();
	}

	public Boolean typeVendorCheckboxDisplayed() {
		return typeVendorCheckbox.isDisplayed();
	}

	public Boolean typeControlSalvageCheckboxDisplayed() {
		return typeControlSalvageCheckbox.isDisplayed();
	}

	public Boolean typeNONControlSalvageCheckboxDisplayed() {
		return typeNONControlSalvageCheckbox.isDisplayed();
	}

	public Boolean statusOpenCheckboxDisplayed() {
		return statusOpenCheckbox.isDisplayed();
	}

	public Boolean statusPendingCheckboxDisplayed() {
		return statusPendingCheckbox.isDisplayed();
	}

	public Boolean statusApprovedCheckboxDisplayed() {
		return statusApprovedCheckbox.isDisplayed();
	}

	public Boolean statusConfirmedCheckboxDisplayed() {
		return statusConfirmedCheckbox.isDisplayed();
	}

	public Boolean statusShippedCheckboxDisplayed() {
		return statusShippedCheckbox.isDisplayed();
	}

	public Boolean statusDeniedCheckboxDisplayed() {
		return statusDeniedCheckbox.isDisplayed();
	}

	public Boolean statusClosedCheckboxDisplayed() {
		return statusClosedCheckbox.isDisplayed();
	}

	public Boolean cancelPrintButtonDisplayed() {
		return cancelPrintButton.isDisplayed();
	}

	public Boolean printButtonDisplayed() {
		return printButton.isDisplayed();
	}

	public Boolean fedexLabelDisplayed() {
		return fedexLabel.isDisplayed();
	}

	public Boolean trackingNoDivDisplayed() {
		return trackingNoDiv.isDisplayed();
	}

	public Boolean printMaterialsButtonDisplayed() {
		return printMaterialsButton.isDisplayed();
	}

	public Boolean claimTypeDivDisplayed() {
		return claimTypeDiv.isDisplayed();
	}

	public Boolean scheduleDivDisplayed() {
		return scheduleDiv.isDisplayed();
	}

	public Boolean createdDivDisplayed() {
		return createdDiv.isDisplayed();
	}

	public Boolean supplierNumberDivDisplayed() {
		return supplierNumberDiv.isDisplayed();
	}

	public Boolean lastEditedByDivDisplayed() {
		return lastEditedByDiv.isDisplayed();
	}

	public Boolean totalCostDivDisplayed() {
		return totalCostDiv.isDisplayed();
	}

	public Boolean returnReasonDivDisplayed() {
		return returnReasonDiv.isDisplayed();
	}

	public Boolean itemDescriptionHeaderDisplayed() {
		return itemDescriptionHeader.isDisplayed();
	}

	public Boolean manufacturerHeaderDisplayed() {
		return manufacturerHeader.isDisplayed();
	}

	public Boolean ormdHeaderDisplayed() {
		return ormdHeader.isDisplayed();
	}

	public Boolean ndcRowHeaderDisplayed() {
		return ndcRowHeader.isDisplayed();
	}

	public Boolean upcHeaderDisplayed() {
		return upcHeader.isDisplayed();
	}

	public Boolean wicHeaderDisplayed() {
		return wicHeader.isDisplayed();
	}

	public Boolean costHeaderDisplayed() {
		return costHeader.isDisplayed();
	}

	public Boolean claimQtyHeaderDisplayed() {
		return claimQtyHeader.isDisplayed();
	}

	public Boolean approvedQtyHeaderDisplayed() {
		return approvedQtyHeader.isDisplayed();
	}

	public Boolean shippedQtyHeaderDisplayed() {
		return shippedQtyHeader.isDisplayed();
	}

	public int getMapSize() {

		String cntStr = driver
				.findElement(
						By.xpath("//*[@id=\"claim-list-container\"]/rx-returns-claim-search/mat-card/div[2]/span[1]"))
				.getText();
		int cnt = Integer.parseInt(cntStr.trim());
		return cnt;
	}

	public String getStatus(int i) {

		WebElement column = driver.findElement(By.xpath("//*[@id=\"return-claims-row-" + i + "]/mat-cell[3]"));
		// WebElement cell = row.findElement(By.id("srch_result_dist_cell"));

		return column.getText();

	}

	public void insertformSearchInput(String s) {
		itemSearchInput.sendKeys(s);
	}

	/*
	 * Click
	 */
	public ClaimDetailsPage clickCreateButton() {
		createButton.click();
		return new ClaimDetailsPage(driver);
	}

	public ClaimDetailsPage claimListViewButtonClick() {
		claimListViewButton.click();
		return new ClaimDetailsPage(driver);
	}

	public void createAClaimButtonClick() {
		createAClaimButton.click();
	}

	public void allCheckboxClicked() {
		selectAllCheckbox.click();
	}

	public void clickTypeControlSalvageCheckbox() {
		typeControlSalvageCheckbox.click();
	}

	public void clickTypeNONControlSalvageCheckbox() {
		typeNONControlSalvageCheckbox.click();
	}

	public void clickstatusPendingCheckbox() {
		statusPendingCheckbox.click();
	}

	public void clickstatusApprovedCheckbox() {
		statusApprovedCheckbox.click();
	}

	public void clickstatusDeniedCheckbox() {
		statusDeniedCheckbox.click();
	}

	public void clickStatusOpenCheckbox() {
		statusOpenCheckbox.click();
	}

	public void clickConfirmedOpenCheckbox() {
		statusConfirmedCheckbox.click();
	}

	public void clicksearchButton() {
		itemSearchInput.sendKeys(Keys.ESCAPE);
		searchButton.click();
	}

	public void clickSearchClaimButton() {
		searchButton.click();
	}

	public void clearItemSearchInput() {
		itemSearchInput.clear();
	}

	public void clickTabButton() {
		itemSearchInput.sendKeys(Keys.TAB);
	}

	public void clickClaimFilter() {
		claimFilter.click();
	}

	public void selectAllCheckboxClick() {
		selectAllCheckbox.click();
		selectAllCheckbox.sendKeys(Keys.ESCAPE);
	}

	public void printMaterialsButtonClick() {
		printMaterialsButton.click();
	}

	/*
	 * Enabled
	 */
	public Boolean claimListViewButtonEnabled() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(claimListViewButton), 30);
		return claimListViewButton.isEnabled();
	}

	public String createButtonDisabled() {
		return createButton.getAttribute("ng-reflect-disabled");
	}

	/*
	 * Error Messages
	 */
	public String getAfterDateErrorMessage() throws InterruptedException {
		createdAfterInput.sendKeys(Keys.TAB);
		synchronized (this) {
			wait(5000);
		}
		return afterDateError.findElement(By.cssSelector("[class='mat-error']")).getText();
	}

	public String getBeforeDateErrorMessage() throws InterruptedException {
		createdBeforeInput.sendKeys(Keys.TAB);
		synchronized (this) {
			wait(5000);
		}
		return beforeDateErrorMessage.findElement(By.cssSelector("[class='mat-error']")).getText();
	}

	public String getNoClaimsFoundMessage() throws InterruptedException {
		synchronized (this) {
			wait(5000);
		}
		return noClaimsFoundMessage.getText();
	}

	/*
	 * Hot Keys
	 */
	public void escapeFromAllCheckBox() {
		selectAllCheckbox.sendKeys(Keys.ESCAPE);
	}

	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(pageHeaderBackButton)) {
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
