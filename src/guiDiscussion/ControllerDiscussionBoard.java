package guiDiscussion;

import applicationMain.DiscussionService;

public class ControllerDiscussionBoard {

	private static final DiscussionService discussionService = DiscussionService.getInstance();

	public ControllerDiscussionBoard() {
	}

	protected static void showAllPosts() {
		ViewDiscussionBoard.text_PostOutput.setText(discussionService.listAllPostsText());
	}

	protected static void createPost() {
		String title = ViewDiscussionBoard.text_PostTitle.getText();
		String body = ViewDiscussionBoard.text_PostBody.getText();
		String author = getAuthorRoleLabel();

		String error = discussionService.createPost(title, body, author);
		if (error != null) {
			ViewDiscussionBoard.showError(error);
			return;
		}

		ViewDiscussionBoard.text_PostTitle.clear();
		ViewDiscussionBoard.text_PostBody.clear();
		showAllPosts();
	}

	protected static void deletePost() {
		if (!canDeletePost()) {
			ViewDiscussionBoard.showError("Only Admin or Staff can delete posts.");
			return;
		}

		String title = ViewDiscussionBoard.text_PostTitle.getText();
		String error = discussionService.deletePostByTitle(title);
		if (error != null) {
			ViewDiscussionBoard.showError(error);
			return;
		}
		showAllPosts();
	}

	protected static void updatePost() {
		String title = ViewDiscussionBoard.text_PostTitle.getText();
		String newTitle = ViewDiscussionBoard.text_UpdateTitle.getText();
		String newBody = ViewDiscussionBoard.text_PostBody.getText();

		String error = discussionService.updatePostByTitle(title, newTitle, newBody);
		if (error != null) {
			ViewDiscussionBoard.showError(error);
			return;
		}

		ViewDiscussionBoard.text_PostTitle.clear();
		ViewDiscussionBoard.text_UpdateTitle.clear();
		ViewDiscussionBoard.text_PostBody.clear();
		showAllPosts();
	}

	protected static void searchPostsByKeyword() {
		String keyword = ViewDiscussionBoard.text_SearchKeyword.getText();
		ViewDiscussionBoard.text_PostOutput.setText(discussionService.listPostSubsetByKeywordText(keyword));
	}

	protected static void createReply() {
		String title = ViewDiscussionBoard.text_ReplyPostTitle.getText();
		String body = ViewDiscussionBoard.text_ReplyBody.getText();
		String author = getAuthorRoleLabel();

		String error = discussionService.createReplyByPostTitle(title, body, author);
		if (error != null) {
			ViewDiscussionBoard.showError(error);
			return;
		}

		ViewDiscussionBoard.text_ReplyBody.clear();
		showAllPosts();
	}

	protected static void listRepliesForPost() {
		String title = ViewDiscussionBoard.text_ReplyPostTitle.getText();
		ViewDiscussionBoard.text_PostOutput.setText(discussionService.listRepliesForPostTitle(title));
	}

	protected static void performReturn() {
		if (ViewDiscussionBoard.returnRole == 1) {
			guiAdminHome.ViewAdminHome.displayAdminHome(ViewDiscussionBoard.theStage, ViewDiscussionBoard.theUser);
		} else if (ViewDiscussionBoard.returnRole == 2) {
			guiStudentHome.ViewStudentHome.displayStudentHome(ViewDiscussionBoard.theStage, ViewDiscussionBoard.theUser);
		} else {
			guiStaffHome.ViewStaffHome.displayStaffHome(ViewDiscussionBoard.theStage, ViewDiscussionBoard.theUser);
		}
	}

	private static boolean canDeletePost() {
		return ViewDiscussionBoard.theUser.getAdminRole() || ViewDiscussionBoard.theUser.getStaffRole();
	}

	private static String getAuthorRoleLabel() {
		return ViewDiscussionBoard.theUser.getUserName();
	}
}
