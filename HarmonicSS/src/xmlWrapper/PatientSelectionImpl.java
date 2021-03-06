package xmlWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ntua.criteria.*;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
//import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import com.google.gson.Gson;

import jsonProcess.*;
import criterionManager.*;
import criterionManager.Criterion;

import org.apache.tomcat.util.codec.binary.Base64;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import static queries.SQL_aux_functions.Make_OR_of_CODES;
import static queries.SQL_aux_functions.Make_begin_end_age_query;
import static queries.SQL_aux_functions.Make_begin_end_date_query;
import static queries.SQL_aux_functions.Make_begin_end_period_query;
import static queries.SQL_aux_functions.Make_specific_age_query;
import static queries.SQL_aux_functions.Make_specific_date_query;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Servlet implementation class PatientSelectionImpl
 */
public class PatientSelectionImpl extends HttpServlet implements XMLFileManager, CriterionManager{
	private static final long serialVersionUID = 1L;
	static ArrayList<String> cohort_names = new ArrayList<String>();
	private static List<JSONObject> inclusion_criteria = new ArrayList<JSONObject>();
	private static List<JSONObject> exclusion_criteria = new ArrayList<JSONObject>();	
	//private static JSONObject criterionResponseInfos = new JSONObject();
	private static String termAndSubterms = "";
	static String requestID = "";
	private static JAXBContext xmljaxbContext;
	private static Marshaller xmljaxbMarshaller;
	private static PatientsSelectionRequest patientsSelectionRequest;
	private static PatientsSelectionResponse patientsSelectionResponse;
	//private static File responseXML = new File("Resp01.xml");
	private static OWLOntology ontology;
	private static OWLOntologyManager manager;
	private static IRI documentIRI;
	private static List<MyOWLClass> allClasses;
	private static List<String[]> UIDsDefined = new ArrayList<String[]>();
	private static List<String[]> UIDsUndefined = new ArrayList<String[]>();
	private static Result_UIDs results;
	//private static List<JSONObject> cohortResponseList = new ArrayList<JSONObject>();
	private static Logger LOGGER;
	private static JSONObject all;
	private static Properties prop;
	private static String myReqXML;
	private static String myRespXML;
	private static boolean patients_found;
	private static List<String> inclusion_queries = new ArrayList<String>();
	private static List<String> exclusion_queries = new ArrayList<String>();
 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PatientSelectionImpl() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    
    @SuppressWarnings("deprecation")
    private static void findClasses(){
		Set<OWLClass> ontClasses = new HashSet<OWLClass>(); 
        ontClasses = ontology.getClassesInSignature();
        allClasses = new ArrayList<MyOWLClass>();
        
        for (Iterator<OWLClass> it = ontClasses.iterator(); it.hasNext(); ) {
        	MyOWLClass f = new MyOWLClass();
        	f.name = it.next();
        	f.id = f.name.getIRI().getFragment();
        	allClasses.add(f);
        }
	}
	
	@SuppressWarnings("deprecation")
	private static void findSubclasses(){
		for (final org.semanticweb.owlapi.model.OWLSubClassOfAxiom subClasse : ontology.getAxioms(AxiomType.SUBCLASS_OF))
        {
        	OWLClassExpression sup = subClasse.getSuperClass();
        	OWLClass sub = (OWLClass) subClasse.getSubClass();
        	
            if (sup instanceof OWLClass && sub instanceof OWLClass)
            {
            	int i;
            	for(i=0; i<allClasses.size(); i++){
            		if (sup.equals(allClasses.get(i).name)) break;
            	}
            	int j;
            	for(j=0; j<allClasses.size(); j++){
            		if (sub.equals(allClasses.get(j).name)){
            			allClasses.get(i).subClasses.add(allClasses.get(j));
            			allClasses.get(j).isSubClass = true;
            			break;
            		}
            	}
            	
            }
        }
	}
	
	private static String getSubKeywords(String keywords, MyOWLClass checked){
		if(keywords.equals("")) keywords += checked.id;			
		else keywords += ","+checked.id;
		for(int i=0; i<checked.subClasses.size(); i++){
			keywords = getSubKeywords(keywords, checked.subClasses.get(i));
		}
		return keywords;
	}
    
    public String getTermsWithNarrowMeaning(String myTerm) {
    	String narrowTerms = "";
    	for(int i=0; i<allClasses.size(); i++){
			if(myTerm.equals(allClasses.get(i).id)){
				try{
					narrowTerms = getSubKeywords(narrowTerms, allClasses.get(i));
				}
				catch (Exception e) {
			    	LOGGER.log(Level.SEVERE,"Exception while retrieving terms with narrow meaning.",true);
		   			System.out.println(e);
		   		}
				
				break;	
			}
		}	
    	return narrowTerms;
    }
    
    private String getXMLS2(String requestID, String username, String password) throws IOException, JSONException {
    	 String myXML = "";
	        String webPage = "https://private.harmonicss.eu/index.php/apps/coh/api/1.0/s2?darId="+requestID;

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
	        /*if(jsonarray.length()>0) myXML = jsonarray.getJSONObject(0).get("serviceConfig").toString();
	        else all.put("errorMessage", "Nothing found for request ID: "+requestID+". Check if this request exists and try again.");*/
	        myXML = jsonarray.getJSONObject(0).get("serviceConfig").toString();
	        
	        
	       /* System.out.println("*** BEGIN ***");
	        System.out.println(result);
	        System.out.println("*** END ***");*/
    	return myXML;
    }

    @SuppressWarnings("unchecked")
	public String readXMLbyRequestID(String requestID, String username, String password) throws JAXBException{
  	  	cohort_names.clear();
  	  	String result_incl = "";
  	  	String result_excl = "";
  	  	
  	  		JAXBContext jaxbContext;
	    	/*ConfigureFile obj = new ConfigureFile("jdbc:mysql://ponte.grid.ece.ntua.gr:3306/HarmonicSS-Patient-Selection-DB","emps","emps"); //?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","");
	    	if(!DBServiceCRUD.makeJDBCConnection(obj))  System.out.println("Connection with the ponte database failed. Check the Credentials and the DB URL.");
	    	else System.out.println("I am ponte and I'm gooooooood");
	    	
	    	DBServiceCRUD.getXMLRequestFromDB(requestID);*/
	    	
	    	/*URL myXMLService = new URL("http://localhost:8080/GetXMLS2/GetXMLServlet?darId="+requestID);
	    	HttpURLConnection con = (HttpURLConnection) myXMLService.openConnection();
			con.setRequestMethod("GET");
			con.setDoOutput(true);
			Reader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
		    StringBuilder sb = new StringBuilder();
		    for (int c; (c = in.read()) >= 0;)
		        sb.append((char)c);*/
		    String resp;
			try {
				resp = getXMLS2(requestID,username,password).toString().replace("&lt;", "<").replace("&gt;", ">").replace("&quot;", "\"");
				if(!resp.equals("")) {
				System.out.println("User "+username+" wants to execute request "+requestID);
				System.out.println("Request xml:"+resp);
		    
		    /*FileWriter fw = new FileWriter(getServletContext().getRealPath("/WEB-INF/Request"+requestID+".xml"));
		    fw.write(resp);
			fw.close();
			File fXmlFile = new File(getServletContext().getRealPath("/WEB-INF/Request"+requestID+".xml"));*/
	    	jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
  	  		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
  	  		StringReader reader = new StringReader(resp);
  	  		patientsSelectionRequest = ((JAXBElement<PatientsSelectionRequest>) jaxbUnmarshaller.unmarshal(reader)).getValue();
  	  		myReqXML = resp;
  	  		

  	  		/*System.out.println(patientsSelectionRequest.getSessionID());
  	  		System.out.println(patientsSelectionRequest.getRequestDate());
  	  		System.out.println(patientsSelectionRequest.getUserID());
  	  		System.out.println(patientsSelectionRequest.getCohortID());
  	  		System.out.println(patientsSelectionRequest.getRequestTitle());
  	  		System.out.println(patientsSelectionRequest.getRequestID());*/
  	  		
  	  		for(org.ntua.criteria.Criterion inclCriterion: patientsSelectionRequest.getEligibilityCriteria().getInclusionCriteria().getCriterion()){
  	  			//System.out.println(inclCriterion.getFormalExpression().get(0).getBooleanExpression().trim());
  	  			result_incl+=inclCriterion.getFormalExpression().get(0).getBooleanExpression().trim();
  	  		}
  	  		for(org.ntua.criteria.Criterion exclCriterion: patientsSelectionRequest.getEligibilityCriteria().getExclusionCriteria().getCriterion()){
	  			//System.out.println(exclCriterion.getFormalExpression().get(0).getBooleanExpression().trim());
	  			result_excl+=exclCriterion.getFormalExpression().get(0).getBooleanExpression().trim();
	  		}
  	  		result_incl ="{\"list_of_criterions\":\n" + 
  	  			"  [\n" + 
  	  			""
  	  			+result_incl
  	  			+"  ]\n" + 
  	  			"}\n" + 
  	  			"";

  	  		result_excl ="{\"list_of_criterions\":\n" + 
  	  			"  [\n" + 
  	  			""
  	  			+result_excl
  	  			+"  ]\n" + 
  	  			"}\n" + 
  	  			"";
  	  
  	  		return result_incl+"XXX"+result_excl;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			all.put("errorMessage", "Nothing found for request ID: "+requestID+". Check if this request exists and try again.");
    			e.printStackTrace();
    		}
		    return "";
    }
    
    private String makeCriterionList(String jsonCriteria) {
    	String result_incl ="{\"list_of_criterions\":\n" + 
  	  			"  [\n" + 
  	  			""
  	  			+jsonCriteria
  	  			+"  ]\n" + 
  	  			"}\n" + 
  	  			"";
    	return result_incl;
    }
    
    private String executeQuery(ArrayList<Criterion> list_of_criteria, boolean incl, String results_of_all_Criterions) {
    	//String results_of_one_Criterion="";
    	for(int i=0; i<list_of_criteria.size(); i++) {
    		//criterionResponseInfos = new JSONObject();
			Criterion current_Criterion=list_of_criteria.get(i); //current_criterion
			if(patients_found) {
			if(!canUseCriterion(current_Criterion)){
				System.out.println("Criterion " + current_Criterion.getCriterion() + " cannot be used.");
				if(incl) inclusion_criteria.add(new JSONObject("{\"notes\":\""+termAndSubterms+"\",\"usage\":\"notused\"}"));
				else exclusion_criteria.add(new JSONObject("{\"notes\":\""+termAndSubterms+"\",\"usage\":\"notused\"}"));
				continue;
			}
			else {
				System.out.println("Criterion " + current_Criterion.getCriterion() + " can be used.");
    			if(incl) inclusion_criteria.add(new JSONObject("{\"notes\":\""+termAndSubterms+"\",\"usage\":\"used\"}"));
				else exclusion_criteria.add(new JSONObject("{\"notes\":\""+termAndSubterms+"\",\"usage\":\"used\"}"));
				String query;
				if(incl) query = inclusion_queries.get(i);
				else query = exclusion_queries.get(i);
				try { 
					if(!results_of_all_Criterions.trim().equals("")) {
						/*String[] previousResults = results_of_all_Criterions.trim().split(" ");
						String inClause = "(";
						for (int j=0; j<previousResults.length; j++) {
							if(j==0) inClause += previousResults[j];
							else inClause += ","+previousResults[j];
						}
						inClause += ")";*/
						String inClause = "("+results_of_all_Criterions.trim()+")";
						if(query.contains("patient.ID NOT IN")) query=query.replaceFirst("WHERE","WHERE patient.ID IN "+inClause+" AND");
						else if(query.contains("patient outerr")) query=query.replaceFirst("WHERE","WHERE outerr.ID IN "+inClause+" AND");
						else query=query.replaceFirst("WHERE","WHERE patient.ID IN "+inClause+" AND");
					}
					System.out.println("We are ready to execute the query: "+query);
					results_of_all_Criterions = DBServiceCRUD.getDataFromDB(query);
					System.out.println("We executed the query: "+query +"\nAnd we had the result: "+results_of_all_Criterions);
					
				} catch (SQLException e) {
					//LOGGER.log(Level.SEVERE,"Bad type query or arguments: "+query,true);
					//flush_handler();
					//if(incl)
						LOGGER.log(Level.SEVERE,"SQLException while executing the query: " + query+", check the query format and db url and credentials.",true);
					//else
						//LOGGER.log(Level.SEVERE,"SQLException while executing the query: " + exclusion_queries.get(i)+", check the query format and db url and credentials.",true);
					all.put("errorMessage", "An error occured while executing your query. Check your json criteria format and try again.");
					e.printStackTrace();
					//return "Bad type query or arguments: "+query;
				}
					
				//LOGGER.log(Level.INFO, "Criterion-"+(i+1)+": "+current_Criterion.getCriterion()+"\nQuery-"+(i+1)+": "+query+"\n"+
				//"Results: "+results_of_one_Criterion+"\n",true);
				
				/*if(results_of_all_Criterions.equals("")) results_of_all_Criterions = results_of_one_Criterion;
				else results_of_all_Criterions = intersection_of_UIDs(results_of_one_Criterion, results_of_all_Criterions);*/
				if(results_of_all_Criterions.trim().equals("")) patients_found = false;
				System.out.println("patients found: "+patients_found);
			}
			}
			else {
				System.out.println("Criterion " + current_Criterion.getCriterion() + " cannot be used.");
    			if(incl) inclusion_criteria.add(new JSONObject("{\"notes\":\"Criterion cannot be reached because no patients found\",\"usage\":\"notused\"}"));
				else exclusion_criteria.add(new JSONObject("{\"notes\":\"Criterion cannot be reached because no patients found\",\"usage\":\"notused\"}"));
				continue;
			}
    	}
    	
    	if(!results_of_all_Criterions.trim().equals("") && !incl) {
    		try {
    			String finalUIDQuery = "SELECT DISTINCT UID FROM patient WHERE ID IN ("+results_of_all_Criterions+")";
    	    	System.out.println("We are ready to execute the UID query: "+finalUIDQuery);
    	    	results_of_all_Criterions = DBServiceCRUD.getUIDDataFromDB(finalUIDQuery);
    			System.out.println("We executed the UID query: "+finalUIDQuery +"\nAnd we had the result: "+results_of_all_Criterions);
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
    	
    	return results_of_all_Criterions;
    }
    
    private ArrayList<String> createPeriodNestedQueries(ArrayList<Criterion> list_of_criterions, boolean mode, boolean isPeriodStart) throws JSONException, JsonParseException, JsonMappingException, IOException {
    	ArrayList<String> nestedCriteria = new ArrayList<String>();
    	for(int i=0; i<list_of_criterions.size(); i++) {
    		Criterion current_Criterion=list_of_criterions.get(i);
    		String query="";
    		switch(current_Criterion.getCriterion()) {
    		case "lifestyle_smoking": { //Check if user provided the info of all the fields 
				  lifestyle_smoking crit_lifestyle_smoking_obj =  (lifestyle_smoking)current_Criterion;
				  
				  String tables = "patient, lifestyle_smoking";
				  String where_clause = "patient.ID = lifestyle_smoking.PATIENT_ID";  			  

				  if(!crit_lifestyle_smoking_obj.getVoc_smoking_status_CODE().isEmpty()) {
						 tables += ", voc_smoking_status";
						 where_clause += " AND lifestyle_smoking.STATUS_ID = voc_smoking_status.ID AND "+Make_OR_of_CODES("voc_smoking_status.CODE", crit_lifestyle_smoking_obj.getVoc_smoking_status_CODE());
					 }
				  
				  if(!(crit_lifestyle_smoking_obj.getSmoking_exact_date_year()).isEmpty()) {
					  tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
					  where_clause += Make_begin_end_period_query (mode,"lifestyle_smoking.PERIOD_ID", "dt_date1", "dt_date2", crit_lifestyle_smoking_obj.getSmoking_exact_date_year(), 
							  crit_lifestyle_smoking_obj.getSmoking_exact_date_month(), crit_lifestyle_smoking_obj.getSmoking_exact_date_day(),
							  crit_lifestyle_smoking_obj.getSmoking_exact_date_year(), crit_lifestyle_smoking_obj.getSmoking_exact_date_month(),
							  crit_lifestyle_smoking_obj.getSmoking_exact_date_day()); 
					  	 							  
				  } else if(!(crit_lifestyle_smoking_obj.getSmoking_period_begin_year()).isEmpty() || !(crit_lifestyle_smoking_obj.getSmoking_period_end_year()).isEmpty()) {
						tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
						where_clause += Make_begin_end_period_query (mode,"lifestyle_smoking.PERIOD_ID", "dt_date1", "dt_date2", crit_lifestyle_smoking_obj.getSmoking_period_begin_year(), 
								  crit_lifestyle_smoking_obj.getSmoking_period_begin_month(), crit_lifestyle_smoking_obj.getSmoking_period_begin_day(),
								  crit_lifestyle_smoking_obj.getSmoking_period_end_year(), crit_lifestyle_smoking_obj.getSmokimg_period_end_month(),
								  crit_lifestyle_smoking_obj.getSmoking_period_end_day()); 
																
					} else if(!(crit_lifestyle_smoking_obj.getSmoking_until_date_year()).isEmpty()) {
						tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
						where_clause += Make_begin_end_period_query (mode,"lifestyle_smoking.PERIOD_ID", "dt_date1", "dt_date2", "1800", 
								  "1", "1",crit_lifestyle_smoking_obj.getSmoking_until_date_year(), crit_lifestyle_smoking_obj.getSmoking_until_date_month(),
								  crit_lifestyle_smoking_obj.getSmoking_until_date_day());
						 								
					}  
				   
				  if(!crit_lifestyle_smoking_obj.getAmount_exact_value().isEmpty()){
					  	tables += ", dt_amount, voc_unit";
					  where_clause += " AND lifestyle_smoking.AMOUNT_ID = dt_amount.ID AND (dt_amount.value=" + crit_lifestyle_smoking_obj.getAmount_exact_value() +" OR (dt_amount.value<=" + crit_lifestyle_smoking_obj.getAmount_exact_value() + " AND dt_amount.value2>=" + crit_lifestyle_smoking_obj.getAmount_exact_value() + ")) AND dt_amount.UNIT_ID=voc_unit.ID AND voc_unit.CODE ='" + crit_lifestyle_smoking_obj.getDt_amount_voc_unit_CODE() + "' ";
					  
				  }
				  
				  if(!crit_lifestyle_smoking_obj.getAmount_range_min_value().isEmpty()){
					  	tables += ", dt_amount, voc_unit";
					  where_clause += " AND lifestyle_smoking.AMOUNT_ID = dt_amount.ID AND (dt_amount.value>=" + crit_lifestyle_smoking_obj.getAmount_range_min_value()+" OR dt_amount.value2>=" + crit_lifestyle_smoking_obj.getAmount_range_min_value()+")";
					  if(!crit_lifestyle_smoking_obj.getAmount_range_max_value().isEmpty()){
						  where_clause += " AND dt_amount.value<=" + crit_lifestyle_smoking_obj.getAmount_range_max_value();
					  }
					  where_clause += " AND dt_amount.UNIT_ID=voc_unit.ID AND voc_unit.CODE ='" + crit_lifestyle_smoking_obj.getDt_amount_voc_unit_CODE() + "'";
					  
				  }
				  
				  else if(!crit_lifestyle_smoking_obj.getAmount_range_max_value().isEmpty()){
					  	tables += ", dt_amount, voc_unit";
					  where_clause += " AND lifestyle_smoking.AMOUNT_ID = dt_amount.ID AND dt_amount.value<=" + crit_lifestyle_smoking_obj.getAmount_range_max_value() + " AND dt_amount.UNIT_ID=voc_unit.ID AND voc_unit.CODE ='" + crit_lifestyle_smoking_obj.getDt_amount_voc_unit_CODE() + "'";
					  
				  }
				  
				  if(!crit_lifestyle_smoking_obj.getStatement().isEmpty()){
					  tables += ", voc_confirmation";
					  where_clause += " AND lifestyle_smoking.STMT_ID=voc_confirmation.ID AND voc_confirmation.CODE='" + crit_lifestyle_smoking_obj.getStatement() + "'";	
					 
				  }
				  
				  where_clause += " AND lifestyle_smoking.STMT_ID=1";
				  
				  /*if(!crit_lifestyle_smoking_obj.getCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.ID HAVING COUNT(*) >= "+crit_lifestyle_smoking_obj.getCount();
				  }*/
					  
				  if(isPeriodStart) {
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date AS dt_date1, dt_period_of_time";
						  where_clause += " AND lifestyle_smoking.PERIOD_ID=dt_period_of_time.ID AND dt_period_of_time.START_DATE_ID=dt_date1.ID";
					  }
					  query = "(SELECT MIN(dt_date1.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
				  }
				  else {
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date AS dt_date2, dt_period_of_time";
						  where_clause += " AND lifestyle_smoking.PERIOD_ID=dt_period_of_time.ID AND dt_period_of_time.END_DATE_ID=dt_date2.ID";
					  }
					  query = "(SELECT MAX(dt_date2.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
				  }
					  
				  if(!crit_lifestyle_smoking_obj.getYearsNested().isEmpty()) {
					  query += " + ("+crit_lifestyle_smoking_obj.getYearsNested()+")";
				  }			
				  
				  //TODO 	will voc_unit.CODE take one value or more than one I also think that voc_direction_CODE() will take one value?
				  
				  //if(!crit_lifestyle_smoking_obj.getDt_period_of_time_voc_confirmation_CODE().isEmpty()) query += "AND dt_period_of_time.EXACT_ID = voc_confirmation.ID AND voc_confirmation.CODE='"+crit_lifestyle_smoking_obj.getDt_period_of_time_voc_confirmation_CODE()+"' "; 
				  //if(!crit_lifestyle_smoking_obj.getDt_period_of_time_BEFORE_PERIOD_ID().isEmpty()) query += "AND dt_period_of_time.BEFORE_PERIOD_ID = '"+crit_lifestyle_smoking_obj.getDt_period_of_time_BEFORE_PERIOD_ID()+"' ";
			  System.out.println("The Query is: "+ query);
			  } break;
			  
    		case "intervention_medication": { //Check if user provided the info of all the fields 
				  intervention_medication  crit_interv_medication_obj =  (intervention_medication )current_Criterion;
				  String tables = "patient, interv_medication";
				  String where_clause = "patient.ID = interv_medication.PATIENT_ID";
				  
				  if(!crit_interv_medication_obj.getVoc_pharm_drug_CODE().isEmpty()) {
					  tables += ", voc_pharm_drug";
					  where_clause += " AND interv_medication.MEDICATION_ID = voc_pharm_drug.ID AND (" + Make_OR_of_CODES("voc_pharm_drug.CODE", crit_interv_medication_obj.getVoc_pharm_drug_CODE());
					  String codes[] = crit_interv_medication_obj.getVoc_pharm_drug_CODE().split(",");
					  for(int k=0; k<codes.length; k++) {
						  String narrowTerms = getTermsWithNarrowMeaning(codes[k].trim());
						  String[] allNarrowTerms = narrowTerms.split(",");
						  for(int c=1; c<allNarrowTerms.length; c++) {
							  where_clause += " OR " + Make_OR_of_CODES("voc_pharm_drug.CODE", allNarrowTerms[c]);
						  }
					  }
					  where_clause += ")";
				  }
				  
				  
				  
				  //if(!crit_interv_medication_obj.getVoc_pharm_drug_BROADER_TERM_ID().isEmpty()) query += "AND voc_pharm_drug.BROADER_TERM_ID = '"+crit_interv_medication_obj.getVoc_pharm_drug_BROADER_TERM_ID()+"' "; //Do we need the Broader_Term_ID? (`BROADER_TERM_ID`) REFERENCES `voc_pharm_drug` (`ID`)
					  
				  if(!crit_interv_medication_obj.getDosage_amount_exact_value().isEmpty()) {
					  tables += ", dt_amount, voc_unit";
					  where_clause += " AND interv_medication.DOSAGE_ID = dt_amount.ID AND (dt_amount.VALUE ='" +crit_interv_medication_obj.getDosage_amount_exact_value()+"' OR (dt_amount.VALUE <="+ crit_interv_medication_obj.getDosage_amount_exact_value() +" AND dt_amount.VALUE2 >="+ crit_interv_medication_obj.getDosage_amount_exact_value() +")) "+
							  "AND dt_amount.UNIT_ID=voc_unit.ID " +
							  	"AND voc_unit.CODE ='"+crit_interv_medication_obj.getDOSAGE_ID_dt_amount_VALUE()+"'";;
				  }
				  
				  if(!crit_interv_medication_obj.getDosage_amount_range_min_value().isEmpty()){
					  	tables += ", dt_amount, voc_unit";
					  where_clause += " AND interv_medication.DOSAGE_ID = dt_amount.ID AND (dt_amount.value>=" + crit_interv_medication_obj.getDosage_amount_range_min_value() + " OR dt_amount.value2>=" + crit_interv_medication_obj.getDosage_amount_range_min_value() + ")";
					  if(!crit_interv_medication_obj.getDosage_amount_range_max_value().isEmpty()){
						  where_clause += " AND dt_amount.value<=" + crit_interv_medication_obj.getDosage_amount_range_max_value();
					  }
					  where_clause += " AND dt_amount.UNIT_ID=voc_unit.ID AND voc_unit.CODE ='" + crit_interv_medication_obj.getDOSAGE_ID_dt_amount_VALUE() + "'";
					  
				  }
				  
				  else if(!crit_interv_medication_obj.getDosage_amount_range_max_value().isEmpty()){
					  	tables += ", dt_amount, voc_unit";
					  where_clause += " AND interv_medication.DOSAGE_ID = dt_amount.ID AND dt_amount.value<=" + crit_interv_medication_obj.getDosage_amount_range_max_value() + " AND dt_amount.UNIT_ID=voc_unit.ID AND voc_unit.CODE ='" + crit_interv_medication_obj.getDOSAGE_ID_dt_amount_VALUE() + "'";
					  
				  }
				  
				  if(!(crit_interv_medication_obj.getMedication_exact_date_year()).isEmpty()) {	
					  tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
					  where_clause += Make_begin_end_period_query (mode,"interv_medication.PERIOD_ID", "dt_date1", "dt_date2", crit_interv_medication_obj.getMedication_exact_date_year(), 
					  			crit_interv_medication_obj.getMedication_exact_date_month(), crit_interv_medication_obj.getMedication_exact_date_day(),
					  			crit_interv_medication_obj.getMedication_exact_date_year(), crit_interv_medication_obj.getMedication_exact_date_month(),
					  			crit_interv_medication_obj.getMedication_exact_date_day());
							  
				  } else if(!crit_interv_medication_obj.getMedication_period_begin_year().isEmpty() || !(crit_interv_medication_obj.getMedication_period_end_year()).isEmpty()) {
					  tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
					  where_clause += Make_begin_end_period_query (mode,"interv_medication.PERIOD_ID", "dt_date1", "dt_date2", crit_interv_medication_obj.getMedication_period_begin_year(), 
								crit_interv_medication_obj.getMedication_period_begin_month(), crit_interv_medication_obj.getMedication_period_begin_day(),
								crit_interv_medication_obj.getMedication_period_end_year(), crit_interv_medication_obj.getMedication_period_end_month(),
								crit_interv_medication_obj.getMedication_period_end_day()); 												
				  
				  } else if(!(crit_interv_medication_obj.getMedication_until_date_year()).isEmpty()) {
					  tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
					  where_clause += Make_begin_end_period_query (mode,"interv_medication.PERIOD_ID", "dt_date1", "dt_date2", "1800", 
							  "1", "1",crit_interv_medication_obj.getMedication_until_date_year(), crit_interv_medication_obj.getMedication_until_date_month(),
							  crit_interv_medication_obj.getMedication_until_date_day()); 							
				  } 
				  
				  if(!crit_interv_medication_obj.getStatement().isEmpty()) {
					  tables += ", voc_confirmation";
					  where_clause += " AND interv_medication.STMT_ID=voc_confirmation.ID AND voc_confirmation.CODE='"+crit_interv_medication_obj.getStatement() + "'";
				  }
				  
				  where_clause += " AND interv_medication.STMT_ID=1";
				  
				  /*if(!crit_interv_medication_obj.getCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.ID HAVING COUNT(*) >= "+crit_interv_medication_obj.getCount();
				  }*/
				  
				  if(isPeriodStart) {
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date AS dt_date1, dt_period_of_time";
						  where_clause += " AND interv_medication.PERIOD_ID=dt_period_of_time.ID AND dt_period_of_time.START_DATE_ID=dt_date1.ID";
					  }
					  query = "(SELECT MIN(dt_date1.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
				  }
				  else {
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date AS dt_date2, dt_period_of_time";
						  where_clause += " AND interv_medication.PERIOD_ID=dt_period_of_time.ID AND dt_period_of_time.END_DATE_ID=dt_date2.ID";
					  }
					  query = "(SELECT MAX(dt_date2.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
				  }
					  
				  if(!crit_interv_medication_obj.getYearsNested().isEmpty()) {
					  query += " + ("+crit_interv_medication_obj.getYearsNested()+")";
				  }	
					
			  } break;
    		case "intervention_chemotherapy": { //Check if user provided the info of all the fields 
				  intervention_chemotherapy  crit_interv_chemotherapy_obj =  (intervention_chemotherapy)current_Criterion;
				  String tables = "patient, interv_chemotherapy";
				  String where_clause = "patient.ID = interv_chemotherapy.PATIENT_ID";
				  
				  if(!crit_interv_chemotherapy_obj.getReason().isEmpty()) {
					  tables += ", voc_confirmation AS conf_1";
					  where_clause += " AND interv_chemotherapy.DUE_TO_PSS_ID = conf_1.ID AND " + Make_OR_of_CODES("conf_1.CODE", crit_interv_chemotherapy_obj.getReason());
				  }
				  
				  if(!(crit_interv_chemotherapy_obj.getChem_exact_date_year()).isEmpty()) {	
					  tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
					  where_clause += Make_begin_end_period_query (mode,"interv_chemotherapy.PERIOD_ID", "dt_date1", "dt_date2", crit_interv_chemotherapy_obj.getChem_exact_date_year(), 
					  			crit_interv_chemotherapy_obj.getChem_exact_date_month(), crit_interv_chemotherapy_obj.getChem_exact_date_day(),
					  			crit_interv_chemotherapy_obj.getChem_exact_date_year(), crit_interv_chemotherapy_obj.getChem_exact_date_month(),
					  			crit_interv_chemotherapy_obj.getChem_exact_date_day()); 						  
					} else if(!(crit_interv_chemotherapy_obj.getChem_period_begin_year()).isEmpty() || !(crit_interv_chemotherapy_obj.getChem_period_end_year()).isEmpty()) {
						tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
						where_clause += Make_begin_end_period_query (mode,"interv_chemotherapy.PERIOD_ID", "dt_date1", "dt_date2", crit_interv_chemotherapy_obj.getChem_period_begin_year(), 
								crit_interv_chemotherapy_obj.getChem_period_begin_month(), crit_interv_chemotherapy_obj.getChem_period_begin_day(),
								crit_interv_chemotherapy_obj.getChem_period_end_year(), crit_interv_chemotherapy_obj.getChem_period_end_month(),
								crit_interv_chemotherapy_obj.getChem_period_end_day()); 												
					} else if(!(crit_interv_chemotherapy_obj.getChem_until_date_year()).isEmpty()) {
						tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
						where_clause += Make_begin_end_period_query (mode,"interv_chemotherapy.PERIOD_ID", "dt_date1", "dt_date2", "1800", 
								  "1", "1",crit_interv_chemotherapy_obj.getChem_until_date_year(), crit_interv_chemotherapy_obj.getChem_until_date_month(),
								  crit_interv_chemotherapy_obj.getChem_until_date_day()); 								
					}
				  
				  if(!crit_interv_chemotherapy_obj.getStatement().isEmpty()) {
					  tables += ", voc_confirmation AS conf_2";
					  where_clause += " AND interv_chemotherapy.STMT_ID=conf_2.ID AND conf_2.CODE='"+crit_interv_chemotherapy_obj.getStatement() + "'";
				  }
				  
				  where_clause += " AND interv_chemotherapy.STMT_ID=1";
				  
				  /*if(!crit_interv_chemotherapy_obj.getCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.ID HAVING COUNT(*) >= "+crit_interv_chemotherapy_obj.getCount();
				  }*/
				  
				  if(isPeriodStart) {
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date AS dt_date1, dt_period_of_time";
						  where_clause += " AND interv_chemotherapy.PERIOD_ID=dt_period_of_time.ID AND dt_period_of_time.START_DATE_ID=dt_date1.ID";
					  }
					  query = "(SELECT MIN(dt_date1.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
				  }
				  else {
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date AS dt_date2, dt_period_of_time";
						  where_clause += " AND interv_chemotherapy.PERIOD_ID=dt_period_of_time.ID AND dt_period_of_time.END_DATE_ID=dt_date2.ID";
					  }
					  query = "(SELECT MAX(dt_date2.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
				  }
					  
				  if(!crit_interv_chemotherapy_obj.getYearsNested().isEmpty()) {
					  query += " + ("+crit_interv_chemotherapy_obj.getYearsNested()+")";
				  }	
					//results_of_one_Criterion=DBServiceCRUD.getDataFromDB(query); 
					//System.out.println("We executed: "+crit_interv_chemotherapy_obj.criterion_name+"\nThe Query is: "+query); 
			  } break;
    		case "other_clinical_trials": { //Check if user provided the info of all the fields 
				  other_clinical_trials  other_clinical_trials_obj =  (other_clinical_trials)current_Criterion;
				  
				  String tables = "patient, other_clinical_trials";
				  String where_clause = "patient.ID = other_clinical_trials.PATIENT_ID";
						  
				  if(!(other_clinical_trials_obj.getPeriod_of_time_exact_year()).isEmpty()) {
						tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
						where_clause += Make_begin_end_period_query(mode, "other_clinical_trials.PERIOD_ID","dt_date1","dt_date2",other_clinical_trials_obj.getPeriod_of_time_exact_year(), 
								  other_clinical_trials_obj.getPeriod_of_time_exact_month(), other_clinical_trials_obj.getPeriod_of_time_exact_day(),other_clinical_trials_obj.getPeriod_of_time_exact_year(), 
								  other_clinical_trials_obj.getPeriod_of_time_exact_month(), other_clinical_trials_obj.getPeriod_of_time_exact_day());					  		
					} else if(!other_clinical_trials_obj.getPeriod_of_time_interval_start_year().isEmpty() || !(other_clinical_trials_obj.getPeriod_of_time_interval_end_year()).isEmpty()) {
						tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
						where_clause += Make_begin_end_period_query (mode,"other_clinical_trials.PERIOD_ID", "dt_date1","dt_date2",other_clinical_trials_obj.getPeriod_of_time_interval_start_year(), 
								  other_clinical_trials_obj.getPeriod_of_time_interval_start_month(), other_clinical_trials_obj.getPeriod_of_time_interval_start_day(),
								  other_clinical_trials_obj.getPeriod_of_time_interval_end_year(), other_clinical_trials_obj.getPeriod_of_time_interval_end_month(),
								  other_clinical_trials_obj.getPeriod_of_time_interval_end_day()); 			  
					} else if(!(other_clinical_trials_obj.getPeriod_of_time_until_year()).isEmpty()) {
						tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
						where_clause += Make_begin_end_period_query ( mode,"other_clinical_trials.PERIOD_ID","dt_date1","dt_date2", "1800", "1", "1", other_clinical_trials_obj.getPeriod_of_time_until_year(), other_clinical_trials_obj.getPeriod_of_time_until_month(),
								  other_clinical_trials_obj.getPeriod_of_time_until_day()); 
					}
			
			if(!other_clinical_trials_obj.getStatement().isEmpty()) 
		  		query += "AND other_clinical_trials.STMT_ID=voc_confirmation.ID " +
		  				 "AND voc_confirmation.CODE='"+other_clinical_trials_obj.getStatement() + "'";
			
			where_clause += " AND other_clinical_trials.STMT_ID=1";
			
			 /*if(!other_clinical_trials_obj.getCount().isEmpty()) {
			  		where_clause += " GROUP BY patient.ID HAVING COUNT(*) >= "+other_clinical_trials_obj.getCount();
			  }*/
			
			if(isPeriodStart) {
				  if(!tables.contains("dt_date")) {
					  tables += ", dt_date AS dt_date1, dt_period_of_time";
					  where_clause += " AND other_clinical_trials.PERIOD_ID=dt_period_of_time.ID AND dt_period_of_time.START_DATE_ID=dt_date1.ID";
				  }
				  query = "(SELECT MIN(dt_date1.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
			  }
			  else {
				  if(!tables.contains("dt_date")) {
					  tables += ", dt_date AS dt_date2, dt_period_of_time";
					  where_clause += " AND other_clinical_trials.PERIOD_ID=dt_period_of_time.ID AND dt_period_of_time.END_DATE_ID=dt_date2.ID";
				  }
				  query = "(SELECT MAX(dt_date2.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
			  }
				  
			  if(!other_clinical_trials_obj.getYearsNested().isEmpty()) {
				  query += " + ("+other_clinical_trials_obj.getYearsNested()+")";
			  }

			  } break;
    		default:
				  System.out.println("Undefined criterion-name-"+(i+1)+" in the input JSON file.");
			} 
			query=query.replace("WHERE  AND", "WHERE");
			query=query.replace("WHERE AND", "WHERE");
			nestedCriteria.add(query);
    	}
  	return nestedCriteria;
    }
    
    private ArrayList<String> createNestedQueries(ArrayList<Criterion> list_of_criterions, boolean mode, boolean isMax) throws JSONException, JsonParseException, JsonMappingException, IOException {
    	//String results_of_one_Criterion="";
    	ArrayList<String> nestedCriteria = new ArrayList<String>();
    	boolean oneDatePregnancy = true;
    	for(int i=0; i<list_of_criterions.size(); i++) {
			Criterion current_Criterion=list_of_criterions.get(i); //current_criterion
			String query="";
			switch(current_Criterion.getCriterion()) { //The name of the Criterion.
				    
			  case "demographics_pregnancy": { //Check if user provided the info of all the fields 
				  demographics_pregnancy crit_demo_pregnancy_obj =  (demographics_pregnancy)current_Criterion;
				  String tables = "patient, demo_pregnancy_data";
				  String where_clause = "patient.ID = demo_pregnancy_data.PATIENT_ID";
				  
				  if(!(crit_demo_pregnancy_obj.getCONCEPTION_DATE_YEAR()).isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  where_clause += Make_specific_date_query(mode, "demo_pregnancy_data.CONCEPTION_DATE_ID","dt_date1",crit_demo_pregnancy_obj.getCONCEPTION_DATE_YEAR(),
							  crit_demo_pregnancy_obj.getCONCEPTION_DATE_MONTH(),crit_demo_pregnancy_obj.getCONCEPTION_DATE_DAY());					  		
				  
				  } else if(!(crit_demo_pregnancy_obj.getCONCEPTION_period_begin_year()).isEmpty() || !(crit_demo_pregnancy_obj.getCONCEPTION_period_end_year()).isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  where_clause += Make_begin_end_date_query (mode,"demo_pregnancy_data.CONCEPTION_DATE_ID", "dt_date1",crit_demo_pregnancy_obj.getCONCEPTION_period_begin_year(), crit_demo_pregnancy_obj.getCONCEPTION_period_begin_month(), crit_demo_pregnancy_obj.getCONCEPTION_period_begin_day(), crit_demo_pregnancy_obj.getCONCEPTION_period_end_year(), crit_demo_pregnancy_obj.getCONCEPTION_period_end_month(),
							  crit_demo_pregnancy_obj.getCONCEPTION_period_end_day());			  
				  
				  } else if(!(crit_demo_pregnancy_obj.getCONCEPTION_until_date_year()).isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  where_clause += Make_begin_end_date_query (mode,"demo_pregnancy_data.CONCEPTION_DATE_ID","dt_date1", "1800", "1", "1", crit_demo_pregnancy_obj.getCONCEPTION_until_date_year(), 
							  crit_demo_pregnancy_obj.getCONCEPTION_until_date_month(), crit_demo_pregnancy_obj.getCONCEPTION_until_date_day()); 
				  }
				  
				  if(!crit_demo_pregnancy_obj.outcome_coded_value.isEmpty()) {
					  tables += ", voc_pregnancy_outcome";
					  where_clause += " AND demo_pregnancy_data.OUTCOME_ID=voc_pregnancy_outcome.ID AND (" + Make_OR_of_CODES("voc_pregnancy_outcome.CODE", crit_demo_pregnancy_obj.outcome_coded_value);
					  String codes[] = crit_demo_pregnancy_obj.outcome_coded_value.split(",");
					  for(int k=0; k<codes.length; k++) {
						  String narrowTerms = getTermsWithNarrowMeaning(codes[k].trim());
						  String[] allNarrowTerms = narrowTerms.split(",");
						  for(int c=1; c<allNarrowTerms.length; c++) {
							  where_clause += " OR " + Make_OR_of_CODES("voc_pregnancy_outcome.CODE", allNarrowTerms[c]);
						  }
					  }
					  where_clause += ")";
				  }
				  
				  if(!crit_demo_pregnancy_obj.outcome_exact_year.isEmpty()) {
					  tables += ", dt_date as dt_date2";
					  where_clause += Make_specific_date_query(mode, "demo_pregnancy_data.OUTCOME_DATE_ID","dt_date2", crit_demo_pregnancy_obj.getOUTCOME_DATE_YEAR(),
							  crit_demo_pregnancy_obj.getOUTCOME_DATE_MONTH(),crit_demo_pregnancy_obj.getOUTCOME_DATE_DAY());	
					 
				  	//} else if(!(crit_demo_pregnancy_obj.getOUTCOME_period_begin_year() + crit_demo_pregnancy_obj.getOUTCOME_period_begin_month() + crit_demo_pregnancy_obj.getOUTCOME_period_begin_day()).isEmpty()) {
				  	} else if(!crit_demo_pregnancy_obj.getOUTCOME_period_begin_year().isEmpty() || !crit_demo_pregnancy_obj.getOUTCOME_period_end_year().isEmpty()) {//+ crit_demo_pregnancy_obj.getOUTCOME_period_begin_month() + crit_demo_pregnancy_obj.getOUTCOME_period_begin_day()).isEmpty()) {

				  		tables += ", dt_date as dt_date2"; 
				  		where_clause += Make_begin_end_date_query (mode,"demo_pregnancy_data.OUTCOME_DATE_ID", "dt_date2", crit_demo_pregnancy_obj.getOUTCOME_period_begin_year(), crit_demo_pregnancy_obj.getOUTCOME_period_begin_month(), crit_demo_pregnancy_obj.getOUTCOME_period_begin_day(), crit_demo_pregnancy_obj.getOUTCOME_period_end_year(), crit_demo_pregnancy_obj.getOUTCOME_period_end_month(),
								  crit_demo_pregnancy_obj.getOUTCOME_period_end_day()); 			  
					
				  	} else if(!(crit_demo_pregnancy_obj.getOUTCOME_until_date_year() + crit_demo_pregnancy_obj.getOUTCOME_until_date_month() + crit_demo_pregnancy_obj.getOUTCOME_until_date_day()).isEmpty()) {
				  		tables += ", dt_date as dt_date2";  
				  		where_clause += Make_begin_end_date_query (mode,"demo_pregnancy_data.OUTCOME_DATE_ID", "dt_date2", "1800", "1", "1", crit_demo_pregnancy_obj.getOUTCOME_until_date_year(), 
								  crit_demo_pregnancy_obj.getOUTCOME_until_date_month(), crit_demo_pregnancy_obj.getOUTCOME_until_date_day()); 
					}
					  

				  	// sos we have mode 
				  	if(!crit_demo_pregnancy_obj.outcome_ss_related.isEmpty()&&!mode) {
				  		tables += ", voc_confirmation AS conf_1"; 
				  		where_clause += " AND demo_pregnancy_data.SS_CONCORDANT_ID=conf_1.ID AND conf_1.CODE='" + crit_demo_pregnancy_obj.outcome_ss_related + "'";
				  	}
				  	else if(!crit_demo_pregnancy_obj.outcome_ss_related.isEmpty()&&mode) {
				  		where_clause += " AND demo_pregnancy_data.SS_CONCORDANT_ID IS NULL"; 
				  	}
				  		
				  	if(!crit_demo_pregnancy_obj.getStatement().isEmpty()) {
				  		tables += ", voc_confirmation AS conf_2";
				  		where_clause += " AND demo_pregnancy_data.STMT_ID=conf_2.ID " +
				  				 "AND conf_2.CODE='"+crit_demo_pregnancy_obj.getStatement() + "'";
				  	}
				  	
				  	where_clause += " AND demo_pregnancy_data.STMT_ID=1";
				
				  	/*if(!crit_demo_pregnancy_obj.getCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.ID HAVING COUNT(*) >= "+crit_demo_pregnancy_obj.getCount();
					}*/
				  	
				  	if(crit_demo_pregnancy_obj.getTypeNested().equals("outcome")) {
				  		if(!tables.contains("dt_date2")) {
				  			tables += ", dt_date as dt_date2";
				  			where_clause += " AND demo_pregnancy_data.OUTCOME_DATE_ID=dt_date2.ID";
				  		}	
				  		if(isMax) query = "(SELECT MAX(dt_date2.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
						else query = "(SELECT MIN(dt_date2.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
				  		if(!crit_demo_pregnancy_obj.getOutcomeYearsNested().isEmpty()) {
							query += " + ("+crit_demo_pregnancy_obj.getOutcomeYearsNested()+")";
						}  
				  	}
				  	else if(crit_demo_pregnancy_obj.getTypeNested().equals("conception")) {
				  		if(!tables.contains("dt_date1")) {
				  			tables += ", dt_date as dt_date1";
				  			where_clause += " AND demo_pregnancy_data.CONCEPTION_DATE_ID=dt_date1.ID";
				  		}
				  		if(isMax) query = "(SELECT MAX(dt_date1.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
						else query = "(SELECT MIN(dt_date1.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
				  		if(!crit_demo_pregnancy_obj.getConceptionYearsNested().isEmpty()) {
							query += " + ("+crit_demo_pregnancy_obj.getConceptionYearsNested()+")";
						} 
				  	}
				  	/*else if(crit_demo_pregnancy_obj.getTypeNested().equals("both")) {
				  		oneDatePregnancy=false;
				  		String tables1 = tables;
				  		String tables2 = tables;
				  		String where_clause1 = where_clause;
				  		String where_clause2 = where_clause;
				  		if(!tables1.contains("dt_date1")) {
				  			tables1 += ", dt_date as dt_date1";
				  			where_clause1 += " AND demo_pregnancy_data.CONCEPTION_DATE_ID=dt_date1.ID";
				  		}
				  		if(!tables2.contains("dt_date2")) {
				  			tables2 += ", dt_date as dt_date2";
				  			where_clause2 += " AND demo_pregnancy_data.OUTCOME_DATE_ID=dt_date2.ID";
				  		}
				  		String query1;
				  		String query2;
				  		if(isMax) {
				  			query1 = "(SELECT MAX(dt_date1.YEAR) FROM " + tables1 + " WHERE patient.ID=outerr.ID AND " + where_clause1 +")";
				  			query2 = "(SELECT MAX(dt_date2.YEAR) FROM " + tables2 + " WHERE patient.ID=outerr.ID AND " + where_clause2 +")";
				  		}
				  		else {
				  			query1 = "(SELECT MIN(dt_date1.YEAR) FROM " + tables1 + " WHERE patient.ID=outerr.ID AND " + where_clause1 +")";
				  			query2 = "(SELECT MIN(dt_date2.YEAR) FROM " + tables2 + " WHERE patient.ID=outerr.ID AND " + where_clause2 +")";
				  		}
				  		if(!crit_demo_pregnancy_obj.getConceptionYearsNested().isEmpty()) {
							query1 += " + ("+crit_demo_pregnancy_obj.getConceptionYearsNested()+")";
						} 
				  		if(!crit_demo_pregnancy_obj.getOutcomeYearsNested().isEmpty()) {
							query2 += " + ("+crit_demo_pregnancy_obj.getOutcomeYearsNested()+")";
						}
				  		
				  		query=query1+"#"+query2;
				  	}*/
				  					  	
			  } break;
			  
			  case "lifestyle_smoking": { //Check if user provided the info of all the fields 
				  lifestyle_smoking crit_lifestyle_smoking_obj =  (lifestyle_smoking)current_Criterion;
				  
				  String tables = "patient, lifestyle_smoking";
				  String where_clause = "patient.ID = lifestyle_smoking.PATIENT_ID";  			  

				  if(!crit_lifestyle_smoking_obj.getVoc_smoking_status_CODE().isEmpty()) {
						 tables += ", voc_smoking_status";
						 where_clause += " AND lifestyle_smoking.STATUS_ID = voc_smoking_status.ID AND "+Make_OR_of_CODES("voc_smoking_status.CODE", crit_lifestyle_smoking_obj.getVoc_smoking_status_CODE());
					 }
				  
				  if(!(crit_lifestyle_smoking_obj.getSmoking_exact_date_year()).isEmpty()) {
					  tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
					  where_clause += Make_begin_end_period_query (mode,"lifestyle_smoking.PERIOD_ID", "dt_date1", "dt_date2", crit_lifestyle_smoking_obj.getSmoking_exact_date_year(), 
							  crit_lifestyle_smoking_obj.getSmoking_exact_date_month(), crit_lifestyle_smoking_obj.getSmoking_exact_date_day(),
							  crit_lifestyle_smoking_obj.getSmoking_exact_date_year(), crit_lifestyle_smoking_obj.getSmoking_exact_date_month(),
							  crit_lifestyle_smoking_obj.getSmoking_exact_date_day()); 
					  	 							  
				  } else if(!(crit_lifestyle_smoking_obj.getSmoking_period_begin_year()).isEmpty() || !(crit_lifestyle_smoking_obj.getSmoking_period_end_year()).isEmpty()) {
						tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
						where_clause += Make_begin_end_period_query (mode,"lifestyle_smoking.PERIOD_ID", "dt_date1", "dt_date2", crit_lifestyle_smoking_obj.getSmoking_period_begin_year(), 
								  crit_lifestyle_smoking_obj.getSmoking_period_begin_month(), crit_lifestyle_smoking_obj.getSmoking_period_begin_day(),
								  crit_lifestyle_smoking_obj.getSmoking_period_end_year(), crit_lifestyle_smoking_obj.getSmokimg_period_end_month(),
								  crit_lifestyle_smoking_obj.getSmoking_period_end_day()); 
																
					} else if(!(crit_lifestyle_smoking_obj.getSmoking_until_date_year()).isEmpty()) {
						tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
						where_clause += Make_begin_end_period_query (mode,"lifestyle_smoking.PERIOD_ID", "dt_date1", "dt_date2", "1800", 
								  "1", "1",crit_lifestyle_smoking_obj.getSmoking_until_date_year(), crit_lifestyle_smoking_obj.getSmoking_until_date_month(),
								  crit_lifestyle_smoking_obj.getSmoking_until_date_day());
						 								
					}  
				   
				  if(!crit_lifestyle_smoking_obj.getAmount_exact_value().isEmpty()){
					  	tables += ", dt_amount, voc_unit";
					  where_clause += " AND lifestyle_smoking.AMOUNT_ID = dt_amount.ID AND (dt_amount.value=" + crit_lifestyle_smoking_obj.getAmount_exact_value() +" OR (dt_amount.value<=" + crit_lifestyle_smoking_obj.getAmount_exact_value() + " AND dt_amount.value2>=" + crit_lifestyle_smoking_obj.getAmount_exact_value() + ")) AND dt_amount.UNIT_ID=voc_unit.ID AND voc_unit.CODE ='" + crit_lifestyle_smoking_obj.getDt_amount_voc_unit_CODE() + "' ";
					  
				  }
				  
				  if(!crit_lifestyle_smoking_obj.getAmount_range_min_value().isEmpty()){
					  	tables += ", dt_amount, voc_unit";
					  where_clause += " AND lifestyle_smoking.AMOUNT_ID = dt_amount.ID AND (dt_amount.value>=" + crit_lifestyle_smoking_obj.getAmount_range_min_value()+" OR dt_amount.value2>=" + crit_lifestyle_smoking_obj.getAmount_range_min_value()+")";
					  if(!crit_lifestyle_smoking_obj.getAmount_range_max_value().isEmpty()){
						  where_clause += " AND dt_amount.value<=" + crit_lifestyle_smoking_obj.getAmount_range_max_value();
					  }
					  where_clause += " AND dt_amount.UNIT_ID=voc_unit.ID AND voc_unit.CODE ='" + crit_lifestyle_smoking_obj.getDt_amount_voc_unit_CODE() + "'";
					  
				  }
				  
				  else if(!crit_lifestyle_smoking_obj.getAmount_range_max_value().isEmpty()){
					  	tables += ", dt_amount, voc_unit";
					  where_clause += " AND lifestyle_smoking.AMOUNT_ID = dt_amount.ID AND dt_amount.value<=" + crit_lifestyle_smoking_obj.getAmount_range_max_value() + " AND dt_amount.UNIT_ID=voc_unit.ID AND voc_unit.CODE ='" + crit_lifestyle_smoking_obj.getDt_amount_voc_unit_CODE() + "'";
					  
				  }
				  
				  if(!crit_lifestyle_smoking_obj.getStatement().isEmpty()){
					  tables += ", voc_confirmation";
					  where_clause += " AND lifestyle_smoking.STMT_ID=voc_confirmation.ID AND voc_confirmation.CODE='" + crit_lifestyle_smoking_obj.getStatement() + "'";	
					 
				  }
				  
				  where_clause += " AND lifestyle_smoking.STMT_ID=1";
				  
				  /*if(!crit_lifestyle_smoking_obj.getCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.ID HAVING COUNT(*) >= "+crit_lifestyle_smoking_obj.getCount();
				  }*/
				  
				  if(isMax) {
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date AS dt_date1, dt_period_of_time";
						  where_clause += " AND lifestyle_smoking.PERIOD_ID=dt_period_of_time.ID AND dt_period_of_time.START_DATE_ID=dt_date1.ID";
					  }
					  query = "(SELECT MAX(dt_date1.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
				  }
				  else {
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date AS dt_date2, dt_period_of_time";
						  where_clause += " AND lifestyle_smoking.PERIOD_ID=dt_period_of_time.ID AND dt_period_of_time.END_DATE_ID=dt_date2.ID";
					  }
					  query = "(SELECT MIN(dt_date2.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
				  }
					  
				  if(!crit_lifestyle_smoking_obj.getYearsNested().isEmpty()) {
					  query += " + ("+crit_lifestyle_smoking_obj.getYearsNested()+")";
				  }		
				  
				  //TODO 	will voc_unit.CODE take one value or more than one I also think that voc_direction_CODE() will take one value?
				  
				  //if(!crit_lifestyle_smoking_obj.getDt_period_of_time_voc_confirmation_CODE().isEmpty()) query += "AND dt_period_of_time.EXACT_ID = voc_confirmation.ID AND voc_confirmation.CODE='"+crit_lifestyle_smoking_obj.getDt_period_of_time_voc_confirmation_CODE()+"' "; 
				  //if(!crit_lifestyle_smoking_obj.getDt_period_of_time_BEFORE_PERIOD_ID().isEmpty()) query += "AND dt_period_of_time.BEFORE_PERIOD_ID = '"+crit_lifestyle_smoking_obj.getDt_period_of_time_BEFORE_PERIOD_ID()+"' ";
			  System.out.println("The Query is: "+ query);
			  } break;
			  
			  case "condition_symptom": { //Check if user provided the info of all the fields 
				  condition_symptom crit_cond_symptom_obj =  (condition_symptom)current_Criterion;
				  
				  String tables = "patient, cond_symptom";
				  String where_clause = "patient.ID = cond_symptom.PATIENT_ID";
						  
				if(!crit_cond_symptom_obj.getVoc_symptom_sign_CODE().isEmpty()) {
					tables += ", voc_symptom_sign";
					where_clause += " AND cond_symptom.CONDITION_ID = voc_symptom_sign.ID AND (" + Make_OR_of_CODES("voc_symptom_sign.CODE", crit_cond_symptom_obj.getVoc_symptom_sign_CODE());
					String codes[] = crit_cond_symptom_obj.getVoc_symptom_sign_CODE().split(",");
					  for(int k=0; k<codes.length; k++) {
						  String narrowTerms = getTermsWithNarrowMeaning(codes[k].trim());
						  String[] allNarrowTerms = narrowTerms.split(",");
						  for(int c=1; c<allNarrowTerms.length; c++) {
							  where_clause += " OR " + Make_OR_of_CODES("voc_symptom_sign.CODE", allNarrowTerms[c]);
						  }
					  }
						where_clause += ")";
				}
				  
				  
						  
				  
				  /*query = "SELECT DISTINCT patient.ID " + 
						  "FROM patient, cond_symptom, voc_symptom_sign, dt_date, voc_direction, voc_confirmation " + 
						  "WHERE patient.ID = cond_symptom.PATIENT_ID " + 
						  "AND cond_symptom.CONDITION_ID = voc_symptom_sign.ID " +
						  "AND " + Make_OR_of_CODES("voc_symptom_sign.CODE", crit_cond_symptom_obj.getVoc_symptom_sign_CODE());*/
				  
				  if(!(crit_cond_symptom_obj.getObserve_exact_date_YEAR()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_specific_date_query(mode, "cond_symptom.OBSERVE_DATE_ID","dt_date",crit_cond_symptom_obj.getObserve_exact_date_YEAR(), //check cond_symptom.OBSERVE_DATE_ID
						crit_cond_symptom_obj.getObserve_exact_date_MONTH(), crit_cond_symptom_obj.getObserve_exact_date_DAY());
						 
				  }else if(!crit_cond_symptom_obj.getObserve_period_begin_year().isEmpty() || !crit_cond_symptom_obj.getObserve_period_end_year().isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (mode,"cond_symptom.OBSERVE_DATE_ID", "dt_date",crit_cond_symptom_obj.getObserve_period_begin_year(), crit_cond_symptom_obj.getObserve_period_begin_month(), crit_cond_symptom_obj.getObserve_period_begin_day(), crit_cond_symptom_obj.getObserve_period_end_year() ,crit_cond_symptom_obj.getObserve_period_end_month(),
									  crit_cond_symptom_obj.getObserve_period_end_day()); 
							  
				  }else if(!(crit_cond_symptom_obj.getObserve_until_date_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (mode,"cond_symptom.OBSERVE_DATE_ID","dt_date", "1800", "1", "1",crit_cond_symptom_obj.getObserve_until_date_year(), 
									  crit_cond_symptom_obj.getObserve_until_date_month(), crit_cond_symptom_obj.getObserve_until_date_day()); 
				  }
				  
				  if(!crit_cond_symptom_obj.getStatement().isEmpty()){
					  tables += ", voc_confirmation";
					  where_clause += " AND cond_symptom.STMT_ID=voc_confirmation.ID " +
				  				 "AND voc_confirmation.CODE='"+crit_cond_symptom_obj.getStatement() + "'";
				  }
				  
				  where_clause += " AND cond_symptom.STMT_ID=1";
				  
				  /*if(!crit_cond_symptom_obj.getCount().isEmpty()) {
					  where_clause += " GROUP BY patient.ID HAVING COUNT(*) >= "+crit_cond_symptom_obj.getCount();
				  }*/
				  
				  if(!tables.contains("dt_date")) {
					  tables += ", dt_date";
					  where_clause += " AND cond_symptom.OBSERVE_DATE_ID=dt_date.ID";
				  }
				  
				  if(isMax) query = "(SELECT MAX(dt_date.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
				  else query = "(SELECT MIN(dt_date.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
				  
				  if(!crit_cond_symptom_obj.getYearsNested().isEmpty()) {
					  query += " + ("+crit_cond_symptom_obj.getYearsNested()+")";
				  }
			  } break;
			  
			  case "condition_diagnosis": { //Check if user provided the info of all the fields 
				  condition_diagnosis condition_diagnosis_obj =  (condition_diagnosis)current_Criterion;
				  
				  String tables = "patient, cond_diagnosis";
				  String where_clause = "patient.ID = cond_diagnosis.PATIENT_ID";
				  
				  if(!condition_diagnosis_obj.getCondition().isEmpty()) {
					  tables += ", voc_medical_condition";
					  where_clause += " AND cond_diagnosis.CONDITION_ID = voc_medical_condition.ID AND (" + Make_OR_of_CODES("voc_medical_condition.CODE", condition_diagnosis_obj.getCondition());
					  String codes[] = condition_diagnosis_obj.getCondition().split(",");
					  for(int k=0; k<codes.length; k++) {
						  String narrowTerms = getTermsWithNarrowMeaning(codes[k].trim());
						  String[] allNarrowTerms = narrowTerms.split(",");
						  for(int c=1; c<allNarrowTerms.length; c++) {
							  where_clause += " OR " + Make_OR_of_CODES("voc_medical_condition.CODE", allNarrowTerms[c]);
						  }
					  }
					  where_clause += ")";
				  }
				  
				  if(!condition_diagnosis_obj.getStage().isEmpty()) {  // [OUTCOME_ASSESSMENT]
					  tables += ", voc_lymphoma_stage";
					  where_clause += " AND cond_diagnosis.STAGE_ID = voc_lymphoma_stage.ID AND " + Make_OR_of_CODES("voc_lymphoma_stage.CODE", condition_diagnosis_obj.getStage());	  
				  }
				  
				  if(!condition_diagnosis_obj.getOrgan().isEmpty()) {  // [OUTCOME_ASSESSMENT]
					  tables += ", cond_diagnosis_organs, voc_lymphoma_organ";
					  where_clause += " AND cond_diagnosis.ID = cond_diagnosis_organs.DIAGNOSIS_ID AND cond_diagnosis_organs.ORGAN_ID = voc_lymphoma_organ.ID AND (" + Make_OR_of_CODES("voc_lymphoma_organ.CODE", condition_diagnosis_obj.getOrgan());
					  String organCodes[] = condition_diagnosis_obj.getOrgan().split(",");
					  for(int k=0; k<organCodes.length; k++) {
						  String narrowTerms = getTermsWithNarrowMeaning(organCodes[k].trim());
						  String[] allNarrowTerms = narrowTerms.split(",");
						  for(int c=1; c<allNarrowTerms.length; c++) {
							  where_clause += " OR " + Make_OR_of_CODES("voc_lymphoma_organ.CODE", allNarrowTerms[c]);
						  }
					  }
					  where_clause += ")";
					  	
				  }
				  
				  if(!condition_diagnosis_obj.getPerformance_status().isEmpty()) {  // [OUTCOME_ASSESSMENT]
					  tables += ", voc_performance_status";
					  where_clause += " AND cond_diagnosis.PERFORMANCE_STATUS_ID = voc_performance_status.ID AND " + Make_OR_of_CODES("voc_performance_status.CODE", condition_diagnosis_obj.getPerformance_status());
					  
				  }
				  
				  if(!(condition_diagnosis_obj.getDate_exact_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_specific_date_query(mode, "cond_diagnosis.DIAGNOSIS_DATE_ID","dt_date",condition_diagnosis_obj.getDate_exact_year(),
							  condition_diagnosis_obj.getDate_exact_month(),condition_diagnosis_obj.getDate_exact_day());
					 			  		
				  } else if(!(condition_diagnosis_obj.getDate_interval_start_year()).isEmpty() || !condition_diagnosis_obj.getDate_interval_end_year().isEmpty()) {
					 tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (mode,"cond_diagnosis.DIAGNOSIS_DATE_ID", "dt_date",condition_diagnosis_obj.getDate_interval_start_year(), condition_diagnosis_obj.getDate_interval_start_month(), condition_diagnosis_obj.getDate_interval_start_day(), condition_diagnosis_obj.getDate_interval_end_year(), condition_diagnosis_obj.getDate_interval_end_month(),
							  condition_diagnosis_obj.getDate_interval_end_day());  
				  } else if(!(condition_diagnosis_obj.getDate_until_year() ).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (mode,"cond_diagnosis.DIAGNOSIS_DATE_ID","dt_date", "1800", "1", "1", condition_diagnosis_obj.getDate_until_year(), 
							  condition_diagnosis_obj.getDate_until_month(), condition_diagnosis_obj.getDate_until_day()); 
				  }
				  
				  if(!condition_diagnosis_obj.getStatement().isEmpty()){ 
					  tables += ", voc_confirmation";
					  where_clause += " AND cond_diagnosis.STMT_ID=voc_confirmation.ID AND voc_confirmation.CODE='"+condition_diagnosis_obj.getStatement() + "'";
				  }
				  where_clause += " AND cond_diagnosis.STMT_ID=1";
				  
				  /*if(!condition_diagnosis_obj.getCount().isEmpty()) {
					  where_clause += " GROUP BY patient.ID HAVING COUNT(*) >= "+condition_diagnosis_obj.getCount();
				  }*/
				  
				  if(!tables.contains("dt_date")) {
					  tables += ", dt_date";
					  where_clause += " AND cond_diagnosis.DIAGNOSIS_DATE_ID=dt_date.ID";
				  }
				 
				  if(isMax) query = "(SELECT MAX(dt_date.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
				  else query = "(SELECT MIN(dt_date.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
				  
				  if(!condition_diagnosis_obj.getYearsNested().isEmpty()) {
					  query += " + ("+condition_diagnosis_obj.getYearsNested()+")";
				  }
			  } break;
			  
			  case "intervention_medication": { //Check if user provided the info of all the fields 
				  intervention_medication  crit_interv_medication_obj =  (intervention_medication )current_Criterion;
				  String tables = "patient, interv_medication";
				  String where_clause = "patient.ID = interv_medication.PATIENT_ID";
				  
				  if(!crit_interv_medication_obj.getVoc_pharm_drug_CODE().isEmpty()) {
					  tables += ", voc_pharm_drug";
					  where_clause += " AND interv_medication.MEDICATION_ID = voc_pharm_drug.ID AND (" + Make_OR_of_CODES("voc_pharm_drug.CODE", crit_interv_medication_obj.getVoc_pharm_drug_CODE());
					  String codes[] = crit_interv_medication_obj.getVoc_pharm_drug_CODE().split(",");
					  for(int k=0; k<codes.length; k++) {
						  String narrowTerms = getTermsWithNarrowMeaning(codes[k].trim());
						  String[] allNarrowTerms = narrowTerms.split(",");
						  for(int c=1; c<allNarrowTerms.length; c++) {
							  where_clause += " OR " + Make_OR_of_CODES("voc_pharm_drug.CODE", allNarrowTerms[c]);
						  }
					  }
					  where_clause += ")";
				  }
				  
				  
				  
				  //if(!crit_interv_medication_obj.getVoc_pharm_drug_BROADER_TERM_ID().isEmpty()) query += "AND voc_pharm_drug.BROADER_TERM_ID = '"+crit_interv_medication_obj.getVoc_pharm_drug_BROADER_TERM_ID()+"' "; //Do we need the Broader_Term_ID? (`BROADER_TERM_ID`) REFERENCES `voc_pharm_drug` (`ID`)
					  
				  if(!crit_interv_medication_obj.getDosage_amount_exact_value().isEmpty()) {
					  tables += ", dt_amount, voc_unit";
					  where_clause += " AND interv_medication.DOSAGE_ID = dt_amount.ID AND (dt_amount.VALUE ='" +crit_interv_medication_obj.getDosage_amount_exact_value()+"' OR (dt_amount.VALUE <="+ crit_interv_medication_obj.getDosage_amount_exact_value() +" AND dt_amount.VALUE2 >="+ crit_interv_medication_obj.getDosage_amount_exact_value() +")) "+
							  "AND dt_amount.UNIT_ID=voc_unit.ID " +
							  	"AND voc_unit.CODE ='"+crit_interv_medication_obj.getDOSAGE_ID_dt_amount_VALUE()+"'";;
				  }
				  
				  if(!crit_interv_medication_obj.getDosage_amount_range_min_value().isEmpty()){
					  	tables += ", dt_amount, voc_unit";
					  where_clause += " AND interv_medication.DOSAGE_ID = dt_amount.ID AND (dt_amount.value>=" + crit_interv_medication_obj.getDosage_amount_range_min_value() + " OR dt_amount.value2>=" + crit_interv_medication_obj.getDosage_amount_range_min_value() + ")";
					  if(!crit_interv_medication_obj.getDosage_amount_range_max_value().isEmpty()){
						  where_clause += " AND dt_amount.value<=" + crit_interv_medication_obj.getDosage_amount_range_max_value();
					  }
					  where_clause += " AND dt_amount.UNIT_ID=voc_unit.ID AND voc_unit.CODE ='" + crit_interv_medication_obj.getDOSAGE_ID_dt_amount_VALUE() + "'";
					  
				  }
				  
				  else if(!crit_interv_medication_obj.getDosage_amount_range_max_value().isEmpty()){
					  	tables += ", dt_amount, voc_unit";
					  where_clause += " AND interv_medication.DOSAGE_ID = dt_amount.ID AND dt_amount.value<=" + crit_interv_medication_obj.getDosage_amount_range_max_value() + " AND dt_amount.UNIT_ID=voc_unit.ID AND voc_unit.CODE ='" + crit_interv_medication_obj.getDOSAGE_ID_dt_amount_VALUE() + "'";
					  
				  }
				  
				  if(!(crit_interv_medication_obj.getMedication_exact_date_year()).isEmpty()) {	
					  tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
					  where_clause += Make_begin_end_period_query (mode,"interv_medication.PERIOD_ID", "dt_date1", "dt_date2", crit_interv_medication_obj.getMedication_exact_date_year(), 
					  			crit_interv_medication_obj.getMedication_exact_date_month(), crit_interv_medication_obj.getMedication_exact_date_day(),
					  			crit_interv_medication_obj.getMedication_exact_date_year(), crit_interv_medication_obj.getMedication_exact_date_month(),
					  			crit_interv_medication_obj.getMedication_exact_date_day());
							  
				  } else if(!crit_interv_medication_obj.getMedication_period_begin_year().isEmpty() || !(crit_interv_medication_obj.getMedication_period_end_year()).isEmpty()) {
					  tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
					  where_clause += Make_begin_end_period_query (mode,"interv_medication.PERIOD_ID", "dt_date1", "dt_date2", crit_interv_medication_obj.getMedication_period_begin_year(), 
								crit_interv_medication_obj.getMedication_period_begin_month(), crit_interv_medication_obj.getMedication_period_begin_day(),
								crit_interv_medication_obj.getMedication_period_end_year(), crit_interv_medication_obj.getMedication_period_end_month(),
								crit_interv_medication_obj.getMedication_period_end_day()); 												
				  
				  } else if(!(crit_interv_medication_obj.getMedication_until_date_year()).isEmpty()) {
					  tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
					  where_clause += Make_begin_end_period_query (mode,"interv_medication.PERIOD_ID", "dt_date1", "dt_date2", "1800", 
							  "1", "1",crit_interv_medication_obj.getMedication_until_date_year(), crit_interv_medication_obj.getMedication_until_date_month(),
							  crit_interv_medication_obj.getMedication_until_date_day()); 							
				  } 
				  
				  if(!crit_interv_medication_obj.getStatement().isEmpty()) {
					  tables += ", voc_confirmation";
					  where_clause += " AND interv_medication.STMT_ID=voc_confirmation.ID AND voc_confirmation.CODE='"+crit_interv_medication_obj.getStatement() + "'";
				  }
				  
				  where_clause += " AND interv_medication.STMT_ID=1";
				  
				  /*if(!crit_interv_medication_obj.getCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.ID HAVING COUNT(*) >= "+crit_interv_medication_obj.getCount();
				  }*/
				  
				  if(isMax) {
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date AS dt_date1, dt_period_of_time";
						  where_clause += " AND interv_medication.PERIOD_ID=dt_period_of_time.ID AND dt_period_of_time.START_DATE_ID=dt_date1.ID";
					  }
					  query = "(SELECT MAX(dt_date1.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
				  }
				  else {
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date AS dt_date2, dt_period_of_time";
						  where_clause += " AND interv_medication.PERIOD_ID=dt_period_of_time.ID AND dt_period_of_time.END_DATE_ID=dt_date2.ID";
					  }
					  query = "(SELECT MIN(dt_date2.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
				  }
					  
				  if(!crit_interv_medication_obj.getYearsNested().isEmpty()) {
					  query += " + ("+crit_interv_medication_obj.getYearsNested()+")";
				  }	
					
			  } break;
			  
			  case "intervention_chemotherapy": { //Check if user provided the info of all the fields 
				  intervention_chemotherapy  crit_interv_chemotherapy_obj =  (intervention_chemotherapy)current_Criterion;
				  String tables = "patient, interv_chemotherapy";
				  String where_clause = "patient.ID = interv_chemotherapy.PATIENT_ID";
				  
				  if(!crit_interv_chemotherapy_obj.getReason().isEmpty()) {
					  tables += ", voc_confirmation AS conf_1";
					  where_clause += " AND interv_chemotherapy.DUE_TO_PSS_ID = conf_1.ID AND " + Make_OR_of_CODES("conf_1.CODE", crit_interv_chemotherapy_obj.getReason());
				  }
				  
				  if(!(crit_interv_chemotherapy_obj.getChem_exact_date_year()).isEmpty()) {	
					  tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
					  where_clause += Make_begin_end_period_query (mode,"interv_chemotherapy.PERIOD_ID", "dt_date1", "dt_date2", crit_interv_chemotherapy_obj.getChem_exact_date_year(), 
					  			crit_interv_chemotherapy_obj.getChem_exact_date_month(), crit_interv_chemotherapy_obj.getChem_exact_date_day(),
					  			crit_interv_chemotherapy_obj.getChem_exact_date_year(), crit_interv_chemotherapy_obj.getChem_exact_date_month(),
					  			crit_interv_chemotherapy_obj.getChem_exact_date_day()); 						  
					} else if(!(crit_interv_chemotherapy_obj.getChem_period_begin_year()).isEmpty() || !(crit_interv_chemotherapy_obj.getChem_period_end_year()).isEmpty()) {
						tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
						where_clause += Make_begin_end_period_query (mode,"interv_chemotherapy.PERIOD_ID", "dt_date1", "dt_date2", crit_interv_chemotherapy_obj.getChem_period_begin_year(), 
								crit_interv_chemotherapy_obj.getChem_period_begin_month(), crit_interv_chemotherapy_obj.getChem_period_begin_day(),
								crit_interv_chemotherapy_obj.getChem_period_end_year(), crit_interv_chemotherapy_obj.getChem_period_end_month(),
								crit_interv_chemotherapy_obj.getChem_period_end_day()); 												
					} else if(!(crit_interv_chemotherapy_obj.getChem_until_date_year()).isEmpty()) {
						tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
						where_clause += Make_begin_end_period_query (mode,"interv_chemotherapy.PERIOD_ID", "dt_date1", "dt_date2", "1800", 
								  "1", "1",crit_interv_chemotherapy_obj.getChem_until_date_year(), crit_interv_chemotherapy_obj.getChem_until_date_month(),
								  crit_interv_chemotherapy_obj.getChem_until_date_day()); 								
					}
				  
				  if(!crit_interv_chemotherapy_obj.getStatement().isEmpty()) {
					  tables += ", voc_confirmation AS conf_2";
					  where_clause += " AND interv_chemotherapy.STMT_ID=conf_2.ID AND conf_2.CODE='"+crit_interv_chemotherapy_obj.getStatement() + "'";
				  }
				  
				  where_clause += " AND interv_chemotherapy.STMT_ID=1";
				  
				  /*if(!crit_interv_chemotherapy_obj.getCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.ID HAVING COUNT(*) >= "+crit_interv_chemotherapy_obj.getCount();
				  }*/
				  
				  if(isMax) {
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date AS dt_date1, dt_period_of_time";
						  where_clause += " AND interv_chemotherapy.PERIOD_ID=dt_period_of_time.ID AND dt_period_of_time.START_DATE_ID=dt_date1.ID";
					  }
					  query = "(SELECT MAX(dt_date1.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
				  }
				  else {
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date AS dt_date2, dt_period_of_time";
						  where_clause += " AND interv_chemotherapy.PERIOD_ID=dt_period_of_time.ID AND dt_period_of_time.END_DATE_ID=dt_date2.ID";
					  }
					  query = "(SELECT MIN(dt_date2.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
				  }
					  
				  if(!crit_interv_chemotherapy_obj.getYearsNested().isEmpty()) {
					  query += " + ("+crit_interv_chemotherapy_obj.getYearsNested()+")";
				  }	
					//results_of_one_Criterion=DBServiceCRUD.getDataFromDB(query); 
					//System.out.println("We executed: "+crit_interv_chemotherapy_obj.criterion_name+"\nThe Query is: "+query); 
			  } break;
			  
			  case "intervention_surgery": { //Check if user provided the info of all the fields 
				  intervention_surgery  crit_interv_surgery_obj =  (intervention_surgery)current_Criterion;
				  
				  String tables = "patient, interv_surgery";
				  String where_clause = "patient.ID = interv_surgery.PATIENT_ID";
				  
				  if(!crit_interv_surgery_obj.getReason().isEmpty()) {
					  tables += ", voc_confirmation AS conf_1";
					  where_clause += " AND interv_surgery.DUE_TO_PSS_ID = conf_1.ID AND "+Make_OR_of_CODES("conf_1.CODE", crit_interv_surgery_obj.getReason());
				  }
				  
				  if(!(crit_interv_surgery_obj.getSurgery_exact_date_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_specific_date_query(mode, "interv_surgery.SURGERY_DATE_ID","dt_date",crit_interv_surgery_obj.getSurgery_exact_date_year(), //check cond_symptom.OBSERVE_DATE_ID
							  crit_interv_surgery_obj.getSurgery_exact_date_month(), crit_interv_surgery_obj.getSurgery_exact_date_day());
					  
				  } else if(!crit_interv_surgery_obj.getSurgery_period_begin_year().isEmpty() || !(crit_interv_surgery_obj.getSurgery_period_end_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (mode,"interv_surgery.SURGERY_DATE_ID", "dt_date",crit_interv_surgery_obj.getSurgery_period_begin_year(),
							  crit_interv_surgery_obj.getSurgery_period_begin_month(), crit_interv_surgery_obj.getSurgery_period_begin_day(), crit_interv_surgery_obj.getSurgery_period_end_year() , 
							  crit_interv_surgery_obj.getSurgery_period_end_month(), crit_interv_surgery_obj.getSurgery_period_end_day()); 
					  
				  } else if(!(crit_interv_surgery_obj.getSurgery_until_date_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (mode,"interv_surgery.SURGERY_DATE_ID","dt_date", "1800", "1", "1", crit_interv_surgery_obj.getSurgery_until_date_year(), 
							  crit_interv_surgery_obj.getSurgery_until_date_month(), crit_interv_surgery_obj.getSurgery_until_date_day()); 
				  }
				  
				  if(!crit_interv_surgery_obj.getStatement().isEmpty()) {
					  tables += ", voc_confirmation AS conf_2";
					  where_clause += " AND interv_surgery.STMT_ID=conf_2.ID AND conf_2.CODE='" + crit_interv_surgery_obj.getStatement() + "'";
				  }
				  
				  where_clause += " AND interv_surgery.STMT_ID=1";
				  
				  /*if(!crit_interv_surgery_obj.getCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.ID HAVING COUNT(*) >= "+crit_interv_surgery_obj.getCount();
				  }*/
				  
				  if(!tables.contains("dt_date")) {
					  tables += ", dt_date";
					  where_clause += " AND interv_surgery.SURGERY_DATE_ID=dt_date.ID";
				  }
				 
				  if(isMax) query = "(SELECT MAX(dt_date.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
				  else query = "(SELECT MIN(dt_date.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
				  
				  if(!crit_interv_surgery_obj.getYearsNested().isEmpty()) {
					  query += " + ("+crit_interv_surgery_obj.getYearsNested()+")";
				  }
			  } break;
			  
			  case "examination_lab_test": { //Check if user provided the info of all the fields 
				  examination_lab_test  examination_lab_test_obj =  (examination_lab_test)current_Criterion;
				  
				  String tables = "patient, exam_lab_test";
				  String where_clause = "patient.ID = exam_lab_test.PATIENT_ID";
				  
				  if(!examination_lab_test_obj.getTest_id().isEmpty()) {
					  tables += ", voc_lab_test";
					  where_clause +=  " AND exam_lab_test.TEST_ID=voc_lab_test.ID AND (" + Make_OR_of_CODES("voc_lab_test.CODE", examination_lab_test_obj.getTest_id());
					  String codes[] = examination_lab_test_obj.getTest_id().split(",");
					  for(int k=0; k<codes.length; k++) {
						  String narrowTerms = getTermsWithNarrowMeaning(codes[k].trim());
						  String[] allNarrowTerms = narrowTerms.split(",");
						  for(int c=1; c<allNarrowTerms.length; c++) {
							  where_clause += " OR " + Make_OR_of_CODES("voc_lab_test.CODE", allNarrowTerms[c]);
						  }
					  }
					  where_clause += ") ";
				  
				  }
				  
				  if(!examination_lab_test_obj.getOutcome_amount_exact_value().isEmpty()) { // [OUTCOME_AMOUNT]
					  tables += ", dt_amount, voc_unit";
					  where_clause += " AND exam_lab_test.OUTCOME_AMOUNT_ID = dt_amount.ID AND (dt_amount.VALUE = '"+examination_lab_test_obj.getOutcome_amount_exact_value()+"' OR (dt_amount.VALUE <= " +examination_lab_test_obj.getOutcome_amount_exact_value()+" AND dt_amount.VALUE2 >= "+ examination_lab_test_obj.getOutcome_amount_exact_value()+")) " +
							  	"AND dt_amount.UNIT_ID=voc_unit.ID " +
							  	"AND voc_unit.CODE ='"+examination_lab_test_obj.getOutcome_amount_unit()+"' ";
					  
				  } else
				  
				  if(!examination_lab_test_obj.getOutcome_amount_range_min_value().isEmpty()&&!examination_lab_test_obj.getOutcome_amount_range_max_value().isEmpty()) { // [OUTCOME_AMOUNT]
					  tables += ", dt_amount, voc_unit";	
					  where_clause += " AND exam_lab_test.OUTCOME_AMOUNT_ID = dt_amount.ID AND dt_amount.VALUE <= '"+examination_lab_test_obj.getOutcome_amount_range_max_value()+"' " +
					  	"AND (dt_amount.VALUE >= '"+examination_lab_test_obj.getOutcome_amount_range_min_value()+"' OR dt_amount.VALUE2 >= '"+examination_lab_test_obj.getOutcome_amount_range_min_value()+") "+ 
					  	"AND dt_amount.UNIT_ID=voc_unit.ID " +
					  	"AND voc_unit.CODE ='"+examination_lab_test_obj.getOutcome_amount_unit()+"' ";
				  } else
					  
				  if(examination_lab_test_obj.getOutcome_amount_range_min_value().isEmpty()&&!examination_lab_test_obj.getOutcome_amount_range_max_value().isEmpty()) { // [OUTCOME_AMOUNT]
					  tables += ", dt_amount, voc_unit";	
					  where_clause += " AND exam_lab_test.OUTCOME_AMOUNT_ID = dt_amount.ID AND dt_amount.VALUE <= '"+examination_lab_test_obj.getOutcome_amount_range_max_value()+"' " +
					  	"AND dt_amount.UNIT_ID=voc_unit.ID " +
					  	"AND voc_unit.CODE ='"+examination_lab_test_obj.getOutcome_amount_unit()+"' ";
				  } else
					  
				  if(!examination_lab_test_obj.getOutcome_amount_range_min_value().isEmpty()&&examination_lab_test_obj.getOutcome_amount_range_max_value().isEmpty()) { // [OUTCOME_AMOUNT]
					  tables += ", dt_amount, voc_unit";
					  where_clause += " AND exam_lab_test.OUTCOME_AMOUNT_ID = dt_amount.ID " + //AND dt_amount.VALUE < '"+examination_lab_test_obj.getOutcome_amount_range_max_value()+"' 
					  	"AND (dt_amount.VALUE >= '"+examination_lab_test_obj.getOutcome_amount_range_min_value()+"' OR dt_amount.VALUE2 >=" +examination_lab_test_obj.getOutcome_amount_range_min_value()+") "+
					  	"AND dt_amount.UNIT_ID=voc_unit.ID " +
					  	"AND voc_unit.CODE ='"+examination_lab_test_obj.getOutcome_amount_unit()+"' ";
				  }
					  
				  
				  if(!examination_lab_test_obj.getOutcome_assessment().isEmpty()) {  // [OUTCOME_ASSESSMENT]
					  tables += ", voc_assessment";	
					  where_clause += " AND exam_lab_test.OUTCOME_ASSESSMENT_ID = voc_assessment.ID " + 
					  	//"AND voc_assessment.CODE ='"+crit_exam_lab_test_obj.OUTCOME_ASSESSMENT_ID_voc_assessment_CODE+"' ";
					  	"AND (" + Make_OR_of_CODES("voc_assessment.CODE", examination_lab_test_obj.getOutcome_assessment());
					  String narrowTermsAssess = getTermsWithNarrowMeaning(examination_lab_test_obj.getOutcome_assessment());
					  String[] allNarrowTermsAssess = narrowTermsAssess.split(",");
					  for(int c=1; c<allNarrowTermsAssess.length; c++) {
						  where_clause += " OR " + Make_OR_of_CODES("voc_assessment.CODE", allNarrowTermsAssess[c]);
					  }
					  where_clause += ")";
				  }
				  
				  //TODO NORMAL_RANGE  does it check if the value belongs in these two limits.		  
				  if(!(examination_lab_test_obj.getNormal_range_value()).isEmpty()) {
					  tables += ", dt_amount_range, voc_unit";	
					  where_clause += " AND exam_lab_test.NORMAL_RANGE_ID = dt_amount_range.ID " +
					  	"AND dt_amount_range.VALUE1 <= '"+examination_lab_test_obj.getNormal_range_value()+"' " +
					  	"AND dt_amount_range.VALUE2 >= '"+examination_lab_test_obj.getNormal_range_value()+"' " +
					    "AND dt_amount_range.UNIT_ID = voc_unit.ID AND voc_unit.CODE='"+examination_lab_test_obj.getOutcome_amount_unit()+"' ";
				  };
				  
				  
				  if(!(examination_lab_test_obj.getOutcome_term()).isEmpty()) {
					  String outcome_term= examination_lab_test_obj.getOutcome_term();
					  String codes[] = outcome_term.split(",");
					  boolean conf = false;
					  boolean cryo = false;
					  boolean ana = false;
					  for(int j=0; j<codes.length; j++) {
						  codes[j] = codes[j].trim();
						  if (codes[j].equals("CONFIRM-01")||codes[j].equals("CONFIRM-02")) {
							  if(!conf) {
								  conf = true;
								  tables += ", voc_confirmation";
							  }
							  if(j==0) where_clause += " AND (exam_lab_test.OUTCOME_TERM_ID = voc_confirmation.ID ";
							  else where_clause += " OR exam_lab_test.OUTCOME_TERM_ID = voc_confirmation.ID ";
							  where_clause += "AND voc_confirmation.CODE='"+codes[j] + "'";
						  }
						  else if(codes[j].equals("CRYO-01")||codes[j].equals("CRYO-02")||codes[j].equals("CRYO-03")){
							  if(!cryo) {
								  cryo = true;
								  tables += ", voc_cryo_type";
							  }
							  if(j==0) where_clause += " AND (exam_lab_test.OUTCOME_TERM_ID = voc_cryo_type.ID ";  
							  else where_clause += " OR exam_lab_test.OUTCOME_TERM_ID = voc_cryo_type.ID "; 
							  where_clause += "AND voc_cryo_type.CODE='"+codes[j]+ "'";
						  }
						  else {
							  if(!ana) {
								  ana = true;
								  tables += ", voc_ana_pattern";
							  }
							  if(j==0) where_clause += " AND (exam_lab_test.OUTCOME_TERM_ID = voc_ana_pattern.ID ";
							  else where_clause += " OR exam_lab_test.OUTCOME_TERM_ID = voc_ana_pattern.ID ";
							  where_clause += "AND voc_ana_pattern.CODE='"+codes[j]+ "'";
						  }
							
					  }
					  where_clause += ")";
					  
					}
					
				  if(!(examination_lab_test_obj.getSample_period_of_time_exact_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_specific_date_query(mode, "exam_lab_test.SAMPLE_DATE_ID","dt_date",examination_lab_test_obj.getSample_period_of_time_exact_year(), 
							  			examination_lab_test_obj.getSample_period_of_time_exact_month(), examination_lab_test_obj.getSample_period_of_time_exact_day());					  		
				  } 
				  
				  else if(!(examination_lab_test_obj.getSample_period_of_time_interval_start_year()).isEmpty() || !(examination_lab_test_obj.getSample_period_of_time_interval_end_year()).isEmpty()) {
							 
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (mode,"exam_lab_test.SAMPLE_DATE_ID", "dt_date",examination_lab_test_obj.getSample_period_of_time_interval_start_year(), 
										examination_lab_test_obj.getSample_period_of_time_interval_start_month(), examination_lab_test_obj.getSample_period_of_time_interval_start_day(),
										examination_lab_test_obj.getSample_period_of_time_interval_end_year(), examination_lab_test_obj.getSample_period_of_time_interval_end_month(),
										examination_lab_test_obj.getSample_period_of_time_interval_end_day()); 			  
						  } 
				  
				  else if(!(examination_lab_test_obj.getSample_period_of_time_until_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (mode,"exam_lab_test.SAMPLE_DATE_ID","dt_date", "1800", "1", "1", examination_lab_test_obj.getSample_period_of_time_until_year(), examination_lab_test_obj.getSample_period_of_time_until_month(),
									  examination_lab_test_obj.getSample_period_of_time_until_day()); 
						  }
				  
				  /*if(!examination_lab_test_obj.getCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.ID HAVING COUNT(*) >= "+examination_lab_test_obj.getCount();
				  }*/
					//results_of_one_Criterion=DBServiceCRUD.getDataFromDB(query); 
					//System.out.println("We executed: "+crit_exam_lab_test_obj.criterion_name+"\nThe Query is: "+query); 
				  
				  if(!tables.contains("dt_date")) {
					  tables += ", dt_date";
					  where_clause += " AND exam_lab_test.SAMPLE_DATE_ID=dt_date.ID";
				  }
				  
				  if(isMax) query = "(SELECT MAX(dt_date.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
				  else query = "(SELECT MIN(dt_date.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
				  
				  if(!examination_lab_test_obj.getYearsNested().isEmpty()) {
					  query += " + ("+examination_lab_test_obj.getYearsNested()+")";
				  }
				  
			  } break; 
			  
			  case "examination_biopsy": { //Check if user provided the info of all the fields 
				  examination_biopsy  examination_biopsy_obj =  (examination_biopsy)current_Criterion;
				  
				  String tables = "patient, exam_biopsy";
				  String where_clause = "patient.ID = exam_biopsy.PATIENT_ID";
				  
				  if(!examination_biopsy_obj.getBiopsy_type().isEmpty()) {
					  tables += ", voc_biopsy";
					  where_clause += " AND exam_biopsy.BIOPSY_ID=voc_biopsy.ID AND ("+Make_OR_of_CODES("voc_biopsy.CODE",examination_biopsy_obj.getBiopsy_type()); 
					  String codes[] = examination_biopsy_obj.getBiopsy_type().split(",");
					  for(int k=0; k<codes.length; k++) {
						  String narrowTerms = getTermsWithNarrowMeaning(codes[k].trim());
						  String[] allNarrowTerms = narrowTerms.split(",");
						  for(int c=1; c<allNarrowTerms.length; c++) {
							  where_clause += " OR " + Make_OR_of_CODES("voc_biopsy.CODE", allNarrowTerms[c]);
						  }
					  }
					  where_clause += ") ";
				  }
				  
				  if(!(examination_biopsy_obj.getTest_id()).isEmpty()) {
					  tables += ", voc_lab_test";
					  where_clause += " AND exam_biopsy.TEST_ID = voc_lab_test.ID AND ("+Make_OR_of_CODES("voc_lab_test.CODE",examination_biopsy_obj.getTest_id()); //'BLOOD-100'
					  String narrowTerms = getTermsWithNarrowMeaning(examination_biopsy_obj.getTest_id());
					  String[] allNarrowTerms = narrowTerms.split(",");
					  for(int c=1; c<allNarrowTerms.length; c++) {
						  where_clause += " OR voc_lab_test.CODE = '"+ allNarrowTerms[c] +"'";  //" + Make_OR_of_CODES("voc_pharm_drug.CODE", allNarrowTerms[c]);
					  }
					  where_clause += ") ";
				  };  
				  
			  
				  if(!examination_biopsy_obj.getOutcome_amount_exact_value().isEmpty()) { // [OUTCOME_AMOUNT]
					  tables += ", dt_amount, voc_unit";
					  where_clause += " AND exam_biopsy.OUTCOME_AMOUNT_ID = dt_amount.ID AND (dt_amount.VALUE = '"+examination_biopsy_obj.getOutcome_amount_exact_value()+"' OR (dt_amount.VALUE <= " +examination_biopsy_obj.getOutcome_amount_exact_value()+" AND dt_amount.VALUE2 >= "+examination_biopsy_obj.getOutcome_amount_exact_value()+")) "+
							  	"AND dt_amount.UNIT_ID=voc_unit.ID " +
							  	"AND voc_unit.CODE ='"+examination_biopsy_obj.getOutcome_amount_unit()+"' ";
					  
				  } else
				  
				  if(!examination_biopsy_obj.getOutcome_amount_range_min_value().isEmpty()&&!examination_biopsy_obj.getOutcome_amount_range_max_value().isEmpty()) { // [OUTCOME_AMOUNT]
					  tables += ", dt_amount, voc_unit";	
					  where_clause += " AND exam_biopsy.OUTCOME_AMOUNT_ID = dt_amount.ID AND dt_amount.VALUE <= '"+examination_biopsy_obj.getOutcome_amount_range_max_value()+"' " +
					  	"AND (dt_amount.VALUE >= '"+examination_biopsy_obj.getOutcome_amount_range_min_value()+"' OR dt_amount.VALUE2 >= " +examination_biopsy_obj.getOutcome_amount_range_min_value()+") "+
					  	"AND dt_amount.UNIT_ID=voc_unit.ID " +
					  	"AND voc_unit.CODE ='"+examination_biopsy_obj.getOutcome_amount_unit()+"' ";
				  } else
					  
				  if(examination_biopsy_obj.getOutcome_amount_range_min_value().isEmpty()&&!examination_biopsy_obj.getOutcome_amount_range_max_value().isEmpty()) { // [OUTCOME_AMOUNT]
					  tables += ", dt_amount, voc_unit";	
					  where_clause += " AND exam_biopsy.OUTCOME_AMOUNT_ID = dt_amount.ID AND dt_amount.VALUE <= '"+examination_biopsy_obj.getOutcome_amount_range_max_value()+"' " +
					  	"AND dt_amount.UNIT_ID=voc_unit.ID " +
					  	"AND voc_unit.CODE ='"+examination_biopsy_obj.getOutcome_amount_unit()+"' ";
				  } else
					  
				  if(!examination_biopsy_obj.getOutcome_amount_range_min_value().isEmpty()&&examination_biopsy_obj.getOutcome_amount_range_max_value().isEmpty()) { // [OUTCOME_AMOUNT]
					  tables += ", dt_amount, voc_unit";
					  where_clause += " AND exam_biopsy.OUTCOME_AMOUNT_ID = dt_amount.ID " + //AND dt_amount.VALUE < '"+examination_lab_test_obj.getOutcome_amount_range_max_value()+"' 
					  	"AND (dt_amount.VALUE >= '"+examination_biopsy_obj.getOutcome_amount_range_min_value()+"' OR dt_amount.VALUE2 >= " +examination_biopsy_obj.getOutcome_amount_range_min_value()+") "+
					  	"AND dt_amount.UNIT_ID=voc_unit.ID " +
					  	"AND voc_unit.CODE ='"+examination_biopsy_obj.getOutcome_amount_unit()+"' ";
				  }
					  					  
				  //TODO NORMAL_RANGE  does it check if the value belongs in these two limits.		  
				  if(!(examination_biopsy_obj.getNormal_range_value()).isEmpty()) {
					  tables += ", dt_amount_range, voc_unit";
					  where_clause += " AND exam_biopsy.NORMAL_RANGE_ID = dt_amount_range.ID " +
					  	"AND dt_amount_range.VALUE1 <= '"+examination_biopsy_obj.getNormal_range_value()+"' " +
					  	"AND dt_amount_range.VALUE2 >= '"+examination_biopsy_obj.getNormal_range_value()+"' " +
					    "AND dt_amount_range.UNIT_ID = voc_unit.ID AND voc_unit.CODE='"+examination_biopsy_obj.getOutcome_amount_unit()+"' ";
				  };
				  
				  if(!examination_biopsy_obj.getAssessment().isEmpty()) {  // [OUTCOME_ASSESSMENT]
					  tables += ", voc_assessment";
					  where_clause += " AND exam_biopsy.ASSESSMENT_ID = voc_assessment.ID " + 
					  	//"AND voc_assessment.CODE ='"+crit_exam_lab_test_obj.OUTCOME_ASSESSMENT_ID_voc_assessment_CODE+"' ";
					  	"AND (" + Make_OR_of_CODES("voc_assessment.CODE", examination_biopsy_obj.getAssessment());
					  String narrowTerms = getTermsWithNarrowMeaning(examination_biopsy_obj.getAssessment());
					  String[] allNarrowTerms = narrowTerms.split(",");
					  for(int c=1; c<allNarrowTerms.length; c++) {
						  where_clause += " OR " + Make_OR_of_CODES("voc_assessment.CODE", allNarrowTerms[c]);
					  }
					  where_clause += ")";
				  }

				  if(!examination_biopsy_obj.getOutcome_check().isEmpty()) {  // [OUTCOME_ASSESSMENT]
					  tables += ", voc_confirmation";
					  where_clause += " AND exam_biopsy.OUTCOME_CHECK_ID = voc_confirmation.ID " + 
					  	//"AND voc_assessment.CODE ='"+crit_exam_lab_test_obj.OUTCOME_ASSESSMENT_ID_voc_assessment_CODE+"' ";
					  	"AND " + Make_OR_of_CODES("voc_confirmation.CODE", examination_biopsy_obj.getOutcome_check());
				  }					  

				  if(!(examination_biopsy_obj.getBiopsy_period_of_time_exact_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_specific_date_query(mode, "exam_biopsy.BIOPSY_DATE_ID","dt_date",examination_biopsy_obj.getBiopsy_period_of_time_exact_year(), 
									  examination_biopsy_obj.getBiopsy_period_of_time_exact_month(), examination_biopsy_obj.getBiopsy_period_of_time_exact_day());					  		
						  } else if(!examination_biopsy_obj.getBiopsy_period_of_time_interval_start_year().isEmpty() || !(examination_biopsy_obj.getBiopsy_period_of_time_interval_end_year()).isEmpty()) {
							  tables += ", dt_date";
							  where_clause += Make_begin_end_date_query (mode,"exam_biopsy.BIOPSY_DATE_ID", "dt_date",examination_biopsy_obj.getBiopsy_period_of_time_interval_start_year(), 
									  examination_biopsy_obj.getBiopsy_period_of_time_interval_start_month(), examination_biopsy_obj.getBiopsy_period_of_time_interval_start_day(),
									  examination_biopsy_obj.getBiopsy_period_of_time_interval_end_year(), examination_biopsy_obj.getBiopsy_period_of_time_interval_end_month(),
									  examination_biopsy_obj.getBiopsy_period_of_time_interval_end_day()); 			  
						  } else if(!(examination_biopsy_obj.getBiopsy_period_of_time_until_year()).isEmpty()) {
							  tables += ", dt_date";
							  where_clause += Make_begin_end_date_query (mode,"exam_biopsy.BIOPSY_DATE_ID","dt_date", "1800", "1", "1", examination_biopsy_obj.getBiopsy_period_of_time_until_year(), examination_biopsy_obj.getBiopsy_period_of_time_until_month(),
									  examination_biopsy_obj.getBiopsy_period_of_time_until_day()); 
						  }
				  
				  /*if(!examination_biopsy_obj.getCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.ID HAVING COUNT(*) >= "+examination_biopsy_obj.getCount();
				  }*/
				  
				  if(!tables.contains("dt_date")) {
					  tables += ", dt_date";
					  where_clause += " AND exam_biopsy.BIOPSY_DATE_ID=dt_date.ID";
				  }
				 
				  if(isMax) query = "(SELECT MAX(dt_date.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
				  else query = "(SELECT MIN(dt_date.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
				  
				  if(!examination_biopsy_obj.getYearsNested().isEmpty()) {
					  query += " + ("+examination_biopsy_obj.getYearsNested()+")";
				  }
					//results_of_one_Criterion=DBServiceCRUD.getDataFromDB(query); 
					//System.out.println("We executed: "+crit_exam_lab_test_obj.criterion_name+"\nThe Query is: "+query); 
			  } break; //examination_medical_imaging_test
			  
			  case "examination_medical_imaging_test": { //Check if user provided the info of all the fields 
				  examination_medical_imaging_test  examination_medical_imaging_test_obj =  (examination_medical_imaging_test)current_Criterion;
				  
				  String tables = "exam_medical_imaging_test, patient";
				  String where_clause = "patient.ID = exam_medical_imaging_test.PATIENT_ID"; 
				  
				  if(!examination_medical_imaging_test_obj.getTest_id().isEmpty()) {
					  tables += ", voc_medical_imaging_test";
					  where_clause += "AND exam_medical_imaging_test.TEST_ID=voc_medical_imaging_test.ID AND (" + Make_OR_of_CODES("voc_medical_imaging_test.CODE", examination_medical_imaging_test_obj.getTest_id());
					  String codes[] = examination_medical_imaging_test_obj.getTest_id().split(",");
					  for(int k=0; k<codes.length; k++) {
						  String narrowTerms = getTermsWithNarrowMeaning(codes[k].trim());
						  String[] allNarrowTerms = narrowTerms.split(",");
						  for(int c=1; c<allNarrowTerms.length; c++) {
							  where_clause += " OR " + Make_OR_of_CODES("voc_medical_imaging_test.CODE", allNarrowTerms[c]);
						  }
					  }
					  where_clause += ") ";
				  }
				  
				  if(!examination_medical_imaging_test_obj.getAssessment().isEmpty()) {  // [OUTCOME_ASSESSMENT]
					  tables += ", voc_assessment";
					  where_clause += "AND exam_medical_imaging_test.ASSESSMENT_ID = voc_assessment.ID " + 
					  	//"AND voc_assessment.CODE ='"+crit_exam_lab_test_obj.OUTCOME_ASSESSMENT_ID_voc_assessment_CODE+"' ";
					  	"AND (" + Make_OR_of_CODES("voc_assessment.CODE", examination_medical_imaging_test_obj.getAssessment());
					  String mycodes[] = examination_medical_imaging_test_obj.getAssessment().split(",");
					  for(int k=0; k<mycodes.length; k++) {
						  String narrowTerms = getTermsWithNarrowMeaning(mycodes[k].trim());
						  String[] allNarrowTerms = narrowTerms.split(",");
						  for(int c=1; c<allNarrowTerms.length; c++) {
							  where_clause += " OR " + Make_OR_of_CODES("voc_assessment.CODE", allNarrowTerms[c]);
						  }
					  }
					  where_clause += ")";
				  }
				  
				  if(!(examination_medical_imaging_test_obj.getTest_period_of_time_exact_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_specific_date_query(mode, "exam_medical_imaging_test.TEST_DATE_ID","dt_date",examination_medical_imaging_test_obj.getTest_period_of_time_exact_year(), 
							  examination_medical_imaging_test_obj.getTest_period_of_time_exact_month(), examination_medical_imaging_test_obj.getTest_period_of_time_exact_day());					  		
				  } else if(!examination_medical_imaging_test_obj.getTest_period_of_time_interval_start_year().isEmpty() || !(examination_medical_imaging_test_obj.getTest_period_of_time_interval_end_year()).isEmpty()) {
					  tables += ", exam_lab_test, dt_date";
					  where_clause += Make_begin_end_date_query (mode,"exam_medical_imaging_test.TEST_DATE_ID", "dt_date",examination_medical_imaging_test_obj.getTest_period_of_time_interval_start_year(), 
							  examination_medical_imaging_test_obj.getTest_period_of_time_interval_start_month(), examination_medical_imaging_test_obj.getTest_period_of_time_interval_start_day(),
							  examination_medical_imaging_test_obj.getTest_period_of_time_interval_end_year(), examination_medical_imaging_test_obj.getTest_period_of_time_interval_end_month(),
							  examination_medical_imaging_test_obj.getTest_period_of_time_interval_end_day()); 			  
				  } else if(!(examination_medical_imaging_test_obj.getTest_period_of_time_until_year()).isEmpty()) {
					  tables += ", exam_lab_test, dt_date";
					  where_clause += Make_begin_end_date_query (mode,"exam_medical_imaging_test.TEST_DATE_ID","dt_date", "1800", "1", "1", examination_medical_imaging_test_obj.getTest_period_of_time_until_year(), examination_medical_imaging_test_obj.getTest_period_of_time_until_month(),
							  examination_medical_imaging_test_obj.getTest_period_of_time_until_day()); 
				  }
				  
				  /*if(!examination_medical_imaging_test_obj.getCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.ID HAVING COUNT(*) >= "+examination_medical_imaging_test_obj.getCount();
				  }*/
				  
				  if(!tables.contains("dt_date")) {
					  tables += ", dt_date";
					  where_clause += " AND exam_medical_imaging_test.TEST_DATE_ID=dt_date.ID";
				  }
				 
				  if(isMax) query = "(SELECT MAX(dt_date.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
				  else query = "(SELECT MIN(dt_date.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
				  
				  if(!examination_medical_imaging_test_obj.getYearsNested().isEmpty()) {
					  query += " + ("+examination_medical_imaging_test_obj.getYearsNested()+")";
				  }

					//results_of_one_Criterion=DBServiceCRUD.getDataFromDB(query); 
					//System.out.println("We executed: "+crit_exam_lab_test_obj.criterion_name+"\nThe Query is: "+query); 
			  } break;
			  case "examination_questionnaire_score": { //Check if user provided the info of all the fields 
				  examination_questionnaire_score  examination_questionnaire_score_obj =  (examination_questionnaire_score)current_Criterion;
				  
				  String tables = "patient, exam_questionnaire_score";
				  String where_clause = "patient.ID = exam_questionnaire_score.PATIENT_ID";
				  
				  if(!examination_questionnaire_score_obj.getScore().isEmpty()) {
					  tables += ", voc_questionnaire";
					  where_clause += " AND exam_questionnaire_score.SCORE_ID=voc_questionnaire.ID AND (" + Make_OR_of_CODES("voc_questionnaire.CODE", examination_questionnaire_score_obj.getScore());
					  String mycodes[] = examination_questionnaire_score_obj.getScore().split(",");
					  for(int k=0; k<mycodes.length; k++) {
						  String narrowTerms = getTermsWithNarrowMeaning(mycodes[k].trim());
						  String[] allNarrowTerms = narrowTerms.split(",");
						  for(int c=1; c<allNarrowTerms.length; c++) {
							  where_clause += " OR " + Make_OR_of_CODES("voc_questionnaire.CODE", allNarrowTerms[c]);
						  }
					  }
					  where_clause += ")";
				  }
				  
				  /*if(!examination_questionnaire_score_obj.getValue().isEmpty()) {  //TODO check value
					  where_clause += " AND " + Make_OR_of_CODES("exam_questionnaire_score.VALUE", examination_questionnaire_score_obj.getValue());
				  }*/
				  
				  if(!examination_questionnaire_score_obj.getExactValue().isEmpty()){
					  where_clause += " AND (exam_questionnaire_score.VALUE="+ examination_questionnaire_score_obj.getExactValue() +" OR (exam_questionnaire_score.VALUE<="+examination_questionnaire_score_obj.getExactValue()+" AND exam_questionnaire_score.VALUE2>="+examination_questionnaire_score_obj.getExactValue()+")) ";
				  }
				  
				  if(!examination_questionnaire_score_obj.getRangeMinValue().isEmpty()){
					  	where_clause += " AND (exam_questionnaire_score.VALUE>=" + examination_questionnaire_score_obj.getRangeMinValue() +" OR exam_questionnaire_score.VALUE2>=" + examination_questionnaire_score_obj.getRangeMinValue() +") "; 
					  	if(!examination_questionnaire_score_obj.getRangeMaxValue().isEmpty()) {
					  		where_clause += " AND exam_questionnaire_score.VALUE<=" + examination_questionnaire_score_obj.getRangeMaxValue();
					  	}
				  }
				  else if(!examination_questionnaire_score_obj.getRangeMaxValue().isEmpty()) {
					  where_clause += " AND exam_questionnaire_score.VALUE<=" + examination_questionnaire_score_obj.getRangeMaxValue();
				  }
				  
				  if(!examination_questionnaire_score_obj.getAssessment().isEmpty()) {  // [OUTCOME_ASSESSMENT]
					  tables += ", voc_assessment";
					  where_clause += " AND exam_questionnaire_score.ASSESSMENT_ID = voc_assessment.ID " + 
					  	//"AND voc_assessment.CODE ='"+crit_exam_lab_test_obj.OUTCOME_ASSESSMENT_ID_voc_assessment_CODE+"' ";
					  	"AND (" + Make_OR_of_CODES("voc_assessment.CODE", examination_questionnaire_score_obj.getAssessment());
					  String codes[] = examination_questionnaire_score_obj.getAssessment().split(",");
					  for(int k=0; k<codes.length; k++) {
						  String narrowTerms = getTermsWithNarrowMeaning(codes[k].trim());
						  String[] allNarrowTerms = narrowTerms.split(",");
						  for(int c=1; c<allNarrowTerms.length; c++) {
							  where_clause += " OR " + Make_OR_of_CODES("voc_assessment.CODE", allNarrowTerms[c]);
						  }
					  }
					  where_clause += ")";
				  }
				  
				  //TODO NORMAL_RANGE  does it check if the value belongs in these two limits.		  
				  if(!(examination_questionnaire_score_obj.getNormal_range_value()).isEmpty()) {
					  tables += ", dt_int_range";//, voc_unit";
					  where_clause += " AND exam_questionnaire_score.NORMAL_RANGE_ID = dt_int_range.ID " +
					  	"AND dt_int_range.INT1 <= '"+examination_questionnaire_score_obj.getNormal_range_value()+"' " +
					  	"AND dt_int_range.INT2 >= '"+examination_questionnaire_score_obj.getNormal_range_value()+"' ";/* +
					    "AND dt_int_range.UNIT_ID = voc_unit.ID AND voc_unit.CODE='"+examination_questionnaire_score_obj.getUnit()+"' ";*/
				  };
				  
				  if(!examination_questionnaire_score_obj.getOther_term().isEmpty()) {  //TODO check value
					  where_clause += " AND " + Make_OR_of_CODES("exam_questionnaire_score.OTHER_TERM_ID", examination_questionnaire_score_obj.getOther_term());
				  }
				  
				  				  
				  if(!(examination_questionnaire_score_obj.getQuestionnaire_period_of_time_exact_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_specific_date_query(mode, "exam_questionnaire_score.QUESTIONNAIRE_DATE_ID","dt_date",examination_questionnaire_score_obj.getQuestionnaire_period_of_time_exact_year(), 
							  examination_questionnaire_score_obj.getQuestionnaire_period_of_time_exact_month(), examination_questionnaire_score_obj.getQuestionnaire_period_of_time_exact_day());					  		
				  } else if(!examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_start_year().isEmpty() || !(examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_end_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (mode,"exam_questionnaire_score.QUESTIONNAIRE_DATE_ID", "dt_date",examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_start_year(), 
							  examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_start_month(), examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_start_day(),
							  examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_end_year(), examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_end_month(),
							  examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_end_day()); 			  
				  } else if(!(examination_questionnaire_score_obj.getQuestionnaire_period_of_time_until_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (mode,"exam_questionnaire_score.QUESTIONNAIRE_DATE_ID","dt_date", "1800", "1", "1", examination_questionnaire_score_obj.getQuestionnaire_period_of_time_until_year(), examination_questionnaire_score_obj.getQuestionnaire_period_of_time_until_month(),
							  examination_questionnaire_score_obj.getQuestionnaire_period_of_time_until_day()); 
				  }
				  
				  /*if(!examination_questionnaire_score_obj.getCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.ID HAVING COUNT(*) >= "+examination_questionnaire_score_obj.getCount();
				  }*/
				  
				  if(!tables.contains("dt_date")) {
					  tables += ", dt_date";
					  where_clause += " AND exam_questionnaire_score.QUESTIONNAIRE_DATE_ID=dt_date.ID";
				  }
				 
				  if(isMax) query = "(SELECT MAX(dt_date.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
				  else query = "(SELECT MIN(dt_date.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
				  
				  if(!examination_questionnaire_score_obj.getYearsNested().isEmpty()) {
					  query += " + ("+examination_questionnaire_score_obj.getYearsNested()+")";
				  }
				  
			  } break;  //examination_essdai_domain
			  
			  case "examination_essdai_domain": { //Check if user provided the info of all the fields 
				  examination_essdai_domain  examination_essdai_domain_obj =  (examination_essdai_domain)current_Criterion;
				  
				  String tables = "patient, exam_essdai_domain";
				  String where_clause = "patient.ID = exam_essdai_domain.PATIENT_ID";
				  
				  if(!examination_essdai_domain_obj.getDomain().isEmpty()) {
					  tables += ", voc_essdai_domain";
					  where_clause += " AND exam_essdai_domain.DOMAIN_ID = voc_essdai_domain.ID AND " + Make_OR_of_CODES("voc_essdai_domain.CODE", examination_essdai_domain_obj.getDomain());
				  }
				  
				  /*query = "SELECT DISTINCT patient.ID " +
						  "FROM patient, exam_essdai_domain, voc_essdai_domain, dt_date, voc_activity_level " + 
						  "WHERE patient.ID = exam_essdai_domain.PATIENT_ID AND " + 
						  "exam_essdai_domain.DOMAIN_ID = voc_essdai_domain.ID " +
						  //"voc_essdai_domain.CODE = '"+examination_essdai_domain_obj.getDomain()+"' ";
				  		  "AND " + Make_OR_of_CODES("voc_essdai_domain.CODE", examination_essdai_domain_obj.getDomain());*/
				 
			  
				  if(!(examination_essdai_domain_obj.getActivity_level()).isEmpty()) {
					  tables += ", voc_activity_level";
					  where_clause += " AND exam_essdai_domain.ACTIVITY_LEVEL_ID = voc_activity_level.ID " +
					  	"AND voc_activity_level.CODE = '" + examination_essdai_domain_obj.getActivity_level() +"' "; //'BLOOD-100'
				  }
				  
				  if(!(examination_essdai_domain_obj.getQuestionnaire_period_of_time_exact_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_specific_date_query(mode, "exam_essdai_domain.QUESTIONNAIRE_DATE_ID","dt_date",examination_essdai_domain_obj.getQuestionnaire_period_of_time_exact_year(), 
							  examination_essdai_domain_obj.getQuestionnaire_period_of_time_exact_month(), examination_essdai_domain_obj.getQuestionnaire_period_of_time_exact_day());					  		
				  } else if(!examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_start_year().isEmpty() || !(examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_end_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (mode,"exam_essdai_domain.QUESTIONNAIRE_DATE_ID", "dt_date",examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_start_year(), 
							  examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_start_month(), examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_start_day(),
							  examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_end_year(), examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_end_month(),
							  examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_end_day()); 			  
				  } else if(!(examination_essdai_domain_obj.getQuestionnaire_period_of_time_until_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (mode,"exam_essdai_domain.QUESTIONNAIRE_DATE_ID","dt_date", "1800", "1", "1", examination_essdai_domain_obj.getQuestionnaire_period_of_time_until_year(), examination_essdai_domain_obj.getQuestionnaire_period_of_time_until_month(),
							  examination_essdai_domain_obj.getQuestionnaire_period_of_time_until_day()); 
				  }
				  
				  /*if(!examination_essdai_domain_obj.getCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.ID HAVING COUNT(*) >= "+examination_essdai_domain_obj.getCount();
				  }*/
				  
				  if(!tables.contains("dt_date")) {
					  tables += ", dt_date";
					  where_clause += " AND exam_essdai_domain.QUESTIONNAIRE_DATE_ID=dt_date.ID";
				  }
				 
				  if(isMax) query = "(SELECT MAX(dt_date.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
				  else query = "(SELECT MIN(dt_date.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
				  
				  if(!examination_essdai_domain_obj.getYearsNested().isEmpty()) {
					  query += " + ("+examination_essdai_domain_obj.getYearsNested()+")";
				  }	
			  } break; //examination_caci_condition
			  
			  case "examination_caci_condition": { //Check if user provided the info of all the fields 
				  examination_caci_condition  examination_caci_condition_obj =  (examination_caci_condition)current_Criterion;
				  
				  String tables = "exam_caci_condition, patient";
				  String where_clause = "patient.ID = exam_caci_condition.PATIENT_ID";
				  
				  if(!examination_caci_condition_obj.getCaci().isEmpty()) {
					  tables += ", voc_caci_condition";
					  where_clause += " AND exam_caci_condition.CACI_ID = voc_caci_condition.ID AND " + Make_OR_of_CODES("voc_caci_condition.CODE", examination_caci_condition_obj.getCaci());
				  }
				  
				  if(!examination_caci_condition_obj.getValue().isEmpty()) {  //TODO check value
					  tables += ", voc_confirmation";
					  where_clause += " AND exam_caci_condition.VALUE_ID = voc_confirmation.ID AND " + Make_OR_of_CODES("voc_confirmation.CODE", examination_caci_condition_obj.getValue());
				  }
				  
				  if(!(examination_caci_condition_obj.getQuestionnaire_period_of_time_exact_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_specific_date_query(mode, "exam_caci_condition.QUESTIONNAIRE_DATE_ID","dt_date",examination_caci_condition_obj.getQuestionnaire_period_of_time_exact_year(), 
							  examination_caci_condition_obj.getQuestionnaire_period_of_time_exact_month(), examination_caci_condition_obj.getQuestionnaire_period_of_time_exact_day());					  		
				  } else if(!examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_start_year().isEmpty() || !(examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_end_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (mode,"exam_caci_condition.QUESTIONNAIRE_DATE_ID", "dt_date",examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_start_year(), 
							  examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_start_month(), examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_start_day(),
							  examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_end_year(), examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_end_month(),
							  examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_end_day()); 			  
				  } else if(!(examination_caci_condition_obj.getQuestionnaire_period_of_time_until_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (mode,"exam_caci_condition.QUESTIONNAIRE_DATE_ID","dt_date", "1800", "1", "1", examination_caci_condition_obj.getQuestionnaire_period_of_time_until_year(), examination_caci_condition_obj.getQuestionnaire_period_of_time_until_month(),
							  examination_caci_condition_obj.getQuestionnaire_period_of_time_until_day()); 
				  }
				  
				  /*if(!examination_caci_condition_obj.getCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.ID HAVING COUNT(*) >= "+examination_caci_condition_obj.getCount();
				  }*/
				  
				  if(!tables.contains("dt_date")) {
					  tables += ", dt_date";
					  where_clause += " AND exam_caci_condition.QUESTIONNAIRE_DATE_ID=dt_date.ID";
				  }
				 
				  if(isMax) query = "(SELECT MAX(dt_date.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
				  else query = "(SELECT MIN(dt_date.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
				  
				  if(!examination_caci_condition_obj.getYearsNested().isEmpty()) {
					  query += " + ("+examination_caci_condition_obj.getYearsNested()+")";
				  }	
			  } break; //other_healthcare_visit
			  
			  case "other_healthcare_visit": { //Check if user provided the info of all the fields 
				  other_healthcare_visit  other_healthcare_visit_obj =  (other_healthcare_visit)current_Criterion;
				  
				  String tables = "patient, other_healthcare_visit";
				  String where_clause = "patient.ID = other_healthcare_visit.PATIENT_ID";
				  
				  if(!other_healthcare_visit_obj.getSpecialist().isEmpty()) {
					  tables += ", voc_specialist";
					  where_clause += " AND other_healthcare_visit.SPECIALIST_ID=voc_specialist.ID AND " + Make_OR_of_CODES("voc_specialist.CODE", other_healthcare_visit_obj.getSpecialist());
				  }
				  
				  /*query = "SELECT DISTINCT patient.ID " +
						  "FROM other_healthcare_visit, patient, dt_date, voc_specialist " + //interv_Surgery, dt_date, voc_direction, voc_confirmation
						  "WHERE patient.ID = other_healthcare_visit.PATIENT_ID AND " + 
						  "other_healthcare_visit.SPECIALIST_ID=voc_specialist.ID " +
				  		  "AND " + Make_OR_of_CODES("voc_specialist.CODE", other_healthcare_visit_obj.getSpecialist());*/
				 // System.out.println("Test id: "+other_healthcare_visit_obj.getSpecialist());
				  
				  if(!(other_healthcare_visit_obj.getPeriod_of_time_exact_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_specific_date_query(mode, "other_healthcare_visit.DATE_ID","dt_date",other_healthcare_visit_obj.getPeriod_of_time_exact_year(), 
							  other_healthcare_visit_obj.getPeriod_of_time_exact_month(), other_healthcare_visit_obj.getPeriod_of_time_exact_day());					  		
				  } else if(!other_healthcare_visit_obj.getPeriod_of_time_interval_start_year().isEmpty() || !(other_healthcare_visit_obj.getPeriod_of_time_interval_end_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (mode,"other_healthcare_visit.DATE_ID", "dt_date",other_healthcare_visit_obj.getPeriod_of_time_interval_start_year(), 
							  other_healthcare_visit_obj.getPeriod_of_time_interval_start_month(), other_healthcare_visit_obj.getPeriod_of_time_interval_start_day(),
							  other_healthcare_visit_obj.getPeriod_of_time_interval_end_year(), other_healthcare_visit_obj.getPeriod_of_time_interval_end_month(),
							  other_healthcare_visit_obj.getPeriod_of_time_interval_end_day()); 			  
				  } else if(!(other_healthcare_visit_obj.getPeriod_of_time_until_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (mode,"other_healthcare_visit.DATE_ID","dt_date", "1800", "1", "1", other_healthcare_visit_obj.getPeriod_of_time_until_year(), other_healthcare_visit_obj.getPeriod_of_time_until_month(),
							  other_healthcare_visit_obj.getPeriod_of_time_until_day()); 
				  }
				  
				  /*if(!other_healthcare_visit_obj.getCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.ID HAVING COUNT(*) >= "+other_healthcare_visit_obj.getCount();
				  }*/
				  
				  if(!tables.contains("dt_date")) {
					  tables += ", dt_date";
					  where_clause += " AND other_healthcare_visit.DATE_ID=dt_date.ID";
				  }
				 
				  if(isMax) query = "(SELECT MAX(dt_date.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
				  else query = "(SELECT MIN(dt_date.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
				  
				  if(!other_healthcare_visit_obj.getYearsNested().isEmpty()) {
					  query += " + ("+other_healthcare_visit_obj.getYearsNested()+")";
				  }	
				  
			  } break;			
			  
			  case "other_clinical_trials": { //Check if user provided the info of all the fields 
				  other_clinical_trials  other_clinical_trials_obj =  (other_clinical_trials)current_Criterion;
				  
				  String tables = "patient, other_clinical_trials";
				  String where_clause = "patient.ID = other_clinical_trials.PATIENT_ID";
						  
				  if(!(other_clinical_trials_obj.getPeriod_of_time_exact_year()).isEmpty()) {
						tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
						where_clause += Make_begin_end_period_query(mode, "other_clinical_trials.PERIOD_ID","dt_date1","dt_date2",other_clinical_trials_obj.getPeriod_of_time_exact_year(), 
								  other_clinical_trials_obj.getPeriod_of_time_exact_month(), other_clinical_trials_obj.getPeriod_of_time_exact_day(),other_clinical_trials_obj.getPeriod_of_time_exact_year(), 
								  other_clinical_trials_obj.getPeriod_of_time_exact_month(), other_clinical_trials_obj.getPeriod_of_time_exact_day());					  		
					} else if(!other_clinical_trials_obj.getPeriod_of_time_interval_start_year().isEmpty() || !(other_clinical_trials_obj.getPeriod_of_time_interval_end_year()).isEmpty()) {
						tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
						where_clause += Make_begin_end_period_query (mode,"other_clinical_trials.PERIOD_ID", "dt_date1","dt_date2",other_clinical_trials_obj.getPeriod_of_time_interval_start_year(), 
								  other_clinical_trials_obj.getPeriod_of_time_interval_start_month(), other_clinical_trials_obj.getPeriod_of_time_interval_start_day(),
								  other_clinical_trials_obj.getPeriod_of_time_interval_end_year(), other_clinical_trials_obj.getPeriod_of_time_interval_end_month(),
								  other_clinical_trials_obj.getPeriod_of_time_interval_end_day()); 			  
					} else if(!(other_clinical_trials_obj.getPeriod_of_time_until_year()).isEmpty()) {
						tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
						where_clause += Make_begin_end_period_query ( mode,"other_clinical_trials.PERIOD_ID","dt_date1","dt_date2", "1800", "1", "1", other_clinical_trials_obj.getPeriod_of_time_until_year(), other_clinical_trials_obj.getPeriod_of_time_until_month(),
								  other_clinical_trials_obj.getPeriod_of_time_until_day()); 
					}
			
			if(!other_clinical_trials_obj.getStatement().isEmpty()) 
		  		query += "AND other_clinical_trials.STMT_ID=voc_confirmation.ID " +
		  				 "AND voc_confirmation.CODE='"+other_clinical_trials_obj.getStatement() + "'";
			
			where_clause += " AND other_clinical_trials.STMT_ID=1";
			
			 /*if(!other_clinical_trials_obj.getCount().isEmpty()) {
			  		where_clause += " GROUP BY patient.ID HAVING COUNT(*) >= "+other_clinical_trials_obj.getCount();
			  }*/
			
			if(isMax) {
				  if(!tables.contains("dt_date")) {
					  tables += ", dt_date AS dt_date1, dt_period_of_time";
					  where_clause += " AND other_clinical_trials.PERIOD_ID=dt_period_of_time.ID AND dt_period_of_time.START_DATE_ID=dt_date1.ID";
				  }
				  query = "(SELECT MAX(dt_date1.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
			  }
			  else {
				  if(!tables.contains("dt_date")) {
					  tables += ", dt_date AS dt_date2, dt_period_of_time";
					  where_clause += " AND other_clinical_trials.PERIOD_ID=dt_period_of_time.ID AND dt_period_of_time.END_DATE_ID=dt_date2.ID";
				  }
				  query = "(SELECT MIN(dt_date2.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID AND " + where_clause +")";
			  }
				  
			  if(!other_clinical_trials_obj.getYearsNested().isEmpty()) {
				  query += " + ("+other_clinical_trials_obj.getYearsNested()+")";
			  }	

			  } break;
			  
			  case "patient": { //Check if user provided the info of all the fields 
				  patient  patient_obj =  (patient)current_Criterion;
				  
				  String tables = "patient";
				  String where_clause = "";
				  
				  /*query = "SELECT DISTINCT patient.ID " +
						  "FROM patient,  dt_date as dt_date1, dt_date as dt_date2, dt_date as dt_date3, dt_date as dt_date4, voc_direction " + //exam_lab_test, voc_lab_test, dt_amount, voc_unit, voc_assessment, dt_amount_range,interv_Surgery, dt_date, voc_direction, voc_confirmation
						  "WHERE "; 
						  //"exam_lab_test.TEST_ID=voc_lab_test.ID " +
						  //"voc_lab_test.CODE='"+crit_exam_lab_test_obj.getTEST_ID_voc_lab_test_CODE()+"' ";
				  		  //"AND " + Make_OR_of_CODES("voc_lab_test.CODE", patient_obj.getBirth_period_of_time_exact_year());
				  System.out.println("Test id: "+patient_obj.getBirth_period_of_time_exact_year());*/
				  if(!patient_obj.get_exact_age().isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  Date date = new Date();
					  Object param = new java.sql.Timestamp(date.getTime());
					  String exact_year = ((Timestamp) param).toString().split("-")[0];
					  Integer exact_birth_year = Integer.valueOf(exact_year) - Integer.valueOf(patient_obj.get_exact_age());
					  exact_year = exact_birth_year.toString();
					  where_clause += Make_specific_date_query(mode, "patient.DATE_OF_BIRTH_ID","dt_date1",exact_year,"","");
				  }
				  else if(!patient_obj.get_max_age().isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  Date date = new Date();
					  Object param = new java.sql.Timestamp(date.getTime());
					  String max_year = ((Timestamp) param).toString().split("-")[0];
					  String min_year = max_year;
					  Integer max_birth_year;
					  if(!patient_obj.get_min_age().isEmpty()) {
						  max_birth_year= Integer.valueOf(max_year) - Integer.valueOf(patient_obj.get_min_age());
						  max_year = max_birth_year.toString();
					  }
					  else max_year = "";
					  
					  Integer min_birth_year = Integer.valueOf(min_year) - Integer.valueOf(patient_obj.get_max_age());
					  min_year = min_birth_year.toString();
					  where_clause += Make_begin_end_date_query (mode,"patient.DATE_OF_BIRTH_ID", "dt_date1",min_year, "", "", max_year,"",""); 		
				  }
				  else if(!patient_obj.get_min_age().isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  Date date = new Date();
					  Object param = new java.sql.Timestamp(date.getTime());
					  String max_year = ((Timestamp) param).toString().split("-")[0];
					  System.out.println(max_year);
					  Integer max_birth_year = Integer.valueOf(max_year) - Integer.valueOf(patient_obj.get_min_age());
					  max_year = max_birth_year.toString();
					  System.out.println(max_year);
					  where_clause += Make_begin_end_date_query (mode, "patient.DATE_OF_BIRTH_ID", "dt_date1","1800", "1", "1", max_year,"",""); 		
				  }
				  
				  if(!patient_obj.getBirth_period_of_time_exact_year().isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  where_clause += Make_specific_date_query(mode, "patient.DATE_OF_BIRTH_ID","dt_date1",patient_obj.getBirth_period_of_time_exact_year(),
							  patient_obj.getBirth_period_of_time_exact_month(),patient_obj.getBirth_period_of_time_exact_day());					  		
				  }else if(!patient_obj.getBirth_period_of_time_interval_start_year().isEmpty() || !patient_obj.getBirth_period_of_time_interval_end_year().isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  where_clause += Make_begin_end_date_query (mode,"patient.DATE_OF_BIRTH_ID", "dt_date1",patient_obj.getBirth_period_of_time_interval_start_year(), patient_obj.getBirth_period_of_time_interval_start_month(), patient_obj.getBirth_period_of_time_interval_start_day(), patient_obj.getBirth_period_of_time_interval_end_year(), patient_obj.getBirth_period_of_time_interval_end_month(),
							  patient_obj.getBirth_period_of_time_interval_end_day()); 			  
				  } else if(!patient_obj.getBirth_period_of_time_until_year().isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  where_clause += Make_begin_end_date_query (mode,"patient.DATE_OF_BIRTH_ID","dt_date1", "1800", "1", "1", patient_obj.getBirth_period_of_time_until_year(), 
							  patient_obj.getBirth_period_of_time_until_month(), patient_obj.getBirth_period_of_time_until_day()); 
				  }
				  
				  if(!patient_obj.getSymptoms_onset_period_of_time_exact_year().isEmpty()) {
					  tables += ", dt_date as dt_date2";
					  where_clause += Make_specific_date_query(mode, "patient.PSS_SYMPTOMS_ONSET_DATE_ID","dt_date2",patient_obj.getSymptoms_onset_period_of_time_exact_year(),
							  patient_obj.getSymptoms_onset_period_of_time_exact_month(),patient_obj.getSymptoms_onset_period_of_time_exact_day());					  		
				  } else if(!patient_obj.getSymptoms_onset_period_of_time_interval_start_year().isEmpty() || !patient_obj.getSymptoms_onset_period_of_time_interval_end_year().isEmpty()) {
					  tables += ", dt_date as dt_date2";
					  where_clause += Make_begin_end_date_query (mode,"patient.PSS_SYMPTOMS_ONSET_DATE_ID", "dt_date2",patient_obj.getSymptoms_onset_period_of_time_interval_start_year(), patient_obj.getSymptoms_onset_period_of_time_interval_start_month(), patient_obj.getSymptoms_onset_period_of_time_interval_start_day(), patient_obj.getSymptoms_onset_period_of_time_interval_end_year(), patient_obj.getSymptoms_onset_period_of_time_interval_end_month(),
							  patient_obj.getSymptoms_onset_period_of_time_interval_end_day()); 			  
				  } else if(!patient_obj.getSymptoms_onset_period_of_time_until_year().isEmpty()) {
					  tables += ", dt_date as dt_date2";
					 /* where_clause += Make_until_date_query("patient.PSS_SYMPTOMS_ONSET_DATE_ID", "dt_date2", patient_obj.getSymptoms_onset_period_of_time_until_year(), 
							  patient_obj.getSymptoms_onset_period_of_time_until_month(), patient_obj.getSymptoms_onset_period_of_time_until_day());*/
					  where_clause += Make_begin_end_date_query (mode,"patient.PSS_SYMPTOMS_ONSET_DATE_ID","dt_date2", "1800", "1", "1", patient_obj.getSymptoms_onset_period_of_time_until_year(), 
							  patient_obj.getSymptoms_onset_period_of_time_until_month(), patient_obj.getSymptoms_onset_period_of_time_until_day()); 
				  }
				  
				  if(!patient_obj.getDiagnosis_period_of_time_exact_year().isEmpty()) {
					  tables += ", dt_date as dt_date3";
					  where_clause += Make_specific_date_query(mode, "patient.PSS_DIAGNOSIS_DATE_ID","dt_date3",patient_obj.getDiagnosis_period_of_time_exact_year(),
							  patient_obj.getDiagnosis_period_of_time_exact_month(),patient_obj.getDiagnosis_period_of_time_exact_day());					  		
				  } else if(!patient_obj.getDiagnosis_period_of_time_interval_start_year().isEmpty() || !patient_obj.getDiagnosis_period_of_time_interval_end_year().isEmpty()) {
					  tables += ", dt_date as dt_date3";
					  where_clause += Make_begin_end_date_query (mode,"patient.PSS_DIAGNOSIS_DATE_ID", "dt_date3",patient_obj.getDiagnosis_period_of_time_interval_start_year(), patient_obj.getDiagnosis_period_of_time_interval_start_month(), patient_obj.getDiagnosis_period_of_time_interval_start_day(), patient_obj.getDiagnosis_period_of_time_interval_end_year(), patient_obj.getDiagnosis_period_of_time_interval_end_month(),
							  patient_obj.getDiagnosis_period_of_time_interval_end_day()); 			  
				  } else if(!patient_obj.getDiagnosis_period_of_time_until_year().isEmpty()) {
					  tables += ", dt_date as dt_date3";
					  /*where_clause += Make_until_date_query("patient.PSS_DIAGNOSIS_DATE_ID", "dt_date3", patient_obj.getDiagnosis_period_of_time_until_year(), 
							  patient_obj.getDiagnosis_period_of_time_until_month(), patient_obj.getDiagnosis_period_of_time_until_day());*/
					  where_clause += Make_begin_end_date_query (mode,"patient.PSS_DIAGNOSIS_DATE_ID","dt_date3", "1800", "1", "1", patient_obj.getDiagnosis_period_of_time_until_year(), 
							  patient_obj.getDiagnosis_period_of_time_until_month(), patient_obj.getDiagnosis_period_of_time_until_day()); 
				  }
				  
				  if(!patient_obj.getCohort_inclusion_period_of_time_exact_year().isEmpty()) {
					  tables += ", dt_date as dt_date4";
					  where_clause += Make_specific_date_query(mode, "patient.COHORT_INCLUSION_DATE_ID","dt_date4",patient_obj.getCohort_inclusion_period_of_time_exact_year(),
							  patient_obj.getCohort_inclusion_period_of_time_exact_month(),patient_obj.getCohort_inclusion_period_of_time_exact_day());					  		
				  } else if(!patient_obj.getCohort_inclusion_period_of_time_interval_start_year().isEmpty() || !patient_obj.getCohort_inclusion_period_of_time_interval_end_year().isEmpty()) {
					  tables += ", dt_date as dt_date4";
					  where_clause += Make_begin_end_date_query (mode,"patient.COHORT_INCLUSION_DATE_ID", "dt_date4",patient_obj.getCohort_inclusion_period_of_time_interval_start_year(), patient_obj.getCohort_inclusion_period_of_time_interval_start_month(), patient_obj.getCohort_inclusion_period_of_time_interval_start_day(), patient_obj.getCohort_inclusion_period_of_time_interval_end_year(), patient_obj.getCohort_inclusion_period_of_time_interval_end_month(),
							  patient_obj.getCohort_inclusion_period_of_time_interval_end_day()); 			  
				  } else if(!patient_obj.getCohort_inclusion_period_of_time_until_year().isEmpty()) {
					  tables += ", dt_date as dt_date4";
					  where_clause += Make_begin_end_date_query (mode,"patient.COHORT_INCLUSION_DATE_ID","dt_date4", "1800", "1", "1", patient_obj.getCohort_inclusion_period_of_time_until_year(), 
							  patient_obj.getCohort_inclusion_period_of_time_until_month(), patient_obj.getCohort_inclusion_period_of_time_until_day()); 
					  /*where_clause += Make_begin_end_date_query (mode,"patient.COHORT_INCLUSION_DATE_ID","dt_date4", "1800", "1", "1", patient_obj.getCohort_inclusion_period_of_time_until_year(), 
							  patient_obj.getCohort_inclusion_period_of_time_until_month(), patient_obj.getCohort_inclusion_period_of_time_until_day()); */
				  }
				  
				  if(!patient_obj.get_exact_age_of_cohort_inclusion().isEmpty()) {
					  if(!tables.contains("dt_date1")) tables += ", dt_date as dt_date1";
					  if(!tables.contains("dt_date4")) tables += ", dt_date as dt_date4";
					  where_clause += Make_specific_age_query(true, mode, "patient.DATE_OF_BIRTH_ID", "patient.COHORT_INCLUSION_DATE_ID","dt_date1","dt_date4",patient_obj.get_exact_age_of_cohort_inclusion());
				  }
				  else if(!patient_obj.get_min_age_of_cohort_inclusion().isEmpty()) {
					  if(!tables.contains("dt_date1")) tables += ", dt_date as dt_date1";
					  if(!tables.contains("dt_date4")) tables += ", dt_date as dt_date4";
					  if(!patient_obj.get_max_age_of_cohort_inclusion().isEmpty()) {
						  where_clause += Make_begin_end_age_query (mode, "patient.DATE_OF_BIRTH_ID", "patient.COHORT_INCLUSION_DATE_ID", "dt_date1", "dt_date4", patient_obj.get_min_age_of_cohort_inclusion(), patient_obj.get_max_age_of_cohort_inclusion()); 	
					  }
					  else where_clause += Make_begin_end_age_query (mode, "patient.DATE_OF_BIRTH_ID", "patient.COHORT_INCLUSION_DATE_ID", "dt_date1", "dt_date4", patient_obj.get_min_age_of_cohort_inclusion(), "200");
				  }
				  else if(!patient_obj.get_max_age_of_cohort_inclusion().isEmpty()) {
					  if(!tables.contains("dt_date1")) tables += ", dt_date as dt_date1";
					  if(!tables.contains("dt_date4")) tables += ", dt_date as dt_date4";
					  where_clause += Make_begin_end_age_query (mode, "patient.DATE_OF_BIRTH_ID", "patient.COHORT_INCLUSION_DATE_ID", "dt_date1", "dt_date4", "0", patient_obj.get_max_age_of_cohort_inclusion());
				  }
				  
				  if(!patient_obj.get_exact_age_of_diagnosis().isEmpty()) {
					  if(!tables.contains("dt_date1")) tables += ", dt_date as dt_date1";
					  if(!tables.contains("dt_date3")) tables += ", dt_date as dt_date3";
					  where_clause += Make_specific_age_query(true, mode, "patient.DATE_OF_BIRTH_ID", "patient.PSS_DIAGNOSIS_DATE_ID","dt_date1","dt_date3",patient_obj.get_exact_age_of_diagnosis());
				  }
				  else if(!patient_obj.get_min_age_of_diagnosis().isEmpty()) {
					  if(!tables.contains("dt_date1")) tables += ", dt_date as dt_date1";
					  if(!tables.contains("dt_date3")) tables += ", dt_date as dt_date3";
					  if(!patient_obj.get_max_age_of_diagnosis().isEmpty()) {
						  where_clause += Make_begin_end_age_query (mode, "patient.DATE_OF_BIRTH_ID", "patient.PSS_DIAGNOSIS_DATE_ID", "dt_date1", "dt_date3", patient_obj.get_min_age_of_diagnosis(), patient_obj.get_max_age_of_diagnosis()); 	
					  }
					  else where_clause += Make_begin_end_age_query (mode, "patient.DATE_OF_BIRTH_ID", "patient.PSS_DIAGNOSIS_DATE_ID", "dt_date1", "dt_date3", patient_obj.get_min_age_of_diagnosis(), "200");
				  }
				  else if(!patient_obj.get_max_age_of_diagnosis().isEmpty()) {
					  if(!tables.contains("dt_date1")) tables += ", dt_date as dt_date1";
					  if(!tables.contains("dt_date3")) tables += ", dt_date as dt_date3";
					  where_clause += Make_begin_end_age_query (mode, "patient.DATE_OF_BIRTH_ID", "patient.PSS_DIAGNOSIS_DATE_ID", "dt_date1", "dt_date3", "0", patient_obj.get_max_age_of_diagnosis());
				  }
				  
				  if(!patient_obj.get_exact_age_of_sign().isEmpty()) {
					  if(!tables.contains("dt_date1")) tables += ", dt_date as dt_date1";
					  if(!tables.contains("dt_date2")) tables += ", dt_date as dt_date2";
					  where_clause += Make_specific_age_query(true, mode, "patient.DATE_OF_BIRTH_ID", "patient.PSS_SYMPTOMS_ONSET_DATE_ID","dt_date1","dt_date2",patient_obj.get_exact_age_of_sign());
				  }
				  else if(!patient_obj.get_min_age_of_sign().isEmpty()) {
					  if(!tables.contains("dt_date1")) tables += ", dt_date as dt_date1";
					  if(!tables.contains("dt_date2")) tables += ", dt_date as dt_date2";
					  if(!patient_obj.get_max_age_of_sign().isEmpty()) {
						  where_clause += Make_begin_end_age_query (mode, "patient.DATE_OF_BIRTH_ID", "patient.PSS_SYMPTOMS_ONSET_DATE_ID", "dt_date1", "dt_date2", patient_obj.get_min_age_of_sign(), patient_obj.get_max_age_of_sign()); 	
					  }
					  else where_clause += Make_begin_end_age_query (mode, "patient.DATE_OF_BIRTH_ID", "patient.PSS_SYMPTOMS_ONSET_DATE_ID", "dt_date1", "dt_date2", patient_obj.get_min_age_of_sign(), "200");
				  }
				  else if(!patient_obj.get_max_age_of_sign().isEmpty()) {
					  if(!tables.contains("dt_date1")) tables += ", dt_date as dt_date1";
					  if(!tables.contains("dt_date2")) tables += ", dt_date as dt_date2";
					  where_clause += Make_begin_end_age_query (mode, "patient.DATE_OF_BIRTH_ID", "patient.PSS_SYMPTOMS_ONSET_DATE_ID", "dt_date1", "dt_date2", "0", patient_obj.get_max_age_of_sign());
				  }
				  
				  if(patient_obj.get_type_nested().equals("birth_date")) {
				  		if(!tables.contains("dt_date1")) {
				  			tables += ", dt_date as dt_date1";
				  			where_clause += " AND patient.DATE_OF_BIRTH_ID=dt_date1.ID";
				  		}	
				  		if(isMax) query = "(SELECT MAX(dt_date1.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID " + where_clause +")";
						else query = "(SELECT MIN(dt_date1.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID " + where_clause +")";
				  		if(!patient_obj.get_years_birth_nested().isEmpty()) {
							query += " + ("+patient_obj.get_years_birth_nested()+")";
						}  
				  	}
				  
				  else if(patient_obj.get_type_nested().equals("symptoms_onset")) {
				  		if(!tables.contains("dt_date2")) {
				  			tables += ", dt_date as dt_date2";
				  			where_clause += " AND patient.PSS_SYMPTOMS_ONSET_DATE_ID=dt_date2.ID";
				  		}
				  		if(isMax) query = "(SELECT MAX(dt_date2.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID " + where_clause +")";
						else query = "(SELECT MIN(dt_date2.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID " + where_clause +")";
				  		if(!patient_obj.get_years_symptoms_onset_nested().isEmpty()) {
							query += " + ("+patient_obj.get_years_symptoms_onset_nested()+")";
						} 
				  	}
				  
				  else if(patient_obj.get_type_nested().equals("diagnosis")) {
				  		if(!tables.contains("dt_date3")) {
				  			tables += ", dt_date as dt_date3";
				  			where_clause += " AND patient.PSS_DIAGNOSIS_DATE_ID=dt_date3.ID";
				  		}	
				  		if(isMax) query = "(SELECT MAX(dt_date3.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID " + where_clause +")";
						else query = "(SELECT MIN(dt_date3.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID " + where_clause +")";
				  		if(!patient_obj.get_years_diagnosis_nested().isEmpty()) {
							query += " + ("+patient_obj.get_years_diagnosis_nested()+")";
						}  
				  	}
				  	
				  else if(patient_obj.get_type_nested().equals("cohort_inclusion")) {
				  		if(!tables.contains("dt_date4")) {
				  			tables += ", dt_date as dt_date4";
				  			where_clause += " AND patient.COHORT_INCLUSION_DATE_ID=dt_date4.ID";
				  		}
				  		if(isMax) query = "(SELECT MAX(dt_date4.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID " + where_clause +")";
						else query = "(SELECT MIN(dt_date4.YEAR) FROM " + tables + " WHERE patient.ID=outerr.ID " + where_clause +")";
				  		if(!patient_obj.get_years_cohort_inclusion_nested().isEmpty()) {
							query += " + ("+patient_obj.get_years_cohort_inclusion_nested()+")";
						} 
				  	}
				  
			  } break;
			  
			  default:
				  System.out.println("Undefined criterion-name-"+(i+1)+" in the input JSON file.");
			} 
			query=query.replace("WHERE  AND", "WHERE");
			query=query.replace("WHERE AND", "WHERE");
			if(!oneDatePregnancy) {
				oneDatePregnancy=true;
				String[] twoQueries = query.split("#");
				nestedCriteria.add(twoQueries[0]);
				nestedCriteria.add(twoQueries[1]);
			}
			else nestedCriteria.add(query);
    	}
    	return nestedCriteria;
    }
    
    private void createQuery(ArrayList<Criterion> list_of_criterions, boolean incl, boolean mode/*, String results_of_all_Criterions*/) throws JSONException, JsonParseException, JsonMappingException, IOException {
    	//String results_of_one_Criterion="";
    	for(int i=0; i<list_of_criterions.size(); i++) {
			Criterion current_Criterion=list_of_criterions.get(i); //current_criterion
			String query="";
			switch(current_Criterion.getCriterion()) { //The name of the Criterion.
			  
			case "demographics_gender": {//changed 12/5
				  query = "SELECT patient.ID " + 
						  "FROM patient, demo_sex_data, voc_sex " + 
						  "WHERE patient.ID=demo_sex_data.PATIENT_ID " + 
						  "AND demo_sex_data.SEX_ID=voc_sex.ID ";
				  if(incl) query += "AND " + Make_OR_of_CODES("voc_sex.CODE", ((demographics_gender) current_Criterion).gender); //Make_OR_of_CODES("","");
				  else query += "AND NOT(" + Make_OR_of_CODES("voc_sex.CODE", ((demographics_gender) current_Criterion).gender)+")"; //Make_OR_of_CODES("","");
			  } break;
			    
			  case "demographics_ethnicity": { 
				   query = "SELECT patient.ID " + 
						   "FROM patient, demo_ethnicity_data, voc_ethnicity " + 
						   "WHERE patient.ID=demo_ethnicity_data.PATIENT_ID " + 
						   "AND demo_ethnicity_data.ETHNICITY_ID=voc_ethnicity.ID ";
				   
				   if(incl) query += "AND " + Make_OR_of_CODES("voc_ethnicity.CODE", ((demographics_ethnicity) current_Criterion).ethnicity);
				   else query += "AND NOT(" + Make_OR_of_CODES("voc_ethnicity.CODE", ((demographics_ethnicity) current_Criterion).ethnicity)+")";
			  } break;
			    
			  case "demographics_education": {
				   query = "SELECT patient.ID " + 
						   "FROM patient, demo_education_level_data, voc_education_level " + 
						   "WHERE patient.ID=demo_education_level_data.PATIENT_ID " + 
						   "AND demo_education_level_data.EDUCATION_LEVEL_ID=voc_education_level.ID ";
				   if(incl) query += "AND " + Make_OR_of_CODES("voc_education_level.CODE", ((demographics_education) current_Criterion).education_level);
				   else query += "AND NOT(" + Make_OR_of_CODES("voc_education_level.CODE", ((demographics_education) current_Criterion).education_level)+")";
			  } break;
				    
			  case "demographics_weight": {
				  query = "SELECT patient.ID " + 
						  "FROM patient, demo_weight_data, voc_bmi_class " + 
				  		  "WHERE patient.ID=demo_weight_data.PATIENT_ID " + 
				  		  "AND demo_weight_data.BMI_CLASS_ID=voc_bmi_class.ID ";
				  if(incl) query += "AND " + Make_OR_of_CODES("voc_bmi_class.CODE", ((demographics_weight) current_Criterion).body_mass_index);
				  else query += "AND NOT(" + Make_OR_of_CODES("voc_bmi_class.CODE", ((demographics_weight) current_Criterion).body_mass_index)+")";
			  } break;
			  
			  case "demographics_occupation": {
				  query = "SELECT patient.ID " + 
						  "FROM patient, demo_occupation_data, voc_confirmation " + 
						  "WHERE patient.ID=demo_occupation_data.PATIENT_ID " + 
						  "AND demo_occupation_data.LOSS_OF_WORK_DUE_TO_PSS_ID = voc_confirmation.ID";
				  if(incl) query += " AND " + Make_OR_of_CODES("voc_confirmation.CODE", ((demographics_occupation) current_Criterion).loss_of_work_due_to_pss);
				  else query += " AND NOT(" + Make_OR_of_CODES("voc_confirmation.CODE", ((demographics_occupation) current_Criterion).loss_of_work_due_to_pss)+")";
			  } break;
				    
			  case "demographics_pregnancy": { //Check if user provided the info of all the fields 
				  demographics_pregnancy crit_demo_pregnancy_obj =  (demographics_pregnancy)current_Criterion;
				  String tables = "patient, demo_pregnancy_data";
				  String where_clause = "patient.ID = demo_pregnancy_data.PATIENT_ID";
				  
				  if(!crit_demo_pregnancy_obj.outcome_coded_value.isEmpty()) {
					  tables += ", voc_pregnancy_outcome";
					  where_clause += " AND demo_pregnancy_data.OUTCOME_ID=voc_pregnancy_outcome.ID";
					  where_clause += " AND (" + Make_OR_of_CODES("voc_pregnancy_outcome.CODE", crit_demo_pregnancy_obj.outcome_coded_value); 
					  String codes[] = crit_demo_pregnancy_obj.getOutcome_coded_value().split(",");
					  for(int k=0; k<codes.length; k++) {
						  String narrowTerms = getTermsWithNarrowMeaning(codes[k].trim());
						  String[] allNarrowTerms = narrowTerms.split(",");
						  for(int c=1; c<allNarrowTerms.length; c++) {
							  where_clause += " OR " + Make_OR_of_CODES("voc_pregnancy_outcome.CODE", allNarrowTerms[c]);
						  }
					  }
					  where_clause += ")";
				  }
				  
				  if(!(crit_demo_pregnancy_obj.getCONCEPTION_DATE_YEAR()/* + crit_demo_pregnancy_obj.conception_exact_month + 
				  	crit_demo_pregnancy_obj.conception_exact_day */).isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  where_clause += Make_specific_date_query(mode, "demo_pregnancy_data.CONCEPTION_DATE_ID","dt_date1",crit_demo_pregnancy_obj.getCONCEPTION_DATE_YEAR(),
							  crit_demo_pregnancy_obj.getCONCEPTION_DATE_MONTH(),crit_demo_pregnancy_obj.getCONCEPTION_DATE_DAY());					  		
				  
				  } else if(!(crit_demo_pregnancy_obj.getCONCEPTION_period_begin_year()/* + crit_demo_pregnancy_obj.getCONCEPTION_period_begin_month() + crit_demo_pregnancy_obj.getCONCEPTION_period_begin_day()*/).isEmpty() || !(crit_demo_pregnancy_obj.getCONCEPTION_period_end_year()).isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  where_clause += Make_begin_end_date_query (mode,"demo_pregnancy_data.CONCEPTION_DATE_ID", "dt_date1",crit_demo_pregnancy_obj.getCONCEPTION_period_begin_year(), crit_demo_pregnancy_obj.getCONCEPTION_period_begin_month(), crit_demo_pregnancy_obj.getCONCEPTION_period_begin_day(), crit_demo_pregnancy_obj.getCONCEPTION_period_end_year(), crit_demo_pregnancy_obj.getCONCEPTION_period_end_month(),
							  crit_demo_pregnancy_obj.getCONCEPTION_period_end_day());			  
				  
				  } else if(!(crit_demo_pregnancy_obj.getCONCEPTION_until_date_year() + crit_demo_pregnancy_obj.getCONCEPTION_until_date_month() + crit_demo_pregnancy_obj.getCONCEPTION_until_date_day()).isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  where_clause += Make_begin_end_date_query (mode,"demo_pregnancy_data.CONCEPTION_DATE_ID","dt_date1", "1800", "1", "1", crit_demo_pregnancy_obj.getCONCEPTION_until_date_year(), 
							  crit_demo_pregnancy_obj.getCONCEPTION_until_date_month(), crit_demo_pregnancy_obj.getCONCEPTION_until_date_day()); 
				  }
				  
				  if(!(crit_demo_pregnancy_obj.outcome_exact_year/* + crit_demo_pregnancy_obj.outcome_exact_month + 
				  	crit_demo_pregnancy_obj.outcome_exact_day*/).isEmpty()) {
					  tables += ", dt_date as dt_date2";
					  where_clause += Make_specific_date_query(mode, "demo_pregnancy_data.OUTCOME_DATE_ID","dt_date2", crit_demo_pregnancy_obj.getOUTCOME_DATE_YEAR(),
							  crit_demo_pregnancy_obj.getOUTCOME_DATE_MONTH(),crit_demo_pregnancy_obj.getOUTCOME_DATE_DAY());	
					 
				  	} else if(!(crit_demo_pregnancy_obj.getOUTCOME_period_begin_year()/* + crit_demo_pregnancy_obj.getOUTCOME_period_begin_month() + crit_demo_pregnancy_obj.getOUTCOME_period_begin_day()*/).isEmpty() || !(crit_demo_pregnancy_obj.getOUTCOME_period_end_year()).isEmpty()) {
				  		tables += ", dt_date as dt_date2"; 
				  		where_clause += Make_begin_end_date_query (mode,"demo_pregnancy_data.OUTCOME_DATE_ID", "dt_date2", crit_demo_pregnancy_obj.getOUTCOME_period_begin_year(), crit_demo_pregnancy_obj.getOUTCOME_period_begin_month(), crit_demo_pregnancy_obj.getOUTCOME_period_begin_day(), crit_demo_pregnancy_obj.getOUTCOME_period_end_year(), crit_demo_pregnancy_obj.getOUTCOME_period_end_month(),
								  crit_demo_pregnancy_obj.getOUTCOME_period_end_day()); 			  
					
				  	} else if(!(crit_demo_pregnancy_obj.getOUTCOME_until_date_year() + crit_demo_pregnancy_obj.getOUTCOME_until_date_month() + crit_demo_pregnancy_obj.getOUTCOME_until_date_day()).isEmpty()) {
				  		tables += ", dt_date as dt_date2"; 
				  		where_clause += Make_begin_end_date_query (mode,"demo_pregnancy_data.OUTCOME_DATE_ID", "dt_date2", "1800", "1", "1", crit_demo_pregnancy_obj.getOUTCOME_until_date_year(), 
								  crit_demo_pregnancy_obj.getOUTCOME_until_date_month(), crit_demo_pregnancy_obj.getOUTCOME_until_date_day()); 
					}
					  

				  	// sos we have mode 
				  	if(!crit_demo_pregnancy_obj.outcome_ss_related.isEmpty()&&!mode) {
				  		tables += ", voc_confirmation AS conf_1"; 
				  		where_clause += " AND demo_pregnancy_data.SS_CONCORDANT_ID=conf_1.ID AND conf_1.CODE='" + crit_demo_pregnancy_obj.outcome_ss_related + "'";
				  	}
				  	else if(!crit_demo_pregnancy_obj.outcome_ss_related.isEmpty()&&mode) {
				  		where_clause += " AND demo_pregnancy_data.SS_CONCORDANT_ID IS NULL"; 
				  	}
				  		
				  	if(!crit_demo_pregnancy_obj.getStatement().isEmpty()) {
				  		tables += ", voc_confirmation AS conf_2";
				  		where_clause += " AND demo_pregnancy_data.STMT_ID=conf_2.ID " +
				  				 "AND conf_2.CODE='"+crit_demo_pregnancy_obj.getStatement() + "'";
				  	}
				  	
				  	where_clause += " AND demo_pregnancy_data.STMT_ID=1";
				  	
				  	if(!crit_demo_pregnancy_obj.getMinCount().isEmpty()) {
				  		if(!crit_demo_pregnancy_obj.getMaxCount().isEmpty()) {
				  			where_clause += " GROUP BY patient.ID HAVING COUNT(*) BETWEEN "+crit_demo_pregnancy_obj.getMinCount()+" AND "+crit_demo_pregnancy_obj.getMaxCount();
				  		}
				  		else where_clause += " GROUP BY patient.ID HAVING COUNT(*) >= "+crit_demo_pregnancy_obj.getMinCount();
					}
				  	else if(!crit_demo_pregnancy_obj.getMaxCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.ID HAVING COUNT(*) <= "+crit_demo_pregnancy_obj.getMaxCount();
					}
				  	
				  	query = "SELECT DISTINCT patient.ID FROM " + tables + " WHERE " + where_clause;
				  	
				  	if(!crit_demo_pregnancy_obj.getOutcomeMaxNested().isEmpty()) {
						  String crit_max_nested = makeCriterionList(crit_demo_pregnancy_obj.getOutcomeMaxNested());
						  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_max_nested);
						  ArrayList<Criterion> list_of_max_nested=null;
						  list_of_max_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
						  ArrayList<String> maxNestedQueries = createNestedQueries(list_of_max_nested, false, true);
						  if(!tables.contains("dt_date2")) {
							  tables += ", dt_date as dt_date2";
							  where_clause += " AND demo_pregnancy_data.OUTCOME_DATE_ID=dt_date2.ID";
						  }
						  for(int k=0; k<maxNestedQueries.size(); k++) {
							  where_clause += " AND (dt_date2.YEAR2 <= "+maxNestedQueries.get(k)+" OR (dt_date2.YEAR2 IS NULL AND dt_date2.YEAR <= "+maxNestedQueries.get(k)+"))";
						  }  
						  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
					  }
					  
				  	if(!crit_demo_pregnancy_obj.getOutcomeMinNested().isEmpty()) {
						  String crit_min_nested = makeCriterionList(crit_demo_pregnancy_obj.getOutcomeMinNested());
						  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_min_nested);
						  ArrayList<Criterion> list_of_min_nested=null;
						  list_of_min_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
						  ArrayList<String> minNestedQueries = createNestedQueries(list_of_min_nested, false, false);
						  if(!tables.contains("dt_date2")) {
							  tables += ", dt_date as dt_date2";
							  where_clause += " AND demo_pregnancy_data.OUTCOME_DATE_ID=dt_date2.ID";
						  }
						  for(int k=0; k<minNestedQueries.size(); k++) {
							  where_clause += " AND dt_date2.YEAR >= "+minNestedQueries.get(k);
						  }  
						  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
					  }
				  	
				  	if(!crit_demo_pregnancy_obj.getConceptionMaxNested().isEmpty()) {
						  String crit_max_nested = makeCriterionList(crit_demo_pregnancy_obj.getConceptionMaxNested());
						  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_max_nested);
						  ArrayList<Criterion> list_of_max_nested=null;
						  list_of_max_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
						  ArrayList<String> maxNestedQueries = createNestedQueries(list_of_max_nested, false, true);
						  if(!tables.contains("dt_date1")) {
							  tables += ", dt_date as dt_date1";
							  where_clause += " AND demo_pregnancy_data.CONCEPTION_DATE_ID=dt_date1.ID";
						  }
						  for(int k=0; k<maxNestedQueries.size(); k++) {
							  where_clause += " AND (dt_date1.YEAR2 <= "+maxNestedQueries.get(k)+" OR (dt_date1.YEAR2 IS NULL AND dt_date1.YEAR <= "+maxNestedQueries.get(k)+"))";
						  }  
						  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
					  }
					 
				  	if(!crit_demo_pregnancy_obj.getConceptionMinNested().isEmpty()) {
						  String crit_min_nested = makeCriterionList(crit_demo_pregnancy_obj.getConceptionMinNested());
						  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_min_nested);
						  ArrayList<Criterion> list_of_min_nested=null;
						  list_of_min_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
						  ArrayList<String> minNestedQueries = createNestedQueries(list_of_min_nested, false, false);
						  if(!tables.contains("dt_date1")) {
							  tables += ", dt_date as dt_date1";
							  where_clause += " AND demo_pregnancy_data.CONCEPTION_DATE_ID=dt_date1.ID";
						  }
						  for(int k=0; k<minNestedQueries.size(); k++) {
							  where_clause += " AND dt_date1.YEAR >= "+minNestedQueries.get(k);
						  }  
						  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
					  }
				  	
				  	if(!crit_demo_pregnancy_obj.getOutcomeEndPeriodNested().isEmpty()) {
						  String crit_end_period_nested = makeCriterionList(crit_demo_pregnancy_obj.getOutcomeEndPeriodNested());
						  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_end_period_nested);
						  ArrayList<Criterion> list_of_end_period_nested=null;
						  list_of_end_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
						  ArrayList<String> endPeriodNestedQueries = createPeriodNestedQueries(list_of_end_period_nested, false, false);
						  if(!tables.contains("dt_date2")) {
							  tables += ", dt_date as dt_date2";
							  where_clause += " AND demo_pregnancy_data.OUTCOME_DATE_ID=dt_date2.ID";
						  }
						  for(int k=0; k<endPeriodNestedQueries.size(); k++) {
							  where_clause += " AND (dt_date2.YEAR2 <= "+endPeriodNestedQueries.get(k)+" OR (dt_date2.YEAR2 IS NULL AND dt_date2.YEAR <= "+endPeriodNestedQueries.get(k)+"))";
						  }  
						  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
					  }
					  
				  	if(!crit_demo_pregnancy_obj.getOutcomeStartPeriodNested().isEmpty()) {
						  String crit_start_period_nested = makeCriterionList(crit_demo_pregnancy_obj.getOutcomeStartPeriodNested());
						  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_start_period_nested);
						  ArrayList<Criterion> list_of_start_period_nested=null;
						  list_of_start_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
						  ArrayList<String> startPeriodNestedQueries = createPeriodNestedQueries(list_of_start_period_nested, false, true);
						  if(!tables.contains("dt_date2")) {
							  tables += ", dt_date as dt_date2";
							  where_clause += " AND demo_pregnancy_data.OUTCOME_DATE_ID=dt_date2.ID";
						  }
						  for(int k=0; k<startPeriodNestedQueries.size(); k++) {
							  where_clause += " AND dt_date2.YEAR >= "+startPeriodNestedQueries.get(k);
						  }  
						  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
					  }
				  	
				  	if(!crit_demo_pregnancy_obj.getConceptionEndPeriodNested().isEmpty()) {
						  String crit_end_period_nested = makeCriterionList(crit_demo_pregnancy_obj.getConceptionEndPeriodNested());
						  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_end_period_nested);
						  ArrayList<Criterion> list_of_end_period_nested=null;
						  list_of_end_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
						  ArrayList<String> endPeriodNestedQueries = createPeriodNestedQueries(list_of_end_period_nested, false, false);
						  if(!tables.contains("dt_date1")) {
							  tables += ", dt_date as dt_date1";
							  where_clause += " AND demo_pregnancy_data.CONCEPTION_DATE_ID=dt_date1.ID";
						  }
						  for(int k=0; k<endPeriodNestedQueries.size(); k++) {
							  where_clause += " AND (dt_date1.YEAR2 <= "+endPeriodNestedQueries.get(k)+" OR (dt_date1.YEAR2 IS NULL AND dt_date1.YEAR <= "+endPeriodNestedQueries.get(k)+"))";
						  }  
						  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
					  }
					  
				  	if(!crit_demo_pregnancy_obj.getConceptionStartPeriodNested().isEmpty()) {
						  String crit_start_period_nested = makeCriterionList(crit_demo_pregnancy_obj.getConceptionStartPeriodNested());
						  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_start_period_nested);
						  ArrayList<Criterion> list_of_start_period_nested=null;
						  list_of_start_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
						  ArrayList<String> startPeriodNestedQueries = createPeriodNestedQueries(list_of_start_period_nested, false, true);
						  if(!tables.contains("dt_date1")) {
							  tables += ", dt_date as dt_date1";
							  where_clause += " AND demo_pregnancy_data.CONCEPTION_DATE_ID=dt_date1.ID";
						  }
						  for(int k=0; k<startPeriodNestedQueries.size(); k++) {
							  where_clause += " AND dt_date1.YEAR >= "+startPeriodNestedQueries.get(k);
						  }  
						  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
					  }
					
						  if(!incl) {
							  query = "SELECT DISTINCT patient.ID FROM patient WHERE patient.ID NOT IN (" +query+ ")";
						  }
				  	
			  } break;
			  
			  case "lifestyle_smoking": { //Check if user provided the info of all the fields 
				  lifestyle_smoking crit_lifestyle_smoking_obj =  (lifestyle_smoking)current_Criterion;
				  
				  String tables = "patient, lifestyle_smoking";
				  String where_clause = "patient.ID = lifestyle_smoking.PATIENT_ID"; 
				  
				 if(!crit_lifestyle_smoking_obj.getVoc_smoking_status_CODE().isEmpty()) {
					 tables += ", voc_smoking_status";
					 where_clause += " AND lifestyle_smoking.STATUS_ID = voc_smoking_status.ID AND "+Make_OR_of_CODES("voc_smoking_status.CODE", crit_lifestyle_smoking_obj.getVoc_smoking_status_CODE());
				 }
				  
				  /*try {
					  if(!incl) {
						  String assistanceQuery = "SELECT DISTINCT patient.ID FROM " + tables + " WHERE " + where_clause + temp_where_clause;
						  System.out.println("We are ready to execute the assistance query: "+assistanceQuery);
						  String[] patientsToExclude = DBServiceCRUD.getDataFromDB(assistanceQuery).trim().split(" ");
						  String assistance_where_clause = "NOT(";
						  for(int j=0; j<patientsToExclude.length; j++) {
							  if(j==0) {
								  assistance_where_clause += "patient.ID = "+patientsToExclude[j];
							  }
							  else assistance_where_clause += " OR patient.ID = "+patientsToExclude[j];
						  }
						  assistance_where_clause += ")";
						  where_clause = assistance_where_clause + " AND " + where_clause;
					  }
					  else {
						  where_clause += temp_where_clause;
					  }
				  } catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				  }*/
				  
				  if(!(crit_lifestyle_smoking_obj.getSmoking_exact_date_year()).isEmpty()) {
					  tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
					  where_clause += Make_begin_end_period_query (mode,"lifestyle_smoking.PERIOD_ID", "dt_date1", "dt_date2", crit_lifestyle_smoking_obj.getSmoking_exact_date_year(), 
							  crit_lifestyle_smoking_obj.getSmoking_exact_date_month(), crit_lifestyle_smoking_obj.getSmoking_exact_date_day(),
							  crit_lifestyle_smoking_obj.getSmoking_exact_date_year(), crit_lifestyle_smoking_obj.getSmoking_exact_date_month(),
							  crit_lifestyle_smoking_obj.getSmoking_exact_date_day()); 
					  	 							  
					} else if(!(crit_lifestyle_smoking_obj.getSmoking_period_begin_year()).isEmpty() || !(crit_lifestyle_smoking_obj.getSmoking_period_end_year()).isEmpty()) {
						tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
						where_clause += Make_begin_end_period_query (mode,"lifestyle_smoking.PERIOD_ID", "dt_date1", "dt_date2", crit_lifestyle_smoking_obj.getSmoking_period_begin_year(), 
								  crit_lifestyle_smoking_obj.getSmoking_period_begin_month(), crit_lifestyle_smoking_obj.getSmoking_period_begin_day(),
								  crit_lifestyle_smoking_obj.getSmoking_period_end_year(), crit_lifestyle_smoking_obj.getSmokimg_period_end_month(),
								  crit_lifestyle_smoking_obj.getSmoking_period_end_day()); 
																
					} else if(!(crit_lifestyle_smoking_obj.getSmoking_until_date_year()).isEmpty()) {
						tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
						where_clause += Make_begin_end_period_query (mode,"lifestyle_smoking.PERIOD_ID", "dt_date1", "dt_date2", "1800", 
								  "1", "1",crit_lifestyle_smoking_obj.getSmoking_until_date_year(), crit_lifestyle_smoking_obj.getSmoking_until_date_month(),
								  crit_lifestyle_smoking_obj.getSmoking_until_date_day());
						 								
					}  
				   
				  if(!crit_lifestyle_smoking_obj.getAmount_exact_value().isEmpty()){
					  	tables += ", dt_amount, voc_unit";
					  where_clause += " AND lifestyle_smoking.AMOUNT_ID = dt_amount.ID AND dt_amount.UNIT_ID=voc_unit.ID";
					  where_clause += " AND (dt_amount.value=" + crit_lifestyle_smoking_obj.getAmount_exact_value() + " OR (dt_amount.value<="+crit_lifestyle_smoking_obj.getAmount_exact_value()+" AND dt_amount.value2>="+crit_lifestyle_smoking_obj.getAmount_exact_value()+")) AND voc_unit.CODE ='" + crit_lifestyle_smoking_obj.getDt_amount_voc_unit_CODE() + "' ";
					  
				  }
				  
				  if(!crit_lifestyle_smoking_obj.getAmount_range_min_value().isEmpty()){
					  	tables += ", dt_amount, voc_unit";
					  	where_clause += " AND lifestyle_smoking.AMOUNT_ID = dt_amount.ID AND dt_amount.UNIT_ID=voc_unit.ID";
					  	where_clause += " AND (dt_amount.value>=" + crit_lifestyle_smoking_obj.getAmount_range_min_value()+" OR dt_amount.value2>=" + crit_lifestyle_smoking_obj.getAmount_range_min_value()+")"; 
					  	if(!crit_lifestyle_smoking_obj.getAmount_range_max_value().isEmpty()) {
					  		where_clause += " AND dt_amount.value<=" + crit_lifestyle_smoking_obj.getAmount_range_max_value();
					  	}
					  	where_clause += " AND voc_unit.CODE ='" + crit_lifestyle_smoking_obj.getDt_amount_voc_unit_CODE() + "'";
				  }
				  else if(!crit_lifestyle_smoking_obj.getAmount_range_max_value().isEmpty()) {
					  tables += ", dt_amount, voc_unit";
					  where_clause += " AND dt_amount.value<=" + crit_lifestyle_smoking_obj.getAmount_range_max_value() + " AND voc_unit.CODE ='" + crit_lifestyle_smoking_obj.getDt_amount_voc_unit_CODE() + "'";
				  }
				  
				  if(!crit_lifestyle_smoking_obj.getStatement().isEmpty()){
					  tables += ", voc_confirmation";
					  where_clause += " AND lifestyle_smoking.STMT_ID=voc_confirmation.ID AND voc_confirmation.CODE='" + crit_lifestyle_smoking_obj.getStatement() + "'";	
					 
				  }
				  
				  where_clause += " AND lifestyle_smoking.STMT_ID=1";
				  
				  if(!crit_lifestyle_smoking_obj.getMinCount().isEmpty()) {
				  		if(!crit_lifestyle_smoking_obj.getMaxCount().isEmpty()) {
				  			where_clause += " GROUP BY patient.ID HAVING COUNT(*) BETWEEN "+crit_lifestyle_smoking_obj.getMinCount()+" AND "+crit_lifestyle_smoking_obj.getMaxCount();
				  		}
				  		else where_clause += " GROUP BY patient.ID HAVING COUNT(*) >= "+crit_lifestyle_smoking_obj.getMinCount();
				  }
				  else if(!crit_lifestyle_smoking_obj.getMaxCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.ID HAVING COUNT(*) <= "+crit_lifestyle_smoking_obj.getMaxCount();
				  }
				  
				  query = "SELECT DISTINCT patient.ID FROM " + tables + " WHERE " + where_clause;
				  
				  if(!crit_lifestyle_smoking_obj.getEndBeforeStartNested().isEmpty()) {
					  String crit_max_nested = makeCriterionList(crit_lifestyle_smoking_obj.getEndBeforeStartNested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_max_nested);
					  ArrayList<Criterion> list_of_max_nested=null;
					  list_of_max_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> maxNestedQueries = createNestedQueries(list_of_max_nested, false, true);
					  if(!tables.contains("dt_period_of_time")) {
						  tables += ", dt_period_of_time";
						  where_clause += " AND lifestyle_smoking.PERIOD_ID=dt_period_of_time.ID";
					  }
					  if(!tables.contains("dt_date2")) {
						  tables += ", dt_date AS dt_date2";
						  where_clause += " AND dt_period_of_time.END_DATE_ID=dt_date2.ID";
					  }
					  for(int k=0; k<maxNestedQueries.size(); k++) {
						  where_clause += " AND dt_date2.YEAR <= "+maxNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  if(!crit_lifestyle_smoking_obj.getStartBeforeStartNested().isEmpty()) {
					  String crit_max_nested = makeCriterionList(crit_lifestyle_smoking_obj.getStartBeforeStartNested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_max_nested);
					  ArrayList<Criterion> list_of_max_nested=null;
					  list_of_max_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> maxNestedQueries = createNestedQueries(list_of_max_nested, false, true);
					  if(!tables.contains("dt_period_of_time")) {
						  tables += ", dt_period_of_time";
						  where_clause += " AND lifestyle_smoking.PERIOD_ID=dt_period_of_time.ID";
					  }
					  if(!tables.contains("dt_date1")) {
						  tables += ", dt_date AS dt_date1";
						  where_clause += " AND dt_period_of_time.START_DATE_ID=dt_date1.ID";
					  }
					  for(int k=0; k<maxNestedQueries.size(); k++) {
						  where_clause += " AND dt_date1.YEAR <= "+maxNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!crit_lifestyle_smoking_obj.getStartAfterEndNested().isEmpty()) {
					  String crit_min_nested = makeCriterionList(crit_lifestyle_smoking_obj.getStartAfterEndNested());
					  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_min_nested);
					  ArrayList<Criterion> list_of_min_nested=null;
					  list_of_min_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
					  ArrayList<String> minNestedQueries = createNestedQueries(list_of_min_nested, false, false);
					  if(!tables.contains("dt_period_of_time")) {
						  tables += ", dt_period_of_time";
						  where_clause += " AND lifestyle_smoking.PERIOD_ID=dt_period_of_time.ID";
					  }
					  if(!tables.contains("dt_date1")) {
						  tables += ", dt_date AS dt_date1";
						  where_clause += " AND dt_period_of_time.START_DATE_ID=dt_date1.ID";
					  }
					  for(int k=0; k<minNestedQueries.size(); k++) {
						  where_clause += " AND dt_date1.YEAR >= "+minNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  } 
				  
				  if(!crit_lifestyle_smoking_obj.getEndAfterEndNested().isEmpty()) {
					  String crit_min_nested = makeCriterionList(crit_lifestyle_smoking_obj.getEndAfterEndNested());
					  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_min_nested);
					  ArrayList<Criterion> list_of_min_nested=null;
					  list_of_min_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
					  ArrayList<String> minNestedQueries = createNestedQueries(list_of_min_nested, false, false);
					  if(!tables.contains("dt_period_of_time")) {
						  tables += ", dt_period_of_time";
						  where_clause += " AND lifestyle_smoking.PERIOD_ID=dt_period_of_time.ID";
					  }
					  if(!tables.contains("dt_date2")) {
						  tables += ", dt_date AS dt_date2";
						  where_clause += " AND dt_period_of_time.END_DATE_ID=dt_date2.ID";
					  }
					  for(int k=0; k<minNestedQueries.size(); k++) {
						  where_clause += " AND dt_date2.YEAR >= "+minNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  } 
				  
				  if(!crit_lifestyle_smoking_obj.getEndAfterStartNested().isEmpty()) {
					  String crit_start_period_nested = makeCriterionList(crit_lifestyle_smoking_obj.getEndAfterStartNested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_start_period_nested);
					  ArrayList<Criterion> list_of_start_period_nested=null;
					  list_of_start_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> startPeriodNestedQueries = createPeriodNestedQueries(list_of_start_period_nested, false, true);
					  if(!tables.contains("dt_period_of_time")) {
						  tables += ", dt_period_of_time";
						  where_clause += " AND lifestyle_smoking.PERIOD_ID=dt_period_of_time.ID";
					  }
					  if(!tables.contains("dt_date2")) {
						  tables += ", dt_date AS dt_date2";
						  where_clause += " AND dt_period_of_time.END_DATE_ID=dt_date2.ID";
					  }
					  for(int k=0; k<startPeriodNestedQueries.size(); k++) {
						  where_clause += " AND dt_date2.YEAR >= "+startPeriodNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!crit_lifestyle_smoking_obj.getEndBeforeEndNested().isEmpty()) {
					  String crit_end_period_nested = makeCriterionList(crit_lifestyle_smoking_obj.getEndBeforeEndNested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_end_period_nested);
					  ArrayList<Criterion> list_of_end_period_nested=null;
					  list_of_end_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> endPeriodNestedQueries = createPeriodNestedQueries(list_of_end_period_nested, false, false);
					  if(!tables.contains("dt_period_of_time")) {
						  tables += ", dt_period_of_time";
						  where_clause += " AND lifestyle_smoking.PERIOD_ID=dt_period_of_time.ID";
					  }
					  if(!tables.contains("dt_date2")) {
						  tables += ", dt_date AS dt_date2";
						  where_clause += " AND dt_period_of_time.END_DATE_ID=dt_date2.ID";
					  }
					  for(int k=0; k<endPeriodNestedQueries.size(); k++) {
						  where_clause += " AND dt_date2.YEAR <= "+endPeriodNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!crit_lifestyle_smoking_obj.getStartBeforeEndNested().isEmpty()) {
					  String crit_end_period_nested = makeCriterionList(crit_lifestyle_smoking_obj.getStartBeforeEndNested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_end_period_nested);
					  ArrayList<Criterion> list_of_end_period_nested=null;
					  list_of_end_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> endPeriodNestedQueries = createPeriodNestedQueries(list_of_end_period_nested, false, false);
					  if(!tables.contains("dt_period_of_time")) {
						  tables += ", dt_period_of_time";
						  where_clause += " AND lifestyle_smoking.PERIOD_ID=dt_period_of_time.ID";
					  }
					  if(!tables.contains("dt_date1")) {
						  tables += ", dt_date AS dt_date1";
						  where_clause += " AND dt_period_of_time.START_DATE_ID=dt_date1.ID";
					  }
					  for(int k=0; k<endPeriodNestedQueries.size(); k++) {
						  where_clause += " AND dt_date1.YEAR <= "+endPeriodNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!crit_lifestyle_smoking_obj.getStartAfterStartNested().isEmpty()) {
					  String crit_start_period_nested = makeCriterionList(crit_lifestyle_smoking_obj.getStartAfterStartNested());
					  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_start_period_nested);
					  ArrayList<Criterion> list_of_start_period_nested=null;
					  list_of_start_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
					  ArrayList<String> startPeriodNestedQueries = createPeriodNestedQueries(list_of_start_period_nested, false, true);
					  if(!tables.contains("dt_period_of_time")) {
						  tables += ", dt_period_of_time";
						  where_clause += " AND lifestyle_smoking.PERIOD_ID=dt_period_of_time.ID";
					  }
					  if(!tables.contains("dt_date1")) {
						  tables += ", dt_date AS dt_date1";
						  where_clause += " AND dt_period_of_time.START_DATE_ID=dt_date1.ID";
					  }
					  for(int k=0; k<startPeriodNestedQueries.size(); k++) {
						  where_clause += " AND dt_date1.YEAR >= "+startPeriodNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  } 	
				  
				  if(!incl) query = "SELECT DISTINCT patient.ID FROM patient WHERE patient.ID NOT IN (" +query+ ")";
				  //TODO 	will voc_unit.CODE take one value or more than one I also think that voc_direction_CODE() will take one value?
				  
				  //if(!crit_lifestyle_smoking_obj.getDt_period_of_time_voc_confirmation_CODE().isEmpty()) query += "AND dt_period_of_time.EXACT_ID = voc_confirmation.ID AND voc_confirmation.CODE='"+crit_lifestyle_smoking_obj.getDt_period_of_time_voc_confirmation_CODE()+"' "; 
				  //if(!crit_lifestyle_smoking_obj.getDt_period_of_time_BEFORE_PERIOD_ID().isEmpty()) query += "AND dt_period_of_time.BEFORE_PERIOD_ID = '"+crit_lifestyle_smoking_obj.getDt_period_of_time_BEFORE_PERIOD_ID()+"' ";
			  System.out.println("The Query is: "+ query);
			  } break;
			  
			  case "condition_symptom": { //Check if user provided the info of all the fields 
				  condition_symptom crit_cond_symptom_obj =  (condition_symptom)current_Criterion;
				  
				  String tables = "patient, cond_symptom";
				  String where_clause = "patient.ID = cond_symptom.PATIENT_ID";
				  
				  if(!crit_cond_symptom_obj.getVoc_symptom_sign_CODE().isEmpty()) {
					  tables += ", voc_symptom_sign";
					  where_clause += " AND cond_symptom.CONDITION_ID = voc_symptom_sign.ID";
					  where_clause += " AND (" + Make_OR_of_CODES("voc_symptom_sign.CODE", crit_cond_symptom_obj.getVoc_symptom_sign_CODE());
					  String codes[] = crit_cond_symptom_obj.getVoc_symptom_sign_CODE().split(",");
					  for(int k=0; k<codes.length; k++) {
						  String narrowTerms = getTermsWithNarrowMeaning(codes[k].trim());
						  String[] allNarrowTerms = narrowTerms.split(",");
						  for(int c=1; c<allNarrowTerms.length; c++) {
							  where_clause += " OR " + Make_OR_of_CODES("voc_symptom_sign.CODE", allNarrowTerms[c]);
						  }
					  }
					  where_clause += ")";
				  }
				  
				  
				  
				  /*try {
					  if(!incl) {
						  String assistanceQuery = "SELECT DISTINCT patient.ID FROM " + tables + " WHERE " + where_clause + temp_where_clause;
						  System.out.println("We are ready to execute the assistance query: "+assistanceQuery);
						  String[] patientsToExclude = DBServiceCRUD.getDataFromDB(assistanceQuery).trim().split(" ");
						  String assistance_where_clause = "NOT(";
						  for(int j=0; j<patientsToExclude.length; j++) {
							  if(j==0) {
								  assistance_where_clause += "patient.ID = "+patientsToExclude[j];
							  }
							  else assistance_where_clause += " OR patient.ID = "+patientsToExclude[j];
						  }
						  assistance_where_clause += ")";
						  where_clause = assistance_where_clause + " AND " + where_clause;
					  }
					  else {
						  where_clause += temp_where_clause;
					  }
				  } catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				  }*/
				  /*query = "SELECT DISTINCT patient.ID " + 
						  "FROM patient, cond_symptom, voc_symptom_sign, dt_date, voc_direction, voc_confirmation " + 
						  "WHERE patient.ID = cond_symptom.PATIENT_ID " + 
						  "AND cond_symptom.CONDITION_ID = voc_symptom_sign.ID " +
						  "AND " + Make_OR_of_CODES("voc_symptom_sign.CODE", crit_cond_symptom_obj.getVoc_symptom_sign_CODE());*/
				  
				  if(!(crit_cond_symptom_obj.getObserve_exact_date_YEAR()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_specific_date_query(mode, "cond_symptom.OBSERVE_DATE_ID","dt_date",crit_cond_symptom_obj.getObserve_exact_date_YEAR(),
							  crit_cond_symptom_obj.getObserve_exact_date_MONTH(),crit_cond_symptom_obj.getObserve_exact_date_DAY());
					  /*where_clause += Make_specific_date_query(mode, "cond_symptom.OBSERVE_DATE_ID","dt_date",crit_cond_symptom_obj.getObserve_exact_date_YEAR(), //check cond_symptom.OBSERVE_DATE_ID
						crit_cond_symptom_obj.getObserve_exact_date_MONTH(), crit_cond_symptom_obj.getObserve_exact_date_DAY());*/
						 
				  }else if(!(crit_cond_symptom_obj.getObserve_period_begin_year()).isEmpty() || !(crit_cond_symptom_obj.getObserve_period_end_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (mode,"cond_symptom.OBSERVE_DATE_ID", "dt_date",crit_cond_symptom_obj.getObserve_period_begin_year(), crit_cond_symptom_obj.getObserve_period_begin_month(), crit_cond_symptom_obj.getObserve_period_begin_day(), crit_cond_symptom_obj.getObserve_period_end_year() ,crit_cond_symptom_obj.getObserve_period_end_month(),
									  crit_cond_symptom_obj.getObserve_period_end_day()); 
							  
				  }else if(!(crit_cond_symptom_obj.getObserve_until_date_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (mode,"cond_symptom.OBSERVE_DATE_ID","dt_date", "1800", "1", "1",crit_cond_symptom_obj.getObserve_until_date_year(), 
									  crit_cond_symptom_obj.getObserve_until_date_month(), crit_cond_symptom_obj.getObserve_until_date_day()); 
				  }
				  
				  if(!crit_cond_symptom_obj.getStatement().isEmpty()){
					  tables += ", voc_confirmation";
					  where_clause += " AND cond_symptom.STMT_ID=voc_confirmation.ID " +
				  				 "AND voc_confirmation.CODE='"+crit_cond_symptom_obj.getStatement() + "'";
				  }
				  where_clause += " AND cond_symptom.STMT_ID=1";
				  
				  if(!crit_cond_symptom_obj.getMinCount().isEmpty()) {
				  		if(!crit_cond_symptom_obj.getMaxCount().isEmpty()) {
				  			where_clause += " GROUP BY patient.ID HAVING COUNT(*) BETWEEN "+crit_cond_symptom_obj.getMinCount()+" AND "+crit_cond_symptom_obj.getMaxCount();
				  		}
				  		else where_clause += " GROUP BY patient.ID HAVING COUNT(*) >= "+crit_cond_symptom_obj.getMinCount();
				  }
				  else if(!crit_cond_symptom_obj.getMaxCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.ID HAVING COUNT(*) <= "+crit_cond_symptom_obj.getMaxCount();
				  }
				  
				  query = "SELECT DISTINCT patient.ID FROM " + tables + " WHERE " + where_clause;
				  
				  if(!crit_cond_symptom_obj.getMaxNested().isEmpty()) {
					  String crit_max_nested = makeCriterionList(crit_cond_symptom_obj.getMaxNested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_max_nested);
					  ArrayList<Criterion> list_of_max_nested=null;
					  list_of_max_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> maxNestedQueries = createNestedQueries(list_of_max_nested, false, true);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND cond_symptom.OBSERVE_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<maxNestedQueries.size(); k++) {
						  where_clause += " AND (dt_date.YEAR2 <= "+maxNestedQueries.get(k)+" OR (dt_date.YEAR2 IS NULL AND dt_date.YEAR <= "+maxNestedQueries.get(k)+"))";
					  }  
					  if(!crit_cond_symptom_obj.getMinNested().isEmpty()) {
						  String crit_min_nested = makeCriterionList(crit_cond_symptom_obj.getMinNested());
						  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_min_nested);
						  ArrayList<Criterion> list_of_min_nested=null;
						  list_of_min_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
						  ArrayList<String> minNestedQueries = createNestedQueries(list_of_min_nested, false, false);
						  for(int k=0; k<minNestedQueries.size(); k++) {
							  where_clause += " AND dt_date.YEAR >= "+minNestedQueries.get(k);
						  }  
					  }
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  else if(!crit_cond_symptom_obj.getMinNested().isEmpty()) {
					  String crit_min_nested = makeCriterionList(crit_cond_symptom_obj.getMinNested());
					  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_min_nested);
					  ArrayList<Criterion> list_of_min_nested=null;
					  list_of_min_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
					  ArrayList<String> minNestedQueries = createNestedQueries(list_of_min_nested, false, false);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND cond_symptom.OBSERVE_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<minNestedQueries.size(); k++) {
						  where_clause += " AND dt_date.YEAR >= "+minNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!crit_cond_symptom_obj.getMaxNested().isEmpty()) {
					  String crit_max_nested = makeCriterionList(crit_cond_symptom_obj.getMaxNested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_max_nested);
					  ArrayList<Criterion> list_of_max_nested=null;
					  list_of_max_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> maxNestedQueries = createNestedQueries(list_of_max_nested, false, true);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND cond_symptom.OBSERVE_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<maxNestedQueries.size(); k++) {
						  where_clause += " AND (dt_date.YEAR2 <= "+maxNestedQueries.get(k)+" OR (dt_date.YEAR2 IS NULL AND dt_date.YEAR <= "+maxNestedQueries.get(k)+"))";
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!crit_cond_symptom_obj.getMinNested().isEmpty()) {
					  String crit_min_nested = makeCriterionList(crit_cond_symptom_obj.getMinNested());
					  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_min_nested);
					  ArrayList<Criterion> list_of_min_nested=null;
					  list_of_min_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
					  ArrayList<String> minNestedQueries = createNestedQueries(list_of_min_nested, false, false);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND cond_symptom.OBSERVE_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<minNestedQueries.size(); k++) {
						  where_clause += " AND dt_date.YEAR >= "+minNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!crit_cond_symptom_obj.getEndPeriodNested().isEmpty()) {
					  String crit_end_period_nested = makeCriterionList(crit_cond_symptom_obj.getEndPeriodNested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_end_period_nested);
					  ArrayList<Criterion> list_of_end_period_nested=null;
					  list_of_end_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> endPeriodNestedQueries = createPeriodNestedQueries(list_of_end_period_nested, false, false);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND cond_symptom.OBSERVE_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<endPeriodNestedQueries.size(); k++) {
						  where_clause += " AND (dt_date.YEAR2 <= "+endPeriodNestedQueries.get(k)+" OR (dt_date.YEAR2 IS NULL AND dt_date.YEAR <= "+endPeriodNestedQueries.get(k)+"))";
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!crit_cond_symptom_obj.getStartPeriodNested().isEmpty()) {
					  String crit_start_period_nested = makeCriterionList(crit_cond_symptom_obj.getStartPeriodNested());
					  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_start_period_nested);
					  ArrayList<Criterion> list_of_start_period_nested=null;
					  list_of_start_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
					  ArrayList<String> startPeriodNestedQueries = createPeriodNestedQueries(list_of_start_period_nested, false, true);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND cond_symptom.OBSERVE_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<startPeriodNestedQueries.size(); k++) {
						  where_clause += " AND dt_date.YEAR >= "+startPeriodNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!incl) query = "SELECT DISTINCT patient.ID FROM patient WHERE patient.ID NOT IN (" +query+ ")";
				  /*if(incl) query = "SELECT DISTINCT patient.ID FROM " + tables + " WHERE " + where_clause + " AND " + temp_where_clause;
				  else query = "SELECT DISTINCT patient.ID FROM " + tables + " WHERE " + where_clause + " AND NOT(" + temp_where_clause + ")";*/
				  
			  } break;
			  
			  case "condition_diagnosis": { //Check if user provided the info of all the fields 
				  condition_diagnosis condition_diagnosis_obj =  (condition_diagnosis)current_Criterion;
				  
				  String tables = "patient, cond_diagnosis";
				  String where_clause = "patient.ID = cond_diagnosis.PATIENT_ID";
				  
				  if(!condition_diagnosis_obj.getCondition().isEmpty()) {
					  tables += ", voc_medical_condition";
					  where_clause += " AND cond_diagnosis.CONDITION_ID = voc_medical_condition.ID";
					  where_clause += " AND (" + Make_OR_of_CODES("voc_medical_condition.CODE", condition_diagnosis_obj.getCondition());
					  String codes[] = condition_diagnosis_obj.getCondition().split(",");
					  for(int k=0; k<codes.length; k++) {
						  String narrowTerms = getTermsWithNarrowMeaning(codes[k].trim());
						  String[] allNarrowTerms = narrowTerms.split(",");
						  for(int c=1; c<allNarrowTerms.length; c++) {
							  where_clause += " OR " + Make_OR_of_CODES("voc_medical_condition.CODE", allNarrowTerms[c]);
						  }
					  }
					  where_clause += ")";
				  }
				  
				  if(!condition_diagnosis_obj.getStage().isEmpty()) {  // [OUTCOME_ASSESSMENT]
					  tables += ", voc_lymphoma_stage";
					  //where_clause += " AND cond_diagnosis.STAGE_ID = voc_lymphoma_stage.ID AND " + Make_OR_of_CODES("voc_lymphoma_stage.CODE", condition_diagnosis_obj.getStage());
					  where_clause += " AND cond_diagnosis.STAGE_ID = voc_lymphoma_stage.ID";
					  where_clause += " AND " + Make_OR_of_CODES("voc_lymphoma_stage.CODE", condition_diagnosis_obj.getStage());

					  	
				  }
				  
				  if(!condition_diagnosis_obj.getOrgan().isEmpty()) {  // [OUTCOME_ASSESSMENT]
					  tables += ", cond_diagnosis_organs, voc_lymphoma_organ";
					  where_clause += " AND cond_diagnosis.ID = cond_diagnosis_organs.DIAGNOSIS_ID AND cond_diagnosis_organs.ORGAN_ID = voc_lymphoma_organ.ID AND (" + Make_OR_of_CODES("voc_lymphoma_organ.CODE", condition_diagnosis_obj.getOrgan());
					  String organCodes[] = condition_diagnosis_obj.getOrgan().split(",");
					  for(int k=0; k<organCodes.length; k++) {
						  String narrowTerms = getTermsWithNarrowMeaning(organCodes[k].trim());
						  String[] allNarrowTerms = narrowTerms.split(",");
						  for(int c=1; c<allNarrowTerms.length; c++) {
							  where_clause += " OR " + Make_OR_of_CODES("voc_lymphoma_organ.CODE", allNarrowTerms[c]);
						  }
					  }
					  where_clause += ")";
					  	
				  }
				  
				  if(!condition_diagnosis_obj.getPerformance_status().isEmpty()) {  // [OUTCOME_ASSESSMENT]
					  tables += ", voc_performance_status";
					  where_clause += " AND cond_diagnosis.PERFORMANCE_STATUS_ID = voc_performance_status.ID AND " + Make_OR_of_CODES("voc_performance_status.CODE", condition_diagnosis_obj.getPerformance_status());
					  
				  }
				  
				  if(!(condition_diagnosis_obj.getDate_exact_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_specific_date_query(mode, "cond_diagnosis.DIAGNOSIS_DATE_ID","dt_date",condition_diagnosis_obj.getDate_exact_year(),
							  condition_diagnosis_obj.getDate_exact_month(),condition_diagnosis_obj.getDate_exact_day());
				  } else if(!(condition_diagnosis_obj.getDate_interval_start_year()).isEmpty() || !(condition_diagnosis_obj.getDate_interval_end_year()).isEmpty()) {
					 tables += ", dt_date";
					 where_clause += Make_begin_end_date_query (mode,"cond_diagnosis.DIAGNOSIS_DATE_ID", "dt_date",condition_diagnosis_obj.getDate_interval_start_year(), condition_diagnosis_obj.getDate_interval_start_month(), condition_diagnosis_obj.getDate_interval_start_day(), condition_diagnosis_obj.getDate_interval_end_year(), condition_diagnosis_obj.getDate_interval_end_month(),
							  condition_diagnosis_obj.getDate_interval_end_day());
				  } else if(!(condition_diagnosis_obj.getDate_until_year() ).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (mode,"cond_diagnosis.DIAGNOSIS_DATE_ID","dt_date", "1800", "1", "1", condition_diagnosis_obj.getDate_until_year(), 
							  condition_diagnosis_obj.getDate_until_month(), condition_diagnosis_obj.getDate_until_day());
				  }
				  
				  if(!condition_diagnosis_obj.getStatement().isEmpty()){ 
					  tables += ", voc_confirmation";
					  where_clause += " AND cond_diagnosis.STMT_ID=voc_confirmation.ID AND voc_confirmation.CODE='"+condition_diagnosis_obj.getStatement() + "'";
				  }
				  where_clause += " AND cond_diagnosis.STMT_ID=1";
				  
				  if(!condition_diagnosis_obj.getMinCount().isEmpty()) {
				  		if(!condition_diagnosis_obj.getMaxCount().isEmpty()) {
				  			where_clause += " GROUP BY patient.ID HAVING COUNT(*) BETWEEN "+condition_diagnosis_obj.getMinCount()+" AND "+condition_diagnosis_obj.getMaxCount();
				  		}
				  		else where_clause += " GROUP BY patient.ID HAVING COUNT(*) >= "+condition_diagnosis_obj.getMinCount();
				  }
				  else if(!condition_diagnosis_obj.getMaxCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.ID HAVING COUNT(*) <= "+condition_diagnosis_obj.getMaxCount();
				  }
				  
				  query = "SELECT DISTINCT patient.ID FROM " + tables + " WHERE " + where_clause;
				  
				  if(!condition_diagnosis_obj.getMaxNested().isEmpty()) {
					  String crit_max_nested = makeCriterionList(condition_diagnosis_obj.getMaxNested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_max_nested);
					  ArrayList<Criterion> list_of_max_nested=null;
					  list_of_max_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> maxNestedQueries = createNestedQueries(list_of_max_nested, false, true);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND cond_diagnosis.DIAGNOSIS_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<maxNestedQueries.size(); k++) {
						  where_clause += " AND (dt_date.YEAR2 <= "+maxNestedQueries.get(k)+" OR (dt_date.YEAR2 IS NULL AND dt_date.YEAR <= "+maxNestedQueries.get(k)+"))";
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!condition_diagnosis_obj.getMinNested().isEmpty()) {
					  String crit_min_nested = makeCriterionList(condition_diagnosis_obj.getMinNested());
					  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_min_nested);
					  ArrayList<Criterion> list_of_min_nested=null;
					  list_of_min_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
					  ArrayList<String> minNestedQueries = createNestedQueries(list_of_min_nested, false, false);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND cond_diagnosis.DIAGNOSIS_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<minNestedQueries.size(); k++) {
						  where_clause += " AND dt_date.YEAR >= "+minNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!condition_diagnosis_obj.getEndPeriodNested().isEmpty()) {
					  String crit_end_period_nested = makeCriterionList(condition_diagnosis_obj.getEndPeriodNested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_end_period_nested);
					  ArrayList<Criterion> list_of_end_period_nested=null;
					  list_of_end_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> endPeriodNestedQueries = createPeriodNestedQueries(list_of_end_period_nested, false, false);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND cond_diagnosis.DIAGNOSIS_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<endPeriodNestedQueries.size(); k++) {
						  where_clause += " AND (dt_date.YEAR2 <= "+endPeriodNestedQueries.get(k)+" OR (dt_date.YEAR2 IS NULL AND dt_date.YEAR <= "+endPeriodNestedQueries.get(k)+"))";
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!condition_diagnosis_obj.getStartPeriodNested().isEmpty()) {
					  String crit_start_period_nested = makeCriterionList(condition_diagnosis_obj.getStartPeriodNested());
					  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_start_period_nested);
					  ArrayList<Criterion> list_of_start_period_nested=null;
					  list_of_start_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
					  ArrayList<String> startPeriodNestedQueries = createPeriodNestedQueries(list_of_start_period_nested, false, true);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND cond_diagnosis.DIAGNOSIS_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<startPeriodNestedQueries.size(); k++) {
						  where_clause += " AND dt_date.YEAR >= "+startPeriodNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!incl) query = "SELECT DISTINCT patient.ID FROM patient WHERE patient.ID NOT IN (" +query+ ")";
			  } break;
			  
			  case "intervention_medication": { //Check if user provided the info of all the fields 
				  intervention_medication  crit_interv_medication_obj =  (intervention_medication )current_Criterion;
				  String tables = "patient, interv_medication";
				  String where_clause = "patient.ID = interv_medication.PATIENT_ID";
				  
				  if(!crit_interv_medication_obj.getVoc_pharm_drug_CODE().isEmpty()) {
					  tables += ", voc_pharm_drug";
					  where_clause += " AND interv_medication.MEDICATION_ID = voc_pharm_drug.ID";
					  where_clause += " AND (" + Make_OR_of_CODES("voc_pharm_drug.CODE", crit_interv_medication_obj.getVoc_pharm_drug_CODE());
					  String codes[] = crit_interv_medication_obj.getVoc_pharm_drug_CODE().split(",");
					  for(int k=0; k<codes.length; k++) {
						  String narrowTerms = getTermsWithNarrowMeaning(codes[k].trim());
						  String[] allNarrowTerms = narrowTerms.split(",");
						  for(int c=1; c<allNarrowTerms.length; c++) {
							  where_clause += " OR " + Make_OR_of_CODES("voc_pharm_drug.CODE", allNarrowTerms[c]);
						  }
					  }
					  where_clause += ")";
				  }
				  
				  /*String narrowTerms = getTermsWithNarrowMeaning(crit_interv_medication_obj.getVoc_pharm_drug_CODE());
				  String[] allNarrowTerms = narrowTerms.split(",");
				  for(int c=1; c<allNarrowTerms.length; c++) {
					  where_clause += " OR " + Make_OR_of_CODES("voc_pharm_drug.CODE", allNarrowTerms[c]);
				  }*/
				  
				  
				  //if(!crit_interv_medication_obj.getVoc_pharm_drug_BROADER_TERM_ID().isEmpty()) query += "AND voc_pharm_drug.BROADER_TERM_ID = '"+crit_interv_medication_obj.getVoc_pharm_drug_BROADER_TERM_ID()+"' "; //Do we need the Broader_Term_ID? (`BROADER_TERM_ID`) REFERENCES `voc_pharm_drug` (`ID`)
					  
				  if(!crit_interv_medication_obj.getDosage_amount_exact_value().isEmpty()) {
					  tables += ", dt_amount, voc_unit";
					  where_clause += " AND interv_medication.DOSAGE_ID = dt_amount.ID AND (dt_amount.VALUE ='" +crit_interv_medication_obj.getDosage_amount_exact_value()+"' OR (dt_amount.VALUE <="+crit_interv_medication_obj.getDosage_amount_exact_value()+" AND dt_amount.VALUE2 >="+crit_interv_medication_obj.getDosage_amount_exact_value()+")) "+
							  "AND dt_amount.UNIT_ID=voc_unit.ID " +
							  	"AND voc_unit.CODE ='"+crit_interv_medication_obj.getDOSAGE_ID_dt_amount_VALUE()+"'";;
				  }
				  
				  if(!crit_interv_medication_obj.getDosage_amount_range_min_value().isEmpty()){
					  	tables += ", dt_amount, voc_unit";
					  where_clause += " AND interv_medication.DOSAGE_ID = dt_amount.ID AND (dt_amount.value>=" + crit_interv_medication_obj.getDosage_amount_range_min_value()+" OR dt_amount.value2>="+ crit_interv_medication_obj.getDosage_amount_range_min_value()+")";
					  if(!crit_interv_medication_obj.getDosage_amount_range_max_value().isEmpty()){
						  where_clause += " AND dt_amount.value<=" + crit_interv_medication_obj.getDosage_amount_range_max_value();
					  }
					  where_clause += " AND dt_amount.UNIT_ID=voc_unit.ID AND voc_unit.CODE ='" + crit_interv_medication_obj.getDOSAGE_ID_dt_amount_VALUE() + "'";
					  
				  }
				  
				  else if(!crit_interv_medication_obj.getDosage_amount_range_max_value().isEmpty()){
					  	tables += ", dt_amount, voc_unit";
					  where_clause += " AND interv_medication.DOSAGE_ID = dt_amount.ID AND dt_amount.value<=" + crit_interv_medication_obj.getDosage_amount_range_max_value() + " AND dt_amount.UNIT_ID=voc_unit.ID AND voc_unit.CODE ='" + crit_interv_medication_obj.getDOSAGE_ID_dt_amount_VALUE() + "'";
					  
				  }
				  
				  if(!(crit_interv_medication_obj.getMedication_exact_date_year()).isEmpty()) {	
					  tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
					  where_clause += Make_begin_end_period_query (mode,"interv_medication.PERIOD_ID", "dt_date1", "dt_date2", crit_interv_medication_obj.getMedication_exact_date_year(), 
					  			crit_interv_medication_obj.getMedication_exact_date_month(), crit_interv_medication_obj.getMedication_exact_date_day(),
					  			crit_interv_medication_obj.getMedication_exact_date_year(), crit_interv_medication_obj.getMedication_exact_date_month(),
					  			crit_interv_medication_obj.getMedication_exact_date_day());
							  
				  } else if(!crit_interv_medication_obj.getMedication_period_begin_year().isEmpty() || !(crit_interv_medication_obj.getMedication_period_end_year()).isEmpty()) {
					  tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
					  where_clause += Make_begin_end_period_query (mode,"interv_medication.PERIOD_ID", "dt_date1", "dt_date2", crit_interv_medication_obj.getMedication_period_begin_year(), 
								crit_interv_medication_obj.getMedication_period_begin_month(), crit_interv_medication_obj.getMedication_period_begin_day(),
								crit_interv_medication_obj.getMedication_period_end_year(), crit_interv_medication_obj.getMedication_period_end_month(),
								crit_interv_medication_obj.getMedication_period_end_day()); 												
				  
				  } else if(!(crit_interv_medication_obj.getMedication_until_date_year()).isEmpty()) {
					  tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
					  where_clause += Make_begin_end_period_query (mode,"interv_medication.PERIOD_ID", "dt_date1", "dt_date2", "1800", 
							  "1", "1",crit_interv_medication_obj.getMedication_until_date_year(), crit_interv_medication_obj.getMedication_until_date_month(),
							  crit_interv_medication_obj.getMedication_until_date_day()); 							
				  } 
				  
				  if(!crit_interv_medication_obj.getStatement().isEmpty()) {
					  tables += ", voc_confirmation";
					  where_clause += " AND interv_medication.STMT_ID=voc_confirmation.ID AND voc_confirmation.CODE='"+crit_interv_medication_obj.getStatement() + "'";
				  }
				  where_clause += " AND interv_medication.STMT_ID=1";
				  /*if(incl) where_clause += " AND interv_medication.STMT_ID=1";
				  else where_clause += " AND interv_medication.STMT_ID=2";*/
				  if(!crit_interv_medication_obj.getMinCount().isEmpty()) {
				  		if(!crit_interv_medication_obj.getMaxCount().isEmpty()) {
				  			where_clause += " GROUP BY patient.ID HAVING COUNT(*) BETWEEN "+crit_interv_medication_obj.getMinCount()+" AND "+crit_interv_medication_obj.getMaxCount();
				  		}
				  		else where_clause += " GROUP BY patient.ID HAVING COUNT(*) >= "+crit_interv_medication_obj.getMinCount();
				  }
				  else if(!crit_interv_medication_obj.getMaxCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.ID HAVING COUNT(*) <= "+crit_interv_medication_obj.getMaxCount();
				  }
				  
				  query = "SELECT DISTINCT patient.ID FROM " + tables + " WHERE " + where_clause;
				  
				  if(!crit_interv_medication_obj.getEndBeforeStartNested().isEmpty()) {
					  String crit_max_nested = makeCriterionList(crit_interv_medication_obj.getEndBeforeStartNested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_max_nested);
					  ArrayList<Criterion> list_of_max_nested=null;
					  list_of_max_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> maxNestedQueries = createNestedQueries(list_of_max_nested, false, true);
					  if(!tables.contains("dt_period_of_time")) {
						  tables += ", dt_period_of_time";
						  where_clause += " AND interv_medication.PERIOD_ID=dt_period_of_time.ID";
					  }
					  if(!tables.contains("dt_date2")) {
						  tables += ", dt_date AS dt_date2";
						  where_clause += " AND dt_period_of_time.END_DATE_ID=dt_date2.ID";
					  }
					  for(int k=0; k<maxNestedQueries.size(); k++) {
						  where_clause += " AND dt_date2.YEAR <= "+maxNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  if(!crit_interv_medication_obj.getStartBeforeStartNested().isEmpty()) {
					  String crit_max_nested = makeCriterionList(crit_interv_medication_obj.getStartBeforeStartNested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_max_nested);
					  ArrayList<Criterion> list_of_max_nested=null;
					  list_of_max_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> maxNestedQueries = createNestedQueries(list_of_max_nested, false, true);
					  if(!tables.contains("dt_period_of_time")) {
						  tables += ", dt_period_of_time";
						  where_clause += " AND interv_medication.PERIOD_ID=dt_period_of_time.ID";
					  }
					  if(!tables.contains("dt_date1")) {
						  tables += ", dt_date AS dt_date1";
						  where_clause += " AND dt_period_of_time.START_DATE_ID=dt_date1.ID";
					  }
					  for(int k=0; k<maxNestedQueries.size(); k++) {
						  where_clause += " AND dt_date1.YEAR <= "+maxNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!crit_interv_medication_obj.getStartAfterEndNested().isEmpty()) {
					  String crit_min_nested = makeCriterionList(crit_interv_medication_obj.getStartAfterEndNested());
					  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_min_nested);
					  ArrayList<Criterion> list_of_min_nested=null;
					  list_of_min_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
					  ArrayList<String> minNestedQueries = createNestedQueries(list_of_min_nested, false, false);
					  if(!tables.contains("dt_period_of_time")) {
						  tables += ", dt_period_of_time";
						  where_clause += " AND interv_medication.PERIOD_ID=dt_period_of_time.ID";
					  }
					  if(!tables.contains("dt_date1")) {
						  tables += ", dt_date AS dt_date1";
						  where_clause += " AND dt_period_of_time.START_DATE_ID=dt_date1.ID";
					  }
					  for(int k=0; k<minNestedQueries.size(); k++) {
						  where_clause += " AND dt_date1.YEAR >= "+minNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  } 
				  
				  if(!crit_interv_medication_obj.getEndAfterEndNested().isEmpty()) {
					  String crit_min_nested = makeCriterionList(crit_interv_medication_obj.getEndAfterEndNested());
					  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_min_nested);
					  ArrayList<Criterion> list_of_min_nested=null;
					  list_of_min_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
					  ArrayList<String> minNestedQueries = createNestedQueries(list_of_min_nested, false, false);
					  if(!tables.contains("dt_period_of_time")) {
						  tables += ", dt_period_of_time";
						  where_clause += " AND interv_medication.PERIOD_ID=dt_period_of_time.ID";
					  }
					  if(!tables.contains("dt_date2")) {
						  tables += ", dt_date AS dt_date2";
						  where_clause += " AND dt_period_of_time.END_DATE_ID=dt_date2.ID";
					  }
					  for(int k=0; k<minNestedQueries.size(); k++) {
						  where_clause += " AND dt_date2.YEAR >= "+minNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  } 
				  
				  if(!crit_interv_medication_obj.getEndAfterStartNested().isEmpty()) {
					  String crit_start_period_nested = makeCriterionList(crit_interv_medication_obj.getEndAfterStartNested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_start_period_nested);
					  ArrayList<Criterion> list_of_start_period_nested=null;
					  list_of_start_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> startPeriodNestedQueries = createPeriodNestedQueries(list_of_start_period_nested, false, true);
					  if(!tables.contains("dt_period_of_time")) {
						  tables += ", dt_period_of_time";
						  where_clause += " AND interv_medication.PERIOD_ID=dt_period_of_time.ID";
					  }
					  if(!tables.contains("dt_date2")) {
						  tables += ", dt_date AS dt_date2";
						  where_clause += " AND dt_period_of_time.END_DATE_ID=dt_date2.ID";
					  }
					  for(int k=0; k<startPeriodNestedQueries.size(); k++) {
						  where_clause += " AND dt_date2.YEAR >= "+startPeriodNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!crit_interv_medication_obj.getEndBeforeEndNested().isEmpty()) {
					  String crit_end_period_nested = makeCriterionList(crit_interv_medication_obj.getEndBeforeEndNested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_end_period_nested);
					  ArrayList<Criterion> list_of_end_period_nested=null;
					  list_of_end_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> endPeriodNestedQueries = createPeriodNestedQueries(list_of_end_period_nested, false, false);
					  if(!tables.contains("dt_period_of_time")) {
						  tables += ", dt_period_of_time";
						  where_clause += " AND interv_medication.PERIOD_ID=dt_period_of_time.ID";
					  }
					  if(!tables.contains("dt_date2")) {
						  tables += ", dt_date AS dt_date2";
						  where_clause += " AND dt_period_of_time.END_DATE_ID=dt_date2.ID";
					  }
					  for(int k=0; k<endPeriodNestedQueries.size(); k++) {
						  where_clause += " AND dt_date2.YEAR <= "+endPeriodNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!crit_interv_medication_obj.getStartBeforeEndNested().isEmpty()) {
					  String crit_end_period_nested = makeCriterionList(crit_interv_medication_obj.getStartBeforeEndNested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_end_period_nested);
					  ArrayList<Criterion> list_of_end_period_nested=null;
					  list_of_end_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> endPeriodNestedQueries = createPeriodNestedQueries(list_of_end_period_nested, false, false);
					  if(!tables.contains("dt_period_of_time")) {
						  tables += ", dt_period_of_time";
						  where_clause += " AND interv_medication.PERIOD_ID=dt_period_of_time.ID";
					  }
					  if(!tables.contains("dt_date1")) {
						  tables += ", dt_date AS dt_date1";
						  where_clause += " AND dt_period_of_time.START_DATE_ID=dt_date1.ID";
					  }
					  for(int k=0; k<endPeriodNestedQueries.size(); k++) {
						  where_clause += " AND dt_date1.YEAR <= "+endPeriodNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!crit_interv_medication_obj.getStartAfterStartNested().isEmpty()) {
					  String crit_start_period_nested = makeCriterionList(crit_interv_medication_obj.getStartAfterStartNested());
					  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_start_period_nested);
					  ArrayList<Criterion> list_of_start_period_nested=null;
					  list_of_start_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
					  ArrayList<String> startPeriodNestedQueries = createPeriodNestedQueries(list_of_start_period_nested, false, true);
					  if(!tables.contains("dt_period_of_time")) {
						  tables += ", dt_period_of_time";
						  where_clause += " AND interv_medication.PERIOD_ID=dt_period_of_time.ID";
					  }
					  if(!tables.contains("dt_date1")) {
						  tables += ", dt_date AS dt_date1";
						  where_clause += " AND dt_period_of_time.START_DATE_ID=dt_date1.ID";
					  }
					  for(int k=0; k<startPeriodNestedQueries.size(); k++) {
						  where_clause += " AND dt_date1.YEAR >= "+startPeriodNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!incl) query = "SELECT DISTINCT patient.ID FROM patient WHERE patient.ID NOT IN (" +query+ ")";
				  //if(incl) query = "SELECT DISTINCT patient.ID FROM " + tables + " WHERE " + where_clause + " AND " + temp_where_clause;
				  //else query = "SELECT DISTINCT patient.ID FROM " + tables + " WHERE " + where_clause + " AND NOT(" + temp_where_clause + ")";
					
			  } break;
			  
			  case "intervention_chemotherapy": { //Check if user provided the info of all the fields 
				  intervention_chemotherapy  crit_interv_chemotherapy_obj =  (intervention_chemotherapy)current_Criterion;
				  String tables = "patient, interv_chemotherapy";
				  String where_clause = "patient.ID = interv_chemotherapy.PATIENT_ID";
				 
				  if(!crit_interv_chemotherapy_obj.getReason().isEmpty()) {
					  tables += ", voc_confirmation AS conf_1";
					  where_clause += " AND interv_chemotherapy.DUE_TO_PSS_ID = conf_1.ID AND "+Make_OR_of_CODES("conf_1.CODE", crit_interv_chemotherapy_obj.getReason());
				  }
				  
				  
				  if(!(crit_interv_chemotherapy_obj.getChem_exact_date_year()).isEmpty()) {	
					  tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
					  where_clause += Make_begin_end_period_query (mode,"interv_chemotherapy.PERIOD_ID", "dt_date1", "dt_date2", crit_interv_chemotherapy_obj.getChem_exact_date_year(), 
					  			crit_interv_chemotherapy_obj.getChem_exact_date_month(), crit_interv_chemotherapy_obj.getChem_exact_date_day(),
					  			crit_interv_chemotherapy_obj.getChem_exact_date_year(), crit_interv_chemotherapy_obj.getChem_exact_date_month(),
					  			crit_interv_chemotherapy_obj.getChem_exact_date_day()); 						  
					} else if(!(crit_interv_chemotherapy_obj.getChem_period_begin_year()).isEmpty() || !(crit_interv_chemotherapy_obj.getChem_period_end_year()).isEmpty()) {
						tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
						where_clause += Make_begin_end_period_query (mode,"interv_chemotherapy.PERIOD_ID", "dt_date1", "dt_date2", crit_interv_chemotherapy_obj.getChem_period_begin_year(), 
								crit_interv_chemotherapy_obj.getChem_period_begin_month(), crit_interv_chemotherapy_obj.getChem_period_begin_day(),
								crit_interv_chemotherapy_obj.getChem_period_end_year(), crit_interv_chemotherapy_obj.getChem_period_end_month(),
								crit_interv_chemotherapy_obj.getChem_period_end_day()); 												
					} else if(!(crit_interv_chemotherapy_obj.getChem_until_date_year()).isEmpty()) {
						tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
						where_clause += Make_begin_end_period_query (mode,"interv_chemotherapy.PERIOD_ID", "dt_date1", "dt_date2", "1800", 
								  "1", "1",crit_interv_chemotherapy_obj.getChem_until_date_year(), crit_interv_chemotherapy_obj.getChem_until_date_month(),
								  crit_interv_chemotherapy_obj.getChem_until_date_day()); 								
					}
				  
				  if(!crit_interv_chemotherapy_obj.getStatement().isEmpty()) {
					  tables += ", voc_confirmation AS conf_2";
					  where_clause += " AND interv_chemotherapy.STMT_ID=conf_2.ID AND conf_2.CODE='"+crit_interv_chemotherapy_obj.getStatement() + "'";
				  }
				  where_clause += " AND interv_chemotherapy.STMT_ID=1";
				  
				  if(!crit_interv_chemotherapy_obj.getMinCount().isEmpty()) {
				  		if(!crit_interv_chemotherapy_obj.getMaxCount().isEmpty()) {
				  			where_clause += " GROUP BY patient.ID HAVING COUNT(*) BETWEEN "+crit_interv_chemotherapy_obj.getMinCount()+" AND "+crit_interv_chemotherapy_obj.getMaxCount();
				  		}
				  		else where_clause += " GROUP BY patient.ID HAVING COUNT(*) >= "+crit_interv_chemotherapy_obj.getMinCount();
				  }
				  else if(!crit_interv_chemotherapy_obj.getMaxCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.ID HAVING COUNT(*) <= "+crit_interv_chemotherapy_obj.getMaxCount();
				  }
				 
				  query = "SELECT DISTINCT patient.ID FROM " + tables + " WHERE " + where_clause;
				  
				  if(!crit_interv_chemotherapy_obj.getEndBeforeStartNested().isEmpty()) {
					  String crit_max_nested = makeCriterionList(crit_interv_chemotherapy_obj.getEndBeforeStartNested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_max_nested);
					  ArrayList<Criterion> list_of_max_nested=null;
					  list_of_max_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> maxNestedQueries = createNestedQueries(list_of_max_nested, false, true);
					  if(!tables.contains("dt_period_of_time")) {
						  tables += ", dt_period_of_time";
						  where_clause += " AND interv_chemotherapy.PERIOD_ID=dt_period_of_time.ID";
					  }
					  if(!tables.contains("dt_date2")) {
						  tables += ", dt_date AS dt_date2";
						  where_clause += " AND dt_period_of_time.END_DATE_ID=dt_date2.ID";
					  }
					  for(int k=0; k<maxNestedQueries.size(); k++) {
						  where_clause += " AND dt_date2.YEAR <= "+maxNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  if(!crit_interv_chemotherapy_obj.getStartBeforeStartNested().isEmpty()) {
					  String crit_max_nested = makeCriterionList(crit_interv_chemotherapy_obj.getStartBeforeStartNested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_max_nested);
					  ArrayList<Criterion> list_of_max_nested=null;
					  list_of_max_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> maxNestedQueries = createNestedQueries(list_of_max_nested, false, true);
					  if(!tables.contains("dt_period_of_time")) {
						  tables += ", dt_period_of_time";
						  where_clause += " AND interv_chemotherapy.PERIOD_ID=dt_period_of_time.ID";
					  }
					  if(!tables.contains("dt_date1")) {
						  tables += ", dt_date AS dt_date1";
						  where_clause += " AND dt_period_of_time.START_DATE_ID=dt_date1.ID";
					  }
					  for(int k=0; k<maxNestedQueries.size(); k++) {
						  where_clause += " AND dt_date1.YEAR <= "+maxNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!crit_interv_chemotherapy_obj.getStartAfterEndNested().isEmpty()) {
					  String crit_min_nested = makeCriterionList(crit_interv_chemotherapy_obj.getStartAfterEndNested());
					  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_min_nested);
					  ArrayList<Criterion> list_of_min_nested=null;
					  list_of_min_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
					  ArrayList<String> minNestedQueries = createNestedQueries(list_of_min_nested, false, false);
					  if(!tables.contains("dt_period_of_time")) {
						  tables += ", dt_period_of_time";
						  where_clause += " AND interv_chemotherapy.PERIOD_ID=dt_period_of_time.ID";
					  }
					  if(!tables.contains("dt_date1")) {
						  tables += ", dt_date AS dt_date1";
						  where_clause += " AND dt_period_of_time.START_DATE_ID=dt_date1.ID";
					  }
					  for(int k=0; k<minNestedQueries.size(); k++) {
						  where_clause += " AND dt_date1.YEAR >= "+minNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  } 
				  
				  if(!crit_interv_chemotherapy_obj.getEndAfterEndNested().isEmpty()) {
					  String crit_min_nested = makeCriterionList(crit_interv_chemotherapy_obj.getEndAfterEndNested());
					  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_min_nested);
					  ArrayList<Criterion> list_of_min_nested=null;
					  list_of_min_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
					  ArrayList<String> minNestedQueries = createNestedQueries(list_of_min_nested, false, false);
					  if(!tables.contains("dt_period_of_time")) {
						  tables += ", dt_period_of_time";
						  where_clause += " AND interv_chemotherapy.PERIOD_ID=dt_period_of_time.ID";
					  }
					  if(!tables.contains("dt_date2")) {
						  tables += ", dt_date AS dt_date2";
						  where_clause += " AND dt_period_of_time.END_DATE_ID=dt_date2.ID";
					  }
					  for(int k=0; k<minNestedQueries.size(); k++) {
						  where_clause += " AND dt_date2.YEAR >= "+minNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  } 
				  
				  if(!crit_interv_chemotherapy_obj.getEndAfterStartNested().isEmpty()) {
					  String crit_start_period_nested = makeCriterionList(crit_interv_chemotherapy_obj.getEndAfterStartNested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_start_period_nested);
					  ArrayList<Criterion> list_of_start_period_nested=null;
					  list_of_start_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> startPeriodNestedQueries = createPeriodNestedQueries(list_of_start_period_nested, false, true);
					  if(!tables.contains("dt_period_of_time")) {
						  tables += ", dt_period_of_time";
						  where_clause += " AND interv_chemotherapy.PERIOD_ID=dt_period_of_time.ID";
					  }
					  if(!tables.contains("dt_date2")) {
						  tables += ", dt_date AS dt_date2";
						  where_clause += " AND dt_period_of_time.END_DATE_ID=dt_date2.ID";
					  }
					  for(int k=0; k<startPeriodNestedQueries.size(); k++) {
						  where_clause += " AND dt_date2.YEAR >= "+startPeriodNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!crit_interv_chemotherapy_obj.getEndBeforeEndNested().isEmpty()) {
					  String crit_end_period_nested = makeCriterionList(crit_interv_chemotherapy_obj.getEndBeforeEndNested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_end_period_nested);
					  ArrayList<Criterion> list_of_end_period_nested=null;
					  list_of_end_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> endPeriodNestedQueries = createPeriodNestedQueries(list_of_end_period_nested, false, false);
					  if(!tables.contains("dt_period_of_time")) {
						  tables += ", dt_period_of_time";
						  where_clause += " AND interv_chemotherapy.PERIOD_ID=dt_period_of_time.ID";
					  }
					  if(!tables.contains("dt_date2")) {
						  tables += ", dt_date AS dt_date2";
						  where_clause += " AND dt_period_of_time.END_DATE_ID=dt_date2.ID";
					  }
					  for(int k=0; k<endPeriodNestedQueries.size(); k++) {
						  where_clause += " AND dt_date2.YEAR <= "+endPeriodNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!crit_interv_chemotherapy_obj.getStartBeforeEndNested().isEmpty()) {
					  String crit_end_period_nested = makeCriterionList(crit_interv_chemotherapy_obj.getStartBeforeEndNested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_end_period_nested);
					  ArrayList<Criterion> list_of_end_period_nested=null;
					  list_of_end_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> endPeriodNestedQueries = createPeriodNestedQueries(list_of_end_period_nested, false, false);
					  if(!tables.contains("dt_period_of_time")) {
						  tables += ", dt_period_of_time";
						  where_clause += " AND interv_chemotherapy.PERIOD_ID=dt_period_of_time.ID";
					  }
					  if(!tables.contains("dt_date1")) {
						  tables += ", dt_date AS dt_date1";
						  where_clause += " AND dt_period_of_time.START_DATE_ID=dt_date1.ID";
					  }
					  for(int k=0; k<endPeriodNestedQueries.size(); k++) {
						  where_clause += " AND dt_date1.YEAR <= "+endPeriodNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!crit_interv_chemotherapy_obj.getStartAfterStartNested().isEmpty()) {
					  String crit_start_period_nested = makeCriterionList(crit_interv_chemotherapy_obj.getStartAfterStartNested());
					  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_start_period_nested);
					  ArrayList<Criterion> list_of_start_period_nested=null;
					  list_of_start_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
					  ArrayList<String> startPeriodNestedQueries = createPeriodNestedQueries(list_of_start_period_nested, false, true);
					  if(!tables.contains("dt_period_of_time")) {
						  tables += ", dt_period_of_time";
						  where_clause += " AND interv_chemotherapy.PERIOD_ID=dt_period_of_time.ID";
					  }
					  if(!tables.contains("dt_date1")) {
						  tables += ", dt_date AS dt_date1";
						  where_clause += " AND dt_period_of_time.START_DATE_ID=dt_date1.ID";
					  }
					  for(int k=0; k<startPeriodNestedQueries.size(); k++) {
						  where_clause += " AND dt_date1.YEAR >= "+startPeriodNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!incl) query = "SELECT DISTINCT patient.ID FROM patient WHERE patient.ID NOT IN (" +query+ ")";
					//results_of_one_Criterion=DBServiceCRUD.getDataFromDB(query); 
					//System.out.println("We executed: "+crit_interv_chemotherapy_obj.criterion_name+"\nThe Query is: "+query); 
			  } break;
			  
			  case "intervention_surgery": { //Check if user provided the info of all the fields 
				  intervention_surgery  crit_interv_surgery_obj =  (intervention_surgery)current_Criterion;
				  
				  String tables = "patient, interv_surgery";
				  String where_clause = "patient.ID = interv_surgery.PATIENT_ID";
				  
				  if(!crit_interv_surgery_obj.getReason().isEmpty()) {
					  tables += ", voc_confirmation AS conf_1";
					  where_clause += " AND interv_surgery.DUE_TO_PSS_ID = conf_1.ID AND "+Make_OR_of_CODES("conf_1.CODE", crit_interv_surgery_obj.getReason());
				  }
				  
				  
				  //AND conf_1.CODE= '"+crit_interv_surgery_obj.getReason()+"'";
				  
				  if(!(crit_interv_surgery_obj.getSurgery_exact_date_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_specific_date_query(mode, "interv_surgery.SURGERY_DATE_ID","dt_date",crit_interv_surgery_obj.getSurgery_exact_date_year(), //check cond_symptom.OBSERVE_DATE_ID
							  crit_interv_surgery_obj.getSurgery_exact_date_month(), crit_interv_surgery_obj.getSurgery_exact_date_day());
					  
				  } else if(!(crit_interv_surgery_obj.getSurgery_period_begin_year()).isEmpty() || !(crit_interv_surgery_obj.getSurgery_period_end_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (mode,"interv_surgery.SURGERY_DATE_ID", "dt_date",crit_interv_surgery_obj.getSurgery_period_begin_year(),
							  crit_interv_surgery_obj.getSurgery_period_begin_month(), crit_interv_surgery_obj.getSurgery_period_begin_day(), crit_interv_surgery_obj.getSurgery_period_end_year() , 
							  crit_interv_surgery_obj.getSurgery_period_end_month(), crit_interv_surgery_obj.getSurgery_period_end_day()); 
					  
				  } else if(!(crit_interv_surgery_obj.getSurgery_until_date_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (mode,"interv_surgery.SURGERY_DATE_ID","dt_date", "1800", "1", "1", crit_interv_surgery_obj.getSurgery_until_date_year(), 
							  crit_interv_surgery_obj.getSurgery_until_date_month(), crit_interv_surgery_obj.getSurgery_until_date_day()); 
				  }
				  
				  if(!crit_interv_surgery_obj.getStatement().isEmpty()) {
					  tables += ", voc_confirmation AS conf_2";
					  where_clause += " AND interv_surgery.STMT_ID=conf_2.ID AND conf_2.CODE='" + crit_interv_surgery_obj.getStatement() + "'";
				  }
				  
				  where_clause += " AND interv_surgery.STMT_ID=1";
				  
				  if(!crit_interv_surgery_obj.getMinCount().isEmpty()) {
				  		if(!crit_interv_surgery_obj.getMaxCount().isEmpty()) {
				  			where_clause += " GROUP BY patient.ID HAVING COUNT(*) BETWEEN "+crit_interv_surgery_obj.getMinCount()+" AND "+crit_interv_surgery_obj.getMaxCount();
				  		}
				  		else where_clause += " GROUP BY patient.ID HAVING COUNT(*) >= "+crit_interv_surgery_obj.getMinCount();
				  }
				  else if(!crit_interv_surgery_obj.getMaxCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.ID HAVING COUNT(*) <= "+crit_interv_surgery_obj.getMaxCount();
				  }
				  
				  query = "SELECT DISTINCT patient.ID FROM " + tables + " WHERE " + where_clause;
				  
				  if(!crit_interv_surgery_obj.getMaxNested().isEmpty()) {
					  String crit_max_nested = makeCriterionList(crit_interv_surgery_obj.getMaxNested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_max_nested);
					  ArrayList<Criterion> list_of_max_nested=null;
					  list_of_max_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> maxNestedQueries = createNestedQueries(list_of_max_nested, false, true);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND interv_surgery.SURGERY_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<maxNestedQueries.size(); k++) {
						  where_clause += " AND (dt_date.YEAR2 <= "+maxNestedQueries.get(k)+" OR (dt_date.YEAR2 IS NULL AND dt_date.YEAR <= "+maxNestedQueries.get(k)+"))";
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!crit_interv_surgery_obj.getMinNested().isEmpty()) {
					  String crit_min_nested = makeCriterionList(crit_interv_surgery_obj.getMinNested());
					  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_min_nested);
					  ArrayList<Criterion> list_of_min_nested=null;
					  list_of_min_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
					  ArrayList<String> minNestedQueries = createNestedQueries(list_of_min_nested, false, false);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND interv_surgery.SURGERY_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<minNestedQueries.size(); k++) {
						  where_clause += " AND dt_date.YEAR >= "+minNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }  
				  
				  if(!crit_interv_surgery_obj.getEndPeriodNested().isEmpty()) {
					  String crit_end_period_nested = makeCriterionList(crit_interv_surgery_obj.getEndPeriodNested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_end_period_nested);
					  ArrayList<Criterion> list_of_end_period_nested=null;
					  list_of_end_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> endPeriodNestedQueries = createPeriodNestedQueries(list_of_end_period_nested, false, false);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND interv_surgery.SURGERY_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<endPeriodNestedQueries.size(); k++) {
						  where_clause += " AND (dt_date.YEAR2 <= "+endPeriodNestedQueries.get(k)+" OR (dt_date.YEAR2 IS NULL AND dt_date.YEAR <= "+endPeriodNestedQueries.get(k)+"))";
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!crit_interv_surgery_obj.getStartPeriodNested().isEmpty()) {
					  String crit_start_period_nested = makeCriterionList(crit_interv_surgery_obj.getStartPeriodNested());
					  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_start_period_nested);
					  ArrayList<Criterion> list_of_start_period_nested=null;
					  list_of_start_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
					  ArrayList<String> startPeriodNestedQueries = createPeriodNestedQueries(list_of_start_period_nested, false, true);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND interv_surgery.SURGERY_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<startPeriodNestedQueries.size(); k++) {
						  where_clause += " AND dt_date.YEAR >= "+startPeriodNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!incl) query = "SELECT DISTINCT patient.ID FROM patient WHERE patient.ID NOT IN (" +query+ ")";
			  } break;
			  
			  case "examination_lab_test": { //Check if user provided the info of all the fields 
				  examination_lab_test  examination_lab_test_obj =  (examination_lab_test)current_Criterion;
				  
				  String tables = "patient, exam_lab_test";
				  String where_clause = "patient.ID = exam_lab_test.PATIENT_ID"; 
				  
				  if(!examination_lab_test_obj.getTest_id().isEmpty()) {
					  tables += ", voc_lab_test";
					  where_clause += " AND exam_lab_test.TEST_ID=voc_lab_test.ID AND (" + Make_OR_of_CODES("voc_lab_test.CODE", examination_lab_test_obj.getTest_id());
					  String codes[] = examination_lab_test_obj.getTest_id().split(",");
					  for(int k=0; k<codes.length; k++) {
						  String narrowTerms = getTermsWithNarrowMeaning(codes[k].trim());
						  String[] allNarrowTerms = narrowTerms.split(",");
						  for(int c=1; c<allNarrowTerms.length; c++) {
							  where_clause += " OR " + Make_OR_of_CODES("voc_lab_test.CODE", allNarrowTerms[c]);
						  }
					  }
					  where_clause += ")";
				  }
				  
					
				  /*query = "SELECT DISTINCT patient.ID " +
						  "FROM patient, exam_lab_test, voc_lab_test " +//, exam_lab_test, voc_lab_test, dt_amount, voc_unit, voc_assessment, dt_amount_range, dt_date " + //, voc_direction interv_Surgery, dt_date, voc_direction, voc_confirmation
						  "WHERE patient.ID = exam_lab_test.PATIENT_ID AND " + 
						  "exam_lab_test.TEST_ID=voc_lab_test.ID " +
						  //"voc_lab_test.CODE='"+crit_exam_lab_test_obj.getTEST_ID_voc_lab_test_CODE()+"' ";
				  		  "AND " + Make_OR_of_CODES("voc_lab_test.CODE", examination_lab_test_obj.getTest_id());  // [TEST_ID]
				  System.out.println("We received: "+examination_lab_test_obj.getNormal_range_value());*/
				  
				  if(!examination_lab_test_obj.getOutcome_amount_exact_value().isEmpty()) { // [OUTCOME_AMOUNT]
					  tables += ", dt_amount, voc_unit";
					  where_clause += " AND exam_lab_test.OUTCOME_AMOUNT_ID = dt_amount.ID AND (dt_amount.VALUE = '"+examination_lab_test_obj.getOutcome_amount_exact_value()+"' OR (dt_amount.VALUE <= " +examination_lab_test_obj.getOutcome_amount_exact_value()+" AND dt_amount.VALUE2 >= "+examination_lab_test_obj.getOutcome_amount_exact_value()+")) "+
							  	"AND dt_amount.UNIT_ID=voc_unit.ID " +
							  	"AND voc_unit.CODE ='"+examination_lab_test_obj.getOutcome_amount_unit()+"' ";
					  
				  } else
				  
				  if(!examination_lab_test_obj.getOutcome_amount_range_min_value().isEmpty()&&!examination_lab_test_obj.getOutcome_amount_range_max_value().isEmpty()) { // [OUTCOME_AMOUNT]
					  tables += ", dt_amount, voc_unit";	
					  where_clause += " AND exam_lab_test.OUTCOME_AMOUNT_ID = dt_amount.ID AND dt_amount.VALUE <= '"+examination_lab_test_obj.getOutcome_amount_range_max_value()+"' " +
					  	"AND (dt_amount.VALUE >= '"+examination_lab_test_obj.getOutcome_amount_range_min_value()+"' OR dt_amount.VALUE2 >= " +examination_lab_test_obj.getOutcome_amount_range_min_value()+") "+
					  	"AND dt_amount.UNIT_ID=voc_unit.ID " +
					  	"AND voc_unit.CODE ='"+examination_lab_test_obj.getOutcome_amount_unit()+"' ";
				  } else
					  
				  if(examination_lab_test_obj.getOutcome_amount_range_min_value().isEmpty()&&!examination_lab_test_obj.getOutcome_amount_range_max_value().isEmpty()) { // [OUTCOME_AMOUNT]
					  tables += ", dt_amount, voc_unit";	
					  where_clause += " AND exam_lab_test.OUTCOME_AMOUNT_ID = dt_amount.ID AND dt_amount.VALUE <= '"+examination_lab_test_obj.getOutcome_amount_range_max_value()+"' " +
					  	"AND dt_amount.UNIT_ID=voc_unit.ID " +
					  	"AND voc_unit.CODE ='"+examination_lab_test_obj.getOutcome_amount_unit()+"' ";
				  } else
					  
				  if(!examination_lab_test_obj.getOutcome_amount_range_min_value().isEmpty()&&examination_lab_test_obj.getOutcome_amount_range_max_value().isEmpty()) { // [OUTCOME_AMOUNT]
					  tables += ", dt_amount, voc_unit";
					  where_clause += " AND exam_lab_test.OUTCOME_AMOUNT_ID = dt_amount.ID " + //AND dt_amount.VALUE < '"+examination_lab_test_obj.getOutcome_amount_range_max_value()+"' 
					  	"AND (dt_amount.VALUE >= '"+examination_lab_test_obj.getOutcome_amount_range_min_value()+"' OR dt_amount.VALUE2 >= " +examination_lab_test_obj.getOutcome_amount_range_min_value()+") "+
					  	"AND dt_amount.UNIT_ID=voc_unit.ID " +
					  	"AND voc_unit.CODE ='"+examination_lab_test_obj.getOutcome_amount_unit()+"' ";
				  }
					  
				  
				  if(!examination_lab_test_obj.getOutcome_assessment().isEmpty()) {  // [OUTCOME_ASSESSMENT]
					  tables += ", voc_assessment";	
					  where_clause += " AND exam_lab_test.OUTCOME_ASSESSMENT_ID = voc_assessment.ID " + 
					  	//"AND voc_assessment.CODE ='"+crit_exam_lab_test_obj.OUTCOME_ASSESSMENT_ID_voc_assessment_CODE+"' ";
					  	"AND (" + Make_OR_of_CODES("voc_assessment.CODE", examination_lab_test_obj.getOutcome_assessment());
					  String narrowTermsAssess = getTermsWithNarrowMeaning(examination_lab_test_obj.getOutcome_assessment());
					  String[] allNarrowTermsAssess = narrowTermsAssess.split(",");
					  for(int c=1; c<allNarrowTermsAssess.length; c++) {
						  where_clause += " OR " + Make_OR_of_CODES("voc_assessment.CODE", allNarrowTermsAssess[c]);
					  }
					  where_clause += ")";
				  }
				  
				  //TODO NORMAL_RANGE  does it check if the value belongs in these two limits.		  
				  if(!(examination_lab_test_obj.getNormal_range_value()).isEmpty()) {
					  tables += ", dt_amount_range, voc_unit";	
					  where_clause += " AND exam_lab_test.NORMAL_RANGE_ID = dt_amount_range.ID " +
					  	"AND dt_amount_range.VALUE1 <= '"+examination_lab_test_obj.getNormal_range_value()+"' " +
					  	"AND dt_amount_range.VALUE2 >= '"+examination_lab_test_obj.getNormal_range_value()+"' " +
					    "AND dt_amount_range.UNIT_ID = voc_unit.ID AND voc_unit.CODE='"+examination_lab_test_obj.getOutcome_amount_unit()+"' ";
				  };
				  
				  
				  if(!(examination_lab_test_obj.getOutcome_term()).isEmpty()) {
					  String outcome_term= examination_lab_test_obj.getOutcome_term();
					  String codes[] = outcome_term.split(",");
					  boolean conf = false;
					  boolean cryo = false;
					  boolean ana = false;
					  for(int j=0; j<codes.length; j++) {
						  codes[j] = codes[j].trim();
						  if (codes[j].equals("CONFIRM-01")||codes[j].equals("CONFIRM-02")) {
							  if(!conf) {
								  conf = true;
								  tables += ", voc_confirmation";
							  }
							  if(j==0) where_clause += " AND (exam_lab_test.OUTCOME_TERM_ID = voc_confirmation.ID ";
							  else where_clause += " OR exam_lab_test.OUTCOME_TERM_ID = voc_confirmation.ID ";
							  where_clause += "AND voc_confirmation.CODE='"+codes[j] + "'";
						  }
						  else if(codes[j].equals("CRYO-01")||codes[j].equals("CRYO-02")||codes[j].equals("CRYO-03")){
							  if(!cryo) {
								  cryo = true;
								  tables += ", voc_cryo_type";
							  }
							  if(j==0) where_clause += " AND (exam_lab_test.OUTCOME_TERM_ID = voc_cryo_type.ID ";  
							  else where_clause += " OR exam_lab_test.OUTCOME_TERM_ID = voc_cryo_type.ID "; 
							  where_clause += "AND voc_cryo_type.CODE='"+codes[j]+ "'";
						  }
						  else {
							  if(!ana) {
								  ana = true;
								  tables += ", voc_ana_pattern";
							  }
							  if(j==0) where_clause += " AND (exam_lab_test.OUTCOME_TERM_ID = voc_ana_pattern.ID ";
							  else where_clause += " OR exam_lab_test.OUTCOME_TERM_ID = voc_ana_pattern.ID ";
							  where_clause += "AND voc_ana_pattern.CODE='"+codes[j]+ "'";
						  }
							
					  }
					  where_clause += ")";
					  
					}
				  
				  if(!(examination_lab_test_obj.getSample_period_of_time_exact_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_specific_date_query(mode, "exam_lab_test.SAMPLE_DATE_ID","dt_date",examination_lab_test_obj.getSample_period_of_time_exact_year(), 
							  			examination_lab_test_obj.getSample_period_of_time_exact_month(), examination_lab_test_obj.getSample_period_of_time_exact_day());					  		
				  } 
				  
				  else if(!(examination_lab_test_obj.getSample_period_of_time_interval_start_year()).isEmpty() || !(examination_lab_test_obj.getSample_period_of_time_interval_end_year()).isEmpty()) {
							 
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (mode,"exam_lab_test.SAMPLE_DATE_ID", "dt_date",examination_lab_test_obj.getSample_period_of_time_interval_start_year(), 
										examination_lab_test_obj.getSample_period_of_time_interval_start_month(), examination_lab_test_obj.getSample_period_of_time_interval_start_day(),
										examination_lab_test_obj.getSample_period_of_time_interval_end_year(), examination_lab_test_obj.getSample_period_of_time_interval_end_month(),
										examination_lab_test_obj.getSample_period_of_time_interval_end_day()); 			  
						  } 
				  
				  else if(!(examination_lab_test_obj.getSample_period_of_time_until_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (mode,"exam_lab_test.SAMPLE_DATE_ID","dt_date", "1800", "1", "1", examination_lab_test_obj.getSample_period_of_time_until_year(), examination_lab_test_obj.getSample_period_of_time_until_month(),
									  examination_lab_test_obj.getSample_period_of_time_until_day()); 
						  }
				  
				  if(!examination_lab_test_obj.getMinCount().isEmpty()) {
				  		if(!examination_lab_test_obj.getMaxCount().isEmpty()) {
				  			where_clause += " GROUP BY patient.ID HAVING COUNT(*) BETWEEN "+examination_lab_test_obj.getMinCount()+" AND "+examination_lab_test_obj.getMaxCount();
				  		}
				  		else where_clause += " GROUP BY patient.ID HAVING COUNT(*) >= "+examination_lab_test_obj.getMinCount();
				  }
				  else if(!examination_lab_test_obj.getMaxCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.ID HAVING COUNT(*) <= "+examination_lab_test_obj.getMaxCount();
				  }
				  
				  query = "SELECT DISTINCT patient.ID FROM " + tables + " WHERE " + where_clause;
				  
				  if(!examination_lab_test_obj.getMaxNested().isEmpty()) {
					  String crit_max_nested = makeCriterionList(examination_lab_test_obj.getMaxNested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_max_nested);
					  ArrayList<Criterion> list_of_max_nested=null;
					  list_of_max_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> maxNestedQueries = createNestedQueries(list_of_max_nested, false, true);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND exam_lab_test.SAMPLE_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<maxNestedQueries.size(); k++) {
						  where_clause += " AND (dt_date.YEAR2 <= "+maxNestedQueries.get(k)+" OR (dt_date.YEAR2 IS NULL AND dt_date.YEAR <= "+maxNestedQueries.get(k)+"))";
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!examination_lab_test_obj.getMinNested().isEmpty()) {
					  String crit_min_nested = makeCriterionList(examination_lab_test_obj.getMinNested());
					  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_min_nested);
					  ArrayList<Criterion> list_of_min_nested=null;
					  list_of_min_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
					  ArrayList<String> minNestedQueries = createNestedQueries(list_of_min_nested, false, false);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND exam_lab_test.SAMPLE_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<minNestedQueries.size(); k++) {
						  where_clause += " AND dt_date.YEAR >= "+minNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }  
				  
				  if(!examination_lab_test_obj.getEndPeriodNested().isEmpty()) {
					  String crit_end_period_nested = makeCriterionList(examination_lab_test_obj.getEndPeriodNested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_end_period_nested);
					  ArrayList<Criterion> list_of_end_period_nested=null;
					  list_of_end_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> endPeriodNestedQueries = createPeriodNestedQueries(list_of_end_period_nested, false, false);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND exam_lab_test.SAMPLE_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<endPeriodNestedQueries.size(); k++) {
						  where_clause += " AND (dt_date.YEAR2 <= "+endPeriodNestedQueries.get(k)+" OR (dt_date.YEAR2 IS NULL AND dt_date.YEAR <= "+endPeriodNestedQueries.get(k)+"))";
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!examination_lab_test_obj.getStartPeriodNested().isEmpty()) {
					  String crit_start_period_nested = makeCriterionList(examination_lab_test_obj.getStartPeriodNested());
					  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_start_period_nested);
					  ArrayList<Criterion> list_of_start_period_nested=null;
					  list_of_start_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
					  ArrayList<String> startPeriodNestedQueries = createPeriodNestedQueries(list_of_start_period_nested, false, true);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND exam_lab_test.SAMPLE_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<startPeriodNestedQueries.size(); k++) {
						  where_clause += " AND dt_date.YEAR >= "+startPeriodNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!incl) query = "SELECT DISTINCT patient.ID FROM patient WHERE patient.ID NOT IN (" +query+ ")";
			  } break; 
			  
			  case "examination_biopsy": { //Check if user provided the info of all the fields 
				  examination_biopsy  examination_biopsy_obj =  (examination_biopsy)current_Criterion;
				  
				  String tables = "patient, exam_biopsy";
				  String where_clause = "patient.ID = exam_biopsy.PATIENT_ID";
				  
				  if(!examination_biopsy_obj.getBiopsy_type().isEmpty()) {
					  tables += ", voc_biopsy";
					  where_clause += " AND exam_biopsy.BIOPSY_ID=voc_biopsy.ID AND ("+Make_OR_of_CODES("voc_biopsy.CODE",examination_biopsy_obj.getBiopsy_type()); 
					  String codes[] = examination_biopsy_obj.getBiopsy_type().split(",");
					  for(int k=0; k<codes.length; k++) {
						  String narrowTerms = getTermsWithNarrowMeaning(codes[k].trim());
						  String[] allNarrowTerms = narrowTerms.split(",");
						  for(int c=1; c<allNarrowTerms.length; c++) {
							  where_clause += " OR " + Make_OR_of_CODES("voc_biopsy.CODE", allNarrowTerms[c]);
						  }
					  }
					  where_clause += ") ";
				  }
				  
					
				  
				  if(!(examination_biopsy_obj.getTest_id()).isEmpty()) {
					  tables += ", voc_lab_test";
					  where_clause += " AND exam_biopsy.TEST_ID = voc_lab_test.ID AND ("+Make_OR_of_CODES("voc_lab_test.CODE",examination_biopsy_obj.getTest_id()); //'BLOOD-100'
					  String codes[] = examination_biopsy_obj.getTest_id().split(",");
					  for(int k=0; k<codes.length; k++) {
						  String narrowTerms = getTermsWithNarrowMeaning(codes[k].trim());
						  String[] allNarrowTerms = narrowTerms.split(",");
						  for(int c=1; c<allNarrowTerms.length; c++) {
							  where_clause += " OR " + Make_OR_of_CODES("voc_lab_test.CODE", allNarrowTerms[c]);
						  }
					  }
					  where_clause += ") ";
				  }  
				  
			  
				  if(!examination_biopsy_obj.getOutcome_amount_exact_value().isEmpty()) { // [OUTCOME_AMOUNT]
					  tables += ", dt_amount, voc_unit";
					  where_clause += " AND exam_biopsy.OUTCOME_AMOUNT_ID = dt_amount.ID AND (dt_amount.VALUE = '"+examination_biopsy_obj.getOutcome_amount_exact_value()+"' OR (dt_amount.VALUE <= " +examination_biopsy_obj.getOutcome_amount_exact_value()+" AND dt_amount.VALUE2 >= " +examination_biopsy_obj.getOutcome_amount_exact_value()+")) "+
							  	"AND dt_amount.UNIT_ID=voc_unit.ID " +
							  	"AND voc_unit.CODE ='"+examination_biopsy_obj.getOutcome_amount_unit()+"' ";
					  
				  } else
				  
				  if(!examination_biopsy_obj.getOutcome_amount_range_min_value().isEmpty()&&!examination_biopsy_obj.getOutcome_amount_range_max_value().isEmpty()) { // [OUTCOME_AMOUNT]
					  tables += ", dt_amount, voc_unit";	
					  where_clause += " AND exam_biopsy.OUTCOME_AMOUNT_ID = dt_amount.ID AND dt_amount.VALUE <= '"+examination_biopsy_obj.getOutcome_amount_range_max_value()+"' " +
					  	"AND (dt_amount.VALUE >= '"+examination_biopsy_obj.getOutcome_amount_range_min_value()+"' OR dt_amount.VALUE2 >= '"+examination_biopsy_obj.getOutcome_amount_range_min_value()+"') "+
					  	"AND dt_amount.UNIT_ID=voc_unit.ID " +
					  	"AND voc_unit.CODE ='"+examination_biopsy_obj.getOutcome_amount_unit()+"' ";
				  } else
					  
				  if(examination_biopsy_obj.getOutcome_amount_range_min_value().isEmpty()&&!examination_biopsy_obj.getOutcome_amount_range_max_value().isEmpty()) { // [OUTCOME_AMOUNT]
					  tables += ", dt_amount, voc_unit";	
					  where_clause += " AND exam_biopsy.OUTCOME_AMOUNT_ID = dt_amount.ID AND dt_amount.VALUE <= '"+examination_biopsy_obj.getOutcome_amount_range_max_value()+"' " +
					  	"AND dt_amount.UNIT_ID=voc_unit.ID " +
					  	"AND voc_unit.CODE ='"+examination_biopsy_obj.getOutcome_amount_unit()+"' ";
				  } else
					  
				  if(!examination_biopsy_obj.getOutcome_amount_range_min_value().isEmpty()&&examination_biopsy_obj.getOutcome_amount_range_max_value().isEmpty()) { // [OUTCOME_AMOUNT]
					  tables += ", dt_amount, voc_unit";
					  where_clause += " AND exam_biopsy.OUTCOME_AMOUNT_ID = dt_amount.ID " + //AND dt_amount.VALUE < '"+examination_lab_test_obj.getOutcome_amount_range_max_value()+"' 
					  	"AND (dt_amount.VALUE >= '"+examination_biopsy_obj.getOutcome_amount_range_min_value()+"' OR dt_amount.VALUE2 >= " +examination_biopsy_obj.getOutcome_amount_range_min_value()+") "+
					  	"AND dt_amount.UNIT_ID=voc_unit.ID " +
					  	"AND voc_unit.CODE ='"+examination_biopsy_obj.getOutcome_amount_unit()+"' ";
				  }
					  					  
				  //TODO NORMAL_RANGE  does it check if the value belongs in these two limits.		  
				  if(!(examination_biopsy_obj.getNormal_range_value()).isEmpty()) {
					  tables += ", dt_amount_range, voc_unit";
					  where_clause += " AND exam_biopsy.NORMAL_RANGE_ID = dt_amount_range.ID " +
					  	"AND dt_amount_range.VALUE1 <= '"+examination_biopsy_obj.getNormal_range_value()+"' " +
					  	"AND dt_amount_range.VALUE2 >= '"+examination_biopsy_obj.getNormal_range_value()+"' " +
					    "AND dt_amount_range.UNIT_ID = voc_unit.ID AND voc_unit.CODE='"+examination_biopsy_obj.getOutcome_amount_unit()+"' ";
				  };
				  
				  if(!examination_biopsy_obj.getAssessment().isEmpty()) {  // [OUTCOME_ASSESSMENT]
					  tables += ", voc_assessment";
					  where_clause += " AND exam_biopsy.ASSESSMENT_ID = voc_assessment.ID " + 
					  	//"AND voc_assessment.CODE ='"+crit_exam_lab_test_obj.OUTCOME_ASSESSMENT_ID_voc_assessment_CODE+"' ";
					  	"AND (" + Make_OR_of_CODES("voc_assessment.CODE", examination_biopsy_obj.getAssessment());
					  String narrowTerms = getTermsWithNarrowMeaning(examination_biopsy_obj.getAssessment());
					  String[] allNarrowTerms = narrowTerms.split(",");
					  for(int c=1; c<allNarrowTerms.length; c++) {
						  where_clause += " OR " + Make_OR_of_CODES("voc_assessment.CODE", allNarrowTerms[c]);
					  }
					  where_clause += ")";
				  }

				  if(!examination_biopsy_obj.getOutcome_check().isEmpty()) {  // [OUTCOME_ASSESSMENT]
					  tables += ", voc_confirmation";
					  where_clause += " AND exam_biopsy.OUTCOME_CHECK_ID = voc_confirmation.ID " + 
					  	//"AND voc_assessment.CODE ='"+crit_exam_lab_test_obj.OUTCOME_ASSESSMENT_ID_voc_assessment_CODE+"' ";
					  	"AND " + Make_OR_of_CODES("voc_confirmation.CODE", examination_biopsy_obj.getOutcome_check());
				  }					  

				  if(!(examination_biopsy_obj.getBiopsy_period_of_time_exact_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_specific_date_query(mode, "exam_biopsy.BIOPSY_DATE_ID","dt_date",examination_biopsy_obj.getBiopsy_period_of_time_exact_year(), 
									  examination_biopsy_obj.getBiopsy_period_of_time_exact_month(), examination_biopsy_obj.getBiopsy_period_of_time_exact_day());					  		
						  } else if(!(examination_biopsy_obj.getBiopsy_period_of_time_interval_start_year()).isEmpty() || !(examination_biopsy_obj.getBiopsy_period_of_time_interval_end_year()).isEmpty()) {
							  tables += ", dt_date";
							  where_clause += Make_begin_end_date_query (mode,"exam_biopsy.BIOPSY_DATE_ID", "dt_date",examination_biopsy_obj.getBiopsy_period_of_time_interval_start_year(), 
									  examination_biopsy_obj.getBiopsy_period_of_time_interval_start_month(), examination_biopsy_obj.getBiopsy_period_of_time_interval_start_day(),
									  examination_biopsy_obj.getBiopsy_period_of_time_interval_end_year(), examination_biopsy_obj.getBiopsy_period_of_time_interval_end_month(),
									  examination_biopsy_obj.getBiopsy_period_of_time_interval_end_day()); 			  
						  } else if(!(examination_biopsy_obj.getBiopsy_period_of_time_until_year()).isEmpty()) {
							  tables += ", dt_date";
							  where_clause += Make_begin_end_date_query (mode,"exam_biopsy.BIOPSY_DATE_ID","dt_date", "1800", "1", "1", examination_biopsy_obj.getBiopsy_period_of_time_until_year(), examination_biopsy_obj.getBiopsy_period_of_time_until_month(),
									  examination_biopsy_obj.getBiopsy_period_of_time_until_day()); 
						  }
				  
				  if(!examination_biopsy_obj.getMinCount().isEmpty()) {
				  		if(!examination_biopsy_obj.getMaxCount().isEmpty()) {
				  			where_clause += " GROUP BY patient.ID HAVING COUNT(*) BETWEEN "+examination_biopsy_obj.getMinCount()+" AND "+examination_biopsy_obj.getMaxCount();
				  		}
				  		else where_clause += " GROUP BY patient.ID HAVING COUNT(*) >= "+examination_biopsy_obj.getMinCount();
				  }
				  else if(!examination_biopsy_obj.getMaxCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.ID HAVING COUNT(*) <= "+examination_biopsy_obj.getMaxCount();
				  }
				  
				  query = "SELECT DISTINCT patient.ID FROM " + tables + " WHERE " + where_clause;
				  
				  if(!examination_biopsy_obj.getMaxNested().isEmpty()) {
					  String crit_max_nested = makeCriterionList(examination_biopsy_obj.getMaxNested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_max_nested);
					  ArrayList<Criterion> list_of_max_nested=null;
					  list_of_max_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> maxNestedQueries = createNestedQueries(list_of_max_nested, false, true);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND exam_biopsy.BIOPSY_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<maxNestedQueries.size(); k++) {
						  where_clause += " AND (dt_date.YEAR2 <= "+maxNestedQueries.get(k)+" OR (dt_date.YEAR2 IS NULL AND dt_date.YEAR <= "+maxNestedQueries.get(k)+"))";
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!examination_biopsy_obj.getMinNested().isEmpty()) {
					  String crit_min_nested = makeCriterionList(examination_biopsy_obj.getMinNested());
					  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_min_nested);
					  ArrayList<Criterion> list_of_min_nested=null;
					  list_of_min_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
					  ArrayList<String> minNestedQueries = createNestedQueries(list_of_min_nested, false, false);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND exam_biopsy.BIOPSY_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<minNestedQueries.size(); k++) {
						  where_clause += " AND dt_date.YEAR >= "+minNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }  
				  
				  if(!examination_biopsy_obj.getEndPeriodNested().isEmpty()) {
					  String crit_end_period_nested = makeCriterionList(examination_biopsy_obj.getEndPeriodNested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_end_period_nested);
					  ArrayList<Criterion> list_of_end_period_nested=null;
					  list_of_end_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> endPeriodNestedQueries = createPeriodNestedQueries(list_of_end_period_nested, false, false);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND exam_biopsy.BIOPSY_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<endPeriodNestedQueries.size(); k++) {
						  where_clause += " AND (dt_date.YEAR2 <= "+endPeriodNestedQueries.get(k)+" OR (dt_date.YEAR2 IS NULL AND dt_date.YEAR <= "+endPeriodNestedQueries.get(k)+"))";
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!examination_biopsy_obj.getStartPeriodNested().isEmpty()) {
					  String crit_start_period_nested = makeCriterionList(examination_biopsy_obj.getStartPeriodNested());
					  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_start_period_nested);
					  ArrayList<Criterion> list_of_start_period_nested=null;
					  list_of_start_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
					  ArrayList<String> startPeriodNestedQueries = createPeriodNestedQueries(list_of_start_period_nested, false, true);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND exam_biopsy.BIOPSY_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<startPeriodNestedQueries.size(); k++) {
						  where_clause += " AND dt_date.YEAR >= "+startPeriodNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!incl) query = "SELECT DISTINCT patient.ID FROM patient WHERE patient.ID NOT IN (" +query+ ")";
					//results_of_one_Criterion=DBServiceCRUD.getDataFromDB(query); 
					//System.out.println("We executed: "+crit_exam_lab_test_obj.criterion_name+"\nThe Query is: "+query); 
			  } break; //examination_medical_imaging_test
			  
			  case "examination_medical_imaging_test": { //Check if user provided the info of all the fields 
				  examination_medical_imaging_test  examination_medical_imaging_test_obj =  (examination_medical_imaging_test)current_Criterion;
				  
				  String tables = "exam_medical_imaging_test, patient";
				  String where_clause = "patient.ID = exam_medical_imaging_test.PATIENT_ID";
				  
				  if(!examination_medical_imaging_test_obj.getTest_id().isEmpty()) {
					  tables += ", voc_medical_imaging_test";
					  where_clause += " AND exam_medical_imaging_test.TEST_ID=voc_medical_imaging_test.ID AND (" + Make_OR_of_CODES("voc_medical_imaging_test.CODE", examination_medical_imaging_test_obj.getTest_id());
					  String codes[] = examination_medical_imaging_test_obj.getTest_id().split(",");
					  for(int k=0; k<codes.length; k++) {
						  String narrowTerms = getTermsWithNarrowMeaning(codes[k].trim());
						  String[] allNarrowTerms = narrowTerms.split(",");
						  for(int c=1; c<allNarrowTerms.length; c++) {
							  where_clause += " OR " + Make_OR_of_CODES("voc_medical_imaging_test.CODE", allNarrowTerms[c]);
						  }
					  }
					  where_clause += ") ";
				  }
				  
				  
				  
				  if(!examination_medical_imaging_test_obj.getAssessment().isEmpty()) {  // [OUTCOME_ASSESSMENT]
					  tables += ", voc_assessment";
					  where_clause += "AND exam_medical_imaging_test.ASSESSMENT_ID = voc_assessment.ID " + 
					  	//"AND voc_assessment.CODE ='"+crit_exam_lab_test_obj.OUTCOME_ASSESSMENT_ID_voc_assessment_CODE+"' ";
					  	"AND (" + Make_OR_of_CODES("voc_assessment.CODE", examination_medical_imaging_test_obj.getAssessment());
					  String mycodes[] = examination_medical_imaging_test_obj.getAssessment().split(",");
					  for(int k=0; k<mycodes.length; k++) {
						  String narrowTerms = getTermsWithNarrowMeaning(mycodes[k].trim());
						  String[] allNarrowTerms = narrowTerms.split(",");
						  for(int c=1; c<allNarrowTerms.length; c++) {
							  where_clause += " OR " + Make_OR_of_CODES("voc_assessment.CODE", allNarrowTerms[c]);
						  }
					  }
					  where_clause += ")";
				  }
				  
				  if(!(examination_medical_imaging_test_obj.getTest_period_of_time_exact_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_specific_date_query(mode, "exam_medical_imaging_test.TEST_DATE_ID","dt_date",examination_medical_imaging_test_obj.getTest_period_of_time_exact_year(), 
							  examination_medical_imaging_test_obj.getTest_period_of_time_exact_month(), examination_medical_imaging_test_obj.getTest_period_of_time_exact_day());					  		
				  } else if(!(examination_medical_imaging_test_obj.getTest_period_of_time_interval_start_year()).isEmpty() || !(examination_medical_imaging_test_obj.getTest_period_of_time_interval_end_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (mode,"exam_medical_imaging_test.TEST_DATE_ID", "dt_date",examination_medical_imaging_test_obj.getTest_period_of_time_interval_start_year(), 
							  examination_medical_imaging_test_obj.getTest_period_of_time_interval_start_month(), examination_medical_imaging_test_obj.getTest_period_of_time_interval_start_day(),
							  examination_medical_imaging_test_obj.getTest_period_of_time_interval_end_year(), examination_medical_imaging_test_obj.getTest_period_of_time_interval_end_month(),
							  examination_medical_imaging_test_obj.getTest_period_of_time_interval_end_day()); 			  
				  } else if(!(examination_medical_imaging_test_obj.getTest_period_of_time_until_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (mode,"exam_medical_imaging_test.TEST_DATE_ID","dt_date", "1800", "1", "1", examination_medical_imaging_test_obj.getTest_period_of_time_until_year(), examination_medical_imaging_test_obj.getTest_period_of_time_until_month(),
							  examination_medical_imaging_test_obj.getTest_period_of_time_until_day()); 
				  }
				  
				  if(!examination_medical_imaging_test_obj.getMinCount().isEmpty()) {
				  		if(!examination_medical_imaging_test_obj.getMaxCount().isEmpty()) {
				  			where_clause += " GROUP BY patient.ID HAVING COUNT(*) BETWEEN "+examination_medical_imaging_test_obj.getMinCount()+" AND "+examination_medical_imaging_test_obj.getMaxCount();
				  		}
				  		else where_clause += " GROUP BY patient.ID HAVING COUNT(*) >= "+examination_medical_imaging_test_obj.getMinCount();
				  }
				  else if(!examination_medical_imaging_test_obj.getMaxCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.ID HAVING COUNT(*) <= "+examination_medical_imaging_test_obj.getMaxCount();
				  }
				  
				  query = "SELECT DISTINCT patient.ID FROM " + tables + " WHERE " + where_clause;
				  
				  if(!examination_medical_imaging_test_obj.getMaxNested().isEmpty()) {
					  String crit_max_nested = makeCriterionList(examination_medical_imaging_test_obj.getMaxNested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_max_nested);
					  ArrayList<Criterion> list_of_max_nested=null;
					  list_of_max_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> maxNestedQueries = createNestedQueries(list_of_max_nested, false, true);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND exam_medical_imaging_test.TEST_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<maxNestedQueries.size(); k++) {
						  where_clause += " AND (dt_date.YEAR2 <= "+maxNestedQueries.get(k)+" OR (dt_date.YEAR2 IS NULL AND dt_date.YEAR <= "+maxNestedQueries.get(k)+"))";
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!examination_medical_imaging_test_obj.getMinNested().isEmpty()) {
					  String crit_min_nested = makeCriterionList(examination_medical_imaging_test_obj.getMinNested());
					  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_min_nested);
					  ArrayList<Criterion> list_of_min_nested=null;
					  list_of_min_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
					  ArrayList<String> minNestedQueries = createNestedQueries(list_of_min_nested, false, false);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND exam_medical_imaging_test.TEST_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<minNestedQueries.size(); k++) {
						  where_clause += " AND dt_date.YEAR >= "+minNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!examination_medical_imaging_test_obj.getEndPeriodNested().isEmpty()) {
					  String crit_end_period_nested = makeCriterionList(examination_medical_imaging_test_obj.getEndPeriodNested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_end_period_nested);
					  ArrayList<Criterion> list_of_end_period_nested=null;
					  list_of_end_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> endPeriodNestedQueries = createPeriodNestedQueries(list_of_end_period_nested, false, false);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND exam_medical_imaging_test.TEST_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<endPeriodNestedQueries.size(); k++) {
						  where_clause += " AND (dt_date.YEAR2 <= "+endPeriodNestedQueries.get(k)+" OR (dt_date.YEAR2 IS NULL AND dt_date.YEAR <= "+endPeriodNestedQueries.get(k)+"))";
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!examination_medical_imaging_test_obj.getStartPeriodNested().isEmpty()) {
					  String crit_start_period_nested = makeCriterionList(examination_medical_imaging_test_obj.getStartPeriodNested());
					  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_start_period_nested);
					  ArrayList<Criterion> list_of_start_period_nested=null;
					  list_of_start_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
					  ArrayList<String> startPeriodNestedQueries = createPeriodNestedQueries(list_of_start_period_nested, false, true);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND exam_medical_imaging_test.TEST_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<startPeriodNestedQueries.size(); k++) {
						  where_clause += " AND dt_date.YEAR >= "+startPeriodNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!incl) query = "SELECT DISTINCT patient.ID FROM patient WHERE patient.ID NOT IN (" +query+ ")";
					//results_of_one_Criterion=DBServiceCRUD.getDataFromDB(query); 
					//System.out.println("We executed: "+crit_exam_lab_test_obj.criterion_name+"\nThe Query is: "+query); 
			  } break;
			  case "examination_questionnaire_score": { //Check if user provided the info of all the fields 
				  examination_questionnaire_score  examination_questionnaire_score_obj =  (examination_questionnaire_score)current_Criterion;
				  
				  String tables = "patient, exam_questionnaire_score";
				  String where_clause = "patient.ID = exam_questionnaire_score.PATIENT_ID";
				  
				  if(!examination_questionnaire_score_obj.getScore().isEmpty()) {
					  tables += ", voc_questionnaire";
					  where_clause += " AND exam_questionnaire_score.SCORE_ID=voc_questionnaire.ID AND (" + Make_OR_of_CODES("voc_questionnaire.CODE", examination_questionnaire_score_obj.getScore());
					  String mycodes[] = examination_questionnaire_score_obj.getScore().split(",");
					  for(int k=0; k<mycodes.length; k++) {
						  String narrowTerms = getTermsWithNarrowMeaning(mycodes[k].trim());
						  String[] allNarrowTerms = narrowTerms.split(",");
						  for(int c=1; c<allNarrowTerms.length; c++) {
							  where_clause += " OR " + Make_OR_of_CODES("voc_questionnaire.CODE", allNarrowTerms[c]);
						  }
					  }
					  where_clause += ")";
				  }
				  
				  
				  /*if(!examination_questionnaire_score_obj.getValue().isEmpty()) {  //TODO check value
					  where_clause += " AND " + Make_OR_of_CODES("exam_questionnaire_score.VALUE", examination_questionnaire_score_obj.getValue());
				  }*/
				  
				  if(!examination_questionnaire_score_obj.getExactValue().isEmpty()){
					  where_clause += " AND exam_questionnaire_score.VALUE="+examination_questionnaire_score_obj.getExactValue(); //+" OR (exam_questionnaire_score.VALUE<="+examination_questionnaire_score_obj.getExactValue()+" AND exam_questionnaire_score.VALUE2>="+examination_questionnaire_score_obj.getExactValue()+")) ";
				  }
				  
				  if(!examination_questionnaire_score_obj.getRangeMinValue().isEmpty()){
					  	where_clause += " AND exam_questionnaire_score.VALUE>=" + examination_questionnaire_score_obj.getRangeMinValue(); //+" OR exam_questionnaire_score.VALUE2>="+examination_questionnaire_score_obj.getRangeMinValue()+")"; 
					  	if(!examination_questionnaire_score_obj.getRangeMaxValue().isEmpty()) {
					  		where_clause += " AND exam_questionnaire_score.VALUE<=" + examination_questionnaire_score_obj.getRangeMaxValue();
					  	}
				  }
				  else if(!examination_questionnaire_score_obj.getRangeMaxValue().isEmpty()) {
					  where_clause += " AND exam_questionnaire_score.VALUE<=" + examination_questionnaire_score_obj.getRangeMaxValue();
				  }
				  
				  if(!examination_questionnaire_score_obj.getAssessment().isEmpty()) {  // [OUTCOME_ASSESSMENT]
					  tables += ", voc_assessment";
					  where_clause += " AND exam_questionnaire_score.ASSESSMENT_ID = voc_assessment.ID " + 
					  	//"AND voc_assessment.CODE ='"+crit_exam_lab_test_obj.OUTCOME_ASSESSMENT_ID_voc_assessment_CODE+"' ";
					  	"AND (" + Make_OR_of_CODES("voc_assessment.CODE", examination_questionnaire_score_obj.getAssessment());
					  String codes[] = examination_questionnaire_score_obj.getAssessment().split(",");
					  for(int k=0; k<codes.length; k++) {
						  String narrowTerms = getTermsWithNarrowMeaning(codes[k].trim());
						  String[] allNarrowTerms = narrowTerms.split(",");
						  for(int c=1; c<allNarrowTerms.length; c++) {
							  where_clause += " OR " + Make_OR_of_CODES("voc_assessment.CODE", allNarrowTerms[c]);
						  }
					  }
					  where_clause += ")";
				  }
				  
				  //TODO NORMAL_RANGE  does it check if the value belongs in these two limits.		  
				  if(!(examination_questionnaire_score_obj.getNormal_range_value()).isEmpty()) {
					  tables += ", dt_int_range";//, voc_unit";
					  where_clause += " AND exam_questionnaire_score.NORMAL_RANGE_ID = dt_int_range.ID " +
					  	"AND dt_int_range.INT1 <= '"+examination_questionnaire_score_obj.getNormal_range_value()+"' " +
					  	"AND dt_int_range.INT2 >= '"+examination_questionnaire_score_obj.getNormal_range_value()+"' ";/* +
					    "AND dt_int_range.UNIT_ID = voc_unit.ID AND voc_unit.CODE='"+examination_questionnaire_score_obj.getUnit()+"' ";*/
				  };
				  
				  if(!examination_questionnaire_score_obj.getOther_term().isEmpty()) {  //TODO check value
					  where_clause += " AND " + Make_OR_of_CODES("exam_questionnaire_score.OTHER_TERM_ID", examination_questionnaire_score_obj.getOther_term());
				  }
				  
				  				  
				  if(!(examination_questionnaire_score_obj.getQuestionnaire_period_of_time_exact_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_specific_date_query(mode, "exam_questionnaire_score.QUESTIONNAIRE_DATE_ID","dt_date",examination_questionnaire_score_obj.getQuestionnaire_period_of_time_exact_year(), 
							  examination_questionnaire_score_obj.getQuestionnaire_period_of_time_exact_month(), examination_questionnaire_score_obj.getQuestionnaire_period_of_time_exact_day());					  		
				  } else if(!(examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_start_year()).isEmpty() || !(examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_end_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (mode,"exam_questionnaire_score.QUESTIONNAIRE_DATE_ID", "dt_date",examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_start_year(), 
							  examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_start_month(), examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_start_day(),
							  examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_end_year(), examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_end_month(),
							  examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_end_day()); 			  
				  } else if(!(examination_questionnaire_score_obj.getQuestionnaire_period_of_time_until_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (mode,"exam_questionnaire_score.QUESTIONNAIRE_DATE_ID","dt_date", "1800", "1", "1", examination_questionnaire_score_obj.getQuestionnaire_period_of_time_until_year(), examination_questionnaire_score_obj.getQuestionnaire_period_of_time_until_month(),
							  examination_questionnaire_score_obj.getQuestionnaire_period_of_time_until_day()); 
				  }
				  
				  if(!examination_questionnaire_score_obj.getMinCount().isEmpty()) {
				  		if(!examination_questionnaire_score_obj.getMaxCount().isEmpty()) {
				  			where_clause += " GROUP BY patient.ID HAVING COUNT(*) BETWEEN "+examination_questionnaire_score_obj.getMinCount()+" AND "+examination_questionnaire_score_obj.getMaxCount();
				  		}
				  		else where_clause += " GROUP BY patient.ID HAVING COUNT(*) >= "+examination_questionnaire_score_obj.getMinCount();
				  }
				  else if(!examination_questionnaire_score_obj.getMaxCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.ID HAVING COUNT(*) <= "+examination_questionnaire_score_obj.getMaxCount();
				  }
				  
				  query = "SELECT DISTINCT patient.ID FROM " + tables + " WHERE " + where_clause;
				  
				  if(!examination_questionnaire_score_obj.getMaxNested().isEmpty()) {
					  String crit_max_nested = makeCriterionList(examination_questionnaire_score_obj.getMaxNested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_max_nested);
					  ArrayList<Criterion> list_of_max_nested=null;
					  list_of_max_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> maxNestedQueries = createNestedQueries(list_of_max_nested, false, true);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND exam_questionnaire_score.QUESTIONNAIRE_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<maxNestedQueries.size(); k++) {
						  where_clause += " AND (dt_date.YEAR2 <= "+maxNestedQueries.get(k)+" OR (dt_date.YEAR2 IS NULL AND dt_date.YEAR <= "+maxNestedQueries.get(k)+"))";
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!examination_questionnaire_score_obj.getMinNested().isEmpty()) {
					  String crit_min_nested = makeCriterionList(examination_questionnaire_score_obj.getMinNested());
					  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_min_nested);
					  ArrayList<Criterion> list_of_min_nested=null;
					  list_of_min_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
					  ArrayList<String> minNestedQueries = createNestedQueries(list_of_min_nested, false, false);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND exam_questionnaire_score.QUESTIONNAIRE_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<minNestedQueries.size(); k++) {
						  where_clause += " AND dt_date.YEAR >= "+minNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }  
				  
				  if(!examination_questionnaire_score_obj.getEndPeriodNested().isEmpty()) {
					  String crit_end_period_nested = makeCriterionList(examination_questionnaire_score_obj.getEndPeriodNested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_end_period_nested);
					  ArrayList<Criterion> list_of_end_period_nested=null;
					  list_of_end_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> endPeriodNestedQueries = createPeriodNestedQueries(list_of_end_period_nested, false, false);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND exam_questionnaire_score.QUESTIONNAIRE_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<endPeriodNestedQueries.size(); k++) {
						  where_clause += " AND (dt_date.YEAR2 <= "+endPeriodNestedQueries.get(k)+" OR (dt_date.YEAR2 IS NULL AND dt_date.YEAR <= "+endPeriodNestedQueries.get(k)+"))";
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!examination_questionnaire_score_obj.getStartPeriodNested().isEmpty()) {
					  String crit_start_period_nested = makeCriterionList(examination_questionnaire_score_obj.getStartPeriodNested());
					  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_start_period_nested);
					  ArrayList<Criterion> list_of_start_period_nested=null;
					  list_of_start_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
					  ArrayList<String> startPeriodNestedQueries = createPeriodNestedQueries(list_of_start_period_nested, false, true);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND exam_questionnaire_score.QUESTIONNAIRE_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<startPeriodNestedQueries.size(); k++) {
						  where_clause += " AND dt_date.YEAR >= "+startPeriodNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!incl) query = "SELECT DISTINCT patient.ID FROM patient WHERE patient.ID NOT IN (" +query+ ")";
				  
			  } break;  //examination_essdai_domain
			  
			  case "examination_essdai_domain": { //Check if user provided the info of all the fields 
				  examination_essdai_domain  examination_essdai_domain_obj =  (examination_essdai_domain)current_Criterion;
				  
				  String tables = "patient, exam_essdai_domain";
				  String where_clause = "patient.ID = exam_essdai_domain.PATIENT_ID";
				  
				  if(!examination_essdai_domain_obj.getDomain().isEmpty()) {
					  tables += ", voc_essdai_domain";
					  where_clause += " AND exam_essdai_domain.DOMAIN_ID = voc_essdai_domain.ID AND " + Make_OR_of_CODES("voc_essdai_domain.CODE", examination_essdai_domain_obj.getDomain());
				  }
			  
				  if(!(examination_essdai_domain_obj.getActivity_level()).isEmpty()) {
					  tables += ", voc_activity_level";
					  where_clause += " AND exam_essdai_domain.ACTIVITY_LEVEL_ID = voc_activity_level.ID AND " + Make_OR_of_CODES("voc_activity_level.CODE", examination_essdai_domain_obj.getActivity_level());
				  }
				  
				  if(!(examination_essdai_domain_obj.getQuestionnaire_period_of_time_exact_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_specific_date_query(mode, "exam_essdai_domain.QUESTIONNAIRE_DATE_ID","dt_date",examination_essdai_domain_obj.getQuestionnaire_period_of_time_exact_year(), 
							  examination_essdai_domain_obj.getQuestionnaire_period_of_time_exact_month(), examination_essdai_domain_obj.getQuestionnaire_period_of_time_exact_day());					  		
				  } else if(!(examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_start_year()).isEmpty() || !(examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_end_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (mode,"exam_essdai_domain.QUESTIONNAIRE_DATE_ID", "dt_date",examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_start_year(), 
							  examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_start_month(), examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_start_day(),
							  examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_end_year(), examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_end_month(),
							  examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_end_day()); 			  
				  } else if(!(examination_essdai_domain_obj.getQuestionnaire_period_of_time_until_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (mode,"exam_essdai_domain.QUESTIONNAIRE_DATE_ID","dt_date", "1800", "1", "1", examination_essdai_domain_obj.getQuestionnaire_period_of_time_until_year(), examination_essdai_domain_obj.getQuestionnaire_period_of_time_until_month(),
							  examination_essdai_domain_obj.getQuestionnaire_period_of_time_until_day()); 
				  }
				  
				  if(!examination_essdai_domain_obj.getMinCount().isEmpty()) {
				  		if(!examination_essdai_domain_obj.getMaxCount().isEmpty()) {
				  			where_clause += " GROUP BY patient.ID HAVING COUNT(*) BETWEEN "+examination_essdai_domain_obj.getMinCount()+" AND "+examination_essdai_domain_obj.getMaxCount();
				  		}
				  		else where_clause += " GROUP BY patient.ID HAVING COUNT(*) >= "+examination_essdai_domain_obj.getMinCount();
				  }
				  else if(!examination_essdai_domain_obj.getMaxCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.ID HAVING COUNT(*) <= "+examination_essdai_domain_obj.getMaxCount();
				  }
				  
				  query = "SELECT DISTINCT patient.ID FROM " + tables + " WHERE " + where_clause;
				  
				  if(!examination_essdai_domain_obj.getMaxNested().isEmpty()) {
					  String crit_max_nested = makeCriterionList(examination_essdai_domain_obj.getMaxNested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_max_nested);
					  ArrayList<Criterion> list_of_max_nested=null;
					  list_of_max_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> maxNestedQueries = createNestedQueries(list_of_max_nested, false, true);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND exam_essdai_domain.QUESTIONNAIRE_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<maxNestedQueries.size(); k++) {
						  where_clause += " AND (dt_date.YEAR2 <= "+maxNestedQueries.get(k)+" OR (dt_date.YEAR2 IS NULL AND dt_date.YEAR <= "+maxNestedQueries.get(k)+"))";
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  if(!examination_essdai_domain_obj.getMinNested().isEmpty()) {
					  String crit_min_nested = makeCriterionList(examination_essdai_domain_obj.getMinNested());
					  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_min_nested);
					  ArrayList<Criterion> list_of_min_nested=null;
					  list_of_min_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
					  ArrayList<String> minNestedQueries = createNestedQueries(list_of_min_nested, false, false);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND exam_essdai_domain.QUESTIONNAIRE_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<minNestedQueries.size(); k++) {
						  where_clause += " AND dt_date.YEAR >= "+minNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }   
				  
				  if(!examination_essdai_domain_obj.getEndPeriodNested().isEmpty()) {
					  String crit_end_period_nested = makeCriterionList(examination_essdai_domain_obj.getEndPeriodNested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_end_period_nested);
					  ArrayList<Criterion> list_of_end_period_nested=null;
					  list_of_end_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> endPeriodNestedQueries = createPeriodNestedQueries(list_of_end_period_nested, false, false);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND exam_essdai_domain.QUESTIONNAIRE_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<endPeriodNestedQueries.size(); k++) {
						  where_clause += " AND (dt_date.YEAR2 <= "+endPeriodNestedQueries.get(k)+" OR (dt_date.YEAR2 IS NULL AND dt_date.YEAR <= "+endPeriodNestedQueries.get(k)+"))";
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  if(!examination_essdai_domain_obj.getStartPeriodNested().isEmpty()) {
					  String crit_start_period_nested = makeCriterionList(examination_essdai_domain_obj.getStartPeriodNested());
					  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_start_period_nested);
					  ArrayList<Criterion> list_of_start_period_nested=null;
					  list_of_start_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
					  ArrayList<String> startPeriodNestedQueries = createPeriodNestedQueries(list_of_start_period_nested, false, true);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND exam_essdai_domain.QUESTIONNAIRE_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<startPeriodNestedQueries.size(); k++) {
						  where_clause += " AND dt_date.YEAR >= "+startPeriodNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }		
				  
				  if(!incl) query = "SELECT DISTINCT patient.ID FROM patient WHERE patient.ID NOT IN (" +query+ ")";
			  } break; //examination_caci_condition
			  
			  case "examination_caci_condition": { //Check if user provided the info of all the fields 
				  examination_caci_condition  examination_caci_condition_obj =  (examination_caci_condition)current_Criterion;
				  
				  String tables = "exam_caci_condition, patient";
				  String where_clause = "patient.ID = exam_caci_condition.PATIENT_ID";
				  
				  if(!examination_caci_condition_obj.getCaci().isEmpty()) {
					  tables += ", voc_caci_condition";
					  where_clause += " AND exam_caci_condition.CACI_ID = voc_caci_condition.ID AND " + Make_OR_of_CODES("voc_caci_condition.CODE", examination_caci_condition_obj.getCaci());
				  }
				  
				  if(!examination_caci_condition_obj.getValue().isEmpty()) {  //TODO check value
					  tables += ", voc_confirmation";
					  where_clause += " AND exam_caci_condition.VALUE_ID = voc_confirmation.ID AND " + Make_OR_of_CODES("voc_confirmation.CODE", examination_caci_condition_obj.getValue());
				  }
				  
				  if(!(examination_caci_condition_obj.getQuestionnaire_period_of_time_exact_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_specific_date_query(mode, "exam_caci_condition.QUESTIONNAIRE_DATE_ID","dt_date",examination_caci_condition_obj.getQuestionnaire_period_of_time_exact_year(), 
							  examination_caci_condition_obj.getQuestionnaire_period_of_time_exact_month(), examination_caci_condition_obj.getQuestionnaire_period_of_time_exact_day());					  		
				  } else if(!(examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_start_year()).isEmpty() || !(examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_end_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (mode,"exam_caci_condition.QUESTIONNAIRE_DATE_ID", "dt_date",examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_start_year(), 
							  examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_start_month(), examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_start_day(),
							  examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_end_year(), examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_end_month(),
							  examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_end_day()); 			  
				  } else if(!(examination_caci_condition_obj.getQuestionnaire_period_of_time_until_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (mode,"exam_caci_condition.QUESTIONNAIRE_DATE_ID","dt_date", "1800", "1", "1", examination_caci_condition_obj.getQuestionnaire_period_of_time_until_year(), examination_caci_condition_obj.getQuestionnaire_period_of_time_until_month(),
							  examination_caci_condition_obj.getQuestionnaire_period_of_time_until_day()); 
				  }
				  
				  if(!examination_caci_condition_obj.getMinCount().isEmpty()) {
				  		if(!examination_caci_condition_obj.getMaxCount().isEmpty()) {
				  			where_clause += " GROUP BY patient.ID HAVING COUNT(*) BETWEEN "+examination_caci_condition_obj.getMinCount()+" AND "+examination_caci_condition_obj.getMaxCount();
				  		}
				  		else where_clause += " GROUP BY patient.ID HAVING COUNT(*) >= "+examination_caci_condition_obj.getMinCount();
				  }
				  else if(!examination_caci_condition_obj.getMaxCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.ID HAVING COUNT(*) <= "+examination_caci_condition_obj.getMaxCount();
				  }
				  
				  query = "SELECT DISTINCT patient.ID FROM " + tables + " WHERE " + where_clause;
				  
				  if(!examination_caci_condition_obj.getMaxNested().isEmpty()) {
					  String crit_max_nested = makeCriterionList(examination_caci_condition_obj.getMaxNested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_max_nested);
					  ArrayList<Criterion> list_of_max_nested=null;
					  list_of_max_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> maxNestedQueries = createNestedQueries(list_of_max_nested, false, true);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND exam_caci_condition.QUESTIONNAIRE_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<maxNestedQueries.size(); k++) {
						  where_clause += " AND (dt_date.YEAR2 <= "+maxNestedQueries.get(k)+" OR (dt_date.YEAR2 IS NULL AND dt_date.YEAR <= "+maxNestedQueries.get(k)+"))";
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  if(!examination_caci_condition_obj.getMinNested().isEmpty()) {
					  String crit_min_nested = makeCriterionList(examination_caci_condition_obj.getMinNested());
					  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_min_nested);
					  ArrayList<Criterion> list_of_min_nested=null;
					  list_of_min_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
					  ArrayList<String> minNestedQueries = createNestedQueries(list_of_min_nested, false, false);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND exam_caci_condition.QUESTIONNAIRE_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<minNestedQueries.size(); k++) {
						  where_clause += " AND dt_date.YEAR >= "+minNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }   
				  
				  if(!examination_caci_condition_obj.getEndPeriodNested().isEmpty()) {
					  String crit_end_period_nested = makeCriterionList(examination_caci_condition_obj.getEndPeriodNested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_end_period_nested);
					  ArrayList<Criterion> list_of_end_period_nested=null;
					  list_of_end_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> endPeriodNestedQueries = createPeriodNestedQueries(list_of_end_period_nested, false, false);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND exam_caci_condition.QUESTIONNAIRE_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<endPeriodNestedQueries.size(); k++) {
						  where_clause += " AND (dt_date.YEAR2 <= "+endPeriodNestedQueries.get(k)+" OR (dt_date.YEAR2 IS NULL AND dt_date.YEAR <= "+endPeriodNestedQueries.get(k)+"))";
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  if(!examination_caci_condition_obj.getStartPeriodNested().isEmpty()) {
					  String crit_start_period_nested = makeCriterionList(examination_caci_condition_obj.getStartPeriodNested());
					  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_start_period_nested);
					  ArrayList<Criterion> list_of_start_period_nested=null;
					  list_of_start_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
					  ArrayList<String> startPeriodNestedQueries = createPeriodNestedQueries(list_of_start_period_nested, false, true);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND exam_caci_condition.QUESTIONNAIRE_DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<startPeriodNestedQueries.size(); k++) {
						  where_clause += " AND dt_date.YEAR >= "+startPeriodNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }	
				  
				  if(!incl) query = "SELECT DISTINCT patient.ID FROM patient WHERE patient.ID NOT IN (" +query+ ")";
			  } break; //other_healthcare_visit
			  
			  case "other_healthcare_visit": { //Check if user provided the info of all the fields 
				  other_healthcare_visit  other_healthcare_visit_obj =  (other_healthcare_visit)current_Criterion;
				  
				  String tables = "patient, other_healthcare_visit";
				  String where_clause = "patient.ID = other_healthcare_visit.PATIENT_ID";
				  
				  if(!other_healthcare_visit_obj.getSpecialist().isEmpty()) {
					  tables += ", voc_specialist";
					  where_clause += " AND other_healthcare_visit.SPECIALIST_ID=voc_specialist.ID AND " + Make_OR_of_CODES("voc_specialist.CODE", other_healthcare_visit_obj.getSpecialist());
				  }
				  
				  if(!(other_healthcare_visit_obj.getPeriod_of_time_exact_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_specific_date_query(mode, "other_healthcare_visit.DATE_ID","dt_date",other_healthcare_visit_obj.getPeriod_of_time_exact_year(), 
							  other_healthcare_visit_obj.getPeriod_of_time_exact_month(), other_healthcare_visit_obj.getPeriod_of_time_exact_day());					  		
				  } else if(!(other_healthcare_visit_obj.getPeriod_of_time_interval_start_year()).isEmpty() || !(other_healthcare_visit_obj.getPeriod_of_time_interval_end_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (mode,"other_healthcare_visit.DATE_ID", "dt_date",other_healthcare_visit_obj.getPeriod_of_time_interval_start_year(), 
							  other_healthcare_visit_obj.getPeriod_of_time_interval_start_month(), other_healthcare_visit_obj.getPeriod_of_time_interval_start_day(),
							  other_healthcare_visit_obj.getPeriod_of_time_interval_end_year(), other_healthcare_visit_obj.getPeriod_of_time_interval_end_month(),
							  other_healthcare_visit_obj.getPeriod_of_time_interval_end_day()); 			  
				  } else if(!(other_healthcare_visit_obj.getPeriod_of_time_until_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (mode,"other_healthcare_visit.DATE_ID","dt_date", "1800", "1", "1", other_healthcare_visit_obj.getPeriod_of_time_until_year(), other_healthcare_visit_obj.getPeriod_of_time_until_month(),
							  other_healthcare_visit_obj.getPeriod_of_time_until_day()); 
				  }
				  
				  if(!other_healthcare_visit_obj.getMinCount().isEmpty()) {
				  		if(!other_healthcare_visit_obj.getMaxCount().isEmpty()) {
				  			where_clause += " GROUP BY patient.ID HAVING COUNT(*) BETWEEN "+other_healthcare_visit_obj.getMinCount()+" AND "+other_healthcare_visit_obj.getMaxCount();
				  		}
				  		else where_clause += " GROUP BY patient.ID HAVING COUNT(*) >= "+other_healthcare_visit_obj.getMinCount();
				  }
				  else if(!other_healthcare_visit_obj.getMaxCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.ID HAVING COUNT(*) <= "+other_healthcare_visit_obj.getMaxCount();
				  }
				  
				  query = "SELECT DISTINCT patient.ID FROM " + tables + " WHERE " + where_clause;
				  
				  if(!other_healthcare_visit_obj.getMaxNested().isEmpty()) {
					  String crit_max_nested = makeCriterionList(other_healthcare_visit_obj.getMaxNested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_max_nested);
					  ArrayList<Criterion> list_of_max_nested=null;
					  list_of_max_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> maxNestedQueries = createNestedQueries(list_of_max_nested, false, true);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND other_healthcare_visit.DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<maxNestedQueries.size(); k++) {
						  where_clause += " AND (dt_date.YEAR2 <= "+maxNestedQueries.get(k)+" OR (dt_date.YEAR2 IS NULL AND dt_date.YEAR <= "+maxNestedQueries.get(k)+"))";
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  if(!other_healthcare_visit_obj.getMinNested().isEmpty()) {
					  String crit_min_nested = makeCriterionList(other_healthcare_visit_obj.getMinNested());
					  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_min_nested);
					  ArrayList<Criterion> list_of_min_nested=null;
					  list_of_min_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
					  ArrayList<String> minNestedQueries = createNestedQueries(list_of_min_nested, false, false);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND other_healthcare_visit.DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<minNestedQueries.size(); k++) {
						  where_clause += " AND dt_date.YEAR >= "+minNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }   
				  
				  if(!other_healthcare_visit_obj.getEndPeriodNested().isEmpty()) {
					  String crit_end_period_nested = makeCriterionList(other_healthcare_visit_obj.getEndPeriodNested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_end_period_nested);
					  ArrayList<Criterion> list_of_end_period_nested=null;
					  list_of_end_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> endPeriodNestedQueries = createPeriodNestedQueries(list_of_end_period_nested, false, false);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND other_healthcare_visit.DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<endPeriodNestedQueries.size(); k++) {
						  where_clause += " AND (dt_date.YEAR2 <= "+endPeriodNestedQueries.get(k)+" OR (dt_date.YEAR2 IS NULL AND dt_date.YEAR <= "+endPeriodNestedQueries.get(k)+"))";
					  }
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  if(!other_healthcare_visit_obj.getStartPeriodNested().isEmpty()) {
					  String crit_start_period_nested = makeCriterionList(other_healthcare_visit_obj.getStartPeriodNested());
					  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_start_period_nested);
					  ArrayList<Criterion> list_of_start_period_nested=null;
					  list_of_start_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
					  ArrayList<String> startPeriodNestedQueries = createPeriodNestedQueries(list_of_start_period_nested, false, true);
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date";
						  where_clause += " AND other_healthcare_visit.DATE_ID=dt_date.ID";
					  }
					  for(int k=0; k<startPeriodNestedQueries.size(); k++) {
						  where_clause += " AND dt_date.YEAR >= "+startPeriodNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
				  if(!incl) query = "SELECT DISTINCT patient.ID FROM patient WHERE patient.ID NOT IN (" +query+ ")";
				  
			  } break;			
			  
			  case "other_family_history": { //Check if user provided the info of all the fields 
				  other_family_history  other_family_history_obj =  (other_family_history)current_Criterion;
				  
				  String tables = "other_family_history, patient";
				  String where_clause = "patient.ID = other_family_history.PATIENT_ID";
				  
				  if(!other_family_history_obj.getMedical_condition().isEmpty()) {
					  tables += ", voc_medical_condition";
					  where_clause += " AND other_family_history.MEDICAL_CONDITION_ID=voc_medical_condition.ID AND (" + Make_OR_of_CODES("voc_medical_condition.CODE", other_family_history_obj.getMedical_condition());
					  String codes[] = other_family_history_obj.getMedical_condition().split(",");
					  for(int k=0; k<codes.length; k++) {
						  String narrowTerms = getTermsWithNarrowMeaning(codes[k].trim());
						  String[] allNarrowTerms = narrowTerms.split(",");
						  for(int c=1; c<allNarrowTerms.length; c++) {
							  where_clause += " OR " + Make_OR_of_CODES("voc_medical_condition.CODE", allNarrowTerms[c]);
						  }
					  }
					  where_clause += ")";
				  }
				  
				  
				  
				  if(!(other_family_history_obj.getRelative_degree()).isEmpty()) {
					  tables += ", voc_relative_degree";
					  where_clause += "AND other_family_history.RELATIVE_DEGREE_ID = voc_relative_degree.ID " +
					  	"AND " +Make_OR_of_CODES("voc_relative_degree.CODE", other_family_history_obj.getRelative_degree()); 
					  
				  }
				  
				  if(!other_family_history_obj.getStatement().isEmpty()) {
					  tables += ", voc_confirmation";
					  where_clause += "AND other_family_history.STMT_ID=voc_confirmation.ID " +
				  				 "AND voc_confirmation.CODE='"+other_family_history_obj.getStatement() + "'";
				  }
				  where_clause += " AND other_family_history.STMT_ID=1";
				  
				  if(!other_family_history_obj.getMinCount().isEmpty()) {
				  		if(!other_family_history_obj.getMaxCount().isEmpty()) {
				  			where_clause += " GROUP BY patient.ID HAVING COUNT(*) BETWEEN "+other_family_history_obj.getMinCount()+" AND "+other_family_history_obj.getMaxCount();
				  		}
				  		else where_clause += " GROUP BY patient.ID HAVING COUNT(*) >= "+other_family_history_obj.getMinCount();
				  }
				  else if(!other_family_history_obj.getMaxCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.ID HAVING COUNT(*) <= "+other_family_history_obj.getMaxCount();
				  }
				  
				  query = "SELECT DISTINCT patient.ID FROM " + tables + " WHERE " + where_clause;
				  if(!incl) query = "SELECT DISTINCT patient.ID FROM patient WHERE patient.ID NOT IN (" +query+ ")";
				  
			  } break;
			  
			  case "other_clinical_trials": { //Check if user provided the info of all the fields 
				  other_clinical_trials  other_clinical_trials_obj =  (other_clinical_trials)current_Criterion;
				  
				  String tables = "patient, other_clinical_trials";
				  String where_clause = "patient.ID = other_clinical_trials.PATIENT_ID";
						  
			if(!(other_clinical_trials_obj.getPeriod_of_time_exact_year()).isEmpty()) {
				tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
				where_clause += Make_begin_end_period_query(mode, "other_clinical_trials.PERIOD_ID","dt_date1","dt_date2",other_clinical_trials_obj.getPeriod_of_time_exact_year(), 
						  other_clinical_trials_obj.getPeriod_of_time_exact_month(), other_clinical_trials_obj.getPeriod_of_time_exact_day(),other_clinical_trials_obj.getPeriod_of_time_exact_year(), 
						  other_clinical_trials_obj.getPeriod_of_time_exact_month(), other_clinical_trials_obj.getPeriod_of_time_exact_day());					  		
			} else if(!(other_clinical_trials_obj.getPeriod_of_time_interval_start_year()).isEmpty() || !(other_clinical_trials_obj.getPeriod_of_time_interval_end_year()).isEmpty()) {
				tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
				where_clause += Make_begin_end_period_query (mode,"other_clinical_trials.PERIOD_ID", "dt_date1","dt_date2",other_clinical_trials_obj.getPeriod_of_time_interval_start_year(), 
						  other_clinical_trials_obj.getPeriod_of_time_interval_start_month(), other_clinical_trials_obj.getPeriod_of_time_interval_start_day(),
						  other_clinical_trials_obj.getPeriod_of_time_interval_end_year(), other_clinical_trials_obj.getPeriod_of_time_interval_end_month(),
						  other_clinical_trials_obj.getPeriod_of_time_interval_end_day()); 			  
			} else if(!(other_clinical_trials_obj.getPeriod_of_time_until_year()).isEmpty()) {
				tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
				where_clause += Make_begin_end_period_query ( mode,"other_clinical_trials.PERIOD_ID","dt_date1","dt_date2", "1800", "1", "1", other_clinical_trials_obj.getPeriod_of_time_until_year(), other_clinical_trials_obj.getPeriod_of_time_until_month(),
						  other_clinical_trials_obj.getPeriod_of_time_until_day()); 
			}
			
			if(!other_clinical_trials_obj.getStatement().isEmpty()) {
				tables += ", voc_confirmation";
				where_clause += " AND other_clinical_trials.STMT_ID=voc_confirmation.ID " +
		  				 "AND voc_confirmation.CODE='"+other_clinical_trials_obj.getStatement() + "'";
			}
			where_clause += " AND other_clinical_trials.STMT_ID=1";
			
			 if(!other_clinical_trials_obj.getMinCount().isEmpty()) {
			  		if(!other_clinical_trials_obj.getMaxCount().isEmpty()) {
			  			where_clause += " GROUP BY patient.ID HAVING COUNT(*) BETWEEN "+other_clinical_trials_obj.getMinCount()+" AND "+other_clinical_trials_obj.getMaxCount();
			  		}
			  		else where_clause += " GROUP BY patient.ID HAVING COUNT(*) >= "+other_clinical_trials_obj.getMinCount();
			  }
			  else if(!other_clinical_trials_obj.getMaxCount().isEmpty()) {
			  		where_clause += " GROUP BY patient.ID HAVING COUNT(*) <= "+other_clinical_trials_obj.getMaxCount();
			  }
			
			 query = "SELECT DISTINCT patient.ID FROM " + tables + " WHERE " + where_clause;
			  
			  if(!other_clinical_trials_obj.getEndBeforeStartNested().isEmpty()) {
				  String crit_max_nested = makeCriterionList(other_clinical_trials_obj.getEndBeforeStartNested());
				  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_max_nested);
				  ArrayList<Criterion> list_of_max_nested=null;
				  list_of_max_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
				  ArrayList<String> maxNestedQueries = createNestedQueries(list_of_max_nested, false, true);
				  if(!tables.contains("dt_period_of_time")) {
					  tables += ", dt_period_of_time";
					  where_clause += " AND other_clinical_trials.PERIOD_ID=dt_period_of_time.ID";
				  }
				  if(!tables.contains("dt_date2")) {
					  tables += ", dt_date AS dt_date2";
					  where_clause += " AND dt_period_of_time.END_DATE_ID=dt_date2.ID";
				  }
				  for(int k=0; k<maxNestedQueries.size(); k++) {
					  where_clause += " AND dt_date2.YEAR <= "+maxNestedQueries.get(k);
				  }  
				  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
			  }
			  if(!other_clinical_trials_obj.getStartBeforeStartNested().isEmpty()) {
				  String crit_max_nested = makeCriterionList(other_clinical_trials_obj.getStartBeforeStartNested());
				  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_max_nested);
				  ArrayList<Criterion> list_of_max_nested=null;
				  list_of_max_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
				  ArrayList<String> maxNestedQueries = createNestedQueries(list_of_max_nested, false, true);
				  if(!tables.contains("dt_period_of_time")) {
					  tables += ", dt_period_of_time";
					  where_clause += " AND other_clinical_trials.PERIOD_ID=dt_period_of_time.ID";
				  }
				  if(!tables.contains("dt_date1")) {
					  tables += ", dt_date AS dt_date1";
					  where_clause += " AND dt_period_of_time.START_DATE_ID=dt_date1.ID";
				  }
				  for(int k=0; k<maxNestedQueries.size(); k++) {
					  where_clause += " AND dt_date1.YEAR <= "+maxNestedQueries.get(k);
				  }  
				  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
			  }
			  
			  if(!other_clinical_trials_obj.getStartAfterEndNested().isEmpty()) {
				  String crit_min_nested = makeCriterionList(other_clinical_trials_obj.getStartAfterEndNested());
				  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_min_nested);
				  ArrayList<Criterion> list_of_min_nested=null;
				  list_of_min_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
				  ArrayList<String> minNestedQueries = createNestedQueries(list_of_min_nested, false, false);
				  if(!tables.contains("dt_period_of_time")) {
					  tables += ", dt_period_of_time";
					  where_clause += " AND other_clinical_trials.PERIOD_ID=dt_period_of_time.ID";
				  }
				  if(!tables.contains("dt_date1")) {
					  tables += ", dt_date AS dt_date1";
					  where_clause += " AND dt_period_of_time.START_DATE_ID=dt_date1.ID";
				  }
				  for(int k=0; k<minNestedQueries.size(); k++) {
					  where_clause += " AND dt_date1.YEAR >= "+minNestedQueries.get(k);
				  }  
				  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
			  } 
			  
			  if(!other_clinical_trials_obj.getEndAfterEndNested().isEmpty()) {
				  String crit_min_nested = makeCriterionList(other_clinical_trials_obj.getEndAfterEndNested());
				  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_min_nested);
				  ArrayList<Criterion> list_of_min_nested=null;
				  list_of_min_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
				  ArrayList<String> minNestedQueries = createNestedQueries(list_of_min_nested, false, false);
				  if(!tables.contains("dt_period_of_time")) {
					  tables += ", dt_period_of_time";
					  where_clause += " AND other_clinical_trials.PERIOD_ID=dt_period_of_time.ID";
				  }
				  if(!tables.contains("dt_date2")) {
					  tables += ", dt_date AS dt_date2";
					  where_clause += " AND dt_period_of_time.END_DATE_ID=dt_date2.ID";
				  }
				  for(int k=0; k<minNestedQueries.size(); k++) {
					  where_clause += " AND dt_date2.YEAR >= "+minNestedQueries.get(k);
				  }  
				  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
			  } 
			  
			  if(!other_clinical_trials_obj.getEndAfterStartNested().isEmpty()) {
				  String crit_start_period_nested = makeCriterionList(other_clinical_trials_obj.getEndAfterStartNested());
				  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_start_period_nested);
				  ArrayList<Criterion> list_of_start_period_nested=null;
				  list_of_start_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
				  ArrayList<String> startPeriodNestedQueries = createPeriodNestedQueries(list_of_start_period_nested, false, true);
				  if(!tables.contains("dt_period_of_time")) {
					  tables += ", dt_period_of_time";
					  where_clause += " AND other_clinical_trials.PERIOD_ID=dt_period_of_time.ID";
				  }
				  if(!tables.contains("dt_date2")) {
					  tables += ", dt_date AS dt_date2";
					  where_clause += " AND dt_period_of_time.END_DATE_ID=dt_date2.ID";
				  }
				  for(int k=0; k<startPeriodNestedQueries.size(); k++) {
					  where_clause += " AND dt_date2.YEAR >= "+startPeriodNestedQueries.get(k);
				  }  
				  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
			  }
			  
			  if(!other_clinical_trials_obj.getEndBeforeEndNested().isEmpty()) {
				  String crit_end_period_nested = makeCriterionList(other_clinical_trials_obj.getEndBeforeEndNested());
				  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_end_period_nested);
				  ArrayList<Criterion> list_of_end_period_nested=null;
				  list_of_end_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
				  ArrayList<String> endPeriodNestedQueries = createPeriodNestedQueries(list_of_end_period_nested, false, false);
				  if(!tables.contains("dt_period_of_time")) {
					  tables += ", dt_period_of_time";
					  where_clause += " AND other_clinical_trials.PERIOD_ID=dt_period_of_time.ID";
				  }
				  if(!tables.contains("dt_date2")) {
					  tables += ", dt_date AS dt_date2";
					  where_clause += " AND dt_period_of_time.END_DATE_ID=dt_date2.ID";
				  }
				  for(int k=0; k<endPeriodNestedQueries.size(); k++) {
					  where_clause += " AND dt_date2.YEAR <= "+endPeriodNestedQueries.get(k);
				  }  
				  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
			  }
			  
			  if(!other_clinical_trials_obj.getStartBeforeEndNested().isEmpty()) {
				  String crit_end_period_nested = makeCriterionList(other_clinical_trials_obj.getStartBeforeEndNested());
				  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_end_period_nested);
				  ArrayList<Criterion> list_of_end_period_nested=null;
				  list_of_end_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
				  ArrayList<String> endPeriodNestedQueries = createPeriodNestedQueries(list_of_end_period_nested, false, false);
				  if(!tables.contains("dt_period_of_time")) {
					  tables += ", dt_period_of_time";
					  where_clause += " AND other_clinical_trials.PERIOD_ID=dt_period_of_time.ID";
				  }
				  if(!tables.contains("dt_date1")) {
					  tables += ", dt_date AS dt_date1";
					  where_clause += " AND dt_period_of_time.START_DATE_ID=dt_date1.ID";
				  }
				  for(int k=0; k<endPeriodNestedQueries.size(); k++) {
					  where_clause += " AND dt_date1.YEAR <= "+endPeriodNestedQueries.get(k);
				  }  
				  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
			  }
			  
			  if(!other_clinical_trials_obj.getStartAfterStartNested().isEmpty()) {
				  String crit_start_period_nested = makeCriterionList(other_clinical_trials_obj.getStartAfterStartNested());
				  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_start_period_nested);
				  ArrayList<Criterion> list_of_start_period_nested=null;
				  list_of_start_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
				  ArrayList<String> startPeriodNestedQueries = createPeriodNestedQueries(list_of_start_period_nested, false, true);
				  if(!tables.contains("dt_period_of_time")) {
					  tables += ", dt_period_of_time";
					  where_clause += " AND other_clinical_trials.PERIOD_ID=dt_period_of_time.ID";
				  }
				  if(!tables.contains("dt_date1")) {
					  tables += ", dt_date AS dt_date1";
					  where_clause += " AND dt_period_of_time.START_DATE_ID=dt_date1.ID";
				  }
				  for(int k=0; k<startPeriodNestedQueries.size(); k++) {
					  where_clause += " AND dt_date1.YEAR >= "+startPeriodNestedQueries.get(k);
				  }  
				  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
			  }
			 
			if(!incl) query = "SELECT DISTINCT patient.ID FROM patient WHERE patient.ID NOT IN (" +query+ ")";

			  } break;
			  
			  case "patient": { //Check if user provided the info of all the fields 
				  patient  patient_obj =  (patient)current_Criterion;
				  
				  String tables = "patient";
				  String where_clause = "";
				  
				  /*query = "SELECT DISTINCT patient.ID " +
						  "FROM patient,  dt_date as dt_date1, dt_date as dt_date2, dt_date as dt_date3, dt_date as dt_date4, voc_direction " + //exam_lab_test, voc_lab_test, dt_amount, voc_unit, voc_assessment, dt_amount_range,interv_Surgery, dt_date, voc_direction, voc_confirmation
						  "WHERE "; 
						  //"exam_lab_test.TEST_ID=voc_lab_test.ID " +
						  //"voc_lab_test.CODE='"+crit_exam_lab_test_obj.getTEST_ID_voc_lab_test_CODE()+"' ";
				  		  //"AND " + Make_OR_of_CODES("voc_lab_test.CODE", patient_obj.getBirth_period_of_time_exact_year());
				  System.out.println("Test id: "+patient_obj.getBirth_period_of_time_exact_year());*/
				  if(!patient_obj.get_exact_age().isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  Date date = new Date();
					  Object param = new java.sql.Timestamp(date.getTime());
					  String exact_year = ((Timestamp) param).toString().split("-")[0];
					  Integer exact_birth_year = Integer.valueOf(exact_year) - Integer.valueOf(patient_obj.get_exact_age());
					  exact_year = exact_birth_year.toString();
					  where_clause += Make_specific_date_query(mode, "patient.DATE_OF_BIRTH_ID","dt_date1",exact_year,"","");
				  }
				  else if(!patient_obj.get_max_age().isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  Date date = new Date();
					  Object param = new java.sql.Timestamp(date.getTime());
					  String max_year = ((Timestamp) param).toString().split("-")[0];
					  String min_year = max_year;
					  Integer max_birth_year;
					  if(!patient_obj.get_min_age().isEmpty()) {
						  max_birth_year= Integer.valueOf(max_year) - Integer.valueOf(patient_obj.get_min_age());
						  max_year = max_birth_year.toString();
					  }
					  else max_year = "";
					  
					  Integer min_birth_year = Integer.valueOf(min_year) - Integer.valueOf(patient_obj.get_max_age());
					  min_year = min_birth_year.toString();
					  where_clause += Make_begin_end_date_query (mode,"patient.DATE_OF_BIRTH_ID", "dt_date1",min_year, "", "", max_year,"",""); 		
				  }
				  else if(!patient_obj.get_min_age().isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  Date date = new Date();
					  Object param = new java.sql.Timestamp(date.getTime());
					  String max_year = ((Timestamp) param).toString().split("-")[0];
					  System.out.println(max_year);
					  Integer max_birth_year = Integer.valueOf(max_year) - Integer.valueOf(patient_obj.get_min_age());
					  max_year = max_birth_year.toString();
					  System.out.println(max_year);
					  where_clause += Make_begin_end_date_query (mode, "patient.DATE_OF_BIRTH_ID", "dt_date1","1800", "1", "1", max_year,"",""); 		
				  }	
				  
				  if(!patient_obj.getBirth_period_of_time_exact_year().isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  where_clause += Make_specific_date_query(mode, "patient.DATE_OF_BIRTH_ID","dt_date1",patient_obj.getBirth_period_of_time_exact_year(),
							  patient_obj.getBirth_period_of_time_exact_month(),patient_obj.getBirth_period_of_time_exact_day());					  		
				  } else if(!patient_obj.getBirth_period_of_time_interval_start_year().isEmpty() || !patient_obj.getBirth_period_of_time_interval_end_year().isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  where_clause += Make_begin_end_date_query (mode,"patient.DATE_OF_BIRTH_ID", "dt_date1",patient_obj.getBirth_period_of_time_interval_start_year(), patient_obj.getBirth_period_of_time_interval_start_month(), patient_obj.getBirth_period_of_time_interval_start_day(), patient_obj.getBirth_period_of_time_interval_end_year(), patient_obj.getBirth_period_of_time_interval_end_month(),
							  patient_obj.getBirth_period_of_time_interval_end_day()); 			  
				  } else if(!patient_obj.getBirth_period_of_time_until_year().isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  where_clause += Make_begin_end_date_query (mode,"patient.DATE_OF_BIRTH_ID","dt_date1", "1800", "1", "1", patient_obj.getBirth_period_of_time_until_year(), 
							  patient_obj.getBirth_period_of_time_until_month(), patient_obj.getBirth_period_of_time_until_day()); 
				  }
				  
				  if(!patient_obj.getSymptoms_onset_period_of_time_exact_year().isEmpty()) {
					  tables += ", dt_date as dt_date2";
					  where_clause += Make_specific_date_query(mode, "patient.PSS_SYMPTOMS_ONSET_DATE_ID","dt_date2",patient_obj.getSymptoms_onset_period_of_time_exact_year(),
							  patient_obj.getSymptoms_onset_period_of_time_exact_month(),patient_obj.getSymptoms_onset_period_of_time_exact_day());					  		
				  } else if(!patient_obj.getSymptoms_onset_period_of_time_interval_start_year().isEmpty() || !patient_obj.getSymptoms_onset_period_of_time_interval_end_year().isEmpty()) {
					  tables += ", dt_date as dt_date2";
					  where_clause += Make_begin_end_date_query (mode,"patient.PSS_SYMPTOMS_ONSET_DATE_ID", "dt_date2",patient_obj.getSymptoms_onset_period_of_time_interval_start_year(), patient_obj.getSymptoms_onset_period_of_time_interval_start_month(), patient_obj.getSymptoms_onset_period_of_time_interval_start_day(), patient_obj.getSymptoms_onset_period_of_time_interval_end_year(), patient_obj.getSymptoms_onset_period_of_time_interval_end_month(),
							  patient_obj.getSymptoms_onset_period_of_time_interval_end_day()); 			  
				  } else if(!patient_obj.getSymptoms_onset_period_of_time_until_year().isEmpty()) {
					  tables += ", dt_date as dt_date2";
					  where_clause += Make_begin_end_date_query (mode,"patient.PSS_SYMPTOMS_ONSET_DATE_ID","dt_date2", "1800", "1", "1", patient_obj.getSymptoms_onset_period_of_time_until_year(), 
							  patient_obj.getSymptoms_onset_period_of_time_until_month(), patient_obj.getSymptoms_onset_period_of_time_until_day()); 
				  }
				  
				  if(!patient_obj.getDiagnosis_period_of_time_exact_year().isEmpty()) {
					  tables += ", dt_date as dt_date3";
					  where_clause += Make_specific_date_query(mode, "patient.PSS_DIAGNOSIS_DATE_ID","dt_date3",patient_obj.getDiagnosis_period_of_time_exact_year(),
							  patient_obj.getDiagnosis_period_of_time_exact_month(),patient_obj.getDiagnosis_period_of_time_exact_day());					  		
				  } else if(!patient_obj.getDiagnosis_period_of_time_interval_start_year().isEmpty() || !patient_obj.getDiagnosis_period_of_time_interval_end_year().isEmpty()) {
					  tables += ", dt_date as dt_date3";
					  where_clause += Make_begin_end_date_query (mode,"patient.PSS_DIAGNOSIS_DATE_ID", "dt_date3",patient_obj.getDiagnosis_period_of_time_interval_start_year(), patient_obj.getDiagnosis_period_of_time_interval_start_month(), patient_obj.getDiagnosis_period_of_time_interval_start_day(), patient_obj.getDiagnosis_period_of_time_interval_end_year(), patient_obj.getDiagnosis_period_of_time_interval_end_month(),
							  patient_obj.getDiagnosis_period_of_time_interval_end_day()); 			  
				  } else if(!patient_obj.getDiagnosis_period_of_time_until_year().isEmpty()) {
					  tables += ", dt_date as dt_date3";
					  where_clause += Make_begin_end_date_query (mode,"patient.PSS_DIAGNOSIS_DATE_ID","dt_date3", "1800", "1", "1", patient_obj.getDiagnosis_period_of_time_until_year(), 
							  patient_obj.getDiagnosis_period_of_time_until_month(), patient_obj.getDiagnosis_period_of_time_until_day()); 
				  }
				  
				  if(!patient_obj.getCohort_inclusion_period_of_time_exact_year().isEmpty()) {
					  tables += ", dt_date as dt_date4";
					  where_clause += Make_specific_date_query(mode, "patient.COHORT_INCLUSION_DATE_ID","dt_date4",patient_obj.getCohort_inclusion_period_of_time_exact_year(),
							  patient_obj.getCohort_inclusion_period_of_time_exact_month(),patient_obj.getCohort_inclusion_period_of_time_exact_day());					  		
				  } else if(!patient_obj.getCohort_inclusion_period_of_time_interval_start_year().isEmpty() || !patient_obj.getCohort_inclusion_period_of_time_interval_end_year().isEmpty()) {
					  tables += ", dt_date as dt_date4";
					  where_clause += Make_begin_end_date_query (mode,"patient.COHORT_INCLUSION_DATE_ID", "dt_date4",patient_obj.getCohort_inclusion_period_of_time_interval_start_year(), patient_obj.getCohort_inclusion_period_of_time_interval_start_month(), patient_obj.getCohort_inclusion_period_of_time_interval_start_day(), patient_obj.getCohort_inclusion_period_of_time_interval_end_year(), patient_obj.getCohort_inclusion_period_of_time_interval_end_month(),
							  patient_obj.getCohort_inclusion_period_of_time_interval_end_day()); 			  
				  } else if(!patient_obj.getCohort_inclusion_period_of_time_until_year().isEmpty()) {
					  tables += ", dt_date as dt_date4";
					  where_clause += Make_begin_end_date_query (mode,"patient.COHORT_INCLUSION_DATE_ID","dt_date4", "1800", "1", "1", patient_obj.getCohort_inclusion_period_of_time_until_year(), 
							  patient_obj.getCohort_inclusion_period_of_time_until_month(), patient_obj.getCohort_inclusion_period_of_time_until_day()); 
					  /*where_clause += Make_begin_end_date_query (mode,"patient.COHORT_INCLUSION_DATE_ID","dt_date4", "1800", "1", "1", patient_obj.getCohort_inclusion_period_of_time_until_year(), 
							  patient_obj.getCohort_inclusion_period_of_time_until_month(), patient_obj.getCohort_inclusion_period_of_time_until_day()); */
				  }
				  
				  if(!patient_obj.get_exact_age_of_cohort_inclusion().isEmpty()) {
					  if(!tables.contains("dt_date1")) tables += ", dt_date as dt_date1";
					  if(!tables.contains("dt_date4")) tables += ", dt_date as dt_date4";
					  where_clause += Make_specific_age_query(true, mode, "patient.DATE_OF_BIRTH_ID", "patient.COHORT_INCLUSION_DATE_ID","dt_date1","dt_date4",patient_obj.get_exact_age_of_cohort_inclusion());
				  }
				  else if(!patient_obj.get_min_age_of_cohort_inclusion().isEmpty()) {
					  if(!tables.contains("dt_date1")) tables += ", dt_date as dt_date1";
					  if(!tables.contains("dt_date4")) tables += ", dt_date as dt_date4";
					  if(!patient_obj.get_max_age_of_cohort_inclusion().isEmpty()) {
						  where_clause += Make_begin_end_age_query (mode, "patient.DATE_OF_BIRTH_ID", "patient.COHORT_INCLUSION_DATE_ID", "dt_date1", "dt_date4", patient_obj.get_min_age_of_cohort_inclusion(), patient_obj.get_max_age_of_cohort_inclusion()); 	
					  }
					  else where_clause += Make_begin_end_age_query (mode, "patient.DATE_OF_BIRTH_ID", "patient.COHORT_INCLUSION_DATE_ID", "dt_date1", "dt_date4", patient_obj.get_min_age_of_cohort_inclusion(), "200");
				  }
				  else if(!patient_obj.get_max_age_of_cohort_inclusion().isEmpty()) {
					  if(!tables.contains("dt_date1")) tables += ", dt_date as dt_date1";
					  if(!tables.contains("dt_date4")) tables += ", dt_date as dt_date4";
					  where_clause += Make_begin_end_age_query (mode, "patient.DATE_OF_BIRTH_ID", "patient.COHORT_INCLUSION_DATE_ID", "dt_date1", "dt_date4", "0", patient_obj.get_max_age_of_cohort_inclusion());
				  }
				  
				  if(!patient_obj.get_exact_age_of_diagnosis().isEmpty()) {
					  if(!tables.contains("dt_date1")) tables += ", dt_date as dt_date1";
					  if(!tables.contains("dt_date3")) tables += ", dt_date as dt_date3";
					  where_clause += Make_specific_age_query(true, mode, "patient.DATE_OF_BIRTH_ID", "patient.PSS_DIAGNOSIS_DATE_ID","dt_date1","dt_date3",patient_obj.get_exact_age_of_diagnosis());
				  }
				  else if(!patient_obj.get_min_age_of_diagnosis().isEmpty()) {
					  if(!tables.contains("dt_date1")) tables += ", dt_date as dt_date1";
					  if(!tables.contains("dt_date3")) tables += ", dt_date as dt_date3";
					  if(!patient_obj.get_max_age_of_diagnosis().isEmpty()) {
						  where_clause += Make_begin_end_age_query (mode, "patient.DATE_OF_BIRTH_ID", "patient.PSS_DIAGNOSIS_DATE_ID", "dt_date1", "dt_date3", patient_obj.get_min_age_of_diagnosis(), patient_obj.get_max_age_of_diagnosis()); 	
					  }
					  else where_clause += Make_begin_end_age_query (mode, "patient.DATE_OF_BIRTH_ID", "patient.PSS_DIAGNOSIS_DATE_ID", "dt_date1", "dt_date3", patient_obj.get_min_age_of_diagnosis(), "200");
				  }
				  else if(!patient_obj.get_max_age_of_diagnosis().isEmpty()) {
					  if(!tables.contains("dt_date1")) tables += ", dt_date as dt_date1";
					  if(!tables.contains("dt_date3")) tables += ", dt_date as dt_date3";
					  where_clause += Make_begin_end_age_query (mode, "patient.DATE_OF_BIRTH_ID", "patient.PSS_DIAGNOSIS_DATE_ID", "dt_date1", "dt_date3", "0", patient_obj.get_max_age_of_diagnosis());
				  }
				  
				  if(!patient_obj.get_exact_age_of_sign().isEmpty()) {
					  if(!tables.contains("dt_date1")) tables += ", dt_date as dt_date1";
					  if(!tables.contains("dt_date2")) tables += ", dt_date as dt_date2";
					  where_clause += Make_specific_age_query(true, mode, "patient.DATE_OF_BIRTH_ID", "patient.PSS_SYMPTOMS_ONSET_DATE_ID","dt_date1","dt_date2",patient_obj.get_exact_age_of_sign());
				  }
				  else if(!patient_obj.get_min_age_of_sign().isEmpty()) {
					  if(!tables.contains("dt_date1")) tables += ", dt_date as dt_date1";
					  if(!tables.contains("dt_date2")) tables += ", dt_date as dt_date2";
					  if(!patient_obj.get_max_age_of_sign().isEmpty()) {
						  where_clause += Make_begin_end_age_query (mode, "patient.DATE_OF_BIRTH_ID", "patient.PSS_SYMPTOMS_ONSET_DATE_ID", "dt_date1", "dt_date2", patient_obj.get_min_age_of_sign(), patient_obj.get_max_age_of_sign()); 	
					  }
					  else where_clause += Make_begin_end_age_query (mode, "patient.DATE_OF_BIRTH_ID", "patient.PSS_SYMPTOMS_ONSET_DATE_ID", "dt_date1", "dt_date2", patient_obj.get_min_age_of_sign(), "200");
				  }
				  else if(!patient_obj.get_max_age_of_sign().isEmpty()) {
					  if(!tables.contains("dt_date1")) tables += ", dt_date as dt_date1";
					  if(!tables.contains("dt_date2")) tables += ", dt_date as dt_date2";
					  where_clause += Make_begin_end_age_query (mode, "patient.DATE_OF_BIRTH_ID", "patient.PSS_SYMPTOMS_ONSET_DATE_ID", "dt_date1", "dt_date2", "0", patient_obj.get_max_age_of_sign());
				  }
				  
				  
				  query = "SELECT DISTINCT patient.ID FROM " + tables + " WHERE " + where_clause;
				  
				  String temp_where_clause = "";
				  
				  if(!patient_obj.get_max_birth_year_nested().isEmpty()) {
					  String crit_max_nested = makeCriterionList(patient_obj.get_max_birth_year_nested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_max_nested);
					  ArrayList<Criterion> list_of_max_nested=null;
					  list_of_max_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> maxNestedQueries = createNestedQueries(list_of_max_nested, false, true);
					  if(!tables.contains("dt_date1")) {
						  tables += ", dt_date as dt_date1";
						  where_clause += " patient.DATE_OF_BIRTH_ID=dt_date1.ID";
					  }
					  for(int k=0; k<maxNestedQueries.size(); k++) {
						  temp_where_clause += " AND (dt_date1.YEAR2 <= "+maxNestedQueries.get(k)+" OR (dt_date1.YEAR2 IS NULL AND dt_date1.YEAR <= "+maxNestedQueries.get(k)+"))";
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient", "outerr") + temp_where_clause;
				  }
				  
				  if(!patient_obj.get_max_symptoms_onset_year_nested().isEmpty()) {
					  String crit_max_nested = makeCriterionList(patient_obj.get_max_symptoms_onset_year_nested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_max_nested);
					  ArrayList<Criterion> list_of_max_nested=null;
					  list_of_max_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> maxNestedQueries = createNestedQueries(list_of_max_nested, false, true);
					  if(!tables.contains("dt_date2")) {
						  tables += ", dt_date as dt_date2";
						  where_clause += " patient.PSS_SYMPTOMS_ONSET_DATE_ID=dt_date2.ID";
					  }
					  for(int k=0; k<maxNestedQueries.size(); k++) {
						  temp_where_clause += " AND (dt_date2.YEAR2 <= "+maxNestedQueries.get(k)+" OR (dt_date2.YEAR2 IS NULL AND dt_date2.YEAR <= "+maxNestedQueries.get(k)+"))";
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient", "outerr") + temp_where_clause;
				  }
				  
				  if(!patient_obj.get_min_symptoms_onset_year_nested().isEmpty()) {
					  String crit_min_nested = makeCriterionList(patient_obj.get_min_symptoms_onset_year_nested());
					  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_min_nested);
					  ArrayList<Criterion> list_of_min_nested=null;
					  list_of_min_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
					  ArrayList<String> minNestedQueries = createNestedQueries(list_of_min_nested, false, false);
					  if(!tables.contains("dt_date2")) {
						  tables += ", dt_date as dt_date2";
						  where_clause += " AND patient.PSS_SYMPTOMS_ONSET_DATE_ID=dt_date2.ID";
					  }
					  for(int k=0; k<minNestedQueries.size(); k++) {
						  temp_where_clause += " AND dt_date2.YEAR >= "+minNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient", "outerr") + temp_where_clause;
				  }
				  
				  if(!patient_obj.get_max_diagnosis_year_nested().isEmpty()) {
					  String crit_max_nested = makeCriterionList(patient_obj.get_max_diagnosis_year_nested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_max_nested);
					  ArrayList<Criterion> list_of_max_nested=null;
					  list_of_max_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> maxNestedQueries = createNestedQueries(list_of_max_nested, false, true);
					  if(!tables.contains("dt_date3")) {
						  tables += ", dt_date as dt_date3";
						  where_clause += " patient.PSS_DIAGNOSIS_DATE_ID=dt_date3.ID";
					  }
					  for(int k=0; k<maxNestedQueries.size(); k++) {
						  temp_where_clause += " AND (dt_date3.YEAR2 <= "+maxNestedQueries.get(k)+" OR (dt_date3.YEAR2 IS NULL AND dt_date3.YEAR <= "+maxNestedQueries.get(k)+"))";
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient", "outerr") + temp_where_clause;
				  }
				  
				  if(!patient_obj.get_min_diagnosis_year_nested().isEmpty()) {
					  String crit_min_nested = makeCriterionList(patient_obj.get_min_diagnosis_year_nested());
					  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_min_nested);
					  ArrayList<Criterion> list_of_min_nested=null;
					  list_of_min_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
					  ArrayList<String> minNestedQueries = createNestedQueries(list_of_min_nested, false, false);
					  if(!tables.contains("dt_date3")) {
						  tables += ", dt_date as dt_date3";
						  where_clause += " AND patient.PSS_DIAGNOSIS_DATE_ID=dt_date3.ID";
					  }
					  for(int k=0; k<minNestedQueries.size(); k++) {
						  temp_where_clause += " AND dt_date3.YEAR >= "+minNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient", "outerr") + temp_where_clause;
				  }
				  
				  if(!patient_obj.get_max_cohort_inclusion_year_nested().isEmpty()) {
					  String crit_max_nested = makeCriterionList(patient_obj.get_max_cohort_inclusion_year_nested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_max_nested);
					  ArrayList<Criterion> list_of_max_nested=null;
					  list_of_max_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> maxNestedQueries = createNestedQueries(list_of_max_nested, false, true);
					  if(!tables.contains("dt_date4")) {
						  tables += ", dt_date as dt_date4";
						  where_clause += " patient.COHORT_INCLUSION_DATE_ID=dt_date4.ID";
					  }
					  for(int k=0; k<maxNestedQueries.size(); k++) {
						  temp_where_clause += " AND (dt_date4.YEAR2 <= "+maxNestedQueries.get(k)+" OR (dt_date4.YEAR2 IS NULL AND dt_date4.YEAR <= "+maxNestedQueries.get(k)+"))";
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient", "outerr") + temp_where_clause;
				  }
				  
				  if(!patient_obj.get_min_cohort_inclusion_year_nested().isEmpty()) {
					  String crit_min_nested = makeCriterionList(patient_obj.get_min_cohort_inclusion_year_nested());
					  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_min_nested);
					  ArrayList<Criterion> list_of_min_nested=null;
					  list_of_min_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
					  ArrayList<String> minNestedQueries = createNestedQueries(list_of_min_nested, false, false);
					  if(!tables.contains("dt_date4")) {
						  tables += ", dt_date as dt_date4";
						  where_clause += " AND patient.COHORT_INCLUSION_DATE_ID=dt_date4.ID";
					  }
					  for(int k=0; k<minNestedQueries.size(); k++) {
						  temp_where_clause += " AND dt_date4.YEAR >= "+minNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient", "outerr") + temp_where_clause;
				  }
				  
				  if(!patient_obj.get_start_period_symptoms_onset_year_nested().isEmpty()) {
					  String crit_start_period_nested = makeCriterionList(patient_obj.get_start_period_symptoms_onset_year_nested());
					  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_start_period_nested);
					  ArrayList<Criterion> list_of_start_period_nested=null;
					  list_of_start_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
					  ArrayList<String> startPeriodNestedQueries = createPeriodNestedQueries(list_of_start_period_nested, false, true);
					  if(!tables.contains("dt_date2")) {
						  tables += ", dt_date as dt_date2";
						  where_clause += " patient.PSS_SYMPTOMS_ONSET_DATE_ID=dt_date2.ID";
					  }
					  for(int k=0; k<startPeriodNestedQueries.size(); k++) {
						  temp_where_clause += " AND dt_date2.YEAR >= "+startPeriodNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient", "outerr") + temp_where_clause;
				  }
				  
				  if(!patient_obj.get_end_period_symptoms_onset_year_nested().isEmpty()) {
					  String crit_end_period_nested = makeCriterionList(patient_obj.get_end_period_symptoms_onset_year_nested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_end_period_nested);
					  ArrayList<Criterion> list_of_end_period_nested=null;
					  list_of_end_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> endPeriodNestedQueries = createPeriodNestedQueries(list_of_end_period_nested, false, false);
					  if(!tables.contains("dt_date2")) {
						  tables += ", dt_date as dt_date2";
						  where_clause += " patient.PSS_SYMPTOMS_ONSET_DATE_ID=dt_date2.ID";
					  }
					  for(int k=0; k<endPeriodNestedQueries.size(); k++) {
						  temp_where_clause += " AND (dt_date2.YEAR2 <= "+endPeriodNestedQueries.get(k)+" OR (dt_date2.YEAR2 IS NULL AND dt_date2.YEAR <= "+endPeriodNestedQueries.get(k)+"))";
					  }
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient", "outerr") + temp_where_clause;
				  }
				  
				  if(!patient_obj.get_start_period_diagnosis_year_nested().isEmpty()) {
					  String crit_start_period_nested = makeCriterionList(patient_obj.get_start_period_diagnosis_year_nested());
					  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_start_period_nested);
					  ArrayList<Criterion> list_of_start_period_nested=null;
					  list_of_start_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
					  ArrayList<String> startPeriodNestedQueries = createPeriodNestedQueries(list_of_start_period_nested, false, true);
					  if(!tables.contains("dt_date3")) {
						  tables += ", dt_date as dt_date3";
						  where_clause += " patient.PSS_DIAGNOSIS_DATE_ID=dt_date3.ID";
					  }
					  for(int k=0; k<startPeriodNestedQueries.size(); k++) {
						  temp_where_clause += " AND dt_date3.YEAR >= "+startPeriodNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient", "outerr") + temp_where_clause;
				  }
				  
				  if(!patient_obj.get_end_period_diagnosis_year_nested().isEmpty()) {
					  String crit_end_period_nested = makeCriterionList(patient_obj.get_end_period_diagnosis_year_nested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_end_period_nested);
					  ArrayList<Criterion> list_of_end_period_nested=null;
					  list_of_end_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> endPeriodNestedQueries = createPeriodNestedQueries(list_of_end_period_nested, false, false);
					  if(!tables.contains("dt_date3")) {
						  tables += ", dt_date as dt_date3";
						  where_clause += " patient.PSS_DIAGNOSIS_DATE_ID=dt_date3.ID";
					  }
					  for(int k=0; k<endPeriodNestedQueries.size(); k++) {
						  temp_where_clause += " AND (dt_date3.YEAR2 <= "+endPeriodNestedQueries.get(k)+" OR (dt_date3.YEAR2 IS NULL AND dt_date3.YEAR <= "+endPeriodNestedQueries.get(k)+"))";
					  }
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient", "outerr") + temp_where_clause;
				  }
				  
				  if(!patient_obj.get_start_period_cohort_inclusion_year_nested().isEmpty()) {
					  String crit_start_period_nested = makeCriterionList(patient_obj.get_start_period_cohort_inclusion_year_nested());
					  String criteria2 = Intermediate_Layer.preProcess_nestedJSON(crit_start_period_nested);
					  ArrayList<Criterion> list_of_start_period_nested=null;
					  list_of_start_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria2).getList_of_criterions();
					  ArrayList<String> startPeriodNestedQueries = createPeriodNestedQueries(list_of_start_period_nested, false, true);
					  if(!tables.contains("dt_date4")) {
						  tables += ", dt_date as dt_date4";
						  where_clause += " patient.COHORT_INCLUSION_DATE_ID=dt_date4.ID";
					  }
					  for(int k=0; k<startPeriodNestedQueries.size(); k++) {
						  temp_where_clause += " AND dt_date4.YEAR >= "+startPeriodNestedQueries.get(k);
					  }  
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient", "outerr") + temp_where_clause;
				  }
				  
				  if(!patient_obj.get_end_period_cohort_inclusion_year_nested().isEmpty()) {
					  String crit_end_period_nested = makeCriterionList(patient_obj.get_end_period_cohort_inclusion_year_nested());
					  String criteria = Intermediate_Layer.preProcess_nestedJSON(crit_end_period_nested);
					  ArrayList<Criterion> list_of_end_period_nested=null;
					  list_of_end_period_nested = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					  ArrayList<String> endPeriodNestedQueries = createPeriodNestedQueries(list_of_end_period_nested, false, false);
					  if(!tables.contains("dt_date4")) {
						  tables += ", dt_date as dt_date4";
						  where_clause += " patient.COHORT_INCLUSION_DATE_ID=dt_date4.ID";
					  }
					  for(int k=0; k<endPeriodNestedQueries.size(); k++) {
						  temp_where_clause += " AND (dt_date4.YEAR2 <= "+endPeriodNestedQueries.get(k)+" OR (dt_date4.YEAR2 IS NULL AND dt_date4.YEAR <= "+endPeriodNestedQueries.get(k)+"))";
					  }
					  query = "SELECT DISTINCT outerr.ID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient", "outerr") + temp_where_clause;
				  }
				  
				  if(!incl) query = "SELECT DISTINCT patient.ID FROM patient WHERE patient.ID NOT IN (" +query+ ")";
				  System.out.println("The query is: "+query);
			  } break;
			  
			  default:
				  System.out.println("Undefined criterion-name-"+(i+1)+" in the input JSON file.");
			} 
			query=query.replace("WHERE  AND", "WHERE");
			query=query.replace("WHERE AND", "WHERE");
			if(incl) inclusion_queries.add(query);
			else exclusion_queries.add(query);
    	}
    }
    
    public void criterionDBmatching(ArrayList<Criterion> list_of_inclusive_criterions, ArrayList<Criterion> list_of_exclusive_criterions) throws JSONException{
    	results = new Result_UIDs();
    	Boolean mode;
    	//for (int j=0;j<2;j++) {
			
    		/*if(j==0)*/ mode = false; //LOGGER.log(Level.INFO,"======** UIDs_defined_ALL_elements **======\n",true);}
    		//else mode = true; //LOGGER.log(Level.INFO,"======** UIDs_UNdefined_some_elements **======\n",true);}
    		String results_of_all_Criterions="";
    		results_of_all_Criterions = executeQuery(list_of_inclusive_criterions, true, results_of_all_Criterions);
    		results_of_all_Criterions = executeQuery(list_of_exclusive_criterions, false, results_of_all_Criterions);
    		results.Input_UIDs(mode,results_of_all_Criterions);
    		//results.Input_UIDs(true,"");
    	//}
    	UIDsDefined.add(results.UIDs_defined_ALL_elements );
    	//UIDsUndefined.add(results.UIDs_UNdefined_some_elements);
    	System.out.println(results.Output_JSON_UIDs());
    }
    
    public static String intersection_of_UIDs(String str_of_users_1, String str_of_users_2 ){
		  Set<String> s1 = new HashSet<String>(Arrays.asList(str_of_users_1.split(" ")));
		  Set<String> s2 = new HashSet<String>(Arrays.asList(str_of_users_2.split(" ")));
		  Set<String> intersect = new TreeSet<String>(s1);
		  intersect.retainAll(s2);
		  String result="";
		    Iterator<String> it = intersect.iterator();
		    while (true) { 
		      if (it.hasNext()) result+=it.next() + " ";
		      else break;
		    }
		  return result;
	  }

    
    public void writeXMLResponse(int cohortIndex, boolean createXML, int cohortID, String statusID, String username){
    	try{
    		if(createXML) {
        		//File fXmlFile = new File(getServletContext().getRealPath("/WEB-INF/Response"+requestID+".xml"));
        		//File fXmlFile = new File("Response"+requestID+".xml");
        		ObjectFactory objectFactory = new ObjectFactory();
        		JAXBElement<PatientsSelectionResponse> je =  objectFactory.createPatientsSelectionResponse(patientsSelectionResponse);
        		//xmljaxbMarshaller.marshal(je, fXmlFile);
        		StringWriter sw = new StringWriter();
        		xmljaxbMarshaller.marshal(je, sw);
        		myRespXML = sw.toString();
        	}
    		else if(cohortIndex==0) {
    		xmljaxbContext = JAXBContext.newInstance(ObjectFactory.class);
    		xmljaxbMarshaller = xmljaxbContext.createMarshaller();
    		xmljaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    		patientsSelectionResponse = new PatientsSelectionResponse();
    		//jaxbMarshaller.marshal(patientsSelectionResponse, responseXML);
    		//patientsSelectionResponse.setUserID(patientsSelectionRequest.getUserID());
    		patientsSelectionResponse.setUserID(username);
    		patientsSelectionResponse.setSessionID(patientsSelectionRequest.getSessionID());
    		GregorianCalendar gregorianCalendar = new GregorianCalendar();
            DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
            XMLGregorianCalendar now = 
                datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
            now.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
    		patientsSelectionResponse.setResponseDate(now);
    		//for(String cohortID: patientsSelectionRequest.getCohortID()){
    		}else {
    			CohortResponse cohortResponse = new CohortResponse();
    			//cohortResponse.setCohortID(patientsSelectionRequest.getCohortID().get(cohortIndex));
    			if (cohortID<10) cohortResponse.setCohortID("COHORT-ID-0"+cohortID);
    			else cohortResponse.setCohortID("COHORT-ID-"+cohortID);
    			cohortResponse.setQueryDate(patientsSelectionRequest.getRequestDate());
    			if(statusID.equals("2")) {
    			if(results.UIDs_defined_ALL_elements.length==1 && results.UIDs_defined_ALL_elements[0].equals("")) cohortResponse.setEligiblePatientsNumber(0);
    			else cohortResponse.setEligiblePatientsNumber(results.UIDs_defined_ALL_elements.length);
    			
    			/*if(patientsSelectionRequest.getRequestID().value().equals("ELIGIBLE_PATIENTS_IDS")) {
    				if(results.UIDs_defined_ALL_elements.length==1 && results.UIDs_defined_ALL_elements[0].equals("")) cohortResponse.setEligiblePatientsIDs("");
    				else{
    					String dbstring = "";
    					for(int k=0; k<results.UIDs_defined_ALL_elements.length; k++) {
    						if(k==0) dbstring += results.UIDs_defined_ALL_elements[k];
    						else dbstring += ", "+results.UIDs_defined_ALL_elements[k];
    					}
    					cohortResponse.setEligiblePatientsIDs(dbstring);
    				}
    			}*/
    			EligibilityCriteriaUsed inclAndExclCriteriaUsed = new EligibilityCriteriaUsed();
    			System.out.println("Incl notes size: "+inclusion_criteria.size());
    			for(int j=0; j<inclusion_criteria.size(); j++) {
    				System.out.println(inclusion_criteria.get(j));
    			}
    			System.out.println("Excl notes size: "+exclusion_criteria.size());
    			for(int j=0; j<exclusion_criteria.size(); j++) {
    				System.out.println(exclusion_criteria.get(j));
    			}
    			int i=0;
    			for(org.ntua.criteria.Criterion inclCriterion: patientsSelectionRequest.getEligibilityCriteria().getInclusionCriteria().getCriterion()){
    				CriterionUsage inclCriterionUsage = new CriterionUsage();
    				inclCriterionUsage.setCriterionID(inclCriterion.getUID());
    				if(inclusion_criteria.get(i).getString("usage").equals("used")) inclCriterionUsage.setCriterionUsageStatus(CriterionUsageStatus.USED);
    				else inclCriterionUsage.setCriterionUsageStatus(CriterionUsageStatus.NOT_USED);
    				//System.out.println(inclusion_criteria.get(i));
    				inclCriterionUsage.setNotesHTML(inclCriterion.getDescription()+": "+inclusion_criteria.get(i).getString("notes")); //+" - "+inclCriterionUsage.getCriterionUsageStatus());
    				//inclCriterionUsage.setNotesHTML(inclCriterion.getDescription()+": "+inclCriterion.getFormalExpression().get(0).getBooleanExpression().trim()+" - "+inclCriterionUsage.getCriterionUsageStatus());
    				inclAndExclCriteriaUsed.getCriterionUsage().add(inclCriterionUsage);
    				i++;
    			}
    			
    			i=0;
    			for(org.ntua.criteria.Criterion exclCriterion: patientsSelectionRequest.getEligibilityCriteria().getExclusionCriteria().getCriterion()){
    				CriterionUsage exclCriterionUsage = new CriterionUsage();
    				exclCriterionUsage.setCriterionID(exclCriterion.getUID());
    				if(exclusion_criteria.get(i).getString("usage").equals("used")) exclCriterionUsage.setCriterionUsageStatus(CriterionUsageStatus.USED);
    				else exclCriterionUsage.setCriterionUsageStatus(CriterionUsageStatus.NOT_USED);
    				//System.out.println(exclusion_criteria.get(i));
    				exclCriterionUsage.setNotesHTML(exclCriterion.getDescription()+": "+exclusion_criteria.get(i).getString("notes")); //+" - "+exclCriterionUsage.getCriterionUsageStatus());
    				//exclCriterionUsage.setNotesHTML(exclCriterion.getDescription()+": "+exclCriterion.getFormalExpression().get(0).getBooleanExpression().trim()+" - "+exclCriterionUsage.getCriterionUsageStatus());
    				inclAndExclCriteriaUsed.getCriterionUsage().add(exclCriterionUsage);
    				i++;
    			}
    			
    			cohortResponse.setEligibilityCriteriaUsed(inclAndExclCriteriaUsed);
    			patientsSelectionResponse.getCohortResponse().add(cohortResponse);
    			}
    			
    		//}
    		}
    	}
    	catch(Exception e){
	    	LOGGER.log(Level.SEVERE,"Unknown Exception while writing xml response.",true);
	    	all.put("errorMessage", "An error occured while writing xml response. Check your request xml format and try again.");
    		e.printStackTrace();
    	}
    }
    
    private String getCohortsC4(String darID, String username, String password) {
    	String result = "";
    	try {
	        String webPage = "https://private.harmonicss.eu/index.php/apps/coh/api/1.0/c4?darId="+darID;

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
	        result = sb.toString();
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
    	return result;
    }
    
    private JSONObject getCredentials(int cohortID, String username, String password) {
    	JSONObject credentials = new JSONObject();
    	try {
	        String webPage = "https://private.harmonicss.eu/index.php/apps/coh/api/1.0/cohortid?id="+cohortID;

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
    
    public void accessCohorts(String darID, String username, String password, ArrayList<Criterion> list_of_inclusive_criterions, ArrayList<Criterion> list_of_exclusive_criterions) throws IOException, JSONException, SQLException, ClassNotFoundException{
    	//String[] cohortAccess = new String[cohort_names.size()];
    	/*URL myXMLService = new URL("http://localhost:8080/GetCohortsC4New/GetCohortsServlet?darId="+darID);
    	HttpURLConnection con = (HttpURLConnection) myXMLService.openConnection();
		con.setRequestMethod("GET");
		con.setDoOutput(true);
		Reader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
	    StringBuilder sb = new StringBuilder();
	    for (int c; (c = in.read()) >= 0;)
	        sb.append((char)c);*/
    	Boolean mode;
    	//for (int j=0;j<2;j++) {
			
    		/*if(j==0)*/ mode = false; //LOGGER.log(Level.INFO,"======** UIDs_defined_ALL_elements **======\n",true);}
    		//else mode = true; //LOGGER.log(Level.INFO,"======** UIDs_UNdefined_some_elements **======\n",true);}
    	createQuery(list_of_inclusive_criterions, true, mode);
    	createQuery(list_of_exclusive_criterions, false, mode);
    	//}
	    JSONArray cohorts = new JSONArray(getCohortsC4(darID, username, password));
	    boolean createXML = false;
	    String cohort_ids = "";
	    int cohortIndex = 0;
	    writeXMLResponse(cohortIndex, createXML, 0, "1", username);
	    cohortIndex++;
    	for(int i=0; i<cohorts.length(); i++){
    		patients_found = true;
    		String cohortName = "";
    		int mycohortid = Integer.valueOf(cohorts.getJSONObject(i).get("cohortId").toString());
    		if(mycohortid<10) cohortName = "chdb00"+mycohortid;
    		else cohortName = "chdb0"+mycohortid;
    		if(i==0) cohort_ids += mycohortid;
    		else cohort_ids += ","+mycohortid;
    		if(cohorts.getJSONObject(i).get("statusId").equals("2")) {
    			JSONObject credentials = getCredentials(mycohortid, username, password);
    			//String cohortName = cohorts.getJSONObject(i).get("cohortName").toString();
    			
    			System.out.println("-------------------------------- Execute query for cohort " + cohortName + " -------------------------------");
    			//ConfigureFile obj = new ConfigureFile("jdbc:mysql://ponte.grid.ece.ntua.gr:3306/"+cohortName,"emps","emps");
    			ConfigureFile obj = new ConfigureFile("jdbc:mysql://"+credentials.getString("dbserver")+":"+credentials.getString("dbport")+"/"+credentials.getString("dbarea")+"?autoReconnect=true&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC",credentials.getString("dbuname"),credentials.getString("dbupass"));
    			if(!DBServiceCRUD.makeJDBCConnection(obj)) {
    				System.out.println("Connection with the Database failed. Check the Credentials and the DB URL.");
    				all.put("errorMessage", "An error occured while connecting to database. Check your database credentials and try again.");
    			}
    	    	else {
    	    	long startTime = System.nanoTime();
    	    	System.out.println("everything's gooooooood");
    			criterionDBmatching(list_of_inclusive_criterions,list_of_exclusive_criterions);
    			//if(i==cohorts.length()-1) createXML=true;
    			writeXMLResponse(cohortIndex, createXML, mycohortid, "2", username);	
    			inclusion_criteria.clear();
    			exclusion_criteria.clear();
    			if(patientsSelectionRequest.getRequestID().value().equals("ELIGIBLE_PATIENTS_IDS")) {
    				DBServiceCRUD.setPatientsIDs(darID, results);
    			}
    			long endTime = System.nanoTime();
    			System.out.println("Request executed in "+TimeUnit.NANOSECONDS.toMillis(endTime - startTime)+" ms");
    			DBServiceCRUD.closeJDBCConnection();
    			System.out.println("End");
    	    }
    		}
    		/*else {
    			if(i==cohorts.length()-1) {
    				createXML=true;
    				writeXMLResponse(cohortIndex, createXML, 0, "1", username);
    			}
    			if(cohorts.getJSONObject(i).get("statusId").equals("1")) {
    				writeXMLResponse(i, createXML, mycohortid, "1", username);
    			}
    			else {
    				writeXMLResponse(i, createXML, mycohortid, "3", username);
    			}
    		}*/
    			
    		
    	}
    	createXML=true;
    	writeXMLResponse(cohortIndex, createXML, 0, "1", username);
    	inclusion_queries.clear();
    	exclusion_queries.clear();
    	Class.forName("com.mysql.jdbc.Driver");
		System.out.println("Congrats - Seems your MySQL JDBC Driver Registered!");
		
		Connection db_con_obj = DriverManager.getConnection("jdbc:mysql://"+prop.getProperty("dbdomain").trim()+":"+prop.getProperty("dbport").trim()+"/"+prop.getProperty("dbname").trim()+"?autoReconnect=true&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC", prop.getProperty("dbusername").trim(), prop.getProperty("dbpassword").trim());
		Date date = new Date();
		Object param = new java.sql.Timestamp(date.getTime());
		System.out.println(darID);
		String query = "INSERT INTO EXECUTION_DATA (USER_ID, REQUEST_ID, REQUEST_XML, EXECUTION_DATE, RESPONSE_XML) VALUES (?, ?, ?, ?, ?)";
		PreparedStatement db_prep_obj = db_con_obj.prepareStatement(query);
		db_prep_obj.setString(1, username);
		db_prep_obj.setString(2, darID);
		db_prep_obj.setString(3, myReqXML);//.replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;"));
		db_prep_obj.setTimestamp(4, (Timestamp) param);
		db_prep_obj.setString(5, myRespXML);//.replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;"));
		db_prep_obj.execute();
		if (db_prep_obj != null) {
        	db_prep_obj.close();
        }
        if (db_con_obj != null) {
        	db_con_obj.close();
        }
    	System.out.println("-------------------------------- Execution data saved to database -------------------------------");
    	
    	final Sardine upSardine = SardineFactory.begin(username, password);
    	if(!upSardine.exists("http://83.212.104.6/hcloud/remote.php/webdav/Patient-Selection-Execution-Results")){
    		upSardine.createDirectory("http://83.212.104.6/hcloud/remote.php/webdav/Patient-Selection-Execution-Results");
    	}
    	byte[] reqBytes = myReqXML.getBytes(StandardCharsets.UTF_8);
    	String requestFile = username+"-"+((Timestamp) param).toString().replace("-","").replace(" ","").replace(":","").split("\\.")[0]+"-"+requestID+"-Request.xml";
    	upSardine.put("http://83.212.104.6/hcloud/remote.php/webdav/Patient-Selection-Execution-Results/"+requestFile, reqBytes);
    	byte[] respBytes = myRespXML.getBytes(StandardCharsets.UTF_8);
	    String responseFile = username+"-"+((Timestamp) param).toString().replace("-","").replace(" ","").replace(":","").split("\\.")[0]+"-"+requestID+"-Response.xml";
    	upSardine.put("http://83.212.104.6/hcloud/remote.php/webdav/Patient-Selection-Execution-Results/"+responseFile, respBytes);
    	JSONObject dbData = setCohortHistory(username, password, ((Timestamp) param).toString().split("\\.")[0], cohort_ids, requestFile, responseFile);
    	System.out.println(dbData);
    }
    
    private static JSONObject setCohortHistory(String username, String password, String submitDate, String cohort_ids, String jsonfileInput, String jsonfileOutput) throws IOException, JSONException{
		URL url = new URL("https://private.harmonicss.eu/index.php/apps/coh/api/1.0/history");
        String authString = username + ":" + password;
        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        String authStringEnc = new String(authEncBytes);
		JSONObject params = new JSONObject();
		params.put("userId", username);
		params.put("serviceId", "1");
	    params.put("submitdate", submitDate);
	    params.put("cohortIds", cohort_ids);
	    
	    params.put("jsonfileInput", jsonfileInput);
	    params.put("jsonfileOutput", jsonfileOutput);
	    params.put("comment", "");
	    params.put("darId", requestID);
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
	    JSONObject dbData = new JSONObject(resp);
	    return dbData;
    }
    
    /*private static String readLineByLineJava8(String filePath) 
	{
	    StringBuilder contentBuilder = new StringBuilder();
	    try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8)) 
	    {
	        stream.forEach(s -> contentBuilder.append(s).append("\n"));
	    }
	    catch (IOException e) 
	    {
	    	LOGGER.log(Level.SEVERE,"IOException, check file path.",true);
	        e.printStackTrace();
	    }
	    return contentBuilder.toString();
	}*/
    
    public boolean canUseCriterion(Criterion crit){
    	String query="";
    	String assistanceQuery="";
    	boolean myresults = false;
    	switch(crit.getCriterion()) {
    		case "demographics_gender": {
    			query = "SELECT * FROM demo_sex_data LIMIT 1";
    			termAndSubterms = "Search for gender data";
    		}
    		break;
    		case "demographics_ethnicity": {
    			query = "SELECT * FROM demo_ethnicity_data LIMIT 1";
    			termAndSubterms = "Search for ethnicity data";
    		}
    		break;
    		case "demographics_education": {
    			query = "SELECT * FROM demo_education_level_data LIMIT 1";
    			termAndSubterms = "Search for education data";
    		}
    		break;
    		case "demographics_weight": {
    			query = "SELECT * FROM demo_weight_data LIMIT 1";
    			termAndSubterms = "Search for weight data";
    		}
    		break;
    		case "demographics_occupation": {
    			query = "SELECT * FROM demo_occupation_data LIMIT 1";
    			termAndSubterms = "Search for occupation data";
    		}
    		break;
    		case "demographics_pregnancy": {
    			query = "SELECT * FROM demo_pregnancy_data LIMIT 1";
    			termAndSubterms = "Search for pregnancy data";
    		}
    		break;
    		case "lifestyle_smoking": {
    			query = "SELECT * FROM lifestyle_smoking LIMIT 1";
    			termAndSubterms = "Search for smoking data";
    		}
    		break;
    		case "condition_symptom": {
    			condition_symptom crit_cond_symptom_obj = (condition_symptom)crit;
    			query = "SELECT * FROM cond_symptom, voc_symptom_sign WHERE cond_symptom.CONDITION_ID = voc_symptom_sign.ID AND (" + Make_OR_of_CODES("voc_symptom_sign.CODE", crit_cond_symptom_obj.getVoc_symptom_sign_CODE());
    			String narrowTerms = getTermsWithNarrowMeaning(crit_cond_symptom_obj.getVoc_symptom_sign_CODE());
				String[] allNarrowTerms = narrowTerms.split(",");
				for(int c=1; c<allNarrowTerms.length; c++) {
					query += " OR " + Make_OR_of_CODES("voc_symptom_sign.CODE", allNarrowTerms[c]);
				}
				query += ") LIMIT 1";
				assistanceQuery = "SELECT NAME FROM voc_symptom_sign WHERE" + query.split("AND")[1]; 
    		}
    		break;
    		case "condition_diagnosis": {
    			condition_diagnosis crit_cond_diagnosis_obj = (condition_diagnosis)crit;
    			query = "SELECT * FROM cond_diagnosis, voc_medical_condition WHERE cond_diagnosis.CONDITION_ID = voc_medical_condition.ID AND (" + Make_OR_of_CODES("voc_medical_condition.CODE", crit_cond_diagnosis_obj.getCondition());
    			String narrowTerms = getTermsWithNarrowMeaning(crit_cond_diagnosis_obj.getCondition());
				String[] allNarrowTerms = narrowTerms.split(",");
				for(int c=1; c<allNarrowTerms.length; c++) {
					query += " OR " + Make_OR_of_CODES("voc_medical_condition.CODE", allNarrowTerms[c]);
				}
				query += ") LIMIT 1";
				assistanceQuery = "SELECT NAME FROM voc_medical_condition WHERE" + query.split("AND")[1]; 
    		}
    		break;
    		case "intervention_medication": {
    			intervention_medication  crit_interv_medication_obj =  (intervention_medication )crit;
				query = "SELECT * FROM interv_medication, voc_pharm_drug WHERE interv_medication.MEDICATION_ID = voc_pharm_drug.ID AND (" + Make_OR_of_CODES("voc_pharm_drug.CODE", crit_interv_medication_obj.getVoc_pharm_drug_CODE());
				String narrowTerms = getTermsWithNarrowMeaning(crit_interv_medication_obj.getVoc_pharm_drug_CODE());
				String[] allNarrowTerms = narrowTerms.split(",");
				for(int c=1; c<allNarrowTerms.length; c++) {
					query += " OR " + Make_OR_of_CODES("voc_pharm_drug.CODE", allNarrowTerms[c]);
				}
				query += ") LIMIT 1";
				assistanceQuery = "SELECT NAME FROM voc_pharm_drug WHERE" + query.split("AND")[1]; 
    		}
    		break;
    		case "intervention_chemotherapy": {
				  //intervention_chemotherapy  crit_interv_chemotherapy_obj =  (intervention_chemotherapy)crit;
				query = "SELECT * FROM interv_chemotherapy LIMIT 1"; //, voc_confirmation WHERE interv_chemotherapy.DUE_TO_PSS_ID = voc_confirmation.ID AND " + Make_OR_of_CODES("voc_confirmation.CODE", crit_interv_chemotherapy_obj.getReason());
				termAndSubterms = "Search for chemotherapy data";
    		}
    		break;
    		case "intervention_surgery": { 
    			query = "SELECT * FROM interv_surgery LIMIT 1";
    			termAndSubterms = "Search for surgery data";
    		}
    		break;
    		case "examination_lab_test": {
    			examination_lab_test  examination_lab_test_obj =  (examination_lab_test)crit;
				query = "SELECT * FROM exam_lab_test, voc_lab_test WHERE exam_lab_test.TEST_ID=voc_lab_test.ID AND (" + Make_OR_of_CODES("voc_lab_test.CODE", examination_lab_test_obj.getTest_id());
				String narrowTerms = getTermsWithNarrowMeaning(examination_lab_test_obj.getTest_id());
				String[] allNarrowTerms = narrowTerms.split(",");
				for(int c=1; c<allNarrowTerms.length; c++) {
					query += " OR " + Make_OR_of_CODES("voc_lab_test.CODE", allNarrowTerms[c]);  
				}
				query += ") LIMIT 1";
				assistanceQuery = "SELECT NAME FROM voc_lab_test WHERE" + query.split("AND")[1];
    		}
    		break;
    		case "examination_biopsy": { //Check if user provided the info of all the fields 
				//examination_biopsy  examination_biopsy_obj =  (examination_biopsy)crit;
				query = "SELECT * FROM exam_biopsy LIMIT 1"; /*, voc_biopsy WHERE exam_biopsy.BIOPSY_ID=voc_biopsy.ID AND (voc_biopsy.CODE='"+examination_biopsy_obj.getBiopsy_type()+"'"; // ='SAL-BIO-21' Make_OR_of_CODES("voc_lab_test.CODE", examination_biopsy_obj.getBiopsy_type());				  		 
				String narrowTerms = getTermsWithNarrowMeaning(examination_biopsy_obj.getBiopsy_type());
				String[] allNarrowTerms = narrowTerms.split(",");
				for(int c=1; c<allNarrowTerms.length; c++) {
					query += " OR voc_biopsy.CODE='" + allNarrowTerms[c] + "'";
				}
				query += ") ";
				assistanceQuery = "SELECT NAME FROM voc_biopsy WHERE" + query.split("AND")[1];*/
    		}
    		break;
    		case "examination_medical_imaging_test": { //Check if user provided the info of all the fields 
				examination_medical_imaging_test  examination_medical_imaging_test_obj =  (examination_medical_imaging_test)crit;
				query = "SELECT * FROM exam_medical_imaging_test, voc_medical_imaging_test WHERE exam_medical_imaging_test.TEST_ID=voc_medical_imaging_test.ID AND (" + Make_OR_of_CODES("voc_medical_imaging_test.CODE", examination_medical_imaging_test_obj.getTest_id()) +" ";
				String myNarrowTerms = getTermsWithNarrowMeaning(examination_medical_imaging_test_obj.getTest_id());
				  String[] myAllNarrowTerms = myNarrowTerms.split(",");
				  for(int c=1; c<myAllNarrowTerms.length; c++) {
					  query += "OR " + Make_OR_of_CODES("voc_medical_imaging_test.CODE", myAllNarrowTerms[c]);
				  }
				  query += ") LIMIT 1";
				  assistanceQuery = "SELECT NAME FROM voc_medical_imaging_test WHERE" + query.split("AND")[1];
    		}
    		break;
    		case "examination_questionnaire_score": { //Check if user provided the info of all the fields 
				examination_questionnaire_score  examination_questionnaire_score_obj =  (examination_questionnaire_score)crit;
				query = "SELECT * FROM exam_questionnaire_score, voc_questionnaire WHERE exam_questionnaire_score.SCORE_ID=voc_questionnaire.ID AND (" + Make_OR_of_CODES("voc_questionnaire.CODE", examination_questionnaire_score_obj.getScore());
				String myNarrowTerms = getTermsWithNarrowMeaning(examination_questionnaire_score_obj.getScore());
				  String[] myAllNarrowTerms = myNarrowTerms.split(",");
				  for(int c=1; c<myAllNarrowTerms.length; c++) {
					  query += " OR " + Make_OR_of_CODES("voc_questionnaire.CODE", myAllNarrowTerms[c]);
				  }
				  query += ") LIMIT 1";
				  assistanceQuery = "SELECT NAME FROM voc_questionnaire WHERE" + query.split("AND")[1];
    		}
    		break;
    		case "examination_essdai_domain": {
    			examination_essdai_domain  examination_essdai_domain_obj =  (examination_essdai_domain)crit;
				query = "SELECT * FROM exam_essdai_domain, voc_essdai_domain WHERE exam_essdai_domain.DOMAIN_ID = voc_essdai_domain.ID AND " + Make_OR_of_CODES("voc_essdai_domain.CODE", examination_essdai_domain_obj.getDomain())+" LIMIT 1";
				assistanceQuery = "SELECT DOMAIN_NAME FROM voc_essdai_domain WHERE" + query.split("AND")[1];
    		}
    		break;
    		case "examination_caci_condition": { //Check if user provided the info of all the fields 
				examination_caci_condition  examination_caci_condition_obj =  (examination_caci_condition)crit;
				query = "SELECT * FROM exam_caci_condition, voc_caci_condition WHERE exam_caci_condition.CACI_ID = voc_caci_condition.ID AND " + Make_OR_of_CODES("voc_caci_condition.CODE", examination_caci_condition_obj.getCaci())+" LIMIT 1";
				assistanceQuery = "SELECT * FROM voc_caci_condition WHERE" + query.split("AND")[1];
    		}
    		break;
    		case "other_healthcare_visit": { //Check if user provided the info of all the fields 
				other_healthcare_visit  other_healthcare_visit_obj =  (other_healthcare_visit)crit;
				query = "SELECT * FROM other_healthcare_visit, voc_specialist WHERE other_healthcare_visit.SPECIALIST_ID=voc_specialist.ID AND " + Make_OR_of_CODES("voc_specialist.CODE", other_healthcare_visit_obj.getSpecialist())+" LIMIT 1";
				assistanceQuery = "SELECT NAME FROM voc_specialist WHERE" + query.split("AND")[1];
    		}
    		break;
    		case "other_family_history": { //Check if user provided the info of all the fields 
				other_family_history  other_family_history_obj =  (other_family_history)crit;
				query = "SELECT * FROM other_family_history, voc_medical_condition WHERE other_family_history.MEDICAL_CONDITION_ID=voc_medical_condition.ID AND (" + Make_OR_of_CODES("voc_medical_condition.CODE", other_family_history_obj.getMedical_condition());
				String narrowTerms = getTermsWithNarrowMeaning(other_family_history_obj.getMedical_condition());
				String[] allNarrowTerms = narrowTerms.split(",");
				for(int c=1; c<allNarrowTerms.length; c++) {
					query += " OR " + Make_OR_of_CODES("voc_medical_condition.CODE", allNarrowTerms[c]);
				}
				query += ") LIMIT 1";
				assistanceQuery = "SELECT NAME FROM voc_medical_condition WHERE" + query.split("AND")[1];
    		}
    		break;
    		case "other_clinical_trials": { //Check if user provided the info of all the fields 
				query = "SELECT * FROM other_clinical_trials LIMIT 1"; 
				termAndSubterms = "Search for other clinical trials data";
    		}
    		break;
    		case "patient": { //Check if user provided the info of all the fields 
				query = "SELECT * FROM patient LIMIT 1";
				termAndSubterms = "Search for patients data";
    		}
    		break;
    		default: System.out.println("Undefined criterion-name in the input JSON file.");
    	}
    	try { 
    		if(!query.equals("")) myresults = DBServiceCRUD.testQuery(query);
    		if(!assistanceQuery.equals("")) {
    			boolean essdai = false;
    			boolean caci = false;
    			if(assistanceQuery.contains("voc_essdai_domain")) essdai=true;
    			else if(assistanceQuery.contains("voc_caci_condition")) caci=true;
    			String[] assistanceResults = DBServiceCRUD.assistanceQuery(assistanceQuery,essdai,caci).split(",");
    			termAndSubterms = "Search for term: " + assistanceResults[0];
    			if(assistanceResults.length>1) {
    				termAndSubterms += " and subterms:";
    				for(int sub=1; sub<assistanceResults.length; sub++) {
    					if(sub==1) termAndSubterms += assistanceResults[sub];
    					else termAndSubterms += "," + assistanceResults[sub];
    				}
    			}
    		}
    	} catch (SQLException e) {
    		LOGGER.log(Level.SEVERE,"SQLException, check DB url and credentials.",true);
    		//flush_handler();
		
    		e.printStackTrace();
    		//return "Bad type query or arguments: "+query;
    	}
    	if (!myresults) return false;
    	else return true;
    }
    
    public int findPatientsIds(String criterion){
    	return 0;
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//Logger LOGGER = Util_Logger.Initialize_logger("C:/Users/Jason/eclipse-workspace/HarmonicSS/LogFile.log");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected synchronized void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		LOGGER = Util_Logger.Initialize_logger(getServletContext().getRealPath("/WEB-INF/LogFile.log"));
		
		all = new JSONObject();
		Infos req = new Gson().fromJson(request.getReader(), Infos.class);
		//cohortResponseList
		requestID = req.requestID;
		if(requestID!=null){
			/*try {
				all.put("requestId", requestID);
			} catch (JSONException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}*/
	    	manager = OWLManager.createOWLOntologyManager();
	    	InputStream input = new FileInputStream(getServletContext().getRealPath("/WEB-INF/infos.properties"));
	    	prop = new Properties();
            // load a properties file
            prop.load(input);
			documentIRI = IRI.create(getServletContext().getResource("/WEB-INF/"+prop.getProperty("owlFile").trim()));
		    try{
		        ontology = manager.loadOntologyFromOntologyDocument(documentIRI);
	            findClasses();
	            findSubclasses();
			}
			catch (OWLOntologyCreationException e) {
		        e.printStackTrace();
		        LOGGER.log(Level.SEVERE,"Ontology cannot be created, check owl file path!",true);
			}
			//crit_incl_excl_in = readXMLbyRequestID(requestID, req.username, req.password);
			//System.out.println(crit_incl_excl_in);
			//crit_incl_excl=crit_incl_excl_in.split("XXX");
			//criteria = Intermediate_Layer.preProcess_JSON(crit_incl_excl[0]);
			//System.out.println("After Criteria preprocessed:\n"+criteria);
		    String criteria = "";
			try {
				String crit_incl_excl_in = "";
				crit_incl_excl_in = readXMLbyRequestID(requestID, req.username, req.password);
				//System.out.println(crit_incl_excl_in);
				if(!crit_incl_excl_in.equals("")) {
					String[] crit_incl_excl=crit_incl_excl_in.split("XXX");
					criteria = Intermediate_Layer.preProcess_JSON(crit_incl_excl[0]);
					ArrayList<Criterion> list_of_inclusive_criterions=null;
					list_of_inclusive_criterions = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					//findCriterion((Criterion)list_of_inclusive_criterions.get(0));
					//System.out.println(list_of_inclusive_criterions);
					criteria = Intermediate_Layer.preProcess_JSON(crit_incl_excl[1]);
					ArrayList<Criterion> list_of_exclusive_criterions=null;
					list_of_exclusive_criterions = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
					//System.out.println(list_of_exclusive_criterions);
					accessCohorts(requestID, req.username, req.password, list_of_inclusive_criterions, list_of_exclusive_criterions);
					//String responseXML = readLineByLineJava8(getServletContext().getRealPath("/WEB-INF/Response"+requestID+".xml"));
					if(!myRespXML.equals("")) 
						all.put("responseXML", myRespXML);
				}else {
					if(!all.has("errorMessage")) all.put("errorMessage", "An error occured while retrieving request XML. Check your credentials and try again.");
				}
				
			} catch (NullPointerException e1) {
				/*LOGGER.log(Level.SEVERE,"JsonParseException Bad JSON format: "+criteria,true);
				flush_handler();*/
				all.put("errorMessage", "An error occured while executing the request. Check your xml format and json criteria and try again.");
				LOGGER.log(Level.SEVERE,"NullPointerException, Error while parsing json criteria. Check your json format and all criteria fields and try again.",true);
				e1.printStackTrace();
				//return "JsonParseException Bad JSON format.";
			} catch (JsonParseException e1) {
				LOGGER.log(Level.SEVERE,"JsonParseException Bad JSON format: "+criteria,true);
				//flush_handler();
				e1.printStackTrace();
				//return "JsonParseException Bad JSON format.";
			} catch (JsonMappingException e1) {
				LOGGER.log(Level.SEVERE,"JsonMappingException Bad JSON format: "+criteria,true);
				//flush_handler();
				e1.printStackTrace();
				//return "JsonParseException Bad JSON format.";
			} catch (IOException e1) {
				LOGGER.log(Level.SEVERE,"IOException Bad JSON format: "+criteria,true);
				//flush_handler();
				e1.printStackTrace();
				//return "JsonParseException Bad JSON format.";
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				LOGGER.log(Level.SEVERE,"SQLException, Check DB URL and credentials",true);
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JAXBException e) {
				all.put("errorMessage", "An error occured while retrieving request XML. Check your XML format and try again.");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//System.out.println("After Criteria preprocessed:\n"+criteria);
			
	    	/*ConfigureFile obj = new ConfigureFile("jdbc:mysql://localhost:3306/harmonicssdb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","");
	    	//ConfigureFile obj = new ConfigureFile("jdbc:mysql://147.102.19.66:3306/harmonicssdb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","emps","emps");
	    	
			if(!DBServiceCRUD.makeJDBCConnection(obj))  System.out.println("Connection with the Database failed. Check the Credentials and the DB URL.");
	    	else System.out.println("everything's gooooooood");
	    	
	    	criterionDBmatching(list_of_inclusive_criterions,list_of_exclusive_criterions);
			writeXMLResponse();			
			System.out.println("End");*/
			
		}
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = response.getWriter();
		pw.flush();
		pw.print(all.toString());
		pw.close();
	}

}