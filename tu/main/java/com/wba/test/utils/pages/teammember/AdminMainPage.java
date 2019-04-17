/*******************************************************************************
 * Copyright 2019 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.teammember;

import java.util.Objects;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

public class AdminMainPage extends LoadableComponent<AdminMainPage> {

    @FindBy(xpath = "/html/body/app-root/app-nav/rx-uikit-app-bar/div/button")
    private WebElement menuIcon;
    @FindBy(xpath = "//button[@aria-label='Team members']")
    private WebElement teamMemberIcon;

    private final String TEAM_MEMBER_ICON_XPATH = "//button[@aria-label='Team members']";
    private WebDriver driver;

    public AdminMainPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isTeamMemberIconDisplayed() {
        CommonUtils.fluentWaitForObject(By.xpath(TEAM_MEMBER_ICON_XPATH));
        return this.teamMemberIcon.isDisplayed();
    }

    public void clickOnMenuIcon() {
        this.menuIcon.click();
    }

    public TeamMembersPage clickOnTeamMemberIcon() {
        this.teamMemberIcon.click();
        return new TeamMembersPage(driver);
    }

    @Override
    protected void isLoaded() throws Error {
        if (Objects.isNull(teamMemberIcon)) {
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
