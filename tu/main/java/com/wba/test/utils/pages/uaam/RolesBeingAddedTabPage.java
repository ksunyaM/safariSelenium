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

public class RolesBeingAddedTabPage extends LoadableComponent<RolesBeingAddedTabPage> {

    public static final String tablexpath = "/html/body/rx-app/app-nav/roles-permissions/app-roles-list/div[2]/mat-tab-group";
    public static final String rowxpath = "//*[@id=\"mat-tab-content-0-1\"]/div/app-adding-roles/div/mat-table/mat-row";
    public static final String rowparametrizedxpath = "//*[@id=\"mat-tab-content-0-1\"]/div/app-adding-roles/div/mat-table/mat-row[%d]/mat-cell";
    public static final String counterxpath = "//*[@id=\"mat-tab-label-0-1\"]/span";
    public static final String columnsxpath = "//*[@id=\"mat-tab-content-0-1\"]/div/app-adding-roles/div/mat-table/mat-header-row/mat-header-cell";
    // *[@id="mat-tab-content-0-1"]/div/app-adding-roles/div/mat-table/mat-row[2]/mat-cell[6]/button[1]
    @FindBy(xpath = "//*[@id=\"mat-tab-label-0-1\"]")
    private WebElement rolesbeingaddedlisttab;

    private WebDriver driver;

    public RolesBeingAddedTabPage(WebDriver driver) {
        this.driver = driver;
    }

    public void clickOnRolesBeingAddedListTab() {
        ui().waitForCondition(driver, ExpectedConditions.visibilityOf(rolesbeingaddedlisttab));
        rolesbeingaddedlisttab.click();
    }

    public String getTabName() {
        return rolesbeingaddedlisttab.getText();
    }

    public boolean isColumnExistingInTable(String columnname) {
        List<WebElement> columns = driver.findElements(By.xpath(columnsxpath));
        for (WebElement column : columns) {
            if (column.getText().equals(columnname)) {
                return column.isDisplayed();
            } else {
                continue;
            }
        }
        throw new NoSuchElementException(String.format("Given column name: %s has not been found", columnname));
    }

    public RolesBeingAddedTabPage clickSortingColumnByName(String columnname) {
        List<WebElement> columns = driver.findElements(By.xpath(columnsxpath));
        for (WebElement column : columns) {
            if (column.getText().equals(columnname)) {
                column.click();
                return new RolesBeingAddedTabPage(driver);
            } else {
                continue;
            }
        }
        throw new NoSuchElementException(String.format("Given column name: %s has not been found", columnname));
    }

    public boolean waitForTabToLoad() {
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
            System.out.println(String.format("cells = %s", cells.get(0).getText()));
            if ((cells.get(0).getText()).equals(rolename)) {
                return true;
            } else {
                ++index;
                continue;
            }
        }
        return false;
    }

    public boolean isRoleCreatedFirstInTable(String rolename) {
        WebElement table = driver.findElement(By.xpath(tablexpath));
        List<WebElement> tableRows = table.findElements(By.tagName("mat-row"));
        return rolename.equals(tableRows.get(0).getText().split("\n")[0]);
    }

    public ViewAddingRolePage clickOnARoleNameFoundByTextInListOfRolesTable(String rolename) {
        WebElement table = driver.findElement(By.xpath(tablexpath));
        List<WebElement> rows = table.findElements(By.xpath(rowxpath));
        int index = 1;
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.xpath(String.format(rowparametrizedxpath, index)));
            if ((cells.get(0).getText()).equals(rolename)) {
                cells.get(0).click();
                return new ViewAddingRolePage(driver);
            } else {
                ++index;
                continue;
            }
        }
        throw new NoSuchElementException(String.format("Given rolename: %s has not been found in the list of roles", rolename));
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
            } else {
                ++index;
                continue;
            }
        }
        return false;
    }

    public EditAddingRolePage clickOnEditIconForAGivenRolename(String rolename) {
        WebElement table = driver.findElement(By.xpath(tablexpath));
        List<WebElement> rows = table.findElements(By.xpath(rowxpath));
        int index = 1;
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.xpath(String.format(rowparametrizedxpath, index)));
            if ((cells.get(0).getText()).equals(rolename)) {
                cells.get(5).findElement(By.xpath(String.format(rowparametrizedxpath, index) + "/button[1]")).click();
                return new EditAddingRolePage(driver);
            } else {
                ++index;
                continue;
            }
        }
        throw new NoSuchElementException(String.format("For given rolename: %s the edit icon has not been found", rolename));
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
            } else {
                ++index;
                continue;
            }
        }
        return false;
    }

    public DeleteAddingRolePopUpPage clickOnDeleteIconForAGivenRolename(String rolename) {
        WebElement table = driver.findElement(By.xpath(tablexpath));
        List<WebElement> rows = table.findElements(By.tagName("mat-row"));
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("mat-cell"));
            if ((cells.get(0).getText()).equals(rolename)) {
                List<WebElement> buttons = cells.get(5).findElements(By.tagName("button"));
                buttons.get(1).click();
                return new DeleteAddingRolePopUpPage(driver);
            }
        }
        throw new NoSuchElementException(String.format("For given rolename: %s the trash icon has not been found", rolename));
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
            } else {
                ++index;
                continue;
            }
        }
        return false;
    }

    public CloneAddingRolePage clickOnCloneIconForAGivenRolename(String rolename) {
        WebElement table = driver.findElement(By.xpath(tablexpath));
        List<WebElement> rows = table.findElements(By.xpath(rowxpath));
        int index = 1;
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.xpath(String.format(rowparametrizedxpath, index)));
            if ((cells.get(0).getText()).equals(rolename)) {
                cells.get(5).findElement(By.xpath(String.format(rowparametrizedxpath, index) + "/button[3]")).click();
                return new CloneAddingRolePage(driver);
            } else {
                ++index;
                continue;
            }
        }
        throw new NoSuchElementException(String.format("For given rolename: %s the clone icon has not been found", rolename));
    }

    @Override
    protected void isLoaded() throws Error {
        if (Objects.isNull(rolesbeingaddedlisttab)) {
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
