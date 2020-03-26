package xmlWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

/**
 * Servlet implementation class FindIDsByRequestID
 */
@WebServlet("/FindIDsByRequestID")
public class FindIDsByRequestID extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger LOGGER;
	static Connection db_con_obj = null;
	static PreparedStatement db_prep_obj = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FindIDsByRequestID() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    private boolean makeJDBCConnection(JSONObject credentials) {
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
			db_con_obj = DriverManager.getConnection("jdbc:mysql://"+credentials.getString("dbserver")+":"+credentials.getString("dbport")+"/"+credentials.getString("dbarea")+"?autoReconnect=true&useSSL=false",credentials.getString("dbuname"),credentials.getString("dbupass"));
			//db_con_obj = DriverManager.getConnection("jdbc:mysql://192.168.50.6:3306/seltool", "iccs", "11iccs22!!");
			//db_con_obj = DriverManager.getConnection("jdbc:mysql://ponte.grid.ece.ntua.gr:3306/HarmonicSS-Patient-Selection-DB", "emps", "emps");
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

    private JSONObject getCredentials(String mycohort, String username, String password) {
    	JSONObject credentials = new JSONObject();
    	int mycohortid = Integer.valueOf(mycohort);
    	try {
	        String webPage = "https://private.harmonicss.eu/index.php/apps/coh/api/1.0/cohortid?id="+mycohortid;

	        String authString = username + ":" + password;
	        //System.out.println("auth string: " + authString);
	        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
	        String authStringEnc = new String(authEncBytes);
	        //System.out.println("Base64 encoded auth string: " + authStringEnc);

	        URL url = new URL(webPage);
	        URLConnection urlConnection = url.openConnection();
	        urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
	        InputStream is = urlConnection.getInputStream();
	        InputStreamReader isr = new InputStreamReader(is);

	        int numCharsRead;
	        char[] charArray = new char[1024];
	        StringBuffer sb = new StringBuffer();
	        while ((numCharsRead = isr.read(charArray)) > 0) {
	            sb.append(charArray, 0, numCharsRead);
	        }
	        String result = sb.toString();
	        JSONArray jsonarray = new JSONArray(result);
	        credentials = jsonarray.getJSONObject(0);
	        /*System.out.println("*** BEGIN ***");
	        System.out.println(result);
	        System.out.println("*** END ***");*/
	    } catch (MalformedURLException e) {
	    	LOGGER.log(Level.SEVERE,"MalformedURLException, check url of service to get credentials.",true);
	        e.printStackTrace();
	    } catch (IOException e) {
	    	LOGGER.log(Level.SEVERE,"IOException, check http response of service to get credentials.",true);
	        e.printStackTrace();
	    }
    	return credentials;
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		LOGGER = Util_Logger.Initialize_logger(getServletContext().getRealPath("/WEB-INF/LogFile.log"));
		JSONObject testResponse = new JSONObject();
		PatientIDsClass req = new Gson().fromJson(request.getReader(), PatientIDsClass.class);
		JSONObject credentials = getCredentials(req.mycohort, req.username, req.password);
		//ConfigureFile obj = new ConfigureFile("jdbc:mysql://"+credentials.getString("dbserver")+":"+credentials.getString("dbport")+"/"+credentials.getString("dbarea")+"?autoReconnect=true&useSSL=false",credentials.getString("dbuname"),credentials.getString("dbupass"));
		if(!makeJDBCConnection(credentials)) {
			System.out.println("Connection with the Database failed. Check the Credentials and the DB URL.");
			testResponse.put("errorMessage", "An error occured while connecting to database. Check your database credentials and try again.");
		}
		else {
			String query = "SELECT * FROM Eligible_Patient_IDs";
			if(!req.requestID.equals("null") && !req.requestID.trim().equals("")) {
				String[] requestIDs = req.requestID.trim().split(",");
				for(int i=0; i<requestIDs.length; i++) {
					if(!requestIDs[i].equals("null") && !requestIDs[i].trim().equals("")) {
						if(i==0) query += " WHERE DAR_ID IN ("+requestIDs[i].trim();
						else query += ","+requestIDs[i].trim();
					}
				}
				query += ")";
			}
			query += " ORDER BY DAR_ID";
			try {
				db_prep_obj = db_con_obj.prepareStatement(query);
				ResultSet rs = db_prep_obj.executeQuery();
				
				List<JSONObject> listJSONobj = new ArrayList<JSONObject>();
				while (rs.next()) {
					JSONObject myjson = new JSONObject();
					myjson.put("request_id", rs.getString("DAR_ID"));
					myjson.put("execution_date", rs.getString("EXEC_DATE").replace("\t","").replace("\n", "").replace("\r", ""));
					String patIDs = rs.getString("PATIENT_IDS");
					if(!patIDs.equals("null") && !patIDs.trim().equals("")) 
						myjson.put("patient_IDs", patIDs.replace("\"","").replace(",",", ").split("\\[")[1].split("\\]")[0]);
					else myjson.put("patient_IDs", "");
					listJSONobj.add(myjson);
				}
				testResponse.put("patient_IDs_list", listJSONobj);
				response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				pw.print(testResponse.toString());
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
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				testResponse.put("errorMessage","Error while retrieving patient IDs. Please check your request IDs and try again.");
				e.printStackTrace();
			}
		}
	}

}
