package calculator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;


class InsertDetails {
	private static String[] semesters = {"sem_1_1", "sem_1_2", 
										 "sem_2_1", "sem_2_2", 
										 "sem_3_1", "sem_3_2", 
										 "sem_4_1", "sem_4_2"};
	private String semester;
	private int noOfSubjects;
	
	private String subCode, subName, grade;
	private float credits;
	
	boolean insertData(Connection con, Scanner sc) {
		
		semester = semesterInput(sc);
		noOfSubjects = noOfSubjectsInput(sc);
		
		
		
		String query = "insert into " + semester + "(SubjectCode, SubjectName, Credits, Grade) values (?, ?, ?, ?)";
		try{
			PreparedStatement ps = con.prepareStatement(query);
		
			while(noOfSubjects != 0) {
				
				subCode = subCodeInput(sc);
				subName = subNameInput(sc);
				credits = creditsInput(sc);
				grade = gradeInput(sc);
				
				ps.setString(1, subCode);
				ps.setString(2, subName);
				ps.setFloat(3, credits);
				ps.setString(4, grade);
				ps.executeUpdate();
				--noOfSubjects;
			}
			
			sgpaUpdate(con);
			con.commit();
			ps.close();
		}
		catch(SQLException e) {
			//e.printStackTrace();
			if(e.getMessage().equals("Check constraint 'Grade_Check_2_1' is violated.")) {
				System.out.println("You entered a invalid data for Grade! Please retry from start!");
			}
			else if(e.getMessage().equals("Check constraint 'Credits_Check_2_1' is violated.")) {
				System.out.println("You entered a invalid data for Credits! Please retry from start!");
			}
			else if(e.getMessage().equals("Duplicate entry '1' for key 'sem_2_1.PRIMARY'")) {
				System.out.println("The data you given is already available! Please recheck!");
			}
			else System.out.println(e.getMessage());
			return false;
		}
		
		return true;
	}
	
	String semesterInput(Scanner sc) {
		System.out.print("Enter the semester(1-8): ");
		while(true) {
			try {
				int sem = sc.nextInt() - 1;
				sc.nextLine();
				return semesters[sem];
			}
			catch(InputMismatchException e) {
				System.out.println("Enter a valid value!");
			}
			catch(ArrayIndexOutOfBoundsException e) {
				System.out.println("Enter a value within 1-8 range");
			}
			catch(Exception e) {
				System.out.println("Failed! Please re-enter!");
			}
		}
	}
	
	int noOfSubjectsInput(Scanner sc) {
		System.out.print("Enter no.of subjects: ");
		while(true) {
			try {
				int number = sc.nextInt();
				sc.nextLine();
				return number;
			}
			catch(InputMismatchException e) {
				System.out.println("Enter a valid value!");
			}
			catch(Exception e) {
				System.out.println("Failed! Please re-enter!");
			}
		}
	}
	
	String subCodeInput(Scanner sc) {
		System.out.print("Subject Code: ");
		String regex = "\\d*";
		String subCode;
		while(true) {
			subCode = sc.nextLine();
			if(subCode.matches(regex)) return subCode;
			else System.out.println("Enter a valid Subject Code!");
		}
	}
	
	String subNameInput(Scanner sc) {
		String regex = "(\\w||\\s){50}";
		String subName;
		System.out.print("Subject Name: ");
		while(true) {
			subName = sc.nextLine();
			if(subName.matches(regex)) return subName;
			else System.out.println("Enter a valid Subject Name!");
		}
	}
	
	float creditsInput(Scanner sc) {
		System.out.print("Credits: ");
		while(true) {
			try{
				float credits = sc.nextFloat();
				sc.nextLine();
				return credits;
			}
			catch(InputMismatchException e) {
				System.out.println("Enter a valid value!");
				sc.nextLine();
			}
			catch(Exception e) {
				System.out.println("Failed! Please re-enter!");
				sc.nextLine();
			}
		}
	}
	
	String gradeInput(Scanner sc) {
		System.out.print("Grade: ");
		return sc.nextLine();
	}
	
	void sgpaUpdate(Connection con) throws SQLException{
		float totalCredits = 0.0f; //Sum of credits
		float summation = 0.0f;  //Sum of(Credits*GradePoints) divided by TotalCredits
		
		PreparedStatement ps = con.prepareStatement("SELECT SUM(Credits) TotalCredits FROM " + semester);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) totalCredits = rs.getFloat("TotalCredits");
		
		ps = con.prepareStatement("SELECT SUM(Credits * GradePoints) Summation FROM " + semester);
		rs = ps.executeQuery();
		while(rs.next()) summation = rs.getFloat("Summation");
	
		ps = con.prepareStatement("UPDATE sgpa_table SET TotalCredits = ? WHERE Semester = ?");
		ps.setFloat(1, totalCredits);
		ps.setString(2, semester);
		ps.executeUpdate();
		
		ps = con.prepareStatement("UPDATE sgpa_table SET SGPA = ROUND(?, 2) WHERE Semester = ?");
		ps.setFloat(1, summation/totalCredits);
		ps.setString(2, semester);
		ps.executeUpdate();
		ps.close();
		
		System.out.println("SGPA Updated.");
	}
}
