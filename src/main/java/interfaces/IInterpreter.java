package interfaces;

import java.util.List;

public interface IInterpreter {

	public List<String> interpret(List<String> input);

	public String getText(List<String> inputs);

	boolean isOperation(String input);

}
