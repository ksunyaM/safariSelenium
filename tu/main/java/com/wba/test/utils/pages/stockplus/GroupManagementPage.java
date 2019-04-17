/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.stockplus;

import static com.oneleo.test.automation.core.UIUtils.ui;

import java.util.List;
import java.util.Objects;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

public class GroupManagementPage extends LoadableComponent<GroupManagementPage>{

	@FindBy(css="#sg-gm-add-group-button")
	private WebElement addButton;
	@FindBy(xpath="//*[@id='mat-tab-content-1-0']/div/app-group-view/rx-group-search-results/div/mat-table/mat-row/mat-cell[2]")
	private WebElement groupFound;
	@FindBy(css="div[class='fixed-header-container']>mat-table>mat-row[class='mat-row selected-focused-row-style']>mat-cell[class='right-align-cell mat-cell cdk-column-numberOfStores mat-column-numberOfStores']")
	private WebElement numberOfStoreFound;
	@FindBy(css="#sg-gm-search-group")
	private WebElement textboxGroupNameSearch;
	@FindBy(css="#sg-gm-refine-group-button")
	private WebElement refineButton;
	@FindBy(id="sg-gm-update-group-button")
	private WebElement buttonUpdateGroup;
	@FindBy(id="sg-gm-delete-group-button")
	private WebElement buttonDelete;
	@FindBy(id="dialog-confirm-button")
	private WebElement buttonYes;
	@FindBy(id="page-header-back-button")
	private WebElement backToDashBoard;
	@FindBy(css="rx-group-search-results>div>mat-table>mat-row[class='mat-row selected-row-style']>mat-cell[class='mat-cell cdk-column-name mat-column-name']")
	private WebElement groupNameSelected;
	@FindBy(css="rx-group-search-results>div>mat-table>mat-row[class='mat-row selected-row-style']>mat-cell[class='mat-cell cdk-column-description mat-column-description']")
	private WebElement groupDescriptionSelected;
	@FindBy(id="dialog-cancel-button")
	private WebElement buttonNo; 
	@FindBy(xpath="//*[@id='dialog-container']/div[1]/div")
	private WebElement confirmDeleteMessage;
	@FindBy(xpath="//*[@id='mat-tab-content-1-0']/div/app-group-view/rx-group-search-results/div/mat-table/mat-row")
	private WebElement firstGroupNameByList;
	@FindBy(css="div[class='fixed-header-container']>mat-table>mat-row[class='mat-row selected-row-style']")
	private WebElement firstGroupNameListSelected;
	@FindBy(id = "mat-tab-label-0-1")
	private WebElement settingStoreGroupsStoreTab;
	
	@FindBy(id = "store-number")
	private WebElement storeNumber;
	
	@FindBy(id = "btn-store-search")
	private WebElement btnStoreSearch;
	
	@FindBy(id = "store-info-search-icon")
	private WebElement storeInfoSearchIcon;
	
	
	private WebDriver driver;

    public GroupManagementPage(WebDriver driver) {
        this.driver = driver;
    }
    
    public AddGroupPage navigateToAddPage() {
    	addButton.click();
    	return new AddGroupPage(driver);
    }
    
    public Boolean isDisplayedAddButton() {
    	return addButton.isDisplayed();
    }
    
    public String checkGroupFound() {
    	return groupFound.getText();
    }
    
    public String checkNumberOfStoreFound() {
    	ui().waitForCondition(driver,ExpectedConditions.visibilityOf(numberOfStoreFound),10);
    	return numberOfStoreFound.getText();
    }
    
    public void insertGroupName(String groupName) {
    ui().waitForCondition(driver,ExpectedConditions.visibilityOf(textboxGroupNameSearch),20);
    	textboxGroupNameSearch.sendKeys(groupName);
    }
    
    public void refineSearch() {
    	refineButton.click();
    }
    
	public void searchGroup(String groupName) {
		insertGroupName(groupName);
		refineSearch();
	}
	
	public void searchStore(String storeNumb) {
		storeNumber.sendKeys(storeNumb);
	}
	
	public void StoreSearch() {
		btnStoreSearch.click();
    }	
	
	public Boolean isStoreInfoSearchIcon() {
		return storeInfoSearchIcon.isDisplayed();
	}
	
	public void deleteGroup() {
		buttonDelete.click();
		ui().waitForCondition(driver,ExpectedConditions.elementToBeClickable(buttonYes),20);
		buttonYes.click();
	}
 	
	public void deleteGroupWithoutConfirm() {
    	ui().waitForCondition(driver,ExpectedConditions.elementToBeClickable(buttonDelete),20);
		buttonDelete.click();
	}
    
	public void notConfirmDelete() {
    	ui().waitForCondition(driver,ExpectedConditions.elementToBeClickable(buttonNo),20);
		buttonNo.click();
	}
    
	public void confirmDelete() {
    	ui().waitForCondition(driver,ExpectedConditions.elementToBeClickable(buttonYes),20);
    	buttonYes.click();
	}
	
	public Boolean notExistDeleteButton() {
		List<WebElement> buttonList = driver.findElements(By.id("sg-gm-delete-group-button"));
		return buttonList.isEmpty();
	}
    
    
    public UpdateGroupPage updateGroup() {
    	buttonUpdateGroup.click();
		return new UpdateGroupPage(driver);
	}
	
	public void selectFirstGroupName() {
    	ui().waitForCondition(driver,ExpectedConditions.elementToBeClickable(firstGroupNameListSelected),20);
    	firstGroupNameListSelected.click();
    }
    
	public void selectSettingStoreGroupsStoreTab() {
    	ui().waitForCondition(driver,ExpectedConditions.elementToBeClickable(settingStoreGroupsStoreTab),20);
    	settingStoreGroupsStoreTab.click();
    }
    
	
    public String getConfirmDeleteMessage() {
    	return confirmDeleteMessage.getText();
    }
    
    public DashboardPage backToDashBoard()
    {
    	backToDashBoard.click();
    	return new DashboardPage(driver);
    }
    
    public String getNameGroup() {
    	ui().waitForCondition(driver,ExpectedConditions.visibilityOf(groupNameSelected),20);
    	return groupNameSelected.getText();
    }
    
    public String getDescriptionGroup() {
    	ui().waitForCondition(driver,ExpectedConditions.visibilityOf(groupDescriptionSelected),20);
    	return groupDescriptionSelected.getText();
    }
    
	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(addButton)) {
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
