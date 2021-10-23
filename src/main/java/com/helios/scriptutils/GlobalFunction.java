package com.helios.scriptutils;

import java.awt.AWTException;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.testng.Assert.*;

import com.helios.excel.ExcelInput;
import com.helios.excel.ExcelMulti;
import com.helios.excel.ExcelOutput;
import com.helios.utilities.StringLiterals;
import com.testautomationguru.utility.PDFUtil;

abstract public class GlobalFunction implements StringLiterals {

	private EventFiringWebDriver remoteDriver;
	protected WebDriverWait wait;
	protected Actions actions;
	protected JavascriptExecutor jsExecutor;
	protected ScenarioState state;
	private String saleSheetName = "AllSalesSheet";
	protected WebDriverWait webdriverWait;
	PDFUtil pdfUtil = new PDFUtil();

	private void registerCall(String functionName) {
		System.out.println(functionName);
		state.registerGlobalFunctionCall(this.getClass().getSimpleName(), functionName);
	}

	private void registerCall(String functionName, String data) {
		state.registerGlobalFunctionCall(this.getClass().getSimpleName(), functionName, data);
	}

	protected String getJSInputText(WebElement element) throws Throwable {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		String text = null;
		try {
			text = (String) jsExecutor.executeScript("return arguments[0].value", element);
		} catch (Exception e) {
			logReportMessage(e.getMessage());
			e.printStackTrace();
		}
		return text;
	}

	public GlobalFunction(ScenarioState state) {
		this.state = state;
		this.remoteDriver = state.getEventFiringDriver();
		PageFactory.initElements(remoteDriver, this);
		webdriverWait = new WebDriverWait(getDriver(), 10);
		wait = new WebDriverWait(remoteDriver, 20);
		actions = new Actions(remoteDriver);
		jsExecutor = (JavascriptExecutor) remoteDriver;
	}

	/**
	 * This opens the website to be tested in the browser.
	 * 
	 * @author Manveer Singh
	 * @since 2018-9-2
	 * @param String
	 *            url for the website to be open
	 */

	protected ScenarioState getState() {
		return state;
	}

	protected EventFiringWebDriver getDriver() {
		return this.remoteDriver;
	}


	protected List<WebElement> getElementsByXpath(String xpath)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		List<WebElement> element = null;
		try {
			// element = remoteDriver.findElementByXPath(xpath);
			element = remoteDriver.findElements(By.xpath(xpath));
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when finding the element", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when finding the element", e);
			throw e;
		}
		return element;
	}


	protected WebElement getExactElemtFromParamXpath(String xpath, String parameter1, String parameter2)
			throws NoSuchElementException, StaleElementReferenceException {
		WebElement element = null;
		try {
			xpath = xpath.replaceFirst(VAR, parameter1);
			xpath = xpath.replaceFirst(VAR, parameter2);
			element = remoteDriver.findElement(By.xpath(xpath));
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when finding the element", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when finding the element", e);
			throw e;
		}
		return element;
	}


	protected boolean scrollToLazyElement(WebElement parent, String xpath, String xpathParam, int maxScrollCount,
			int offset) {
		for (int i = 0; i < maxScrollCount; i++) {

			jsExecutor.executeScript("arguments[0].scrollBy(0," + offset + ")", parent);
			// actions.sendKeys(Keys.PAGE_DOWN).build().perform();
			System.out.println("Scrolled:" + i);
			pause(100);
			try {
				if (!isVisibleInViewport(getElemtFromParamXpath(xpath, xpathParam))) {
					actions.sendKeys(Keys.PAGE_DOWN).build().perform();
					System.out.println("Scrolled:" + i);
					pause(100);
					continue;
				}
			} catch (Exception e) {
				continue;
			}

			break;
		}

		return true;
	}

	protected boolean scrollToLazyElement(WebElement parent, WebElement element, int maxScrollCount, int offset) {
		for (int i = 0; i < maxScrollCount; i++) {

			jsExecutor.executeScript("arguments[0].scrollBy(0," + offset + ")", parent);
			// actions.sendKeys(Keys.PAGE_DOWN).build().perform();
			System.out.println("Scrolled:" + i);
			pause(100);
			try {
				if (!isVisibleInViewport(element)) {
					actions.sendKeys(Keys.PAGE_DOWN).build().perform();
					System.out.println("Scrolled:" + i);
					pause(100);
					continue;
				}
			} catch (Exception e) {
				continue;
			}

			break;
		}

		return true;
	}

	protected boolean scrollToLazyElementHorizontal(WebElement parent, WebElement element, int maxScrollCount,
			int offset) {
		for (int i = 0; i < maxScrollCount; i++) {

			jsExecutor.executeScript("arguments[0].scrollBy(" + offset + ",0)", parent);
			// actions.sendKeys(Keys.PAGE_DOWN).build().perform();
			System.out.println("Scrolled:" + i);
			pause(100);
			try {
				if (!isVisibleInViewport(element)) {
					System.out.println(isVisibleInViewport(element));
//					actions.sendKeys(Keys.PAGE_DOWN).build().perform();
					System.out.println("Scrolled:" + i);
					pause(100);
					continue;
				}
			} catch (Exception e) {
				continue;
			}

			break;
		}

		return true;
	}

	private void attachScreenshot() {
		TakesScreenshot camera = (TakesScreenshot) getState().getDriverHandler().getDriver();
		byte[] screenshot = camera.getScreenshotAs(OutputType.BYTES);
		getState().getScenario().embed(screenshot, "image/png");
	}

	Queue<String> stylesQueue = new LinkedList<>();

	

	protected String getCurrentPageTitle() {
		return remoteDriver.getTitle();
	}

	protected boolean switchTabPolling() {
		String parentPageTitle;
		String newPageTitle;
		for (int i = 0; i < 5; i++) {
			parentPageTitle = getCurrentPageTitle();
			switchTab();
			newPageTitle = getCurrentPageTitle();
			if (parentPageTitle.equalsIgnoreCase(newPageTitle)) {
				switchTab();
			} else {
				break;
			}
		}
		return true;
	}

	
	protected boolean waitForElement(String paramXpath, String value) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), value);
		int tryCount = PAGE_IMPLICIT_WAIT / 2;
		int currentCount = 0;
		int time = 0;
		while (!isPresent(paramXpath, value) && currentCount++ < tryCount) {
			pause(2000);
			time = (time + 1);
			System.out.println(time);
		}
		if (isPresent(paramXpath, value))
			return true;
		else {
			remoteDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			try {
				getElemtFromParamXpath(paramXpath, value).isDisplayed();
				return true;
			} catch (Exception e) {
				remoteDriver.manage().timeouts().implicitlyWait(PAGE_IMPLICIT_WAIT, TimeUnit.SECONDS);
				throw e;
			}

		}
	}

	protected String base64Decode(String input) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		byte[] decodedPwdBytes = Base64.decodeBase64(input);
		String decodedPwd = new String(decodedPwdBytes);
		return decodedPwd;
	}

	
	protected String getTestDataValue(String key, boolean value) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), key);
		return ExcelInput.fetchData(state.getFeatureName(), state.getDataSetName(), key);
	}

	
	protected ArrayList<Map<String, String>> getMultiExcelData(String dataName) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), dataName);
		return ExcelMulti.getData(dataName);
	}

	public List<String> getStringsAsList(String str, String delimeter) {
		List<String> colValues = new ArrayList<>();
		String[] excelValue = str.split(delimeter);
		for (String string : excelValue) {
			colValues.add(string.replaceAll(delimeter, "").trim());
		}
		return colValues;
	}

	protected boolean refreshPage() {

		try {
			remoteDriver.navigate().refresh();
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public void logReportMessageAndTakeScreenShot(String message) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), message);
		try {
			String src = ((TakesScreenshot) remoteDriver).getScreenshotAs(OutputType.BASE64);
			attachScreenshot();
			// Reporter.addStepLog("<span><b>[INFO]</b>\t" + message
			// + "<a data-featherlight='image' href='data:image/png;base64," +
			// src
			// + "' style='display:inline;float: right;''><i
			// class='material-icons'>panorama</i></a></span>");
		} catch (Exception e) {
			e.printStackTrace();
			// Reporter.setTestRunnerOutput("Error while taking Screenshot");
		}
	}

	
	public String getSelectBoxText(WebElement element) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			String value = element.getAttribute("value");
			WebElement option = element.findElement(By.xpath("option[@value='" + value + "']"));
			
			
			Select select = new Select(element);
			select.getFirstSelectedOption().getText();
			
			return option.getText();
		} catch (Exception e) {
			e.printStackTrace();
			// Reporter.setTestRunnerOutput("Error while finding the element
			// from select
			// box");
			throw e;
		}

	}

	public String getInputTextOrText(WebElement element) throws Throwable {
		String text = "";
		text = getText(element);
		if (text == null || text.trim().equalsIgnoreCase("")) {
			text = getJSInputText(element);
		}
		return text;
	}

	protected boolean acceptUnhandledAlert() {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		if (wait.until(ExpectedConditions.alertIsPresent()) == null) {
			embedScreenshot();
		} else {
			acceptAlert();
			embedScreenshot();
		}
		return true;
	}

	protected boolean jsClearAndEnterText(WebElement element, String text) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), text);
		try {
			while (element.getAttribute("value").length() > 0) {
				element.sendKeys(Keys.BACK_SPACE);
			}

			element.sendKeys(text);

		} catch (Exception e) {
			logError("Error while JS click", e);
			e.printStackTrace();
			throw e;
		}
		return true;
	}

	protected boolean scrollToTop() {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			((JavascriptExecutor) remoteDriver).executeScript("window.scrollTo(0, -document.body.scrollHeight)");
		} catch (Exception e) {
			logError("Error while scrolling to top", e);
			e.printStackTrace();
			throw e;
		}

		return true;
	}


	public String parseDateToNewFormat(String originalDate, String originalFormatDate, String newFormat) {
		String dateStr = "";
		try {
			dateStr = originalDate;
			DateFormat srcDf = new SimpleDateFormat(originalFormatDate);
			Date date = srcDf.parse(dateStr);
			DateFormat destDf = new SimpleDateFormat(newFormat);
			dateStr = destDf.format(date);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateStr;
	}

	protected static void setDownloadFilePathAndStartDownload(String fileName) {
		File folder = new File(DOWNLOAD_PATH);
		if (!folder.exists()) {
			folder.mkdir();
		}
		String filePath = new File(DOWNLOAD_PATH).getAbsolutePath() + "\\" + fileName;
		System.out.println(filePath);
		StringSelection stringSelection = new StringSelection(filePath);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);

		Robot robot = null;

		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}

		robot.delay(250);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.delay(150);
		robot.keyRelease(KeyEvent.VK_ENTER);
	}
	protected void clickClearAndEnterText(WebElement element, String text) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), text);
		try {
			isElementInteractable(element);
			actions.click(element).build().perform();
			isElementInteractable(element);
			element.clear();
			isElementInteractable(element);
			char[] txt = text.toCharArray();
			for (char c : txt) {
				if (c == '/') {
					jsEnterChar(element);
				} else {
					element.sendKeys(String.valueOf(c));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			logError("Error while click clear and EnterText", e);
		}
	}

	
	protected boolean openURL(String url) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());

		try {
			remoteDriver.manage().timeouts().pageLoadTimeout(50, TimeUnit.SECONDS);
			remoteDriver.get(url);
			return true;
		} catch (WebDriverException e) {
			logError("Exception while opening the page", e);
			remoteDriver.navigate().refresh();
			return false;
		} finally {
			remoteDriver.manage().timeouts().pageLoadTimeout(-1, TimeUnit.SECONDS);
		}

	}

	
	/**
	 * Current URL of the page that is in the focus
	 * 
	 * @author Manveer Singh
	 * @since 2018-9-2
	 * @return URL of the page that is currently in focus
	 */
	protected String getCurrentUrl() {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		return remoteDriver.getCurrentUrl();
	}

	/**
	 * Clicks on the Webelement provided
	 * 
	 * @author Manveer Singh
	 * @since 2018-9-2
	 * @param WebElement
	 *            Element to be clicked on
	 */

	protected boolean click(WebElement element) throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			element.click();
			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception while ckicking on the elemnt", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption while ckicking on the element", e);
			throw e;
		}

	}

	protected WebElement getElementByXpath(String xpath) throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		WebElement element = null;
		try {
			// element = remoteDriver.findElementByXPath(xpath);
			element = remoteDriver.findElement(By.xpath(xpath));
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when finding the element", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when finding the element", e);
			throw e;
		}
		return element;
	}

	protected boolean isCheckboxSelected(WebElement element)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			logReportMessage("webelement is selected");
			return element.isSelected();
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when checking element is selected ", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exception when checking element is selected ", e);
			throw e;
		}
	}
	public boolean isElementInteractable(WebElement element) {
		try {
			webdriverWait.until(ExpectedConditions.visibilityOf(element));
		} catch (Exception e) {
			System.out.println("Waited for Element Visibility But Element Not Visible");
		}
		try {
			webdriverWait.until(ExpectedConditions.elementToBeClickable(element));
		} catch (Exception e) {
			System.out.println("Waited for Element Clickibility But Element Not Clickable");
		}
		return true;
	}
	protected boolean moveAndClick(WebElement element) throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			isElementInteractable(element);
			actions.moveToElement(element).click().build().perform();
			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when finding the element", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when finding the element", e);
			throw e;
		}
	}
	
	protected String decodeBase64String(String encodedText) {
		byte[] decodedPwdBytes = Base64.decodeBase64(encodedText);
		String decodedPwd = new String(decodedPwdBytes);
		return decodedPwd;
	}

	protected boolean enterText(WebElement element, String text)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), text);
		try {
			element.sendKeys(text);
			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when entering text in the element", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when entering text in finding the element", e);
			throw e;
		}
	}

	protected boolean enterTextAndSend(WebElement element, String text)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), text);
		try {
			element.sendKeys(text);
			element.sendKeys(Keys.ENTER);
			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when entering text in the element", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when entering text in finding the element", e);
			throw e;
		}
	}

	protected boolean enterText(WebElement element, Keys text)
			throws NoSuchElementException, StaleElementReferenceException {
		try {
			element.sendKeys(text);
			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when entering text in the element", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when entering text in finding the element", e);
			throw e;
		}
	}

	protected boolean pressKey(WebElement element, Keys key)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			element.sendKeys(key);
			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when entering text in the element", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when entering text in finding the element", e);
			throw e;
		}
	}

	/**
	 * Get Element Inner Text using Javascript Executor InnerText
	 * 
	 * @author Sagar Vyas
	 * @since 2018-12-27
	 * @param WebElement
	 *            whose value is to be known
	 * @return String value of the element
	 */
	protected String getElementInnerText(WebElement webElement) {
		String textValue = null;
		try {

			textValue = webElement.getAttribute("innerText");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return textValue;
	}

	/**
	 * Scroll till the element is found in the view
	 * 
	 * @author Sagar Vyas
	 * @since 2019-04-15
	 * @param Webelement
	 */
	protected void scrollIntoView(WebElement element) {
		jsExecutor.executeScript("arguments[0].scrollIntoView();", element);
	}

	/**
	 * Scroll till the element is found in the view
	 * 
	 * @author Sagar Vyas
	 * @since 2019-04-15
	 * @param Webelement
	 */
	protected void scrollTillElementFound(int num) {
		Robot rb = null;
		try {
			rb = new Robot();
			while (num != 0) {
				rb.keyPress(KeyEvent.VK_PAGE_DOWN);
				rb.keyRelease(KeyEvent.VK_PAGE_DOWN);
				num--;
			}
		} catch (AWTException e) {
		}

	}

	protected String getText(WebElement element) throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		String text = null;
		try {
			text = element.getText();
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when getting text from the element", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when getting text from the element", e);
			throw e;
		}
		return text;
	}

	protected String getTextByJS(WebElement element) {
		String text = (String) ((JavascriptExecutor) remoteDriver).executeScript("return arguments[0].innerHTML;",
				element);
		return text;
	}

	protected boolean moveToElement(WebElement element) throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			actions.moveToElement(element).build().perform();
			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when moving to the element", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when moving to the element", e);
			throw e;
		}
	}

	protected boolean clickAndEnterText(WebElement element, String text)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			actions.click(element).sendKeys(text).build().perform();
			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception ", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption", e);
			throw e;
		}

	}

	protected boolean moveClickAndEnterText(WebElement element, String text)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			pause(300);
			actions.moveToElement(element).click().sendKeys(text).build().perform();
			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception ", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption", e);
			throw e;
		}

	}
	
	protected boolean jsEnterChar(WebElement element) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			jsExecutor.executeScript("arguments[0].value=arguments[0].value+String.fromCharCode(0x2f);", element);
		} catch (Exception e) {
			logError("Error while JS click", e);
			e.printStackTrace();
			throw e;
		}
		return true;
	}
	
	protected boolean moveClickClearAndEnterText(WebElement element, String text)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			isElementInteractable(element);
			isElementInteractable(element);
			actions.moveToElement(element).click().build().perform();
			element.clear();
			isElementInteractable(element);
			char[] txt = text.toCharArray();
			for (char c : txt) {
				if (c == '/') {
					jsEnterChar(element);
				} else {
					element.sendKeys(String.valueOf(c));
				}
			}

			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception ", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption", e);
			throw e;
		}
	}


	protected boolean moveClickWaitClearAndEnterText(WebElement element, String text)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			actions.moveToElement(element).click().build().perform();
			element.clear();
			actions.moveToElement(element).click().build().perform();
			pause(1500);
			element.sendKeys(text);
			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception ", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption", e);
			throw e;
		}
	}

	protected boolean clearText(WebElement element) throws NoSuchElementException, StaleElementReferenceException {

		try {
			element.clear();
			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception ", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption", e);
			throw e;
		}
	}


	public void logReportMessageAndTakeScreenShot(String message, WebElement element) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), message);
		try {
			highlightElement(element);
			String src = ((TakesScreenshot) remoteDriver).getScreenshotAs(OutputType.BASE64);
			attachScreenshot();
			// Reporter.addStepLog("<span><b>[INFO]</b>\t" + message
			// + "<a data-featherlight='image' href='data:image/png;base64," +
			// src
			// + "' style='display:inline;float: right;''><i
			// class='material-icons'>panorama</i></a></span>");
			unHighlightElement(element);
		} catch (Exception e) {
			e.printStackTrace();
			// Reporter.setTestRunnerOutput("Error while taking Screenshot check
			// stack trace
			// for more info");
		}
	}

	protected boolean switchToiFrame(WebElement element) throws Exception {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			remoteDriver.switchTo().frame(element);
			return true;
		} catch (Exception e) {
			logError("Exception while switching to Iframe", e);
			throw e;
		}
	}

	protected void pause(int millis) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), millis + "");
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected WebElement getElemtFromParamXpath(String xpath, String parameter)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		WebElement element = null;
		try {
			isElementInteractable(element);
			xpath = xpath.replaceFirst(VAR, parameter);
			element = remoteDriver.findElement(By.xpath(xpath));
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when finding the element", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when finding the element", e);
			throw e;
		}
		return element;
	}

	/**
	 * This method replaces VAR in the xapth int with the parameter and returns
	 * webelement
	 * 
	 * @author Manveer Singh
	 * @since 2018-9-2
	 * @param Webelement
	 *            and int to be entered as parameter
	 */
	protected WebElement getElemtFromParamXpath(String xpath, int parameter)
			throws NoSuchElementException, StaleElementReferenceException {
		WebElement element = null;
		try {
			xpath = xpath.replaceFirst(VAR, parameter + "");
			element = remoteDriver.findElement(By.xpath(xpath));
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when finding the element", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when finding the element", e);
			throw e;
		}
		return element;
	}

	protected List<String> getselectBoxElements(WebElement element)
			throws NoSuchElementException, StaleElementReferenceException {
		try {
			Select select = new Select(element);
			List<WebElement> allOptions = select.getOptions();
			List<String> allOptionsValues = new ArrayList<String>();
			for (WebElement optionElement : allOptions) {
				allOptionsValues.add(optionElement.getText());
			}
			return allOptionsValues;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when selecting option by value", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when selecting option by value", e);
			throw e;
		}
	}

	protected WebElement getSelectedElementDropDown(WebElement element)
			throws NoSuchElementException, StaleElementReferenceException {
		try {
			Select select = new Select(element);
			return select.getFirstSelectedOption();

		} catch (NoSuchElementException e) {
			logError("Element Not found exception when selecting option by value", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when selecting option by value", e);
			throw e;
		}
	}

	protected String getValue(WebElement element) {
		String value = element.getAttribute("value");
		return value;
		// jsExecutor.executeScript("arguments[0].value;", element);
	}


	protected String getAttributeValue(WebElement element, String attribute) {
		String value = element.getAttribute(attribute);
		return value;
	}

	protected WebElement getElemtFromParamXpath(String xpath, String parameter1, String parameter2)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		WebElement element = null;
		try {
			xpath = xpath.replaceFirst(VAR, parameter1);
			xpath = xpath.replaceFirst(VAR, parameter2);
			element = remoteDriver.findElement(By.xpath(xpath));
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when finding the element", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when finding the element", e);
			throw e;
		}
		return element;
	}

	protected WebElement getElemtFromParamXpath(String xpath, String parameter1, String parameter2, String parameter3)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		WebElement element = null;
		try {
			xpath = xpath.replaceFirst(VAR, parameter1);
			xpath = xpath.replaceFirst(VAR, parameter2);
			xpath = xpath.replaceFirst(VAR, parameter3);
			element = remoteDriver.findElement(By.xpath(xpath));
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when finding the element", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when finding the element", e);
			throw e;
		}
		return element;
	}

	protected boolean selectBoxByVisibleText(WebElement element, String text)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			Select select = new Select(element);
			select.selectByVisibleText(text);
			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when selecting option by visible text", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when selecting option by visible text", e);
			throw e;
		}
	}

	protected boolean selectBoxByValue(WebElement element, String text)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			Select select = new Select(element);
			select.selectByValue(text);
			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when selecting option by value", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when selecting option by value", e);
			throw e;
		}
	}

	protected boolean selectBoxByIndex(WebElement element, int index)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			Select select = new Select(element);
			select.selectByIndex(index);
			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when selecting option by value", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when selecting option by value", e);
			throw e;
		}
	}

	protected boolean scrollToElement(WebElement element)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			jsExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when scrolling to element (JavaScript)", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when scrolling to element (JavaScript)", e);
			throw e;
		}
	}

	protected boolean safeJavaScriptClick(WebElement element) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			if (element.isEnabled() && element.isDisplayed()) {
				((JavascriptExecutor) remoteDriver).executeScript("arguments[0].click();", element);
			} else {
				scrollToElement(element);
				click(element);
			}
			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when scrolling to element (JavaScript)", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when scrolling to element (JavaScript)", e);
			throw e;
		}
	}

	protected boolean isEnabled(WebElement element) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			if (element.isEnabled()) {
				return true;
			} else {
				return false;
			}
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when scrolling to element (JavaScript)", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when scrolling to element (JavaScript)", e);
			throw e;
		}
	}

	protected void waitForInvisibility(WebElement webElement, int maxSeconds) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		Long startTime = System.currentTimeMillis();

		while (System.currentTimeMillis() - startTime < maxSeconds * 1000 && webElement.isDisplayed()) {
		}

	}

	public void logReportMessage(String message) {
		getState().getScenario().write("[INFO]\t" + message);
		// Reporter.addStepLog("<b>[INFO]</b>\t"+message);
	}
	
	public void logReportMessage(String message, String status) {
		if (status.equalsIgnoreCase("Success") || status.equalsIgnoreCase("Pass")) {
			getState().getScenario().write("<b>[INFO]</b>\t <font color='#00af00'><b>" + message + " " + status + "</b></font>");
		} else if (status.equalsIgnoreCase("Failure") || status.equalsIgnoreCase("Fail")) {
			getState().getScenario().write("<b>[INFO]</b>\t <font color='#e91e63'><b>" + message + " " + status + "</b></font>");
		}

	}

	protected void logError(String message, Exception e) {
		getState().getScenario().write("[Error]\t" + message);
		// Reporter.addStepLog("<b>[Error]</b>\t"+message);
	}

	protected boolean switchTab() {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		Set<String> handles = remoteDriver.getWindowHandles();
		try {
			for (String h : handles) {
				if (!h.equals(remoteDriver.getWindowHandle())) {
					remoteDriver.switchTo().window(h);
				}
			}
			return true;
		} catch (Exception e) {
			logError("Error while switching tab. (No other tab found)", e);
			throw e;
		}
	}

	protected boolean doesElementExist(WebElement element) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			if (element.isDisplayed()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	protected boolean doesElementExist(WebElement element, int seconds) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		remoteDriver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
		try {
			if (element.isDisplayed()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	protected boolean jsClick(WebElement element) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			jsExecutor.executeScript("arguments[0].click();", element);
			return true;
		} catch (Exception e) {
			logError("Error while JS click", e);
			e.printStackTrace();
			throw e;
		}
	}

	protected String getPageLoadStatus() {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			jsExecutor.executeScript("return document.readyState");
			jsExecutor.toString();
			return (String) jsExecutor.executeScript("return document.readyState");
		} catch (Exception e) {
			logError("Error while JS click", e);
			e.printStackTrace();
			throw e;
		}
	}

	protected void zAssertTrueAndTakeScreenshot(boolean condition, String successMessage, String failMessage) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			assertTrue(condition);
			logReportMessage("<font color='#00af00'> <b>[Assertion Pass]</b>\t" + successMessage + "</font>");
			embedScreenshot(successMessage);
		} catch (Exception e) {
			logReportMessage("<font color='#e91e63'> <b>[Assertion Fail]</b>\t" + failMessage + "</font>");
			embedScreenshot(failMessage);
			throw e;
		}
	}

	protected void zAssertTrueAndTakeScreenshot(boolean condition, WebElement element, String successMessage,
			String failMessage) {
		try {
			assertTrue(condition);
			logReportMessage("<font color='#00af00'> <b>[Assertion Pass]</b>\t" + successMessage + "</font>");
			embedScreenshot(element);
			embedScreenshot(successMessage);
		} catch (Exception e) {
			logReportMessage("<font color='#e91e63'> <b>[Assertion Fail]</b>\t" + failMessage + "</font>");
			embedScreenshot(failMessage);
			throw e;
		}
	}

	protected void zAssertTrue(boolean condition, String successMessage, String failMessage) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			assertTrue(condition);
			logReportMessage("<font color='#00af00'><b>[Assertion Pass]</b>\t" + successMessage + "</font>");
		} catch (Exception e) {
			logReportMessage("<font color='#e91e63'><b>[Assertion Failure]</b>\t" + failMessage + "</font>");
			throw e;
		}
	}
	/**
	 * This method is used to accept the alerts. It will wait for 10 seconds and
	 * will throw exception
	 * 
	 * @author Manveer Singh
	 * @since 2018-26-2018
	 * @return Returns true if alert is accepted successfully
	 * @param unused
	 */

	protected boolean acceptAlert() {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		for (int i = 0; i < 10; i++) {
			try {
				remoteDriver.switchTo().alert().accept();
				return true;
			} catch (Exception e) {
				System.out.println("Waiting for alert");
				pause(1000);
			}
		}

		try {
			remoteDriver.switchTo().alert().accept();
			return true;
		} catch (Exception e) {
			System.out.println("No alert found after waiting for 10 Seconds");
			logError("No alert found exception", e);
			throw e;
		}
	}

	/**
	 * This method is used to dismiss the alerts.
	 * It will wait for 10 seconds and will throw exception
	 * 
	 * @author Sagar Vyas
	 * @since 15-7-2019
	 * @return Returns true if alert is dismissed successfully
	 * @param unused
	 * */
	
	protected boolean dismissAlert(){ 
		registerCall(new Object(){}.getClass().getEnclosingMethod().getName());
		for(int  i = 0 ; i < 10; i++ ){
			try{
				remoteDriver.switchTo().alert().dismiss();
				return true;
			}catch(Exception e){
				System.out.println("Waiting for alert");
				pause(1000);
			}
		}
		
		try{
			remoteDriver.switchTo().alert().dismiss();
			return true;
		}catch(Exception e){
			System.out.println("No alert found after waiting for 10 Seconds");
			logError("No alert found exception", e);
			throw e;
		}
	}
	
	/**
	 * @author Srushti Ramteke
	 * @since 2018-10-30
	 * @param To
	 *            press control+s
	 */
	protected boolean controlSave() {
		Robot rb;
		try {
			rb = new Robot();
			rb.keyPress(KeyEvent.VK_CONTROL);
			rb.keyPress(KeyEvent.VK_S);
			pause(500);
			rb.keyRelease(KeyEvent.VK_CONTROL);
			rb.keyRelease(KeyEvent.VK_T);
			pause(500);
		} catch (AWTException e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * @author Srushti Ramteke
	 * @since 2018-10-30
	 * @param To
	 *            come back to the first window
	 */
	public boolean switchToOriginalTab() {
		ArrayList<String> tabs = new ArrayList<String>(remoteDriver.getWindowHandles());
		remoteDriver.switchTo().window(tabs.get(0));
		return true;

	}

	protected String acceptAlertGetAlertMsg() {
		String msg;
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		for (int i = 0; i < 10; i++) {
			try {
				msg = remoteDriver.switchTo().alert().getText();
				remoteDriver.switchTo().alert().accept();
				return msg;
			} catch (Exception e) {
				System.out.println("Waiting for alert");
				pause(1000);
			}
		}

		try {
			msg = remoteDriver.switchTo().alert().getText();
			remoteDriver.switchTo().alert().accept();
			return msg;

		} catch (Exception e) {
			System.out.println("No alert found after waiting for 10 Seconds");
			logError("No alert found exception", e);
			throw e;
		}
	}

	public void embedScreenshot(ArrayList<WebElement> list) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());

		try {
			for (WebElement ele : list) {
				highlightElement(ele);
			}
			// String src = ((TakesScreenshot)
			// remoteDriver).getScreenshotAs(OutputType.BASE64);
			attachScreenshot();
			for (WebElement ele : list) {
				unHighlightElement(ele);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logError("Error while taking the screenshot", e);
		}
	}
	
	public boolean convertToBoolean(String str) {
		boolean val = false;
		if (str.equals("Yes") || str.equals("True"))
			val = true;
		else if (str.equals("No") || str.equals("False"))
			val = false;

		return val;
	}

	public void embedScreenshot() {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		// String src = ((TakesScreenshot)
		// remoteDriver).getScreenshotAs(OutputType.BASE64);
		try {
			attachScreenshot();
		} catch (Exception e) {
			logError("Error while taking screenshot", e);
			e.printStackTrace();
		}
	}

	public void embedScreenshot(String title) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		// String src = ((TakesScreenshot)
		// remoteDriver).getScreenshotAs(OutputType.BASE64);
		try {
			attachScreenshot();
		} catch (Exception e) {
			logError("Error while taking screenshot", e);
			e.printStackTrace();
		}
	}

	public void embedScreenshot(WebElement webElement) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());

		try {
			highlightElement(webElement);
			// String src = ((TakesScreenshot)
			// remoteDriver).getScreenshotAs(OutputType.BASE64);
			attachScreenshot();
			unHighlightElement(webElement);
		} catch (Exception e) {
			e.printStackTrace();
			logError("Error while taking the screenshot", e);
		}

	}

	public void addScreenshot(String filename) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		// String reportPath = new
		// File(ExecutionState.getCurrentReportsPath()).getAbsolutePath()+"/temp/"+filename;
		try {
			attachScreenshot();
		} catch (Exception e) {
			logError("Error while taking screenshot", e);
			e.printStackTrace();
		}
	}

	protected void capturePDFScreenshot(String pdfPath, int pagenumber) throws IOException {
		pdfUtil.savePdfAsImage(pdfPath, pagenumber, pagenumber);

		List<String> screenshotPath = pdfUtil.savePdfAsImage(pdfPath, pagenumber, pagenumber);
		System.out.println("Screenshot Path : " + screenshotPath);
		for (String string : screenshotPath) {
			File f = new File(string);
			String encodstring = encodeFileToBase64Binary(f);

			addScreenshot(encodstring);

		}

	}

	public String getInputText(WebElement element) {

		return element.getAttribute("value");

	}


	/**
	 * Highlights the webelement
	 * 
	 * @author Sagar Vyas
	 * @since 2018-09-26
	 * @param WebElement
	 *            Element to be highlighted
	 */
	protected void highlightElement(WebElement webElement) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			String style = (String) jsExecutor.executeScript("arguments[0].getAttribute('style')", webElement);
			jsExecutor.executeScript("arguments[0].setAttribute('style', 'border:4px dashed #F00')", webElement);
			stylesQueue.add(style);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void addCustomCSS(WebElement webElement, String css) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			String style = (String) jsExecutor.executeScript("arguments[0].getAttribute('style')", webElement);
			jsExecutor.executeScript("arguments[0].setAttribute('style', " + css + ")", webElement);
			stylesQueue.add(style);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Unhightlights the previously highlighted element. Only to be used after
	 * highlighting the element otherwise the Webelment will loose its orignal
	 * styling if any present previously.
	 * 
	 * @author Sagar Vyas
	 * @since 2018-09-26
	 * @param WebElement
	 *            Element to be unhighlighted
	 */
	protected void unHighlightElement(WebElement webElement) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			String style = stylesQueue.poll();
			jsExecutor.executeScript("arguments[0].setAttribute('style', '" + style + "')", webElement);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void switchToMainWindow() {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		remoteDriver.switchTo().defaultContent();
	}

	protected void openSingleNewTabJS() throws AWTException {
		pause(2000);
		((JavascriptExecutor) remoteDriver).executeScript("window.open()");
		System.out.println("Opened new tab Java Script Executor");

	}

	protected boolean isPresent(WebElement element) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		remoteDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		try {
			element.isDisplayed();
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			remoteDriver.manage().timeouts().implicitlyWait(PAGE_IMPLICIT_WAIT, TimeUnit.SECONDS);
		}
	}

	/**
	 * @author Sagar Vyas
	 * @since 2019-5-24
	 * @param Webelement
	 * @param waitTime
	 *            time for element to polled in milliseconds
	 * @param repeat
	 *            poll the step for number of time
	 */
	protected boolean checkForElementPresence(WebElement element, int repeat, int waitTime) {
		Boolean elementPresent = false;
		for (int i = 0; i < repeat; i++) {
			pause(waitTime);
			if (isPresent(element)) {
				elementPresent = true;
				break;
			}
		}
		return elementPresent;
	}

	protected Boolean isVisibleInViewport(WebElement element) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		return (Boolean) ((JavascriptExecutor) remoteDriver).executeScript(
				"var elem = arguments[0],                 " + "  box = elem.getBoundingClientRect(),    "
						+ "  cx = box.left + box.width / 2,         " + "  cy = box.top + box.height / 2,         "
						+ "  e = document.elementFromPoint(cx, cy); " + "for (; e; e = e.parentElement) {         "
						+ "  if (e === elem)                        " + "    return true;                         "
						+ "}                                        " + "return false;                            ",
				element);
	}

	protected boolean waitForElement(WebElement element) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		int tryCount = PAGE_IMPLICIT_WAIT / 2;
		int currentCount = 0;
		int time = 0;
		while (!isPresent(element) && currentCount++ < tryCount) {
			pause(2000);
			time = (time + 1);
			System.out.println(time);
		}
		if (isPresent(element))
			return true;
		else {
			remoteDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			try {
				element.isDisplayed();
				return true;
			} catch (Exception e) {
				remoteDriver.manage().timeouts().implicitlyWait(PAGE_IMPLICIT_WAIT, TimeUnit.SECONDS);
				throw e;
			}

		}
	}

	protected boolean waitForElementVisibility(WebElement ele)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			remoteDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			wait.until(ExpectedConditions.visibilityOf(ele));
			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception ", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption", e);
			throw e;
		} finally {
			remoteDriver.manage().timeouts().implicitlyWait(PAGE_IMPLICIT_WAIT, TimeUnit.SECONDS);
		}
	}

	protected boolean waitForElementClickabality(WebElement ele)
			throws NoSuchElementException, StaleElementReferenceException, TimeoutException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			remoteDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			wait.until(ExpectedConditions.elementToBeClickable(ele));
			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception while waiting for clickability of element. ", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption while waiting for clickability of element.", e);
			throw e;
		} catch (TimeoutException e) {
			logError("Timeout exeption while waiting for clickability of element.", e);
			throw e;
		} finally {
			remoteDriver.manage().timeouts().implicitlyWait(PAGE_IMPLICIT_WAIT, TimeUnit.SECONDS);
		}
	}

	protected boolean isPresent(String paramXpath, String value) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		remoteDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		try {
			remoteDriver.findElement(By.xpath(paramXpath.replaceAll(VAR, value)));
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			remoteDriver.manage().timeouts().implicitlyWait(PAGE_IMPLICIT_WAIT, TimeUnit.SECONDS);
		}
	}

	// protected String base64Decode(String input){
	// registerCall(new Object(){}.getClass().getEnclosingMethod().getName());
	// byte[] outputA = Base64.getDecoder().decode(input);
	// String output = "";
	// for (byte b : outputA) {
	// output = output + (char)b;
	// }
	// return output;
	// }

	protected String getEnvProperty(String key) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), key);
		return ExcelInput.fetchProperty("Environment_Data", key);
	}

	protected String getTestDataValue(String key) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), key);
		return ExcelInput.fetchData(state.getFeatureName(), state.getDataSetName(), key);
	}

	protected boolean setTestDataValue(String key, String value) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), "(" + key + "," + value + ")");
		return ExcelInput.updateData(state.getFeatureName(), state.getDataSetName(), key, value);
	}

	protected boolean setOutputDataValue(String key, String value) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), "(" + key + "," + value + ")");
		return ExcelOutput.setOutputDataValue(DEFAULT_OUTPUT_SHEET_NAME, key, value, state.getId());
	}

	protected boolean setColumnarDataValues(String key, String value) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), "(" + key + "," + value + ")");
		return ExcelOutput.setOutputDataValue(DEFAULT_OUTPUT_SHEET_NAME, key, value, state.getId());
	}

	protected List<String> getSaleDataValues(String columnName) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), "(" + columnName + ")");
		return ExcelInput.getColumnsAsList(saleSheetName, columnName);
	}

	protected boolean setSaleDataValues(String key, String value) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), "(" + key + "," + value + ")");
		return ExcelInput.updateData(saleSheetName, state.getDataSetName(), key, value);
	}

	protected String getExcelValue(String key) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), key);
		return ExcelInput.fetchData(state.getFeatureName(), state.getDataSetName(), key);
	}

	protected boolean clearSaleDataValues() {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		return ExcelInput.clearSheet(saleSheetName);
	}

	protected void setRuntimeData(String key, String data) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), "(" + key + "," + data + ")");
		getState().getScenarioMap().put(key, data);
	}

	protected void setRuntimeData(String key, int data) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), "(" + key + "," + data + ")");
		getState().getScenarioMap().put(key, data);
	}

	protected String getRuntimeData(String key) throws NullPointerException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), "(" + key + ")");
		String value;
		value = (String) getState().getScenarioMap().get(key);
		if (value == null)
			throw new NullPointerException();
		return value;
	}

	protected void setRuntimeDataList(String key, List<String> data) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), "(" + key + "," + data.toString() + ")");
		getState().getScenarioMap().put(key, data);
	}

	protected List<String> getRuntimeDataList(String key) throws NullPointerException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), "(" + key + ")");
		List<String> value;
		value = (List<String>) getState().getScenarioMap().get(key);
		if (value == null)
			throw new NullPointerException();
		return value;
	}

	protected boolean refresh() {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			remoteDriver.navigate().refresh();
			return true;
		} catch (Exception e) {
			logError("Exception while refreshing the page", e);
			throw e;
		}

	}

	protected boolean openNewTab(String baseUrl) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), baseUrl);
		// rb = new Robot();
		// rb.keyPress(KeyEvent.VK_CONTROL);
		// rb.keyPress(KeyEvent.VK_T);
		// rb.keyRelease(KeyEvent.VK_CONTROL);
		// rb.keyRelease(KeyEvent.VK_T);
		ArrayList<String> tabs = new ArrayList<String>(remoteDriver.getWindowHandles());
		System.out.println("TABS SIZE : " + tabs.size());
		System.out.println("OPENING NEW TAB ");
		Keys.chord(Keys.CONTROL, "t");
		System.out.println("OPENED NEW TAB now switching" + tabs.size());
		remoteDriver.switchTo().window(tabs.get(tabs.size() - 1));
		System.out.println("Switched to new ");
		pause(1000);
		remoteDriver.get(baseUrl);
		return true;
	}

	protected boolean startHarPage(String pageName) {
		getState().getDriverHandler().startNewPage(pageName);
		return false;
	}

	protected String getEnvironment() {
		return getEnvProperty("Environment");
	}

	/**
	 * @author Srushti Ramteke
	 * @since 2018-10-30
	 * @param This
	 *            method is to double click on an element
	 */
	protected boolean doubleClickAndEnterText(WebElement ele, String text) {
		actions.doubleClick(ele).sendKeys(text).build().perform();
		return true;

	}

	protected boolean doubleClick(WebElement ele) {
		actions.doubleClick(ele);
		// actions.moveToElement(ele).click().build().perform();
		return true;

	}

	/**
	 * @author Srushti Ramteke
	 * @since 2018-10-30
	 * @param To
	 *            come to the second window
	 */
	protected boolean switchToSecondTab() {
		ArrayList<String> tabs = new ArrayList<String>(remoteDriver.getWindowHandles());
		remoteDriver.switchTo().window(tabs.get(1));
		return true;

	}

	/**
	 * Enter data in alert box
	 * 
	 * @author Sagar Vyas
	 * @since 2019-2-2019
	 * @param String
	 *            text
	 * @throws AWTException
	 */
	protected boolean enterDataInAlert(String text) {
		System.out.println("Inside enter text");
		System.out.println("Text : " + remoteDriver.switchTo().alert().getText());

		remoteDriver.switchTo().alert().sendKeys(text);
		return true;
	}

	/**
	 * Check if alert is present
	 * 
	 * @author Sagar Vyas
	 * @since 2019-2-2019
	 * @param String
	 *            text
	 */
	protected boolean checkAlertPresence() {
		if (!(ExpectedConditions.alertIsPresent().equals(null))) {
			System.out.println("Alert is present");
			System.out.println("ALERT : " + remoteDriver.switchTo().alert().getText());
			embedScreenshot();
			return true;
		}

		else {
			System.out.println("Alert not present");
			return false;
		}

	}

	/**
	 * @author Srushti Ramteke
	 * @since 2018-10-30
	 * @param To
	 *            come to the third window
	 */
	protected boolean switchToThirdTab() {
		ArrayList<String> tabs = new ArrayList<String>(remoteDriver.getWindowHandles());
		remoteDriver.switchTo().window(tabs.get(2));
		return true;

	}

	protected boolean switchToForthTab() {
		ArrayList<String> tabs = new ArrayList<String>(remoteDriver.getWindowHandles());
		remoteDriver.switchTo().window(tabs.get(3));
		return true;

	}

	protected boolean switchToLastTab() {
		ArrayList<String> tabs = new ArrayList<String>(remoteDriver.getWindowHandles());
		
		remoteDriver.switchTo().window(tabs.get(tabs.size()-1));
		return true;

	}

	/**
	 * 
	 * @author Sagar Vyas
	 * @since 2019-04-22
	 * 
	 * @param String
	 *            pdfDownloadPath : Location of pdf file
	 * @param String
	 *            validationText : Text to be validated if present in the pdf
	 *            file for a particular page
	 * @param int
	 *            pageNumber : Page Number in PDF file where the text needs to
	 *            be searched
	 * 
	 * @throws IOException
	 * 
	 */
	protected void checkStringPresentInPDFPage(String pdfDownloadPath, String validationText, int pageNumber)
			throws IOException {
		boolean stringMatch;
		if (pdfUtil.getText(pdfDownloadPath, pageNumber, pageNumber).contains(validationText)) {
			logReportMessage(validationText + " present in the page : " + pageNumber + " - ", "Success");
			stringMatch = true;
		} else {
			logReportMessage(validationText + " not present in the page : " + pageNumber + " - ", "Failure");
			stringMatch = false;
		}
		// zAssertTrue(stringMatch, validationText+" present in the page :
		// "+pageNumber, validationText+" not present in the page :
		// "+pageNumber);
	}

	protected static String encodeFileToBase64Binary(File file) {
		String encodedfile = null;
		try {
			FileInputStream fileInputStreamReader = new FileInputStream(file);
			byte[] bytes = new byte[(int) file.length()];
			fileInputStreamReader.read(bytes);
			encodedfile = new String(Base64.encodeBase64(bytes), "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return encodedfile;
	}

	/**
	 * @author Nidhi
	 * @since 5-9-2019
	 * @param file
	 *            path
	 */

	protected void fileUpload(WebElement element, String fileName) {
		String filePath = UPLOAD_FILES_PATH + "/" + fileName;
		System.out.println(filePath);

		enterText(element, fileName);

		// File file = new File(filePath);
		// if (!file.exists()) {
		// return;
		// }
		// StringSelection stringSelection = new
		// StringSelection(file.getAbsolutePath());
		// Clipboard clipboard =
		// Toolkit.getDefaultToolkit().getSystemClipboard();
		// clipboard.setContents(stringSelection, null);
		//
		// Robot robot = null;
		//
		// try {
		// robot = new Robot();
		// } catch (AWTException e) {
		// e.printStackTrace();
		// }
		//
		// robot.delay(250);
		// robot.keyPress(KeyEvent.VK_ENTER);
		// robot.keyRelease(KeyEvent.VK_ENTER);
		// robot.keyPress(KeyEvent.VK_CONTROL);
		// robot.keyPress(KeyEvent.VK_V);
		// robot.keyRelease(KeyEvent.VK_V);
		// robot.keyRelease(KeyEvent.VK_CONTROL);
		// robot.keyPress(KeyEvent.VK_ENTER);
		// robot.delay(150);
		// robot.keyRelease(KeyEvent.VK_ENTER);

	}

	protected void roboClickEnter() {
		Robot robot = null;

		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.delay(150);
		robot.keyRelease(KeyEvent.VK_ENTER);
	}

	protected void clickEnter(WebElement element) {
		try {
			element.sendKeys(Keys.ENTER);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean switchToMainWindow1() {
		String title1 = "Christie's - Object Finder";
		String title2 = "TST COS";
		String baseHandel = null;
		Set<String> handles = getDriver().getWindowHandles();
		for (String handle : handles) {
			getDriver().switchTo().window(handle);
			System.out.println(getDriver().getTitle().trim());
			if (getDriver().getTitle().trim().contains(title1) || getDriver().getTitle().trim().contains(title2))
				baseHandel = getDriver().getWindowHandle();
			else
				getDriver().close();
		}
		getDriver().switchTo().window(baseHandel);

		return false;
	}
	
	public void configureAuth(WebDriverWait webdriverWait, String url, String username, String password) {
		pause(10000);
		getDriver().get("chrome-extension://mnloefcpaepkpmhaoipjkpikbnkmbnic/options.html");
		webdriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("http-host")));
		embedScreenshot(getDriver().findElement(By.id("http-host")));
		getDriver().findElement(By.id("http-host")).sendKeys(url);
		
		webdriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
		embedScreenshot(getDriver().findElement(By.id("username")));
		getDriver().findElement(By.id("username")).sendKeys(username);
		
		webdriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
		embedScreenshot(getDriver().findElement(By.id("password")));
		getDriver().findElement(By.id("password")).sendKeys(password);
		
		getDriver().findElement(By.id("general")).click();
		System.out.println("--------------------------");

	}
	protected boolean openURLOnAWS(String url) {
		WebDriverWait webdriverWait = new WebDriverWait(getDriver(), 5);
		String username = "svyas-nc";
		String password = "Everyday56";
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());

		try {
			if (getState().getExecutionProperty().getAddress().equalsIgnoreCase("172.18.1.195")) {
				System.out.println("Running on AWS---------------------------------------");
				configureAuth(webdriverWait, "webproxyus.christies.com", username, password);
				remoteDriver.get(url);
			}
			switchToMainWindow1();
			return true;
		} catch (WebDriverException e) {
			logError("Exception while opening the page", e);
			remoteDriver.navigate().refresh();
			return false;
		}
	}


}