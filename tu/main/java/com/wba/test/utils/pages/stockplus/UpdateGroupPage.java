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
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

public class UpdateGroupPage extends LoadableComponent<UpdateGroupPage>{
	
	@FindBy(css = "#save-button")
	private WebElement saveButton;
	@FindBy(css = "#group-store-table > div.table-header > mat-checkbox > label > div")
	private WebElement checkBoxSelectAll;
	@FindBy(css ="#remove-store-button > span > mat-icon")
	private WebElement buttonRemoveStore;
	@FindBy(id = "group-stores-count")
	private WebElement textCountStores;
	@FindBy(id = "cancel-button")
	private WebElement buttonCancel;
	@FindBy(id = "dialog-confirm-button")
	private WebElement buttonYes;
	@FindBy(id = "dialog-cancel-button")
	private WebElement buttonNo;
	@FindBy(id="available-stores-refine-value")
	private WebElement inputStoreSearch;
	@FindBy(id="available-stores-refine-button")
	private WebElement buttonRefineSearch;
	@FindBy(css="#available-store-table > div.scrollable-container.bottom-store-card > div > div:nth-child(1) > div > mat-pseudo-checkbox")
	private WebElement checkStore;
	@FindBy(css="#add-store-button > span > mat-icon")
	private WebElement addStoreButton;
	@FindBy(css="#group-store-table > div.scrollable-container.bottom-store-card > div")
	private WebElement tableGroupStoreList;
	@FindBy(css="#group-store-table > div.scrollable-container.bottom-store-card > div > div.row-container.first-removed-row > div >")
	private WebElement tableRemovedGroupStoreList;
	@FindBy(css="#group-store-table > div.scrollable-container.bottom-store-card > div > div.row-container.first-removed-row")
	private WebElement removedLabelGroupStoreList;
	@FindBy(xpath="//input[contains(@id,'store-group-start-date')][@placeholder='Start Date']")
	private WebElement startDate;
	@FindBy(id = "store-group-name")
	private WebElement fieldName;
	@FindBy(id = "store-group-description")
	private WebElement fieldDescription;
	@FindBy(id = "available-stores-refine-criteria")
	private WebElement storeRefine;
	@FindBy(id = "available-stores-count")
	private WebElement storeFounds;
	@FindBy(id = "group-stores-title")
	private WebElement groupStoreTitle;
	@FindBy(css="#dialog-container > div.mat-dialog-title.margin-bottom-2x")
	private WebElement confirmExitPopupTitle;
	@FindBy(css="#dialog-container > div.mat-dialog-content.mat-body-1.margin-bottom-3x > div")
	private WebElement confirmExitPopupMessage;
	@FindBy(css="rx-confirm-dialog>div[id='dialog-container']>div>div")
	private WebElement popupMessage;
	@FindBy(css="#available-store-table > div.table-header > mat-checkbox > label > div")
	private WebElement checkBoxSelectAllAvailableStores;

	private String availableStoreTableRowsFind = "//*[@id='available-store-table']/div[2]/div/div";
	
	private WebDriver driver;
	private String numberOfStores;
	
    public UpdateGroupPage(WebDriver driver) {
        this.driver = driver;
    }
	
	public void selectAllStores() {
		checkBoxSelectAll.click();
	}
	
	public void selectAllAvailableStores() {
		checkBoxSelectAllAvailableStores.click();
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
    
    public void insertMoreStore(String storeNumber) {
    	int sizeTable=availableStoreTableSize();
        for (int i=1 ; i<=sizeTable;i++) 
        {
        	WebElement checkBox = driver.findElement(By.xpath(availableStoreTableRowsFind+"[" + i + "]/div/mat-pseudo-checkbox"));
        	WebElement store = driver.findElement(By.xpath(availableStoreTableRowsFind+"[" + i + "]/div/p[1]"));
        	if(store.getText().equals(storeNumber)) {
        		checkBox.click();
        		break;
        	}
        }
    }
    
    public void addStoreClick() {
		addStoreButton.click();
    }
    
    public GroupManagementPage cancelOperation() {
    	buttonCancel.click();
    	buttonYes.click();
    	return new GroupManagementPage(driver);
    }
    
    public String exportCountStores() throws InterruptedException {
    	numberOfStores=textCountStores.getText();
    	return numberOfStores;
    }
    
    public String exportAvailableStores() throws InterruptedException {
    	return numberOfStores=storeFounds.getText();
    }
    
    public void clickSelectAll() {
    	checkBoxSelectAll.click();
    }
    
    public void deselectStoreFromList(String storeNumber) {
    	List<WebElement> storeList=tableGroupStoreList.findElements(By.tagName("div"));
    	for (WebElement store : storeList) {
			if (store.findElement(By.cssSelector("div > p:nth-child(2)")).getText().equals(storeNumber)) {
				store.findElement(By.cssSelector("div > mat-pseudo-checkbox")).click();
			}
		}
    }
    
    public void selectStoreFromList(String storeNumber) {
    	List<WebElement> storeList=tableGroupStoreList.findElements(By.tagName("div"));
    	ui().waitForCondition(driver, ExpectedConditions.visibilityOfAllElements(storeList), 20);
    	for (WebElement store : storeList) {
			if (store.findElement(By.cssSelector("div > p:nth-child(2)")).getText().equals(storeNumber)) {
				store.findElement(By.cssSelector("div > mat-pseudo-checkbox")).click();
				if (store.findElement(By.cssSelector("div > mat-pseudo-checkbox")).getAttribute("ng-reflect-state").equals("unchecked")) {
					store.findElement(By.cssSelector("div > mat-pseudo-checkbox")).click();
				}
			}
		}
    }
    
    public Boolean selectAllIsChecked() {
    	WebElement parent = (WebElement) ((JavascriptExecutor) driver).executeScript(
                "return arguments[0].parentNode.parentNode;", checkBoxSelectAll);
    	if (parent.getAttribute("class").contains("mat-checkbox-checked")) {
        	return true;
    	}
    	return false;
    }
    
    public String getGroupStoreTitle() {
    	return groupStoreTitle.getText();
    }
    
    public String getGroupStoreCount() {
    	return textCountStores.getText();
    }
    
    /**
     * @author mcasaburi
     * @return
     */
    public String getConfirmExitPopupTitle() {
    	return this.confirmExitPopupTitle.getText();
    }
    
    /**
     * @author mcasaburi
     * @return
     */
    public String getConfirmExitPopupMessage() {
    	return this.confirmExitPopupMessage.getText();
    }
    
    /**
     * @author mi
     * @return true True if the startDate is enabled, false otherwise.
     */
    public Boolean isEnableStartDate()
    {
		ui().waitForCondition(driver,ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[contains(@id,'store-group-start-date')][@placeholder='Start Date']")),10);
    	return startDate.isEnabled();
    }

    /**
     * @author mi
     * @return true True if the startDate is enabled, false otherwise.
     */
    public Boolean isEnableName()
    {
    	return fieldName.isEnabled();
    }
    
    /**
     * @author mi
     * @return true True if the startDate is enabled, false otherwise.
     */
    public Boolean isEnableDescription()
    {
    	return fieldDescription.isEnabled();
    }
   
    public void setDescriptionTextContent(String description) {
    	fieldDescription.clear();
    	fieldDescription.sendKeys(description);
    }
    /**
     * @author mi
     * @return the text content.
     */
    public String getNameTextContent()
    {
    	return fieldName.getText();
    }
    
    public void setNameTextContent(String name) {
    	fieldName.clear();
    	fieldName.sendKeys(name);
    	fieldName.sendKeys(Keys.ENTER);
    }
    
    public void removeNameTextContent() {
    	int length = fieldName.getAttribute("ng-reflect-model").length();
    	for (int i = 0; i < length; i++) {
        	fieldName.sendKeys(Keys.BACK_SPACE);
		}
    }
    
    public void leaveNameTextContent() {
    	fieldName.sendKeys(Keys.TAB);
    }
    
    public void saveInformation() {
    	saveButton.click();
    }
    
    /**
     * @author mi
     * @return the text content.
     */
    public String getDescriptionTextContent()
    {
    	return fieldDescription.getText();
    }
    
    /**
     * @author mi
     * @return the text content.
     */
    public String getStartDateTextContent()
    {
 
    	String strDate;
    	strDate = startDate.getAttribute("ng-reflect-model");
    	return strDate;
    }
    
    /**
     * @author mi
     * @return the text content.
     */
    public String getStoreRefineTextContent()
    {
    	return storeRefine.getText();
    }
    
    /**
     * @author mi
     * @return true True if the isSelectedFieldName is selected, false otherwise.
     */
    public Boolean isSelectedName()
    {
    	return fieldName.equals(driver.switchTo().activeElement());
    	
    }
    
    /**
     * @author mi
     * @return number rows from available store table.
    */
    public int availableStoreTableSize() {
        return driver.findElements(By.xpath(availableStoreTableRowsFind)).size();
    }
    
    public int groupStoreListSize() {
    	String cssSelectorGroupStoreList = "#group-store-table > div.scrollable-container.bottom-store-card > div > div";
    	return driver.findElements(By.cssSelector(cssSelectorGroupStoreList)).size();
    }
    /**
     * @author mi
     * @return List<Map<String,String>>: rows for available store table content ( Keys: check,store,storelocation,storetype)
     */
    public List<Map<String,String>> getAvailableStoreTableRowsContent() {
        
    	int sizeTable=availableStoreTableSize();
    	
    	List<Map<String,String>> retList = new ArrayList<Map<String,String>>();
    	
        List<WebElement> findRows = driver.findElements(By.xpath(availableStoreTableRowsFind));
       
        for (int i=1 ; i<=sizeTable;i++) 
        {
        	//Example:
        	//*[@id="available-store-table"]/div[2]/div/div[1]/div/mat-pseudo-checkbox
        	//*[@id='available-store-table']/div[2]/div/div (base list rows)
        	//*[@id="available-store-table"]/div[2]/div/div[2]/div/p[1] (first row first column)
        	
        	String checBoxExist = (existsElement(availableStoreTableRowsFind+"[" + i + "]/div/mat-pseudo-checkbox"))? "yes":"";
        	//WebElement checkBox  = driver.findElement(By.xpath(availableStoreTableRowsFind+"[" + i + "]/div/mat-pseudo-checkbox"));
        	WebElement store = driver.findElement(By.xpath(availableStoreTableRowsFind+"[" + i + "]/div/p[1]"));
        	WebElement storeLocation = driver.findElement(By.xpath(availableStoreTableRowsFind+"[" + i + "]/div/p[2]"));
        	WebElement storeType = driver.findElement(By.xpath(availableStoreTableRowsFind+"[" + i + "]/div/p[3]"));
        	
        	Map<String,String> map = new HashMap<>();
        	map.put("store", store.getText());
        	map.put("location", storeLocation.getText());
        	map.put("storetype", storeType.getText());
        	map.put("check", checBoxExist);
        	retList.add(map);
        }
        return retList;
    }
    
    public Boolean isPresentIntoAvailableStores(String storeName) {
    	Boolean found = Boolean.FALSE;
    	int sizeTable=availableStoreTableSize();
        
        for (int i=1 ; i<=sizeTable;i++) 
        {
        	WebElement store = driver.findElement(By.xpath(availableStoreTableRowsFind+"[" + i + "]/div/p[1]"));
        	if(store.getText().equals(storeName)) {
        		found = Boolean.TRUE;
        		break;
        	}
        }

    	return found;
    }
    
    
    public Boolean isRemovedFromGroupStoreList(String storeName) {
    	Boolean found = Boolean.FALSE;
    	int sizeTable=groupStoreListSize();
        String cssSelectorStoreRemoved = "#group-store-table > div.scrollable-container.bottom-store-card > div > div.row-container.first-removed-row > div > p:nth-child(2)";
        for (int i=1 ; i<=sizeTable;i++) 
        {
        	WebElement store = driver.findElement(By.cssSelector(cssSelectorStoreRemoved));
        	if(store.getText().equals(storeName)) {
        		found = Boolean.TRUE;
        		break;
        	}
        }

    	return found;
    }

    public String getErrorMessage() {
		ui().waitForCondition(driver,ExpectedConditions.visibilityOf(popupMessage), 20);
    	return popupMessage.getText();
    }
    
    public String getAlertMessage() {
    	WebElement alertMessage = driver.findElement(By.cssSelector("#dialog-container > div.mat-dialog-title.margin-bottom-2x"));
    	ui().waitForCondition(driver,ExpectedConditions.visibilityOf(alertMessage), 20);
    	return alertMessage.getText();
    }
    
    public String getDuplicateGroupMessage() {
    	WebElement messageError = driver.findElement(By.cssSelector("mat-error>div[ngxerror = 'duplicateGroup']"));
		ui().waitForCondition(driver,ExpectedConditions.visibilityOf(messageError), 20);
    	return messageError.getText();
    }

    public void setStartDate(String startDate)
    {
		ui().waitForCondition(driver,ExpectedConditions.visibilityOf(this.startDate), 20);
    	this.startDate.clear();
    	this.startDate.sendKeys(startDate);
    }
    
    public void confirmPopupOperation() {
		ui().waitForCondition(driver,ExpectedConditions.visibilityOf(this.buttonYes), 20);
		buttonYes.click();
    }
    
    public void notConfirmPopupOperation() {
		ui().waitForCondition(driver,ExpectedConditions.visibilityOf(this.buttonNo), 20);
		buttonNo.click();
    }

    public Boolean verifyIfStorePresentIntoGroupStoreList(String storeNumber) throws InterruptedException {
    	
    	Thread.sleep(500);
    	Boolean found = Boolean.FALSE;
    	List<WebElement> storeList=tableGroupStoreList.findElements(By.tagName("div"));
    	ui().waitForCondition(driver, ExpectedConditions.visibilityOfAllElements(storeList), 20);
    	for (WebElement store : storeList) {
    		if (store.findElement(By.cssSelector("div > p:nth-child(2)")).getText().equals(storeNumber)) {
        		found = Boolean.TRUE;
        		break;
    		}
    	}
    	return found;
    }
    
    public Boolean verifyIfStorePresentIntoAvailable(String storeNumber) {
    	Boolean found = Boolean.FALSE;
    	int sizeTable=availableStoreTableSize();
        for (int i=1 ; i<=sizeTable;i++) 
        {
        	WebElement store = driver.findElement(By.xpath(availableStoreTableRowsFind+"[" + i + "]/div/p[1]"));
    		ui().waitForCondition(driver,ExpectedConditions.visibilityOf(store), 20);
        	if(store.getText().equals(storeNumber)) {
        		found = Boolean.TRUE;
        		break;
        	}
        }

    	return found;
    	
    }
    
    public Boolean verifyIfAllAvailableStoresAreChecked() {
    	Boolean checked = Boolean.TRUE;
    	int sizeTable=availableStoreTableSize();
    	String cssSelectorRow = "#available-store-table > div.scrollable-container.bottom-store-card > div > div:nth-child";
        for (int i=1 ; i<=sizeTable;i++) 
        {
        	WebElement checkStore = driver.findElement(By.cssSelector(cssSelectorRow+"(" + i + ") > div > mat-pseudo-checkbox"));
        	ui().waitForCondition(driver,ExpectedConditions.visibilityOf(checkStore), 20);
        	if (!checkStore.getAttribute("ng-reflect-state").equals("checked")) {
        		checked = Boolean.FALSE;
        	}
        }
    	return checked;
    }
    public Boolean verifyIfCheckedIntoAvailable(String storeNumber) {
    	
    	Boolean checked = Boolean.FALSE;
    	int sizeTable=availableStoreTableSize();
    	String cssSelectorRow = "#available-store-table > div.scrollable-container.bottom-store-card > div > div:nth-child";
        for (int i=1 ; i<=sizeTable;i++) 
        {
        	WebElement store = driver.findElement(By.cssSelector(cssSelectorRow+"(" + i + ") > div > p:nth-child(2)"));
    		ui().waitForCondition(driver,ExpectedConditions.visibilityOf(store), 20);
        	if(store.getText().equals(storeNumber)) {
            	WebElement checkStore = driver.findElement(By.cssSelector(cssSelectorRow+"(" + i + ") > div > mat-pseudo-checkbox"));
            	if (checkStore.getAttribute("ng-reflect-state").equals("checked")) {
            		checked = Boolean.TRUE;
	        		break;
            	}
        	}
        }
    	return checked;
    }
    
    /**
     * @author mi
     * @param xpath 
     * @return true if web element exist 
     */
    private boolean existsElement(String xpath) {

		try {
			ui().waitForCondition(driver,ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)),10);
	        driver.findElement(By.xpath(xpath));   
		}
	     catch (Throwable e) 
		{
	        return false;
	    } 
	    return true;
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
