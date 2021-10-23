package com.helios.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Recordset;
import com.helios.utilities.StringLiterals;

public class ExcelMulti extends ExcelConnectionManager implements StringLiterals {

	private static Map<String, ArrayList<Map<String, String>>> sheetStore = new HashMap<String, ArrayList<Map<String, String>>>();
	
	public static synchronized ArrayList<Map<String, String>> getData(String sheetName) {
		if (!sheetStore.containsKey(sheetName)) {
			String strQuery = "Select * from `" + sheetName + "`";
			Recordset recordset;
			ArrayList<Map<String, String>> sheetMap = new ArrayList<>();
			try {
				recordset = getMultiDataInputConnection().executeQuery(strQuery);
				ArrayList<String> fieldNames = recordset.getFieldNames();
				
				int recordsCount = recordset.getCount();
				for (int i = 0; i < recordsCount; i++) {
					recordset.next();
					Map<String, String> map = new HashMap<String, String>();
					for (String key : fieldNames) {
						map.put(key, recordset.getField(key));
					}
					sheetMap.add(map);
				}
				
			} catch (FilloException e) {
				e.printStackTrace();
			}
			sheetStore.put(sheetName, sheetMap);
			return sheetStore.get(sheetName);
		}else {
			return sheetStore.get(sheetName);
		}
	}
	
	
	public static void main(String args[]) {
//		getData("SaleData");
//		dumpMultiOutputData();
		
		ArrayList<Map<String, String>> list = ExcelMulti.getData("SaleData");
		
		for (Map<String, String> map : list) {
			System.out.println(map.get("ID"));
			map.put("ID", "Blah Blah");
		}
		
		ExcelMulti.dumpMultiOutputData();
		
	}
	
	
	
	
	public static  synchronized void  dumpMultiOutputData() {
		Set<String> sheets = sheetStore.keySet();
		try {
			for (String sheetName : sheets) {
				ArrayList<Map<String, String>> sheetMap = sheetStore.get(sheetName);
				String deleteQuery = "Delete from `" + sheetName + "`";
				getMultiDataInputConnection().executeUpdate(deleteQuery);
				String insertStatementTemplate = "INSERT INTO `" + sheetName + "`(" ;
				Set<String> fieldsName = sheetMap.get(0).keySet();
				for (String field : fieldsName) {
					insertStatementTemplate = insertStatementTemplate.concat(""+field+",");
				}
				
				insertStatementTemplate =insertStatementTemplate.substring(0,insertStatementTemplate.length()-1) + ") VALUES(";
				for (Map<String, String> row : sheetMap) {
					String insertQuery = insertStatementTemplate;
					for(String field: fieldsName) {
						String value = "";
						if(row.containsKey(field)) value = row.get(field).replace("'", "");
						insertQuery = insertQuery + "'" + value  + "',";
					}
					insertQuery = insertQuery.subSequence(0, insertQuery.length()-1) + ")";
//					System.out.println(insertQuery);
					getMultiDataInputConnection().executeUpdate(insertQuery);
				}
							}
		} catch (FilloException e) {
			e.printStackTrace();
		}

	}
	
	
	
	
	
}
