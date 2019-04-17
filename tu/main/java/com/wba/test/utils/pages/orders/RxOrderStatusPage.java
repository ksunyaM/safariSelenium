/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.orders;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

public class RxOrderStatusPage  extends LoadableComponent<RxOrderStatusPage> {

    @FindBy(xpath = "//span[contains(text(),'Rx order status')]")
    private WebElement rxOrderStatusPageLabel;

    private WebDriver driver;

    public RxOrderStatusPage(WebDriver driver) {
        this.driver = driver;
    }

    public void openViewDetails(String status, String statusInfo) {
        driver
                .findElement(By.xpath("//tr[td/div[contains(text(),'" + status.toUpperCase() + "')] and td[contains(text(),'" + statusInfo.toUpperCase() + "')]]/child::td[contains(@class,'dot-menu')]"))
                .click();
        driver.findElement(By.xpath("//button[contains(text(),'View details')]")).click();
    }

    private void clickOnReprint(String status) {
        driver
                .findElement(By.xpath("//div[contains(text(),'" + status.toUpperCase() + "')]/parent::td[contains(@class,'column-status')]/following-sibling::td[contains(@class,'dot-menu')]"))
                .click();
        driver.findElement(By.xpath("//button[contains(text(),'Reprint')]")).click();
    }

    private List<String> getListOfTextValueFromWebelement(List<WebElement> elementList){
        return  elementList
                .stream()
                .map(WebElement::getText)
                .sorted()
                .collect(Collectors.toList());
    }

    public List<String> getSortedListOfDotMenuOptions(String status){
        clickOnReprint(status);
        List<WebElement>elementList = driver
                .findElements(By.xpath("//button[contains(@class,'mat-menu-item') and @role = 'menuitem' and @tabindex!='-1']"));
        return getListOfTextValueFromWebelement(elementList);
    }

    public boolean isDetailsWindowFullfilled(){
        return driver.findElement(By.id("product-name")).getText()!=null
                && driver.findElement(By.id("patient-name")).getText()!=null
                && driver.findElement(By.xpath("//div[@class='app-order-status-dialog']//div[contains(@class,'label')]")).getText()!=null
                && driver.findElement(By.xpath("//p[contains(text(),'Status info')]//following-sibling::p")).getText()!=null
                && driver.findElement(By.xpath("//p[contains(text(),'Rx')]//following-sibling::p")).getText()!=null;
    }

    public void closeDetailsWindow(){
        driver.findElement(By.xpath("//mat-icon[contains(text(),'close')]")).click();
    }

    public boolean isDetailsWindowDesplayed(){
        return driver.findElement(By.xpath("//span[contains(text(),'Details')]")).isDisplayed();
    }





    @Override
    protected void load() {
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void isLoaded() throws Error {
        if (Objects.isNull(rxOrderStatusPageLabel)) {
            throw new Error();
        }
        try {
        } catch (NoSuchElementException e) {
            Assert.fail("Page is not loaded!");
        }

    }
}