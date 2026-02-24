package entityClasses;

public class Reply {

	private int replyId;
	private int postId;
	private String body;
	private String authorAsuUserId;

	public Reply(int replyId, int postId, String body, String authorAsuUserId) {
		this.replyId = replyId;
		this.postId = postId;
		this.body = body;
		this.authorAsuUserId = authorAsuUserId;
	}

	public int getReplyId() {
		return replyId;
	}

	public void setReplyId(int replyId) {
		this.replyId = replyId;
	}

	public int getPostId() {
		return postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getAuthorAsuUserId() {
		return authorAsuUserId;
	}

	public void setAuthorAsuUserId(String authorAsuUserId) {
		this.authorAsuUserId = authorAsuUserId;
	}
}
