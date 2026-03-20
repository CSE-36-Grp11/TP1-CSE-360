package guiDiscussion;

import applicationMain.DiscussionService;
import entityClasses.User;

/*******
 * <p> Title: ModelDiscussionBoard Class. </p>
 *
 * <p> Description: The Discussion Board Model. This model centralizes data operations for
 * discussion posts and replies so the controller can focus on coordinating user actions and
 * view updates. </p>
 *
 * <p> Copyright: Lynn Robert Carter © 2026 </p>
 *
 * @author TP2 Team
 *
 * @version 1.00 2026-03-18 Initial version
 */
public class ModelDiscussionBoard {

	private final DiscussionService discussionService;

	/**
	 * Default constructor that wires this model to the shared DiscussionService.
	 */
	public ModelDiscussionBoard() {
		discussionService = DiscussionService.getInstance();
	}

	/**
	 * Returns formatted text for all posts and their replies.
	 *
	 * @return formatted discussion board text
	 */
	public String readAllPostsText() {
		return discussionService.listAllPostsText();
	}

	/**
	 * Creates a new post.
	 *
	 * @param title post title from the view
	 * @param body post body from the view
	 * @param authorAsuUserId current user's ASU user id
	 * @return null when successful, otherwise an error message
	 */
	public String createPost(String title, String body, String authorAsuUserId) {
		return discussionService.createPost(title, body, authorAsuUserId);
	}

	/**
	 * Updates an existing post identified by title.
	 *
	 * @param titleInput current title used to locate the post
	 * @param newTitleInput new title value (optional)
	 * @param newBodyInput new body value (optional)
	 * @return null when successful, otherwise an error message
	 */
	public String updatePostByTitle(String titleInput, String newTitleInput, String newBodyInput) {
		return discussionService.updatePostByTitle(titleInput, newTitleInput, newBodyInput);
	}

	/**
	 * Deletes a post identified by title.
	 *
	 * @param titleInput post title to delete
	 * @return null when successful, otherwise an error message
	 */
	public String deletePostByTitle(String titleInput) {
		return discussionService.deletePostByTitle(titleInput);
	}

	/**
	 * Returns formatted post subset text for a keyword.
	 *
	 * @param keyword keyword provided by the user
	 * @return formatted post subset text
	 */
	public String searchPostsByKeywordText(String keyword) {
		return discussionService.listPostSubsetByKeywordText(keyword);
	}

	/**
	 * Creates a reply for the post identified by title.
	 *
	 * @param postTitleInput post title to reply to
	 * @param body reply body
	 * @param authorAsuUserId current user's ASU user id
	 * @return null when successful, otherwise an error message
	 */
	public String createReplyByPostTitle(String postTitleInput, String body, String authorAsuUserId) {
		return discussionService.createReplyByPostTitle(postTitleInput, body, authorAsuUserId);
	}

	/**
	 * Returns formatted replies for the specified post title.
	 *
	 * @param postTitleInput post title to inspect
	 * @return formatted replies text or an error message
	 */
	public String listRepliesForPostTitle(String postTitleInput) {
		return discussionService.listRepliesForPostTitle(postTitleInput);
	}

	/**
	 * Determines whether this user can delete posts.
	 *
	 * @param user current user
	 * @return true when user has admin or staff role; false otherwise
	 */
	public boolean canDeletePosts(User user) {
		return user != null && (user.getAdminRole() || user.getStaffRole());
	}
}