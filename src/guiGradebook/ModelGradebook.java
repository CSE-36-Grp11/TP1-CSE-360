package guiGradebook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import applicationMain.AccessDeniedException;
import applicationMain.DiscussionService;
import applicationMain.GradingService;
import applicationMain.Role;
import entityClasses.Post;
import entityClasses.PostList;
import entityClasses.Reply;
import entityClasses.ReplyList;
import entityClasses.User;

public class ModelGradebook {

	private final PostList postList;
	private final ReplyList replyList;
	private final GradingService gradingService;

	public ModelGradebook() {
		DiscussionService discussionService = DiscussionService.getInstance();
		postList = discussionService.getPostList();
		replyList = discussionService.getReplyList();
		gradingService = new GradingService(postList, replyList, Role.INSTRUCTOR);
	}

	public boolean canAccessGradebook(User user) {
		if (user == null) {
			return false;
		}
		return user.getStaffRole() || user.getAdminRole();
	}

	public List<String> getStudentsWithReplies() {
		List<String> students = new ArrayList<String>();
		for (Reply reply : replyList.readAllReplies()) {
			String author = reply.getAuthorAsuUserId();
			if (author == null || author.trim().isEmpty()) {
				continue;
			}

			String cleanAuthor = author.trim();
			if (!students.contains(cleanAuthor)) {
				students.add(cleanAuthor);
			}
		}
		Collections.sort(students);
		return students;
	}

	public String buildStudentReviewText(String studentUserName) {
		if (studentUserName == null || studentUserName.trim().isEmpty()) {
			return "Select a student to review their gradebook details.";
		}

		String studentId = studentUserName.trim();
		StringBuilder text = new StringBuilder();
		text.append("Student: ").append(studentId).append("\n");

		int uniqueCount;
		boolean meetsRule;
		try {
			uniqueCount = gradingService.countUniqueStudentsAnswered(studentId);
			meetsRule = gradingService.meetsThreeUniqueRequirement(studentId);
		} catch (AccessDeniedException ex) {
			return "Access denied while reading gradebook data.";
		}

		text.append("Unique students answered: ").append(uniqueCount).append("\n");
		if (meetsRule) {
			text.append("Status: COMPLETE (Rule of Three met)").append("\n\n");
		} else {
			text.append("Status: INCOMPLETE (Needs replies to more unique students)").append("\n\n");
		}

		List<Reply> replies;
		try {
			replies = gradingService.getAnswersForReview(studentId);
		} catch (AccessDeniedException ex) {
			return "Access denied while reading replies for review.";
		}

		if (replies.isEmpty()) {
			text.append("No replies found for this student.");
			return text.toString();
		}

		text.append("Proof (what they said and to whom):\n");
		int index = 1;
		for (Reply reply : replies) {
			String target = reply.getOriginalPostAuthorAsuUserId();
			if (target == null || target.trim().isEmpty()) {
				Post post = postList.readPostById(reply.getPostId());
				if (post != null) {
					target = post.getAuthorAsuUserId();
				}
			}

			if (target == null || target.trim().isEmpty()) {
				target = "Unknown";
			}

			text.append(index).append(". Replied to ").append(target.trim()).append("\n");
			text.append("   Message: ").append(reply.getBody()).append("\n\n");
			index++;
		}

		return text.toString();
	}
}
