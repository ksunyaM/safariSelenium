/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.stockplus;

import static com.oneleo.test.automation.core.UIUtils.ui;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.LoadableComponent;

public class PurchaseOrderPage extends LoadableComponent<PurchaseOrderPage> {

    @FindBy(xpath = "//*[@id='mat-input-4']")
    private WebElement orderFilterInputBox;
    @FindBy(xpath = "//*[@id='supplier-group']")
    private WebElement supplierLabel;
    @FindBy(xpath = "//*[@id='product-group']")
    private WebElement productLabel;
    @FindBy(xpath = "//*[@id='po-table']/mat-row/mat-cell[3]")
    private WebElement supplierNameCell;
    @FindBy(xpath = "//*[@id='po-table']/mat-row/mat-cell[5]")
    private WebElement orderStatusCell;

    private WebDriver driver;
    private WebElement overlay;
    @FindBy(xpath = "//input[@id='mat-input-5']")
    private WebElement startDate;
    @FindBy(xpath = "//input[@id='mat-input-6']")
    private WebElement endDate;
    @FindBy(xpath = "//*[@id='pov-mat-card']/div[2]/div")
    private WebElement countsFound;

    @FindBy(xpath = "//*[@id='rx-inventory-main-container']/div/div[2]/rx-purchase-order-view/div/mat-toolbar[2]/div/mat-toolbar-row/button")
    private WebElement viewButton;

    @FindBy(id = "search-button")
    private WebElement refineButton;
    @FindBy(xpath = "//*[@id='form-wrapper']/form/button[2]")
    private WebElement clearButton;

    @FindBy(xpath = "//*[@id='mat-select-0']")
    private WebElement filterForStatus;

    @FindBy(xpath = "//*[@id='mat-option-0']/span[1]/mat-pseudo-checkbox")
    private WebElement filterStatusAll;

    @FindBy(xpath = "//*[@id='mat-option-1']/span[1]/mat-pseudo-checkbox")
    private WebElement filterStatusOpen;

    @FindBy(xpath = "//*[@id='mat-option-2']/span[1]/mat-pseudo-checkbox")
    private WebElement filterStatusPending;

    @FindBy(xpath = "//*[@id='mat-option-3']/span[1]/mat-pseudo-checkbox")
    private WebElement filterStatusConfirmed;

    @FindBy(xpath = "//*[@id='mat-option-4']/span[1]/mat-pseudo-checkbox")
    private WebElement filterStatusShipped;

    @FindBy(id = "mat-input-3")
    private WebElement deliveryDateFrom;
    @FindBy(id = "mat-input-4")
    private WebElement deliveryDateTo;
    
    @FindBy(css = "#navigation-panel > div > div.list > div.list-item.active > mat-icon")
    private WebElement orderItemsbutton;
	@FindBy(css = "#navigation-panel > div > div.list > div:nth-child(2) > mat-icon")
	private WebElement cartIcon;
    
    

    @FindBy(xpath = "//*[@id='mat-input-2']")
    private WebElement orderFilter;

    private String labelOrderPurchase = "//*[@id='rx-inventory-main-container']/div/div[2]/rx-purchase-order-view/div/mat-toolbar[1]/div/mat-toolbar-row/span";
    
    private static final String SUPPLIER = "supplier";
    private static final String PRODUCT = "product";
    private static final String NDC = "ndc";
    private static final String UPC = "upc";
    private static final String QUICKCODE = "quickcode";
    private static final String ITEMDESCRIPTION = "itemdescription";
    private static final String DELIVERYDATE_HEADER = "deliveryDate";
    private static final String ORDERSTATUS_HEADER = "orderStatus";

    public PurchaseOrderPage(WebDriver driver) {
        this.driver = driver;
    }
    
	public CartPage navigateToCartPage() {
		ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(cartIcon), 30);
		cartIcon.click();
		return new CartPage(driver);
	}

    public Boolean isDisplayedProductLink() {
        Boolean isPresentAndDisplayed = Boolean.FALSE;

        try {
            WebElement element = driver.findElement(By.id("main-left-sidenav-button-Drug"));
            if (element.isDisplayed()) {
                isPresentAndDisplayed = Boolean.TRUE;
            }
        } catch (NoSuchElementException nsee) {
            isPresentAndDisplayed = Boolean.FALSE;
        }

        return isPresentAndDisplayed;
    }

    public Boolean isDisplayedPatientItem() {
        Boolean isPresentAndDisplayed = Boolean.FALSE;

        try {
            WebElement element = driver.findElement(By.id("main-left-sidenav-button-Patient"));
            if (element.isDisplayed()) {
                isPresentAndDisplayed = Boolean.TRUE;
            }
        } catch (NoSuchElementException nsee) {
            isPresentAndDisplayed = Boolean.FALSE;
        }

        return isPresentAndDisplayed;
    }

    public String getSupplierNameCell() {
        waitElementIsAttached(supplierNameCell);
        return supplierNameCell.getText();
    }

    public String getOrderStatusCell() {
        return orderStatusCell.getText();
    }

    public String getOrderFilterInputBox() {
        WebElement orderFilterBox = driver.findElement(By.xpath("//*[@id='mat-input-4']"));
        return orderFilterBox.getAttribute("value");
    }

    public String getStartDate() {
        return startDate.getText();
    }

    public String getEndDate() {
        return endDate.getText();
    }

    public Boolean scrollbarIsPresent() {
        JavascriptExecutor javascript = (JavascriptExecutor) driver;
        return (Boolean) javascript.executeScript("return document.documentElement.scrollHeight>0;");
    }

    public void writeInOrderFilterBox(String input) {
        if (StringUtils.isNotBlank(input)) {
            orderFilterInputBox.sendKeys(input);
            ui().waitForCondition(driver, ExpectedConditions.textToBePresentInElementValue(this.orderFilterInputBox, input));
        }
    }

    public List<String> checkTenElementEachSection() {
        List<String> lista = new ArrayList<>();
        if (supplierLabel.isDisplayed() && productLabel.isDisplayed()) {
            List<WebElement> dropDownElements = driver.findElements(By.tagName("mat-optgroup"));
            for (WebElement element : dropDownElements) {

                lista.addAll(extractedString(element.getText().replace("\n", ",")));
            }
        }

        return lista;
    }

    private List<String> extractedString(String element) {
        List<String> resultList = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(element, ",");
        while (st.hasMoreTokens()) {
            resultList.add(st.nextToken());
        }

        return resultList;
    }

    public Boolean checkOneResultForEachSection() {
        Boolean checker = false;
        if (supplierLabel.isDisplayed() && productLabel.isDisplayed()) {
            List<WebElement> dropDownelements = driver.findElements(By.tagName("mat-option"));
            for (WebElement element : dropDownelements) {
                if (element.getText().startsWith("340")) {
                    checker = true;
                }
            }
        }
        return checker;

    }

    public Boolean onlySectionDisplayed(String label) {
        Boolean checker = false;
        if (SUPPLIER.equals(label)) {
            checker = supplierLabel.isDisplayed() && checkProductLabelNotDisplayed("//*[@id='product-group']");
        }
        if (PRODUCT.equals(label)) {
            checker = productLabel.isDisplayed() && checkProductLabelNotDisplayed("//*[@id='supplier-group']");
        }
        return checker;
    }

    private Boolean checkProductLabelNotDisplayed(String xPath) {
        Boolean check = false;

        try {
            driver.findElement(By.xpath(xPath));
        } catch (NoSuchElementException e) {
            check = true;
        }

        return check;
    }

    public void clickRowTable(int numRow) {
        WebElement row = driver.findElement(By.xpath("//*[@id='po-table']/mat-row[" + numRow + "]"));
        row.click();
    }

    public int checkOneSelectedRowTime() {
        return driver.findElements(By.cssSelector("#po-table > mat-row.mat-row.active")).size();
    }

    public void clickOnStatusBox() {
    	ui().waitForCondition(driver,ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@id='form-wrapper']/form/mat-form-field[2]/div/div[1]/div"))),30);
        WebElement statusBox = driver.findElement(By.xpath("//*[@id='form-wrapper']/form/mat-form-field[2]/div/div[1]/div"));
        statusBox.click();
    }

    public Boolean checkAllSelectedStatus() {
        List<WebElement> boxes = driver.findElements(By.tagName("mat-option"));
        Boolean check = Boolean.TRUE;
        for (WebElement statusBox : boxes) {
            if (statusBox.getCssValue("aria-selected") == "false") {
                check = Boolean.FALSE;
            }
        }
        return check;
    }

    public Boolean checkSelectedStatus(String status) {
        Boolean check = Boolean.FALSE;
        List<WebElement> boxes = driver.findElements(By.tagName("mat-option"));
        for (WebElement statusBox : boxes) {
            if (status.equals(statusBox.getAttribute("ng-reflect-value")) && statusBox.getCssValue("aria-selected") == "true") {
                check = Boolean.TRUE;
            }
        }
        return check;
    }

    public void clickStatusAll() {
        WebElement statusAll = driver.findElement(By.cssSelector("mat-option[ng-reflect-value='Select All']"));
        statusAll.click();
    }

    public void clickStatusBox(List<String> statusList) {
        clickStatusAll();

        for (String status : statusList) {
            WebElement box = driver.findElement(By.cssSelector("mat-option[ng-reflect-value='" + status + "']"));
            box.click();
        }

    }

    public void useRefineButtonForStatus() {
        WebElement button = driver.findElement(By.xpath("//*[@id='search-button']"));
        Actions actions = new Actions(driver);
        actions.click(button);
        actions.perform();
        button.click();
    }

    public void useRefineButton() {
        WebElement refineButton = driver.findElement(By.xpath("//*[@id='search-button']"));
        Actions actions = new Actions(driver);
        actions.click(refineButton);
        actions.perform();
        refineButton.click();
    }

    public void refineByHotkey() {
        WebElement refineButton = driver.findElement(By.xpath("//*[@id='search-button']"));
        ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(refineButton), 20);
        Actions keyAction = new Actions(driver);
        keyAction.keyDown(Keys.ALT).sendKeys("r").perform();
    }

    public List<String> checkSelectedStatus() {
        List<WebElement> rows = driver.findElements(By.xpath("//*[@id='po-table']/mat-row"));
        List<String> selectedStatus = new ArrayList<>();
        for (int i = 1; i <= rows.size(); i++) {

            try {
                if (rows.get(i - 1).findElements(By.tagName("mat-cell")).size() >= 5) {

                    WebElement orderStatus = driver.findElement(By.xpath("//*[@id='po-table']/mat-row[" + i + "]/mat-cell[5]"));
                    selectedStatus.add(orderStatus.getText());
                }
            } catch (StaleElementReferenceException | NoSuchElementException ex) {
                return selectedStatus;
            }

        }

        return selectedStatus;
    }

    public void selectSupplierSmartSearchResult() {
        driver.findElement(By.xpath("//*[@id='mat-option-5']")).click();
    }

    public void selectResultFromSmartSearchResult(String elementType) {
        List<WebElement> elementList = driver.findElements(By.tagName("mat-option"));
        for (WebElement element : elementList) {
            if (NDC.equals(elementType) && element.getText().contains("-")) {
                element.click();
                break;
            }
            if (UPC.equals(elementType) && !element.getText().contains("-")) {
                element.click();
                break;
            }
            if (QUICKCODE.equals(elementType) && element.getText().length() == 8) {
                element.click();
                break;
            }
            if (ITEMDESCRIPTION.equals(elementType) && element.getText().toLowerCase().contains(getOrderFilterInputBox().toLowerCase())) {
                element.click();
                break;
            }

        }
    }

    public Boolean checkIfOverlayIsDisplayed() {
        FluentWait<WebDriver> fWait = new FluentWait<WebDriver>(driver).withTimeout(5, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class, TimeoutException.class).ignoring(StaleElementReferenceException.class);

        overlay = driver.findElement(By.xpath("//*[@id='cdk-overlay-2']/mat-dialog-container"));
        try {

            fWait.until(ExpectedConditions.visibilityOf(overlay));

        } catch (NoSuchElementException e) {

            return false;

        }
        return overlay.isDisplayed();

    }

    public String getMessageOnOverlay() {
        return overlay.getText();
    }

    public void setStartDate_EndDate(String dateFrom, String dateTo) {

        if (StringUtils.isNotBlank(dateFrom)) {
            startDate = driver.findElement(By.cssSelector("#mat-input-3"));
            new Actions(driver).moveToElement(startDate).click().perform();
            startDate.sendKeys(Keys.CONTROL + "a");
            startDate.sendKeys(Keys.DELETE);

            startDate.sendKeys(dateFrom);

            endDate = driver.findElement(By.cssSelector("#mat-input-4"));

            endDate.sendKeys("");

        }

        if (StringUtils.isNotBlank(dateTo)) {
            endDate.sendKeys(Keys.CONTROL + "a");
            endDate.sendKeys(Keys.DELETE);

            endDate.sendKeys(dateTo);

            useRefineButtonForStatus();

        }

    }

    public void setFilters(String dateFrom, String dateTo, String status, String orderFilter) {
        if (StringUtils.isNotBlank(status)) {
            List<String> statusList = new ArrayList<>();
            statusList.add(status);
            clickOnStatusBox();
            clickStatusBox(statusList);
        }
        if (StringUtils.isNotBlank(orderFilter)) {
            WebElement orderFilterBox = driver.findElement(By.xpath("//*[@id='mat-input-2']"));
            orderFilterBox.sendKeys(orderFilter);
        }
        if (StringUtils.isNotBlank(dateFrom)) {
            startDate = driver.findElement(By.cssSelector("#mat-input-3"));
            new Actions(driver).moveToElement(startDate).click().perform();
            startDate.sendKeys(Keys.CONTROL + "a");
            startDate.sendKeys(Keys.DELETE);

            startDate.sendKeys(dateFrom);

            endDate = driver.findElement(By.cssSelector("#mat-input-4"));

            endDate.sendKeys("");

        }

        if (StringUtils.isNotBlank(dateTo)) {
            endDate.sendKeys(Keys.CONTROL + "a");
            endDate.sendKeys(Keys.DELETE);

            endDate.sendKeys(dateTo);

            useRefineButtonForStatus();

        }

    }

    public void clickClearButton() {
        driver.findElement(By.xpath("//*[@id='form-wrapper']/form/button[2]")).click();
    }

    public String getFooterMessage() {
        WebElement footer = driver.findElement(By.xpath("//*[@id='no-results-msg']"));
        return footer.getText();
    }

    public String getSupplierNameCellForResultSmartSearch() {
        ui().waitForCondition(driver, ExpectedConditions.textToBePresentInElement(countsFound, "1"), 20);
        return supplierNameCell.getText();
    }

    public String getCountsFound() {
        WebElement countsFound = driver.findElement(By.xpath("//*[@id='pov-mat-card']/div[2]/div"));
        ui().waitForCondition(driver, ExpectedConditions.visibilityOf(countsFound), 20);
        return countsFound.getText();
    }

    private void waitElementIsAttached(WebElement element) {
        boolean breakIt = true;
        while (true) {
            breakIt = true;
            try {
                element.getText();
            } catch (Exception e) {
                if (e.getMessage().contains("element is not attached to the page document")) {
                    breakIt = false;
                }
            }
            if (breakIt) {
                break;
            }

        }
    }

    public void pressTableHeader(String header) {
        if (DELIVERYDATE_HEADER.equals(header)) {
            driver.findElement(By.xpath("//*[@id='po-table']/mat-header-row/mat-header-cell[2]")).click();
        }
        if (ORDERSTATUS_HEADER.equals(header)) {
            driver.findElement(By.xpath("//*[@id='po-table']/mat-header-row/mat-header-cell[5]")).click();
        }
    }

    private Integer getRowsNumber() {
        String countsElements = driver.findElement(By.cssSelector("#pov-mat-card > div.margin-top.margin-bottom.margin-left-3x > div")).getText();
        String itemsNumber = countsElements.substring(0, 2).trim();
        return Integer.valueOf(itemsNumber);
    }

    public List<String> getHeaderSorting(String header) {
        int matCell = 0;
        if (DELIVERYDATE_HEADER.equals(header)) {
            matCell = 2;
        }
        Integer tableItems = getRowsNumber();
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= tableItems; i++) {
            String element = driver.findElement(By.xpath("//*[@id='po-table']/mat-row[" + i + "]/mat-cell[" + matCell + "]")).getText();
            list.add(element);
        }

        return list;

    }

    public List<String[]> getTableRows() {
        List<String[]> list = new ArrayList<>();
        List<WebElement> rows = driver.findElements(By.tagName("mat-row"));
        for (WebElement row : rows) {
            list.add(convert(row.getText().replace("\n", ",")));
        }

        return list;
    }

    public Map<String, List<String>> getTableResult() {
        ui().waitForCondition(driver, ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@id='po-table']/mat-row"), 0), 60);
        List<WebElement> rows = driver.findElements(By.xpath("//*[@id='po-table']/mat-row"));
        Map<String, List<String>> result = new LinkedHashMap<>();
        List<String> selectedStatus = new ArrayList<>();
        List<String> orderNumbers = new ArrayList<>();
        List<String> deliveryDates = new ArrayList<>();
        List<String> suppliers = new ArrayList<>();
        for (int i = 1; i <= rows.size(); i++) {

            try {
                if (rows.get(i - 1).findElements(By.tagName("mat-cell")).size() >= 5) {
                	ui().waitForCondition(driver, ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@id='po-table']/mat-row[" + i + "]/mat-cell[1]"), 0), 60);
                    WebElement orderNumber = driver.findElement(By.xpath("//*[@id='po-table']/mat-row[" + i + "]/mat-cell[1]"));
                    orderNumbers.add(orderNumber.getText());
                    ui().waitForCondition(driver, ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@id='po-table']/mat-row[" + i + "]/mat-cell[2]"), 0), 60);
                    WebElement deliveryDate = driver.findElement(By.xpath("//*[@id='po-table']/mat-row[" + i + "]/mat-cell[2]"));
                    deliveryDates.add(deliveryDate.getText());
                    ui().waitForCondition(driver, ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@id='po-table']/mat-row[" + i + "]/mat-cell[3]"), 0), 60);
                    WebElement supplier = driver.findElement(By.xpath("//*[@id='po-table']/mat-row[" + i + "]/mat-cell[3]"));
                    suppliers.add(supplier.getText());
                    ui().waitForCondition(driver, ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@id='po-table']/mat-row[" + i + "]/mat-cell[5]"), 0), 60);
                    WebElement orderStatus = driver.findElement(By.xpath("//*[@id='po-table']/mat-row[" + i + "]/mat-cell[5]"));
                    selectedStatus.add(orderStatus.getText());
                }
            } catch (StaleElementReferenceException | NoSuchElementException ex) {
                return result;
            }

        }
        result.put("OrderNumbers", orderNumbers);
        result.put("DeliveryDate", deliveryDates);
        result.put("Supplier", suppliers);
        result.put("Status", selectedStatus);
        return result;
    }

    static String[] convert(String element) {
        StringTokenizer st = new StringTokenizer(element, ",");
        String[] array = new String[st.countTokens()];
        int iterator = st.countTokens();
        while (st.hasMoreTokens()) {
            array[iterator - st.countTokens()] = st.nextToken();
        }

        return array;
    }



    public OrderDetailsPage navigateToOrderDetails() {
        ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(viewButton));
        assertThat(viewButton.isDisplayed(), equalTo(Boolean.TRUE));
        assertThat(viewButton.isEnabled(), equalTo(Boolean.TRUE));
        viewButton.click();
        return new OrderDetailsPage(driver);
    }

    public void searchSingleOrderByFilterDeliveryDate(String deliveryDate) throws Throwable {
        this.deliveryDateFrom.clear();
        this.deliveryDateTo.clear();

        if (StringUtils.isNotBlank(deliveryDate)) {
            this.deliveryDateFrom.sendKeys(deliveryDate);
            ui().waitForCondition(driver, ExpectedConditions.textToBePresentInElementValue(this.deliveryDateFrom, deliveryDate));
            this.deliveryDateTo.sendKeys(deliveryDate);
            ui().waitForCondition(driver, ExpectedConditions.textToBePresentInElementValue(this.deliveryDateTo, deliveryDate));
        } else {
            throw new java.lang.Exception("deliveryDate missing");
        }

        ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(refineButton), 10);
        refineButton.click();

    }

    /**
     * 
     * @param orderNumber
     * @param product
     * @param deliveryDate
     * @param Supplier
     * @param orderStatus
     * @return PurchaseOrderManagementPage
     * @throws Throwable
     */
    public void searchSingleOrderByFilterStatus(OrderStatus orderStatus) throws Throwable {

        filterForStatus.click();
        ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(filterStatusAll), 10);
        filterStatusAll.click();

        switch (orderStatus) {
        case OPEN:
            if (filterStatusOpen.isDisplayed())
                filterStatusOpen.click();
            else
                throw new java.lang.Exception("Filter status Open not displayed ");
            break;

        case PENDING:
            if (filterStatusOpen.isDisplayed())
                filterStatusPending.click();
            else
                throw new java.lang.Exception("Filter status Pending not displayed ");
            break;

        case CONFIRMED:
            if (filterStatusOpen.isDisplayed())
                filterStatusConfirmed.click();
            else
                throw new java.lang.Exception("Filter status Confirmed not displayed ");
            break;

        case SHIPPED:
            if (filterStatusOpen.isDisplayed())
                filterStatusShipped.click();
            else
                throw new java.lang.Exception("Filter status Shipped not displayed ");
            break;

        default:

            throw new java.lang.Exception("orderStatus incorrect");
        }

        refineByHotkey();

    }
    
    
	private boolean existsElement(String xpath) {

		try {
			ui().waitForCondition(driver,ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)),10);
	        driver.findElement(By.xpath(xpath));   
		}
	     catch (Throwable e) 
		{
	        return false;
	    } 
	    return true;
	}
    
	public String getTextFromWebElement(String xpath)
	{
		String retStr="";
		
		if (existsElement(xpath))
		{
			retStr=driver.findElement(By.xpath(xpath)).getText();
		}
		
		return retStr;
	}
	
	public String getContentTextLabelPurchaseOrderPage()
	{
		return getTextFromWebElement("//*[@id='rx-inventory-main-container']/div/div[2]/rx-purchase-order-details/div/mat-toolbar/div/mat-toolbar-row/span");
	}

    public Boolean elementFocus() {
        ui().waitForCondition(driver, ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='po-table']/mat-row[1]")), 20);
        WebElement row = driver.findElement(By.xpath("//*[@id='po-table']/mat-row[1]"));
        return "mat-row active" == row.getAttribute("class");
    }
    
    public Boolean checkDisabledStatus(String status) {
		Boolean check = Boolean.FALSE;
		List<WebElement> boxes = driver.findElements(By.tagName("mat-option"));
		for (WebElement statusBox : boxes) {
			if (status.equals(statusBox.getAttribute("ng-reflect-value")) && statusBox.getCssValue("aria-disabled") == "true") {
				check = Boolean.TRUE;
			}
		}
		return check;
	}
    

	public List<String> getDisabledStatuses(List<String> orderStatusList) {
		List<String> statusesDisabled = new ArrayList<String>();
		for(String status : orderStatusList){
			WebElement box = this.driver.findElement(By.cssSelector("mat-option[ng-reflect-value=\'" + status + "\']"));
			ui().waitForCondition(driver, ExpectedConditions.visibilityOfElementLocated(By.cssSelector("mat-option[ng-reflect-value=\'" + status + "\']")), 60);
			if (box.getAttribute("aria-disabled").equals("true")) {
				statusesDisabled.add(status);
			}
		}
		return statusesDisabled;
	}
	
    public OrderItemsPage navigateToOrderItemsPage() throws Exception {
        orderItemsbutton.click();
        return new OrderItemsPage(driver);
    }

	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(refineButton)) {
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
