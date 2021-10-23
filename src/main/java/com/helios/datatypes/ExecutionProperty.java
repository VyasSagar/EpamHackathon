package com.helios.datatypes;

public class ExecutionProperty {
	private String executionType;
	private String browser;
	private String resolution;
	private String address;
	private String port;
	private boolean slackUpdate;
	private boolean browserMob;
	private String slackChannel;
	private String runner;
	private boolean toggle;
	private String scenarioName;
	private String featureName;
	private String dataSetName;
	private boolean isRunnerActive;
	private int retryCount;
	private String fileIdentifier;
	private int order;
	
	public String getExecutionType() {
		return executionType;
	}
	public void setExecutionType(String executionType) {
		this.executionType = executionType.trim();
	}
	public String getBrowser() {
		return browser;
	}
	public void setBrowser(String browser) {
		this.browser = browser.trim();
	}
	public String getResolution() {
		return resolution;
	}
	public void setResolution(String resolution) {
		this.resolution = resolution.trim();
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address.trim();
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port.trim();
	}
	public boolean isSlackUpdate() {
		return slackUpdate;
	}
	public void setSlackUpdate(boolean slackUpdate) {
		this.slackUpdate = slackUpdate;
	}
	public boolean isBrowserMob() {
		return browserMob;
	}
	public void setBrowserMob(boolean browserMob) {
		this.browserMob = browserMob;
	}
	public String getSlackChannel() {
		return slackChannel;
	}
	public void setSlackChannel(String slackChannel) {
		this.slackChannel = slackChannel.trim();
	}
	public String getRunner() {
		return runner;
	}
	public void setRunner(String runner) {
		this.runner = runner.trim();
	}
	public boolean isToggle() {
		return toggle;
	}
	public void setToggle(boolean toggle) {
		this.toggle = toggle;
	}
	public String getScenarioName() {
		return scenarioName.trim();
	}
	public void setScenarioName(String scenarioName) {
		this.scenarioName = scenarioName;
	}
	public String getFeatureName() {
		return featureName;
	}
	public void setFeatureName(String featureName) {
		this.featureName = featureName.trim();
	}
	public String getDataSetName() {
		return dataSetName;
	}
	public void setDataSetName(String dataSetName) {
		this.dataSetName = dataSetName.trim();
	}
	
	
	public boolean isRunnerActive() {
		return isRunnerActive;
	}
	public void setRunnerActive(boolean isRunnerActive) {
		this.isRunnerActive = isRunnerActive;
	}
	
	
	
	
	
	
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public int getRetryCount() {
		return retryCount;
	}
	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}
	
	
	
	public String getIdentifier() {
		String id = featureName.trim()+"."+scenarioName.trim()+"."+dataSetName.trim();
		return id.toLowerCase();
	}
	
	public String getFileIdentifier() {
		return this.fileIdentifier;
	}
	
	public void setFileIdentifier(String identifier){
		this.fileIdentifier = identifier;
	}
	
	@Override
	public String toString() {
		return "ExecutionProperty [executionType=" + executionType + ", browser=" + browser + ", resolution="
				+ resolution + ", address=" + address + ", port=" + port + ", slackUpdate=" + slackUpdate
				+ ", browserMob=" + browserMob + ", slackChannel=" + slackChannel + ", runner=" + runner + ", toggle="
				+ toggle + ", scenarioName=" + scenarioName + ", featureName=" + featureName + ", dataSetName="
				+ dataSetName + ", isRunnerActive=" + isRunnerActive + ", retryCount=" + retryCount
				+ ", fileIdentifier=" + fileIdentifier + ", order=" + order + "]";
	}
	
	
	
	
	
	
	
	
}