package xmlWrapper;

import static queries.SQL_aux_functions.Make_OR_of_CODES;
import static queries.SQL_aux_functions.Make_begin_end_date_query;
import static queries.SQL_aux_functions.Make_begin_end_period_query;
import static queries.SQL_aux_functions.Make_specific_date_query;
import static queries.SQL_aux_functions.Make_specific_age_query;
import static queries.SQL_aux_functions.Make_begin_end_age_query;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.codec.binary.Base64;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ntua.criteria.PatientsSelectionRequest;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import com.google.gson.Gson;
import com.mysql.cj.jdbc.exceptions.CommunicationsException;

import criterionManager.Criterion;
import criterionManager.Criterions;
import criterionManager.condition_diagnosis;
import criterionManager.condition_symptom;
import criterionManager.demographics_education;
import criterionManager.demographics_ethnicity;
import criterionManager.demographics_gender;
import criterionManager.demographics_occupation;
import criterionManager.demographics_pregnancy;
import criterionManager.demographics_weight;
import criterionManager.examination_biopsy;
import criterionManager.examination_caci_condition;
import criterionManager.examination_essdai_domain;
import criterionManager.examination_lab_test;
import criterionManager.examination_medical_imaging_test;
import criterionManager.examination_questionnaire_score;
import criterionManager.intervention_chemotherapy;
import criterionManager.intervention_medication;
import criterionManager.intervention_surgery;
import criterionManager.lifestyle_smoking;
import criterionManager.other_clinical_trials;
import criterionManager.other_family_history;
import criterionManager.other_healthcare_visit;
import criterionManager.patient;
import jsonProcess.Intermediate_Layer;

/**
 * Servlet implementation class CriterionsTestServlet
 */
public class CriterionsTestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static OWLOntology ontology;
	private static OWLOntologyManager manager;
	private static IRI documentIRI;
	private static List<MyOWLClass> allClasses;
	private static Result_UIDs results;
	private static List<String[]> UIDsDefined = new ArrayList<String[]>();
	private static List<String[]> UIDsUndefined = new ArrayList<String[]>();
	private static JSONObject criterionResponseInfos = new JSONObject();
	private static String termAndSubterms = "";
	private static List<JSONObject> inclusion_criteria = new ArrayList<JSONObject>();
	private static PatientsSelectionRequest patientsSelectionRequest;
	private static List<JSONObject> cohortResponseList = new ArrayList<JSONObject>();
	private static boolean patients_found;
	JSONObject all;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CriterionsTestServlet() {
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
				  		where_clause += " GROUP BY patient.UID HAVING COUNT(*) >= "+crit_lifestyle_smoking_obj.getCount();
				  }*/
					  
				  if(isPeriodStart) {
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date AS dt_date1, dt_period_of_time";
						  where_clause += " AND lifestyle_smoking.PERIOD_ID=dt_period_of_time.ID AND dt_period_of_time.START_DATE_ID=dt_date1.ID";
					  }
					  query = "(SELECT MIN(dt_date1.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
				  }
				  else {
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date AS dt_date2, dt_period_of_time";
						  where_clause += " AND lifestyle_smoking.PERIOD_ID=dt_period_of_time.ID AND dt_period_of_time.END_DATE_ID=dt_date2.ID";
					  }
					  query = "(SELECT MAX(dt_date2.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
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
				  		where_clause += " GROUP BY patient.UID HAVING COUNT(*) >= "+crit_interv_medication_obj.getCount();
				  }*/
				  
				  if(isPeriodStart) {
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date AS dt_date1, dt_period_of_time";
						  where_clause += " AND interv_medication.PERIOD_ID=dt_period_of_time.ID AND dt_period_of_time.START_DATE_ID=dt_date1.ID";
					  }
					  query = "(SELECT MIN(dt_date1.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
				  }
				  else {
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date AS dt_date2, dt_period_of_time";
						  where_clause += " AND interv_medication.PERIOD_ID=dt_period_of_time.ID AND dt_period_of_time.END_DATE_ID=dt_date2.ID";
					  }
					  query = "(SELECT MAX(dt_date2.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
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
				  		where_clause += " GROUP BY patient.UID HAVING COUNT(*) >= "+crit_interv_chemotherapy_obj.getCount();
				  }*/
				  
				  if(isPeriodStart) {
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date AS dt_date1, dt_period_of_time";
						  where_clause += " AND interv_chemotherapy.PERIOD_ID=dt_period_of_time.ID AND dt_period_of_time.START_DATE_ID=dt_date1.ID";
					  }
					  query = "(SELECT MIN(dt_date1.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
				  }
				  else {
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date AS dt_date2, dt_period_of_time";
						  where_clause += " AND interv_chemotherapy.PERIOD_ID=dt_period_of_time.ID AND dt_period_of_time.END_DATE_ID=dt_date2.ID";
					  }
					  query = "(SELECT MAX(dt_date2.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
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
			  		where_clause += " GROUP BY patient.UID HAVING COUNT(*) >= "+other_clinical_trials_obj.getCount();
			  }*/
			
			if(isPeriodStart) {
				  if(!tables.contains("dt_date")) {
					  tables += ", dt_date AS dt_date1, dt_period_of_time";
					  where_clause += " AND other_clinical_trials.PERIOD_ID=dt_period_of_time.ID AND dt_period_of_time.START_DATE_ID=dt_date1.ID";
				  }
				  query = "(SELECT MIN(dt_date1.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
			  }
			  else {
				  if(!tables.contains("dt_date")) {
					  tables += ", dt_date AS dt_date2, dt_period_of_time";
					  where_clause += " AND other_clinical_trials.PERIOD_ID=dt_period_of_time.ID AND dt_period_of_time.END_DATE_ID=dt_date2.ID";
				  }
				  query = "(SELECT MAX(dt_date2.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
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
					  where_clause += Make_specific_date_query(true, mode, "demo_pregnancy_data.CONCEPTION_DATE_ID","dt_date1",crit_demo_pregnancy_obj.getCONCEPTION_DATE_YEAR(),
							  crit_demo_pregnancy_obj.getCONCEPTION_DATE_MONTH(),crit_demo_pregnancy_obj.getCONCEPTION_DATE_DAY());					  		
				  
				  } else if(!(crit_demo_pregnancy_obj.getCONCEPTION_period_begin_year()).isEmpty() || !(crit_demo_pregnancy_obj.getCONCEPTION_period_end_year()).isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  where_clause += Make_begin_end_date_query (true, mode,"demo_pregnancy_data.CONCEPTION_DATE_ID", "dt_date1",crit_demo_pregnancy_obj.getCONCEPTION_period_begin_year(), crit_demo_pregnancy_obj.getCONCEPTION_period_begin_month(), crit_demo_pregnancy_obj.getCONCEPTION_period_begin_day(), crit_demo_pregnancy_obj.getCONCEPTION_period_end_year(), crit_demo_pregnancy_obj.getCONCEPTION_period_end_month(),
							  crit_demo_pregnancy_obj.getCONCEPTION_period_end_day());			  
				  
				  } else if(!(crit_demo_pregnancy_obj.getCONCEPTION_until_date_year()).isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  where_clause += Make_begin_end_date_query (true, mode,"demo_pregnancy_data.CONCEPTION_DATE_ID","dt_date1", "1800", "1", "1", crit_demo_pregnancy_obj.getCONCEPTION_until_date_year(), 
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
					  where_clause += Make_specific_date_query(true, mode, "demo_pregnancy_data.OUTCOME_DATE_ID","dt_date2", crit_demo_pregnancy_obj.getOUTCOME_DATE_YEAR(),
							  crit_demo_pregnancy_obj.getOUTCOME_DATE_MONTH(),crit_demo_pregnancy_obj.getOUTCOME_DATE_DAY());	
					 
				  	//} else if(!(crit_demo_pregnancy_obj.getOUTCOME_period_begin_year() + crit_demo_pregnancy_obj.getOUTCOME_period_begin_month() + crit_demo_pregnancy_obj.getOUTCOME_period_begin_day()).isEmpty()) {
				  	} else if(!crit_demo_pregnancy_obj.getOUTCOME_period_begin_year().isEmpty() || !crit_demo_pregnancy_obj.getOUTCOME_period_end_year().isEmpty()) {//+ crit_demo_pregnancy_obj.getOUTCOME_period_begin_month() + crit_demo_pregnancy_obj.getOUTCOME_period_begin_day()).isEmpty()) {

				  		tables += ", dt_date as dt_date2"; 
				  		where_clause += Make_begin_end_date_query (true, mode,"demo_pregnancy_data.OUTCOME_DATE_ID", "dt_date2", crit_demo_pregnancy_obj.getOUTCOME_period_begin_year(), crit_demo_pregnancy_obj.getOUTCOME_period_begin_month(), crit_demo_pregnancy_obj.getOUTCOME_period_begin_day(), crit_demo_pregnancy_obj.getOUTCOME_period_end_year(), crit_demo_pregnancy_obj.getOUTCOME_period_end_month(),
								  crit_demo_pregnancy_obj.getOUTCOME_period_end_day()); 			  
					
				  	} else if(!(crit_demo_pregnancy_obj.getOUTCOME_until_date_year() + crit_demo_pregnancy_obj.getOUTCOME_until_date_month() + crit_demo_pregnancy_obj.getOUTCOME_until_date_day()).isEmpty()) {
				  		tables += ", dt_date as dt_date2";  
				  		where_clause += Make_begin_end_date_query (true, mode,"demo_pregnancy_data.OUTCOME_DATE_ID", "dt_date2", "1800", "1", "1", crit_demo_pregnancy_obj.getOUTCOME_until_date_year(), 
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
				
				  	/*if(!crit_demo_pregnancy_obj.getMinCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.UID HAVING COUNT(*) >= "+crit_demo_pregnancy_obj.getMinCount();
					}
				  	
				  	if(!crit_demo_pregnancy_obj.getMaxCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.UID HAVING COUNT(*) >= "+crit_demo_pregnancy_obj.getMaxCount();
					}*/
				  	
				  	if(crit_demo_pregnancy_obj.getTypeNested().equals("outcome")) {
				  		if(!tables.contains("dt_date2")) {
				  			tables += ", dt_date as dt_date2";
				  			where_clause += " AND demo_pregnancy_data.OUTCOME_DATE_ID=dt_date2.ID";
				  		}	
				  		if(isMax) query = "(SELECT MAX(dt_date2.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
						else query = "(SELECT MIN(dt_date2.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
				  		if(!crit_demo_pregnancy_obj.getOutcomeYearsNested().isEmpty()) {
							query += " + ("+crit_demo_pregnancy_obj.getOutcomeYearsNested()+")";
						}  
				  	}
				  	else if(crit_demo_pregnancy_obj.getTypeNested().equals("conception")) {
				  		if(!tables.contains("dt_date1")) {
				  			tables += ", dt_date as dt_date1";
				  			where_clause += " AND demo_pregnancy_data.CONCEPTION_DATE_ID=dt_date1.ID";
				  		}
				  		if(isMax) query = "(SELECT MAX(dt_date1.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
						else query = "(SELECT MIN(dt_date1.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
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
				  			query1 = "(SELECT MAX(dt_date1.YEAR) FROM " + tables1 + " WHERE patient.UID=outerr.UID AND " + where_clause1 +")";
				  			query2 = "(SELECT MAX(dt_date2.YEAR) FROM " + tables2 + " WHERE patient.UID=outerr.UID AND " + where_clause2 +")";
				  		}
				  		else {
				  			query1 = "(SELECT MIN(dt_date1.YEAR) FROM " + tables1 + " WHERE patient.UID=outerr.UID AND " + where_clause1 +")";
				  			query2 = "(SELECT MIN(dt_date2.YEAR) FROM " + tables2 + " WHERE patient.UID=outerr.UID AND " + where_clause2 +")";
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
				  		where_clause += " GROUP BY patient.UID HAVING COUNT(*) >= "+crit_lifestyle_smoking_obj.getCount();
				  }*/
					  
				  if(isMax) {
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date AS dt_date1, dt_period_of_time";
						  where_clause += " AND lifestyle_smoking.PERIOD_ID=dt_period_of_time.ID AND dt_period_of_time.START_DATE_ID=dt_date1.ID";
					  }
					  query = "(SELECT MAX(dt_date1.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
				  }
				  else {
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date AS dt_date2, dt_period_of_time";
						  where_clause += " AND lifestyle_smoking.PERIOD_ID=dt_period_of_time.ID AND dt_period_of_time.END_DATE_ID=dt_date2.ID";
					  }
					  query = "(SELECT MIN(dt_date2.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
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
				  
				  
						  
				  
				  /*query = "SELECT DISTINCT patient.UID " + 
						  "FROM patient, cond_symptom, voc_symptom_sign, dt_date, voc_direction, voc_confirmation " + 
						  "WHERE patient.ID = cond_symptom.PATIENT_ID " + 
						  "AND cond_symptom.CONDITION_ID = voc_symptom_sign.ID " +
						  "AND " + Make_OR_of_CODES("voc_symptom_sign.CODE", crit_cond_symptom_obj.getVoc_symptom_sign_CODE());*/
				  
				  if(!(crit_cond_symptom_obj.getObserve_exact_date_YEAR()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_specific_date_query(true, mode, "cond_symptom.OBSERVE_DATE_ID","dt_date",crit_cond_symptom_obj.getObserve_exact_date_YEAR(), //check cond_symptom.OBSERVE_DATE_ID
						crit_cond_symptom_obj.getObserve_exact_date_MONTH(), crit_cond_symptom_obj.getObserve_exact_date_DAY());
						 
				  }else if(!crit_cond_symptom_obj.getObserve_period_begin_year().isEmpty() || !crit_cond_symptom_obj.getObserve_period_end_year().isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (true, mode,"cond_symptom.OBSERVE_DATE_ID", "dt_date",crit_cond_symptom_obj.getObserve_period_begin_year(), crit_cond_symptom_obj.getObserve_period_begin_month(), crit_cond_symptom_obj.getObserve_period_begin_day(), crit_cond_symptom_obj.getObserve_period_end_year() ,crit_cond_symptom_obj.getObserve_period_end_month(),
									  crit_cond_symptom_obj.getObserve_period_end_day()); 
							  
				  }else if(!(crit_cond_symptom_obj.getObserve_until_date_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (true, mode,"cond_symptom.OBSERVE_DATE_ID","dt_date", "1800", "1", "1",crit_cond_symptom_obj.getObserve_until_date_year(), 
									  crit_cond_symptom_obj.getObserve_until_date_month(), crit_cond_symptom_obj.getObserve_until_date_day()); 
				  }
				  
				  if(!crit_cond_symptom_obj.getStatement().isEmpty()){
					  tables += ", voc_confirmation";
					  where_clause += " AND cond_symptom.STMT_ID=voc_confirmation.ID " +
				  				 "AND voc_confirmation.CODE='"+crit_cond_symptom_obj.getStatement() + "'";
				  }
				  
				  where_clause += " AND cond_symptom.STMT_ID=1";
				  
				  /*if(!crit_cond_symptom_obj.getCount().isEmpty()) {
					  where_clause += " GROUP BY patient.UID HAVING COUNT(*) >= "+crit_cond_symptom_obj.getCount();
				  }*/
				  
				  if(!tables.contains("dt_date")) {
					  tables += ", dt_date";
					  where_clause += " AND cond_symptom.OBSERVE_DATE_ID=dt_date.ID";
				  }
				  
				  if(isMax) query = "(SELECT MAX(dt_date.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
				  else query = "(SELECT MIN(dt_date.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
				  
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
					  where_clause += Make_specific_date_query(true, mode, "cond_diagnosis.DIAGNOSIS_DATE_ID","dt_date",condition_diagnosis_obj.getDate_exact_year(),
							  condition_diagnosis_obj.getDate_exact_month(),condition_diagnosis_obj.getDate_exact_day());
					 			  		
				  } else if(!(condition_diagnosis_obj.getDate_interval_start_year()).isEmpty() || !condition_diagnosis_obj.getDate_interval_end_year().isEmpty()) {
					 tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (true, mode,"cond_diagnosis.DIAGNOSIS_DATE_ID", "dt_date",condition_diagnosis_obj.getDate_interval_start_year(), condition_diagnosis_obj.getDate_interval_start_month(), condition_diagnosis_obj.getDate_interval_start_day(), condition_diagnosis_obj.getDate_interval_end_year(), condition_diagnosis_obj.getDate_interval_end_month(),
							  condition_diagnosis_obj.getDate_interval_end_day());  
				  } else if(!(condition_diagnosis_obj.getDate_until_year() ).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (true, mode,"cond_diagnosis.DIAGNOSIS_DATE_ID","dt_date", "1800", "1", "1", condition_diagnosis_obj.getDate_until_year(), 
							  condition_diagnosis_obj.getDate_until_month(), condition_diagnosis_obj.getDate_until_day()); 
				  }
				  
				  if(!condition_diagnosis_obj.getStatement().isEmpty()){ 
					  tables += ", voc_confirmation";
					  where_clause += " AND cond_diagnosis.STMT_ID=voc_confirmation.ID AND voc_confirmation.CODE='"+condition_diagnosis_obj.getStatement() + "'";
				  }
				  where_clause += " AND cond_diagnosis.STMT_ID=1";
				  
				  /*if(!condition_diagnosis_obj.getCount().isEmpty()) {
					  where_clause += " GROUP BY patient.UID HAVING COUNT(*) >= "+condition_diagnosis_obj.getCount();
				  }*/
				  
				  if(!tables.contains("dt_date")) {
					  tables += ", dt_date";
					  where_clause += " AND cond_diagnosis.DIAGNOSIS_DATE_ID=dt_date.ID";
				  }
				 
				  if(isMax) query = "(SELECT MAX(dt_date.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
				  else query = "(SELECT MIN(dt_date.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
				  
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
				  		where_clause += " GROUP BY patient.UID HAVING COUNT(*) >= "+crit_interv_medication_obj.getCount();
				  }*/
				  
				  if(isMax) {
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date AS dt_date1, dt_period_of_time";
						  where_clause += " AND interv_medication.PERIOD_ID=dt_period_of_time.ID AND dt_period_of_time.START_DATE_ID=dt_date1.ID";
					  }
					  query = "(SELECT MAX(dt_date1.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
				  }
				  else {
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date AS dt_date2, dt_period_of_time";
						  where_clause += " AND interv_medication.PERIOD_ID=dt_period_of_time.ID AND dt_period_of_time.END_DATE_ID=dt_date2.ID";
					  }
					  query = "(SELECT MIN(dt_date2.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
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
				  		where_clause += " GROUP BY patient.UID HAVING COUNT(*) >= "+crit_interv_chemotherapy_obj.getCount();
				  }*/
				  
				  if(isMax) {
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date AS dt_date1, dt_period_of_time";
						  where_clause += " AND interv_chemotherapy.PERIOD_ID=dt_period_of_time.ID AND dt_period_of_time.START_DATE_ID=dt_date1.ID";
					  }
					  query = "(SELECT MAX(dt_date1.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
				  }
				  else {
					  if(!tables.contains("dt_date")) {
						  tables += ", dt_date AS dt_date2, dt_period_of_time";
						  where_clause += " AND interv_chemotherapy.PERIOD_ID=dt_period_of_time.ID AND dt_period_of_time.END_DATE_ID=dt_date2.ID";
					  }
					  query = "(SELECT MIN(dt_date2.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
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
					  where_clause += Make_specific_date_query(true, mode, "interv_surgery.SURGERY_DATE_ID","dt_date",crit_interv_surgery_obj.getSurgery_exact_date_year(), //check cond_symptom.OBSERVE_DATE_ID
							  crit_interv_surgery_obj.getSurgery_exact_date_month(), crit_interv_surgery_obj.getSurgery_exact_date_day());
					  
				  } else if(!crit_interv_surgery_obj.getSurgery_period_begin_year().isEmpty() || !(crit_interv_surgery_obj.getSurgery_period_end_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (true, mode,"interv_surgery.SURGERY_DATE_ID", "dt_date",crit_interv_surgery_obj.getSurgery_period_begin_year(),
							  crit_interv_surgery_obj.getSurgery_period_begin_month(), crit_interv_surgery_obj.getSurgery_period_begin_day(), crit_interv_surgery_obj.getSurgery_period_end_year() , 
							  crit_interv_surgery_obj.getSurgery_period_end_month(), crit_interv_surgery_obj.getSurgery_period_end_day()); 
					  
				  } else if(!(crit_interv_surgery_obj.getSurgery_until_date_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (true, mode,"interv_surgery.SURGERY_DATE_ID","dt_date", "1800", "1", "1", crit_interv_surgery_obj.getSurgery_until_date_year(), 
							  crit_interv_surgery_obj.getSurgery_until_date_month(), crit_interv_surgery_obj.getSurgery_until_date_day()); 
				  }
				  
				  if(!crit_interv_surgery_obj.getStatement().isEmpty()) {
					  tables += ", voc_confirmation AS conf_2";
					  where_clause += " AND interv_surgery.STMT_ID=conf_2.ID AND conf_2.CODE='" + crit_interv_surgery_obj.getStatement() + "'";
				  }
				  
				  where_clause += " AND interv_surgery.STMT_ID=1";
				  
				  /*if(!crit_interv_surgery_obj.getCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.UID HAVING COUNT(*) >= "+crit_interv_surgery_obj.getCount();
				  }*/
				  
				  if(!tables.contains("dt_date")) {
					  tables += ", dt_date";
					  where_clause += " AND interv_surgery.SURGERY_DATE_ID=dt_date.ID";
				  }
				 
				  if(isMax) query = "(SELECT MAX(dt_date.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
				  else query = "(SELECT MIN(dt_date.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
				  
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
					  where_clause += Make_specific_date_query(true, mode, "exam_lab_test.SAMPLE_DATE_ID","dt_date",examination_lab_test_obj.getSample_period_of_time_exact_year(), 
							  			examination_lab_test_obj.getSample_period_of_time_exact_month(), examination_lab_test_obj.getSample_period_of_time_exact_day());					  		
				  } 
				  
				  else if(!(examination_lab_test_obj.getSample_period_of_time_interval_start_year()).isEmpty() || !(examination_lab_test_obj.getSample_period_of_time_interval_end_year()).isEmpty()) {
							 
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (true, mode,"exam_lab_test.SAMPLE_DATE_ID", "dt_date",examination_lab_test_obj.getSample_period_of_time_interval_start_year(), 
										examination_lab_test_obj.getSample_period_of_time_interval_start_month(), examination_lab_test_obj.getSample_period_of_time_interval_start_day(),
										examination_lab_test_obj.getSample_period_of_time_interval_end_year(), examination_lab_test_obj.getSample_period_of_time_interval_end_month(),
										examination_lab_test_obj.getSample_period_of_time_interval_end_day()); 			  
						  } 
				  
				  else if(!(examination_lab_test_obj.getSample_period_of_time_until_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (true, mode,"exam_lab_test.SAMPLE_DATE_ID","dt_date", "1800", "1", "1", examination_lab_test_obj.getSample_period_of_time_until_year(), examination_lab_test_obj.getSample_period_of_time_until_month(),
									  examination_lab_test_obj.getSample_period_of_time_until_day()); 
						  }
				  
				  /*if(!examination_lab_test_obj.getCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.UID HAVING COUNT(*) >= "+examination_lab_test_obj.getCount();
				  }*/
					//results_of_one_Criterion=DBServiceCRUD.getDataFromDB(query); 
					//System.out.println("We executed: "+crit_exam_lab_test_obj.criterion_name+"\nThe Query is: "+query); 
				  
				  if(!tables.contains("dt_date")) {
					  tables += ", dt_date";
					  where_clause += " AND exam_lab_test.SAMPLE_DATE_ID=dt_date.ID";
				  }
				  
				  if(isMax) query = "(SELECT MAX(dt_date.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
				  else query = "(SELECT MIN(dt_date.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
				  
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
					  where_clause += Make_specific_date_query(true, mode, "exam_biopsy.BIOPSY_DATE_ID","dt_date",examination_biopsy_obj.getBiopsy_period_of_time_exact_year(), 
									  examination_biopsy_obj.getBiopsy_period_of_time_exact_month(), examination_biopsy_obj.getBiopsy_period_of_time_exact_day());					  		
						  } else if(!examination_biopsy_obj.getBiopsy_period_of_time_interval_start_year().isEmpty() || !(examination_biopsy_obj.getBiopsy_period_of_time_interval_end_year()).isEmpty()) {
							  tables += ", dt_date";
							  where_clause += Make_begin_end_date_query (true, mode,"exam_biopsy.BIOPSY_DATE_ID", "dt_date",examination_biopsy_obj.getBiopsy_period_of_time_interval_start_year(), 
									  examination_biopsy_obj.getBiopsy_period_of_time_interval_start_month(), examination_biopsy_obj.getBiopsy_period_of_time_interval_start_day(),
									  examination_biopsy_obj.getBiopsy_period_of_time_interval_end_year(), examination_biopsy_obj.getBiopsy_period_of_time_interval_end_month(),
									  examination_biopsy_obj.getBiopsy_period_of_time_interval_end_day()); 			  
						  } else if(!(examination_biopsy_obj.getBiopsy_period_of_time_until_year()).isEmpty()) {
							  tables += ", dt_date";
							  where_clause += Make_begin_end_date_query (true, mode,"exam_biopsy.BIOPSY_DATE_ID","dt_date", "1800", "1", "1", examination_biopsy_obj.getBiopsy_period_of_time_until_year(), examination_biopsy_obj.getBiopsy_period_of_time_until_month(),
									  examination_biopsy_obj.getBiopsy_period_of_time_until_day()); 
						  }
				  
				  /*if(!examination_biopsy_obj.getCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.UID HAVING COUNT(*) >= "+examination_biopsy_obj.getCount();
				  }*/
				  
				  if(!tables.contains("dt_date")) {
					  tables += ", dt_date";
					  where_clause += " AND exam_biopsy.BIOPSY_DATE_ID=dt_date.ID";
				  }
				 
				  if(isMax) query = "(SELECT MAX(dt_date.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
				  else query = "(SELECT MIN(dt_date.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
				  
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
					  where_clause += Make_specific_date_query(true, mode, "exam_medical_imaging_test.TEST_DATE_ID","dt_date",examination_medical_imaging_test_obj.getTest_period_of_time_exact_year(), 
							  examination_medical_imaging_test_obj.getTest_period_of_time_exact_month(), examination_medical_imaging_test_obj.getTest_period_of_time_exact_day());					  		
				  } else if(!examination_medical_imaging_test_obj.getTest_period_of_time_interval_start_year().isEmpty() || !(examination_medical_imaging_test_obj.getTest_period_of_time_interval_end_year()).isEmpty()) {
					  tables += ", exam_lab_test, dt_date";
					  where_clause += Make_begin_end_date_query (true, mode,"exam_medical_imaging_test.TEST_DATE_ID", "dt_date",examination_medical_imaging_test_obj.getTest_period_of_time_interval_start_year(), 
							  examination_medical_imaging_test_obj.getTest_period_of_time_interval_start_month(), examination_medical_imaging_test_obj.getTest_period_of_time_interval_start_day(),
							  examination_medical_imaging_test_obj.getTest_period_of_time_interval_end_year(), examination_medical_imaging_test_obj.getTest_period_of_time_interval_end_month(),
							  examination_medical_imaging_test_obj.getTest_period_of_time_interval_end_day()); 			  
				  } else if(!(examination_medical_imaging_test_obj.getTest_period_of_time_until_year()).isEmpty()) {
					  tables += ", exam_lab_test, dt_date";
					  where_clause += Make_begin_end_date_query (true, mode,"exam_medical_imaging_test.TEST_DATE_ID","dt_date", "1800", "1", "1", examination_medical_imaging_test_obj.getTest_period_of_time_until_year(), examination_medical_imaging_test_obj.getTest_period_of_time_until_month(),
							  examination_medical_imaging_test_obj.getTest_period_of_time_until_day()); 
				  }
				  
				  /*if(!examination_medical_imaging_test_obj.getCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.UID HAVING COUNT(*) >= "+examination_medical_imaging_test_obj.getCount();
				  }*/
				  
				  if(!tables.contains("dt_date")) {
					  tables += ", dt_date";
					  where_clause += " AND exam_medical_imaging_test.TEST_DATE_ID=dt_date.ID";
				  }
				 
				  if(isMax) query = "(SELECT MAX(dt_date.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
				  else query = "(SELECT MIN(dt_date.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
				  
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
					  where_clause += Make_specific_date_query(true, mode, "exam_questionnaire_score.QUESTIONNAIRE_DATE_ID","dt_date",examination_questionnaire_score_obj.getQuestionnaire_period_of_time_exact_year(), 
							  examination_questionnaire_score_obj.getQuestionnaire_period_of_time_exact_month(), examination_questionnaire_score_obj.getQuestionnaire_period_of_time_exact_day());					  		
				  } else if(!examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_start_year().isEmpty() || !(examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_end_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (true, mode,"exam_questionnaire_score.QUESTIONNAIRE_DATE_ID", "dt_date",examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_start_year(), 
							  examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_start_month(), examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_start_day(),
							  examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_end_year(), examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_end_month(),
							  examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_end_day()); 			  
				  } else if(!(examination_questionnaire_score_obj.getQuestionnaire_period_of_time_until_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (true, mode,"exam_questionnaire_score.QUESTIONNAIRE_DATE_ID","dt_date", "1800", "1", "1", examination_questionnaire_score_obj.getQuestionnaire_period_of_time_until_year(), examination_questionnaire_score_obj.getQuestionnaire_period_of_time_until_month(),
							  examination_questionnaire_score_obj.getQuestionnaire_period_of_time_until_day()); 
				  }
				  
				  /*if(!examination_questionnaire_score_obj.getCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.UID HAVING COUNT(*) >= "+examination_questionnaire_score_obj.getCount();
				  }*/
				  
				  if(!tables.contains("dt_date")) {
					  tables += ", dt_date";
					  where_clause += " AND exam_questionnaire_score.QUESTIONNAIRE_DATE_ID=dt_date.ID";
				  }
				 
				  if(isMax) query = "(SELECT MAX(dt_date.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
				  else query = "(SELECT MIN(dt_date.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
				  
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
				  }
				  
				  if(!(examination_essdai_domain_obj.getQuestionnaire_period_of_time_exact_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_specific_date_query(true, mode, "exam_essdai_domain.QUESTIONNAIRE_DATE_ID","dt_date",examination_essdai_domain_obj.getQuestionnaire_period_of_time_exact_year(), 
							  examination_essdai_domain_obj.getQuestionnaire_period_of_time_exact_month(), examination_essdai_domain_obj.getQuestionnaire_period_of_time_exact_day());					  		
				  } else if(!examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_start_year().isEmpty() || !(examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_end_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (true, mode,"exam_essdai_domain.QUESTIONNAIRE_DATE_ID", "dt_date",examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_start_year(), 
							  examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_start_month(), examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_start_day(),
							  examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_end_year(), examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_end_month(),
							  examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_end_day()); 			  
				  } else if(!(examination_essdai_domain_obj.getQuestionnaire_period_of_time_until_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (true, mode,"exam_essdai_domain.QUESTIONNAIRE_DATE_ID","dt_date", "1800", "1", "1", examination_essdai_domain_obj.getQuestionnaire_period_of_time_until_year(), examination_essdai_domain_obj.getQuestionnaire_period_of_time_until_month(),
							  examination_essdai_domain_obj.getQuestionnaire_period_of_time_until_day()); 
				  }
				  
				  /*if(!examination_essdai_domain_obj.getCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.UID HAVING COUNT(*) >= "+examination_essdai_domain_obj.getCount();
				  }*/
				  
				  if(!tables.contains("dt_date")) {
					  tables += ", dt_date";
					  where_clause += " AND exam_essdai_domain.QUESTIONNAIRE_DATE_ID=dt_date.ID";
				  }
				 
				  if(isMax) query = "(SELECT MAX(dt_date.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
				  else query = "(SELECT MIN(dt_date.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
				  
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
					  where_clause += Make_specific_date_query(true, mode, "exam_caci_condition.QUESTIONNAIRE_DATE_ID","dt_date",examination_caci_condition_obj.getQuestionnaire_period_of_time_exact_year(), 
							  examination_caci_condition_obj.getQuestionnaire_period_of_time_exact_month(), examination_caci_condition_obj.getQuestionnaire_period_of_time_exact_day());					  		
				  } else if(!examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_start_year().isEmpty() || !(examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_end_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (true, mode,"exam_caci_condition.QUESTIONNAIRE_DATE_ID", "dt_date",examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_start_year(), 
							  examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_start_month(), examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_start_day(),
							  examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_end_year(), examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_end_month(),
							  examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_end_day()); 			  
				  } else if(!(examination_caci_condition_obj.getQuestionnaire_period_of_time_until_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (true, mode,"exam_caci_condition.QUESTIONNAIRE_DATE_ID","dt_date", "1800", "1", "1", examination_caci_condition_obj.getQuestionnaire_period_of_time_until_year(), examination_caci_condition_obj.getQuestionnaire_period_of_time_until_month(),
							  examination_caci_condition_obj.getQuestionnaire_period_of_time_until_day()); 
				  }
				  
				  /*if(!examination_caci_condition_obj.getCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.UID HAVING COUNT(*) >= "+examination_caci_condition_obj.getCount();
				  }*/
				  
				  if(!tables.contains("dt_date")) {
					  tables += ", dt_date";
					  where_clause += " AND exam_caci_condition.QUESTIONNAIRE_DATE_ID=dt_date.ID";
				  }
				 
				  if(isMax) query = "(SELECT MAX(dt_date.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
				  else query = "(SELECT MIN(dt_date.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
				  
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
				  
				  /*query = "SELECT DISTINCT patient.UID " +
						  "FROM other_healthcare_visit, patient, dt_date, voc_specialist " + //interv_Surgery, dt_date, voc_direction, voc_confirmation
						  "WHERE patient.ID = other_healthcare_visit.PATIENT_ID AND " + 
						  "other_healthcare_visit.SPECIALIST_ID=voc_specialist.ID " +
				  		  "AND " + Make_OR_of_CODES("voc_specialist.CODE", other_healthcare_visit_obj.getSpecialist());*/
				 // System.out.println("Test id: "+other_healthcare_visit_obj.getSpecialist());
				  
				  if(!(other_healthcare_visit_obj.getPeriod_of_time_exact_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_specific_date_query(true, mode, "other_healthcare_visit.DATE_ID","dt_date",other_healthcare_visit_obj.getPeriod_of_time_exact_year(), 
							  other_healthcare_visit_obj.getPeriod_of_time_exact_month(), other_healthcare_visit_obj.getPeriod_of_time_exact_day());					  		
				  } else if(!other_healthcare_visit_obj.getPeriod_of_time_interval_start_year().isEmpty() || !(other_healthcare_visit_obj.getPeriod_of_time_interval_end_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (true, mode,"other_healthcare_visit.DATE_ID", "dt_date",other_healthcare_visit_obj.getPeriod_of_time_interval_start_year(), 
							  other_healthcare_visit_obj.getPeriod_of_time_interval_start_month(), other_healthcare_visit_obj.getPeriod_of_time_interval_start_day(),
							  other_healthcare_visit_obj.getPeriod_of_time_interval_end_year(), other_healthcare_visit_obj.getPeriod_of_time_interval_end_month(),
							  other_healthcare_visit_obj.getPeriod_of_time_interval_end_day()); 			  
				  } else if(!(other_healthcare_visit_obj.getPeriod_of_time_until_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (true, mode,"other_healthcare_visit.DATE_ID","dt_date", "1800", "1", "1", other_healthcare_visit_obj.getPeriod_of_time_until_year(), other_healthcare_visit_obj.getPeriod_of_time_until_month(),
							  other_healthcare_visit_obj.getPeriod_of_time_until_day()); 
				  }
				  
				  /*if(!other_healthcare_visit_obj.getCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.UID HAVING COUNT(*) >= "+other_healthcare_visit_obj.getCount();
				  }*/
				  
				  if(!tables.contains("dt_date")) {
					  tables += ", dt_date";
					  where_clause += " AND other_healthcare_visit.DATE_ID=dt_date.ID";
				  }
				 
				  if(isMax) query = "(SELECT MAX(dt_date.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
				  else query = "(SELECT MIN(dt_date.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
				  
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
			  		where_clause += " GROUP BY patient.UID HAVING COUNT(*) >= "+other_clinical_trials_obj.getCount();
			  }*/
			
			if(isMax) {
				  if(!tables.contains("dt_date")) {
					  tables += ", dt_date AS dt_date1, dt_period_of_time";
					  where_clause += " AND other_clinical_trials.PERIOD_ID=dt_period_of_time.ID AND dt_period_of_time.START_DATE_ID=dt_date1.ID";
				  }
				  query = "(SELECT MAX(dt_date1.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
			  }
			  else {
				  if(!tables.contains("dt_date")) {
					  tables += ", dt_date AS dt_date2, dt_period_of_time";
					  where_clause += " AND other_clinical_trials.PERIOD_ID=dt_period_of_time.ID AND dt_period_of_time.END_DATE_ID=dt_date2.ID";
				  }
				  query = "(SELECT MIN(dt_date2.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID AND " + where_clause +")";
			  }
				  
			  if(!other_clinical_trials_obj.getYearsNested().isEmpty()) {
				  query += " + ("+other_clinical_trials_obj.getYearsNested()+")";
			  }

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
				  if(!patient_obj.get_exact_age().isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  Date date = new Date();
					  Object param = new java.sql.Timestamp(date.getTime());
					  String exact_year = ((Timestamp) param).toString().split("-")[0];
					  Integer exact_birth_year = Integer.valueOf(exact_year) - Integer.valueOf(patient_obj.get_exact_age());
					  exact_year = exact_birth_year.toString();
					  where_clause += Make_specific_date_query(true, mode, "patient.DATE_OF_BIRTH_ID","dt_date1",exact_year,"","");
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
					  where_clause += Make_begin_end_date_query (true, mode,"patient.DATE_OF_BIRTH_ID", "dt_date1",min_year, "", "", max_year,"",""); 		
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
					  where_clause += Make_begin_end_date_query (true, mode, "patient.DATE_OF_BIRTH_ID", "dt_date1","1800", "1", "1", max_year,"",""); 		
				  }
				  
				  if(!patient_obj.getBirth_period_of_time_exact_year().isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  where_clause += Make_specific_date_query(true, mode, "patient.DATE_OF_BIRTH_ID","dt_date1",patient_obj.getBirth_period_of_time_exact_year(),
							  patient_obj.getBirth_period_of_time_exact_month(),patient_obj.getBirth_period_of_time_exact_day());					  		
				  }else if(!patient_obj.getBirth_period_of_time_interval_start_year().isEmpty() || !patient_obj.getBirth_period_of_time_interval_end_year().isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  where_clause += Make_begin_end_date_query (true, mode,"patient.DATE_OF_BIRTH_ID", "dt_date1",patient_obj.getBirth_period_of_time_interval_start_year(), patient_obj.getBirth_period_of_time_interval_start_month(), patient_obj.getBirth_period_of_time_interval_start_day(), patient_obj.getBirth_period_of_time_interval_end_year(), patient_obj.getBirth_period_of_time_interval_end_month(),
							  patient_obj.getBirth_period_of_time_interval_end_day()); 			  
				  } else if(!patient_obj.getBirth_period_of_time_until_year().isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  where_clause += Make_begin_end_date_query (true, mode,"patient.DATE_OF_BIRTH_ID","dt_date1", "1800", "1", "1", patient_obj.getBirth_period_of_time_until_year(), 
							  patient_obj.getBirth_period_of_time_until_month(), patient_obj.getBirth_period_of_time_until_day()); 
				  }
				  
				  if(!patient_obj.getSymptoms_onset_period_of_time_exact_year().isEmpty()) {
					  tables += ", dt_date as dt_date2";
					  where_clause += Make_specific_date_query(true, mode, "patient.PSS_SYMPTOMS_ONSET_DATE_ID","dt_date2",patient_obj.getSymptoms_onset_period_of_time_exact_year(),
							  patient_obj.getSymptoms_onset_period_of_time_exact_month(),patient_obj.getSymptoms_onset_period_of_time_exact_day());					  		
				  } else if(!patient_obj.getSymptoms_onset_period_of_time_interval_start_year().isEmpty() || !patient_obj.getSymptoms_onset_period_of_time_interval_end_year().isEmpty()) {
					  tables += ", dt_date as dt_date2";
					  where_clause += Make_begin_end_date_query (true, mode,"patient.PSS_SYMPTOMS_ONSET_DATE_ID", "dt_date2",patient_obj.getSymptoms_onset_period_of_time_interval_start_year(), patient_obj.getSymptoms_onset_period_of_time_interval_start_month(), patient_obj.getSymptoms_onset_period_of_time_interval_start_day(), patient_obj.getSymptoms_onset_period_of_time_interval_end_year(), patient_obj.getSymptoms_onset_period_of_time_interval_end_month(),
							  patient_obj.getSymptoms_onset_period_of_time_interval_end_day()); 			  
				  } else if(!patient_obj.getSymptoms_onset_period_of_time_until_year().isEmpty()) {
					  tables += ", dt_date as dt_date2";
					 /* where_clause += Make_until_date_query("patient.PSS_SYMPTOMS_ONSET_DATE_ID", "dt_date2", patient_obj.getSymptoms_onset_period_of_time_until_year(), 
							  patient_obj.getSymptoms_onset_period_of_time_until_month(), patient_obj.getSymptoms_onset_period_of_time_until_day());*/
					  where_clause += Make_begin_end_date_query (true, mode,"patient.PSS_SYMPTOMS_ONSET_DATE_ID","dt_date2", "1800", "1", "1", patient_obj.getSymptoms_onset_period_of_time_until_year(), 
							  patient_obj.getSymptoms_onset_period_of_time_until_month(), patient_obj.getSymptoms_onset_period_of_time_until_day()); 
				  }
				  
				  if(!patient_obj.getDiagnosis_period_of_time_exact_year().isEmpty()) {
					  tables += ", dt_date as dt_date3";
					  where_clause += Make_specific_date_query(true, mode, "patient.PSS_DIAGNOSIS_DATE_ID","dt_date3",patient_obj.getDiagnosis_period_of_time_exact_year(),
							  patient_obj.getDiagnosis_period_of_time_exact_month(),patient_obj.getDiagnosis_period_of_time_exact_day());					  		
				  } else if(!patient_obj.getDiagnosis_period_of_time_interval_start_year().isEmpty() || !patient_obj.getDiagnosis_period_of_time_interval_end_year().isEmpty()) {
					  tables += ", dt_date as dt_date3";
					  where_clause += Make_begin_end_date_query (true, mode,"patient.PSS_DIAGNOSIS_DATE_ID", "dt_date3",patient_obj.getDiagnosis_period_of_time_interval_start_year(), patient_obj.getDiagnosis_period_of_time_interval_start_month(), patient_obj.getDiagnosis_period_of_time_interval_start_day(), patient_obj.getDiagnosis_period_of_time_interval_end_year(), patient_obj.getDiagnosis_period_of_time_interval_end_month(),
							  patient_obj.getDiagnosis_period_of_time_interval_end_day()); 			  
				  } else if(!patient_obj.getDiagnosis_period_of_time_until_year().isEmpty()) {
					  tables += ", dt_date as dt_date3";
					  /*where_clause += Make_until_date_query("patient.PSS_DIAGNOSIS_DATE_ID", "dt_date3", patient_obj.getDiagnosis_period_of_time_until_year(), 
							  patient_obj.getDiagnosis_period_of_time_until_month(), patient_obj.getDiagnosis_period_of_time_until_day());*/
					  where_clause += Make_begin_end_date_query (true, mode,"patient.PSS_DIAGNOSIS_DATE_ID","dt_date3", "1800", "1", "1", patient_obj.getDiagnosis_period_of_time_until_year(), 
							  patient_obj.getDiagnosis_period_of_time_until_month(), patient_obj.getDiagnosis_period_of_time_until_day()); 
				  }
				  
				  if(!patient_obj.getCohort_inclusion_period_of_time_exact_year().isEmpty()) {
					  tables += ", dt_date as dt_date4";
					  where_clause += Make_specific_date_query(true, mode, "patient.COHORT_INCLUSION_DATE_ID","dt_date4",patient_obj.getCohort_inclusion_period_of_time_exact_year(),
							  patient_obj.getCohort_inclusion_period_of_time_exact_month(),patient_obj.getCohort_inclusion_period_of_time_exact_day());					  		
				  } else if(!patient_obj.getCohort_inclusion_period_of_time_interval_start_year().isEmpty() || !patient_obj.getCohort_inclusion_period_of_time_interval_end_year().isEmpty()) {
					  tables += ", dt_date as dt_date4";
					  where_clause += Make_begin_end_date_query (true, mode,"patient.COHORT_INCLUSION_DATE_ID", "dt_date4",patient_obj.getCohort_inclusion_period_of_time_interval_start_year(), patient_obj.getCohort_inclusion_period_of_time_interval_start_month(), patient_obj.getCohort_inclusion_period_of_time_interval_start_day(), patient_obj.getCohort_inclusion_period_of_time_interval_end_year(), patient_obj.getCohort_inclusion_period_of_time_interval_end_month(),
							  patient_obj.getCohort_inclusion_period_of_time_interval_end_day()); 			  
				  } else if(!patient_obj.getCohort_inclusion_period_of_time_until_year().isEmpty()) {
					  tables += ", dt_date as dt_date4";
					  where_clause += Make_begin_end_date_query (true, mode,"patient.COHORT_INCLUSION_DATE_ID","dt_date4", "1800", "1", "1", patient_obj.getCohort_inclusion_period_of_time_until_year(), 
							  patient_obj.getCohort_inclusion_period_of_time_until_month(), patient_obj.getCohort_inclusion_period_of_time_until_day()); 
					  /*where_clause += Make_begin_end_date_query (true, mode,"patient.COHORT_INCLUSION_DATE_ID","dt_date4", "1800", "1", "1", patient_obj.getCohort_inclusion_period_of_time_until_year(), 
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
						  where_clause += Make_begin_end_age_query (true, mode, "patient.DATE_OF_BIRTH_ID", "patient.COHORT_INCLUSION_DATE_ID", "dt_date1", "dt_date4", patient_obj.get_min_age_of_cohort_inclusion(), patient_obj.get_max_age_of_cohort_inclusion()); 	
					  }
					  else where_clause += Make_begin_end_age_query (true, mode, "patient.DATE_OF_BIRTH_ID", "patient.COHORT_INCLUSION_DATE_ID", "dt_date1", "dt_date4", patient_obj.get_min_age_of_cohort_inclusion(), "200");
				  }
				  else if(!patient_obj.get_max_age_of_cohort_inclusion().isEmpty()) {
					  if(!tables.contains("dt_date1")) tables += ", dt_date as dt_date1";
					  if(!tables.contains("dt_date4")) tables += ", dt_date as dt_date4";
					  where_clause += Make_begin_end_age_query (true, mode, "patient.DATE_OF_BIRTH_ID", "patient.COHORT_INCLUSION_DATE_ID", "dt_date1", "dt_date4", "0", patient_obj.get_max_age_of_cohort_inclusion());
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
						  where_clause += Make_begin_end_age_query (true, mode, "patient.DATE_OF_BIRTH_ID", "patient.PSS_DIAGNOSIS_DATE_ID", "dt_date1", "dt_date3", patient_obj.get_min_age_of_diagnosis(), patient_obj.get_max_age_of_diagnosis()); 	
					  }
					  else where_clause += Make_begin_end_age_query (true, mode, "patient.DATE_OF_BIRTH_ID", "patient.PSS_DIAGNOSIS_DATE_ID", "dt_date1", "dt_date3", patient_obj.get_min_age_of_diagnosis(), "200");
				  }
				  else if(!patient_obj.get_max_age_of_diagnosis().isEmpty()) {
					  if(!tables.contains("dt_date1")) tables += ", dt_date as dt_date1";
					  if(!tables.contains("dt_date3")) tables += ", dt_date as dt_date3";
					  where_clause += Make_begin_end_age_query (true, mode, "patient.DATE_OF_BIRTH_ID", "patient.PSS_DIAGNOSIS_DATE_ID", "dt_date1", "dt_date3", "0", patient_obj.get_max_age_of_diagnosis());
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
						  where_clause += Make_begin_end_age_query (true, mode, "patient.DATE_OF_BIRTH_ID", "patient.PSS_SYMPTOMS_ONSET_DATE_ID", "dt_date1", "dt_date2", patient_obj.get_min_age_of_sign(), patient_obj.get_max_age_of_sign()); 	
					  }
					  else where_clause += Make_begin_end_age_query (true, mode, "patient.DATE_OF_BIRTH_ID", "patient.PSS_SYMPTOMS_ONSET_DATE_ID", "dt_date1", "dt_date2", patient_obj.get_min_age_of_sign(), "200");
				  }
				  else if(!patient_obj.get_max_age_of_sign().isEmpty()) {
					  if(!tables.contains("dt_date1")) tables += ", dt_date as dt_date1";
					  if(!tables.contains("dt_date2")) tables += ", dt_date as dt_date2";
					  where_clause += Make_begin_end_age_query (true, mode, "patient.DATE_OF_BIRTH_ID", "patient.PSS_SYMPTOMS_ONSET_DATE_ID", "dt_date1", "dt_date2", "0", patient_obj.get_max_age_of_sign());
				  }
				  
				  if(patient_obj.get_type_nested().equals("birth_date")) {
				  		if(!tables.contains("dt_date1")) {
				  			tables += ", dt_date as dt_date1";
				  			where_clause += " AND patient.DATE_OF_BIRTH_ID=dt_date1.ID";
				  		}	
				  		if(isMax) query = "(SELECT MAX(dt_date1.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID " + where_clause +")";
						else query = "(SELECT MIN(dt_date1.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID " + where_clause +")";
				  		if(!patient_obj.get_years_birth_nested().isEmpty()) {
							query += " + ("+patient_obj.get_years_birth_nested()+")";
						}  
				  	}
				  	
				  else if(patient_obj.get_type_nested().equals("symptoms_onset")) {
				  		if(!tables.contains("dt_date2")) {
				  			tables += ", dt_date as dt_date2";
				  			where_clause += " AND patient.PSS_SYMPTOMS_ONSET_DATE_ID=dt_date2.ID";
				  		}
				  		if(isMax) query = "(SELECT MAX(dt_date2.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID " + where_clause +")";
						else query = "(SELECT MIN(dt_date2.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID " + where_clause +")";
				  		if(!patient_obj.get_years_symptoms_onset_nested().isEmpty()) {
							query += " + ("+patient_obj.get_years_symptoms_onset_nested()+")";
						} 
				  	}
				  
				  else if(patient_obj.get_type_nested().equals("diagnosis")) {
				  		if(!tables.contains("dt_date3")) {
				  			tables += ", dt_date as dt_date3";
				  			where_clause += " AND patient.PSS_DIAGNOSIS_DATE_ID=dt_date3.ID";
				  		}	
				  		if(isMax) query = "(SELECT MAX(dt_date3.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID " + where_clause +")";
						else query = "(SELECT MIN(dt_date3.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID " + where_clause +")";
				  		if(!patient_obj.get_years_diagnosis_nested().isEmpty()) {
							query += " + ("+patient_obj.get_years_diagnosis_nested()+")";
						}  
				  	}
				  	
				  else if(patient_obj.get_type_nested().equals("cohort_inclusion")) {
				  		if(!tables.contains("dt_date4")) {
				  			tables += ", dt_date as dt_date4";
				  			where_clause += " AND patient.COHORT_INCLUSION_DATE_ID=dt_date4.ID";
				  		}
				  		if(isMax) query = "(SELECT MAX(dt_date4.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID " + where_clause +")";
						else query = "(SELECT MIN(dt_date4.YEAR) FROM " + tables + " WHERE patient.UID=outerr.UID " + where_clause +")";
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
    
    private String createQuery(ArrayList<Criterion> list_of_criterions, boolean mode, String results_of_all_Criterions) throws JSONException, JsonParseException, JsonMappingException, IOException {
    	String results_of_one_Criterion="";
    	for(int i=0; i<list_of_criterions.size(); i++) {
    		criterionResponseInfos = new JSONObject();
			Criterion current_Criterion=list_of_criterions.get(i); //current_criterion
			if(patients_found) {
			if(!canUseCriterion(current_Criterion)){
				System.out.println("Criterion " + current_Criterion.getCriterion() + " cannot be used.");
				criterionResponseInfos.put("usage", "notused");
				criterionResponseInfos.put("notes", termAndSubterms);
				inclusion_criteria.add(criterionResponseInfos);
				
				System.out.println(criterionResponseInfos);
				continue;
			}
			else System.out.println("Criterion " + current_Criterion.getCriterion() + " can be used.");
			criterionResponseInfos.put("usage", "used");
			criterionResponseInfos.put("notes", termAndSubterms);
			inclusion_criteria.add(criterionResponseInfos);
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
				  String tables = "patient, demo_pregnancy_data";
				  String where_clause = "patient.ID = demo_pregnancy_data.PATIENT_ID";
				  
				  if(!(crit_demo_pregnancy_obj.getCONCEPTION_DATE_YEAR()/* + crit_demo_pregnancy_obj.conception_exact_month + 
				  	crit_demo_pregnancy_obj.conception_exact_day */).isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  where_clause += Make_specific_date_query(true, mode, "demo_pregnancy_data.CONCEPTION_DATE_ID","dt_date1",crit_demo_pregnancy_obj.getCONCEPTION_DATE_YEAR(),
							  crit_demo_pregnancy_obj.getCONCEPTION_DATE_MONTH(),crit_demo_pregnancy_obj.getCONCEPTION_DATE_DAY());					  		
				  
				  } else if(!(crit_demo_pregnancy_obj.getCONCEPTION_period_begin_year()/* + crit_demo_pregnancy_obj.getCONCEPTION_period_begin_month() + crit_demo_pregnancy_obj.getCONCEPTION_period_begin_day()*/).isEmpty() || !(crit_demo_pregnancy_obj.getCONCEPTION_period_end_year()).isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  where_clause += Make_begin_end_date_query (true, mode,"demo_pregnancy_data.CONCEPTION_DATE_ID", "dt_date1",crit_demo_pregnancy_obj.getCONCEPTION_period_begin_year(), crit_demo_pregnancy_obj.getCONCEPTION_period_begin_month(), crit_demo_pregnancy_obj.getCONCEPTION_period_begin_day(), crit_demo_pregnancy_obj.getCONCEPTION_period_end_year(), crit_demo_pregnancy_obj.getCONCEPTION_period_end_month(),
							  crit_demo_pregnancy_obj.getCONCEPTION_period_end_day());			  
				  
				  } else if(!(crit_demo_pregnancy_obj.getCONCEPTION_until_date_year()/* + crit_demo_pregnancy_obj.getCONCEPTION_until_date_month() + crit_demo_pregnancy_obj.getCONCEPTION_until_date_day()*/).isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  where_clause += Make_begin_end_date_query (true, mode,"demo_pregnancy_data.CONCEPTION_DATE_ID","dt_date1", "1800", "1", "1", crit_demo_pregnancy_obj.getCONCEPTION_until_date_year(), 
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
				  
				  if(!crit_demo_pregnancy_obj.outcome_exact_year.isEmpty()) {/* + crit_demo_pregnancy_obj.outcome_exact_month + 
				  	crit_demo_pregnancy_obj.outcome_exact_day).isEmpty()) {*/
					  tables += ", dt_date as dt_date2";
					  where_clause += Make_specific_date_query(true, mode, "demo_pregnancy_data.OUTCOME_DATE_ID","dt_date2", crit_demo_pregnancy_obj.getOUTCOME_DATE_YEAR(),
							  crit_demo_pregnancy_obj.getOUTCOME_DATE_MONTH(),crit_demo_pregnancy_obj.getOUTCOME_DATE_DAY());	
					 
				  	//} else if(!(crit_demo_pregnancy_obj.getOUTCOME_period_begin_year() + crit_demo_pregnancy_obj.getOUTCOME_period_begin_month() + crit_demo_pregnancy_obj.getOUTCOME_period_begin_day()).isEmpty()) {
				  	} else if(!crit_demo_pregnancy_obj.getOUTCOME_period_begin_year().isEmpty() || !crit_demo_pregnancy_obj.getOUTCOME_period_end_year().isEmpty()) {//+ crit_demo_pregnancy_obj.getOUTCOME_period_begin_month() + crit_demo_pregnancy_obj.getOUTCOME_period_begin_day()).isEmpty()) {

				  		tables += ", dt_date as dt_date2"; 
				  		where_clause += Make_begin_end_date_query (true, mode,"demo_pregnancy_data.OUTCOME_DATE_ID", "dt_date2", crit_demo_pregnancy_obj.getOUTCOME_period_begin_year(), crit_demo_pregnancy_obj.getOUTCOME_period_begin_month(), crit_demo_pregnancy_obj.getOUTCOME_period_begin_day(), crit_demo_pregnancy_obj.getOUTCOME_period_end_year(), crit_demo_pregnancy_obj.getOUTCOME_period_end_month(),
								  crit_demo_pregnancy_obj.getOUTCOME_period_end_day()); 			  
					
				  	} else if(!(crit_demo_pregnancy_obj.getOUTCOME_until_date_year() + crit_demo_pregnancy_obj.getOUTCOME_until_date_month() + crit_demo_pregnancy_obj.getOUTCOME_until_date_day()).isEmpty()) {
				  		tables += ", dt_date as dt_date2";  
				  		where_clause += Make_begin_end_date_query (true, mode,"demo_pregnancy_data.OUTCOME_DATE_ID", "dt_date2", "1800", "1", "1", crit_demo_pregnancy_obj.getOUTCOME_until_date_year(), 
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
				  			where_clause += " GROUP BY patient.UID HAVING COUNT(*) BETWEEN "+crit_demo_pregnancy_obj.getMinCount()+" AND "+crit_demo_pregnancy_obj.getMaxCount();
				  		}
				  		else where_clause += " GROUP BY patient.UID HAVING COUNT(*) >= "+crit_demo_pregnancy_obj.getMinCount();
					}
				  	else if(!crit_demo_pregnancy_obj.getMaxCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.UID HAVING COUNT(*) <= "+crit_demo_pregnancy_obj.getMaxCount();
					}
				  	
				  	query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;
				  	
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
						  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
						  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
						  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
						  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
						  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
						  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
						  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
						  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
				  
				  if(!crit_lifestyle_smoking_obj.getMinCount().isEmpty()) {
				  		if(!crit_lifestyle_smoking_obj.getMaxCount().isEmpty()) {
				  			where_clause += " GROUP BY patient.UID HAVING COUNT(*) BETWEEN "+crit_lifestyle_smoking_obj.getMinCount()+" AND "+crit_lifestyle_smoking_obj.getMaxCount();
				  		}
				  		else where_clause += " GROUP BY patient.UID HAVING COUNT(*) >= "+crit_lifestyle_smoking_obj.getMinCount();
				  }
				  else if(!crit_lifestyle_smoking_obj.getMaxCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.UID HAVING COUNT(*) <= "+crit_lifestyle_smoking_obj.getMaxCount();
				  }
				  
				  query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;
					  
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
				  
				  
						  
				  
				  /*query = "SELECT DISTINCT patient.UID " + 
						  "FROM patient, cond_symptom, voc_symptom_sign, dt_date, voc_direction, voc_confirmation " + 
						  "WHERE patient.ID = cond_symptom.PATIENT_ID " + 
						  "AND cond_symptom.CONDITION_ID = voc_symptom_sign.ID " +
						  "AND " + Make_OR_of_CODES("voc_symptom_sign.CODE", crit_cond_symptom_obj.getVoc_symptom_sign_CODE());*/
				  
				  if(!(crit_cond_symptom_obj.getObserve_exact_date_YEAR()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_specific_date_query(true, mode, "cond_symptom.OBSERVE_DATE_ID","dt_date",crit_cond_symptom_obj.getObserve_exact_date_YEAR(), //check cond_symptom.OBSERVE_DATE_ID
						crit_cond_symptom_obj.getObserve_exact_date_MONTH(), crit_cond_symptom_obj.getObserve_exact_date_DAY());
						 
				  }else if(!crit_cond_symptom_obj.getObserve_period_begin_year().isEmpty() || !crit_cond_symptom_obj.getObserve_period_end_year().isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (true, mode,"cond_symptom.OBSERVE_DATE_ID", "dt_date",crit_cond_symptom_obj.getObserve_period_begin_year(), crit_cond_symptom_obj.getObserve_period_begin_month(), crit_cond_symptom_obj.getObserve_period_begin_day(), crit_cond_symptom_obj.getObserve_period_end_year() ,crit_cond_symptom_obj.getObserve_period_end_month(),
									  crit_cond_symptom_obj.getObserve_period_end_day()); 
							  
				  }else if(!(crit_cond_symptom_obj.getObserve_until_date_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (true, mode,"cond_symptom.OBSERVE_DATE_ID","dt_date", "1800", "1", "1",crit_cond_symptom_obj.getObserve_until_date_year(), 
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
				  			where_clause += " GROUP BY patient.UID HAVING COUNT(*) BETWEEN "+crit_cond_symptom_obj.getMinCount()+" AND "+crit_cond_symptom_obj.getMaxCount();
				  		}
				  		else where_clause += " GROUP BY patient.UID HAVING COUNT(*) >= "+crit_cond_symptom_obj.getMinCount();
				  }
				  else if(!crit_cond_symptom_obj.getMaxCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.UID HAVING COUNT(*) <= "+crit_cond_symptom_obj.getMaxCount();
				  }
				  
				  query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;
				  
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  where_clause += Make_specific_date_query(true, mode, "cond_diagnosis.DIAGNOSIS_DATE_ID","dt_date",condition_diagnosis_obj.getDate_exact_year(),
							  condition_diagnosis_obj.getDate_exact_month(),condition_diagnosis_obj.getDate_exact_day());
					 			  		
				  } else if(!(condition_diagnosis_obj.getDate_interval_start_year()).isEmpty() || !condition_diagnosis_obj.getDate_interval_end_year().isEmpty()) {
					 tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (true, mode,"cond_diagnosis.DIAGNOSIS_DATE_ID", "dt_date",condition_diagnosis_obj.getDate_interval_start_year(), condition_diagnosis_obj.getDate_interval_start_month(), condition_diagnosis_obj.getDate_interval_start_day(), condition_diagnosis_obj.getDate_interval_end_year(), condition_diagnosis_obj.getDate_interval_end_month(),
							  condition_diagnosis_obj.getDate_interval_end_day());  
				  } else if(!(condition_diagnosis_obj.getDate_until_year() ).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (true, mode,"cond_diagnosis.DIAGNOSIS_DATE_ID","dt_date", "1800", "1", "1", condition_diagnosis_obj.getDate_until_year(), 
							  condition_diagnosis_obj.getDate_until_month(), condition_diagnosis_obj.getDate_until_day()); 
				  }
				  
				  if(!condition_diagnosis_obj.getStatement().isEmpty()){ 
					  tables += ", voc_confirmation";
					  where_clause += " AND cond_diagnosis.STMT_ID=voc_confirmation.ID AND voc_confirmation.CODE='"+condition_diagnosis_obj.getStatement() + "'";
				  }
				  where_clause += " AND cond_diagnosis.STMT_ID=1";
				  
				  if(!condition_diagnosis_obj.getMinCount().isEmpty()) {
				  		if(!condition_diagnosis_obj.getMaxCount().isEmpty()) {
				  			where_clause += " GROUP BY patient.UID HAVING COUNT(*) BETWEEN "+condition_diagnosis_obj.getMinCount()+" AND "+condition_diagnosis_obj.getMaxCount();
				  		}
				  		else where_clause += " GROUP BY patient.UID HAVING COUNT(*) >= "+condition_diagnosis_obj.getMinCount();
				  }
				  else if(!condition_diagnosis_obj.getMaxCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.UID HAVING COUNT(*) <= "+condition_diagnosis_obj.getMaxCount();
				  }
				  
				  query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;
				  
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
				  
				  if(!crit_interv_medication_obj.getMinCount().isEmpty()) {
				  		if(!crit_interv_medication_obj.getMaxCount().isEmpty()) {
				  			where_clause += " GROUP BY patient.UID HAVING COUNT(*) BETWEEN "+crit_interv_medication_obj.getMinCount()+" AND "+crit_interv_medication_obj.getMaxCount();
				  		}
				  		else where_clause += " GROUP BY patient.UID HAVING COUNT(*) >= "+crit_interv_medication_obj.getMinCount();
				  }
				  else if(!crit_interv_medication_obj.getMaxCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.UID HAVING COUNT(*) <= "+crit_interv_medication_obj.getMaxCount();
				  }

				  query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;
				  
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
				  
				  if(!crit_interv_chemotherapy_obj.getMinCount().isEmpty()) {
				  		if(!crit_interv_chemotherapy_obj.getMaxCount().isEmpty()) {
				  			where_clause += " GROUP BY patient.UID HAVING COUNT(*) BETWEEN "+crit_interv_chemotherapy_obj.getMinCount()+" AND "+crit_interv_chemotherapy_obj.getMaxCount();
				  		}
				  		else where_clause += " GROUP BY patient.UID HAVING COUNT(*) >= "+crit_interv_chemotherapy_obj.getMinCount();
				  }
				  else if(!crit_interv_chemotherapy_obj.getMaxCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.UID HAVING COUNT(*) <= "+crit_interv_chemotherapy_obj.getMaxCount();
				  }
				  
				  query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;
				  
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  where_clause += Make_specific_date_query(true, mode, "interv_surgery.SURGERY_DATE_ID","dt_date",crit_interv_surgery_obj.getSurgery_exact_date_year(), //check cond_symptom.OBSERVE_DATE_ID
							  crit_interv_surgery_obj.getSurgery_exact_date_month(), crit_interv_surgery_obj.getSurgery_exact_date_day());
					  
				  } else if(!crit_interv_surgery_obj.getSurgery_period_begin_year().isEmpty() || !(crit_interv_surgery_obj.getSurgery_period_end_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (true, mode,"interv_surgery.SURGERY_DATE_ID", "dt_date",crit_interv_surgery_obj.getSurgery_period_begin_year(),
							  crit_interv_surgery_obj.getSurgery_period_begin_month(), crit_interv_surgery_obj.getSurgery_period_begin_day(), crit_interv_surgery_obj.getSurgery_period_end_year() , 
							  crit_interv_surgery_obj.getSurgery_period_end_month(), crit_interv_surgery_obj.getSurgery_period_end_day()); 
					  
				  } else if(!(crit_interv_surgery_obj.getSurgery_until_date_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (true, mode,"interv_surgery.SURGERY_DATE_ID","dt_date", "1800", "1", "1", crit_interv_surgery_obj.getSurgery_until_date_year(), 
							  crit_interv_surgery_obj.getSurgery_until_date_month(), crit_interv_surgery_obj.getSurgery_until_date_day()); 
				  }
				  
				  if(!crit_interv_surgery_obj.getStatement().isEmpty()) {
					  tables += ", voc_confirmation AS conf_2";
					  where_clause += " AND interv_surgery.STMT_ID=conf_2.ID AND conf_2.CODE='" + crit_interv_surgery_obj.getStatement() + "'";
				  }
				  
				  where_clause += " AND interv_surgery.STMT_ID=1";
				  
				  if(!crit_interv_surgery_obj.getMinCount().isEmpty()) {
				  		if(!crit_interv_surgery_obj.getMaxCount().isEmpty()) {
				  			where_clause += " GROUP BY patient.UID HAVING COUNT(*) BETWEEN "+crit_interv_surgery_obj.getMinCount()+" AND "+crit_interv_surgery_obj.getMaxCount();
				  		}
				  		else where_clause += " GROUP BY patient.UID HAVING COUNT(*) >= "+crit_interv_surgery_obj.getMinCount();
				  }
				  else if(!crit_interv_surgery_obj.getMaxCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.UID HAVING COUNT(*) <= "+crit_interv_surgery_obj.getMaxCount();
				  }
				  
				  query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;
				  
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  where_clause += Make_specific_date_query(true, mode, "exam_lab_test.SAMPLE_DATE_ID","dt_date",examination_lab_test_obj.getSample_period_of_time_exact_year(), 
							  			examination_lab_test_obj.getSample_period_of_time_exact_month(), examination_lab_test_obj.getSample_period_of_time_exact_day());					  		
				  } 
				  
				  else if(!(examination_lab_test_obj.getSample_period_of_time_interval_start_year()).isEmpty() || !(examination_lab_test_obj.getSample_period_of_time_interval_end_year()).isEmpty()) {
							 
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (true, mode,"exam_lab_test.SAMPLE_DATE_ID", "dt_date",examination_lab_test_obj.getSample_period_of_time_interval_start_year(), 
										examination_lab_test_obj.getSample_period_of_time_interval_start_month(), examination_lab_test_obj.getSample_period_of_time_interval_start_day(),
										examination_lab_test_obj.getSample_period_of_time_interval_end_year(), examination_lab_test_obj.getSample_period_of_time_interval_end_month(),
										examination_lab_test_obj.getSample_period_of_time_interval_end_day()); 			  
						  } 
				  
				  else if(!(examination_lab_test_obj.getSample_period_of_time_until_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (true, mode,"exam_lab_test.SAMPLE_DATE_ID","dt_date", "1800", "1", "1", examination_lab_test_obj.getSample_period_of_time_until_year(), examination_lab_test_obj.getSample_period_of_time_until_month(),
									  examination_lab_test_obj.getSample_period_of_time_until_day()); 
						  }
				  
				  if(!examination_lab_test_obj.getMinCount().isEmpty()) {
				  		if(!examination_lab_test_obj.getMaxCount().isEmpty()) {
				  			where_clause += " GROUP BY patient.UID HAVING COUNT(*) BETWEEN "+examination_lab_test_obj.getMinCount()+" AND "+examination_lab_test_obj.getMaxCount();
				  		}
				  		else where_clause += " GROUP BY patient.UID HAVING COUNT(*) >= "+examination_lab_test_obj.getMinCount();
				  }
				  else if(!examination_lab_test_obj.getMaxCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.UID HAVING COUNT(*) <= "+examination_lab_test_obj.getMaxCount();
				  }
				
				  query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;
				  
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  where_clause += Make_specific_date_query(true, mode, "exam_biopsy.BIOPSY_DATE_ID","dt_date",examination_biopsy_obj.getBiopsy_period_of_time_exact_year(), 
									  examination_biopsy_obj.getBiopsy_period_of_time_exact_month(), examination_biopsy_obj.getBiopsy_period_of_time_exact_day());					  		
						  } else if(!examination_biopsy_obj.getBiopsy_period_of_time_interval_start_year().isEmpty() || !(examination_biopsy_obj.getBiopsy_period_of_time_interval_end_year()).isEmpty()) {
							  tables += ", dt_date";
							  where_clause += Make_begin_end_date_query (true, mode,"exam_biopsy.BIOPSY_DATE_ID", "dt_date",examination_biopsy_obj.getBiopsy_period_of_time_interval_start_year(), 
									  examination_biopsy_obj.getBiopsy_period_of_time_interval_start_month(), examination_biopsy_obj.getBiopsy_period_of_time_interval_start_day(),
									  examination_biopsy_obj.getBiopsy_period_of_time_interval_end_year(), examination_biopsy_obj.getBiopsy_period_of_time_interval_end_month(),
									  examination_biopsy_obj.getBiopsy_period_of_time_interval_end_day()); 			  
						  } else if(!(examination_biopsy_obj.getBiopsy_period_of_time_until_year()).isEmpty()) {
							  tables += ", dt_date";
							  where_clause += Make_begin_end_date_query (true, mode,"exam_biopsy.BIOPSY_DATE_ID","dt_date", "1800", "1", "1", examination_biopsy_obj.getBiopsy_period_of_time_until_year(), examination_biopsy_obj.getBiopsy_period_of_time_until_month(),
									  examination_biopsy_obj.getBiopsy_period_of_time_until_day()); 
						  }
				  
				  if(!examination_biopsy_obj.getMinCount().isEmpty()) {
				  		if(!examination_biopsy_obj.getMaxCount().isEmpty()) {
				  			where_clause += " GROUP BY patient.UID HAVING COUNT(*) BETWEEN "+examination_biopsy_obj.getMinCount()+" AND "+examination_biopsy_obj.getMaxCount();
				  		}
				  		else where_clause += " GROUP BY patient.UID HAVING COUNT(*) >= "+examination_biopsy_obj.getMinCount();
				  }
				  else if(!examination_biopsy_obj.getMaxCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.UID HAVING COUNT(*) <= "+examination_biopsy_obj.getMaxCount();
				  }
				  
				  query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;
				  
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  where_clause += Make_specific_date_query(true, mode, "exam_medical_imaging_test.TEST_DATE_ID","dt_date",examination_medical_imaging_test_obj.getTest_period_of_time_exact_year(), 
							  examination_medical_imaging_test_obj.getTest_period_of_time_exact_month(), examination_medical_imaging_test_obj.getTest_period_of_time_exact_day());					  		
				  } else if(!examination_medical_imaging_test_obj.getTest_period_of_time_interval_start_year().isEmpty() || !(examination_medical_imaging_test_obj.getTest_period_of_time_interval_end_year()).isEmpty()) {
					  tables += ", exam_lab_test, dt_date";
					  where_clause += Make_begin_end_date_query (true, mode,"exam_medical_imaging_test.TEST_DATE_ID", "dt_date",examination_medical_imaging_test_obj.getTest_period_of_time_interval_start_year(), 
							  examination_medical_imaging_test_obj.getTest_period_of_time_interval_start_month(), examination_medical_imaging_test_obj.getTest_period_of_time_interval_start_day(),
							  examination_medical_imaging_test_obj.getTest_period_of_time_interval_end_year(), examination_medical_imaging_test_obj.getTest_period_of_time_interval_end_month(),
							  examination_medical_imaging_test_obj.getTest_period_of_time_interval_end_day()); 			  
				  } else if(!(examination_medical_imaging_test_obj.getTest_period_of_time_until_year()).isEmpty()) {
					  tables += ", exam_lab_test, dt_date";
					  where_clause += Make_begin_end_date_query (true, mode,"exam_medical_imaging_test.TEST_DATE_ID","dt_date", "1800", "1", "1", examination_medical_imaging_test_obj.getTest_period_of_time_until_year(), examination_medical_imaging_test_obj.getTest_period_of_time_until_month(),
							  examination_medical_imaging_test_obj.getTest_period_of_time_until_day()); 
				  }
				  
				  if(!examination_medical_imaging_test_obj.getMinCount().isEmpty()) {
				  		if(!examination_medical_imaging_test_obj.getMaxCount().isEmpty()) {
				  			where_clause += " GROUP BY patient.UID HAVING COUNT(*) BETWEEN "+examination_medical_imaging_test_obj.getMinCount()+" AND "+examination_medical_imaging_test_obj.getMaxCount();
				  		}
				  		else where_clause += " GROUP BY patient.UID HAVING COUNT(*) >= "+examination_medical_imaging_test_obj.getMinCount();
				  }
				  else if(!examination_medical_imaging_test_obj.getMaxCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.UID HAVING COUNT(*) <= "+examination_medical_imaging_test_obj.getMaxCount();
				  }
				  
				  query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;
				  
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  where_clause += " AND exam_questionnaire_score.VALUE="+ examination_questionnaire_score_obj.getExactValue(); // +" OR (exam_questionnaire_score.VALUE<="+examination_questionnaire_score_obj.getExactValue()+" AND exam_questionnaire_score.VALUE2>="+examination_questionnaire_score_obj.getExactValue()+")) ";
				  }
				  
				  if(!examination_questionnaire_score_obj.getRangeMinValue().isEmpty()){
					  	where_clause += " AND exam_questionnaire_score.VALUE>=" + examination_questionnaire_score_obj.getRangeMinValue(); // +" OR exam_questionnaire_score.VALUE2>=" + examination_questionnaire_score_obj.getRangeMinValue() +") "; 
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
					  where_clause += Make_specific_date_query(true, mode, "exam_questionnaire_score.QUESTIONNAIRE_DATE_ID","dt_date",examination_questionnaire_score_obj.getQuestionnaire_period_of_time_exact_year(), 
							  examination_questionnaire_score_obj.getQuestionnaire_period_of_time_exact_month(), examination_questionnaire_score_obj.getQuestionnaire_period_of_time_exact_day());					  		
				  } else if(!examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_start_year().isEmpty() || !(examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_end_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (true, mode,"exam_questionnaire_score.QUESTIONNAIRE_DATE_ID", "dt_date",examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_start_year(), 
							  examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_start_month(), examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_start_day(),
							  examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_end_year(), examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_end_month(),
							  examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_end_day()); 			  
				  } else if(!(examination_questionnaire_score_obj.getQuestionnaire_period_of_time_until_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (true, mode,"exam_questionnaire_score.QUESTIONNAIRE_DATE_ID","dt_date", "1800", "1", "1", examination_questionnaire_score_obj.getQuestionnaire_period_of_time_until_year(), examination_questionnaire_score_obj.getQuestionnaire_period_of_time_until_month(),
							  examination_questionnaire_score_obj.getQuestionnaire_period_of_time_until_day()); 
				  }
				  
				  if(!examination_questionnaire_score_obj.getMinCount().isEmpty()) {
				  		if(!examination_questionnaire_score_obj.getMaxCount().isEmpty()) {
				  			where_clause += " GROUP BY patient.UID HAVING COUNT(*) BETWEEN "+examination_questionnaire_score_obj.getMinCount()+" AND "+examination_questionnaire_score_obj.getMaxCount();
				  		}
				  		else where_clause += " GROUP BY patient.UID HAVING COUNT(*) >= "+examination_questionnaire_score_obj.getMinCount();
				  }
				  else if(!examination_questionnaire_score_obj.getMaxCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.UID HAVING COUNT(*) <= "+examination_questionnaire_score_obj.getMaxCount();
				  }
				  
				  query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;
				  
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
				  
				  /*query = "SELECT DISTINCT patient.UID " +
						  "FROM patient, exam_essdai_domain, voc_essdai_domain, dt_date, voc_activity_level " + 
						  "WHERE patient.ID = exam_essdai_domain.PATIENT_ID AND " + 
						  "exam_essdai_domain.DOMAIN_ID = voc_essdai_domain.ID " +
						  //"voc_essdai_domain.CODE = '"+examination_essdai_domain_obj.getDomain()+"' ";
				  		  "AND " + Make_OR_of_CODES("voc_essdai_domain.CODE", examination_essdai_domain_obj.getDomain());*/
				 
			  
				  if(!(examination_essdai_domain_obj.getActivity_level()).isEmpty()) {
					  tables += ", voc_activity_level";
					  where_clause += " AND exam_essdai_domain.ACTIVITY_LEVEL_ID = voc_activity_level.ID AND " + Make_OR_of_CODES("voc_activity_level.CODE", examination_essdai_domain_obj.getActivity_level());
				  }
				  
				  if(!(examination_essdai_domain_obj.getQuestionnaire_period_of_time_exact_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_specific_date_query(true, mode, "exam_essdai_domain.QUESTIONNAIRE_DATE_ID","dt_date",examination_essdai_domain_obj.getQuestionnaire_period_of_time_exact_year(), 
							  examination_essdai_domain_obj.getQuestionnaire_period_of_time_exact_month(), examination_essdai_domain_obj.getQuestionnaire_period_of_time_exact_day());					  		
				  } else if(!examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_start_year().isEmpty() || !(examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_end_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (true, mode,"exam_essdai_domain.QUESTIONNAIRE_DATE_ID", "dt_date",examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_start_year(), 
							  examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_start_month(), examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_start_day(),
							  examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_end_year(), examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_end_month(),
							  examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_end_day()); 			  
				  } else if(!(examination_essdai_domain_obj.getQuestionnaire_period_of_time_until_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (true, mode,"exam_essdai_domain.QUESTIONNAIRE_DATE_ID","dt_date", "1800", "1", "1", examination_essdai_domain_obj.getQuestionnaire_period_of_time_until_year(), examination_essdai_domain_obj.getQuestionnaire_period_of_time_until_month(),
							  examination_essdai_domain_obj.getQuestionnaire_period_of_time_until_day()); 
				  }
				  
				  if(!examination_essdai_domain_obj.getMinCount().isEmpty()) {
				  		if(!examination_essdai_domain_obj.getMaxCount().isEmpty()) {
				  			where_clause += " GROUP BY patient.UID HAVING COUNT(*) BETWEEN "+examination_essdai_domain_obj.getMinCount()+" AND "+examination_essdai_domain_obj.getMaxCount();
				  		}
				  		else where_clause += " GROUP BY patient.UID HAVING COUNT(*) >= "+examination_essdai_domain_obj.getMinCount();
				  }
				  else if(!examination_essdai_domain_obj.getMaxCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.UID HAVING COUNT(*) <= "+examination_essdai_domain_obj.getMaxCount();
				  }
				  
				  query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;	
				  
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  where_clause += Make_specific_date_query(true, mode, "exam_caci_condition.QUESTIONNAIRE_DATE_ID","dt_date",examination_caci_condition_obj.getQuestionnaire_period_of_time_exact_year(), 
							  examination_caci_condition_obj.getQuestionnaire_period_of_time_exact_month(), examination_caci_condition_obj.getQuestionnaire_period_of_time_exact_day());					  		
				  } else if(!examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_start_year().isEmpty() || !(examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_end_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (true, mode,"exam_caci_condition.QUESTIONNAIRE_DATE_ID", "dt_date",examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_start_year(), 
							  examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_start_month(), examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_start_day(),
							  examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_end_year(), examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_end_month(),
							  examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_end_day()); 			  
				  } else if(!(examination_caci_condition_obj.getQuestionnaire_period_of_time_until_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (true, mode,"exam_caci_condition.QUESTIONNAIRE_DATE_ID","dt_date", "1800", "1", "1", examination_caci_condition_obj.getQuestionnaire_period_of_time_until_year(), examination_caci_condition_obj.getQuestionnaire_period_of_time_until_month(),
							  examination_caci_condition_obj.getQuestionnaire_period_of_time_until_day()); 
				  }
				  
				  if(!examination_caci_condition_obj.getMinCount().isEmpty()) {
				  		if(!examination_caci_condition_obj.getMaxCount().isEmpty()) {
				  			where_clause += " GROUP BY patient.UID HAVING COUNT(*) BETWEEN "+examination_caci_condition_obj.getMinCount()+" AND "+examination_caci_condition_obj.getMaxCount();
				  		}
				  		else where_clause += " GROUP BY patient.UID HAVING COUNT(*) >= "+examination_caci_condition_obj.getMinCount();
				  }
				  else if(!examination_caci_condition_obj.getMaxCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.UID HAVING COUNT(*) <= "+examination_caci_condition_obj.getMaxCount();
				  }
				  
				  query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;
				  
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
				  
				  /*query = "SELECT DISTINCT patient.UID " +
						  "FROM other_healthcare_visit, patient, dt_date, voc_specialist " + //interv_Surgery, dt_date, voc_direction, voc_confirmation
						  "WHERE patient.ID = other_healthcare_visit.PATIENT_ID AND " + 
						  "other_healthcare_visit.SPECIALIST_ID=voc_specialist.ID " +
				  		  "AND " + Make_OR_of_CODES("voc_specialist.CODE", other_healthcare_visit_obj.getSpecialist());*/
				 // System.out.println("Test id: "+other_healthcare_visit_obj.getSpecialist());
				  
				  if(!(other_healthcare_visit_obj.getPeriod_of_time_exact_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_specific_date_query(true, mode, "other_healthcare_visit.DATE_ID","dt_date",other_healthcare_visit_obj.getPeriod_of_time_exact_year(), 
							  other_healthcare_visit_obj.getPeriod_of_time_exact_month(), other_healthcare_visit_obj.getPeriod_of_time_exact_day());					  		
				  } else if(!other_healthcare_visit_obj.getPeriod_of_time_interval_start_year().isEmpty() || !(other_healthcare_visit_obj.getPeriod_of_time_interval_end_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (true, mode,"other_healthcare_visit.DATE_ID", "dt_date",other_healthcare_visit_obj.getPeriod_of_time_interval_start_year(), 
							  other_healthcare_visit_obj.getPeriod_of_time_interval_start_month(), other_healthcare_visit_obj.getPeriod_of_time_interval_start_day(),
							  other_healthcare_visit_obj.getPeriod_of_time_interval_end_year(), other_healthcare_visit_obj.getPeriod_of_time_interval_end_month(),
							  other_healthcare_visit_obj.getPeriod_of_time_interval_end_day()); 			  
				  } else if(!(other_healthcare_visit_obj.getPeriod_of_time_until_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += Make_begin_end_date_query (true, mode,"other_healthcare_visit.DATE_ID","dt_date", "1800", "1", "1", other_healthcare_visit_obj.getPeriod_of_time_until_year(), other_healthcare_visit_obj.getPeriod_of_time_until_month(),
							  other_healthcare_visit_obj.getPeriod_of_time_until_day()); 
				  }
				  
				  if(!other_healthcare_visit_obj.getMinCount().isEmpty()) {
				  		if(!other_healthcare_visit_obj.getMaxCount().isEmpty()) {
				  			where_clause += " GROUP BY patient.UID HAVING COUNT(*) BETWEEN "+other_healthcare_visit_obj.getMinCount()+" AND "+other_healthcare_visit_obj.getMaxCount();
				  		}
				  		else where_clause += " GROUP BY patient.UID HAVING COUNT(*) >= "+other_healthcare_visit_obj.getMinCount();
				  }
				  else if(!other_healthcare_visit_obj.getMaxCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.UID HAVING COUNT(*) <= "+other_healthcare_visit_obj.getMaxCount();
				  }
				  
				  query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;
				  
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
				  }
				  
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
					  
				  };
				  
				  if(!other_family_history_obj.getStatement().isEmpty()) 
					  where_clause += "AND other_family_history.STMT_ID=voc_confirmation.ID " +
				  				 "AND voc_confirmation.CODE='"+other_family_history_obj.getStatement() + "'";
				  
				  where_clause += " AND other_family_history.STMT_ID=1";
				  
				  if(!other_family_history_obj.getMinCount().isEmpty()) {
				  		if(!other_family_history_obj.getMaxCount().isEmpty()) {
				  			where_clause += " GROUP BY patient.UID HAVING COUNT(*) BETWEEN "+other_family_history_obj.getMinCount()+" AND "+other_family_history_obj.getMaxCount();
				  		}
				  		else where_clause += " GROUP BY patient.UID HAVING COUNT(*) >= "+other_family_history_obj.getMinCount();
				  }
				  else if(!other_family_history_obj.getMaxCount().isEmpty()) {
				  		where_clause += " GROUP BY patient.UID HAVING COUNT(*) <= "+other_family_history_obj.getMaxCount();
				  }
				  
				  query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;
				  
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
			
			if(!other_clinical_trials_obj.getMinCount().isEmpty()) {
		  		if(!other_clinical_trials_obj.getMaxCount().isEmpty()) {
		  			where_clause += " GROUP BY patient.UID HAVING COUNT(*) BETWEEN "+other_clinical_trials_obj.getMinCount()+" AND "+other_clinical_trials_obj.getMaxCount();
		  		}
		  		else where_clause += " GROUP BY patient.UID HAVING COUNT(*) >= "+other_clinical_trials_obj.getMinCount();
		  }
		  else if(!other_clinical_trials_obj.getMaxCount().isEmpty()) {
		  		where_clause += " GROUP BY patient.UID HAVING COUNT(*) <= "+other_clinical_trials_obj.getMaxCount();
		  }
			
			query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;
			  
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
				  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
				  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
				  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
				  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
				  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
				  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
				  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
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
				  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient.ID", "outerr.ID");
			  }

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
				  if(!patient_obj.get_exact_age().isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  Date date = new Date();
					  Object param = new java.sql.Timestamp(date.getTime());
					  String exact_year = ((Timestamp) param).toString().split("-")[0];
					  Integer exact_birth_year = Integer.valueOf(exact_year) - Integer.valueOf(patient_obj.get_exact_age());
					  exact_year = exact_birth_year.toString();
					  where_clause += Make_specific_date_query(true, mode, "patient.DATE_OF_BIRTH_ID","dt_date1",exact_year,"","");
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
					  where_clause += Make_begin_end_date_query (true, mode,"patient.DATE_OF_BIRTH_ID", "dt_date1",min_year, "", "", max_year,"",""); 		
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
					  where_clause += Make_begin_end_date_query (true, mode, "patient.DATE_OF_BIRTH_ID", "dt_date1","1800", "1", "1", max_year,"",""); 		
				  }
				  
				  if(!patient_obj.getBirth_period_of_time_exact_year().isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  where_clause += Make_specific_date_query(true, mode, "patient.DATE_OF_BIRTH_ID","dt_date1",patient_obj.getBirth_period_of_time_exact_year(),
							  patient_obj.getBirth_period_of_time_exact_month(),patient_obj.getBirth_period_of_time_exact_day());					  		
				  }else if(!patient_obj.getBirth_period_of_time_interval_start_year().isEmpty() || !patient_obj.getBirth_period_of_time_interval_end_year().isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  where_clause += Make_begin_end_date_query (true, mode,"patient.DATE_OF_BIRTH_ID", "dt_date1",patient_obj.getBirth_period_of_time_interval_start_year(), patient_obj.getBirth_period_of_time_interval_start_month(), patient_obj.getBirth_period_of_time_interval_start_day(), patient_obj.getBirth_period_of_time_interval_end_year(), patient_obj.getBirth_period_of_time_interval_end_month(),
							  patient_obj.getBirth_period_of_time_interval_end_day()); 			  
				  } else if(!patient_obj.getBirth_period_of_time_until_year().isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  where_clause += Make_begin_end_date_query (true, mode,"patient.DATE_OF_BIRTH_ID","dt_date1", "1800", "1", "1", patient_obj.getBirth_period_of_time_until_year(), 
							  patient_obj.getBirth_period_of_time_until_month(), patient_obj.getBirth_period_of_time_until_day()); 
				  }
				  
				  if(!patient_obj.getSymptoms_onset_period_of_time_exact_year().isEmpty()) {
					  tables += ", dt_date as dt_date2";
					  where_clause += Make_specific_date_query(true, mode, "patient.PSS_SYMPTOMS_ONSET_DATE_ID","dt_date2",patient_obj.getSymptoms_onset_period_of_time_exact_year(),
							  patient_obj.getSymptoms_onset_period_of_time_exact_month(),patient_obj.getSymptoms_onset_period_of_time_exact_day());					  		
				  } else if(!patient_obj.getSymptoms_onset_period_of_time_interval_start_year().isEmpty() || !patient_obj.getSymptoms_onset_period_of_time_interval_end_year().isEmpty()) {
					  tables += ", dt_date as dt_date2";
					  where_clause += Make_begin_end_date_query (true, mode,"patient.PSS_SYMPTOMS_ONSET_DATE_ID", "dt_date2",patient_obj.getSymptoms_onset_period_of_time_interval_start_year(), patient_obj.getSymptoms_onset_period_of_time_interval_start_month(), patient_obj.getSymptoms_onset_period_of_time_interval_start_day(), patient_obj.getSymptoms_onset_period_of_time_interval_end_year(), patient_obj.getSymptoms_onset_period_of_time_interval_end_month(),
							  patient_obj.getSymptoms_onset_period_of_time_interval_end_day()); 			  
				  } else if(!patient_obj.getSymptoms_onset_period_of_time_until_year().isEmpty()) {
					  tables += ", dt_date as dt_date2";
					 /* where_clause += Make_until_date_query("patient.PSS_SYMPTOMS_ONSET_DATE_ID", "dt_date2", patient_obj.getSymptoms_onset_period_of_time_until_year(), 
							  patient_obj.getSymptoms_onset_period_of_time_until_month(), patient_obj.getSymptoms_onset_period_of_time_until_day());*/
					  where_clause += Make_begin_end_date_query (true, mode,"patient.PSS_SYMPTOMS_ONSET_DATE_ID","dt_date2", "1800", "1", "1", patient_obj.getSymptoms_onset_period_of_time_until_year(), 
							  patient_obj.getSymptoms_onset_period_of_time_until_month(), patient_obj.getSymptoms_onset_period_of_time_until_day()); 
				  }
				  
				  if(!patient_obj.getDiagnosis_period_of_time_exact_year().isEmpty()) {
					  tables += ", dt_date as dt_date3";
					  where_clause += Make_specific_date_query(true, mode, "patient.PSS_DIAGNOSIS_DATE_ID","dt_date3",patient_obj.getDiagnosis_period_of_time_exact_year(),
							  patient_obj.getDiagnosis_period_of_time_exact_month(),patient_obj.getDiagnosis_period_of_time_exact_day());					  		
				  } else if(!patient_obj.getDiagnosis_period_of_time_interval_start_year().isEmpty() || !patient_obj.getDiagnosis_period_of_time_interval_end_year().isEmpty()) {
					  tables += ", dt_date as dt_date3";
					  where_clause += Make_begin_end_date_query (true, mode,"patient.PSS_DIAGNOSIS_DATE_ID", "dt_date3",patient_obj.getDiagnosis_period_of_time_interval_start_year(), patient_obj.getDiagnosis_period_of_time_interval_start_month(), patient_obj.getDiagnosis_period_of_time_interval_start_day(), patient_obj.getDiagnosis_period_of_time_interval_end_year(), patient_obj.getDiagnosis_period_of_time_interval_end_month(),
							  patient_obj.getDiagnosis_period_of_time_interval_end_day()); 			  
				  } else if(!patient_obj.getDiagnosis_period_of_time_until_year().isEmpty()) {
					  tables += ", dt_date as dt_date3";
					  /*where_clause += Make_until_date_query("patient.PSS_DIAGNOSIS_DATE_ID", "dt_date3", patient_obj.getDiagnosis_period_of_time_until_year(), 
							  patient_obj.getDiagnosis_period_of_time_until_month(), patient_obj.getDiagnosis_period_of_time_until_day());*/
					  where_clause += Make_begin_end_date_query (true, mode,"patient.PSS_DIAGNOSIS_DATE_ID","dt_date3", "1800", "1", "1", patient_obj.getDiagnosis_period_of_time_until_year(), 
							  patient_obj.getDiagnosis_period_of_time_until_month(), patient_obj.getDiagnosis_period_of_time_until_day()); 
				  }
				  
				  if(!patient_obj.getCohort_inclusion_period_of_time_exact_year().isEmpty()) {
					  tables += ", dt_date as dt_date4";
					  where_clause += Make_specific_date_query(true, mode, "patient.COHORT_INCLUSION_DATE_ID","dt_date4",patient_obj.getCohort_inclusion_period_of_time_exact_year(),
							  patient_obj.getCohort_inclusion_period_of_time_exact_month(),patient_obj.getCohort_inclusion_period_of_time_exact_day());					  		
				  } else if(!patient_obj.getCohort_inclusion_period_of_time_interval_start_year().isEmpty() || !patient_obj.getCohort_inclusion_period_of_time_interval_end_year().isEmpty()) {
					  tables += ", dt_date as dt_date4";
					  where_clause += Make_begin_end_date_query (true, mode,"patient.COHORT_INCLUSION_DATE_ID", "dt_date4",patient_obj.getCohort_inclusion_period_of_time_interval_start_year(), patient_obj.getCohort_inclusion_period_of_time_interval_start_month(), patient_obj.getCohort_inclusion_period_of_time_interval_start_day(), patient_obj.getCohort_inclusion_period_of_time_interval_end_year(), patient_obj.getCohort_inclusion_period_of_time_interval_end_month(),
							  patient_obj.getCohort_inclusion_period_of_time_interval_end_day()); 			  
				  } else if(!patient_obj.getCohort_inclusion_period_of_time_until_year().isEmpty()) {
					  tables += ", dt_date as dt_date4";
					  where_clause += Make_begin_end_date_query (true, mode,"patient.COHORT_INCLUSION_DATE_ID","dt_date4", "1800", "1", "1", patient_obj.getCohort_inclusion_period_of_time_until_year(), 
							  patient_obj.getCohort_inclusion_period_of_time_until_month(), patient_obj.getCohort_inclusion_period_of_time_until_day()); 
					  /*where_clause += Make_begin_end_date_query (true, mode,"patient.COHORT_INCLUSION_DATE_ID","dt_date4", "1800", "1", "1", patient_obj.getCohort_inclusion_period_of_time_until_year(), 
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
						  where_clause += Make_begin_end_age_query (true, mode, "patient.DATE_OF_BIRTH_ID", "patient.COHORT_INCLUSION_DATE_ID", "dt_date1", "dt_date4", patient_obj.get_min_age_of_cohort_inclusion(), patient_obj.get_max_age_of_cohort_inclusion()); 	
					  }
					  else where_clause += Make_begin_end_age_query (true, mode, "patient.DATE_OF_BIRTH_ID", "patient.COHORT_INCLUSION_DATE_ID", "dt_date1", "dt_date4", patient_obj.get_min_age_of_cohort_inclusion(), "200");
				  }
				  else if(!patient_obj.get_max_age_of_cohort_inclusion().isEmpty()) {
					  if(!tables.contains("dt_date1")) tables += ", dt_date as dt_date1";
					  if(!tables.contains("dt_date4")) tables += ", dt_date as dt_date4";
					  where_clause += Make_begin_end_age_query (true, mode, "patient.DATE_OF_BIRTH_ID", "patient.COHORT_INCLUSION_DATE_ID", "dt_date1", "dt_date4", "0", patient_obj.get_max_age_of_cohort_inclusion());
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
						  where_clause += Make_begin_end_age_query (true, mode, "patient.DATE_OF_BIRTH_ID", "patient.PSS_DIAGNOSIS_DATE_ID", "dt_date1", "dt_date3", patient_obj.get_min_age_of_diagnosis(), patient_obj.get_max_age_of_diagnosis()); 	
					  }
					  else where_clause += Make_begin_end_age_query (true, mode, "patient.DATE_OF_BIRTH_ID", "patient.PSS_DIAGNOSIS_DATE_ID", "dt_date1", "dt_date3", patient_obj.get_min_age_of_diagnosis(), "200");
				  }
				  else if(!patient_obj.get_max_age_of_diagnosis().isEmpty()) {
					  if(!tables.contains("dt_date1")) tables += ", dt_date as dt_date1";
					  if(!tables.contains("dt_date3")) tables += ", dt_date as dt_date3";
					  where_clause += Make_begin_end_age_query (true, mode, "patient.DATE_OF_BIRTH_ID", "patient.PSS_DIAGNOSIS_DATE_ID", "dt_date1", "dt_date3", "0", patient_obj.get_max_age_of_diagnosis());
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
						  where_clause += Make_begin_end_age_query (true, mode, "patient.DATE_OF_BIRTH_ID", "patient.PSS_SYMPTOMS_ONSET_DATE_ID", "dt_date1", "dt_date2", patient_obj.get_min_age_of_sign(), patient_obj.get_max_age_of_sign()); 	
					  }
					  else where_clause += Make_begin_end_age_query (true, mode, "patient.DATE_OF_BIRTH_ID", "patient.PSS_SYMPTOMS_ONSET_DATE_ID", "dt_date1", "dt_date2", patient_obj.get_min_age_of_sign(), "200");
				  }
				  else if(!patient_obj.get_max_age_of_sign().isEmpty()) {
					  if(!tables.contains("dt_date1")) tables += ", dt_date as dt_date1";
					  if(!tables.contains("dt_date2")) tables += ", dt_date as dt_date2";
					  where_clause += Make_begin_end_age_query (true, mode, "patient.DATE_OF_BIRTH_ID", "patient.PSS_SYMPTOMS_ONSET_DATE_ID", "dt_date1", "dt_date2", "0", patient_obj.get_max_age_of_sign());
				  }
				  
				  
				  query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;
				  
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient", "outerr") + temp_where_clause;
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient", "outerr") + temp_where_clause;
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient", "outerr") + temp_where_clause;
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient", "outerr") + temp_where_clause;
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient", "outerr") + temp_where_clause;
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient", "outerr") + temp_where_clause;
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient", "outerr") + temp_where_clause;
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient", "outerr") + temp_where_clause;
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient", "outerr") + temp_where_clause;
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient", "outerr") + temp_where_clause;
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient", "outerr") + temp_where_clause;
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient", "outerr") + temp_where_clause;
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
					  query = "SELECT DISTINCT outerr.UID FROM " + tables.replace("patient", "patient outerr") + " WHERE " + where_clause.replace("patient", "outerr") + temp_where_clause;
				  }
				  
			  } break;
			  
			  default:
				  System.out.println("Undefined criterion-name-"+(i+1)+" in the input JSON file.");
			} 
			
			try { 
				query=query.replace("WHERE  AND", "WHERE");
				query=query.replace("WHERE AND", "WHERE");
				if(!results_of_one_Criterion.trim().equals("")) {
					  String[] previousResults = results_of_one_Criterion.trim().split(" ");
					  String inClause = "(";
					  for (int j=0; j<previousResults.length; j++) {
						  if(j==0) inClause += previousResults[j];
						  else inClause += ","+previousResults[j];
					  }
					  inClause += ")";
					  query=query.replaceFirst("WHERE","WHERE UID IN "+inClause+" AND");
				}
				//System.out.println("The query is: "+query);
				System.out.println("We are ready to execute the query: "+query);
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
			
			/*if(results_of_all_Criterions.equals("")) results_of_all_Criterions = results_of_one_Criterion;
			else results_of_all_Criterions = intersection_of_UIDs(results_of_one_Criterion, results_of_all_Criterions);
			if(results_of_all_Criterions.trim().equals("")) patients_found = false;*/
			if(results_of_one_Criterion.trim().equals("")) patients_found = false;
			System.out.println("patients found: "+patients_found);
			}
			else {
				System.out.println("Criterion " + current_Criterion.getCriterion() + " cannot be used.");
				criterionResponseInfos.put("usage", "notused");
				criterionResponseInfos.put("notes", "Criterion cannot be reached because no patients found");
				inclusion_criteria.add(criterionResponseInfos);
				
				System.out.println(criterionResponseInfos);
				continue;
			}
    	}
    	//return results_of_all_Criterions;
    	return results_of_one_Criterion;
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
        			String codes[] = crit_cond_symptom_obj.getVoc_symptom_sign_CODE().split(",");
  				  	for(int k=0; k<codes.length; k++) {
  				  		String narrowTerms = getTermsWithNarrowMeaning(codes[k].trim());
  				  		String[] allNarrowTerms = narrowTerms.split(",");
  				  		for(int c=1; c<allNarrowTerms.length; c++) {
  				  			query += " OR " + Make_OR_of_CODES("voc_symptom_sign.CODE", allNarrowTerms[c]);
  				  		}
  				  	}
    				query += ") LIMIT 1";
    				assistanceQuery = "SELECT NAME FROM voc_symptom_sign WHERE" + query.split("AND")[1]; 
        		}
        		break;
        		case "condition_diagnosis": {
        			condition_diagnosis crit_cond_diagnosis_obj = (condition_diagnosis)crit;
        			query = "SELECT * FROM cond_diagnosis, voc_medical_condition WHERE cond_diagnosis.CONDITION_ID = voc_medical_condition.ID AND (" + Make_OR_of_CODES("voc_medical_condition.CODE", crit_cond_diagnosis_obj.getCondition());
        			String codes[] = crit_cond_diagnosis_obj.getCondition().split(",");
  				  	for(int k=0; k<codes.length; k++) {
  				  		String narrowTerms = getTermsWithNarrowMeaning(codes[k].trim());
  				  		String[] allNarrowTerms = narrowTerms.split(",");
  				  		for(int c=1; c<allNarrowTerms.length; c++) {
  				  			query += " OR " + Make_OR_of_CODES("voc_medical_condition.CODE", allNarrowTerms[c]);
  				  		}
  				  	}
    				query += ") LIMIT 1";
    				assistanceQuery = "SELECT NAME FROM voc_medical_condition WHERE" + query.split("AND")[1]; 
        		}
        		break;
        		case "intervention_medication": {
        			intervention_medication  crit_interv_medication_obj =  (intervention_medication )crit;
    				query = "SELECT * FROM interv_medication, voc_pharm_drug WHERE interv_medication.MEDICATION_ID = voc_pharm_drug.ID AND (" + Make_OR_of_CODES("voc_pharm_drug.CODE", crit_interv_medication_obj.getVoc_pharm_drug_CODE());
    				String codes[] = crit_interv_medication_obj.getVoc_pharm_drug_CODE().split(",");
  				  	for(int k=0; k<codes.length; k++) {
  				  		String narrowTerms = getTermsWithNarrowMeaning(codes[k].trim());
  				  		String[] allNarrowTerms = narrowTerms.split(",");
  				  		for(int c=1; c<allNarrowTerms.length; c++) {
  				  			query += " OR " + Make_OR_of_CODES("voc_pharm_drug.CODE", allNarrowTerms[c]);
  				  		}
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
    				String codes[] = examination_lab_test_obj.getTest_id().split(",");
  				  	for(int k=0; k<codes.length; k++) {
  				  		String narrowTerms = getTermsWithNarrowMeaning(codes[k].trim());
  				  		String[] allNarrowTerms = narrowTerms.split(",");
  				  		for(int c=1; c<allNarrowTerms.length; c++) {
  				  			query += " OR " + Make_OR_of_CODES("voc_lab_test.CODE", allNarrowTerms[c]);
  				  		}
  				  	}
    				query += ") LIMIT 1";
    				assistanceQuery = "SELECT NAME FROM voc_lab_test WHERE" + query.split("AND")[1];
        		}
        		break;
        		case "examination_biopsy": { //Check if user provided the info of all the fields 
    				//examination_biopsy  examination_biopsy_obj =  (examination_biopsy)crit;
    				query = "SELECT * FROM exam_biopsy LIMIT 1"; /*, voc_biopsy WHERE exam_biopsy.BIOPSY_ID=voc_biopsy.ID AND (voc_biopsy.CODE='"+examination_biopsy_obj.getBiopsy_type()+"'"; // ='SAL-BIO-21' Make_OR_of_CODES("voc_lab_test.CODE", examination_biopsy_obj.getBiopsy_type());				  		 
    				String codes[] = examination_biopsy_obj.getBiopsy_type().split(",");
  				  	for(int k=0; k<codes.length; k++) {
  				  		String narrowTerms = getTermsWithNarrowMeaning(codes[k].trim());
  				  		String[] allNarrowTerms = narrowTerms.split(",");
  				  		for(int c=1; c<allNarrowTerms.length; c++) {
  				  			query += " OR " + Make_OR_of_CODES("voc_biopsy.CODE", allNarrowTerms[c]);
  				  		}
  				  	}
    				query += ") ";
    				assistanceQuery = "SELECT NAME FROM voc_biopsy WHERE" + query.split("AND")[1];*/
        		}
        		break;
        		case "examination_medical_imaging_test": { //Check if user provided the info of all the fields 
    				examination_medical_imaging_test  examination_medical_imaging_test_obj =  (examination_medical_imaging_test)crit;
    				query = "SELECT * FROM exam_medical_imaging_test, voc_medical_imaging_test WHERE exam_medical_imaging_test.TEST_ID=voc_medical_imaging_test.ID AND (" + Make_OR_of_CODES("voc_medical_imaging_test.CODE", examination_medical_imaging_test_obj.getTest_id()) +" ";
    				String codes[] = examination_medical_imaging_test_obj.getTest_id().split(",");
  				  	for(int k=0; k<codes.length; k++) {
  				  		String narrowTerms = getTermsWithNarrowMeaning(codes[k].trim());
  				  		String[] allNarrowTerms = narrowTerms.split(",");
  				  		for(int c=1; c<allNarrowTerms.length; c++) {
  				  			query += " OR " + Make_OR_of_CODES("voc_medical_imaging_test.CODE", allNarrowTerms[c]);
  				  		}
  				  	}
    				query += ") LIMIT 1";
    				assistanceQuery = "SELECT NAME FROM voc_medical_imaging_test WHERE" + query.split("AND")[1];
        		}
        		break;
        		case "examination_questionnaire_score": { //Check if user provided the info of all the fields 
    				examination_questionnaire_score  examination_questionnaire_score_obj =  (examination_questionnaire_score)crit;
    				query = "SELECT * FROM exam_questionnaire_score, voc_questionnaire WHERE exam_questionnaire_score.SCORE_ID=voc_questionnaire.ID AND (" + Make_OR_of_CODES("voc_questionnaire.CODE", examination_questionnaire_score_obj.getScore());
    				String codes[] = examination_questionnaire_score_obj.getScore().split(",");
  				  	for(int k=0; k<codes.length; k++) {
  				  		String narrowTerms = getTermsWithNarrowMeaning(codes[k].trim());
  				  		String[] allNarrowTerms = narrowTerms.split(",");
  				  		for(int c=1; c<allNarrowTerms.length; c++) {
  				  			query += " OR " + Make_OR_of_CODES("voc_questionnaire.CODE", allNarrowTerms[c]);
  				  		}
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
    				String codes[] = other_family_history_obj.getMedical_condition().split(",");
  				  	for(int k=0; k<codes.length; k++) {
  				  		String narrowTerms = getTermsWithNarrowMeaning(codes[k].trim());
  				  		String[] allNarrowTerms = narrowTerms.split(",");
  				  		for(int c=1; c<allNarrowTerms.length; c++) {
  				  			query += " OR " + Make_OR_of_CODES("voc_medical_condition.CODE", allNarrowTerms[c]);
  				  		}
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
        		//LOGGER.log(Level.SEVERE,"Bad type query or arguments: "+query,true);
        		//flush_handler();
        		e.printStackTrace();
        		//return "Bad type query or arguments: "+query;
        	}
        	if (!myresults) return false;
        	else return true;
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
    	
    	private static String getSubKeywords(String keywords, MyOWLClass checked){
    		if(keywords.equals("")) keywords += checked.id;			
    		else keywords += ","+checked.id;
    		for(int i=0; i<checked.subClasses.size(); i++){
    			keywords = getSubKeywords(keywords, checked.subClasses.get(i));
    		}
    		return keywords;
    	}
    
    public void criterionDBmatching(ArrayList<Criterion> list_of_criterions) throws JSONException, JsonParseException, JsonMappingException, IOException{
    	results = new Result_UIDs();
    	Boolean mode = true;
    	long startTime = 0;
    	long endTime = 0;
    	for (int j=0;j<2;j++) {
			
    		if(j==0) mode = false; //LOGGER.log(Level.INFO,"======** UIDs_defined_ALL_elements **======\n",true);}
    		else mode = true; //LOGGER.log(Level.INFO,"======** UIDs_UNdefined_some_elements **======\n",true);}
    		String results_of_all_Criterions="";
    		startTime = System.nanoTime();
    		results_of_all_Criterions = createQuery(list_of_criterions, mode, results_of_all_Criterions);
    		endTime = System.nanoTime();
    		results.Input_UIDs(mode,results_of_all_Criterions);
    		//results.Input_UIDs(true,"");
    	}
    	UIDsDefined.add(results.UIDs_defined_ALL_elements );
    	UIDsUndefined.add(results.UIDs_UNdefined_some_elements);
    	//System.out.println(results.Output_JSON_UIDs());
    	System.out.println(TimeUnit.NANOSECONDS.toMillis(endTime - startTime)+" ms");
    }
    
    private JSONObject getCredentials(int cohortID) {
    	JSONObject credentials = new JSONObject();
    	try {
	        String webPage = "https://private.harmonicss.eu/index.php/apps/coh/api/1.0/cohortid?id="+cohortID;

	        String authString = "test1:1test12!";
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
    
    private void accessCohort(String mycohort, ArrayList<Criterion> list_of_criterions) throws SQLException, JsonParseException, JsonMappingException, JSONException, IOException {
    	JSONObject cohortResponse = new JSONObject();
    	int mycohortid = Integer.valueOf(mycohort);
    	ConfigureFile obj;
		if(mycohortid<10) {
			mycohort = "Harm-DB-0"+mycohortid;
			obj = new ConfigureFile("jdbc:mysql://ponte.grid.ece.ntua.gr:3306/Harm-DB-0"+mycohortid, "emps", "emps");;
		}
		else{
			mycohort = "chdb0"+mycohortid;
			JSONObject credentials = getCredentials(mycohortid);
			obj = new ConfigureFile("jdbc:mysql://"+credentials.getString("dbserver")+":"+credentials.getString("dbport")+"/"+credentials.getString("dbarea")+"?autoReconnect=true&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC",credentials.getString("dbuname"),credentials.getString("dbupass"));
		}
    	
		cohortResponse.put("cohort_name", mycohort);
		//ConfigureFile obj = new ConfigureFile("jdbc:mysql://"+credentials.getString("dbserver")+":"+credentials.getString("dbport")+"/"+credentials.getString("dbarea"),credentials.getString("dbuname"),credentials.getString("dbupass"));
    	//ConfigureFile obj = new ConfigureFile("jdbc:mysql://ponte.grid.ece.ntua.gr:3306/Harm-DB-09", "emps", "emps");
    	if(!DBServiceCRUD.makeJDBCConnection(obj)) {
    		all.put("error_message", "Error while connecting to database. Check database url and credentials and try again.");
			all.put("status", "error");
    		System.out.println("Connection with the Database failed. Check the Credentials and the DB URL.");
    	}
    	else 
    	{System.out.println("everything's gooooooood");
    	
		criterionDBmatching(list_of_criterions);
		
    	cohortResponse.put("patients_IDs_list", results.UIDs_defined_ALL_elements);
    	/*if(results.UIDs_defined_ALL_elements.length==1 && results.UIDs_defined_ALL_elements[0].equals("")) {
    		if(results.UIDs_UNdefined_some_elements.length==1 && results.UIDs_UNdefined_some_elements[0].equals("")) {
    			cohortResponse.put("patients_IDs_list", results.UIDs_defined_ALL_elements);
    		}
    		else cohortResponse.put("patients_IDs_list", results.UIDs_UNdefined_some_elements);
    	}
    	else {
    		if(results.UIDs_UNdefined_some_elements.length==1 && results.UIDs_UNdefined_some_elements[0].equals("")) {
    			cohortResponse.put("patients_IDs_list", results.UIDs_defined_ALL_elements);
    		}
    		else {
    			if(results.UIDs_defined_ALL_elements.length > results.UIDs_UNdefined_some_elements.length) cohortResponse.put("patients_IDs_list", results.UIDs_defined_ALL_elements);
    			else cohortResponse.put("patients_IDs_list", results.UIDs_UNdefined_some_elements);
    		}
    	}*/
    	String result_incl = "";
    	
	  	for(int i=0; i<list_of_criterions.size(); i++){
	  		//System.out.println(inclCriterion.getFormalExpression().get(0).getBooleanExpression().trim());
	  		result_incl+=list_of_criterions.get(i).getCriterion()+": "+inclusion_criteria.get(i).getString("notes");
	  		if(inclusion_criteria.get(i).getString("usage").equals("used")) result_incl += " - USED<br>";
	  		else result_incl += " - NOT USED<br>";
	  	}
	  	cohortResponse.put("inclusion_criteria", "Tested criteria: <br>"+result_incl);
	  	cohortResponseList.add(cohortResponse);
		inclusion_criteria.clear();
		DBServiceCRUD.closeJDBCConnection();
		System.out.println("End");
    	}
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
		System.out.println("Proccess started");
		cohortResponseList.clear();
		patients_found = true;
		all = new JSONObject();
		manager = OWLManager.createOWLOntologyManager();
		InputStream input = new FileInputStream(getServletContext().getRealPath("/WEB-INF/infos.properties"));
    	Properties prop = new Properties();
        // load a properties file
        prop.load(input);
        System.out.println("Properties file read");
    	/*Scanner s1 = new Scanner(new BufferedReader(new FileReader(getServletContext().getRealPath("/WEB-INF/infos.txt"))));
		String[] line = s1.nextLine().split(":");
	    documentIRI = IRI.create("file:///C:/Users/Jason/Desktop/", "HarmonicSS-Reference-Model+Vocabularies-v.0.9.owl");
		documentIRI = IRI.create(getServletContext().getResource("/WEB-INF/"+line[1].trim()));*/
		documentIRI = IRI.create(getServletContext().getResource("/WEB-INF/"+prop.getProperty("owlFile")));
	    try{
	        ontology = manager.loadOntologyFromOntologyDocument(documentIRI);
            findClasses();
            findSubclasses();
            System.out.println("OWL file read");
		}
		catch (OWLOntologyCreationException e) {
			all.put("error_message", "Could not find owl file. Check file path and try again.");
			all.put("status", "error");
	        e.printStackTrace();
			
		}
	    TestInfos req = new Gson().fromJson(request.getReader(), TestInfos.class);
		
		String crit_incl = makeCriterionList(req.jsonCriteria);
		String criteria = Intermediate_Layer.preProcess_JSON(crit_incl);
		ArrayList<Criterion> list_of_criterions=null;
		try {
			list_of_criterions = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
			//findCriterion((Criterion)list_of_inclusive_criterions.get(0));
			accessCohort(req.mycohort, list_of_criterions);
			if(cohortResponseList.size()>0) {
				all.put("cohort_response_list", cohortResponseList);
				all.put("status", "ok");
			}
		} catch (NullPointerException e1) {
			/*LOGGER.log(Level.SEVERE,"JsonParseException Bad JSON format: "+criteria,true);
			flush_handler();*/
			all.put("error_message", "Error while parsing json criteria. Check your json format and all criteria fields and try again.");
			all.put("status", "error");
			e1.printStackTrace();
			//return "JsonParseException Bad JSON format.";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*try {
			accessCohort(req.mycohort, list_of_criterions);
			if(cohortResponseList.size()>0) {
				all.put("cohort_response_list", cohortResponseList);
				all.put("status", "ok");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			all.put("error_message", "Error while connecting to database. Check database url and credentials and try again.");
			all.put("status", "error");
			e.printStackTrace();
		}*/
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = response.getWriter();
		pw.print(all.toString());
		pw.close();
	}

}
