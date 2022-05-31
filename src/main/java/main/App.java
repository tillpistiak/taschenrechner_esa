package main;

import java.io.IOException;

import interfaces.IController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.Controller;
import logic.Interpreter;

/**
 * JavaFX App
 */
public class App extends Application {

	private static final String DELETE = "\u232B";
	private static final String TITLE = "Calculator";
	private static final double SCENE_WIDTH = 400;
	private static final double SCENE_HEIGTH = 400;
	private static final double ROW_COUNT = 4;
	private static final double COLUMN_COUNT = 4;
	private TextField tf;
	private IController controller = new Controller(new Interpreter());

	/**
	 * JavaFX start method
	 */
	@Override
	public void start(Stage stage) throws IOException {
		Scene scene = new Scene(getView(), SCENE_WIDTH, SCENE_HEIGTH);
		stage.setTitle(TITLE);
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch();
	}

	private Pane getView() {
		VBox vbox = new VBox();
		tf = new TextField();
		tf.setDisable(true);
		vbox.getChildren().add(tf);
		vbox.getChildren().add(getGrid());
		return vbox;
	}

	private Pane getGrid() {
		GridPane grid = new GridPane();
		grid.addRow(0, getButton("+"), getButton("-"), getButton("*"), getButton("/"));
		grid.addRow(1, getButton("7"), getButton("8"), getButton("9"), getSolveButton("="));
		grid.addRow(2, getButton("4"), getButton("5"), getButton("6"), getDeleteClearButtons());
		grid.addRow(3, getButton("1"), getButton("2"), getButton("3"), getButton("0"));
		return grid;
	}

	private Button getButton(String text) {
		Button b = getDefaultButton(text);
		b.setOnAction(e -> {
			tf.setText(controller.addValue(text));
		});
		return b;
	}

	private Button getDefaultButton(String text) {
		Button b = new Button(text);
		b.setPrefSize(SCENE_WIDTH / COLUMN_COUNT, SCENE_HEIGTH / ROW_COUNT);
		return b;
	}

	private VBox getDeleteClearButtons() {
		Button delete = new Button(DELETE);
		delete.setPrefSize(SCENE_WIDTH / COLUMN_COUNT, SCENE_HEIGTH / ROW_COUNT / 2);
		delete.setOnAction(e -> {
			tf.setText(controller.delete());
		});
		Button clear = new Button("Clear");
		clear.setPrefSize(SCENE_WIDTH / COLUMN_COUNT, SCENE_HEIGTH / ROW_COUNT / 2);
		clear.setOnAction(e -> {
			tf.setText(controller.clear());
		});

		VBox buttons = new VBox(delete, clear);
		return buttons;
	}

	private Button getSolveButton(String text) {
		Button b = getDefaultButton(text);
		b.setOnAction(e -> {
			tf.setText(controller.solve());
		});
		return b;
	}

}