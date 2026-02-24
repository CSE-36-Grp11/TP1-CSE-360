package guiUserLogin;

import applicationMain.ValidationUtil;
import database.Database;
import entityClasses.User;
import javafx.stage.Stage;

/*******
 * <p> Title: ControllerUserLogin Class. </p>
 * 
 * <p> Description: The Java/FX-based User Login Page.  This class provides the controller
 * actions basic on the user's use of the JavaFX GUI widgets defined by the View class.
 * 
 * This controller determines if the log in is valid.  If so set up the link to the database, 
 * determines how many roles this user is authorized to play, and the calls one the of the array of
 * role home pages if there is only one role.  If there are more than one role, it setup up and
 * calls the multiple roles dispatch page for the user to determine which role the user wants to
 * play.
 * 
 * The class has been written assuming that the View or the Model are the only class methods that
 * can invoke these methods.  This is why each has been declared at "protected".  Do not change any
 * of these methods to public.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-08-17 Initial version
 * @version 1.01		2025-09-16 Update Javadoc documentation *  
 */

public class ControllerUserLogin {
	
	/*-********************************************************************************************

	The User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	*/

	/**
	 * Default constructor is not used.
	 */
	public ControllerUserLogin() {
	}

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	private static Stage theStage;	
	
	/**********
	 * <p> Method: public doLogin() </p>
	 * 
	 * <p> Description: This method is called when the user has clicked on the Login button. This
	 * method checks the username and password to see if they are valid.  If so, it then logs that
	 * user in my determining which role to use.
	 * 
	 * The method reaches batch to the view page and to fetch the information needed rather than
	 * passing that information as parameters.
	 * 
	 */	
	protected static void doLogin(Stage ts) {
		theStage = ts;
		String username = ViewUserLogin.text_Username.getText().trim();
		String password = ViewUserLogin.text_Password.getText();
    	
    	// Validate username and password format
    	String usernameError = ValidationUtil.validateAsuUserId(username);
    	if (usernameError != null) {
    		ViewUserLogin.alertUsernamePasswordError.setContentText(usernameError);
    		ViewUserLogin.alertUsernamePasswordError.showAndWait();
    		return;
    	}
    	
    	String passwordError = ValidationUtil.validatePassword(password);
    	if (passwordError != null) {
    		ViewUserLogin.alertUsernamePasswordError.setContentText(passwordError);
    		ViewUserLogin.alertUsernamePasswordError.showAndWait();
    		return;
    	}
    	
		// Check if user exists in database
     	if (!theDatabase.getUserAccountDetails(username)) {
    		ViewUserLogin.alertUsernamePasswordError.setContentText("Incorrect username/password. Try again!");
    		ViewUserLogin.alertUsernamePasswordError.showAndWait();
    		return;
    	}
		
		// Check if password matches
    	String actualPassword = theDatabase.getCurrentPassword();
    	if (!password.equals(actualPassword)) {
    		ViewUserLogin.alertUsernamePasswordError.setContentText("Incorrect username/password. Try again!");
    		ViewUserLogin.alertUsernamePasswordError.showAndWait();
    		return;
    	}
		
		// Create user object with data from database
    	User user = new User(username, password, theDatabase.getCurrentFirstName(), 
    			theDatabase.getCurrentMiddleName(), theDatabase.getCurrentLastName(), 
    			theDatabase.getCurrentPreferredFirstName(), theDatabase.getCurrentEmailAddress(), 
    			theDatabase.getCurrentAdminRole(), 
    			theDatabase.getCurrentStudentRole(), theDatabase.getCurrentStaffRole());
    	
    	// Check how many roles this user has
		int numberOfRoles = theDatabase.getNumberOfRoles(user);
		
		// If user has one role, go to that role's home page
		if (numberOfRoles == 1) {
			if (user.getAdminRole()) {
				theDatabase.loginAdmin(user);
				guiAdminHome.ViewAdminHome.displayAdminHome(theStage, user);
			} else if (user.getStudentRole()) {
				theDatabase.loginStudent(user);
				guiStudentHome.ViewStudentHome.displayStudentHome(theStage, user);
			} else if (user.getStaffRole()) {
				theDatabase.loginStaff(user);
				guiStaffHome.ViewStaffHome.displayStaffHome(theStage, user);
			}
		} else if (numberOfRoles > 1) {
			guiMultipleRoleDispatch.ViewMultipleRoleDispatch.displayMultipleRoleDispatch(theStage, user);
		}
	}

	/**********
	 * <p> Method: public doInvitationCode() </p>
	 * 
	 * <p> Description: This method is called when the user enters an invitation code and
	 * clicks the button. It validates the code and redirects to the email registration page
	 * with the invited email prefilled.</p>
	 * 
	 */
	protected static void doInvitationCode(Stage ts) {
		theStage = ts;
		String code = ViewUserLogin.text_InvitationCode.getText().trim();
		if (code.isEmpty()) {
			ViewUserLogin.alertInvitationError.setContentText("Invitation code is required.");
			ViewUserLogin.alertInvitationError.showAndWait();
			return;
		}
		String email = theDatabase.getEmailAddressUsingCode(code);
		if (email == null || email.isEmpty()) {
			ViewUserLogin.alertInvitationError.setContentText("Invalid invitation code. Try again.");
			ViewUserLogin.alertInvitationError.showAndWait();
			return;
		}
		String role = theDatabase.getRoleGivenAnInvitationCode(code);
		guiEmailRegister.ViewEmailRegister.displayEmailRegister(theStage, email, code, role);
	}
	
	/**********
	 * <p> Method: public doCreateNewAccount() </p>
	 * 
	 * <p> Description: This method is called when the user clicks the "Create New Account" button
	 * without an invitation code. It redirects to the email registration page where users can
	 * create accounts without needing an invitation code.</p>
	 * 
	 */
	protected static void doCreateNewAccount(Stage ts) {
		theStage = ts;
		guiEmailRegister.ViewEmailRegister.displayEmailRegister(theStage);
	}

	
	/**********
	 * <p> Method: public performQuit() </p>
	 * 
	 * <p> Description: This method is called when the user has clicked on the Quit button.  Doing
	 * this terminates the execution of the application.</p>
	 * 
	 */	
	protected static void performQuit() {
		System.exit(0);
	}	

}
