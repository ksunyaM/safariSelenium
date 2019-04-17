/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.stockplus;

import static com.oneleo.test.automation.core.UIUtils.ui;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
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



public class OrderDetailsPage extends LoadableComponent<OrderDetailsPage> {
	
	private WebDriver driver;
	
	@FindBy(xpath="//*[@id='rx-inventory-main-container']/div/div[2]/rx-purchase-order-details/div/mat-toolbar/div/mat-toolbar-row/span")
	private WebElement labelOrderDetails;
	
	@FindBy(xpath="//*[@id='card-search-details']/div")
	private WebElement labelLineItemFound;
	
	@FindBy(xpath="//*[@id='mat-input-5']")
	private WebElement itemFilter;
	
	@FindBy(xpath="//*[@id='card-search-details']/form/div[2]/button")
	private WebElement refineButton;
	
	@FindBy(xpath="//*[@id='card-search-details']/form/div[3]/button")
	private WebElement clearButton;
	
	@FindBy(xpath="//*[@id='rx-inventory-main-container']/div/div[2]/rx-purchase-order-view/div/mat-toolbar[1]/div/mat-toolbar-row/button/span/mat-icon")
	private WebElement backButton;
	
	private String txtLabelItemFound=null;
	private String txtLabelItemFoundAfterSearch=null;
    private static final String STRING_NOT_FOUND="anySearchForNotfound";
	
	@Override
	protected void load() {
		PageFactory.initElements(driver, this);
	}
	
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
	
	public String getTextFromWebElement(String xpath)
	{
		String retStr="";
		
		if (existsElement(xpath))
		{
			retStr=driver.findElement(By.xpath(xpath)).getText();
		}
		
		return retStr;
	}
	
	public String contentTextFromLabelItmFound()
	{
		boolean existLabelItemFound=existsElement("//*[@id='card-search-details']/div");
        return (existLabelItemFound)? labelLineItemFound.getText():"";	
	}
	
	public void testSearchAndClean()
	{
		boolean existLabelItemFound=existsElement("//*[@id='card-search-details']/div");
				
        // Store the label with number order line in ingress to page
		//Boolean isPresent = driver.findElements(By.xpath("")).size() > 0
		if (existLabelItemFound) 	
			txtLabelItemFound = labelLineItemFound.getText();
		
		// Send a generic string for start search
		this.itemFilter.clear();
		this.itemFilter.sendKeys(STRING_NOT_FOUND);
		ui().waitForCondition(driver, ExpectedConditions.textToBePresentInElementValue(this.itemFilter, STRING_NOT_FOUND));
		// Press the refine button
		ui().waitForCondition(driver,ExpectedConditions.elementToBeClickable(refineButton),10);
		refineButton.click();
        // Click on clearButton for reset view	
		ui().waitForCondition(driver,ExpectedConditions.elementToBeClickable(clearButton),10);
		clearButton.click();
				
		
		// The item filter could be blank
		assertThat(StringUtils.isBlank(this.itemFilter.getText()), equalTo(Boolean.TRUE));
		
		// Check that the old label 
		if (existLabelItemFound) 	
			txtLabelItemFoundAfterSearch = labelLineItemFound.getText();
		else
			txtLabelItemFound="";
		
		if (txtLabelItemFound!=null)
		{
			
			// Order with line in origin
			if (existLabelItemFound) 	
				assertThat(txtLabelItemFound, equalTo(txtLabelItemFoundAfterSearch));
			
		}
		
	}
		
	
	
	@Override
	protected void isLoaded() throws Error {
		
		if (Objects.isNull(labelOrderDetails)) {
			throw new Error();
		}
		try {
			//assertThat(signInButton.isDisplayed(), equalTo(Boolean.TRUE));
			ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(labelOrderDetails));
		} catch (NoSuchElementException e) {
			Assert.fail("Page is not loaded!");
		}
		
	}
	
	public String getContentTextLabelOrderDetails()
	{
		return getTextFromWebElement("//*[@id='rx-inventory-main-container']/div/div[2]/rx-purchase-order-details/div/mat-toolbar/div/mat-toolbar-row/span");
	}
	

	public void pressRefineButton()
	{
		ui().waitForCondition(driver,ExpectedConditions.elementToBeClickable(refineButton),10);
		refineButton.click();
	}
	
	public PurchaseOrderPage pressBackButton()
	{
		
		//ui().waitForCondition(driver,ExpectedConditions.elementToBeClickable(backButton),10);
		 driver.findElement(By.xpath("//*[@id='rx-inventory-main-container']/div/div[2]/rx-purchase-order-details/div/mat-toolbar/div/mat-toolbar-row/button")).click(); 
		//backButton.click();
		return new PurchaseOrderPage(driver);
		
	}
	
	public void writeIntoItemFilter(String writeString)
	{
		// Send a generic string for start search
		this.itemFilter.clear();
		this.itemFilter.sendKeys(writeString);
		ui().waitForCondition(driver, ExpectedConditions.textToBePresentInElementValue(this.itemFilter, writeString));
				
	}
	
	public void testErroMessageSearch(String message)
	{
		String messageDisplayed="";
		String xpathErrorMessage="//*[@id='mat-error-0']/div";
		boolean existLabelItemFound=existsElement(xpathErrorMessage);
		// The item filter could be blank
		assertThat(existLabelItemFound, equalTo(Boolean.TRUE));
		messageDisplayed = getTextFromWebElement(xpathErrorMessage);
		
		assertThat(messageDisplayed, equalTo(message));
	}
	
	public int countRowOrderDetails()
	{   
		List<WebElement> rowCount=null;
		String xPathRow="//*[@id='rx-inventory-main-container']/div/div[2]/rx-purchase-order-details/div/rx-order-details-table/div/mat-table/mat-row[@role='row']";
		
		ui().waitForCondition(driver,ExpectedConditions.elementToBeClickable(refineButton),10);
		rowCount = driver.findElements(By.xpath(xPathRow));    
	    
		return ((rowCount!=null)?rowCount.size():0);
	}
	
	public boolean firstIsSelected()
	{   
		List<WebElement> rowCount=null;
		String xPathRow="//*[@id='rx-inventory-main-container']/div/div[2]/rx-purchase-order-details/div/rx-order-details-table/div/mat-table/mat-row[@role='row']";
		boolean retTest=false;
		
		ui().waitForCondition(driver,ExpectedConditions.elementToBeClickable(refineButton),10);
		rowCount = driver.findElements(By.xpath(xPathRow));    
	    
		if (rowCount!=null)
		{
			WebElement w1 = rowCount.get(0);
			//String classStr= w1.getCssValue("class");
			String classStr= w1.getAttribute("class");
		    if (classStr!=null)
		    {
		    	retTest=(classStr.indexOf("active-details")>=0)?true:false;
		    }
		}
		return retTest;
	}
	
	public List<String> retItemsDescriptionFromTable()
	{   
		List<WebElement> rowList=null;
		List<String> returnList = new ArrayList<String>();
		String xPathRow="//*[@id='rx-inventory-main-container']/div/div[2]/rx-purchase-order-details/div/rx-order-details-table/div/mat-table/mat-row/mat-cell[@role='gridcell']/rx-ellipsis/span";
		String descr=null;
		ui().waitForCondition(driver,ExpectedConditions.elementToBeClickable(refineButton),10);
		rowList = driver.findElements(By.xpath(xPathRow));    
	    if (rowList!=null)
	    {
	    	for (WebElement webe: rowList)
	    	{
	    		descr=webe.getAttribute("ng-reflect-message");
	    		returnList.add(descr);
	    	}
	    }
	    return returnList;
	}	
	
	public OrderDetailsPage(WebDriver driver) {
		this.driver=driver;
	}
	

}
