package guiEmailRegister;

import javafx.stage.Stage;
import applicationMain.ValidationUtil;
import database.Database;
import entityClasses.User;
import guiUserLogin.ViewUserLogin;
import guiMultipleRoleDispatch.ViewMultipleRoleDispatch;

/*******
 * <p> Title: ControllerEmailRegister Class. </p>
 * 
 * <p> Description: The Controller for the Email Registration page.</p>
 * 
 * <p> Copyright: Modified 2026 </p>
 * 
 * @version 1.00		2026-02-08 Initial version
 *  
 */

public class ControllerEmailRegister {
	
	// Reference for the in-memory database
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	
	/**********
	 * <p> Method: doCreateAccount(Stage theStage) </p>
	 * 
	 * <p> Description: This method validates the email registration form and creates a new account.</p>
	 * 
	 * @param theStage specifies the JavaFX Stage
	 * 
	 */
	public static void doCreateAccount(Stage theStage) {
		
		// Get the entered values
		String email = ViewEmailRegister.text_Email.getText().trim();
		String username = ViewEmailRegister.text_Username.getText().trim();
		String password1 = ViewEmailRegister.text_Password1.getText();
		String password2 = ViewEmailRegister.text_Password2.getText();
		
		// Validate email address
		String emailError = ValidationUtil.validateEmail(email);
		if (emailError != null) {
			ViewEmailRegister.alertEmailError.setContentText(emailError);
			ViewEmailRegister.alertEmailError.showAndWait();
			return;
		}
		
		// Validate username
		String usernameError = ValidationUtil.validateAsuUserId(username);
		if (usernameError != null) {
			ViewEmailRegister.alertEmailError.setContentText(usernameError);
			ViewEmailRegister.alertEmailError.showAndWait();
			return;
		}
		
		// Check if username already exists
		if (theDatabase.doesUserExist(username)) {
			ViewEmailRegister.alertEmailError.setContentText("Username already exists. Please choose a different username.");
			ViewEmailRegister.alertEmailError.showAndWait();
			return;
		}
		
		// Validate passwords
		String passwordError = ValidationUtil.validatePassword(password1);
		if (passwordError != null) {
			ViewEmailRegister.alertPasswordError.setContentText(passwordError);
			ViewEmailRegister.alertPasswordError.showAndWait();
			return;
		}
		if (password2 == null || password2.isEmpty()) {
			ViewEmailRegister.alertPasswordError.setContentText("Password is required.");
			ViewEmailRegister.alertPasswordError.showAndWait();
			return;
		}

		if (!password1.equals(password2)) {
			ViewEmailRegister.alertPasswordError.setContentText("Passwords do not match. Please try again.");
			ViewEmailRegister.alertPasswordError.showAndWait();
			return;
		}
		
		// Create the new user account with default role (Role1)
		// Constructor: User(username, password, firstName, middleName, lastName, preferredFirstName, 
		//              emailAddress, adminRole, role1, role2)
		User newUser = new User(username, password1, "", "", "", "", email, false, true, false);
		
		// Add the user to the database
		try {
			theDatabase.register(newUser);
		} catch (Exception e) {
			ViewEmailRegister.alertEmailError.setContentText("Error creating account: " + e.getMessage());
			ViewEmailRegister.alertEmailError.showAndWait();
			return;
		}
		
		// Show success message
		ViewEmailRegister.alertAccountCreated.showAndWait();
		
		// Log the user in and dispatch to appropriate home page
		ViewMultipleRoleDispatch.displayMultipleRoleDispatch(theStage, newUser);
	}
	
	/**********
	 * <p> Method: performCancel(Stage theStage) </p>
	 * 
	 * <p> Description: This method returns to the login page.</p>
	 * 
	 * @param theStage specifies the JavaFX Stage
	 * 
	 */
	public static void performCancel(Stage theStage) {
		ViewUserLogin.displayUserLogin(theStage);
	}
}
