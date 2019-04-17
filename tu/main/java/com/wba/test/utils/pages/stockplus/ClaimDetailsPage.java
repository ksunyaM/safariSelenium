/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.stockplus;

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
import org.openqa.selenium.support.ui.LoadableComponent;

public class ClaimDetailsPage extends LoadableComponent<ClaimDetailsPage> {

	private WebDriver driver;

	@FindBy(css = "body > rx-app > dashboard-view > div > div > div.content-container > rx-returns > rx-returns-claim-details > rx-claim-details > mat-card > div:nth-child(1) > div.text-display-large")
	private WebElement claimNumber;

	@FindBy(css = "body > rx-app > dashboard-view > div > div > div.content-container > rx-returns > rx-returns-claim-details > rx-claim-details > mat-card > div.ng-untouched.ng-pristine.ng-valid > div:nth-child(1) > div")
	private WebElement claimTypeDiv;
	@FindBy(css = "#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-returns-management > div > rx-return-claim-detail > div > rx-claim-details-info > div > mat-card > div:nth-child(3) > div:nth-child(2)")
	private WebElement scheduleDiv;
	@FindBy(css = "body > rx-app > dashboard-view > div > div > div.content-container > rx-returns > rx-returns-claim-details > rx-claim-details > mat-card > div.ng-untouched.ng-pristine.ng-valid > div:nth-child(4) > div")
	private WebElement createdDiv;
	@FindBy(css = "body > rx-app > dashboard-view > div > div > div.content-container > rx-returns > rx-returns-claim-details > rx-claim-details > mat-card > div.ng-untouched.ng-pristine.ng-valid > div:nth-child(2) > div")
	private WebElement supplierNumberDiv;
	@FindBy(css = "body > rx-app > dashboard-view > div > div > div.content-container > rx-returns > rx-returns-claim-details > rx-claim-details > mat-card > div.ng-untouched.ng-pristine.ng-valid > div:nth-child(3) > div")
	private WebElement supplierDiv;
	@FindBy(css = "body > rx-app > dashboard-view > div > div > div.content-container > rx-returns > rx-returns-claim-details > rx-claim-details > mat-card > div.ng-untouched.ng-pristine.ng-valid > div:nth-child(5) > div")
	private WebElement lastEditedByDiv;
	@FindBy(css = "body > rx-app > dashboard-view > div > div > div.content-container > rx-returns > rx-returns-claim-details > rx-claim-details > mat-card > div.ng-untouched.ng-pristine.ng-valid > div:nth-child(6) > div")
	private WebElement totalCostDiv;
	@FindBy(css = "body > rx-app > dashboard-view > div > div > div.content-container > rx-returns > rx-returns-claim-details > rx-claim-details > mat-card > div.ng-untouched.ng-pristine.ng-valid > div:nth-child(7) > div")
	private WebElement returnReasonDiv;

	@FindBy(id = "claim-search-input")
	private WebElement itemSearchInput;
	@FindBy(id = "search-button")
	private WebElement searchButton;
	@FindBy(id = "clear-button")
	private WebElement clearButton;

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

	@FindBy(id = "page-header-title")
	private WebElement pageHeaderTitle;

	@FindBy(id = "claim-filter")
	private WebElement claimFilter;

	@FindBy(id = "claim-list-container")
	private WebElement claimListContainer;

	@FindBy(id = "store-information")
	private WebElement storeInformation;

	@FindBy(id = "claim-list-view-button")
	private WebElement claimListViewButton;

	@FindBy(id = "mat-error-1")
	private WebElement claimNumberMsg;

	@FindBy(css = "#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-returns-management > div > rx-return-claim-detail > div > rx-claim-details-info > div > mat-card > div:nth-child(3) > div:nth-child(8)")
	private WebElement fedexLabel;
	@FindBy(css = "#rx-store-user-app-main-container > dashboard-view > div > div > div.content-container > rx-returns-management > div > rx-return-claim-detail > div > rx-claim-details-info > div > mat-card > div:nth-child(3) > div:nth-child(9)")
	private WebElement trackingNoDiv;

	@FindBy(css = "body > rx-app > dashboard-view > div > div > div.content-container > rx-returns > rx-returns-claim-details > rx-claim-details > mat-card > div:nth-child(1) > div:nth-child(2) > button")
	private WebElement deleteClaimButton;
	@FindBy(css = "#mat-select-1 > div > div.mat-select-arrow-wrapper")
	private WebElement deletionReasonMatSelect;
	@FindBy(id = "cdk-overlay-1")
	private WebElement deleteReasonOverlay;
	@FindBy(css = "#cdk-overlay-0 > mat-dialog-container > rx-delete-claim-dialog > div > button.mat-raised-button.mat-primary")
	private WebElement okButton;
	@FindBy(css = "body > rx-app > dashboard-view > div > div > div.content-container > rx-returns > rx-returns-claim-details > rx-claim-details > mat-card > div:nth-child(1) > div:nth-child(2) > div > button:nth-child(1)")
	private WebElement cancelEditButton;
	@FindBy(css = "body > rx-app > dashboard-view > div > div > div.content-container > rx-returns > rx-returns-claim-details > rx-claim-details > mat-card > div:nth-child(1) > div:nth-child(2) > div > button:nth-child(2)")
	private WebElement saveEditButton;
	@FindBy(id = "page-header-back-button")
	private WebElement backButton;
	@FindBy(css = "body > rx-app > dashboard-view > div > div > div.content-container > rx-returns > rx-returns-claim-details > rx-claim-details > mat-card > div:nth-child(4) > div.text-right > span.text-heading")
	private WebElement itemsNumber;
	@FindBy(css = "body > rx-app > dashboard-view > div > div > div.content-container > rx-returns > rx-returns-claim-details > rx-claim-details > mat-card > div.margin-top-4x.margin-bottom-4x > div.text-heading")
	private WebElement createdDate;
	@FindBy(css = "body > rx-app > dashboard-view > div > div > div.content-container > rx-returns > rx-returns-claim-details > rx-claim-details > mat-card > div:nth-child(1) > div:nth-child(2) > div > button:nth-child(2) > span")
	private WebElement saveEditingClaimButton;

	@FindBy(css = "body > rx-app > dashboard-view > div > div > div.content-container > rx-returns > rx-returns-claim-details > rx-claim-item-list > mat-toolbar > div > mat-toolbar-row > button")
	private WebElement shipBoxButton;
	@FindBy(id = "01")
	private WebElement returnUnitQuantityInput;
	@FindBy(id = "tracking-number")
	private WebElement trackingNumberInput;
	@FindBy(id = "claim-item-list-item-return-quantity-header")
	private WebElement returnUnitQuantityHeader;
	@FindBy(css = "#mat-select-1 > div")
	private WebElement shippingMethodOverlay;
	@FindBy(css = "#cdk-overlay-0")
	private WebElement shippingMethodMatOption;
	@FindBy(css = "body > rx-app > dashboard-view > div > div > div.content-container > rx-returns > rx-returns-claim-details > rx-claim-details > mat-card > div:nth-child(4) > div:nth-child(1) > div:nth-child(2) > span")
	private WebElement shipBoxWarningMessage;
	@FindBy(css = "body > rx-app > dashboard-view > div > div > div.content-container > rx-returns > rx-returns-claim-details > rx-claim-details > mat-card > div:nth-child(4) > div:nth-child(1) > div > span")
	private WebElement scanBoxLabel;
	@FindBy(css = "#mat-select-1 > div > div.mat-select-arrow-wrapper")
	private WebElement formReceivedOverlay;
	@FindBy(css = "#cdk-overlay-0")
	private WebElement formReceivedMatOption;
	@FindBy(css = "#mat-error-2")
	private WebElement wrongTrackingNumberMessage;
	@FindBy(css = "body > rx-app > dashboard-view > div > div > div.content-container > rx-returns > rx-returns-claim-details > rx-claim-details > mat-card > div:nth-child(4) > div:nth-child(1) > div > span")
	private WebElement missingTrackingNumberWarningMessage;
	@FindBy(css = "body > rx-app > dashboard-view > div > div > div.content-container > rx-returns > rx-returns-claim-details > rx-claim-details > mat-card > div:nth-child(4) > div:nth-child(1) > div > mat-icon")
	private WebElement warningIcon;
	@FindBy(css="body > rx-app > dashboard-view > div > div > div.content-container > rx-returns > rx-returns-claim-details > rx-claim-details > mat-card > div:nth-child(4) > div:nth-child(1) > div > span:nth-child(4)")
	private WebElement confirmFormMessage;
	@FindBy(css="body > rx-app > dashboard-view > div > div > div.content-container > rx-returns > rx-returns-claim-details > rx-claim-details > mat-card > div.margin-top-4x.margin-bottom-4x > div.label.error")
	private WebElement deniedLabel;
	@FindBy(css="body > rx-app > dashboard-view > div > div > div.content-container > rx-returns > rx-returns-claim-details > rx-claim-details > mat-card > div.margin-top-4x.margin-bottom-4x > div.label.primary")
	private WebElement approvedLabel;
	@FindBy(css="body > rx-app > dashboard-view > div > div > div.content-container > rx-returns > rx-returns-claim-details > rx-claim-details > mat-card > div:nth-child(4) > div.text-right > span.text-body")
	private WebElement itemsLabel;
	
	@FindBy(id="claim-items-table")
	private WebElement claimItemsTable;
	@FindBy(css="#item-search-info-container > div.padding-6x > div:nth-child(1) > span.text-heading")
	private WebElement itemInClaimsCounter;
	@FindBy(css="#item-search-info-container > div.padding-6x > div:nth-child(1) > span.text-body")
	private WebElement itemInClaimLabel;
	
	/*submit popup*/
	@FindBy(id="thirdQuestion2")
	private WebElement shippingMethodRadioButton;
	@FindBy(css="#mat-radio-3")
	private WebElement firstQuestionNo;
	@FindBy(css="#mat-radio-6")
	private WebElement secondQuestionNo;
	@FindBy(css="#mat-radio-2")
	private WebElement firstQuestionYes;
	@FindBy(css="#mat-radio-5")
	private WebElement secondQuestionYes;
	@FindBy(css="#thirdQuestion1")
	private WebElement fedex;
	@FindBy(id="confirmButton")
	private WebElement confirmButton;
	@FindBy(css="#cdk-overlay-1 > mat-dialog-container > rx-claim-submit-dialog > form > div.dialog-actions > button.mat-raised-button.mat-tertiary")
	private WebElement cancelButton;
	@FindBy(css="#cdk-overlay-0 > mat-dialog-container > rx-claim-submit-dialog > form > div.dialog-actions > button.mat-raised-button.mat-tertiary")
	private WebElement salvageClaimCancelButton;
	@FindBy(css="#confirmButton > div.mat-button-ripple.mat-ripple")
	private WebElement salvageClaimConfirmButton;
	
	@FindBy(css="#cdk-overlay-1 > mat-dialog-container > rx-claim-submit-dialog > form > div:nth-child(3) > div > p")
	private WebElement firstWarning;
	@FindBy(css="#cdk-overlay-1 > mat-dialog-container > rx-claim-submit-dialog > form > div:nth-child(7) > p")
	private WebElement secondWarning;
	@FindBy(css="#cdk-overlay-1 > mat-dialog-container > rx-claim-submit-dialog > form > div:nth-child(9) > div:nth-child(3) > p")
	private WebElement thirdWarning;
	@FindBy(css="#cdk-overlay-1 > mat-dialog-container > rx-claim-submit-dialog > form > p.dialog-body.margin-top-4x.margin-bottom-4x")
	private WebElement firstQuestion;
	@FindBy(css="#cdk-overlay-1 > mat-dialog-container > rx-claim-submit-dialog > form > p.dialog-body.margin-top-6x.margin-bottom-4x")
	private WebElement secondQuestion;
	@FindBy(css="#cdk-overlay-1 > mat-dialog-container > rx-claim-submit-dialog > form > div:nth-child(8) > p")
	private WebElement thirdQuestion;
	
	@FindBy(css="#cdk-overlay-0 > mat-dialog-container > rx-claim-submit-dialog > form > p.dialog-body.margin-top-4x.margin-bottom-4x")
	private WebElement salvageClaimFirstQuestion;
	@FindBy(css="#cdk-overlay-0 > mat-dialog-container > rx-claim-submit-dialog > form > p.dialog-body.margin-top-6x.margin-bottom-4x")
	private WebElement salvageClaimSecondQuestion;

	@FindBy(xpath="/html/body/rx-app/dashboard-view/div/div/div[2]/rx-returns/rx-returns-claim-details/rx-claim-details/mat-card/div[3]/div[1]")
	private WebElement labelClaimTypeC3;
	
	@FindBy(xpath="/html/body/rx-app/dashboard-view/div/div/div[2]/rx-returns/rx-returns-claim-details/rx-claim-details/mat-card/div[3]/div[2]")
	private WebElement labelScheduleC3;
	
	@FindBy(xpath="/html/body/rx-app/dashboard-view/div/div/div[2]/rx-returns/rx-returns-claim-details/rx-claim-details/mat-card/div[3]/div[3]")
    private WebElement labelSupplierNumberC3;
	
	@FindBy(xpath="/html/body/rx-app/dashboard-view/div/div/div[2]/rx-returns/rx-returns-claim-details/rx-claim-details/mat-card/div[3]/div[4]")
	private WebElement labelSupplierC3;
	
	@FindBy(xpath="/html/body/rx-app/dashboard-view/div/div/div[2]/rx-returns/rx-returns-claim-details/rx-claim-details/mat-card/div[3]/div[5]")
	private WebElement labelCreatedByC3;
	
	@FindBy(xpath="/html/body/rx-app/dashboard-view/div/div/div[2]/rx-returns/rx-returns-claim-details/rx-claim-details/mat-card/div[3]/div[6]")
	private WebElement labelLastEditedByC3;
	
	@FindBy(xpath="/html/body/rx-app/dashboard-view/div/div/div[2]/rx-returns/rx-returns-claim-details/rx-claim-details/mat-card/div[3]/div[7]")
	private WebElement labelReturnReasonC3;

	@FindBy(xpath="/html/body/rx-app/dashboard-view/div/div/div[2]/rx-returns/rx-returns-claim-details/rx-claim-details/mat-card/div[4]/div[2]/span[2]")
	private WebElement labelFindAnItemC3;
	
	@FindBy(id="claim-item-list-item-description-header")
	private WebElement labelItemDescription;

	@FindBy(id="claim-item-list-manufacturer-header")
	private WebElement labelManufacturerHeader;

	@FindBy(id="claim-item-list-item-ormd-header")
	private WebElement labelORMD;

	@FindBy(id="claim-item-list-item-ndc-header")
	private WebElement labelNDC;

	@FindBy(id="claim-item-list-item-upc-header")
	private WebElement labelUPC;

	@FindBy(id="claim-item-list-item-wic-header")
	private WebElement labelWIC;

	@FindBy(id="claim-item-list-item-packSize-header")
	private WebElement labelPackSize;

	@FindBy(id="claim-item-list-item-avalaible-for-return-header")
	private WebElement labelAvalaibleForReturn;

	@FindBy(id="claim-item-list-item-return-quantity-header")
	private WebElement labelReturnQuantityHeader;

	@FindBy(xpath="/html/body/rx-app/dashboard-view/div/div/div[2]/rx-returns/rx-returns-claim-details/rx-claim-search-item/mat-card/form/div[1]")
	private WebElement labelFindAnItem;
	
	@FindBy(xpath="/html/body/rx-app/dashboard-view/div/div/div[2]/rx-returns/rx-returns-claim-details/rx-claim-search-item/mat-card/form/div[1]/button")
	private WebElement searchButtonDetailPage;

	@FindBy(xpath="/html/body/rx-app/dashboard-view/div/div/div[2]/rx-returns/rx-returns-claim-details/rx-claim-details/mat-card/div[1]/div[2]/button")
	private WebElement deleteClaimBtn;
	
	@FindBy(id="page-header-back-button")
	private WebElement backButtonClick;
	
	@FindBy(xpath="/html/body/rx-app/dashboard-view/div/div/div[2]/rx-returns/rx-returns-claim-details/rx-claim-details/mat-card/div[4]/div[2]/span[2]")
	private WebElement xItems;
	
	@FindBy(xpath="/html/body/rx-app/dashboard-view/div/div/div[2]/rx-returns/rx-returns-claim-details/rx-claim-item-list/mat-toolbar/div/mat-toolbar-row/button")
	private WebElement submitClaimButton;
	
	@FindBy(xpath="//*[@id=\"item-search-info-container\"]/div[1]/div[2]/button/span")
	private WebElement addItemButton;
	
	@FindBy(xpath="/html/body/rx-app/dashboard-view/div/div/div[2]/rx-returns/rx-returns-claim-details/rx-claim-details/mat-card/div[1]/div[2]/button")
	private WebElement editClaimButton;
	
	@FindBy(xpath="/html/body/rx-app/dashboard-view/div/div/div[2]/rx-returns/rx-returns-claim-details/rx-claim-details/mat-card/div[2]/div[2]")
	private WebElement labelStatus;
	
	@FindBy(css="#cdk-overlay-0 > mat-dialog-container > rx-claim-submit-dialog > form > div:nth-child(7) > p")
	private WebElement secondQuestionWarningMessage;
	@FindBy(css="#cdk-overlay-0 > mat-dialog-container > rx-claim-submit-dialog > form > div:nth-child(8) > div:nth-child(3) > p")
	private WebElement shippingMethodWarningMessage;
	
	@FindBy(id = "mat-error-20")
	private WebElement maxQuantityMsg;
	
	@FindBy(id = "return-unit-quantity")
	private WebElement returnUnitQuantity;
	
	@FindBy(xpath = "//*[@id=\"cdk-overlay-7\"]/mat-dialog-container")
	private WebElement itemOrmdAlert;
	
	@FindBy(id = "add-to-claim-button")
	private WebElement addToClaimDetailButton;
	
	@FindBy(id = "claim-search-item-input")
	private WebElement itemSearchForm;
	
	@FindBy(id = "rc-table")
	private WebElement itemListTable;
	
	@FindBy(xpath="//*[@id=\"item-search-info-container\"]/div[1]/div[2]/button[1]")
	private WebElement cancelBtn;
	
	@FindBy(xpath="//*[@id=\"item-search-info-container\"]/div[1]/div[2]/button[2]")
	private WebElement saveButton;
	
	@FindBy(xpath="//*[@id=\"claim-list-claim-type-header\"]/div/button")
	private WebElement claimTypeButton;

	@FindBy(id="rc-table-row-0")
	private WebElement firstRowTable;
	
	public ClaimDetailsPage(WebDriver driver) {
		this.driver = driver;
	}

	
	
	public String returnQuantityHeaderLabel() {
		return returnUnitQuantityHeader.getText();
	}
	
	public Boolean firstRowTableDisplayed() {
		return firstRowTable.isDisplayed();
	}
	
	
 
 	
    public void searchButtonDetailPageClicked() {
		 searchButtonDetailPage.click();
	}
 	
    
	public Boolean claimDetailButtonDisplayed() {
		return addToClaimDetailButton.isDisplayed();
	}
	
	public void itemSearchForm(String s) {
	    itemSearchForm.sendKeys(s);
	}
	
	public List<WebElement> tableElementsDisplayed() {
//	ui().waitForCondition(driver, ExpectedConditions.visibilityOf(itemListTable), 30);
    	return itemListTable.findElements(By.cssSelector("mat-row"));
	}
	
	public void selectFirstTableElement() {
		List<WebElement> rows = tableElementsDisplayed();
		for (WebElement row : rows) {
			row.click();
			break;
		}
	}
	
	public Boolean searchButtonDetailPageIsEnabled() {
		return searchButtonDetailPage.isEnabled();
	}
	
    public void addToClaimDetailButtonClicked() {
    	addToClaimDetailButton.click();
	}
    
    
	public Boolean saveButtonDisplayed() {
		return saveButton.isDisplayed();
	}
	
 	public Boolean cancelButtonDisplayed() {
		return cancelBtn.isDisplayed();
	}
 	
	
	public Boolean submitClaimButtonDisplayed() {
		return submitClaimButton.isDisplayed();
	}
    
	
 	public String firstRowTableTxt() {
		return firstRowTable.getText();
	}
	
    public void claimDetailButtonClicked() {
		addToClaimDetailButton.click();
	}	
   
    public void addItemButtonClicked() {
		 addItemButton.click();
	}
    
	
	public String checkMaxQuantityMsg() {
	return maxQuantityMsg.getText();
    }
	
	
	public void insertReturnUnitQuantity(String s) {
		returnUnitQuantity.sendKeys(s);
	}

	public String getItemOrmdMessage() {
	return itemOrmdAlert.getText();
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
	
    public String labelXItems() {
		return xItems.getText();
	}
	
	public String labelStatusDisplayed() {
		return labelStatus.getText();
	}
	
 	public String labelFindAnItem() {
		return labelFindAnItem.getText();
	}
	
	/*
	 * Displayed
	 */

	
	public Boolean shipBoxButtonDisplayed() {
		return shipBoxButton.isDisplayed();
	}
	
	public Boolean itemInClaimsCounterDisplayed() {
		return itemInClaimsCounter.isDisplayed();
	}
	
	public Boolean itemInClaimLabelDisplayed() {
		return itemInClaimLabel.isDisplayed();
	}
	
	public List<Boolean> claimItemsTableElementsDisplayed() {
		List <Boolean> displayed = new ArrayList<>();
		List<WebElement> rows = claimItemsTable.findElements(By.cssSelector("mat-row"));
		for(WebElement row : rows) {
			List<WebElement> cells = row.findElements(By.cssSelector("mat-cell"));
			for(WebElement cell : cells) {
				displayed.add(cell.isDisplayed());
			}
		}
		return displayed;
	}
	
	public Boolean itemsLabelDisplayed() {
		return itemsLabel.isDisplayed();
	}
	
	public Boolean itemsNumberDisplayed() {
		return itemsNumber.isDisplayed();
	}
	
	public Boolean warningIconDisplayed() {
		return warningIcon.isDisplayed();
	}

	public Boolean saveEditButtonDisplayed() {
		return saveEditButton.isDisplayed();
	}

	public Boolean cancelEditButtonDisplayed() {
		return cancelEditButton.isDisplayed();
	}

	public Boolean okButtonDisplayed() {
		return okButton.isDisplayed();
	}

	public Boolean returnUnitQuantityHeaderDisplayed() {
		return returnUnitQuantityHeader.isDisplayed();
	}

	public Boolean returnUnitQuantityInputDisplayed() {
		return returnUnitQuantityInput.isDisplayed();
	}

	public Boolean createdDateDisplayed() {
		return createdDate.isDisplayed();
	}

	public Boolean editShippingDetailsButtonDisplayed() {
		return deleteClaimButton.isDisplayed(); // selector for Edit Shipping
												// Details is the same for
												// delete
	}

	public Boolean deleteClaimButtonDisplayed() {
		return deleteClaimButton.isDisplayed();
	}

	public Boolean claimNumberDisplayed() {
		return claimNumber.isDisplayed();
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

	public Boolean supplierDivDisplayed() {
		return supplierDiv.isDisplayed();
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

	public Boolean itemSearchInputDisplayed() {
		return itemSearchInput.isDisplayed();
	}

	public Boolean searchButtonDisplayed() {
		return searchButton.isDisplayed();
	}

	public Boolean clearButtonDisplayed() {
		return clearButton.isDisplayed();
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
	
	
    public Boolean searchButtonDetailPageIsDispayed() {
		return searchButtonDetailPage.isDisplayed();
	}

	public Boolean deleteClaimButtonIsDisplayed() {
		return deleteClaimBtn.isDisplayed();
	}
	
	public Boolean editClaimButtonDisplayed() {
		return editClaimButton.isDisplayed();
	}	
	
	
    public Boolean addItemButtonDisplayed() {
		return addItemButton.isDisplayed();
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
	 * Enabled
	 */
	public String salvageClaimCancelButtonEnabled() {
		return salvageClaimCancelButton.findElement(By.cssSelector("div.mat-button-ripple.mat-ripple")).getAttribute("ng-reflect-disabled");
	}
	
	public String salvageClaimConfirmButtonEnabled() {
		return salvageClaimConfirmButton.getAttribute("ng-reflect-disabled");
	}
	
	public Boolean cancelButtonEnabled() {
		return cancelButton.isEnabled();
	}
	
	public Boolean shipBoxButtonEnabled() throws InterruptedException {
		synchronized (this) {
			wait(6000);
		}
		return shipBoxButton.isEnabled();
	}
	
	public Boolean confirmButtonEnabled() {
		return confirmButton.isEnabled();
	}

	/*
	 * Click
	 */
	public void shippingMethodRadioButtonClick() {
		shippingMethodRadioButton.click();
	}
	
	public void salvageClaimCancelButtonClick() {
		salvageClaimCancelButton.click();
	}
	
	public void salvageClaimConfirmButtonClick() {
		salvageClaimConfirmButton.click();
	}
	
	public void cancelButtonClick() {
		cancelButton.click();
	}
	
	public void firstQuestionNoClick() {
		firstQuestionNo.click();
	}
	
	public void secondQuestionNoClick() {
		secondQuestionNo.click();
	}
	
	public void secondQuestionYesClick()
	{
		secondQuestionYes.click();
	}
	
	public void firstQuestionYesClick() {
		firstQuestionYes.click();
	}
	
	public void fedexClick() {
		fedex.click();
	}
	
	public void shipBoxButtonClick() {
		shipBoxButton.click();
	}
	
	public void chooseformReceived(String choosen) {
		formReceivedOverlay.click();
		List<WebElement> methods = formReceivedMatOption.findElements(By.cssSelector("div > div > mat-option"));
		for (WebElement method : methods) {
			if (method.findElement(By.cssSelector("span")).getText().equals(choosen)) {
				method.click();
				break;
			}
		}
	}

	public void chooseShippingMethod(String choosen) {
		shippingMethodOverlay.click();
		List<WebElement> methods = shippingMethodMatOption.findElements(By.cssSelector("div > div > mat-option"));
		for (WebElement method : methods) {
			if (method.findElement(By.cssSelector("span")).getText().equals(choosen)) {
				method.click();
				break;
			}
		}
	}

	public void clickSaveEditingClaimButton() {
		saveEditingClaimButton.click();
	}

	public void clickEditShippingDetailsButton() {
		deleteClaimButton.click();
	}

	public void clickBackButton() {
		backButton.click();
	}

	public void clickOkButton() {
		okButton.click();
	}

	public void selectDeleteReasonOverlay() {
		List<WebElement> deleteReasons = deleteReasonOverlay.findElements(By.cssSelector("div > div > mat-option"));
		for (WebElement reason : deleteReasons) {
			reason.click();
			break;
		}
	}

	public void clickDeletionReasonMatSelect() {
		deletionReasonMatSelect.click();
	}

	public void clickDeleteClaimButton() {
		deleteClaimButton.click();
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
	}

	public void printMaterialsButtonClick() {
		printMaterialsButton.click();
	}

	/*
	 * Text handle
	 */
	public String getShippingMethodWarningMessage() {
		return shippingMethodWarningMessage.getText();
	}
	
	public String getSecondQuestionWarningMessage() {
		return secondQuestionWarningMessage.getText();
	}
	
	public String getSalvageClaimFirstQuestion() {
		return salvageClaimFirstQuestion.getText();
	}
	
	public String getSalvageClaimSecondQuestion() {
		return salvageClaimSecondQuestion.getText();
	}
	
	public String getFirstQuestion() {
		return firstQuestion.getText();
	}
	
	public String getSecondQuestion() {
		return secondQuestion.getText();
	}
	
	public String getThirdQuestion() {
		return thirdQuestion.getText();
	}
	
	public String getFirstWarning() {
		return firstWarning.getText();
	}
	
	public String getSecondWarning() {
		return secondWarning.getText();
	}
	
	public String getThirdWarning() {
		return thirdWarning.getText();
	}
	
	public String getDeniedLabel() {
		return deniedLabel.getText();
	}
	
	public String getApprovedLabel() {
		return approvedLabel.getText();
	}
	
	public String getWrongTrackingNumberMessage() throws InterruptedException {
		synchronized (this) {
			wait(6000);
		}
		return wrongTrackingNumberMessage.getText();
	}

	public String getScanBoxLabelMessage() {
		return scanBoxLabel.getText();
	}

	public String getShipBoxWarningMessage() {
		return shipBoxWarningMessage.getText();
	}

	public String getCreatedDate() {
		return createdDate.getText();
	}

	public String getItemNumber() {
		return itemsNumber.getText();
	}

	public String getTrackingNumberInput() {
		return trackingNumberInput.getText();
	}

	public String getValueTrackingNumberInput() throws InterruptedException {
		synchronized (this) {
			wait(5000);
		}
		return trackingNumberInput.getAttribute("value");
	}


	public String labelClaimTypeC3() {
		return labelClaimTypeC3.getText();
	}
	
	public String labelScheduleC3() {
		return labelScheduleC3.getText();
	}
 	
	public String labelSupplierC3() {
		return labelSupplierC3.getText();
	}
 
 	public String labelSupplierNumberC3() {
		return labelSupplierNumberC3.getText();
	}
 
	public String labelCreatedByC3() {
		return labelCreatedByC3.getText();
	}
 
	public String labelLastEditedByC3() {
		return labelLastEditedByC3.getText();
	}
 
	public String labelReturnReasonC3() {
		return labelReturnReasonC3.getText();
	}
 
	public String labelItemDescription() {
		return labelItemDescription.getText();
	}
	
	public String labelManufacturerHeader() {
		return labelManufacturerHeader.getText();
	}
	public String labelORMD() {
		return labelORMD.getText();
	}
	public String labelNDC() {
		return labelNDC.getText();
	}
	public String labelUPC() {
		return labelUPC.getText();
	}
	public String labelWIC() {
		return labelWIC.getText();
	}
	public String labelPackSize() {
		return labelPackSize.getText();
	}
	public String labelAvalaibleForReturn() {
		return labelAvalaibleForReturn.getText();
	}
	public String labelReturnQuantityHeader() {
		return labelReturnQuantityHeader.getText();
	}
	
	
	
	
	public void insertTrackingNumberInput(String trackingNumber) {
		trackingNumberInput.clear();
		trackingNumberInput.sendKeys(trackingNumber);
	}

	public void deleteTrackingNumberByKey() {
		String trackingNumber = trackingNumberInput.getAttribute("value");
		for (int i = 0; i < trackingNumber.length(); i++) {
			trackingNumberInput.sendKeys(Keys.BACK_SPACE);
		}
	}

	/*
	 * Error
	 */
	public String missingTrackingNumberWarningMessage() {
		return missingTrackingNumberWarningMessage.getText();
	}
	
	public String confirmFormMessage() {
		return confirmFormMessage.getText();
	}

	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(claimTypeDiv)) {
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
