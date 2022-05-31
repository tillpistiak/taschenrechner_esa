package logic;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

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
	private Controller controller;

	@Test
	public void testSolve() {
		doReturn(Arrays.asList("Test")).when(interpreter).interpret(any());

		String result = controller.solve();

		assertThat(result, is("Test"));
		assertThat(controller.getText(), is("Test"));

		verify(controller, times(1)).clear();
	}

	@Test
	public void testGetText() {
		doReturn("Test").when(interpreter).getText(any());

		String result = controller.getText();

		assertThat(result, is("Test"));
	}

}
