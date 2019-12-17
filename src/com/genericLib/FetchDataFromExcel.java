package com.genericLib;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class FetchDataFromExcel {
	
	public String FetchData(String sheet , int row, int col) throws EncryptedDocumentException, IOException {
		
		FileInputStream file = new FileInputStream("./TestData.xlsx");
		
		Workbook book = WorkbookFactory.create(file);
		
		String value = book.getSheet(sheet).getRow(row).getCell(col).getStringCellValue();
		
		return value;
		
	}

}
