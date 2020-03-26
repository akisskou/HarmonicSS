package xmlWrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ntua.criteria.CohortResponse;
import org.ntua.criteria.ObjectFactory;
import org.ntua.criteria.PatientsSelectionRequest;
import org.ntua.criteria.PatientsSelectionResponse;

/**
 * Servlet implementation class MidServiceForReqRespList
 */
public class MidServiceForReqRespList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MidServiceForReqRespList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected synchronized void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String userID = request.getParameter("userID");
		String requestID = request.getParameter("requestID");
		InputStream input = new FileInputStream(getServletContext().getRealPath("/WEB-INF/infos.properties"));
    	Properties prop = new Properties();
    	List<JSONObject> listJSONobj = new ArrayList<JSONObject>();
        // load a properties file
        prop.load(input);
        JSONObject myresult = new JSONObject();
        System.out.println(prop.getProperty("domain")+":"+prop.getProperty("port"));
		try {
	        String webPage = "http://"+prop.getProperty("domain").trim()+":"+prop.getProperty("port").trim()+"/HarmonicSS/ReqRespList?userID="+userID+"&requestID="+requestID;
			//String webPage = "http://harm.harmonicss.eu:8989/HarmonicSS/ReqRespList?userID="+userID+"&requestID="+requestID;
	        URL url = new URL(webPage);
	        URLConnection urlConnection = url.openConnection();
	        InputStream is = urlConnection.getInputStream();
	        InputStreamReader isr = new InputStreamReader(is);

	        int numCharsRead;
	        char[] charArray = new char[1024];
	        StringBuffer sb = new StringBuffer();
	        while ((numCharsRead = isr.read(charArray)) > 0) {
	            sb.append(charArray, 0, numCharsRead);
	        }
	        String result = sb.toString();
	        List<String> myresp = Arrays.asList(result.split("\\[",2));
	        String jsonarr = "[" + myresp.get(1);
	        //System.out.println(jsonarr);
	        JSONArray jsonarray = new JSONArray(jsonarr);
	        for(int j=0; j<jsonarray.length(); j++) {
	        	JSONObject myjson = new JSONObject();
	        	JSONObject execData = jsonarray.getJSONObject(j);
	        	myjson.put("user_id", execData.getString("user_id"));
				myjson.put("request_id", execData.getString("request_id"));
	        	String requestXML = execData.getString("requestXML");
	        	/*FileWriter fw = new FileWriter(getServletContext().getRealPath("/WEB-INF/tempRequest.xml"));
			    fw.write(requestXML);
			    fw.close();
			    File myXMLRequest = new File(getServletContext().getRealPath("/WEB-INF/tempRequest.xml"));*/
				JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
	  	  		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	  	  		StringReader reqReader = new StringReader(requestXML);
				PatientsSelectionRequest patientsSelectionRequest = ((JAXBElement<PatientsSelectionRequest>) jaxbUnmarshaller.unmarshal(reqReader)).getValue();
				reqReader.close();
	  	  		String result_incl = "Inclusion Criteria:<br>";
	  	  		String result_excl = "Exclusion Criteria:<br>";
	  	  		for(org.ntua.criteria.Criterion inclCriterion: patientsSelectionRequest.getEligibilityCriteria().getInclusionCriteria().getCriterion()){
	  	  			result_incl+=inclCriterion.getUID()+": "+inclCriterion.getDescription()+"<br>";//+" - "+inclCriterion.getFormalExpression().get(0).getBooleanExpression().trim()+"<br>";
	  	  		}
	  	  		for(org.ntua.criteria.Criterion exclCriterion: patientsSelectionRequest.getEligibilityCriteria().getExclusionCriteria().getCriterion()){
	  	  			result_excl+=exclCriterion.getUID()+": "+exclCriterion.getDescription()+"<br>";//+": "+exclCriterion.getFormalExpression().get(0).getBooleanExpression().trim()+"<br>";
	  	  		}
	  	  		String cohorts = "Cohorts:<br>";
	  	  		List<String> cohortList = patientsSelectionRequest.getCohortID();
	  	  		for(String myCohort : cohortList) {
	  	  			//System.out.println("Cohort with id="+myCohort.split("-")[2]+" updated successfully.");
	  	  			//cohorts += "Harm-DB-"+myCohort.split("-")[2]+"<br>";
	  	  			cohorts += myCohort+"<br>";
	  	  		}
	  	  		String requestSynopsis = cohorts + result_incl + result_excl;
	  	  		myjson.put("request_synopsis", requestSynopsis);
	  	  		myjson.put("execution_date", execData.getString("execution_date"));
	  	  		String responseXML = execData.getString("responseXML");
	  	  		/*FileWriter pw = new FileWriter(getServletContext().getRealPath("/WEB-INF/tempResponse.xml"));
	  	  		pw.write(responseXML);
	  	  		pw.close();*/
	  	  		String responseSynopsis = "";
	  	  		if(!responseXML.equals("")) {
	  	  			//File myXMLResponse = new File(getServletContext().getRealPath("/WEB-INF/tempResponse.xml"));
	  	  			jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
	  	  			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	  	  			StringReader respReader = new StringReader(responseXML);
					PatientsSelectionResponse patientsSelectionResponse = ((JAXBElement<PatientsSelectionResponse>) jaxbUnmarshaller.unmarshal(respReader)).getValue();
					respReader.close();
  	  		
	  	  			for(CohortResponse cohortResponse : patientsSelectionResponse.getCohortResponse()) {
	  	  				int i=0;
  	  			//String cohortID = myCohort.split("-")[2];
	  	  				if(Integer.valueOf(cohortResponse.getCohortID().split("-")[2])<10) responseSynopsis += "Harm-DB-"+cohortResponse.getCohortID().split("-")[2]+":<br>";
	  	  				else responseSynopsis += "chdb0"+cohortResponse.getCohortID().split("-")[2]+":<br>";
	  	  				//responseSynopsis += cohortResponse.getCohortID()+":<br>";
	  	  				if(cohortResponse.getEligibilityCriteriaUsed()!=null) {
	  	  					responseSynopsis += "Number of patients found: "+cohortResponse.getEligiblePatientsNumber()+"<br>";
	  	  					responseSynopsis += "Inclusion Criteria:<br>";
  	  			
	  	  					for (org.ntua.criteria.Criterion inclCriterion: patientsSelectionRequest.getEligibilityCriteria().getInclusionCriteria().getCriterion()) {
	  	  						responseSynopsis += inclCriterion.getUID()+": "+cohortResponse.getEligibilityCriteriaUsed().getCriterionUsage().get(i).getNotesHTML()/*inclCriterion.getDescription()+": "+inclCriterion.getFormalExpression().get(0).getBooleanExpression().trim()*/+" - ";
	  	  						responseSynopsis += cohortResponse.getEligibilityCriteriaUsed().getCriterionUsage().get(i).getCriterionUsageStatus()+"<br>";
	  	  						i++;
	  	  					}
	  	  					responseSynopsis += "Exclusion Criteria:<br>";
	  	  					for (org.ntua.criteria.Criterion exclCriterion: patientsSelectionRequest.getEligibilityCriteria().getExclusionCriteria().getCriterion()) {
	  	  						responseSynopsis += exclCriterion.getUID()+": "+cohortResponse.getEligibilityCriteriaUsed().getCriterionUsage().get(i).getNotesHTML()/*exclCriterion.getDescription()+": "+exclCriterion.getFormalExpression().get(0).getBooleanExpression().trim()*/+" - ";
	  	  						responseSynopsis += cohortResponse.getEligibilityCriteriaUsed().getCriterionUsage().get(i).getCriterionUsageStatus()+"<br>";
	  	  						i++;
	  	  					}
	  	  				}
	  	  				else responseSynopsis += "Cohort not available.<br>";
	  	  				responseSynopsis += "<br>";
	  	  			}
	  	  		}
	  	  		myjson.put("response_synopsis", responseSynopsis);
	  	  		listJSONobj.add(myjson);
	        }
	        
			myresult.put("request_response_list", listJSONobj);
			
	        
	       /* System.out.println("*** BEGIN ***");
	        System.out.println(result);
	        System.out.println("*** END ***");*/
	    } catch (MalformedURLException e) {
	    	//LOGGER.log(Level.SEVERE,"MalformedURLException while retrieving xml request.",true);
	        e.printStackTrace();
	    } catch (IOException e) {
	    	//LOGGER.log(Level.SEVERE,"IOException while retrieving xml request.",true);
	    	myresult.put("errorMessage","Error while connecting to database. Check your connection and db credentials and try again.");
	        e.printStackTrace();
	    } catch (NullPointerException e) {
	    	//LOGGER.log(Level.SEVERE,"IOException while retrieving xml request.",true);
	    	myresult.put("errorMessage","Error while connecting to database. Check your connection and db credentials and try again.");
	    	e.printStackTrace();
	    }catch (JSONException e) {
			// TODO Auto-generated catch block
	    	//LOGGER.log(Level.SEVERE,"JSONException while retrieving xml request.",true);
			e.printStackTrace();
		}
    	//return myXML;
		catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = response.getWriter();
		pw.print(myresult.toString());
		pw.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
	}

}
