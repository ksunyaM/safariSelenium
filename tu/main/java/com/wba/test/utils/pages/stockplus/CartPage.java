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

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;


public class CartPage extends LoadableComponent<CartPage>{
	private WebDriver driver;
		
	@FindBy(xpath="//*[@id='cart-sub-header']/div[2]/div/div[1]")
	private WebElement labelItemCounter; 
	
	@FindBy(css="#page-header-back-button > span > mat-icon	")
	private WebElement backButton;
	
	@FindBy(id="add-additional-items")
	private WebElement addAdditionalItemsButton;
	
	@FindBy(id="shopping-cart-icon")
	private WebElement shoppingIcon;
	
    public CartPage(WebDriver driver) {
        this.driver = driver;
    }
    
    public OrderItemsPage goBackToOrderItems(String buttonName){
    	if(buttonName.equals("<"))
    	{
    		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(backButton));
    		backButton.click();
    	}
    	else
    	{
    		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(addAdditionalItemsButton));
    		addAdditionalItemsButton.click();
    	}
    	return new OrderItemsPage(driver);
    	
    }
    
	public List<String> getItemsTotalsLabels() throws Exception {
		ArrayList<String> itemsTotalsList = new ArrayList();

		try {
			for (int i = 1; i <= 5; ++i) {
				WebElement name = (WebElement) ui().waitForCondition(this.driver, ExpectedConditions
						.presenceOfElementLocated(By.xpath("//*[@id='cart-sub-header']/div[2]/div/div[" + i + "]/b")),
						10L);
				WebElement number = (WebElement) ui().waitForCondition(this.driver,
						ExpectedConditions.presenceOfElementLocated(
								By.xpath("//*[@id='cart-sub-header']/div[2]/div/div[" + i + "]/span")),
						10L);
				String itemsTotalsListLabel = "";
				if (StringUtils.isNotBlank(name.getText()) && StringUtils.isNotBlank(number.getText()))
					itemsTotalsListLabel = name.getText().concat(" ").concat(number.getText());
				itemsTotalsList.add(itemsTotalsListLabel);
			}
		} catch (Exception e) {

		}

		return itemsTotalsList;
	}

	public List<Map<String, ?>> getCardsHeaders() {
		List<Map<String, ?>> rv = new ArrayList<>();
		// cards display on two columns

		List<WebElement> headers = this.driver.findElements(By.tagName("mat-expansion-panel-header"));
		Assert.assertTrue(!headers.isEmpty());

		for (WebElement header : headers) {
			Map<String, Object> headerMap = new HashMap<>();
			WebElement deliveryDateLabel = null;
			String deliveryDateLabelStr = null;
			WebElement deliveryDateValue = null;
			String deliveryDateValueStr = null;
			WebElement cutoffValue = null;
			WebElement status = null;
			String statusStr = null;
			String cuttOffStr = "Released within the last hour";
			WebElement title = header.findElement(By.className("mat-expansion-panel-header-title"));
			String titleStr = title.getText();
			
			WebElement timer = header.findElement(By.tagName("rx-timer"));
			WebElement clockIcon = timer.findElement(By.tagName("mat-icon"));
			WebElement chevronButton = header.findElement(By.className("mat-expansion-indicator"));

			if (!title.getText().contains("Released")) {
				deliveryDateLabel = header.findElement(By.className("estimated-date-label"));
				deliveryDateLabelStr = deliveryDateLabel.getText();
				deliveryDateValue = header.findElement(By.className("estimated-date"));
				deliveryDateValueStr = deliveryDateValue.getText();
				cutoffValue = header.findElement(By.className("font-size-12px"));
				status = header.findElement(By.className("status"));
				statusStr = status.getText();
				cuttOffStr = cutoffValue.getText().replace("access_time", "").trim();
			}
			if (statusStr != null && statusStr.equals("Active")) {
				titleStr = titleStr.replace("Active", "").trim();
			}

			headerMap.put("title", titleStr);
			headerMap.put("deliveryDateLabel", deliveryDateLabelStr);
			headerMap.put("deliveryDateValue", deliveryDateValueStr);
			headerMap.put("clockIcon", !Objects.isNull(clockIcon));
			headerMap.put("cutoffValue", cuttOffStr);
			headerMap.put("chevronButton", !Objects.isNull(chevronButton));
			headerMap.put("status", statusStr);
			rv.add(headerMap);
		}

		
		return rv;
	}

	public List<Map<String, ?>> getCards() {
		List<Map<String, ?>> rv = new ArrayList<>();
		// cards display on two columns

		List<WebElement> cards = this.driver.findElements(By.tagName("mat-expansion-panel"));

		for (WebElement card : cards) {
			Map<String, Object> cardMap = new HashMap<>();
			WebElement deliveryDateLabel = null;
			String deliveryDateLabelStr = null;
			WebElement deliveryDateValue = null;
			String deliveryDateValueStr = null;
			WebElement cutoffValue = null;
			WebElement status = null;
			String statusStr = null;
			String cuttOffStr = "Released within the last hour";

			// header
			WebElement header = card.findElement(By.tagName("mat-expansion-panel-header"));
			WebElement title = header.findElement(By.className("mat-expansion-panel-header-title"));
			String titleStr = title.getText();

			WebElement timer = header.findElement(By.tagName("rx-timer"));
			WebElement clockIcon = timer.findElement(By.tagName("mat-icon"));
			WebElement chevronButton = header.findElement(By.className("mat-expansion-indicator"));

			if (!title.getText().contains("Released")) {
				deliveryDateLabel = header.findElement(By.className("estimated-date-label"));
				deliveryDateLabelStr = deliveryDateLabel.getText();
				deliveryDateValue = header.findElement(By.className("estimated-date"));
				deliveryDateValueStr = deliveryDateValue.getText();
				cutoffValue = header.findElement(By.className("font-size-12px"));
				status = header.findElement(By.className("status"));
				statusStr = status.getText();
				cuttOffStr = cutoffValue.getText().replace("access_time", "").trim();
			}

			if (statusStr != null && statusStr.equals("Active")) {
				titleStr = titleStr.replace("Active", "").trim();
			}

			cardMap.put("header_title", titleStr);
			cardMap.put("header_deliveryDateLabel", deliveryDateLabelStr);
			cardMap.put("header_deliveryDateValue", deliveryDateValueStr);
			cardMap.put("header_clockIcon", !Objects.isNull(clockIcon));
			cardMap.put("header_cutoffValue", cuttOffStr);
			cardMap.put("header_chevronButton", !Objects.isNull(chevronButton));
			cardMap.put("header_status", statusStr);

			// tiles
			List<WebElement> tiles = card.findElements(By.className("margin-top"));
			Assert.assertTrue(!tiles.isEmpty());

			for (WebElement tile : tiles) {

				WebElement itemDescription;
				WebElement manufacturerValue;
				WebElement substituteValue;
				WebElement ordqtyValue;
				WebElement supplierValue;
				WebElement exDeDateValue;
				WebElement orderDetBtn;
				WebElement storageIcon;
				WebElement pancilIcon;
				WebElement trashIcon;

				String itemDescriptionStr = null;
				String ndcStr = null;
				String manufacturerLabelStr = null;
				String manufacturerValueStr = null;
				String substituteLabelStr = null;
				String substituteValueStr = null;
				String ordqtyLabelStr = null;
				String ordqtyValueStr = null;
				String supplierLabelStr = null;
				String supplierValueStr = null;
				String exDeDateLabelStr = null;
				String exDeDateValueStr = null;
				String orderDetBtnBool = null;
				boolean storageIconBool = false;
				boolean cIIdrugsIconBool = false;
				String pancilIconStr = "";
				String trashIconStr = "";

				// tiles labels
				List<WebElement> labels = tile.findElements(By.className("line-labels"));
				Assert.assertTrue(!labels.isEmpty());

				for (int i = 0; i < labels.size(); i++) {
					WebElement label = labels.get(i);

					if (label.getText().startsWith("NDC"))
						ndcStr = label.getText();
					else if (label.getText().equals("Manufacturer"))
						manufacturerLabelStr = "Manufacturer";
					else if (label.getText().equals("Substitute"))
						substituteLabelStr = "Substitute";
					else if (label.getText().equals("Ord Qty"))
						ordqtyLabelStr = "Ord Qty";
					else if (label.getText().equals("Supplier"))
						supplierLabelStr = "Supplier";
					else if (label.getText().equals("Estimated Delivery:"))
						exDeDateLabelStr = "Estimated Delivery:";
				}

				// tiles values
				List<WebElement> values = tile.findElements(By.className("line-values"));
				Assert.assertTrue(!values.isEmpty());

				itemDescription = values.get(0);
				itemDescriptionStr = itemDescription.getText();
				manufacturerValue = values.get(1);
				manufacturerValueStr = manufacturerValue.getText();

				WebElement orderline = tile.findElement(By.tagName("order-line"));
				substituteValue = orderline.findElement(By.xpath("./div[3]/div[2]"));
				substituteValueStr = substituteValue.getText();
				ordqtyValue = orderline.findElement(By.xpath("./div[4]/div[2]"));
				ordqtyValueStr = ordqtyValue.getText();

				if (titleStr.contains("Drop-Ship")) {
					supplierValue = orderline.findElement(By.xpath("./div[5]/span"));
					supplierValueStr = supplierValue.getText();
					storageIconBool = testStorageIcon(orderline, 6);
				}
				if (titleStr.contains("Released")) {
					supplierValue = orderline.findElement(By.xpath("./div[5]/span"));
					supplierValueStr = supplierValue.getText();
					storageIconBool = testStorageIcon(orderline, 6);
				}
				if (titleStr.contains("CII")) {
					// CII with or without storage
					List<WebElement> lsIcons = orderline.findElements(By.className("fridge-size"));
					Assert.assertTrue(!lsIcons.isEmpty());
					cIIdrugsIconBool = !Objects.isNull(lsIcons.get(0));
					if (lsIcons.size() > 1)
						storageIconBool = !Objects.isNull(lsIcons.get(1));
				} else {
					storageIconBool = testStorageIcon(orderline, 5);
				}

				// tiles edit-trash icons
				if (!titleStr.contains("Released")) {
					List<WebElement> icons = tile.findElements(By.tagName("mat-icon"));
					for (WebElement icon : icons) {
						if (icon.getText().contains("edit"))
							pancilIconStr = "mode_edit";
						if (icon.getText().contains("delete"))
							trashIconStr = "delete";
					}
				}

				cardMap.put("tile_itemDescription", itemDescriptionStr);
				cardMap.put("tile_NDC", ndcStr);

				cardMap.put("tile_manufacturer_label", manufacturerLabelStr);
				cardMap.put("tile_manufacturer_value", manufacturerValueStr);

				cardMap.put("tile_substitute_label", substituteLabelStr);
				cardMap.put("tile_substitute_value", substituteValueStr);

				cardMap.put("tile_ordqty_label", ordqtyLabelStr);
				cardMap.put("tile_ordqty_value", ordqtyValueStr);

				cardMap.put("tile_supplier_label", supplierLabelStr);
				cardMap.put("tile_supplier_value", supplierValueStr);

				cardMap.put("tile_estimate_delivery_date_label", exDeDateLabelStr);
				cardMap.put("tile_estimate_delivery_date_value", exDeDateValueStr);

				cardMap.put("tile_order_details_button", orderDetBtnBool);
				cardMap.put("tile_storage_icon", storageIconBool);
				cardMap.put("tile_CII_icon", cIIdrugsIconBool);

				cardMap.put("tile_pancil_icon", pancilIconStr);
				cardMap.put("tile_trash_icon", trashIconStr);
				rv.add(cardMap);
			}

		}

		return rv;
	}

	private boolean testStorageIcon(WebElement orderline, int index) {
		WebElement storageIcon;
		boolean storageIconBool;
		try {
			storageIcon = orderline.findElement(By.xpath("./div[" + index + "]"));
			storageIconBool = true;

		} catch (Exception e) {
			storageIconBool = false;
		}
		return storageIconBool;
	}

	public List<Map<String, ?>> getCardsTiles() {
		List<Map<String, ?>> rv = new ArrayList<>();
		// cards display on two columns

		return rv;

	}

    public void clickShoppingIcon() {
    	shoppingIcon.click();
    }
 
	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(shoppingIcon)) {
			throw new Error();
		}
		try {
			//assertThat(signInButton.isDisplayed(), equalTo(Boolean.TRUE));
			ui().waitForCondition(driver, ExpectedConditions.visibilityOf(shoppingIcon),20);
		} catch (NoSuchElementException e) {
			Assert.fail("Page is not loaded!");
		}
	}

	@Override
	protected void load() {
		PageFactory.initElements(driver, this);
	}
}
