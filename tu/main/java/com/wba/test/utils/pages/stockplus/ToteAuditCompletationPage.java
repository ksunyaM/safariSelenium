/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.stockplus;

import static com.oneleo.test.automation.core.UIUtils.ui;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

public class ToteAuditCompletationPage extends LoadableComponent<ToteAuditCompletationPage> {


	private WebDriver driver;

	public ToteAuditCompletationPage(WebDriver driver) {
		this.driver = driver;
	}


	@Override
	protected void load() {
		PageFactory.initElements(driver, this);
	}


	@Override
	protected void isLoaded() throws Error {

	}

	public WebElement getShipmentCard(int ord){
		return driver.findElements(By.tagName("rx-goods-receipt-card")).get(ord);
	}
	
	public List<WebElement> getCardList(){
		return driver.findElements(By.className("mat-card"));
	}

	public List<WebElement> getShipmentCardList(){
		return driver.findElements(By.tagName("rx-goods-receipt-card"));
	}

	public String getBadge(){
		ui().waitForCondition(driver,ExpectedConditions.elementToBeClickable(driver.findElement(By.className("badge"))),30);
		return driver.findElement(By.className("badge")).getText();
	}

	public List<WebElement> getIconAuditCompleteList(int ord ){
		return driver.findElements(By.xpath("//*[@id='rx-store-user-handheld-app-main-container']/rx-goods-receipt-summary/div/div[1]/rx-goods-receipt-card["+ord+"]/mat-card/div/div[4]/mat-icon"));
	}
	
	public List<WebElement> getIconAuditCompleteOnDetailsList(){
		return driver.findElements(By.className("mat-primary"));
	}
	
	public List<WebElement> getTextAuditCompleteList(int ord ){
		return driver.findElements(By.xpath("//*[@id='rx-store-user-handheld-app-main-container']/rx-goods-receipt-summary/div/div[1]/rx-goods-receipt-card["+ord+"]/mat-card/div/div[5]/div"));
	}
	
	public List<WebElement> getTextAuditCompleteOnDetailsList(int ord ){
		return driver.findElements(By.xpath("//*[@id='rx-store-user-handheld-app-main-container']/rx-goods-receipt-details/div/div[2]/mat-card["+ord+"]/div[2]/div"));
	}
}
