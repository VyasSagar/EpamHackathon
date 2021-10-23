package com.helios.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.helios.datatypes.ExecutionProperty;
import com.helios.excel.ExcelExecutionProperty;
import com.helios.scriptutils.ExecutionPropertyStore;
import com.helios.scriptutils.ExecutionState;

import cucumber.api.SnippetType;
import cucumber.api.testng.CucumberFeatureWrapper;
import cucumber.api.testng.PickleEventWrapper;

public abstract class RunnerBase {
	
	protected Map<String, Object> getCucumberOptions(String tags){
		Map<String, Object> valuesMap = new HashMap<>();
		
		String jsonReportPath = "";
		if (!ExecutionEnvironment.isJenkinsBuild()) {
			jsonReportPath = "json:" + ExecutionState.getCurrentReportsPath() + "/json/CucumberTestReport_"
					+ this.getClass().getSimpleName() + ".json";
		} else {
			jsonReportPath = "json:target/cucumber_j/CucumberTestReport_"
					+ this.getClass().getSimpleName() + ".json";
		}
		
		String runnerName = this.getClass().getSimpleName();
		ArrayList<String> reporters = new ArrayList<>();
		reporters.add("pretty");
		reporters.add(jsonReportPath);
		reporters.add("html:" + ExecutionState.getCurrentReportsPath() + "/html/cucumber-pretty_"+ this.getClass().getSimpleName());
		reporters.add("rerun:" + ExecutionState.getCurrentReportsPath() + "/rerun/rerun_"+ this.getClass().getSimpleName() + ".txt");
//		if(ExcelExecutionProperty.isRunnerActive(runnerName)) reporters.add("com.epam.reportportal.cucumber.StepReporter");
//		if(ExcelExecutionProperty.isRunnerActive(runnerName)) reporters.add("com.epam.reportportal.cucumber.ScenarioReporter");
		
		String pl[] = reporters.toArray(new String[reporters.size()]);
		valuesMap.put("features", new String[] { "src/main/java/com/helios/features" });
		valuesMap.put("junit", new String[] {});
		valuesMap.put("dryRun", false);
		valuesMap.put("plugin",pl);
		valuesMap.put("glue", new String[] { "com.helios.stepDefinations" });
		valuesMap.put("extraGlue", new String[] {});
		valuesMap.put("snippets", SnippetType.UNDERSCORE);
		valuesMap.put("name", new String[] {});
		valuesMap.put("format", new String[] {});
		valuesMap.put("monochrome", true);
		valuesMap.put("strict", false);
		valuesMap.put("tags", new String[] { tags });
		return valuesMap;
	}
	
	
	protected PickleEventWrapper[] getScenarios(Object[][] scenariosBlob){
		String runnnerName = this.getClass().getSimpleName();	
		HashMap<String, ExecutionProperty> prop = ExecutionPropertyStore.get();
		HashMap<String, ExecutionProperty> prop2 = ExecutionPropertyStore.getScenariosInExecution();
		PickleEventWrapper[] scenarios = new PickleEventWrapper[ExecutionPropertyStore.getScenarioCount(runnnerName)];
		if(ExcelExecutionProperty.countRows(runnnerName) <=0) return  new PickleEventWrapper[0];
		PickleEventWrapper[] tempContainer = new PickleEventWrapper[ExcelExecutionProperty.countRows(runnnerName)];
		
		for (Object[] sc : scenariosBlob) {
			PickleEventWrapper scWrapper = (PickleEventWrapper) sc[0];
			CucumberFeatureWrapper ftWrapper = (CucumberFeatureWrapper) sc[1];
			String dataSetName = "none";
			String scenarioName = scWrapper.getPickleEvent().pickle.getName().split("#")[0].trim().toLowerCase();
			String featureName = ftWrapper.toString().toLowerCase().replace("\"", "");
			if (scWrapper.getPickleEvent().pickle.getName().split("#").length > 1)
				dataSetName = scWrapper.getPickleEvent().pickle.getName().split("#")[1];
			String identifier = featureName.trim() + "." + scenarioName.trim() + "." + dataSetName.trim();
			ExecutionProperty property = prop.get(identifier.toLowerCase());
			if (property != null 
					&& property.getRunner() != null 
					&& property.getRunner().equalsIgnoreCase(runnnerName) 
					&& property.isRunnerActive() 
					&& property.isToggle()) {
				
				String fName = StringUtils.substringBetween(scWrapper.getPickleEvent().uri, "features/", ".");
				String fIdentifier = fName.trim() + "." + scenarioName.trim() + "." + dataSetName.trim();
				property.setFileIdentifier(fIdentifier);
				prop2.put(fIdentifier.toLowerCase(), property);
				tempContainer[(property.getOrder()) - 1] = scWrapper;
			}
		}
		int index = 0;
		for (PickleEventWrapper s : tempContainer) {
			if (s != null)
				scenarios[index++] = s;
		}
		return scenarios;
	}
	
	
	
	
	
}