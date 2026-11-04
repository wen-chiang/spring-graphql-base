package com.example.springgraphqlbase.routine.hello;

import com.example.springgraphqlbase.annotation.EnvProfile;
import com.example.springgraphqlbase.annotation.QueryRoute;
import com.example.springgraphqlbase.routine.QueryRouteContext;
import com.example.springgraphqlbase.routine.QueryRouteRoutine;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(30)
@QueryRoute("hello")
@EnvProfile({"prod"})
public class HelloProdProfileRoutine implements QueryRouteRoutine {

	@Override
	public void apply(QueryRouteContext context) {
		context.addStep(getClass().getSimpleName());
	}
}
