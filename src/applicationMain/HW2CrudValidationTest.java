package applicationMain;

import entityClasses.Post;
import entityClasses.PostList;
import entityClasses.Reply;
import entityClasses.ReplyList;

/**
 * Semi-automated validation test suite for TP2 student post and reply workflows.
 *
 * <p><strong>Purpose (Task 6):</strong> provide executable evidence that CRUD and validation
 * behaviors satisfy the intent of Students User Stories.
 *
 * <p><strong>Requirement list covered in this class:</strong>
 * <ol>
 *   <li>R1: Create post succeeds for valid input.</li>
 *   <li>R2: Create post rejects duplicate IDs.</li>
 *   <li>R3: Create post rejects invalid title input.</li>
 *   <li>R4: Read post by id returns existing post.</li>
 *   <li>R5: Update post succeeds for valid target and data.</li>
 *   <li>R6: Update post rejects missing target.</li>
 *   <li>R7: Delete post succeeds for existing target.</li>
 *   <li>R8: Delete post reports false for missing target.</li>
 *   <li>R9: Create reply succeeds when post reference exists.</li>
 *   <li>R10: Create reply rejects non-existent post reference.</li>
 *   <li>R11: Update reply rejects invalid body.</li>
 *   <li>R12: Subset search returns expected post/reply subsets.</li>
 * </ol>
 *
 * <p><strong>How to interpret output:</strong>
 * each test prints PASS or FAIL. The summary prints total passed/failed counts.
 * A requirement is considered satisfied when its mapped test prints PASS and summary shows
 * zero unexpected failures.
 */
public class HW2CrudValidationTest {

	private static int passed = 0;
	private static int failed = 0;

	/**
	 * Executes all built-in test cases in deterministic order.
	 *
	 * @param args command-line arguments (not used)
	 */
	public static void main(String[] args) {
		System.out.println("=== HW2 CRUD + Validation Tests ===\n");

		PostList postList = new PostList();
		ReplyList replyList = new ReplyList();

		testCreatePostSuccess(postList);
		testCreatePostDuplicateId(postList);
		testCreatePostInvalidTitle(postList);
		testReadPostById(postList);
		testUpdatePostSuccess(postList);
		testUpdatePostMissing(postList);
		testDeletePostSuccess(postList);
		testDeletePostMissing(postList);

		testCreateReplySuccess(postList, replyList);
		testCreateReplyInvalidPostReference(postList, replyList);
		testUpdateReplyInvalidBody(replyList);
		testSubsetSearch(postList, replyList);

		System.out.println("\n=== Test Summary ===");
		System.out.println("Passed: " + passed);
		System.out.println("Failed: " + failed);
	}

	/**
	 * Test ID: T1 (maps to R1).
	 *
	 * <p>Creates a valid post and verifies no error is returned.
	 * PASS means create behavior accepts valid student input.
	 */
	private static void testCreatePostSuccess(PostList postList) {
		Post post = new Post(1, "Homework 2 CRUD question", "How should we store post subsets?", "CSE360", "student1", false);
		String error = postList.createPost(post);
		assertTrue(error == null, "Create post success", "Expected no error, got: " + error);
	}

	/**
	 * Test ID: T2 (maps to R2).
	 *
	 * <p>Attempts to create a post using an existing ID and verifies duplicate-ID rejection.
	 * PASS means uniqueness validation is enforced.
	 */
	private static void testCreatePostDuplicateId(PostList postList) {
		Post duplicate = new Post(1, "Another title", "Duplicate ID should fail", "CSE360", "student2", false);
		String error = postList.createPost(duplicate);
		assertTrue("Post ID already exists.".equals(error), "Create duplicate post ID", "Expected duplicate ID message, got: " + error);
	}

	/**
	 * Test ID: T3 (maps to R3).
	 *
	 * <p>Attempts to create a post with a blank title and verifies validation error.
	 * PASS means title-required validation is enforced.
	 */
	private static void testCreatePostInvalidTitle(PostList postList) {
		Post badPost = new Post(2, "   ", "Body exists", "CSE360", "student3", false);
		String error = postList.createPost(badPost);
		assertTrue("Post title is required.".equals(error), "Create post invalid title", "Expected title required message, got: " + error);
	}

	/**
	 * Test ID: T4 (maps to R4).
	 *
	 * <p>Reads an existing post by ID and verifies a non-null result.
	 * PASS means read-by-id behavior returns expected record.
	 */
	private static void testReadPostById(PostList postList) {
		Post found = postList.readPostById(1);
		assertTrue(found != null, "Read post by ID", "Expected post with ID 1");
	}

	/**
	 * Test ID: T5 (maps to R5).
	 *
	 * <p>Updates an existing post and verifies resolved state and error-free update.
	 * PASS means update writes valid values to target post.
	 */
	private static void testUpdatePostSuccess(PostList postList) {
		String error = postList.updatePost(1, "Homework 2 CRUD answer", "Use one list for all and one for subset.", "CSE360", true);
		Post updated = postList.readPostById(1);
		boolean ok = error == null && updated != null && updated.isResolved();
		assertTrue(ok, "Update post success", "Expected successful update");
	}

	/**
	 * Test ID: T6 (maps to R6).
	 *
	 * <p>Attempts update on a non-existent post ID and verifies missing-target message.
	 * PASS means update correctly rejects unknown IDs.
	 */
	private static void testUpdatePostMissing(PostList postList) {
		String error = postList.updatePost(999, "Missing", "Missing", "CSE360", false);
		assertTrue("Post not found for update.".equals(error), "Update missing post", "Expected missing post message, got: " + error);
	}

	/**
	 * Test ID: T7 (maps to R7).
	 *
	 * <p>Creates then deletes a post and verifies delete returns true.
	 * PASS means delete removes an existing record.
	 */
	private static void testDeletePostSuccess(PostList postList) {
		Post temp = new Post(3, "Temporary", "Delete me", "CSE360", "student4", false);
		postList.createPost(temp);
		boolean deleted = postList.deletePost(3);
		assertTrue(deleted, "Delete post success", "Expected delete true");
	}

	/**
	 * Test ID: T8 (maps to R8).
	 *
	 * <p>Attempts delete on a non-existent ID and verifies return value is false.
	 * PASS means delete missing-target behavior is correct.
	 */
	private static void testDeletePostMissing(PostList postList) {
		boolean deleted = postList.deletePost(999);
		assertTrue(!deleted, "Delete post missing", "Expected delete false");
	}

	/**
	 * Test ID: T9 (maps to R9).
	 *
	 * <p>Creates a reply referencing an existing post and verifies no error.
	 * PASS means reply-create with valid post linkage is supported.
	 */
	private static void testCreateReplySuccess(PostList postList, ReplyList replyList) {
		Reply reply = new Reply(1, 1, "Store subset in a second list and refresh after search.", "staff01");
		String error = replyList.createReplyForPost(reply, postList);
		assertTrue(error == null, "Create reply success", "Expected no error, got: " + error);
	}

	/**
	 * Test ID: T10 (maps to R10).
	 *
	 * <p>Attempts reply creation with an invalid post ID and verifies rejection.
	 * PASS means referential integrity is enforced for replies.
	 */
	private static void testCreateReplyInvalidPostReference(PostList postList, ReplyList replyList) {
		Reply badReply = new Reply(2, 999, "This should fail because post does not exist.", "staff02");
		String error = replyList.createReplyForPost(badReply, postList);
		assertTrue("Reply must reference an existing post.".equals(error), "Create reply invalid post", "Expected missing-post message, got: " + error);
	}

	/**
	 * Test ID: T11 (maps to R11).
	 *
	 * <p>Attempts to update a reply with a blank body and verifies validation error.
	 * PASS means reply-body validation is enforced on update.
	 */
	private static void testUpdateReplyInvalidBody(ReplyList replyList) {
		String error = replyList.updateReply(1, "   ");
		assertTrue("Reply body is required.".equals(error), "Update reply invalid body", "Expected reply body required message, got: " + error);
	}

	/**
	 * Test ID: T12 (maps to R12).
	 *
	 * <p>Builds subset views for posts/replies and verifies expected subset sizes.
	 * PASS means search/filter subset mechanics are functioning.
	 */
	private static void testSubsetSearch(PostList postList, ReplyList replyList) {
		postList.createPost(new Post(4, "Project setup", "How do I run maven test?", "CSE360", "student5", false));
		postList.setSubsetFromKeyword("maven");

		replyList.createReplyForPost(new Reply(3, 4, "Use mvn test in the project root.", "staff03"), postList);
		replyList.setSubsetFromPostId(4);

		boolean ok = postList.readSubsetPosts().size() == 1 && replyList.readSubsetReplies().size() == 1;
		assertTrue(ok, "Subset search", "Expected one post and one reply in subset");
	}

	/**
	 * Assertion helper used by all built-in tests.
	 *
	 * @param condition test condition to verify
	 * @param testName display name of test case
	 * @param failureDetails message shown on failure
	 */
	private static void assertTrue(boolean condition, String testName, String failureDetails) {
		if (condition) {
			passed++;
			System.out.println("PASS: " + testName);
		} else {
			failed++;
			System.out.println("FAIL: " + testName + " -> " + failureDetails);
		}
	}
}
