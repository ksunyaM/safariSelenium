import PDA_config.Configuration;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class CapababilitiesPDA extends Capabilities
{

    public static void main(String[] args) throws IOException, InterruptedException {
        RemoteWebDriver driver = Capabilities();
        System.out.println("Start execution");
        Thread.sleep(10000);
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.findElementByXPath("//android.widget.Button[@content-desc='Add All ']").click();
        driver.findElementByXPath("//android.widget.EditText[@resource-id='stationNumber']").sendKeys("12345");
    }
}
