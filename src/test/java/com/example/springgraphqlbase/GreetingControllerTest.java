package com.example.springgraphqlbase;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.List;

import com.example.springgraphqlbase.annotation.QueryRoute;
import com.example.springgraphqlbase.routine.error.ErrorEvenFailedRoutine;
import com.example.springgraphqlbase.routine.error.ErrorMainRoutine;
import com.example.springgraphqlbase.routine.error.ErrorOnlyFailedRoutine;
import com.example.springgraphqlbase.routine.QueryRouteContext;
import com.example.springgraphqlbase.routine.QueryRouteRoutine;
import com.example.springgraphqlbase.routine.QueryRouteRoutineExecutor;
import com.example.springgraphqlbase.routine.common.CommonRouteAuditRoutine;
import com.example.springgraphqlbase.routine.hello.HelloGreetingFormatRoutine;
import com.example.springgraphqlbase.routine.hello.HelloProdProfileRoutine;
import com.example.springgraphqlbase.routine.hello.HelloGreetingValidationRoutine;
import com.example.springgraphqlbase.routine.welcome.WelcomeGreetingValidationRoutine;
import org.junit.jupiter.api.Test;

class GreetingControllerTest {

	private final QueryRouteRoutineExecutor executor = new QueryRouteRoutineExecutor(List.of(
			new CommonRouteAuditRoutine(),
			new HelloGreetingFormatRoutine(),
			new HelloGreetingValidationRoutine(),
			new HelloProdProfileRoutine(),
			new WelcomeGreetingValidationRoutine(),
			new ErrorMainRoutine(),
			new ErrorEvenFailedRoutine(),
			new ErrorOnlyFailedRoutine()));
	private final GreetingController greetingController = new GreetingController(executor);

	@Test
	void shouldResolveHelloQuery() {
		assertThat(greetingController.hello("Spring", null)).isEqualTo(
				"Hello, Spring! [route=hello, env=default, routines=[CommonRouteAuditRoutine, HelloGreetingValidationRoutine, HelloGreetingFormatRoutine], failed=false, errors=[]]");
	}

	@Test
	void shouldUseWorldWhenNameMissing() {
		assertThat(greetingController.hello(" ", null)).isEqualTo(
				"Hello, World! [route=hello, env=default, routines=[CommonRouteAuditRoutine, HelloGreetingValidationRoutine, HelloGreetingFormatRoutine], failed=false, errors=[]]");
	}

	@Test
	void shouldResolveWelcomeQueryHandler() {
		assertThat(greetingController.welcome("Spring", null)).isEqualTo(
				"Welcome aboard, Spring! [route=welcome, env=default, routines=[CommonRouteAuditRoutine, WelcomeGreetingValidationRoutine], failed=false, errors=[]]");
	}

	@Test
	void shouldUseGenericWelcomeWhenNameMissing() {
		assertThat(greetingController.welcome(" ", null)).isEqualTo(
				"Welcome aboard, World! [route=welcome, env=default, routines=[CommonRouteAuditRoutine, WelcomeGreetingValidationRoutine], failed=false, errors=[]]");
	}

	@Test
	void shouldCarryQueryRouteMetadata() throws Exception {
		Method helloMethod = GreetingController.class.getDeclaredMethod("hello", String.class, String.class);
		Method welcomeMethod = GreetingController.class.getDeclaredMethod("welcome", String.class, String.class);

		assertThat(helloMethod.getAnnotation(QueryRoute.class).value()).containsExactly("hello");
		assertThat(welcomeMethod.getAnnotation(QueryRoute.class).value()).containsExactly("welcome");
	}

	@Test
	void shouldExecuteOnlyMatchingRouteRoutinesInOrder() {
		QueryRouteContext context = new QueryRouteContext("hello", "World", "default", "Hello, World!");

		executor.execute("hello", context);

		assertThat(context.executedSteps()).containsExactly(
				"CommonRouteAuditRoutine",
				"HelloGreetingValidationRoutine",
				"HelloGreetingFormatRoutine");
	}

	@Test
	void shouldRunEvenAndOnlyFailedRoutinesForErrorRoute() {
		assertThat(greetingController.error("default")).isEqualTo(
				"Error flow even-failed-continued only-failed-branch [route=error, env=default, routines=[ErrorMainRoutine, ErrorEvenFailedRoutine, ErrorOnlyFailedRoutine], failed=true, errors=[ErrorMainRoutine: Simulated error in main routine.]]");
	}

	@Test
	void shouldRunEnvMatchedRoutineOnly() {
		assertThat(greetingController.hello("Spring", "prod")).isEqualTo(
				"Hello, Spring! [route=hello, env=prod, routines=[CommonRouteAuditRoutine, HelloGreetingValidationRoutine, HelloGreetingFormatRoutine, HelloProdProfileRoutine], failed=false, errors=[]]");
		assertThat(greetingController.hello("Spring", "dev")).isEqualTo(
				"Hello, Spring! [route=hello, env=dev, routines=[CommonRouteAuditRoutine, HelloGreetingValidationRoutine, HelloGreetingFormatRoutine], failed=false, errors=[]]");
	}

	@Test
	void shouldSupportMultipleQueryRouteNamesOnOneRoutine() {
		QueryRouteRoutineExecutor multiRouteExecutor = new QueryRouteRoutineExecutor(List.of(new MultiRouteRoutine()));
		QueryRouteContext context = new QueryRouteContext("hello", "World", "default", "Hello, World!");

		multiRouteExecutor.execute("hello", context);

		assertThat(context.executedSteps()).containsExactly("MultiRouteRoutine");
	}

	@QueryRoute({"hello", "helloAlias"})
	private static class MultiRouteRoutine implements QueryRouteRoutine {

		@Override
		public void apply(QueryRouteContext context) {
			context.addStep(getClass().getSimpleName());
		}
	}
}
