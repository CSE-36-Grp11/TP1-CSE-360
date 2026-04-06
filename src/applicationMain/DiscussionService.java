package applicationMain;

import java.util.List;
import java.util.ArrayList;

import entityClasses.Post;
import entityClasses.PostList;
import entityClasses.Reply;
import entityClasses.ReplyList;

public class DiscussionService {

	private static final DiscussionService INSTANCE = new DiscussionService();

	private final PostList postList;
	private final ReplyList replyList;
	private int nextPostId;
	private int nextReplyId;

	private DiscussionService() {
		postList = new PostList();
		replyList = new ReplyList();
		nextPostId = 1;
		nextReplyId = 1;
	}

	public static DiscussionService getInstance() {
		return INSTANCE;
	}

	public PostList getPostList() {
		return postList;
	}

	public ReplyList getReplyList() {
		return replyList;
	}

	public String createPost(String title, String body, String authorAsuUserId) {
		String titleError = ValidationUtil.validatePostTitle(title);
		if (titleError != null) {
			return titleError;
		}

		String bodyError = ValidationUtil.validatePostBody(body);
		if (bodyError != null) {
			return bodyError;
		}

		String authorError = ValidationUtil.validateAsuUserId(authorAsuUserId);
		if (authorError != null) {
			return "Post author: " + authorError;
		}

		if (findPostByTitle(title) != null) {
			return "Post title already exists. Please use a unique title.";
		}

		Post post = new Post(nextPostId, title.trim(), body.trim(), "CSE360", authorAsuUserId.trim(), false);
		String error = postList.createPost(post);
		if (error == null) {
			nextPostId++;
		}
		return error;
	}

	public String deletePostByTitle(String titleInput) {
		Post post = findPostByTitle(titleInput);
		if (post == null) {
			return "Post title not found for delete.";
		}

		if (!postList.deletePost(post.getPostId())) {
			return "Post title not found for delete.";
		}
		return null;
	}

	public String createReplyByPostTitle(String postTitleInput, String body, String authorAsuUserId) {
		Post post = findPostByTitle(postTitleInput);
		if (post == null) {
			return "Post title not found for reply.";
		}

		String bodyError = ValidationUtil.validateReplyBody(body);
		if (bodyError != null) {
			return bodyError;
		}

		String authorError = ValidationUtil.validateAsuUserId(authorAsuUserId);
		if (authorError != null) {
			return "Reply author: " + authorError;
		}

		int postId = post.getPostId();
		String replyBody = body.trim();
		Reply reply = new Reply(nextReplyId, postId, replyBody, authorAsuUserId);
		String error = replyList.createReplyForPost(reply, postList);
		if (error == null) {
			nextReplyId++;
		}
		return error;
	}

	public String updatePostByTitle(String titleInput, String newTitleInput, String newBodyInput) {
		Post post = findPostByTitle(titleInput);
		if (post == null) {
			return "Post title not found for update.";
		}

		String updatedTitle = newTitleInput;
		if (updatedTitle == null || updatedTitle.trim().isEmpty()) {
			updatedTitle = post.getTitle();
		}

		String updatedBody = newBodyInput;
		if (updatedBody == null || updatedBody.trim().isEmpty()) {
			updatedBody = post.getBody();
		}

		Post duplicate = findPostByTitle(updatedTitle);
		if (duplicate != null && duplicate.getPostId() != post.getPostId()) {
			return "Post title already exists. Please use a unique title.";
		}

		return postList.updatePost(post.getPostId(), updatedTitle, updatedBody,
				post.getCourseTag(), post.isResolved());
	}

	public String listAllPostsText() {
		return formatPosts(postList.readAllPosts());
	}

	public String listPostSubsetByKeywordText(String keyword) {
		postList.setSubsetFromKeyword(keyword);
		return formatPosts(postList.readSubsetPosts());
	}

	public String listRepliesForPostTitle(String postTitleInput) {
		Post post = findPostByTitle(postTitleInput);
		if (post == null) {
			return "Post title not found for replies.";
		}

		replyList.setSubsetFromPostId(post.getPostId());
		return formatReplies(replyList.readSubsetReplies());
	}

	public String listAllRepliesText() {
		return formatReplies(replyList.readAllReplies());
	}

	private String formatPosts(List<Post> posts) {
		if (posts.isEmpty()) {
			return "No posts found.";
		}

		StringBuilder output = new StringBuilder();
		for (Post post : posts) {
			output.append("Title: ").append(post.getTitle())
					.append(" | ").append(post.getCourseTag())
					.append("\nAuthor: ").append(post.getAuthorAsuUserId())
					.append("\nBody: ").append(post.getBody())
					.append("\n");

			List<Reply> repliesForPost = getRepliesForPost(post.getPostId());
			if (repliesForPost.isEmpty()) {
				output.append("Replies: none\n\n");
			} else {
				output.append("Replies:\n");
				for (Reply reply : repliesForPost) {
					output.append("  - ").append(reply.getAuthorAsuUserId())
							.append(": ").append(reply.getBody())
							.append("\n");
				}
				output.append("\n");
			}
		}
		return output.toString();
	}

	private List<Reply> getRepliesForPost(int postId) {
		List<Reply> matchedReplies = new ArrayList<Reply>();
		for (Reply reply : replyList.readAllReplies()) {
			if (reply.getPostId() == postId) {
				matchedReplies.add(reply);
			}
		}
		return matchedReplies;
	}

	private String formatReplies(List<Reply> replies) {
		if (replies.isEmpty()) {
			return "No replies found.";
		}

		StringBuilder output = new StringBuilder();
		for (Reply reply : replies) {
			Post post = postList.readPostById(reply.getPostId());
			String postTitle = post == null ? "Unknown Post" : post.getTitle();
			output.append("Reply to: ").append(postTitle)
					.append("\nAuthor: ").append(reply.getAuthorAsuUserId())
					.append("\nBody: ").append(reply.getBody())
					.append("\n\n");
		}
		return output.toString();
	}

	private Post findPostByTitle(String titleInput) {
		if (titleInput == null) {
			return null;
		}

		String target = titleInput.trim();
		if (target.isEmpty()) {
			return null;
		}

		for (Post post : postList.readAllPosts()) {
			if (post.getTitle() != null && post.getTitle().trim().equalsIgnoreCase(target)) {
				return post;
			}
		}

		for (Post post : postList.readAllPosts()) {
			if (post.getTitle() != null && post.getTitle().toLowerCase().contains(target.toLowerCase())) {
				return post;
			}
		}

		return null;
	}
}
