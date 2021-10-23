package com.helios.stepDefinations;


import com.helios.pageobjects.YoutubeHome;
import com.helios.scriptutils.ScenarioState;
import com.helios.scriptutils.StepBase;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class YoutubeHomeSteps extends StepBase{
	YoutubeHome ytHome;
	
	public YoutubeHomeSteps(ScenarioState scenarioState) {
		super(scenarioState);
		ytHome = new YoutubeHome(getState());
	} 

	
	@Given("^open youtube$")
	public void user_navigates_to_playwaze() throws Throwable {
		if(ytHome == null) ytHome = new YoutubeHome(getState());
		ytHome.openYoutubeHome();
	}	
	
	@Then("^Navigate to Channel$")
	public void navigate_to_Channel() throws Throwable {
		if(ytHome == null) ytHome = new YoutubeHome(getState());
		ytHome.navigateToChannel();
	}
	
	@Then("^Get All Video Details$")
	public void get_all_video_details() throws Throwable {
		if(ytHome == null) ytHome = new YoutubeHome(getState());
		ytHome.getVideoDetails();
	}

	
}
