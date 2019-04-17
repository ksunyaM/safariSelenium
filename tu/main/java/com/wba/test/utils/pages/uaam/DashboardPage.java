/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.uaam;

import static com.oneleo.test.automation.core.UIUtils.ui;

import java.util.Objects;

import org.junit.Assert;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

public class DashboardPage extends LoadableComponent<DashboardPage> {

	@FindBy(id="rx-corp-submenu-list-button-2")
	private WebElement forecastingParametersButton;
	
	@FindBy(css = "#rx-corp-list-name-11")
	private WebElement groupManagementButton;
	
	@FindBy(css = "#rx-corp-list-name-2")
	private WebElement stockManagementButton;

	@FindBy(css = "#rx-corp-list-9")
	private WebElement administrationButton;

	@FindBy(id = "rx-corp-submenu-list-2")
	private WebElement storeGroupButton;

	@FindBy(id = "rx-corp-list-button-6")
	private WebElement replenishmentButton;

	@FindBy(id = "rx-corp-list-button-3")
	private WebElement ordersButton;

	@FindBy(id = "rx-corp-submenu-Exception Counts-list-name-3")
	private WebElement exceptionCountsButton;
	
	
	@FindBy(id = "rx-corp-submenu-list-3")
	private WebElement orderSchedulesButton;
	
	@FindBy(id="rx-corp-list-button-8")
	private WebElement settingsButton;
	
	@FindBy(id="rx-corp-submenu-list-3")
	private WebElement productGroupsButton;
	
	@FindBy(id="rx-corp-submenu-list-button-5")
	private WebElement replenishmentParametersButton;
	
	@FindBy(id = "rx-corp-submenu-list-button-4")
	private WebElement replenishmentDiagnosticButtonMenu;
	
	@FindBy(id = "rx-corp-submenu-list-2")
	private WebElement forecastParametersButton;
	
	@FindBy(id = "rx-corp-list-6")
	private WebElement replenishmentLink;
	
	@FindBy(id = "rx-corp-list-name-5")
	private WebElement returnsButton;
		
	
	@FindBy(xpath="//*[@id=\"rx-corporate-user-app-main-container\"]/unauthorized-page/div/span")
	private WebElement unauthorizedPage;

	@FindBy(xpath="//*[@id=\"hamburger-button\"]")
	private WebElement hamBurgerButton ;
	
	@FindBy(xpath="//*[@id=\"rx-corporate-user-app-main-container\"]/unauthorized-page/div/button")
	private WebElement buttonLogoutUnauthorizedPage;

	@FindBy(xpath="//*[@id=\"mat-sidenav-container\"]/mat-list/mat-list-item/div")
	private WebElement buttonLogout;

	@FindBy(id = "rx-corp-submenu-Replenishment Diagnostic-list-name-4")
	private WebElement replenishmentDiagnosticMenuItem;

	@FindBy(id = "rx-corp-submenu-Returns Management-list-name-2")
	private WebElement returnsManagementLink;
	
	@FindBy(id = "mat-tab-label-0-1")
	private WebElement replenishmentDiagnosticStoreTab;

	@FindBy(id = "mat-tab-label-1-1")
	private WebElement settingStoreGroupsStoreTab;

	
	
	private WebDriver driver;

	public DashboardPage(WebDriver driver) {
		this.driver = driver;
	}
	
//	public ForecastParametersPage navigateToFoecastParametersPage() {
//		forecastingParametersButton.click();
//		return new ForecastParametersPage(driver);
//	}
//
//	public GroupManagementPage navigateToGroupManagementPage() {
//		groupManagementButton.click();
//		return new GroupManagementPage(driver);
//	}

	
	public void clickAdministrationButton() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.administrationButton), 20);
		this.administrationButton.click();
	}

//	public GroupManagementPage clickStoreGroupButton() {
//		this.storeGroupButton.click();
//		return new GroupManagementPage(driver);
//	}
//
//	public ReplenishmentPage clickReplenishmentButton() {
//		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.replenishmentButton), 20);
//		this.replenishmentButton.click();
//		return new ReplenishmentPage(driver);
//	}
//	
//	public ReplenishmentPage clickReplenishmentDiagnosisPanelButton() {
//		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.replenishmentDiagnosticButtonMenu), 20);
//		this.replenishmentDiagnosticButtonMenu.click();
//		return new ReplenishmentPage(driver);
//	}
//	public StockManagementPage clickStockManagementButton() {
//		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.stockManagementButton), 20);
//		this.stockManagementButton.click();
//		return new StockManagementPage(driver);
//	}
//	
//	public ReplenishmentPage clickReplenishmentDiagnostic() {
//		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.replenishmentDiagnosticMenuItem), 20);
//		this.replenishmentDiagnosticMenuItem.click();
//		return new ReplenishmentPage(driver);
//	}
	

	
	public com.wba.test.utils.pages.stockplus.ReturnsManagementPage clickReturnsManagement()
	 {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.returnsManagementLink), 20);
		this.returnsManagementLink.click();
		return new com.wba.test.utils.pages.stockplus.ReturnsManagementPage(driver);
	}
	
//	public ReplenishmentDiagnosingStorePage navigateReplenishmentDiagnostic() {
//		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.replenishmentDiagnosticMenuItem), 20);
//		this.replenishmentDiagnosticMenuItem.click();
//		return new ReplenishmentDiagnosingStorePage(driver);
//	}
//	
//	public ReplenishmentParametersPage clickReplenishmentParametersButton() {
//		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.replenishmentParametersButton), 20);
//		this.replenishmentParametersButton.click();
//		return new ReplenishmentParametersPage(driver);
//	}
//	
//	public ForecastParametersPage clickForecastParametersButton() {
//		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.replenishmentParametersButton), 20);
//		this.forecastParametersButton.click();
//		return new ForecastParametersPage(driver);
//	}
//	
//	public ExceptionCountsPage navigateToExceptionCountsPage() {
//		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.stockManagementButton), 30);
//		this.stockManagementButton.click();
//		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.exceptionCountsButton), 30);
//		this.exceptionCountsButton.click();
//
//		return new ExceptionCountsPage(driver);		
//		
//	}
//
//	public OrderSchedulingManagementPage navigateToOrderSchedulingManagement() {
//		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.ordersButton), 30);
//		this.ordersButton.click();
//		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.orderSchedulesButton), 30);
//		this.orderSchedulesButton.click();
//
//		return new OrderSchedulingManagementPage(driver);
//	}
	
	public void clickSettingsButton() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(settingsButton), 30);
		this.settingsButton.click();
	}
	
//	public ProductGroupManagementPage clickProductGroupsButton() {
//		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.productGroupsButton), 30);
//		this.productGroupsButton.click();
//		return new ProductGroupManagementPage(driver);
//	}
//	
//	public ReplenishmentPage clickReplenishmentLink() {
//		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.replenishmentLink), 50);
//		this.replenishmentLink.click();
//		return new ReplenishmentPage(driver);
//	}
//	
//	public ReplenishmentPage clickReturnsButton() {
//		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.returnsButton), 50);
//		this.returnsButton.click();
//		return new ReplenishmentPage(driver);
//	}
//	
	
	public void clickHamBurgerButton() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.hamBurgerButton), 50);
		this.hamBurgerButton.click();
	}

	public boolean viewUnauthorizedPage() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(unauthorizedPage), 30);
	return unauthorizedPage.isEnabled();
	}



	public void clickButtonLogoutUnauthorizedPage() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.buttonLogoutUnauthorizedPage), 50);
		this.buttonLogoutUnauthorizedPage.click();
	}
	
	public void clickButtonLogout() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(this.buttonLogout), 50);
		this.buttonLogout.click();
	}
	
	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(groupManagementButton)) {
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
