package com.helios.excel;

import java.util.ArrayList;
import java.util.List;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Recordset;
//import com.cucumber.listener.Reporter;

public class ExcelInput extends ExcelConnectionManager {
	
	public synchronized static String fetchData(String sheetName, String colName, String key) {
		System.out.println(">>>ExcelInput  " + sheetName +"  - "+ colName +"  - "+ key );
		Recordset recordset;
		String strQuery = "Select * from `" + sheetName + "`";
		try {
			recordset = getInputSheetConnection().executeQuery(strQuery).where("DataSetName='" + colName + "'");
			if (!recordset.next())
				return null;
			return recordset.getField(key);
		} catch (FilloException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public synchronized static boolean updateData(String sheetName, String colname, String key, String value) {
		value = value.replaceAll("'", " ");
		String query = "Update `" + sheetName + "` Set `" + key + "`='" + value + "' where DataSetName='"
				+ colname + "'";
		try {
			getInputSheetConnection().executeUpdate(query);
			return true;
		} catch (FilloException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public synchronized static boolean insertData(String sheetName, String key, String value) {
		value = value.replaceAll("'", " ");
		String query = "INSERT INTO `" + sheetName + "`(`" + key + "`) VALUES('" + value+ "')";		
		try {			
			getInputSheetConnection().executeUpdate(query);
			return true;
		} catch (FilloException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	public synchronized static String fetchProperty(String sheetName, String key) {
		Recordset recordset;
		String strQuery = "Select * from `"+sheetName+"`";
		try {
			recordset = getInputSheetConnection().executeQuery(strQuery).where("property='" + key + "'");
			if (!recordset.next())
				return null;
			return recordset.getField("value");
		} catch (FilloException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public synchronized static boolean clearSheet(String sheetName) {
		String query = "DELETE from `" + sheetName + "`";
		
		try {
			getInputSheetConnection().executeUpdate(query);
			return true;
		} catch (FilloException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public synchronized static List<String> getColumnsAsList(String sheetName,String colName){
		List<String> value = new ArrayList<String>();
		Recordset recordset;
		String strQuery = "Select * from `" + sheetName + "`";
		try {
			recordset = getInputSheetConnection().executeQuery(strQuery);
			if (!recordset.next())
				return null;
			else{
				do{
					value.add(recordset.getField(colName));
				}while (recordset.next());
			return value;
			}

		} catch (FilloException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public synchronized static int countRows(String sheetName) {
		try {
			return countRows(sheetName , getInputSheetConnection());
		} catch (FilloException e) {
			return -1;
		}
	}
	
	
}
