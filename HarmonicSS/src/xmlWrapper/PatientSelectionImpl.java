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
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;



















import org.json.JSONException;
import org.json.JSONObject;
import org.ntua.criteria.*;

import jsonProcess.*;
import criterionManager.*;
import criterionManager.Criterion;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;



import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
	  			result_incl+=exclCriterion.getFormalExpression().get(0).getBooleanExpression().trim();
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


  	  		System.out.println("The ids of the included criterions are: "+crit_names_incl);
  	  		System.out.println("The ids of the excluded criterions are: "+crit_names_excl);
  	      
  	  
  		
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
			readXMLbyRequestID(requestID);
			writeXMLResponse();
			/*System.out.println("The ids of the included criterions are: "+crit_names_incl);
			System.out.println("The ids of the excluded criterions are: "+crit_names_excl);
			String[] cohortAccess = getCohortsAccessByRequestID(requestID);
			for(int i=0; i<cohortAccess.length; i++){
				if(cohortAccess[i].equals("Accepted"))
					System.out.println("Cohort with accepted access: "+cohort_names.get(i));
			}
			for(int i=0; i<crit_names_incl.size(); i++){
				if(canUseCriterion(1)){
				
					System.out.println("Inclusive Criterion "+crit_names_incl.get(i)+" can be used");
					System.out.println("Patients satisfying this criterion: "+findPatientsIds(crit_names_incl.get(i)));
				}
			}
			for(int i=0; i<crit_names_excl.size(); i++){
				if(canUseCriterion(1)){
					System.out.println("Exclusive Criterion "+crit_names_incl.get(i)+" can be used");
					System.out.println("Patients satisfying this criterion: "+findPatientsIds(crit_names_excl.get(i)));
				}
		
			}
		
			try {
				all.put("requestID", requestID);
				all.put("incl_ids", crit_names_incl.size());
				all.put("excl_ids", crit_names_excl.size());
				all.put("cohorts_accepted", cohort_names.size());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
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

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//Logger LOGGER = Initialize_logger("LogFile.log");
		System.out.println("Begin");
		
		final String requestID = "Req01";

		PatientSelectionImpl testObj = new PatientSelectionImpl();
		
		String crit_incl_excl_in = testObj.readXMLbyRequestID(requestID);
		String[] crit_incl_excl=crit_incl_excl_in.split("XXX");
		String criteria = Intermediate_Layer.preProcess_JSON(crit_incl_excl[0]);
		System.out.println("After Criteria preprocessed:\n"+criteria);
		ArrayList<Criterion> list_of_criterions=null;
		try {
			list_of_criterions = Criterions.From_JSON_String_to_Criterion_ArrayList(criteria).getList_of_criterions();
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
		testObj.writeXMLResponse();
		
		System.out.println("End");
		
	}

	
}
