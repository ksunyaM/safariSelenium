/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.pageobjects.pages.desktop.st6;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import com.wba.test.utils.AwtRobot;
import com.wba.test.utils.StatefulSingleton;

public class LoginPage extends LoadableComponent<LoginPage> {

    private WebDriver driver;
    private AwtRobot awtRobot;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void login() throws Throwable {

    	StatefulSingleton.instance().setDriver(driver);
	    Thread.sleep(8000);
	    try{
	        driver.findElement(By.xpath("//*[@id='rx-app-nav']/rx-uikit-app-bar/div/button")).click();
	    }catch(Exception E){
	        awtRobot= new AwtRobot(200);
	        awtRobot.typeString("devrph00");
	        awtRobot.sendSeleniumKeys(Keys.TAB,1);
	        awtRobot.typeString("Welcome1");
	        awtRobot.sendSeleniumKeys(Keys.ENTER,1);
	    }

    }
    
    public void loginWithCredentials(String username, String password) throws Throwable {

    	StatefulSingleton.instance().setDriver(driver);
	    Thread.sleep(8000);
	    try{
	        driver.findElement(By.xpath("//*[@id='rx-app-nav']/rx-uikit-app-bar/div/button")).click();
	    }catch(Exception E){
	        awtRobot= new AwtRobot(200);
	        awtRobot.typeString(username);
	        awtRobot.sendSeleniumKeys(Keys.TAB,1);
	        awtRobot.typeString(password);
	        awtRobot.sendSeleniumKeys(Keys.ENTER,1);
	    }

    }
    

    @Override
    protected void load() {
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void isLoaded() throws Error {

    }
}
