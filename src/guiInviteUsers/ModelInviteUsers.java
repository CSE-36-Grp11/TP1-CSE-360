package guiInviteUsers;

/*******
 * <p> Title: ModelInviteUsers Class</p>
 * 
 * <p> Description: The Model for the Invite Users page. This class manages the data
 * and business logic for generating invitation codes.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2026-02-09 Initial version
 *  
 */

public class ModelInviteUsers {
	
	private String emailAddress;
	private String selectedRole;
	private String generatedCode;
	
	/**
	 * Default constructor
	 */
	public ModelInviteUsers() {
		this.emailAddress = "";
		this.selectedRole = "student";
		this.generatedCode = "";
	}
	
	/**
	 * Get the email address
	 * @return email address string
	 */
	public String getEmailAddress() {
		return emailAddress;
	}
	
	/**
	 * Set the email address
	 * @param email the email address to set
	 */
	public void setEmailAddress(String email) {
		this.emailAddress = email;
	}
	
	/**
	 * Get the selected role
	 * @return role string
	 */
	public String getSelectedRole() {
		return selectedRole;
	}
	
	/**
	 * Set the selected role
	 * @param role the role to set
	 */
	public void setSelectedRole(String role) {
		this.selectedRole = role;
	}
	
	/**
	 * Get the generated code
	 * @return generated code string
	 */
	public String getGeneratedCode() {
		return generatedCode;
	}
	
	/**
	 * Set the generated code
	 * @param code the code to set
	 */
	public void setGeneratedCode(String code) {
		this.generatedCode = code;
	}
}
