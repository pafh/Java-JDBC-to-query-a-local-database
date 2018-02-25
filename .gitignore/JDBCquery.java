import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Scanner; 
public class PDBMassignment2 {
private static Scanner keyboard;
	 /**
	 */
	static
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch (ClassNotFoundException e)
		{
			System.err.println("Could not load MySql driver.");
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}
	public static void main (String args[])
	{
		String uname = "null";
		String psswrd = "null";
		int eventnr = 1; 
		int nbparticipants = 0;
		try
		{
			BufferedReader br1 = new BufferedReader (new InputStreamReader(System.in));
			System.out.print("Enter your username on MySql: ");
			uname = br1.readLine();

			BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Enter your password on MySql: ");
			psswrd = br2.readLine();
		}
		catch (IOException e)
		{
			System.out.print("Failed to get uname/passwd");
			System.out.println(":" + e.getMessage());
			System.exit(1);
		}	
		// do while loop
		do{
			keyboard = new Scanner(System.in);
			System.out.print("Please enter the event number: ");
			eventnr = keyboard.nextInt();
			if(eventnr>0)
			{
				String host = "jdbc:mysql://localhost/athletedb";
				/* Sample query */
				String query = "SELECT * "
						+ "FROM event "
						+ "WHERE eventID = "+eventnr+"";
				String query2 = "SELECT count(*) "
						+ "from participation_ind "
						+ "where eventID = "+eventnr+"";
				String query3 = "SELECT Fname, Lname, Performance_in_minutes "
						+ "FROM athlete A, participation_ind P "
						+ "WHERE A.athleteid = P.athleteid "
						+ "AND eventID = "+eventnr+" "
								+ "ORDER BY performance_in_minutes";
				String query4 = "SELECT fname, lname "
						+ "from participation_ind P, athlete A "
						+ "WHERE A.athleteID = P.athleteID "
						+ "AND eventID = ? "
						+ "AND sex = 'M' AND performance_in_minutes > 0 "
						+ "GROUP BY performance_in_minutes";
				String query5 = "SELECT fname, lname "
						+ "from participation_ind P, athlete A "
						+ "WHERE A.athleteID = P.athleteID "
						+ "AND eventID = ? "
						+ "AND sex = 'F' AND performance_in_minutes > 0 "
						+ "GROUP BY performance_in_minutes";
		
         
		/* Example of querying a database */
		try
		{
			/* Connect to MySql database */
			Connection conn = DriverManager.getConnection(host, uname, psswrd);
			System.out.println("Connection established...");
			System.out.println();
			/* Create statement */
			Statement stmt = conn.createStatement();
			/* Execute query */
			ResultSet rs = stmt.executeQuery(query);
			/* Output */
			while (rs.next())
			{
				String eventname = rs.getString(2);
				String eventlocation = rs.getString(3);
				String eventdate = rs.getString(4);
				System.out.println("The running event "+eventname+" is organized on "+eventdate+" at "+eventlocation+".");
				System.out.println("*******************************************************************************************");
				System.out.print(""+eventname+" has ");
			} 
			rs.close();
			stmt.close();
			/* Create statement */
			Statement stmt2 = conn.createStatement();
			// Execute query2 
			ResultSet rs2 = stmt2.executeQuery(query2);
			// Output 
			while (rs2.next())
			{
				nbparticipants = rs2.getInt(1);
				System.out.print(nbparticipants);
				System.out.println(" participants");
			} 
			rs2.close();
			stmt2.close();
			/* Create statement */	
			Statement stmt3 = conn.createStatement();
			/* Execute query3 */
			ResultSet rs3 = stmt3.executeQuery(query3);
			/* Output */
			System.out.println(" ");
			System.out.println("First Name // Last Name // Performance");
			System.out.println("------------------------------");
			while (rs3.next())
			{
				System.out.print(rs3.getString(1));
				System.out.print((" // "));
				System.out.print(rs3.getString(2));
				System.out.print((" // "));
				if(rs3.getString(3) == null){
					System.out.println("***");}
				else{
					System.out.println(rs3.getString(3) + "minutes");
			} 
			}
			rs3.close();
			stmt3.close();
			/* Create statement */
			Statement stmt6 = conn.createStatement();
			/* Execute the query */
			ResultSet rs6 = stmt6.executeQuery(query);
			/* Output */	
			while (rs6.next())
			{
				System.out.println(" ");
				String eventname = rs6.getString(2);
				System.out.print("The winner of "+eventname+" is ");
			}
			rs6.close();
			stmt6.close();
			try
			{
				/* Create prepared statement */
				PreparedStatement preparedStatement = conn.prepareStatement(query4);
				preparedStatement.setInt(1, eventnr);
				/* Execute the query */
				ResultSet rs4 = preparedStatement.executeQuery();
				/* Output */
				rs4.next();
				System.out.print(rs4.getString(1));
				System.out.print(" ");
				System.out.print(rs4.getString(2));
				System.out.print(" (men) and ");
				rs4.close();
				preparedStatement.close();	
			}
			catch (SQLException e)
			{
				System.out.println("*** (men) and ");
				System.out.println();
			}	
			try
			{
				/* Create prepared statement */
				PreparedStatement preparedStatement2 = conn.prepareStatement(query5);
				preparedStatement2.setInt(1, eventnr);
				/* Execute the query */
				ResultSet rs5 = preparedStatement2.executeQuery();
				/* Output */
				rs5.next();
				System.out.print(rs5.getString(1));
				System.out.print(" ");
				System.out.print(rs5.getString(2));
				System.out.println(" (women).");
				System.out.println();
				rs5.close();
				preparedStatement2.close();
			}
			catch (SQLException e)
			{
				System.out.println("*** (women).");
				System.out.println();
			}
			conn.close();
			}
			catch (SQLException e)
			{
				System.out.println("SQL Exception: ");
				System.err.println(e.getMessage());
			}
			}
		}
		while (eventnr>0);
		System.out.println();
		System.out.println("END OF SESSION");
	}
}

