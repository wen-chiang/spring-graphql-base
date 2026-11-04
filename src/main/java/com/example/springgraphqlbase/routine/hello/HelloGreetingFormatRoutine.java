package com.example.springgraphqlbase.routine.hello;

import com.example.springgraphqlbase.annotation.QueryRoute;
import com.example.springgraphqlbase.routine.QueryRouteContext;
import com.example.springgraphqlbase.routine.QueryRouteRoutine;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(20)
@QueryRoute("hello")
public class HelloGreetingFormatRoutine implements QueryRouteRoutine {

	@Override
	public void apply(QueryRouteContext context) {
		String message = context.message();
		if (!message.endsWith("!")) {
			context.message(message + "!");
		}
		context.addStep(getClass().getSimpleName());
	}
}
