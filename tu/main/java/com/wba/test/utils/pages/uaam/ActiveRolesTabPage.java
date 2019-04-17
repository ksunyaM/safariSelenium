/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.uaam;

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

public class ActiveRolesTabPage extends LoadableComponent<ActiveRolesTabPage>  {
	
	public static final String tablexpath = "//*[@id=\"mat-tab-content-0-0\"]/div/app-active-roles/div/mat-table";
	public static final String rowxpath = "//*[@id=\"mat-tab-content-0-0\"]/div/app-active-roles/div/mat-table/mat-row";
	public static final String rowparametrizedxpath = "//*[@id=\"mat-tab-content-0-0\"]/div/app-active-roles/div/mat-table/mat-row[%d]/mat-cell";
	public static final String rolenamexpath = "//*[@id=\"mat-tab-content-0-0\"]/div/app-active-roles/div/mat-table/mat-row[%d]/mat-cell[1]";
	public static final String statusxpath = "//*[@id=\"mat-tab-content-0-0\"]/div/app-active-roles/div/mat-table/mat-row[%d]/mat-cell[6]";
	public static final String editiconxpath = "//*[@id=\"mat-tab-content-0-0\"]/div/app-active-roles/div/mat-table/mat-row[%d]/mat-cell[7]/button[1]";
	public static final String deleteiconxpath = "//*[@id=\"mat-tab-content-0-0\"]/div/app-active-roles/div/mat-table/mat-row[%d]/mat-cell[7]/button[2]";
	public static final String cloneiconxpath = "//*[@id=\"mat-tab-content-0-0\"]/div/app-active-roles/div/mat-table/mat-row[%d]/mat-cell[7]/button[3]";
	
	@FindBy(xpath = "//*[@id=\"mat-tab-label-0-0\"]")
	private WebElement activeroleslisttab;
	@FindBy(xpath = "//*[@id=\"mat-tab-label-0-1\"]")
	private WebElement rolesbeingaddedlisttab;
	@FindBy(xpath = "//*[@id=\"mat-tab-label-0-2\"]")
	private WebElement rolesbeingupdatedlisttab; 
	@FindBy(xpath = "//*[@id=\"mat-tab-label-0-3\"]")
	private WebElement rolesbeingdeletedlisttab; 
	
	private WebDriver driver;

	public ActiveRolesTabPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public boolean waitForTabToLoad() {	
		synchronized (this) {
			try {
				wait(5000);
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}
		}		
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(activeroleslisttab));
		try {
			activeroleslisttab.isDisplayed();
			return true;
		}
		catch (NoSuchElementException e){			
			throw new RuntimeException(String.format("The element: %s does not exists", activeroleslisttab.getText()));			
		}
	}	
	
	public ActiveRolesTabPage clickOnActiveRolesListTab() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(activeroleslisttab));
		activeroleslisttab.click();
		return new ActiveRolesTabPage(driver);
	}

	public RolesBeingAddedTabPage clickOnRolesBeingAddedListTab() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(rolesbeingaddedlisttab));
		rolesbeingaddedlisttab.click();
		return new RolesBeingAddedTabPage(driver);
	}
	
	public RolesBeingDeletedTabPage clickOnRolesBeingUpdatedListTab() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(rolesbeingdeletedlisttab));
		rolesbeingdeletedlisttab.click();
		return new RolesBeingDeletedTabPage(driver);
	}
	
	public RolesBeingDeletedTabPage clickOnRolesBeingDeletedListTab() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(rolesbeingdeletedlisttab));
		rolesbeingdeletedlisttab.click();
		return new RolesBeingDeletedTabPage(driver);
	}
	
	public boolean isARoleNameDisplayedInListOfRolesTable(String rolename) {
		WebElement table = driver.findElement(By.xpath(tablexpath));	
		List<WebElement> rows = table.findElements(By.xpath(rowxpath));
        int index = 1;
		for (WebElement row : rows) {	
			List<WebElement> cells = row.findElements(By.xpath(String.format(rowparametrizedxpath, index)));		
			if ((cells.get(0).getText()).equals(rolename)) {
				return true;	
			}
			else {
			++index;
			continue;
			}
		}
		return false;
	}
	
	public void clickOnARoleNameFoundByTextInListOfRolesTable(String rolename) {
		WebElement table = driver.findElement(By.xpath(tablexpath));	
		List<WebElement> rows = table.findElements(By.xpath(rowxpath));
        int index = 1;
		for (WebElement row : rows) {	
			List<WebElement> cells = row.findElements(By.xpath(String.format(rowparametrizedxpath, index)));		
			if ((cells.get(0).getText()).equals(rolename)) {
				cells.get(0).click();
				break;
			}
			else {
			++index;
			continue;
			}
		}	
	}

	public boolean isEditIconDisplayedForAGivenRolename(String rolename) {
		WebElement table = driver.findElement(By.xpath(tablexpath));	
		List<WebElement> rows = table.findElements(By.xpath(rowxpath));
        int index = 1;
		for (WebElement row : rows) {	
			List<WebElement> cells = row.findElements(By.xpath(String.format(rowparametrizedxpath, index)));		
			if ((cells.get(0).getText()).equals(rolename)) {
				cells.get(6).findElement(By.xpath(String.format(rowparametrizedxpath, index) + "/button[1]"));
				return true;	
			}
			else {
			++index;
			continue;
			}
		}
		return false;
	}
	
	public EditActiveRolePage clickOnEditIconForAGivenRolename(String rolename) {
		WebElement table = driver.findElement(By.xpath(tablexpath));	
		List<WebElement> rows = table.findElements(By.xpath(rowxpath));
        int index = 1;
		for (WebElement row : rows) {	
			List<WebElement> cells = row.findElements(By.xpath(String.format(rowparametrizedxpath, index)));		
			if ((cells.get(0).getText()).equals(rolename)) {
				cells.get(6).findElement(By.xpath(String.format(rowparametrizedxpath, index) + "/button[1]")).click();
				break;
			}
			else {
			++index;
			continue;
			}
		}
		return new EditActiveRolePage(driver);
	}

	public boolean isDeleteIconDisplayedForAGivenRolename(String rolename) {
		WebElement table = driver.findElement(By.xpath(tablexpath));	
		List<WebElement> rows = table.findElements(By.xpath(rowxpath));
        int index = 1;
		for (WebElement row : rows) {	
			List<WebElement> cells = row.findElements(By.xpath(String.format(rowparametrizedxpath, index)));		
			if ((cells.get(0).getText()).equals(rolename)) {
				cells.get(6).findElement(By.xpath(String.format(rowparametrizedxpath, index) + "/button[2]"));
				return true;	
			}
			else {
			++index;
			continue;
			}
		}
		return false;
	}
	
	public DeleteActiveRolePopUpPage clickOnDeleteIconForAGivenRolename(String rolename) {
		WebElement table = driver.findElement(By.xpath(tablexpath));	
		List<WebElement> rows = table.findElements(By.xpath(rowxpath));
        int index = 1;
		for (WebElement row : rows) {	
			List<WebElement> cells = row.findElements(By.xpath(String.format(rowparametrizedxpath, index)));		
			if ((cells.get(0).getText()).equals(rolename)) {
				cells.get(6).findElement(By.xpath(String.format(rowparametrizedxpath, index) + "/button[2]")).click();
				break;
			}
			else {
			++index;
			continue;
			}
		}
		return new DeleteActiveRolePopUpPage(driver);
	}
	
	public boolean isCloneIconDisplayedForAGivenRolename(String rolename) {
		WebElement table = driver.findElement(By.xpath(tablexpath));	
		List<WebElement> rows = table.findElements(By.xpath(rowxpath));
        int index = 1;
		for (WebElement row : rows) {	
			List<WebElement> cells = row.findElements(By.xpath(String.format(rowparametrizedxpath, index)));		
			if ((cells.get(0).getText()).equals(rolename)) {
				cells.get(6).findElement(By.xpath(String.format(rowparametrizedxpath, index) + "/button[3]"));
				return true;	
			}
			else {
			++index;
			continue;
			}
		}
		return false;
	} 
	
	public void clickOnCloneIconForAGivenRolename(String rolename) {
		WebElement table = driver.findElement(By.xpath(tablexpath));	
		List<WebElement> rows = table.findElements(By.xpath(rowxpath));
        int index = 1;
		for (WebElement row : rows) {	
			List<WebElement> cells = row.findElements(By.xpath(String.format(rowparametrizedxpath, index)));		
			if ((cells.get(0).getText()).equals(rolename)) {
				cells.get(6).findElement(By.xpath(String.format(rowparametrizedxpath, index) + "/button[3]")).click();
				break;
			}
			else {
			++index;
			continue;
			}
		}
	}
	
	public String getStatusOfGivenRole(String rolename) {
			WebElement table = driver.findElement(By.xpath(tablexpath));	
			List<WebElement> rows = table.findElements(By.xpath(rowxpath));
			int index = 1;
			for (WebElement row : rows) {	
				List<WebElement> cells = row.findElements(By.xpath(String.format(rowparametrizedxpath, index)));		
				if ((cells.get(0).getText()).equals(rolename)) {
					return cells.get(5).getText();
				}
				else {
					++index;
					continue;
				}
			}
		throw new NoSuchElementException(String.format("Status for a rolename %s has not been found", rolename));
	}

	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(activeroleslisttab)) {
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

