package com.sample.tests.junit;

import java.io.File;

import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.sample.framework.Configuration;
import com.sample.framework.Driver;
import com.sample.framework.ui.PageFactory;
import com.sample.tests.pages.SearchPage;

public class SearchPageUITest extends TestCommon {

    public SearchPageUITest() {
        // TODO Auto-generated constructor stub
    }


    @Test
    public void testVerifyUIOnSearchPage() {
        assertTrue(searchPage.editDestination.exists());
        assertTrue(searchPage.checkoutDayExpand.exists());
        assertTrue(searchPage.radioBusiness.exists());
        assertTrue(searchPage.radioLeisure.exists());
        //Assert.assertTrue(searchPage.radioHotels.exists());
        assertTrue(searchPage.selectAdultsNumber.exists());
        assertTrue(searchPage.buttonSubmit.exists());
    }
}
