package logic;

import static logic.Interpreter.OPERATION_REGEX;

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
		if (value.matches(OPERATION_REGEX)) {
			addOperation(value);
		} else {
			addNumericValue(value);
		}
		return getText();
	}

	@Override
	public String delete() {
		inputs.remove(inputs.size() - 1);
		return getText();
	}

	@Override
	public String clear() {
		inputs.clear();
		return getText();
	}

	@Override
	public String solve() {
		List<String> result = interpreter.interpret(inputs);
		clear();
		inputs.addAll(result);
		return getText();
	}

	private void addNumericValue(String value) {
		int lastIndex = inputs.size() - 1;
		if (inputs.isEmpty()) {
			inputs.add(value);
		} else if (inputs.get(lastIndex).matches(OPERATION_REGEX)
				&& (inputs.size() != 1 && !inputs.get(lastIndex - 1).matches(OPERATION_REGEX))) {
			inputs.add(value);
		} else {
			inputs.set(lastIndex, inputs.get(lastIndex) + value);
		}

	}

	private void addOperation(String value) {
		int lastIndex = inputs.size() - 1;
		if (inputs.isEmpty() && value.matches("\\-")) {
			inputs.add(value);
		} else if (!inputs.isEmpty()
				&& (!inputs.get(lastIndex).matches(OPERATION_REGEX))) {
			inputs.add(value);
		} else if (inputs.size() >= 2 && value.matches("\\-") && inputs.get(lastIndex).matches(OPERATION_REGEX)
				&& !inputs.get(lastIndex - 1).matches(OPERATION_REGEX)) {
			inputs.add(value);
		}
	}

	@Override
	public String getText() {
		return interpreter.getText(inputs);
	}
}
