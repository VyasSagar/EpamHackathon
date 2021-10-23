Feature: PlaywazeHome


	Background:
	
	Given open youtube
	
	Scenario Outline: Youtube exploration#<DataSetName>
 	
 	When Navigate to Channel
 	Then Get All Video Details
 	Examples:
	|DataSetName|
 	|Playwaze DS1|
 
 	
 	
 #	Scenario Outline: Add Account Of Person To Another Person #<DataSetName>
 #	
 #	Examples:
#	|DataSetName|
 #	|Add an Account to existing one|
 	
