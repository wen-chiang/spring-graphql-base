package com.example.springgraphqlbase.routine.sample;

import com.example.springgraphqlbase.annotation.EnvProfile;
import com.example.springgraphqlbase.annotation.EvenFailed;
import com.example.springgraphqlbase.annotation.QueryRoute;
import com.example.springgraphqlbase.routine.QueryRouteContext;
import com.example.springgraphqlbase.routine.QueryRouteRoutine;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(15)
@QueryRoute({"hello", "welcome"})
@EnvProfile({"dev", "prod"})
@EvenFailed
public class SampleAnnotationRouteRoutine implements QueryRouteRoutine {

	@Override
	public void apply(QueryRouteContext context) {
		String suffix = context.failed() ? " [sample=recovery]" : " [sample=normal]";
		context.message(context.message() + suffix);
		context.addStep(getClass().getSimpleName());
	}
}
