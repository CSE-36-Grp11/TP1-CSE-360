package entityClasses;

import java.util.ArrayList;
import java.util.List;

import applicationMain.ValidationUtil;

public class PostList {

	private final List<Post> allPosts;
	private List<Post> subsetPosts;

	public PostList() {
		this.allPosts = new ArrayList<Post>();
		this.subsetPosts = new ArrayList<Post>();
	}

	public String createPost(Post post) {
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

		allPosts.add(post);
		return null;
	}

	public Post readPostById(int postId) {
		for (Post post : allPosts) {
			if (post.getPostId() == postId) {
				return post;
			}
		}
		return null;
	}

	public List<Post> readAllPosts() {
		return new ArrayList<Post>(allPosts);
	}

	public List<Post> readSubsetPosts() {
		return new ArrayList<Post>(subsetPosts);
	}

	public String updatePost(int postId, String newTitle, String newBody, String newCourseTag, boolean newResolved) {
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

	public boolean deletePost(int postId) {
		Post existing = readPostById(postId);
		if (existing == null) {
			return false;
		}

		allPosts.remove(existing);
		subsetPosts.remove(existing);
		return true;
	}

	public void setSubsetFromCourseTag(String courseTag) {
		subsetPosts = new ArrayList<Post>();
		for (Post post : allPosts) {
			if (post.getCourseTag().equalsIgnoreCase(courseTag)) {
				subsetPosts.add(post);
			}
		}
	}

	public void setSubsetFromKeyword(String keyword) {
		subsetPosts = new ArrayList<Post>();
		if (keyword == null || keyword.trim().isEmpty()) {
			return;
		}

		String lowerKeyword = keyword.toLowerCase();
		for (Post post : allPosts) {
			String title = post.getTitle() == null ? "" : post.getTitle().toLowerCase();
			String body = post.getBody() == null ? "" : post.getBody().toLowerCase();
			if (title.contains(lowerKeyword) || body.contains(lowerKeyword)) {
				subsetPosts.add(post);
			}
		}
	}

	public void clearSubset() {
		subsetPosts = new ArrayList<Post>();
	}

	private String validatePost(Post post) {
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
