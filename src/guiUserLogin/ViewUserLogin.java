package guiUserLogin;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;


/*******
 * <p> Title: GUIStartupPage Class. </p>
 * 
 * <p> Description: The Java/FX-based System Startup Page for user login.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-04-20 Initial version
 *  
 */

public class ViewUserLogin {

	/*-********************************************************************************************

	Attributes

	 *********************************************************************************************/

	// These are the application values required by the user interface

	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

	private static Label label_ApplicationTitle = new Label("Foundation Application Startup Page");

	// This set is for all subsequent starts of the system
	private static Label label_OperationalStartTitle = new Label("User Login");
	private static Label label_LogInInsrtuctions = new Label("Enter your user name and password and "+	
			"then click on the LogIn button");
	protected static Alert alertUsernamePasswordError = new Alert(AlertType.INFORMATION);


	//	private User user;
	protected static TextField text_Username = new TextField();
	protected static PasswordField text_Password = new PasswordField();
	private static Button button_Login = new Button("Log In");	

	// Invitation code for new users
	private static Label label_InvitationInstructions = new Label("New user? Enter invitation code to sign up");
	protected static TextField text_InvitationCode = new TextField();
	private static Button button_UseInvitationCode = new Button("Use Invitation Code");
	protected static Alert alertInvitationError = new Alert(AlertType.INFORMATION);
	
	// Create new account without invitation
	private static Button button_CreateNewAccount = new Button("Create New Account");

	private static Button button_Quit = new Button("Quit");

	private static Stage theStage;	
	private static Pane theRootPane;
	public static Scene theUserLoginScene = null;	


	private static ViewUserLogin theView = null;	//	private static guiUserLogin.ControllerUserLogin theController;


	/*-********************************************************************************************

	Constructor

	 *********************************************************************************************/

	public static void displayUserLogin(Stage ps) {
		
		// Establish the references to the GUI. There is no current user yet.
		theStage = ps;
		
		// If not yet established, populate the static aspects of the GUI
		if (theView == null) theView = new ViewUserLogin();
		
		// Populate the dynamic aspects of the GUI with the data from the user and the current
		// state of the system.		
		text_Username.setText("");		// Reset the username and password from the last use
		text_Password.setText("");
		text_InvitationCode.setText("");

		// Set the title for the window, display the page, and wait for the Admin to do something
		theStage.setTitle("CSE 360 Foundation Code: User Login Page");		
		theStage.setScene(theUserLoginScene);
		theStage.show();
	}

	/**********
	 * <p> Method: ViewUserLoginPage() </p>
	 * 
	 * <p> Description: This method is called when the application first starts. It must handle
	 * two cases: 1) when no user has been established and 2) when one or more users have been 
	 * established.
	 * 
	 * If there are no users in the database, this means that the person starting the system must
	 * be an administrator, so a special GUI is provided to allow this Admin to set a username and
	 * password.
	 * 
	 * If there is at least one user, then a display is shown for existing users to login.</p>
	 * 
	 * @param ps specifies the JavaFX Stage to be used for this GUI and it's methods
	 * 
	 * @param theRoot specifies the JavaFX Pane to be used for this GUI and it's methods
	 * 
	 * @param db specifies the Database to be used by this GUI and it's methods
	 * 
	 */
	private ViewUserLogin() {

		// Create the Pane for the list of widgets and the Scene for the window
		theRootPane = new Pane();
		theUserLoginScene = new Scene(theRootPane, width, height);
		
		// Populate the window with the title and other common widgets and set their static state
		setupLabelUI(label_ApplicationTitle, "Arial", 32, width, Pos.CENTER, 0, 10);

		setupLabelUI(label_OperationalStartTitle, "Arial", 24, width, Pos.CENTER, 0, 60);


		// Existing user log in portion of the page

		setupLabelUI(label_LogInInsrtuctions, "Arial", 18, width, Pos.BASELINE_LEFT, 20, 120);

		// Establish the text input operand field for the username
		setupTextUI(text_Username, "Arial", 18, 300, Pos.BASELINE_LEFT, 50, 160, true);
		text_Username.setPromptText("Enter Username");

		// Establish the text input operand field for the password
		setupTextUI(text_Password, "Arial", 18, 300, Pos.BASELINE_LEFT, 50, 210, true);
		text_Password.setPromptText("Enter Password");

		// Set up the Log In button
		setupButtonUI(button_Login, "Dialog", 18, 200, Pos.CENTER, 475, 180);
		button_Login.setOnAction((_) -> {ControllerUserLogin.doLogin(theStage); });

		alertUsernamePasswordError.setTitle("Invalid username/password!");
		alertUsernamePasswordError.setHeaderText(null);

		// Invitation code section
		setupLabelUI(label_InvitationInstructions, "Arial", 16, width, Pos.CENTER, 0, 260);
		setupTextUI(text_InvitationCode, "Arial", 18, 220, Pos.BASELINE_LEFT, 50, 300, true);
		text_InvitationCode.setPromptText("Enter invitation code");
		setupButtonUI(button_UseInvitationCode, "Dialog", 18, 220, Pos.CENTER, 320, 300);
		button_UseInvitationCode.setOnAction((_) -> {ControllerUserLogin.doInvitationCode(theStage); });
		alertInvitationError.setTitle("Invalid Invitation Code");
		alertInvitationError.setHeaderText(null);
		
		// Create new account button
		setupButtonUI(button_CreateNewAccount, "Dialog", 18, 220, Pos.CENTER, 320, 350);
		button_CreateNewAccount.setOnAction((_) -> {ControllerUserLogin.doCreateNewAccount(theStage); });

		// Set up the Quit button  
		setupButtonUI(button_Quit, "Dialog", 18, 250, Pos.CENTER, 300, 520);
		button_Quit.setOnAction((_) -> {ControllerUserLogin.performQuit(); });

		theRootPane.getChildren().addAll(
				label_ApplicationTitle, 
				label_OperationalStartTitle,
				label_LogInInsrtuctions, text_Username,
				button_Login, text_Password,
				label_InvitationInstructions, text_InvitationCode,
				button_UseInvitationCode,
				button_CreateNewAccount,
				button_Quit);
	}


	/*-********************************************************************************************

	Helper methods to reduce code length

	 *********************************************************************************************/

	/**********
	 * Private local method to initialize the standard fields for a label
	 */

	private void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y){
		l.setFont(Font.font(ff, f));
		l.setMinWidth(w);
		l.setAlignment(p);
		l.setLayoutX(x);
		l.setLayoutY(y);		
	}


	/**********
	 * Private local method to initialize the standard fields for a button
	 * 
	 * @param b		The Button object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the Button
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	private void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y){
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);		
	}

	/**********
	 * Private local method to initialize the standard fields for a text field
	 */
	private void setupTextUI(TextField t, String ff, double f, double w, Pos p, double x, double y, boolean e){
		t.setFont(Font.font(ff, f));
		t.setMinWidth(w);
		t.setMaxWidth(w);
		t.setAlignment(p);
		t.setLayoutX(x);
		t.setLayoutY(y);		
		t.setEditable(e);
	}		
}
