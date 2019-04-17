/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.stockplus;

import static com.oneleo.test.automation.core.UIUtils.ui;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

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

public class GoodsReceiptDetailsPage extends LoadableComponent<GoodsReceiptDetailsPage> {


	private WebDriver driver;

	public GoodsReceiptDetailsPage(WebDriver driver) {
		this.driver = driver;
	}


	@Override
	protected void load() {
		PageFactory.initElements(driver, this);
	}


	@Override
	protected void isLoaded() throws Error {

	}

	public List<WebElement> getGoodsRecepitCardList(){
		return driver.findElements(By.tagName("rx-goods-receipt-card"));
	}

	public WebElement getGoodsRecepitCard(int ord){
		return driver.findElements(By.tagName("rx-goods-receipt-card")).get(ord);
	}

	public List<WebElement> getBadgeList(){
		return driver.findElements(By.className("badge"));
	}

	public String getTitleDetailsPage(){
		return driver.findElement(By.xpath("//*[@id='rx-receiving-management-app-main-container']/rx-goods-receipt-details/mat-toolbar/div/mat-toolbar-row/div/div/span")).getText();
	}

	public String getShipmentNumber(){
		ui().waitForCondition(driver,ExpectedConditions.elementToBeClickable(driver.findElement(By.className("goods-receipt-number"))),30);
		return driver.findElement(By.className("goods-receipt-number")).getText();
	}

	public String getVendor(){
		ui().waitForCondition(driver,ExpectedConditions.elementToBeClickable(driver.findElement(By.className("goods-receipt-vendor"))),30);
		return driver.findElement(By.className("goods-receipt-vendor")).getText();
	}

	public String getDeliveryDate(){
		ui().waitForCondition(driver,ExpectedConditions.elementToBeClickable(driver.findElement(By.className("goods-receipt-delivery-date"))),30);
		return driver.findElement(By.className("goods-receipt-delivery-date")).getText();
	}

	public String getBadge(){
		ui().waitForCondition(driver,ExpectedConditions.elementToBeClickable(driver.findElement(By.className("badge"))),30);
		return driver.findElement(By.className("badge")).getText();
	}

	public String getTotReceived(){
		ui().waitForCondition(driver,ExpectedConditions.elementToBeClickable(driver.findElement(By.className("goods-receipt-bold-text"))),30);
		return driver.findElement(By.className("goods-receipt-bold-text")).getText().split("/")[0];
	}

	public String getAllTots(){
		return driver.findElement(By.className("goods-receipt-bold-text")).getText().split("/")[1];
	}

	public List<WebElement> getToteList(){
		return driver.findElements(By.className("tote-number"));
	}

	public Long getToteNumber(int ord){
		WebElement tote = driver.findElement(By.xpath("//*[@id='rx-receiving-management-app-main-container']/rx-goods-receipt-details/div/div[2]/mat-card["+ ord +"]/div[1]/div"));
		return Long.parseLong(tote.getText());
	}

	public WebElement getTote(int ord){
		return driver.findElement(By.xpath("//*[@id='rx-receiving-management-app-main-container']/rx-goods-receipt-details/div/div[2]/mat-card[" + ord + "]/div[1]/mat-icon"));
	}

	public String getstatusOnDetailsPage(){
		ui().waitForCondition(driver,ExpectedConditions.elementToBeClickable(driver.findElements(By.className("badge")).get(0)),30);
		return driver.findElements(By.className("badge")).get(0).getText();
	}

	public boolean isNeutralIconNotPresent(){
		return driver.findElements(By.className("neutral500")).size() == 0;
	}

	public String getManualReceivedLabel(int ord){
		return driver.findElements(By.className("manual-received-label")).get(ord).getText();
	}

	public boolean isOptionMenuDisplayed(int ord){
		return driver.findElements(By.className("neutral600")).get(ord).getText().equals("more_vert");
	}

	public WebElement getOptionMenu(int ord){
		ui().waitForCondition(driver,ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@id='rx-receiving-management-app-main-container']/rx-goods-receipt-details/div/div[2]/mat-card[" + ( ord + 1 ) + "]/div[2]/button"))),30);
		return driver.findElement(By.xpath("//*[@id='rx-receiving-management-app-main-container']/rx-goods-receipt-details/div/div[2]/mat-card[" + ( ord + 1 ) + "]/div[2]/button"));
	}

	public WebElement getManuallyReceivePopUp(){
		ui().waitForCondition(driver,ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@id='cdk-overlay-0']/div/div/button"))),30);
		return driver.findElement(By.xpath("//*[@id='cdk-overlay-0']/div/div/button"));
	}

	public WebElement getToteIcon(int ord){

		return driver.findElement(By.xpath("//*[@id='rx-receiving-management-app-main-container']/rx-goods-receipt-details/div/div[2]/mat-card[" + ord + "]/div[1]/mat-icon"));
	}

	public WebElement getDoneButton(){
		return driver.findElement(By.xpath("//*[@id='rx-receiving-management-app-main-container']/rx-goods-receipt-details/mat-toolbar/div/mat-toolbar-row/div/button/span/span"));
	}

	public WebElement getYesPopupButton(){
		return driver.findElement(By.xpath("//*[@id='cdk-overlay-0']/mat-dialog-container/rx-missing-received-totes-confirmation-dialog/div[2]/button[1]"));
	}

	public WebElement getNoPopupButton(){
		return driver.findElement(By.xpath("//*[@id='cdk-overlay-0']/mat-dialog-container/rx-missing-received-totes-confirmation-dialog/div[2]/button[2]"));
	}

	public WebElement getBackReceivingButton(){
		return driver.findElement(By.className("back-receiving"));
	}

	public boolean isReceivingActivityPage(){
		ui().waitForCondition(driver,ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@id='rx-receiving-management-app-main-container']/mat-toolbar/div/mat-toolbar-row/div/span"))),30);
		WebElement titlePage = driver.findElement(By.xpath("//*[@id='rx-receiving-management-app-main-container']/mat-toolbar/div/mat-toolbar-row/div/span"));

		return titlePage.getText().equals("Receiving Activity");
	}

	public boolean isReceivingCompletePage(){
		ui().waitForCondition(driver,ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@id='cdk-overlay-1']/mat-dialog-container/rx-receiving-complete-dialog/div/div[1]/span"))),30);
		WebElement titlePage = driver.findElement(By.xpath("//*[@id='cdk-overlay-1']/mat-dialog-container/rx-receiving-complete-dialog/div/div[1]/span"));

		return titlePage.getText().equals("Receiving Complete");
	}

	public boolean isReceiptPartiallyCompleted(int ord){
		ui().waitForCondition(driver,ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@id='rx-receiving-management-app-main-container']/rx-goods-receipt-summary/div/div[1]/rx-goods-receipt-card["+ ord + "]/mat-card/div/div[5]/mat-icon"))),30);
		WebElement titlePage = driver.findElement(By.xpath("//*[@id='rx-receiving-management-app-main-container']/rx-goods-receipt-summary/div/div[1]/rx-goods-receipt-card["+ ord + "]/mat-card/div/div[5]/mat-icon"));

		return (titlePage!=null);
	}

	public List<WebElement> getIconWarningList(int ord ){

		return driver.findElements(By.xpath("//*[@id='rx-receiving-management-app-main-container']/rx-goods-receipt-summary/div/div[1]/rx-goods-receipt-card["+(ord+1)+"]/mat-card/div/div[5]/mat-icon"));
	}
	
	public boolean isDoneButtonVisible(){
		return driver.findElements(By.xpath("//*[@id='rx-receiving-management-app-main-container']/rx-goods-receipt-details/mat-toolbar/div/mat-toolbar-row/div/button/span/span")).size() == 0;
	}
}
