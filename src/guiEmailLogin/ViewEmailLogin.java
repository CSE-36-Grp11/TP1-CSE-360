package guiEmailLogin;

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
 * <p> Title: ViewEmailLogin Class. </p>
 * 
 * <p> Description: The ViewEmailLogin Page allows users to authenticate with their 
 * email address and password that they created during email registration.</p>
 * 
 * <p> Copyright: Modified 2026 </p>
 * 
 * @version 1.00		2026-02-09 Initial version
 *  
 */

public class ViewEmailLogin {
	
	/*-********************************************************************************************

	Attributes
	
	*/
	
	// These are the application values required by the user interface
	
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;
	
	private static Label label_ApplicationTitle = 
			new Label("Foundation Application Email Login");
    protected static Label label_EmailLogin = new Label("Email Account Login");
    protected static Label label_Instructions = new Label("Enter your email address and password.");
    
    protected static Label label_Email = new Label("Email Address:");
    protected static TextField text_Email = new TextField();
    
    protected static Label label_Password = new Label("Password:");
    protected static PasswordField text_Password = new PasswordField();
    
    protected static Button button_Login = new Button("Login");

	// Alerts for error conditions
    protected static Alert alertLoginError = new Alert(AlertType.ERROR);

    protected static Button button_Cancel = new Button("Cancel");

	// These attributes are used to configure the page and populate it with this user's information
	private static ViewEmailLogin theView;		// Is instantiation of the class needed?

	protected static Stage theStage;			// The Stage that JavaFX has established for us
	private static Pane theRootPane;			// The Pane that holds all the GUI widgets 
	public static Scene theEmailLoginScene = null;	
	

	/*-********************************************************************************************

	Constructors
	
	*/

	/**********
	 * <p> Method: displayEmailLogin(Stage ps) </p>
	 * 
	 * <p> Description: This method is the single entry point from outside this package to cause
	 * the EmailLogin page to be displayed.</p>
	 * 
	 * @param ps specifies the JavaFX Stage to be used for this GUI and it's methods
	 * 
	 */
	public static void displayEmailLogin(Stage ps) {
		
		// Establish the references to the GUI
		theStage = ps;
		
		if (theView == null) theView = new ViewEmailLogin();
		
		text_Email.setText("");			// Clear the input fields
		text_Password.setText("");
		
    	// Place all of the established GUI elements into the pane
    	theRootPane.getChildren().clear();
    	theRootPane.getChildren().addAll(
    			label_ApplicationTitle,
    			label_EmailLogin, 
    			label_Instructions, 
    			label_Email, text_Email,
    			label_Password, text_Password, 
    			button_Login, 
    			button_Cancel);    	

		// Set the title for the window, display the page
		theStage.setTitle("CSE 360 Foundation Code: Email Login");	
        theStage.setScene(theEmailLoginScene);
		theStage.show();
	}
	
	/**********
	 * <p> Constructor: ViewEmailLogin() </p>
	 * 
	 * <p> Description: This constructor is called just once, the first time email login 
	 * needs to be performed.</p>
	 * 
	 */
	private ViewEmailLogin() {

		// Create the Pane for the list of widgets and the Scene for the window
		theRootPane = new Pane();
		theEmailLoginScene = new Scene(theRootPane, width, height);
		
		// Populate the window with the title and other widgets
		setupLabelUI(label_ApplicationTitle, "Arial", 28, width, Pos.CENTER, 0, 10);

		setupLabelUI(label_EmailLogin, "Arial", 24, width, Pos.CENTER, 0, 60);
		
		setupLabelUI(label_Instructions, "Arial", 16, width, Pos.CENTER, 0, 100);

		// Email field
		setupLabelUI(label_Email, "Arial", 18, 150, Pos.BASELINE_RIGHT, 200, 160);
		setupTextUI(text_Email, "Arial", 18, 300, Pos.BASELINE_LEFT, 360, 155, true);
		text_Email.setPromptText("Enter your email");

		// Password field
		setupLabelUI(label_Password, "Arial", 18, 150, Pos.BASELINE_RIGHT, 200, 220);
		setupTextUI(text_Password, "Arial", 18, 300, Pos.BASELINE_LEFT, 360, 215, true);
		text_Password.setPromptText("Enter your password");

		// Set up the Login button
		setupButtonUI(button_Login, "Dialog", 18, 200, Pos.CENTER, 350, 300);
		button_Login.setStyle("-fx-background-color: #4285F4; -fx-text-fill: white;");
		button_Login.setOnAction((_) -> {ControllerEmailLogin.doEmailLogin(theStage); });

		// Set up alerts
		alertLoginError.setTitle("Login Error");
		alertLoginError.setHeaderText(null);

		// Set up the Cancel button  
		setupButtonUI(button_Cancel, "Dialog", 18, 200, Pos.CENTER, 350, 360);
		button_Cancel.setOnAction((_) -> {ControllerEmailLogin.performCancel(theStage); });
	}

	/*-***********************************************************************************

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
