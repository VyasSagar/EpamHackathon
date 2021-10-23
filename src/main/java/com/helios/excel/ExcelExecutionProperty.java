package com.helios.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Recordset;

public class ExcelExecutionProperty extends ExcelConnectionManager {
	
	public static synchronized boolean isRunnerActive(String runnerName) {

		String query = "select * from RunnerProperties where RunnerName='" + runnerName + "'";
		System.out.println(query);
		try {
			Recordset recordset = executeSelectStatement(query, getExecutionPropertiesConnection());
			if (!recordset.next())
				return false;
			return recordset.getField("Is Active").equalsIgnoreCase("yes");
		} catch (FilloException e) {
			System.out.println("Problem while fetching runners");
			e.printStackTrace();
		}
		return false;
	} 
	
	public static synchronized List<HashMap<String, String>> getRunnersList(){
		List<HashMap<String, String>> runners  = new ArrayList<>();
		
		String query = "select * from RunnerProperties";
		
		Recordset recordset = executeSelectStatement(query, getExecutionPropertiesConnection());
		try {
			int recordsCount  = recordset.getCount();
			for (int i = 0; i < recordsCount ; i++) {
				recordset.next();
				HashMap<String, String> runner = new HashMap<>();
				runner.put("RunnerName", recordset.getField("RunnerName"));
				runner.put("Hostname", recordset.getField("Hostname"));
				runner.put("Port", recordset.getField("Port"));
				runner.put("Is Active", recordset.getField("Is Active"));
				runners.add(runner);
			}			
		} catch (FilloException e) {
			System.out.println("Problem while fetching runners");
			e.printStackTrace();
		} 
		return runners;
	}
	
	
	public static synchronized List<HashMap<String, String>> getScenarioList(String runnerName){
		List<HashMap<String, String>> scenarios  = new ArrayList<>();
		
		String query = "select * from " +  runnerName;
		
		Recordset recordset = executeSelectStatement(query, getExecutionPropertiesConnection(),true);
		try {
			int recordsCount  = recordset.getCount();
			for (int i = 0; i < recordsCount ; i++) {
				recordset.next();
				HashMap<String, String> scenario = new HashMap<>();
				scenario.put("FeatureName", recordset.getField("FeatureName"));
				scenario.put("ScenarioName", recordset.getField("ScenarioName"));
				scenario.put("DataSetName", recordset.getField("DataSetName"));
				scenario.put("Switch", recordset.getField("Switch"));
				scenario.put("Browser", recordset.getField("Browser"));
				scenario.put("FeatureName", recordset.getField("FeatureName"));
				scenario.put("ExecutionType", recordset.getField("ExecutionType"));
				scenario.put("Retry Count", recordset.getField("Retry Count"));
				scenario.put("Order", recordset.getField("Order"));
				scenarios.add(scenario);
			}			
		} catch (Exception e) {
			System.out.println("No table for "+ runnerName + " found");
		} 
		return scenarios;
	}
	
	
	
	
	public static synchronized String getGlobalExecutionProperty(String propertyName){
		Recordset recordset;
		String strQuery = "Select * from `Global`";
		try {
			recordset = getExecutionPropertiesConnection().executeQuery(strQuery).where("property='" + propertyName + "'");
			if (!recordset.next())
				return null;
			return recordset.getField("value");
		} catch (FilloException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public synchronized static int countRows(String sheetName) {
		try {
			return countRows(sheetName , getExecutionPropertiesConnection());
		} catch (Exception e) {
			return 0;
		}
	}
	
	
	
	
}
