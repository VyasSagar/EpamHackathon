package com.helios.utilities;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.helios.datatypes.ExecutionProperty;
import com.helios.scriptutils.ExecutionState;
import com.helios.scriptutils.ScenarioState;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.proxy.CaptureType;

public class DriverHandler implements StringLiterals {

	private RemoteWebDriver driver = null;
	private String environment;
	private String browser;

	private String hubURL;
	private String hubPort;
	private String resolution;

	private String browserStackUrl;
	private String os;
	public boolean isBrowserMobEnabled = false;
	private DesiredCapabilities capabilities;
	private ScenarioState scenarioState;

	private String propertySheetName = "ExecutionProperties";
	private String username;
	private String access_key;

	public RemoteWebDriver getDriver() {
		return driver;
	}

	public void tearDown(boolean status) {

		if (isBrowserMobEnabled) {

			File harFolder = new File(ExecutionState.getCurrentReportsPath() + "/HAR");
			if (!harFolder.exists())
				harFolder.mkdirs();
			Har har = proxy.getHar();

			String filename = harFolder + "/" + scenarioState.getFeatureName() + " - " + scenarioState.getScenarioName()
					+ " - " + scenarioState.getDataSetName() + " " + scenarioState.getId();
			File harFile = new File(filename + ".har");
			try {
				har.writeTo(harFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			proxy.stop();
		}
		try {
			driver.quit();
		} catch (Exception e) {
			System.out.println("Browser Closed Already");
		}
	}

	public DriverHandler(ScenarioState scenarioState) {
		this.scenarioState = scenarioState;
		readProperties();
		buildDriver();
	}

	public DriverHandler() {
		readProperties();
		isBrowserMobEnabled = false;
		buildDriver();
	}

	private void readProperties() {
		// isBrowserMobEnabled = getExcecutionProperty("Browser
		// Mob").equalsIgnoreCase("yes");
		ExecutionProperty executionProperty = scenarioState.getExecutionProperty();
		environment = executionProperty.getExecutionType();
		browser = executionProperty.getBrowser();
		hubURL = executionProperty.getAddress();
		hubPort = executionProperty.getPort();
		resolution = executionProperty.getResolution();

		// browserStackUrl = getExcecutionProperty("Remote Address");
		// username = getExcecutionProperty("Username");
		// access_key = getExcecutionProperty("Access Key");
		// browserStackUrl = browserStackUrl.replaceAll("<USERNAME>", username);
		// browserStackUrl = browserStackUrl.replaceAll("<KEY>", access_key);
		// os = getExcecutionProperty("OS");

		if (scenarioState != null)
			isBrowserMobEnabled = this.scenarioState.isMobProxyEnabled();

		//
		// environment = "Grid";
		// browser = "Chrome";
		// // hubURL = "192.168.186.128";
		// hubURL = "localhost";
		// hubPort = "4444";
		// resolution = "1366x768";
	}

	// private String getExcecutionProperty(String key) {
	// return ExcelInput.fetchProperty(propertySheetName, key);
	// }

	public void startNewPage(String lastPage) {
		if (!isBrowserMobEnabled)
			return;
		proxy.newPage(lastPage);
	}

	private void buildDriver() {
		capabilities = new DesiredCapabilities();
		if (isBrowserMobEnabled) {
			Proxy seleniumProxy = getSeleniumProxy(getProxyServer());
			capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
			// proxy.newHar("christies.com");
			proxy.newHar();
		}
		switch (environment.toLowerCase()) {
		case "grid":
			buildGrid();
			break;
		case "Browser Stack":
			buildBrowserStack();
			break;
		default:
			buildLocal();
			break;
		}
		// int widht = Integer.parseInt(resolution.toLowerCase().split("x")[0]);
		// int height =
		// Integer.parseInt(resolution.toLowerCase().split("x")[1]);
		// Dimension d = new Dimension(widht, height);
		// driver.manage().window().setSize(d);
		// Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		// org.openqa.selenium.Dimension d = new
		// org.openqa.selenium.Dimension(screenSize.width, screenSize.height);
		// driver.manage().window().setSize(d);
//		driver.manage().timeouts().implicitlyWait(PAGE_IMPLICIT_WAIT, TimeUnit.SECONDS);
		// driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
	}

	private void buildBrowserStack() {

		capabilities.setCapability("os", os);
		String osVersion = (os.equalsIgnoreCase("windows")) ? "10" : "High Sierra";
		capabilities.setCapability("os_version", osVersion);
		capabilities.setCapability("resolution", resolution);

		capabilities.setCapability("browser", browser);
		capabilities.setCapability("browser_version", "69.0");

		switch (browser) {
		case "Chrome":
			capabilities.setCapability("browser", browser);
			capabilities.setCapability("browser_version", "69.0");
			break;
		case "Firefox":
			capabilities.setCapability("browser", browser);
			capabilities.setCapability("browser_version", "61.0");
			break;
		case "IE":
			if (!os.equals("Windows")) {
				capabilities.setCapability("browser", browser);
				capabilities.setCapability("browser_version", "69.0");
			} else {
				capabilities.setCapability("browser", browser);
				capabilities.setCapability("browser_version", "11.0");
			}

			break;
		case "Edge":
			if (!os.equals("Windows")) {
				capabilities.setCapability("browser", browser);
				capabilities.setCapability("browser_version", "69.0");
			} else {
				capabilities.setCapability("browser", browser);
				capabilities.setCapability("browser_version", "17.0");
			}
			break;
		case "Safari":
			if (os.equals("Windows")) {
				capabilities.setCapability("browser", browser);
				capabilities.setCapability("browser_version", "69.0");
			} else {
				capabilities.setCapability("browser", browser);
				capabilities.setCapability("browser_version", "11.1");
			}
			break;
		}

		try {
			driver = new RemoteWebDriver(new URL(browserStackUrl), capabilities);
			driver.setFileDetector(new LocalFileDetector());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	private void buildGrid() {
		String seleniumHubURL = "http://" + hubURL + ":" + hubPort + "/wd/hub/";
		String proxyPath = "Proxy-Helper_v1.2.7.crx";
		switch (browser) {
		case "Chrome":
			ChromeOptions chromeOptions = new ChromeOptions();
			if (scenarioState.getExecutionProperty().getAddress().contains(AWS_IP)) {
				chromeOptions.addExtensions(new File(proxyPath));
			}
			capabilities.setCapability(CapabilityType.BROWSER_NAME, BrowserType.CHROME);
			capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
			capabilities.setCapability("enableVNC", true);
			// capabilities.setCapability("enableVideo", true);
			break;
		case "chrome-headless":
			chromeOptions = new ChromeOptions();
			capabilities.setCapability(CapabilityType.BROWSER_NAME, BrowserType.CHROME);
			capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
			break;
		case "Firefox":
			capabilities.setCapability(CapabilityType.BROWSER_NAME, BrowserType.FIREFOX);
			capabilities.setCapability("enableVNC", true);
			break;
		case "IE":
			capabilities.setCapability(CapabilityType.BROWSER_NAME, BrowserType.IE);
			capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			capabilities.setCapability("ignoreZoomSetting", true);
			capabilities.setCapability("ignoreProtectedModeSettings", true);
			capabilities.setCapability("requireWindowFocus", true);

			break;
		default:
			capabilities = DesiredCapabilities.chrome();
			capabilities.setCapability("enableVNC", true);
		}

		try {
			driver = new RemoteWebDriver(new URL(seleniumHubURL), capabilities);
			driver.setFileDetector(new LocalFileDetector());

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	private void buildLocal() {
		switch (browser) {
		case "Chrome":
			if (!System.getProperty("os.name").equalsIgnoreCase("linux")) {
				System.setProperty("webdriver.chrome.driver", "driver\\chromedriver.exe");
			} else {
				System.out.println("BUILDING LOCAL ##########################################");
				System.out.println("linuxgrid/chromedriver");
				System.setProperty("webdriver.chrome.driver","linuxgrid/chromedriver");
			}
			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			chromePrefs.put("profile.default_content_settings.popups", 0);
			chromePrefs.put("download.default_directory", ExecutionState.getCurrentOutputPath());
			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("prefs", chromePrefs);
			driver = new ChromeDriver(options);
			break;
		case "IE":
			capabilities.setCapability("requireWindowFocus", true);
			capabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
			capabilities.setCapability("ie.ensureCleanSession", true);
			System.setProperty("webdriver.ie.driver", "driver\\IEDriverServer.exe");
			driver = new InternetExplorerDriver(capabilities);
			break;
		case "chromeheadless":
			final ChromeOptions chromeOptions = new ChromeOptions();
			capabilities.setCapability(CapabilityType.BROWSER_NAME, BrowserType.CHROME);
			capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
			break;
		case "Firefox":
			if (!System.getProperty("os.name").equalsIgnoreCase("linux")) {
				System.setProperty("webdriver.gecko.driver", "driver\\geckodriver.exe");
			} else {
				System.setProperty("webdriver.gecko.driver", "linuxgrid\\geckodriver");
			}
			capabilities.setCapability(CapabilityType.BROWSER_NAME, BrowserType.FIREFOX);
			driver = new FirefoxDriver();
		default:
			if (!System.getProperty("os.name").equalsIgnoreCase("linux")) {
				System.setProperty("webdriver.chrome.driver", "driver\\chromedriver.exe");
			} else {
				System.setProperty("webdriver.chrome.driver", "linuxgrid\\chromedriver");
			}
			break;
		}

	}

	public BrowserMobProxy getProxyServer() {
		proxy = new BrowserMobProxyServer();
		proxy.enableHarCaptureTypes(CaptureType.getAllContentCaptureTypes());
		proxy.enableHarCaptureTypes(CaptureType.getCookieCaptureTypes());
		proxy.enableHarCaptureTypes(CaptureType.getRequestCaptureTypes());
		proxy.enableHarCaptureTypes(CaptureType.getResponseCaptureTypes());
		proxy.enableHarCaptureTypes(CaptureType.getBinaryContentCaptureTypes());
		proxy.enableHarCaptureTypes(CaptureType.getNonBinaryContentCaptureTypes());
		proxy.setTrustAllServers(true);
		ExecutionState.mobProxyPorts++;
		proxy.start(ExecutionState.mobProxyPorts);

		return proxy;
	}

	public Proxy getSeleniumProxy(BrowserMobProxy proxyServer) {
		Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxyServer);
		try {
			String hostIp = Inet4Address.getLocalHost().getHostAddress();
			System.out.println(Inet4Address.getLocalHost().getHostAddress() + ":" + proxyServer.getPort());
			seleniumProxy.setHttpProxy(hostIp + ":" + proxyServer.getPort());
			seleniumProxy.setSslProxy(hostIp + ":" + proxyServer.getPort());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return seleniumProxy;
	}

	private BrowserMobProxy proxy;

}
