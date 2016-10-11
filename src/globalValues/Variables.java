package globalValues;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;


public class Variables {
	public static String LANGUAGE;
	public static String COUNTRY;
	public static ResourceBundle BUNDLE;
	private static CallableStatement stmt;
	private static ResultSet rs;
	
	public static void initBundle(){
		try {
			stmt = DBConnection.con.prepareCall("{call spSelectLanguage()}");
			stmt.execute();
			rs = stmt.getResultSet();
			rs.next();
			LANGUAGE = rs.getString("lang");
			COUNTRY = rs.getString("country");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		switch (LANGUAGE){
			case  "en":
				BUNDLE = ResourceBundle.getBundle("globalValues.Resources.Bundle_EN");
				break;
			case "es":
				BUNDLE = ResourceBundle.getBundle("globalValues.Resources.Bundle_ES");
				break;
			default:
				BUNDLE = ResourceBundle.getBundle("globalValues.Resources.Bundle_EN");
				break;
		}
		
		
	}
	
	public static void initBundle(String language, String country){
		LANGUAGE = language;
		COUNTRY = country;
		Locale l = new Locale(LANGUAGE, COUNTRY);
		BUNDLE = ResourceBundle.getBundle("globalValues.Resources.Bundle", l);
	}
	
	private Variables(){
		//prevents to instantiate this class by reflection
		throw new RuntimeException();
	}
}
