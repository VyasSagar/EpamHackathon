package com.helios.stepDefinations;

import com.helios.datatypes.SaleHeader;
import com.helios.datatypes.SellerAgreement;
import com.helios.datatypes.StockOrder;
import com.helios.excel.ExcelExecutionProperty;
import com.helios.excel.ExcelInput;
import com.helios.scriptutils.OutputLogger;
import com.helios.scriptutils.ScenarioState;
import com.helios.slack.SlackClient;
import com.helios.utilities.StringLiterals;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;

public class Hooks implements StringLiterals {

	private ScenarioState scenarioState;

	public Hooks(ScenarioState scenarioState) {
		this.scenarioState = scenarioState;

	}

	@Before(order = 10001)
	public void setupEnvironment(Scenario scenario) {
		System.out.println("SCENARIO STATE Init");
		scenarioState.init(scenario);
		scenarioState.attach(new OutputLogger());
		if (ExcelExecutionProperty.getGlobalExecutionProperty("Slack Update").equalsIgnoreCase("Yes"))
			scenarioState.attach(new SlackClient());
		scenarioState.initComplete();

//		jdePreExecutionDataFetching(); // JDE Specific
	}

	@Before("@HAR")
	public void initMobProxy() {
		System.out.println("Browser Mob Init");
		this.scenarioState.enableMobProxy();
	}

	// -----------------------------------------------JDE
	// Specific-----------------------------------
	// can be omitted if not using JDE scenarios

	private boolean saveSOforSAActive = false;
	private boolean soActive = false;
	private boolean fetchSOActive = false;

	private void jdePreExecutionDataFetching() {
		if (soActive) {
			if ((ExcelInput.fetchData(scenarioState.getFeatureName(), scenarioState.getDataSetName(), "selleragreement_flag")
					.equalsIgnoreCase("yes"))) {
				SellerAgreement.getInstance().setMap(
						ExcelInput.fetchData(scenarioState.getFeatureName(), scenarioState.getDataSetName(), "Map"));
				SellerAgreement.getInstance().fetch(StockOrder.getInstance().getMap());
			}
			
			StockOrder.getInstance().setMap(
					ExcelInput.fetchData(scenarioState.getFeatureName(), scenarioState.getDataSetName(), "Map"));
			SaleHeader.getInstance().fetch(StockOrder.getInstance().getMap());
			
		}
		if (saveSOforSAActive) {
			StockOrder.getInstance().setMap(
					ExcelInput.fetchData(scenarioState.getFeatureName(), scenarioState.getDataSetName(), "Map"));

			if (!(ExcelInput.fetchData(scenarioState.getFeatureName(), scenarioState.getDataSetName(), "isPrivateSale")
					.equalsIgnoreCase("yes"))) {
				SaleHeader.getInstance().fetch(StockOrder.getInstance().getMap());
			}

		}
		if (fetchSOActive) {
			String map = ExcelInput.fetchData(scenarioState.getFeatureName(), scenarioState.getDataSetName(), "Map");
			map = map.replaceAll(",", " ");
			if (map.toLowerCase().contains("s")) {
				map = map.replaceAll("s", "");
				map = map.replaceAll("S", "");
			}
			SellerAgreement.getInstance().setMap(map);
			StockOrder.getInstance().fetch(map);
		}
	}

	@After
	public void tearDown(Scenario scenario) {
		scenarioState.destroy();
	}

	@After("@SA")
	public void saveSellerAgreement(Scenario scenario) {
		if (!scenario.isFailed())
			SellerAgreement.getInstance().dump();
	}

	@Before("@SH")
	public void initSaleHeader() {
		// ExcelSheet.initOutputWorkbook();
	}

	@After("@SH")
	public void saveSaleHeader(Scenario scenario) {
		if (!scenario.isFailed())
			SaleHeader.getInstance().dump();
	}

	@Before("@SO")
	public void initStockOrder() {
		// ExcelSheet.initOutputWorkbook();
		soActive = true;
	}

	@After("@UpdateStatusSO")
	public void saveStockOrder(Scenario scenario) {
		if (!scenario.isFailed())
			StockOrder.getInstance().dump();
	}

	@Before("@SaveSOforSA")
	public void initStockOrderForSA() {
		// ExcelSheet.initOutputWorkbook();
		saveSOforSAActive = true;
	}

	@After("@SaveSOforSA")
	public void saveStockOrderForSellerAgreement(Scenario scenario) {
		if (!scenario.isFailed())
			StockOrder.getInstance().dump();

	}

	@Before("@fetchSO")
	public void initSellerAgreement() {
		fetchSOActive = true;
	}
}