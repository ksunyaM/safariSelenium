/*******************************************************************************
 * Copyright 2019 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.pages.teammember;

import com.wba.test.utils.StatefulSingleton;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class CommonUtils {

    private static WebDriver driver;

    public static void waitForObject(WebDriver driver, long waitTime, String objectLocator, String locatorProperties) {
        driver = StatefulSingleton.instance().getDriver();
        objectLocator = objectLocator.toLowerCase();
        switch (objectLocator) {
            case "id":
                new WebDriverWait(driver, waitTime).until(ExpectedConditions.visibilityOfElementLocated(By.id(locatorProperties)));
            case "xpath":
                new WebDriverWait(driver, waitTime).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locatorProperties)));
            case "css":
                new WebDriverWait(driver, waitTime).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(locatorProperties)));
            default:
                throw new NotFoundException();
        }
    }

    public static void fluentWaitForObject(final By locator) {
        driver = StatefulSingleton.instance().getDriver();
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(30, TimeUnit.SECONDS)
                .pollingEvery(500, TimeUnit.MILLISECONDS)
                .ignoring(NoSuchElementException.class);

        ((FluentWait<WebDriver>) wait).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
}
