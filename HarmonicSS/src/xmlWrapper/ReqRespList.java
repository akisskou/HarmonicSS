package xmlWrapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.json.JSONObject;
import org.ntua.criteria.CohortResponse;
import org.ntua.criteria.ObjectFactory;
import org.ntua.criteria.PatientsSelectionRequest;
import org.ntua.criteria.PatientsSelectionResponse;


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
		String userID = request.getParameter("userID");
		String requestID = request.getParameter("requestID");
		
		String query = "SELECT * FROM EXECUTION_DATA";
		if(userID.equals("null") || userID.trim().equals("")) {
			if(!requestID.equals("null") && !requestID.trim().equals("")) {
				String[] requestIDs = requestID.split(",");
				for(int i=0; i<requestIDs.length; i++) {
					if(!requestIDs[i].equals("null") && !requestIDs[i].trim().equals("")) {
						if(i==0) query += " WHERE (REQUEST_ID='" + requestIDs[i].trim() +"'";
						else query += " OR REQUEST_ID='" + requestIDs[i].trim() +"'";
					}
				}
				query += ")";
			}
		}
		else {
			String[] userIDs = userID.split(",");
			for(int i=0; i<userIDs.length; i++) {
				if(!userIDs[i].equals("null") && !userIDs[i].trim().equals("")) {
					if(i==0) query += " WHERE (USER_ID='" + userIDs[i].trim() +"'";
					else query += " OR USER_ID='" + userIDs[i].trim() +"'";
				}
			}
			query += ")";
			if(!requestID.equals("null") && !requestID.trim().equals("")) {
				String[] requestIDs = requestID.split(",");
				for(int i=0; i<requestIDs.length; i++) {
					if(!requestIDs[i].equals("null") && !requestIDs[i].trim().equals("")) {
						if(i==0) query += " AND (REQUEST_ID='" + requestIDs[i].trim() +"'";
						else query += " OR REQUEST_ID='" + requestIDs[i].trim() +"'";
					}
				}
				query += ")";
			}
		}
		try {
			db_prep_obj = db_con_obj.prepareStatement(query);
			ResultSet rs = db_prep_obj.executeQuery();
			List<JSONObject> listJSONobj = new ArrayList<JSONObject>();
			
			while (rs.next()) {
				JSONObject myjson = new JSONObject();
				myjson.put("user_id", rs.getString("USER_ID"));
				myjson.put("request_id", rs.getString("REQUEST_ID"));
				//myjson.put("request_XML", rs.getString("REQUEST_XML").replace("\t","").replace("\n", "").replace("\r", ""));
				String requestXML = rs.getString("REQUEST_XML").replace("&lt;", "<").replace("&gt;", ">").replace("&quot;", "\"");
				FileWriter fw = new FileWriter(getServletContext().getRealPath("/WEB-INF/tempRequest.xml"));
			    fw.write(requestXML);
			    fw.close();
			    /*String responseXML = rs.getString("RESPONSE_XML").replace("&lt;", "<").replace("&gt;", ">").replace("&quot;", "\"");
			    fw = new FileWriter(getServletContext().getRealPath("/WEB-INF/tempResponse.xml"));
			    fw.write(responseXML);
				fw.close();*/
				File myXMLRequest = new File(getServletContext().getRealPath("/WEB-INF/tempRequest.xml"));
				JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
	  	  		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	  	  		PatientsSelectionRequest patientsSelectionRequest = ((JAXBElement<PatientsSelectionRequest>) jaxbUnmarshaller.unmarshal(myXMLRequest)).getValue();
	  	  		String result_incl = "Inclusion Criteria:<br>";
	  	  		String result_excl = "Exclusion Criteria:<br>";
	  	  		for(org.ntua.criteria.Criterion inclCriterion: patientsSelectionRequest.getEligibilityCriteria().getInclusionCriteria().getCriterion()){
	  	  			result_incl+=inclCriterion.getDescription()+": "+inclCriterion.getFormalExpression().get(0).getBooleanExpression().trim()+"<br>";
	  	  		}
	  	  		for(org.ntua.criteria.Criterion exclCriterion: patientsSelectionRequest.getEligibilityCriteria().getExclusionCriteria().getCriterion()){
	  	  			result_excl+=exclCriterion.getDescription()+": "+exclCriterion.getFormalExpression().get(0).getBooleanExpression().trim()+"<br>";
	  	  		}
	  	  		String cohorts = "Cohorts:<br>";
	  	  		List<String> cohortList = patientsSelectionRequest.getCohortID();
	  	  		for(String myCohort : cohortList) {
	  	  			//System.out.println("Cohort with id="+myCohort.split("-")[2]+" updated successfully.");
	  	  			cohorts += "Harm-DB-"+myCohort.split("-")[2]+"<br>";
	  	  		}
	  	  		String requestSynopsis = cohorts + result_incl + result_excl;
	  	  		myjson.put("request_synopsis", requestSynopsis);
				myjson.put("execution_date", rs.getString("EXECUTION_DATE").replace("\t","").replace("\n", "").replace("\r", ""));
				
				String responseXML = rs.getString("RESPONSE_XML").replace("&lt;", "<").replace("&gt;", ">").replace("&quot;", "\"");
				FileWriter pw = new FileWriter(getServletContext().getRealPath("/WEB-INF/tempResponse.xml"));
			    pw.write(responseXML);
			    pw.close();
			    String responseSynopsis = "";
			    if(!responseXML.equals("")) {
				File myXMLResponse = new File(getServletContext().getRealPath("/WEB-INF/tempResponse.xml"));
				jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
	  	  		jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	  	  		PatientsSelectionResponse patientsSelectionResponse = ((JAXBElement<PatientsSelectionResponse>) jaxbUnmarshaller.unmarshal(myXMLResponse)).getValue();
	  	  		
	  	  		
	  	  		for(CohortResponse cohortResponse : patientsSelectionResponse.getCohortResponse()) {
	  	  			int i=0;
	  	  			//String cohortID = myCohort.split("-")[2];
	  	  			if(Integer.valueOf(cohortResponse.getCohortID().split("-")[2])<10) responseSynopsis += "Harm-DB-"+cohortResponse.getCohortID().split("-")[2]+":<br>";
	  	  			else responseSynopsis += "chdb0"+cohortResponse.getCohortID().split("-")[2]+":<br>";
	  	  			if(cohortResponse.getEligibilityCriteriaUsed()!=null) {
	  	  				responseSynopsis += "Number of patients found: "+cohortResponse.getEligiblePatientsNumber()+"<br>";
	  	  				responseSynopsis += "Inclusion Criteria:<br>";
	  	  			
	  	  				for (org.ntua.criteria.Criterion inclCriterion: patientsSelectionRequest.getEligibilityCriteria().getInclusionCriteria().getCriterion()) {
	  	  					responseSynopsis += inclCriterion.getDescription()+": "+inclCriterion.getFormalExpression().get(0).getBooleanExpression().trim()+" - ";
	  	  					responseSynopsis += cohortResponse.getEligibilityCriteriaUsed().getCriterionUsage().get(i).getCriterionUsageStatus()+"<br>";
	  	  					i++;
	  	  				}
	  	  				responseSynopsis += "Exclusion Criteria:<br>";
	  	  				for (org.ntua.criteria.Criterion exclCriterion: patientsSelectionRequest.getEligibilityCriteria().getExclusionCriteria().getCriterion()) {
	  	  					responseSynopsis += exclCriterion.getDescription()+": "+exclCriterion.getFormalExpression().get(0).getBooleanExpression().trim()+" - ";
	  	  					responseSynopsis += cohortResponse.getEligibilityCriteriaUsed().getCriterionUsage().get(i).getCriterionUsageStatus()+"<br>";
	  	  					i++;
	  	  				}
	  	  			}
	  	  			else responseSynopsis += "Cohort not available.<br>";
	  	  			responseSynopsis += "<br>";
	  	  		}
			    }
	  	  		myjson.put("response_synopsis", responseSynopsis);
				//myjson.put("response_XML", rs.getString("RESPONSE_XML").replace("\t","").replace("\n", "").replace("\r", ""));
				listJSONobj.add(myjson);
			}
			JSONObject result = new JSONObject();
			result.put("request_response_list", listJSONobj);
			response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			PrintWriter pw = response.getWriter();
			pw.print(result.toString());
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
		} catch (SQLException | JAXBException e) {
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
