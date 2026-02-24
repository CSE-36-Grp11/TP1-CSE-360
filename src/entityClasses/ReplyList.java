package entityClasses;

import java.util.ArrayList;
import java.util.List;

import applicationMain.ValidationUtil;

public class ReplyList {

	private final List<Reply> allReplies;
	private List<Reply> subsetReplies;

	public ReplyList() {
		this.allReplies = new ArrayList<Reply>();
		this.subsetReplies = new ArrayList<Reply>();
	}

	public String createReply(Reply reply) {
		if (reply == null) {
			return "Reply is required.";
		}

		String error = validateReply(reply);
		if (error != null) {
			return error;
		}

		if (readReplyById(reply.getReplyId()) != null) {
			return "Reply ID already exists.";
		}

		allReplies.add(reply);
		return null;
	}

	public String createReplyForPost(Reply reply, PostList postList) {
		String error = createReply(reply);
		if (error != null) {
			return error;
		}

		if (postList == null || postList.readPostById(reply.getPostId()) == null) {
			allReplies.remove(reply);
			return "Reply must reference an existing post.";
		}

		return null;
	}

	public Reply readReplyById(int replyId) {
		for (Reply reply : allReplies) {
			if (reply.getReplyId() == replyId) {
				return reply;
			}
		}
		return null;
	}

	public List<Reply> readAllReplies() {
		return new ArrayList<Reply>(allReplies);
	}

	public List<Reply> readSubsetReplies() {
		return new ArrayList<Reply>(subsetReplies);
	}

	public String updateReply(int replyId, String newBody) {
		Reply existing = readReplyById(replyId);
		if (existing == null) {
			return "Reply not found for update.";
		}

		String bodyError = ValidationUtil.validateReplyBody(newBody);
		if (bodyError != null) {
			return bodyError;
		}

		existing.setBody(newBody.trim());
		return null;
	}

	public boolean deleteReply(int replyId) {
		Reply existing = readReplyById(replyId);
		if (existing == null) {
			return false;
		}

		allReplies.remove(existing);
		subsetReplies.remove(existing);
		return true;
	}

	public void setSubsetFromPostId(int postId) {
		subsetReplies = new ArrayList<Reply>();
		for (Reply reply : allReplies) {
			if (reply.getPostId() == postId) {
				subsetReplies.add(reply);
			}
		}
	}

	public void setSubsetFromKeyword(String keyword) {
		subsetReplies = new ArrayList<Reply>();
		if (keyword == null || keyword.trim().isEmpty()) {
			return;
		}

		String lowerKeyword = keyword.toLowerCase();
		for (Reply reply : allReplies) {
			String body = reply.getBody() == null ? "" : reply.getBody().toLowerCase();
			if (body.contains(lowerKeyword)) {
				subsetReplies.add(reply);
			}
		}
	}

	public void clearSubset() {
		subsetReplies = new ArrayList<Reply>();
	}

	private String validateReply(Reply reply) {
		String replyIdError = ValidationUtil.validateReplyId(reply.getReplyId());
		if (replyIdError != null) {
			return replyIdError;
		}

		String postIdError = ValidationUtil.validatePostId(reply.getPostId());
		if (postIdError != null) {
			return "Reply post ID: " + postIdError;
		}

		String bodyError = ValidationUtil.validateReplyBody(reply.getBody());
		if (bodyError != null) {
			return bodyError;
		}

		String authorError = ValidationUtil.validateAsuUserId(reply.getAuthorAsuUserId());
		if (authorError != null) {
			return "Reply author: " + authorError;
		}

		return null;
	}
}
