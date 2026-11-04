package com.example.springgraphqlbase.routine.common;

import com.example.springgraphqlbase.annotation.QueryRoute;
import com.example.springgraphqlbase.routine.QueryRouteContext;
import com.example.springgraphqlbase.routine.QueryRouteRoutine;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@QueryRoute({"hello", "welcome"})
public class CommonRouteAuditRoutine implements QueryRouteRoutine {

	@Override
	public void apply(QueryRouteContext context) {
		context.addStep(getClass().getSimpleName());
	}
}
