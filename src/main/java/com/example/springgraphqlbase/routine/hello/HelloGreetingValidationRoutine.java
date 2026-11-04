package com.example.springgraphqlbase.routine.hello;

import com.example.springgraphqlbase.annotation.QueryRoute;
import com.example.springgraphqlbase.routine.QueryRouteContext;
import com.example.springgraphqlbase.routine.QueryRouteRoutine;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(10)
@QueryRoute("hello")
public class HelloGreetingValidationRoutine implements QueryRouteRoutine {

	@Override
	public void apply(QueryRouteContext context) {
		if (!context.message().startsWith("Hello,")) {
			throw new IllegalStateException("Route hello requires a Hello greeting prefix.");
		}
		context.addStep(getClass().getSimpleName());
	}
}
