package com.helios.utilities;

import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFValidator implements StringLiterals {

	private String pdfContent;

	public PDFValidator() throws Exception {
		String pdfContent = null;
		try {
			File folder = new File(DOWNLOAD_PATH);
			if (!folder.exists()) folder.mkdir();
			File listOfFiles[] = folder.listFiles();
			for (File file : listOfFiles) {
				for (int i = 0; i < 10; i++) {
					Thread.sleep(200);
					if (file.getName().contains(".crdownload")) {
						System.out.println("Not downloaded yet");
					} else {
						System.out.println("Downloaded");
						break;
					}
				}

				System.out.println("File name is " + file.getName());
				PDDocument document = PDDocument.load(file);
				PDFTextStripper pdfStripper = new PDFTextStripper();
				Thread.sleep(300);
				pdfContent = pdfStripper.getText(document);
				// System.out.println(pdfcontent);
				document.close();
			}
		} catch (Exception e) {
			System.out.println("Not Able to read the PDF File");
			e.printStackTrace();
			throw e;
		}
		this.pdfContent = pdfContent;
	}

	public boolean validateText(String text) {
		if (text == null) {
			System.out.println("NULL Was Provided For PDF Validation");
			return false;
		}

		return pdfContent.contains(text);
	}

	public boolean deleteFile() {
		File folder = new File(DOWNLOAD_PATH);
		File listOfFiles[] = folder.listFiles();
		listOfFiles[0].delete();
		//folder.delete();
		return true;
	}
	
}
