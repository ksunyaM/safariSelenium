import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class CapabilitiesPDA
{

    public static void main(String[] args) throws MalformedURLException, InterruptedException {
        File f = new File("src/config");
        File fs = new File(f, "app-debug-test.apk");

        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability(MobileCapabilityType.DEVICE_NAME,"PDAEmulator");
        cap.setCapability(MobileCapabilityType.APP,fs.getAbsolutePath());
        cap.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        cap.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "60");

        RemoteWebDriver driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"),cap);
        //driver.rotate(ScreenOrientation.LANDSCAPE);
        System.out.println("Start execution");
        Thread.sleep(10000);
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.findElementByXPath("//android.widget.Button[@content-desc='Add All ']").click();
        driver.findElementByXPath("//android.widget.EditText[@resource-id='stationNumber']").sendKeys("12345");
    }
}
