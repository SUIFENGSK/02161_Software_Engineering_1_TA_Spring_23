package dtu.library.acceptance_tests;

import io.cucumber.java.en.Given;

public class TimeSteps {
	
	MockDateHolder dateHolder;
	
	public TimeSteps(MockDateHolder dateHolder) {
		this.dateHolder = dateHolder;
	}
	
	@Given("{int} days have passed")
	public void daysHavePassed(int days) throws Exception {
	    dateHolder.advanceDateByDays(days);
	}
}

