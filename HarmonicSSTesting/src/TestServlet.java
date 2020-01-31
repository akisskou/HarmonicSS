

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONObject;
import org.ntua.criteria.ObjectFactory;
import org.ntua.criteria.PatientsSelectionRequest;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		try {
			int darId = createRequest("FanisKalatzisServicePost", "2018-11-21 21:59:59", "2", "VAL_A1POSTED");
			System.out.println("New request with id="+darId+" created successfully.");
			File fXmlFile = new File("Req01.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
  	  		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
  	  		PatientsSelectionRequest patientsSelectionRequest = ((JAXBElement<PatientsSelectionRequest>) jaxbUnmarshaller.unmarshal(fXmlFile)).getValue();
  	  		List<String> cohortList = patientsSelectionRequest.getCohortID();
  	  		for(String myCohort : cohortList) {
  	  			setCohortsStatus(darId, myCohort.split("-")[2], 2, "2010-12-04 21:00:00", "2011-12-04 21:00:00", "Remarks for my service", "testfilename");
  	  			System.out.println("Cohort with id="+myCohort.split("-")[2]+" updated successfully.");
  	  		}
			String requestXML = readLineByLineJava8("Req01.xml");
			setRequestXML(darId, requestXML);
			System.out.println("Request xml updated successfully.");
			/*Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Congrats - Seems your MySQL JDBC Driver Registered!");
			Connection db_con_obj = DriverManager.getConnection("jdbc:mysql://ponte.grid.ece.ntua.gr:3306/HarmonicSS-Patient-Selection-DB", "emps","emps");
			String query = "INSERT INTO EXECUTION_DATA (REQUEST_XML) VALUES (\"&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt; &lt;iccs:PatientsSelectionRequest sessionID=&quot;101010203920&quot; requestDate=&quot;2019-10-16&quot; xsi:schemaLocation=&quot;http://www.ntua.org/criteria PatientsSelection-EligibilityCriteria-Schema-v.1.3.xsd&quot; xmlns:iccs=&quot;http://www.ntua.org/criteria&quot; xmlns:xsi=&quot;http://www.w3.org/2001/XMLSchema-instance&quot;&gt; &lt;UserID&gt;HARM-USER-15&lt;/UserID&gt; &lt;CohortID&gt;COHORT-ID-01&lt;/CohortID&gt; &lt;CohortID&gt;COHORT-ID-17&lt;/CohortID&gt; &lt;RequestTitle&gt;Patient Selection request for clinical study XYZ&lt;/RequestTitle&gt; &lt;RequestID&gt;ELIGIBLE_PATIENTS_NUMBER&lt;/RequestID&gt; &lt;EligibilityCriteria&gt; &lt;InclusionCriteria&gt; &lt;!-- Demographics Criterion --&gt; &lt;Criterion UID=&quot;CRIT-ID-01&quot; Name=&quot;-&quot;&gt; &lt;Description&gt;Received Glucocorticoids&lt;/Description&gt; &lt;FormalExpression Language=&quot;JSON&quot; ID=&quot;CRIT-ID-01-FE&quot; Origin=&quot;-&quot; InfoLoss=&quot;false&quot; ProducedBy=&quot;HarmonicSS_GUI&quot; Model=&quot;HarmonicSS_ReferenceModel&quot;&gt; &lt;BooleanExpression&gt; {&quot;criterion&quot;:&quot;condition_diagnosis&quot;,&quot;condition&quot;:&quot;COND-101000&quot;} &lt;/BooleanExpression&gt; &lt;/FormalExpression&gt; &lt;/Criterion&gt; &lt;!-- Blood Test Criterion --&gt; &lt;!--Criterion UID=&quot;CRIT-ID-03&quot; Name=&quot;-&quot;&gt; &lt;Description&gt;Diagnosed with cancer&lt;/Description&gt; &lt;FormalExpression Language=&quot;JSON&quot; ID=&quot;CRIT-ID-03-FE&quot; Origin=&quot;-&quot; InfoLoss=&quot;false&quot; ProducedBy=&quot;HarmonicSS_GUI&quot; Model=&quot;HarmonicSS_ReferenceModel&quot;&gt; &lt;BooleanExpression&gt; {&quot;criterion&quot;:&quot;condition_diagnosis&quot;,&quot;condition&quot;:&quot;COND-140000&quot;} &lt;/BooleanExpression&gt; &lt;/FormalExpression&gt; &lt;/Criterion--&gt; &lt;/InclusionCriteria&gt; &lt;ExclusionCriteria&gt; &lt;!-- Medication Criterion --&gt; &lt;Criterion UID=&quot;CRIT-ID-04&quot; Name=&quot;-&quot;&gt; &lt;Description&gt;Anti-Ro/SSA was true (normal)&lt;/Description&gt; &lt;FormalExpression Language=&quot;JSON&quot; ID=&quot;CRIT-ID-04-FE&quot; Origin=&quot;-&quot; InfoLoss=&quot;false&quot; ProducedBy=&quot;HarmonicSS_GUI&quot; Model=&quot;HarmonicSS_ReferenceModel&quot;&gt; &lt;BooleanExpression&gt; {&quot;criterion&quot;:&quot;examination_lab_test&quot;,&quot;test_id&quot;:&quot;BLOOD-530&quot;} &lt;/BooleanExpression&gt; &lt;/FormalExpression&gt; &lt;/Criterion&gt; &lt;/ExclusionCriteria&gt; &lt;/EligibilityCriteria&gt; &lt;/iccs:PatientsSelectionRequest&gt;\")";
			PreparedStatement db_prep_obj = db_con_obj.prepareStatement(query);
			db_prep_obj.execute();
			query = "SELECT MAX(ID) FROM EXECUTION_DATA";
			db_prep_obj = db_con_obj.prepareStatement(query);
			ResultSet rs = db_prep_obj.executeQuery();
			if(rs.next()) {
				query = "INSERT INTO EXECUTION_DATA (REQUEST_ID) VALUES (\"Req"+rs.getString("ID")+"\") WHERE ID"
			}*/
		} catch (Exception e) {
			System.out.println("Sorry, couldn't found JDBC driver. Make sure you have added JDBC Maven Dependency Correctly");
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private static int createRequest(String userId, String submitDate, String serviceId, String justification) {
		try{
			HttpServletResponse response = null;
			URL url = new URL("https://private.harmonicss.eu/index.php/apps/coh/api/1.0/dar");
			String name = "hrexpert";
	        String password = "1hrexpert2!";

	        String authString = name + ":" + password;
	        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
	        String authStringEnc = new String(authEncBytes);
			JSONObject params = new JSONObject();
			params.put("userId", userId);
		    params.put("submitDate", submitDate);
		    params.put("serviceId", serviceId);
		    params.put("justification", justification);
		    String postData = params.toString();
		    //System.out.println(postData);
		    byte[] postDataBytes = postData.getBytes("UTF-8");
		    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		    conn.setRequestMethod("POST");
		    conn.setRequestProperty("Content-Type", "application/json");
		    conn.setRequestProperty("Authorization", "Basic " + authStringEnc);
		    conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
		    conn.setDoOutput(true);
		    conn.getOutputStream().write(postDataBytes);
		    Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		    StringBuilder sb = new StringBuilder();
		    for (int c; (c = in.read()) >= 0;)
		        sb.append((char)c);
		    String resp = sb.toString();
		    System.out.println(resp);
		    JSONObject newRequest = new JSONObject(resp);
		    return newRequest.getInt("id");
		}
	
		
		catch (Exception e) {
   			System.out.println(e);
   		}
		return 0;
	}
	
	private static String readLineByLineJava8(String filePath) 
	{
	    StringBuilder contentBuilder = new StringBuilder();
	    try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8)) 
	    {
	        stream.forEach(s -> contentBuilder.append(s).append("\n"));
	    }
	    catch (IOException e) 
	    {
	        e.printStackTrace();
	    }
	    return contentBuilder.toString();
	}
	
	private static void setCohortsStatus(int darId, String cohortId, int statusId, String statusDate, String validDate, String remarks, String filename) {
		try{
			URL url = new URL("https://private.harmonicss.eu/index.php/apps/coh/api/1.0/p6");
			String name = "hrexpert";
	        String password = "1hrexpert2!";

	        String authString = name + ":" + password;
	        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
	        String authStringEnc = new String(authEncBytes);
			JSONObject params = new JSONObject();
			params.put("darId", darId);
		    params.put("cohortId", cohortId);
		    params.put("statusId", statusId);
		    params.put("statusDate", statusDate);
		    params.put("validDate", validDate);
		    params.put("remarks", remarks);
		    params.put("filename", filename);
		    String postData = params.toString();
		    //System.out.println(postData);
		    byte[] postDataBytes = postData.getBytes("UTF-8");
		    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		    conn.setRequestMethod("POST");
		    conn.setRequestProperty("Content-Type", "application/json");
		    conn.setRequestProperty("Authorization", "Basic " + authStringEnc);
		    conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
		    conn.setDoOutput(true);
		    conn.getOutputStream().write(postDataBytes);
		    Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		    StringBuilder sb = new StringBuilder();
		    for (int c; (c = in.read()) >= 0;)
		        sb.append((char)c);
		    String resp = sb.toString();
		    System.out.println(resp);
		}
	
		
		catch (Exception e) {
   			System.out.println(e);
   		}
	}
	
	private static void setRequestXML(int darId, String requestXML) {
		try{
			URL url = new URL("https://private.harmonicss.eu/hcloud/index.php/apps/coh/api/1.0/s1");
			String name = "hrexpert";
	        String password = "1hrexpert2!";

	        String authString = name + ":" + password;
	        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
	        String authStringEnc = new String(authEncBytes);
			JSONObject params = new JSONObject();
			params.put("darId", darId);
		    params.put("serviceConfig",requestXML.replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;")); //"&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt; &lt;iccs:PatientsSelectionRequest sessionID=&quot;101010203920&quot; requestDate=&quot;2019-10-16&quot; xsi:schemaLocation=&quot;http://www.ntua.org/criteria PatientsSelection-EligibilityCriteria-Schema-v.1.3.xsd&quot; xmlns:iccs=&quot;http://www.ntua.org/criteria&quot; xmlns:xsi=&quot;http://www.w3.org/2001/XMLSchema-instance&quot;&gt; &lt;UserID&gt;HARM-USER-15&lt;/UserID&gt; &lt;CohortID&gt;COHORT-ID-01&lt;/CohortID&gt; &lt;CohortID&gt;COHORT-ID-17&lt;/CohortID&gt; &lt;RequestTitle&gt;Patient Selection request for clinical study XYZ&lt;/RequestTitle&gt; &lt;RequestID&gt;ELIGIBLE_PATIENTS_NUMBER&lt;/RequestID&gt; &lt;EligibilityCriteria&gt; &lt;InclusionCriteria&gt; &lt;!-- Demographics Criterion --&gt; &lt;Criterion UID=&quot;CRIT-ID-01&quot; Name=&quot;-&quot;&gt; &lt;Description&gt;Received Glucocorticoids&lt;/Description&gt; &lt;FormalExpression Language=&quot;JSON&quot; ID=&quot;CRIT-ID-01-FE&quot; Origin=&quot;-&quot; InfoLoss=&quot;false&quot; ProducedBy=&quot;HarmonicSS_GUI&quot; Model=&quot;HarmonicSS_ReferenceModel&quot;&gt; &lt;BooleanExpression&gt; {&quot;criterion&quot;:&quot;condition_diagnosis&quot;,&quot;condition&quot;:&quot;COND-101000&quot;} &lt;/BooleanExpression&gt; &lt;/FormalExpression&gt; &lt;/Criterion&gt; &lt;!-- Blood Test Criterion --&gt; &lt;!--Criterion UID=&quot;CRIT-ID-03&quot; Name=&quot;-&quot;&gt; &lt;Description&gt;Diagnosed with cancer&lt;/Description&gt; &lt;FormalExpression Language=&quot;JSON&quot; ID=&quot;CRIT-ID-03-FE&quot; Origin=&quot;-&quot; InfoLoss=&quot;false&quot; ProducedBy=&quot;HarmonicSS_GUI&quot; Model=&quot;HarmonicSS_ReferenceModel&quot;&gt; &lt;BooleanExpression&gt; {&quot;criterion&quot;:&quot;condition_diagnosis&quot;,&quot;condition&quot;:&quot;COND-140000&quot;} &lt;/BooleanExpression&gt; &lt;/FormalExpression&gt; &lt;/Criterion--&gt; &lt;/InclusionCriteria&gt; &lt;ExclusionCriteria&gt; &lt;!-- Medication Criterion --&gt; &lt;Criterion UID=&quot;CRIT-ID-04&quot; Name=&quot;-&quot;&gt; &lt;Description&gt;Anti-Ro/SSA was true (normal)&lt;/Description&gt; &lt;FormalExpression Language=&quot;JSON&quot; ID=&quot;CRIT-ID-04-FE&quot; Origin=&quot;-&quot; InfoLoss=&quot;false&quot; ProducedBy=&quot;HarmonicSS_GUI&quot; Model=&quot;HarmonicSS_ReferenceModel&quot;&gt; &lt;BooleanExpression&gt; {&quot;criterion&quot;:&quot;examination_lab_test&quot;,&quot;test_id&quot;:&quot;BLOOD-530&quot;} &lt;/BooleanExpression&gt; &lt;/FormalExpression&gt; &lt;/Criterion&gt; &lt;/ExclusionCriteria&gt; &lt;/EligibilityCriteria&gt; &lt;/iccs:PatientsSelectionRequest&gt;");
		    String postData = params.toString();
		    //System.out.println(postData);
		    byte[] postDataBytes = postData.getBytes("UTF-8");
		    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		    conn.setRequestMethod("PUT");
		    conn.setRequestProperty("Content-Type", "application/json");
		    conn.setRequestProperty("Authorization", "Basic " + authStringEnc);
		    conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
		    conn.setDoOutput(true);
		    conn.getOutputStream().write(postDataBytes);
		    Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		    StringBuilder sb = new StringBuilder();
		    for (int c; (c = in.read()) >= 0;)
		        sb.append((char)c);
		    String resp = sb.toString();
		    System.out.println(resp);
		    System.out.println(requestXML);
		}
		catch (Exception e) {
   			System.out.println(e);
   		}
	}
	
	/*public static void main(String[] args) throws Exception {
		
		
		
		
	}*/

}
