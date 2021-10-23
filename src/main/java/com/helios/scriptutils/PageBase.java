package com.helios.scriptutils;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class PageBase extends GlobalFunction{

	public PageBase(ScenarioState state) {
		super(state);
	}

	Robot robot;
	protected boolean checkPageLoad() {
		boolean status = false;
		for (int i = 0; i < 30; i++) {
			if (jsExecutor.executeScript("return document.readyState").toString().equalsIgnoreCase("Complete")) {
				status = true;
			} else {
				status = false;
			}

		}
		return status;
	}

	public boolean pressCTRL_ALT_Plus(Character key) throws Throwable {
		try {
			robot.keyPress(KeyEvent.VK_ALT);
			robot.keyPress(KeyEvent.VK_CONTROL);
			key = ("" + key).toUpperCase().charAt(0);
			robot.keyPress((int) key);
			System.out.println("Send Keys : CTRL + ALT + " + key);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logReportMessage(e.getMessage());
			return false;
		} finally {
			robot.keyRelease(KeyEvent.VK_ALT);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			key = ("" + key).toUpperCase().charAt(0);
			robot.keyRelease((int) key);
			System.out.println("Release Keys : CTRL + ALT + " + key);
		}
	}

	public boolean jsActionsCatalogueText() {
		pause(60000);
		try {
			robot = new Robot();
			robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
			pause(10000);
			robot.keyPress(KeyEvent.VK_F11);
			robot.keyRelease(KeyEvent.VK_F11);
			robot.mouseMove(20, 20);
			pause(5000);
			robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
			pause(500);
			robot.keyPress(KeyEvent.VK_RIGHT);
			robot.keyRelease(KeyEvent.VK_RIGHT);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
			robot.keyPress(KeyEvent.VK_F11);
			robot.keyRelease(KeyEvent.VK_F11);
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>");
		} catch (AWTException e) {
			e.printStackTrace();
		}

		return true;
	}


	protected boolean KeyFunctionBackAndSpace(WebElement element) throws Throwable {
		try {
			click(element);
			actions.sendKeys(Keys.ARROW_LEFT);
			actions.sendKeys(Keys.SPACE);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logReportMessage(e.getMessage());
			return false;
		}
	}

	protected boolean scrollToElementAndEnterText(WebElement moveToelement, WebElement element, String text)
			throws Throwable {
		try {
			int count = 0;
			while (!isVisibleInViewport(moveToelement) && count < 20) {
				scrollToElement(moveToelement);
				pause(100);
			}
			// jsExecutor.executeScript("arguments[0].scrollIntoView(true);",
			// moveToelement);
			pause(200);
			clickAndEnterText(element, text);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logReportMessage(e.getMessage());
			return false;
		}
	}

	protected String setDate(int days) throws Throwable {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(new Date());
			c.add(Calendar.DATE, days);
		} catch (Exception e) {
			logReportMessage(e.getMessage());
			e.printStackTrace();
		}
		return sdf.format(c.getTime());
	}

//	protected String getInputText(WebElement element) throws Throwable {
//		String text = null;
//		try {
//			text = element.getAttribute("value");
//		} catch (Exception e) {
//			logReportMessage(e.getMessage());
//			e.printStackTrace();
//		}
//		return text;
//	}

	protected boolean rightClickOnElement(WebElement element) throws Throwable {
		try {
			actions.contextClick(element).build().perform();
			return true;
		} catch (Exception e) {
			logReportMessage(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	protected void doubleClickOnElement(WebElement element) {
		actions.doubleClick(element).build().perform();
	}

	protected boolean roboClick(WebElement element) {
		Point point = element.getLocation();
		robot.mouseMove(point.getX(), point.getY());
		System.out.println(point.getX() + "   " + point.getY());
		pause(100);
		robot.mousePress(InputEvent.BUTTON1_MASK);
		pause(100);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
		return true;
	}

	protected boolean checkBoxSelectIfNotSelected(WebElement element) throws Throwable {
		try {
			if (!element.isSelected()) {
				element.click();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logReportMessage(e.getMessage());
			return false;
		}
	}



	

}
