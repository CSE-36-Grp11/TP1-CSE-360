package applicationMain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import entityClasses.Post;
import entityClasses.PostList;
import entityClasses.Reply;
import entityClasses.ReplyList;

public class GradingService {

	private final PostList postList;
	private final ReplyList replyList;
	private Role currentRole;

	public GradingService(PostList postList, ReplyList replyList, Role currentRole) {
		this.postList = postList;
		this.replyList = replyList;
		this.currentRole = currentRole;
	}

	public void setCurrentRole(Role currentRole) {
		this.currentRole = currentRole;
	}

	/**
	 * Adds a reply while preserving who authored the original post.
	 * This relationship is required for rule-of-three analytics.
	 *
	 * @param replyId unique reply id
	 * @param postId target post id
	 * @param body reply content
	 * @param replierId author id for the replier
	 * @return saved reply
	 */
	public Reply addReply(int replyId, int postId, String body, String replierId) {
		if (isBlank(replierId)) {
			throw new IllegalArgumentException("Reply author is required.");
		}
		if (isBlank(body)) {
			throw new IllegalArgumentException("Reply body is required.");
		}

		Post targetPost = postList.readPostById(postId);
		if (targetPost == null) {
			throw new IllegalArgumentException("Reply must reference an existing post.");
		}

		String cleanBody = body.trim();
		String cleanReplierId = replierId.trim();
		Reply reply = new Reply(replyId, postId, cleanBody, cleanReplierId, targetPost.getAuthorAsuUserId());
		String error = replyList.createReplyForPost(reply, postList);
		if (error != null) {
			throw new IllegalArgumentException(error);
		}
		return reply;
	}

	/**
	 * Computes how many different students this student has replied to.
	 *
	 * @param studentId student id being evaluated
	 * @return number of unique recipients
	 * @throws AccessDeniedException when caller is not staff/instructor role
	 */
	public int countUniqueStudentsAnswered(String studentId) throws AccessDeniedException {
		requireStaffRole();
		if (isBlank(studentId)) {
			return 0;
		}
		String cleanStudentId = studentId.trim();

		// INTERNAL COMMENT: We use a HashSet here because it strictly
		// enforces uniqueness. This satisfies the requirement to ensure
		// the student answered three DIFFERENT people, not just one person
		// three times.
		// A set also prevents double counting repeated replies to the same target.
		Set<String> uniqueStudentIds = new HashSet<String>();
		for (Reply reply : replyList.readAllReplies()) {
			String replyAuthorId = trimOrEmpty(reply.getAuthorAsuUserId());
			if (!cleanStudentId.equals(replyAuthorId)) {
				continue;
			}

			String answeredStudentId = trimOrEmpty(reply.getOriginalPostAuthorAsuUserId());
			if (answeredStudentId.isEmpty()) {
				Post targetPost = postList.readPostById(reply.getPostId());
				if (targetPost != null) {
					answeredStudentId = trimOrEmpty(targetPost.getAuthorAsuUserId());
				}
			}

			if (!answeredStudentId.isEmpty() && !cleanStudentId.equals(answeredStudentId)) {
				uniqueStudentIds.add(answeredStudentId);
			}
		}
		return uniqueStudentIds.size();
	}

	/**
	 * Assignment-facing method name for the rule-of-three check.
	 *
	 * @param studentId student id being evaluated
	 * @return true if student has replied to at least three unique recipients
	 * @throws AccessDeniedException when caller is not staff/instructor role
	 */
	public boolean checkGradingEligibility(String studentId) throws AccessDeniedException {
		return countUniqueStudentsAnswered(studentId) >= 3;
	}

	/**
	 * Backward-compatible alias for rule-of-three eligibility.
	 *
	 * @param studentId student id being evaluated
	 * @return true when eligibility is met
	 * @throws AccessDeniedException when caller is not staff/instructor role
	 */
	public boolean meetsThreeUniqueRequirement(String studentId) throws AccessDeniedException {
		return checkGradingEligibility(studentId);
	}

	/**
	 * Returns all replies authored by a student for quick reasonableness review.
	 *
	 * @param studentId student id being reviewed
	 * @return list of replies by the student
	 * @throws AccessDeniedException when caller is not staff/instructor role
	 */
	public List<Reply> getAnswersForReview(String studentId) throws AccessDeniedException {
		requireStaffRole();
		List<Reply> answers = new ArrayList<Reply>();
		if (isBlank(studentId)) {
			return answers;
		}
        String cleanStudentId = studentId.trim();

		for (Reply reply : replyList.readAllReplies()) {
			if (cleanStudentId.equals(trimOrEmpty(reply.getAuthorAsuUserId()))) {
				answers.add(reply);
			}
		}
		return answers;
	}

	/**
	 * Builds a compact map of student id to unique-recipient count.
	 *
	 * @return stats map used by grader/teacher view
	 * @throws AccessDeniedException when caller is not staff/instructor role
	 */
	public Map<String, Integer> showAllStats() throws AccessDeniedException {
		requireStaffRole();
		Map<String, Integer> stats = new HashMap<String, Integer>();
		for (Reply reply : replyList.readAllReplies()) {
			String replier = trimOrEmpty(reply.getAuthorAsuUserId());
			if (replier.isEmpty()) {
				continue;
			}
			if (stats.containsKey(replier)) {
				continue;
			}
			stats.put(replier, countUniqueStudentsAnswered(replier));
		}
		return stats;
	}

	private void requireStaffRole() throws AccessDeniedException {
		if (currentRole != Role.INSTRUCTOR && currentRole != Role.GRADER && currentRole != Role.TA) {
			throw new AccessDeniedException("Access denied. Staff role required.");
		}
	}

	private boolean isBlank(String value) {
		return value == null || value.trim().isEmpty();
	}

	private String trimOrEmpty(String value) {
		if (value == null) {
			return "";
		}
		return value.trim();
	}
}
