/*
	This is the Geb configuration file.

	See: http://www.gebish.org/manual/current/configuration.html
*/

import org.openqa.selenium.Dimension
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.openqa.selenium.remote.DesiredCapabilities
// Use htmlunit as the default
// See: http://code.google.com/p/selenium/wiki/HtmlUnitDriver
driver = {
    DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
    desiredCapabilities.setJavascriptEnabled(true);
    desiredCapabilities.setCapability("takesScreenshot", true);

    def phantomJSDriver = new PhantomJSDriver(desiredCapabilities)
    phantomJSDriver.manage().window().setSize(new Dimension(1028, 768))
    phantomJSDriver
}

waiting {
    timeout = 15
    retryInterval = 0.5
}

environments {
    // run as “grails -Dgeb.env=chrome test-app”
    // See: http://code.google.com/p/selenium/wiki/ChromeDriver
    chrome {
        driver = { new ChromeDriver() }
    }

    // run as “grails -Dgeb.env=firefox test-app”
    // See: http://code.google.com/p/selenium/wiki/FirefoxDriver
    firefox {
        driver = { new FirefoxDriver() }
    }
}
