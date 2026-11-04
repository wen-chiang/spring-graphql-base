package com.example.springgraphqlbase;

import com.example.springgraphqlbase.annotation.QueryRoute;
import com.example.springgraphqlbase.routine.QueryRouteContext;
import com.example.springgraphqlbase.routine.QueryRouteRoutineExecutor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
class GreetingController {

	private final QueryRouteRoutineExecutor routeRoutineExecutor;

	GreetingController(QueryRouteRoutineExecutor routeRoutineExecutor) {
		this.routeRoutineExecutor = routeRoutineExecutor;
	}

	@QueryRoute("hello")
	@QueryMapping("hello")
	String hello(@Argument String name, @Argument String env) {
		String resolvedName = resolveName(name);
		String resolvedEnv = resolveEnv(env);
		QueryRouteContext context = new QueryRouteContext("hello", resolvedName, resolvedEnv, "Hello, " + resolvedName + "!");
		routeRoutineExecutor.execute("hello", context);
		return withRoute(context);
	}

	@QueryRoute("welcome")
	@QueryMapping("welcome")
	String welcome(@Argument String name, @Argument String env) {
		String resolvedName = resolveName(name);
		String resolvedEnv = resolveEnv(env);
		QueryRouteContext context = new QueryRouteContext("welcome", resolvedName, resolvedEnv, "Welcome aboard, " + resolvedName + "!");
		routeRoutineExecutor.execute("welcome", context);
		return withRoute(context);
	}

	@QueryRoute("error")
	@QueryMapping("error")
	String error(@Argument String env) {
		String resolvedEnv = resolveEnv(env);
		QueryRouteContext context = new QueryRouteContext("error", "error", resolvedEnv, "Error flow");
		routeRoutineExecutor.execute("error", context);
		return withRoute(context);
	}

	private String withRoute(QueryRouteContext context) {
		return context.message()
				+ " [route=" + context.route()
				+ ", env=" + context.env()
				+ ", routines=" + context.executedSteps()
				+ ", failed=" + context.failed()
				+ ", errors=" + context.errors() + "]";
	}

	private String resolveName(String name) {
		if (name == null || name.isBlank()) {
			return "World";
		}
		return name;
	}

	private String resolveEnv(String env) {
		if (env == null || env.isBlank()) {
			return "default";
		}
		return env;
	}
}
