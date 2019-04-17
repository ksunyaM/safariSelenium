/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.notificationAdapter;

import static com.oneleo.test.automation.core.UIUtils.ui;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.FindsByXPath;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

public class SsePage extends LoadableComponent<SsePage>{
    
    @FindBy(xpath = "/html/body/common-showcase/div/div/main/mat-toolbar/div/mat-toolbar-row/span[1]")
    private WebElement titleSse;
    @FindBy(id = "mat-input-1")
    private WebElement employeeType;
    @FindBy(id = "mat-input-2")
    private WebElement employeeNumber;
    @FindBy(id = "mat-input-3")
    private WebElement storeId;
    @FindBy(xpath = "/html/body/common-showcase/div/div/main/mat-card/rx-server-sent-event/form/p/button")
    private WebElement openChannelBFFButton;
    private WebElement checkResponseMessage;
    
    private WebDriver driver;

    public SsePage(WebDriver driver) {
        this.driver = driver;
    }
    
    public void openChannelBFF(String employeeTypeData, String employeeNumberData, String storeIdData) throws Exception {
        insertData(employeeTypeData, employeeNumberData, storeIdData);
        ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(this.openChannelBFFButton), 20);
        openChannelBFFButton.click();
    }
    
    public void checkResponse(String orderLineCode) throws Exception {
        checkResponseMessage = ui().waitForCondition(driver, ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/common-showcase/div/div/main/mat-card/rx-server-sent-event/div[1]")),20);
        assertThat(checkResponseMessage.isDisplayed(), equalTo(Boolean.TRUE));
        checkResponseMessage.getText().contains(orderLineCode);
    }
    
    protected void insertData(String employeeTypeData, String employeeNumberData, String storeIdData) throws Exception { 
            this.employeeType.sendKeys(employeeTypeData);        
            this.employeeNumber.sendKeys(employeeNumberData);
            this.storeId.sendKeys(storeIdData);
    }

    @Override
    protected void load() {
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void isLoaded() throws Error {
        if (Objects.isNull(titleSse)) {
            throw new Error();
        }
        try {
        } catch (NoSuchElementException e) {
            Assert.fail("Page is not loaded!");
        }
    }

}
