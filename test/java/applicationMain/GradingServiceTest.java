package applicationMain;

import entityClasses.Post;
import entityClasses.PostList;
import entityClasses.Reply;
import entityClasses.ReplyList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GradingServiceTest {

	private PostList postList;
	private ReplyList replyList;
	private GradingService gradingService;

	@BeforeEach
	void setUp() {
		postList = new PostList();
		replyList = new ReplyList();
		gradingService = new GradingService(postList, replyList, Role.INSTRUCTOR);

		postList.createPost(new Post(100, "Q1", "Question", "CSE360", "studB", false));
		postList.createPost(new Post(101, "Q2", "Question", "CSE360", "studC", false));
		postList.createPost(new Post(102, "Q3", "Question", "CSE360", "studD", false));
		postList.createPost(new Post(103, "Q4", "Question", "CSE360", "studE", false));
	}

	@Test
	void boundary_zeroUniqueStudents_returnsFalse() throws Exception {
		assertEquals(0, gradingService.countUniqueStudentsAnswered("studA"));
		assertFalse(gradingService.checkGradingEligibility("studA"));
	}

	@Test
	void boundary_oneUniqueStudent_returnsFalse() throws Exception {
		gradingService.addReply(50, 100, "Answer B", "studA");

		assertEquals(1, gradingService.countUniqueStudentsAnswered("studA"));
		assertFalse(gradingService.checkGradingEligibility("studA"));
	}

	@Test
	void belowBoundary_twoUniqueStudents_returnsFalse() throws Exception {
		gradingService.addReply(1, 100, "Answer B", "studA");
		gradingService.addReply(2, 101, "Answer C", "studA");

		assertEquals(2, gradingService.countUniqueStudentsAnswered("studA"));
		assertFalse(gradingService.checkGradingEligibility("studA"));
	}

	@Test
	void onBoundary_threeUniqueStudents_returnsTrue() throws Exception {
		gradingService.addReply(3, 100, "Answer B", "studA");
		gradingService.addReply(4, 101, "Answer C", "studA");
		gradingService.addReply(5, 102, "Answer D", "studA");

		assertEquals(3, gradingService.countUniqueStudentsAnswered("studA"));
		assertTrue(gradingService.checkGradingEligibility("studA"));
	}

	@Test
	void aboveBoundary_tenUniqueStudents_returnsTrue() throws Exception {
		for (int i = 0; i < 10; i++) {
			int postId = 200 + i;
			postList.createPost(new Post(postId, "Q" + i, "Question " + i, "CSE360", "peer" + i, false));
			gradingService.addReply(1000 + i, postId, "Answer " + i, "studA");
		}

		assertEquals(10, gradingService.countUniqueStudentsAnswered("studA"));
		assertTrue(gradingService.checkGradingEligibility("studA"));
	}

	@Test
	void duplicateEdge_multipleRepliesToSameAuthor_countsOnce() throws Exception {
		gradingService.addReply(6, 100, "Answer 1", "studA");
		gradingService.addReply(7, 100, "Answer 2", "studA");
		gradingService.addReply(8, 100, "Answer 3", "studA");

		assertEquals(1, gradingService.countUniqueStudentsAnswered("studA"));
		assertFalse(gradingService.checkGradingEligibility("studA"));
	}

	@Test
	void xssInjection_inputIsSanitizedBeforeSaving() {
		Reply saved = gradingService.addReply(9, 100, "<script>alert('hacked')</script>", "studA");
		assertEquals("&lt;script&gt;alert(&#39;hacked&#39;)&lt;/script&gt;", saved.getBody());
	}

	@Test
	void nullOrEmptyInput_throwsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> gradingService.addReply(10, 100, "   ", "studA"));
		assertThrows(IllegalArgumentException.class, () -> gradingService.addReply(11, 100, "Body", null));
	}

	@Test
	void unauthorizedAccess_studentRoleThrowsAccessDenied() {
		gradingService.setCurrentRole(Role.STUDENT);
		assertThrows(AccessDeniedException.class, () -> gradingService.showAllStats());
	}

	@Test
	void coveragePaths_zeroReplies_and_reviewSelection() throws Exception {
		assertEquals(0, gradingService.countUniqueStudentsAnswered("noRepliesStudent"));

		gradingService.addReply(12, 100, "Answer B", "studA");
		gradingService.addReply(13, 101, "Answer C", "studA");
		gradingService.addReply(14, 103, "Answer E", "studZ");

		List<Reply> review = gradingService.getAnswersForReview("studA");
		assertEquals(2, review.size());
	}

	/**
	 * Positive prototype test used for documentation screenshots.
	 * A student with three unique recipients should be grading-eligible.
	 */
	@Test
	void testEligibleStudent() throws Exception {
		gradingService.addReply(2001, 100, "Answer to B", "studX");
		gradingService.addReply(2002, 101, "Answer to C", "studX");
		gradingService.addReply(2003, 102, "Answer to D", "studX");

		assertTrue(gradingService.checkGradingEligibility("studX"));
		assertEquals(3, gradingService.countUniqueStudentsAnswered("studX"));
	}

	/**
	 * Negative/boundary prototype test used for documentation screenshots.
	 * Repeated replies to one recipient must not count as multiple unique recipients.
	 */
	@Test
	void testIneligibleStudent() throws Exception {
		gradingService.addReply(3001, 100, "First reply", "studY");
		gradingService.addReply(3002, 100, "Second reply same recipient", "studY");
		gradingService.addReply(3003, 100, "Third reply same recipient", "studY");

		assertFalse(gradingService.checkGradingEligibility("studY"));
		assertEquals(1, gradingService.countUniqueStudentsAnswered("studY"));
	}
}
