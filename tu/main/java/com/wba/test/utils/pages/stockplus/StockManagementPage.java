/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.stockplus;

import static com.oneleo.test.automation.core.UIUtils.ui;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

public class StockManagementPage extends LoadableComponent<StockManagementPage> {

	@FindBy(id = "rx-corp-submenu-list-2")
	
	private WebDriver driver;
	
	@FindBy(id = "rx-corp-list-name-6")
	private WebElement stockManagemenButton;
   
	
	@FindBy(id="item-view-button")
	private WebElement viewButton;

	
	
	@FindBy(id="rx-corp-submenu-Search Stock-list-name-2")
	private WebElement searchStockLink;
	
	

	public StockManagementPage(WebDriver driver) {
		this.driver = driver;
	}

	
    public StockSearchPage clickSearchStockLink() {
    	 WebElement searchStockLink = driver.findElement(By.id("rx-corp-submenu-Search Stock-list-name-2"));
         searchStockLink.click();
     	return new StockSearchPage(driver); 
      }

    
    public Boolean isDisplayedItemDescription() {
        Boolean isPresentAndDisplayed = Boolean.FALSE;

        try {
            WebElement element = driver.findElement(By.id("srch_lbl"));
            if (element.isDisplayed()) {
                isPresentAndDisplayed = Boolean.TRUE;
            }
        } catch (NoSuchElementException nsee) {
            isPresentAndDisplayed = Boolean.FALSE;
        }

        return isPresentAndDisplayed;
    }
    
	
    public Boolean isDisplayedStockManagement() {
        Boolean isPresentAndDisplayed = Boolean.FALSE;

        try {
            WebElement element = driver.findElement(By.id("rx-corp-submenu-Stock Management-list-name-1"));
            if (element.isDisplayed()) {
                isPresentAndDisplayed = Boolean.TRUE;
            }
        } catch (NoSuchElementException nsee) {
            isPresentAndDisplayed = Boolean.FALSE;
        }

        return isPresentAndDisplayed;
    }


    
    public Boolean isDisplayedGridManagementButton() {
        Boolean isPresentAndDisplayed = Boolean.FALSE;

        try {
            WebElement element = driver.findElement(By.id("rx-corp-submenu-Grid Management-list-name-6"));
            if (element.isDisplayed()) {
                isPresentAndDisplayed = Boolean.TRUE;
            }
        } catch (NoSuchElementException nsee) {
            isPresentAndDisplayed = Boolean.FALSE;
        }

        return isPresentAndDisplayed;
    }
    
    
    
	public void clickViewButton() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(viewButton));
		viewButton.click();
	}

	@Override
	protected void load() {
		PageFactory.initElements(driver, this);
	}





	@Override
	protected void isLoaded() throws Error {
		// TODO Auto-generated method stub
		
	}

}
