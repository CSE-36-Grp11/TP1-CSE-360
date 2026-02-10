package guiEmailRegister;

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
 * <p> Title: ViewEmailRegister Class. </p>
 * 
 * <p> Description: The ViewEmailRegister Page allows users to sign in with their 
 * email address and create an account with email and password.</p>
 * 
 * <p> Copyright: Modified 2026 </p>
 * 
 * @version 1.00		2026-02-08 Initial version
 *  
 */

public class ViewEmailRegister {
	
	/*-********************************************************************************************

	Attributes
	
	*/
	
	// These are the application values required by the user interface
	
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;
	
	private static Label label_ApplicationTitle = 
			new Label("Foundation Application Email Registration");
    protected static Label label_EmailRegistration = new Label("Email Account Registration");
    protected static Label label_Instructions = new Label("Please enter your email address and create a password.");
    
    protected static Label label_Email = new Label("Email Address:");
    protected static TextField text_Email = new TextField();
    
    protected static Label label_Password = new Label("Password:");
    protected static PasswordField text_Password1 = new PasswordField();
    
    protected static Label label_ConfirmPassword = new Label("Confirm Password:");
    protected static PasswordField text_Password2 = new PasswordField();
    
    protected static Label label_Username = new Label("Username:");
    protected static TextField text_Username = new TextField();
    
    protected static Button button_CreateAccount = new Button("Create Account");

	// Alerts for various error conditions
    protected static Alert alertEmailError = new Alert(AlertType.ERROR);
	protected static Alert alertPasswordError = new Alert(AlertType.ERROR);
	protected static Alert alertAccountCreated = new Alert(AlertType.INFORMATION);

    protected static Button button_Cancel = new Button("Cancel");

	// These attributes are used to configure the page and populate it with this user's information
	private static ViewEmailRegister theView;		// Is instantiation of the class needed?

	protected static Stage theStage;			// The Stage that JavaFX has established for us
	private static Pane theRootPane;			// The Pane that holds all the GUI widgets 
	public static Scene theEmailRegisterScene = null;	
	

	/*-********************************************************************************************

	Constructors
	
	*/

	/**********
	 * <p> Method: displayEmailRegister(Stage ps) </p>
	 * 
	 * <p> Description: This method is the single entry point from outside this package to cause
	 * the EmailRegister page to be displayed.</p>
	 * 
	 * @param ps specifies the JavaFX Stage to be used for this GUI and it's methods
	 * 
	 */
	public static void displayEmailRegister(Stage ps) {
		
		// Establish the references to the GUI
		theStage = ps;
		
		if (theView == null) theView = new ViewEmailRegister();
		
		text_Email.setText("");			// Clear the input fields
		text_Username.setText("");
		text_Password1.setText("");
		text_Password2.setText("");
		
    	// Place all of the established GUI elements into the pane
    	theRootPane.getChildren().clear();
    	theRootPane.getChildren().addAll(
    			label_ApplicationTitle,
    			label_EmailRegistration, 
    			label_Instructions, 
    			label_Email, text_Email,
    			label_Username, text_Username,
    			label_Password, text_Password1,
    			label_ConfirmPassword, text_Password2, 
    			button_CreateAccount, 
    			button_Cancel);    	

		// Set the title for the window, display the page
		theStage.setTitle("CSE 360 Foundation Code: Email Registration");	
        theStage.setScene(theEmailRegisterScene);
		theStage.show();
	}
	
	/**********
	 * <p> Constructor: ViewEmailRegister() </p>
	 * 
	 * <p> Description: This constructor is called just once, the first time email registration 
	 * needs to be performed.</p>
	 * 
	 */
	private ViewEmailRegister() {

		// Create the Pane for the list of widgets and the Scene for the window
		theRootPane = new Pane();
		theEmailRegisterScene = new Scene(theRootPane, width, height);
		
		// Populate the window with the title and other widgets
		setupLabelUI(label_ApplicationTitle, "Arial", 28, width, Pos.CENTER, 0, 10);

		setupLabelUI(label_EmailRegistration, "Arial", 24, width, Pos.CENTER, 0, 60);
		
		setupLabelUI(label_Instructions, "Arial", 16, width, Pos.CENTER, 0, 100);

		// Email field
		setupLabelUI(label_Email, "Arial", 18, 150, Pos.BASELINE_RIGHT, 200, 150);
		setupTextUI(text_Email, "Arial", 18, 300, Pos.BASELINE_LEFT, 360, 145, true);
		text_Email.setPromptText("Enter your email");

		// Username field
		setupLabelUI(label_Username, "Arial", 18, 150, Pos.BASELINE_RIGHT, 200, 200);
		setupTextUI(text_Username, "Arial", 18, 300, Pos.BASELINE_LEFT, 360, 195, true);
		text_Username.setPromptText("Choose a username");

		// Password field
		setupLabelUI(label_Password, "Arial", 18, 150, Pos.BASELINE_RIGHT, 200, 250);
		setupTextUI(text_Password1, "Arial", 18, 300, Pos.BASELINE_LEFT, 360, 245, true);
		text_Password1.setPromptText("Enter password");

		// Confirm password field
		setupLabelUI(label_ConfirmPassword, "Arial", 18, 150, Pos.BASELINE_RIGHT, 200, 300);
		setupTextUI(text_Password2, "Arial", 18, 300, Pos.BASELINE_LEFT, 360, 295, true);
		text_Password2.setPromptText("Confirm password");

		// Set up the Create Account button
		setupButtonUI(button_CreateAccount, "Dialog", 18, 200, Pos.CENTER, 350, 360);
		button_CreateAccount.setStyle("-fx-background-color: #4285F4; -fx-text-fill: white;");
		button_CreateAccount.setOnAction((_) -> {ControllerEmailRegister.doCreateAccount(theStage); });

		// Set up alerts
		alertEmailError.setTitle("Invalid Email");
		alertEmailError.setHeaderText(null);
		
		alertPasswordError.setTitle("Password Error");
		alertPasswordError.setHeaderText(null);
		
		alertAccountCreated.setTitle("Account Created");
		alertAccountCreated.setHeaderText("Your account has been created successfully!");

		// Set up the Cancel button  
		setupButtonUI(button_Cancel, "Dialog", 18, 200, Pos.CENTER, 350, 420);
		button_Cancel.setOnAction((_) -> {ControllerEmailRegister.performCancel(theStage); });
	}

	/*-*******************************************************************************************

	Helper methods used to minimize the number of lines of code needed
	
	*/

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
	 */
	private void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y){
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);		
	}

	/**********
	 * Private local method to initialize the standard fields for a text input field
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
