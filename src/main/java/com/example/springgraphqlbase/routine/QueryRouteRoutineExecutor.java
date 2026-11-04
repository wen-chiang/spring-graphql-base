package com.example.springgraphqlbase.routine;

import java.util.Arrays;
import java.util.List;

import com.example.springgraphqlbase.annotation.EnvProfile;
import com.example.springgraphqlbase.annotation.EvenFailed;
import com.example.springgraphqlbase.annotation.OnlyFailed;
import com.example.springgraphqlbase.annotation.QueryRoute;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

@Component
public class QueryRouteRoutineExecutor {

	private final List<QueryRouteRoutine> routines;

	public QueryRouteRoutineExecutor(List<QueryRouteRoutine> routines) {
		this.routines = routines.stream()
				.sorted(AnnotationAwareOrderComparator.INSTANCE)
				.toList();
	}

	public void execute(String route, QueryRouteContext context) {
		for (QueryRouteRoutine routine : routines) {
			QueryRoute queryRoute = routine.getClass().getAnnotation(QueryRoute.class);
			if (!matchesRoute(route, routine, queryRoute)) {
				continue;
			}
			if (!matchesEnvProfile(context, routine)) {
				continue;
			}
			if (!shouldRunRoutine(context, routine)) {
				continue;
			}
			try {
				routine.apply(context);
			} catch (RuntimeException ex) {
				context.markFailed(routine.getClass().getSimpleName() + ": " + ex.getMessage());
			}
		}
	}

	private boolean matchesRoute(String route, QueryRouteRoutine routine, QueryRoute queryRoute) {
		return queryRoute != null
				&& Arrays.stream(queryRoute.value()).anyMatch(mappedRoute -> mappedRoute.equals(route));
	}

	private boolean matchesEnvProfile(QueryRouteContext context, QueryRouteRoutine routine) {
		EnvProfile envProfile = routine.getClass().getAnnotation(EnvProfile.class);
		if (envProfile == null) {
			return true;
		}
		return Arrays.stream(envProfile.value())
				.anyMatch(profile -> profile.equalsIgnoreCase(context.env()));
	}

	private boolean shouldRunRoutine(QueryRouteContext context, QueryRouteRoutine routine) {
		boolean routineWithEvenFailed = routine.getClass().isAnnotationPresent(EvenFailed.class);
		boolean routineWithOnlyFailed = routine.getClass().isAnnotationPresent(OnlyFailed.class);
		if (routineWithEvenFailed) {
			return true;
		}
		if (routineWithOnlyFailed) {
			return context.failed();
		}
		return !context.failed();
	}
}
