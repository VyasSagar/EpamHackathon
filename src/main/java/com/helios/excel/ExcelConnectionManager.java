package com.helios.excel;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.helios.scriptutils.ExecutionState;
import com.helios.utilities.ExecutionEnvironment;
import com.helios.utilities.StringLiterals;

public class ExcelConnectionManager implements StringLiterals {
	
	protected ExcelConnectionManager() {
		
	}
	
	private static ExcelConnectionManager connectionManager;
	private static Connection inputSheetConnection;
	private static Connection outputSheetConnection;
	private static Connection multiDataInputConnection;
	private static Connection executionPropertyConnection;
	private static Connection externalWorkbookConnection;
	
	protected static ExcelConnectionManager getInstance() {
		if(connectionManager == null) connectionManager = new ExcelConnectionManager();
		return connectionManager;
	}
	
	
	protected static Connection getInputSheetConnection() throws FilloException {
		if(inputSheetConnection == null) {
			Fillo fillo = new Fillo();
			inputSheetConnection = fillo.getConnection(TEST_DATA_WORKBOOK_PATH);
		}
		return inputSheetConnection;
	}
	
	
	protected static Connection getOutputSheetConnection() {
		if(outputSheetConnection == null) {
//			File file = new File(OUTPUT_EXCEL_TEMPLATE_FILE);
//			String outputFilePath = ExecutionState.getCurrentReportsPath() + "/" + OUTPUT_EXCEL_FILE;
//			File outputFile = new File(outputFilePath);
//			try {
//				FileUtils.copyFile(file, outputFile);
//			} catch (IOException e) {
//				System.out.println("Problem copying output file.");
//				e.printStackTrace();
//			}
			Fillo fillo = new Fillo();
			try {
				outputSheetConnection = fillo.getConnection(OUTPUT_EXCEL_TEMPLATE_FILE);
			} catch (FilloException e) {
				System.out.println("[ERROR] problem getting output sheet connection");
				e.printStackTrace();
			}
		}
		return outputSheetConnection;
	}
	public static Connection getExternalWorkbookConnection(String path) throws FilloException {
		if(externalWorkbookConnection == null) {
			Fillo fillo = new Fillo();
			externalWorkbookConnection = fillo.getConnection(path);
			System.out.println("CONNECTION : ");
			System.out.println(path);
		}
		return externalWorkbookConnection;
	}
	protected static Connection getMultiDataInputConnection() {
		if(multiDataInputConnection == null) {
			Fillo fillo = new Fillo();
			try {
				multiDataInputConnection = fillo.getConnection(MULTI_DATA_TEMPLATE);
			} catch (FilloException e) {
				System.out.println("Problem getting Multi Data Input Connection.(Verify if file exists.)");
				e.printStackTrace();
			}
		}
		return multiDataInputConnection;
	}
	
	protected static Connection getExecutionPropertiesConnection(){
		if(executionPropertyConnection == null){
			Fillo fillo = new Fillo();
			try{
				if (ExecutionEnvironment.isJenkinsBuild()) {
					System.out.println("JENKINS JOB");
					String jobName = ExecutionEnvironment.getJobName();
					System.out.println("JOB NAME : "+jobName);
					System.out.println("MASTER BRANCH : ONLY FOR ONE TIME JENKINS TEST SHOULD BE REMOVED ASAP");
						executionPropertyConnection = fillo.getConnection(EXECUTION_PROPERTIES_EXCEL_PATH);
				}
				else
				executionPropertyConnection = fillo.getConnection(EXECUTION_PROPERTIES_EXCEL_PATH);
			}catch (FilloException e) {
				System.out.println("Problem getting Execution Properties Connection.(Verify if file exists.)");
				e.printStackTrace();
			}
		}
		
		return executionPropertyConnection;
	}
	
	protected static synchronized boolean executeInsertStatement(String strQuery, Connection connection ) {
		try {
			System.out.println("[QUERY] " + strQuery + " -- " + connection.getClass().getSimpleName());
			connection.executeUpdate(strQuery);
			return true;
		} catch (FilloException e) {
			System.out.println("[ERROR] Unable to execute insert statement.");
			e.printStackTrace();
			return false;
		}

	}

	protected static synchronized Recordset executeSelectStatement(String query, Connection connection ) {
		
		Recordset recordset;
		try {
			System.out.println("[QUERY] " + query + " -- ");
			recordset = connection.executeQuery(query);
			return recordset;
		} catch (FilloException e) {
			System.out.println("[ERROR] Unable to execute select statement.");
			e.printStackTrace();
			return null;
		}
	}
	
	protected static synchronized Recordset executeSelectStatement(String query, Connection connection ,boolean suppress) {
		
		Recordset recordset;
		try {
			recordset = connection.executeQuery(query);
			return recordset;
		} catch (FilloException e) {
			return null;
		}
	}

	protected static synchronized int countRows(String sheetName, Connection connection) {
		Recordset recordset;
		String query = "select * from " + sheetName;
		try {
			recordset = connection.executeQuery(query);
			return recordset.getCount();
		} catch (FilloException e) {
			return -1;
		}
	}
	
	
	
	
	
}
