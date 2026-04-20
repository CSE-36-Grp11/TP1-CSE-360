package guiGradebook;

import entityClasses.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * View for the Teacher Gradebook showing student participation.
 * <p>
 * JUnit tests used: {@link applicationMain.FoundationsS26Test}
 * nSemi-automated tests used: N/A
 * nManual tests used: ViewGradebook Display Tests (see Manual Tests.pdf)
 * </p>
 */

/**
 * View for the Teacher Gradebook showing student participation.
 * <p>
 * JUnit tests used: {@link applicationMain.FoundationsS26Test}
 * nSemi-automated tests used: N/A
 * nManual tests used: ViewGradebook Display Tests (see Manual Tests.pdf)
 * </p>
 */

public class ViewGradebook {

	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

	private static ViewGradebook theView;
	private static Scene theScene;
	private static BorderPane rootPane;

	protected static Stage theStage;
	protected static User theUser;

	private static Label label_Title = new Label("Teacher Gradebook");
	private static Label label_User = new Label();
	private static Label label_Help = new Label("Pick a student to verify rule-of-three completion.");

	protected static ListView<String> list_Students = new ListView<String>();
	protected static TextArea text_Review = new TextArea();

	private static Button button_Refresh = new Button("Refresh Students");
	private static Button button_View = new Button("View Selected Student");
	private static Button button_Return = new Button("Return");

	private static Alert alert = new Alert(Alert.AlertType.INFORMATION);

	public static void displayGradebook(Stage stage, User user) {
		theStage = stage;
		theUser = user;

		if (theView == null) {
			theView = new ViewGradebook();
		}

		if (!ControllerGradebook.canAccess()) {
			showMessage("Access denied: students cannot open the grading page.");
			guiStudentHome.ViewStudentHome.displayStudentHome(theStage, theUser);
			return;
		}

		label_User.setText("Teacher Account: " + theUser.getUserName());
		ControllerGradebook.loadStudents();

		theStage.setTitle("CSE 360 Foundations: Teacher Gradebook");
		theStage.setScene(theScene);
		theStage.setMaximized(true);
		theStage.show();
	}

	private ViewGradebook() {
		rootPane = new BorderPane();
		rootPane.setPadding(new Insets(16));
		theScene = new Scene(rootPane, width, height);

		setupLabel(label_Title, 28);
		setupLabel(label_User, 18);
		setupLabel(label_Help, 14);

		label_Title.setAlignment(Pos.CENTER_LEFT);
		label_User.setAlignment(Pos.CENTER_LEFT);
		label_Help.setAlignment(Pos.CENTER_LEFT);

		VBox topBox = new VBox(4);
		topBox.getChildren().addAll(label_Title, label_User, label_Help);

		list_Students.setPrefWidth(260);
		list_Students.setMinWidth(220);

		text_Review.setWrapText(true);
		text_Review.setEditable(false);
		text_Review.setPrefHeight(500);

		setupButton(button_Refresh, 150, (_) -> ControllerGradebook.loadStudents());
		setupButton(button_View, 190, (_) -> ControllerGradebook.reviewSelectedStudent());
		setupButton(button_Return, 120, (_) -> ControllerGradebook.performReturn());

		VBox leftBox = new VBox(10);
		leftBox.getChildren().addAll(new Label("Students"), list_Students, button_Refresh, button_View);
		leftBox.setPadding(new Insets(0, 14, 0, 0));
		VBox.setVgrow(list_Students, Priority.ALWAYS);

		VBox centerBox = new VBox(8);
		centerBox.getChildren().addAll(new Label("Gradebook Review"), text_Review);
		VBox.setVgrow(text_Review, Priority.ALWAYS);

		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		HBox footer = new HBox(10);
		footer.getChildren().addAll(spacer, button_Return);

		rootPane.setTop(topBox);
		rootPane.setLeft(leftBox);
		rootPane.setCenter(centerBox);
		rootPane.setBottom(footer);
		BorderPane.setMargin(leftBox, new Insets(10, 0, 0, 0));
		BorderPane.setMargin(centerBox, new Insets(10, 0, 0, 0));
		BorderPane.setMargin(footer, new Insets(12, 0, 0, 0));

		alert.setTitle("Gradebook");
		alert.setHeaderText(null);
	}

	private static void setupLabel(Label label, double size) {
		label.setFont(Font.font("Arial", size));
	}

	private static void setupButton(Button button, double minWidth,
			javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
		button.setMinWidth(minWidth);
		button.setOnAction(handler);
	}

	private static void showMessage(String message) {
		alert.setContentText(message);
		alert.showAndWait();
	}
}
