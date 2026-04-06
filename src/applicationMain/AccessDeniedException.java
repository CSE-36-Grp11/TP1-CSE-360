package applicationMain;

/**
 * Simple custom exception used when a user tries to access a protected grading feature.
 */
public class AccessDeniedException extends Exception {

	public AccessDeniedException(String message) {
		super(message);
	}
}