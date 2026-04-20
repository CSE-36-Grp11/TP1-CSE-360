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

/**
 * Service class for grading student discussions based on their replies.
 * <p>
 * JUnit tests used: {@link applicationMain.GradingServiceTest}
 * nSemi-automated tests used: N/A
 * nManual tests used: GradingService Manual Tests (see Manual Tests.pdf)
 * </p>
 */

/**
 * Service class for grading student discussions based on their replies.
 * <p>
 * JUnit tests used: {@link applicationMain.GradingServiceTest}
 * nSemi-automated tests used: N/A
 * nManual tests used: GradingService Manual Tests (see Manual Tests.pdf)
 * </p>
 */

public class GradingService {

	// In-memory list of posts used to resolve reply targets and authors.
	private final PostList postList;
	// In-memory list of replies used to compute participation statistics.
	private final ReplyList replyList;
	// Role of the currently logged-in user for access-control checks.
	private Role currentRole;

	/**
	 * Builds a grading service around the shared post/reply stores.
	 *
	 * @param postList list of discussion posts used to resolve recipients
	 * @param replyList list of replies used to compute participation stats
	 * @param currentRole active caller role used for access checks
	 */
	public GradingService(PostList postList, ReplyList replyList, Role currentRole) {
		// Keep references so all grading operations use the same data sources.
		this.postList = postList;
		this.replyList = replyList;
		// Store caller role to enforce instructor/grader/TA-only operations.
		this.currentRole = currentRole;
	}

	/**
	 * Updates the active role used by protected grading methods.
	 *
	 * @param currentRole new role to apply for authorization
	 */
	public void setCurrentRole(Role currentRole) {
		// Allow controllers/tests to change active role without recreating service.
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
		// Reject missing/blank author to enforce input integrity.
		if (isBlank(replierId)) {
			throw new IllegalArgumentException("Reply author is required.");
		}
		// Reject missing/blank message body to avoid empty participation entries.
		if (isBlank(body)) {
			throw new IllegalArgumentException("Reply body is required.");
		}

		// Resolve the post being answered so we can preserve recipient identity.
		Post targetPost = postList.readPostById(postId);
		if (targetPost == null) {
			throw new IllegalArgumentException("Reply must reference an existing post.");
		}

		// Normalize user input before persisting.
		String cleanBody = body.trim();
		String cleanReplierId = replierId.trim();
		// Store recipient author on the reply to support unique-recipient counting.
		Reply reply = new Reply(replyId, postId, cleanBody, cleanReplierId, targetPost.getAuthorAsuUserId());
		// Persist reply via ReplyList which also performs data-level validation.
		String error = replyList.createReplyForPost(reply, postList);
		if (error != null) {
			// Bubble persistence/validation errors as IllegalArgumentException.
			throw new IllegalArgumentException(error);
		}
		// Return the saved reply so caller can inspect stored values.
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
		// Only staff/instructors can inspect grading analytics.
		requireStaffRole();
		// Empty student IDs have no measurable participation.
		if (isBlank(studentId)) {
			return 0;
		}
		// Trim to avoid whitespace-caused mismatches.
		String cleanStudentId = studentId.trim();

		// HashSet guarantees one entry per recipient ID, preventing duplicates.
		Set<String> uniqueStudentIds = new HashSet<String>();
		// Evaluate every reply and keep only those authored by this student.
		for (Reply reply : replyList.readAllReplies()) {
			// Normalize reply author for safe equality checks.
			String replyAuthorId = trimOrEmpty(reply.getAuthorAsuUserId());
			if (!cleanStudentId.equals(replyAuthorId)) {
				// Skip replies written by other students.
				continue;
			}

			// Primary recipient source: original post author captured on the reply.
			String answeredStudentId = trimOrEmpty(reply.getOriginalPostAuthorAsuUserId());
			if (answeredStudentId.isEmpty()) {
				// Fallback for legacy replies: resolve recipient from referenced post.
				Post targetPost = postList.readPostById(reply.getPostId());
				if (targetPost != null) {
					answeredStudentId = trimOrEmpty(targetPost.getAuthorAsuUserId());
				}
			}

			// Count only valid peer interactions (exclude blanks and self-replies).
			if (!answeredStudentId.isEmpty() && !cleanStudentId.equals(answeredStudentId)) {
				// Duplicate adds are ignored by HashSet automatically.
				uniqueStudentIds.add(answeredStudentId);
			}
		}
		// Set size is the unique-peer count used by Rule of Three.
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
		// Eligibility flips to true only at the threshold of 3 unique peers.
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
		// Alias retained for readability/backward compatibility in callers.
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
		// Restrict review access to staff/instructors.
		requireStaffRole();
		// Output list for all replies written by the requested student.
		List<Reply> answers = new ArrayList<Reply>();
		// Empty student IDs have no reviewable replies.
		if (isBlank(studentId)) {
			return answers;
		}
		// Normalize once and compare repeatedly.
        String cleanStudentId = studentId.trim();

		// Collect authored replies exactly matching the selected student.
		for (Reply reply : replyList.readAllReplies()) {
			if (cleanStudentId.equals(trimOrEmpty(reply.getAuthorAsuUserId()))) {
				answers.add(reply);
			}
		}
		// Return evidence list used by gradebook proof panel.
		return answers;
	}

	/**
	 * Builds a compact map of student id to unique-recipient count.
	 *
	 * @return stats map used by grader/teacher view
	 * @throws AccessDeniedException when caller is not staff/instructor role
	 */
	public Map<String, Integer> showAllStats() throws AccessDeniedException {
		// Restrict global analytics to staff/instructors.
		requireStaffRole();
		// Key: student ID, Value: unique peers answered by that student.
		Map<String, Integer> stats = new HashMap<String, Integer>();
		// Iterate all replies to discover which students have participated.
		for (Reply reply : replyList.readAllReplies()) {
			// Normalize replier ID to avoid null/whitespace edge cases.
			String replier = trimOrEmpty(reply.getAuthorAsuUserId());
			if (replier.isEmpty()) {
				// Skip malformed replies with no author.
				continue;
			}
			if (stats.containsKey(replier)) {
				// Already computed this student's count; avoid redundant recalculation.
				continue;
			}
			// Compute Rule-of-Three numerator for each discovered student.
			stats.put(replier, countUniqueStudentsAnswered(replier));
		}
		// Return full snapshot used by teacher/staff stats views.
		return stats;
	}

	/**
	 * Enforces that only instructional roles can access grading analytics.
	 *
	 * @throws AccessDeniedException when current role is not INSTRUCTOR, GRADER, or TA
	 */
	private void requireStaffRole() throws AccessDeniedException {
		// Permit only roles authorized to access grading analytics.
		if (currentRole != Role.INSTRUCTOR && currentRole != Role.GRADER && currentRole != Role.TA) {
			// Student/other roles receive explicit access denial.
			throw new AccessDeniedException("Access denied. Staff role required.");
		}
	}

	/**
	 * Checks whether a string is null or whitespace-only.
	 *
	 * @param value value to evaluate
	 * @return true when value is null or blank after trimming
	 */
	private boolean isBlank(String value) {
		// Utility: true when null or whitespace-only.
		return value == null || value.trim().isEmpty();
	}

	/**
	 * Normalizes null strings to empty strings and trims surrounding spaces.
	 *
	 * @param value input value
	 * @return normalized non-null value
	 */
	private String trimOrEmpty(String value) {
		// Utility: normalize null to empty string for safe comparisons.
		if (value == null) {
			return "";
		}
		// Utility: normalize surrounding whitespace.
		return value.trim();
	}
}
