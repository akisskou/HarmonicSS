package xmlWrapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;




public class DBServiceCRUD {
	static Connection db_con_obj = null;
	static PreparedStatement db_prep_obj = null;
	
	public static Boolean makeJDBCConnection(ConfigureFile configureFile_obj) {
		 Boolean connection_succes=true;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Congrats - Seems your MySQL JDBC Driver Registered!");
		} catch (ClassNotFoundException e) {
			System.out.println("Sorry, couldn't found JDBC driver. Make sure you have added JDBC Maven Dependency Correctly");
			e.printStackTrace();
			return false;
		}
 
		try {
			System.out.println("URL: "+configureFile_obj.getDbURL()+" username: "+configureFile_obj.getUsername()+" password: "+configureFile_obj.getPassword());
			db_con_obj = DriverManager.getConnection(configureFile_obj.getDbURL(), configureFile_obj.getUsername(), configureFile_obj.getPassword());
			//db_con_obj = DriverManager.getConnection("jdbc:mysql://147.102.19.66:3306/HarmonicSS","ponte", "p0nt3");
			if (db_con_obj != null) {
				System.out.println("Connection Successful! Enjoy. Now it's time to Store data");
			} else {
				System.out.println("Failed to make connection!");
				return false;
			}
		} catch (SQLException e) {
			System.out.println("MySQL Connection Failed!");
			e.printStackTrace();
			return false;
		}
		return connection_succes;
	}
	
	public static boolean testQuery(String getQueryStatement) throws SQLException {
		String query_results=""; 
		
			System.out.println("Stage-1 Prepare statement.");
			db_prep_obj = db_con_obj.prepareStatement(getQueryStatement);
			
			System.out.println("Stage-2 Ready to execute the query.");
			// Execute the Query, and get a java ResultSet
			
			ResultSet rs = db_prep_obj.executeQuery();
			//System.out.println("We are ready to retrieve data from DB.");
			
			System.out.println("Stage-3 The query is executed.");
			// Let's iterate through the java ResultSet
			if (rs == null) return false;
			
			return true;
	}
	
	public static String getDataFromDB(String getQueryStatement) throws SQLException {
		String query_results=""; 
		
			System.out.println("Stage-1 Prepare statement.");
			db_prep_obj = db_con_obj.prepareStatement(getQueryStatement);
			
			System.out.println("Stage-2 Ready to execute the query.");
			// Execute the Query, and get a java ResultSet
			
			ResultSet rs = db_prep_obj.executeQuery();
			//System.out.println("We are ready to retrieve data from DB.");
			
			System.out.println("Stage-3 The query is executed.");
			// Let's iterate through the java ResultSet
			if (rs == null) return "";
			while (rs.next()) {
				//System.out.println("Stage-4 We retrieve the IDs one by one.");
				String id = rs.getString("UID");//.getInt("ID");
				//Float value = rs.getFloat("VALUE");//  .getString("Address");
				
				query_results+=" "+id;//Integer.toString(id) + " ";//+Float.toString(value)+". ";
				
			}System.out.println("Stage-5 Finished.");
			return query_results;
	}
	
	public static void getXMLRequestFromDB(String requestID) throws SQLException, IOException {
		String query = "SELECT REQUEST_XML FROM EXECUTION_DATA WHERE REQUEST_ID='" + requestID +"'";
		db_prep_obj = db_con_obj.prepareStatement(query);
		ResultSet rs = db_prep_obj.executeQuery();
		//File fXmlFile = new File(requestID+".xml");
		/*FileOutputStream fos = new FileOutputStream(fXmlFile);
		System.out.println("Writing BLOB to file " + fXmlFile.getAbsolutePath());
        while (rs.next()) {
            InputStream input = rs.getBinaryStream("REQUEST_XML");
            byte[] buffer = new byte[1024];
            while (input.read(buffer) > 0) {
                fos.write(buffer);
            }
        }*/
		FileWriter fw = new FileWriter(requestID + ".xml");
		 while (rs.next()) {
			 fw.write(rs.getString("REQUEST_XML"));
			 fw.close();
		 }
		
        if (rs != null) {
            rs.close();
        }
        if (db_prep_obj != null) {
        	db_prep_obj.close();
        }
        if (db_con_obj != null) {
        	db_con_obj.close();
        }
        /*if (fos != null) {
            fos.close();
        }*/
		//return fXmlFile;
	}
	
	
	public static void main(String [] args) {

	}
}
