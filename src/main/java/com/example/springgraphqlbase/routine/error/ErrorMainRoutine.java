package com.example.springgraphqlbase.routine.error;

import com.example.springgraphqlbase.annotation.QueryRoute;
import com.example.springgraphqlbase.routine.QueryRouteContext;
import com.example.springgraphqlbase.routine.QueryRouteRoutine;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(10)
@QueryRoute("error")
public class ErrorMainRoutine implements QueryRouteRoutine {

	@Override
	public void apply(QueryRouteContext context) {
		context.addStep(getClass().getSimpleName());
		throw new IllegalStateException("Simulated error in main routine.");
	}
}
