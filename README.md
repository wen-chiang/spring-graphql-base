# spring-graphql-base

Spring Framework command-line project with GraphQL input, built with **Gradle Wrapper**.

This repository is a compact demo that shows these annotation-driven skills working together in one flow:
- route-based routine orchestration (`@QueryRoute`)
- multi-route binding on one routine (`@QueryRoute({"a","b"})`)
- env profile filtering (`@EnvProfile`)
- fail-path controls (`@EvenFailed`, `@OnlyFailed`)
- ordered execution (`@Order`)

## Project structure

```text
spring-graphql-base/
├── build.gradle
├── gradlew
├── gradlew.bat
├── settings.gradle
├── gradle/wrapper/
└── src/
    ├── main/
    │   ├── java/com/example/springgraphqlbase/
    │   │   ├── SpringGraphqlBaseApplication.java
    │   │   ├── GreetingController.java
    │   │   ├── GraphqlCliRunner.java
    │   │   └── routine/
    │   │       └── sample/
    │   │           └── SampleAnnotationRouteRoutine.java
    │   └── resources/
    │       ├── application.properties
    │       └── graphql/schema.graphqls
    └── test/java/com/example/springgraphqlbase/
        ├── SpringGraphqlBaseApplicationTests.java
        └── GreetingControllerTest.java
```

## Run

```bash
.\gradlew.bat bootRun --args="{ hello(name:\"Copilot\") }"
```

If no `--args` query is provided, the app uses `{ hello }` by default.

## Route routine design

`GreetingController` dispatches query execution to `QueryRouteRoutineExecutor`.
All Spring beans that implement `QueryRouteRoutine` and are annotated with
`@QueryRoute("<route-name>")` are executed for that route in `@Order` sequence.
You can still place shared routines under `routine.common`, but matching is driven by
`@QueryRoute` (including multi-route values).

For failure routing, routines can be annotated with:
- `@EvenFailed` to run even when a previous routine has failed.
- `@OnlyFailed` to run only after a previous routine has failed.

For env routing, routines can be annotated with:
- `@EnvProfile({"dev","prod"})` to run only for matching incoming `env`.

## Sample route routine

This is a minimal route file showing the annotation-driven design in action. It binds one routine to multiple routes, filters by env, and allows it to keep running after a failure.

```java
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
```

The runtime will automatically match this routine when the route is `hello` or `welcome`, only for `dev`/`prod`, and it will still execute after a prior routine fails because of `@EvenFailed`.

## Example queries

```graphql
{ hello(name:"Copilot", env:"prod") }
{ welcome(name:"Copilot", env:"dev") }
{ error(env:"default") }
```

Each response includes route, env, executed routines, fail state, and errors so you can verify orchestration behavior quickly.
