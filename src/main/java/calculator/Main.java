package calculator;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
	private static final Scanner sc = new Scanner(System.in);
	public static void main(String[] args) {
		Connection con = null;
		try {
			String url = "jdbc:mysql://localhost:3306/SGPA_DB";
			String user = "root";
			String pass = "Kundan@2004";
			con = DriverManager.getConnection(url, user, pass);
			con.setAutoCommit(false);
			
			System.out.println("Successfully connected to database.");
		}
		catch(SQLException e) {
			e.printStackTrace();
			System.out.println("Unsuccessful connecting to database!");
			sc.close();
			return;
		}
		
		
		
		while(true) {
			System.out.println("Choose an option:");
			System.out.println("1. Insert Details\n"
								+ "2. Show SGPA\n"
								+ "3. Show CGPA\n"
								+ "4. Delete Data\n"
								+ "Any other number to close");
			
			int choice = sc.nextInt();
			sc.nextLine();
			
			if(choice == 1) {
				boolean result = new InsertDetails().insertData(con, sc);
				if(result) {
					System.out.println("Subjects details are updated.");
					try {con.commit();}
					catch(Exception e) {e.printStackTrace();}
				}
				else {
					System.out.println("Error occured while updating subjects details!");
					try {con.rollback();}
					catch(SQLException e) {e.printStackTrace();}
				}
			}
			else if(choice == 2) {
				boolean result = new ShowSGPA().showSGPA(con);
				if(!result) System.out.println("Error occured while fetching SGPA details!");
			}
			else if(choice == 3) {
				new ShowCGPA().showCGPA(con);
			}
			else if(choice == 4) {
				boolean result = new DeleteData().deleteData(con, sc);
				if(result) {
					try {con.commit();}
					catch(Exception e) {e.printStackTrace();}
				}
				else {
					try{con.rollback();}
					catch(Exception e) {e.printStackTrace();}
				}
			}
			else {
				break;
			}
		}
		
		try {
			con.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		sc.close();
	}
}
