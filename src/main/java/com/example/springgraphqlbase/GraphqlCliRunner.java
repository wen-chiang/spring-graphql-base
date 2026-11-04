package com.example.springgraphqlbase;

import java.io.Console;

import graphql.ExecutionResult;
import org.springframework.boot.CommandLineRunner;
import org.springframework.graphql.execution.GraphQlSource;
import org.springframework.stereotype.Component;

@Component
class GraphqlCliRunner implements CommandLineRunner {

	private static final String DEFAULT_QUERY = "{ hello }";

	private final GraphQlSource graphQlSource;

	GraphqlCliRunner(GraphQlSource graphQlSource) {
		this.graphQlSource = graphQlSource;
	}

	@Override
	public void run(String... args) {
		String query = normalizeQuery(readQuery(args));
		ExecutionResult result = graphQlSource.graphQl().execute(query);
		if (!result.getErrors().isEmpty()) {
			System.err.println("GraphQL errors: " + result.getErrors());
			return;
		}
		System.out.println("GraphQL result: " + result.getData());
	}

	private String readQuery(String[] args) {
		if (args.length > 0) {
			String queryFromArgs = String.join(" ", args).trim();
			if (!queryFromArgs.isBlank()) {
				return queryFromArgs;
			}
		}
		Console console = System.console();
		if (console == null) {
			return DEFAULT_QUERY;
		}
		String input = console.readLine("GraphQL query (default %s): ", DEFAULT_QUERY);
		if (input == null || input.isBlank()) {
			return DEFAULT_QUERY;
		}
		return input;
	}

	private String normalizeQuery(String query) {
		String normalized = query.trim();
		if (normalized.length() >= 2 && normalized.startsWith("\"") && normalized.endsWith("\"")) {
			normalized = normalized.substring(1, normalized.length() - 1);
		}
		normalized = normalized.replace("\\\"", "\"");
		normalized = normalized.replaceAll(":\\\\([A-Za-z0-9_]+)\\\\", ":\"$1\"");
		normalized = normalized.replaceAll("name\\s*:\\s*([A-Za-z_][A-Za-z0-9_]*)", "name:\"$1\"");
		return normalized.replaceAll("env\\s*:\\s*([A-Za-z_][A-Za-z0-9_]*)", "env:\"$1\"");
	}
}
