package com.example.springgraphqlbase.routine.error;

import com.example.springgraphqlbase.annotation.OnlyFailed;
import com.example.springgraphqlbase.annotation.QueryRoute;
import com.example.springgraphqlbase.routine.QueryRouteContext;
import com.example.springgraphqlbase.routine.QueryRouteRoutine;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(30)
@QueryRoute("error")
@OnlyFailed
public class ErrorOnlyFailedRoutine implements QueryRouteRoutine {

	@Override
	public void apply(QueryRouteContext context) {
		context.addStep(getClass().getSimpleName());
		context.message(context.message() + " only-failed-branch");
	}
}
