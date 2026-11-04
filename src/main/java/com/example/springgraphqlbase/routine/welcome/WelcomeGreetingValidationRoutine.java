package com.example.springgraphqlbase.routine.welcome;

import com.example.springgraphqlbase.annotation.QueryRoute;
import com.example.springgraphqlbase.routine.QueryRouteContext;
import com.example.springgraphqlbase.routine.QueryRouteRoutine;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(10)
@QueryRoute("welcome")
public class WelcomeGreetingValidationRoutine implements QueryRouteRoutine {

	@Override
	public void apply(QueryRouteContext context) {
		if (!context.message().startsWith("Welcome")) {
			throw new IllegalStateException("Route welcome requires a Welcome greeting prefix.");
		}
		context.addStep(getClass().getSimpleName());
	}
}
