package com.helios.pageobjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;


import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.FindBy;

import com.helios.scriptutils.PageBase;
import com.helios.scriptutils.ScenarioState;

public class YoutubeHome extends PageBase{

	public YoutubeHome(ScenarioState state) {
		super(state);
		// TODO Auto-generated constructor stub
	}

	/*** Change the Authorization Type And Account Account Objects***/
	//To Check
	
	@FindBy(xpath ="//div[contains(@class,'ytd-topbar-logo-renderer')]")
	WebElement imageYTLOGO;

	@FindBy(id ="search-icon")
	WebElement inputYTSearchBar;

	@FindBy(id ="search-icon-legacy")
	WebElement btnSearchContent;
	
	String linkChannel = "//a[@id='main-link' and contains(.,'"+VAR+"')]";
	
	String channelName = null;

	
	// ChannelPage
	@FindBy(xpath ="//div[contains(@class,'tab-content') and contains(.,'Videos')]")
	WebElement btnSwitchToVideos;

	@FindBy(id ="icon-label")
	WebElement btnSortButton; 

	@FindBy(xpath ="//body")
	WebElement body; 

	@FindBy(xpath ="//a[contains(@class,'yt-dropdown-menu') and contains(.,'Date added (newest)')]")
	WebElement btnSortByLatest; 
	
	@FindBy(id ="video-title")
	List<WebElement> txtvideoTitle; 
	
	
	
	public void openYoutubeHome() {
		openURL("https://www.youtube.com");
		String originalHandle = getDriver().getWindowHandle();

	    //Do something to open new tabs

	    for(String handle : getDriver().getWindowHandles()) {
	        if (!handle.equals(originalHandle)) {
	        	getDriver().switchTo().window(handle);
	        	getDriver().close();
	        }
	    }

	    getDriver().switchTo().window(originalHandle);
	}

	public void navigateToChannel() throws InterruptedException {
		zAssertTrue(doesElementExist(imageYTLOGO), "Navigated to YT", "Could not navigate to YT");
		
		click(imageYTLOGO);
		channelName = getTestDataValue("Channel Name");
		
		
		moveClickAndEnterText(body, "/"+channelName);
		moveClickAndEnterText(body, "/"+Keys.ENTER);
		
		pause(5000);
		getDriver().switchTo().frame(getElemtFromParamXpath(linkChannel, channelName));
		embedScreenshot(getElemtFromParamXpath(linkChannel, channelName));
		click(getElemtFromParamXpath(linkChannel, channelName));
		embedScreenshot();
	}
	

	public void getVideoDetails() throws InterruptedException {
		pause(5000);
		
		embedScreenshot(btnSwitchToVideos);
		click(btnSwitchToVideos);
		pause(1000);
		click(btnSortButton);
		click(btnSortByLatest);
		pause(1000);
		for (WebElement webElement : txtvideoTitle) {
			System.out.println(getText(webElement));
		}
		
	}
	
	
}
