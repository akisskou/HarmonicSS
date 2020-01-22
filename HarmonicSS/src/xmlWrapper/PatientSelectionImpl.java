package xmlWrapper;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;



























import org.json.JSONException;
import org.json.JSONObject;
import org.ntua.criteria.*;

import jsonProcess.*;
import criterionManager.*;
import criterionManager.Criterion;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import static queries.SQL_aux_functions.Make_OR_of_CODES;
import static queries.SQL_aux_functions.Make_begin_end_date_query;
import static queries.SQL_aux_functions.Make_begin_end_period_query;
import static queries.SQL_aux_functions.Make_specific_date_query;

import java.io.File;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Servlet implementation class PatientSelectionImpl
 */
public class PatientSelectionImpl extends HttpServlet implements XMLFileManager, CriterionManager{
	private static final long serialVersionUID = 1L;
	static ArrayList<String> criterion_incl = new ArrayList<String>();
	static ArrayList<String> criterion_excl = new ArrayList<String>();
	static ArrayList<String> crit_names_incl = new ArrayList<String>();
	static ArrayList<String> crit_names_excl = new ArrayList<String>();
	static ArrayList<String> cohort_names = new ArrayList<String>();
	static String requestID = "";
	private static PatientsSelectionRequest patientsSelectionRequest;
	private static File responseXML = new File("Resp01.xml");
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PatientSelectionImpl() {
        super();
        // TODO Auto-generated constructor stub
    }

    @SuppressWarnings("unchecked")
	public String readXMLbyRequestID(String requestID){
    	criterion_incl.clear();
  	  	criterion_excl.clear();
  	  	crit_names_incl.clear();
  	  	crit_names_excl.clear();
  	  	cohort_names.clear();
  	  	String result_incl = "";
  	  	String result_excl = "";
  	  	
  	  	try {
  	  		JAXBContext jaxbContext;
  	  		File fXmlFile = new File(requestID+".xml");
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
    
    public void criterionDBmatching(ArrayList<Criterion> list_of_inclusive_criterions, ArrayList<Criterion> list_of_exclusive_criterions){
    	Result_UIDs results = new Result_UIDs();
    	Boolean mode;
    	for (int j=0;j<2;j++) {
			
		if(j==0) mode = false; //LOGGER.log(Level.INFO,"======** UIDs_defined_ALL_elements **======\n",true);}
		else mode = true; //LOGGER.log(Level.INFO,"======** UIDs_UNdefined_some_elements **======\n",true);}
    	String results_of_one_Criterion="";
    	String results_of_all_Criterions="";
    	for(int i=0; i<list_of_inclusive_criterions.size(); i++) {
    		
			Criterion current_Criterion=list_of_inclusive_criterions.get(i); //current_criterion
			if(!canUseCriterion(current_Criterion)){
				System.out.println("Criterion " + current_Criterion.getCriterion() + " cannot be used.");
				continue;
			}
			else System.out.println("Criterion " + current_Criterion.getCriterion() + " can be used.");

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
					  where_clause += " AND " + Make_specific_date_query(mode, "demo_pregnancy_data.CONCEPTION_DATE_ID","dt_date1",crit_demo_pregnancy_obj.getCONCEPTION_DATE_YEAR(),
							  crit_demo_pregnancy_obj.getCONCEPTION_DATE_MONTH(),crit_demo_pregnancy_obj.getCONCEPTION_DATE_DAY());					  		
				  
				  } else if(!(crit_demo_pregnancy_obj.getCONCEPTION_period_begin_year() + crit_demo_pregnancy_obj.getCONCEPTION_period_begin_month() + crit_demo_pregnancy_obj.getCONCEPTION_period_begin_day()).isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  where_clause += " AND " + Make_begin_end_date_query (mode,"demo_pregnancy_data.CONCEPTION_DATE_ID", "dt_date1",crit_demo_pregnancy_obj.getCONCEPTION_period_begin_year(), crit_demo_pregnancy_obj.getCONCEPTION_period_begin_month(), crit_demo_pregnancy_obj.getCONCEPTION_period_begin_day(), crit_demo_pregnancy_obj.getCONCEPTION_period_end_year(), crit_demo_pregnancy_obj.getCONCEPTION_period_end_month(),
							  crit_demo_pregnancy_obj.getCONCEPTION_period_end_day());			  
				  
				  } else if(!(crit_demo_pregnancy_obj.getCONCEPTION_until_date_year() + crit_demo_pregnancy_obj.getCONCEPTION_until_date_month() + crit_demo_pregnancy_obj.getCONCEPTION_until_date_day()).isEmpty()) {
					  tables += ", dt_date as dt_date1";
					  where_clause += " AND " + Make_begin_end_date_query (mode,"demo_pregnancy_data.CONCEPTION_DATE_ID","dt_date1", "1800", "1", "1", crit_demo_pregnancy_obj.getCONCEPTION_until_date_year(), 
							  crit_demo_pregnancy_obj.getCONCEPTION_until_date_month(), crit_demo_pregnancy_obj.getCONCEPTION_until_date_day()); 
				  }
				  
				  if(!(crit_demo_pregnancy_obj.outcome_exact_year + crit_demo_pregnancy_obj.outcome_exact_month + 
				  	crit_demo_pregnancy_obj.outcome_exact_day).isEmpty()) {
					  tables += ", dt_date as dt_date2";
					  where_clause += " AND " + Make_specific_date_query(mode, "demo_pregnancy_data.OUTCOME_DATE_ID","dt_date2", crit_demo_pregnancy_obj.getOUTCOME_DATE_YEAR(),
							  crit_demo_pregnancy_obj.getOUTCOME_DATE_MONTH(),crit_demo_pregnancy_obj.getOUTCOME_DATE_DAY());	
					 
				  	} else if(!(crit_demo_pregnancy_obj.getOUTCOME_period_begin_year() + crit_demo_pregnancy_obj.getOUTCOME_period_begin_month() + crit_demo_pregnancy_obj.getOUTCOME_period_begin_day()).isEmpty()) {
				  		tables += ", dt_date as dt_date2"; 
				  		where_clause += " AND " + Make_begin_end_date_query (mode,"demo_pregnancy_data.OUTCOME_DATE_ID", "dt_date2", crit_demo_pregnancy_obj.getOUTCOME_period_begin_year(), crit_demo_pregnancy_obj.getOUTCOME_period_begin_month(), crit_demo_pregnancy_obj.getOUTCOME_period_begin_day(), crit_demo_pregnancy_obj.getOUTCOME_period_end_year(), crit_demo_pregnancy_obj.getOUTCOME_period_end_month(),
								  crit_demo_pregnancy_obj.getOUTCOME_period_end_day()); 			  
					
				  	} else if(!(crit_demo_pregnancy_obj.getOUTCOME_until_date_year() + crit_demo_pregnancy_obj.getOUTCOME_until_date_month() + crit_demo_pregnancy_obj.getOUTCOME_until_date_day()).isEmpty()) {
				  		tables += ", dt_date as dt_date2";  
				  		where_clause += " AND " + Make_begin_end_date_query (mode,"demo_pregnancy_data.OUTCOME_DATE_ID", "dt_date2", "1800", "1", "1", crit_demo_pregnancy_obj.getOUTCOME_until_date_year(), 
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
				  	
				  	query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;
				  	
			  } break;
			  
			  case "lifestyle_smoking": { //Check if user provided the info of all the fields 
				  lifestyle_smoking crit_lifestyle_smoking_obj =  (lifestyle_smoking)current_Criterion;
				  
				  String tables = "patient, lifestyle_smoking, voc_smoking_status";
				  String where_clause = "patient.ID = lifestyle_smoking.PATIENT_ID AND lifestyle_smoking.STATUS_ID = voc_smoking_status.ID AND " + Make_OR_of_CODES("voc_smoking_status.CODE", crit_lifestyle_smoking_obj.getVoc_smoking_status_CODE());
				  

				  if(!(crit_lifestyle_smoking_obj.getSmoking_exact_date_year()).isEmpty()) {
					  tables += ", dt_date AS dt_date1, dt_date AS dt_date2";
					  where_clause += " AND " + Make_begin_end_period_query (mode,"lifestyle_smoking.PERIOD_ID", "dt_date1", "dt_date2", crit_lifestyle_smoking_obj.getSmoking_exact_date_year(), 
							  crit_lifestyle_smoking_obj.getSmoking_exact_date_month(), crit_lifestyle_smoking_obj.getSmoking_exact_date_day(),
							  crit_lifestyle_smoking_obj.getSmoking_exact_date_year(), crit_lifestyle_smoking_obj.getSmoking_exact_date_month(),
							  crit_lifestyle_smoking_obj.getSmoking_exact_date_day()); 
					  							  
					} else if(!(crit_lifestyle_smoking_obj.getSmoking_period_end_year()).isEmpty()) {
						tables += ", dt_date AS dt_date1, dt_date AS dt_date2";
						where_clause += " AND " + Make_begin_end_period_query (mode,"lifestyle_smoking.PERIOD_ID", "dt_date1", "dt_date2", crit_lifestyle_smoking_obj.getSmoking_period_begin_year(), 
								  crit_lifestyle_smoking_obj.getSmoking_period_begin_month(), crit_lifestyle_smoking_obj.getSmoking_period_begin_day(),
								  crit_lifestyle_smoking_obj.getSmoking_period_end_year(), crit_lifestyle_smoking_obj.getSmokimg_period_end_month(),
								  crit_lifestyle_smoking_obj.getSmoking_period_end_day());
																		
					} else if(!(crit_lifestyle_smoking_obj.getSmoking_until_date_year()).isEmpty()) {
						tables += ", dt_date AS dt_date1, dt_date AS dt_date2";
						where_clause += " AND " + Make_begin_end_period_query (mode,"lifestyle_smoking.PERIOD_ID", "dt_date1", "dt_date2", "1800", 
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
					  
				  query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;		
				  
				  //TODO 	will voc_unit.CODE take one value or more than one I also think that voc_direction_CODE() will take one value?
				  
				  //if(!crit_lifestyle_smoking_obj.getDt_period_of_time_voc_confirmation_CODE().isEmpty()) query += "AND dt_period_of_time.EXACT_ID = voc_confirmation.ID AND voc_confirmation.CODE='"+crit_lifestyle_smoking_obj.getDt_period_of_time_voc_confirmation_CODE()+"' "; 
				  //if(!crit_lifestyle_smoking_obj.getDt_period_of_time_BEFORE_PERIOD_ID().isEmpty()) query += "AND dt_period_of_time.BEFORE_PERIOD_ID = '"+crit_lifestyle_smoking_obj.getDt_period_of_time_BEFORE_PERIOD_ID()+"' ";
			  System.out.println("The Query is: "+ query);
			  } break;
			  
			  case "condition_symptom": { //Check if user provided the info of all the fields 
				  condition_symptom crit_cond_symptom_obj =  (condition_symptom)current_Criterion;
				  
				  String tables = "patient, cond_symptom, voc_symptom_sign";
				  String where_clause = "patient.ID = cond_symptom.PATIENT_ID AND cond_symptom.CONDITION_ID = voc_symptom_sign.ID AND " + Make_OR_of_CODES("voc_symptom_sign.CODE", crit_cond_symptom_obj.getVoc_symptom_sign_CODE());
				  
				  
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
				  query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;
			  } break;
			  
			  case "condition_diagnosis": { //Check if user provided the info of all the fields 
				  condition_diagnosis condition_diagnosis_obj =  (condition_diagnosis)current_Criterion;
				  
				  String tables = "patient, cond_diagnosis, voc_medical_condition";
				  String where_clause = "patient.ID = cond_diagnosis.PATIENT_ID AND cond_diagnosis.CONDITION_ID = voc_medical_condition.ID AND " + Make_OR_of_CODES("voc_medical_condition.CODE", condition_diagnosis_obj.getCondition());
				 
				  
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
					  where_clause += " AND " + Make_specific_date_query(mode, "cond_diagnosis.DIAGNOSIS_DATE_ID","dt_date",condition_diagnosis_obj.getDate_exact_year(),
							  condition_diagnosis_obj.getDate_exact_month(),condition_diagnosis_obj.getDate_exact_day());
					 			  		
				  } else if(!(condition_diagnosis_obj.getDate_interval_start_year()).isEmpty()) {
					 tables += ", dt_date";
					  where_clause += " AND " + Make_begin_end_date_query (mode,"cond_diagnosis.DIAGNOSIS_DATE_ID", "dt_date",condition_diagnosis_obj.getDate_interval_start_year(), condition_diagnosis_obj.getDate_interval_start_month(), condition_diagnosis_obj.getDate_interval_start_day(), condition_diagnosis_obj.getDate_interval_end_year(), condition_diagnosis_obj.getDate_interval_end_month(),
							  condition_diagnosis_obj.getDate_interval_end_day());  
				  } else if(!(condition_diagnosis_obj.getDate_until_year() ).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += " AND " + Make_begin_end_date_query (mode,"cond_diagnosis.DIAGNOSIS_DATE_ID","dt_date", "1800", "1", "1", condition_diagnosis_obj.getDate_until_year(), 
							  condition_diagnosis_obj.getDate_until_month(), condition_diagnosis_obj.getDate_until_day()); 
				  }
				  
				  if(!condition_diagnosis_obj.getStatement().isEmpty()){ 
					  tables += ", voc_confirmation";
					  where_clause += " AND cond_diagnosis.STMT_ID=voc_confirmation.ID AND voc_confirmation.CODE='"+condition_diagnosis_obj.getStatement() + "'";
				  		query += " AND cond_diagnosis.STMT_ID=voc_confirmation.ID " +
				  				 "AND voc_confirmation.CODE='"+condition_diagnosis_obj.getStatement() + "'";
				  }
				  query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;
			  } break;
			  
			  case "intervention_medication": { //Check if user provided the info of all the fields 
				  intervention_medication  crit_interv_medication_obj =  (intervention_medication )current_Criterion;
				  
				  String tables = "patient, interv_medication, voc_pharm_drug";
				  String where_clause = "patient.ID = interv_medication.PATIENT_ID AND interv_medication.MEDICATION_ID = voc_pharm_drug.ID AND " + Make_OR_of_CODES("voc_pharm_drug.CODE", crit_interv_medication_obj.getVoc_pharm_drug_CODE());
				  
				  
				  //if(!crit_interv_medication_obj.getVoc_pharm_drug_BROADER_TERM_ID().isEmpty()) query += "AND voc_pharm_drug.BROADER_TERM_ID = '"+crit_interv_medication_obj.getVoc_pharm_drug_BROADER_TERM_ID()+"' "; //Do we need the Broader_Term_ID? (`BROADER_TERM_ID`) REFERENCES `voc_pharm_drug` (`ID`)
					  
				  if(!crit_interv_medication_obj.getDosage_amount_exact_value().isEmpty()) {
					  tables += ", dt_amount";
					  where_clause += " AND interv_medication.DOSSAGE_ID = dt_amount.ID AND dt_amount.VALUE ='" +crit_interv_medication_obj.getDosage_amount_exact_value()+"'";
				  }
				  
				  if(!(crit_interv_medication_obj.getMedication_exact_date_year()).isEmpty()) {	
					  tables += ", dt_date AS dt_date1, dt_date AS dt_date2";
					  where_clause += " AND " + Make_begin_end_period_query (mode,"interv_medication.PERIOD_ID", "dt_date1", "dt_date2", crit_interv_medication_obj.getMedication_exact_date_year(), 
					  			crit_interv_medication_obj.getMedication_exact_date_month(), crit_interv_medication_obj.getMedication_exact_date_day(),
					  			crit_interv_medication_obj.getMedication_exact_date_year(), crit_interv_medication_obj.getMedication_exact_date_month(),
					  			crit_interv_medication_obj.getMedication_exact_date_day());
							  
				  } else if(!(crit_interv_medication_obj.getMedication_period_end_year()).isEmpty()) {
					  tables += ", dt_date AS dt_date1, dt_date AS dt_date2";
					  where_clause += " AND " + Make_begin_end_period_query (mode,"interv_medication.PERIOD_ID", "dt_date1", "dt_date2", crit_interv_medication_obj.getMedication_period_begin_year(), 
								crit_interv_medication_obj.getMedication_period_begin_month(), crit_interv_medication_obj.getMedication_period_begin_day(),
								crit_interv_medication_obj.getMedication_period_end_year(), crit_interv_medication_obj.getMedication_period_end_month(),
								crit_interv_medication_obj.getMedication_period_end_day()); 												
				  
				  } else if(!(crit_interv_medication_obj.getMedication_until_date_year()).isEmpty()) {
					  tables += ", dt_date AS dt_date1, dt_date AS dt_date2";
					  where_clause += " AND " + Make_begin_end_period_query (mode,"interv_medication.PERIOD_ID", "dt_date1", "dt_date2", "1800", 
							  "1", "1",crit_interv_medication_obj.getMedication_until_date_year(), crit_interv_medication_obj.getMedication_until_date_month(),
							  crit_interv_medication_obj.getMedication_until_date_day()); 							
				  } 
				  
				  if(!crit_interv_medication_obj.getStatement().isEmpty()) {
					  tables += ", voc_confirmation";
					  where_clause += "AND interv_medication.STMT_ID=voc_confirmation.ID AND voc_confirmation.CODE='"+crit_interv_medication_obj.getStatement() + "'";
				  }
				  query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;
					
			  } break;
			  
			  case "intervention_chemotherapy": { //Check if user provided the info of all the fields 
				  intervention_chemotherapy  crit_interv_chemotherapy_obj =  (intervention_chemotherapy)current_Criterion;
				  
				  String tables = "patient, interv_chemotherapy, voc_confirmation AS conf_1";
				  String where_clause = "patient.ID = interv_chemotherapy.PATIENT_ID AND interv_chemotherapy.DUE_TO_PSS_ID = conf_1.ID AND " + Make_OR_of_CODES("conf_1.CODE", crit_interv_chemotherapy_obj.getReason());
				  query = "SELECT DISTINCT patient.UID " +
						  "FROM patient, interv_chemotherapy, voc_confirmation AS conf_1, voc_confirmation AS conf_2, dt_period_of_time, dt_date AS dt_date1, dt_date AS dt_date2, voc_direction " +
						  "WHERE patient.ID = interv_chemotherapy.PATIENT_ID " +
						  "AND interv_chemotherapy.DUE_TO_PSS_ID = conf_1.ID " +
						  //"AND voc_confirmation.CODE = '"+crit_interv_chemotherapy_obj.getVoc_confirmation_CODE() +"' ";
						  "AND " + Make_OR_of_CODES("conf_1.CODE", crit_interv_chemotherapy_obj.getReason());
				  		  //TODO the voc_confirmation do not need multiple CODES, do they?
				  
				  
				  if(!(crit_interv_chemotherapy_obj.getChem_exact_date_year()).isEmpty()) {	
					  tables += ", dt_date AS dt_date1, dt_date AS dt_date2";
					  where_clause += " AND " + Make_begin_end_period_query (mode,"interv_chemotherapy.PERIOD_ID", "dt_date1", "dt_date2", crit_interv_chemotherapy_obj.getChem_exact_date_year(), 
					  			crit_interv_chemotherapy_obj.getChem_exact_date_month(), crit_interv_chemotherapy_obj.getChem_exact_date_day(),
					  			crit_interv_chemotherapy_obj.getChem_exact_date_year(), crit_interv_chemotherapy_obj.getChem_exact_date_month(),
					  			crit_interv_chemotherapy_obj.getChem_exact_date_day()); 						  
					} else if(!(crit_interv_chemotherapy_obj.getChem_period_end_year()).isEmpty()) {
						tables += ", dt_date AS dt_date1, dt_date AS dt_date2";
						where_clause += " AND " + Make_begin_end_period_query (mode,"interv_chemotherapy.PERIOD_ID", "dt_date1", "dt_date2", crit_interv_chemotherapy_obj.getChem_period_begin_year(), 
								crit_interv_chemotherapy_obj.getChem_period_begin_month(), crit_interv_chemotherapy_obj.getChem_period_begin_day(),
								crit_interv_chemotherapy_obj.getChem_period_end_year(), crit_interv_chemotherapy_obj.getChem_period_end_month(),
								crit_interv_chemotherapy_obj.getChem_period_end_day()); 												
					} else if(!(crit_interv_chemotherapy_obj.getChem_until_date_year()).isEmpty()) {
						tables += ", dt_date AS dt_date1, dt_date AS dt_date2";
						where_clause += " AND " + Make_begin_end_period_query (mode,"interv_chemotherapy.PERIOD_ID", "dt_date1", "dt_date2", "1800", 
								  "1", "1",crit_interv_chemotherapy_obj.getChem_until_date_year(), crit_interv_chemotherapy_obj.getChem_until_date_month(),
								  crit_interv_chemotherapy_obj.getChem_until_date_day()); 								
					}
				  
				  if(!crit_interv_chemotherapy_obj.getStatement().isEmpty()) {
					  tables += ", voc_confirmation AS conf_2";
					  where_clause += " AND interv_chemotherapy.STMT_ID=conf_2.ID AND conf_2.CODE='"+crit_interv_chemotherapy_obj.getStatement() + "'";
				  }
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
					  where_clause += " AND " + Make_specific_date_query(mode, "interv_surgery.SURGERY_DATE_ID","dt_date",crit_interv_surgery_obj.getSurgery_exact_date_year(), //check cond_symptom.OBSERVE_DATE_ID
							  crit_interv_surgery_obj.getSurgery_exact_date_month(), crit_interv_surgery_obj.getSurgery_exact_date_day());
					  
				  } else if(!(crit_interv_surgery_obj.getSurgery_period_end_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += " AND " + Make_begin_end_date_query (mode,"interv_surgery.SURGERY_DATE_ID", "dt_date",crit_interv_surgery_obj.getSurgery_period_begin_year(),
							  crit_interv_surgery_obj.getSurgery_period_begin_month(), crit_interv_surgery_obj.getSurgery_period_begin_day(), crit_interv_surgery_obj.getSurgery_period_end_year() , 
							  crit_interv_surgery_obj.getSurgery_period_end_month(), crit_interv_surgery_obj.getSurgery_period_end_day()); 
					  
				  } else if(!(crit_interv_surgery_obj.getSurgery_until_date_year()).isEmpty()) {
					  tables += ", dt_date";
					  where_clause += " AND " + Make_begin_end_date_query (mode,"interv_surgery.SURGERY_DATE_ID","dt_date", "1800", "1", "1", crit_interv_surgery_obj.getSurgery_until_date_year(), 
							  crit_interv_surgery_obj.getSurgery_until_date_month(), crit_interv_surgery_obj.getSurgery_until_date_day()); 
				  }
				  
				  if(!crit_interv_surgery_obj.getStatement().isEmpty()) {
					  tables += ", voc_confirmation AS conf_2";
					  where_clause += " AND interv_surgery.STMT_ID=conf_2.ID AND conf_2.CODE='" + crit_interv_surgery_obj.getStatement() + "'";
				  }
				  query = "SELECT DISTINCT patient.UID FROM " + tables + " WHERE " + where_clause;
				  
			  } break;
			  
			  case "examination_lab_test": { //Check if user provided the info of all the fields 
				  System.out.println("We begin the construction of the query.");
				  examination_lab_test  examination_lab_test_obj =  (examination_lab_test)current_Criterion;
				  query = "SELECT DISTINCT patient.UID " +
						  "FROM patient, exam_lab_test, voc_lab_test " +//, exam_lab_test, voc_lab_test, dt_amount, voc_unit, voc_assessment, dt_amount_range, dt_date " + //, voc_direction interv_Surgery, dt_date, voc_direction, voc_confirmation
						  "WHERE patient.ID = exam_lab_test.PATIENT_ID AND " + 
						  "exam_lab_test.TEST_ID=voc_lab_test.ID " +
						  //"voc_lab_test.CODE='"+crit_exam_lab_test_obj.getTEST_ID_voc_lab_test_CODE()+"' ";
				  		  "AND " + Make_OR_of_CODES("voc_lab_test.CODE", examination_lab_test_obj.getTest_id());  // [TEST_ID]
				  System.out.println("We received: "+examination_lab_test_obj.getNormal_range_value());
				  
				  if(!examination_lab_test_obj.getOutcome_amount_exact_value().isEmpty()) { // [OUTCOME_AMOUNT]
					  query=query.replace("FROM patient","FROM patient, dt_amount, voc_unit");		
					  query += "AND exam_lab_test.OUTCOME_AMOUNT_ID = dt_amount.ID AND dt_amount.VALUE = '"+examination_lab_test_obj.getOutcome_amount_exact_value()+"' " +
					  	"AND dt_amount.UNIT_ID=voc_unit.ID " +
					  	"AND voc_unit.CODE ='"+examination_lab_test_obj.getOutcome_amount_unit()+"' ";
				  } else
				  
				  if(!examination_lab_test_obj.getOutcome_amount_range_min_value().isEmpty()&&!examination_lab_test_obj.getOutcome_amount_range_max_value().isEmpty()) { // [OUTCOME_AMOUNT]
					  query=query.replace("FROM patient","FROM patient, dt_amount, voc_unit");	
					  query += "AND exam_lab_test.OUTCOME_AMOUNT_ID = dt_amount.ID AND dt_amount.VALUE < '"+examination_lab_test_obj.getOutcome_amount_range_max_value()+"' " +
					  	"AND dt_amount.VALUE > '"+examination_lab_test_obj.getOutcome_amount_range_min_value()+"' " +
					  	"AND dt_amount.UNIT_ID=voc_unit.ID " +
					  	"AND voc_unit.CODE ='"+examination_lab_test_obj.getOutcome_amount_unit()+"' ";
				  } else
					  
				  if(examination_lab_test_obj.getOutcome_amount_range_min_value().isEmpty()&&!examination_lab_test_obj.getOutcome_amount_range_max_value().isEmpty()) { // [OUTCOME_AMOUNT]
					  query=query.replace("FROM patient","FROM patient, dt_amount, voc_unit");	
					  query += "AND exam_lab_test.OUTCOME_AMOUNT_ID = dt_amount.ID AND dt_amount.VALUE < '"+examination_lab_test_obj.getOutcome_amount_range_max_value()+"' " +
					  	//"AND dt_amount.VALUE > '"+examination_lab_test_obj.getOutcome_amount_range_min_value()+"' " +
					  	"AND dt_amount.UNIT_ID=voc_unit.ID " +
					  	"AND voc_unit.CODE ='"+examination_lab_test_obj.getOutcome_amount_unit()+"' ";
				  } else
					  
				  if(!examination_lab_test_obj.getOutcome_amount_range_min_value().isEmpty()&&examination_lab_test_obj.getOutcome_amount_range_max_value().isEmpty()) { // [OUTCOME_AMOUNT]
					  query=query.replace("FROM patient","FROM patient, dt_amount, voc_unit");	
					  query += "AND exam_lab_test.OUTCOME_AMOUNT_ID = dt_amount.ID " + //AND dt_amount.VALUE < '"+examination_lab_test_obj.getOutcome_amount_range_max_value()+"' 
					  	"AND dt_amount.VALUE > '"+examination_lab_test_obj.getOutcome_amount_range_min_value()+"' " +
					  	"AND dt_amount.UNIT_ID=voc_unit.ID " +
					  	"AND voc_unit.CODE ='"+examination_lab_test_obj.getOutcome_amount_unit()+"' ";
				  }
					  
				  
				  if(!examination_lab_test_obj.getOutcome_assessment().isEmpty()) {  // [OUTCOME_ASSESSMENT]
					  query=query.replace("FROM patient","FROM patient, voc_assessment");	
					  query += "AND exam_lab_test.OUTCOME_ASSESSMENT_ID = voc_assessment.ID " + 
					  	//"AND voc_assessment.CODE ='"+crit_exam_lab_test_obj.OUTCOME_ASSESSMENT_ID_voc_assessment_CODE+"' ";
					  	"AND " + Make_OR_of_CODES("voc_assessment.CODE", examination_lab_test_obj.getOutcome_assessment());
				  }
				  
				  //TODO NORMAL_RANGE  does it check if the value belongs in these two limits.		  
				  if(!(examination_lab_test_obj.getNormal_range_value()).isEmpty()) {
					  query=query.replace("FROM patient","FROM patient, dt_amount_range, voc_unit");	
					  query += "AND exam_lab_test.NORMAL_RANGE_ID = dt_amount_range.ID " +
					  	"AND dt_amount_range.VALUE1 <= '"+examination_lab_test_obj.getNormal_range_value()+"' " +
					  	"AND dt_amount_range.VALUE2 >= '"+examination_lab_test_obj.getNormal_range_value()+"' " +
					    "AND dt_amount_range.UNIT_ID = voc_unit.ID AND voc_unit.CODE='"+examination_lab_test_obj.getOutcome_amount_unit()+"' ";
				  };
				  
				  
				  if(!(examination_lab_test_obj.getOutcome_term()).isEmpty()) {
					  String outcome_term= examination_lab_test_obj.getOutcome_term();
					  if (outcome_term=="CONFIRM-01"||outcome_term=="CONFIRM-02") {
						  query += "AND exam_lab_test.OUTCOME_TERM_ID = voc_confirmation.ID " +
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
							query=query.replace("FROM patient","FROM patient, voc_cryo_type");
							System.out.println("BLOOD-310"); //voc_ana_pattern
							query += "AND exam_lab_test.OUTCOME_TERM_ID = voc_cryo_type.ID " +
							" AND voc_cryo_type.CODE='"+examination_lab_test_obj.getOutcome_term() + "'";
						    break;
						  /*case "BLOOD-521":
						    System.out.println("BLOOD-521");
						    query += "AND exam_lab_test.OUTCOME_TERM_ID = voc_ana_pattern.ID " +
							"AND voc_ana_pattern.CODE='"+examination_lab_test_obj.getOutcome_term() + "'";
						    break;
						  case "BLOOD-522":
						    System.out.println("BLOOD-522");
						    query += "AND exam_lab_test.OUTCOME_TERM_ID = voc_ana_pattern.ID " +
							"AND voc_ana_pattern.CODE='"+examination_lab_test_obj.getOutcome_term() + "'";
						    break;*/
						  case "BLOOD-523":
							  query=query.replace("FROM patient","FROM patient, voc_ana_pattern");
						    System.out.println("BLOOD-523"); //voc_ana_pattern
						    query += "AND exam_lab_test.OUTCOME_TERM_ID = voc_ana_pattern.ID " +
							" AND voc_ana_pattern.CODE='"+examination_lab_test_obj.getOutcome_term() + "'";
						    break;
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
					  query=query.replace("FROM patient","FROM patient, dt_date");
							  query += Make_specific_date_query(mode, "exam_lab_test.SAMPLE_DATE_ID","dt_date",examination_lab_test_obj.getSample_period_of_time_exact_year(), 
							  			examination_lab_test_obj.getSample_period_of_time_exact_month(), examination_lab_test_obj.getSample_period_of_time_exact_day());					  		
						  } 
				  
				  else if(!(examination_lab_test_obj.getSample_period_of_time_interval_end_year()).isEmpty()) {
							  query=query.replace("FROM patient","FROM patient, dt_date");
							  query += Make_begin_end_date_query (mode,"exam_lab_test.SAMPLE_DATE_ID", "dt_date",examination_lab_test_obj.getSample_period_of_time_interval_start_year(), 
										examination_lab_test_obj.getSample_period_of_time_interval_start_month(), examination_lab_test_obj.getSample_period_of_time_interval_start_day(),
										examination_lab_test_obj.getSample_period_of_time_interval_end_year(), examination_lab_test_obj.getSample_period_of_time_interval_end_month(),
										examination_lab_test_obj.getSample_period_of_time_interval_end_day()); 			  
						  } 
				  
				  else if(!(examination_lab_test_obj.getSample_period_of_time_until_year()).isEmpty()) {
							  query=query.replace("FROM patient","FROM patient, dt_date");
							  query += Make_begin_end_date_query (mode,"exam_lab_test.SAMPLE_DATE_ID","dt_date", "1800", "1", "1", examination_lab_test_obj.getSample_period_of_time_until_year(), examination_lab_test_obj.getSample_period_of_time_until_month(),
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
				  System.out.println("The query has been constructed. It is: "+query);
			  } break; 
			  
			  case "examination_biopsy": { //Check if user provided the info of all the fields 
				  examination_biopsy  examination_biopsy_obj =  (examination_biopsy)current_Criterion;
				  query = "SELECT DISTINCT patient.UID " +
						  "FROM patient, exam_biopsy, dt_amount, dt_amount_range, voc_confirmation, voc_biopsy, dt_date, voc_assessment, voc_lab_test, voc_unit " + //interv_Surgery, dt_date, voc_direction, voc_confirmation
						  "WHERE patient.ID = exam_biopsy.PATIENT_ID AND " + 
						  "exam_biopsy.BIOPSY_ID=voc_biopsy.ID AND " +
						  "voc_biopsy.CODE='"+examination_biopsy_obj.getBiopsy_type()+"' "; // ='SAL-BIO-21' Make_OR_of_CODES("voc_lab_test.CODE", examination_biopsy_obj.getBiopsy_type());				  		 

				  
				  
				  if(!(examination_biopsy_obj.getTest_id()).isEmpty()) {
					  	query += "AND exam_biopsy.TEST_ID = voc_lab_test.ID " +
					  	"AND voc_lab_test.CODE = '"+examination_biopsy_obj.getTest_id() +"' "; //'BLOOD-100'
				  };  
				  
			  
				  if(!examination_biopsy_obj.getOutcome_amount_exact_value().isEmpty()) { // [OUTCOME_AMOUNT]
					  	query += "AND exam_biopsy.OUTCOME_AMOUNT_ID = dt_amount.ID AND dt_amount.VALUE = '"+examination_biopsy_obj.getOutcome_amount_exact_value()+"' " +
					  	"AND dt_amount.UNIT_ID=voc_unit.ID " +
					  	"AND voc_unit.CODE ='"+examination_biopsy_obj.getOutcome_amount_unit()+"' ";
				  } else
				  
				  if(!examination_biopsy_obj.getOutcome_amount_range_min_value().isEmpty()&&!examination_biopsy_obj.getOutcome_amount_range_max_value().isEmpty()) { // [OUTCOME_AMOUNT]
					  	query += "AND exam_biopsy.OUTCOME_AMOUNT_ID = dt_amount.ID AND dt_amount.VALUE < '"+examination_biopsy_obj.getOutcome_amount_range_max_value()+"' " +
					  	"AND dt_amount.VALUE > '"+examination_biopsy_obj.getOutcome_amount_range_min_value()+"' " +
					  	"AND dt_amount.UNIT_ID=voc_unit.ID " +
					  	"AND voc_unit.CODE ='"+examination_biopsy_obj.getOutcome_amount_unit()+"' ";
				  } else
					  
				  if(examination_biopsy_obj.getOutcome_amount_range_min_value().isEmpty()&&!examination_biopsy_obj.getOutcome_amount_range_max_value().isEmpty()) { // [OUTCOME_AMOUNT]
					  	query += "AND exam_biopsy.OUTCOME_AMOUNT_ID = dt_amount.ID AND dt_amount.VALUE < '"+examination_biopsy_obj.getOutcome_amount_range_max_value()+"' " +
					  	//"AND dt_amount.VALUE > '"+examination_lab_test_obj.getOutcome_amount_range_min_value()+"' " +
					  	"AND dt_amount.UNIT_ID=voc_unit.ID " +
					  	"AND voc_unit.CODE ='"+examination_biopsy_obj.getOutcome_amount_unit()+"' ";
					  	System.out.println("Mpikame max value amount.");
				  } else
					  
				  if(!examination_biopsy_obj.getOutcome_amount_range_min_value().isEmpty()&&examination_biopsy_obj.getOutcome_amount_range_max_value().isEmpty()) { // [OUTCOME_AMOUNT]
					  	query += "AND exam_biopsy.OUTCOME_AMOUNT_ID = dt_amount.ID " + //AND dt_amount.VALUE < '"+examination_lab_test_obj.getOutcome_amount_range_max_value()+"' 
					  	"AND dt_amount.VALUE > '"+examination_biopsy_obj.getOutcome_amount_range_min_value()+"' " +
					  	"AND dt_amount.UNIT_ID=voc_unit.ID " +
					  	"AND voc_unit.CODE ='"+examination_biopsy_obj.getOutcome_amount_unit()+"' ";
				  }
					  					  
				  //TODO NORMAL_RANGE  does it check if the value belongs in these two limits.		  
				  if(!(examination_biopsy_obj.getNormal_range_value()).isEmpty()) {
					  	query += "AND exam_biopsy.NORMAL_RANGE_ID = dt_amount_range.ID " +
					  	"AND dt_amount_range.VALUE1 <= '"+examination_biopsy_obj.getNormal_range_value()+"' " +
					  	"AND dt_amount_range.VALUE2 >= '"+examination_biopsy_obj.getNormal_range_value()+"' " +
					    "AND dt_amount_range.UNIT_ID = voc_unit.ID AND voc_unit.CODE='"+examination_biopsy_obj.getOutcome_amount_unit()+"' ";
				  };
				  
				  if(!examination_biopsy_obj.getAssessment().isEmpty()) {  // [OUTCOME_ASSESSMENT]
					  	query += "AND exam_biopsy.ASSESSMENT_ID = voc_assessment.ID " + 
					  	//"AND voc_assessment.CODE ='"+crit_exam_lab_test_obj.OUTCOME_ASSESSMENT_ID_voc_assessment_CODE+"' ";
					  	"AND " + Make_OR_of_CODES("voc_assessment.CODE", examination_biopsy_obj.getAssessment());
				  }

				  if(!examination_biopsy_obj.getOutcome_check().isEmpty()) {  // [OUTCOME_ASSESSMENT]
					  	query += "AND exam_biopsy.OUTCOME_CHECK_ID = voc_confirmation.ID " + 
					  	//"AND voc_assessment.CODE ='"+crit_exam_lab_test_obj.OUTCOME_ASSESSMENT_ID_voc_assessment_CODE+"' ";
					  	"AND " + Make_OR_of_CODES("voc_confirmation.CODE", examination_biopsy_obj.getOutcome_check());
				  }					  

				  if(!(examination_biopsy_obj.getBiopsy_period_of_time_exact_year()).isEmpty()) {
							  query += Make_specific_date_query(mode, "exam_biopsy.BIOPSY_DATE_ID","dt_date",examination_biopsy_obj.getBiopsy_period_of_time_exact_year(), 
									  examination_biopsy_obj.getBiopsy_period_of_time_exact_month(), examination_biopsy_obj.getBiopsy_period_of_time_exact_day());					  		
						  } else if(!(examination_biopsy_obj.getBiopsy_period_of_time_interval_end_year()).isEmpty()) {
							  query += Make_begin_end_date_query (mode,"exam_biopsy.BIOPSY_DATE_ID", "dt_date",examination_biopsy_obj.getBiopsy_period_of_time_interval_start_year(), 
									  examination_biopsy_obj.getBiopsy_period_of_time_interval_start_month(), examination_biopsy_obj.getBiopsy_period_of_time_interval_start_day(),
									  examination_biopsy_obj.getBiopsy_period_of_time_interval_end_year(), examination_biopsy_obj.getBiopsy_period_of_time_interval_end_month(),
									  examination_biopsy_obj.getBiopsy_period_of_time_interval_end_day()); 			  
						  } else if(!(examination_biopsy_obj.getBiopsy_period_of_time_until_year()).isEmpty()) {
							  query += Make_begin_end_date_query (mode,"exam_biopsy.BIOPSY_DATE_ID","dt_date", "1800", "1", "1", examination_biopsy_obj.getBiopsy_period_of_time_until_year(), examination_biopsy_obj.getBiopsy_period_of_time_until_month(),
									  examination_biopsy_obj.getBiopsy_period_of_time_until_day()); 
						  }
				  
				  

					//results_of_one_Criterion=DBServiceCRUD.getDataFromDB(query); 
					//System.out.println("We executed: "+crit_exam_lab_test_obj.criterion_name+"\nThe Query is: "+query); 
			  } break; //examination_medical_imaging_test
			  
			  case "examination_medical_imaging_test": { //Check if user provided the info of all the fields 
				  examination_medical_imaging_test  examination_medical_imaging_test_obj =  (examination_medical_imaging_test)current_Criterion;
				  query = "SELECT DISTINCT patient.UID " +
						  "FROM exam_medical_imaging_test, patient, voc_medical_imaging_test, dt_date, voc_assessment " + //interv_Surgery, dt_date, voc_direction, voc_confirmation
						  "WHERE patient.ID = exam_medical_imaging_test.PATIENT_ID AND " + 
						  "exam_medical_imaging_test.TEST_ID=voc_medical_imaging_test.ID " +
						  //"voc_lab_test.CODE='"+crit_exam_lab_test_obj.getTEST_ID_voc_lab_test_CODE()+"' ";
				  		  "AND " + Make_OR_of_CODES("voc_medical_imaging_test.CODE", examination_medical_imaging_test_obj.getTest_id()) +" ";
				  System.out.println("Test id: "+examination_medical_imaging_test_obj.getTest_id());
				  
				  if(!examination_medical_imaging_test_obj.getAssessment().isEmpty()) {  // [OUTCOME_ASSESSMENT]
					  	query += "AND exam_medical_imaging_test.ASSESSMENT_ID = voc_assessment.ID " + 
					  	//"AND voc_assessment.CODE ='"+crit_exam_lab_test_obj.OUTCOME_ASSESSMENT_ID_voc_assessment_CODE+"' ";
					  	"AND " + Make_OR_of_CODES("voc_assessment.CODE", examination_medical_imaging_test_obj.getAssessment());
				  }
				  
				  if(!(examination_medical_imaging_test_obj.getTest_period_of_time_exact_year()).isEmpty()) {
					  query += Make_specific_date_query(mode, "exam_lab_test.BIOPSY_DATE_ID","dt_date",examination_medical_imaging_test_obj.getTest_period_of_time_exact_year(), 
							  examination_medical_imaging_test_obj.getTest_period_of_time_exact_month(), examination_medical_imaging_test_obj.getTest_period_of_time_exact_day());					  		
				  } else if(!(examination_medical_imaging_test_obj.getTest_period_of_time_interval_end_year()).isEmpty()) {
					  query += Make_begin_end_date_query (mode,"exam_lab_test.BIOPSY_DATE_ID", "dt_date",examination_medical_imaging_test_obj.getTest_period_of_time_interval_start_year(), 
							  examination_medical_imaging_test_obj.getTest_period_of_time_interval_start_month(), examination_medical_imaging_test_obj.getTest_period_of_time_interval_start_day(),
							  examination_medical_imaging_test_obj.getTest_period_of_time_interval_end_year(), examination_medical_imaging_test_obj.getTest_period_of_time_interval_end_month(),
							  examination_medical_imaging_test_obj.getTest_period_of_time_interval_end_day()); 			  
				  } else if(!(examination_medical_imaging_test_obj.getTest_period_of_time_until_year()).isEmpty()) {
					  query += Make_begin_end_date_query (mode,"exam_lab_test.BIOPSY_DATE_ID","dt_date", "1800", "1", "1", examination_medical_imaging_test_obj.getTest_period_of_time_until_year(), examination_medical_imaging_test_obj.getTest_period_of_time_until_month(),
							  examination_medical_imaging_test_obj.getTest_period_of_time_until_day()); 
				  }

					//results_of_one_Criterion=DBServiceCRUD.getDataFromDB(query); 
					//System.out.println("We executed: "+crit_exam_lab_test_obj.criterion_name+"\nThe Query is: "+query); 
			  } break;
			  case "examination_questionnaire_score": { //Check if user provided the info of all the fields 
				  examination_questionnaire_score  examination_questionnaire_score_obj =  (examination_questionnaire_score)current_Criterion;
				  query = "SELECT DISTINCT patient.UID " +
						  "FROM patient, exam_questionnaire_score, voc_questionnaire, dt_date, voc_assessment, dt_int_range " + 
						  "WHERE patient.ID = exam_questionnaire_score.PATIENT_ID AND " + 
						  "exam_questionnaire_score.SCORE_ID=voc_questionnaire.ID " +
						  //"voc_lab_test.CODE='"+crit_exam_lab_test_obj.getTEST_ID_voc_lab_test_CODE()+"' ";
				  		  "AND " + Make_OR_of_CODES("voc_questionnaire.CODE", examination_questionnaire_score_obj.getScore());
				  
				  if(!examination_questionnaire_score_obj.getValue().isEmpty()) {  //TODO check value
					  	query += "AND " + Make_OR_of_CODES("exam_questionnaire_score.VALUE", examination_questionnaire_score_obj.getValue());
				  }
				  
				  if(!examination_questionnaire_score_obj.getAssessment().isEmpty()) {  // [OUTCOME_ASSESSMENT]
					  	query += "AND exam_questionnaire_score.ASSESSMENT_ID = voc_assessment.ID " + 
					  	//"AND voc_assessment.CODE ='"+crit_exam_lab_test_obj.OUTCOME_ASSESSMENT_ID_voc_assessment_CODE+"' ";
					  	"AND " + Make_OR_of_CODES("voc_assessment.CODE", examination_questionnaire_score_obj.getAssessment());
				  }
				  
				  //TODO NORMAL_RANGE  does it check if the value belongs in these two limits.		  
				  if(!(examination_questionnaire_score_obj.getNormal_range_value()).isEmpty()) {
					  	query += "AND exam_lab_test.NORMAL_RANGE_ID = dt_amount_range.ID " +
					  	"AND dt_amount_range.VALUE1 <= '"+examination_questionnaire_score_obj.getNormal_range_value()+"' " +
					  	"AND dt_amount_range.VALUE2 >= '"+examination_questionnaire_score_obj.getNormal_range_value()+"' " +
					    "AND dt_amount_range.UNIT_ID = voc_unit.ID AND voc_unit.CODE='"+examination_questionnaire_score_obj.getUnit()+"' ";
				  };
				  
				  if(!examination_questionnaire_score_obj.getOther_term().isEmpty()) {  //TODO check value
					  	query += "AND " + Make_OR_of_CODES("examination_questionnaire_score.OTHER_TERM_ID", examination_questionnaire_score_obj.getOther_term());
				  }
				  
				  				  
				  if(!(examination_questionnaire_score_obj.getQuestionnaire_period_of_time_exact_year()).isEmpty()) {
					  query += Make_specific_date_query(mode, "exam_questionnaire_score.QUESTIONNAIRE_DATE_ID","dt_date",examination_questionnaire_score_obj.getQuestionnaire_period_of_time_exact_year(), 
							  examination_questionnaire_score_obj.getQuestionnaire_period_of_time_exact_month(), examination_questionnaire_score_obj.getQuestionnaire_period_of_time_exact_day());					  		
				  } else if(!(examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_end_year()).isEmpty()) {
					  query += Make_begin_end_date_query (mode,"exam_questionnaire_score.QUESTIONNAIRE_DATE_ID", "dt_date",examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_start_year(), 
							  examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_start_month(), examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_start_day(),
							  examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_end_year(), examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_end_month(),
							  examination_questionnaire_score_obj.getQuestionnaire_period_of_time_interval_end_day()); 			  
				  } else if(!(examination_questionnaire_score_obj.getQuestionnaire_period_of_time_until_year()).isEmpty()) {
					  query += Make_begin_end_date_query (mode,"exam_questionnaire_score.QUESTIONNAIRE_DATE_ID","dt_date", "1800", "1", "1", examination_questionnaire_score_obj.getQuestionnaire_period_of_time_until_year(), examination_questionnaire_score_obj.getQuestionnaire_period_of_time_until_month(),
							  examination_questionnaire_score_obj.getQuestionnaire_period_of_time_until_day()); 
				  }
				  
				  
			  } break;  //examination_essdai_domain
			  
			  case "examination_essdai_domain": { //Check if user provided the info of all the fields 
				  examination_essdai_domain  examination_essdai_domain_obj =  (examination_essdai_domain)current_Criterion;
				  query = "SELECT DISTINCT patient.UID " +
						  "FROM patient, exam_essdai_domain, voc_essdai_domain, dt_date, voc_activity_level " + 
						  "WHERE patient.ID = exam_essdai_domain.PATIENT_ID AND " + 
						  "exam_essdai_domain.DOMAIN_ID = voc_essdai_domain.ID " +
						  //"voc_essdai_domain.CODE = '"+examination_essdai_domain_obj.getDomain()+"' ";
				  		  "AND " + Make_OR_of_CODES("voc_essdai_domain.CODE", examination_essdai_domain_obj.getDomain());
				 
			  
				  if(!(examination_essdai_domain_obj.getActivity_level()).isEmpty()) {
					  	query += "AND exam_essdai_domain.ACTIVITY_LEVEL_ID = voc_activity_level.ID " +
					  	"AND voc_activity_level.CODE = '" + examination_essdai_domain_obj.getActivity_level() +"' "; //'BLOOD-100'
				  };
				  
				  if(!(examination_essdai_domain_obj.getQuestionnaire_period_of_time_exact_year()).isEmpty()) {
					  query += Make_specific_date_query(mode, "exam_essdai_domain.QUESTIONNAIRE_DATE_ID","dt_date",examination_essdai_domain_obj.getQuestionnaire_period_of_time_exact_year(), 
							  examination_essdai_domain_obj.getQuestionnaire_period_of_time_exact_month(), examination_essdai_domain_obj.getQuestionnaire_period_of_time_exact_day());					  		
				  } else if(!(examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_end_year()).isEmpty()) {
					  query += Make_begin_end_date_query (mode,"exam_essdai_domain.QUESTIONNAIRE_DATE_ID", "dt_date",examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_start_year(), 
							  examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_start_month(), examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_start_day(),
							  examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_end_year(), examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_end_month(),
							  examination_essdai_domain_obj.getQuestionnaire_period_of_time_interval_end_day()); 			  
				  } else if(!(examination_essdai_domain_obj.getQuestionnaire_period_of_time_until_year()).isEmpty()) {
					  query += Make_begin_end_date_query (mode,"exam_essdai_domain.QUESTIONNAIRE_DATE_ID","dt_date", "1800", "1", "1", examination_essdai_domain_obj.getQuestionnaire_period_of_time_until_year(), examination_essdai_domain_obj.getQuestionnaire_period_of_time_until_month(),
							  examination_essdai_domain_obj.getQuestionnaire_period_of_time_until_day()); 
				  }
				  System.out.println("Test id: "+query);
			  } break; //examination_caci_condition
			  
			  case "examination_caci_condition": { //Check if user provided the info of all the fields 
				  examination_caci_condition  examination_caci_condition_obj =  (examination_caci_condition)current_Criterion;
				  query = "SELECT DISTINCT patient.UID " +
						  "FROM exam_caci_condition, patient, voc_caci_condition, dt_date, voc_confirmation " + //interv_Surgery, dt_date, voc_direction, voc_confirmation
						  "WHERE patient.ID = exam_caci_condition.PATIENT_ID AND " + 
						  "exam_caci_condition.CACI_ID = voc_caci_condition.ID " +
						  //"voc_lab_test.CODE='"+crit_exam_lab_test_obj.getTEST_ID_voc_lab_test_CODE()+"' ";
				  		  "AND " + Make_OR_of_CODES("voc_caci_condition.CODE", examination_caci_condition_obj.getCaci());
				  
				  if(!examination_caci_condition_obj.getValue().isEmpty()) {  //TODO check value
					  	query += "AND " + Make_OR_of_CODES("exam_questionnaire_score.VALUE", examination_caci_condition_obj.getValue());
				  }
				  
				  if(!(examination_caci_condition_obj.getQuestionnaire_period_of_time_exact_year()).isEmpty()) {
					  query += Make_specific_date_query(mode, "exam_questionnaire_score.QUESTIONNAIRE_DATE_ID","dt_date",examination_caci_condition_obj.getQuestionnaire_period_of_time_exact_year(), 
							  examination_caci_condition_obj.getQuestionnaire_period_of_time_exact_month(), examination_caci_condition_obj.getQuestionnaire_period_of_time_exact_day());					  		
				  } else if(!(examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_end_year()).isEmpty()) {
					  query += Make_begin_end_date_query (mode,"exam_questionnaire_score.QUESTIONNAIRE_DATE_ID", "dt_date",examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_start_year(), 
							  examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_start_month(), examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_start_day(),
							  examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_end_year(), examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_end_month(),
							  examination_caci_condition_obj.getQuestionnaire_period_of_time_interval_end_day()); 			  
				  } else if(!(examination_caci_condition_obj.getQuestionnaire_period_of_time_until_year()).isEmpty()) {
					  query += Make_begin_end_date_query (mode,"exam_questionnaire_score.QUESTIONNAIRE_DATE_ID","dt_date", "1800", "1", "1", examination_caci_condition_obj.getQuestionnaire_period_of_time_until_year(), examination_caci_condition_obj.getQuestionnaire_period_of_time_until_month(),
							  examination_caci_condition_obj.getQuestionnaire_period_of_time_until_day()); 
				  }
				  
				  //System.out.println("Test id: "+examination_caci_condition_obj.getCaci());
			  } break; //other_healthcare_visit
			  
			  case "other_healthcare_visit": { //Check if user provided the info of all the fields 
				  other_healthcare_visit  other_healthcare_visit_obj =  (other_healthcare_visit)current_Criterion;
				  query = "SELECT DISTINCT patient.UID " +
						  "FROM other_healthcare_visit, patient, dt_date, voc_specialist " + //interv_Surgery, dt_date, voc_direction, voc_confirmation
						  "WHERE patient.ID = other_healthcare_visit.PATIENT_ID AND " + 
						  "other_healthcare_visit.SPECIALIST_ID=voc_specialist.ID " +
				  		  "AND " + Make_OR_of_CODES("voc_specialist.CODE", other_healthcare_visit_obj.getSpecialist());
				 // System.out.println("Test id: "+other_healthcare_visit_obj.getSpecialist());
				  
				  if(!(other_healthcare_visit_obj.getPeriod_of_time_exact_year()).isEmpty()) {
					  query += Make_specific_date_query(mode, "other_healthcare_visit.DATE_ID","dt_date",other_healthcare_visit_obj.getPeriod_of_time_exact_year(), 
							  other_healthcare_visit_obj.getPeriod_of_time_exact_month(), other_healthcare_visit_obj.getPeriod_of_time_exact_day());					  		
				  } else if(!(other_healthcare_visit_obj.getPeriod_of_time_interval_end_year()).isEmpty()) {
					  query += Make_begin_end_date_query (mode,"other_healthcare_visit.DATE_ID", "dt_date",other_healthcare_visit_obj.getPeriod_of_time_interval_start_year(), 
							  other_healthcare_visit_obj.getPeriod_of_time_interval_start_month(), other_healthcare_visit_obj.getPeriod_of_time_interval_start_day(),
							  other_healthcare_visit_obj.getPeriod_of_time_interval_end_year(), other_healthcare_visit_obj.getPeriod_of_time_interval_end_month(),
							  other_healthcare_visit_obj.getPeriod_of_time_interval_end_day()); 			  
				  } else if(!(other_healthcare_visit_obj.getPeriod_of_time_until_year()).isEmpty()) {
					  query += Make_begin_end_date_query (mode,"other_healthcare_visit.DATE_ID","dt_date", "1800", "1", "1", other_healthcare_visit_obj.getPeriod_of_time_until_year(), other_healthcare_visit_obj.getPeriod_of_time_until_month(),
							  other_healthcare_visit_obj.getPeriod_of_time_until_day()); 
				  }
				  
			  } break;			
			  
			  case "other_family_history": { //Check if user provided the info of all the fields 
				  other_family_history  other_family_history_obj =  (other_family_history)current_Criterion;
				  query = "SELECT DISTINCT patient.UID " +
						  "FROM other_family_history, patient, voc_medical_condition, voc_relative_degree, voc_confirmation " + //interv_Surgery, dt_date, voc_direction, voc_confirmation
						  "WHERE patient.ID = other_family_history.PATIENT_ID AND " + 
						  "other_family_history.MEDICAL_CONDITION_ID=voc_medical_condition.ID " +
						  //"voc_lab_test.CODE='"+crit_exam_lab_test_obj.getTEST_ID_voc_lab_test_CODE()+"' ";
				  		  "AND " + Make_OR_of_CODES("voc_medical_condition.CODE", other_family_history_obj.getMedical_condition());
				  
				  if(!(other_family_history_obj.getRelative_degree()).isEmpty()) {
					  	query += "AND other_family_history.RELATIVE_DEGREE_ID = voc_relative_degree.ID " +
					  	"AND " +Make_OR_of_CODES("voc_relative_degree.CODE", other_family_history_obj.getRelative_degree()); 
					  
				  };
				  
				  if(!other_family_history_obj.getStatement().isEmpty()) 
				  		query += "AND other_family_history.STMT_ID=voc_confirmation.ID " +
				  				 "AND voc_confirmation.CODE='"+other_family_history_obj.getStatement() + "'";
				  
				  System.out.println("Test id: "+other_family_history_obj.getMedical_condition());
				  
			  } break;
			  
			  case "other_clinical_trials": { //Check if user provided the info of all the fields 
				  other_clinical_trials  other_clinical_trials_obj =  (other_clinical_trials)current_Criterion;
				  query = "SELECT DISTINCT patient.UID " +
						  "FROM patient, other_clinical_trials, dt_period_of_time, dt_date, voc_confirmation " + //interv_Surgery, dt_date, voc_direction, voc_confirmation
						  "WHERE patient.ID = other_clinical_trials.PATIENT_ID "; 
						  
			if(!(other_clinical_trials_obj.getPeriod_of_time_exact_year()).isEmpty()) {
				  query += Make_specific_date_query(mode, "other_clinical_trials.PERIOD_ID","dt_date",other_clinical_trials_obj.getPeriod_of_time_exact_year(), 
						  other_clinical_trials_obj.getPeriod_of_time_exact_month(), other_clinical_trials_obj.getPeriod_of_time_exact_day());					  		
			} else if(!(other_clinical_trials_obj.getPeriod_of_time_interval_end_year()).isEmpty()) {
				  query += Make_begin_end_date_query (mode,"other_clinical_trials.PERIOD_ID", "dt_date",other_clinical_trials_obj.getPeriod_of_time_interval_start_year(), 
						  other_clinical_trials_obj.getPeriod_of_time_interval_start_month(), other_clinical_trials_obj.getPeriod_of_time_interval_start_day(),
						  other_clinical_trials_obj.getPeriod_of_time_interval_end_year(), other_clinical_trials_obj.getPeriod_of_time_interval_end_month(),
						  other_clinical_trials_obj.getPeriod_of_time_interval_end_day()); 			  
			} else if(!(other_clinical_trials_obj.getPeriod_of_time_until_year()).isEmpty()) {
				  query += Make_begin_end_date_query (mode,"other_clinical_trials.PERIOD_ID","dt_date", "1800", "1", "1", other_clinical_trials_obj.getPeriod_of_time_until_year(), other_clinical_trials_obj.getPeriod_of_time_until_month(),
						  other_clinical_trials_obj.getPeriod_of_time_until_day()); 
			}
			
			if(!other_clinical_trials_obj.getStatement().isEmpty()) 
		  		query += "AND other_clinical_trials.STMT_ID=voc_confirmation.ID " +
		  				 "AND voc_confirmation.CODE='"+other_clinical_trials_obj.getStatement() + "'";

			  } break;
			  
			  case "patient": { //Check if user provided the info of all the fields 
				  patient  patient_obj =  (patient)current_Criterion;
				  query = "SELECT DISTINCT patient.UID " +
						  "FROM patient,  dt_date as dt_date1, dt_date as dt_date2, dt_date as dt_date3, dt_date as dt_date4, voc_direction " + //exam_lab_test, voc_lab_test, dt_amount, voc_unit, voc_assessment, dt_amount_range,interv_Surgery, dt_date, voc_direction, voc_confirmation
						  "WHERE "; 
						  //"exam_lab_test.TEST_ID=voc_lab_test.ID " +
						  //"voc_lab_test.CODE='"+crit_exam_lab_test_obj.getTEST_ID_voc_lab_test_CODE()+"' ";
				  		  //"AND " + Make_OR_of_CODES("voc_lab_test.CODE", patient_obj.getBirth_period_of_time_exact_year());
				  System.out.println("Test id: "+patient_obj.getBirth_period_of_time_exact_year());
				  				  
				  if(!(patient_obj.getBirth_period_of_time_exact_year()).isEmpty()) {
					  query += Make_specific_date_query(mode, "patient.DATE_OF_BIRTH_ID","dt_date1",patient_obj.getBirth_period_of_time_exact_year(),
							  patient_obj.getBirth_period_of_time_exact_month(),patient_obj.getBirth_period_of_time_exact_day());					  		
				  } else if(!(patient_obj.getBirth_period_of_time_interval_start_year()).isEmpty()) {
					  query += Make_begin_end_date_query (mode,"patient.DATE_OF_BIRTH_ID", "dt_date1",patient_obj.getBirth_period_of_time_interval_start_year(), patient_obj.getBirth_period_of_time_interval_start_month(), patient_obj.getBirth_period_of_time_interval_start_day(), patient_obj.getBirth_period_of_time_interval_end_year(), patient_obj.getBirth_period_of_time_interval_end_month(),
							  patient_obj.getBirth_period_of_time_interval_end_day()); 			  
				  } else if(!(patient_obj.getBirth_period_of_time_until_year()).isEmpty()) {
					  query += Make_begin_end_date_query (mode,"patient.DATE_OF_BIRTH_ID","dt_date1", "1800", "1", "1", patient_obj.getBirth_period_of_time_until_year(), 
							  patient_obj.getBirth_period_of_time_until_month(), patient_obj.getBirth_period_of_time_until_day()); 
				  }
				  
				  if(!(patient_obj.getSymptoms_onset_period_of_time_exact_year()).isEmpty()) {
					  query += Make_specific_date_query(mode, "patient.PSS_SYMPTOMS_ONSET_DATE_ID","dt_date2",patient_obj.getSymptoms_onset_period_of_time_exact_year(),
							  patient_obj.getSymptoms_onset_period_of_time_exact_month(),patient_obj.getSymptoms_onset_period_of_time_exact_day());					  		
				  } else if(!(patient_obj.getSymptoms_onset_period_of_time_interval_start_year()).isEmpty()) {
					  query += Make_begin_end_date_query (mode,"patient.PSS_SYMPTOMS_ONSET_DATE_ID", "dt_date2",patient_obj.getSymptoms_onset_period_of_time_interval_start_year(), patient_obj.getSymptoms_onset_period_of_time_interval_start_month(), patient_obj.getSymptoms_onset_period_of_time_interval_start_day(), patient_obj.getSymptoms_onset_period_of_time_interval_end_year(), patient_obj.getSymptoms_onset_period_of_time_interval_end_month(),
							  patient_obj.getSymptoms_onset_period_of_time_interval_end_day()); 			  
				  } else if(!(patient_obj.getSymptoms_onset_period_of_time_until_year()).isEmpty()) {
					  query += Make_begin_end_date_query (mode,"patient.PSS_SYMPTOMS_ONSET_DATE_ID","dt_date2", "1800", "1", "1", patient_obj.getSymptoms_onset_period_of_time_until_year(), 
							  patient_obj.getSymptoms_onset_period_of_time_until_month(), patient_obj.getSymptoms_onset_period_of_time_until_day()); 
				  }
				  
				  if(!(patient_obj.getDiagnosis_period_of_time_exact_year()).isEmpty()) {
					  query += Make_specific_date_query(mode, "patient.PSS_DIAGNOSIS_DATE_ID","dt_date3",patient_obj.getDiagnosis_period_of_time_exact_year(),
							  patient_obj.getDiagnosis_period_of_time_exact_month(),patient_obj.getDiagnosis_period_of_time_exact_day());					  		
				  } else if(!(patient_obj.getDiagnosis_period_of_time_interval_start_year()).isEmpty()) {
					  query += Make_begin_end_date_query (mode,"patient.PSS_DIAGNOSIS_DATE_ID", "dt_date3",patient_obj.getDiagnosis_period_of_time_interval_start_year(), patient_obj.getDiagnosis_period_of_time_interval_start_month(), patient_obj.getDiagnosis_period_of_time_interval_start_day(), patient_obj.getDiagnosis_period_of_time_interval_end_year(), patient_obj.getDiagnosis_period_of_time_interval_end_month(),
							  patient_obj.getDiagnosis_period_of_time_interval_end_day()); 			  
				  } else if(!(patient_obj.getDiagnosis_period_of_time_until_year()).isEmpty()) {
					  query += Make_begin_end_date_query (mode,"patient.PSS_DIAGNOSIS_DATE_ID","dt_date3", "1800", "1", "1", patient_obj.getDiagnosis_period_of_time_until_year(), 
							  patient_obj.getDiagnosis_period_of_time_until_month(), patient_obj.getDiagnosis_period_of_time_until_day()); 
				  }
				  
				  if(!(patient_obj.getCohort_inclusion_period_of_time_exact_year()).isEmpty()) {
					  query += Make_specific_date_query(mode, "patient.COHORT_INCLUSION_DATE_ID","dt_date4",patient_obj.getCohort_inclusion_period_of_time_exact_year(),
							  patient_obj.getCohort_inclusion_period_of_time_exact_month(),patient_obj.getCohort_inclusion_period_of_time_exact_day());					  		
				  } else if(!(patient_obj.getCohort_inclusion_period_of_time_interval_start_year()).isEmpty()) {
					  query += Make_begin_end_date_query (mode,"patient.COHORT_INCLUSION_DATE_ID", "dt_date4",patient_obj.getCohort_inclusion_period_of_time_interval_start_year(), patient_obj.getCohort_inclusion_period_of_time_interval_start_month(), patient_obj.getCohort_inclusion_period_of_time_interval_start_day(), patient_obj.getCohort_inclusion_period_of_time_interval_end_year(), patient_obj.getCohort_inclusion_period_of_time_interval_end_month(),
							  patient_obj.getCohort_inclusion_period_of_time_interval_end_day()); 			  
				  } else if(!(patient_obj.getCohort_inclusion_period_of_time_until_year()).isEmpty()) {
					  query += Make_begin_end_date_query (mode,"patient.COHORT_INCLUSION_DATE_ID","dt_date4", "1800", "1", "1", patient_obj.getCohort_inclusion_period_of_time_until_year(), 
							  patient_obj.getCohort_inclusion_period_of_time_until_month(), patient_obj.getCohort_inclusion_period_of_time_until_day()); 
				  }
				  
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
			
			if(i==0) results_of_all_Criterions = results_of_one_Criterion;
			else results_of_all_Criterions = intersection_of_UIDs(results_of_one_Criterion, results_of_all_Criterions);
    	}
    	results.Input_UIDs(mode,results_of_all_Criterions);
    	}
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

    
    public void writeXMLResponse(){
    	try{
    		
    		JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
    		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
    		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    		PatientsSelectionResponse patientsSelectionResponse = new PatientsSelectionResponse();
    		//jaxbMarshaller.marshal(patientsSelectionResponse, responseXML);
    		patientsSelectionResponse.setUserID(patientsSelectionRequest.getUserID());
    		patientsSelectionResponse.setSessionID(patientsSelectionRequest.getSessionID());
    		patientsSelectionResponse.setResponseDate(patientsSelectionRequest.getRequestDate());
    		for(String cohortID: patientsSelectionRequest.getCohortID()){
    			CohortResponse cohortResponse = new CohortResponse();
    			cohortResponse.setCohortID(cohortID);
    			cohortResponse.setQueryDate(patientsSelectionRequest.getRequestDate());
    			cohortResponse.setEligiblePatientsNumber(14);
    			EligibilityCriteriaUsed inclAndExclCriteriaUsed = new EligibilityCriteriaUsed();
    			for(org.ntua.criteria.Criterion inclCriterion: patientsSelectionRequest.getEligibilityCriteria().getInclusionCriteria().getCriterion()){
    				CriterionUsage inclCriterionUsage = new CriterionUsage();
    				inclCriterionUsage.setCriterionID(inclCriterion.getUID());
    				inclCriterionUsage.setCriterionUsageStatus(CriterionUsageStatus.USED);
    				inclCriterionUsage.setNotesHTML("+++ Additional terms (if any) +++");
    				inclAndExclCriteriaUsed.getCriterionUsage().add(inclCriterionUsage);
    			}
    			for(org.ntua.criteria.Criterion exclCriterion: patientsSelectionRequest.getEligibilityCriteria().getExclusionCriteria().getCriterion()){
    				CriterionUsage exclCriterionUsage = new CriterionUsage();
    				exclCriterionUsage.setCriterionID(exclCriterion.getUID());
    				exclCriterionUsage.setCriterionUsageStatus(CriterionUsageStatus.NOT_USED);
    				exclCriterionUsage.setNotesHTML("+++ The Reason for not using this criterion +++");
    				inclAndExclCriteriaUsed.getCriterionUsage().add(exclCriterionUsage);
    			}
    			cohortResponse.setEligibilityCriteriaUsed(inclAndExclCriteriaUsed);
    			patientsSelectionResponse.getCohortResponse().add(cohortResponse);
    		}
    		ObjectFactory objectFactory = new ObjectFactory();
    		JAXBElement<PatientsSelectionResponse> je =  objectFactory.createPatientsSelectionResponse(patientsSelectionResponse);
    		jaxbMarshaller.marshal(je, responseXML);
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    /*public static void findCriterion(Criterion myCrit){
    	switch(myCrit.getCriterion()) 
		{ 
	    case "demographics_pregnancy": 
	        {//System.out.println("demographics_pregnancy");
	        demographics_pregnancy aCrit = (demographics_pregnancy)myCrit;
	    	System.out.println("Pregnancy outcome code: "+aCrit.getOutcome_coded_value());}
	    break; 
	    case "lifestyle_smoking": 
	    	{//System.out.println("lifestyle_smoking"); 
	    	lifestyle_smoking aCrit = (lifestyle_smoking)myCrit;
	    	System.out.println("Smoking amount unit: "+aCrit.getDt_amount_voc_unit_CODE());}
	    break; 
	    case "condition_symptom": 
	    	{System.out.println("condition_symptom"); 
	    	myCrit = (condition_symptom)myCrit;}
	    break; 
	    case "condition_diagnosis": 
	    	{System.out.println("condition_diagnosis");  
	    	myCrit = (condition_diagnosis)myCrit;}
	    break; 
	    case "intervention_medication": 
	    	{System.out.println("intervention_medication"); 
	    	myCrit = (intervention_medication)myCrit;}
	    break; 
	    case "intervention_chemotherapy": 
	        {System.out.println("intervention_chemotherapy"); //condition_diagnosis
	        myCrit = (intervention_chemotherapy)myCrit;}
	    break; 
	    case "intervention_surgery": 
	        {System.out.println("intervention_surgery"); //condition_diagnosis
	        myCrit = (intervention_surgery)myCrit;}
	    break;  
	    case "examination_lab_test": 
        	{System.out.println("examination_lab_test"); //condition_diagnosis
        	examination_lab_test myfirst = (examination_lab_test)myCrit;
	    	System.out.println(myfirst.getTest_id());}
        break; //
	    case "examination_biopsy": 
    		{System.out.println("examination_biopsy"); //condition_diagnosis
    		myCrit = (examination_biopsy)myCrit;}
    	break; 
	    case "examination_medical_imaging_test": 
			{System.out.println("examination_medical_imaging_test"); 
			myCrit = (examination_medical_imaging_test)myCrit;}
		break; 
	    case "examination_questionnaire_score": 
			{System.out.println("examination_questionnaire_score"); 
			myCrit = (examination_questionnaire_score)myCrit;}
		break; 
	    case "examination_essdai_domain": 
			{System.out.println("examination_essdai_domain"); 
			myCrit = (examination_essdai_domain)myCrit;}
		break; 
	    case "examination_caci_condition": 
			{System.out.println("examination_caci_condition"); 
			myCrit = (examination_caci_condition)myCrit;}
		break; //other_healthcare_visit
	    case "other_healthcare_visit": 
			{System.out.println("other_healthcare_visit"); 
			myCrit = (other_healthcare_visit)myCrit;}
		break; //other_healthcare_visit
	    case "other_family_history": 
			{System.out.println("other_family_history"); 
			myCrit = (other_family_history)myCrit;}
		break; 
	    case "other_clinical_trials": 
			{System.out.println("other_clinical_trials"); 
			myCrit = (other_clinical_trials)myCrit;}
		break; 
	    case "patient": 
			{System.out.println("patient"); 
			myCrit = (patient)myCrit;}
		break; //patient
	    default: 
	        System.out.println("no match"); 
		}
    }*/
    
    public String[] getCohortsAccessByRequestID(String requestID){
    	String[] cohortAccess = new String[cohort_names.size()];
    	for(int i=0; i<cohort_names.size(); i++){
    		cohortAccess[i]="Accepted";
    	}
    	return cohortAccess;
    }
    
    public boolean canUseCriterion(Criterion crit){
    	String query="";
    	boolean myresults = false;
    	switch(crit.getCriterion()) {
    		case "demographics_gender": {
    			query = "SELECT * FROM demo_sex_data";
    		}
    		break;
    		case "demographics_ethnicity": {
    			query = "SELECT * FROM demo_ethnicity_data";
    		}
    		break;
    		case "demographics_education": {
    			query = "SELECT * FROM demo_education_level_data";
    		}
    		break;
    		case "demographics_weight": {
    			query = "SELECT * FROM demo_weight_data";
    		}
    		break;
    		case "demographics_occupation": {
    			query = "SELECT * FROM demo_occupation_data";
    		}
    		break;
    		case "demographics_pregnancy": {
    			query = "SELECT * FROM demo_pregnancy_data";
    		}
    		break;
    		case "lifestyle_smoking": {
    			query = "SELECT * FROM lifestyle_smoking";
    		}
    		break;
    		case "condition_symptom": {
    			condition_symptom crit_cond_symptom_obj = (condition_symptom)crit;
    			query = "SELECT * FROM cond_symptom, voc_symptom_sign WHERE cond_symptom.CONDITION_ID = voc_symptom_sign.ID AND " + Make_OR_of_CODES("voc_symptom_sign.CODE", crit_cond_symptom_obj.getVoc_symptom_sign_CODE());
    		}
    		break;
    		case "condition_diagnosis": {
    			condition_diagnosis crit_cond_diagnosis_obj = (condition_diagnosis)crit;
    			query = "SELECT * FROM cond_diagnosis, voc_medical_condition WHERE cond_diagnosis.CONDITION_ID = voc_medical_condition.ID AND " + Make_OR_of_CODES("voc_medical_condition.CODE", crit_cond_diagnosis_obj.getCondition());
    		}
    		break;
    		case "intervention_medication": {
    			intervention_medication  crit_interv_medication_obj =  (intervention_medication )crit;
				query = "SELECT * FROM interv_medication, voc_pharm_drug WHERE interv_medication.MEDICATION_ID = voc_pharm_drug.ID AND " + Make_OR_of_CODES("voc_pharm_drug.CODE", crit_interv_medication_obj.getVoc_pharm_drug_CODE());
    		}
    		break;
    		case "intervention_chemotherapy": {
				  //intervention_chemotherapy  crit_interv_chemotherapy_obj =  (intervention_chemotherapy)crit;
				query = "SELECT * FROM interv_chemotherapy"; //, voc_confirmation WHERE interv_chemotherapy.DUE_TO_PSS_ID = voc_confirmation.ID AND " + Make_OR_of_CODES("voc_confirmation.CODE", crit_interv_chemotherapy_obj.getReason());
    		}
    		break;
    		case "intervention_surgery": { 
    			query = "SELECT * FROM intervention_surgery";
    		}
    		break;
    		case "examination_lab_test": {
    			examination_lab_test  examination_lab_test_obj =  (examination_lab_test)crit;
				query = "SELECT * FROM exam_lab_test, voc_lab_test WHERE exam_lab_test.TEST_ID=voc_lab_test.ID AND " + Make_OR_of_CODES("voc_lab_test.CODE", examination_lab_test_obj.getTest_id());
    		}
    		break;
    		case "examination_biopsy": { //Check if user provided the info of all the fields 
				examination_biopsy  examination_biopsy_obj =  (examination_biopsy)crit;
				query = "SELECT * FROM exam_biopsy, voc_biopsy WHERE exam_biopsy.BIOPSY_ID=voc_biopsy.ID AND voc_biopsy.CODE='"+examination_biopsy_obj.getBiopsy_type()+"' "; // ='SAL-BIO-21' Make_OR_of_CODES("voc_lab_test.CODE", examination_biopsy_obj.getBiopsy_type());				  		 
    		}
    		break;
    		case "examination_medical_imaging_test": { //Check if user provided the info of all the fields 
				examination_medical_imaging_test  examination_medical_imaging_test_obj =  (examination_medical_imaging_test)crit;
				query = "SELECT * FROM exam_medical_imaging_test, voc_medical_imaging_test WHERE exam_medical_imaging_test.TEST_ID=voc_medical_imaging_test.ID AND " + Make_OR_of_CODES("voc_medical_imaging_test.CODE", examination_medical_imaging_test_obj.getTest_id()) +" ";
    		}
    		break;
    		case "examination_questionnaire_score": { //Check if user provided the info of all the fields 
				examination_questionnaire_score  examination_questionnaire_score_obj =  (examination_questionnaire_score)crit;
				query = "SELECT * FROM exam_questionnaire_score, voc_questionnaire WHERE exam_questionnaire_score.SCORE_ID=voc_questionnaire.ID AND " + Make_OR_of_CODES("voc_questionnaire.CODE", examination_questionnaire_score_obj.getScore());
    		}
    		break;
    		case "examination_essdai_domain": {
    			examination_essdai_domain  examination_essdai_domain_obj =  (examination_essdai_domain)crit;
				query = "SELECT * FROM exam_essdai_domain, voc_essdai_domain WHERE exam_essdai_domain.DOMAIN_ID = voc_essdai_domain.ID AND " + Make_OR_of_CODES("voc_essdai_domain.CODE", examination_essdai_domain_obj.getDomain());
    		}
    		break;
    		case "examination_caci_condition": { //Check if user provided the info of all the fields 
				examination_caci_condition  examination_caci_condition_obj =  (examination_caci_condition)crit;
				query = "SELECT * FROM exam_caci_condition, voc_caci_condition WHERE exam_caci_condition.CACI_ID = voc_caci_condition.ID AND " + Make_OR_of_CODES("voc_caci_condition.CODE", examination_caci_condition_obj.getCaci());
    		}
    		break;
    		case "other_healthcare_visit": { //Check if user provided the info of all the fields 
				other_healthcare_visit  other_healthcare_visit_obj =  (other_healthcare_visit)crit;
				query = "SELECT * FROM other_healthcare_visit, voc_specialist WHERE other_healthcare_visit.SPECIALIST_ID=voc_specialist.ID AND " + Make_OR_of_CODES("voc_specialist.CODE", other_healthcare_visit_obj.getSpecialist());
    		}
    		break;
    		case "other_family_history": { //Check if user provided the info of all the fields 
				other_family_history  other_family_history_obj =  (other_family_history)crit;
				query = "SELECT * FROM other_family_history, voc_medical_condition WHERE other_family_history.MEDICAL_CONDITION_ID=voc_medical_condition.ID AND " + Make_OR_of_CODES("voc_medical_condition.CODE", other_family_history_obj.getMedical_condition());
    		}
    		break;
    		case "other_clinical_trials": { //Check if user provided the info of all the fields 
				query = "SELECT * FROM other_clinical_trials"; 
    		}
    		break;
    		case "patient": { //Check if user provided the info of all the fields 
				query = "SELECT * FROM patient";
    		}
    		break;
    		default: System.out.println("Undefined criterion-name in the input JSON file.");
    	}
    	try { 
    		if(!query.equals("")) myresults = DBServiceCRUD.testQuery(query);		
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
		JSONObject all = new JSONObject();
		requestID = request.getParameter("requestID");
		if(requestID!=null){
			String crit_incl_excl_in = readXMLbyRequestID(requestID);
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
	    	ConfigureFile obj = new ConfigureFile("jdbc:mysql://localhost:3306/harmonicssdb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","");
	    	//ConfigureFile obj = new ConfigureFile("jdbc:mysql://147.102.19.66:3306/harmonicssdb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","emps","emps");

			if(!DBServiceCRUD.makeJDBCConnection(obj))  System.out.println("Connection with the Database failed. Check the Credentials and the DB URL.");
	    	else System.out.println("everything's gooooooood");
	    	
	    	criterionDBmatching(list_of_inclusive_criterions,list_of_exclusive_criterions);
			writeXMLResponse();			
			System.out.println("End");
			
			
		}
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = response.getWriter();
		pw.print(all.toString());
		pw.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/*public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//Logger LOGGER = Initialize_logger("LogFile.log");
		System.out.println("Begin");
		
		final String requestID = "Req01";

		PatientSelectionImpl testObj = new PatientSelectionImpl();
		
		String crit_incl_excl_in = testObj.readXMLbyRequestID(requestID);
		System.out.println(crit_incl_excl_in);
		String[] crit_incl_excl=crit_incl_excl_in.split("XXX");
		String criteria = Intermediate_Layer.preProcess_JSON(crit_incl_excl[0]);
		System.out.println("After Criteria preprocessed:\n"+criteria);
		ArrayList<Criterion> list_of_inclusive_criterions=null;
		try {
			list_of_inclusive_criterions = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
			findCriterion((Criterion)list_of_inclusive_criterions.get(0));
			System.out.println(list_of_inclusive_criterions);
			
		} catch (JsonParseException e1) {
			/*LOGGER.log(Level.SEVERE,"JsonParseException Bad JSON format: "+criteria,true);
			flush_handler();*/
			//e1.printStackTrace();
			//return "JsonParseException Bad JSON format.";
		//} catch (JsonMappingException e1) {
			/*LOGGER.log(Level.SEVERE,"JsonMappingException Bad JSON format: "+criteria,true);
			flush_handler();*/
			//e1.printStackTrace();
			//return "JsonParseException Bad JSON format.";
		//} catch (IOException e1) {
			/*LOGGER.log(Level.SEVERE,"IOException Bad JSON format: "+criteria,true);
			flush_handler();*/
			//e1.printStackTrace();
			//return "JsonParseException Bad JSON format.";
		/*}
		criteria = Intermediate_Layer.preProcess_JSON(crit_incl_excl[1]);
		System.out.println("After Criteria preprocessed:\n"+criteria);
		ArrayList<Criterion> list_of_exclusive_criterions=null;
		try {
			list_of_exclusive_criterions = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
			findCriterion((Criterion)list_of_exclusive_criterions.get(0));
			System.out.println(list_of_exclusive_criterions);
				
		} catch (JsonParseException e1) {
			/*LOGGER.log(Level.SEVERE,"JsonParseException Bad JSON format: "+criteria,true);
			flush_handler();*/
			//e1.printStackTrace();
			//return "JsonParseException Bad JSON format.";
		//} catch (JsonMappingException e1) {
			/*LOGGER.log(Level.SEVERE,"JsonMappingException Bad JSON format: "+criteria,true);
			flush_handler();*/
			//e1.printStackTrace();
			//return "JsonParseException Bad JSON format.";
		//} catch (IOException e1) {
			/*LOGGER.log(Level.SEVERE,"IOException Bad JSON format: "+criteria,true);
			flush_handler();*/
			//e1.printStackTrace();
			//return "JsonParseException Bad JSON format.";
		/*}
		testObj.writeXMLResponse();
		
		System.out.println("End");
		
	}*/

	
}
