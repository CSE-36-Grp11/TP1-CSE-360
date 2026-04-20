#!/bin/bash

# Add Javadoc to GradingService
sed -i '' -e '/public class GradingService/i\
/**\
 * Service class for grading student discussions based on their replies.\
 * <p>\
 * JUnit tests used: {@link applicationMain.GradingServiceTest}\
 * \nSemi-automated tests used: N/A\
 * \nManual tests used: GradingService Manual Tests (see Manual Tests.pdf)\
 * </p>\
 */\
' src/applicationMain/GradingService.java

# Add Javadoc to ControllerGradebook
sed -i '' -e '/public class ControllerGradebook/i\
/**\
 * Controller for the Teacher Gradebook.\
 * <p>\
 * JUnit tests used: {@link applicationMain.FoundationsS26Test}\
 * \nSemi-automated tests used: N/A\
 * \nManual tests used: Gradebook UI Manual Testing, 3-Unique Reply UI Testing (see Manual Tests.pdf)\
 * </p>\
 */\
' src/guiGradebook/ControllerGradebook.java

# Add Javadoc to ModelGradebook
sed -i '' -e '/public class ModelGradebook/i\
/**\
 * Model for the Teacher Gradebook.\
 * <p>\
 * JUnit tests used: {@link applicationMain.GradingServiceTest}, {@link applicationMain.FoundationsS26Test}\
 * \nSemi-automated tests used: N/A\
 * \nManual tests used: Gradebook Logic Manual Testing (see Manual Tests.pdf)\
 * </p>\
 */\
' src/guiGradebook/ModelGradebook.java

# Add Javadoc to ViewGradebook
sed -i '' -e '/public class ViewGradebook/i\
/**\
 * View for the Teacher Gradebook showing student participation.\
 * <p>\
 * JUnit tests used: {@link applicationMain.FoundationsS26Test}\
 * \nSemi-automated tests used: N/A\
 * \nManual tests used: ViewGradebook Display Tests (see Manual Tests.pdf)\
 * </p>\
 */\
' src/guiGradebook/ViewGradebook.java


# Add Javadoc to ValidationUtil
sed -i '' -e '/public class ValidationUtil/i\
/**\
 * Utility class for validating and sanitizing script inputs.\
 * <p>\
 * JUnit tests used: {@link applicationMain.ValidationUtilTest}\
 * \nSemi-automated tests used: In-built test cases (HW2CrudValidationTest, UsernameValidationTest)\
 * \nManual tests used: Scripting constraints manual testing (see Manual Tests.pdf)\
 * </p>\
 */\
' src/applicationMain/ValidationUtil.java

# Add Javadoc to PostList
sed -i '' -e '/public class PostList/i\
/**\
 * Entity representing a list of posts, including logic to prevent script injection.\
 * <p>\
 * JUnit tests used: {@link entityClasses.PostListTest}\
 * \nSemi-automated tests used: In-built test cases (HW2CrudValidationTest)\
 * \nManual tests used: Post List Logic Manual Testing (see Manual Tests.pdf)\
 * </p>\
 */\
' src/entityClasses/PostList.java

