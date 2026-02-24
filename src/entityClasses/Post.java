package entityClasses;

public class Post {

	private int postId;
	private String title;
	private String body;
	private String courseTag;
	private String authorAsuUserId;
	private boolean resolved;

	public Post(int postId, String title, String body, String courseTag, String authorAsuUserId, boolean resolved) {
		this.postId = postId;
		this.title = title;
		this.body = body;
		this.courseTag = courseTag;
		this.authorAsuUserId = authorAsuUserId;
		this.resolved = resolved;
	}

	public int getPostId() {
		return postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getCourseTag() {
		return courseTag;
	}

	public void setCourseTag(String courseTag) {
		this.courseTag = courseTag;
	}

	public String getAuthorAsuUserId() {
		return authorAsuUserId;
	}

	public void setAuthorAsuUserId(String authorAsuUserId) {
		this.authorAsuUserId = authorAsuUserId;
	}

	public boolean isResolved() {
		return resolved;
	}

	public void setResolved(boolean resolved) {
		this.resolved = resolved;
	}
}