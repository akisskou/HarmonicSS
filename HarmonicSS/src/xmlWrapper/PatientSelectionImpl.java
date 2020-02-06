package xmlWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
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
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
//import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import com.google.gson.Gson;

import jsonProcess.*;
import criterionManager.*;
import criterionManager.Criterion;

import org.apache.tomcat.util.codec.binary.Base64;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import static queries.SQL_aux_functions.Make_OR_of_CODES;
import static queries.SQL_aux_functions.Make_begin_end_date_query;
import static queries.SQL_aux_functions.Make_begin_end_period_query;
import static queries.SQL_aux_functions.Make_specific_date_query;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
/**
 * Servlet implementation class PatientSelectionImpl
 */
public class PatientSelectionImpl extends HttpServlet implements XMLFileManager, CriterionManager{
	private static final long serialVersionUID = 1L;
	static ArrayList<String> cohort_names = new ArrayList<String>();
	private static List<JSONObject> inclusion_criteria = new ArrayList<JSONObject>();
	private static List<JSONObject> exclusion_criteria = new ArrayList<JSONObject>();	
	private static JSONObject criterionResponseInfos = new JSONObject();
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
	private static List<JSONObject> cohortResponseList = new ArrayList<JSONObject>();
	
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
		   			System.out.println(e);
		   		}
				
				break;	
			}
		}	
    	return narrowTerms;
    }
    
    private String getXMLS2(String requestID, String username, String password) {
    	 String myXML = "";
    	try {
	        String webPage = "https://private.harmonicss.eu/index.php/apps/coh/api/1.0/s2?darId="+requestID;

	        String authString = username + ":" + password;
	        System.out.println("auth string: " + authString);
	        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
	        String authStringEnc = new String(authEncBytes);
	        System.out.println("Base64 encoded auth string: " + authStringEnc);

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
	        myXML = jsonarray.getJSONObject(0).get("serviceConfig").toString();
	        
	        
	       /* System.out.println("*** BEGIN ***");
	        System.out.println(result);
	        System.out.println("*** END ***");*/
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return myXML;
    }

    @SuppressWarnings("unchecked")
	public String readXMLbyRequestID(String requestID, String username, String password){
  	  	cohort_names.clear();
  	  	String result_incl = "";
  	  	String result_excl = "";
  	  	
  	  	try {
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
		    String resp = getXMLS2(requestID,username,password).toString().replace("&lt;", "<").replace("&gt;", ">").replace("&quot;", "\"");
		    
		    System.out.println(resp);
		    
		    FileWriter fw = new FileWriter(getServletContext().getRealPath("/WEB-INF/Request"+requestID+".xml"));
		    fw.write(resp);
			fw.close();
		    
			File fXmlFile = new File(getServletContext().getRealPath("/WEB-INF/Request"+requestID+".xml"));
	    	jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
  	  		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
  	  		patientsSelectionRequest = ((JAXBElement<PatientsSelectionRequest>) jaxbUnmarshaller.unmarshal(fXmlFile)).getValue();

  	  		System.out.println(patientsSelectionRequest.getSessionID());
  	  		System.out.println(patientsSelectionRequest.getRequestDate());
  	  		System.out.println(patientsSelectionRequest.getUserID());
  	  		System.out.println(patientsSelectionRequest.getCohortID());
  	  		System.out.println(patientsSelectionRequest.getRequestTitle());
  	  		System.out.println(patientsSelectionRequest.getRequestID());
  	  		
  	  		for(org.ntua.criteria.Criterion inclCriterion: patientsSelectionRequest.getEligibilityCriteria().getInclusionCriteria().getCriterion()){
  	  			System.out.println(inclCriterion.getFormalExpression().get(0).getBooleanExpression().trim());
  	  			result_incl+=inclCriterion.getFormalExpression().get(0).getBooleanExpression().trim();
  	  		}
  	  		for(org.ntua.criteria.Criterion exclCriterion: patientsSelectionRequest.getEligibilityCriteria().getExclusionCriteria().getCriterion()){
	  			System.out.println(exclCriterion.getFormalExpression().get(0).getBooleanExpression().trim());
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
  	  
  		
  	   } catch (Exception e) {
  	      e.printStackTrace();
  	   }
  	   return result_incl+"XXX"+result_excl;
    }
    
    private String createQuery(ArrayList<Criterion> list_of_criterions, boolean incl, boolean mode, String results_of_all_Criterions) throws JSONException {
    	String results_of_one_Criterion="";
    	for(int i=0; i<list_of_criterions.size(); i++) {
    		criterionResponseInfos = new JSONObject();
			Criterion current_Criterion=list_of_criterions.get(i); //current_criterion
			if(!canUseCriterion(current_Criterion)){
				System.out.println("Criterion " + current_Criterion.getCriterion() + " cannot be used.");
				criterionResponseInfos.put("usage", "notused");
				criterionResponseInfos.put("notes", termAndSubterms);
				if(incl) inclusion_criteria.add(criterionResponseInfos);
				else exclusion_criteria.add(criterionResponseInfos);
				System.out.println(criterionResponseInfos);
				continue;
			}
			else System.out.println("Criterion " + current_Criterion.getCriterion() + " can be used.");
			criterionResponseInfos.put("usage", "used");
			criterionResponseInfos.put("notes", termAndSubterms);
			if(incl) inclusion_criteria.add(criterionResponseInfos);
			else exclusion_criteria.add(criterionResponseInfos);
			System.out.println(criterionResponseInfos);
			String query="";
			switch(current_Criterion.getCriterion()) { //The name of the Criterion.
			  
			case "demographics_gender": {//changed 12/5
				  query = "SELECT patient.UID " + 
						  "FROM patient, demo_sex_data, voc_sex " + 
						  "WHERE patient.ID=demo_sex_data.PATIENT_ID " + 
						  "AND demo_sex_data.SEX_ID=voc_sex.ID " + 
						  "AND " + Make_OR_of_CODES("voc_sex.CODE", ((demographics_gender) current_Criterion).gender); //Make_OR_of_CODES("","");
			  } break;
			    
			  case "demographics_ethnicity": { 
				   query = "SELECT patient.UID " + 
						   "FROM patient, demo_ethnicity_data, voc_ethnicity " + 
						   "WHERE patient.ID=demo_ethnicity_data.PATIENT_ID " + 
						   "AND demo_ethnicity_data.ETHNICITY_ID=voc_ethnicity.ID " + 
						   "AND " + Make_OR_of_CODES("voc_ethnicity.CODE", ((demographics_ethnicity) current_Criterion).ethnicity);

			  } break;
			    
			  case "demographics_education": {
				   query = "SELECT patient.UID " + 
						   "FROM patient, demo_education_level_data, voc_education_level " + 
						   "WHERE patient.ID=demo_education_level_data.PATIENT_ID " + 
						   "AND demo_education_level_data.EDUCATION_LEVEL_ID=voc_education_level.ID " + 
				   		   "AND " + Make_OR_of_CODES("voc_education_level.CODE", ((demographics_education) current_Criterion).education_level);
			  } break;
				    
			  case "demographics_weight": {
				  query = "SELECT patient.UID " + 
						  "FROM patient, demo_weight_data, voc_bmi_class " + 
				  		  "WHERE patient.ID=demo_weight_data.PATIENT_ID " + 
				  		  "AND demo_weight_data.BMI_CLASS_ID=voc_bmi_class.ID " + 
				  		  "AND " + Make_OR_of_CODES("voc_bmi_class.CODE", ((demographics_weight) current_Criterion).body_mass_index);
			  } break;
			  
			  case "demographics_occupation": {
				  query = "SELECT patient.UID " + 
						  "FROM patient, demo_occupation_data, voc_confirmation " + 
						  "WHERE patient.ID=demo_occupation_data.PATIENT_ID " + 
						  "AND demo_occupation_data.LOSS_OF_WORK_DUE_TO_PSS_ID = voc_confirmation.ID " + 
				  		  "AND " + Make_OR_of_CODES("voc_confirmation.CODE", ((demographics_occupation) current_Criterion).loss_of_work_due_to_pss);
			  } break;
				    
			  case "demographics_pregnancy": { //Check if user provided the info of all the fields 
				  demographics_pregnancy crit_demo_pregnancy_obj =  (demographics_pregnancy)current_Criterion;
				  String tables = "patient, demo_pregnancy_data, voc_pregnancy_outcome";
				  String where_clause = "patient.ID = demo_pregnancy_data.PATIENT_ID AND demo_pregnancy_data.OUTCOME_ID=voc_pregnancy_outcome.ID AND " + Make_OR_of_CODES("voc_pregnancy_outcome.CODE", crit_demo_pregnancy_obj.outcome_coded_value);
				  
				  if(!(crit_demo_pregnancy_obj.conception_exact_year + crit_demo_pregnancy_obj.conception_exact_month + 
				  	crit_demo_pregnancy_obj.conception_exact_day ).isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  where_clause += Make_specific_date_query(mode, "demo_pregnancy_data.CONCEPTION_DATE_ID","dt_date1",crit_demo_pregnancy_obj.getCONCEPTION_DATE_YEAR(),
							  crit_demo_pregnancy_obj.getCONCEPTION_DATE_MONTH(),crit_demo_pregnancy_obj.getCONCEPTION_DATE_DAY());					  		
				  
				  } else if(!(crit_demo_pregnancy_obj.getCONCEPTION_period_begin_year() + crit_demo_pregnancy_obj.getCONCEPTION_period_begin_month() + crit_demo_pregnancy_obj.getCONCEPTION_period_begin_day()).isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  where_clause += Make_begin_end_date_query (mode,"demo_pregnancy_data.CONCEPTION_DATE_ID", "dt_date1",crit_demo_pregnancy_obj.getCONCEPTION_period_begin_year(), crit_demo_pregnancy_obj.getCONCEPTION_period_begin_month(), crit_demo_pregnancy_obj.getCONCEPTION_period_begin_day(), crit_demo_pregnancy_obj.getCONCEPTION_period_end_year(), crit_demo_pregnancy_obj.getCONCEPTION_period_end_month(),
							  crit_demo_pregnancy_obj.getCONCEPTION_period_end_day());			  
				  
				  } else if(!(crit_demo_pregnancy_obj.getCONCEPTION_until_date_year() + crit_demo_pregnancy_obj.getCONCEPTION_until_date_month() + crit_demo_pregnancy_obj.getCONCEPTION_until_date_day()).isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  where_clause += Make_begin_end_date_query (mode,"demo_pregnancy_data.CONCEPTION_DATE_ID","dt_date1", "1800", "1", "1", crit_demo_pregnancy_obj.getCONCEPTION_until_date_year(), 
							  crit_demo_pregnancy_obj.getCONCEPTION_until_date_month(), crit_demo_pregnancy_obj.getCONCEPTION_until_date_day()); 
				  }
				  
				  if(!(crit_demo_pregnancy_obj.outcome_exact_year + crit_demo_pregnancy_obj.outcome_exact_month + 
				  	crit_demo_pregnancy_obj.outcome_exact_day).isEmpty()) {
					  tables += ", dt_date as dt_date2";
					  where_clause += Make_specific_date_query(mode, "demo_pregnancy_data.OUTCOME_DATE_ID","dt_date2", crit_demo_pregnancy_obj.getOUTCOME_DATE_YEAR(),
							  crit_demo_pregnancy_obj.getOUTCOME_DATE_MONTH(),crit_demo_pregnancy_obj.getOUTCOME_DATE_DAY());	
					 
				  	} else if(!(crit_demo_pregnancy_obj.getOUTCOME_period_begin_year() + crit_demo_pregnancy_obj.getOUTCOME_period_begin_month() + crit_demo_pregnancy_obj.getOUTCOME_period_begin_day()).isEmpty()) {
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
				  	
				  	if(incl) where_clause += " AND demo_pregnancy_data.STMT_ID=1";
					else where_clause += " AND demo_pregnancy_data.STMT_ID=2";
				  	
				  	query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;
				  	
			  } break;
			  
			  case "lifestyle_smoking": { //Check if user provided the info of all the fields 
				  lifestyle_smoking crit_lifestyle_smoking_obj =  (lifestyle_smoking)current_Criterion;
				  
				  String tables = "patient, lifestyle_smoking, voc_smoking_status";
				  String where_clause = "patient.ID = lifestyle_smoking.PATIENT_ID AND lifestyle_smoking.STATUS_ID = voc_smoking_status.ID AND " + Make_OR_of_CODES("voc_smoking_status.CODE", crit_lifestyle_smoking_obj.getVoc_smoking_status_CODE());  			  

				  if(!(crit_lifestyle_smoking_obj.getSmoking_exact_date_year()).isEmpty()) {
					  tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
					  where_clause += Make_begin_end_period_query (mode,"lifestyle_smoking.PERIOD_ID", "dt_date1", "dt_date2", crit_lifestyle_smoking_obj.getSmoking_exact_date_year(), 
							  crit_lifestyle_smoking_obj.getSmoking_exact_date_month(), crit_lifestyle_smoking_obj.getSmoking_exact_date_day(),
							  crit_lifestyle_smoking_obj.getSmoking_exact_date_year(), crit_lifestyle_smoking_obj.getSmoking_exact_date_month(),
							  crit_lifestyle_smoking_obj.getSmoking_exact_date_day()); 
					  	 							  
					} else if(!(crit_lifestyle_smoking_obj.getSmoking_period_end_year()).isEmpty()) {
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
					  where_clause += " AND lifestyle_smoking.AMOUNT_ID = dt_amount.ID AND dt_amount.value='" + crit_lifestyle_smoking_obj.getAmount_exact_value() + "' AND dt_amount.UNIT_ID=voc_unit.ID AND voc_unit.CODE ='" + crit_lifestyle_smoking_obj.getDt_amount_voc_unit_CODE() + "' ";
					  
				  }
				  
				  if(!crit_lifestyle_smoking_obj.getAmount_range_min_value().isEmpty()){
					  	tables += ", dt_amount, voc_unit";
					  where_clause += " AND lifestyle_smoking.AMOUNT_ID = dt_amount.ID AND dt_amount.value>=" + crit_lifestyle_smoking_obj.getAmount_range_min_value() + " AND dt_amount.UNIT_ID=voc_unit.ID AND voc_unit.CODE ='" + crit_lifestyle_smoking_obj.getDt_amount_voc_unit_CODE() + "'";
					  
				  }
				  
				  if(!crit_lifestyle_smoking_obj.getStatement().isEmpty()){
					  tables += ", voc_confirmation";
					  where_clause += " AND lifestyle_smoking.STMT_ID=voc_confirmation.ID AND voc_confirmation.CODE='" + crit_lifestyle_smoking_obj.getStatement() + "'";	
					 
				  }
				  
				  if(incl) where_clause += " AND lifestyle_smoking.STMT_ID=1";
				  else where_clause += " AND lifestyle_smoking.STMT_ID=2";
					  
				  query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;		
				  
				  //TODO 	will voc_unit.CODE take one value or more than one I also think that voc_direction_CODE() will take one value?
				  
				  //if(!crit_lifestyle_smoking_obj.getDt_period_of_time_voc_confirmation_CODE().isEmpty()) query += "AND dt_period_of_time.EXACT_ID = voc_confirmation.ID AND voc_confirmation.CODE='"+crit_lifestyle_smoking_obj.getDt_period_of_time_voc_confirmation_CODE()+"' "; 
				  //if(!crit_lifestyle_smoking_obj.getDt_period_of_time_BEFORE_PERIOD_ID().isEmpty()) query += "AND dt_period_of_time.BEFORE_PERIOD_ID = '"+crit_lifestyle_smoking_obj.getDt_period_of_time_BEFORE_PERIOD_ID()+"' ";
			  System.out.println("The Query is: "+ query);
			  } break;
			  
			  case "condition_symptom": { //Check if user provided the info of all the fields 
				  condition_symptom crit_cond_symptom_obj =  (condition_symptom)current_Criterion;
				  
				  String tables = "patient, cond_symptom, voc_symptom_sign";
				  String where_clause = "patient.ID = cond_symptom.PATIENT_ID AND cond_symptom.CONDITION_ID = voc_symptom_sign.ID AND (" + Make_OR_of_CODES("voc_symptom_sign.CODE", crit_cond_symptom_obj.getVoc_symptom_sign_CODE());
				  String narrowTerms = getTermsWithNarrowMeaning(crit_cond_symptom_obj.getVoc_symptom_sign_CODE());
					String[] allNarrowTerms = narrowTerms.split(",");
					for(int c=1; c<allNarrowTerms.length; c++) {
						where_clause += " OR " + Make_OR_of_CODES("voc_symptom_sign.CODE", allNarrowTerms[c]);
					}
					where_clause += ")";
				  /*query = "SELECT DISTINCT patient.UID " + 
						  "FROM patient, cond_symptom, voc_symptom_sign, dt_date, voc_direction, voc_confirmation " + 
						  "WHERE patient.ID = cond_symptom.PATIENT_ID " + 
						  "AND cond_symptom.CONDITION_ID = voc_symptom_sign.ID " +
						  "AND " + Make_OR_of_CODES("voc_symptom_sign.CODE", crit_cond_symptom_obj.getVoc_symptom_sign_CODE());*/
				  
				  if(!(crit_cond_symptom_obj.getObserve_exact_date_YEAR()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_specific_date_query(mode, "cond_symptom.OBSERVE_DATE_ID","dt_date",crit_cond_symptom_obj.getObserve_exact_date_YEAR(), //check cond_symptom.OBSERVE_DATE_ID
						crit_cond_symptom_obj.getObserve_exact_date_MONTH(), crit_cond_symptom_obj.getObserve_exact_date_DAY());
						 
				  }else if(!(crit_cond_symptom_obj.getObserve_period_end_year()  ).isEmpty()) {
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
					  where_clause += "AND cond_symptom.STMT_ID=voc_confirmation.ID " +
				  				 "AND voc_confirmation.CODE='"+crit_cond_symptom_obj.getStatement() + "'";
				  }
				  
				  if(incl) where_clause += " AND cond_symptom.STMT_ID=1";
				  else where_clause += " AND cond_symptom.STMT_ID=2";
				  
				  query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;
			  } break;
			  
			  case "condition_diagnosis": { //Check if user provided the info of all the fields 
				  condition_diagnosis condition_diagnosis_obj =  (condition_diagnosis)current_Criterion;
				  
				  String tables = "patient, cond_diagnosis, voc_medical_condition";
				  String where_clause = "patient.ID = cond_diagnosis.PATIENT_ID AND cond_diagnosis.CONDITION_ID = voc_medical_condition.ID AND (" + Make_OR_of_CODES("voc_medical_condition.CODE", condition_diagnosis_obj.getCondition());
				  String narrowTerms = getTermsWithNarrowMeaning(condition_diagnosis_obj.getCondition());
				  String[] allNarrowTerms = narrowTerms.split(",");
				  for(int c=1; c<allNarrowTerms.length; c++) {
					  where_clause += " OR " + Make_OR_of_CODES("voc_medical_condition.CODE", allNarrowTerms[c]);
				  }
				  where_clause += ")";
				  
				  if(!condition_diagnosis_obj.getStage().isEmpty()) {  // [OUTCOME_ASSESSMENT]
					  tables += ", voc_lymphoma_stage";
					  where_clause += " AND cond_diagnosis.STAGE_ID = voc_lymphoma_stage.ID AND " + Make_OR_of_CODES("voc_lymphoma_stage.CODE", condition_diagnosis_obj.getStage());
					  
					  	
				  }
				  
				  if(!condition_diagnosis_obj.getPerformance_status().isEmpty()) {  // [OUTCOME_ASSESSMENT]
					  tables += ", voc_performance_status";
					  where_clause += " AND cond_diagnosis.PERFORMANCE_STATUS_ID = voc_performance_status.ID AND " + Make_OR_of_CODES("voc_performance_status.CODE", condition_diagnosis_obj.getPerformance_status());
					  
				  }
				  
				  if(!(condition_diagnosis_obj.getDate_exact_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_specific_date_query(mode, "cond_diagnosis.DIAGNOSIS_DATE_ID","dt_date",condition_diagnosis_obj.getDate_exact_year(),
							  condition_diagnosis_obj.getDate_exact_month(),condition_diagnosis_obj.getDate_exact_day());
					 			  		
				  } else if(!(condition_diagnosis_obj.getDate_interval_start_year()).isEmpty()) {
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
				  		query += " AND cond_diagnosis.STMT_ID=voc_confirmation.ID " +
				  				 "AND voc_confirmation.CODE='"+condition_diagnosis_obj.getStatement() + "'";
				  }
				  if(incl) where_clause += " AND cond_diagnosis.STMT_ID=1";
				  else where_clause += " AND cond_diagnosis.STMT_ID=2";
				  
				  query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;
			  } break;
			  
			  case "intervention_medication": { //Check if user provided the info of all the fields 
				  intervention_medication  crit_interv_medication_obj =  (intervention_medication )current_Criterion;
				  String tables = "patient, interv_medication, voc_pharm_drug";
				  String where_clause = "patient.ID = interv_medication.PATIENT_ID AND interv_medication.MEDICATION_ID = voc_pharm_drug.ID AND (" + Make_OR_of_CODES("voc_pharm_drug.CODE", crit_interv_medication_obj.getVoc_pharm_drug_CODE());
				  String narrowTerms = getTermsWithNarrowMeaning(crit_interv_medication_obj.getVoc_pharm_drug_CODE());
				  String[] allNarrowTerms = narrowTerms.split(",");
				  for(int c=1; c<allNarrowTerms.length; c++) {
					  where_clause += " OR " + Make_OR_of_CODES("voc_pharm_drug.CODE", allNarrowTerms[c]);
				  }
				  where_clause += ")";
				  
				  //if(!crit_interv_medication_obj.getVoc_pharm_drug_BROADER_TERM_ID().isEmpty()) query += "AND voc_pharm_drug.BROADER_TERM_ID = '"+crit_interv_medication_obj.getVoc_pharm_drug_BROADER_TERM_ID()+"' "; //Do we need the Broader_Term_ID? (`BROADER_TERM_ID`) REFERENCES `voc_pharm_drug` (`ID`)
					  
				  if(!crit_interv_medication_obj.getDosage_amount_exact_value().isEmpty()) {
					  tables += ", dt_amount";
					  where_clause += " AND interv_medication.DOSSAGE_ID = dt_amount.ID AND dt_amount.VALUE ='" +crit_interv_medication_obj.getDosage_amount_exact_value()+"'";
				  }
				  
				  if(!(crit_interv_medication_obj.getMedication_exact_date_year()).isEmpty()) {	
					  tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
					  where_clause += Make_begin_end_period_query (mode,"interv_medication.PERIOD_ID", "dt_date1", "dt_date2", crit_interv_medication_obj.getMedication_exact_date_year(), 
					  			crit_interv_medication_obj.getMedication_exact_date_month(), crit_interv_medication_obj.getMedication_exact_date_day(),
					  			crit_interv_medication_obj.getMedication_exact_date_year(), crit_interv_medication_obj.getMedication_exact_date_month(),
					  			crit_interv_medication_obj.getMedication_exact_date_day());
							  
				  } else if(!(crit_interv_medication_obj.getMedication_period_end_year()).isEmpty()) {
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
					  where_clause += "AND interv_medication.STMT_ID=voc_confirmation.ID AND voc_confirmation.CODE='"+crit_interv_medication_obj.getStatement() + "'";
				  }
				  
				  if(incl) where_clause += " AND interv_medication.STMT_ID=1";
				  else where_clause += " AND interv_medication.STMT_ID=2";
				  
				  query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;
					
			  } break;
			  
			  case "intervention_chemotherapy": { //Check if user provided the info of all the fields 
				  intervention_chemotherapy  crit_interv_chemotherapy_obj =  (intervention_chemotherapy)current_Criterion;
				  String tables = "patient, interv_chemotherapy, voc_confirmation AS conf_1";
				  String where_clause = "patient.ID = interv_chemotherapy.PATIENT_ID AND interv_chemotherapy.DUE_TO_PSS_ID = conf_1.ID AND " + Make_OR_of_CODES("conf_1.CODE", crit_interv_chemotherapy_obj.getReason());
				 
				  
				  if(!(crit_interv_chemotherapy_obj.getChem_exact_date_year()).isEmpty()) {	
					  tables += ", dt_date AS dt_date1, dt_date AS dt_date2, dt_period_of_time";
					  where_clause += Make_begin_end_period_query (mode,"interv_chemotherapy.PERIOD_ID", "dt_date1", "dt_date2", crit_interv_chemotherapy_obj.getChem_exact_date_year(), 
					  			crit_interv_chemotherapy_obj.getChem_exact_date_month(), crit_interv_chemotherapy_obj.getChem_exact_date_day(),
					  			crit_interv_chemotherapy_obj.getChem_exact_date_year(), crit_interv_chemotherapy_obj.getChem_exact_date_month(),
					  			crit_interv_chemotherapy_obj.getChem_exact_date_day()); 						  
					} else if(!(crit_interv_chemotherapy_obj.getChem_period_end_year()).isEmpty()) {
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
				  
				  if(incl) where_clause += " AND interv_chemotherapy.STMT_ID=1";
				  else where_clause += " AND interv_chemotherapy.STMT_ID=2";
				  
				  query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;
					//results_of_one_Criterion=DBServiceCRUD.getDataFromDB(query); 
					//System.out.println("We executed: "+crit_interv_chemotherapy_obj.criterion_name+"\nThe Query is: "+query); 
			  } break;
			  
			  case "intervention_surgery": { //Check if user provided the info of all the fields 
				  intervention_surgery  crit_interv_surgery_obj =  (intervention_surgery)current_Criterion;
				  
				  String tables = "patient, interv_Surgery, voc_confirmation AS conf_1";
				  String where_clause = "patient.ID = interv_Surgery.PATIENT_ID AND interv_Surgery.DUE_TO_PSS_ID = conf_1.ID AND conf_1.CODE= '"+crit_interv_surgery_obj.getReason()+"'";
				  
				  if(!(crit_interv_surgery_obj.getSurgery_exact_date_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_specific_date_query(mode, "interv_surgery.SURGERY_DATE_ID","dt_date",crit_interv_surgery_obj.getSurgery_exact_date_year(), //check cond_symptom.OBSERVE_DATE_ID
							  crit_interv_surgery_obj.getSurgery_exact_date_month(), crit_interv_surgery_obj.getSurgery_exact_date_day());
					  
				  } else if(!(crit_interv_surgery_obj.getSurgery_period_end_year()).isEmpty()) {
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
				  
				  if(incl) where_clause += " AND interv_surgery.STMT_ID=1";
				  else where_clause += " AND interv_surgery.STMT_ID=2";
				  
				  query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;
			  } break;
			  
			  case "examination_lab_test": { //Check if user provided the info of all the fields 
				  examination_lab_test  examination_lab_test_obj =  (examination_lab_test)current_Criterion;
				  
				  String tables = "patient, exam_lab_test, voc_lab_test";
				  String where_clause = "patient.ID = exam_lab_test.PATIENT_ID AND exam_lab_test.TEST_ID=voc_lab_test.ID AND (" + Make_OR_of_CODES("voc_lab_test.CODE", examination_lab_test_obj.getTest_id());
				  
				  String narrowTerms = getTermsWithNarrowMeaning(examination_lab_test_obj.getTest_id());
				  String[] allNarrowTerms = narrowTerms.split(",");
				  for(int c=1; c<allNarrowTerms.length; c++) {
					  where_clause += " OR " + Make_OR_of_CODES("voc_lab_test.CODE", allNarrowTerms[c]);  //" + Make_OR_of_CODES("voc_pharm_drug.CODE", allNarrowTerms[c]);
				  }
				  where_clause += ") ";
					
				  /*query = "SELECT DISTINCT patient.UID " +
						  "FROM patient, exam_lab_test, voc_lab_test " +//, exam_lab_test, voc_lab_test, dt_amount, voc_unit, voc_assessment, dt_amount_range, dt_date " + //, voc_direction interv_Surgery, dt_date, voc_direction, voc_confirmation
						  "WHERE patient.ID = exam_lab_test.PATIENT_ID AND " + 
						  "exam_lab_test.TEST_ID=voc_lab_test.ID " +
						  //"voc_lab_test.CODE='"+crit_exam_lab_test_obj.getTEST_ID_voc_lab_test_CODE()+"' ";
				  		  "AND " + Make_OR_of_CODES("voc_lab_test.CODE", examination_lab_test_obj.getTest_id());  // [TEST_ID]
				  System.out.println("We received: "+examination_lab_test_obj.getNormal_range_value());*/
				  
				  if(!examination_lab_test_obj.getOutcome_amount_exact_value().isEmpty()) { // [OUTCOME_AMOUNT]
					  tables += ", dt_amount, voc_unit";
					  where_clause += " AND exam_lab_test.OUTCOME_AMOUNT_ID = dt_amount.ID AND dt_amount.VALUE = '"+examination_lab_test_obj.getOutcome_amount_exact_value()+"' " +
							  	"AND dt_amount.UNIT_ID=voc_unit.ID " +
							  	"AND voc_unit.CODE ='"+examination_lab_test_obj.getOutcome_amount_unit()+"' ";
					  
				  } else
				  
				  if(!examination_lab_test_obj.getOutcome_amount_range_min_value().isEmpty()&&!examination_lab_test_obj.getOutcome_amount_range_max_value().isEmpty()) { // [OUTCOME_AMOUNT]
					  tables += ", dt_amount, voc_unit";	
					  where_clause += " AND exam_lab_test.OUTCOME_AMOUNT_ID = dt_amount.ID AND dt_amount.VALUE < '"+examination_lab_test_obj.getOutcome_amount_range_max_value()+"' " +
					  	"AND dt_amount.VALUE > '"+examination_lab_test_obj.getOutcome_amount_range_min_value()+"' " +
					  	"AND dt_amount.UNIT_ID=voc_unit.ID " +
					  	"AND voc_unit.CODE ='"+examination_lab_test_obj.getOutcome_amount_unit()+"' ";
				  } else
					  
				  if(examination_lab_test_obj.getOutcome_amount_range_min_value().isEmpty()&&!examination_lab_test_obj.getOutcome_amount_range_max_value().isEmpty()) { // [OUTCOME_AMOUNT]
					  tables += ", dt_amount, voc_unit";	
					  where_clause += " AND exam_lab_test.OUTCOME_AMOUNT_ID = dt_amount.ID AND dt_amount.VALUE < '"+examination_lab_test_obj.getOutcome_amount_range_max_value()+"' " +
					  	"AND dt_amount.UNIT_ID=voc_unit.ID " +
					  	"AND voc_unit.CODE ='"+examination_lab_test_obj.getOutcome_amount_unit()+"' ";
				  } else
					  
				  if(!examination_lab_test_obj.getOutcome_amount_range_min_value().isEmpty()&&examination_lab_test_obj.getOutcome_amount_range_max_value().isEmpty()) { // [OUTCOME_AMOUNT]
					  tables += ", dt_amount, voc_unit";
					  where_clause += " AND exam_lab_test.OUTCOME_AMOUNT_ID = dt_amount.ID " + //AND dt_amount.VALUE < '"+examination_lab_test_obj.getOutcome_amount_range_max_value()+"' 
					  	"AND dt_amount.VALUE > '"+examination_lab_test_obj.getOutcome_amount_range_min_value()+"' " +
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
					  if (outcome_term=="CONFIRM-01"||outcome_term=="CONFIRM-02") {
						  tables += ", voc_confirmation";
						  where_clause += " AND exam_lab_test.OUTCOME_TERM_ID = voc_confirmation.ID " +
								    "AND voc_confirmation.CODE='"+examination_lab_test_obj.getOutcome_term() + "'";
						  		//= " +examination_lab_test_obj.getOutcome_term() +" ";
					  }
					  else {
						  switch (examination_lab_test_obj.getTest_id()) {
						  /*case "BLOOD-310":
						    System.out.println("BLOOD-310");
						    query += "AND exam_lab_test.OUTCOME_TERM_ID = voc_cryo_type.ID " +
									  "AND voc_cryo_type.CODE='"+examination_lab_test_obj.getOutcome_term() + "'";
						    break;
						  case "BLOOD-311":
							System.out.println("BLOOD-310");
							query += "AND exam_lab_test.OUTCOME_TERM_ID = voc_cryo_type.ID " +
							"AND voc_cryo_type.CODE='"+examination_lab_test_obj.getOutcome_term() + "'";
						    break;*/
						  case "BLOOD-312":
							  tables += ", voc_cryo_type";
							  System.out.println("BLOOD-310"); //voc_ana_pattern
							  where_clause += " AND exam_lab_test.OUTCOME_TERM_ID = voc_cryo_type.ID " +
							  "AND voc_cryo_type.CODE='"+examination_lab_test_obj.getOutcome_term() + "'";
						    break;
						  case "BLOOD-521":
							  tables += ", voc_ana_pattern";
							  System.out.println("BLOOD-521");
							  where_clause += " AND exam_lab_test.OUTCOME_TERM_ID = voc_ana_pattern.ID " +
							  "AND voc_ana_pattern.CODE='"+examination_lab_test_obj.getOutcome_term() + "'";
						    break;
						  /*case "BLOOD-522":
							  tables += ", voc_ana_pattern";
							  System.out.println("BLOOD-522");
							  where_clause += " AND exam_lab_test.OUTCOME_TERM_ID = voc_ana_pattern.ID " +
							  "AND voc_ana_pattern.CODE='"+examination_lab_test_obj.getOutcome_term() + "'";
						    break;
						  case "BLOOD-523":
							  query=query.replace("FROM patient","FROM patient, voc_ana_pattern");
						    System.out.println("BLOOD-523"); //voc_ana_pattern
						    query += "AND exam_lab_test.OUTCOME_TERM_ID = voc_ana_pattern.ID " +
							" AND voc_ana_pattern.CODE='"+examination_lab_test_obj.getOutcome_term() + "'";
						    break;*/
					  }
					  
					}
					  
					  	//query += "AND exam_lab_test.OUTCOME_TERM_ID = " +examination_lab_test_obj.getOutcome_term() +" ";
				  };
				  
/*					  if(!(examination_lab_test_obj.getSample_period_of_time_interval_start_year() + examination_lab_test_obj.getSample_period_of_time_interval_start_month() + 
						  examination_lab_test_obj.getSample_period_of_time_interval_start_day()).isEmpty()) {
				 		query += "AND exam_lab_test.SAMPLE_DATE_ID = dt_date.ID ";
				 		if(!examination_lab_test_obj.getSample_period_of_time_interval_start_year().isEmpty()) query += "AND dt_date.YEAR='"+examination_lab_test_obj.getSample_period_of_time_interval_start_year()+"' ";
				 		if(!examination_lab_test_obj.getSample_period_of_time_interval_start_month().isEmpty()) query += "AND dt_date.MONTH='"+examination_lab_test_obj.getSample_period_of_time_interval_start_month()+"' ";
				 		if(!examination_lab_test_obj.getSample_period_of_time_interval_start_day().isEmpty()) query += "AND dt_date.DAY='"+examination_lab_test_obj.getSample_period_of_time_interval_start_day()+"' ";
				 		//if(!examination_lab_test_obj.getSTART_DATE_voc_direction_CODE().isEmpty()) query += "AND dt_date.OP_ID= voc_direction.ID " +
				 		//"AND voc_direction.CODE='"+examination_lab_test_obj.getSTART_DATE_voc_direction_CODE()+"' ";
				 		//if(true) query += "AND dt_date.BEFORE_DATE_ID='" + Crit_lifestyle_smoking_obj.getSTART_DATE_dt_date_BEFORE_DATE_ID()+"' ";
				 	} */
				  if(!(examination_lab_test_obj.getSample_period_of_time_exact_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_specific_date_query(mode, "exam_lab_test.SAMPLE_DATE_ID","dt_date",examination_lab_test_obj.getSample_period_of_time_exact_year(), 
							  			examination_lab_test_obj.getSample_period_of_time_exact_month(), examination_lab_test_obj.getSample_period_of_time_exact_day());					  		
				  } 
				  
				  else if(!(examination_lab_test_obj.getSample_period_of_time_interval_end_year()).isEmpty()) {
							 
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
				  
				  
				  
/*					  if(!(examination_lab_test_obj.getSample_period_of_time_exact_year()).isEmpty()) {						  
					  	query += Make_begin_end_period_query (mode,"exam_lab_test.SAMPLE_DATE_ID", "dt_date1", "dt_date2", examination_lab_test_obj.getSample_period_of_time_exact_year(), 
					  			examination_lab_test_obj.getSample_period_of_time_exact_month(), examination_lab_test_obj.getSample_period_of_time_exact_day(),
					  			examination_lab_test_obj.getSample_period_of_time_exact_year(), 
					  			examination_lab_test_obj.getSample_period_of_time_exact_month(), examination_lab_test_obj.getSample_period_of_time_exact_day()); 							  
					} else if(!(examination_lab_test_obj.getSample_period_of_time_interval_end_year()).isEmpty()) {						  
						query += Make_begin_end_period_query (mode,"exam_lab_test.SAMPLE_DATE_ID", "dt_date1", "dt_date2", examination_lab_test_obj.getSample_period_of_time_interval_start_year(), 
								examination_lab_test_obj.getSample_period_of_time_interval_start_month(), examination_lab_test_obj.getSample_period_of_time_interval_start_day(),
								examination_lab_test_obj.getSample_period_of_time_interval_end_year(), examination_lab_test_obj.getSample_period_of_time_interval_end_month(),
								examination_lab_test_obj.getSample_period_of_time_interval_end_day()); 												
					} else if(!(examination_lab_test_obj.getSample_period_of_time_until_year()).isEmpty()) {						  
						query += Make_begin_end_period_query (mode,"exam_lab_test.SAMPLE_DATE_ID", "dt_date1", "dt_date2", "1800", 
								  "1", "1",examination_lab_test_obj.getSample_period_of_time_until_year(), examination_lab_test_obj.getSample_period_of_time_until_month(),
								  examination_lab_test_obj.getSample_period_of_time_until_day()); 								
					}*/
				  
					//results_of_one_Criterion=DBServiceCRUD.getDataFromDB(query); 
					//System.out.println("We executed: "+crit_exam_lab_test_obj.criterion_name+"\nThe Query is: "+query); 
				  query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;
			  } break; 
			  
			  case "examination_biopsy": { //Check if user provided the info of all the fields 
				  examination_biopsy  examination_biopsy_obj =  (examination_biopsy)current_Criterion;
				  
				  String tables = "patient, exam_biopsy, voc_biopsy";
				  String where_clause = "patient.ID = exam_biopsy.PATIENT_ID AND exam_biopsy.BIOPSY_ID=voc_biopsy.ID AND (voc_biopsy.CODE='"+examination_biopsy_obj.getBiopsy_type()+"' ";
				  String myNarrowTerms = getTermsWithNarrowMeaning(examination_biopsy_obj.getBiopsy_type());
					String[] myAllNarrowTerms = myNarrowTerms.split(",");
					for(int c=1; c<myAllNarrowTerms.length; c++) {
						where_clause += " OR voc_biopsy.CODE='" + myAllNarrowTerms[c] + "'";
					}
					where_clause += ") ";
				  
				  if(!(examination_biopsy_obj.getTest_id()).isEmpty()) {
					  tables += ", voc_lab_test";
					  where_clause += "AND exam_biopsy.TEST_ID = voc_lab_test.ID AND (voc_lab_test.CODE = '"+examination_biopsy_obj.getTest_id() +"'"; //'BLOOD-100'
					  String narrowTerms = getTermsWithNarrowMeaning(examination_biopsy_obj.getTest_id());
					  String[] allNarrowTerms = narrowTerms.split(",");
					  for(int c=1; c<allNarrowTerms.length; c++) {
						  where_clause += " OR voc_lab_test.CODE = '"+ allNarrowTerms[c] +"'";  //" + Make_OR_of_CODES("voc_pharm_drug.CODE", allNarrowTerms[c]);
					  }
					  where_clause += ") ";
				  };  
				  
			  
				  if(!examination_biopsy_obj.getOutcome_amount_exact_value().isEmpty()) { // [OUTCOME_AMOUNT]
					  tables += ", dt_amount"; //, voc_unit";
					  where_clause += "AND exam_biopsy.OUTCOME_AMOUNT_ID = dt_amount.ID AND dt_amount.VALUE = '"+examination_biopsy_obj.getOutcome_amount_exact_value()+"' " /* +
					  	"AND dt_amount.UNIT_ID=voc_unit.ID " +
					  	"AND voc_unit.CODE ='"+examination_biopsy_obj.getOutcome_amount_unit()+"' "*/;
				  } else
				  
				  if(!examination_biopsy_obj.getOutcome_amount_range_min_value().isEmpty()&&!examination_biopsy_obj.getOutcome_amount_range_max_value().isEmpty()) { // [OUTCOME_AMOUNT]
					  tables += ", dt_amount"; //, voc_unit";
					  where_clause += "AND exam_biopsy.OUTCOME_AMOUNT_ID = dt_amount.ID AND dt_amount.VALUE < '"+examination_biopsy_obj.getOutcome_amount_range_max_value()+"' " +
					  	"AND dt_amount.VALUE > '"+examination_biopsy_obj.getOutcome_amount_range_min_value()+"' "/* +
					  	"AND dt_amount.UNIT_ID=voc_unit.ID " +
					  	"AND voc_unit.CODE ='"+examination_biopsy_obj.getOutcome_amount_unit()+"' "*/;
				  } else
					  
				  if(examination_biopsy_obj.getOutcome_amount_range_min_value().isEmpty()&&!examination_biopsy_obj.getOutcome_amount_range_max_value().isEmpty()) { // [OUTCOME_AMOUNT]
					  tables += ", dt_amount"; //, voc_unit";	
					  where_clause += "AND exam_biopsy.OUTCOME_AMOUNT_ID = dt_amount.ID AND dt_amount.VALUE < '"+examination_biopsy_obj.getOutcome_amount_range_max_value()+"' "/* +
					  	//"AND dt_amount.VALUE > '"+examination_lab_test_obj.getOutcome_amount_range_min_value()+"' " +
					  	"AND dt_amount.UNIT_ID=voc_unit.ID " +
					  	"AND voc_unit.CODE ='"+examination_biopsy_obj.getOutcome_amount_unit()+"' "*/;
					  	System.out.println("Mpikame max value amount.");
				  } else
					  
				  if(!examination_biopsy_obj.getOutcome_amount_range_min_value().isEmpty()&&examination_biopsy_obj.getOutcome_amount_range_max_value().isEmpty()) { // [OUTCOME_AMOUNT]
					  tables += ", dt_amount"; //, voc_unit";	
					  where_clause += "AND exam_biopsy.OUTCOME_AMOUNT_ID = dt_amount.ID " + //AND dt_amount.VALUE < '"+examination_lab_test_obj.getOutcome_amount_range_max_value()+"' 
					  	"AND dt_amount.VALUE > '"+examination_biopsy_obj.getOutcome_amount_range_min_value()+"' "/* +
					  	"AND dt_amount.UNIT_ID=voc_unit.ID " +
					  	"AND voc_unit.CODE ='"+examination_biopsy_obj.getOutcome_amount_unit()+"' "*/;
				  }
					  					  
				  //TODO NORMAL_RANGE  does it check if the value belongs in these two limits.		  
				  if(!(examination_biopsy_obj.getNormal_range_value()).isEmpty()) {
					  tables += ", dt_amount_range, voc_unit";
					  where_clause += "AND exam_biopsy.NORMAL_RANGE_ID = dt_amount_range.ID " +
					  	"AND dt_amount_range.VALUE1 <= '"+examination_biopsy_obj.getNormal_range_value()+"' " +
					  	"AND dt_amount_range.VALUE2 >= '"+examination_biopsy_obj.getNormal_range_value()+"' " +
					    "AND dt_amount_range.UNIT_ID = voc_unit.ID AND voc_unit.CODE='"+examination_biopsy_obj.getOutcome_amount_unit()+"' ";
				  };
				  
				  if(!examination_biopsy_obj.getAssessment().isEmpty()) {  // [OUTCOME_ASSESSMENT]
					  tables += ", voc_assessment";
					  where_clause += "AND exam_biopsy.ASSESSMENT_ID = voc_assessment.ID " + 
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
						  } else if(!(examination_biopsy_obj.getBiopsy_period_of_time_interval_end_year()).isEmpty()) {
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
				  
				  query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;

					//results_of_one_Criterion=DBServiceCRUD.getDataFromDB(query); 
					//System.out.println("We executed: "+crit_exam_lab_test_obj.criterion_name+"\nThe Query is: "+query); 
			  } break; //examination_medical_imaging_test
			  
			  case "examination_medical_imaging_test": { //Check if user provided the info of all the fields 
				  examination_medical_imaging_test  examination_medical_imaging_test_obj =  (examination_medical_imaging_test)current_Criterion;
				  
				  String tables = "exam_medical_imaging_test, patient, voc_medical_imaging_test";
				  String where_clause = "patient.ID = exam_medical_imaging_test.PATIENT_ID AND exam_medical_imaging_test.TEST_ID=voc_medical_imaging_test.ID AND (" + Make_OR_of_CODES("voc_medical_imaging_test.CODE", examination_medical_imaging_test_obj.getTest_id()) +" ";
				  
				  String myNarrowTerms = getTermsWithNarrowMeaning(examination_medical_imaging_test_obj.getTest_id());
				  String[] myAllNarrowTerms = myNarrowTerms.split(",");
				  for(int c=1; c<myAllNarrowTerms.length; c++) {
					  where_clause += "OR " + Make_OR_of_CODES("voc_medical_imaging_test.CODE", myAllNarrowTerms[c]);
				  }
				  where_clause += ") ";
				  
				  if(!examination_medical_imaging_test_obj.getAssessment().isEmpty()) {  // [OUTCOME_ASSESSMENT]
					  tables += ", voc_assessment";
					  where_clause += "AND exam_medical_imaging_test.ASSESSMENT_ID = voc_assessment.ID " + 
					  	//"AND voc_assessment.CODE ='"+crit_exam_lab_test_obj.OUTCOME_ASSESSMENT_ID_voc_assessment_CODE+"' ";
					  	"AND (" + Make_OR_of_CODES("voc_assessment.CODE", examination_medical_imaging_test_obj.getAssessment());
					  String narrowTerms = getTermsWithNarrowMeaning(examination_medical_imaging_test_obj.getAssessment());
					  String[] allNarrowTerms = narrowTerms.split(",");
					  for(int c=1; c<allNarrowTerms.length; c++) {
						  where_clause += " OR " + Make_OR_of_CODES("voc_assessment.CODE", allNarrowTerms[c]);
					  }
					  where_clause += ")";
				  }
				  
				  if(!(examination_medical_imaging_test_obj.getTest_period_of_time_exact_year()).isEmpty()) {
					  tables += ", exam_lab_test, dt_date";
					  where_clause += Make_specific_date_query(mode, "exam_lab_test.BIOPSY_DATE_ID","dt_date",examination_medical_imaging_test_obj.getTest_period_of_time_exact_year(), 
							  examination_medical_imaging_test_obj.getTest_period_of_time_exact_month(), examination_medical_imaging_test_obj.getTest_period_of_time_exact_day());					  		
				  } else if(!(examination_medical_imaging_test_obj.getTest_period_of_time_interval_end_year()).isEmpty()) {
					  tables += ", exam_lab_test, dt_date";
					  where_clause += Make_begin_end_date_query (mode,"exam_lab_test.BIOPSY_DATE_ID", "dt_date",examination_medical_imaging_test_obj.getTest_period_of_time_interval_start_year(), 
							  examination_medical_imaging_test_obj.getTest_period_of_time_interval_start_month(), examination_medical_imaging_test_obj.getTest_period_of_time_interval_start_day(),
							  examination_medical_imaging_test_obj.getTest_period_of_time_interval_end_year(), examination_medical_imaging_test_obj.getTest_period_of_time_interval_end_month(),
							  examination_medical_imaging_test_obj.getTest_period_of_time_interval_end_day()); 			  
				  } else if(!(examination_medical_imaging_test_obj.getTest_period_of_time_until_year()).isEmpty()) {
					  tables += ", exam_lab_test, dt_date";
					  where_clause += Make_begin_end_date_query (mode,"exam_lab_test.BIOPSY_DATE_ID","dt_date", "1800", "1", "1", examination_medical_imaging_test_obj.getTest_period_of_time_until_year(), examination_medical_imaging_test_obj.getTest_period_of_time_until_month(),
							  examination_medical_imaging_test_obj.getTest_period_of_time_until_day()); 
				  }
				  
				  query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;

					//results_of_one_Criterion=DBServiceCRUD.getDataFromDB(query); 
					//System.out.println("We executed: "+crit_exam_lab_test_obj.criterion_name+"\nThe Query is: "+query); 
			  } break;
			  case "examination_questionnaire_score": { //Check if user provided the info of all the fields 
				  examination_questionnaire_score  examination_questionnaire_score_obj =  (examination_questionnaire_score)current_Criterion;
				  
				  String tables = "patient, exam_questionnaire_score, voc_questionnaire";
				  String where_clause = "patient.ID = exam_questionnaire_score.PATIENT_ID AND exam_questionnaire_score.SCORE_ID=voc_questionnaire.ID AND (" + Make_OR_of_CODES("voc_questionnaire.CODE", examination_questionnaire_score_obj.getScore());
				  
				  String myNarrowTerms = getTermsWithNarrowMeaning(examination_questionnaire_score_obj.getScore());
				  String[] myAllNarrowTerms = myNarrowTerms.split(",");
				  for(int c=1; c<myAllNarrowTerms.length; c++) {
					  where_clause += " OR " + Make_OR_of_CODES("voc_questionnaire.CODE", myAllNarrowTerms[c]);
				  }
				  where_clause += ")";
				  
				  if(!examination_questionnaire_score_obj.getValue().isEmpty()) {  //TODO check value
					  where_clause += " AND " + Make_OR_of_CODES("exam_questionnaire_score.VALUE", examination_questionnaire_score_obj.getValue());
				  }
				  
				  if(!examination_questionnaire_score_obj.getAssessment().isEmpty()) {  // [OUTCOME_ASSESSMENT]
					  tables += ", voc_assessment";
					  where_clause += " AND exam_questionnaire_score.ASSESSMENT_ID = voc_assessment.ID " + 
					  	//"AND voc_assessment.CODE ='"+crit_exam_lab_test_obj.OUTCOME_ASSESSMENT_ID_voc_assessment_CODE+"' ";
					  	"AND (" + Make_OR_of_CODES("voc_assessment.CODE", examination_questionnaire_score_obj.getAssessment());
					  String narrowTerms = getTermsWithNarrowMeaning(examination_questionnaire_score_obj.getAssessment());
					  String[] allNarrowTerms = narrowTerms.split(",");
					  for(int c=1; c<allNarrowTerms.length; c++) {
						  where_clause += " OR " + Make_OR_of_CODES("voc_assessment.CODE", allNarrowTerms[c]);
					  }
					  where_clause += ")";
				  }
				  
				  //TODO NORMAL_RANGE  does it check if the value belongs in these two limits.		  
				  if(!(examination_questionnaire_score_obj.getNormal_range_value()).isEmpty()) {
					  tables += ", exam_lab_test, dt_amount_range, voc_unit";
					  where_clause += " AND exam_lab_test.NORMAL_RANGE_ID = dt_amount_range.ID " +
					  	"AND dt_amount_range.VALUE1 <= '"+examination_questionnaire_score_obj.getNormal_range_value()+"' " +
					  	"AND dt_amount_range.VALUE2 >= '"+examination_questionnaire_score_obj.getNormal_range_value()+"' " +
					    "AND dt_amount_range.UNIT_ID = voc_unit.ID AND voc_unit.CODE='"+examination_questionnaire_score_obj.getUnit()+"' ";
				  };
				  
				  if(!examination_questionnaire_score_obj.getOther_term().isEmpty()) {  //TODO check value
					  where_clause += " AND " + Make_OR_of_CODES("exam_questionnaire_score.OTHER_TERM_ID", examination_questionnaire_score_obj.getOther_term());
				  }
				  
				  				  
				  if(!(examination_questionnaire_score_obj.getQuestionnaire_period_of_time_exact_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_specific_date_query(mode, "exam_questionnaire_score.QUESTIONNAIRE_DATE_ID","dt_date",examination_questionnaire_score_obj.getQuestionnaire_period_of_time_exact_year(), 
							  examination_questionnaire_score_obj.getQuestionnaire_period_of_time_exact_month(), examination_questionnaire_score_obj.getQuestionnaire_period_of_time_exact_day());					  		
				  } else if(!(examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_end_year()).isEmpty()) {
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
				  query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;
				  
			  } break;  //examination_essdai_domain
			  
			  case "examination_essdai_domain": { //Check if user provided the info of all the fields 
				  examination_essdai_domain  examination_essdai_domain_obj =  (examination_essdai_domain)current_Criterion;
				  
				  String tables = "patient, exam_essdai_domain, voc_essdai_domain";
				  String where_clause = "patient.ID = exam_essdai_domain.PATIENT_ID AND exam_essdai_domain.DOMAIN_ID = voc_essdai_domain.ID AND " + Make_OR_of_CODES("voc_essdai_domain.CODE", examination_essdai_domain_obj.getDomain());
				  
				  /*query = "SELECT DISTINCT patient.UID " +
						  "FROM patient, exam_essdai_domain, voc_essdai_domain, dt_date, voc_activity_level " + 
						  "WHERE patient.ID = exam_essdai_domain.PATIENT_ID AND " + 
						  "exam_essdai_domain.DOMAIN_ID = voc_essdai_domain.ID " +
						  //"voc_essdai_domain.CODE = '"+examination_essdai_domain_obj.getDomain()+"' ";
				  		  "AND " + Make_OR_of_CODES("voc_essdai_domain.CODE", examination_essdai_domain_obj.getDomain());*/
				 
			  
				  if(!(examination_essdai_domain_obj.getActivity_level()).isEmpty()) {
					  tables += ", voc_activity_level";
					  where_clause += " AND exam_essdai_domain.ACTIVITY_LEVEL_ID = voc_activity_level.ID " +
					  	"AND voc_activity_level.CODE = '" + examination_essdai_domain_obj.getActivity_level() +"' "; //'BLOOD-100'
				  };
				  
				  if(!(examination_essdai_domain_obj.getQuestionnaire_period_of_time_exact_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_specific_date_query(mode, "exam_essdai_domain.QUESTIONNAIRE_DATE_ID","dt_date",examination_essdai_domain_obj.getQuestionnaire_period_of_time_exact_year(), 
							  examination_essdai_domain_obj.getQuestionnaire_period_of_time_exact_month(), examination_essdai_domain_obj.getQuestionnaire_period_of_time_exact_day());					  		
				  } else if(!(examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_end_year()).isEmpty()) {
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
				  query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;
			  } break; //examination_caci_condition
			  
			  case "examination_caci_condition": { //Check if user provided the info of all the fields 
				  examination_caci_condition  examination_caci_condition_obj =  (examination_caci_condition)current_Criterion;
				  
				  String tables = "exam_caci_condition, patient, voc_caci_condition";
				  String where_clause = "patient.ID = exam_caci_condition.PATIENT_ID AND exam_caci_condition.CACI_ID = voc_caci_condition.ID AND " + Make_OR_of_CODES("voc_caci_condition.CODE", examination_caci_condition_obj.getCaci());
				  
				  if(!examination_caci_condition_obj.getValue().isEmpty()) {  //TODO check value
					  tables += ", exam_questionnaire_score";
					  where_clause += " AND " + Make_OR_of_CODES("exam_questionnaire_score.VALUE", examination_caci_condition_obj.getValue());
				  }
				  
				  if(!(examination_caci_condition_obj.getQuestionnaire_period_of_time_exact_year()).isEmpty()) {
					  tables += ", exam_questionnaire_score, dt_date";
					  where_clause += Make_specific_date_query(mode, "exam_questionnaire_score.QUESTIONNAIRE_DATE_ID","dt_date",examination_caci_condition_obj.getQuestionnaire_period_of_time_exact_year(), 
							  examination_caci_condition_obj.getQuestionnaire_period_of_time_exact_month(), examination_caci_condition_obj.getQuestionnaire_period_of_time_exact_day());					  		
				  } else if(!(examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_end_year()).isEmpty()) {
					  tables += ", exam_questionnaire_score, dt_date";
					  where_clause += Make_begin_end_date_query (mode,"exam_questionnaire_score.QUESTIONNAIRE_DATE_ID", "dt_date",examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_start_year(), 
							  examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_start_month(), examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_start_day(),
							  examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_end_year(), examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_end_month(),
							  examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_end_day()); 			  
				  } else if(!(examination_caci_condition_obj.getQuestionnaire_period_of_time_until_year()).isEmpty()) {
					  tables += ", exam_questionnaire_score, dt_date";
					  where_clause += Make_begin_end_date_query (mode,"exam_questionnaire_score.QUESTIONNAIRE_DATE_ID","dt_date", "1800", "1", "1", examination_caci_condition_obj.getQuestionnaire_period_of_time_until_year(), examination_caci_condition_obj.getQuestionnaire_period_of_time_until_month(),
							  examination_caci_condition_obj.getQuestionnaire_period_of_time_until_day()); 
				  }
				  
				  query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;
			  } break; //other_healthcare_visit
			  
			  case "other_healthcare_visit": { //Check if user provided the info of all the fields 
				  other_healthcare_visit  other_healthcare_visit_obj =  (other_healthcare_visit)current_Criterion;
				  
				  String tables = "patient, other_healthcare_visit, voc_specialist";
				  String where_clause = "patient.ID = other_healthcare_visit.PATIENT_ID AND other_healthcare_visit.SPECIALIST_ID=voc_specialist.ID AND " + Make_OR_of_CODES("voc_specialist.CODE", other_healthcare_visit_obj.getSpecialist());
				  
				  /*query = "SELECT DISTINCT patient.UID " +
						  "FROM other_healthcare_visit, patient, dt_date, voc_specialist " + //interv_Surgery, dt_date, voc_direction, voc_confirmation
						  "WHERE patient.ID = other_healthcare_visit.PATIENT_ID AND " + 
						  "other_healthcare_visit.SPECIALIST_ID=voc_specialist.ID " +
				  		  "AND " + Make_OR_of_CODES("voc_specialist.CODE", other_healthcare_visit_obj.getSpecialist());*/
				 // System.out.println("Test id: "+other_healthcare_visit_obj.getSpecialist());
				  
				  if(!(other_healthcare_visit_obj.getPeriod_of_time_exact_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_specific_date_query(mode, "other_healthcare_visit.DATE_ID","dt_date",other_healthcare_visit_obj.getPeriod_of_time_exact_year(), 
							  other_healthcare_visit_obj.getPeriod_of_time_exact_month(), other_healthcare_visit_obj.getPeriod_of_time_exact_day());					  		
				  } else if(!(other_healthcare_visit_obj.getPeriod_of_time_interval_end_year()).isEmpty()) {
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
				  
				  query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;
				  
			  } break;			
			  
			  case "other_family_history": { //Check if user provided the info of all the fields 
				  other_family_history  other_family_history_obj =  (other_family_history)current_Criterion;
				  
				  String tables = "other_family_history, patient, voc_medical_condition";
				  String where_clause = "patient.ID = other_family_history.PATIENT_ID AND other_family_history.MEDICAL_CONDITION_ID=voc_medical_condition.ID AND (" + Make_OR_of_CODES("voc_medical_condition.CODE", other_family_history_obj.getMedical_condition());
				  
				  String narrowTerms = getTermsWithNarrowMeaning(other_family_history_obj.getMedical_condition());
				  String[] allNarrowTerms = narrowTerms.split(",");
				  for(int c=1; c<allNarrowTerms.length; c++) {
					  where_clause += " OR " + Make_OR_of_CODES("voc_medical_condition.CODE", allNarrowTerms[c]);
				  }
				  where_clause += ")";
				  
				  if(!(other_family_history_obj.getRelative_degree()).isEmpty()) {
					  tables += ", voc_relative_degree";
					  where_clause += "AND other_family_history.RELATIVE_DEGREE_ID = voc_relative_degree.ID " +
					  	"AND " +Make_OR_of_CODES("voc_relative_degree.CODE", other_family_history_obj.getRelative_degree()); 
					  
				  };
				  
				  if(!other_family_history_obj.getStatement().isEmpty()) 
					  where_clause += "AND other_family_history.STMT_ID=voc_confirmation.ID " +
				  				 "AND voc_confirmation.CODE='"+other_family_history_obj.getStatement() + "'";
				  
				  if(incl) where_clause += " AND other_family_history.STMT_ID=1";
				  else where_clause += " AND other_family_history.STMT_ID=2";
				  
				  query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;
				  
			  } break;
			  
			  case "other_clinical_trials": { //Check if user provided the info of all the fields 
				  other_clinical_trials  other_clinical_trials_obj =  (other_clinical_trials)current_Criterion;
				  
				  String tables = "patient, other_clinical_trials";
				  String where_clause = "patient.ID = other_clinical_trials.PATIENT_ID";
				  
				 /* query = "SELECT DISTINCT patient.UID " +
						  "FROM patient, other_clinical_trials, dt_period_of_time, dt_date, voc_confirmation " + //interv_Surgery, dt_date, voc_direction, voc_confirmation
						  "WHERE patient.ID = other_clinical_trials.PATIENT_ID "; */
						  
			if(!(other_clinical_trials_obj.getPeriod_of_time_exact_year()).isEmpty()) {
				tables += ", dt_date";
				where_clause += Make_specific_date_query(mode, "other_clinical_trials.PERIOD_ID","dt_date",other_clinical_trials_obj.getPeriod_of_time_exact_year(), 
						  other_clinical_trials_obj.getPeriod_of_time_exact_month(), other_clinical_trials_obj.getPeriod_of_time_exact_day());					  		
			} else if(!(other_clinical_trials_obj.getPeriod_of_time_interval_end_year()).isEmpty()) {
				tables += ", dt_date";
				where_clause += Make_begin_end_date_query (mode,"other_clinical_trials.PERIOD_ID", "dt_date",other_clinical_trials_obj.getPeriod_of_time_interval_start_year(), 
						  other_clinical_trials_obj.getPeriod_of_time_interval_start_month(), other_clinical_trials_obj.getPeriod_of_time_interval_start_day(),
						  other_clinical_trials_obj.getPeriod_of_time_interval_end_year(), other_clinical_trials_obj.getPeriod_of_time_interval_end_month(),
						  other_clinical_trials_obj.getPeriod_of_time_interval_end_day()); 			  
			} else if(!(other_clinical_trials_obj.getPeriod_of_time_until_year()).isEmpty()) {
				tables += ", dt_date";
				where_clause += Make_begin_end_date_query (mode,"other_clinical_trials.PERIOD_ID","dt_date", "1800", "1", "1", other_clinical_trials_obj.getPeriod_of_time_until_year(), other_clinical_trials_obj.getPeriod_of_time_until_month(),
						  other_clinical_trials_obj.getPeriod_of_time_until_day()); 
			}
			
			if(!other_clinical_trials_obj.getStatement().isEmpty()) 
		  		query += "AND other_clinical_trials.STMT_ID=voc_confirmation.ID " +
		  				 "AND voc_confirmation.CODE='"+other_clinical_trials_obj.getStatement() + "'";
			
			if(incl) where_clause += " AND other_clinical_trials.STMT_ID=1";
			  else where_clause += " AND other_clinical_trials.STMT_ID=2";
			
			query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;

			  } break;
			  
			  case "patient": { //Check if user provided the info of all the fields 
				  patient  patient_obj =  (patient)current_Criterion;
				  
				  String tables = "patient";
				  String where_clause = "";
				  
				  /*query = "SELECT DISTINCT patient.UID " +
						  "FROM patient,  dt_date as dt_date1, dt_date as dt_date2, dt_date as dt_date3, dt_date as dt_date4, voc_direction " + //exam_lab_test, voc_lab_test, dt_amount, voc_unit, voc_assessment, dt_amount_range,interv_Surgery, dt_date, voc_direction, voc_confirmation
						  "WHERE "; 
						  //"exam_lab_test.TEST_ID=voc_lab_test.ID " +
						  //"voc_lab_test.CODE='"+crit_exam_lab_test_obj.getTEST_ID_voc_lab_test_CODE()+"' ";
				  		  //"AND " + Make_OR_of_CODES("voc_lab_test.CODE", patient_obj.getBirth_period_of_time_exact_year());
				  System.out.println("Test id: "+patient_obj.getBirth_period_of_time_exact_year());*/
				  				  
				  if(!(patient_obj.getBirth_period_of_time_exact_year()).isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  where_clause += Make_specific_date_query(mode, "patient.DATE_OF_BIRTH_ID","dt_date1",patient_obj.getBirth_period_of_time_exact_year(),
							  patient_obj.getBirth_period_of_time_exact_month(),patient_obj.getBirth_period_of_time_exact_day());					  		
				  } else if(!(patient_obj.getBirth_period_of_time_interval_start_year()).isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  where_clause += Make_begin_end_date_query (mode,"patient.DATE_OF_BIRTH_ID", "dt_date1",patient_obj.getBirth_period_of_time_interval_start_year(), patient_obj.getBirth_period_of_time_interval_start_month(), patient_obj.getBirth_period_of_time_interval_start_day(), patient_obj.getBirth_period_of_time_interval_end_year(), patient_obj.getBirth_period_of_time_interval_end_month(),
							  patient_obj.getBirth_period_of_time_interval_end_day()); 			  
				  } else if(!(patient_obj.getBirth_period_of_time_until_year()).isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  where_clause += Make_begin_end_date_query (mode,"patient.DATE_OF_BIRTH_ID","dt_date1", "1800", "1", "1", patient_obj.getBirth_period_of_time_until_year(), 
							  patient_obj.getBirth_period_of_time_until_month(), patient_obj.getBirth_period_of_time_until_day()); 
				  }
				  
				  if(!(patient_obj.getSymptoms_onset_period_of_time_exact_year()).isEmpty()) {
					  tables += ", dt_date as dt_date2";
					  where_clause += Make_specific_date_query(mode, "patient.PSS_SYMPTOMS_ONSET_DATE_ID","dt_date2",patient_obj.getSymptoms_onset_period_of_time_exact_year(),
							  patient_obj.getSymptoms_onset_period_of_time_exact_month(),patient_obj.getSymptoms_onset_period_of_time_exact_day());					  		
				  } else if(!(patient_obj.getSymptoms_onset_period_of_time_interval_start_year()).isEmpty()) {
					  tables += ", dt_date as dt_date2";
					  where_clause += Make_begin_end_date_query (mode,"patient.PSS_SYMPTOMS_ONSET_DATE_ID", "dt_date2",patient_obj.getSymptoms_onset_period_of_time_interval_start_year(), patient_obj.getSymptoms_onset_period_of_time_interval_start_month(), patient_obj.getSymptoms_onset_period_of_time_interval_start_day(), patient_obj.getSymptoms_onset_period_of_time_interval_end_year(), patient_obj.getSymptoms_onset_period_of_time_interval_end_month(),
							  patient_obj.getSymptoms_onset_period_of_time_interval_end_day()); 			  
				  } else if(!(patient_obj.getSymptoms_onset_period_of_time_until_year()).isEmpty()) {
					  tables += ", dt_date as dt_date2";
					  where_clause += Make_begin_end_date_query (mode,"patient.PSS_SYMPTOMS_ONSET_DATE_ID","dt_date2", "1800", "1", "1", patient_obj.getSymptoms_onset_period_of_time_until_year(), 
							  patient_obj.getSymptoms_onset_period_of_time_until_month(), patient_obj.getSymptoms_onset_period_of_time_until_day()); 
				  }
				  
				  if(!(patient_obj.getDiagnosis_period_of_time_exact_year()).isEmpty()) {
					  tables += ", dt_date as dt_date3";
					  where_clause += Make_specific_date_query(mode, "patient.PSS_DIAGNOSIS_DATE_ID","dt_date3",patient_obj.getDiagnosis_period_of_time_exact_year(),
							  patient_obj.getDiagnosis_period_of_time_exact_month(),patient_obj.getDiagnosis_period_of_time_exact_day());					  		
				  } else if(!(patient_obj.getDiagnosis_period_of_time_interval_start_year()).isEmpty()) {
					  tables += ", dt_date as dt_date3";
					  where_clause += Make_begin_end_date_query (mode,"patient.PSS_DIAGNOSIS_DATE_ID", "dt_date3",patient_obj.getDiagnosis_period_of_time_interval_start_year(), patient_obj.getDiagnosis_period_of_time_interval_start_month(), patient_obj.getDiagnosis_period_of_time_interval_start_day(), patient_obj.getDiagnosis_period_of_time_interval_end_year(), patient_obj.getDiagnosis_period_of_time_interval_end_month(),
							  patient_obj.getDiagnosis_period_of_time_interval_end_day()); 			  
				  } else if(!(patient_obj.getDiagnosis_period_of_time_until_year()).isEmpty()) {
					  tables += ", dt_date as dt_date3";
					  where_clause += Make_begin_end_date_query (mode,"patient.PSS_DIAGNOSIS_DATE_ID","dt_date3", "1800", "1", "1", patient_obj.getDiagnosis_period_of_time_until_year(), 
							  patient_obj.getDiagnosis_period_of_time_until_month(), patient_obj.getDiagnosis_period_of_time_until_day()); 
				  }
				  
				  if(!(patient_obj.getCohort_inclusion_period_of_time_exact_year()).isEmpty()) {
					  tables += ", dt_date as dt_date4";
					  where_clause += Make_specific_date_query(mode, "patient.COHORT_INCLUSION_DATE_ID","dt_date4",patient_obj.getCohort_inclusion_period_of_time_exact_year(),
							  patient_obj.getCohort_inclusion_period_of_time_exact_month(),patient_obj.getCohort_inclusion_period_of_time_exact_day());					  		
				  } else if(!(patient_obj.getCohort_inclusion_period_of_time_interval_start_year()).isEmpty()) {
					  tables += ", dt_date as dt_date4";
					  where_clause += Make_begin_end_date_query (mode,"patient.COHORT_INCLUSION_DATE_ID", "dt_date4",patient_obj.getCohort_inclusion_period_of_time_interval_start_year(), patient_obj.getCohort_inclusion_period_of_time_interval_start_month(), patient_obj.getCohort_inclusion_period_of_time_interval_start_day(), patient_obj.getCohort_inclusion_period_of_time_interval_end_year(), patient_obj.getCohort_inclusion_period_of_time_interval_end_month(),
							  patient_obj.getCohort_inclusion_period_of_time_interval_end_day()); 			  
				  } else if(!(patient_obj.getCohort_inclusion_period_of_time_until_year()).isEmpty()) {
					  tables += ", dt_date as dt_date4";
					  where_clause += Make_begin_end_date_query (mode,"patient.COHORT_INCLUSION_DATE_ID","dt_date4", "1800", "1", "1", patient_obj.getCohort_inclusion_period_of_time_until_year(), 
							  patient_obj.getCohort_inclusion_period_of_time_until_month(), patient_obj.getCohort_inclusion_period_of_time_until_day()); 
				  }
				  query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;
				  query=query.replace("WHERE  AND", "WHERE");
				  query=query.replace("WHERE AND", "WHERE");
				  System.out.println("The query is: "+query);
			  } break;
			  
			  default:
				  System.out.println("Undefined criterion-name-"+(i+1)+" in the input JSON file.");
			} 
			
			try { System.out.println("We are ready to execute the query: "+query);
				results_of_one_Criterion = DBServiceCRUD.getDataFromDB(query);
				System.out.println("We executed the query: "+query +"\nAnd we had the result: "+results_of_one_Criterion);
			} catch (SQLException e) {
				//LOGGER.log(Level.SEVERE,"Bad type query or arguments: "+query,true);
				//flush_handler();
				
				e.printStackTrace();
				//return "Bad type query or arguments: "+query;
			}
				
			//LOGGER.log(Level.INFO, "Criterion-"+(i+1)+": "+current_Criterion.getCriterion()+"\nQuery-"+(i+1)+": "+query+"\n"+
			//"Results: "+results_of_one_Criterion+"\n",true);
			
			if(results_of_all_Criterions.equals("")) results_of_all_Criterions = results_of_one_Criterion;
			else results_of_all_Criterions = intersection_of_UIDs(results_of_one_Criterion, results_of_all_Criterions);
    	}
    	return results_of_all_Criterions;
    }
    
    public void criterionDBmatching(ArrayList<Criterion> list_of_inclusive_criterions, ArrayList<Criterion> list_of_exclusive_criterions) throws JSONException{
    	results = new Result_UIDs();
    	Boolean mode;
    	for (int j=0;j<2;j++) {
			
    		if(j==0) mode = false; //LOGGER.log(Level.INFO,"======** UIDs_defined_ALL_elements **======\n",true);}
    		else mode = true; //LOGGER.log(Level.INFO,"======** UIDs_UNdefined_some_elements **======\n",true);}
    		String results_of_all_Criterions="";
    		results_of_all_Criterions = createQuery(list_of_inclusive_criterions, true, mode, results_of_all_Criterions);
    		results_of_all_Criterions = createQuery(list_of_exclusive_criterions, false, mode, results_of_all_Criterions);
    		results.Input_UIDs(mode,results_of_all_Criterions);
    	}
    	UIDsDefined.add(results.UIDs_defined_ALL_elements );
    	UIDsUndefined.add(results.UIDs_UNdefined_some_elements);
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

    
    public void writeXMLResponse(int cohortIndex, boolean createXML, int cohortID){
    	try{
    		if(cohortIndex==0) {
    		xmljaxbContext = JAXBContext.newInstance(ObjectFactory.class);
    		xmljaxbMarshaller = xmljaxbContext.createMarshaller();
    		xmljaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    		patientsSelectionResponse = new PatientsSelectionResponse();
    		//jaxbMarshaller.marshal(patientsSelectionResponse, responseXML);
    		patientsSelectionResponse.setUserID(patientsSelectionRequest.getUserID());
    		patientsSelectionResponse.setSessionID(patientsSelectionRequest.getSessionID());
    		GregorianCalendar gregorianCalendar = new GregorianCalendar();
            DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
            XMLGregorianCalendar now = 
                datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
            now.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
    		patientsSelectionResponse.setResponseDate(now);
    		//for(String cohortID: patientsSelectionRequest.getCohortID()){
    		}
    			CohortResponse cohortResponse = new CohortResponse();
    			//cohortResponse.setCohortID(patientsSelectionRequest.getCohortID().get(cohortIndex));
    			if (cohortID<10) cohortResponse.setCohortID("COHORT-ID-0"+cohortID);
    			else cohortResponse.setCohortID("COHORT-ID-"+cohortID);
    			cohortResponse.setQueryDate(patientsSelectionRequest.getRequestDate());
    			if(results.UIDs_defined_ALL_elements.length==1 && results.UIDs_defined_ALL_elements[0].equals("")) cohortResponse.setEligiblePatientsNumber(0);
    			else cohortResponse.setEligiblePatientsNumber(results.UIDs_defined_ALL_elements.length);
    			
    			if(patientsSelectionRequest.getRequestID().value().equals("ELIGIBLE_PATIENTS_IDS")) {
    				if(results.UIDs_defined_ALL_elements.length==1 && results.UIDs_defined_ALL_elements[0].equals("")) cohortResponse.setEligiblePatientsIDs("");
    				else{
    					String dbstring = "";
    					for(int k=0; k<results.UIDs_defined_ALL_elements.length; k++) {
    						if(k==0) dbstring += results.UIDs_defined_ALL_elements[k];
    						else dbstring += ", "+results.UIDs_defined_ALL_elements[k];
    					}
    					cohortResponse.setEligiblePatientsIDs(dbstring);
    				}
    			}
    			EligibilityCriteriaUsed inclAndExclCriteriaUsed = new EligibilityCriteriaUsed();
    			int i=0;
    			for(org.ntua.criteria.Criterion inclCriterion: patientsSelectionRequest.getEligibilityCriteria().getInclusionCriteria().getCriterion()){
    				CriterionUsage inclCriterionUsage = new CriterionUsage();
    				inclCriterionUsage.setCriterionID(inclCriterion.getUID());
    				if(inclusion_criteria.get(i).getString("usage").equals("used")) inclCriterionUsage.setCriterionUsageStatus(CriterionUsageStatus.USED);
    				else inclCriterionUsage.setCriterionUsageStatus(CriterionUsageStatus.NOT_USED);
    				System.out.println(inclusion_criteria.get(i));
    				inclCriterionUsage.setNotesHTML(inclusion_criteria.get(i).getString("notes"));
    				inclAndExclCriteriaUsed.getCriterionUsage().add(inclCriterionUsage);
    				i++;
    			}
    			
    			i=0;
    			for(org.ntua.criteria.Criterion exclCriterion: patientsSelectionRequest.getEligibilityCriteria().getExclusionCriteria().getCriterion()){
    				CriterionUsage exclCriterionUsage = new CriterionUsage();
    				exclCriterionUsage.setCriterionID(exclCriterion.getUID());
    				if(exclusion_criteria.get(i).getString("usage").equals("used")) exclCriterionUsage.setCriterionUsageStatus(CriterionUsageStatus.USED);
    				else exclCriterionUsage.setCriterionUsageStatus(CriterionUsageStatus.NOT_USED);
    				System.out.println(exclusion_criteria.get(i));
    				exclCriterionUsage.setNotesHTML(exclusion_criteria.get(i).getString("notes"));
    				inclAndExclCriteriaUsed.getCriterionUsage().add(exclCriterionUsage);
    				i++;
    			}
    			
    			cohortResponse.setEligibilityCriteriaUsed(inclAndExclCriteriaUsed);
    			patientsSelectionResponse.getCohortResponse().add(cohortResponse);
    		//}
    		if(createXML) {
    		File fXmlFile = new File(getServletContext().getRealPath("/WEB-INF/Response"+requestID+".xml"));
    		//File fXmlFile = new File("Response"+requestID+".xml");
    		ObjectFactory objectFactory = new ObjectFactory();
    		JAXBElement<PatientsSelectionResponse> je =  objectFactory.createPatientsSelectionResponse(patientsSelectionResponse);
    		xmljaxbMarshaller.marshal(je, fXmlFile);
    		}
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    private String getCohortsC4(String darID, String username, String password) {
    	String result = "";
    	try {
	        String webPage = "https://private.harmonicss.eu/index.php/apps/coh/api/1.0/c4?darId="+darID;

	        String authString = username + ":" + password;
	        System.out.println("auth string: " + authString);
	        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
	        String authStringEnc = new String(authEncBytes);
	        System.out.println("Base64 encoded auth string: " + authStringEnc);

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
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
    	return result;
    }
    
    private JSONObject getCredentials(int cohortID, String username, String password) {
    	JSONObject credentials = new JSONObject();
    	try {
	        String webPage = "https://private.harmonicss.eu/index.php/apps/coh/api/1.0/cohortid?id="+cohortID;

	        String authString = username + ":" + password;
	        System.out.println("auth string: " + authString);
	        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
	        String authStringEnc = new String(authEncBytes);
	        System.out.println("Base64 encoded auth string: " + authStringEnc);

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
	        e.printStackTrace();
	    } catch (IOException e) {
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
	    JSONArray cohorts = new JSONArray(getCohortsC4(darID, username, password));
	    boolean createXML = false;
    	for(int i=0; i<cohorts.length(); i++){
    		JSONObject cohortResponse = new JSONObject();
    		String cohortName = "";
    		//if(cohorts.getJSONObject(i).get("cohortId").equals("2")) cohortName = "Harm-DB-09";
    		//if(cohorts.getJSONObject(i).get("cohortId").equals("3")) cohortName = "Harm-DB-09";
    		//cohortName = "Harm-DB-0"+cohorts.getJSONObject(i).get("cohortId");
    		int mycohortid = Integer.valueOf(cohorts.getJSONObject(i).get("cohortId").toString());
    		if(mycohortid<10) cohortName = "chdb00"+mycohortid;
    		else cohortName = "chdb0"+mycohortid;
    		cohortResponse.put("cohort_name", cohortName);
    		if(cohorts.getJSONObject(i).get("statusId").equals("2")) {
    			JSONObject credentials = getCredentials(mycohortid, username, password);
    			//String cohortName = cohorts.getJSONObject(i).get("cohortName").toString();
    			
    			System.out.println("-------------------------------- Execute query for cohort " + cohortName + " -------------------------------");
    			//ConfigureFile obj = new ConfigureFile("jdbc:mysql://ponte.grid.ece.ntua.gr:3306/"+cohortName,"emps","emps");
    			ConfigureFile obj = new ConfigureFile("jdbc:mysql://"+credentials.getString("dbserver")+":"+credentials.getString("dbport")+"/"+credentials.getString("dbarea"),credentials.getString("dbuname"),credentials.getString("dbupass"));
    			if(!DBServiceCRUD.makeJDBCConnection(obj))  System.out.println("Connection with the Database failed. Check the Credentials and the DB URL.");
    	    	else System.out.println("everything's gooooooood");
    			criterionDBmatching(list_of_inclusive_criterions,list_of_exclusive_criterions);
    			if(i==cohorts.length()-1) createXML=true;
    			writeXMLResponse(i, createXML, mycohortid);	
    			cohortResponse.put("patients_IDs_list", results.UIDs_defined_ALL_elements);
    			String result_incl = "";
      	  		String result_excl = "";
      	  		int j = 0;
      	  		for(org.ntua.criteria.Criterion inclCriterion: patientsSelectionRequest.getEligibilityCriteria().getInclusionCriteria().getCriterion()){
      	  			//System.out.println(inclCriterion.getFormalExpression().get(0).getBooleanExpression().trim());
      	  			result_incl+=inclCriterion.getDescription()+": "+inclCriterion.getFormalExpression().get(0).getBooleanExpression().trim();
      	  			if(inclusion_criteria.get(j).getString("usage").equals("used")) result_incl += " - USED<br>";
      	  			else result_incl += " - NOT USED<br>";
      	  			j++;
      	  		}
      	  		cohortResponse.put("inclusion_criteria", "Inclusion Criteria: <br>"+result_incl);
      	  		j=0;
      	  		for(org.ntua.criteria.Criterion exclCriterion: patientsSelectionRequest.getEligibilityCriteria().getExclusionCriteria().getCriterion()){
      	  			//System.out.println(exclCriterion.getFormalExpression().get(0).getBooleanExpression().trim());
      	  			result_excl+=exclCriterion.getDescription()+": "+exclCriterion.getFormalExpression().get(0).getBooleanExpression().trim();
      	  			if(exclusion_criteria.get(j).getString("usage").equals("used")) result_excl += " - USED<br>";
      	  			else result_excl += " - NOT USED<br>";
      	  			j++;
      	  		}
      	  		cohortResponse.put("exclusion_criteria", "Exclusion Criteria: <br>"+result_excl);
    			cohortResponseList.add(cohortResponse);
    			inclusion_criteria.clear();
    			exclusion_criteria.clear();
    			DBServiceCRUD.closeJDBCConnection();
    			if(patientsSelectionRequest.getRequestID().value().equals("ELIGIBLE_PATIENTS_IDS")) {
    				int lastDigit = mycohortid%10;
    				String ponteCohort = "Harm-DB-0"+lastDigit;
    				Class.forName("com.mysql.jdbc.Driver");
    				System.out.println("Congrats - Seems your MySQL JDBC Driver Registered!");
    				Connection db_con_obj = DriverManager.getConnection("jdbc:mysql://ponte.grid.ece.ntua.gr:3306/"+ponteCohort, "emps","emps");
    				Date date = new Date();
    				Object param = new java.sql.Timestamp(date.getTime());
    				String query = "INSERT INTO Eligible_Patient_IDs (DAR_ID, EXEC_DATE, PATIENT_IDS) VALUES (?, ?, ?)";
    				PreparedStatement db_prep_obj = db_con_obj.prepareStatement(query);
    				db_prep_obj.setString(1, darID);
    				db_prep_obj.setTimestamp(2, (Timestamp) param);
    				if(results.UIDs_defined_ALL_elements.length==1 && results.UIDs_defined_ALL_elements[0].equals("")) db_prep_obj.setString(3, "");
    				else {
    					String dbstring = "";
    					for(int k=0; k<results.UIDs_defined_ALL_elements.length; k++) {
    						if(k==0) dbstring += results.UIDs_defined_ALL_elements[k];
    						else dbstring += ", "+results.UIDs_defined_ALL_elements[k];
    					}
    					db_prep_obj.setString(3, dbstring);
    				}
    				db_prep_obj.execute();
    				if (db_prep_obj != null) {
    					db_prep_obj.close();
    				}	
    				if (db_con_obj != null) {
    					db_con_obj.close();
    				}
    			}
    			System.out.println("End");
    		}
    		else {
    			cohortResponse.put("cohort_notes","Cohort not available.");
    			cohortResponseList.add(cohortResponse);
    		}
    			
    		
    	}
    	/*ConfigureFile obj = new ConfigureFile("jdbc:mysql://ponte.grid.ece.ntua.gr:3306/HarmonicSS-Patient-Selection-DB","emps","emps");
    	if(!DBServiceCRUD.makeJDBCConnection(obj))  System.out.println("Connection with the Database failed. Check the Credentials and the DB URL.");
    	else System.out.println("everything's gooooooood");
    	DBServiceCRUD.setExecutedDataToDB(requestID);
    	DBServiceCRUD.closeJDBCConnection();*/
    	Class.forName("com.mysql.jdbc.Driver");
		System.out.println("Congrats - Seems your MySQL JDBC Driver Registered!");
		Connection db_con_obj = DriverManager.getConnection("jdbc:mysql://ponte.grid.ece.ntua.gr:3306/HarmonicSS-Patient-Selection-DB", "emps","emps");
		String requestXML = readLineByLineJava8(getServletContext().getRealPath("/WEB-INF/Request"+darID+".xml"));
		String responseXML = readLineByLineJava8(getServletContext().getRealPath("/WEB-INF/Response"+darID+".xml"));
		//String responseXML = readLineByLineJava8("Response"+darID+".xml");
		Date date = new Date();
		Object param = new java.sql.Timestamp(date.getTime());
		System.out.println(darID);
		String query = "INSERT INTO EXECUTION_DATA (USER_ID, REQUEST_ID, REQUEST_XML, EXECUTION_DATE, RESPONSE_XML) VALUES (?, ?, ?, ?, ?)";
		PreparedStatement db_prep_obj = db_con_obj.prepareStatement(query);
		db_prep_obj.setString(1, username);
		db_prep_obj.setString(2, darID);
		db_prep_obj.setString(3, requestXML.replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;"));
		db_prep_obj.setTimestamp(4, (Timestamp) param);
		db_prep_obj.setString(5, responseXML.replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;"));
		db_prep_obj.execute();
		if (db_prep_obj != null) {
        	db_prep_obj.close();
        }
        if (db_con_obj != null) {
        	db_con_obj.close();
        }
    	System.out.println("-------------------------------- Execution data saved to database -------------------------------");
    	
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
    
    public boolean canUseCriterion(Criterion crit){
    	String query="";
    	String assistanceQuery="";
    	boolean myresults = false;
    	switch(crit.getCriterion()) {
    		case "demographics_gender": {
    			query = "SELECT * FROM demo_sex_data";
    			termAndSubterms = "Search for gender data";
    		}
    		break;
    		case "demographics_ethnicity": {
    			query = "SELECT * FROM demo_ethnicity_data";
    			termAndSubterms = "Search for ethnicity data";
    		}
    		break;
    		case "demographics_education": {
    			query = "SELECT * FROM demo_education_level_data";
    			termAndSubterms = "Search for education data";
    		}
    		break;
    		case "demographics_weight": {
    			query = "SELECT * FROM demo_weight_data";
    			termAndSubterms = "Search for weight data";
    		}
    		break;
    		case "demographics_occupation": {
    			query = "SELECT * FROM demo_occupation_data";
    			termAndSubterms = "Search for occupation data";
    		}
    		break;
    		case "demographics_pregnancy": {
    			query = "SELECT * FROM demo_pregnancy_data";
    			termAndSubterms = "Search for pregnancy data";
    		}
    		break;
    		case "lifestyle_smoking": {
    			query = "SELECT * FROM lifestyle_smoking";
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
				query += ")";
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
				query += ")";
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
				query += ")";
				assistanceQuery = "SELECT NAME FROM voc_pharm_drug WHERE" + query.split("AND")[1]; 
    		}
    		break;
    		case "intervention_chemotherapy": {
				  //intervention_chemotherapy  crit_interv_chemotherapy_obj =  (intervention_chemotherapy)crit;
				query = "SELECT * FROM interv_chemotherapy"; //, voc_confirmation WHERE interv_chemotherapy.DUE_TO_PSS_ID = voc_confirmation.ID AND " + Make_OR_of_CODES("voc_confirmation.CODE", crit_interv_chemotherapy_obj.getReason());
				termAndSubterms = "Search for chemotherapy data";
    		}
    		break;
    		case "intervention_surgery": { 
    			query = "SELECT * FROM intervention_surgery";
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
				query += ") ";
				assistanceQuery = "SELECT NAME FROM voc_lab_test WHERE" + query.split("AND")[1];
    		}
    		break;
    		case "examination_biopsy": { //Check if user provided the info of all the fields 
				examination_biopsy  examination_biopsy_obj =  (examination_biopsy)crit;
				query = "SELECT * FROM exam_biopsy, voc_biopsy WHERE exam_biopsy.BIOPSY_ID=voc_biopsy.ID AND (voc_biopsy.CODE='"+examination_biopsy_obj.getBiopsy_type()+"'"; // ='SAL-BIO-21' Make_OR_of_CODES("voc_lab_test.CODE", examination_biopsy_obj.getBiopsy_type());				  		 
				String narrowTerms = getTermsWithNarrowMeaning(examination_biopsy_obj.getBiopsy_type());
				String[] allNarrowTerms = narrowTerms.split(",");
				for(int c=1; c<allNarrowTerms.length; c++) {
					query += " OR voc_biopsy.CODE='" + allNarrowTerms[c] + "'";
				}
				query += ") ";
				assistanceQuery = "SELECT NAME FROM voc_biopsy WHERE" + query.split("AND")[1];
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
				  query += ") ";
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
				  query += ")";
				  assistanceQuery = "SELECT NAME FROM voc_questionnaire WHERE" + query.split("AND")[1];
    		}
    		break;
    		case "examination_essdai_domain": {
    			examination_essdai_domain  examination_essdai_domain_obj =  (examination_essdai_domain)crit;
				query = "SELECT * FROM exam_essdai_domain, voc_essdai_domain WHERE exam_essdai_domain.DOMAIN_ID = voc_essdai_domain.ID AND " + Make_OR_of_CODES("voc_essdai_domain.CODE", examination_essdai_domain_obj.getDomain());
				assistanceQuery = "SELECT NAME FROM voc_essdai_domain WHERE" + query.split("AND")[1];
    		}
    		break;
    		case "examination_caci_condition": { //Check if user provided the info of all the fields 
				examination_caci_condition  examination_caci_condition_obj =  (examination_caci_condition)crit;
				query = "SELECT * FROM exam_caci_condition, voc_caci_condition WHERE exam_caci_condition.CACI_ID = voc_caci_condition.ID AND " + Make_OR_of_CODES("voc_caci_condition.CODE", examination_caci_condition_obj.getCaci());
				assistanceQuery = "SELECT NAME FROM voc_caci_condition WHERE" + query.split("AND")[1];
    		}
    		break;
    		case "other_healthcare_visit": { //Check if user provided the info of all the fields 
				other_healthcare_visit  other_healthcare_visit_obj =  (other_healthcare_visit)crit;
				query = "SELECT * FROM other_healthcare_visit, voc_specialist WHERE other_healthcare_visit.SPECIALIST_ID=voc_specialist.ID AND " + Make_OR_of_CODES("voc_specialist.CODE", other_healthcare_visit_obj.getSpecialist());
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
				query += ")";
				assistanceQuery = "SELECT NAME FROM voc_medical_condition WHERE" + query.split("AND")[1];
    		}
    		break;
    		case "other_clinical_trials": { //Check if user provided the info of all the fields 
				query = "SELECT * FROM other_clinical_trials"; 
				termAndSubterms = "Search for other clinical trials data";
    		}
    		break;
    		case "patient": { //Check if user provided the info of all the fields 
				query = "SELECT * FROM patient";
				termAndSubterms = "Search for patients data";
    		}
    		break;
    		default: System.out.println("Undefined criterion-name in the input JSON file.");
    	}
    	try { 
    		if(!query.equals("")) myresults = DBServiceCRUD.testQuery(query);
    		if(!assistanceQuery.equals("")) {
    			String[] assistanceResults = DBServiceCRUD.assistanceQuery(assistanceQuery).split(",");
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
    		//LOGGER.log(Level.SEVERE,"Bad type query or arguments: "+query,true);
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//Logger LOGGER = Util_Logger.Initialize_logger("C:/Users/Jason/eclipse-workspace/HarmonicSS/LogFile.log");
		JSONObject all = new JSONObject();
		cohortResponseList.clear();
		Infos req = new Gson().fromJson(request.getReader(), Infos.class);
		//cohortResponseList
		requestID = req.requestID;
		if(requestID!=null){
			try {
				all.put("requestId", requestID);
			} catch (JSONException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
	    	manager = OWLManager.createOWLOntologyManager();
	    	Scanner s1 = new Scanner(new BufferedReader(new FileReader(getServletContext().getRealPath("/WEB-INF/infos.txt"))));
			String[] line = s1.nextLine().split(":");
		    //documentIRI = IRI.create("file:///C:/Users/Jason/Desktop/", "HarmonicSS-Reference-Model+Vocabularies-v.0.9.owl");
			documentIRI = IRI.create(getServletContext().getResource("/WEB-INF/"+line[1].trim()));
		    try{
		        ontology = manager.loadOntologyFromOntologyDocument(documentIRI);
	            findClasses();
	            findSubclasses();
			}
			catch (OWLOntologyCreationException e) {
		        e.printStackTrace();
				
			}
			String crit_incl_excl_in = readXMLbyRequestID(requestID, req.username, req.password);
			System.out.println(crit_incl_excl_in);
			String[] crit_incl_excl=crit_incl_excl_in.split("XXX");
			String criteria = Intermediate_Layer.preProcess_JSON(crit_incl_excl[0]);
			System.out.println("After Criteria preprocessed:\n"+criteria);
			ArrayList<Criterion> list_of_inclusive_criterions=null;
			try {
				list_of_inclusive_criterions = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
				//findCriterion((Criterion)list_of_inclusive_criterions.get(0));
				System.out.println(list_of_inclusive_criterions);
				
			} catch (JsonParseException e1) {
				/*LOGGER.log(Level.SEVERE,"JsonParseException Bad JSON format: "+criteria,true);
				flush_handler();*/
				e1.printStackTrace();
				//return "JsonParseException Bad JSON format.";
			} catch (JsonMappingException e1) {
				/*LOGGER.log(Level.SEVERE,"JsonMappingException Bad JSON format: "+criteria,true);
				flush_handler();*/
				e1.printStackTrace();
				//return "JsonParseException Bad JSON format.";
			} catch (IOException e1) {
				/*LOGGER.log(Level.SEVERE,"IOException Bad JSON format: "+criteria,true);
				flush_handler();*/
				e1.printStackTrace();
				//return "JsonParseException Bad JSON format.";
			}
			criteria = Intermediate_Layer.preProcess_JSON(crit_incl_excl[1]);
			System.out.println("After Criteria preprocessed:\n"+criteria);
			ArrayList<Criterion> list_of_exclusive_criterions=null;
			try {
				list_of_exclusive_criterions = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
				//findCriterion((Criterion)list_of_exclusive_criterions.get(0));
				System.out.println(list_of_exclusive_criterions);
					
			} catch (JsonParseException e1) {
				/*LOGGER.log(Level.SEVERE,"JsonParseException Bad JSON format: "+criteria,true);
				flush_handler();*/
				e1.printStackTrace();
				//return "JsonParseException Bad JSON format.";
			} catch (JsonMappingException e1) {
				/*LOGGER.log(Level.SEVERE,"JsonMappingException Bad JSON format: "+criteria,true);
				flush_handler();*/
				e1.printStackTrace();
				//return "JsonParseException Bad JSON format.";
			} catch (IOException e1) {
				/*LOGGER.log(Level.SEVERE,"IOException Bad JSON format: "+criteria,true);
				flush_handler();*/
				e1.printStackTrace();
				//return "JsonParseException Bad JSON format.";
			}
	    	/*ConfigureFile obj = new ConfigureFile("jdbc:mysql://localhost:3306/harmonicssdb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","");
	    	//ConfigureFile obj = new ConfigureFile("jdbc:mysql://147.102.19.66:3306/harmonicssdb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","emps","emps");
	    	
			if(!DBServiceCRUD.makeJDBCConnection(obj))  System.out.println("Connection with the Database failed. Check the Credentials and the DB URL.");
	    	else System.out.println("everything's gooooooood");
	    	
	    	criterionDBmatching(list_of_inclusive_criterions,list_of_exclusive_criterions);
			writeXMLResponse();			
			System.out.println("End");*/
			try {
				accessCohorts(requestID, req.username, req.password, list_of_inclusive_criterions, list_of_exclusive_criterions);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		try {
			all.put("cohort_response_list", cohortResponseList);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = response.getWriter();
		pw.flush();
		pw.print(all.toString());
		pw.close();
	}

}