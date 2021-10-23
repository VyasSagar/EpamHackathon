package com.helios.utilities;

import java.io.File;

public interface StringLiterals {

	int PAGE_IMPLICIT_WAIT = 60;

	String PROJECT_NAME = "Team Helios Automation";

	String DESCRIPTION = "PROJECT HELIOS TESTS";
	String authorName = "Sagar Vyas";
	String AWS_IP = "172";
	String BASE_FOLDER_PATH = getBasePath();
	String UPLOAD_FILES_PATH = BASE_FOLDER_PATH + "/upload_files";
	String PROPERTIES_FILES_PATH = BASE_FOLDER_PATH + "";
	String EXCEL_FILES_PATH = BASE_FOLDER_PATH + "";
	String SCREENSHOTS_BASE_PATH = BASE_FOLDER_PATH + "/output/screenshots";

	String TEST_DATA_WORKBOOK_PATH = BASE_FOLDER_PATH + "/TestData.xls";

	String OUTPUT_EXCEL_TEMPLATE_FILE = BASE_FOLDER_PATH + "/OutputDataFile.xls";
	String OUTPUT_EXCEL_FILE = "OutputDataFile.xls";

	String REPORTS_PATH = getReportsPath();
	String GSPEC_BASE_PATH = BASE_FOLDER_PATH + "/gspecs";
	// String OUTPUT_DATAWORKBOOK_PATH = BASE_FOLDER_PATH+"/OutputDataFile.xls";
	String DEFAULT_OUTPUT_SHEET_NAME = "OutputData";
	String VAR = "####";

	String EXECUTION_PROPERTIES_EXCEL_PATH = BASE_FOLDER_PATH + "/ExecutionProperties.xls";
	String MULTI_DATA_TEMPLATE = BASE_FOLDER_PATH + "/MultiDataTemplate.xls";
	String MULTI_DATA_OUTPUT = "MultiDataOutput.xls";
	String REPORT_TYPE = "PDF";

	String DOWNLOAD_PATH = BASE_FOLDER_PATH + "/downloads";


	public static String getBasePath() {
		String path = System.getProperty("input_data_path");
		if (path == null) {
			System.out.println("No argument passed for input data directory. Using default.");
			path = "support_files";
		}
		File file = new File(path);
		try {
			if (!file.exists()) {
				System.out.println("Support files directory not found, switching to default");
				path = "support_files";
			}
		} catch (Exception e) {
			System.out.println("Support files directory not found, switching to default");
			path = "support_files";
		}
		System.out.println("\nBase path = \t" + new File(path).getAbsolutePath());
		return path;
	}

	public static String getReportsPath() {
		String path = System.getProperty("output_data_path");
		if (path == null) {
			System.out.println("No argument passed for output data directory. Using default.");
			path = "Reports";
		}
		File file = new File(path);
		try {
			if (!file.exists()) {
				System.out.println("Reports directory not found, trying to create one.");
				file.mkdir();
				System.out.println("Directory created successfully");
			}
		} catch (Exception e) {
			System.out.println("Tried creating reports folder but failed. Using default");
			path = "Reports";
		}
		System.out.println("\nReports path = \t" + new File(path).getAbsolutePath() + "\n");
		return path;
	}
}
