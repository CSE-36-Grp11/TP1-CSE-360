package applicationMain;

public class ValidationUtil {

	private ValidationUtil() {
	}

	public static String validateAsuUserId(String value) {
		if (value == null || value.isEmpty()) {
			return "ASU User ID is required.";
		}
		if (value.contains(" ")) {
			return "ASU User ID cannot contain spaces.";
		}
		if (!value.matches("[A-Za-z0-9]+")) {
			return "ASU User ID can only contain letters and numbers.";
		}
		if (value.length() < 4 || value.length() > 20) {
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

	public static String validatePostId(int value) {
		if (value <= 0) {
			return "Post ID must be a positive number.";
		}
		return null;
	}

	public static String validateReplyId(int value) {
		if (value <= 0) {
			return "Reply ID must be a positive number.";
		}
		return null;
	}

	public static String validatePostTitle(String value) {
		if (value == null || value.trim().isEmpty()) {
			return "Post title is required.";
		}

		String trimmed = value.trim();
		if (trimmed.length() > 120) {
			return "Post title must be 1-120 characters.";
		}
		return null;
	}

	public static String validatePostBody(String value) {
		if (value == null || value.trim().isEmpty()) {
			return "Post body is required.";
		}

		String trimmed = value.trim();
		if (trimmed.length() > 2000) {
			return "Post body must be 1-2000 characters.";
		}
		return null;
	}

	public static String validateReplyBody(String value) {
		if (value == null || value.trim().isEmpty()) {
			return "Reply body is required.";
		}

		String trimmed = value.trim();
		if (trimmed.length() > 1000) {
			return "Reply body must be 1-1000 characters.";
		}
		return null;
	}

	public static String validateCourseTag(String value) {
		if (value == null || value.trim().isEmpty()) {
			return "Course tag is required (example: CSE360).";
		}

		String trimmed = value.trim();
		if (!trimmed.matches("[A-Za-z]{3}[0-9]{3}")) {
			return "Course tag must look like CSE360.";
		}
		return null;
	}
}