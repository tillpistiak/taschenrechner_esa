package main;

import static main.Interpreter.OPERATION_REGEX;

import java.util.ArrayList;
import java.util.List;

import interfaces.IInterpreter;

public class Controller {

	private List<String> inputs;
	private IInterpreter interpreter;

	public Controller(IInterpreter interpreter) {
		inputs = new ArrayList<>();
		this.interpreter = interpreter;
	}

	public String addValue(String value) {
		if (value.matches(OPERATION_REGEX)) {
			addOperation(value);
		} else {
			addNumericValue(value);
		}
		return getText();
	}

	public String delete() {
		inputs.remove(inputs.size() - 1);
		return getText();
	}

	public String clear() {
		inputs.clear();
		;
		return getText();
	}

	public String solve() {
		List<String> result = interpreter.interpret(inputs);
		inputs.clear();
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

	public String getText() {
		return interpreter.getText(inputs);
	}
}
