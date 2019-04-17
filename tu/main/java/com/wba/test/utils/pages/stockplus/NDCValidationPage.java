/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.stockplus;

import static com.oneleo.test.automation.core.UIUtils.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class NDCValidationPage extends LoadableComponent<GoodsReceiptDetailsPage>{


	private WebDriver driver;

	public NDCValidationPage(WebDriver driver) {
		this.driver = driver;
	}

	@Override
	protected void load() {
		PageFactory.initElements(driver, this);
	}


	@Override
	protected void isLoaded() throws Error {

	}

	public List<WebElement> getShipmentCardList(){
		ui().waitForCondition(driver,ExpectedConditions.elementToBeClickable(driver.findElements(By.tagName("rx-goods-receipt-card")).get(0)),30);
		return driver.findElements(By.tagName("rx-goods-receipt-card"));
	}

	public boolean isIconCIIPresent(WebElement shipmentCard){
		return shipmentCard.findElements(By.className("goods-receipt-image-red")).size() > 0;
	}

	public WebElement getShipmentCard(int ord){
		return driver.findElements(By.tagName("rx-goods-receipt-card")).get(ord);
	}

	public WebElement getManuallyReceivePopUp(){
		ui().waitForCondition(driver,ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@id='cdk-overlay-0']/div/div/button"))),30);
		return driver.findElement(By.xpath("//*[@id='cdk-overlay-0']/div/div/button"));
	}

	public List<WebElement> getToteList(){
		List<WebElement> list = driver.findElements(By.tagName("mat-card"));
		return list;
	}

	public WebElement getTote(int ord){
		return driver.findElement(By.xpath("//*[@id='rx-store-user-handheld-app-main-container']/rx-goods-receipt-details/div/div[2]/mat-card[" + ord + "]/div[1]/mat-icon"));
	}

	public WebElement getOptionMenu(){

		return driver.findElement(By.xpath("//*[@id='rx-store-user-handheld-app-main-container']/rx-goods-receipt-details/div/div[2]/mat-card/div[2]/button/span/mat-icon"));
	}

	public WebElement getAddItemsManually(){
		return driver.findElement(By.xpath("//*[@id='add-item-button']/button"));
	}

	public WebElement getFirstNDC(){
		return driver.findElement(By.xpath("//*[@id='mat-input-0']"));
	}

	public WebElement getSecondNDC(){
		return driver.findElement(By.xpath("//*[@id='mat-input-1']"));
	}

	public WebElement getThirdNDC(){
		return driver.findElement(By.xpath("//*[@id='mat-input-2']"));
	}

	public void setFilters(String firstNDC, String secondNDC, String thirdNDC) {

		if (StringUtils.isNotBlank(firstNDC)) {
			WebElement firstNDCBox = getFirstNDC();
			firstNDCBox.click();
			firstNDCBox.sendKeys(firstNDC);
		}

		if (StringUtils.isNotBlank(secondNDC)) {
			WebElement secondNDCBox = getSecondNDC();
			secondNDCBox.click();
			secondNDCBox.sendKeys(secondNDC);
		}

		if (StringUtils.isNotBlank(thirdNDC)) {
			WebElement thirdNDCBox = getThirdNDC();
			thirdNDCBox.click();
			thirdNDCBox.sendKeys(thirdNDC);
		}
	}
	
	public String getNDCMsgError()
	{
		
		String errorMsg = driver.findElement(By.xpath("//*[@id='mat-error-0']")).getText();
		return errorMsg;
	}
	
	public void clickOnSubmit(){
		ui().waitForCondition(driver,ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@id='cdk-overlay-1']/mat-dialog-container/rx-goods-receipt-tote-manually-receive-dialog/div/div[2]/div[3]/button[2]"))),30);
		driver.findElement(By.xpath("//*[@id='cdk-overlay-1']/mat-dialog-container/rx-goods-receipt-tote-manually-receive-dialog/div/div[2]/div[3]/button[2]")).click();
		
	}
	
}
