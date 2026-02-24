package applicationMain;

/**
 * Test class to verify username validation behavior
 */
public class UsernameValidationTest {
    
    public static void main(String[] args) {
        System.out.println("=== Username Validation Tests ===\n");
        
        // Test 1: Empty username
        testValidation("", "Empty username");
        
        // Test 2: Username with only spaces
        testValidation("   ", "Spaces only");
        
        // Test 3: Username too short (less than 4 chars)
        testValidation("abc", "Too short (3 chars)");
        
        // Test 4: Username too long (more than 20 chars)
        testValidation("abcdefghijklmnopqrstuvwxyz", "Too long (26 chars)");
        
        // Test 5: Username with special characters
        testValidation("user@123", "Special char (@)");
        
        // Test 6: Username with spaces in middle
        testValidation("user name", "Space in middle");
        
        // Test 7: Username with leading spaces
        testValidation("  user123", "Leading spaces");
        
        // Test 8: Username with trailing spaces
        testValidation("user123  ", "Trailing spaces");
        
        // Test 9: Valid username (4 chars)
        testValidation("user", "Valid (4 chars)");
        
        // Test 10: Valid username (20 chars)
        testValidation("abcdefghij1234567890", "Valid (20 chars)");
        
        // Test 11: Valid username (alphanumeric)
        testValidation("User123", "Valid (alphanumeric)");
        
        // Test 12: Valid username with numbers only
        testValidation("12345678", "Valid (numbers only)");
        
        // Test 13: Valid username with letters only
        testValidation("username", "Valid (letters only)");
        
        System.out.println("\n=== All tests completed ===");
    }
    
    private static void testValidation(String username, String testDescription) {
        String error = ValidationUtil.validateAsuUserId(username);
        
        if (error != null) {
            System.out.println("❌ FAIL: " + testDescription);
            System.out.println("   Input: \"" + username + "\"");
            System.out.println("   Error: " + error);
        } else {
            System.out.println("✅ PASS: " + testDescription);
            System.out.println("   Input: \"" + username + "\"");
        }
        System.out.println();
    }
}
