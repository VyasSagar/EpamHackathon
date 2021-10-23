package com.helios.scriptutils;

import org.openqa.selenium.support.events.WebDriverEventListener;


public interface ScenarioEventListener extends WebDriverEventListener {

	public abstract void onScenarioStart(ScenarioState scenarioState);

	void onScenarioEnd(ScenarioState scenarioState);

	void onScenarioPass(ScenarioState scenarioState2);

	public abstract void onScenarioFailed(ScenarioState scenarioState);

	public abstract void onPageObjectChange(ScenarioState scenarioState, String previous, String current);

	public abstract void onGlobalFunctionCalled(ScenarioState scenarioState, String pageObjectName,
			String functionInfo);

}
