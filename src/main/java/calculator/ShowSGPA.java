package calculator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ShowSGPA {
	boolean showSGPA(Connection con) {
		String query = "SELECT Semester, SGPA FROM sgpa_table";
		try(PreparedStatement ps = con.prepareStatement(query)) {
			ResultSet rs = ps.executeQuery();
			System.out.println("Semseter: " + "SGPA");
			System.out.println("-".repeat(14));
			while(rs.next()) {
				float sgpa = rs.getFloat("SGPA");
				System.out.printf("%-8s: ", rs.getString("Semester"));
				if(sgpa == 0) {
					System.out.println();
					continue;
				}
				System.out.println(sgpa);
			}
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
}
