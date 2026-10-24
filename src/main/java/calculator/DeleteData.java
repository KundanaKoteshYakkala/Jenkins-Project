package calculator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class DeleteData {
	boolean deleteData(Connection con, Scanner sc) {
		String semester = new InsertDetails().semesterInput(sc);
		try(PreparedStatement ps1 = con.prepareStatement("DELETE FROM " + semester);
			PreparedStatement ps2 = con.prepareStatement("UPDATE sgpa_table SET SGPA = null, TotalCredits = null WHERE Semester = ?")){
			 System.out.println(ps1.executeUpdate() + "rows deleted.");
			 ps2.setString(1, semester);
			 int sgpaRowsDeleted = ps2.executeUpdate();
			 if(sgpaRowsDeleted != 0) {
				 System.out.println("SGPA details of that semester are deleted.");
			 }
			 return true;
		}
		catch(SQLException e) {
			System.out.println("Error occured while deleting! Please try again!");
			return false;
		}
	}
}
