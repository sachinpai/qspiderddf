package com.genericLib;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class FetchDataFromProperty {

	public String FetchData(String key) throws IOException {
		
		FileInputStream file = new FileInputStream("./CommanData.properties");
		Properties pro = new Properties();
		pro.load(file);
		String value = pro.getProperty(key);
		return value;
		
	}
	

}
