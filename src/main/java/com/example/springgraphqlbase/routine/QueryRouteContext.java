package com.example.springgraphqlbase.routine;

import java.util.ArrayList;
import java.util.List;

public class QueryRouteContext {

	private final String route;
	private final String name;
	private final String env;
	private String message;
	private final List<String> executedSteps = new ArrayList<>();
	private final List<String> errors = new ArrayList<>();
	private boolean failed;

	public QueryRouteContext(String route, String name, String env, String message) {
		this.route = route;
		this.name = name;
		this.env = env;
		this.message = message;
	}

	public String route() {
		return route;
	}

	public String name() {
		return name;
	}

	public String env() {
		return env;
	}

	public String message() {
		return message;
	}

	public void message(String message) {
		this.message = message;
	}

	public void addStep(String step) {
		executedSteps.add(step);
	}

	public List<String> executedSteps() {
		return List.copyOf(executedSteps);
	}

	public boolean failed() {
		return failed;
	}

	public void markFailed(String error) {
		this.failed = true;
		errors.add(error);
	}

	public List<String> errors() {
		return List.copyOf(errors);
	}
}
