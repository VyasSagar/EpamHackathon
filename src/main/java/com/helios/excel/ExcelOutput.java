package com.helios.excel;

import com.codoid.products.fillo.Recordset;

public class ExcelOutput extends ExcelConnectionManager {

	public static synchronized boolean setOutputDataValue(String sheetName, String key, String value,
			int currentRowId) {
		value = value.replaceAll("'", " ");
		String query = "Update `" + sheetName + "` Set `" + key + "`='" + value + "' where ID='" + currentRowId + "'";
		return executeInsertStatement(query, getOutputSheetConnection());
	}

	public static synchronized boolean executeInsertStatement(String strQuery) {
		return executeInsertStatement(strQuery, getOutputSheetConnection());
	}

	public static synchronized Recordset executeSelectStatement(String query) {
		return executeSelectStatement(query, getOutputSheetConnection());
	}

}
