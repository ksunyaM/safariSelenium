/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.store;

import java.util.Objects;

import org.junit.Assert;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

import static com.oneleo.test.automation.core.UIUtils.ui;


public class DashboardPage extends LoadableComponent<DashboardPage>{


	@FindBy(xpath="//*[@id=\"hamburger-button\"]")
	private WebElement hamBurgerButton ;

    @FindBy(id="rx-store-submenu-Order Schedules-list-name-4")
    private WebElement orderSchedules;

    @FindBy(id="rx-store-submenu-Placed and Received Orders-list-name-2")
    private WebElement placedAndReceivedOrderButton;
    @FindBy(id="rx-store-list-button-3")
	private WebElement orderingButton;
	@FindBy(id="rx-store-submenu-list-button-3")
	private WebElement manualOrdersButton;
	@FindBy(id="rx-store-list-button-2")
	private WebElement stockManagementButton;
	@FindBy(id="rx-store-submenu-list-button-2")
	private WebElement stockSearchButton;
	@FindBy(id="rx-store-submenu-list-button-2")
	private WebElement placedAndReceivedOrdersButton;
	@FindBy(id="rx-store-list-button-4")
	private WebElement returnsButton;


	private WebDriver driver;

	public DashboardPage(WebDriver driver) {
		this.driver = driver;
	}
	
	/*
	 * Click buttons
	 */
	public void clickOrderingButton() {
		orderingButton.click();
	}
	
	public OrderItemsPage clickManualOrdersButton() {
		manualOrdersButton.click();
		return new OrderItemsPage(driver);
	}

	public void clickHamBurgerButton() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.hamBurgerButton), 10);
		this.hamBurgerButton.click();
	}
	
	public void clickStockManagementButton() {
		this.stockManagementButton.click();
	}
	
	public StockSearchPage clickStockSearchButton() {
		this.stockSearchButton.click();
		return new StockSearchPage(driver);
	}
	
	public PurchaseOrderManagementPage clickplacedAndReceivedOrdersButton() {
		this.placedAndReceivedOrdersButton.click();
		return new PurchaseOrderManagementPage(driver);
	}
	
	public ReturnsManagementPage clickReturnManagementButton() {
		this.stockSearchButton.click();
		return new ReturnsManagementPage(driver);
	}
	
	public void clickReturnsButton(){
		returnsButton.click();
	}

	public boolean isDisplayedPlacedAndReceivedOrderButton()
	{
		boolean isDisplayed=false;
		try{
			ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(placedAndReceivedOrderButton), 10);
			isDisplayed = placedAndReceivedOrderButton.isDisplayed();
		}
		catch (NoSuchElementException ne){}
		catch (TimeoutException to){}
		return  isDisplayed;
	}

	public boolean isDisplayedManualOrderButton()
	{
		boolean isDisplayed=false;
		try{
			ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(manualOrdersButton), 10);
			isDisplayed = manualOrdersButton.isDisplayed();
		}
		catch (NoSuchElementException ne){}
		catch (TimeoutException to){}
		return  isDisplayed;
	}

    public boolean isDisplayedOrderSchedules()
    {
        boolean isDisplayed=false;
        try{
            ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(orderSchedules), 10);
            isDisplayed = orderSchedules.isDisplayed();
        }
        catch (NoSuchElementException ne){}
        catch (TimeoutException to){}
        return  isDisplayed;
    }


    public PlacedAndReceivedOrderPage navigateToPlacedAndReceivedOrderButton()
	{
		placedAndReceivedOrderButton.click();
		return new PlacedAndReceivedOrderPage(driver);
	}
	
	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(orderingButton)) {
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
