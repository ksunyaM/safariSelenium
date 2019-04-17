/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.uaam;

import static com.oneleo.test.automation.core.UIUtils.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;

public class HelpContentSection extends LoadableComponent<HelpContentSection>  {

	@FindBy(xpath = "/html/body/app-root/app-nav/rx-uikit-app-bar/div/div/button/span/mat-icon")
	private WebElement helpDrawerToggleButton;
	@FindBy(xpath = "/html/body/app-root/app-nav/rx-uikit-contextual-drawer/div/div[1]/div/button/span/mat-icon")
	private WebElement closeHelpContent;
	@FindBy(xpath = "/html/body/app-root/app-nav/rx-uikit-contextual-drawer/div/div[1]/button/span/mat-icon")
	private WebElement backHelpContent;
	@FindBy(id = "caretHelpContent")	
	private WebElement caretHelpContent;
	@FindBy(xpath = "/html/body/app-root/app-nav/rx-uikit-contextual-drawer/div/div[1]/span")
	private WebElement titleHelpContent;
	@FindBy(xpath = "/html/body/app-root/app-nav/rx-uikit-contextual-drawer/div/div[2]/rx-uikit-cd-main/div/rx-lib-help-guide/div/div[1]")
	private WebElement titleGuide;
	@FindBy(xpath = "/html/body/app-root/app-nav/rx-uikit-contextual-drawer/div/div[2]/rx-uikit-cd-main/div/rx-lib-help-guide/div/div[2]")
	private WebElement contentGuide;
	@FindBy(css = "div [class='text-heading']")
	private WebElement titleArticle;
	@FindBy(xpath = "/html/body/app-root/app-nav/rx-uikit-contextual-drawer/div/div[2]/rx-uikit-cd-main/div/rx-lib-help-guide/div/ul")
	private WebElement listGuidesArticle;
	@FindBy(xpath = "/html/body/app-root/app-nav/rx-uikit-contextual-drawer/div/div[2]/rx-uikit-cd-page/div/rx-lib-help-article/mat-accordion/div")
	private WebElement relatedArticles;
	@FindBy(xpath = "/html/body/app-root/app-nav/rx-uikit-contextual-drawer/div/div[2]/rx-uikit-cd-page/div/rx-lib-help-article/div/mat-accordion/mat-expansion-panel")
	private WebElement matPanelFirstRelatedArticle;
	@FindBy(xpath = "//*[@id='cdk-accordion-child-0']/div/div[1]")
	private WebElement titleFirstGuideOfRelatedArticle;

	private WebDriver driver;

	public HelpContentSection(WebDriver driver) {
		this.driver = driver;
	}

	public void clickOnHelpDrawerToggleButton() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(helpDrawerToggleButton));
		helpDrawerToggleButton.click();
	}

	public void clickOnCloseHelpContent() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOf(closeHelpContent));
		closeHelpContent.click();
	}

	public void clickOnFirstAticle() {
		try {
			WebElement firstArticleTitle = driver.findElement(By.xpath("/html/body/app-root/app-nav/rx-uikit-contextual-drawer/div/div[2]/rx-uikit-cd-main/div/rx-lib-help-guide/ul/li"));
			ui().waitForCondition(driver, ExpectedConditions.visibilityOf(firstArticleTitle));
			firstArticleTitle.click();
		} catch (NoSuchElementException e) {
		}
	}

	public void clickOnBackButton() {
		backHelpContent.click();
	}

	public Boolean verifyTitleHelpContent(String title) {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/app-root/app-nav/rx-uikit-contextual-drawer/div/div[1]/span")), 5);
		return titleHelpContent.getText().equals(title);
	}

	public Boolean isDisplayedCloseHelpContent() {
		ui().waitForCondition(driver, ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/app-root/app-nav/rx-uikit-contextual-drawer/div/div[1]/div/button/span/mat-icon")), 5);
		return closeHelpContent.isDisplayed();
	}

	public Boolean isDisplayedBackHelpContent() {
		return backHelpContent.isDisplayed();
	}

	public Boolean isDisplayedHelperDrawer() {
		synchronized (this) {
			try {
				wait(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return helpDrawerToggleButton.isDisplayed();
	}

	public Boolean isDisplayedGuideTitle() {
		synchronized (this) {
			try {
				wait(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return titleGuide.isDisplayed();
	}

	public String getGuideTitle() {
		return titleGuide.getText();
	}

	public String getGuideContent() {
		return contentGuide.getText();
	}

	public int numberElementsOfList(){
		return listGuidesArticle.findElements(By.cssSelector("li")).size();
	}

	public Boolean verifyIfTitleIsPresent(String title){
		Boolean sameTitle = Boolean.FALSE;
		List<WebElement> listArticles = listGuidesArticle.findElements(By.cssSelector("li"));
		for (WebElement webElement : listArticles) {
			if(webElement.getText().equals(title)){
				sameTitle = Boolean.TRUE;
				break;
			}
		}
		return sameTitle;
	}

	public void clickOnTitle(String title){
		List<WebElement> listArticles = listGuidesArticle.findElements(By.cssSelector("li"));
		for (WebElement webElement : listArticles) {
			if(webElement.getText().equals(title)){
				webElement.click();
				break;
			}
		}
	}

	public void clickOnFirstMainArticle(){
		List<WebElement> listArticles = listGuidesArticle.findElements(By.cssSelector("li"));
		for (WebElement webElement : listArticles) {
			webElement.click();
			break;
		}
	}
	
	public void clickOnYesFeedbackButtonOfFirstMainArticle() {
		List<WebElement> listButton = listGuidesArticle.findElements(By.cssSelector("rx-uikit-feedback[class='ng-star-inserted'] div button[class='mat-button'] span[class='mat-button-wrapper']"));
		
	}

	public Boolean isDisplayedRelatedArticles(){
		synchronized (this) {
			try {
				wait(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return relatedArticles.isDisplayed();
	}

	public Boolean isDisplayedMatPanelFirstRelatedArticle(){
		synchronized (this) {
			try {
				wait(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return matPanelFirstRelatedArticle.isDisplayed();
	}

	public void clickOnFirstRelatedArticle(){
		matPanelFirstRelatedArticle.click();
	}

	public Boolean isDisplayedTitleFirstGuideOfRelatedArticle(){
		synchronized (this) {
			try {
				wait(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return Boolean.valueOf(titleFirstGuideOfRelatedArticle.getAttribute("aria-expanded"));
	}

	public String getContentFirstArticleGuide(){
		WebElement contentArticle = driver.findElement(By.cssSelector("div[class='ng-star-inserted'] div[class= 'text-body'] p u i"));
		return contentArticle.getText();
	}
	@Override
	protected void isLoaded() throws Error {
		if (Objects.isNull(helpDrawerToggleButton)) {
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

