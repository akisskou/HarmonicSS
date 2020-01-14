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
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;



import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
	public void readXMLbyRequestID(String requestID){
    	criterion_incl.clear();
  	  	criterion_excl.clear();
  	  	crit_names_incl.clear();
  	  	crit_names_excl.clear();
  	  	cohort_names.clear();
  	  	/*String result_incl = "";
  	  	String result_excl = "";
  	  	String result="";*/
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
  	  		
  	  		
  	  		//File fXmlFile = new File("/WEB-INF/Req01.xml");
  	  		/*DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
  	  		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
  	  		Document doc = dBuilder.parse(fXmlFile);
  				
  	  		//optional, but recommended
  	  		//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
  		
  	  		doc.getDocumentElement().normalize();
  	  		
  	  		NodeList ListIncl_ch =  doc.getElementsByTagName("CohortID"); //ListIncl = doc.getElementsByTagName("Criterion");
  	  		//System.out.println("The length of cohorts is: "+ ListIncl_ch.getLength());
  	  		System.out.println("The cohorts that we are looking for :" );
  	  		for (int temp = 0; temp < ListIncl_ch.getLength(); temp++) {
  	  			Node nNode = ListIncl_ch.item(temp);
  	  			System.out.println("Cohort :" + nNode.getTextContent());
  	  			cohort_names.add(nNode.getTextContent());
  	  		}
  	  		
  	  		NodeList ListIncl2 = doc.getElementsByTagName("InclusionCriteria");
  	  		Node nNode2 = ListIncl2.item(0);
  	  		Element eElement2 = (Element) nNode2;
  	  		//eElement2.getElementsByTagName("Criterion");
  		
  		
  	  		NodeList ListIncl =  eElement2.getElementsByTagName("Criterion"); //ListIncl = doc.getElementsByTagName("Criterion");
  	  		//System.out.println("The length of ListIncl is: "+ ListIncl.getLength());
  	  		for (int temp = 0; temp < ListIncl.getLength(); temp++) {
  	  			Node nNode = ListIncl.item(temp);
  	  			crit_names_incl.add(nNode.getAttributes().item(1).toString().replace("UID=", "").replace("\"", ""));
  	        
  	  			//crit_names_incl += nNode.getAttributes().item(1);
  	  			System.out.println("Criterion Name:" +nNode.getAttributes().item(1));
  	  			//System.out.println("\nCurrent Element :" + nNode.getNodeName());
  	  			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
  	  				Element eElement = (Element) nNode;*/
  	  				/*            System.out.println("InclusionCriteria : " 
  	               	+ eElement.getAttribute("Criterion"));*/
  	  				/*criterion_incl.add(eElement.getElementsByTagName("BooleanExpression").item(0).getTextContent().toString());
  	  				//result_incl+=eElement.getElementsByTagName("BooleanExpression").item(0).getTextContent();
  	  				System.out.println("Inclusive Criterion-"+temp+": " + eElement.getElementsByTagName("BooleanExpression").item(0).getTextContent());
  	  			}       
  	  		}

  		
  	  		ListIncl2 = doc.getElementsByTagName("ExclusionCriteria");
  	  		nNode2 = ListIncl2.item(0);
  	  		eElement2 = (Element) nNode2;
  	  		//eElement2.getElementsByTagName("Criterion");
  		
  		
  	  		ListIncl =  eElement2.getElementsByTagName("Criterion"); //ListIncl = doc.getElementsByTagName("Criterion");
  	  		//System.out.println("The length of ListIncl is: "+ ListIncl.getLength());
  	  		for (int temp = 0; temp < ListIncl.getLength(); temp++) {
  	  			Node nNode = ListIncl.item(temp);
  	  			crit_names_excl.add(nNode.getAttributes().item(1).toString().replace("UID=", "").replace("\"", ""));
  	  			//crit_names_excl+=nNode.getAttributes().item(1)+","; //new
  	        
  	  			System.out.println("Criterion Name:" +nNode.getAttributes().item(1));
  	  			//System.out.println("\nCurrent Element :" + nNode.getNodeName());
  	  			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
  	  				Element eElement = (Element) nNode;*/
  	  				/*            System.out.println("InclusionCriteria : " 
  	               + eElement.getAttribute("Criterion"));*/
  	  				/*criterion_excl.add(eElement.getElementsByTagName("BooleanExpression").item(0).getTextContent().toString());
  	  				//result_excl+=eElement.getElementsByTagName("BooleanExpression").item(0).getTextContent();
  	  				System.out.println("Exclusive Criterion-"+temp+": " + eElement.getElementsByTagName("BooleanExpression").item(0).getTextContent());
  	  			}       
  	  		}*/
  		
  	   } catch (Exception e) {
  	      e.printStackTrace();
  	   }    
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
    			for(Criterion inclCriterion: patientsSelectionRequest.getEligibilityCriteria().getInclusionCriteria().getCriterion()){
    				CriterionUsage inclCriterionUsage = new CriterionUsage();
    				inclCriterionUsage.setCriterionID(inclCriterion.getUID());
    				inclCriterionUsage.setCriterionUsageStatus(CriterionUsageStatus.USED);
    				inclCriterionUsage.setNotesHTML("+++ Additional terms (if any) +++");
    				inclAndExclCriteriaUsed.getCriterionUsage().add(inclCriterionUsage);
    			}
    			for(Criterion exclCriterion: patientsSelectionRequest.getEligibilityCriteria().getExclusionCriteria().getCriterion()){
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

		System.out.println("Begin");
		
		final String requestID = "Req01";

		PatientSelectionImpl testObj = new PatientSelectionImpl();
		
		testObj.readXMLbyRequestID(requestID);
		testObj.writeXMLResponse();
		
		System.out.println("End");
		
	}

	
}
