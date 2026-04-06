package guiGradebook;

import java.util.List;

public class ControllerGradebook {

	private static final ModelGradebook model = new ModelGradebook();

	public ControllerGradebook() {
	}

	protected static void loadStudents() {
		List<String> students = model.getStudentsWithReplies();
		ViewGradebook.list_Students.getItems().clear();
		ViewGradebook.list_Students.getItems().addAll(students);

		if (students.isEmpty()) {
			ViewGradebook.text_Review.setText("No student replies are available yet.");
		} else {
			ViewGradebook.text_Review.setText("Select a student to view rule-of-three proof.");
		}
	}

	protected static void reviewSelectedStudent() {
		String selected = ViewGradebook.list_Students.getSelectionModel().getSelectedItem();
		String reviewText = model.buildStudentReviewText(selected);
		ViewGradebook.text_Review.setText(reviewText);
	}

	protected static boolean canAccess() {
		return model.canAccessGradebook(ViewGradebook.theUser);
	}

	protected static void performReturn() {
		guiStaffHome.ViewStaffHome.displayStaffHome(ViewGradebook.theStage, ViewGradebook.theUser);
	}
}
