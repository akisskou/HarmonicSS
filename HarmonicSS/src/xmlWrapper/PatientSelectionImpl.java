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

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
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
    
    public static void findCriterion(Criterion myCrit){
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
		break; //patient*/
	    default: 
	        System.out.println("no match"); 
		}
    }
    
    public String[] getCohortsAccessByRequestID(String requestID){
    	String[] cohortAccess = new String[cohort_names.size()];
    	for(int i=0; i<cohort_names.size(); i++){
    		cohortAccess[i]="Accepted";
    	}
    	return cohortAccess;
    }
    
    public boolean canUseCriterion(int flag){
    	if (flag==1) return true;
    	else return false;
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
				findCriterion((Criterion)list_of_inclusive_criterions.get(0));
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
				findCriterion((Criterion)list_of_exclusive_criterions.get(0));
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
	    	if(!DBServiceCRUD.makeJDBCConnection(obj))  System.out.println("Connection with the Database failed. Check the Credentials and the DB URL.");
	    	else System.out.println("everything's gooooooood");
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
