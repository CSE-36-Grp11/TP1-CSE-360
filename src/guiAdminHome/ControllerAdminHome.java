package guiAdminHome;

import java.util.List;
import java.util.Optional;

import database.Database;
import entityClasses.User;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;

/*******
 * <p> Title: GUIAdminHomePage Class. </p>
 * 
 * <p> Description: The Java/FX-based Admin Home Page.  This class provides the controller actions
 * basic on the user's use of the JavaFX GUI widgets defined by the View class.
 * 
 * This page contains a number of buttons that have not yet been implemented.  WHen those buttons
 * are pressed, an alert pops up to tell the user that the function associated with the button has
 * not been implemented. Also, be aware that What has been implemented may not work the way the
 * final product requires and there maybe defects in this code.
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

public class ControllerAdminHome {
	
	/*-*******************************************************************************************

	User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	*/
	
	/**
	 * Default constructor is not used.
	 */
	public ControllerAdminHome() {
	}
	
	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	/**********
	 * <p> 
	 * 
	 * Title: setOnetimePassword () Method. </p>
	 * 
	 * <p> Description: Protected method that allows an admin to set a one-time password
	 * for a user. The admin can enter a password or allow the system to generate one. </p>
	 */
	protected static void setOnetimePassword () {
		TextInputDialog userDialog = new TextInputDialog("");
		userDialog.setTitle("Set One-Time Password");
		userDialog.setHeaderText("Enter the username to reset");
		Optional<String> userResult = userDialog.showAndWait();
		if (userResult.isEmpty()) return;
		String username = userResult.get().trim();
		if (username.length() < 1) return;

		if (!theDatabase.getUserAccountDetails(username)) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("User Not Found");
			alert.setHeaderText("No user found with that username");
			alert.setContentText("Please verify the username and try again.");
			alert.showAndWait();
			return;
		}

		TextInputDialog passDialog = new TextInputDialog("");
		passDialog.setTitle("Set One-Time Password");
		passDialog.setHeaderText("Enter a one-time password (leave blank to auto-generate)");
		Optional<String> passResult = passDialog.showAndWait();
		if (passResult.isEmpty()) return;
		String newPassword = passResult.get().trim();
		if (newPassword.length() < 1) newPassword = generateOneTimePassword();

		if (theDatabase.updatePassword(username, newPassword)) {
			if (ViewAdminHome.theUser != null && username.equals(ViewAdminHome.theUser.getUserName())) {
				ViewAdminHome.theUser.setPassword(newPassword);
			}
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("One-Time Password Set");
			alert.setHeaderText("Password reset completed");
			alert.setContentText("Temporary password for " + username + ": " + newPassword +
					"\nAsk the user to change it after login.");
			alert.showAndWait();
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Password Update Failed");
			alert.setHeaderText("Unable to update the password");
			alert.setContentText("Please try again or verify the database connection.");
			alert.showAndWait();
		}
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: deleteUser () Method. </p>
	 * 
	 * <p> Description: Protected method that allows an admin to delete a user account. </p>
	 */
	protected static void deleteUser() {
		TextInputDialog userDialog = new TextInputDialog("");
		userDialog.setTitle("Delete User");
		userDialog.setHeaderText("Enter the username to delete");
		Optional<String> userResult = userDialog.showAndWait();
		if (userResult.isEmpty()) return;
		String username = userResult.get().trim();
		if (username.length() < 1) return;

		if (ViewAdminHome.theUser != null && username.equals(ViewAdminHome.theUser.getUserName())) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Delete User");
			alert.setHeaderText("Cannot delete the active user");
			alert.setContentText("Log out and use another admin account to delete this user.");
			alert.showAndWait();
			return;
		}

		Alert confirm = new Alert(AlertType.CONFIRMATION);
		confirm.setTitle("Confirm Delete");
		confirm.setHeaderText("Delete user: " + username + "?");
		confirm.setContentText("This action cannot be undone.");
		Optional<ButtonType> choice = confirm.showAndWait();
		if (choice.isEmpty() || choice.get() != ButtonType.OK) return;

		if (theDatabase.deleteUser(username)) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Delete User");
			alert.setHeaderText("User deleted");
			alert.setContentText("The user account was removed successfully.");
			alert.showAndWait();
			ViewAdminHome.label_NumberOfUsers.setText("Number of users: " +
					theDatabase.getNumberOfUsers());
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Delete User");
			alert.setHeaderText("Delete failed");
			alert.setContentText("No matching user found or database error occurred.");
			alert.showAndWait();
		}
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: listUsers () Method. </p>
	 * 
	 * <p> Description: Protected method that shows a list of all users. </p>
	 */
	protected static void listUsers() {
		List<User> users = theDatabase.getAllUsers();
		StringBuilder sb = new StringBuilder();
		if (users.isEmpty()) {
			sb.append("No users found in the system.");
		} else {
			for (User user : users) {
				sb.append(user.getUserName());
				sb.append("  [Roles: ");
				sb.append(formatRoles(user));
				sb.append("]");
				String email = user.getEmailAddress();
				if (email != null && email.length() > 0) {
					sb.append("  Email: ").append(email);
				}
				sb.append("\n");
			}
		}

		TextArea textArea = new TextArea(sb.toString());
		textArea.setEditable(false);
		textArea.setWrapText(true);
		textArea.setPrefWidth(600);
		textArea.setPrefHeight(300);

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("User List");
		alert.setHeaderText("All users in the system");
		alert.getDialogPane().setContent(textArea);
		alert.showAndWait();
	}

	/**********
	 * Helper to format user roles for display.
	 */
	private static String formatRoles(User user) {
		StringBuilder roles = new StringBuilder();
		if (user.getAdminRole()) roles.append("Admin, ");
		if (user.getStudentRole()) roles.append("Student, ");
		if (user.getStaffRole()) roles.append("Staff, ");
		if (roles.length() == 0) return "None";
		return roles.substring(0, roles.length() - 2);
	}

	/**********
	 * Helper to generate a one-time password.
	 */
	private static String generateOneTimePassword() {
		String uuid = java.util.UUID.randomUUID().toString().replace("-", "");
		return uuid.substring(0, 10);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: addRemoveRoles () Method. </p>
	 * 
	 * <p> Description: Protected method that allows an admin to add and remove roles for any of
	 * the users currently in the system.  This is done by invoking the AddRemoveRoles Page. There
	 * is no need to specify the home page for the return as this can only be initiated by and
	 * Admin.</p>
	 */
	protected static void addRemoveRoles() {
		guiAddRemoveRoles.ViewAddRemoveRoles.displayAddRemoveRoles(ViewAdminHome.theStage, 
				ViewAdminHome.theUser);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: performLogout () Method. </p>
	 * 
	 * <p> Description: Protected method that logs this user out of the system and returns to the
	 * login page for future use.</p>
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewAdminHome.theStage);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: performQuit () Method. </p>
	 * 
	 * <p> Description: Protected method that gracefully terminates the execution of the program.
	 * </p>
	 */
	protected static void performQuit() {
		System.exit(0);
	}
}
