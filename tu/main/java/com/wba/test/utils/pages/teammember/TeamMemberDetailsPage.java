/*******************************************************************************
 * Copyright 2019 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.teammember;

import com.wba.test.utils.StatefulSingleton;
import org.junit.Assert;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.util.Objects;

public class TeamMemberDetailsPage extends LoadableComponent<TeamMemberDetailsPage> {

    @FindBy(xpath = "//span[contains(., 'Team member details')]")
    private WebElement teamMemberDetailsHeader;
    @FindBy(xpath = "//div[@class='profile-name']")
    private WebElement teamMemberName;
    //@FindBy(xpath = "//div[contains(text(), 'LICENSES & CERTIFICATIONS']")
    
    @FindBy(xpath = "//*[@id=\"mat-tab-label-1-1\"]/div")
    private WebElement systemAccessTab;
    
    private WebDriver driver;
    
    
    public SystemAccessTab clickSystemAccessTab() {
        systemAccessTab.click();
        return new SystemAccessTab(driver);
    }

    public TeamMemberDetailsPage(WebDriver driver) {
        this.driver = driver;
        StatefulSingleton.instance().setDriver(driver);
    }

    public boolean isHeaderDisplayed() {
        return teamMemberDetailsHeader.isDisplayed();
    }

    public boolean isTeamMemberNameDisplayed() {
        return teamMemberName.isDisplayed();
    }

    public String getHeaderText() {
        return teamMemberDetailsHeader.getText();
    }

    public String getTeamMemberNameText() {
        return teamMemberName.getText();
    }

    @Override
    protected void isLoaded() throws Error {
        if (Objects.isNull(teamMemberDetailsHeader)) {
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
