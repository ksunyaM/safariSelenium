/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.stockplus;

import static com.oneleo.test.automation.core.UIUtils.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

public class OrderSchedulingMngStoresResultsPage extends LoadableComponent<OrderSchedulingMngStoresResultsPage> {
	WebDriver driver;

	@FindBy(xpath = "//*[@id='rx-corporate-user-app-main-container']/dashboard-view/div/div/div[2]/rx-order-schedule/div/div/rx-stores/div/div[2]")
	private WebElement countLabel;
	@FindBy(id="item-view-button")	
	private WebElement viewButton;

	public OrderSchedulingMngStoresResultsPage(WebDriver driver) {
		this.driver = driver;
	}

	public int getCountLabel() {
		return Integer.parseInt(countLabel.findElement(By.tagName("b")).getText());
	}

	public int getRows() {
		return driver.findElements(By.tagName("mat-row")).size();
	}

	public List<Map<String, Object>> getHeaderLabels() {
		List<Map<String, Object>> rv = new ArrayList<>();
		List<WebElement> headerCells = this.driver.findElements(By.tagName("mat-header-cell"));
		for (WebElement headerCell : headerCells) {
			Map<String, Object> map = new HashMap<>();
			String cellLabel = headerCell.findElement(By.tagName("span")).getText();
			boolean sortable = false;
			if (!cellLabel.equals("Address") && !cellLabel.equals("City") && !cellLabel.equals("ZIP"))
				sortable = !Objects.isNull(headerCell.findElement(By.tagName("svg")));
			map.put("label", cellLabel);
			map.put("sortable", sortable);
			rv.add(map);
		}

		return rv;
	}

	public OrderSchedulingStoreLevelPage clickViewButton() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(viewButton), 20);
		viewButton.click();
		return new OrderSchedulingStoreLevelPage(driver).get();
	}
	
	public CreateTemporaryScheduleBox clickViewButtonForCreate() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(viewButton), 20);
		viewButton.click();
		return new CreateTemporaryScheduleBox(driver).get();
	}

	public void assertIsFirstRowSelected() {
		String className = driver.findElements(By.tagName("mat-row")).get(0).getAttribute("class");
		Assert.assertThat("Check first row  selected", className.contains("selected"), Matchers.equalTo(true));
	}

	public void assertIsPresentViewButton() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(viewButton), 20);
		Assert.assertThat("assertIsPresentViewButton", Objects.nonNull(viewButton), Matchers.equalTo(true));
	}

	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(countLabel)) {
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
