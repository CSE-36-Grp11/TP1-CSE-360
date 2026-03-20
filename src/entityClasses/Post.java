package entityClasses;

import java.util.ArrayList;
import java.util.List;

import applicationMain.ValidationUtil;

/**
 * Represents a discussion post and provides class-level CRUD operations for post management.
 *
 * <p>This class satisfies TP2 Task 3 by combining:
 * <ul>
 *   <li>Post attributes needed by Students User Stories and future staff epics.</li>
 *   <li>Input validation for safe create/update behavior.</li>
 *   <li>CRUD operations over an in-memory post repository.</li>
 * </ul>
 *
 * <p><strong>Students User Story coverage:</strong>
 * <ul>
 *   <li>Create: students can submit a post with validated fields.</li>
 *   <li>Read: users can retrieve one post, all posts, or search subsets.</li>
 *   <li>Update: users can modify title/body/course/resolved status.</li>
 *   <li>Delete: posts can be removed by id.</li>
 * </ul>
 *
 * <p><strong>TP3 readiness:</strong> Course context, author identity, and resolved status are
 * included to support future instructional analytics and participation workflows.</p>
 *
 * @author TP2 Team
 * @version 2.00 2026-03-18 Added CRUD and validation operations to Post class
 */
public class Post {

	/**
	 * Repository of all posts for CRUD actions in this class.
	 */
	private static final List<Post> allPosts = new ArrayList<Post>();

	/**
	 * Last computed subset for search/filter operations.
	 */
	private static List<Post> subsetPosts = new ArrayList<Post>();

	/**
	 * Unique identifier for this post.
	 *
	 * <p>Rationale: stable IDs provide a reliable target for update/delete operations and avoid
	 * ambiguity when titles are similar.
	 */
	private int postId;

	/**
	 * Short subject line shown in post lists.
	 *
	 * <p>Rationale: students need a concise summary for scanning discussion threads quickly.
	 */
	private String title;

	/**
	 * Main post content written by the author.
	 *
	 * <p>Rationale: captures the full question/idea needed for discussion and reply workflows.
	 */
	private String body;

	/**
	 * Course context tag such as CSE360.
	 *
	 * <p>Rationale: enables filtering/grouping when a system supports multiple courses or topics.
	 */
	private String courseTag;

	/**
	 * Author identifier for ownership, accountability, and role-based actions.
	 *
	 * <p>Rationale: used for displaying who posted and for future permission checks and analytics.
	 */
	private String authorAsuUserId;

	/**
	 * Indicates whether the post question is considered resolved.
	 *
	 * <p>Rationale: supports student visibility and future instructional-team workflows that track
	 * unresolved questions and participation outcomes.
	 */
	private boolean resolved;

	/**
	 * Creates a new post with all required domain attributes.
	 *
	 * @param postId unique post identifier
	 * @param title post title shown in summaries
	 * @param body full discussion content
	 * @param courseTag course/topic context tag
	 * @param authorAsuUserId ASU user id of the post author
	 * @param resolved true when post is resolved; false otherwise
	 */
	public Post(int postId, String title, String body, String courseTag, String authorAsuUserId, boolean resolved) {
		this.postId = postId;
		this.title = title;
		this.body = body;
		this.courseTag = courseTag;
		this.authorAsuUserId = authorAsuUserId;
		this.resolved = resolved;
	}

	/**
	 * Returns this post's unique identifier.
	 *
	 * @return post id
	 */
	public int getPostId() {
		return postId;
	}

	/**
	 * Updates this post's unique identifier.
	 *
	 * @param postId new post id
	 */
	public void setPostId(int postId) {
		this.postId = postId;
	}

	/**
	 * Returns the current post title.
	 *
	 * @return post title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Updates the post title.
	 *
	 * @param title new title value
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Returns the current post body text.
	 *
	 * @return post body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * Updates the post body text.
	 *
	 * @param body new body value
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * Returns the course tag used for this post.
	 *
	 * @return course tag
	 */
	public String getCourseTag() {
		return courseTag;
	}

	/**
	 * Updates the course tag used for this post.
	 *
	 * @param courseTag new course tag value
	 */
	public void setCourseTag(String courseTag) {
		this.courseTag = courseTag;
	}

	/**
	 * Returns the ASU user id of the post author.
	 *
	 * @return author ASU user id
	 */
	public String getAuthorAsuUserId() {
		return authorAsuUserId;
	}

	/**
	 * Updates the ASU user id of the post author.
	 *
	 * @param authorAsuUserId new author id
	 */
	public void setAuthorAsuUserId(String authorAsuUserId) {
		this.authorAsuUserId = authorAsuUserId;
	}

	/**
	 * Indicates whether this post is marked resolved.
	 *
	 * @return true if resolved; false otherwise
	 */
	public boolean isResolved() {
		return resolved;
	}

	/**
	 * Updates the resolved state of this post.
	 *
	 * @param resolved new resolved flag value
	 */
	public void setResolved(boolean resolved) {
		this.resolved = resolved;
	}

	/**
	 * Creates a post after validating all required fields.
	 *
	 * @param post post to create
	 * @return null when successful; otherwise a validation or duplicate-id message
	 */
	public static String createPost(Post post) {
		if (post == null) {
			return "Post is required.";
		}

		String error = validatePost(post);
		if (error != null) {
			return error;
		}

		if (readPostById(post.getPostId()) != null) {
			return "Post ID already exists.";
		}

		// Requirement mapping (Task 3.2): validated create operation
		allPosts.add(post);
		return null;
	}

	/**
	 * Reads a post by its unique id.
	 *
	 * @param postId post id to read
	 * @return matching post or null when not found
	 */
	public static Post readPostById(int postId) {
		for (Post post : allPosts) {
			if (post.getPostId() == postId) {
				return post;
			}
		}
		return null;
	}

	/**
	 * Reads all posts as a defensive copy.
	 *
	 * @return copy of all posts
	 */
	public static List<Post> readAllPosts() {
		return new ArrayList<Post>(allPosts);
	}

	/**
	 * Reads the current subset view as a defensive copy.
	 *
	 * @return copy of subset posts
	 */
	public static List<Post> readSubsetPosts() {
		return new ArrayList<Post>(subsetPosts);
	}

	/**
	 * Updates an existing post.
	 *
	 * @param postId id of post to update
	 * @param newTitle new title
	 * @param newBody new body
	 * @param newCourseTag new course tag
	 * @param newResolved new resolved state
	 * @return null when successful; otherwise an error message
	 */
	public static String updatePost(int postId, String newTitle, String newBody,
			String newCourseTag, boolean newResolved) {
		Post existing = readPostById(postId);
		if (existing == null) {
			return "Post not found for update.";
		}

		String titleError = ValidationUtil.validatePostTitle(newTitle);
		if (titleError != null) {
			return titleError;
		}

		String bodyError = ValidationUtil.validatePostBody(newBody);
		if (bodyError != null) {
			return bodyError;
		}

		String courseTagError = ValidationUtil.validateCourseTag(newCourseTag);
		if (courseTagError != null) {
			return courseTagError;
		}

		existing.setTitle(newTitle.trim());
		existing.setBody(newBody.trim());
		existing.setCourseTag(newCourseTag.trim());
		existing.setResolved(newResolved);
		return null;
	}

	/**
	 * Deletes a post by id.
	 *
	 * @param postId id of post to delete
	 * @return true when removed; false when not found
	 */
	public static boolean deletePost(int postId) {
		Post existing = readPostById(postId);
		if (existing == null) {
			return false;
		}

		allPosts.remove(existing);
		subsetPosts.remove(existing);
		return true;
	}

	/**
	 * Builds a subset using course tag equality (case-insensitive).
	 *
	 * @param courseTag course tag used for filtering
	 */
	public static void setSubsetFromCourseTag(String courseTag) {
		subsetPosts = new ArrayList<Post>();
		for (Post post : allPosts) {
			if (post.getCourseTag() != null && post.getCourseTag().equalsIgnoreCase(courseTag)) {
				subsetPosts.add(post);
			}
		}
	}

	/**
	 * Builds a subset from keyword matches in title or body.
	 *
	 * @param keyword keyword text to search
	 */
	public static void setSubsetFromKeyword(String keyword) {
		subsetPosts = new ArrayList<Post>();
		if (keyword == null || keyword.trim().isEmpty()) {
			return;
		}

		String lowerKeyword = keyword.toLowerCase();
		for (Post post : allPosts) {
			String titleText = post.getTitle() == null ? "" : post.getTitle().toLowerCase();
			String bodyText = post.getBody() == null ? "" : post.getBody().toLowerCase();
			if (titleText.contains(lowerKeyword) || bodyText.contains(lowerKeyword)) {
				subsetPosts.add(post);
			}
		}
	}

	/**
	 * Clears all subset results.
	 */
	public static void clearSubset() {
		subsetPosts = new ArrayList<Post>();
	}

	/**
	 * Utility for tests to reset all stored posts.
	 */
	public static void clearAllPosts() {
		allPosts.clear();
		subsetPosts.clear();
	}

	/**
	 * Validates a post object before create.
	 *
	 * @param post post to validate
	 * @return null if valid; otherwise error message
	 */
	private static String validatePost(Post post) {
		String postIdError = ValidationUtil.validatePostId(post.getPostId());
		if (postIdError != null) {
			return postIdError;
		}

		String titleError = ValidationUtil.validatePostTitle(post.getTitle());
		if (titleError != null) {
			return titleError;
		}

		String bodyError = ValidationUtil.validatePostBody(post.getBody());
		if (bodyError != null) {
			return bodyError;
		}

		String courseTagError = ValidationUtil.validateCourseTag(post.getCourseTag());
		if (courseTagError != null) {
			return courseTagError;
		}

		String authorError = ValidationUtil.validateAsuUserId(post.getAuthorAsuUserId());
		if (authorError != null) {
			return "Post author: " + authorError;
		}

		return null;
	}
}