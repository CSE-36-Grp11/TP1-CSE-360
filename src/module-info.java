module FoundationsF25 {
	requires javafx.controls;
	requires java.sql;
	
	opens applicationMain to javafx.graphics, javafx.fxml;
	opens guiEmailRegister to javafx.graphics, javafx.fxml;
	opens guiEmailLogin to javafx.graphics, javafx.fxml;
	opens guiStudentHome to javafx.graphics, javafx.fxml;
	opens guiStaffHome to javafx.graphics, javafx.fxml;
}
