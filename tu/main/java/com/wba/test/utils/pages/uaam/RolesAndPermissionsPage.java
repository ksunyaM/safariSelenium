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

public class RolesAndPermissionsPage extends LoadableComponent<RolesAndPermissionsPage> {

    @FindBy(xpath = "//*[@id=\"rx-admin-app-main-container\"]/roles-permissions/div/mat-toolbar/div/mat-toolbar-row/span[1]")
    private WebElement pageTitle;
    @FindBy(xpath = "//*[@id=\"mat-input-27\"]")
    private WebElement searcharoleinput;
    @FindBy(xpath = "/html/body/app-root/app-nav/app-roles-permissions/app-roles-list/div[1]/button/span")
    private WebElement addanewrole;
    @FindBy(xpath = "//*[@id=\"mat-tab-label-0-0\"]")
    private WebElement activeroleslisttab;
    @FindBy(xpath = "//*[@id=\"mat-tab-label-0-1\"]")
    private WebElement rolesbeingaddedlisttab;
    @FindBy(xpath = "//*[@id=\"mat-tab-label-0-2\"]")
    private WebElement rolesbeingupdatedlisttab;
    @FindBy(xpath = "//*[@id=\"mat-tab-label-0-3\"]")
    private WebElement rolesbeingdeletedlisttab;

    private WebDriver driver;

    public RolesAndPermissionsPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getPageTitle() {
        return pageTitle.getText();
    }

    public String getPageTitleByFindElement() {
        pageTitle = driver.findElement(By.xpath("/html/body/rx-app/app-nav/roles-permissions/mat-toolbar/div/mat-toolbar-row/span[1]"));
        return pageTitle.getText();
    }

    public Boolean isPageTitleExist() {
        try {
            driver.findElement(By.xpath("//*[@id=\"rx-admin-app-main-container\"]/roles-permissions/mat-toolbar/div/mat-toolbar-row/span[1]"));
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    public String getLinkAccountMessage() {
        return driver.findElement(By.xpath("//*[@id=\"cdk-overlay-0\"]/mat-dialog-container/icplus-credentials-synched-exception-dialog/h3")).getText();
    }

    public String getLogWebSDLMessage() {
        return driver.findElement(By.xpath("//*[@id=\"cdk-overlay-0\"]/mat-dialog-container/icplus-credentials-synched-exception-dialog/p")).getText();
    }

    public Boolean isDiplayedGOTOWEBSDLButton() {
        return driver.findElement(By.xpath("//*[@id=\"cdk-overlay-0\"]/mat-dialog-container/icplus-credentials-synched-exception-dialog/div/button")).isDisplayed();
    }

    public String getLinkContactManagerMessage() {
        return driver.findElement(By.xpath("//*[@id=\"cdk-overlay-0\"]/mat-dialog-container/no-active-roles-dialog/h3")).getText();
    }

    public String getManagerAssignMessage() {
        return driver.findElement(By.xpath("//*[@id=\"cdk-overlay-0\"]/mat-dialog-container/no-active-roles-dialog/p")).getText();
    }

    public Boolean isDiplayedOKButton() {
        return driver.findElement(By.xpath("//*[@id=\"cdk-overlay-0\"]/mat-dialog-container/no-active-roles-dialog/div/button")).isDisplayed();
    }

    public void clickOKButton() throws InterruptedException {
        driver.findElement(By.xpath("//*[@id=\"cdk-overlay-0\"]/mat-dialog-container/no-active-roles-dialog/div/button")).click();
        synchronized (this) {
            wait(2000);
        }
    }

    public void clickGOTOWEBSDLButton() throws InterruptedException {
        driver.findElement(By.xpath("//*[@id=\"cdk-overlay-0\"]/mat-dialog-container/icplus-credentials-synched-exception-dialog/div/button")).click();
        synchronized (this) {
            wait(2000);
        }
    }

    public AddANewRolePage clickAddANewRoleButton() {
        ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(addanewrole));
        addanewrole.click();
        return new AddANewRolePage(driver);
    }

    public boolean isANewRoleButtonDisplayed() {
        ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(addanewrole));
        return addanewrole.isDisplayed();
    }

    public boolean isSearchARoleInputFieldDisplayed() {
        ui().waitForCondition(driver, ExpectedConditions.visibilityOf(searcharoleinput));
        return searcharoleinput.isDisplayed();
    }

    public void instertTextIntoSearchARoleInputField() {
        ui().waitForCondition(driver, ExpectedConditions.visibilityOf(searcharoleinput));
        searcharoleinput.click();
    }

    public ActiveRolesTabPage clickOnActiveRolesListTab() {
        ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(activeroleslisttab));
        activeroleslisttab.click();
        return new ActiveRolesTabPage(driver);
    }

    public boolean isActiveRolesTabDisplayed() {
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
        } catch (NoSuchElementException e) {
            throw new RuntimeException(String.format("The element: %s does not exists", activeroleslisttab.getText()));
        }
    }

    public RolesBeingAddedTabPage clickOnRolesBeingAddedListTab() {
        ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(rolesbeingaddedlisttab));
        rolesbeingaddedlisttab.click();
        return new RolesBeingAddedTabPage(driver);
    }

    public RolesBeingAddedTabPage clickOnRolesBeingAddedListTabAfterCreatedRole() throws InterruptedException {
        driver.findElement(By.xpath("//*[@id=\"mat-tab-label-2-1\"]")).click();
        synchronized (this) {
            wait(2000);
        }
        return new RolesBeingAddedTabPage(driver);
    }

    public boolean isRolesBeingAddedListTabDisplayed() {
        synchronized (this) {
            try {
                wait(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ui().waitForCondition(driver, ExpectedConditions.visibilityOf(rolesbeingaddedlisttab));
        try {
            rolesbeingaddedlisttab.isDisplayed();
            return true;
        } catch (NoSuchElementException e) {
            throw new RuntimeException(String.format("The element: %s does not exists", rolesbeingaddedlisttab.getText()));
        }
    }

    public RolesBeingDeletedTabPage clickOnRolesBeingDeletedListTab() {
        ui().waitForCondition(driver, ExpectedConditions.visibilityOf(rolesbeingdeletedlisttab));
        rolesbeingdeletedlisttab.click();
        return new RolesBeingDeletedTabPage(driver);
    }

    public boolean isRolesBeingDeletedListTabDisplayed() {
        synchronized (this) {
            try {
                wait(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ui().waitForCondition(driver, ExpectedConditions.visibilityOf(rolesbeingdeletedlisttab));
        try {
            rolesbeingdeletedlisttab.isDisplayed();
            return true;
        } catch (NoSuchElementException e) {
            throw new RuntimeException(String.format("The element: %s does not exists", rolesbeingdeletedlisttab.getText()));
        }
    }

    public Boolean isCorrectVersionApp(String version) throws InterruptedException{
    	/*
    	 * Click on avatar
    	 */
        synchronized (this) {
            wait(2000);
        }
        WebElement initialsButton = driver.findElement(By.xpath("/html/body/app-root/app-nav/rx-uikit-app-bar/div/div/rx-uikit-app-user-menu/button/span/span"));
        initialsButton.click();
    	/*
    	 * Click on About This Help
    	 */
        synchronized (this) {
            wait(2000);
        }
        List<WebElement> userMenuHeader = driver.findElements(By.cssSelector("div [class='mat-menu-content']  > div > span"));
        for (WebElement webElement : userMenuHeader) {
			if (webElement.getText().equals("About this app")){
				webElement.click();
				break;
			}
		}
    	/*
    	 * about tag <p>
    	 */
    	Boolean versionFind = Boolean.FALSE;
    	List<WebElement> elementsAbout =  driver.findElements(By.cssSelector("div [class='cdk-overlay-pane small-dialog'] > mat-dialog-container > rx-uikit-help-dialog > p"));
    	for (WebElement tagP : elementsAbout) {
			if (tagP.getText().contains(version)){
				versionFind = Boolean.TRUE;
				break;
			}
		}
    	/*
    	 * Close button
    	 */
    	driver.findElement(By.cssSelector("div [class='dialog-actions'] button")).click();
    	
    	return versionFind;
    }
    
    public void logout() throws InterruptedException {
        synchronized (this) {
            wait(2000);
        }
//        WebElement initialsButton = driver.findElement(By.xpath("//*[@id=\"avatar-button\"]/span"));
        WebElement initialsButton = driver.findElement(By.xpath("/html/body/app-root/app-nav/rx-uikit-app-bar/div/div/rx-uikit-app-user-menu/button/span/span"));
        initialsButton.click();
        synchronized (this) {
            wait(5000);
        }
//        WebElement logoutButton = driver.findElement(By.cssSelector("div[class='user-menu-log-out']"));
        WebElement logoutButton = driver.findElement(By.cssSelector("div[class='item-menu log-out ng-star-inserted']"));
        logoutButton.click();
        synchronized (this) {
            wait(8000);
        }
    }

    @Override
    protected void isLoaded() throws Error {
        if (Objects.isNull(searcharoleinput)) {
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
