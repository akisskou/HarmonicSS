

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;


/**
 * Servlet implementation class ReqRespList
 */
@WebServlet("/ReqRespList")
public class ReqRespList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Connection db_con_obj = null;
	static PreparedStatement db_prep_obj = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReqRespList() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    private boolean makeJDBCConnection() {
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
			//System.out.println("URL: "+configureFile_obj.getDbURL()+" username: "+configureFile_obj.getUsername()+" password: "+configureFile_obj.getPassword());
			db_con_obj = DriverManager.getConnection("jdbc:mysql://ponte.grid.ece.ntua.gr:3306/HarmonicSS-Patient-Selection-DB", "emps", "emps");
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

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		if(!makeJDBCConnection())  System.out.println("Connection with the ponte database failed. Check the Credentials and the DB URL.");
    	else System.out.println("I am ponte and I'm gooooooood");
		
		String query = "SELECT * FROM EXECUTION_DATA";
		try {
			db_prep_obj = db_con_obj.prepareStatement(query);
			ResultSet rs = db_prep_obj.executeQuery();
			List<JSONObject> listJSONobj = new ArrayList<JSONObject>();
			while (rs.next()) {
				JSONObject myjson = new JSONObject();
				myjson.put("id", rs.getString("ID"));
				myjson.put("user_id", rs.getString("USER_ID"));
				myjson.put("request_id", rs.getString("REQUEST_ID"));
				myjson.put("request_XML", rs.getString("REQUEST_XML").replace("\t","").replace("\n", "").replace("\r", ""));
				myjson.put("execution_date", rs.getString("EXECUTION_DATE").replace("\t","").replace("\n", "").replace("\r", ""));
				myjson.put("response_XML", rs.getString("RESPONSE_XML").replace("\t","").replace("\n", "").replace("\r", ""));
				listJSONobj.add(myjson);
			}
			JSONObject result = new JSONObject();
			result.put("request_response_list", listJSONobj);
			response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			PrintWriter pw = response.getWriter();
			pw.print(listJSONobj.toString());
			pw.close();
			if (rs != null) {
	            rs.close();
	        }
	        if (db_prep_obj != null) {
	        	db_prep_obj.close();
	        }
	        if (db_con_obj != null) {
	        	db_con_obj.close();
	        }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
	}

}
