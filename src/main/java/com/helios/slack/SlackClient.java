package com.helios.slack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.helios.datatypes.APIBean;
import com.helios.excel.ExcelExecutionProperty;
import com.helios.excel.ExcelInput;
import com.helios.scriptutils.ExecutionState;
import com.helios.scriptutils.ScenarioEventListener;
import com.helios.scriptutils.ScenarioState;
import com.helios.utilities.ApiClient;
import com.helios.utilities.ExecutionEnvironment;
import com.helios.utilities.StringLiterals;

public class SlackClient implements ScenarioEventListener,StringLiterals {
	
	private static final String OAUTH_KEY = "Bearer xoxb-405580933268-461962978516-r5YB0KYwJtGBRvrtA7v7nyX8";
	private static String messageTimeStamp = null;
	
	private static final String SLACK_POST_URL = "https://slack.com/api/chat.postMessage";
	private static final String SLACK_UPDATE_URL = "https://slack.com/api/chat.update";
	private static final String SLACK_FILE_UPLOAD_URL = "https://slack.com/api/files.upload";
	
	private static ArrayList<ScenarioState> scenarios = new ArrayList<>();
	
	private static String getSlackChannel(){
		return ExcelExecutionProperty.getGlobalExecutionProperty("Slack Channel");
	}
	
	
	@Override
	public void beforeAlertAccept(WebDriver driver) {
		
		
	}

	@Override
	public void afterAlertAccept(WebDriver driver) {
		
		
	}

	@Override
	public void afterAlertDismiss(WebDriver driver) {
		
		
	}

	@Override
	public void beforeAlertDismiss(WebDriver driver) {
		
		
	}

	@Override
	public void beforeNavigateTo(String url, WebDriver driver) {
		
		
	}

	@Override
	public void afterNavigateTo(String url, WebDriver driver) {
		
		
	}

	@Override
	public void beforeNavigateBack(WebDriver driver) {
		
		
	}

	@Override
	public void afterNavigateBack(WebDriver driver) {
		
		
	}

	@Override
	public void beforeNavigateForward(WebDriver driver) {
		
		
	}

	@Override
	public void afterNavigateForward(WebDriver driver) {
		
		
	}

	@Override
	public void beforeNavigateRefresh(WebDriver driver) {
		
		
	}

	@Override
	public void afterNavigateRefresh(WebDriver driver) {
		
		
	}

	@Override
	public void beforeFindBy(By by, WebElement element, WebDriver driver) {
		
		
	}

	@Override
	public void afterFindBy(By by, WebElement element, WebDriver driver) {
		
		
	}

	@Override
	public void beforeClickOn(WebElement element, WebDriver driver) {
		
		
	}

	@Override
	public void afterClickOn(WebElement element, WebDriver driver) {
		
		
	}

	@Override
	public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
		
		
	}

	@Override
	public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
		
		
	}

	@Override
	public void beforeScript(String script, WebDriver driver) {
		
		
	}

	@Override
	public void afterScript(String script, WebDriver driver) {
		
		
	}

	@Override
	public void beforeSwitchToWindow(String windowName, WebDriver driver) {
		
		
	}

	@Override
	public void afterSwitchToWindow(String windowName, WebDriver driver) {
		
		
	}

	@Override
	public void onException(Throwable throwable, WebDriver driver) {
		
		
	}

	@Override
	public <X> void beforeGetScreenshotAs(OutputType<X> target) {
		
		
	}

	@Override
	public <X> void afterGetScreenshotAs(OutputType<X> target, X screenshot) {
		
		
	}

	@Override
	public void beforeGetText(WebElement element, WebDriver driver) {
		
		
	}

	@Override
	public void afterGetText(WebElement element, WebDriver driver, String text) {
		
		
	}

	@Override
	public void onScenarioStart(ScenarioState scenarioState) {
		scenarios.add(scenarioState);
		updateSlack();
	}

	

	@Override
	public void onScenarioEnd(ScenarioState scenarioState) {
		updateSlack();
		
	}

	@Override
	public void onScenarioPass(ScenarioState scenarioState2) {
		
		
	}

	@Override
	public void onScenarioFailed(ScenarioState scenarioState) {
		
		
	}

	@Override
	public void onPageObjectChange(ScenarioState scenarioState, String previous, String current) {
		
		
	}

	@Override
	public void onGlobalFunctionCalled(ScenarioState scenarioState, String pageObjectName, String functionInfo) {
		
		
	}
	
	
	public static void main(String[] args) {
//		SlackClient.messageTimeStamp = "1543492188.075700";
//		uploadFlie("C:\\Users\\Zenuser\\git\\Christies-.comu\\support_files\\OutputTemplate.xlsx", "testfile", "xls");
		
//		SlackMessage message = new SlackMessage();
//		message.setText("SAGAR testing slack");
//		message.setChannel("GDB94QX71");
//		message.setTs("1543920764.023100");
//		
//		SlackAttachment attachment = new SlackAttachment();
//		attachment.setTitle("Title");
//		attachment.setPretext("Pretext");
//		attachment.setText("TEXT");
//		attachment.setColor("#000");
//		
//		SlackField field = new SlackField();
//		field.setTitle("Something");
//		field.setValue("Value");
//		field.setShort(true);
//		
//		SlackField field2 = new SlackField();
//		field2.setTitle("Something");
//		field2.setValue("Value");
//		field2.setShort(true);
//		
//		SlackField field3 = new SlackField();
//		field3.setTitle("Something");
//		field3.setValue("Value");
//		field3.setShort(false);
//		
//		ArrayList<SlackField> feilds = new ArrayList<>();
//		feilds.add(field);
//		feilds.add(field2);
//		feilds.add(field3);
//		
//		attachment.setFields(feilds);
//		ArrayList<SlackAttachment> list = new ArrayList<>();
//		list.add(attachment);
//		message.setAttachments(list);
//		makeRequest(message);
		
		uploadFlie("C:\\manveer\\Projects\\Automation_Framework_r1\\Reports\\Prototype_2018-11-24_10-30-59\\OutputData.xlsx", "TestFile.xlsx", "xlsx","GDB94QX71","");
	}
	

	public static synchronized void initSlack() {
		if(!ExcelInput.fetchProperty("ExecutionProperties", "Slack Update").equals("Yes")) return;
		SlackMessage message = new SlackMessage();
		message.setChannel(getSlackChannel());
		message.setText(DESCRIPTION + " - *Intializing*");
		makeRequest(message);
	}
	
	
	
	
	public static synchronized void endSlack() {
		SlackMessage message = buildMessage();
		message.setText(DESCRIPTION + " - *Execution Completed*");
		makeRequest(message);
		
		
		
	}
	
	public static synchronized String uploadFlie(String filePath, String fileName, String fileType , String channelName) {
		ApiClient apiClient = new ApiClient();
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Authorization", OAUTH_KEY);
		
		HashMap<String , String> fields = new HashMap<>();
		    fields.put("channels", channelName);
		    fields.put("filetype", fileType);
		    fields.put("thread_ts", messageTimeStamp);
		    fields.put("title", fileName);
		String response = apiClient.uploadFile(SLACK_FILE_UPLOAD_URL, filePath, "file", headers, fields);
		System.out.println("[SLACK UPLOAD RESPONSE]\n"+response);
		return response;
	}
	
	public static synchronized String uploadFlie(String filePath, String fileName, String fileType , String channelName, String messageTimeStamp) {
		ApiClient apiClient = new ApiClient();
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Authorization", OAUTH_KEY);
		
		HashMap<String , String> fields = new HashMap<>();
		    fields.put("channels", channelName);
		    fields.put("filetype", fileType);
		    fields.put("thread_ts", messageTimeStamp);
		    fields.put("title", fileName);
		String response = apiClient.uploadFile(SLACK_FILE_UPLOAD_URL, filePath, "file", headers, fields);
		System.out.println("[SLACK UPLOAD RESPONSE]\n"+response);
		return response;
	}
	
	
	
	private static synchronized void updateSlack() {
		SlackMessage message = buildMessage();
		makeRequest(message);
	}
	
	
	private static synchronized void makeRequest(SlackMessage message) {
		APIBean apiBean = buildRequest(message);
		ApiClient apiClient = new ApiClient();
		apiClient.request(apiBean);
		System.out.println("[SLACK POST REQUEST]" + "\n" +  apiBean.getRequestPayload());
		System.out.println("[SLACK POST RESPONSE]" + "\n" +  apiBean.getActualResponse());
		JSONObject response = new JSONObject(apiBean.getActualResponse());
		messageTimeStamp = response.getString("ts");
	}

	private static synchronized APIBean buildRequest(SlackMessage message) {
		APIBean apiBean = new APIBean();
		apiBean.setMethod("POST");
		if(messageTimeStamp == null) {
			if(message.getTs()  != null) apiBean.setUrl(SLACK_UPDATE_URL);
			else apiBean.setUrl(SLACK_POST_URL);
		}else {
			apiBean.setUrl(SLACK_UPDATE_URL);
			message.setTs(messageTimeStamp);
		}
		Map<String, String> map = new HashMap<>();
		map.put("Authorization", OAUTH_KEY);
		map.put("Content-Type", "application/json");
		apiBean.setHeaders(map);
		
		JSONObject jsonObject = new JSONObject(message);
		apiBean.setRequestPayload(jsonObject.toString());
		return apiBean;
	}

	private static synchronized SlackMessage buildMessage() {
		int pass=0;
		int fail=0;
		int running=0;
		int completed=0;
		
		
		ArrayList<ScenarioState> failedScenarios = new ArrayList<>();
		for (ScenarioState scenarioState : scenarios) {
			if(scenarioState.getStatus().equals("RUNNING")) running++;
			if(scenarioState.getStatus().equals("COMPLETED")) {
				completed++;
				if(scenarioState.getResult().equals("PASSED")) pass++;
				if(scenarioState.getResult().equals("FAILED")) {
					fail++;
					failedScenarios.add(scenarioState);
				}
			}
		}
		
		SlackMessage slackMessage = new SlackMessage();
		slackMessage.setText(DESCRIPTION);
		System.out.println("------------"  + getSlackChannel());
		slackMessage.setChannel(getSlackChannel());
		ArrayList<SlackAttachment> attachments = new ArrayList<>();
		
		SlackAttachment summary = new SlackAttachment();
		summary.setTitle("Execution Summary");
		summary.setColor("#2196f3");
		ArrayList<SlackField> summFields = new ArrayList<>();

		SlackField runningField = new SlackField();
		runningField.setTitle("Running");
		runningField.setValue(running+"");
		runningField.setShort(true);
		
		SlackField completedField = new SlackField();
		completedField.setTitle("Completed");
		completedField.setValue(completed+"");
		completedField.setShort(true);
		
		SlackField passedField = new SlackField();
		passedField.setTitle("Passed");
		passedField.setValue(pass+"");
		passedField.setShort(true);
		
		SlackField failedField = new SlackField();
		failedField.setTitle("Failed");
		failedField.setValue(fail+"");
		failedField.setShort(true);
		
		if(ExecutionEnvironment.getHostName() != null){
			SlackField field = new SlackField();
			field.setTitle("Hostname");
			field.setShort(true);
			field.setValue(ExecutionEnvironment.getHostName());
			summFields.add(field);
		}
		
		if(ExecutionEnvironment.getHostIp() != null){
			SlackField field = new SlackField();
			field.setTitle("Host IP");
			field.setShort(true);
			field.setValue(ExecutionEnvironment.getHostIp());
			summFields.add(field);
		}
		
		if(ExecutionEnvironment.isJenkinsBuild() && ExecutionEnvironment.getJobName() != null){
			SlackField field = new SlackField();
			field.setTitle("Job Name");
			field.setShort(true);
			field.setValue(ExecutionEnvironment.getJobName());
			summFields.add(field);
		}
		
		if(ExecutionEnvironment.isJenkinsBuild() && ExecutionEnvironment.getJobName() != null){
			SlackField field = new SlackField();
			field.setTitle("Job Number");
			field.setShort(true);
			field.setValue(ExecutionEnvironment.getJobName());
			summFields.add(field);
		}
		
		
		
		
		summFields.add(runningField);
		summFields.add(completedField);
		summFields.add(passedField);
		summFields.add(failedField);
		summary.setFields(summFields);
		attachments.add(summary);
		
		for (ScenarioState scenario : failedScenarios) {
			SlackAttachment attachment = new SlackAttachment();
			String message ="";
			message = message.concat(scenario.getScenarioName());
			attachment.setText(message);
			attachment.setColor("#f44336");
//			+ ": " +  scenario.getScenarioName() + " - " + scenario.getDataSetName() + " " + " ChristiesComQAFail"
			
			SlackField field = new SlackField();
			field.setTitle("Feature Name");
			field.setValue(scenario.getFeatureName());
			field.setShort(true);
			
			SlackField field2 = new SlackField();
			field2.setTitle("Dataset");
			field2.setValue(scenario.getDataSetName());
			field2.setShort(true);
			
			SlackField field3 = new SlackField();
			field3.setTitle("Status");
			field3.setValue(" ChristiesComQAFail");
			field3.setShort(true);
			
			ArrayList<SlackField> fields = new ArrayList<>();
			fields.add(field);
			fields.add(field2);
			fields.add(field3);
			
			attachment.setFields(fields);
			attachments.add(attachment);
		}
		
		slackMessage.setAttachments(attachments);
		
		return slackMessage;
	}
	
	
	
	
	

}
