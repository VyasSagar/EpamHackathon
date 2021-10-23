package com.helios.scriptutils;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.helios.utilities.StringLiterals;

public class ExecutionState implements StringLiterals {
	private static String currentReportsPath = null;

	private static String currentOutputPath = null;

	public static int mobProxyPorts = 9090;

	public static int scenarioIdCounter = 1;

	public static String getCurrentReportsPath() {

		if (currentReportsPath == null) {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_hh-mm-ss");
			LocalDateTime now = LocalDateTime.now();
			currentReportsPath = REPORTS_PATH + "/" + PROJECT_NAME + "_" + dtf.format(now);
		}

		return currentReportsPath;
	}

	public static String getCurrentOutputPath() {
		if (currentOutputPath == null) {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_hh-mm-ss");
			LocalDateTime now = LocalDateTime.now();
			currentOutputPath = "\\" + "output" + "\\" + "SA" + "_" + dtf.format(now);
			File file = new File(currentOutputPath);
			file.mkdirs();
		}
		String filePath = new File("").getAbsolutePath();
		return filePath.concat(currentOutputPath);
	}


}
