package logic;

import java.util.ArrayList;
import java.util.List;

import interfaces.IController;
import interfaces.IInterpreter;

public class Controller implements IController {

	private List<String> inputs;
	private IInterpreter interpreter;

	public Controller(IInterpreter interpreter) {
		inputs = new ArrayList<>();
		this.interpreter = interpreter;
	}

	@Override
	public String addValue(String value) {
		if (interpreter.isOperation(value)) {
			addOperation(value);
		} else {
			addNumericValue(value);
		}
		return getText();
	}

	@Override
	public String delete() {
		getInputs().remove(getInputs().size() - 1);
		return getText();
	}

	@Override
	public String clear() {
		getInputs().clear();
		return getText();
	}

	@Override
	public String solve() {
		List<String> result = interpreter.interpret(getInputs());
		clear();
		getInputs().addAll(result);
		return getText();
	}

	void addNumericValue(String value) {
		int lastIndex = getInputs().size() - 1;
		if (getInputs().isEmpty()) {
			getInputs().add(value);
		} else if (interpreter.isOperation(getInputs().get(lastIndex))
				&& (getInputs().size() != 1 && !interpreter.isOperation(getInputs().get(lastIndex - 1)))) {
			getInputs().add(value);
		} else {
			getInputs().set(lastIndex, getInputs().get(lastIndex) + value);
		}

	}

	void addOperation(String value) {
		int lastIndex = getInputs().size() - 1;
		if (getInputs().isEmpty() && value.matches("\\-")) {
			getInputs().add(value);
		} else if (!getInputs().isEmpty()
				&& (!interpreter.isOperation(getInputs().get(lastIndex)))) {
			getInputs().add(value);
		} else if (getInputs().size() >= 2 && value.matches("\\-")
				&& interpreter.isOperation(getInputs().get(lastIndex))
				&& !interpreter.isOperation(getInputs().get(lastIndex - 1))) {
			getInputs().add(value);
		}
	}

	@Override
	public String getText() {
		return interpreter.getText(getInputs());
	}

	List<String> getInputs() {
		return inputs;
	}

}
