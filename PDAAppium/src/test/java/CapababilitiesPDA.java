import PDA_config.Configuration;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class CapababilitiesPDA
{

    public static void main(String[] args) throws IOException, InterruptedException {
        Configuration.load();
        Configuration.print();

        File f = new File(Configuration.get("app_path"));
        File fs = new File(f,Configuration.get("app_name"));

        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability(MobileCapabilityType.DEVICE_NAME,Configuration.get("device_name"));
        cap.setCapability(MobileCapabilityType.APP,fs.getPath());
        cap.setCapability(MobileCapabilityType.PLATFORM_NAME, Configuration.get("platform"));
        cap.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, Configuration.get("timeout"));
        cap.setCapability(MobileCapabilityType.ORIENTATION, ScreenOrientation.LANDSCAPE);

        RemoteWebDriver driver = new RemoteWebDriver(new URL(Configuration.get("appium_server")),cap);
        //RemoteWebDriver driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"),cap);
        System.out.println("Start execution");
        Thread.sleep(10000);
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.findElementByXPath("//android.widget.Button[@content-desc='Add All ']").click();
        driver.findElementByXPath("//android.widget.EditText[@resource-id='stationNumber']").sendKeys("12345");
    }
}
