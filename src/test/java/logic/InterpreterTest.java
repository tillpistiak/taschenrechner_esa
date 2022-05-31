package logic;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class InterpreterTest {

	private Interpreter interpreter = new Interpreter();

	@Test
	public void testCalculate() {
		double result = interpreter.calculate("1+1", 1.0, 1.0);
		assertThat(result, is(2.0));

		result = interpreter.calculate("1-1", 1.0, 1.0);
		assertThat(result, is(0.0));

		result = interpreter.calculate("2*2", 2.0, 2.0);
		assertThat(result, is(4.0));

		result = interpreter.calculate("4/2", 4.0, 2.0);
		assertThat(result, is(2.0));
	}

	@Test
	public void testCalculateAddSub() {
		String result = interpreter.solveAddSub(Arrays.asList("1", "+", "1")).get(0);
		assertThat(result, is("2.0"));

		result = interpreter.solveAddSub(Arrays.asList("1", "+", "1", "+", "1")).get(0);
		assertThat(result, is("3.0"));

		result = interpreter.solveAddSub(Arrays.asList("1", "+", "1", "-", "1")).get(0);
		assertThat(result, is("1.0"));

		result = interpreter.solveAddSub(Arrays.asList("1", "+", "1", "-", "1", "-", "1")).get(0);
		assertThat(result, is("0.0"));

		result = interpreter.solveAddSub(Arrays.asList("1", "+", "1", "-", "1", "-", "1", "-", "1")).get(0);
		assertThat(result, is("-1.0"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCalculateException() {
		interpreter.calculate("1ü1", 1.0, 1.0);
	}

	@Test
	public void testEinfacheOperationen() {
		String result = interpreter.interpret(Arrays.asList("1", "+", "1")).get(0);
		assertThat(result, is("2.0"));

		result = interpreter.interpret(Arrays.asList("1", "-", "1")).get(0);
		assertThat(result, is("0.0"));

		result = interpreter.interpret(Arrays.asList("2", "*", "4")).get(0);
		assertThat(result, is("8.0"));

		result = interpreter.interpret(Arrays.asList("4", "/", "2")).get(0);
		assertThat(result, is("2.0"));
	}

	@Test
	public void testAddition() {
		String result = interpreter.interpret(Arrays.asList("1", "+", "1", "+", "1")).get(0);
		assertThat(result, is("3.0"));

		result = interpreter.interpret(Arrays.asList("100", "+", "1234", "+", "10")).get(0);
		assertThat(result, is("1344.0"));

	}

	@Test
	public void testSubstraction() {
		String result = interpreter.interpret(Arrays.asList("2", "-", "1")).get(0);
		assertThat(result, is("1.0"));

		result = interpreter.interpret(Arrays.asList("10", "-", "1234", "-", "2")).get(0);
		assertThat(result, is("-1226.0"));

	}

	@Test
	public void testMultiplication() {
		String result = interpreter.interpret(Arrays.asList("1", "*", "1", "*", "1")).get(0);
		assertThat(result, is("1.0"));

		result = interpreter.interpret(Arrays.asList("10", "*", "1234", "*", "2")).get(0);
		assertThat(result, is("24680.0"));
	}

	@Test
	public void testDivision() {
		String result = interpreter.interpret(Arrays.asList("1", "/", "0")).get(0);
		assertThat(result, is(Double.NaN + ""));

		result = interpreter.interpret(Arrays.asList("8", "/", "2", "/", "2")).get(0);
		assertThat(result, is("2.0"));

	}

	@Test
	public void testComplexCalculations() {
		String result = interpreter.interpret(Arrays.asList("2", "+", "3", "*", "4", "/", "8")).get(0);
		assertThat(result, is("3.5"));

		result = interpreter.interpret(Arrays.asList("2", "*", "3", "+", "16", "/", "8", "-", "-4", "/", "2")).get(0);
		assertThat(result, is("10.0"));
	}

	@Test
	public void testSingleNumber() {
		String result = interpreter.interpret(Arrays.asList("1")).get(0);
		assertThat(result, is("1.0"));
	}

	@Test
	public void testResolveMulDiv() {
		List<String> result = interpreter.solveMulDiv(Arrays.asList("1", "+", "2", "*", "2", "+", "1"));
		assertThat(result, is(Arrays.asList("1", "+", "4.0", "+", "1")));

		result = interpreter.solveMulDiv(Arrays.asList("1", "+", "2", "*", "2", "+", "1", "+", "4", "/", "2"));
		assertThat(result, is(Arrays.asList("1", "+", "4.0", "+", "1", "+", "2.0")));

		result = interpreter.solveMulDiv(Arrays.asList("1", "+", "2", "*", "2", "/", "4", "*", "2", "+", "1"));
		assertThat(result, is(Arrays.asList("1", "+", "2.0", "+", "1")));
	}

	@Test
	public void testValidateSyntax() {
		boolean result = interpreter.validateSyntax("1");
		assertThat(result, is(true));

		result = interpreter.validateSyntax("1.0");
		assertThat(result, is(true));

		result = interpreter.validateSyntax("1+1.0");
		assertThat(result, is(true));

		result = interpreter.validateSyntax("-1+1");
		assertThat(result, is(true));

		result = interpreter.validateSyntax("-1+-1");
		assertThat(result, is(true));

		result = interpreter.validateSyntax("-1-1");
		assertThat(result, is(true));

		result = interpreter.validateSyntax("1--1*2/5");
		assertThat(result, is(true));

		result = interpreter.validateSyntax("");
		assertThat(result, is(false));

		result = interpreter.validateSyntax("-1+1-");
		assertThat(result, is(false));

		result = interpreter.validateSyntax("-1++1");
		assertThat(result, is(false));

		result = interpreter.validateSyntax("1--1*2/*5");
		assertThat(result, is(false));

		result = interpreter.validateSyntax("*1+1");
		assertThat(result, is(false));

		result = interpreter.validateSyntax("1+1*");
		assertThat(result, is(false));

	}
}
