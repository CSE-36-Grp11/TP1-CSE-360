package guiEmailLogin;

import javafx.stage.Stage;
import database.Database;
import entityClasses.User;
import guiUserLogin.ViewUserLogin;
import guiMultipleRoleDispatch.ViewMultipleRoleDispatch;

/*******
 * <p> Title: ControllerEmailLogin Class. </p>
 * 
 * <p> Description: The Controller for the Email Login page.</p>
 * 
 * <p> Copyright: Modified 2026 </p>
 * 
 * @version 1.00		2026-02-09 Initial version
 *  
 */

public class ControllerEmailLogin {
	
	// Reference for the in-memory database
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	
	/**********
	 * <p> Method: doEmailLogin(Stage theStage) </p>
	 * 
	 * <p> Description: This method validates the email and password, and if valid, logs the user in.</p>
	 * 
	 * @param theStage specifies the JavaFX Stage
	 * 
	 */
	public static void doEmailLogin(Stage theStage) {
		
		// Get the entered values
		String email = ViewEmailLogin.text_Email.getText().trim();
		String password = ViewEmailLogin.text_Password.getText();
		
		// Validate email address
		if (email.isEmpty() || !email.contains("@")) {
			ViewEmailLogin.alertLoginError.setContentText("Please enter a valid email address.");
			ViewEmailLogin.alertLoginError.showAndWait();
			return;
		}
		
		// Validate password
		if (password.isEmpty()) {
			ViewEmailLogin.alertLoginError.setContentText("Please enter your password.");
			ViewEmailLogin.alertLoginError.showAndWait();
			return;
		}
		
		// Look up user by email address
		// First, we need to get the username associated with this email
		String username = theDatabase.getUsernameByEmail(email);
		
		if (username == null || username.isEmpty()) {
			ViewEmailLogin.alertLoginError.setContentText("Invalid email or password.");
			ViewEmailLogin.alertLoginError.showAndWait();
			return;
		}
		
		// Fetch the user account details
		if (!theDatabase.getUserAccountDetails(username)) {
			ViewEmailLogin.alertLoginError.setContentText("Invalid email or password.");
			ViewEmailLogin.alertLoginError.showAndWait();
			return;
		}
		
		// Check to see that the login password matches the account password
		String actualPassword = theDatabase.getCurrentPassword();
		
		if (!password.equals(actualPassword)) {
			ViewEmailLogin.alertLoginError.setContentText("Invalid email or password.");
			ViewEmailLogin.alertLoginError.showAndWait();
			return;
		}
		
		// Establish this user's details
		User user = new User(username, password, theDatabase.getCurrentFirstName(), 
				theDatabase.getCurrentMiddleName(), theDatabase.getCurrentLastName(), 
				theDatabase.getCurrentPreferredFirstName(), theDatabase.getCurrentEmailAddress(), 
				theDatabase.getCurrentAdminRole(), 
				theDatabase.getCurrentStudentRole(), theDatabase.getCurrentStaffRole());
		
		// See which home page dispatch to use
		int numberOfRoles = theDatabase.getNumberOfRoles(user);
		
		if (numberOfRoles == 1) {
			// Single Account Home Page - The user has no choice here
			
			// Admin role
			if (user.getAdminRole()) {
				theDatabase.loginAdmin(user);
				guiAdminHome.ViewAdminHome.displayAdminHome(theStage, user);
			} else if (user.getStudentRole()) {
				theDatabase.loginStudent(user);
				guiStudentHome.ViewStudentHome.displayStudentHome(theStage, user);
			} else if (user.getStaffRole()) {
				theDatabase.loginStaff(user);
				guiStaffHome.ViewStaffHome.displayStaffHome(theStage, user);
			} else {
				System.out.println("***** EmailLogin has an invalid role");
			}
		} else if (numberOfRoles > 1) {
			// Multiple Account Home Page - The user chooses which role to play
			ViewMultipleRoleDispatch.displayMultipleRoleDispatch(theStage, user);
		}
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
