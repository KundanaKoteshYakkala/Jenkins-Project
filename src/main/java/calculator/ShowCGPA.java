package calculator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShowCGPA {
	void showCGPA(Connection con) {
		float sumTotalCreditsSGPA = 0.0f;
		float sumTotalCredits = 0.0f;
		try(PreparedStatement ps = con.prepareStatement("SELECT SUM(TotalCredits*SGPA) Summation FROM sgpa_table");
			ResultSet rs = ps.executeQuery();){
			while(rs.next()) {
				sumTotalCreditsSGPA = rs.getFloat("Summation");
			}
		}
		catch(SQLException e) {
			System.out.println("Error occured while fetching CGPA! Please try again!");
		}
		
		try(PreparedStatement ps = con.prepareStatement("SELECT SUM(TotalCredits) SumTotalCredits FROM sgpa_table");
			ResultSet rs = ps.executeQuery()){
			while(rs.next()) {
				sumTotalCredits = rs.getFloat("SumTotalCredits");
			}
		}
		catch(SQLException e) {
			System.out.println("Error occured while fetching CGPA! Please try again!");
		}
		
		if(sumTotalCredits == 0) System.out.println(0.0);
		else System.out.println(sumTotalCreditsSGPA / sumTotalCredits);
	}
}
