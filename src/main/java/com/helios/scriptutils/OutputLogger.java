package com.helios.scriptutils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.helios.excel.ExcelOutput;
import com.helios.utilities.StringLiterals;

public class OutputLogger implements ScenarioEventListener,StringLiterals {

	long startTime;
	long endTime;
	
	
	public OutputLogger() {
//		ExcelOutput.init();
	}
	
	@Override
	public void beforeAlertAccept(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterAlertAccept(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterAlertDismiss(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeAlertDismiss(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeNavigateTo(String url, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterNavigateTo(String url, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeNavigateBack(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterNavigateBack(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeNavigateForward(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterNavigateForward(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeNavigateRefresh(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterNavigateRefresh(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeFindBy(By by, WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterFindBy(By by, WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeClickOn(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterClickOn(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeScript(String script, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterScript(String script, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeSwitchToWindow(String windowName, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterSwitchToWindow(String windowName, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onException(Throwable throwable, WebDriver driver) {
		
		
	}

	@Override
	public <X> void beforeGetScreenshotAs(OutputType<X> target) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <X> void afterGetScreenshotAs(OutputType<X> target, X screenshot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeGetText(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterGetText(WebElement element, WebDriver driver, String text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScenarioStart(ScenarioState scenarioState) {
		startTime = System.currentTimeMillis();
		int id = scenarioState.getId();
		String strQuery = "INSERT INTO " + DEFAULT_OUTPUT_SHEET_NAME + "(ID,Feature,Scenario) " + "VALUES('" + id
				+ "','" + scenarioState.getFeatureName() + "','"+scenarioState.getScenarioName()+"')";
		ExcelOutput.executeInsertStatement(strQuery);
		ExcelOutput.setOutputDataValue(DEFAULT_OUTPUT_SHEET_NAME, "DataSetName", scenarioState.getDataSetName()  , id);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");  
	    LocalDateTime now = LocalDateTime.now();  
	    String startTime = dtf.format(now);
	    ExcelOutput.setOutputDataValue(DEFAULT_OUTPUT_SHEET_NAME, "Start Time", startTime  , id);
	    ExcelOutput.setOutputDataValue(DEFAULT_OUTPUT_SHEET_NAME, "Runner Name", scenarioState.getRunnerName()  , id);
	}

	@Override
	public void onScenarioEnd(ScenarioState scenarioState) {
		int id = scenarioState.getId();
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
	    String time = dtf.format(now);
	    ExcelOutput.setOutputDataValue(DEFAULT_OUTPUT_SHEET_NAME, "End Time", time  , id);
	    ExcelOutput.setOutputDataValue(DEFAULT_OUTPUT_SHEET_NAME, "Status", scenarioState.getScenario().getStatus().toString()  , id);
		endTime = System.currentTimeMillis();
		long elapsedTime = endTime - startTime;
		Duration d = Duration.ofMillis(elapsedTime ) ;
		long minutes = d.toMinutes();
		ExcelOutput.setOutputDataValue(DEFAULT_OUTPUT_SHEET_NAME, "ExecutionTime", minutes+""  , id);
	}

	@Override
	public void onScenarioPass(ScenarioState scenarioState2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScenarioFailed(ScenarioState scenarioState) {
		scenarioState.getScenario().write("Scenario failed on " + scenarioState.getCurrentPageObject());
		try{
			TakesScreenshot camera = (TakesScreenshot) scenarioState.getDriverHandler().getDriver();
	        byte[] screenshot = camera.getScreenshotAs(OutputType.BYTES);
	        scenarioState.getScenario().embed(screenshot, "image/png");
		}catch(Exception e){
			scenarioState.getScenario().write("Failed to take screenshot.");
		}
		
	}

	@Override
	public void onPageObjectChange(ScenarioState scenarioState, String previous, String current) {
		scenarioState.getScenario().write("[PageChanged]\t" + previous + " > " + current);
	}

	@Override
	public void onGlobalFunctionCalled(ScenarioState scenarioState, String pageObjectName, String functionInfo) {
		scenarioState.getScenario().write("[Action]\t" + functionInfo);
	}

}
