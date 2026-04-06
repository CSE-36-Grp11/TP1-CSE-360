package applicationMain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ValidationUtilTest {

    @Test
    void validateAsuUserId_boundaries() {
        assertEquals("ASU User ID must be 4–20 characters.", ValidationUtil.validateAsuUserId("abc"));
        assertNull(ValidationUtil.validateAsuUserId("abcd"));
        assertNull(ValidationUtil.validateAsuUserId("abcdefghijklmnopqrst"));
        assertEquals("ASU User ID must be 4–20 characters.", ValidationUtil.validateAsuUserId("abcdefghijklmnopqrstu"));
    }

    @Test
    void validateAsuUserId_rejectsSpacesAndSymbols() {
        assertEquals("ASU User ID cannot contain spaces.", ValidationUtil.validateAsuUserId("user name"));
        assertEquals("ASU User ID can only contain letters and numbers.", ValidationUtil.validateAsuUserId("user@123"));
    }

    @Test
    void validateAsuUserId_rejectsScriptInjectionName() {
        assertEquals("ASU User ID can only contain letters and numbers.",
                ValidationUtil.validateAsuUserId("<script>BadCode</script>"));
    }

    @Test
    void validateEmail_positiveAndNegativeCases() {
        assertNull(ValidationUtil.validateEmail("student@asu.edu"));
        assertEquals("Please enter a valid email address with one @.", ValidationUtil.validateEmail("student@@asu.edu"));
        assertEquals("Please enter a valid email address with one @.", ValidationUtil.validateEmail(" student @asu.edu "));
    }

    @Test
    void validatePhoneNumber_boundaries() {
        assertEquals("Phone number must be a valid 10-digit number.", ValidationUtil.validatePhoneNumber("123456789"));
        assertNull(ValidationUtil.validatePhoneNumber("1234567890"));
        assertEquals("Phone number must be a valid 10-digit number.", ValidationUtil.validatePhoneNumber("12345678901"));
        assertEquals("Phone number can only contain numbers.", ValidationUtil.validatePhoneNumber("12345abcde"));
    }

    @Test
    void validateCourseTag_formatRules() {
        assertNull(ValidationUtil.validateCourseTag("CSE360"));
        assertEquals("Course tag must look like CSE360.", ValidationUtil.validateCourseTag("CSE36"));
        assertEquals("Course tag must look like CSE360.", ValidationUtil.validateCourseTag("CSE-360"));
    }
}
