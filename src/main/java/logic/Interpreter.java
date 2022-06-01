package logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import interfaces.IInterpreter;

public class Interpreter implements IInterpreter {

	// public Strings
	public static final String NUMBER_REGEX = "(\\-)?[0-9]+((\\.){1}[0-9]+)?";
	public static final String OPERATION_REGEX = "[\\+\\-\\*\\/]";

	// private Strings
	private static final String INVALID_INPUT = "Invalid Input";
	private static final String NOT_A_NUMBER = "NaN";
	private static final String MUL_DIV_REGEX = "[\\*\\/]";

	// Predicates
	public static final Predicate<String> IS_OPERATION = val -> val.matches(OPERATION_REGEX);
	public static final Predicate<String> IS_NUMBER = val -> val.matches(NUMBER_REGEX);

	@Override
	public List<String> interpret(List<String> input) {
		if (!validateSyntax(getText(input))) {
			return Arrays.asList(INVALID_INPUT);
		}
		return solveEquation(Collections.unmodifiableList(input));
	}

	private List<String> solveEquation(List<String> input) {
		List<String> equationWithSolvedMulDiv = solveMulDiv(input);

		if (equationWithSolvedMulDiv.contains(NOT_A_NUMBER)) {
			return equationWithSolvedMulDiv;
		}

		return solveAddSub(equationWithSolvedMulDiv);
	}

	double calculate(String input, double d1, double d2) {
		if (input.contains("+")) {
			return d1 + d2;
		}
		if (input.contains("-")) {
			return d1 - d2;
		}
		if (input.contains("*")) {
			return d1 * d2;
		}
		if (input.contains("/")) {
			return d2 != 0
					? d1 / d2
					: Double.NaN;
		}
		throw new IllegalArgumentException();
	}

	List<String> solveAddSub(List<String> input) {
		List<Double> numbers = extractNumbers(input);
		List<String> operations = extractOperations(input);
		double result = numbers.get(0);
		for (int i = 0; i < operations.size(); i++) {
			result = calculate(operations.get(i), result, numbers.get(i + 1));
		}
		return Arrays.asList(result + "");
	}

	private List<String> extractOperations(List<String> input) {
		return input.stream()
				.filter(IS_OPERATION)
				.collect(Collectors.toList());
	}

	private List<Double> extractNumbers(List<String> input) {
		return input.stream()
				.filter(IS_NUMBER)
				.map(Double::parseDouble)
				.collect(Collectors.toList());
	}

	List<String> solveMulDiv(List<String> input) {
		List<String> solved = new ArrayList<>(input);
		for (int i = 0; i < solved.size(); i++) {
			if (solved.get(i).matches(MUL_DIV_REGEX)) {
				double v1 = Double.parseDouble(solved.remove(i - 1));
				double v2 = Double.parseDouble(solved.remove(i));
				String operation = solved.get(i - 1);
				String result = "" + calculate(operation, v1, v2);
				solved.set(i - 1, result);
				i--;
			}
		}
		return Collections.unmodifiableList(solved);
	}

	boolean validateSyntax(String input) {
		return input.matches(NUMBER_REGEX + "(" + OPERATION_REGEX + "{1}" + NUMBER_REGEX + ")*");
	}

	@Override
	public String getText(List<String> inputs) {
		return inputs.stream().reduce("", (acc, val) -> acc += val);
	}

	@Override
	public boolean isOperation(String input) {
		return IS_OPERATION.test(input);
	}
}
