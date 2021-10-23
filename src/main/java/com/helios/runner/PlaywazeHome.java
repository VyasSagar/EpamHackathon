package com.helios.runner;

import org.testng.annotations.AfterClass;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.helios.utilities.RunnerBase;
import com.helios.utilities.RuntimeAnnotations;
import com.helios.utilities.StringLiterals;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.PickleEventWrapper;
import cucumber.api.testng.TestNGCucumberRunner;

@CucumberOptions()
public class PlaywazeHome extends RunnerBase implements StringLiterals {
	private TestNGCucumberRunner testNGCucumberRunner;

	@Parameters({ "tags" })
	@BeforeClass(alwaysRun = true)
	public void setUpClass(String tags) throws Exception {
		RuntimeAnnotations.putAnnotation(this.getClass(), CucumberOptions.class, getCucumberOptions(tags));
		testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
	}

	@Test(groups = "cucumber", description = "Cucmber Scenarios", dataProvider = "scenarios")
	public void feature(PickleEventWrapper pickleEventWrapper) {
		try {
			testNGCucumberRunner.runScenario(pickleEventWrapper.getPickleEvent());
		} catch (Throwable e) {
			System.out.println("[" + this.getClass().getSimpleName() + "]\tScenario Failed - "
					+ pickleEventWrapper.getPickleEvent().pickle.getName());
			e.printStackTrace();
		}
	}

	@DataProvider
	public PickleEventWrapper[] scenarios() {
		return getScenarios(testNGCucumberRunner.provideScenarios());
	}

	@AfterClass(alwaysRun = true)
	public void tearDownClass() throws Exception {
		testNGCucumberRunner.finish();
	}

	@AfterClass
	public void teardown() {
		System.out.println("Suite Complete - " + this.getClass().getSimpleName());
	}

}
