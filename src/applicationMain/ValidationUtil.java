package applicationMain;

public class ValidationUtil {

	private ValidationUtil() {
	}

	public static String validateAsuUserId(String value) {
		if (value == null || value.trim().isEmpty()) {
			return "ASU User ID is required.";
		}
		String trimmed = value.trim();
		if (!trimmed.matches("[A-Za-z0-9]+")) {
			return "ASU User ID can only contain letters and numbers.";
		}
		if (trimmed.length() < 4 || trimmed.length() > 20) {
			return "ASU User ID must be 4–20 characters.";
		}
		return null;
	}

	public static String validatePassword(String value) {
		if (value == null || value.isEmpty()) {
			return "Password is required.";
		}
		if (value.contains(" ")) {
			return "Password cannot contain spaces.";
		}
		if (value.length() < 8) {
			return "Password must be at least 8 characters.";
		}
		return null;
	}

	public static String validateEmail(String value) {
		if (value == null || value.trim().isEmpty()) {
			return "Email is required.";
		}
		String trimmed = value.trim();
		if (trimmed.contains(" ")) {
			return "Please enter a valid email address with one @.";
		}
		int atIndex = trimmed.indexOf('@');
		if (atIndex < 1 || atIndex != trimmed.lastIndexOf('@') || atIndex == trimmed.length() - 1) {
			return "Please enter a valid email address with one @.";
		}
		return null;
	}

	public static String validatePhoneNumber(String value) {
		if (value == null || value.trim().isEmpty()) {
			return "Phone number must be a valid 10-digit number.";
		}
		String trimmed = value.trim();
		if (!trimmed.matches("\\d+")) {
			return "Phone number can only contain numbers.";
		}
		if (trimmed.length() != 10) {
			return "Phone number must be a valid 10-digit number.";
		}
		return null;
	}

	public static String validatePersonName(String value) {
		if (value == null || value.trim().isEmpty()) {
			return "Name is required.";
		}
		String trimmed = value.trim();
		if (trimmed.length() < 1 || trimmed.length() > 50) {
			return "Name is required.";
		}
		if (!trimmed.matches("[A-Za-z\\s’'-]+")) {
			return "Name can only contain letters and basic punctuation.";
		}
		return null;
	}
}