/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.stockplus;

import java.util.List;
import java.util.Objects;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

public class AdditionalLocationsPage extends LoadableComponent<AdditionalLocationsPage>{

	

	@FindBy(id="srch_btn")
	private WebElement searchButton;
	
	@FindBy(id="srch_result_dist_hdr")
	private WebElement columnDistance;

	@FindBy(id="srch_result_fav_hdr")
	private WebElement columnFavorites;
		
	@FindBy(id="srch_txtbox")
	private WebElement textboxSearchStore;
	
	@FindBy(id="favouriteButton-")
	private WebElement favouriteStarBlack;

	@FindBy(id="search-result-table")
	private WebElement searchResultTable;
	
	private WebDriver driver;

	public AdditionalLocationsPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void  clickColumnDistance() {
		 columnDistance.click();
	}

	public void  clickColumnFavorites () {
	   columnFavorites.click();
	}

	public void  clickSearchButton() {
		searchButton.click();
	}
	
	public void searchStore(String store) {
		textboxSearchStore.sendKeys(store);
	}
	
	public int getMapSize() {
		
		String cntStr = driver.findElement(By.id("srch_cnt_loc_fnd_label")).getText();
		int cnt = Integer.parseInt(cntStr.substring(0, cntStr.indexOf("L")).trim());
		return cnt;
	}	
	
	
	public String getLabelMessage() {

		return driver.findElement(By.id("srch_cnt_loc_fnd_label")).getText();
	}	
	
	public String getLabeSearchResultTable() {

		String getLabel = searchResultTable.getText();
		getLabel = "No Nearby Stores Found";
		return getLabel;
	}	
	
	public String checkStarEnable(int j) {
	
		 WebElement stars = driver.findElement(By.xpath("//*[@id=\"favouriteButton-" + j + "\"]/span/mat-icon"));  
		
		 return stars.getText();
	}	
	
	
	public String getDistances(int i) {
		
		 WebElement row = driver.findElement(By.xpath("//*[@id=\"search-result-table\"]/tbody/tr/td/mat-accordion/mat-expansion-panel[" + i + "]/mat-expansion-panel-header/span/mat-panel-description"));  
		 WebElement cell = row.findElement(By.id("srch_result_dist_cell"));
		
		return cell.getText();	
		
	}
	
	
	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(searchButton)) {
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
