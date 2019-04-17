/* Copyright 2018 Walgreen Co. */
package com.wba.test.utils;

import org.junit.Assert;
import org.openqa.selenium.Keys;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;

public class AwtRobot {
    private Robot robot;
    private int waitTime = 50;

    public AwtRobot() throws Throwable{
        robot = new Robot();
    }

    public AwtRobot(int waitTime) throws Throwable{
        this();
        this.waitTime = waitTime;
    }

    public void sendSeleniumKeys(Keys key, int numberOfKeys) throws Throwable{
        int awtKey = translateSeleniumKey(key);
        while (numberOfKeys > 0) {
            robot.keyPress(awtKey);
            robot.keyRelease(awtKey);
            robot.delay(waitTime);
            numberOfKeys--;
        }
    }

    private int translateSeleniumKey(Keys key) {
        switch (key) {
            case ENTER:
                return KeyEvent.VK_ENTER;
            case ARROW_LEFT:
                return KeyEvent.VK_LEFT;
            case ARROW_RIGHT:
                return KeyEvent.VK_RIGHT;
            case TAB:
                return KeyEvent.VK_TAB;
            default:
                Assert.fail(String.format("Not supported Selenium key $s for AWT Robot", key.toString()));
                return 0;

        }
    }

    public void typeString(String str)
    {
        for(int i=0;i<str.length();i++)
        {
            typeCharacter(""+str.charAt(i));
        }
    }
    public void typeCharacter(String letter)
    {
        try
        {
            int keyCode = translateCharacter(letter);
            boolean upperCase = translateUpperCase(letter);

            if (keyCode == -1) {
                upperCase = Character.isUpperCase( letter.charAt(0) );
                String variableName = "VK_" + letter.toUpperCase();
                Class clazz = KeyEvent.class;
                Field field = clazz.getField(variableName);
                keyCode = field.getInt(null);
            }

            if (upperCase) robot.keyPress( KeyEvent.VK_SHIFT );

            robot.keyPress( keyCode );
            robot.keyRelease( keyCode );

            if (upperCase) robot.keyRelease( KeyEvent.VK_SHIFT );

            robot.delay(waitTime);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    public void copyToCliboard() {

        robot.keyPress( KeyEvent.VK_CONTROL );
        robot.keyPress( KeyEvent.VK_C);
        robot.keyRelease( KeyEvent.VK_C);
        robot.keyRelease( KeyEvent.VK_CONTROL );

        robot.delay(waitTime);
    }


    private int translateCharacter(String letter) {
        switch (letter.charAt(0)) {
            case ':':
                return KeyEvent.VK_SEMICOLON;
            case '\\':
                return KeyEvent.VK_BACK_SLASH;
            case '-':
            case '_':
                return KeyEvent.VK_MINUS;
            case '.':
                return 0x2E;
            default:
                return -1;
        }

    }

    private boolean translateUpperCase(String letter) {
        switch (letter.charAt(0)) {
            case ':':
            case '_':
                return true;
            default:
                return false;
        }

    }
}

