package guiInviteUsers;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Scene;

/*******
 * <p> Title: ViewInviteUsers Class</p>
 * 
 * <p> Description: The View for the Invite Users page. This class provides the GUI
 * for admins to generate invitation codes for new users.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2026-02-09 Initial version
 *  
 */

public class ViewInviteUsers {
	
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;
	
	// GUI widgets
	private static Label label_PageTitle = new Label("Invite New User");
	private static Label label_Instructions = new Label(
			"Enter the email address of the user and select their role, then click Generate Code");
	
	private static Label label_EmailAddress = new Label("Email Address:");
	protected static TextField text_EmailAddress = new TextField();
	
	private static Label label_Role = new Label("Select Role:");
	protected static ComboBox<String> comboBox_Role = new ComboBox<>();
	
	private static Button button_GenerateCode = new Button("Generate Invitation Code");
	private static Button button_Return = new Button("Return to Admin Home");
	
	// Scene and stage
	private static ViewInviteUsers theView;
	protected static Stage theStage;
	protected static entityClasses.User theUser;
	private static Pane theRootPane;
	private static Scene theInviteScene = null;
	
	/**
	 * Display the Invite Users page
	 * @param ps the JavaFX Stage
	 * @param user the current user
	 */
	public static void displayInviteUsers(Stage ps, entityClasses.User user) {
		theStage = ps;
		theUser = user;
		
		// Only create once if not already instantiated
		if (theView == null) {
			theView = new ViewInviteUsers();
		}
		
		// Set the title and display the scene
		theStage.setTitle("CSE 360 Foundation Code: Invite New User");
		theStage.setScene(theInviteScene);
		theStage.show();
	}
	
	/**
	 * Private constructor to initialize the view
	 */
	private ViewInviteUsers() {
		// Create the root pane and scene
		theRootPane = new Pane();
		theInviteScene = new Scene(theRootPane, width, height);
		
		// Set up the page title
		setupLabelUI(label_PageTitle, "Arial", 32, width, Pos.CENTER, 0, 20);
		
		// Set up instructions
		setupLabelUI(label_Instructions, "Arial", 14, width, Pos.CENTER, 0, 70);
		
		// Set up email address label and field
		setupLabelUI(label_EmailAddress, "Arial", 16, 150, Pos.BASELINE_RIGHT, 50, 130);
		setupTextUI(text_EmailAddress, "Arial", 16, 300, Pos.BASELINE_LEFT, 250, 130, true);
		text_EmailAddress.setPromptText("user@example.com");
		text_EmailAddress.textProperty().addListener((_, _, _) -> {
			ControllerInviteUsers.setEmailAddress();
		});
		
		// Set up role label and combobox
		setupLabelUI(label_Role, "Arial", 16, 150, Pos.BASELINE_RIGHT, 50, 180);
		comboBox_Role.setStyle("-fx-font-size: 16");
		comboBox_Role.getItems().addAll("student", "staff", "admin");
		comboBox_Role.setValue("student");
		comboBox_Role.setPrefWidth(300);
		comboBox_Role.setLayoutX(250);
		comboBox_Role.setLayoutY(165);
		comboBox_Role.setOnAction((_) -> {
			ControllerInviteUsers.setSelectedRole();
		});
		
		// Set up generate code button
		setupButtonUI(button_GenerateCode, "Dialog", 16, 220, Pos.CENTER, 300, 250);
		button_GenerateCode.setOnAction((_) -> {
			ControllerInviteUsers.generateInvitationCode();
		});
		
		// Set up return button
		setupButtonUI(button_Return, "Dialog", 16, 220, Pos.CENTER, 300, 450);
		button_Return.setOnAction((_) -> {
			ControllerInviteUsers.returnToAdminHome(theStage);
		});
		
		// Add all widgets to the pane
		theRootPane.getChildren().addAll(
				label_PageTitle,
				label_Instructions,
				label_EmailAddress,
				text_EmailAddress,
				label_Role,
				comboBox_Role,
				button_GenerateCode,
				button_Return
		);
	}
	
	// Helper methods for UI setup
	
	/**
	 * Set up label UI properties
	 */
	private void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y) {
		l.setFont(Font.font(ff, f));
		l.setMinWidth(w);
		l.setAlignment(p);
		l.setLayoutX(x);
		l.setLayoutY(y);
	}
	
	/**
	 * Set up button UI properties
	 */
	private void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y) {
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);
	}
	
	/**
	 * Set up text field UI properties
	 */
	private void setupTextUI(TextField t, String ff, double f, double w, Pos p, double x, double y, boolean e) {
		t.setFont(Font.font(ff, f));
		t.setMinWidth(w);
		t.setMaxWidth(w);
		t.setAlignment(p);
		t.setLayoutX(x);
		t.setLayoutY(y);
		t.setEditable(e);
	}
}
