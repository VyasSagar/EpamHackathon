package com.helios.scriptutils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.SkipException;

import com.helios.datatypes.ExecutionProperty;
import com.helios.utilities.DriverHandler;
import com.helios.utilities.StringLiterals;

import cucumber.api.Scenario;

public class ScenarioState implements StringLiterals {

	private int id;
	private Scenario scenario;
	private String featureName;
	private String scenarioName;
	private String dataSetName;
	private DriverHandler driverHandler;
	private List<ScenarioEventListener> listeners = new ArrayList<>();
	private EventFiringWebDriver eventDriver;
	private String currentPageObject = "##";
	private Map<String,Object> runtimeData = new HashMap<>();
	private String runnerName = "";
	private String status;
	private String result;
	private String totalExecutionTime;
	private long startTime;
	private long endTime;
	private boolean isBrowserMobEnabled = false;
	private ExecutionProperty executionProperty;
	public void attach(ScenarioEventListener eventListener) {
		listeners.add(eventListener);
		eventDriver = new EventFiringWebDriver(getDriver());
		eventDriver.register(eventListener);
	}

	public ScenarioState() {
		startTime = System.currentTimeMillis();
		status = "INITIALIZING";
		totalExecutionTime = "0";
	}
	
	
	
	public DriverHandler getDriverHandler(){
		return this.driverHandler;
	}

	public String getFeatureName() {
		return featureName;
	}

	public String getScenarioName() {
		return scenarioName;
	}

	public String getDataSetName() {
		return dataSetName;
	}

	public Scenario getScenario() {
		return scenario;
	}

	RemoteWebDriver getDriver() {
		return driverHandler.getDriver();
	}

	public String getRunnerName() {
		return runnerName;
	}
	
	
	// -------------------------------------------------LifeCycle

	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void registerGlobalFunctionCall(String pageObjectName, String functionName) {
		setCurrentPageObject(pageObjectName);
		notifyGlobalFunctionCall(pageObjectName, functionName);
	}

	public void registerGlobalFunctionCall(String pageObjectName, String functionName, String data) {
		setCurrentPageObject(pageObjectName);
		notifyGlobalFunctionCall(pageObjectName, functionName + " | " + data);
	}

	public String getCurrentPageObject() {
		return currentPageObject;
	}

	public void setCurrentPageObject(String currentPageObject) {
		if (!currentPageObject.equalsIgnoreCase(this.currentPageObject)) {
			notifyPageObjectChange(this.currentPageObject, currentPageObject);
		}
		this.currentPageObject = currentPageObject;
	}

	public void init(Scenario scenario) {
		this.scenario = scenario;
		id = ExecutionState.scenarioIdCounter++;
		featureName = StringUtils.substringBetween(scenario.getId(), "features/", ".");
		scenarioName = scenario.getName().split("#")[0];
		if (scenario.getName().contains("#") && !scenarioName.contains("<DataSetName>")) {
			dataSetName = scenario.getName().split("#")[1];
		} else {
			dataSetName = "NONE";
		}
		String identifier = featureName.trim()+"." + scenarioName.trim() + "."+dataSetName.trim();
		executionProperty = ExecutionPropertyStore.getScenariosInExecution().get(identifier.toLowerCase());
		featureName = executionProperty.getFeatureName();
		this.runnerName = executionProperty.getRunner();
		initDriverHandler();
//		throw new SkipException("Scenario Skipped");
	}
	
	public void initComplete(){
		notifyScenarioStart();
	}
	
	public void destroy() {
		endTime = System.currentTimeMillis();
		long elapsedTime = endTime - startTime;
		Duration d = Duration.ofMillis(elapsedTime );
		long minutes = d.toMinutes();
		totalExecutionTime = minutes + " M";
		
		if (!scenario.isFailed())
			notifyScenarioPassed();
		if (scenario.isFailed())
			notifyScenarioFailed();
		
		notifyScenarioEnd();
		driverHandler.tearDown(scenario.isFailed());
	}

	public EventFiringWebDriver getEventFiringDriver() {
		return this.eventDriver;
	}

	// -------------------------------------------------LifeCycle

	// Notifiers--------------------------------------------------------------

	private void notifyScenarioStart() {
		status = "RUNNING";
		result = "PENDING";
		for (ScenarioEventListener scenarioEventListener : listeners) {
			try {
				scenarioEventListener.onScenarioStart(this);
			} catch (Exception e) {
				
				System.err.println("[BROADCAST FAILED]\t " + new Object() {
				}.getClass().getEnclosingMethod().getName() + " - " + scenarioEventListener.getClass().getName());
				e.printStackTrace();
			}

		}
	}

	private void notifyScenarioEnd() {
		status = "COMPLETED";
		for (ScenarioEventListener scenarioEventListener : listeners) {
			try {
				scenarioEventListener.onScenarioEnd(this);
			} catch (Exception e) {
				System.err.println("[BROADCAST FAILED]\t " + new Object() {
				}.getClass().getEnclosingMethod().getName() + " - " + scenarioEventListener.getClass().getName());
				e.printStackTrace();
			}
		}
	}

	private void notifyScenarioPassed() {
		result = "PASSED";
		for (ScenarioEventListener scenarioEventListener : listeners) {
			try {
				scenarioEventListener.onScenarioPass(this);
			} catch (Exception e) {
				System.err.println("[BROADCAST FAILED]\t " + new Object() {
				}.getClass().getEnclosingMethod().getName() + " - " + scenarioEventListener.getClass().getName());
				e.printStackTrace();
			}
		}
	}

	private void notifyScenarioFailed() {
		result = "FAILED";
		for (ScenarioEventListener scenarioEventListener : listeners) {
			try {
				scenarioEventListener.onScenarioFailed(this);
			} catch (Exception e) {
				System.err.println("[BROADCAST FAILED]\t " + new Object() {
				}.getClass().getEnclosingMethod().getName() + " - " + scenarioEventListener.getClass().getName());
				e.printStackTrace();
			}
		}
	}

	private void notifyPageObjectChange(String previous, String current) {
		for (ScenarioEventListener scenarioEventListener : listeners) {
			try {
				scenarioEventListener.onPageObjectChange(this, previous, current);
			} catch (Exception e) {
				System.err.println("[BROADCAST FAILED]\t " + new Object() {
				}.getClass().getEnclosingMethod().getName() + " - " + scenarioEventListener.getClass().getName());
				e.printStackTrace();
			}
		}

	}

	private void notifyGlobalFunctionCall(String pageObjectName, String functionInfo) {

		for (ScenarioEventListener scenarioEventListener : listeners) {
			try {
				scenarioEventListener.onGlobalFunctionCalled(this, pageObjectName, functionInfo);
			} catch (Exception e) {
				System.err.println("[BROADCAST FAILED]\t " + new Object() {
				}.getClass().getEnclosingMethod().getName() + " - " + scenarioEventListener.getClass().getName());
				e.printStackTrace();
			}
		}
	}

	public Map<String, Object> getScenarioMap() {
		return runtimeData;
	}

	
	
	

	// Notifiers--------------------------------------------------------------

	public String getStatus() {
		return status;
	}

	public String getResult() {
		return result;
	}

	public String getTotalExecutionTime() {
		return totalExecutionTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void enableMobProxy() {
		this.isBrowserMobEnabled = true;	
	}
	
	public boolean isMobProxyEnabled(){
		return this.isBrowserMobEnabled;
	}

	public void initDriverHandler() {
		driverHandler = new DriverHandler(this);
		
	}
	
	public ExecutionProperty getExecutionProperty(){
		return this.executionProperty;
	}
	
	
	
}
