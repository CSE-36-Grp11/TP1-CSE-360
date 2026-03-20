package guiDiscussion;

/**
 * Controller for the discussion board GUI.
 *
 * <p>This controller coordinates user actions from the view with discussion data operations in
 * the model. It applies role checks for restricted actions and refreshes view output after
 * successful create, read, update, and delete operations.
 *
 * <p>Students User Story alignment includes creating posts, reading all posts, searching by
 * keyword, updating posts, creating replies, and viewing replies for a selected post.
 *
 * @author TP2 Team
 * @version 1.00 2026-03-18 MVC documentation update
 */
public class ControllerDiscussionBoard {

	private static final ModelDiscussionBoard model = new ModelDiscussionBoard();

	/**
	 * Default constructor is not used. Controller methods are invoked statically by the view.
	 */
	public ControllerDiscussionBoard() {
	}

	/**
	 * Reads and displays all posts and their replies in the output area.
	 */
	protected static void showAllPosts() {
		ViewDiscussionBoard.text_PostOutput.setText(model.readAllPostsText());
	}

	/**
	 * Creates a post from user input fields and refreshes the display on success.
	 */
	protected static void createPost() {
		String title = ViewDiscussionBoard.text_PostTitle.getText();
		String body = ViewDiscussionBoard.text_PostBody.getText();
		String author = getAuthorRoleLabel();

		String error = model.createPost(title, body, author);
		if (error != null) {
			ViewDiscussionBoard.showError(error);
			return;
		}

		ViewDiscussionBoard.text_PostTitle.clear();
		ViewDiscussionBoard.text_PostBody.clear();
		showAllPosts();
	}

	/**
	 * Deletes a post by title when the current user has admin or staff privileges.
	 */
	protected static void deletePost() {
		if (!canDeletePost()) {
			ViewDiscussionBoard.showError("Only Admin or Staff can delete posts.");
			return;
		}

		String title = ViewDiscussionBoard.text_PostTitle.getText();
		String error = model.deletePostByTitle(title);
		if (error != null) {
			ViewDiscussionBoard.showError(error);
			return;
		}
		showAllPosts();
	}

	/**
	 * Updates an existing post title/body using input fields and refreshes output on success.
	 */
	protected static void updatePost() {
		String title = ViewDiscussionBoard.text_PostTitle.getText();
		String newTitle = ViewDiscussionBoard.text_UpdateTitle.getText();
		String newBody = ViewDiscussionBoard.text_PostBody.getText();

		String error = model.updatePostByTitle(title, newTitle, newBody);
		if (error != null) {
			ViewDiscussionBoard.showError(error);
			return;
		}

		ViewDiscussionBoard.text_PostTitle.clear();
		ViewDiscussionBoard.text_UpdateTitle.clear();
		ViewDiscussionBoard.text_PostBody.clear();
		showAllPosts();
	}

	/**
	 * Runs a keyword search over posts and displays the matching subset.
	 */
	protected static void searchPostsByKeyword() {
		String keyword = ViewDiscussionBoard.text_SearchKeyword.getText();
		ViewDiscussionBoard.text_PostOutput.setText(model.searchPostsByKeywordText(keyword));
	}

	/**
	 * Creates a reply for the selected post title and refreshes posts on success.
	 */
	protected static void createReply() {
		String title = ViewDiscussionBoard.text_ReplyPostTitle.getText();
		String body = ViewDiscussionBoard.text_ReplyBody.getText();
		String author = getAuthorRoleLabel();

		String error = model.createReplyByPostTitle(title, body, author);
		if (error != null) {
			ViewDiscussionBoard.showError(error);
			return;
		}

		ViewDiscussionBoard.text_ReplyBody.clear();
		showAllPosts();
	}

	/**
	 * Displays all replies associated with the title entered in the reply target field.
	 */
	protected static void listRepliesForPost() {
		String title = ViewDiscussionBoard.text_ReplyPostTitle.getText();
		ViewDiscussionBoard.text_PostOutput.setText(model.listRepliesForPostTitle(title));
	}

	/**
	 * Returns the user to the role-specific home page.
	 */
	protected static void performReturn() {
		if (ViewDiscussionBoard.returnRole == 1) {
			guiAdminHome.ViewAdminHome.displayAdminHome(ViewDiscussionBoard.theStage, ViewDiscussionBoard.theUser);
		} else if (ViewDiscussionBoard.returnRole == 2) {
			guiStudentHome.ViewStudentHome.displayStudentHome(ViewDiscussionBoard.theStage, ViewDiscussionBoard.theUser);
		} else {
			guiStaffHome.ViewStaffHome.displayStaffHome(ViewDiscussionBoard.theStage, ViewDiscussionBoard.theUser);
		}
	}

	/**
	 * Determines whether the current user can delete posts.
	 *
	 * @return true when delete is permitted; false otherwise
	 */
	private static boolean canDeletePost() {
		return model.canDeletePosts(ViewDiscussionBoard.theUser);
	}

	/**
	 * Returns the current user's username for author fields.
	 *
	 * @return current user's username
	 */
	private static String getAuthorRoleLabel() {
		return ViewDiscussionBoard.theUser.getUserName();
	}
}
