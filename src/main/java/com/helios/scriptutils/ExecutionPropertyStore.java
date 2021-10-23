package com.helios.scriptutils;

import java.util.HashMap;
import java.util.List;

import com.helios.datatypes.ExecutionProperty;
import com.helios.excel.ExcelExecutionProperty;
import com.helios.excel.ExcelInput;

public class ExecutionPropertyStore {

	private ExecutionPropertyStore() {

	}
	
	private static HashMap<String, ExecutionProperty> hashMap = null;
	
//	private static HashMap<String, HashMap<String, ExecutionProperty>> propertiesMap = new HashMap<>();
	
	private static HashMap<String, ExecutionProperty> scenariosForExecution = new HashMap<>();
	
	private static HashMap<String, Integer> scenariosCount = new HashMap<>();
	
	public static synchronized HashMap<String, ExecutionProperty> get() {
		if (hashMap == null) {
			hashMap = new HashMap<>();
			String slackChannel = ExcelExecutionProperty.getGlobalExecutionProperty("Slack Channel");
			boolean slackUpdateOn = ExcelExecutionProperty.getGlobalExecutionProperty("Slack Update").trim()
					.equalsIgnoreCase("yes");
			String resolution = ExcelExecutionProperty.getGlobalExecutionProperty("Resolution");
			List<HashMap<String, String>> runners = ExcelExecutionProperty.getRunnersList();
			for (HashMap<String, String> runner : runners) {
				String runnerName = runner.get("RunnerName");
				String hostname = runner.get("Hostname");
				String port = runner.get("Port");
				boolean isRunnerActive = runner.get("Is Active").trim().equalsIgnoreCase("yes");
				if(!isRunnerActive) continue;
				scenariosCount.put(runnerName, 0);
				List<HashMap<String, String>> scenarios = ExcelExecutionProperty.getScenarioList(runnerName);
				for (HashMap<String, String> scenario : scenarios) {
					if(!scenario.get("Switch").trim().equalsIgnoreCase("yes")) continue;
					ExecutionProperty executionProperty = new ExecutionProperty();
					executionProperty.setAddress(hostname);
					executionProperty.setPort(port);
					executionProperty.setRunnerActive(isRunnerActive);
					executionProperty.setBrowser(scenario.get("Browser"));
					executionProperty.setDataSetName(scenario.get("DataSetName"));
					executionProperty.setFeatureName(scenario.get("FeatureName"));
					executionProperty.setExecutionType(scenario.get("ExecutionType"));
					executionProperty.setScenarioName(scenario.get("ScenarioName"));
					executionProperty.setOrder(Integer.parseInt(scenario.get("Order")));
					executionProperty.setResolution(resolution);
					executionProperty.setRunner(runnerName);
					int retryCount = 0;
					try {
						retryCount = Integer.parseInt(scenario.get("Retry Count"));
					} catch (Exception e) {
						System.out.println("Could not fetch retry count, setting it to 0");
					}
					executionProperty.setRetryCount(retryCount);
					executionProperty.setToggle(scenario.get("Switch").trim().equalsIgnoreCase("yes"));
					executionProperty.setSlackUpdate(slackUpdateOn);
					executionProperty.setSlackChannel(slackChannel);
					hashMap.put(executionProperty.getIdentifier(), executionProperty);
					scenariosCount.put(runnerName, ((Integer)scenariosCount.get(runnerName))+1);
				}
			}
		}
		return hashMap;
	}
	
	
	
	
	public static synchronized int getScenarioCount(String runnerName){
		return scenariosCount.containsKey(runnerName) ? scenariosCount.get(runnerName) : 0;
	}
	
//	public static synchronized HashMap<String, ExecutionProperty> get(String rName){
//		if(!propertiesMap.containsKey(rName)){
//			HashMap<String, ExecutionProperty> hashMap = new HashMap<>();
//			String slackChannel = ExcelExecutionProperty.getGlobalExecutionProperty("Slack Channel");
//			boolean slackUpdateOn = ExcelExecutionProperty.getGlobalExecutionProperty("Slack Update").trim()
//					.equalsIgnoreCase("yes");
//			String resolution = ExcelExecutionProperty.getGlobalExecutionProperty("Resolution");
//			List<HashMap<String, String>> runners = ExcelExecutionProperty.getRunnersList();
//			for (HashMap<String, String> runner : runners) {
//				if(!runner.get("RunnerName").equalsIgnoreCase(rName)) continue;
//				String runnerName = runner.get("RunnerName");
//				String hostname = runner.get("Hostname");
//				String port = runner.get("Port");
//				boolean isRunnerActive = runner.get("Is Active").trim().equalsIgnoreCase("yes");
//				List<HashMap<String, String>> scenarios = ExcelExecutionProperty.getScenarioList(runnerName);
//				for (HashMap<String, String> scenario : scenarios) {
//					ExecutionProperty executionProperty = new ExecutionProperty();
//					executionProperty.setAddress(hostname);
//					executionProperty.setPort(port);
//					executionProperty.setRunnerActive(isRunnerActive);
//					executionProperty.setBrowser(scenario.get("Browser"));
//					executionProperty.setDataSetName(scenario.get("DataSetName"));
//					executionProperty.setFeatureName(scenario.get("FeatureName"));
//					executionProperty.setExecutionType(scenario.get("ExecutionType"));
//					executionProperty.setScenarioName(scenario.get("ScenarioName"));
//					executionProperty.setResolution(resolution);
//					executionProperty.setRunner(runnerName);
//					executionProperty.setRunnerActive(isRunnerActive);
//					int retryCount = 0;
//					try {
//						retryCount = Integer.parseInt(scenario.get("Retry Count"));
//					} catch (Exception e) {
//						System.out.println("Could not fetch retry count, setting it to 0");
//					}
//					executionProperty.setRetryCount(retryCount);
//					executionProperty.setToggle(scenario.get("Switch").trim().equalsIgnoreCase("yes"));
//					executionProperty.setSlackUpdate(slackUpdateOn);
//					executionProperty.setSlackChannel(slackChannel);
//					hashMap.put(executionProperty.getIdentifier(), executionProperty);
//				}
//			}
//		}
//		return propertiesMap.get(rName);
//		
//	}
	
	
	
	public static synchronized HashMap<String, ExecutionProperty> getScenariosInExecution(){
		return scenariosForExecution;
	}
	
}
