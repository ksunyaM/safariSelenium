/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.orders;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;


public class PatientPage extends LoadableComponent<PatientPage>  {
	
	@FindBy(xpath = "//button/span [contains(text(),'ADD RX/SERVICE')]")
	private WebElement addrxservicebutton;
	@FindBy(xpath = "//mat-icon[contains(text(),'arrow_back')]")
	private WebElement backbutton;
	@FindBy(xpath = "//div [@ng-reflect-class-base='total-ready text-heading']")
	private WebElement totalLabel;
	@FindBy(xpath = "//button[contains(@class,'see-more')]")
	private WebElement moreButton;
	
	//Menu items Add Rx/ Service
	@FindBy(xpath = "//button [contains(text(),'New Rx')]")
	private WebElement newrxitem;
	@FindBy(xpath = "//button [contains(text(),'Refill')]")
	private WebElement refilltem;
	@FindBy(xpath = "//button [contains(text(),'Immunization')]")
	private WebElement immunizationitem;
	@FindBy(xpath = "//button [contains(text(),'Consultation')]")
	private WebElement consultationitem;
	@FindBy(xpath = "//button [contains(text(),'Call-in')]")
	private WebElement callinitem;
	@FindBy(xpath = "//button [contains(text(),'Transfer')]")
	private WebElement transferitem;
	//Score cards
	@FindBy(xpath = "//button [@ng-reflect-class-base='scorecard']")
	private List<WebElement> scoreCardList;
	//Products
	@FindBy(xpath = "//mat-card [contains(@class,'mat-card')]")
	private List<WebElement> allproductsList;
	@FindBy(xpath = "//mat-card [contains(@id,'progress-orders')]")
	private List<WebElement> inprogressproductsList;
	@FindBy(xpath = "//mat-card [contains(@id,'ready-orders')]")
	private List<WebElement> readyproductsList;
	@FindBy(xpath = "//mat-card [contains(@id,'delayed-orders')]")
	private List<WebElement> delayedproductsList;
	@FindBy(xpath = "//mat-card [contains(@id,'delivered-orders')]")
	private List<WebElement> deliveredproductsList;
	//Sub-xpath
	String statusXpath = ".//div/div[1]/div";
	String productnameXpath = ".//div/div[2]/div[1]/p[1]";
	String totalAmountXpath = ".//div/div[2]/div[2]/p[2]";
    String promiseTimeXpath = ".//div/div[2]/div[2]/p[1]";
    String rxidXpath = ".//div/div[2]/div[1]/p[2]/span";
    String kebabMenu = ".//button [@class='icon-button']";
    //Global variable
    private WebDriver driver;
    Date defaultDate;
    String prefixPromisedTime = "Promise time:";
    WebDriverWait wait; 
    //Pop up elements
    @FindBy(xpath = "//button/span [contains(text(),'Delete')]")
	private WebElement cancelDeletionButton;
	@FindBy(xpath = "//button/span [contains(text(),'Cancel')]")
	private WebElement submitDeletionButton;
	@FindBy(xpath = ".//button [normalize-space()='Delete']")
	private WebElement deleteOption;
	//Internal class is needed for sorting of page order items
	 private class PatientOrderItem {
	        String group;
	        String productname;
	        Date promisedtime;

	        private PatientOrderItem(String group, String productname, Date promisedtime) {
	            this.group = group;
	            this.productname = productname;
	            this.promisedtime = promisedtime;
	        }

	        @Override
	        public String toString() {
	            return "Patient{" +
	                    "group='" + group + '\'' +
	                    ", productname='" + productname + '\'' +
	                    ", promisedtime='" + promisedtime.toString() + '\'' +
	                    '}';
	        }

	        public String getGroup() {
	            return group;
	        }

	        public void setGroup(String group) {
	            this.group = group;
	        }

	        public String getProductname() {
	            return productname;
	        }

	        public void setpPoductName(String productname) {
	            this.productname = productname;
	        }

	        public Date getPromisedTime() {
	            return promisedtime;
	        }

	        public void setPromisedTime(Date promisedtime) {
	            this.promisedtime = promisedtime;
	        }
	    }
	

	


	public PatientPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, 10);
		defaultDate = new Date();
	}
	
	public void clickOnBackButton() {
		backbutton.click();
	}
	

	public boolean isScoreCardAbsent() {
		return scoreCardList.isEmpty();
	}
	
	private Date convertPromiseTimeToDate(String promisetime) {
		DateFormat format = new SimpleDateFormat("mm/dd/yy - hh:mm aa");
  	   Date date = null;
	try {
		date = format.parse(promisetime);
	} catch (ParseException e) {
		e.printStackTrace();
		System.out.println("Format of input Promised Time is wrong");
	}
  	   return date;
	}
	
	public boolean isSortedbyGroup(Map<String,Integer> productvsorderMap) {
		ArrayList<Integer> listOfGroup = new ArrayList<Integer>();
		for(WebElement orderitem:allproductsList) {
			String status = orderitem.findElement(By.xpath(statusXpath)).getText();
			int orderNumber = productvsorderMap.get(status);
			if(!listOfGroup.contains(orderNumber)) {
				listOfGroup.add(orderNumber);
			}
		}
			if(listOfGroup == null || listOfGroup.isEmpty())
		        return false;

		    if(listOfGroup.size() == 1)  
		        return true;

		    for(int i=1; i<listOfGroup.size();i++)
		    {
		        if(listOfGroup.get(i).compareTo(listOfGroup.get(i-1)) < 0 )
		            return false;
		    }

		    return true;
		
	}
	
	public boolean isDelayedListSorted() {
		ArrayList<PatientOrderItem> listOfPatientOrderItem = new ArrayList<PatientOrderItem>();
		for(WebElement delayeditem:delayedproductsList) {
			String group = delayeditem.findElement(By.xpath(statusXpath)).getText();
			String productname = delayeditem.findElement(By.xpath(productnameXpath)).getText();
			Date promisedtime = convertPromiseTimeToDate(delayeditem
					.findElement(By.xpath(promiseTimeXpath)).getText().replace(prefixPromisedTime, "").trim());
			listOfPatientOrderItem.add(new PatientOrderItem(group, productname, promisedtime));
		}
		List<PatientOrderItem> listOfPatientOrderItemSorted = listOfPatientOrderItem
				.stream()
				.sorted(Comparator.comparing(PatientOrderItem::getProductname)
						.thenComparing(PatientOrderItem::getPromisedTime)).collect(Collectors.toList());
		
		if (listOfPatientOrderItem.equals(listOfPatientOrderItemSorted)){
			return true;
		}
		else return false;
	}
	
	public boolean isReadyListSorted() {
		ArrayList<PatientOrderItem> listOfPatientOrderItem = new ArrayList<PatientOrderItem>();
		for(WebElement readyitem:readyproductsList) {
			String group = readyitem.findElement(By.xpath(statusXpath)).getText();
			String productname = readyitem.findElement(By.xpath(productnameXpath)).getText();
			listOfPatientOrderItem.add(new PatientOrderItem(group, productname, defaultDate));
		}
		List<PatientOrderItem> listOfPatientOrderItemSorted = listOfPatientOrderItem
				.stream()
				.sorted(Comparator.comparing(PatientOrderItem::getProductname)).collect(Collectors.toList());
		
		if (listOfPatientOrderItem.equals(listOfPatientOrderItemSorted)){
			return true;
		}
		else return false;	
	}
	
	public boolean isDeliveredListSorted() {
		ArrayList<PatientOrderItem> listOfPatientOrderItem = new ArrayList<PatientOrderItem>();
		for(WebElement delivereditem:deliveredproductsList) {
			String group = delivereditem.findElement(By.xpath(statusXpath)).getText();
			String productname = delivereditem.findElement(By.xpath(productnameXpath)).getText();
			listOfPatientOrderItem.add(new PatientOrderItem(group, productname, defaultDate));
		}
		List<PatientOrderItem> listOfPatientOrderItemSorted = listOfPatientOrderItem
				.stream()
				.sorted(Comparator.comparing(PatientOrderItem::getProductname)).collect(Collectors.toList());
		
		if (listOfPatientOrderItem.equals(listOfPatientOrderItemSorted)){
			return true;
		}
		else return false;	
	}
	
	public boolean isInProgressListSorted() {
		ArrayList<PatientOrderItem> listOfPatientOrderItem = new ArrayList<PatientOrderItem>();
		HashMap<String,String> inprogressStatusMap = new HashMap<String,String>();
		inprogressStatusMap.put("reviewed", "1"); //Map with ordering rules for in progress set
		inprogressStatusMap.put("printed", "2");
		inprogressStatusMap.put("filled", "3");
		inprogressStatusMap.put("entered", "0");
		for(WebElement inprogressitem:inprogressproductsList) {
			String group = inprogressStatusMap.get(inprogressitem.findElement(By.xpath(statusXpath)).getText());
			String productname = inprogressitem.findElement(By.xpath(productnameXpath)).getText();
			Date promisedtime = convertPromiseTimeToDate(inprogressitem
					.findElement(By.xpath(promiseTimeXpath)).getText().replace(prefixPromisedTime, "").trim());
			listOfPatientOrderItem.add(new PatientOrderItem(group, productname, promisedtime));
		}
		Map<String, List<PatientOrderItem>> listOfPatientOrderItemGrouped = listOfPatientOrderItem.stream() //Grouping of data by Status
                .collect(Collectors.groupingBy(PatientOrderItem::getGroup, Collectors.toList()));
		
		List<PatientOrderItem> listOfPatientOrderItemSorted = listOfPatientOrderItemGrouped.entrySet().stream() //Sorting by ProductName and Promised time ASC
                .flatMap(e -> e.getValue().stream().sorted(Comparator.comparing(PatientOrderItem::getProductname)
                        .thenComparing(PatientOrderItem::getPromisedTime))).collect(Collectors.toList());
		
		if (listOfPatientOrderItem.equals(listOfPatientOrderItemSorted)){
			return true;
		}
		else return false;
	}
	
	
	public void clickOnAddReFillMenu() {
		addrxservicebutton.click();
	}
	
	public boolean isAllItemfOfRefillMenuExists() {
		if(newrxitem.isDisplayed()&&refilltem.isDisplayed()&&immunizationitem.isDisplayed()
				&&consultationitem.isDisplayed()&&callinitem.isDisplayed()&&transferitem.isDisplayed() ) {
			return true;
			
		}
		else return false;
	}
	
	public boolean isTotalAmountEmpty() {
		return totalLabel == null;	
	}
	
	private boolean isNonDeliveredReadyItemContainAllParams(List<WebElement> itemList) {
		return itemList.get(0).findElement(By.xpath(statusXpath)).getText()!="" //status value
				&&itemList.get(0).findElement(By.xpath(productnameXpath)).getText()!="" //product name
						&&itemList.get(0).findElement(By.xpath(totalAmountXpath)).getText()!="" //total amount
								&&itemList.get(0).findElement(By.xpath(promiseTimeXpath)).getText()!="" //promised time
										&&itemList.get(0).findElement(By.xpath(rxidXpath)).getText()!="" ; //RX id
	}
   private boolean isDeliveredReadyItemContainAllParams(List<WebElement> itemList) {
	return itemList.get(0).findElement(By.xpath(statusXpath)).getText()!="" //status value
			&&itemList.get(0).findElement(By.xpath(productnameXpath)).getText()!="" //product name
					&&itemList.get(0).findElement(By.xpath(totalAmountXpath)).getText()!="" //total amount
									&&itemList.get(0).findElement(By.xpath(rxidXpath)).getText()!="" ; //RX id
	}
   
   public boolean isInProgressItemValid() {
	 return isNonDeliveredReadyItemContainAllParams(inprogressproductsList);
   } 
   
   public boolean isDeliveredItemValid() {
	   return isDeliveredReadyItemContainAllParams(deliveredproductsList);
   } 

   public boolean isDelayedItemValid() {
		 return isNonDeliveredReadyItemContainAllParams(delayedproductsList);
   } 

   public boolean isReadyItemValid() {
		return isDeliveredReadyItemContainAllParams(readyproductsList); 
   } 



   public DetailsPage goToDetailsPage() {
		wait.until(ExpectedConditions.elementToBeClickable(moreButton));
		moreButton.click();
		return new DetailsPage(driver);
	}
   
   public boolean isListOfOptionsInKebabMenuCorrect(List<String> menuoptionList,String statusOfOrder) {
	   boolean result =false;
	   for(WebElement item:allproductsList) {
		   if(item.findElement(By.xpath(statusXpath)).getText().toUpperCase().equals(statusOfOrder.toUpperCase())) {
			   item.findElement(By.xpath(kebabMenu)).click();
			   for(String option:menuoptionList) {
				   try {
				   driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
				   result = driver.findElement(By.xpath(".//button [normalize-space()='" + option + "']")).isDisplayed();
				   }
				   catch (NoSuchElementException exception) {
					   System.out.println(option + "webelement was not found");
					   result = false;
				   }
			   }
			   break;
		   }
	   }
	return result; 
   }
   
   public void cancelDeletionOfOrder() {
	   allproductsList.get(0).findElement(By.xpath(kebabMenu)).click();
	   deleteOption.click();
	   wait.until(ExpectedConditions.elementToBeClickable(cancelDeletionButton));
	   cancelDeletionButton.click();
   }
   public void submitDeletionOfOrder() {
	   allproductsList.get(0).findElement(By.xpath(kebabMenu)).click();
	   deleteOption.click();
	   wait.until(ExpectedConditions.elementToBeClickable(submitDeletionButton));
	   submitDeletionButton.click();
   }
   
   public boolean isSetOfOrdersChangedAfterDeletion() {
	   driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	   List<WebElement> listOfOrdersAfterDeletion = driver.findElements(By.xpath("//mat-card [contains(@class,'mat-card')]"));
	   return listOfOrdersAfterDeletion.size()<allproductsList.size();
   }
   
   public boolean constains2DigitsAfterDot() {
	   String totalAmount = inprogressproductsList.get(0).findElement(By.xpath(totalAmountXpath)).getText();
	   int dotIndex = 0;
	   for (int i = 0; i < totalAmount.length(); i++) {
		   if (totalAmount.charAt(i) == '.') {
			   dotIndex = i;
			   break;
		   }
	   }
	   String afterDot = totalAmount.substring(dotIndex + 1, totalAmount.length());
	   return afterDot.length() == 2;
   }

	
	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(addrxservicebutton)) {
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
