/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.uaam;

import static com.oneleo.test.automation.core.UIUtils.ui;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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

import avro.shaded.com.google.common.collect.Ordering;

public class RolesBeingDeletedTabPage extends LoadableComponent<RolesBeingDeletedTabPage>  {
	
	public static final String tablexpath = "//*[@id=\"mat-tab-content-0-3\"]/div/app-deleting-roles/div/mat-table";
	public static final String rowxpath = "//*[@id=\"mat-tab-content-0-3\"]/div/app-deleting-roles/div/mat-table/mat-row";
	public static final String rowparametrizedxpath = "//*[@id=\"mat-tab-content-0-3\"]/div/app-deleting-roles/div/mat-table/mat-row[%d]/mat-cell";
	public static final String rolenamexpath = "//*[@id=\"mat-tab-content-0-3\"]/div/app-deleting-roles/div/mat-table/mat-row[%d]/mat-cell[1]";
    public static final String columnsxpath = "//*[@id=\"mat-tab-content-0-3\"]/div/app-deleting-roles/div/mat-table/mat-header-row/mat-header-cell";
    public static final String counterxpath = "//*[@id=\"mat-tab-label-0-3\"]/span";
    public static final String sortingarrow = "//*[@id=\"mat-tab-content-0-3\"]/div/app-deleting-roles/div/mat-table/mat-header-row/mat-header-cell[%d]";
    public static final String roleColumn = "Role";
    public static final String scheduledDeletionDate = "Scheduled deletion date";
    public static final String lastEdited = "Last edited";
    public static final String lastEditedBy = "Last edited by";
    
	@FindBy(xpath = "//*[@id=\"mat-tab-label-0-3\"]")
	private WebElement rolesbeingdeletedlisttab;
	@FindBy(xpath = "//*[@id=\"mat-tab-label-0-0\"]")
	private WebElement activeroleslisttab;
	
	private WebDriver driver;

	public RolesBeingDeletedTabPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public Date stringToDate(String strDate) throws ParseException {
		DateFormat format = new SimpleDateFormat("MM/DD/YYYY", Locale.US);
		return format.parse(strDate);	
	}
		 
	public void clickOnRolesBeingDeletedListTab() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(rolesbeingdeletedlisttab));
		rolesbeingdeletedlisttab.click();
	}
	
	public String getTabName() {		
		return rolesbeingdeletedlisttab.getText();
	}
	
	public ActiveRolesTabPage clickOnActiveRolesTab() {
		activeroleslisttab.click();
		return new ActiveRolesTabPage(driver);
	}
	
	public boolean isColumnExistingInTable(String columnname) {		
	    List<WebElement> columns = driver.findElements(By.xpath(columnsxpath));
	    for (WebElement column : columns) {
	    	if (column.getText().equals(columnname)) {
	    		return column.isDisplayed();   		
	    	}
	    	else {
	    		continue;
	    	}	    		
	    }
	    return false;		
	}
	
	public RolesBeingDeletedTabPage clickSortingColumnByName(String columnname) {	
		synchronized (this) {
			try {
				wait(5000);
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}
		}
	    List<WebElement> columns = driver.findElements(By.xpath(columnsxpath));
	    for (WebElement column : columns) {
	    	if (column.getText().equals(columnname)) {
	    		column.click(); 
	    		return new RolesBeingDeletedTabPage(driver);
	    	}
	    	else {
	    		continue;
	    	}	    		
	    }
	    throw new NoSuchElementException(String.format("Given column name: %s has not been found", columnname));	
	}	 
	
	public int mapColumnnameOnIntegerColumnIndex(String columnname) {
		int columnid = 0;
        switch (columnname) {
        case roleColumn:
        	columnid = 0;
        	break;
        case scheduledDeletionDate:
        	columnid = 1;
        	break;
        case lastEdited:  
        	columnid = 2;
        	break;
        case lastEditedBy:
        	columnid = 3;
        	break;
        default:
        	throw new NoSuchElementException(String.format("Given column name: %s is probably wrong thus the action can not be performed",         			                                        columnname));	
        }
        return columnid;
	}
	
	public RolesBeingDeletedTabPage clickSortingArrowForColumnName(String columnname) {	
		synchronized (this) {
			try {
				wait(5000);
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}
		}		
		int columnid = this.mapColumnnameOnIntegerColumnIndex(columnname);	
		driver.findElement(By.xpath(String.format(sortingarrow, columnid + 1))).click(); 
		return new RolesBeingDeletedTabPage(driver);			
	}

	
	public boolean isTableSortedInAscendingOrder(String columnname) throws ParseException {
		synchronized (this) {
			try {
				wait(5000);
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}
		}
		
		WebElement table = driver.findElement(By.xpath(tablexpath));	
		List<WebElement> rows = table.findElements(By.xpath(rowxpath));
		WebElement prevElement = null;
		int columnid = this.mapColumnnameOnIntegerColumnIndex(columnname);
        int index = 1;
               
		for (WebElement row : rows) {
			List<WebElement> cells = row.findElements(By.xpath(String.format(rowparametrizedxpath, index)));	
			if (index == 1) {
				prevElement = cells.get(columnid);	
				index++;
				continue;
			}
			else if ((columnname.equals(roleColumn) || columnname.equals(lastEditedBy)) && prevElement.getText().compareTo(cells.get(columnid).getText()) <= 0) { 
				prevElement = cells.get(columnid);		
				index++;
				continue;
			}
			else if ((columnname.equals(scheduledDeletionDate) || columnname.equals(lastEdited)) && stringToDate(prevElement.getText()).compareTo(stringToDate(cells.get(columnid).getText())) <= 0 ) { 
				prevElement = cells.get(columnid);
				index++;
                continue;
			}
			else if ((columnname.equals(roleColumn) || columnname.equals(lastEditedBy)) && prevElement.getText().compareTo(cells.get(columnid).getText()) > 0) {
				return false;
			}		
			else if ((columnname.equals(scheduledDeletionDate) || columnname.equals(lastEdited)) && stringToDate(prevElement.getText()).compareTo(stringToDate(cells.get(columnid).getText())) > 0) {
				return false;
			}	
		}
		return true;
	}
	
	public boolean isTableSortedInDescendingOrder(String columnname) throws ParseException {
		WebElement table = driver.findElement(By.xpath(tablexpath));	
		List<WebElement> rows = table.findElements(By.xpath(rowxpath));
		WebElement prevElement = null;
		int columnid = this.mapColumnnameOnIntegerColumnIndex(columnname);
        int index = 1;
		for (WebElement row : rows) {
			List<WebElement> cells = row.findElements(By.xpath(String.format(rowparametrizedxpath, index)));			
			if (index == 1) {
				prevElement = cells.get(columnid);		
				index++;
				continue;
			}
			else if ((columnname.equals(roleColumn) || columnname.equals(lastEditedBy)) && prevElement.getText().compareTo(cells.get(columnid).getText()) >= 0) {
			prevElement = cells.get(columnid);
			index++;
			continue;
			}
			else if ((columnname.equals(scheduledDeletionDate) || columnname.equals(lastEdited)) && stringToDate(prevElement.getText()).compareTo(stringToDate(cells.get(columnid).getText())) >= 0 ) { 
				prevElement = cells.get(columnid);
				index++;
                continue;
			}
			else if ((columnname.equals(roleColumn) || columnname.equals(lastEditedBy)) && prevElement.getText().compareTo(cells.get(columnid).getText()) < 0) {
				return false;
			}	
			else if ((columnname.equals(scheduledDeletionDate) || columnname.equals(lastEdited)) && stringToDate(prevElement.getText()).compareTo(stringToDate(cells.get(columnid).getText())) < 0) {
				return false;
			}
		}
		return true;	
	}
	
	public boolean waitForTabToLoad() {	
		synchronized (this) {
			try {
				wait(3000);
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}
		}		
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(rolesbeingdeletedlisttab));
		try {
			rolesbeingdeletedlisttab.isDisplayed();
			return true;
		}
		catch (NoSuchElementException e){			
			throw new RuntimeException(String.format("The element: %s does not exists", rolesbeingdeletedlisttab.getText()));			
		}
	}	
	
	public boolean isCounterDisplayed() {
		WebElement counter = driver.findElement(By.xpath(counterxpath));
		return counter.isDisplayed();
	}
	
	public int getCounterValue() {
		WebElement counter = driver.findElement(By.xpath(counterxpath));
		return Integer.parseInt(counter.getText());
	}
	
	public int getNumberOfRolesInTheList() {
		WebElement table = driver.findElement(By.xpath(tablexpath));	
		List<WebElement> rows = table.findElements(By.xpath(rowxpath));
		return rows.size();
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
	
	public ViewDeletingRolePage clickOnARoleNameFoundByTextInListOfRolesTable(String rolename) {
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
		return new ViewDeletingRolePage(driver);
	}

	public boolean isEditIconDisplayedForAGivenRolename(String rolename) {
		WebElement table = driver.findElement(By.xpath(tablexpath));	
		List<WebElement> rows = table.findElements(By.xpath(rowxpath));
        int index = 1;
		for (WebElement row : rows) {	
			List<WebElement> cells = row.findElements(By.xpath(String.format(rowparametrizedxpath, index)));		
			if ((cells.get(0).getText()).equals(rolename)) {
				cells.get(5).findElement(By.xpath(String.format(rowparametrizedxpath, index) + "/button[1]"));
				return true;	
			}
			else {
			++index;
			continue;
			}
		}
		return false;
	}
	
	public EditDeletingRolePopUpPage clickOnEditIconForAGivenRolename(String rolename) {
		WebElement table = driver.findElement(By.xpath(tablexpath));	
		List<WebElement> rows = table.findElements(By.xpath(rowxpath));
        int index = 1;
		for (WebElement row : rows) {	
			List<WebElement> cells = row.findElements(By.xpath(String.format(rowparametrizedxpath, index)));		
			if ((cells.get(0).getText()).equals(rolename)) {
				cells.get(5).findElement(By.xpath(String.format(rowparametrizedxpath, index) + "/button[1]")).click();
				break;
			}
			else {
			++index;
			continue;
			}
		}
		return new EditDeletingRolePopUpPage(driver);
	}

	public boolean isDeleteIconDisplayedForAGivenRolename(String rolename) {
		WebElement table = driver.findElement(By.xpath(tablexpath));	
		List<WebElement> rows = table.findElements(By.xpath(rowxpath));
        int index = 1;
		for (WebElement row : rows) {	
			List<WebElement> cells = row.findElements(By.xpath(String.format(rowparametrizedxpath, index)));		
			if ((cells.get(0).getText()).equals(rolename)) {
				cells.get(5).findElement(By.xpath(String.format(rowparametrizedxpath, index) + "/button[2]"));
				return true;	
			}
			else {
			++index;
			continue;
			}
		}
		return false;
	}
	
	public void clickOnDeleteIconForAGivenRolename(String rolename) {
		WebElement table = driver.findElement(By.xpath(tablexpath));	
		List<WebElement> rows = table.findElements(By.xpath(rowxpath));
        int index = 1;
		for (WebElement row : rows) {	
			List<WebElement> cells = row.findElements(By.xpath(String.format(rowparametrizedxpath, index)));		
			if ((cells.get(0).getText()).equals(rolename)) {
				cells.get(5).findElement(By.xpath(String.format(rowparametrizedxpath, index) + "/button[2]")).click();
				break;
			}
			else {
			++index;
			continue;
			}
		}
	}
	
	public boolean isDeleteIconForAGivenRolenameEnabled(String rolename) {
		WebElement table = driver.findElement(By.xpath(tablexpath));	
		List<WebElement> rows = table.findElements(By.xpath(rowxpath));
		WebElement trashIcon = null;
        int index = 1;
		for (WebElement row : rows) {	
			List<WebElement> cells = row.findElements(By.xpath(String.format(rowparametrizedxpath, index)));		
			if ((cells.get(0).getText()).equals(rolename)) {
				trashIcon = cells.get(5).findElement(By.xpath(String.format(rowparametrizedxpath, index) + "/button[2]"));
				break;
			}
			else {
			++index;
			continue;
			}			
		}
		if (trashIcon == null)
			throw new NoSuchElementException(String.format("Delete Icon for rolename: %s has not been found", rolename));
		else 
		return trashIcon.isEnabled();
	}
	
	public boolean isCloneIconDisplayedForAGivenRolename(String rolename) {
		WebElement table = driver.findElement(By.xpath(tablexpath));	
		List<WebElement> rows = table.findElements(By.xpath(rowxpath));
        int index = 1;
		for (WebElement row : rows) {	
			List<WebElement> cells = row.findElements(By.xpath(String.format(rowparametrizedxpath, index)));		
			if ((cells.get(0).getText()).equals(rolename)) {
				cells.get(5).findElement(By.xpath(String.format(rowparametrizedxpath, index) + "/button[3]"));
				return true;	
			}
			else {
			++index;
			continue;
			}
		}
		return false;
	} 
	
	public CloneDeletingRolePage clickOnCloneIconForAGivenRolename(String rolename) {
		WebElement table = driver.findElement(By.xpath(tablexpath));	
		List<WebElement> rows = table.findElements(By.xpath(rowxpath));
        int index = 1;
		for (WebElement row : rows) {	
			List<WebElement> cells = row.findElements(By.xpath(String.format(rowparametrizedxpath, index)));		
			if ((cells.get(0).getText()).equals(rolename)) {
				cells.get(5).findElement(By.xpath(String.format(rowparametrizedxpath, index) + "/button[3]")).click();
				break;
			}
			else {
			++index;
			continue;
			}
		}
		return new CloneDeletingRolePage(driver);
	}

	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(rolesbeingdeletedlisttab)) {
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
