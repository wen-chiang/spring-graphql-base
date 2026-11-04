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
    │   │   └── GraphqlCliRunner.java
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

## Example queries

```graphql
{ hello(name:"Copilot", env:"prod") }
{ welcome(name:"Copilot", env:"dev") }
{ error(env:"default") }
```

Each response includes route, env, executed routines, fail state, and errors so you can verify orchestration behavior quickly.
