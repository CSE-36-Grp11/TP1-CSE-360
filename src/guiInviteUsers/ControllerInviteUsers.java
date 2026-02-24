package guiInviteUsers;

import applicationMain.ValidationUtil;
import database.Database;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/*******
 * <p> Title: ControllerInviteUsers Class</p>
 * 
 * <p> Description: The Controller for the Invite Users page. This class handles
 * the business logic for generating invitation codes and sending them to users.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2026-02-09 Initial version
 *  
 */

public class ControllerInviteUsers {
	
	private static ModelInviteUsers theModel = new ModelInviteUsers();
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	
	/**
	 * Set the email address from the view
	 */
	public static void setEmailAddress() {
		theModel.setEmailAddress(ViewInviteUsers.text_EmailAddress.getText());
	}
	
	/**
	 * Set the selected role from the view
	 */
	public static void setSelectedRole() {
		theModel.setSelectedRole(ViewInviteUsers.comboBox_Role.getValue());
	}
	
	/**
	 * Generate invitation code
	 */
	public static void generateInvitationCode() {
		String email = theModel.getEmailAddress();
		if (email == null) {
			email = "";
		}
		email = email.trim();

		String role = theModel.getSelectedRole();
		if (role == null || role.trim().isEmpty()) {
			role = "student";
		}
		
		// Validate email
		String emailError = ValidationUtil.validateEmail(email);
		if (emailError != null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid Email");
			alert.setHeaderText("Email Validation Error");
			alert.setContentText(emailError);
			alert.showAndWait();
			return;
		}
		
		// Check if email already has an invitation or is already registered
		if (theDatabase.emailaddressHasBeenUsed(email)) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Email Already Used");
			alert.setHeaderText("Invitation Already Sent");
			alert.setContentText("An invitation has already been sent to this email address.");
			alert.showAndWait();
			return;
		}
		
		// Generate the invitation code
		String code = theDatabase.generateInvitationCode(email, role);
		theModel.setGeneratedCode(code);
		
		// Display the generated code to the admin
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Invitation Code Generated");
		alert.setHeaderText("Success");
		alert.setContentText("Invitation code generated successfully!\n\n" +
				"Email: " + email + "\n" +
				"Role: " + role + "\n" +
				"Code: " + code + "\n\n" +
				"Share this code with the user so they can create their account.");
		alert.showAndWait();
		
		// Clear the form
		ViewInviteUsers.text_EmailAddress.clear();
		ViewInviteUsers.text_EmailAddress.requestFocus();
	}
	
	/**
	 * Return to admin home
	 */
	public static void returnToAdminHome(Stage theStage) {
		guiAdminHome.ViewAdminHome.displayAdminHome(theStage, ViewInviteUsers.theUser);
	}
}
