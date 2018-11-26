import PDA_config.Configuration;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class Capabilities

{
    public static RemoteWebDriver Capabilities() throws IOException, InterruptedException {
        Configuration.load();
        Configuration.print();

        File f = new File(Configuration.get("app_path"));
        File fs = new File(f, Configuration.get("app_name"));

        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability(MobileCapabilityType.DEVICE_NAME, Configuration.get("device_name"));
        cap.setCapability(MobileCapabilityType.APP, fs.getPath());
        cap.setCapability(MobileCapabilityType.PLATFORM_NAME, Configuration.get("platform"));
        cap.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, Configuration.get("timeout"));
        cap.setCapability(MobileCapabilityType.ORIENTATION, ScreenOrientation.LANDSCAPE);

        RemoteWebDriver driver = new RemoteWebDriver(new URL(Configuration.get("appium_server")), cap);
        //RemoteWebDriver driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"),cap);

        return driver;
    }
}
