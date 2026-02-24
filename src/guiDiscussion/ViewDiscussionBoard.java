package guiDiscussion;

import entityClasses.User;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ViewDiscussionBoard {

	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

	protected static Stage theStage;
	protected static User theUser;
	protected static int returnRole;

	private static ViewDiscussionBoard theView;
	private static Pane theRootPane;
	private static Scene theDiscussionScene;

	private static Label label_PageTitle = new Label("Discussion Board (Ed-Style)");
	private static Label label_User = new Label();
	private static Label label_Role = new Label();

	protected static TextField text_PostTitle = new TextField();
	protected static TextField text_UpdateTitle = new TextField();
	protected static TextField text_SearchKeyword = new TextField();
	protected static TextArea text_PostBody = new TextArea();
	protected static TextArea text_PostOutput = new TextArea();

	protected static TextField text_ReplyPostTitle = new TextField();
	protected static TextArea text_ReplyBody = new TextArea();
	private static Label label_ReplyHelp = new Label("Reply to this post: type the title, then your reply body below.");

	private static Button button_CreatePost = new Button("Create Post");
	private static Button button_UpdatePost = new Button("Update Post");
	private static Button button_DeletePost = new Button("Delete Post");
	private static Button button_ShowPosts = new Button("Show All Posts");
	private static Button button_SearchKeyword = new Button("Search Keyword");

	private static Button button_CreateReply = new Button("Create Reply");
	private static Button button_ShowRepliesForPost = new Button("Replies For Post");

	private static Button button_Return = new Button("Return");

	private static Alert alertError = new Alert(Alert.AlertType.INFORMATION);

	public static void displayDiscussionBoard(Stage ps, User user, int roleToReturn) {
		theStage = ps;
		theUser = user;
		returnRole = roleToReturn;

		if (theView == null) {
			theView = new ViewDiscussionBoard();
		}

		label_User.setText("User: " + user.getUserName());
		label_Role.setText("Role: " + ViewDiscussionBoard.getRoleLabel(user));
		ControllerDiscussionBoard.showAllPosts();
		label_ReplyHelp.setText("Reply to this post: type the title, then your reply body below.");
		button_DeletePost.setVisible(user.getAdminRole() || user.getStaffRole());

		theStage.setTitle("CSE 360 Foundations: Discussion Board");
		theStage.setScene(theDiscussionScene);
		theStage.show();
	}

	private ViewDiscussionBoard() {
		theRootPane = new Pane();
		theDiscussionScene = new Scene(theRootPane, width, height);

		setupLabel(label_PageTitle, 28, width, 0, 5);
		setupLabel(label_User, 18, width, 20, 45);
		setupLabel(label_Role, 16, width, 20, 70);

		setupLabel(new Label("Title:"), 14, 60, 20, 115);
		setupField(text_PostTitle, 410, 90, 115, "Unique post title");

		setupLabel(new Label("New Title (optional):"), 12, 150, 20, 145);
		setupField(text_UpdateTitle, 410, 170, 145, "Leave empty to keep title");

		setupArea(text_PostBody, 410, 80, 90, 175, "Post body");
		setupArea(text_PostOutput, 760, 390, 20, 255, "");

		setupButton(button_CreatePost, 120, 520, 145, (_) -> ControllerDiscussionBoard.createPost());
		setupButton(button_UpdatePost, 120, 520, 175, (_) -> ControllerDiscussionBoard.updatePost());
		setupButton(button_DeletePost, 120, 520, 205, (_) -> ControllerDiscussionBoard.deletePost());
		setupButton(button_ShowPosts, 120, 650, 145, (_) -> ControllerDiscussionBoard.showAllPosts());

		setupLabel(new Label("Keyword:"), 14, 80, 520, 240);
		setupField(text_SearchKeyword, 120, 590, 240, "search text");
		setupButton(button_SearchKeyword, 120, 720, 240, (_) -> ControllerDiscussionBoard.searchPostsByKeyword());

		setupLabel(new Label("Reply To Title:"), 14, 120, 20, 470);
		setupField(text_ReplyPostTitle, 280, 140, 470, "Exact post title");
		setupLabel(label_ReplyHelp, 12, 760, 20, 500);
		setupArea(text_ReplyBody, 350, 70, 220, 520, "Reply body");

		setupButton(button_CreateReply, 160, 590, 470, (_) -> ControllerDiscussionBoard.createReply());
		setupButton(button_ShowRepliesForPost, 160, 590, 505, (_) -> ControllerDiscussionBoard.listRepliesForPost());

		setupButton(button_Return, 120, 740, 15, (_) -> ControllerDiscussionBoard.performReturn());

		alertError.setTitle("Discussion Error");
		alertError.setHeaderText(null);

		theRootPane.getChildren().addAll(
				text_PostTitle, text_UpdateTitle, text_SearchKeyword, text_PostBody,
				text_PostOutput,
				text_ReplyPostTitle, text_ReplyBody,
				button_CreatePost, button_UpdatePost, button_DeletePost, button_ShowPosts,
				button_SearchKeyword,
				button_CreateReply,
				button_ShowRepliesForPost, button_Return);
	}

	private static String getRoleLabel(User user) {
		if (user.getAdminRole()) {
			return "Admin";
		}
		if (user.getStaffRole()) {
			return "Staff";
		}
		return "Student";
	}

	protected static void showError(String message) {
		alertError.setContentText(message);
		alertError.showAndWait();
	}

	private void setupLabel(Label label, double fontSize, double width, double x, double y) {
		label.setFont(Font.font("Arial", fontSize));
		label.setMinWidth(width);
		label.setAlignment(Pos.BASELINE_LEFT);
		label.setLayoutX(x);
		label.setLayoutY(y);
		theRootPane.getChildren().add(label);
	}

	private void setupField(TextField field, double width, double x, double y, String prompt) {
		field.setMinWidth(width);
		field.setLayoutX(x);
		field.setLayoutY(y);
		field.setPromptText(prompt);
	}

	private void setupArea(TextArea area, double width, double height, double x, double y, String prompt) {
		area.setPrefWidth(width);
		area.setPrefHeight(height);
		area.setLayoutX(x);
		area.setLayoutY(y);
		area.setPromptText(prompt);
		area.setWrapText(true);
	}

	private void setupButton(Button button, double width, double x, double y,
				javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
		button.setMinWidth(width);
		button.setLayoutX(x);
		button.setLayoutY(y);
		button.setOnAction(handler);
	}
}
