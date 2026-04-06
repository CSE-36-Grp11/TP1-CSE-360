package entityClasses;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReplyListTest {

    private ReplyList replyList;
    private PostList postList;

    @BeforeEach
    void setUp() {
        replyList = new ReplyList();
        postList = new PostList();
        postList.createPost(new Post(10, "Question", "Need help", "CSE360", "student10", false));
    }

    @Test
    void createReplyForPost_rollsBackWhenPostMissing() {
        Reply orphan = new Reply(1, 999, "I can answer this", "staff01");
        assertEquals("Reply must reference an existing post.", replyList.createReplyForPost(orphan, postList));
        assertTrue(replyList.readAllReplies().isEmpty());
    }

    @Test
    void createReply_rejectsDuplicateId() {
        Reply first = new Reply(2, 10, "First answer", "staff01");
        assertNull(replyList.createReplyForPost(first, postList));

        Reply duplicate = new Reply(2, 10, "Duplicate id", "staff02");
        assertEquals("Reply ID already exists.", replyList.createReplyForPost(duplicate, postList));
    }

    @Test
    void updateReply_validatesAndTrimsBody() {
        Reply reply = new Reply(3, 10, "Initial", "staff03");
        assertNull(replyList.createReplyForPost(reply, postList));

        assertEquals("Reply body is required.", replyList.updateReply(3, "   "));
        assertNull(replyList.updateReply(3, "  Updated reply  "));
        assertEquals("Updated reply", replyList.readReplyById(3).getBody());
    }

    @Test
    void deleteReply_returnsExpectedBoolean() {
        assertFalse(replyList.deleteReply(999));

        Reply reply = new Reply(4, 10, "Delete me", "staff04");
        assertNull(replyList.createReplyForPost(reply, postList));
        assertTrue(replyList.deleteReply(4));
    }

    @Test
    void subsetKeywordFiltersRepliesCaseInsensitive() {
        replyList.createReplyForPost(new Reply(5, 10, "Use Maven test", "staff05"), postList);
        replyList.createReplyForPost(new Reply(6, 10, "Use JavaFX controls", "staff06"), postList);

        replyList.setSubsetFromKeyword("maven");
        List<Reply> subset = replyList.readSubsetReplies();
        assertEquals(1, subset.size());
        assertEquals(5, subset.get(0).getReplyId());

        replyList.clearSubset();
        assertTrue(replyList.readSubsetReplies().isEmpty());
    }
}
