package guiDiscussion;

import entityClasses.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * View for the discussion board GUI.
 *
 * <p>This class defines and lays out the widgets used by students, staff, and admins to
 * interact with discussion posts and replies. The view delegates user actions to the controller
 * and displays formatted output returned by the model/service layers.
 *
 * <p>Students User Story alignment: create post, update post, read all posts, keyword search,
 * create reply, and list replies for a specific post.
 *
 * @author TP2 Team
 * @version 1.00 2026-03-18 MVC documentation update
 */
public class ViewDiscussionBoard {

	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

	/** Application stage used to render the discussion board scene. */
	protected static Stage theStage;
	/** Active user interacting with the discussion board. */
	protected static User theUser;
	/** Role id used for return navigation to the correct home page. */
	protected static int returnRole;

	private static ViewDiscussionBoard theView;
	private static BorderPane theRootPane;
	private static Scene theDiscussionScene;

	private static Label label_PageTitle = new Label("Discussion Board (Ed-Style)");
	private static Label label_User = new Label();
	private static Label label_Role = new Label();

	/** Input field for creating/updating post title selection. */
	protected static TextField text_PostTitle = new TextField();
	/** Optional field for replacing an existing post title. */
	protected static TextField text_UpdateTitle = new TextField();
	/** Keyword input used for subset filtering of posts. */
	protected static TextField text_SearchKeyword = new TextField();
	/** Input area for post body content. */
	protected static TextArea text_PostBody = new TextArea();
	/** Output area for formatted post/reply listing results. */
	protected static TextArea text_PostOutput = new TextArea();

	/** Input field naming the post title to reply to. */
	protected static TextField text_ReplyPostTitle = new TextField();
	/** Input area for reply body content. */
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

	/**
	 * Displays the discussion board scene for the active user.
	 *
	 * @param ps application stage
	 * @param user active logged-in user
	 * @param roleToReturn role id for return navigation (1=admin, 2=student, else=staff)
	 */
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
		theStage.setMaximized(true);
		theStage.show();
	}

	/**
	 * Builds the discussion board scene and initializes static UI widgets once.
	 */
	private ViewDiscussionBoard() {
		theRootPane = new BorderPane();
		theRootPane.setPadding(new Insets(16));
		theDiscussionScene = new Scene(theRootPane, width, height);

		VBox headerBox = new VBox(4);
		headerBox.getChildren().addAll(label_PageTitle, label_User, label_Role);

		GridPane postFormGrid = new GridPane();
		postFormGrid.setHgap(8);
		postFormGrid.setVgap(8);
		ColumnConstraints labelCol = new ColumnConstraints();
		labelCol.setMinWidth(130);
		ColumnConstraints fieldCol = new ColumnConstraints();
		fieldCol.setHgrow(Priority.ALWAYS);
		postFormGrid.getColumnConstraints().addAll(labelCol, fieldCol);

		Label labelTitle = new Label("Title:");
		Label labelNewTitle = new Label("New Title (optional):");
		Label labelBody = new Label("Post Body:");

		setupLabel(label_PageTitle, 28, Pos.CENTER_LEFT);
		setupLabel(label_User, 18, Pos.CENTER_LEFT);
		setupLabel(label_Role, 16, Pos.CENTER_LEFT);
		setupLabel(labelTitle, 14, Pos.CENTER_LEFT);
		setupLabel(labelNewTitle, 12, Pos.CENTER_LEFT);
		setupLabel(labelBody, 12, Pos.CENTER_LEFT);

		setupField(text_PostTitle, "Unique post title");
		setupField(text_UpdateTitle, "Leave empty to keep title");
		setupArea(text_PostBody, 120, "Post body");

		postFormGrid.add(labelTitle, 0, 0);
		postFormGrid.add(text_PostTitle, 1, 0);
		postFormGrid.add(labelNewTitle, 0, 1);
		postFormGrid.add(text_UpdateTitle, 1, 1);
		postFormGrid.add(labelBody, 0, 2);
		postFormGrid.add(text_PostBody, 1, 2);

		HBox postActions = new HBox(10);
		setupButton(button_CreatePost, 120, (_) -> ControllerDiscussionBoard.createPost());
		setupButton(button_UpdatePost, 120, (_) -> ControllerDiscussionBoard.updatePost());
		setupButton(button_DeletePost, 120, (_) -> ControllerDiscussionBoard.deletePost());
		setupButton(button_ShowPosts, 120, (_) -> ControllerDiscussionBoard.showAllPosts());
		postActions.getChildren().addAll(button_CreatePost, button_UpdatePost, button_DeletePost, button_ShowPosts);

		Label labelKeyword = new Label("Keyword:");
		setupLabel(labelKeyword, 14, Pos.CENTER_LEFT);
		setupField(text_SearchKeyword, "search text");
		setupButton(button_SearchKeyword, 140, (_) -> ControllerDiscussionBoard.searchPostsByKeyword());

		HBox searchRow = new HBox(8);
		searchRow.setAlignment(Pos.CENTER_LEFT);
		searchRow.getChildren().addAll(labelKeyword, text_SearchKeyword, button_SearchKeyword);
		HBox.setHgrow(text_SearchKeyword, Priority.ALWAYS);

		Label labelReplyTitle = new Label("Reply To Title:");
		Label labelReplyBody = new Label("Reply Body:");
		setupLabel(labelReplyTitle, 14, Pos.CENTER_LEFT);
		setupLabel(label_ReplyHelp, 12, Pos.CENTER_LEFT);
		setupLabel(labelReplyBody, 12, Pos.CENTER_LEFT);

		setupField(text_ReplyPostTitle, "Exact post title");
		setupArea(text_ReplyBody, 110, "Reply body");

		GridPane replyFormGrid = new GridPane();
		replyFormGrid.setHgap(8);
		replyFormGrid.setVgap(8);
		ColumnConstraints replyLabelCol = new ColumnConstraints();
		replyLabelCol.setMinWidth(130);
		ColumnConstraints replyFieldCol = new ColumnConstraints();
		replyFieldCol.setHgrow(Priority.ALWAYS);
		replyFormGrid.getColumnConstraints().addAll(replyLabelCol, replyFieldCol);

		replyFormGrid.add(labelReplyTitle, 0, 0);
		replyFormGrid.add(text_ReplyPostTitle, 1, 0);
		replyFormGrid.add(label_ReplyHelp, 1, 1);
		replyFormGrid.add(labelReplyBody, 0, 2);
		replyFormGrid.add(text_ReplyBody, 1, 2);

		HBox replyActions = new HBox(10);
		setupButton(button_CreateReply, 150, (_) -> ControllerDiscussionBoard.createReply());
		setupButton(button_ShowRepliesForPost, 150, (_) -> ControllerDiscussionBoard.listRepliesForPost());
		replyActions.getChildren().addAll(button_CreateReply, button_ShowRepliesForPost);

		VBox leftColumn = new VBox(12);
		leftColumn.getChildren().addAll(
			headerBox,
			new Separator(),
			postFormGrid,
			postActions,
			searchRow,
			new Separator(),
			replyFormGrid,
			replyActions
		);
		leftColumn.setPadding(new Insets(0, 12, 0, 0));

		setupArea(text_PostOutput, 0, "");
		text_PostOutput.setEditable(false);

		Label outputLabel = new Label("Posts and Replies");
		setupLabel(outputLabel, 14, Pos.CENTER_LEFT);

		VBox outputColumn = new VBox(8);
		outputColumn.getChildren().addAll(outputLabel, text_PostOutput);
		VBox.setVgrow(text_PostOutput, Priority.ALWAYS);

		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		setupButton(button_Return, 120, (_) -> ControllerDiscussionBoard.performReturn());

		HBox footer = new HBox(10);
		footer.setAlignment(Pos.CENTER_LEFT);
		footer.getChildren().addAll(spacer, button_Return);

		theRootPane.setLeft(leftColumn);
		theRootPane.setCenter(outputColumn);
		theRootPane.setBottom(footer);
		BorderPane.setMargin(outputColumn, new Insets(0, 0, 0, 8));
		BorderPane.setMargin(footer, new Insets(12, 0, 0, 0));

		text_PostTitle.setMaxWidth(Double.MAX_VALUE);
		text_UpdateTitle.setMaxWidth(Double.MAX_VALUE);
		text_SearchKeyword.setMaxWidth(Double.MAX_VALUE);
		text_ReplyPostTitle.setMaxWidth(Double.MAX_VALUE);
		text_PostBody.setMaxWidth(Double.MAX_VALUE);
		text_ReplyBody.setMaxWidth(Double.MAX_VALUE);
		text_PostOutput.setMaxWidth(Double.MAX_VALUE);
		text_PostOutput.setMaxHeight(Double.MAX_VALUE);

		alertError.setTitle("Discussion Error");
		alertError.setHeaderText(null);
	}

	/**
	 * Converts role flags to a user-facing role label.
	 *
	 * @param user active user
	 * @return Admin, Staff, or Student
	 */
	private static String getRoleLabel(User user) {
		if (user.getAdminRole()) {
			return "Admin";
		}
		if (user.getStaffRole()) {
			return "Staff";
		}
		return "Student";
	}

	/**
	 * Shows an error/information dialog with the provided message.
	 *
	 * @param message message to display
	 */
	protected static void showError(String message) {
		alertError.setContentText(message);
		alertError.showAndWait();
	}

	/**
	 * Helper that applies common label formatting and placement.
	 */
	private void setupLabel(Label label, double fontSize, Pos alignment) {
		label.setFont(Font.font("Arial", fontSize));
		label.setAlignment(alignment);
	}

	/**
	 * Helper that applies common text field formatting and placement.
	 */
	private void setupField(TextField field, String prompt) {
		field.setMinWidth(200);
		field.setPromptText(prompt);
	}

	/**
	 * Helper that applies common text area formatting and placement.
	 */
	private void setupArea(TextArea area, double minHeight, String prompt) {
		area.setMinHeight(minHeight);
		area.setPrefHeight(minHeight);
		area.setPromptText(prompt);
		area.setWrapText(true);
	}

	/**
	 * Helper that applies common button formatting, placement, and action handler.
	 */
	private void setupButton(Button button, double width,
				javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
		button.setMinWidth(width);
		button.setOnAction(handler);
	}
}
