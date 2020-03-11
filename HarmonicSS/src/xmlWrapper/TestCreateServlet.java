package xmlWrapper;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.stream.Stream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import org.ntua.criteria.CriteriaList;
import org.ntua.criteria.Criterion;
import org.ntua.criteria.EligibilityCriteria;
import org.ntua.criteria.FormalExpression;
import org.ntua.criteria.FormalExpressionContent;
import org.ntua.criteria.FormalExpressionLanguage;
import org.ntua.criteria.Module;
import org.ntua.criteria.ObjectFactory;
import org.ntua.criteria.PatientSelectionType;
import org.ntua.criteria.PatientsSelectionRequest;

import com.google.gson.Gson;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestCreateServlet")
public class TestCreateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestCreateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public String writeXMLRequest(String user, String jsonInclCriteria, String jsonExclCriteria, String[] cohortIDs) throws JAXBException, DatatypeConfigurationException, Exception{
    	Exception e = new Exception();
    	CriteriaList inclCriteriaList = new CriteriaList();
		String[] inclCriteria;
		if(!jsonInclCriteria.trim().isEmpty()) {
			if (!jsonInclCriteria.contains("{") || !jsonInclCriteria.contains("}") || !jsonInclCriteria.contains(":")) throw e;
			else {
				inclCriteria = jsonInclCriteria.split("}");
				for(int i=0; i<inclCriteria.length; i++) {
					inclCriteria[i] = inclCriteria[i].trim()+"}";
					Criterion inclCriterion = new Criterion();
		    		if(i+1<10) inclCriterion.setUID("CRIT-ID-0"+(i+1));
		    		else inclCriterion.setUID("CRIT-ID-"+(i+1));
		    		inclCriterion.setName("-");
		    		inclCriterion.setDescription(inclCriteria[i]);
		    		FormalExpression myFormExp = new FormalExpression();
		    		myFormExp.setLanguage(FormalExpressionLanguage.JSON);
		    		myFormExp.setID(inclCriterion.getUID()+"-FE");
		    		myFormExp.setOrigin("-");
		    		myFormExp.setInfoLoss(false);
		    		myFormExp.setProducedBy(Module.HARMONIC_SS_GUI);
		    		myFormExp.setModel(FormalExpressionContent.HARMONIC_SS_REFERENCE_MODEL);
		    		myFormExp.setBooleanExpression(inclCriteria[i]);
		    		inclCriterion.getFormalExpression().add(myFormExp);
		    		inclCriteriaList.getCriterion().add(inclCriterion);
				}
		    		
		    	
			}
		}
		
		CriteriaList exclCriteriaList = new CriteriaList();
		String[] exclCriteria; 
		if(!jsonExclCriteria.trim().isEmpty()) {
			if(!jsonExclCriteria.contains("{") || !jsonExclCriteria.contains("}") || !jsonExclCriteria.contains(":")) throw e;
			else {
				exclCriteria = jsonExclCriteria.split("}");
				for(int i=0; i<exclCriteria.length; i++) {
					exclCriteria[i] = exclCriteria[i].trim()+"}";
					Criterion exclCriterion = new Criterion();
		    		if(i+inclCriteriaList.getCriterion().size()+1<10) exclCriterion.setUID("CRIT-ID-0"+(i+inclCriteriaList.getCriterion().size()+1));
		    		else exclCriterion.setUID("CRIT-ID-"+(i+inclCriteriaList.getCriterion().size()+1));
		    		exclCriterion.setName("-");
		    		exclCriterion.setDescription(exclCriteria[i]);
		    		FormalExpression myFormExp = new FormalExpression();
		    		myFormExp.setLanguage(FormalExpressionLanguage.JSON);
		    		myFormExp.setID(exclCriterion.getUID()+"-FE");
		    		myFormExp.setOrigin("-");
		    		myFormExp.setInfoLoss(false);
		    		myFormExp.setProducedBy(Module.HARMONIC_SS_GUI);
		    		myFormExp.setModel(FormalExpressionContent.HARMONIC_SS_REFERENCE_MODEL);
		    		myFormExp.setBooleanExpression(exclCriteria[i]);
		    		exclCriterion.getFormalExpression().add(myFormExp);
		    		exclCriteriaList.getCriterion().add(exclCriterion);
				}
			}
		}
    	JAXBContext xmljaxbContext = JAXBContext.newInstance(ObjectFactory.class);
    	Marshaller xmljaxbMarshaller = xmljaxbContext.createMarshaller();
    	xmljaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    	xmljaxbMarshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://www.ntua.org/criteria PatientsSelection-EligibilityCriteria-Schema-v.1.3.xsd");
    	PatientsSelectionRequest patientsSelectionRequest = new PatientsSelectionRequest();
    	GregorianCalendar gregorianCalendar = new GregorianCalendar();
        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
        XMLGregorianCalendar now = 
            datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
        now.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
        patientsSelectionRequest.setRequestDate(now);
    	patientsSelectionRequest.setUserID(user);
    	for(int i=0; i<cohortIDs.length; i++) {
    		patientsSelectionRequest.getCohortID().add("chdb0"+cohortIDs[i]);
    	}
    	patientsSelectionRequest.setRequestTitle("Patient Selection request for clinical study XYZ");
    	patientsSelectionRequest.setRequestID(PatientSelectionType.ELIGIBLE_PATIENTS_IDS);
    	EligibilityCriteria inclAndExclCriteria = new EligibilityCriteria();
    	
    	
    	inclAndExclCriteria.setInclusionCriteria(inclCriteriaList);
    	
    	inclAndExclCriteria.setExclusionCriteria(exclCriteriaList);
    	
    	patientsSelectionRequest.setEligibilityCriteria(inclAndExclCriteria);
    	
    	File fXmlFile = new File(getServletContext().getRealPath("/WEB-INF/Response.xml"));
		//File fXmlFile = new File("Response"+requestID+".xml");
		ObjectFactory objectFactory = new ObjectFactory();
		JAXBElement<PatientsSelectionRequest> je =  objectFactory.createPatientsSelectionRequest(patientsSelectionRequest);
		xmljaxbMarshaller.marshal(je, fXmlFile);
		StringWriter sw = new StringWriter();
		xmljaxbMarshaller.marshal(je, sw);
		return sw.toString();
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
	protected synchronized void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		TestCreateRequest req = new Gson().fromJson(request.getReader(), TestCreateRequest.class);
		JSONObject testResponse = new JSONObject();
		try {
			/*Scanner s = new Scanner(new BufferedReader(new FileReader(getServletContext().getRealPath("/WEB-INF/properties.txt"))));
			String[] line1 = s.nextLine().split(",");*/
			/*InputStream input = new FileInputStream(getServletContext().getRealPath("/WEB-INF/infos.properties"));
	    	Properties prop = new Properties();
            // load a properties file
            prop.load(input);*/
			String username = req.username;
			String password = req.password;
			int cohortStatus = 2;
			String[] cohortIDs = new String[req.cohortIDs.length];
			for(int i=0; i<req.cohortIDs.length; i++) cohortIDs[i] = req.cohortIDs[i];
			//File fXmlFile = new File(getServletContext().getRealPath("/WEB-INF/"+myRequestXML+".xml"));
			//File fXmlFile = new File(new URI("file:///"+line1[1].trim()+myRequestXML+".xml"));
			//File fXmlFile = new File(new URI("file:///"+prop.getProperty("pathToXML")+myRequestXML+".xml"));
			String requestXML = writeXMLRequest(username, req.jsonInclCriteria, req.jsonExclCriteria, cohortIDs);
			Date date = new Date();
			Object param = new java.sql.Timestamp(date.getTime());
			System.out.println("Ready to create new request...");
			int darId = createRequest(username, password, ((Timestamp)param).toString(), "1", "VAL_A1POSTED"); // TODO: current date
			System.out.println("New request with id: "+darId+" created successfully!");
  	  		
  	  		//List<String> cohortList = patientsSelectionRequest.getCohortID();
	  		System.out.println("Ready to update cohorts status...");
  	  		for(String myCohort : cohortIDs) {
  	  			setCohortsStatus(username, password, darId, myCohort, cohortStatus, ((Timestamp)param).toString(), ((Timestamp)param).toString(), "Remarks for my service", "testfilename");
  	  			System.out.println("Cohort with id="+myCohort+" updated successfully.");
  	  		}
  	  		System.out.println("Status of all cohorts updated successfully!");
			//String requestXML = readLineByLineJava8(getServletContext().getRealPath("/WEB-INF/"+myRequestXML+".xml"));
			//String requestXML = readLineByLineJava8(new URI("file:///"+prop.getProperty("pathToXML")+myRequestXML+".xml"));
  	  		
			System.out.println("Create request XML...");
			
  	  		setRequestXML(username, password, darId, requestXML);
  	  		System.out.println("Request XML created successfully...");
			
			testResponse.put("requestID", darId);
			
		} catch (JAXBException e) {
			//System.out.println("Sorry, couldn't found JDBC driver. Make sure you have added JDBC Maven Dependency Correctly");
			testResponse.put("errorMessage", "An error occured while creating request. Check your json criteria format and try again.");
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			testResponse.put("errorMessage", "An error occured while creating request. Check your credentials and internet connection and try again.");
			e.printStackTrace();
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			testResponse.put("errorMessage", "An error occured while creating request. Check your json criteria format and try again.");
			e.printStackTrace();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			testResponse.put("errorMessage", "An error occured while creating request. Check your json criteria format and try again.");
			e1.printStackTrace();
		}
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = response.getWriter();
		pw.print(testResponse.toString());
		pw.close();
	}
	
	// TODO: Close connections
	
	private static int createRequest(String username, String password, String submitDate, String serviceId, String justification) throws IOException, JSONException{
			HttpServletResponse response = null;
			URL url = new URL("https://private.harmonicss.eu/index.php/apps/coh/api/1.0/dar");
	        String authString = username + ":" + password;
	        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
	        String authStringEnc = new String(authEncBytes);
			JSONObject params = new JSONObject();
			params.put("userId", username);
		    params.put("submitDate", submitDate);
		    params.put("serviceId", serviceId);
		    params.put("justification", justification);
		    String postData = params.toString();
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
		    JSONObject newRequest = new JSONObject(resp);
		    return newRequest.getInt("id");
	}
	
	private static void setCohortsStatus(String username, String password, int darId, String cohortId, int statusId, String statusDate, String validDate, String remarks, String filename) {
		try{
			URL url = new URL("https://private.harmonicss.eu/index.php/apps/coh/api/1.0/p6");
	        String authString = username + ":" + password;
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
		}
	
		
		catch (Exception e) {
   			System.out.println(e);
   		}
	}
	
	private static void setRequestXML(String username, String password, int darId, String requestXML) {
		try{
			URL url = new URL("https://private.harmonicss.eu/hcloud/index.php/apps/coh/api/1.0/s1");
	        String authString = username + ":" + password;
	        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
	        String authStringEnc = new String(authEncBytes);
			JSONObject params = new JSONObject();
			params.put("darId", darId);
		    params.put("serviceConfig",requestXML);//.replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;")); //.replace("\n","").replace("\t","")); //"&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt; &lt;iccs:PatientsSelectionRequest sessionID=&quot;101010203920&quot; requestDate=&quot;2019-10-16&quot; xsi:schemaLocation=&quot;http://www.ntua.org/criteria PatientsSelection-EligibilityCriteria-Schema-v.1.3.xsd&quot; xmlns:iccs=&quot;http://www.ntua.org/criteria&quot; xmlns:xsi=&quot;http://www.w3.org/2001/XMLSchema-instance&quot;&gt; &lt;UserID&gt;HARM-USER-15&lt;/UserID&gt; &lt;CohortID&gt;COHORT-ID-01&lt;/CohortID&gt; &lt;CohortID&gt;COHORT-ID-17&lt;/CohortID&gt; &lt;RequestTitle&gt;Patient Selection request for clinical study XYZ&lt;/RequestTitle&gt; &lt;RequestID&gt;ELIGIBLE_PATIENTS_NUMBER&lt;/RequestID&gt; &lt;EligibilityCriteria&gt; &lt;InclusionCriteria&gt; &lt;!-- Demographics Criterion --&gt; &lt;Criterion UID=&quot;CRIT-ID-01&quot; Name=&quot;-&quot;&gt; &lt;Description&gt;Received Glucocorticoids&lt;/Description&gt; &lt;FormalExpression Language=&quot;JSON&quot; ID=&quot;CRIT-ID-01-FE&quot; Origin=&quot;-&quot; InfoLoss=&quot;false&quot; ProducedBy=&quot;HarmonicSS_GUI&quot; Model=&quot;HarmonicSS_ReferenceModel&quot;&gt; &lt;BooleanExpression&gt; {&quot;criterion&quot;:&quot;condition_diagnosis&quot;,&quot;condition&quot;:&quot;COND-101000&quot;} &lt;/BooleanExpression&gt; &lt;/FormalExpression&gt; &lt;/Criterion&gt; &lt;!-- Blood Test Criterion --&gt; &lt;!--Criterion UID=&quot;CRIT-ID-03&quot; Name=&quot;-&quot;&gt; &lt;Description&gt;Diagnosed with cancer&lt;/Description&gt; &lt;FormalExpression Language=&quot;JSON&quot; ID=&quot;CRIT-ID-03-FE&quot; Origin=&quot;-&quot; InfoLoss=&quot;false&quot; ProducedBy=&quot;HarmonicSS_GUI&quot; Model=&quot;HarmonicSS_ReferenceModel&quot;&gt; &lt;BooleanExpression&gt; {&quot;criterion&quot;:&quot;condition_diagnosis&quot;,&quot;condition&quot;:&quot;COND-140000&quot;} &lt;/BooleanExpression&gt; &lt;/FormalExpression&gt; &lt;/Criterion--&gt; &lt;/InclusionCriteria&gt; &lt;ExclusionCriteria&gt; &lt;!-- Medication Criterion --&gt; &lt;Criterion UID=&quot;CRIT-ID-04&quot; Name=&quot;-&quot;&gt; &lt;Description&gt;Anti-Ro/SSA was true (normal)&lt;/Description&gt; &lt;FormalExpression Language=&quot;JSON&quot; ID=&quot;CRIT-ID-04-FE&quot; Origin=&quot;-&quot; InfoLoss=&quot;false&quot; ProducedBy=&quot;HarmonicSS_GUI&quot; Model=&quot;HarmonicSS_ReferenceModel&quot;&gt; &lt;BooleanExpression&gt; {&quot;criterion&quot;:&quot;examination_lab_test&quot;,&quot;test_id&quot;:&quot;BLOOD-530&quot;} &lt;/BooleanExpression&gt; &lt;/FormalExpression&gt; &lt;/Criterion&gt; &lt;/ExclusionCriteria&gt; &lt;/EligibilityCriteria&gt; &lt;/iccs:PatientsSelectionRequest&gt;");
		    String postData = params.toString();
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
		}
		catch (Exception e) {
   			System.out.println(e);
   		}
	}
}