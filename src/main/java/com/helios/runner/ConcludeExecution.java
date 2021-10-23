package com.helios.runner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import com.helios.excel.ExcelExecutionProperty;
import com.helios.scriptutils.ExecutionState;
import com.helios.slack.SlackClient;
import com.helios.utilities.ExecutionEnvironment;
import com.helios.utilities.StringLiterals;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.Reportable;

public class ConcludeExecution implements StringLiterals {

	@Test
	public void stub() {
		// Placeholder function
	}

	@AfterClass(alwaysRun = true)
	public void endExecution() {
		String reportURL = "Not Available";
		try {
			if (ExcelExecutionProperty.getGlobalExecutionProperty("Slack Update").equalsIgnoreCase("Yes")) {
				String channelName = ExcelExecutionProperty.getGlobalExecutionProperty("Slack Channel");
				SlackClient.endSlack();
				String response = SlackClient.uploadFlie(
						ExecutionState.getCurrentReportsPath() + "/" + OUTPUT_EXCEL_FILE, "Execution Output",
						OUTPUT_EXCEL_FILE.split("\\.")[1], channelName);

				File file = new File(ExecutionState.getCurrentReportsPath() + "/HAR");

				if (file.exists() && file.isDirectory()) {
					File[] harList = file.listFiles();
					for (File har : harList) {
						SlackClient.uploadFlie(har.getAbsolutePath(), har.getName(), "har", channelName);
					}
				}
				JSONObject responseObject = new JSONObject(response);
				reportURL = responseObject.getJSONObject("file").getString("url_private_download");
			}
		} catch (Exception e) {
			System.out.println("Final slack update failed.");
		}
		if(!ExecutionEnvironment.isJenkinsBuild())generateReport();
		System.out.println(
				"============================Execution Complete==============================================================");

	}

	private void generateReport() {

		File reportOutputDirectory = new File(ExecutionState.getCurrentReportsPath());
		List<String> jsonFiles = new ArrayList<>();
		File file = new File(ExecutionState.getCurrentReportsPath() + "/json");
		if (file.exists() && file.isDirectory() && file.listFiles().length > 0) {
			for (File jFile : file.listFiles()) {
				jsonFiles.add(jFile.getAbsolutePath());
			}
		} else {
			System.out.println("Can't generate report because cucmber json output directory not found");
			return;
		}
		String projectName = PROJECT_NAME;
		boolean runWithJenkins = ExecutionEnvironment.isJenkinsBuild();
		Configuration configuration = new Configuration(reportOutputDirectory, projectName);
		// optional configuration - check javadoc
		configuration.setRunWithJenkins(false);
		if (runWithJenkins)
			configuration.setBuildNumber(ExecutionEnvironment.getBuildNumber());
		if (runWithJenkins)
			configuration.addClassifications("Jenkins Jobname", ExecutionEnvironment.getJobName());
		if (runWithJenkins)
			configuration.addClassifications("Jenkins Buildnumber", ExecutionEnvironment.getBuildNumber());
		configuration.addClassifications("Machine IP", ExecutionEnvironment.getHostIp());
		configuration.addClassifications("Hostname", ExecutionEnvironment.getHostName());
		configuration.setParallelTesting(true);
		ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
		Reportable result = reportBuilder.generateReports();
		try {
			System.out.println("Reports Path");
			System.out.println(
					"============================Execution Complete==============================================================");

			System.out.println("\n" + new File(ExecutionState.getCurrentReportsPath()).getAbsolutePath() + "\n");

		} catch (Exception e) {
			System.out.println("Reports directory not found.");
		}

	}

}
