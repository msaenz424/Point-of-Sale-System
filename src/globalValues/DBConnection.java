package globalValues;
import java.sql.*;

public class DBConnection {
	private static String connectionString = "jdbc:mysql://localhost:3306/POS";
	private static String connectionUser = "root";
	private static String connectionPassword = "hicetnunc";
	public static Connection con = null;
	public String userID = "";
	public static int selectedIDIndex = -1;
	
	
	private DBConnection(){
		throw new RuntimeException();
	}

	public static void connectDB(){		
		// TODO Auto-generated method stub
		try {
			con = DriverManager.getConnection(connectionString, connectionUser, connectionPassword);
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	public String getUserID(){
		return (userID);
	}
	
	public void setUserID(String userID){
		this.userID = userID;
	}

}

	
