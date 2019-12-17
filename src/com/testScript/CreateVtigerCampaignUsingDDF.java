package com.testScript;

import java.util.Iterator;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.genericLib.FetchDataFromProperty;
import com.genericLib.FetchDataFromExcel;

public class CreateVtigerCampaignUsingDDF {

	public static void main(String[] args) throws IOException, InterruptedException {
		
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		
		FetchDataFromProperty data = new FetchDataFromProperty();
		String url = data.FetchData("vtigerurl");
		String username = data.FetchData("vtigerusername");
		String pass = data.FetchData("vtigerpassword");
		
		// Browser Launch and Login Activity
		driver.get(url);
		driver.findElement(By.name("user_name")).sendKeys(username);
		driver.findElement(By.name("user_password")).sendKeys(pass);
		driver.findElement(By.id("submitButton")).click();
		
		// Create Campaign activity
		
		Actions act = new Actions(driver);
		act.moveToElement(driver.findElement(By.linkText("More"))).perform();
		act.click(driver.findElement(By.name("Campaigns"))).perform();
		
		driver.findElement(By.xpath("//img[@title='Create Campaign...']")).click();
		
		FetchDataFromExcel f = new FetchDataFromExcel();
		String campaignName = f.FetchData("Sheet1",0, 1);
		String campaignConfirmMess = f.FetchData("Sheet1",1, 1);
		
		driver.findElement(By.name("campaignname")).sendKeys(campaignName);
		driver.findElement(By.xpath("//img[@alt='Select']")).click();
		
		String windowHandle = driver.getWindowHandle();
		Set<String> windowHandles = driver.getWindowHandles();
		Iterator <String> iterator = windowHandles.iterator();
		while(iterator.hasNext()) {
			String winid = iterator.next();
			if(!winid.equals(windowHandle)) {
				driver.switchTo().window(winid);
			}
		}
		
		String productName = f.FetchData("Sheet1", 2, 1);
		driver.findElement(By.id("search_txt")).sendKeys(productName + Keys.ENTER);
		driver.findElement(By.linkText(productName)).click();
		driver.switchTo().window(windowHandle);
		
		driver.findElement(By.xpath("//input[@value='  Save  ']")).click();
		
		String actText = driver.findElement(By.className("dvHeaderText")).getText();
		
		if(actText.contains(campaignConfirmMess)) {
			System.out.println("Script pass - Campaign created");
		} else {
			System.out.println("Script Fail - Confirmation Text Not Matched");
		}
		
		driver.findElement(By.linkText("Campaigns")).click();
		String record="";
		try {
			record = driver.findElement(By.xpath("//td[contains(text(),'Showing')]")).getText().split(" ")[6];
		}catch(Throwable eb) {
			System.out.println("No Campaign Running at present");
		}
		int intcampaignCount = Integer.parseInt(record);
		driver.findElement(By.name("search_text")).sendKeys(campaignName);
		WebElement campaignDrop = driver.findElement(By.id("bas_searchfield"));
		Select campaignSort = new Select(campaignDrop);
		campaignSort.selectByValue("campaignname");
		driver.findElement(By.xpath("//input[@value=' Search Now ']")).click();
		driver.findElement(By.xpath("//a[text()='"+campaignName+"']/../../td/input")).click();
		driver.findElement(By.xpath("//input[@value='Delete']")).click();
		driver.switchTo().alert().accept();
		try {			
			String campaignDeleteText = driver.findElement(By.xpath("//span[@class='genHeaderSmall']")).getText();
			String expText = "No Campaign Found !";
			if(campaignDeleteText.contains(expText)) {
				System.out.println("Script Pass - Campaign Deleted");
			}else {
				System.out.println("Script Fail - Campaign Not Deteled");
			}			
		} catch(Throwable e) {
			String finalCampaign = driver.findElement(By.xpath("//td[contains(text(),'Showing')]")).getText().split(" ")[6];
			int finalCampaignCount = Integer.parseInt(finalCampaign);
			if(finalCampaignCount < intcampaignCount) {
				System.out.println("Script Pass - Campaign Deleted");
			}else {
				System.out.println("Script Fail - Campaign Not Deteled");
			}	
		}
		driver.close();

	}

}
