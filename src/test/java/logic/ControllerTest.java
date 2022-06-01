package logic;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ControllerTest {

	@Spy
	private Interpreter interpreter;

	@Spy
	@InjectMocks
	private Controller classUnderTest;

	private List<String> inputs = new ArrayList<>();

	@Before
	public void setup() {
		doReturn(inputs).when(classUnderTest).getInputs();
	}

	@Test
	public void testSolve() {
		doReturn(Arrays.asList("Test")).when(interpreter).interpret(any());

		String result = classUnderTest.solve();

		assertThat(result, is("Test"));
		assertThat(classUnderTest.getText(), is("Test"));

		verify(classUnderTest, times(1)).clear();
	}

	@Test
	public void testGetText() {
		doReturn("Test").when(interpreter).getText(any());

		String result = classUnderTest.getText();

		assertThat(result, is("Test"));

		verify(interpreter, times(1)).getText(inputs);
	}

	@Test
	public void testAddValueOperation() {
		doReturn(true).when(interpreter).isOperation("*");

		classUnderTest.addValue("*");

		verify(classUnderTest, times(1)).addOperation("*");
		verify(classUnderTest, never()).addNumericValue(any());
		verify(classUnderTest, times(1)).getText();
	}

	@Test
	public void testAddValueNumber() {
		doReturn(false).when(interpreter).isOperation("-12");

		classUnderTest.addValue("-12");

		verify(classUnderTest, times(1)).addNumericValue("-12");
		verify(classUnderTest, never()).addOperation(any());
		verify(classUnderTest, times(1)).getText();
	}

	@Test
	public void testAddNumericValueFirstValue() {
		classUnderTest.addNumericValue("-12");

		assertThat(inputs, hasItem("-12"));
	}

	@Test
	public void testAddNumericValueLatestValueIsNumber() {
		inputs.add("-1");

		classUnderTest.addNumericValue("2");

		assertThat(inputs, hasItem("-12"));
	}

	@Test
	public void testAddNumericValueLastValueIsOperation() {
		inputs.add("-1");
		inputs.add("+");

		classUnderTest.addNumericValue("2");

		assertThat(inputs, hasItem("2"));
	}

	@Test
	public void testAddNumericValueLastValuesAreOperations() {
		inputs.add("-1");
		inputs.add("-");
		inputs.add("*");
		classUnderTest.addNumericValue("2");

		assertThat(inputs, not(hasItem("2")));
	}

	@Test
	public void testAddNumericValueLatestValueIsDash() {
		inputs.add("-1");
		inputs.add("-");
		inputs.add("-");

		classUnderTest.addNumericValue("2");

		assertThat(inputs, hasItem("-2"));
	}

	@Test
	public void testAddNumericValueOnlyValueIsDash() {
		inputs.add("-");

		classUnderTest.addNumericValue("2");

		assertThat(inputs, hasItem("-2"));
	}

	@Test
	public void testAddOperationFirstValueDash() {
		classUnderTest.addOperation("-");

		assertThat(inputs, hasItem("-"));
	}

	@Test
	public void testAddOperationLatestValueIsNumber() {
		inputs.add("-1");

		classUnderTest.addOperation("-");

		assertThat(inputs, hasItem("-"));
	}

	@Test
	public void testAddOperationLatestValueIsOperationNotDash() {
		inputs.add("-1");
		inputs.add("*");

		classUnderTest.addOperation("+");

		assertThat(inputs, not(hasItem("+")));
	}

	@Test
	public void testAddOperationLatestValueIsOperationAddDash() {
		inputs.add("-1");
		inputs.add("*");

		classUnderTest.addOperation("-");

		assertThat(inputs, hasItem("-"));
	}

	@Test
	public void testAddOperationLatest2ValuesAreOperationAddDash() {
		inputs.add("-1");
		inputs.add("*");
		inputs.add("-");

		classUnderTest.addOperation("-");

		assertThat(inputs.size(), is(3));
	}

	@Test
	public void testDelete() {
		inputs.add("-1");
		inputs.add("*");

		classUnderTest.delete();

		assertThat(inputs.size(), is(1));
		assertThat(inputs, hasItem("-1"));
	}

	@Test
	public void testClear() {
		inputs.add("-1");
		inputs.add("*");

		classUnderTest.clear();

		assertThat(inputs.size(), is(0));
	}

}
