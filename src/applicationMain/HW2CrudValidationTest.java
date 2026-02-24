package applicationMain;

import entityClasses.Post;
import entityClasses.PostList;
import entityClasses.Reply;
import entityClasses.ReplyList;

public class HW2CrudValidationTest {

	private static int passed = 0;
	private static int failed = 0;

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

	private static void testCreatePostSuccess(PostList postList) {
		Post post = new Post(1, "Homework 2 CRUD question", "How should we store post subsets?", "CSE360", "student1", false);
		String error = postList.createPost(post);
		assertTrue(error == null, "Create post success", "Expected no error, got: " + error);
	}

	private static void testCreatePostDuplicateId(PostList postList) {
		Post duplicate = new Post(1, "Another title", "Duplicate ID should fail", "CSE360", "student2", false);
		String error = postList.createPost(duplicate);
		assertTrue("Post ID already exists.".equals(error), "Create duplicate post ID", "Expected duplicate ID message, got: " + error);
	}

	private static void testCreatePostInvalidTitle(PostList postList) {
		Post badPost = new Post(2, "   ", "Body exists", "CSE360", "student3", false);
		String error = postList.createPost(badPost);
		assertTrue("Post title is required.".equals(error), "Create post invalid title", "Expected title required message, got: " + error);
	}

	private static void testReadPostById(PostList postList) {
		Post found = postList.readPostById(1);
		assertTrue(found != null, "Read post by ID", "Expected post with ID 1");
	}

	private static void testUpdatePostSuccess(PostList postList) {
		String error = postList.updatePost(1, "Homework 2 CRUD answer", "Use one list for all and one for subset.", "CSE360", true);
		Post updated = postList.readPostById(1);
		boolean ok = error == null && updated != null && updated.isResolved();
		assertTrue(ok, "Update post success", "Expected successful update");
	}

	private static void testUpdatePostMissing(PostList postList) {
		String error = postList.updatePost(999, "Missing", "Missing", "CSE360", false);
		assertTrue("Post not found for update.".equals(error), "Update missing post", "Expected missing post message, got: " + error);
	}

	private static void testDeletePostSuccess(PostList postList) {
		Post temp = new Post(3, "Temporary", "Delete me", "CSE360", "student4", false);
		postList.createPost(temp);
		boolean deleted = postList.deletePost(3);
		assertTrue(deleted, "Delete post success", "Expected delete true");
	}

	private static void testDeletePostMissing(PostList postList) {
		boolean deleted = postList.deletePost(999);
		assertTrue(!deleted, "Delete post missing", "Expected delete false");
	}

	private static void testCreateReplySuccess(PostList postList, ReplyList replyList) {
		Reply reply = new Reply(1, 1, "Store subset in a second list and refresh after search.", "staff01");
		String error = replyList.createReplyForPost(reply, postList);
		assertTrue(error == null, "Create reply success", "Expected no error, got: " + error);
	}

	private static void testCreateReplyInvalidPostReference(PostList postList, ReplyList replyList) {
		Reply badReply = new Reply(2, 999, "This should fail because post does not exist.", "staff02");
		String error = replyList.createReplyForPost(badReply, postList);
		assertTrue("Reply must reference an existing post.".equals(error), "Create reply invalid post", "Expected missing-post message, got: " + error);
	}

	private static void testUpdateReplyInvalidBody(ReplyList replyList) {
		String error = replyList.updateReply(1, "   ");
		assertTrue("Reply body is required.".equals(error), "Update reply invalid body", "Expected reply body required message, got: " + error);
	}

	private static void testSubsetSearch(PostList postList, ReplyList replyList) {
		postList.createPost(new Post(4, "Project setup", "How do I run maven test?", "CSE360", "student5", false));
		postList.setSubsetFromKeyword("maven");

		replyList.createReplyForPost(new Reply(3, 4, "Use mvn test in the project root.", "staff03"), postList);
		replyList.setSubsetFromPostId(4);

		boolean ok = postList.readSubsetPosts().size() == 1 && replyList.readSubsetReplies().size() == 1;
		assertTrue(ok, "Subset search", "Expected one post and one reply in subset");
	}

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
