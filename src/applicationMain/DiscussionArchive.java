package applicationMain;

import java.util.ArrayList;
import java.util.List;

import entityClasses.Post;
import entityClasses.PostList;
import entityClasses.Reply;
import entityClasses.ReplyList;

/**
 * Prototype archive for grader/teacher workflows.
 *
 * <p>This class provides filtered views over discussion data so graders can quickly inspect
 * student activity without scanning the full board.
 */
public class DiscussionArchive {

	private final PostList postList;
	private final ReplyList replyList;

	/**
	 * Builds an archive view using the shared discussion repository.
	 */
	public DiscussionArchive() {
		DiscussionService service = DiscussionService.getInstance();
		this.postList = service.getPostList();
		this.replyList = service.getReplyList();
	}

	/**
	 * Returns all posts authored by one student.
	 *
	 * @param studentId student id used for filtering
	 * @return posts created by that student
	 */
	public List<Post> getPostsByStudentId(String studentId) {
		List<Post> matched = new ArrayList<Post>();
		if (studentId == null || studentId.trim().isEmpty()) {
			return matched;
		}

		String cleanStudentId = studentId.trim();
		for (Post post : postList.readAllPosts()) {
			String authorId = post.getAuthorAsuUserId();
			if (authorId != null && cleanStudentId.equals(authorId.trim())) {
				matched.add(post);
			}
		}
		return matched;
	}

	/**
	 * Returns all replies authored by one student.
	 *
	 * @param studentId student id used for filtering
	 * @return replies created by that student
	 */
	public List<Reply> getRepliesByStudentId(String studentId) {
		List<Reply> matched = new ArrayList<Reply>();
		if (studentId == null || studentId.trim().isEmpty()) {
			return matched;
		}

		String cleanStudentId = studentId.trim();
		for (Reply reply : replyList.readAllReplies()) {
			String authorId = reply.getAuthorAsuUserId();
			if (authorId != null && cleanStudentId.equals(authorId.trim())) {
				matched.add(reply);
			}
		}
		return matched;
	}
}
