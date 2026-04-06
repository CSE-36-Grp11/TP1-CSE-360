package entityClasses;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PostListTest {

    private PostList postList;

    @BeforeEach
    void setUp() {
        postList = new PostList();
    }

    @Test
    void createPost_rejectsNullAndDuplicateId() {
        assertEquals("Post is required.", postList.createPost(null));

        Post p1 = new Post(1, "Title", "Body", "CSE360", "student1", false);
        assertNull(postList.createPost(p1));

        Post duplicate = new Post(1, "Other", "Other Body", "CSE360", "student2", false);
        assertEquals("Post ID already exists.", postList.createPost(duplicate));
    }

    @Test
    void createPost_rejectsInvalidAuthor() {
        Post post = new Post(2, "Valid Title", "Valid body", "CSE360", "bad user", false);
        assertEquals("Post author: ASU User ID cannot contain spaces.", postList.createPost(post));
    }

    @Test
    void createPost_sanitizesMaliciousScriptInput() {
        Post post = new Post(20, "<script>bad</script>", "Body <script>alert('x')</script>",
                "CSE360", "student20", false);

        assertNull(postList.createPost(post));

        Post saved = postList.readPostById(20);
        assertNotNull(saved);
        assertEquals("&lt;script&gt;bad&lt;/script&gt;", saved.getTitle());
        assertEquals("Body &lt;script&gt;alert(&#39;x&#39;)&lt;/script&gt;", saved.getBody());
    }

    @Test
    void updatePost_trimsValuesAndSetsResolved() {
        Post post = new Post(3, "Original", "Original body", "CSE360", "student3", false);
        assertNull(postList.createPost(post));

        String error = postList.updatePost(3, "  Updated  ", "  New body  ", "  cse360  ", true);
        assertNull(error);

        Post updated = postList.readPostById(3);
        assertNotNull(updated);
        assertEquals("Updated", updated.getTitle());
        assertEquals("New body", updated.getBody());
        assertEquals("cse360", updated.getCourseTag());
        assertTrue(updated.isResolved());
    }

    @Test
    void deletePost_returnsExpectedBoolean() {
        assertFalse(postList.deletePost(404));

        Post post = new Post(4, "Delete", "Target", "CSE360", "student4", false);
        assertNull(postList.createPost(post));
        assertTrue(postList.deletePost(4));
    }

    @Test
    void setSubsetFromKeyword_supportsCaseInsensitiveSearch() {
        postList.createPost(new Post(5, "Maven setup", "How to run tests", "CSE360", "student5", false));
        postList.createPost(new Post(6, "JavaFX", "UI scene graph", "CSE360", "student6", false));

        postList.setSubsetFromKeyword("MAVEN");
        List<Post> subset = postList.readSubsetPosts();
        assertEquals(1, subset.size());
        assertEquals(5, subset.get(0).getPostId());

        postList.setSubsetFromKeyword("   ");
        assertTrue(postList.readSubsetPosts().isEmpty());
    }
}
