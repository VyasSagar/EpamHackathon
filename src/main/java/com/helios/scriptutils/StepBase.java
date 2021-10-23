package com.helios.scriptutils;

import java.util.ArrayList;
import java.util.Map;

import com.helios.excel.ExcelInput;
import com.helios.excel.ExcelMulti;
import com.helios.utilities.StringLiterals;

public class StepBase implements StringLiterals {

	private ScenarioState scenarioState;	
	
	public StepBase(ScenarioState  scenarioState) {
		this.scenarioState = scenarioState;
	}
	
	public ScenarioState getState() {
		return this.scenarioState;
	}
	
	protected ArrayList<Map<String, String>> getMultiExcelData(){
		return ExcelMulti.getData("MultiLotData");
	}
	
	protected String getTestDataValue(String key) {
		return ExcelInput.fetchData(getState().getFeatureName(), getState().getDataSetName(), key);
	}
	
	
}
